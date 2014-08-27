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
import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.PushDeviceTuple;
import org.kuali.mobility.push.service.PushDeviceTupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the Push Data Access Object for <code>Push</code> objects
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
@Repository("pushDao")
public class PushDaoImpl implements PushDao {

	/** A reference to a logger */
	private static final Logger LOG = Logger.getLogger(PushDaoImpl.class);

	/** A reference to the <code>EntityManger</code> */
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PushDeviceTupleService pdtService;
	
	/**
	 * Creates a new instance of a <code>PushDaoImpl</code>
	 */
	public PushDaoImpl(){}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDao#findPushById(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public Push findPushById(Long id){
		Query query = entityManager.createNamedQuery("Push.find");
		query.setParameter("pushId", id);
		Push result;
		try{
			result = (Push) query.getSingleResult();
		}catch(Exception e){
			LOG.info("Exception: " + e.getMessage());
			result = null;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDao#findAllPush()
	 */
	@SuppressWarnings("unchecked")
	public List<Push> findAllPush(){
		Query query = entityManager.createNamedQuery("Push.findAll");
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDao#countPushes()
	 */
	@SuppressWarnings("unchecked")
	public int countPushes(){
		Query query = entityManager.createNamedQuery("Push.countAll");
		return ((Long)query.getSingleResult()).intValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDao#findUnsentPushTuples()
	 */
	@SuppressWarnings("unchecked")
	public List<PushDeviceTuple> findUnsentPushTuples(){
		Query query = entityManager.createNamedQuery("PushDeviceTuple.findUnsent");
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDao#savePush(org.kuali.mobility.push.entity.Push)
	 */
	@Transactional
	public Push savePush(Push push){
		if(push == null){
			return null;
		}
		if(push.getPushId() == null){
			entityManager.persist(push);
            return push;
		}else{
			return entityManager.merge(push);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDao#removePush(org.kuali.mobility.push.entity.Push)
	 */
	@Transactional
	public boolean removePush(Push push){
		boolean result = true;
		if(push == null){
			result = false;
		} else if(push.getPushId() == null) {
			result = false;
		} else {
			
			int removedTuples = pdtService.removeTuplesForPush(push);
			if(removedTuples > 0){
				LOG.info("Removed " + removedTuples + " tuples associated with Push notifications.");
			}else{
				LOG.info("No tuples associated with Push notifications removed.");				
			}
			
			try{
				getEntityManager().remove(getEntityManager().contains(push)?push:getEntityManager().merge(push));
			}catch(Exception e){
				LOG.info("Exception while trying to remove push notification.", e);
				result = false;
			}
		}
		return result;
	}	
	

	@Transactional
	public void savePush(Push push, Collection<Device> devices){
		if(push == null){
			return;
		}
		if(push.getPushId() == null){
			entityManager.persist(push);
		}else{
			entityManager.merge(push);
		}
		Iterator<Device> i = devices.iterator();
		while(i.hasNext()){
			Device d = i.next();
            // Somehow the iterator gives a null even though there is no null entries
            if(d == null) continue;
			PushDeviceTuple pdt = new PushDeviceTuple();
			pdt.setPushId(push.getPushId());
			pdt.setDeviceId(d.getId());
			pdt.setPostedTimestamp(new Timestamp(System.currentTimeMillis()));
			pdt.setStatus(PushDeviceTuple.STATUS_PENDING);
			entityManager.persist(pdt);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDao#findDevicesForPush(org.kuali.mobility.push.entity.Push)
	 */
	@Transactional
	public List<Device> findDevicesForPush(Push push){
		Query query = entityManager.createNamedQuery("PushDeviceTuple.findPushDevices");
		query.setParameter("pushId", push.getPushId());
		return query.getResultList();
	}

	@Transactional
	public void savePushDeviceTuples(List<PushDeviceTuple> tuples){
		if(tuples == null){
			return;
		}
		Iterator<PushDeviceTuple> i = tuples.iterator();
		while(i.hasNext()){
			PushDeviceTuple pdt = i.next();
			if(pdt.getId() == null){
				entityManager.persist(pdt);
			}else{
				entityManager.merge(pdt);
			}
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * @return the pdtService
	 */
	public PushDeviceTupleService getPdtService() {
		return pdtService;
	}

	/**
	 * @param pdtService the pdtService to set
	 */
	public void setPdtService(PushDeviceTupleService pdtService) {
		this.pdtService = pdtService;
	}


}
