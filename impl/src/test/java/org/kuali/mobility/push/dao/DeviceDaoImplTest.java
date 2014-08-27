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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.mobility.push.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Kuali Mobility Team (mobility.collab@kuali.org)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:TestSpringBeans.xml")
public class DeviceDaoImplTest {
	private static final String FALSE_KEYWORD = "FALSE_KEYWORD";
	
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	@Qualifier("deviceDao")
	private DeviceDao dao;

	@Test
	public void testFindAllDevices() {
		List<Device> devices = getDao().findAllDevices();
		assertNotNull("FindAllDevices returned null and should not have.",devices);
		assertNotEquals("FindAllDevices returned no devices.", devices.size() , 0);
		assertEquals("Did not find the expected number of devices.", 6, devices.size());
	}

	@Test
	public void testFindDevicesByUsername() {
		List<Device> devices = getDao().findDevicesByUsername("userA");
		assertNotNull("FindAllDevicesByUsername returned null and should not have.", devices);
		assertNotEquals("FindAllDevicesByUsername returned no devices.", devices.size() , 0);
		assertEquals("FindAllDevicesByUsername failed to find 2 results for userA.", 2, devices.size());
	}

	@Test
	public void testFindDeviceByNullId() {
		Device device = getDao().findDeviceById(null);
		assertNull("Found device with id = null, and shouldn't have", device);
	}

	@Test
	public void testFindDeviceByBadId() {
		Device device = getDao().findDeviceById(new Long(9999));
		assertNull("Found device with id = 9999, and shouldn't have",device);
	}

	@Test
	public void testFindDeviceById() {
		Device device = getDao().findDeviceById(new Long(5));
		assertNotNull("Failed to find device with id = 5.", device);
		assertNull("Device found had username and should not have.", device.getUsername());
	}

	@Test
	public void testDoesDeviceHaveUsername() {
		boolean hasUsername = getDao().doesDeviceHaveUsername("deviceIdE");
		assertFalse("Device had user name and should not have.",hasUsername);

		hasUsername = getDao().doesDeviceHaveUsername("deviceIdD");
		assertTrue("Device had no user name and should have.",hasUsername);
	}

	@Test
	public void testFindDevicesWithoutUsername() {
		List<Device> devices = getDao().findDevicesWithoutUsername();
		assertFalse("findDevicesWithoutUsername returned null and should not have.", null == devices);
		assertTrue("findDevicesWithoutUsername returned no devices.", devices.size() > 0);
		assertTrue("findDevicesWithoutUsername found more than 1 device and should not have.",devices.size()==1);
	}

	@Test
	public void testFindDevicesByDeviceBadId() {
		List<Device> devices = getDao().findDevicesByDeviceId("deviceIdD");
		assertFalse("findDevicesByDeviceId returned null and should not have.", null == devices);
		assertTrue("findDevicesByDeviceId returned no devices.", devices.size() > 0);
		assertTrue("findDevicesByDeviceId found more than 1 device and should not have.",devices.size()==1);
	}


	@Test
	public void testFindDeviceByRegId() {
		Device device = getDao().findDeviceByRegId("regIdA");
		assertFalse("findDevicesByRegId returned null and should not have.", null != device);
//		assertTrue("findDevicesByRegId returned no devices.", devices.size() > 0);
//		assertTrue("findDevicesByRegId found more/less than 3 device and should not have.",devices.size()==3);
	}

	@Test
	public void testFindDevicesByDeviceId() {
		List<Device> devices = getDao().findDevicesByDeviceId("deviceIdB");
		assertFalse("findDevicesByDeviceId returned null and should not have.", null == devices);
		assertTrue("findDevicesByDeviceId returned no devices.", devices.size() > 0);
		assertTrue("findDevicesByDeviceId found more than 1 device and should not have.",devices.size()==1);
	}

	@Test
	public void testFindDevicesByKeyword() {
		List<Device> devices = getDao().findDevicesByKeyword("nameA");
		assertTrue("findDevicesByKeyword failed to find devices for keyword 'nameA'.",devices.size()==1);
		devices = getDao().findDevicesByKeyword(FALSE_KEYWORD);
		assertTrue("findDevicesByKeyword failed to find devices for keyword 'nameA'.",devices.size()==0);

	}
	
