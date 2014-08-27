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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.PushDeviceTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Kuali Mobility Team (mobility.collab@kuali.org)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:TestSpringBeans.xml")
public class PushDeviceTupleDaoImplTest {

	private static final Logger LOG = Logger.getLogger(PushDeviceTupleDaoImplTest.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PushDeviceTupleDao dao;

	@Test
    @DirtiesContext
	public void countUnsentTuples() {
		Query query = getEntityManager().createNamedQuery("PushDeviceTuple.countUnsent");
		Long count = (Long)query.getSingleResult();
		LOG.debug("There are "+count.toString()+" unsent tuples in the database.");
		assertTrue("Count was null and should never be.",count!=null);
	}

	@Test
    @DirtiesContext
	public void testFindUnsentTuples() {
		List<? extends PushDeviceTuple> tuples = getDao().findUnsentTuples();
		assertNotNull("findUnsentTuples returned null.", tuples);
		assertFalse("findUnsentTuples returned an empty list.",tuples.isEmpty());
		assertEquals("Should have found 4 unsent tuples and did not.", 5, tuples.size());
	}

	@Test
    @DirtiesContext
	public void testSaveTupleWithNull() {
		Long id = getDao().saveTuple(null);
		assertTrue("Saved a null object and shouldn't have.",id == null);
	}

	@Test
    @DirtiesContext
	public void testSaveTuple() {
		PushDeviceTuple tuple = new PushDeviceTuple();
		tuple.setDeviceId(new Long(3));
		tuple.setPushId(new Long(6));
		Calendar cal = Calendar.getInstance();
		tuple.setPostedTimestamp(new Timestamp(cal.getTimeInMillis()));
		tuple.setStatus(0);
		Long id = getDao().saveTuple(tuple);
		LOG.debug("========== Saved tuple with id "+id+" =========");
		assertNotNull("Tuple failed to save.", id);
	}

	@Test
    @DirtiesContext
	public void testUpdateTuple() {
		List<PushDeviceTuple> tuples = getDao().findUnsentTuples();
		PushDeviceTuple tuple = tuples.get(2);
		Calendar cal = Calendar.getInstance();
		tuple.setPostedTimestamp(new Timestamp(cal.getTimeInMillis()));
		Long oldId = tuple.getId();
		Long newId = getDao().saveTuple(tuple);
		assertTrue("Tuple was inserted, not updated.",oldId.compareTo(newId)==0);
	}

	@Test
    @DirtiesContext
	public void testFindTuplesForPush(){
		Push push = new Push();
		push.setPushId(new Long(3));
		List<PushDeviceTuple> tuples = getDao().findTuplesForPush(push);
		assertFalse("FindTuplesForPush returned null. Shouldn't have.",tuples==null);
		assertTrue("FindTuplesForPush returned " + tuples.size() + " tuples, shaould have returned 1", tuples.size() == 1);
	}
	
	@Test
    @DirtiesContext
	public void testRemoveTuplesForPush(){
		Push push = new Push();
		push.setPushId(1L);
		int didRemove = getDao().removeTuplesForPush(push);
		assertFalse("Should have removed tuples, but had an error.", didRemove<0);
		assertTrue("Should have removed 3 tuples, didn't.",didRemove==3);
	}
	
	@Test
    @DirtiesContext
	public void testRemoveTuplesForPushWithNullPush(){
		int didRemove = getDao().removeTuplesForPush(null);
		assertTrue("Should have returned that an error occured(-1).", didRemove<0);
		assertFalse("Should not have removed 3 tuples, did.",didRemove==3);		
	}
	
	@Test
    @DirtiesContext
	public void testRemoveTuplesForPushWithNullPushId(){
		Push push = new Push();
		int didRemove = getDao().removeTuplesForPush(push);
		assertTrue("Should have returned that an error occured(-1).", didRemove<0);
		assertFalse("Should not have removed 3 tuples, did.",didRemove==3);	
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public PushDeviceTupleDao getDao() {
		return dao;
	}

	public void setDao(PushDeviceTupleDao dao) {
		this.dao = dao;
	}
}
