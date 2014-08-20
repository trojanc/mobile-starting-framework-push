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
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * A class representing a Push Notification message.
 * A single push notification instance is shared for many users and devices.
 *
 * The {@link org.kuali.mobility.push.entity.PushDeviceTuple} class represent the status
 * of a push message being sent to a device.
 *
 * The {@link org.kuali.mobility.push.entity.Sender} class represents a tool or external service that initialised the
 * push notification.
 *
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
@NamedQueries({
	/**
	 * Query to find a specific push by its ID
	 */
	@NamedQuery(
	name = "Push.find",
	query="SELECT p FROM Push p WHERE p.pushId = :pushId"
	),
	/**
	 * Query to find all Push objects
	 */
	@NamedQuery(
		name = "Push.findAll",
		query="SELECT p FROM Push p ORDER BY p.postedTimestamp DESC"
	),
	/**
	 * Counts the number of push objects persisted
	 */
	@NamedQuery(
	name = "Push.countAll",
	query="SELECT COUNT(p) FROM Push p"
	)

})
@Entity
@Table(name="KME_PSH_MSG_T")
public class Push implements Serializable {

	private static final long serialVersionUID = -9158722924017383328L;

	/**
	 * Instance id for this <code>Push</code> instance
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name="ID")
	private Long pushId;

	/**
	 * Title for this <code>Push</code> instance.
	 */
	@Column(name="TTL")
	private String title;

	/**
	 * Message for this <code>Push</code> instance.
	 */
	@Column(name="MSG")
	private String message;


	/**
	 * Timestamp this <code>Push</code> was last persisted.
	 */
	@Column(name="PST_TS")
	@XmlTransient
	private Timestamp postedTimestamp;

	/**
	 * Sender for this <code>Push</code> instance.
	 */
	@Column(name="SNDR")
	private String sender;

	/**
	 * Number of recipients for this <code>Push</code> instance.
	 */
	@Column(name="RCPT")
	private int recipients;

	/**
	 * URL for this <code>Push</code> instance.
	 */
	@Column(name="URL")
	private String url;

	/**
	 * Flag if this <code>Push</code> is an emergency message.
	 */
	@Column(name="EMR")
	private boolean emergency;

	/**
	 * Version number
	 */
	@Version
	@Column(name="VER_NBR")
	@XmlTransient
	private Long versionNumber;

	/**
	 * Creates a new instance of a <code>Push</code>
	 */
	public Push() {
	}

	/**
	 * Gets Timestamp this <code>Push</code> was last persisted.
	 * @return Timestamp this <code>Push</code> was last persisted.
	 */
	public Timestamp getPostedTimestamp() {
		return postedTimestamp;
	}


	/**
	 * Sets Timestamp this <code>Push</code> was last persisted.
	 * @param postedTimestamp Timestamp this <code>Push</code> was last persisted.
	 */
	public void setPostedTimestamp(Timestamp postedTimestamp) {
		this.postedTimestamp = postedTimestamp;
	}

	/**
	 * Returns true if it is an emergency.
	 * @return true if it is an emergency.
	 */
	public boolean getEmergency() {
		return emergency;
	}

	/**
	 * Sets if it is an emergency
	 * @param emergency Flag if it is an emergency
	 */
	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
	}

	/**
	 * Returns the ID.
	 * @return The ID
	 */
	public Long getPushId() {
		return pushId;
	}

	/**
	 * Sets the ID.
	 * @param pushId The ID
	 */
	public void setPushId(Long pushId) {
		this.pushId = pushId;
	}

	/**
	 * Gets the number of recipients
	 * @return number of recipients
	 */
	public int getRecipients() {
		return recipients;
	}

	/**
	 * Sets the number of recipients
	 * @param recipients number of recipients
	 */
	public void setRecipients(int recipients) {
		this.recipients = recipients;
	}

	/**
	 * Gets the sender
	 * @return The sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Sets the sender
	 * @param sender The sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}    

	/**
	 * Gets the URL
	 * @return The URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the URL
	 * @param url The URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the title.
	 * @return The title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 * @param title The title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the message.
	 * @return The message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * @param message The message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the version number.
	 * @return The version number.
	 */
	public Long getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Sets the version number.
	 * @param versionNumber The version number.
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
		String str = newline +"PushID:     " + this.getPushId();
		str = str + newline + "Title:      " + this.getTitle();
		str = str + newline + "Message:    " + this.getMessage(); 
		str = str + newline + "Emergency:  " + this.getEmergency(); 
		str = str + newline + "URL:        " + this.getUrl();  
		str = str + newline + "Sender:     " + this.getSender();  
		str = str + newline + "Recipients: " + this.getRecipients(); 
		str = str + newline + "Timestamp:  " + this.getPostedTimestamp();    
		return str;
	}

	public String toJson() {

		String str = "{";
		str += "\"id\":\"" 				+ this.getPushId() + "\",";
		str += "\"title\":\"" 			+ this.getTitle() + "\",";
		str += "\"message\":\"" 		+ this.getMessage() + "\","; 
		str += "\"emergency\":\"" 		+ this.getEmergency() + "\","; 
		str += "\"url\":\"" 			+ this.getUrl() + "\",";  
		str += "\"sender\":\"" 			+ this.getSender() + "\",";  
		str += "\"recipients\":\"" 		+ this.getRecipients() + "\","; 
    	str += "\"sentTimeStamp\":\"" 	+ this.getPostedTimestamp().getTime() + "\",";   		
		str += "\"timestamp\":\"" 		+ this.getPostedTimestamp() + "\"}";    
		return str;
	}
	
}
