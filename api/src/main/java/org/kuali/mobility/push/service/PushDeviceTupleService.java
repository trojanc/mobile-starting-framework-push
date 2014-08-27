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

import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.PushDeviceTuple;

import java.util.List;

/**
 * Interface for managing <code>PushDeviceTuple</code> instances.
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.1.0
 */
public interface PushDeviceTupleService{
	
	/**
	 * Service Method to mark a specific <code>PushDeviceTuple<code> as sent.
	 * @param tuple
	 */
	 void markTupleAsSent(PushDeviceTuple tuple);
	
	/**
	 * Service Method to save a given <code>PushDeviceTuple<code>. 
	 * @param tuple
	 * @return Long id of the saved <code>PushDeviceTuple<code>.
	 */
	 void saveTuple(PushDeviceTuple tuple);
	
	/**
	 * Service Method to find all unsent <code>PushDeviceTuple<code> and return them as a List<PushDeviceTuple>
	 * @return List list of <code>PushDeviceTuple<code>
	 */
	 List<PushDeviceTuple> findUnsentTuples();
		
	/**
	 * Service Method to find all <code>PushDeviceTuple<code> associated with a given <code>Push</code>
	 * @return List list of <code>PushDeviceTuple<code>
	 */
	 List<PushDeviceTuple> findTuplesForPush(Push push);

	/**
	 * Service Method to remove <code>PushDeviceTuple<code> associated with a given <code>Push</code>
	 * @param push
	 * @return int Number for tuples removed. -1 if there was a minor error. 
	 */
	 int removeTuplesForPush(Push push);
	
}
