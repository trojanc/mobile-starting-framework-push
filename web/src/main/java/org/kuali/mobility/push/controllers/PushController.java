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

import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.entity.PushMessage;
import org.kuali.mobility.push.entity.Sender;
import org.kuali.mobility.push.service.DeviceService;
import org.kuali.mobility.push.service.PushMessageService;
import org.kuali.mobility.push.service.PushService;
import org.kuali.mobility.push.service.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

/**
 * Controller for Push related pages
 *
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.0.0
 */
@Controller
@RequestMapping("/push")
public class PushController {

    /**
     * A reference to a logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(PushController.class);

    private static final String RECIPIENTS = "recipients";

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Autowired
	@Qualifier("kmeProperties")
	private Properties kmeProperties;
	
	@Autowired
	@Qualifier("senderKeysProperties")
	private Properties senderKeysProperties;
	
	
	/**
	 * A reference 
	 */
	private String keyListFile;

    private Map<String, String> stockPushMessages;
    /*
	 * static { stockPushMessages = new LinkedHashMap<String, String>(); //
	 * stockPushMessages.put("Pre-defined Push", "");
	 * stockPushMessages.put("Test Message STG", "Test Message Detail STG");
	 * stockPushMessages.put("New Feature in KME",
	 * "Check out the new {BLANK} in KME");
	 * stockPushMessages.put("Log into a protected tool",
	 * "Once you log into a CAS protected tool (such as My Classes or Classifieds) the push notifications you receive will be tailored specifically to you."
	 * ); stockPushMessages.put("Tornado Warning",
	 * "A Tornado warning has been issued for the area near campus and downtown. Please report to a shelter until all-clear has been sounded."
	 * ); stockPushMessages.put("Crime: Local Car Thieves",
	 * "Police have issued a warning for the downtown area regarding a series of car thefts. Take care when parking and locking your car."
	 * ); }
	 */


    /** Rendered obsolete by MOBILE-336 and MOBILE-386
     // These are "keys" for whom using the webservice. Without a key they
     // service will error out.
     // This is not ideal. They should be stripped out and not hard coded.
     private Map<String, String> senders;
     // static {
     // senders = new LinkedHashMap<String, String>();
     // senders.put("72241k6tlukzrs7mtqs4", "DINING");
     // senders.put("blo6iy4sj3szrdx5nyxg", "RPS");
     // senders.put("i6ijmfnwsj5n8pa327s7", "RES_HALLS");
     // senders.put("gs5z6ekg7jo8bev49jgv", "UITS");
     // senders.put("thsdzf0mmzp4vonkvr1h", "IUMOBILE");
     // senders.put("0l14yoyh38rbja14ynl6", "UIX");
     // }
     */

    /**
     * A reference to Default precipients.
     */
    private static final Map<String, String> RECIPIENTS_MAP;

	static {
		RECIPIENTS_MAP = new LinkedHashMap<String, String>();
		// RECIPIENTS_MAP.put("Select Recipients", "");
		RECIPIENTS_MAP.put("All", "all");
		RECIPIENTS_MAP.put("All without Network ID", "usernameless");
	}

    /**
     * A reference to the <code>PushService</code>.
     */
    @Autowired
    private PushService pushService;

    /**
     * A reference to the <code>DeviceService</code>.
     */
    @Autowired
    private DeviceService deviceService;

    /**
     * A reference to the <code>SenderService</code>.
     */
    @Autowired
    private SenderService senderService;

    @Autowired
    private PushMessageService pushMessageService;


