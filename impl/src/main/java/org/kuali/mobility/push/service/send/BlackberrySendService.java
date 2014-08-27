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


package org.kuali.mobility.push.service.send;

import net.rim.pushsdk.commons.content.Content;
import net.rim.pushsdk.commons.content.TextContent;
import net.rim.pushsdk.pap.PapService;
import net.rim.pushsdk.pap.StatusCode;
import net.rim.pushsdk.pap.control.DeliveryMethod;
import net.rim.pushsdk.pap.control.PPGType;
import net.rim.pushsdk.pap.control.PushMessageControl;
import net.rim.pushsdk.pap.control.QualityOfService;
import net.rim.pushsdk.pap.unmarshal.PushResponse;
import net.sf.json.JSONObject;
import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.service.SendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Send service implementation for BlackBerry devices.<br>
 * This service should never be called directly, but instead be called via <code>SendServiceDelegator</code>.
 * 
 * <br><br>
 * The <code>SendServiceDelegator</code> service ensures that the OS specific implementations are called in a 
 * separate thread, calling this service directly can cause unexpected long waits in inappropriate threads.<br>
 * <br>
 * 
 * <br>
 * This implementation's properties are set by spring injection if used as a bean.
 * <br>
 * Available spring properties are:<br>
 * <table style="border-collapse:collapse; border: 1px solid;" border="1" cellpadding="3px">
 * 	<tr><th>Property</th><th>Purpose</th></tr>
 *  <tr><td><code>shared.sender.blackberry.appId</code></td><td> Blackberry Application ID from the push registration email</td></tr>
 * 	<tr><td><code>shared.sender.blackberry.password</code></td><td>Password for the application from the push registration email.</td></tr>
 * </table>
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.2.0
 */
public class BlackberrySendService implements SendService {

	/** A reference to this class' logger */
	private static final Logger LOG = LoggerFactory.getLogger(BlackberrySendService.class);

	/** 
	 * Blackberry Application ID from the push registration email
	 * To set this property automatically by spring injection, set the 
	 * <code>shared.sender.blackberry.authorisation</code> property in spring.
	 */
	@Value("${push.blackberry.appId}")
	private String appId;

	/** 
	 * Password for the application from the push registration email.
	 * To set this property automatically by spring injection, set the 
	 * <code>shared.sender.blackberry.password</code> property in spring.
	 */
	@Value("${push.blackberry.appPassword}")
	private String password;


	/**
	 * A reference to the <code>PapService</code> implementation.
	 */
	@Autowired
	private PapService papService;

	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.SendService#sendPush(org.kuali.mobility.push.entity.Push, org.kuali.mobility.push.entity.Device)
	 */
	@Override
	public void sendPush(Push push, Device device) {
		List<Device> devices = new ArrayList<Device>();
		devices.add(device);
		this.sendPush(push, devices);
	}


	/*
	 * (non-Javadoc)
	 * @see org.kuali.mobility.push.service.SendService#sendPush(org.kuali.mobility.push.entity.Push, java.util.List)
	 */
	@Override
	public void sendPush(Push push, Collection<Device> devices) {

		
		
		PushMessageControl pushMessageControl = new PushMessageControl(PPGType.PUBLIC_PUSH);
		QualityOfService qos = new QualityOfService();
		qos.setDeliveryMethod(DeliveryMethod.NOTSPECIFIED);
		
		List<String> addressList = buildAddressList(devices);

		if(!addressList.isEmpty()){
			pushMessageControl.setAddressList(addressList);
			pushMessageControl.setPPGType(PPGType.PUBLIC_PUSH);
			pushMessageControl.setSourceReference(this.appId);
			pushMessageControl.setPushId(""+push.getPushId());
			pushMessageControl.setQualityOfService(qos);
			// TODO pushMessageControl.setDeliverBeforeTimestamp(deliverBeforeTimestamp);
	
			Content content = buildMessage(push);
	
			try {
				PushResponse response = papService.push(this.appId, this.password,  this.appId, pushMessageControl, content);
				if(response.getCode() == StatusCode.ACCEPTED){
					LOG.debug("Message sent! PushId : " + push.getPushId());
				}
				else{
					StringBuilder sb = new StringBuilder();
					sb.append("Push was not sent successful.");
					sb.append(", Description: ").append(response.getDescription());
					sb.append(", PushId: ").append(response.getPushId());
					sb.append(", Status Code: ").append(response.getCode().toString());
					LOG.warn(sb.toString());
				}
			} catch (Exception e) {
				LOG.warn("Exception while trying to send push notication", e);
			}
		}		
	}


	/**
	 * Builds a <code>Content</code> object that contains the formatted data to be sent
	 * @param push Push message to create the <code>Content</code> object of. 
	 * @return
	 */
	private static Content buildMessage(Push push){
		String emer = (push.getEmergency()) ? "YES" : "NO";
		JSONObject jsonData = new JSONObject();
		jsonData.accumulate("id", push.getPushId().toString());
		jsonData.accumulate("message", push.getMessage());
		jsonData.accumulate("title", push.getTitle());
		jsonData.accumulate("url", push.getUrl());
		jsonData.accumulate("emer", emer);

		return new TextContent(jsonData.toString());
	}


	/**
	 * Builds a list of devices pins the message should be sent to
	 * @param devices List of devices to send the message to
	 * @return List of device pins to send the message to
	 */
	private static List<String> buildAddressList(Collection<Device> devices){
		List<String> bbPins = new ArrayList<String>(devices.size());
		for (Device d : devices){
			bbPins.add(d.getDeviceId());
		}
		return bbPins;
	}

}
