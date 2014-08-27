/**
 * The MIT License
 * Copyright (c) 2011 Kuali Mobility Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


package org.kuali.mobility.push.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Implementation of the CXF Device Service
 *
 * @deprecated This class should be replaced and implemented by {@link org.kuali.mobility.push.service.rest.PushServiceRest}
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 3.0
 */
@Service
@Deprecated
public class CXFPushService {

	/** A reference to a logger for this service */
	private static final Logger LOG = LoggerFactory.getLogger(CXFPushService.class);
	
	private static final String RECIPIENTS = "recipients";
	private static final String KME_ALL_USERS = "all";
	private static final String KME_USERNAMELESS = "usernameless";
	
	/**
	 * A reference to the <code>PushService</code>.
	 */
	@Autowired
	private PushService pushService;
	
	/** A reference to the <code>DeviceService</code> object used by this ServiceImpl */
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	@Qualifier("kmeProperties")
	private Properties kmeProperties;
	
	@Autowired
	@Qualifier("senderKeysProperties")
	private Properties senderKeysProperties;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	
    @Autowired
    private PushMessageService pushMessageService;
	
	/**
	 * A reference to the <code>SenderService</code>.
	 */
	@Autowired
	private SenderService senderService;
	
	@GET
	@Path("/ping/get")
	public String pingGet(){
		return "{\"status\":\"OK\"}";
	}

	
	
	
	@POST
	@Path("/ping/post")
	public String pingPost(){
		return "{\"status\":\"OK\"}";
	}

	

	/**
	 * A CXF Service method for processing an HTTP. Post submitted push notification. 
	 * 
	 * @param data String - A JSON formatted string containing details for the push to send as well as the recipient users or devices ids. 
	 * @return
	 */
    @POST
    @Path("/submit")
	public Response angularService(@RequestBody String data) {
		LOG.info("Angular Service : " + data);
    	return service(data);
    }

	/**
	 * A CXF Service method for processing an HTTP.Post submitted push notification. 
	 * 
	 * @param data String - A JSON formatted string containing details for the push to send as well as the recipient users or devices ids. 
	 * @return
	 */
    @POST
    @Path("/service")
	public Response formService(@FormParam(value = "data") String data) {
		LOG.info("FormService: " + data);
    	return service(data);
    }
    	
	private Response service(String data) {
		LOG.info("Service JSON: " + data);
		JSONObject queryParams;
		List<Device> devices = new ArrayList<Device>();
		String title;
		String message;
		String url;
		String recipient;
		String sender = "KME_PUSH";
		String senderKey;
		JSONArray recipients;
		try {			
			queryParams = (JSONObject) JSONSerializer.toJSON(data);
			title = queryParams.getString("title");
			message = queryParams.getString("message");
			url = queryParams.getString("url");
			senderKey = queryParams.getString("senderKey");
			sender = this.getSenderService().findSenderBySenderKey(senderKey).getShortName();
			
			String source = getKmeProperties().getProperty("push.sender.key.source", "database");
			if("database".equalsIgnoreCase(source)){
				LOG.info("Getting SenderKeys from database.");
				Sender senderObj = senderService.findSenderBySenderKey(senderKey);
				if (senderObj == null) {
					LOG.info("---- " + senderKey + " is Not in LHM. May not Send Push Notifications");
					return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
				} else {
					sender = senderObj.getShortName();
				}
			}else if("properties".equalsIgnoreCase(source)){
				LOG.info("Getting SenderKeys from properties file.");
				String shortName = getSenderKeysProperties().getProperty(senderKey);
				if(shortName == null){
					LOG.info("---- " + senderKey + " is Not in LHM. May not Send Push Notifications");
					return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
				}else{
					sender = shortName;
				}
			}
			
			
			if (queryParams.has(RECIPIENTS)) {
				if ( "net.sf.json.JSONArray".equals(queryParams.get(RECIPIENTS).getClass().getName()) ) {

					recipients = queryParams.getJSONArray(RECIPIENTS);
					LOG.info("recipients: " + recipients.toString());
					Iterator i = recipients.iterator();
					while (i.hasNext()) {
						String username = i.next().toString();
						LOG.info("username: " + username);
						devices.addAll(deviceService
								.findDevicesByUsername(username));
					}
				} else {
					recipient = queryParams.getString("recipients");
	                if (KME_ALL_USERS.equals(recipient)) {
	                    devices = deviceService.findAllDevices();
	                } else if (KME_USERNAMELESS.equals(recipient)) {
	                    devices = deviceService.findDevicesWithoutUsername();
	                }					
					LOG.info(recipient);
				}
			}
			if (queryParams.has("devices") && "net.sf.json.JSONArray".equals(queryParams.get("devices").getClass().getName()) ) {
				JSONArray jDevices = queryParams.getJSONArray("devices");
				LOG.info("devices: " + jDevices.toString());
				Iterator i = jDevices.iterator();
				while (i.hasNext()) {
					String sDevice = i.next().toString();
					LOG.info("device: " + sDevice);
					devices.add(deviceService.findDeviceByDeviceId(sDevice));
				}
			}

		} catch (JSONException je) {
			LOG.error("JSONException in :" + data + " : " + je.getMessage());
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		} catch (ClassCastException cce) {
			LOG.error(cce.getLocalizedMessage(), cce);
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		}

		Push push = new Push();
		push.setEmergency(false);
		push.setMessage(message);
		push.setTitle(title);
		push.setUrl(url);
		push.setSender(sender);
		push.setPostedTimestamp(new Timestamp(System.currentTimeMillis()));
		pushService.savePush(push, devices);
        pushService.sendPush(push, devices);
		LOG.info(push.toString());

		return Response.status(Response.Status.OK.getStatusCode()).build();
	}
	
	
	@GET
	@Path("/history")
	public String getPushHistory(){
		String results = "[";
		List<Push> pastPushes = this.getPushService().findAllPush();
		Iterator<Push> it = pastPushes.iterator();
		while(it.hasNext()){
			Push push = it.next();
			results += push.toJson() + ",";
		}
		results = results.substring(0, results.length() - 1) + "]";		
		return results;
	}
	