    /**
     * A controller method for accessing and displaying the details of a specific push message.
     *
     * @param uiModel
     * @param request
     * @param pushHash Id of the Push message to display.
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/detail/{pushHash}", method = RequestMethod.GET)
    public String getPushDetails(Model uiModel, HttpServletRequest request,
                                 @PathVariable("pushHash") Long pushHash) {

        String isAdmin = "no";
// TODO convert to spring security
//        if (isAllowedAccess("KME-ADMINISTRATOR", request)) {
//            isAdmin = "yes";
//        }
//		User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);
//		List<Group> groups = user.getGroups();
//		Iterator<Group> it = groups.iterator();
//		while(it.hasNext()){
//			Group group = it.next();
//			if("KME-ADMINISTRATOR".equals(group.getName())){
//				isAdmin = "yes";
//			}
//		}
        uiModel.addAttribute("isAdminMember", isAdmin);

        if (pushHash != null) {
            LOG.info("PushID is :" + pushHash);
        } else {
            return "push";
        }
        Push push = pushService.findPushById(pushHash);
        uiModel.addAttribute("thispush", push);

        List<Device> devices = pushService.findDevicesForPush(push);
        LOG.info("Found Devices for Push " + devices.size());
        uiModel.addAttribute("thesedevices", devices);
        return "push/pushdetail";
    }


    /**
     * A controller method to send a message to a device
     *
     * @param uiModel
     * @param request
     * @param deviceID Id of device to which to send.
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String sendToChosenDevice(Model uiModel, HttpServletRequest request,
                                     @RequestParam(value = "id", required = false) long deviceID) {
        String viewName = "push/index";
// TODO convert to spring security
//        if (!isAllowedAccess("KME-ADMINISTRATOR", request)) {
//            viewName = "redirect:/errors/401";
//        } else {
            stockPushMessages = getPushMessages();
            uiModel.addAttribute(RECIPIENTS, RECIPIENTS_MAP);
            uiModel.addAttribute("stockPushMessages", stockPushMessages);
            Push push = new Push();
            Device device = deviceService.findDeviceById(deviceID);
            uiModel.addAttribute("push", push);
            uiModel.addAttribute("device", device);
//        }

        viewName = "push/index.html";
        return viewName;
    }

    @RequestMapping(value = "error", method = RequestMethod.GET)
    public String error(Model uiModel, HttpServletRequest request) {
    	return "redirect:/errors/401";
    } 
    
    
    
    /**
     * A controller method to setup a UI form for sending a single push to a single device via a JSP. Will send to all devices or single selected device.
     *
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model uiModel, HttpServletRequest request) {
        LOG.info("index() in Push Tool was called." );
    	
        String viewName;
        // TODO convert to spring security
//        if (!isAllowedAccess("KME-ADMINISTRATOR", request)) {
//        	viewName = "redirect:/errors/401.jsp";
//            LOG.info("Redirecting to: " + viewName );
//        } else {
            // Get Stock Push Messages.
            String source = getKmeProperties().getProperty("push.stock.messages.source");
            if ("database".equalsIgnoreCase(source)) {
                LOG.info("GET Stock Messages from DB.");
                LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
                Locale locale = localeResolver.resolveLocale(request);
                this.setStockPushMessages(this.getPushMessages(locale));
            } else if ("properties".equalsIgnoreCase(source)) {
                LOG.info("GET Stock Messages from Properties.");
                this.setStockPushMessages(this.getPushMessages());
            }

            uiModel.addAttribute(RECIPIENTS, RECIPIENTS_MAP);
            uiModel.addAttribute("stockPushMessages", stockPushMessages);
            Push push = new Push();

            LOG.info("keyListFile: " + keyListFile);

            uiModel.addAttribute("push", push);
            
            viewName = "push/index.html";
//        }
        return viewName;
    }

    @RequestMapping(value = "/js/history.js")
    public String getHistoryJavaScript(Model uiModel, HttpServletRequest request) {
        return "push/js/history.js.jsp";
    }
    
    @RequestMapping(value = "/js/push.js")
    public String getJavaScript(Model uiModel, HttpServletRequest request) {
        String source = getKmeProperties().getProperty("push.stock.messages.source");
        if ("database".equalsIgnoreCase(source)) {
            LOG.info("GET Stock Messages from DB.");
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            Locale locale = localeResolver.resolveLocale(request);
            this.setStockPushMessages(this.getPushMessages(locale));
        } else if ("properties".equalsIgnoreCase(source)) {
            LOG.info("GET Stock Messages from Properties.");
            this.setStockPushMessages(this.getPushMessages());
        }

        Sender defaultSender = senderService.findSenderByShortName("KME_PUSH");
        uiModel.addAttribute("senderKey", defaultSender.getSenderKey());
        
        uiModel.addAttribute(RECIPIENTS, RECIPIENTS_MAP);
        uiModel.addAttribute("stockPushMessages", stockPushMessages);
    	
        return "push/js/push.js.jsp";
    }

    /**
     * A controller method for displaying a historical list of push notifications.
     *
     * @param request
     * @param uiModel
     * @param key
     * @return
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String history(HttpServletRequest request, Model uiModel,
                          @RequestParam(value = "key", required = false) String key) {
       return "push/pushhistory.html";
    }

    /**
     * A controller method for processing the submission of the form for sending a push notification.
     *
     * @param uiModel
     * @param push
     * @param result
     * @param id        Id of device to which to send a push.
     * @param sender    ShortName of the Sender that is sending the push.
     * @param recipient Recipients of push.
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public String submitPush(
            HttpServletRequest request,
            Model uiModel,
            @ModelAttribute("Push") Push push,
            BindingResult result,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "sender", required = false) String sender,
            @RequestParam(value = "recipientSelect", required = false) String recipient) {
        String viewName;
        // TODO convert to spring security
//        if (!isAllowedAccess("KME-ADMINISTRATOR", request)) {
//            viewName = "redirect:/errors/401";
//        } else {
            push.setPostedTimestamp(new Timestamp(System.currentTimeMillis()));
            Sender defaultSender = senderService.findSenderByShortName("KME_PUSH");
            LOG.info(defaultSender.toString());

            if (defaultSender != null) {
                push.setSender(defaultSender.getShortName());
            } else {
                push.setSender("KME_PUSH");
            }
            //		push.setSender(sender);

            LOG.info("---- Recipient Select: " + recipient);

            List<Device> devices = new ArrayList<Device>();

            if (id != null) {
                LOG.info("---- Selected ID");
                devices.add(deviceService.findDeviceById((long) id));
            }

            if (recipient != null) {
                if (recipient.equals("all")) {
                    devices = deviceService.findAllDevices();
                } else if (recipient.equals("usernameless")) {
                    devices = deviceService.findDevicesWithoutUsername();
                }
            }

            LOG.info(devices.size() + " devices in list");

            if (isValidPush(push, result)) {
                // pushService.savePush(push);
                pushService.savePush(push, devices);

                // TODO: SavePushDeviceTuple here.

                int pushed = pushService.sendPush(push, devices);
                LOG.info("Pushed Count: " + pushed);
                push.setRecipients(pushed);
                pushService.savePush(push);
                LOG.info("Post Push:" + push.toString());

                int pushCount = pushService.countPushes();
                uiModel.addAttribute("pushCount", pushCount);

                List<Push> pastPushes = pushService.findAllPush();
                uiModel.addAttribute("pastPushes", pastPushes);
                viewName = "push/history";
            } else {
                viewName = "push/index";
            }
//        }
        return viewName;
    }

    /**
     * A controller method for testing if a submitted push is valid.
     *
     * @param p      A Push to check for validity.
     * @param result
     * @return
     */
    private boolean isValidPush(Push p, BindingResult result) {
        Errors errors = ((Errors) result);
        boolean isValid = true;
        if (p.getMessage() == null || "".equals(p.getMessage().trim())) {
            errors.rejectValue("message",
                    "Please type a Message for the Push notification.");
            isValid = false;
        }
        if (p.getTitle() == null || "".equals(p.getTitle().trim())) {
            errors.rejectValue("title",
                    "Please type a Title for the Push notification.");
            isValid = false;
        }

        return isValid;
    }


