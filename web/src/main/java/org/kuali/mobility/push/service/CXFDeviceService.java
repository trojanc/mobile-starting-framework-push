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

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.kuali.mobility.push.entity.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the CXF Device Service
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 3.0
 * @deprecated This class should be replaced and implemented by {@link org.kuali.mobility.push.service.rest.DeviceServiceRest}
 */
@Service
@Deprecated
public class CXFDeviceService {

	private static final String USERNAME = "username";
	
	private static final long ONE_MONTH = 2628000000L;
	private static final long THREE_MONTHS = 7884000000L;
	private static final long SIX_MONTHS = 15768000000L;
	private static final long ONE_YEAR = 31536000000L;
	
	/** A reference to a logger for this service */
	private static final Logger LOG = LoggerFactory.getLogger(CXFDeviceService.class);
	
	/**
	 * A reference to the Controllers <code>DeviceService</code> object.
	 */
	@Autowired
	private DeviceService deviceService;

	@GET
	@Path("/count")
    public String countDevices(@QueryParam(value="data") String data) {
    	LOG.info("-----Register----- " + data);
    	if(data == null){
    		return "{\"status\":\"NO_CONTENT\"}";
    	} 

    	JSONObject queryParams;
    	try{
    		queryParams = (JSONObject) JSONSerializer.toJSON(data);
    		LOG.info(queryParams.toString());
    	}catch(JSONException je){
    		LOG.error("JSONException in :" + data + " : " + je.getMessage());
    		return "{\"status\":\"BAD_REQUEST\"}";
    	}
    	String type = queryParams.getString("type");
    	Long counted = 0l;
    	if("username".equals(type)){
    		String username = queryParams.getString("username");
    		counted = this.getDeviceService().countDevicesWithUsername(username);
    	}else if("1month".equals(type)){
    		counted = this.getDeviceService().countDevicesBefore(new Timestamp(System.currentTimeMillis() - ONE_MONTH));
    	}else if("3month".equals(type)){
    		counted = this.getDeviceService().countDevicesBefore(new Timestamp(System.currentTimeMillis() - THREE_MONTHS));
    	}else if("6month".equals(type)){
    		counted = this.getDeviceService().countDevicesBefore(new Timestamp(System.currentTimeMillis() - SIX_MONTHS));
    	}else if("1year".equals(type)){
    		counted = this.getDeviceService().countDevicesBefore(new Timestamp(System.currentTimeMillis() - ONE_YEAR));
    	}else if("allios".equals(type)){
    		counted = this.getDeviceService().countDevices(Device.TYPE_IOS);
    	}else if("allandroid".equals(type)){
    		counted = this.getDeviceService().countDevices(Device.TYPE_ANDROID);
    	}else if("allblackberry".equals(type)){
    		counted = this.getDeviceService().countDevices(Device.TYPE_BLACKBERRY);
    	}else if("allwindows".equals(type)){
    		counted = this.getDeviceService().countDevices(Device.TYPE_WINDOWS);
    	}else if("all".equals(type)){
    		counted = this.getDeviceService().countDevices();
    	} 
    	return "{\"status\":\"OK\", \"count\":\"" + counted + "\"}";
	}	
	
