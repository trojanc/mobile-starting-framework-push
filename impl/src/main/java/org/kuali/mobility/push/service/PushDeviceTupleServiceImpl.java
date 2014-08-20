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

import org.kuali.mobility.push.dao.PushDeviceTupleDao;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.PushDeviceTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the <code>PushDeviceTupleService</code>
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.1.0
 */
@Service
public class PushDeviceTupleServiceImpl implements PushDeviceTupleService{

	/** Reference to the <code>PushDeviceTupleDao</code> */
	@Autowired
	private PushDeviceTupleDao pdtDao;

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.PushDeviceTupleService#markTupleAsSent(org.kuali.mobility.push.entity.PushDeviceTuple)
	 */
	@Override
	@Transactional
	public void markTupleAsSent(PushDeviceTuple tuple){
		pdtDao.markTupleAsSent(tuple);
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.PushDeviceTupleService#saveTuple(org.kuali.mobility.push.entity.PushDeviceTuple)
	 */
	@Override
	@Transactional
	public void saveTuple(PushDeviceTuple tuple){
		pdtDao.saveTuple(tuple);
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.PushDeviceTupleService#findUnsentTuples()
	 */
	@Override
	@Transactional
	public List<PushDeviceTuple> findUnsentTuples(){
		return pdtDao.findUnsentTuples();
	}

	/**
	 * Set the reference to the <code>PushDeviceTupleDao</code>
	 * @param dao
	 */
	public void setPushDeviceTupleDao(PushDeviceTupleDao dao) {
		this.pdtDao = dao;
	}

	/**
	 * Get the reference to the <code>PushDeviceTupleDao</code>
	 * @return dao
	 */
	public PushDeviceTupleDao getPushDeviceTupleDao() {
		return pdtDao;
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.PushDeviceTupleService#findTuplesForPush(org.kuali.mobility.push.entity.Push)
	 */
	public List<PushDeviceTuple> findTuplesForPush(Push push){
		return pdtDao.findTuplesForPush(push);
	}

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.PushDeviceTupleService#removeTuplesForPush(org.kuali.mobility.push.entity.Push)
	 */
	@Transactional
	public int removeTuplesForPush(Push push){
		return pdtDao.removeTuplesForPush(push);
	}
}
