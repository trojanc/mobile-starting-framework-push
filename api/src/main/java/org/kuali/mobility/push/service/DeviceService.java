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

import org.kuali.mobility.push.entity.Device;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Interface for managing <code>Device</code> instances.
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
public interface DeviceService {

	/**
	 * Persists a <code>Device</code> instance
	 * @param device The device to persist
	 */
    Device saveDevice(Device device);

	/**
	 * Persists a <code>Device</code> instance
	 * @param device The device to persist
	 * @deprecated This method is misleading as it does not register a device
	 * but persists the state of a device. Rather use {@link #saveDevice(org.kuali.mobility.push.entity.Device)}.
	 * This method will be removed in version 3.0
	 */
	@Deprecated
	void registerDevice(Device device);

	/**
	 * Removes a <code>Device</code> instance from persistence.
	 * @param device The device to remove.
	 * @return
	 */
	boolean removeDevice(Device device); 

	/**
	 * 
	 * @param username
	 * @return
	 */
	boolean removeAllDevicesWithUsername(String username);	

	/**
	 * 
	 * @return
	 */
	boolean removeAllDevices();
	
	/**
	 * 
	 * @return
	 */
	boolean removeAllDevicesByType(String type);


	/**
	 * 
	 * @param t
	 * @return
	 */
	boolean removeAllDevicesBefore(Timestamp t);
	

	/**
	 * Counts the number of all registered devices.
	 * This can included devices that does not have a Registration ID and Username.
	 * @return
	 */
	Long countDevices();


	/**
	 * Counts the number of devices by device type.
	 * This can included devices that does not have a Registration ID and Username.
	 * @return
	 */
	Long countDevices(String deviceType);

	/**
	 * 
	 * @param ts
	 * @return
	 */
	Long countDevicesBefore(Timestamp ts);

	/**
	 * 
	 * @param username
	 * @return
	 */
	Long countDevicesWithUsername(String username);
	
	
	/**
	 * Returns a count of all devices that does not have a username
	 * @return
	 */
	Long countDevicesWithoutUsername();


	/**
	 * Find all devices that are registered.
	 * This can included devices that does not have a Registration ID and Username
	 * @return
	 */
	List<Device> findAllDevices();


	/**
	 * Returns a list of device types that are set up.
	 * This list will be dictated by the SenderService implemenations that was
	 * set up with the <code>SendServiceDelegator</code>.
	 * Device types returns in this list are expected to have SendService implementations
	 * configured.
	 * This method will call the <code>SendServiceDelegator</code> to find supported device types.
	 * @return A list of supported device types.
	 */
	List<String> getSupportedDeviceTypes();


	/**
	 * Finds all devices of a specific type.
	 * This can included devices that does not have a Registration ID and Username
	 * @param deviceType 
	 * @return
	 */
	List<Device> findAllDevices(String deviceType);


	/**
	 * Gets a map of all registerd devices in a mapping between device type
	 * and a list of the devices for that type.
	 * 
	 * @return
	 */
	Map<String, List<Device>> findDevicesMap();


	/**
	 * Return tue if there is a username for the specifed deviceId
	 * @param deviceid
	 * @return
	 */
	boolean doesDeviceHaveUsername(String deviceid);


	/**
	 * Returns a list of devices registed by a username.
	 * @param username
	 * @return
	 */
	List<Device> findDevicesByUsername(String username);


	/**
	 * Finds all device that does not have a username
	 * @return
	 */
	List<Device> findDevicesWithoutUsername();


	/**
	 * Finds devices with the specified device id.
	 * In theory, this should only return on <code>Device</code>
	 * @param deviceid
	 * @return
	 */
	List<Device> findDevicesByDeviceId(String deviceid);

	/**
	 * Finds all devices with a specified device Type. 
	 * @param type
	 * @return
	 */
	 List<Device> findDevicesByType(String type);
	 
	/**
	 * Finds devices with the specified reg id.
	 * In theory, this should only return on <code>Device</code>
	 * @param deviceid
	 * @return
	 */
	Device findDeviceByRegId(String regid);
	

	/**
	 * Finds a device with the specified device ID
	 * @param deviceid
	 * @return
	 */
	Device findDeviceByDeviceId(String deviceid);


	/**
	 * Finds a <code>Device</code> by the Object ID for the <code>Device</code>
	 * @param id If of the <code>Device</code> to find. 
	 * @return
	 */
	Device findDeviceById(Long id);

	/**
	 * Finds a List of <code>Device</code> based on the specified keyword.
	 * @param keyword
	 * @return Device List. 
	 */
	List<Device> findDevicesByKeyword(String keyword);
	

	/**
	 * CXF method for search for devices by keyword. 
	 * @param keyword
	 * @return
	 */
	String devicesByKeyword(String keyword); 
	
	/**
	 * CXF method for retrieving devices for a given username.
	 * @param username
	 * @return
	 */
    String devicesFromUsername(String username);
	
    /**
     * CXF method for retrieving a username for a given device. 
     * @param id
     * @return
     */
    String usernameFromDeviceId(String id);
	
}
