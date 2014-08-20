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
public class PreferenceServiceImplTest {

    private static final String KME_PUSH = "KME_PUSH";

	@Autowired
	@Qualifier("preferenceService")
	private PreferenceService service;

	@Test
	public void testFindPreferencesByUsername(){
		List<Preference> preferences = getService().findPreferencesByUsername("userA");
		assertFalse("FindPreferencesByUsername returned null and should not have.",null==preferences);
		assertTrue("FindPreferencesByUsername returned no preferences.", preferences.size() > 0);
		assertTrue("FindPreferencesByUsername failed to find the pre-loaded data.",preferences.size()==2 );

		// Non-existant User
		preferences = getService().findPreferencesByUsername("userB");
		assertFalse("FindPreferencesByUsername returned null and should not have.",null==preferences);
		assertFalse("FindPreferencesByUsername returned no preferences.", preferences.size() > 0);
	}

	@Test
	public void testFindPreferencesWithWrongUsername(){
		// Non-existant User
		List<Preference> preferences = getService().findPreferencesByUsername("userB");
		assertFalse("FindPreferencesByUsername returned null and should not have.",null==preferences);
		assertFalse("FindPreferencesByUsername returned no preferences.", preferences.size() > 0);
	}

	@Test
	public void testFindPreferencesWithEmptyUsername(){
		// Non-existant User
		List<Preference> preferences = getService().findPreferencesByUsername("");
		assertFalse("FindPreferencesByUsername returned null and should not have.",null==preferences);
		assertFalse("FindPreferencesByUsername returned no preferences.", preferences.size() > 0);
	}
	
	@Test
	public void testFindPreferencesWithUsernameAndShortname(){
		Preference preferences = getService().findPreference("userA",KME_PUSH);
		assertFalse("FindPreferencesWithUsernameAndShortname returned null and should not have.",null==preferences);
	}

	@Test
	public void testFindPreferencesWithWrongUsernameAndShortname(){
		//Non-existant user
		Preference preferences = getService().findPreference("userB",KME_PUSH);
		assertTrue("FindPreferencesWithWrongUsernameAndShortname should have returned null and didn't.",null==preferences);

		// Non-existant sender.
		preferences = getService().findPreference("userA",KME_PUSH+"4");
		assertTrue("FindPreferencesWithWrongUsernameAndShortname should have returned null and didn't.",null==preferences);
	}
	
	@Test
	public void testFindPreferenceWithUsernameAndSender(){
		Preference preferences = getService().findPreference("userA",KME_PUSH);
		// Fails for some reason. Shouldn't. 
		assertFalse("FindPreferenceWithUsernameAndSender returned null and should not have.",null==preferences);
	}

	@Test
	public void testFindPreferenceWithWrongUsernameAndSender(){
		Sender sender;
		//Wrong username
		Preference preferences = getService().findPreference("userB", KME_PUSH);
		assertTrue("FindPreferenceWithWrongUsernameAndSender should have returned null and didn't.",null==preferences);

		//Wrong sender
		preferences = getService().findPreference("userA",KME_PUSH+"x");
		assertTrue("FindPreferenceWithWrongUsernameAndSender should have returned null and didn't.",null==preferences);
	}
	
	@Test
	public void testFindPreferenceWithID(){
		long id = 1L;
		Preference preferences = getService().findPreference(id);
		assertFalse("FindPreferenceWithID returned null and should not have.",null==preferences);
	}
	
	@Test
    @DirtiesContext
	public void testSavePreference(){
		Preference pref = new Preference();
		pref.setEnabled(true);
		pref.setPushSenderID(1L);
		pref.setUsername("userA");
		try{
			getService().savePreference(pref);	
		}catch(Exception e){
			fail("Exception thrown when saveDevice tries to insert with null primary key.");
		}
		assertFalse("Preference saved without id and should not have.",null==pref.getId());
	}
	
	@Test
    @DirtiesContext
	public void testSaveNullPreference(){
		try {
			getService().savePreference(null);
		} catch(Exception e) {
			fail("Exception thrown when savePreference tries to save a null object.");
		}
	}
	
	@Test
    @DirtiesContext
	public void testRemoveNullPreference() {
		boolean didRemove = getService().removePreference(null);
		assertFalse("Removed a device and should not have.",didRemove);
	}
	
	@Test
    @DirtiesContext
	public void testRemovePreferenceNotInDatabase() {
		boolean didRemove = getService().removePreference(Long.MAX_VALUE);
		assertFalse("Removed a Preference that isn't in the database.",didRemove);
	}


	/**
	 * @return the service
	 */
	public PreferenceService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(PreferenceService service) {
		this.service = service;
	}

	
}
