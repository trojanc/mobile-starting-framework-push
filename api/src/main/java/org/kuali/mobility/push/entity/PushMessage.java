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
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A class representing a sample Push Message.
 * This class is only intented to by used for "ready made" push notification that can be selected from a list.
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */

@NamedQueries({
	/**
	 * Query to find a specific push by its ID
	 */
	@NamedQuery(
	name = "PushMessage.findByLanguage",
	query="SELECT p FROM PushMessage p WHERE p.language = :language"
	),
	/**
	 * Query to find all Push objects
	 */
	@NamedQuery(
		name = "PushMessage.findById",
		query="SELECT p FROM PushMessage p WHERE  p.messageId = :id"
	)
})
@Entity
@Table(name="KME_PUSHMESSAGE_T")
public class PushMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5910024337809275890L;

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name="ID")
	private Long messageId;

	/**
	 * Title for this <code>PushMessage</code>
	 */
	@Column(name="TTL")
	private String title;

	/**
	 * Content for the message.
	 */
	@Column(name="MSG")
	private String message;

	/**
	 * Language for this message
	 */
	@Column(name="LANG")
	private String language;

	/**
	 * 
	 */
	@Column(name="PST_TS")
	private Timestamp postedTimestamp;

	/**
	 * 
	 */
	@Version
	@Column(name="VER_NBR")
	private Long versionNumber;
	
	/**
	 * Creates a new instance of a <code>PushMessage</code>
	 */
	public PushMessage(){		
	}

    /**
     * Gets the ID of the message.
     * @return ID of the message
     */
	public Long getMessageId() {
		return messageId;
	}

    /**
     * Sets the ID of the message.
     * @return ID of the message.
     */
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	/**
     * Gets the title of the message.
	 * @return The title of the message.
	 */
	public String getTitle() {
		return title;
	}

	/**
     * Sets the title of the message.
	 * @param title The title of the message.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
     * Gets the message content
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
     * Sets the message content
	 * @param message Message content.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
     * Gets the language of the message
	 * @return The language of the message.
	 */
	public String getLanguage() {
		return language;
	}

	/**
     * Sets the language of the message.
	 * @param language The language of the message.
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the postedTimestamp
	 */
	public Timestamp getPostedTimestamp() {
		return postedTimestamp;
	}

	/**
	 * @param postedTimestamp the postedTimestamp to set
	 */
	public void setPostedTimestamp(Timestamp postedTimestamp) {
		this.postedTimestamp = postedTimestamp;
	}

	/**
	 * @return the versionNumber
	 */
	public Long getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @param versionNumber the versionNumber to set
	 */
	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PushMessage [messageId=" + messageId + ", title=" + title
				+ ", message=" + message + ", language=" + language
				+ ", postedTimestamp=" + postedTimestamp + ", versionNumber="
				+ versionNumber + "]";
	}
	
	
	
}
