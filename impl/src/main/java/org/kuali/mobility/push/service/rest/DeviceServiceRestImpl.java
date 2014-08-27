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


package org.kuali.mobility.push.service.rest;

import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.service.DeviceService;
import org.kuali.mobility.push.service.rest.pojo.DeviceResponse;
import org.kuali.mobility.push.service.rest.pojo.DevicesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A restful service implementation for devices
 */
@Service
public class DeviceServiceRestImpl implements DeviceServiceRest {

	/**
	 * A reference to a logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DeviceServiceRestImpl.class);

	/**
	 * A reference to the device service
	 */
	@Autowired
	private DeviceService deviceService;

	@Override
	public DeviceResponse saveDevice(Device device) {
		return new DeviceResponse(deviceService.saveDevice(device));
	}

	@Override
	public DeviceResponse registerDevice(Device device) {
		Device existingdevice = deviceService.findDeviceByDeviceId(device.getDeviceId());
		if(existingdevice != null){
			existingdevice.setUsername(device.getUsername());
			existingdevice.setType(device.getType());
			existingdevice.setPostedTimestamp(device.getPostedTimestamp());
			existingdevice.setDeviceId(device.getDeviceId());
			existingdevice.setRegId(device.getDeviceId());
		}
		else{
			existingdevice = device;
		}
		return new DeviceResponse(deviceService.saveDevice(existingdevice));
	}

	@Override
	public DevicesResponse getDevices() {
		return new DevicesResponse(deviceService.findAllDevices());
	}

	@Override
	public DeviceResponse findDeviceByDeviceId(String deviceId) {
		return new DeviceResponse(deviceService.findDeviceByDeviceId(deviceId));
	}

	public void setDeviceService(DeviceService deviceService){
		this.deviceService = deviceService;
	}
}