	@Test
	public void testFindDeviceByDeviceIdWithNullInput() {
		Device device = getDao().findDeviceByDeviceId(null);
		assertTrue("Found device with id = null.",null == device);
	}

	@Test
	public void testFindDeviceByDeviceIdWithBadId() {
		Device device = getDao().findDeviceByDeviceId("abcdefg");
		assertTrue("Found device with id = abcdefg.",null == device);
	}

	@Test
	public void testFindDeviceByDeviceId() {
		Device device = getDao().findDeviceByDeviceId("deviceIdA");
		assertTrue("Found device with id = null.",null != device);
	}
		
	@Test
	public void testSaveDevice() {
		Device device = new Device();
		device.setDeviceId("deviceIdZ");
		device.setType("typeA");
		device.setUsername(null);
		device.setRegId("regIdC");
		try {
			getDao().saveDevice(device);
		} catch(Exception e) {
			fail("Exception thrown when saveDevice tries to insert with null primary key.");
		}
		assertFalse("Device saved without id and should not have.",null==device.getId());
	}

	@Test
	@DirtiesContext
	public void testSaveNullDevice() {
		try {
			getDao().saveDevice(null);
		} catch(Exception e) {
			fail("Exception thrown when saveDevice tries to save a null object.");
		}
	}

	@Test
	@DirtiesContext
	public void testSaveDeviceWithUpdate() {
		Device device = getDao().findDeviceByDeviceId("deviceIdA");
		assertTrue("Failed to find deviceIdA", device!= null);
		Calendar cal = Calendar.getInstance();
		device.setPostedTimestamp(new Timestamp(cal.getTimeInMillis()));
		Long originalId = device.getId();
		getDao().saveDevice(device);
		assertTrue("Device id changed with update and should not have.",originalId.compareTo(device.getId())==0);
	}

	@Test
	@DirtiesContext
	public void testRegisterDevice() {
		Device device = new Device();
		device.setDeviceId("deviceIdZ");
		device.setType("typeA");
		device.setUsername(null);
		device.setRegId("regIdC");
		try {
			getDao().saveDevice(device);
		} catch(Exception e) {
			fail("Exception thrown when saveDevice tries to insert with null primary key.");
		}
		assertFalse("Device saved without id and should not have.",null==device.getId());
	}

	@Test
	@DirtiesContext
	public void testRegisterNullDevice() {
		try {
			getDao().saveDevice(null);
		} catch(Exception e) {
			fail("Exception thrown when saveDevice tries to save a null object.");
		}
	}

	@Test
	@DirtiesContext
	public void testRegisterDeviceWithUpdate() {
		Device device = getDao().findDeviceByDeviceId("deviceIdA");
		assertTrue("Failed to find deviceIdA", device!= null);
		Calendar cal = Calendar.getInstance();
		device.setPostedTimestamp(new Timestamp(cal.getTimeInMillis()));
		Long originalId = device.getId();
		getDao().saveDevice(device);
		assertTrue("Device id changed with update and should not have.",originalId.compareTo(device.getId())==0);
	}
	
	@Test
	@DirtiesContext
	public void testRemoveNullDevice() {
		boolean didRemove = getDao().removeDevice(null);
		assertFalse("Removed a device and should not have.",didRemove);
	}

	@Test
	@DirtiesContext
	public void testRemoveDeviceNotInDatabase() {
		Device device = new Device();
		boolean didRemove = getDao().removeDevice(device);
		assertFalse("Removed a device that isn't in the database.",didRemove);
	}

	@Test
	@DirtiesContext
	public void testRemoveDevice() {
		Device device = getDao().findDeviceByDeviceId("deviceIdY");
		assertTrue("Failed to find deviceIdY", device!= null);
		boolean didRemove = getDao().removeDevice(device);
		assertTrue("Failed to remove a device when we should have.",didRemove);
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
