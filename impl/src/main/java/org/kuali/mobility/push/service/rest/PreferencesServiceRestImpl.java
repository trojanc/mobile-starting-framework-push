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

package org.kuali.mobility.push.service.rest;

import org.kuali.mobility.push.entity.Preference;
import org.kuali.mobility.push.service.PreferenceService;
import org.kuali.mobility.push.service.rest.pojo.ListResponse;
import org.kuali.mobility.push.service.rest.pojo.PreferenceResponse;
import org.kuali.mobility.push.service.rest.pojo.PreferencesResponse;

/**
 * Created by charl on 2014/07/10.
 */
public class PreferencesServiceRestImpl implements PreferenceServiceRest {

	private PreferenceService preferenceService;

	@Override
	public PreferencesResponse findPreferencesByUsername(String username) {
		return new PreferencesResponse(preferenceService.findPreferencesByUsername(username));
	}

	@Override
	public PreferenceResponse findPreference(String username, String shortName) {
		return new PreferenceResponse(preferenceService.findPreference(username, shortName));
	}

	@Override
	public PreferenceResponse findPreference(String username, Long senderId) {
		return new PreferenceResponse(preferenceService.findPreference(username, senderId));
	}

	@Override
	public PreferenceResponse findPreference(long id) {
		return new PreferenceResponse(preferenceService.findPreference(id));
	}

	@Override
	public ListResponse<String> findUsersThatAllowedSender(String senderKey) {
		return new ListResponse<String>(preferenceService.findUsersThatAllowedSender(senderKey));
	}

	@Override
	public PreferenceResponse savePreference(Preference preference) {
		return new PreferenceResponse(preferenceService.savePreference(preference));
	}

	@Override
	public boolean removePreference(Long preferenceId) {
		return preferenceService.removePreference(preferenceId);
	}

	public void setPreferenceService(PreferenceService preferenceService){
		this.preferenceService = preferenceService;
	}
}
