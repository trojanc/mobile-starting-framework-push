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
import org.kuali.mobility.push.entity.PushMessage;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Implementation of the <code>PushMessageDao</code>
 * 
 * @since 2.4.0
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 */
@Repository("pushMessageDao")
public class PushMessageDaoImpl implements PushMessageDao {


	/** A reference to a logger for this service */
	private static final Logger LOG = Logger.getLogger(PushMessageDaoImpl.class);
	
	/** A reference to the <code>EntityManger</code> */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Creates new instance of the <code>PushMessageDaoImpl</code>
	 */
	public PushMessageDaoImpl(){}
	
	/* (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushMessageDao#findAllPushMessagesByLanguage(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<PushMessage> findAllPushMessagesByLanguage(String language) {
		Query query = entityManager.createNamedQuery("PushMessage.findByLanguage");
		query.setParameter("language", language);
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushMessageDao#findPushMessageById(java.lang.Long)
	 */
	@Override
	public PushMessage findPushMessageById(Long id) {
		Query query = entityManager.createNamedQuery("PushMessage.findById");
		query.setParameter("id", id);
		PushMessage result;
		try{
				result = (PushMessage)query.getSingleResult();
		}catch(Exception e){
			result = null;					
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushMessageDao#savePushMessage(org.kuali.mobility.push.entity.PushMessage)
	 */
	@Transactional
	public void savePushMessage(PushMessage pushMessage) {
		if(pushMessage == null){
			return;
		}
		if(pushMessage.getMessageId() == null){
			entityManager.persist(pushMessage);
			LOG.info("Persist PushMessage");
		}else{
			entityManager.merge(pushMessage);
			LOG.info("Merge PushMessage");
		}
		LOG.info(pushMessage);
	}

	/* (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.PushMessageDao#removePushMessage(org.kuali.mobility.push.entity.PushMessage)
	 */
	@Transactional
	public boolean removePushMessage(PushMessage pushMessage) {
		boolean result = true;
		if(pushMessage == null){
			result = false;
		} else if(pushMessage.getMessageId() == null) {
			result = false;
		} else {
			try{
				getEntityManager().remove(getEntityManager().contains(pushMessage)?pushMessage:getEntityManager().merge(pushMessage));
			}catch(Exception e){
				LOG.info("Exception while trying to remove push notification.", e);
				result = false;
			}
		}
		return result;
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

}
