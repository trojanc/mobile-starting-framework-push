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

package org.kuali.mobility.push.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A class which will serve as a tuple for messages that has to be sent.
 * This class will be read by a Service querying that will send the unsent messages and keep track of retries
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.1.0
 */
@NamedQueries({
	/**
	 * Query to find all PushDeviceTuple instances that needs to be sent
	 */
	@NamedQuery(
		name = "PushDeviceTuple.findUnsent",
		query="SELECT t FROM PushDeviceTuple t WHERE t.status = 0 OR t.status = 2"
	),
	/**
	 * Query to find devices that was linked to a specific push
	 */
	@NamedQuery(
		name = "PushDeviceTuple.findPushDevices",
		query="SELECT d from PushDeviceTuple t, Device d  WHERE t.deviceId = d.id and t.pushId = :pushId"
	),
	@NamedQuery(
		name="PushDeviceTuple.countUnsent",
		query="select count(t) from PushDeviceTuple t where t.status = 0"
	),
	@NamedQuery(
			name="PushDeviceTuple.findTuplesForPush",
			query="select t from PushDeviceTuple t where t.pushId = :pushId"
	),
    @NamedQuery(
            name="PushDeviceTuple.deleteForStatus",
            query="DELETE PushDeviceTuple t WHERE t.status = :status"
    )
})
@Entity
@Table(name="KME_PSHDEV_T")
public class PushDeviceTuple implements Serializable {

	/** Status indicating that the message is pending to be sent, i.e a message just added */
	public static final int STATUS_PENDING = 0;

	/** Status indicatiing that the message has been sent */
	public static final int STATUS_SENT = 1;

	/** Status indicating that sending the message has failed once, but waiting for a retry attempt */
	public static final int STATUS_WAITING_RETRY = 2;

	/** 
	 * Status indicating the a maximum number of retries has failed, therefore the message is
	 *  seen as failed, and no further retries will be attempted */
	public static final int STATUS_FAILED = 3;

	/** Serial version UID*/
	private static final long serialVersionUID = 9083352553678796701L;

	/**
	 * Id for this PushDeviceTuple
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name="ID")
	private Long tupleId;

	/**
	 * ID of the <code>Push</code> messages this class relates to.
	 */
	@Column(name="PID")
	private Long pushId;

	/**
	 * ID of the devices this message should be sent too.
	 */
	@Column(name="DID")
	private Long deviceId;

	/**
	 * Last time the status of this object has changed
	 */
	@Column(name="PST_TS")
	private Timestamp postedTimestamp;

	/**
	 * The current status of this <code>PushDeviceTuple</code>
	 */
	@Column(name="STATUS")
	private int status;

	/**
	 * The incremental version number for this 
	 */
	@Version
	@Column(name="VER_NBR")
	private Long versionNumber;

	/**
	 * Creates a new instance of a <code>PushDeviceTuple</code>
	 */
	public PushDeviceTuple(){}

	/**
	 * Returns the id of this <code>PushDeviceTuple</code>
	 * @return
	 */
	public Long getId() {
		return tupleId;
	}


	/**
	 * Sets the id of this <code>
	 * @param tupleId
	 */
	public void setId(Long tupleId) {
		this.tupleId = tupleId;
	}


	/**
	 * Gets the ID of the <code>Push</code> this <code>PushDeviceTuple</code> is
	 * linked to.
	 * @return
	 */
	public Long getPushId() {
		return pushId;
	}


	/**
	 * Sets the ID of the <code>Push</code> this <code>PushDeviceTuple</code> is
	 * linked to.
	 * @return ID of the <code>Push</code> this <code>PushDeviceTuple</code> is
	 * linked to.
	 */
	public void setPushId(Long pushId) {
		this.pushId = pushId;
	}


	/**
	 * Gets the ID of the device this <code>PushDeviceTuple</code> is linked to.
	 * @return ID of the device this <code>PushDeviceTuple</code> is linked to.
	 */
	public Long getDeviceId() {
		return deviceId;
	}


	/**
	 * Gets the ID of the device this <code>PushDeviceTuple</code> is linked to.
	 * @return ID of the device this <code>PushDeviceTuple</code> is linked to.
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}


	/**
	 * Gets the timestamp of the last update of this <code>PushDeviceTuple</code>
	 * @return
	 */
	public Timestamp getPostedTimestamp() {
		return postedTimestamp;
	}


	/**
	 * Gets the timestamp of the last update of this <code>PushDeviceTuple</code>
	 * @return The timestamp of the last update of this <code>PushDeviceTuple</code>
	 */
	public void setPostedTimestamp(Timestamp postedTimestamp) {
		this.postedTimestamp = postedTimestamp;
	}


	/**
	 * Sets the status of this <code>PushDeviceTuple</code>
	 * @param status New status for this <code>PushDeviceTuple</code>
	 */
	public void setStatus(int status){
		this.status = status;
	}

	/**
	 * Returns this status of this  <code>PushDeviceTuple</code>
	 * @return Status of this  <code>PushDeviceTuple</code>
	 */
	public int getStatus(){
		return this.status;
	}


	/**
	 * Sets the status of this <code>PushDeviceTuple</code> to 
	 * <code>PushDeviceTuple.STATUS_SENT</code>
	 */
	public void setSent() {
		this.setStatus(STATUS_SENT);
	}

	/**
	 * Returns true if the status of this <code>PushDeviceTuple</code> is
	 * <code>PushDeviceTuple.STATUS_SENT</code>
	 * @return
	 */
	public boolean isSent() {
		return this.status == STATUS_SENT;
	}


	/**
	 * Returns true if the status of this <code>PushDeviceTuple</code> is
	 * <code>PushDeviceTuple.STATUS_WAITING_RETRY</code>
	 * @return
	 */
	public boolean isWaitingRetry(){
		return this.status == STATUS_WAITING_RETRY;
	}


	/**
	 * Sets the status of this <code>PushDeviceTuple</code> to 
	 * <code>PushDeviceTuple.STATUS_WAITING_RETRY</code>
	 */
	public void setWaitingRetry(){
		this.setStatus(STATUS_WAITING_RETRY);
	}


	/**
	 * Gets the version number for this <code>PushDeviceTuple</code>
	 * @return Version number for this <code>PushDeviceTuple</code>
	 */
	public Long getVersionNumber() {
		return versionNumber;
	}


	/**
	 * Sets the version number for this <code>PushDeviceTuple</code>
	 * @param Version number for this <code>PushDeviceTuple</code>
	 */
	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String newline = "\r\n";

		String str = newline + "ID:          " + this.getId();
		str = str + newline + "PushID:     " + this.getPushId();
		str = str + newline + "DeviceID:   " + this.getDeviceId();
		str = str + newline + "Sent:       " + (this.isSent() ? "True" : "False");
		str = str + newline + "Timestamp:  " + this.getPostedTimestamp();    
		return str;
	}


}
