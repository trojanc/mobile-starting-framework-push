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
