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


package org.kuali.mobility.push.service;

import org.kuali.mobility.push.entity.PushMessage;

import java.util.List;

/**
 * A class representing a default Push Message.
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.4.0
 */
public interface PushMessageService {
	
	/**
	 * Returns a list of <b>ALL</b> the <code>PushMessage</code> with a given Language 
	 * @param language
	 * @return A List of all <code>PushMessage</code> associated with a given language.
	 */
	List<PushMessage> findAllPushMessagesByLanguage(String language);
	
	/**
	 * Returns a <code>PushMessage</code> with a given id 
	 * @param id
	 * @return A <code>PushMessage</code> with a given id.
	 */
	PushMessage findPushMessageById(Long id);

	/**
	 * Save/Persist a <code>PushMessage</code> object. 
	 * @param pushMessage
	 */
	void savePushMessage(PushMessage pushMessage);

	/**
	 * Remove a <code>PushMessage</code> object.
	 * @param pushMessage
	 * @return Boolean success status of removal. 
	 */
	boolean removePushMessage(PushMessage pushMessage);
}
