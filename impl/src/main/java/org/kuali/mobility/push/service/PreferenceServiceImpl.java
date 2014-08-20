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

package org.kuali.mobility.push.service;

import org.kuali.mobility.push.dao.PreferenceDao;
import org.kuali.mobility.push.entity.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the Sender Service
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
@Service
public class PreferenceServiceImpl implements PreferenceService{
	
	/** A reference to the <code>PreferenceDao</code> */
	@Autowired
	private PreferenceDao preferenceDao;

	@Override
	public List<Preference> findPreferencesByUsername(String username){
		return preferenceDao.findPreferencesByUsername(username);
	}

	@Override
	public Preference findPreference(String username, String shortName){
		return preferenceDao.findPreference(username, shortName);
	}

	@Override
	public Preference findPreference(String username, Long senderId) {
		return preferenceDao.findPreference(username, senderId);
	}

	@Override
	public Preference findPreference(long id){
		return preferenceDao.findPreference(id);
	}

	@Override
	public List<String> findUsersThatAllowedSender(String senderKey) {
		return preferenceDao.findUsersThatAllowedSender(senderKey);
	}

	@Override
	public Preference savePreference(Preference preference){
		return preferenceDao.savePreference(preference);
	}

	@Override
	public boolean removePreference(Long id){
		return preferenceDao.removePreference(id);
	}

	/**
	 * Gets the reference to the <code>PreferenceDao</code>
	 * @return
	 */
	public PreferenceDao getPreferenceDao() {
		return preferenceDao;
	}

	/**
	 * Sets the reference to the <code>PreferenceDao</code>
	 * @param preferenceDao
	 */
	@Autowired
	public void setPreferenceDao(PreferenceDao preferenceDao) {
		this.preferenceDao = preferenceDao;
	}

}
