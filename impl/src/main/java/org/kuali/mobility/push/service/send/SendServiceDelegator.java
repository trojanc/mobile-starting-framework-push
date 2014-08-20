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

import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Preference;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.service.PreferenceService;
import org.kuali.mobility.push.service.SendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This implementation of the <code>SendService</code>
 * Can send Push notifications to a configured set of Operating systems.
 * 
 * At this moment, this service will not perform any persistence of messages sent, nor will the 
 * implementations that the messages will be delegated to. The persist message being sent, rather
 * call the <code>PushService</code> which does persistence, and uses this service to perform the
 * actual sending of messages.
 * 
 * This service will maintains a thread pool and notifications are put in a queue to be sent 
 * (it uses <codde>Executors.newCachedThreadPool()</code>)
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.2.0
 */
@Service("sendServiceDelegator")
public class SendServiceDelegator implements SendService {
    private static final Logger LOG = LoggerFactory.getLogger(SendServiceDelegator.class);

	/** A list of senders that this service can use */
	private final Map<String, SendService> senders = new HashMap<String, SendService>();

	/** A cached thread pool which allows us to queue notification tasks */
	private final ExecutorService exeService = Executors.newCachedThreadPool();

	/** A PreferenceService object used for checking if Sender is blocked for a given user. */
	@Autowired
	private PreferenceService preferenceService;
	
	/**
	 * Sets the senders that this SendService can send to.
	 * This is typically set using Spring
	 * @param senders
	 */
	public void setSenders(Map<String, SendService> senders){
		this.senders.putAll(senders);
	}


	/**
	 * Gets the map of senders
	 * @return returns a map of available sender services
	 */
	private Map<String, SendService> getSenders(){
		return this.senders;
	}


	/**
	 * Returns a list of device types that this <code>SendServiceDelegator</code>supports
	 * @return
	 */
	public List<String> getSupportedDeviceTypes(){
		return new Vector<String>(this.senders.keySet());
	}


	/**
	 * This method will delegate the push notification to the appropriate implementation for the destination device
	 * The message will first be queued in this class, before being delegated to the implementation in a
	 * seperate thread.
	 */
	public void sendPush(Push push, Device device){
		this.exeService.execute(new SendRunner(push, device));
	}


	/**
	 * This method will deletegate the push message to the appropriate implementations for the destination devices.
	 * This message will first be queued in this service, before being delegated to the different implementations
	 */
	public void sendPush(Push push, Collection<Device> devices){
		this.exeService.execute(new SendRunner(push, devices));
	}

	/**
	 * This method is used to set the <code>PreferenceService</code> for the <code>SendServiceDelegator</code> object. 
	 * 
	 * @param preferenceService
	 */
	public void setPreferenceService(PreferenceService preferenceService) {
		this.preferenceService = preferenceService;
	}


	/**
	 * A runnable that will be used to send the notification using a device
	 * specific implementation of the send service.
	 */
	private class SendRunner implements Runnable{

		/** The Push message to be sent */
		private Push push;

		/** List of devices that the message should be sent to */
		private Collection<Device> devices;

		/**
		 * Creates a new instance of the <code>SendRunner</code>
		 * @param push Push message to send
		 * @param device The device to sent to
		 */
		public SendRunner(Push push, Device device){
			this(push, Collections.nCopies(1, device));
		}


		/**
		 * Creates a new instance of the <code>SendRunner</code>
		 * @param push Push message to send.
		 * @param devices List of devices the message should be sent to.
		 */
		public SendRunner(Push push, Collection<Device> devices){
			this.push = push;
			this.devices = devices;
		}


		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			for(String type : senders.keySet()){
				List<Device> deviceList = getDevicesOfType(type, devices);
				if (deviceList.size() > 0){
					SendService sender = getSenders().get(type);
					if (sender != null){
						sender.sendPush(push, deviceList);
					}
				}
			}
		}


		/**
		 * Filters all the devices of a specific type into a list.
		 * @param deviceType
		 * @param devices
		 * @return
		 */
		private List<Device> getDevicesOfType(String deviceType, Collection<Device> devices){
			List<Device> deviceList = new ArrayList<Device>();
			for(Device d : devices){
				if (d != null && deviceType.equals(d.getType())){
					
					Preference preference = preferenceService.findPreference(d.getUsername(), push.getSender());
					if(preference == null || !preference.isSenderBlocked()){ 					
						deviceList.add(d);
					} else {
                        LOG.debug("Device excluded from push by preferences: "+d.getDeviceId());
                    }
					
				}
			}
			return deviceList;
		}

	}
}
