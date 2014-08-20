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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.kuali.mobility.push.entity.Device;
import org.kuali.mobility.push.entity.Push;
import org.kuali.mobility.push.service.DeviceService;
import org.kuali.mobility.push.service.SendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Implementation of the send service for Android.
 * This service should never be called directly, but instead be called via <code>SendServiceDelegator</code>.<br>
 * This service limits the total number of simultanious connections to the GCM service to a maximum of 10 connections.
 * If another thread attempts to send another message and the connection limit has been reached, the thread will block
 * until a connection is available.<br>
 * This implementation will send messages requested by the <code>sendPush(Push, List)</code> in batches of 1000 registration ids
 * per batch.
 * <br><br>
 * The <code>SendServiceDelegator</code> service ensures that the OS specific implementations are called in a 
 * separate thread, calling this service directly can cause unexpected long waits in inappropriate threads.<br>
 * <br>
 * This implementation's properties are set by spring injection if used as a bean.
 * <br>
 * Available spring properties are:<br>
 * <table style="border-collapse:collapse; border: 1px solid;" border="1" cellpadding="3px">
 * 	<tr><th>Property</th><th>Purpose</th></tr>
 *  <tr><td><code>google.api.key</code></td><td>Google API key</td></tr>
 * 	<tr><td><code>push.google.gcm.host</code></td><td>URL for the GCM host</td></tr>
 * 	<tr><td><code>push.google.gcm.dryRun</code></td><td>If true, GCM will not send the push to the actual device, but still validate as if it was sent</td></tr>
 *
 *
 * </table>
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.2.0
 */
public class AndroidSendService implements SendService {

	/** A reference to this class' logger */
	private static final Logger LOG = LoggerFactory.getLogger(AndroidSendService.class);

	private static final int BATCH_SIZE = 1000;

	/** Semaphore used to limit the max allowed number of active connections */
	private final Semaphore androidConnectionLimit = new Semaphore(10, true);

    /**
     * Check that the request contains a registration ID (either in the registration_id
     * parameter in a plain text message, or in the registration_ids field in JSON).
     */
    public static final String GCM_ERROR_MISSING_REGISTRATION = "MissingRegistration";

    /**
     * Check the formatting of the registration ID that you pass to the server. Make sure
     * it matches the registration ID the phone receives in the
     * com.google.android.c2dm.intent.REGISTRATION intent and that you're not truncating
     * it or adding additional characters.
     */
    public static final String GCM_ERROR_INVALID_REGISTRATION = "InvalidRegistration";

    /**
     * A registration ID is tied to a certain group of senders. When an application
     * registers for GCM usage, it must specify which senders are allowed to send
     * messages. Make sure you're using one of those when trying to send messages to
     * the device. If you switch to a different sender, the existing registration IDs won't work.
     */
    public static final String GCM_ERROR_MISMATCH_SENDER = "MismatchSenderId";

    /**
     * An existing registration ID may cease to be valid in a number of scenarios, including:
     * - If the application manually unregisters by issuing a com.google.android.c2dm.intent.UNREGISTER intent.
     * - If the application is automatically unregistered, which can happen (but is not guaranteed) if the user uninstalls the application.
     * - If the registration ID expires. Google might decide to refresh registration IDs.
     * - If the application is updated but the new version does not have a broadcast receiver configured to
     *   receive com.google.android.c2dm.intent.RECEIVE intents.
     * For all these cases, you should remove this registration ID from the 3rd-party server and stop using it to send messages.
     */
    public static final String GCM_ERROR_UNREGISTERED_DEVICE = "NotRegistered";

    /**
     * The total size of the payload data that is included in a message can't exceed 4096 bytes.
     * Note that this includes both the size of the keys as well as the values.
     */
    public static final String GCM_ERROR_MESSAGE_TOO_BIG = "MessageTooBig";

    /**
     * The payload data contains a key (such as from or any value prefixed by google.)
     * that is used internally by GCM in the com.google.android.c2dm.intent.RECEIVE
     * Intent and cannot be used. Note that some words (such as collapse_key) are also
     * used by GCM but are allowed in the payload, in which case the payload value will
     * be overridden by the GCM value.
     */
    public static final String GCM_ERROR_INVALID_DATA_KEY = "InvalidDataKey";

    /**
     * The value for the Time to Live field must be an integer representing a duration in
     * seconds between 0 and 2,419,200 (4 weeks)
     */
    public static final String GCM_ERROR_INVALID_TTL = "InvalidTtl";

    /**
     * The server encountered an error while trying to process the request. You could retry the same request
     * (obeying the requirements listed in the Timeout section), but if the error persists, please report the problem in the android-gcm group.
     * Happens when the HTTP status code is 500, or when the error field of a JSON object in the results array is InternalServerError.
     */
    public static final String GCM_ERROR_INTERNAL_SERVER_ERROR = "InternalServerError";

    /**
     * A message was addressed to a registration ID whose package name did not match the value passed in the request.
     */
    public static final String GCM_ERROR_INVALID_PACKAGE_NAME = "InvalidPackageName";


