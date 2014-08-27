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


package org.kuali.mobility.push.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A class representing a Push Notification Opt-out Preference.
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
@NamedQueries({
	/**
	 * Query to find all PushDeviceTuple instances that needs to be sent
	 */
	@NamedQuery(
		name = "Preference.findPreferencesByUsername",
		query="SELECT p FROM Preference p WHERE p.username = :username"
	),
	/**
	 * Query to find devices that was linked to a specific push
	 */
	@NamedQuery(
		name = "Preference.findPreferenceWithUsernameAndShortname",
		query="SELECT p FROM Preference p WHERE p.username = :username AND p.pushSenderID = (SELECT s.id FROM Sender s WHERE s.shortName = :shortName)"
	),
	/**
	 * Query to find devices that was linked to a specific push
	 */
	@NamedQuery(
			name = "Preference.findPreferenceWithUsernameAndSenderId",
			query="SELECT p FROM Preference p WHERE p.username = :username AND p.pushSenderID = :pushSenderID"
	),
	@NamedQuery(
		name="Preference.findPreferenceWithUsernameAndSender",
		query="SELECT p FROM Preference p WHERE p.username = :username AND p.pushSenderID = :senderId"
	),
	@NamedQuery(
			name="Preference.findPreferenceWithId",
			query="SELECT p FROM Preference p WHERE p.id = :id"
	),
    @NamedQuery(
            name="Preference.findUsersThatBlockedSenderKey",
            query="SELECT p.username FROM Preference p WHERE p.enabled = false AND p.pushSenderID = (SELECT s.id FROM Sender s WHERE s.senderKey = :senderKey)"
    )
})
@Entity
@Table(name="KME_PSH_PREF_T")
@XmlRootElement
public class Preference implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8566828971044365068L;

	/**
	 * ID for this <code>Preference</code> instance.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name="ID")
	private Long id;
	
	/**
	 * Username of the person 
	 */
	@Column(name="USR")
	private String username;

	/**
	 * Enabled denotes whether the whether the user wants to receive
	 * Push from a given sender, only in table if false.
	 * (opt-out) If true, removed from table.
	 */
	@Column(name="ENB")
	private boolean enabled;
	
	/**
	 * Id of the sender.  
	 */
	@Column(name="SID")
	private Long pushSenderID;
	
	/**
	 * PostedTimestamp at which the preference was set and inserted into the database.  
	 */
	@Column(name="PST_TS")
	private Timestamp postedTimestamp;
	
	/**
	 * VersionNumber is the versionNumber of the preference.  
	 */
	@Version
	@Column(name="VER_NBR")
	private Long versionNumber;

	/**
	 * Creates a new instance of a <code>Preference</code>
	 */
	public Preference(){}
	
	/**
	 * Gets the ID for this <code>Preference</code>
	 * @return ID for this <code>Preference</code>
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id
	 * @param id The ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the username for this <code>Sender</code>
	 * @return username for this <code>Sender</code>
	 */	
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the Username
	 * @param username The Username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the boolean for enabled for this <code>Preference</code>
	 * @return hidden for this <code>Preference</code>
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Gets the boolean for enabled for this <code>Preference</code>
	 * @return whether the sender for this <code>Preference</code> is blocked.
	 */
	public boolean isSenderBlocked() {
		return !enabled;
	}
	
	/**
	 * Sets the hidden
	 * @param enabled The Hidden
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets the pushSenderId for this <code>Preference</code>
	 * @return pushSenderId for this <code>Preference</code>
	 */
	public Long getPushSenderID() {
		return pushSenderID;
	}

	/**
	 * Sets the PushSenderId
	 * @param pushSenderID The PushSenderID
	 */
	public void setPushSenderID(Long pushSenderID) {
		this.pushSenderID = pushSenderID;
	}

	/**
	 * Gets the postedTimestamp for this <code>Preference</code>
	 * @return postedTimestamp for this <code>Preference</code>
	 */
	@XmlTransient
	public Timestamp getPostedTimestamp() {
		return postedTimestamp;
	}

	/**
	 * Sets the PostedTimestamp
	 * @param postedTimestamp The PostedTimestamp
	 */
	public void setPostedTimestamp(Timestamp postedTimestamp) {
		this.postedTimestamp = postedTimestamp;
	}

	/**
	 * Gets the versionNumber for this <code>Preference</code>
	 * @return versionNumber for this <code>Preference</code>
	 */
	@XmlTransient
	public Long getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Sets the VersionNumber
	 * @param versionNumber The VersionNumber
	 */
	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return	"\nPreference   id = " + id 
				+ "\nusername		= " + username  
				+ "\nenabled		 = " + (enabled ? "true" : "false")   
				+ "\npushSenderID	= " + pushSenderID
				+ "\npostedTimestamp = " + postedTimestamp 
				+ "\nversionNumber   = " + versionNumber + "\n";
	}

	
	
}
