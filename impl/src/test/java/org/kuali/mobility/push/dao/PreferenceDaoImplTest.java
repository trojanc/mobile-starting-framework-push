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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.mobility.push.entity.Preference;
import org.kuali.mobility.push.entity.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Kuali Mobility Team (mobility.collab@kuali.org)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:TestSpringBeans.xml")
public class PreferenceDaoImplTest {

	private static final String KME_PUSH = "KME_PUSH";
	private static final String USER_A = "userA";
	private static final String USER_B = "userB";

	@Autowired
	@Qualifier("preferenceDao")
	private PreferenceDao dao;

	@Test
	public void testFindPreferencesByUsername(){
		List<Preference> preferences = getDao().findPreferencesByUsername(USER_A);
		assertNotNull("FindPreferencesByUsername returned null and should not have.",preferences);
		assertNotEquals("FindPreferencesByUsername returned no preferences.", 0, preferences.size());
		assertEquals("FindPreferencesByUsername failed to find the pre-loaded data.", 2, preferences.size());

		// Non-existant User
		preferences = getDao().findPreferencesByUsername(USER_B);
		assertNotNull("FindPreferencesByUsername returned null and should not have.", preferences);
		assertEquals("FindPreferencesByUsername returned no preferences.", 0, preferences.size());
	}

	@Test
	public void testFindPreferencesWithWrongUsername(){
		// Non-existant User
		List<Preference> preferences = getDao().findPreferencesByUsername(USER_B);
		assertNotNull("FindPreferencesByUsername returned null and should not have.",preferences);
		assertEquals("FindPreferencesByUsername should have returned no preferences", 0, preferences.size());
	}

	@Test
	public void testFindPreferencesWithEmptyUsername(){
		// Non-existant User
		List<Preference> preferences = getDao().findPreferencesByUsername("");
		assertNotNull("FindPreferencesByUsername returned null and should not have.", preferences);
		assertEquals("FindPreferencesByUsername should have returned no preferences.", 0, preferences.size());
	}
	
	@Test
	public void testFindPreferencesWithUsernameAndShortname(){
		Preference preferences = getDao().findPreference(USER_A,KME_PUSH);
		assertNotNull("FindPreferencesWithUsernameAndShortname returned null and should not have.",preferences);
	}

	@Test
	public void testFindPreferencesWithWrongUsernameAndShortname(){
		//Non-existant user
		Preference preferences = getDao().findPreference(USER_B,KME_PUSH);
		assertNull("FindPreferencesWithWrongUsernameAndShortname should have returned null and didn't.",preferences);

		// Non-existant sender.
		preferences = getDao().findPreference(USER_A,KME_PUSH+"4");
		assertNull("FindPreferencesWithWrongUsernameAndShortname should have returned null and didn't.",preferences);
	}
	
	@Test
	public void testFindPreferenceWithUsernameAndSender(){
		Preference preferences = getDao().findPreference(USER_A,KME_PUSH);
		assertNotNull("FindPreferenceWithUsernameAndSender returned null and should not have.",preferences);
	}

	@Test
	public void testFindPreferenceWithWrongUsernameAndSender(){
		Sender sender = new Sender();
		sender.setShortName("KME_PUSH");
		sender.setId(1L);
		//Wrong username
		Preference preferences = getDao().findPreference(USER_B,KME_PUSH);
		assertNull("FindPreferenceWithWrongUsernameAndSender should have returned null and didn't.",preferences);

		//Wrong sender
		preferences = getDao().findPreference(USER_A,KME_PUSH+"x");
		assertNull("FindPreferenceWithWrongUsernameAndSender should have returned null and didn't.",preferences);
	}
	
	@Test
	public void testFindPreferenceWithID(){
		long id = 1L;
		Preference preferences = getDao().findPreference(id);
		assertNotNull("FindPreferenceWithID returned null and should not have.",preferences);
	}
	
	@Test
	@DirtiesContext
	public void testSavePreference(){
		Preference pref = new Preference();
		pref.setEnabled(true);
		pref.setPushSenderID(1L);
		pref.setUsername(USER_A);
		try{
			getDao().savePreference(pref);	
		}catch(Exception e){
			fail("Exception thrown when saveDevice tries to insert with null primary key.");
		}
		assertNotNull("Preference saved without id and should not have.",pref.getId());
	}
	
	@Test
	@DirtiesContext
	public void testSaveNullPreference(){
		try {
			getDao().savePreference(null);
		} catch(Exception e) {
			fail("Exception thrown when savePreference tries to save a null object.");
		}
	}
	
	@Test
	@DirtiesContext
	public void testRemoveNullPreference() {
		boolean didRemove = getDao().removePreference(null);
		assertFalse("Removed a device and should not have.",didRemove);
	}
	
	@Test
	@DirtiesContext
	public void testRemovePreferenceNotInDatabase() {
		boolean didRemove = getDao().removePreference(Long.MAX_VALUE);
		assertFalse("Removed a Preference that isn't in the database.",didRemove);
	}

	/**
	 * @return the dao
	 */
	public PreferenceDao getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(PreferenceDao dao) {
		this.dao = dao;
	}
	
	
	
}
