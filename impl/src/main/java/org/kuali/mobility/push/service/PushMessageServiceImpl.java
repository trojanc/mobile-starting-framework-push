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

import org.apache.log4j.Logger;
import org.kuali.mobility.push.dao.PushMessageDao;
import org.kuali.mobility.push.entity.PushMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Implementation of the <code>PushMessageService</code>
 * 
 * @since 2.4.0
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 */
//@Service
public class PushMessageServiceImpl implements PushMessageService {

	/** A reference to a logger for this service */
	private static final Logger LOG = Logger.getLogger(PushMessageServiceImpl.class);
	
	/** A reference to the <code>PushDao</code> */
	@Autowired
	private PushMessageDao pmDao;
	
	/* (non-Javadoc)
	 * @see org.kuali.mobility.push.service.PushMessageService#findAllPushMessagesByLanguage(java.lang.String)
	 */
	@Override
	public List<PushMessage> findAllPushMessagesByLanguage(String language) {
		return pmDao.findAllPushMessagesByLanguage(language);
	}

	/* (non-Javadoc)
	 * @see org.kuali.mobility.push.service.PushMessageService#findPushMessageById(java.lang.Long)
	 */
	@Override
	public PushMessage findPushMessageById(Long id) {
		return pmDao.findPushMessageById(id);
	}
	
	/* (non-Javadoc)
	 * @see org.kuali.mobility.push.service.PushMessageService#savePushMessage(org.kuali.mobility.push.entity.PushMessage)
	 */
	@Override
	public void savePushMessage(PushMessage pushMessage) {
		pmDao.savePushMessage(pushMessage);
	}
	
	/* (non-Javadoc)
	 * @see org.kuali.mobility.push.service.PushMessageService#removePushMessage(org.kuali.mobility.push.entity.PushMessage)
	 */
	@Override
	public boolean removePushMessage(PushMessage pushMessage){
		return pmDao.removePushMessage(pushMessage);
	}

	/**
	 * @return the pmDao
	 */
	public PushMessageDao getPmDao() {
		return pmDao;
	}

	/**
	 * @param pmDao the pmDao to set
	 */
	public void setPmDao(PushMessageDao pmDao) {
		this.pmDao = pmDao;
	}

}
