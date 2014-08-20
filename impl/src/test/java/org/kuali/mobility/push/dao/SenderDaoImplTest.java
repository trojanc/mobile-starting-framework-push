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
import org.kuali.mobility.push.entity.Sender;
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
public class SenderDaoImplTest {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * There seems to be a pre-existing senderDaoImpl bean in Test, generated where, I have no idea. 
	 * We use this preexisting bean instead of of adding one to TestSpringBeans.xml.
	 */
	@Autowired
	private SenderDao dao;

	@Test
	public void testFindAllSenders() {
		List<Sender> senders = getDao().findAllSenders();
		assertFalse("FindAllSenders returned null and should not have.",null==senders);
		assertTrue("FindAllSenders returned no devices.", senders.size() > 0);
		assertTrue("FindAllSenders failed to find the pre-loaded data.",senders.size()==3);
	}

	@Test
	public void testFindAllUnhiddenSenders() {
		List<Sender> senders = getDao().findAllUnhiddenSenders();
		assertFalse("FindAllUnhiddenSenders returned null and should not have.",null==senders);
		assertTrue("FindAllUnhiddenSenders returned no devices.", senders.size() > 0);
		assertTrue("FindAllUnhiddenSenders failed to find the pre-loaded data.",senders.size()==1);
	}

	@Test
	public void testFindSenderById() {
		Sender senders = getDao().findSenderById(1L);
		assertFalse("FindSenderById returned null and should not have.",null==senders);
	}

	@Test
	public void testFindSenderByWrongId() {
		Sender senders = getDao().findSenderById(123L);
		assertTrue("FindSenderById shouldn't return Sender, but did.",null==senders);
	}

	@Test
	public void testFindSenderByName(){
//		Sender senders = getDao().findSenderByName("KME_PUSH");
//		assertFalse("FindSenderByName returned null and should not have.",null==senders);		
	}
	
	@Test
	public void testFindSenderByWrongName(){
		Sender senders = getDao().findSenderByName("FALSE_KME_PUSH");
		assertTrue("FindSenderByWrongName should have returned null, but didn't.",null==senders);				
	}
	
	@Test
	public void testFindSenderBySenderKey(){
		Sender senders = getDao().findSenderBySenderKey("3AbHRDjirFn2hvii4Pq3");
		assertFalse("FindSenderById returned null and should not have.",null==senders);				
	}

	@Test
	public void testFindSenderByWrongSenderKey(){
		Sender senders = getDao().findSenderBySenderKey("FALSE_SENDER_KEY");
		assertTrue("FindSenderById should have returned null, but didn't.",null==senders);				
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
	public SenderDao getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(SenderDao dao) {
		this.dao = dao;
	}
	
	
}
