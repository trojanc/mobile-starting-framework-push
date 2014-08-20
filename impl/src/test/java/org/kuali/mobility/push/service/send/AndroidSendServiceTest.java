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

package org.kuali.mobility.push.service.send;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.kuali.mobility.push.service.send.AndroidSendService.*;

/**
 * Unit test for the Android Send Service
 * @since 3.2.0
 * @author Kuali Mobility Team (mobility.collab@kuali.org)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:/TestSpringBeans.xml")
public class AndroidSendServiceTest {

    public static final String REGISTRATION_1 = "AAAAAAAABBBBBBBB";
    public static final String REGISTRATION_2 = "BBBBBBBBCCCCCCCC";
    public static final String REGISTRATION_3 = "CCCCCCCCDDDDDDDD";
    public static final String REGISTRATION_4 = "DDDDDDDDEEEEEEEE";
    public static final String[] REGISTRATIONS = {REGISTRATION_1,REGISTRATION_2, REGISTRATION_3,REGISTRATION_4};


    /**
     * A reference to the <code>DeviceService</code>
     */
    @Qualifier("deviceService")
    @Autowired
    private DeviceService deviceService;

    /**
     * A reference to a <code>AndroidService</code>
     */
    @Qualifier("androidSendService")
    @Autowired
    private AndroidSendService androidSendService;

    /**
     * A reference for the push to handle
     */
    private Push push;

    /**
     * List of registration ids.
     */
    private List<String> registrationIds = new ArrayList<String>();

    /**
     * Initialise some data for the unit tests
     */
    @Before
    public void setup(){
        // Setup some test devices
        for(int i = 0 ; i < 4 ; i++){
            Device d = new Device();
            d.setRegId(REGISTRATIONS[i]);
            d.setUsername("Test user");
            d.setPostedTimestamp(new Timestamp(new Date().getTime()));
            d.setType(Device.TYPE_ANDROID);
            deviceService.saveDevice(d);
            registrationIds.add(REGISTRATIONS[i]);
        }

        // Create test push message
        push = new Push();
        push.setPushId(new Long(5));
        push.setPostedTimestamp(new Timestamp(new Date().getTime()));
        push.setEmergency(false);
        push.setTitle("Test message");
        push.setSender("TesterXX");
        push.setMessage("This is a test message");
    }

    /**
     * Test the when GCm replies that a device is unregistered
     * that we actually delete the device from the database.
     */
    @Test
    @DirtiesContext
    public void testRemoveUnregisteredDevice(){
        final int INVALID_IDX = 1;

        // Sanity check
        Device device = deviceService.findDeviceByRegId(REGISTRATIONS[INVALID_IDX]);
        assertNotNull("Not expecting device to be null!", device);

        // First lets create a message to send
        JSONObject requestMessage = androidSendService.buildJsonMessage(push, registrationIds);
        JSONObject response = new JSONObject();

        // Create a response
        createResponse(requestMessage, response);

        // Set device at idx 1 as unregistered
        unregisteredDevice(INVALID_IDX, response);

        // Let the send service handle the response
        androidSendService.handleResponse(requestMessage, response);

        // Check that device id at idx 1 is gone
        device = deviceService.findDeviceByRegId(REGISTRATIONS[INVALID_IDX]);
        assertNull("Not expecting to still have that device registered!", device);
    }

    /**
     * Check that if GCM replies that a device has an invalid registration ID
     * that we delete that device
     */
    @Test
    @DirtiesContext
    public void testRemoveInvalidDevice(){
        final int INVALID_IDX = 3;

        // Sanity check
        Device device = deviceService.findDeviceByRegId(REGISTRATIONS[INVALID_IDX]);
        assertNotNull("Not expecting device to be null!", device);

        // First lets create a message to send
        JSONObject requestMessage = androidSendService.buildJsonMessage(push, registrationIds);
        JSONObject response = new JSONObject();

        // Create a response
        createResponse(requestMessage, response);

        // Set device at idx 1 as unregistered
        invalidDevice(INVALID_IDX, response);

        // Let the send service handle the response
        androidSendService.handleResponse(requestMessage, response);

        // Check that device id at idx 1 is gone
        device = deviceService.findDeviceByRegId(REGISTRATIONS[INVALID_IDX]);
        assertNull("Not expecting to still have that device registered!", device);
    }

    /**
     * Check that if GCM replies that a device's registration ID should be changed, that
     * we actually update the device with the new registration ID.
     */
    @Test
    @DirtiesContext
    public void testUpdatedDevice(){
        final int INVALID_IDX = 0;

        // Sanity check
        Device device = deviceService.findDeviceByRegId(REGISTRATIONS[INVALID_IDX]);
        assertNotNull("Not expecting device to be null!", device);
        long deviceId = device.getId();

        // First lets create a message to send
        JSONObject requestMessage = androidSendService.buildJsonMessage(push, registrationIds);
        JSONObject response = new JSONObject();

        // Create a response
        createResponse(requestMessage, response);

        // Set device at idx 1 as unregistered
        String newRegistationId = updateRegistrationId(INVALID_IDX, response);

        // Let the send service handle the response
        androidSendService.handleResponse(requestMessage, response);

        // Check that device id at idx 0 is gone
        device = deviceService.findDeviceByRegId(REGISTRATIONS[INVALID_IDX]);
        assertNull("Not expecting to still have that device registered!", device);

        // Check that a device with the new registationId exists
        device = deviceService.findDeviceByRegId(newRegistationId);
        assertNotNull("Expected to find the device with the new registation id!", device);

        // Check that the device is still the same entry
        assertEquals("Expected that the device id is the same", deviceId, device.getId().longValue());
    }

    /**
     * Update a result to be successful, be the device registration ID should be updated
     * for future request
     * @param idx Index of the device to update
     * @param response The response JSON object
     * @return The new ID assigned
     */
    private String updateRegistrationId(int idx, JSONObject response){
        String newRegId = "ABCD" + (int)(Math.random()*50000000); // Random multicast id

        // Increment the failures
        response.accumulate("failure", response.getInt("failure") +1);
        // Increment the failures
        response.accumulate("canonical_ids", response.getInt("canonical_ids") +1);

        // Edit result to be a failure
        JSONArray results = response.getJSONArray(GCM_FIELD_RESULTS);
        JSONObject result = results.getJSONObject(idx);

        // Delete the error from the result if there was one
        if (result.has(GCM_FIELD_ERROR)) result.discard(GCM_FIELD_ERROR);

        result.accumulate(GCM_FIELD_MESSAGE_ID, "1:"+ (int)(Math.random()*500));
        result.accumulate(GCM_FIELD_REGISTRATION_ID, newRegId);

        return newRegId;
    }


    /**
     * Update the response to indicate that a device is unregistered.
     * @param idx Index of the device to mark as unregistered.
     * @param response Response object to modify.
     */
    private void unregisteredDevice(int idx, JSONObject response){

        // Increment the failures
        response.accumulate("failure", response.getInt("failure") +1);

        // Edit result to be a failure
        JSONArray results = response.getJSONArray(GCM_FIELD_RESULTS);
        JSONObject result = results.getJSONObject(idx);
        if (result.has(GCM_FIELD_MESSAGE_ID)) result.discard(GCM_FIELD_MESSAGE_ID);
        result.accumulate(GCM_FIELD_ERROR, GCM_ERROR_UNREGISTERED_DEVICE);
    }

    /**
     * Update the response to indicate that a device registation ID was sent invalid
     * @param idx Index of the device to mark as incorrect registation id
     * @param response Response object to modify.
     */
    private void invalidDevice(int idx, JSONObject response){

        // Increment the failures
        response.accumulate("failure", response.getInt("failure") +1);

        // Edit result to be a failure
        JSONArray results = response.getJSONArray(GCM_FIELD_RESULTS);
        JSONObject result = results.getJSONObject(idx);
        if (result.has(GCM_FIELD_MESSAGE_ID)) result.discard(GCM_FIELD_MESSAGE_ID);
        result.accumulate(GCM_FIELD_ERROR, GCM_ERROR_INVALID_REGISTRATION);
    }

    /**
     * Create a default response object that has all set as successful
     * @param request Request object that would have been sent to GCM
     * @param response Response object to modidy.
     */
    private void createResponse(JSONObject request, JSONObject response){
        response.accumulate("multicast_id",  (int)(Math.random()*500)); // Random multicast id

        JSONArray registration_ids = request.getJSONArray(GCM_FIELD_REGISTRATION_IDS);

        response.accumulate("success",  registration_ids.size());
        response.accumulate("failure",  0);
        response.accumulate("canonical_ids",  0);

        JSONArray results = new JSONArray();
        for(int i = 0 ; i < registration_ids.size() ; i++){
            JSONObject result = new JSONObject();
            result.accumulate(GCM_FIELD_MESSAGE_ID, "1:"+ (int)(Math.random()*500));
            results.add(result);
        }

        response.accumulate(GCM_FIELD_RESULTS,  results); // Add the results
        // Success for each message
        response.accumulate("success",  (int)(Math.random()*500)); // Random multicast id
    }

}