    public static final String GCM_FIELD_REGISTRATION_ID = "registration_id";
    public static final String GCM_FIELD_REGISTRATION_IDS = "registration_ids";
    public static final String GCM_FIELD_ERROR = "error";
    public static final String GCM_FIELD_RESULTS = "results";
    public static final String GCM_FIELD_MESSAGE_ID = "message_id";
    public static final String GCM_FIELD_DRY_RUN = "dry_run";
    public static final String GCM_FIELD_DATA = "data";


    /**
     * A reference to the device service
     */
    @Autowired
    private DeviceService deviceService;

    /**
     * Authentication key for this project from the google developer website.
     * To set this property automatically by spring injection, set the
     * <code>google.api.key</code> property in spring.
     */
    @Value("${google.api.key}")
    private String auth;

    /**
     * URL of the GCM host
     * To set this property automatically by spring injection, set the
     * <code>push.google.gcm.host</code> property in spring.
     */
    @Value("${push.google.gcm.host}")
    private String sendURL;

    /**
     * Flag if the push should be a dry run
     */
    @Value("${push.google.gcm.dryRun}")
    private Boolean dryRun = false;


    /**
     *
     * @param push The <code>Push</code> message to send.
     * @param device
     */

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
	public void sendPush(Push push, Collection<Device> devices) {//CodeReview would be good to implement
		if (devices != null){
			List<String> regIds = new ArrayList<String>();
			for (Device device : devices) {
				regIds.add(device.getRegId());
				//check if max batch reached then sendPush
				if(regIds.size() >= BATCH_SIZE) {
					this.executePush(push, regIds);
					//re-initialize regIDs and continue looping
					regIds.clear();
				}
			}
			//check if there are any remaining and process
			if(regIds.size()>0){
				this.executePush(push, regIds);
			}
		} else {
			LOG.info("No devices available");
		}
	}


	/**
	 * Attempts to send a push notification to the specified registration IDs.
	 * If the connection limit is currently reached, the call to this method will cause the 
	 * current thread to block until a connection is available.
	 * @param push
	 * @param registrationIds
	 */
	private void executePush(Push push , List<String> registrationIds){
		HttpsURLConnection conn =null;
		try {
			this.androidConnectionLimit.acquire(); // Attempt to acquire a lock for a connection
			conn = createConnection();
			JSONObject request = buildJsonMessage(push, registrationIds);
			String responseData = sendToGCM(conn, request.toString());
            JSONObject response = JSONObject.fromObject(responseData);

			if (LOG.isDebugEnabled()){
				LOG.debug("Response from GCM : " + responseData);
			}
            handleResponse(request, response);
		} catch (Exception e) {
			LOG.error("Exception while trying to send notification", e);
		}
		finally{
			this.androidConnectionLimit.release();
			if (conn != null){
				conn.disconnect();
			}
		}
	}

    /**
     * Handles the GCM response
     * This method is default accessibility to have it work with unit tests, this method should not be called
     * directly elsewhere
     * @param request JSON request sent to GCM
     * @param response JSON response from GCM
     */
    /* default */ void handleResponse(JSONObject request, JSONObject response){
        JSONArray results = response.getJSONArray(GCM_FIELD_RESULTS);

        for(int idx = 0 ; idx < results.size() ; idx++){
            JSONObject result = results.getJSONObject(idx);
            String registrationId = request.getJSONArray(GCM_FIELD_REGISTRATION_IDS).getString(idx);

            /*
             * If the result has the registration_id field, we need to update the device in our database
             * to rather use the device ID which GCM recommends for the device to avoid having future
             * messages rejected.
             */
            if(result.has(GCM_FIELD_REGISTRATION_ID)){
                String newRegId = result.getString(GCM_FIELD_REGISTRATION_ID);
                LOG.debug("Need to update registration id from : " + registrationId + " to " + newRegId);
                Device device = deviceService.findDeviceByRegId(registrationId);
                if(device!= null){
                    device.setRegId(newRegId);
                    deviceService.saveDevice(device);
                }
            }

            // If something went wrong
            if(result.has(GCM_FIELD_ERROR)){
                LOG.debug("There was an error sending message to registration id : " + registrationId);
                String error = result.getString(GCM_FIELD_ERROR);

                if(GCM_ERROR_MISSING_REGISTRATION.equals(error)){
                    LOG.debug("No registration id was set, registration id attempted was : \"" + registrationId + "\"");
                    // Not sure what to do here, just don't send messages without the device id !
                }

                if(GCM_ERROR_INVALID_REGISTRATION.equals(error)){
                    LOG.debug("Invalid registration id was sent : \"" + registrationId + "\". Removing device...");
                    Device device = deviceService.findDeviceByRegId(registrationId);
                    if(device!= null){
                        deviceService.removeDevice(device);
                    }
                }

                if(GCM_ERROR_MISMATCH_SENDER.equals(error)){
                    LOG.debug("Sender ID mismatch : \"" + registrationId + "\". Please check your GCM auth token"); // Didn't log auth token for security reasons
                    /*
                     * The native application's allowed sender ids does not match the sender id
                     * bind to our auth token. We could either delete these devices, or it could
                     * be a configuration problem where the auth token is incorrect. Just deleting all
                     * these devices could mean if you accidentally have your auth token wrong in production
                     * all devices could be deleted unintentionally.
                     */
                }

                if(GCM_ERROR_UNREGISTERED_DEVICE.equals(error)){
                    LOG.debug("Device is not registered to receive push notifications : \"" + registrationId + "\". Removing device...");
                    Device device = deviceService.findDeviceByRegId(registrationId);
                    if(device!= null){
                        deviceService.removeDevice(device);
                    }
                }

                if(GCM_ERROR_MESSAGE_TOO_BIG.equals(error)){
                    LOG.debug("The message sent was too big!");
                    // Don't send such big messages! Rather let this service force the message to be truncated
                }

                if(GCM_ERROR_INVALID_DATA_KEY.equals(error)){
                    LOG.debug("An invalid data key.");
                    // This should never happen, because we have a set data key that never changes.
                }

                if(GCM_ERROR_INVALID_TTL.equals(error)){
                    LOG.debug("Time to Live value is invalid");
                    // This should never happen, because we use default TTL
                }

                if(GCM_ERROR_INTERNAL_SERVER_ERROR.equals(error)){
                    LOG.debug("Internal error happened on GCM");
                    // TODO set retry to later
                }

                if(GCM_ERROR_INVALID_PACKAGE_NAME.equals(error)){
                    LOG.debug("The device's native application package name does not match our auth token binded package id");
                    // Not much we can do here, we need to get the correct native application to the user.
                }


            }else if(result.has(GCM_FIELD_MESSAGE_ID)){
                LOG.debug("Message successfully sent to device: " + registrationId);
            }
        }
    }

