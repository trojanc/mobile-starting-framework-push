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

import java.util.Collection;

/**
 * This is an interface for an service that can send Push messages to devices.
 * The implementation of this service should understand how to send a message to each.
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.1.0
 */
public interface SendService {

	/**
	 * Sends a <code>Push</code> message to one specific device.
	 * If the implementation can not send a message to the specified device
	 * eg. The implementation can only send to Android devices and a iOS device is specified, an
	 * InvalidArgumentException can be thrown.
	 * 
	 * @param push The <code>Push</code> message to send.
	 * @param device The device to send the message to.
	 */
	 void sendPush(Push push, Device device) throws IllegalArgumentException;


	/**
	 * Sends a <code>Push</code> message to a list of devices.
	 * If the implementation can not send a message to one (or more) of 
	 * the devices in the list, the service may throw an IllegalArgumentException
	 * and not send further to any other devices in the list.
	 * 
	 * @param push The <code>Push</code> message to send.
	 * @param devices The list of devices to send the message to.
	 */
	 void sendPush(Push push, Collection<Device> devices) throws IllegalArgumentException;
}
