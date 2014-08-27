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
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.PushDeviceTuple;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Kuali Mobility Team (mobility.collab@kuali.org)
 * @since 2.1.0
 */
@Repository("pushDeviceTupleDao")
public class PushDeviceTupleDaoImpl implements PushDeviceTupleDao {

	/** A reference to a logger */
	private static final Logger LOG = Logger.getLogger(DeviceDaoImpl.class);

	/**
	 * A reference to the <code>EntityManager</code>
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Creates a new instance of a <code>PushDeviceTupleDaoImpl</code>
	 */
	public PushDeviceTupleDaoImpl(){
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDeviceTupleDao#markTupleAsSent(org.kuali.mobility.push.entity.PushDeviceTuple)
	 */
	public void markTupleAsSent(PushDeviceTuple tuple){
		tuple.setSent();
		this.saveTuple(tuple);
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDeviceTupleDao#saveTuple(org.kuali.mobility.push.entity.PushDeviceTuple)
	 */
	@Transactional
	public Long saveTuple(PushDeviceTuple tuple){
		Long id = null;
		if(tuple != null){
			if(tuple.getId() == null){
				entityManager.persist(tuple);
			}else{
				entityManager.merge(tuple);
			}
			id = tuple.getId();
		}
		return id;
	}


	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDeviceTupleDao#findUnsentTuples()
	 */
	@SuppressWarnings("unchecked")
	public List<PushDeviceTuple> findUnsentTuples(){
		Query query = entityManager.createNamedQuery("PushDeviceTuple.findUnsent");
		return query.getResultList();
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDeviceTupleDao#findTuplesForPush(org.kuali.mobility.push.entity.Push)
	 */
	@SuppressWarnings("unchecked")
	public List<PushDeviceTuple> findTuplesForPush(Push push){
		Query query = entityManager.createNamedQuery("PushDeviceTuple.findTuplesForPush");
		query.setParameter("pushId", push.getPushId());
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushDeviceTupleDao#findTuplesForPush(org.kuali.mobility.push.entity.Push)
	 */
	@Transactional
	public int removeTuplesForPush(Push push){
		LOG.info("PDTDI.removeTuplesForPush");
		int result = 0;
		if(null == push){
			return -1;
		}
		if(push.getPushId() != null){
			String hql = "DELETE PushDeviceTuple t WHERE t.pushId = :pushId";
			result = entityManager.createQuery(hql).setParameter("pushId", push.getPushId()).executeUpdate();
		}else{
			result = -1;
		}
		return result;
	}
	
	/**
	 * Return the <code>EntityManager</code> for this DaoImpl..
	 * @return
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Set the <code>EntityMAnager</code> for this DaoImpl.
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
