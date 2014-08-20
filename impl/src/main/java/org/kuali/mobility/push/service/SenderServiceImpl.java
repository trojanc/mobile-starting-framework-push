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

import org.kuali.mobility.push.dao.SenderDao;
import org.kuali.mobility.push.entity.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;

/**
 * Implementation of the Device Service
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
@Service
public class SenderServiceImpl implements SenderService {
		
	/** A reference to the logger */
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SenderServiceImpl.class);		

	
	/** A reference to the <code>SenderDao</code> */
	@Autowired
	private SenderDao senderDao;
	
	@Resource(name="kmeProperties")
	private Properties kmeProperties;
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.SenderService#findAllSenders()
	 */
	@Transactional
	public List<Sender> findAllSenders(){
		return senderDao.findAllSenders();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.SenderService#findAllUnhiddenSenders()
	 */
	@Transactional
	public List<Sender> findAllUnhiddenSenders(){
		return senderDao.findAllUnhiddenSenders();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.SenderService#findSenderById(long)
	 */	
	@Transactional
	public Sender findSenderById(long id){
		return senderDao.findSenderById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.SenderService#findSenderByName(java.lang.String)
	 */	
	@Transactional
	public Sender findSenderByName(String name){
		return senderDao.findSenderByName(name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.SenderService#findSenderByShortName(java.lang.String)
	 */	
	@Transactional
	public Sender findSenderByShortName(String shortName){
		return senderDao.findSenderByShortName(shortName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.SenderService#findSenderBySenderKey(java.lang.String)
	 */		@Transactional
	public Sender findSenderBySenderKey(String senderKey){
		return senderDao.findSenderBySenderKey(senderKey);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.SenderService#saveSender(org.kuali.mobility.push.entity.Sender)
	 */	
	@Transactional	
	public void saveSender(Sender sender){
		senderDao.saveSender(sender);
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.SenderService#isValidSenderKey(java.lang.String)
	 */	
	@Transactional
	public 	boolean isValidSenderKey(String senderKey){
		return senderDao.isValidSenderKey(senderKey);
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.SenderService#removeSender(org.kuali.mobility.push.entity.Sender)
	 */		
	@Transactional
	public boolean removeSender(Long senderIdr){
		return senderDao.removeSender(senderIdr);
	}
	
	/**
	 * Sets the reference to the <code>SenderDao</code>
	 * @param dao
	 */
    @Autowired
    public void setSenderDao(SenderDao dao) {
        this.senderDao = dao;
    }
    
	/**
	 * Gets the reference to the <code>SenderDao</code>
	 * @return
	 */
    public SenderDao getSenderDao() {
        return senderDao;
    }
   
	/**
	 * @return the kmeProperties
	 */
	public Properties getKmeProperties() {
		return kmeProperties;
	}

	/**
	 * @param kmeProperties the kmeProperties to set
	 */
	public void setKmeProperties(Properties kmeProperties) {
		this.kmeProperties = kmeProperties;
	}
    
}
