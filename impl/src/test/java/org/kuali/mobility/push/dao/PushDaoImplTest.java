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

package org.kuali.mobility.push.dao;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.PushDeviceTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Kuali Mobility Team (mobility.collab@kuali.org)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:TestSpringBeans.xml")
public class PushDaoImplTest {
	private static final Logger LOG = Logger.getLogger(PushDaoImplTest.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	@Qualifier("pushDao")
	private PushDao dao;

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
	public void testRemovePush() {
		Push push = getDao().findPushById(new Long(2));
		boolean removed = getDao().removePush(push);
		assertTrue("Should have removed a push, didn't.",removed);
	}
	
	@Test
	public void testRemovePushWithNullPush() {
		boolean removed = getDao().removePush(null);
		assertFalse("Removing a null push should've return False.",removed);
	}
	
	@Test
	public void testRemovePushWithNullPushId() {
		Push push = getDao().findPushById(new Long(2));
		push.setPushId(null);
		boolean removed = getDao().removePush(push);
		assertFalse("Removing a push with a null PushID should've return False.",removed);
	}
	
	@Test
	public void testFindAllPush() {
		List<Push> pushes = getDao().findAllPush();
		assertFalse("findAllPush returned null.",null==pushes);
		LOG.debug("Found "+pushes.size()+" push notification.");
		assertTrue("findAllPush returned incorrect number of pre-loaded pushes.",pushes.size()==6);
	}

	@Test
	public void testCountPushes() {
		int count = getDao().countPushes();
		assertTrue("countPushes Failed to find proper count",count==5);
	}

	@Test
	public void testFindPushByNullId() {
		Push push = getDao().findPushById(null);
		assertTrue("findPushById failed to return information for null push id.",push==null);
	}

	@Test
	public void testFindPushById() {
		Push push = getDao().findPushById(new Long(3));
		assertTrue("findPushById failed to return information for push id = 3.", push != null);
		assertTrue("findPushById returned the wrong push notification.","testC".equals(push.getTitle()));
	}

	@Test
	public void testFindDevicesForPush() {
		Push push = getDao().findPushById(new Long(3));
		List<Device> devices = getDao().findDevicesForPush(push);
		assertFalse("Received null device list for find devices for push id 3",devices==null);
		assertFalse("Received empty device list for find devices for push id 3",devices.isEmpty());
	}

	@Test
	public void testFindUnsentPushTuples(){
		List<PushDeviceTuple> tuples = getDao().findUnsentPushTuples();
		assertFalse("Returned null tuples list, should not have been null", null==tuples);
		assertFalse("Returned an empty tuples list, should not have been empty.", tuples.isEmpty());
		assertTrue("Returned the wrong number of tuples, " + tuples.size() + " should have been 5", tuples.size() == 5);
		
	}
	
	public PushDao getDao() {
		return dao;
	}

	public void setDao(PushDao dao) {
		this.dao = dao;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