    /**
     * A method to get predefined PushMessages. Pulled from localized *.properties files.
     *
     * @return Map<String, String>
     */
    // getPushMessages() method reads values from properties file
    public Map<String, String> getPushMessages() {
        Map<String, String> pushMessages = new LinkedHashMap<String, String>();
        String stockPushMessage1 = messageSource.getMessage(
                "push.test.message.stg", null, null);
        String stockPushMessage2 = messageSource.getMessage(
                "push.new.feature.kme", null, null);
        String stockPushMessage3 = messageSource.getMessage(
                "push.log.protected.tool", null, null);
        String stockPushMessage4 = messageSource.getMessage(
                "push.tornado.warning", null, null);
        String stockPushMessage5 = messageSource.getMessage(
                "push.crime.local.car.thieves", null, null);
        pushMessages.put("Test Message STG", stockPushMessage1);
        pushMessages.put("New Feature in KME", stockPushMessage2);
        pushMessages.put("Log into a protected tool", stockPushMessage3);
        pushMessages.put("Tornado Warning", stockPushMessage4);
        pushMessages.put("Crime: Local Car Thieves", stockPushMessage5);
        return pushMessages;
    }

    /**
     * A method to get predefined PushMessages. Pulled from Database by Locale attribute.
     * Stock Push Messages are inserted during the PushBootListener bootstrapping.
     *
     * @param locale
     * @return Map<String, String> of push Messages.
     */
    public Map<String, String> getPushMessages(Locale locale) {
        List<PushMessage> pms = pushMessageService.findAllPushMessagesByLanguage(locale.getLanguage());
        Map<String, String> pushMessages = new LinkedHashMap<String, String>();
        Iterator<PushMessage> it = pms.iterator();
        while (it.hasNext()) {
            PushMessage pm = it.next();
            pushMessages.put(pm.getTitle(), pm.getMessage());
        }
        return pushMessages;
    }