    @GET
    @Path("/delete")
    public String deleteDevice(@QueryParam(value="data") String data) {
    	LOG.info("-----Register----- " + data);
    	if(data == null){
    		return "{\"status\":\"NO_CONTENT\"}";
    	} 

    	JSONObject queryParams;
    	try{
    		queryParams = (JSONObject) JSONSerializer.toJSON(data);
    		LOG.info(queryParams.toString());
    	}catch(JSONException je){
    		LOG.error("JSONException in :" + data + " : " + je.getMessage());
    		return "{\"status\":\"BAD_REQUEST\"}";
    	}
    	String type = queryParams.getString("type");
    	int deleted = 20;
    	if("username".equals(type)){
    		String username = queryParams.getString("username");
    		LOG.info("Will delete all devices for username :\"" + username + "\"");
    		deviceService.removeAllDevicesWithUsername(username);
    	}else if("1month".equals(type)){
    		LOG.info("Will Delete devices older than 1 month.");
    		this.getDeviceService().removeAllDevicesBefore(new Timestamp(System.currentTimeMillis() - ONE_MONTH));
    	}else if("3month".equals(type)){
    		LOG.info("Will Delete devices older than 3 months.");
    		this.getDeviceService().removeAllDevicesBefore(new Timestamp(System.currentTimeMillis() - THREE_MONTHS));
    	}else if("6month".equals(type)){
    		LOG.info("Will Delete devices older than 6 months.");
    		this.getDeviceService().removeAllDevicesBefore(new Timestamp(System.currentTimeMillis() - SIX_MONTHS));
    	}else if("1year".equals(type)){
    		LOG.info("Will Delete devices older than 1 year.");
    		this.getDeviceService().removeAllDevicesBefore(new Timestamp(System.currentTimeMillis() - ONE_YEAR));
    	}else if("allios".equals(type)){
    		LOG.info("Will Delete all iOS devices.");
    		this.getDeviceService().removeAllDevicesByType(Device.TYPE_IOS);
    	}else if("allandroid".equals(type)){
    		LOG.info("Will Delete all Android devices.");
    		this.getDeviceService().removeAllDevicesByType(Device.TYPE_ANDROID);
    	}else if("allblackberry".equals(type)){
    		LOG.info("Will Delete all Blackberry devices.");
    		this.getDeviceService().removeAllDevicesByType(Device.TYPE_BLACKBERRY);
    	}else if("allwindows".equals(type)){
    		LOG.info("Will Delete all Windows devices.");
    		this.getDeviceService().removeAllDevicesByType(Device.TYPE_WINDOWS);
    	}else if("all".equals(type)){
    		LOG.info("Will Delete all devices.");
    		deviceService.removeAllDevices();
    	} 
		return "{\"status\":\"OK\", \"count\":\"" + deleted + "\"}";
    }
	
	@POST
	@Path("/remove")
	public Response removeDevice(@RequestBody String data){
		LOG.info("----Remove Device: " + data);
        if (data == null) {
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        }

        JSONObject queryParams;
        try {
            queryParams = (JSONObject) JSONSerializer.toJSON(data);
            LOG.info(queryParams.toString());
        } catch (JSONException je) {
            LOG.error("JSONException in :" + data + " : " + je.getMessage());
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }		
		
        String devId = queryParams.getString("deviceId");
		LOG.info("----Remove Device: " + devId);
		
        Device deviceToDelete = getDeviceService().findDeviceByDeviceId(devId);
		LOG.info(deviceToDelete.toString());
        
        if (deviceToDelete != null) {
            LOG.debug("Will delete device with Id: " + deviceToDelete.getId());
            if (getDeviceService().removeDevice(deviceToDelete)) {
                LOG.debug("Did delete device.");
        		return Response.status(Response.Status.OK.getStatusCode()).build();
            }else{
            	return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
            }
        }
		return Response.status(Response.Status.OK.getStatusCode()).build();
	}
	
	@GET
	@Path("/counts")
	public String getDeviceCounts(){
		Long all = this.getDeviceService().countDevices();
		Long iOS = this.getDeviceService().countDevices(Device.TYPE_IOS);
		Long android = this.getDeviceService().countDevices(Device.TYPE_ANDROID);
		Long blackberry = this.getDeviceService().countDevices(Device.TYPE_IOS);
		Long windows = this.getDeviceService().countDevices(Device.TYPE_WINDOWS);
		Long unreg = this.getDeviceService().countDevicesWithoutUsername();
				
		return "{\"all\":\"" + all + "\",\"iOS\":\"" + iOS +"\",\"android\":\"" + android + "\",\"blackberry\":\""+blackberry+"\",\"windows\":\"" + windows + "\",\"unregistered\":\"" + unreg + "\" }";
	}
	
