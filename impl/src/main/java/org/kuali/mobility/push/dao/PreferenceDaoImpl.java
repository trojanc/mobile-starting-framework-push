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

import org.kuali.mobility.push.entity.Preference;
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
 * Implementation of the Preference Data Access Object.
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
@Repository("preferenceDao")
public class PreferenceDaoImpl implements PreferenceDao{

	/** A reference to a logger */
	private static final Logger LOG = LoggerFactory.getLogger(PreferenceDaoImpl.class);
	

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * Creates new instance of the <code>PreferenceDaoImpl</code>.
	 */
	public PreferenceDaoImpl(){}

	public List<Preference> findPreferencesByUsername(String username){
		Query query = entityManager.createQuery("select p from Preference p where p.username = :username");
		query.setParameter("username", username);
		return query.getResultList();
	}

	public Preference findPreference(String username, String shortName){
		Query query = getEntityManager().createNamedQuery("Preference.findPreferenceWithUsernameAndShortname");
		query.setParameter("username", username);
		query.setParameter("shortName", shortName);
		Preference result;
		try{
			result = (Preference) query.getSingleResult();
		}catch(Exception e){
			LOG.info("No Preference Found: " + username + ", " + shortName);
			result = null;
		}	
		return result;
	}

	@Override
	public Preference findPreference(String username, Long senderId) {
		try{
			Query query = getEntityManager().createNamedQuery("Preference.findPreferenceWithUsernameAndSenderId");
			query.setParameter("username", username);
			query.setParameter("pushSenderID", senderId);
			return (Preference)query.getSingleResult();
		}
		catch(NoResultException nre){
			// If there is no result we return nothing
			return null;
		}
	}


	public Preference findPreference(Long id){
		Query query = entityManager.createQuery("select p from Preference p where p.id = :id");
		query.setParameter("id", id);
		Preference result;
		try{
			result = (Preference) query.getSingleResult();
		}catch(Exception e){
			LOG.info("Exception: " + e.getMessage());
			result = null;
		}	
		return result;
		
	}

	@Transactional
	public Preference savePreference(Preference preference){
		if(preference == null){
			return null;
		}
		if(preference.getId() == null){
			entityManager.persist(preference);
		}else{
			preference = entityManager.merge(preference);
		}
		return preference;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public boolean removePreference(Long id){
		LOG.info("PreferenceDaoImpl.removePreference");
		Preference p = this.findPreference(id);
		boolean result = true;
		if(p!= null){
			LOG.info("PreferenceDaoImpl.removePreference ! null");
			try{
				entityManager.remove(p);
				LOG.info("PreferenceDaoImpl.removePreference : remove(p)");
			}catch(Exception e){
				LOG.info("Exception Caught: " + e.getMessage());
				result = false;
			}
		}else{
			result = false;
		}
		LOG.info("PreferenceDaoImpl.removePreference " + (result ? "true":"false") );
		return result;
	}

	@Override
	public List<String> findUsersThatAllowedSender(String senderKey) {

		// Get all the usernames of users that blocked senders, because we only save users that opted out
		Query query = getEntityManager().createNamedQuery("Preference.findUsersThatBlockedSenderKey");
		query.setParameter("senderKey", senderKey);
		List<String> blockedUsernames = (List<String>)query.getResultList();

		// Find all the users that has devices
		query = getEntityManager().createNamedQuery("Device.getDeviceUsernames");
		List<String> allowedUsers = (List<String>)query.getResultList();

		// Now remove all the users that blocked
		allowedUsers.removeAll(blockedUsernames);

		// Those that remained should receive the notifications
		return allowedUsers;
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
