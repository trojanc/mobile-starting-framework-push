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
import org.kuali.mobility.push.service.rest.pojo.DeviceResponse;
import org.kuali.mobility.push.service.rest.pojo.DevicesResponse;
import org.kuali.mobility.push.service.rest.pojo.ServiceObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public interface DeviceServiceRest {

    /** Device type constant for Android devices */
    String TYPE_ANDROID 	= "Android";

    /** Devices type constant for iOS devices */
   String TYPE_IOS			= "iOS";

    /** Device type constant for BlackBerry devices */
   String TYPE_BLACKBERRY 	= "BlackBerry";

    /** Device type constraint for Windows devices */
    String TYPE_WINDOWS		= "WindowsMobile";

    /**
     * Persists a device and returns the updated device
     * @param device
     */
    @POST
    @Path("/saveDevice")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	DeviceResponse saveDevice(Device device);

    /**
     * Registers a device
     * @param device
     */
    @POST
    @Path("/registerDevice")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	DeviceResponse registerDevice(Device device);

    /**
     *
     * @return
     */
    @GET
    @Path("/devices")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	DevicesResponse getDevices();

    /**
     * Finds a <code>Device</code> by the Object ID for the <code>Device</code>
     * @param id Id of the <code>Device</code> to find.
     * @return
     */
    @GET
    @Path("/findDeviceById")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    DeviceResponse findDeviceById(@QueryParam(value="id") Long id);

    /**
     * Finds a <code>Device</code> by the Object ID for the <code>Device</code>
     * @param deviceId Id of the <code>Device</code> to find.
     * @return
     */
    @GET
    @Path("/findDeviceByDeviceId")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	DeviceResponse findDeviceByDeviceId(@QueryParam(value="deviceId") String deviceId);


    @GET
    @Path("/findDeviceByUsername")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    DevicesResponse findDeviceByUsername(@QueryParam(value="username") String username);


    @DELETE
    @Path("/deleteDeviceByDeviceId")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    ServiceObject deleteDeviceByDeviceId(@QueryParam(value="deviceId") String deviceId);


}