	/**
	 * A controller method for registering a device for push notifications as defined by a JSON formatted string. 
	 * 
	 * @param data JSON formatted string describing a device to be register for push notifications.
	 * @return
	 */
	@GET
    @Path("/register")
	public Response register(@Context MessageContext context, @QueryParam(value="data") String data) {
		HttpServletRequest request = context.getHttpServletRequest();
		LOG.info("-----Register-----");
		if(data == null){
			return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
		} 

		JSONObject queryParams;
		try{
			queryParams = (JSONObject) JSONSerializer.toJSON(data);
			LOG.info(queryParams.toString());
		}catch(JSONException je){
			LOG.error("JSONException in :" + data + " : " + je.getMessage());
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		}
			
		Device device = new Device();
		device.setDeviceName(queryParams.getString("name"));
		device.setDeviceId(queryParams.getString("deviceId"));
		device.setRegId(queryParams.getString("regId"));
		device.setType(queryParams.getString("type"));
		
		// We might not have a username yet
		if (queryParams.containsKey(USERNAME)){
			device.setUsername(queryParams.getString(USERNAME));
		}
		device.setPostedTimestamp(new Timestamp(System.currentTimeMillis()));
		LOG.info("\n-----New Device-----" + device.toString());

		Device temp = getDeviceService().findDeviceByDeviceId(device.getDeviceId());

		// If the device already exists, update. 
		try {
			if(temp != null){
				LOG.info("-----Device already exists." + temp.toString());
				temp.setDeviceName(queryParams.getString("name"));
				temp.setRegId(queryParams.getString("regId"));
				temp.setType(queryParams.getString("type"));
				// We might not have a username yet
				if (queryParams.containsKey(USERNAME)){
					temp.setUsername(queryParams.getString(USERNAME));
				}
				temp.setPostedTimestamp(new Timestamp(System.currentTimeMillis()));
				getDeviceService().saveDevice(temp);
			}else{
				LOG.info("-----Device Doesn't already exist." + device.toString());
				getDeviceService().saveDevice(device);
			}
		} catch (Exception e) {
			LOG.error("Exception while trying to update device", e);
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		} 		
		return Response.status(Response.Status.OK.getStatusCode()).build();
	}
	
