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


package org.kuali.mobility.push.dao;

import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.PushDeviceTuple;

import java.util.Collection;
import java.util.List;

/**
 * Data Access Object for Push Objects
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
public interface PushDao {

	/**
	 * Persists a <code>Push</code> instance.
	 * @param push
	 */
    Push savePush(Push push);

	/**
	 * Removes a <code>Push</code> instance.
	 * @param push
	 */
	 boolean removePush(Push push);

	/**
	 * Persists a <code>Push</code> instance and creates <code>PushDeviceTuple</code>
	 * instances for each of the specified devices.
	 * @param push The <code>Push</code> message to persists.
	 * @param devices A list of device to create <code>PushDeviceTuple</code> records for.
	 */
	 void savePush(Push push, Collection<Device> devices);


	/**
	 * Returns the number of <code>Push</code> messages persisted.
	 * @return number of <code>Push</code> messages persisted.
	 */
	 int countPushes();


	/**
	 * Finds a <code>Push</code> by its ID.
	 * @param id ID of the <code>Push</code> to find.
	 * @return The <code>Push</code> that has the specified ID.
	 */
	 Push findPushById(Long id);


	/**
	 * Returns a list of <b>ALL</b> the <code>Push</code> messages
	 * that are persisted.<br>
	 * <b>WARNING: This method can return A LOT of data</b>
	 * @return A lsit of all the persisted <code>Push</code> messages.
	 */
	 List<Push> findAllPush();

	/**
	 * Finds all Pushes sent by a sender by the specified tool
	 * @param senderId username of the sender
	 * @param Tool to filter on
	 * @return List of notifications sent
	 */
	// List<Push> findSentItems(String senderId, String tool);


	/**
	 * Finds the devices a push message was sent too
	 * @param push <code>Push</code> message to find devices of.
	 * @return
	 */
	 List<Device> findDevicesForPush(Push push);


	/**
	 * Finds push notifications that has to be sent
	 * @return A list of <code>PushDeviceTuple</code> entries that has to be sent.
	 */
	 List<PushDeviceTuple> findUnsentPushTuples();
}
