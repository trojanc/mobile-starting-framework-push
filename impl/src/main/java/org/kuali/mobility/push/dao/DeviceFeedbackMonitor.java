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


package org.kuali.mobility.push.dao;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.SSLSocket;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;

/**
 * A task which will run at a configured interval to check for notifications that has to be sent.
 * 
 * The regularity of this Monitor is configured via spring the property push.device.feedbackTimer in kme.config.properites.
 * Current value for development and testing is 10 seconds (10000 ms). This should be set to something considerable higher
 * in production so as to not flood logs or be a general nuisance. On the order of hours to days for systems with low device
 * turnover rates or tens of minutes to hours for higher device turnonver institutions. 
 * 
 * </table>
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.3.0
 */
public class DeviceFeedbackMonitor {

	/** A reference to a logger */
	private static final Logger LOG = Logger.getLogger(DeviceFeedbackMonitor.class);
	
	
	/**
	 * A reference to the <code>DeviceService</code>
	 */
	@Autowired
	private DeviceService deviceService;

	/** Connection pool */
	@Autowired
	private GenericObjectPool<SSLSocket> iOSFeedbackConnectionPool;
	
	
	/**
	 * This is a public method for initiating a system for checking if devices need to be removed from device tables. 
	 * TODO: Weigh pros & cons of splitting these out into separate threads. 
	 */
	public void checkDeviceFeedback(){
		LOG.info("Checking Device Feedback");
		checkiOSDeviceFeedback();
//		checkAndroidDeviceFeedback();
//		checkBlackberryDeviceFeedback();
	}
	
	/**
	 * This is a private method that checks Apple's feedback service for devices that need to be removed. 
	 * 
	 */
	private void checkiOSDeviceFeedback(){
		LOG.info("Checking iOS Device Feedback");
    	final int cFEEDBACKTUPLESIZE = 38;
    	final int cBLOCKSIZE = 1024;
    	final int cBYTEMASK = 0x000000FF;

    	
//   	SSLSocket feedbackSocket = openAppleSocket(feedbackHost, feedbackPort);
    	SSLSocket feedbackSocket = null;
		try {
			feedbackSocket = iOSFeedbackConnectionPool.borrowObject();
		} catch (Exception e) {
			LOG.info("Was unable to borrow SSQLSocket from Pool");
		}
    	    	
		if(null == feedbackSocket){
			LOG.info("APNS Feedback Socket is NOT connected.");
		}else{
			LOG.info("APNS Feedback Socket is connected. Checking Feedback.");
			try{
				InputStream in	= feedbackSocket.getInputStream();	

                // Read bytes        
                byte[] b = new byte[cBLOCKSIZE];
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                int nbBytes = 0;
                // socketStream.available can return 0
                // http://forums.sun.com/thread.jspa?threadID=5428561
                while ((nbBytes = in.read(b, 0, cBLOCKSIZE)) != -1) {
                        message.write(b, 0, nbBytes);
                }
                
                byte[] listOfDevices = message.toByteArray();
                int nbDevices = listOfDevices.length / cFEEDBACKTUPLESIZE;                    
                LOG.info(nbDevices + " devices had feedback.");
                
                for (int j = 0; j < nbDevices; j++) {
                    int offset = j * cFEEDBACKTUPLESIZE;

                    // Build date
                    int index = 0;
                    int firstByte = 0;
                    int secondByte = 0;
                    int thirdByte = 0;
                    int fourthByte = 0;
                    long anUnsignedInt = 0;


                    
                    firstByte = (cBYTEMASK & ((int) listOfDevices[offset]));
                    secondByte = (cBYTEMASK & ((int) listOfDevices[offset + 1]));
                    thirdByte = (cBYTEMASK);
                    fourthByte = (cBYTEMASK & ((int) listOfDevices[offset + 3]));
                    index = index + 4;
                    anUnsignedInt = ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
                    Timestamp timestamp = new Timestamp(anUnsignedInt * 1000);

                    // Build device token length
                    int deviceTokenLength = listOfDevices[offset + 4] << 8 | listOfDevices[offset + 5];

                    // Build device token
                    String deviceToken = "";
                    int octet = 0;
                    for (int k = 0; k < 32; k++) {
                            octet = (cBYTEMASK & ((int) listOfDevices[offset + 6 + k]));
                            deviceToken = deviceToken.concat(String.format("%02x", octet));
                    }

                    LOG.info(timestamp);
                    LOG.info(deviceToken);
                    Device dtoDelete = deviceService.findDeviceByRegId(deviceToken);                        
                    if(deviceService.removeDevice(dtoDelete)){
                    	LOG.info("Deleted " + dtoDelete.getDeviceName());
                    }
                }
                
                
			}catch(Exception e){

				
			}finally{
				try{
					feedbackSocket.close();
				}catch(Exception e){
					
				}
			}
		}
	}
	
	/**
	 * This is a private method that checks Android's feedback service for devices that need to be removed. 
	 * STUB
	 */

	private void checkAndroidDeviceFeedback(){
		LOG.info("Checking Android Device Feedback - STUB");
	}
	
	/**
	 * This is a private method that checks Blackberries feedback service for devices that need to be removed. 
	 * STUB
	 */
	private void checkBlackberryDeviceFeedback(){
		LOG.info("Checking Blackberry Device Feedback - STUB");
	}
	
	
	/**
	 * This is a method to get this object's <code>DeviceService</code>.
	 * @return
	 */
	public DeviceService getDeviceService() {
		return deviceService;
	}

	/**
	 * This is a method to set this object's <code>DeviceService</code> 
	 * @param deviceService
	 */
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
}