    /**
     * A method for setting the keyListFile for the controller.
     *
     * @param keyListFile
     */
    public void setKeyListFile(String keyListFile) {
        this.keyListFile = keyListFile;
    }

    /**
     * A method for retrieving the keyListFile from the controller.
     *
     * @return
     */
    public String getKeyListFile() {
        return this.keyListFile;
    }

    /**
     * A method for setting the <code>PushService</code> for the controller.
     *
     * @param pushService
     */
    public void setPushService(PushService pushService) {
        this.pushService = pushService;
    }

    /**
     * A method for setting the <code>DeviceService</code> for the controller.
     *
     * @param deviceService
     */
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * A method for setting the <code>SenderService</code> for the controller.
     *
     * @param senderService
     */
    public void setSenderService(SenderService senderService) {
        this.senderService = senderService;
    }

    public SenderService getSenderService() {
        return senderService;
    }

    /**
     * @return the messageSource
     */
    public MessageSource getMessageSource() {
        return messageSource;
    }

    /**
     * @param messageSource the messageSource to set
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    /**
     * @param pmService the pmService to set
     */
    public void setPushMessageService(PushMessageService pmService) {
        this.pushMessageService = pmService;
    }


    /**
     * @return the stockPushMessages
     */
    public Map<String, String> getStockPushMessages() {
        return stockPushMessages;
    }

    /**
     * @param stockPushMessages the stockPushMessages to set
     */
    public void setStockPushMessages(Map<String, String> stockPushMessages) {
        this.stockPushMessages = stockPushMessages;
    }

    /**
     * @return the kmeProperties
     */
    public Properties getKmeProperties() {
        return kmeProperties;
    }

    /**
     * @param kmeProperties the kmeProperties to set
     */
    public void setKmeProperties(Properties kmeProperties) {
        this.kmeProperties = kmeProperties;
    }

	/**
	 * @return the senderKeysProperties
	 */
	public Properties getSenderKeysProperties() {
		return senderKeysProperties;
	}


	/**
	 * @param senderKeysProperties the senderKeysProperties to set
	 */
	public void setSenderKeysProperties(Properties senderKeysProperties) {
		this.senderKeysProperties = senderKeysProperties;
	}
}
