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

import org.kuali.mobility.push.entity.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Implementation of the Sender Data Access Object.
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
@Repository("senderDao")
public class SenderDaoImpl implements SenderDao{

	/** A reference to a logger */
	private static final Logger LOG = LoggerFactory.getLogger(DeviceDaoImpl.class);
	
	@PersistenceContext
    private EntityManager entityManager;
	
	/**
	 * Creates new instance of the <code>DeviceDaoImpl</code>.
	 */
	public SenderDaoImpl(){}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.SenderDao#findAllSenders()
	 */
	public List<Sender> findAllSenders(){
        Query query = entityManager.createQuery("select s from Sender s order by s.name");
        return query.getResultList();	
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.SenderDao#findAllUnhiddenSenders()
	 */
	public List<Sender> findAllUnhiddenSenders(){
        Query query = entityManager.createQuery("select s from Sender s where s.hidden = 0 order by s.name");
        return query.getResultList();	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.SenderDao#findSenderById(long)
	 */
	public Sender findSenderById(long id){
        Query query = entityManager.createQuery("select s from Sender s where s.id = :id");
        query.setParameter("id", id);        
        Sender s;
        try{
        	s = (Sender)query.getSingleResult();
        }catch(NoResultException e){
        	LOG.info("Sender with id " + id + " was not found or was invalid.");
        	return null;
        }
        return s;	}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.SenderDao#findSenderByName(java.lang.String)
	 */
	public Sender findSenderByName(String name){
        Query query = entityManager.createQuery("select s from Sender s where s.name = :name");
        query.setParameter("name", name);        
        Sender s;
        try{
        	s = (Sender)query.getSingleResult();
        }catch(NoResultException e){
        	LOG.info("Sender with name " + name + " was not found or was invalid.");
        	return null;
        }
        return s;	
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.SenderDao#findSenderBySenderKey(java.lang.String)
	 */
	public Sender findSenderBySenderKey(String senderKey){
        Query query = entityManager.createQuery("select s from Sender s where s.senderKey = :senderKey");
        query.setParameter("senderKey", senderKey);  
        Sender s;
        try{
        	s = (Sender)query.getSingleResult();
        }catch(NoResultException e){
        	LOG.info("Sender with senderKey " + senderKey + " was not found or was invalid.");
        	return null;
        }
        return s;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.SenderDao#findSenderByShortName(java.lang.String)
	 */
	public Sender findSenderByShortName(String shortName){
        Query query = entityManager.createQuery("select s from Sender s where s.shortName = :shortName");
        query.setParameter("shortName", shortName); 
        Sender s;
        try{
        	s = (Sender)query.getSingleResult();
        }catch(NoResultException e){
        	LOG.info("Sender with Shortname " + shortName + " was not found or was invalid.");
        	return null;
        }
        return s;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.SenderDao#isValidSenderKey(java.lang.String)
	 */
	public 	boolean isValidSenderKey(String senderKey){
        Query query = entityManager.createQuery("select s from Sender s where s.senderKey = :senderKey");
        query.setParameter("senderKey", senderKey);        
        try{
        	query.getSingleResult();
        }catch(Exception e){
        	LOG.info("SenderKey " + senderKey + " was not found or was invalid.");
        	return false;
        }
        return true;        
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.SenderDao#saveSender(org.kuali.mobility.push.entity.Sender)
	 */
	public void saveSender(Sender sender){
		LOG.info("SenderDaoImpl.saveSender");
		if(sender == null){
    		return;
    	}
		LOG.info("SenderDaoImpl.saveSender : id = " + sender.getId());
		if(sender.getId() == null){
    		LOG.info("SenderDaoImpl.saveSender : persist");
    		entityManager.persist(sender);
    	}else{
    		LOG.info("SenderDaoImpl.saveSender : merge");
    		entityManager.merge(sender);
    	}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.dao.SenderDao#removeSender(org.kuali.mobility.push.entity.Sender)
	 */
	@Transactional
	public boolean removeSender(Long senderIdr){
    	boolean result = true;
    	if(senderIdr == null){
    		return false;
    	}
    	else{
    		try{
    			Sender s = entityManager.find(Sender.class, senderIdr);
    			entityManager.remove(s);
    		}catch(Exception e){
    			LOG.info("Exception Caught: " + e.getMessage());
    			result = false;
    		}
    	}
    	return result;
	}
	
	/** A reference to the <code>EntityManager</code> */ /**
	 * Returns the reference to the <code>EntityManager</code>
	 * @return The reference to the <code>EntityManager</code>
	 */
    public EntityManager getEntityManager() {
        return entityManager;
    }

	/**
	 * Sets the reference to the <code>EntityManager</code>
	 * @param entityManager The reference to the <code>EntityManager</code>
	 */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
