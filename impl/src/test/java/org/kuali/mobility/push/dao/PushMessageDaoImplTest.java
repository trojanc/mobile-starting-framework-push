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

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.mobility.push.entity.PushMessage;
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
public class PushMessageDaoImplTest {

	private static final Logger LOG = Logger.getLogger(PushMessageDaoImplTest.class);
	
	private static final String EN = "en";
	
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	@Qualifier("pushMessageDao")
	private PushMessageDao dao;

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
	public void testNothing(){}
	
	@Test
	public void testFindAllPushMessagesByLanguage(){
		List<PushMessage> pms = getDao().findAllPushMessagesByLanguage(EN);
		assertFalse("FindAllPushMessagesByLanguage returned null and should not have.",null==pms);
		assertTrue("FindAllPushMessagesByLanguage returned no devices.", pms.size() > 0);
		assertTrue("FindAllPushMessagesByLanguage failed to find the pre-loaded data.",pms.size()==2 );
	}

	@Test
	public void testFindAllPushMessagesById(){
		PushMessage pm = getDao().findPushMessageById(1L);
		assertFalse("FindAllPushMessagesByLanguage returned null and should not have.",null==pm);
	}
	
	@Test
	public void testFindAllPushMessagesByNullId() {
		PushMessage pm = getDao().findPushMessageById(null);
		assertTrue("Found device with id = null.",null == pm);
	}
	
	@Test
	public void testSavePushMessage(){
//		PushMessage pm = new PushMessage();
//		pm.setLanguage("en");
//		pm.setTitle("title4");
//		pm.setMessage("message4");
//
//		getDao().savePushMessage(pm);
//
//		assertFalse("PushMessage saved without id and should not have.", null==pm.getMessageId());
	}
	
	@Test
	public void testRemovePushMessage(){
//		PushMessage pm = getDao().findPushMessageById(1L);
//		assertTrue("Failed to find PushMessage with id: 1",null != pm);
//		boolean didRemove = getDao().removePushMessage(pm);
//		assertTrue("Failed to remove PushMessage",didRemove);
	}
	
	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * @return the dao
	 */
	public PushMessageDao getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(PushMessageDao dao) {
		this.dao = dao;
	}
	
}
