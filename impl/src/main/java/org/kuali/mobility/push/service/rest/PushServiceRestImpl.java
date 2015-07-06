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
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.service.DeviceService;
import org.kuali.mobility.push.service.PushService;
import org.kuali.mobility.push.service.rest.pojo.PushConfigResponse;
import org.kuali.mobility.push.service.rest.pojo.PushResponse;
import org.kuali.mobility.push.service.rest.pojo.SendPushRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * A restfull service implementation for devices
 */
@Service
public class PushServiceRestImpl implements PushServiceRest {

    private static final Logger LOG = LoggerFactory.getLogger(PushServiceRestImpl.class);

    @Autowired
    private PushService pushService;

    @Autowired
    private DeviceService deviceService;

	@Resource(name="kmeProperties")
	private Properties kmeProperties;


    @Override
    public PushResponse sendPush(SendPushRequest request) {

        // First persist the new push from the request
        Push push = request.getPush();
        push = pushService.savePush(push);

        // List of all devices this push message will be sent to
        Map<Long, Device> devices = new HashMap<Long, Device>();

        // Now we find all the devices for the usernames specified
        List<String> deviceIds =  request.getDeviceIds();
        for(String deviceId : deviceIds){
            Device device = deviceService.findDeviceByDeviceId(deviceId);
            if(device == null){
                LOG.warn("Could not find device for deviceId : \"" + deviceId + "\"");
            }
            // Only add device if its not already there
            else if(devices.containsKey(device.getId())){
                devices.put(device.getId(), device);
            }
        }

        // Find all the devices for usernames added
        List<String> usernames = request.getUsernames();
        for(String username : usernames){
            List<Device> userDevices = deviceService.findDevicesByUsername(username);
            if(userDevices == null || userDevices.size() == 0){
                LOG.warn("Could not find devices for username : \"" + username + "\"");
            }else{
                for(Device device : userDevices){
                    if(device != null && devices.containsKey(device.getId())){
                        devices.put(device.getId(), device);
                    }
                }
            }
        }
        pushService.savePush(push, devices.values());
        return new PushResponse(push);
    }

    @Override
    public PushConfigResponse getPushConfig() {
		HashMap<String, String> config = new HashMap<String, String>();

		// Android
		config.put("android.senderId", kmeProperties.getProperty("push.google.gcm.senderId"));
        return new PushConfigResponse(config);
    }

    @Override
    public PushResponse findPushById(Long pushId) {
        return new PushResponse(pushService.findPushById(pushId));
    }
}
