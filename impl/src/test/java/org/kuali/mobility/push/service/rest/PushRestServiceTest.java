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


package org.kuali.mobility.push.service.rest;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.service.rest.pojo.PushConfigResponse;
import org.kuali.mobility.push.service.rest.pojo.SendPushRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Unit test for the Android Send Service
 * @since 3.2.0
 * @author Kuali Mobility Team (mobility.collab@kuali.org)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:PushRestSpringBeans.xml")
@Ignore
public class PushRestServiceTest {


    private static final Logger LOG = LoggerFactory.getLogger(PushRestServiceTest.class);

    /**
     * A reference to the <code>DeviceService</code>
     */
    @Resource(name="pushServiceRestClient")
    private PushServiceRest pushServiceRest;


    /**
     * Test the when GCm replies that a device is unregistered
     * that we actually delete the device from the database.
     */
    @Test
    public void testSendPush(){

		Push push = new Push();
		push.setEmergency(true);
		push.setTitle("Unit test Title");
		push.setMessage("Sample message from a unit test");
		push.setSender("UNITTEST");

		SendPushRequest request = new SendPushRequest();
        String[] usernames = {"john", "jane", "wendy"};
        request.setUsernames(new ArrayList(Arrays.asList(usernames)));
		request.setPush(push);

        pushServiceRest.sendPush(request);
    }

	/**
	 * Test the when GCm replies that a device is unregistered
	 * that we actually delete the device from the database.
	 */
	@Test
	public void testGetConfig(){
		PushConfigResponse response = pushServiceRest.getPushConfig();
		Map<String, String> map = response.getMap();
		for(String key : map.keySet()){
			LOG.debug("key :" + key + " , value : " + map.get(key));
		}
	}

}
