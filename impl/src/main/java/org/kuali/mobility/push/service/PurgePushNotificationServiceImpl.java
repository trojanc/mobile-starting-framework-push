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

import org.apache.log4j.Logger;
import org.kuali.mobility.push.dao.PurgePushNotificationDao;
import org.kuali.mobility.push.service.send.SendServiceDelegator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;

/**
 * PurgePushNotificationServiceImpl class is implementation of PurgePushNotificationService interface and it provides
 * a method namely purgePushNotification to purge push data from the database based on the status provided.
 *
 * @author Kuali Mobility Team (mobility.collab@kuali.org)
 */
public class PurgePushNotificationServiceImpl implements PurgePushNotificationService {

    /** A reference to a logger for this service */
    private static final Logger LOG = Logger.getLogger(PurgePushNotificationServiceImpl.class);

    /** A reference to the <code>SendServiceDelegator</code> object used by this ServiceImpl */
    @Autowired
    @Qualifier("sendServiceDelegator")
    private SendServiceDelegator sendService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PurgePushNotificationDao purgePushNotificationDao;

    @Autowired
    private PushDeviceTupleService pdtService;


    public boolean purgePushNotification(HttpServletRequest request, int status) {
        return purgePushNotificationDao.purgePushNotification(request, status);
    }


    /** A reference to the <code>DeviceService</code> object used by this ServiceImpl */
    public DeviceService getDeviceService() {
        return deviceService;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /** A reference to the <code>PushDeviceTupleService</code> object used by this ServiceImpl */
    public PushDeviceTupleService getPdtService() {
        return pdtService;
    }

    public void setPdtService(PushDeviceTupleService pdtService) {
        this.pdtService = pdtService;
    }
}