	@GET
	@Path("/historyByPage/{pageId}")
	public String getPushHistoryByPage(@PathParam("pageId") Integer pageNum){
		String results = "[";
		List<Push> pastPushes = this.getPushService().findAllPushByPageNumber(pageNum);
		Iterator<Push> it = pastPushes.iterator();
		while(it.hasNext()){
			Push push = it.next();
			results += push.toJson() + ",";
		}
		results = results.substring(0, results.length() - 1) + "]";		
		return results;
	}
	
	@GET
	@Path("/historyCount")
	public String getPushHistoryCount(){
		String results = "[";
		int pushCount= this.getPushService().findAllPush().size();
		results+=pushCount+"]";
		return results;
	}
	
	/**
	 * CXF Service method used by devices informing KME that a message has been received
	 * on the device.
	 * 
	 * @param data
	 *            JSON containing the device id and notification id
	 * @return
	 */
	@GET
	@Path("/received")
	public Response receivedNotification(@QueryParam(value="data") String data) {
		try {
			JSONObject inform = (JSONObject) JSONSerializer.toJSON(data);
			String deviceId = inform.getString("deviceId");
			List<Device> d = deviceService.findDevicesByDeviceId(deviceId);
			if (d == null || d.size() == 0) {
				return Response.status(Response.Status.OK.getStatusCode()).build();
			}
			// TODO update user push, update PushDeviceTuple
			/*
			 * String username = d.get(0).getUsername(); long notificationId =
			 * inform.getLong("notificationId");
			 * this.userPushService.markPushReceived(username, notificationId);
			 */
		} catch (JSONException je) {
			LOG.error("JSONException in :" + data + " : " + je.getMessage());
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		} catch (ClassCastException cce) {
			LOG.error(cce.getLocalizedMessage(), cce);
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		}
		return Response.status(Response.Status.OK.getStatusCode()).build();
	}
    
    @GET
    @Path("/getUserDetails")
    public String getUserDetails(@QueryParam("pushId") final String id) {
        Push push = new Push();
        long pushId = 0;
        try {
            pushId = Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            LOG.error( "Number Format Exception: "+ nfe.getMessage() );
        }
        push = pushService.findPushById(pushId);
        String value = "pushJSON('" + JSONSerializer.toJSON(push).toString() + "');";
        return value;
    }

	/**
	 * @return the pushService
	 */
	public PushService getPushService() {
		return pushService;
	}

	/**
	 * @param pushService the pushService to set
	 */
	public void setPushService(PushService pushService) {
		this.pushService = pushService;
	}

	/**
	 * @return the deviceService
	 */
	public DeviceService getDeviceService() {
		return deviceService;
	}

	/**
	 * @param deviceService the deviceService to set
	 */
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	/**
	 * @return the kmeProperties
	 */
	public Properties getKmeProperties() {
		return kmeProperties;
	}

	/**
	 * @param kmeProperties the kmeProperties to set
	 */
	public void setKmeProperties(Properties kmeProperties) {
		this.kmeProperties = kmeProperties;
	}

	/**
	 * @return the senderKeysProperties
	 */
	public Properties getSenderKeysProperties() {
		return senderKeysProperties;
	}

	/**
	 * @param senderKeysProperties the senderKeysProperties to set
	 */
	public void setSenderKeysProperties(Properties senderKeysProperties) {
		this.senderKeysProperties = senderKeysProperties;
	}

	/**
	 * @return the senderService
	 */
	public SenderService getSenderService() {
		return senderService;
	}

	/**
	 * @param senderService the senderService to set
	 */
	public void setSenderService(SenderService senderService) {
		this.senderService = senderService;
	}
	
    /**
     * @return the messageSource
     */
    public MessageSource getMessageSource() {
        return messageSource;
    }

    /**
     * @param messageSource the messageSource to set
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * @param pmService the pmService to set
     */
    public void setPushMessageService(PushMessageService pmService) {
        this.pushMessageService = pmService;
    }
    
}