	/**
	 * Creates a connection to the GCM server
	 * @return The connection to the GCM server
	 * @throws java.net.MalformedURLException
	 * @throws java.io.IOException
	 */
	private HttpsURLConnection createConnection() throws IOException{
		HttpsURLConnection conn;
		//retrieve the connection from the pool.
		conn = (HttpsURLConnection) new URL(this.sendURL).openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "key=" + this.auth);
		return conn;
	}


	/**
	 * Builds a json formatted message string using the push message and device specified.
	 * @param push Push message to send
	 * @param registrationIds Registration ID to send messages too
	 * @return
	 */
	/* default */ JSONObject buildJsonMessage(Push push, List<String> registrationIds  ){
		String emer = (push.getEmergency()) ? "YES" : "NO";
		JSONObject jsonData = new JSONObject();
		jsonData.accumulate("id", push.getPushId().toString());
		jsonData.accumulate("message", push.getMessage());
		jsonData.accumulate("title", push.getTitle());
		jsonData.accumulate("url", ( push.getUrl() == null ? "" : push.getUrl() ) );
		jsonData.accumulate("emer", emer);

		JSONObject jsonObject = new JSONObject();
		JSONArray jsa = new JSONArray();
		for(String registrationID : registrationIds){
			jsa.add(registrationID);
		}
		jsonObject.accumulate(GCM_FIELD_REGISTRATION_IDS,jsa); // GCM supports sending to more than one device (up to 1000 ids)
        if(this.dryRun != null &&  this.dryRun== true){
            jsonObject.accumulate(GCM_FIELD_DRY_RUN, true);
        }


		jsonObject.accumulate(GCM_FIELD_DATA, jsonData.toString());
		return jsonObject;
	}


	/**
	 * Writes the data over the connection
	 * @param conn
	 * @param data
	 * @return
	 * @throws java.net.MalformedURLException
	 * @throws java.io.IOException
	 */
	private static String sendToGCM(HttpsURLConnection conn, String data) throws IOException{
		byte[] dataBytes = data.getBytes();
		conn.setFixedLengthStreamingMode(dataBytes.length);
		OutputStream out = null;
		String response = null;
		try {
			out = conn.getOutputStream();
			out.write(dataBytes);
			out.flush();
			response = readResponse(conn);

		}
		catch (IOException e) {
			LOG.warn("Exception while trying to write data to GCM", e);
		}
		finally{
			IOUtils.closeQuietly(out);
            conn.disconnect();
		}
		return response;
	}


	/**
	 * Reads the response from the GCM server.
	 * @param conn Connection to read the response from
	 * @return The JSON formatted response from GCM
	 */
	private static String readResponse(HttpsURLConnection conn){
		InputStreamReader ir = null;
		String jsonResponse = null;
		try{
			jsonResponse = IOUtils.toString(conn.getInputStream());
		}
		catch (IOException e) {
			LOG.warn("Exception while trying to read input", e);
		}
		finally{
			IOUtils.closeQuietly(ir);
		}
		return jsonResponse;
	}

}
