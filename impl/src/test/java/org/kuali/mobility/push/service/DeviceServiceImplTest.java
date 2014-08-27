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

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.mobility.push.dao.DeviceDao;
import org.kuali.mobility.push.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Kuali Mobility Team (mobility.collab@kuali.org)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:/TestSpringBeans.xml")
public class DeviceServiceImplTest {

	private static final Logger LOG = Logger.getLogger(DeviceServiceImplTest.class);

	
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	@Qualifier("deviceService")
	private DeviceService service;

	@Autowired
	@Qualifier("deviceDao")
	private DeviceDao dao;

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void preTest() {
	}

	@Test
	public void testFindAllDevices() {
		List<Device> devices = getService().findAllDevices();
		assertNotNull("findAllDevices failed to find preloaded devices.",devices);
		assertEquals("findAllDevices failed to find 5 devices.",6, devices.size());
	}

	@Test
	public void testDoesDeviceHaveUsername() {
		boolean hasUsername = getService().doesDeviceHaveUsername("deviceIdC");
		assertTrue("doesDeviceHaveUsername returned false and should not have.",hasUsername);
	}

	@Test
	public void testFindDevicesByUsername() {
		List<Device> devices = getService().findDevicesByUsername("userA");
        assertNotNull("findDevicesByUsername failed to find any devices for userA.",devices);
        assertEquals("findDevicesByUsername failed to find 2 devices for userA.",2, devices.size());
	}

	@Test
	public void testFindDevicesWithoutUsername() {
		List<Device> devices = getService().findDevicesWithoutUsername();
        assertNotNull("findDevicesWithoutUsername failed to find devices without users.",devices);
        assertEquals("findDevicesWithoutUsername failed to find 1 device without a user.",1, devices.size());
	}

	@Test
	public void testFindDeviceByRegId() {
		Device device = getService().findDeviceByRegId("regIdB");
        assertNull("findDevicesByRegId failed to find any devices for regIdB.",device);
//		assertTrue("findDevicesByRegId failed to find 2 devices for regIdB.",device.size()==2);
	}

	@Test
	public void testFindDevicesByDeviceId() {
		List<Device> devices = getService().findDevicesByDeviceId("deviceIdC");
		assertFalse("findDevicesByDeviceId failed to find any devices for deviceIdC.",null==devices);
		assertTrue("findDevicesByDeviceId failed to find 1 device for deviceIdC.",devices.size()==1);
	}

	@Test
	public void testFindDeviceByDeviceId() {
		Device device = getService().findDeviceByDeviceId("deviceIdC");
		assertFalse("findDevicesByDeviceId failed to find device for deviceIdC.",null==device);
	}

	
	private static final String FALSE_KEYWORD = "FALSE_KEYWORD";
	private static final String KEYWORD_DEVICENAME_IOS_TYPE = "nameA :i";
	private static final String KEYWORD_IOS_TYPE = ":i";
	private static final String FALSE_KEYWORD_DEVICENAME_IOS_TYPE = "nameD :i";
	private static final String KEYWORD_DEVICENAME_ANDROID_TYPE = "nameA :a";
	private static final String FALSE_KEYWORD_DEVICENAME_ANDROID_TYPE = "nameD :a";
		
	@Test
	public void testFindDevicesByKeyword() {
		List<Device> devices = getService().findDevicesByKeyword(KEYWORD_DEVICENAME_IOS_TYPE);
		assertTrue("findDevicesByKeyword failed to find devices for keyword 'nameA :i'.",devices.size()==1);
		assertFalse("findDevicesByKeyword failed to find any devices for keyword 'nameA :i'.",null==devices);
		assertFalse("findDevicesByKeyword failed to find any devices for keyword 'nameA :i'.",devices.isEmpty());

		devices = getService().findDevicesByKeyword(FALSE_KEYWORD_DEVICENAME_IOS_TYPE);
		assertTrue("findDevicesByKeyword found devices for keyword 'FALSE_KEYWORD', should have found none.",devices.size()==0);
		assertTrue("findDevicesByKeyword failed to find devices for keyword 'FALSE_KEYWORD'.",devices.isEmpty());	

		devices = getService().findDevicesByKeyword(KEYWORD_IOS_TYPE);
		assertTrue("findDevicesByKeyword failed to find the right number of devices for keyword ':i'.",devices.size()==3);
		assertFalse("findDevicesByKeyword failed to find devices for keyword ':i'.",devices.isEmpty());	

	}

	
	
	@Test
	public void testCountDevices() {
		Long count = getService().countDevices();
		assertEquals("Failed to find proper count of devices.",6, count.intValue());
	}

	@Test
	public void testCountDevicesWithoutUsername() {
		Long count = getService().countDevicesWithoutUsername();
        assertEquals("Found more than one device without a username.",1L, count.longValue());
	}

	@Test
	public void testCountDevicesByType() {
		Long count = getService().countDevices("Android");
        assertEquals("Found incorrect number of Android devices.",2L, count.longValue());
	}

	@Test
	public void testFindAllDevicesByType() {
		List<Device> devices = getService().findAllDevices("iOS");
		assertFalse("findAllDevices Failed to find any devices of iOS.",null==devices);
		assertFalse("findAllDevices Failed to find any devices of iOS.",devices.isEmpty());
		assertTrue("findAllDevices Incorrect number of iOS devices found.",devices.size()==3);
	}

	public DeviceService getService() {
		return service;
	}

	public void setService(DeviceService service) {
		this.service = service;
	}

	public DeviceDao getDao() {
		return dao;
	}

	public void setDao(DeviceDao dao) {
		this.dao = dao;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
