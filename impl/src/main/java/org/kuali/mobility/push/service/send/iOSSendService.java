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

package org.kuali.mobility.push.service.send;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.service.SendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.SSLSocket;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Collection;

/**
 * An implementation of the SendService for iOS.
 * 
 * This implementation makes use of a connection pool that needs to be configured with spring.
 * The connection pool ensures that a maximum number of open connections can be controlled, as well as closing
 * unsed connections when not used for a period of time.
 * <br>
 * This implementation will attempt to send a message with three retries before giving up.
 *

 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.2.0
 */
public class iOSSendService implements SendService {
	
	/**
	 *  Template for an emergency message
	 */
	private static final String TEMPLATE_EMERGENCY = "\'{\'\"aps\":\'{\'\"alert\":\"{0}\",\"badge\":{1},\"sound\":\"default\"\'}\',\"i\":{2},\"e\":\"{3}\",\"url\":\"{4}\"\'}\'";
	
	/**
	 * Template for a normal push message
	 */
	private static final String TEMPLATE = "\'{\'\"aps\":\'{\'\"badge\":{0},\"sound\":\"default\"\'}\',\"i\":{1},\"e\":\"{2}\",\"url\":\"{3}\"\'}\'";

	private static final int FIRST_BYTE = 0;
	private static final int SECOND_BYTE = 1;
	private static final int THIRD_BYTE = 2;
	private static final int FORTH_BYTE = 3;
	private static final int DEVICE_ID_LENGTH = 32;
	private static final int MAX_RETRY_ATTEMPTS = 3;
	
	
	/** Connection pool */
	@Autowired
	private GenericObjectPool<SSLSocket> iOSConnectionPool;

	/** Reference to a logger */
	private static final Logger LOG = LoggerFactory.getLogger(iOSSendService.class);

	/**
	 * Sends the specified <code>Push</code> message to the specified <code>Device</code>.
	 * This implementation makes use of a connection pool. If there is currently no connection 
	 * available the current thread will block until a connection becomes available (unless 
	 * otherwise configured)
	 */
	@Override
	public void sendPush( Push push, Device device ) {

		byte[] payload = preparePayload(push);
		byte[] deviceToken = createDeviceToken(device);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(1); 					// Command Byte: New format = 1
			baos.write(deviceToken[FIRST_BYTE]); 	// Identifier Byte 1
			baos.write(deviceToken[SECOND_BYTE]); 	// Identifier Byte 2
			baos.write(deviceToken[THIRD_BYTE]); 	// Identifier Byte 3
			baos.write(deviceToken[FORTH_BYTE]); 	// Identifier Byte 4
			baos.write(0); 					// Expiry Byte 1
			baos.write(0); 					// Expiry Byte 2
			baos.write(0); 					// Expiry Byte 3
			baos.write(1); 					// Expiry Byte 4
			baos.write(0); 					// Device ID Length
			baos.write(DEVICE_ID_LENGTH);
			baos.write(deviceToken); 		// Device ID
			baos.write(0); 					// Payload Length
			baos.write(payload.length);
			baos.write(payload); 			// Payload
		} catch ( IOException e ) {
			LOG.error("Failed Creating Payload", e);
			return;
		} 

		int retryAttempt = 1; // Number of tries to send the notification, quits when zero or lower
		boolean success = false;
		OutputStream out = null;//CodeReview could use chained streams here
		while (!success && retryAttempt<=MAX_RETRY_ATTEMPTS) {
			SSLSocket socket = null;
			try {
				socket= iOSConnectionPool.borrowObject();
				out = socket.getOutputStream();
				out.write(baos.toByteArray());
				if (LOG.isDebugEnabled()){
					LOG.debug(baos.toString());
				}
				out.flush(); // We do not close the output stream as it is reused
				success = true;
			} catch (Exception e) {
				LOG.error("Exception while trying to write message over socket (Retry attempt : " + retryAttempt + ")", e);
				IOUtils.closeQuietly(out); // Close potentially broken stream
				retryAttempt++;
			}
			finally{
				try {
					iOSConnectionPool.returnObject(socket);
				} catch (Exception e) {
					LOG.warn("Exception while trying to put Socket back into pool",e);
				}
			}
		}
	}

	/**
	 * Sends the specified push message to the list of devices.
	 * This implementation does not currently support sending messages to batches, 
	 * therefor calling this method will call <code>sendPush(Push , Device)</code>
	 * once for every device.
	 */
	@Override
	public void sendPush(Push push, Collection<Device> devices) {
		if (devices != null){
			for (Device device : devices){
				this.sendPush(push, device);
			}
		}
	}
	
	/**
	 * Creates a device token from the device object
	 * @param device The device to create a token of
	 * @return The byte array of the token
	 */
	private static byte[] createDeviceToken(Device device){
		char[] t = device.getRegId().toCharArray();
		byte[] tokenBytes = null;
		try {
			tokenBytes = Hex.decodeHex(t);
		} catch (DecoderException e) {
			LOG.error("Failed decoding Token", e);
		}
		return tokenBytes;
	}

	/**
	 * This method returns a String in the format required by APNS.
	 * @param p The push message to create payload of.
	 * @return A string formatted ready to be sent to APNS
	 * @throws java.io.UnsupportedEncodingException
	 */
	private static byte[] preparePayload(Push p){
		String message;
		if (p.getEmergency()){
			Object[] arguments = {
					p.getTitle(),							// Message
					"0",									// Badge
					String.valueOf(p.getPushId()),			// Message id
					(p.getEmergency()?"YES":"NO"),			// Emergency
					((p.getUrl()!=null && p.getUrl().length() > 0)?"YES":"NO")	// Has url, CodeReview Might be a good idea to explain (using comments) why you do this.
			};
			message = MessageFormat.format(TEMPLATE_EMERGENCY, arguments);
		}
		else {
			Object[] arguments = {
					"0",									// Badge
					String.valueOf(p.getPushId()),			// Message id
					(p.getEmergency()?"YES":"NO"),			// Emergency
					((p.getUrl()!=null && p.getUrl().length() > 0)?"YES":"NO")	// Has url, CodeReview Might be a good idea to explain (using comments) why you do this.
			};
			message = MessageFormat.format(TEMPLATE, arguments);
		}
		try {
			return message.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.warn("Exception while converting device token from string to bytes", e);
			return null;
		}
	}
	
}
