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

import org.kuali.mobility.push.entity.Sender;

import java.util.List;

/**
 * Data Access Object Interface for Sender Objects
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
public interface SenderDao {

	/**
	 * Persists a <code>Sender</code> object.
	 * @param sender Sender to persist.
	 */
	 void saveSender(Sender sender);

	/**
	 * Removes a <code>Sender</code> object.
	 * @param sender Sender to remove.
	 */
	 boolean removeSender(Long senderIdr);

	/**
	 * Determine validity/availability of the senderKey.
	 * @param senderKey SenderKey to determine if valid.
	 * @return validity of a senderKey.
	 */
		boolean isValidSenderKey(String senderKey);
	
	/**
	 * Find and return all available <code>Sender</code> objects, useful for opt-in/opt-out preferences.
	 * @return List of all <code>Sender</code> objects.
	 */
		List<Sender> findAllSenders();

	/**
	 * Find and return all available <code>Sender</code> objects, useful for opt-in/opt-out preferences.
	 * @return List of all <code>Sender</code> objects whom are not set to be hidden.
	 */
		List<Sender> findAllUnhiddenSenders();
	
	/**
	 * Find and return <code>Sender</code> object, using Id as search criteria.
	 * @param id Id for the <code>Sender</code> to find.
	 * @return <code>Sender</code> as determined by id.
	 */
		Sender findSenderById(long id);

	/**
	 * Find and return <code>Sender</code> object, using Name as search criteria.
	 * @param name Name of the <code>Sender</code> to find.
	 * @return <code>Sender</code> as determined by Name.
	 */	
		Sender findSenderByName(String name);
	
	/**
	 * Find and return <code>Sender</code> object, using ShortName as search criteria.
	 * @param shortName ShortName for the <code>Sender</code> to find.
	 * @return <code>Sender</code> as determined by ShortName.
	 */
		Sender findSenderByShortName(String shortName);
	
	/**
	 * Find and return <code>Sender</code> object, using SenderKey as search criteria.
	 * @param senderKey SenderKey for the <code>Sender</code> to find.
	 * @return <code>Sender</code> as determined by SenderKey.
	 */
		Sender findSenderBySenderKey(String senderKey);
	
}
