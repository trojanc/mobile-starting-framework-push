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

import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.PushDeviceTuple;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Collection;
import java.util.List;

/**
 * Interface for sending and persisting <code>Push</code> messages to
 * <code>Device</code>.
 * 
 * Implementations of this service will typically persist <code>Push</code>
 * messages from a database, retrieve devices from a service, and send the
 * message to the devices using an implementation of the <code>SendService</code>
 * interface.
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
public interface PushService {

	/**
	 * Persists a <code>Push</code> message.
	 * 
	 * @param push Push message to persist.
	 */
    @POST
    @Path("/savePush")
    Push savePush(Push push);

	/**
	 * Removes a <code>Push</code> message.
	 * 
	 * @param push Push message to persist.
	 */
    @POST
    @Path("/removePush")
	boolean removePush(Push push);

	/**
	 * Persists a push messages that should be sent to many devices
	 * 
	 * @param push Push message to be sent.
	 * @param Devices Devices the Push message should be sent to.
	 */
    @POST
    @Path("/savePush")
	void savePush(Push push, Collection<Device> Devices);

	/**
	 * Returns the number of <code>Push</code> messages persisted.
	 * 
	 * @return number of <code>Push</code> messages persisted.
	 */
	int countPushes();

	/**
	 * Finds a <code>Push</code> by its ID.
	 * 
	 * @param id ID of the <code>Push</code> to find.
	 * @return The <code>Push</code> that has the specified ID.
	 */
	Push findPushById(Long id);

	/**
	 * Returns a list of <b>ALL</b> the <code>Push</code> messages that are
	 * persisted.<br>
	 * <b>WARNING: This method can return A LOT of data</b>
	 * 
	 * @return A list of all the persisted <code>Push</code> messages.
	 */
	List<Push> findAllPush();

	/**
	 * Returns a list of <b>ALL</b> the <code>Push</code> messages per page number that are
	 * persisted.<br>
	 * The number of records per page is fetched from property file.<br>
	 * <b>WARNING: This method can return A LOT of data</b>
	 * 
	 * @return A lsit of all the persisted <code>Push</code> messages.
	 */
	List<Push> findAllPushByPageNumber(Integer pageNum);

	/**
	 * Finds a list of <code>Devices</code> that was linked to a
	 * <code>Push</code>.
	 * 
	 * @param push
	 *            <code>Push</code> to find devices of</code>.
	 * @return List of <code>Devices</code> that was linked.
	 */
	List<Device> findDevicesForPush(Push push);

	/**
	 * Finds a list of unsent <code>PushDeviceTuple</code>.
	 * 
	 * @return List of unsent <code>PushDeviceTuple</code>.
	 */
	List<PushDeviceTuple> findUnsentPushTuples();

	/**
	 * Sends a <code>Push</code> message to the specified <code>Device</code>
	 * without persisting the message.
	 * @deprecated use {@link #savePush(org.kuali.mobility.push.entity.Push)} instead.
	 * @param push
	 *            Push message to send.
	 * @param device
	 *            Device to send the message to.
	 */
    @Deprecated
	int sendPush(Push push, Device device);

	/**
	 * Sends a <code>Push</code< message to the specified list of devices
	 * without persisting the message.
	 *
     * @deprecated use {@link #savePush(org.kuali.mobility.push.entity.Push, java.util.Collection)} instead.
	 * @param push Push message to send.
	 * @param devices Devices to send the message to.
	 */
    @Deprecated
	int sendPush(Push push, Collection<Device> devices);

	/**
	 * Sends a
	 * <code>Push</code< message to the specified list of username. All devices for those users will be searched
	 * for, and messages will be sent to each device without persisting the message.
	 * 
	 * @param push
	 *            Push message to send.
	 * @param usernames
	 *            List of usernames to send push notifications too.
	 */
	int sendPushToUsers(Push push, List<String> usernames);

	/**
	 * Attempts to send <code>PushDeviceTuple</code>
	 * 
	 * @param tuples
	 *            List of <code>PushDeviceTuple</code> to send.
	 * @return
	 */
	int sendPush(List<PushDeviceTuple> tuples);

}
