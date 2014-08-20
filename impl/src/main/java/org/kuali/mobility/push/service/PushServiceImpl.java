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

import org.kuali.mobility.push.dao.PushDao;
import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.PushDeviceTuple;
import org.kuali.mobility.push.service.send.SendServiceDelegator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Implementation of the <code>PushService</code>
 * 
 * @since 2.0.0
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 */
// @Service
public class PushServiceImpl implements PushService {

	/** A reference to a logger for this service */
	private static final Logger LOG = LoggerFactory.getLogger(PushServiceImpl.class);

	/**
	 * A reference to the <code>SendServiceDelegator</code> object used by this
	 * ServiceImpl
	 */
	@Autowired
	@Qualifier("sendServiceDelegator")
	private SendServiceDelegator sendService;

	/**
	 * A reference to the <code>DeviceService</code> object used by this
	 * ServiceImpl
	 */
	@Autowired
	private DeviceService deviceService;

	/**
	 * A reference to the <code>PushDeviceTupleService</code> object used by
	 * this ServiceImpl
	 */
	@Autowired
	private PushDeviceTupleService pdtService;

	/** A reference to the <code>PushDao</code> */
	@Autowired
	private PushDao pushDao;

	@Resource(name = "kmeProperties")
	private Properties kmeProperties;

	@Override
	@Transactional
	public Push savePush(Push push) {
		return pushDao.savePush(push);
	}

	@Override
	@Transactional
	public boolean removePush(Push push) {
		return pushDao.removePush(push);
	}

	@Override
	@Transactional
	public void savePush(Push push, Collection<Device> devices) {
		pushDao.savePush(push, devices);
	}

	@Transactional
	public Push findPushById(Long id) {
		return pushDao.findPushById(id);
	}

	@Transactional
	public List<Device> findDevicesForPush(Push push) {
		return pushDao.findDevicesForPush(push);
	}

	@Transactional
	public List<PushDeviceTuple> findUnsentPushTuples() {
		return pushDao.findUnsentPushTuples();
	}

	@Transactional
	public List<Push> findAllPush() {
		return pushDao.findAllPush();
	}

	@Transactional
	public int countPushes() {
		return pushDao.countPushes();
	}

	public int sendPush(Push push, Device device) {
		this.getSendService().sendPush(push, device);
		return -1;
	}

	public int sendPush(Push push, Collection<Device> devices) {
		this.getSendService().sendPush(push, devices);
		return -1;
	}

	@Override
	public int sendPushToUsers(Push push, List<String> usernames) {
		/*
		 * Create an array for the devices that the users has. Each user might
		 * have more than one device.
		 */
		List<Device> userDevices = new ArrayList<Device>();
		for (String username : usernames) {
			userDevices.addAll(deviceService.findDevicesByUsername(username));
		}
		return this.sendPush(push, userDevices);
	}

	@Transactional
	public int sendPush(List<PushDeviceTuple> tuples) {
		Iterator<PushDeviceTuple> i = tuples.iterator();
		Push p = null;
		Device d = null;
		while (i.hasNext()) {
			PushDeviceTuple pdt = i.next();
			LOG.info("Push id :   " + pdt.getPushId() + " Device id: " + pdt.getDeviceId());
			p = this.findPushById(pdt.getPushId());
			LOG.info("Push Title: " + p.getTitle());
			d = getDeviceService().findDeviceById(pdt.getDeviceId());
			LOG.info("Device Name:" + d.getDeviceName());
            /*
             * TODO, okay wait, you have to actually send the message before you can mark it as sent!
             * And only the send service implementation for the platform can tell if it was a success
             */

			getPdtService().markTupleAsSent(pdt);
		}
		return 0;
	}

	/**
	 * Sets the reference to the <code>PushDao</code>
	 * 
	 * @param dao
	 *            Reference to the <code>PushDao</code>
	 */
	public void setPushDao(PushDao dao) {
		this.pushDao = dao;
	}

	/**
	 * Gets the reference to the <code>PushDao</code>
	 * 
	 * @return
	 */
	public PushDao getPushDao() {
		return pushDao;
	}

	/**
	 * A reference to the <code>SendServiceDelegator</code> responsible for
	 * delegating push messages to appropriate implementations for different
	 * devices.
	 */
	public SendServiceDelegator getSendService() {
		return sendService;
	}

	/**
	 * Set the reference to the <code>SendServiceDelegator</code> for this
	 * ServiceImpl.
	 * 
	 * @param sendService
	 */
	public void setSendService(SendServiceDelegator sendService) {
		this.sendService = sendService;
	}

	/** Get reference to the <code>DeviceService</code> */
	public DeviceService getDeviceService() {
		return deviceService;
	}

	/**
	 * Set the reference to the <code>DeviceService</code> for this ServiceImpl.
	 * 
	 * @param deviceService
	 */
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	/** A reference to the <code>PushDeviceTupleService</code> */
	public PushDeviceTupleService getPdtService() {
		return pdtService;
	}

	/**
	 * Set the reference to the <code>PushDeviceTupleService</code> for this
	 * ServiceImpl.
	 * 
	 * @param pdtService
	 */
	public void setPdtService(PushDeviceTupleService pdtService) {
		this.pdtService = pdtService;
	}

	@Override
	public List<Push> findAllPushByPageNumber(Integer pageNum) {
		List<Push> pushList = pushDao.findAllPush();
		Collections.sort(pushList, new Comparator<Push>() {
			@Override
			public int compare(Push o1, Push o2) {
				return o1.getPostedTimestamp().compareTo(
						o2.getPostedTimestamp());
			}
		});
		Integer recordsPerPage = Integer.valueOf(kmeProperties
				.getProperty("push.history.items.per.page"));
		Integer listStartCount = pageNum * recordsPerPage - recordsPerPage;
		Integer listEndCount = pageNum * recordsPerPage - 1;
		List<Push> pushReturnList = new ArrayList<Push>();
		for (int i = listStartCount; i <= listEndCount; i++) {
			if(i==pushList.size()){
				break;
			}
			pushReturnList.add(pushList.get(i));
		}
		return pushReturnList;
	}

	/**
	 * @return the kmeProperties
	 */
	public Properties getKmeProperties() {
		return kmeProperties;
	}

	/**
	 * @param kmeProperties
	 *            the kmeProperties to set
	 */
	public void setKmeProperties(Properties kmeProperties) {
		this.kmeProperties = kmeProperties;
	}

}
