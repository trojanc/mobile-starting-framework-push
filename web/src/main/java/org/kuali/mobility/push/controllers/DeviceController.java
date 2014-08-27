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


package org.kuali.mobility.push.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for managing devices
 *
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
@Controller
@RequestMapping("/devices")
public class DeviceController {

    /**
     * A controller method for loading the main index.jsp for the Device tool.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "ui3/device/index.html";
    }

    @RequestMapping(value = "/js/devices.js")
    public String getJavaScript() {
        return "ui3/device/js/devices.js.jsp";
    }

    /**
     * Simple controller which responds with 200.
     * This controller can be used by devices to check if the server is online
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<String> ping() {
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