	/**
	 * CXF Service method for updating a pre-existing registered device. 
	 * 
	 * @param data A JSON formatted string describing details of a device to update.
	 * @return
	 */
	@GET
    @Path("/update")
	public Response update(@Context MessageContext context, @QueryParam(value="data") String data) {
		HttpServletRequest request = context.getHttpServletRequest();
		LOG.info("-----Register-----");
		if(data == null){
			return Response.status(Response.Status.OK.getStatusCode()).build();
		} 

		JSONObject queryParams;
		try{
			queryParams = (JSONObject) JSONSerializer.toJSON(data);
			LOG.info(queryParams.toString());
		}catch(JSONException je){
			LOG.error("JSONException in :" + data + " : " + je.getMessage());
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		}

		Device temp = getDeviceService().findDeviceByDeviceId(queryParams.getString("deviceId"));

		// If the device already exists, update.
		if(temp != null){
			LOG.info("-----Device already exists." + temp.toString());
			// Remove this device from the logged user's profile if it exists.
//			User user = getUserDao().loadUserByLoginName(temp.getUsername());
//			if( user != null ) {
//				user.removeAttribute(AuthenticationConstants.DEVICE_ID,temp.getDeviceId());
//				getUserDao().saveUser(user);
//			}
			temp.setDeviceName(queryParams.getString("name"));
			temp.setUsername(queryParams.getString(USERNAME));
			temp.setPostedTimestamp(new Timestamp(System.currentTimeMillis()));
			getDeviceService().saveDevice(temp);

			// Find the real user and set the new device.
//			User user2 = (User)request.getSession().getAttribute(AuthenticationConstants.KME_USER_KEY);
//			if( user2 == null ) {
//				LOG.error("No user found in request. This should never happen!");
//			} else if( user2.isPublicUser() ) {
//				LOG.debug("Public user found, no user profile updates necessary.");
//			} else if( !user2.getLoginName().equals(temp.getUsername()) ) {
//				LOG.debug("User on device does not match user in session! This should never happen either.");
//			} else if( user2.attributeExists(AuthenticationConstants.DEVICE_ID,temp.getDeviceId())) {
//				LOG.debug("Device id already exists on user and no action needs to be taken.");
//			} else {
//				user2.addAttribute(AuthenticationConstants.DEVICE_ID,temp.getDeviceId());
//				getUserDao().saveUser(user2);
//			}

		}
		return Response.status(Response.Status.OK.getStatusCode()).build();
	}
	
	
	/**
	 * CXF Service method for retrieving devices based on a keyword search. 
	 * @param keyword
	 * @return
	 */
	@GET
    @Path("/keyword/{keyword}")
    public String devicesByKeyword(@PathParam("keyword") final String keyword) {  
		List<Device> devices = getDeviceService().findDevicesByKeyword(keyword);
		LOG.info(devices.size() + " elements returned.");

		String json = "";
		if(devices.size() > 0){
			Iterator<Device> i = devices.iterator();	
			if(devices.size() > 1){
				json = "{\"total\":\"" + devices.size() + " Devices Found.\" , \"devices\":[";
			}else{
				json = "{\"total\":\"" + devices.size() + " Device Found.\" , \"devices\":[";
			}			
			while(i.hasNext()){
				json += ((Device)i.next()).toJson();
				json += ",";
			}
			json = json.substring(0, json.length()-1);
			json += "]}";		
		}else{
			json = "{\"total\":\"No Devices Found\"}";
		}		
		return json;
    }
	
	/**
	 * CXF Service method for retrieving a list of device associated with a given username. 
	 * @param username
	 * @return
	 */
	@POST
    @Path("/username/")
    public String devicesFromUsername(@FormParam("username") final String username) {  
		List<Device> devices = getDeviceService().findDevicesByUsername(username);
		String result = "{\"username\":\"" + username + "\",\"devices\":[";
		String sDevices = "";
		Iterator<Device> i = devices.iterator();
		
		while(i.hasNext()){
			Device d = (Device)i.next();
			sDevices += "\"" + d.getDeviceId() + "\"," ;
		}
		
		if(sDevices.length() > 0){
			sDevices = sDevices.substring(0, sDevices.length() - 1);
		}
		
		result += sDevices + "]}";
		return result;
	}
	
	/**
	 * CXF Service method for retrieving a username for a given device based on the deviceId. 
	 * @param id
	 * @return
	 */
	@POST
    @Path("/deviceid/")
    public String usernameFromDeviceId(@FormParam("deviceid") final String id) {  
		Device device = getDeviceService().findDeviceByDeviceId(id);
		if(device == null){
			return "{\"deviceExists\":false}";			
		}
		if(device.getUsername() == null || device.getUsername().length() == 0){
			return "{\"deviceExists\":true,\"hasUsername\":false}";
		}else{
			return "{\"deviceExists\":true,\"hasUsername\":true,\"username\":\"" + device.getUsername() + "\"}";
		}
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
	
//	/**
//	 * Get the controllers <code>UserDao</code> object.
//	 * @return
//	 */
//	public UserDao getUserDao() {
//		return userDao;
//	}
//
//	/**
//	 * A method for setting the <code>UserDao</code> of the controller.
//	 *
//	 * @param userDao
//	 */
//	public void setUserDao(UserDao userDao) {
//		this.userDao = userDao;
//	}
	
}
