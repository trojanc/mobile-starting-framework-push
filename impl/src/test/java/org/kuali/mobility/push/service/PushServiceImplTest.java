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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.mobility.push.dao.PushDao;
import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.PushDeviceTuple;
import org.kuali.mobility.push.service.send.SendServiceDelegator;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Kuali Mobility Team (mobility.collab@kuali.org)
 */
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class PushServiceImplTest {

	@Mock
	private PushDao dao;
	@Mock
	private DeviceService deviceService;
	@Mock
	private SendServiceDelegator delegator;
	@Mock
	private PushDeviceTupleService tupleService;
	private PushServiceImpl service;

	@Before
	public void preTest() {
		setService(new PushServiceImpl());
		getService().setPushDao(getDao());
		getService().setDeviceService(getDeviceService());
		getService().setPdtService(getTupleService());
		getService().setSendService(getDelegator());
	}

	@Test
	public void testSavePush() {
		getService().savePush(new Push());
	}

	@Test
	public void testRemovePush() {
		Push push = getService().findPushById(new Long(1));
		//boolean removed = getService().removePush(push);
	}
	
	
	@Test
	public void testSavePushWithDevices() {
		getService().savePush(new Push(),new ArrayList<Device>());
	}

	@Test
	public void testFindPushById() {
		Push p = new Push();
		p.setPushId(new Long(53));
		when(getDao().findPushById(p.getPushId())).thenReturn(p);
		Push p2 = getService().findPushById(p.getPushId());
		getService().getPushDao();
		assertTrue("Failed to find expected push object by ID.", p.equals(p2));
	}

	@Test
	public void testFindDevicesForPush() {
		Push p = new Push();
		p.setPushId(new Long(53));
		List<Device> devices = new ArrayList<Device>();
		devices.add(new Device());
		devices.add(new Device());
		when(getDao().findDevicesForPush(p)).thenReturn(devices);
		List<Device> d2 = getService().findDevicesForPush(p);
		assertTrue("Failed to find devices for push.",d2!=null && !d2.isEmpty());
		assertTrue("Failed to return the proper number of devices for push.",d2.size()==2);
	}

	@Test
	public void testFindUnsentPushTuples() {
		List<PushDeviceTuple> tuples = new ArrayList<PushDeviceTuple>();
		tuples.add(new PushDeviceTuple());
		tuples.add(new PushDeviceTuple());
		tuples.add(new PushDeviceTuple());
		when(getDao().findUnsentPushTuples()).thenReturn(tuples);
		List<PushDeviceTuple> t2 = getService().findUnsentPushTuples();
		assertTrue("Failed to find unsent tuples.",t2!=null && !t2.isEmpty());
		assertTrue("Failed to return the proper number of unsent tuples.",t2.size()==tuples.size() && t2.size()==3);
	}

	@Test
	public void testFindAllPush() {
		List<Push> pushes = new ArrayList<Push>();
		pushes.add(new Push());
		pushes.add(new Push());
		pushes.add(new Push());
		when(getDao().findAllPush()).thenReturn(pushes);
		List<Push> p2 = getService().findAllPush();
		assertTrue("Failed to find all push notifications.",p2!=null && p2.size()==pushes.size());
	}

	@Test
	public void testCountPushes() {
		List<Push> pushes = new ArrayList<Push>();
		pushes.add(new Push());
		pushes.add(new Push());
		pushes.add(new Push());
		when(getDao().countPushes()).thenReturn(pushes.size());
		int count = getService().countPushes();
		assertTrue("Found incorrect number of push notifications.",count==pushes.size());
	}

	@Test
	public void testSendPush() {
		getService().sendPush(new Push(), new Device());
	}

	@Test
	public void testSendPushToMultipleDevices() {
		getService().sendPush(new Push(),new ArrayList<Device>());
	}

	@Test
	public void testSendPushWithTuples() {
		List<PushDeviceTuple> tuples = new ArrayList<PushDeviceTuple>();
		tuples.add(new PushDeviceTuple());
		tuples.get(0).setPushId(new Long(1));
		tuples.get(0).setDeviceId(new Long(9));
		tuples.add(new PushDeviceTuple());
		tuples.get(1).setPushId(new Long(2));
		tuples.get(1).setDeviceId(new Long(8));
		tuples.add(new PushDeviceTuple());
		tuples.get(2).setPushId(new Long(3));
		tuples.get(2).setDeviceId(new Long(7));
		Push p = new Push();
		p.setTitle("Faux Title");
		Device d = new Device();
		d.setDeviceName("Faux Device");
		when(getDao().findPushById(any(Long.class))).thenReturn(p);
		when(getDeviceService().findDeviceById(any(Long.class))).thenReturn(d);
		getService().sendPush(tuples);
	}

	public PushDao getDao() {
		return dao;
	}

	public void setDao(PushDao dao) {
		this.dao = dao;
	}

	public PushServiceImpl getService() {
		return service;
	}

	public void setService(PushServiceImpl service) {
		this.service = service;
	}

	public DeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	public SendServiceDelegator getDelegator() {
		return delegator;
	}

	public void setDelegator(SendServiceDelegator delegator) {
		this.delegator = delegator;
	}

	public PushDeviceTupleService getTupleService() {
		return tupleService;
	}

	public void setTupleService(PushDeviceTupleService tupleService) {
		this.tupleService = tupleService;
	}
}
