/*
  The MIT License (MIT)
  
  Copyright (C) 2014 by Kuali Foundation

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
 
  The above copyright notice and this permission notice shall be included in

  all copies or substantial portions of the Software.
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
*/

package org.kuali.mobility.push.service;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.kuali.mobility.push.dao.DeviceDao;
import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.service.send.SendServiceDelegator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the Device Service
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
@Service
public class DeviceServiceImpl implements DeviceService {

	/** A reference to a logger for this service */
	private static final Logger LOG = Logger.getLogger(DeviceServiceImpl.class);
	
	/** A reference to the <code>DeviceDao</code> */
	@Autowired
	private DeviceDao deviceDao;
	
	/**
	 * A reference to the <code>SendServiceDelegator</code>
	 */
	@Autowired
	@Qualifier("sendServiceDelegator")
	private SendServiceDelegator sendServiceDelegator;

	@Override
	public Device saveDevice(Device device){
		return deviceDao.saveDevice(device);
	}
	

	@Deprecated
	@Override
	public void registerDevice(Device device) {
		this.saveDevice(device);
	}


	@Override
	public List<Device> findAllDevices() {
		return deviceDao.findAllDevices();
	}

	@Override
	public boolean doesDeviceHaveUsername(String deviceid){
		return deviceDao.doesDeviceHaveUsername(deviceid);
	}


	@Override
	public List<Device> findDevicesByUsername(String username) {
		return deviceDao.findDevicesByUsername(username);
	}

	@Override
	public List<Device> findDevicesWithoutUsername() {
		return deviceDao.findDevicesWithoutUsername();
	}

	@Override
	public Device findDeviceByRegId(String regid) {
		return deviceDao.findDeviceByRegId(regid);
	}

	@Override
	public List<Device> findDevicesByDeviceId(String deviceid) {
		return deviceDao.findDevicesByDeviceId(deviceid);
	}

	@Override
	public Device findDeviceByDeviceId(String deviceid) {
		return deviceDao.findDeviceByDeviceId(deviceid);
	}

	@Override
	public Device findDeviceById(Long id) {
		return deviceDao.findDeviceById(id);
	}

	@Override
	public List<Device> findDevicesByKeyword(String keyword){
		return deviceDao.findDevicesByKeyword(keyword);
	}

	@Override
	public List<Device> findDevicesByType(String type){
		 return deviceDao.findDevicesByType(type);
	 }

	@Override
	public boolean removeDevice(Device device){
		return deviceDao.removeDevice(device);
	}

	@Override
	public boolean removeAllDevicesWithUsername(String username){
		return deviceDao.removeAllDevicesWithUsername(username);
	}

	@Override
	public boolean removeAllDevices(){
		return deviceDao.removeAllDevices();
	}

	@Override
	public boolean removeAllDevicesByType(String type){
		return deviceDao.removeAllDevicesByType(type);
	}

	@Override
	public boolean removeAllDevicesBefore(Timestamp t){
		return deviceDao.removeAllDevicesBefore(t);
	}

	@Override
	public Long countDevices(){
		return deviceDao.countDevices();
	}

	@Override
	public Long countDevicesWithoutUsername(){
		return deviceDao.countDevicesWithoutUsername();
	}

	@Override
	public Long countDevices(String deviceType) {
		return this.deviceDao.countDevices(deviceType);
	}

	@Override
	public Long countDevicesBefore(Timestamp ts){
		return deviceDao.countDevicesBefore(ts);
	}
	
	@Override
	public Long countDevicesWithUsername(String username){
		return deviceDao.countDevicesWithUsername(username);
	}
	
	@Override
	public List<Device> findAllDevices(String deviceType) {
		return this.deviceDao.findAllDevices(deviceType);
	}

	@Override
	public Map<String, List<Device>> findDevicesMap() {
		Map<String, List<Device>> deviceMapping = new HashedMap();
		List<String> deviceTypes = this.getSupportedDeviceTypes();
		List<Device> devices;
		for (String deviceType : deviceTypes){
			devices = this.findAllDevices(deviceType);
			deviceMapping.put(deviceType, devices);
		}
		return deviceMapping;
	}


	/**
	 * Sets the reference to the <code>DeviceDao</code>
	 * @param dao
	 */
	public void setDeviceDao(DeviceDao dao) {
		this.deviceDao = dao;
	}


	/**
	 * Gets the reference to the <code>DeviceDao</code>
	 * @return
	 */
	public DeviceDao getDeviceDao() {
		return this.deviceDao;
	}

	/**
	 * Get a list of the Supported Device Types. 
	 * @return List<String>  
	 */
	@Override
	public List<String> getSupportedDeviceTypes() {
		return this.sendServiceDelegator.getSupportedDeviceTypes();
	}
	

	@Override
	@GET
    @Path("/keyword/{keyword}")
    public String devicesByKeyword(@PathParam("keyword") final String keyword) {  
		List<Device> devices = findDevicesByKeyword(keyword);
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
	
	@Override
	@POST
    @Path("/username/")
    public String devicesFromUsername(@FormParam("username") final String username) {  
		List<Device> devices = findDevicesByUsername(username);
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

	@Override
	@POST
    @Path("/deviceid/")
    public String usernameFromDeviceId(@FormParam("deviceid") final String id) {  
		Device device = findDeviceByDeviceId(id);
		if(device == null){
			return "{\"deviceExists\":false}";			
		}
		if(device.getUsername() == null || device.getUsername().length() == 0){
			return "{\"deviceExists\":true,\"hasUsername\":false}";
		}else{
			return "{\"deviceExists\":true,\"hasUsername\":true,\"username\":\"" + device.getUsername() + "\"}";
		}
	}
	
}
