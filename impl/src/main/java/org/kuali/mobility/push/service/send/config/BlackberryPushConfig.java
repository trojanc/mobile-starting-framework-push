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


package org.kuali.mobility.push.service.send.config;

import net.rim.pushsdk.commons.PushSDKProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Properties;

/**
 * Class to act as the configuration require for BlackBerry Push notifications.
 * This class only implements public messaging (not BES or BDS)
 *
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 2.2.0
 */
public class BlackberryPushConfig implements PushSDKProperties {

	/** Flag if this config is for the evaluation of Blackberry Push Service */
	private boolean eval = true;

	/**
	 * Application ID
	 */
	private String applicationId;

	/**
	 * Content provider ID
	 */
	private String cpid = "";

	/**
	 * Base PPG URL
	 */
	private String ppgBaseUrl;

	/**
	 * URL to send Push notifications too
	 */
	private String pushUrl;

	/**
	 * A reference to the KME Properties
	 */
	@Autowired
	@Qualifier("kmeProperties")
	private Properties kmeProperties;

	/**
	 * Maximum number of attempts to generate a unique push id before giving up
	 */
	// Property : regenerate.pushid.max.attempts
	private int regeneratePushIdMaxAttempts = 5;

	/**
	 * The default deliver before timestamp is the current time plus this offset in milliseconds.
	 * Required for low level APIs. This is only used in the low-level PAP component, if no expiry date is specified through the push API.
	 * The high-level push uses the push application's defaultPushLiveTime, if not specified in the API through PushParameters.
	 */
	// Property : default.deliver.before.timestamp.offset
	private long defaultDeliverBeforeTimestampOffset = 3600000L;

	/**
	 * List of parser special characters that should be avoided when specifying values of PAP message elements.
 	 * For example, the characters below are not allowed for the pushId element of the PAP XML.
 	 * Required for low level APIs
	 */
	// Property :
	private char[] parserSpecialCharacters = new char[] {'&','"',':','<'};

	/**
	 *  Maximum number of SQL IN clause parameters allowed by the DB implementation being used (i.e. Oracle limit is 1000)
	 */
	// Property : "parser.special.characters
	private int maxInClauseValues = 1000;

	/**
	 * Push submission URL of the public/BIS push server. If you are not using a public/BIS PPG, this value should not be set.
	 * Required for low level APIs (if using a BIS push server)
	 */
	// Property : public.ppg.address
	private String publicPpgAddress = "/mss/PD_pushRequest";

	/**
	 * Push submission URL of the enterprise/BES central push server. If you are not using an enterprise/BES PPG, this value should not be set.
 	 * Note: For backwards compatibility this URL may be a BDS PPG URL if you wish to use your existing push initiator unchanged to push to BB10 devices.
	 * Required for low level APIs (if using a BES push server)
	 */
	// Property : enterprise.ppg.address
	private String enterprisePpgAddress = "/pap";

	/**
	 * Push submission URL of the enterprise/BDS central push server. If you are not using an enterprise/BDS PPG, this value should not be set.
	 * Required for low level APIs (if using a BDS push server)
	 */
	// Property : bds.ppg.address
	private String BDSPpgAddress = "/pap";

	/**
	 * Maximum size of the result notification queue before it starts rejecting new notifications
	 */
	// Property : acknowledgement.max.queue.size
	private int acknowledgementMaxQueueSize = 100000;

	/**
	 * Maximum number of threads processing the internal result notification queue and notifying the listeners
	 */
	// Property : acknowledgement.max.threads
	private int acknowledgementMaxThreads = 20;

	/**
	 * Maximum number of result notifications in a batch for processing
	 */
	// Property : acknowledgement.batch.size
	private int acknowledgementBatchSize = 500;

	/**
	 * Time in milliseconds to delay, after looking up and not finding the push request detail that corresponds to
	 * a result notification, before retrying the lookup.
	 * This property is needed for the case where the result notification has come back before the push request
	 * detail has been committed to storage.
	 * Note: This property only applies to push applications that have the "store push requests" flag set to true.
	 */
	// Property : acknowledgement.push.lookup.retry.delay
	private int acknowledgementPushLookupRetryDelay = 5000;

	/**
	 * When the number of subscribers to validate are below this water mark an optimized query for small number of subscribers will be used;
	 * conversely when above this number a second optimized query for a large number of subscribers will be used
	 */
	// Property : subscription.validation.high.water.mark
	private int subscriptionValidationHighWaterMark = 100000;

	/**
	 * Number of subscribers to load at once from the database to validate. Making this number too large may exceed available system memory.
	 * Making this number too small will decrease performance as more calls to the persistent store will be required.
	 * Important: This number should never be zero! One must be the minimum value
	 */
	// Property : subscription.validation.batch.size
	private int subscriptionValidationBatchSize = 100000;

	/**
	 * Subscriber Deregistration URL for public/BIS push
	 * Note: If you are not using a public/BIS PPG, this value should not be set.
	 */
	// Property : subscription.deregistration.url
	private String subscriptionDeregistrationUrl = "/mss/PD_cpDeregUser?pin=";

	/**
	 * Suspend Subscription URL for public/BIS push
	 * Note: If you are not using a public/BIS PPG, this value should not be set.
	 */
	// Property : subscription.suspend.url
	private String subscriptionSuspendUrl = "/mss/PD_cpSub?cpAction=suspend&pin=";

	/**
	 * Resume Subscription URL for public/BIS push
	 * Note: If you are not using a public/BIS PPG, this value should not be set.
	 */
	// Property : subscription.resume.url
	private String subscriptionResumeUrl = "/mss/PD_cpSub?cpAction=resume&pin=";

	/**
	 * Maximum number of threads to use for large subscription validation/subscription matching
	 */
	// Property : subscription.matching.max.threads
	private int subscriptionMatchingMaxThreads = 5;

	/**
	 * The max. number of results returned by a subscriber find operation. Making this value too large may exceed available system memory.
	 */
	// Property : subscription.find.max.results
	private int subscriptionFindMaxResults = 100000;

	/**
	 * The max. number of results returned by a push request detail find operation. Making this value too large may exceed available system memory.
	 */
	// Property : push.request.detail.find.max.results
	private int pushRequestDetailFindMaxResults = 100000;

	/**
	 * Defines the type of database that the SDK is working with. The options are: mysql, oracle.
	 */
	// Property : database.type
	private String databaseType = "mysql";

	/**
	 * DTD declaration to use when constructing XML to send to the public/BIS PPG (this property is not related to parsing XML sent from the PPG).
	 * Important: This property should not be changed unless the PPG changes the DTD it is using first!
	 * Required for low level APIs (if using BIS push server)
	 */
	// Property : dtd.declaration.public
	private String dtdDeclarationPublic = "<!DOCTYPE pap PUBLIC \"-//WAPFORUM//DTD PAP 2.1//EN\" \"http://www.openmobilealliance.org/tech/DTD/pap_2.1.dtd\">";

	/**
	 * DTD declaration to use when constructing XML to send to the enterprise/BES and enterprise/BDS PPGs (this property is not related to parsing XML sent from the PPG).
	 * Important: This property should not be changed unless the PPG changes the DTD it is using first!
	 * Required for low level APIs (if using BES or BDS push server)
	 */
	// Property : dtd.declaration.enterprise
	private String dtdDeclarationEnterprise = "<!DOCTYPE pap PUBLIC \"-//WAPFORUM//DTD PAP 2.0//EN\" \"http://www.wapforum.org/DTD/pap_2.0.dtd\" [<?wap-pap-ver supported-versions=\"2.0\"?>]>";

	/**
	 * Connection timeout in milliseconds
	 * Required for low level APIs
	 */
	// Property : http.connection.timeout
	private int httpConnectionTimeout = 60000;

	/**
	 * Read timeout in milliseconds
	 * Required for low level APIs
	 */
	// Property : http.read.timeout
	private int httpReadTimeout = 120000;

	/**
	 * Whether to use persistent connections (true or false)
 	 * Required for low level APIs
	 */
	// Property : http.is.persistent
	private boolean httpIsPersistent = true;

	/**
	 *  Queue size to use during unsubscribes for a hard application delete.
	 *  The queue will block and wait if the threads working on the unsubscribes cannot keep up with the queue filling up.
	 */
	// Property : pushapp.delete.unsubscribe.queuesize
	private int pushAppDeleteUnsubscribeQueueSize = 100000;

	/**
	 *  The amount of time to wait (in minutes) for the unsubscribing of subscribers
	 *  for a hard application delete before timing out and throwing an exception.
	 */
	// Property : pushapp.delete.unsubscribe.timeout
	private int pushAppDeleteUnsubscribeTimeout = 30;

	/**
	 *  The amount of time to wait (in minutes) for the deletion of subscribers
	 *  for a hard application delete before timing out and throwing an exception.
	 */
	// Property : pushapp.subscriber.delete.timeout
	private int pushAppSubscriberDeleteTimeout = 10;

	/**
	 *  Queue size to use for large subscription validation/subscription matching
	 *  The queue will block and wait if the threads working on the subscription validation/matching cannot keep up with the queue filling up.
	 */
	// Property : subscription.matching.queuesize
	private int subscriptionMatchingQueueSize = 50000;

	/**
	 *  When syncing up the status of subscribers with the PPG, the batch size to use for each sync request.
	 *  Important: The max. number of subscribers to include in each sync request is actually defined by the PPG.
	 *  It is present here in case it changes on the PPG end and has to be updated here on the SDK side.
	 *  Note: If you are not using a public/BIS PPG, this property will be ignored.
	 */
	// Property : subscription.ppg.sync.batch.size
	private int subscriptionPPGSyncBatchSize = 10000;

	/**
	 * Subscription query URL for public/BIS push
	 * Note: If you are not using a public/BIS PPG, this value should not be set.
	 * Required for low level APIs (if using a BIS push server)
	 */
	// Property : subscription.query.url
	private String subscriptionQueryUrl = "/mss/PD_cpSubQuery";

	/**
	 *  Queue size to use for syncing up the status of subscribers with the PPG.
	 *  The queue will block and wait if the threads working on the subscription status syncing cannot keep up with the queue filling up.
	 *  Note: If you are not using a public/BIS PPG, this property will be ignored.
	 */
	// Property : subscription.ppg.sync.queuesize
	private int subscriptionPPGSyncQueueSize = 5000;

	/**
	 *  The amount of time to wait (in minutes) for the syncing up of the status of subscribers with the PPG before timing out
	 *  and throwing an exception.
	 *  Note: If you are not using a public/BIS PPG, this property will be ignored.
	 */
	// Property : subscription.ppg.sync.timeout
	private int subscriptionPPGSyncTimeout = 30;

	/**
	 * Maximum number of threads to use for the syncing up of the status of subscribers with the PPG.
	 * Note: If you are not using a public/BIS PPG, this property will be ignored.
	 */
	// Property : subscription.ppg.sync.threads
	private int subscriptionPPGSyncMaxThreads = 5;

	/**
	 *  The frequency (in seconds) with which push statistics are updated in storage.  All counts and content sums will be kept in memory until
	 *  a batch update is done to storage at the specified interval.  As a result, there might be slight inconsistencies in the stats until
	 *  the update takes place.  Setting a larger value for this property increases the chances for inconsistency.  Setting a smaller value
	 *  guarantees more regular updates, but will impact performance more significantly.
	 */
	// Property : push.stats.update.frequency
	private int pushStatsUpdateFrequency = 120;

	/**
	 * The max. number of push statistics in the queue waiting to be sent for a batch update.
	 * If the queue has reached its max. size, it will block until the size of the queue goes down again.
	 */
	// Property : push.stats.update.queuesize
	private int pushStatsUpdateQueueSize = 100000;

	/**
	 * Area from the push notifications url.
	 * Only required for production.
	 * Valid values are: na, ?
	 */
	private String pushArea = "na";

	/**
	 * Method to initialise this class
	 */
	public void initialize(){
		this.setApplicationId(getKmeProperties().getProperty("push.blackberry.appId"));
		this.setEval(Boolean.valueOf(getKmeProperties().getProperty("push.blackberry.eval","true")));
	}
	/**
	 * Returns the base url for blackberry urls
	 * @return
	 */
	private String getBasePpgUrl(){
		if (this.ppgBaseUrl == null){
			if (this.eval){
				this.ppgBaseUrl = "https://"+this.cpid+".pushapi.eval.blackberry.com";
			}
			else{
				this.ppgBaseUrl = "https://"+this.cpid+".pushapi.blackberry.com";
			}
		}
		return ppgBaseUrl;
	}

	/**
	 * Returns the url which should be used by the native app to open a push notifications listener
	 * @return
	 */
	public String getPushUrl(){
		if (this.pushUrl == null){
			if (this.eval){
				this.pushUrl = "http://cpXXX.pushapi.eval.blackberry.com";
			}
			else{
				this.pushUrl = "http://cp"+this.cpid+".pushapi." + this.pushArea + ".blackberry.com";
			}
		}
		return this.pushUrl;

	}

	/**
	 *
	 * @return
	 */
	public final String getBaseBdsUrl(){
		throw new IllegalArgumentException("Method not implemented");
	}

	/**
	 *
	 * @return
	 */
	public final String getBaseBesUrl(){
		throw new IllegalArgumentException("Method not implemented");
	}

	/**
	 * @see {@link #acknowledgementMaxQueueSize}
	 */
	public int getAcknowledgementMaxQueueSize(){
		return this.acknowledgementMaxQueueSize;
	}

	/**
	 * @see {@link #acknowledgementMaxThreads}
	 */
	public int getAcknowledgementMaxThreads(){
		return this.acknowledgementMaxThreads;
	}

	/**
	 * @see {@link #acknowledgementBatchSize}
	 */
	public int getAcknowledgementBatchSize() {
		return this.acknowledgementBatchSize;
	}

	/**
	 * @see {@link #acknowledgementPushLookupRetryDelay}
	 */
	public int getAcknowledgementPushLookupRetryDelay(){
		return this.acknowledgementPushLookupRetryDelay;
	}

	/**
	 * @see {@link #databaseType}
	 */
	public String getDatabaseType(){
		return this.databaseType;
	}

	/**
	 * @see {@link #defaultDeliverBeforeTimestampOffset}
	 */
	public long getDefaultDeliverBeforeTimestampOffset() {
		return defaultDeliverBeforeTimestampOffset;
	}

	/**
	 * @see {@link #maxInClauseValues}
	 */
	public int getMaxInClauseValues(){
		return this.maxInClauseValues;
	}

	/**
	 * @see {@link #publicPpgAddress}
	 */
	public String getPublicPpgAddress() {
		return this.getBasePpgUrl() + this.publicPpgAddress;
	}

	/**
	 * @see {@link #enterprisePpgAddress}
	 */
	public String getEnterprisePpgAddress(){
		return this.getBaseBesUrl() + this.enterprisePpgAddress;
	}


	/**
	 * @see {@link #BDSPpgAddress}
	 */
	public String getBDSPpgAddress(){
		return this.getBaseBdsUrl() + this.BDSPpgAddress;
	}

	/**
	 * @see {@link #pushRequestDetailFindMaxResults}
	 */
	public int getPushRequestDetailFindMaxResults() {
		return this.pushRequestDetailFindMaxResults;
	}

	/**
	 * @see {@link #regeneratePushIdMaxAttempts}
	 */
	public int getRegeneratePushIdMaxAttempts(){
		return this.regeneratePushIdMaxAttempts;
	}

	/**
	 * @see {@link #subscriptionDeregistrationUrl}
	 */
	public String getSubscriptionDeregistrationUrl(){
		return this.getBasePpgUrl() + this.subscriptionDeregistrationUrl;
	}

	/**
	 * @see {@link #subscriptionFindMaxResults}
	 */
	public int getSubscriptionFindMaxResults(){
		return this.subscriptionFindMaxResults;
	}

	/**
	 * @see {@link #subscriptionMatchingMaxThreads}
	 */
	public int getSubscriptionMatchingMaxThreads(){
		return this.subscriptionMatchingMaxThreads;
	}

	/**
	 * @see {@link #subscriptionResumeUrl}
	 */
	public String getSubscriptionResumeUrl(){
		return this.getBasePpgUrl() + this.subscriptionResumeUrl;
	}

	/**
	 * @see {@link #subscriptionSuspendUrl}
	 */
	public String getSubscriptionSuspendUrl(){
		return this.getBasePpgUrl() + this.subscriptionSuspendUrl;
	}

	/**
	 * @see {@link #subscriptionValidationBatchSize}
	 */
	public int getSubscriptionValidationBatchSize() {
		return this.subscriptionValidationBatchSize;
	}

	/**
	 * @see {@link #subscriptionValidationHighWaterMark}
	 */
	public int getSubscriptionValidationHighWaterMark() {
		return this.subscriptionValidationHighWaterMark;
	}

	/**
	 * @see {@link #acknowledgementMaxQueueSize}
	 */
	public void setAcknowledgementMaxQueueSize(int value){
		this.acknowledgementMaxQueueSize = value;
	}

	/**
	 * @see {@link #acknowledgementMaxThreads}
	 */
	public void setAcknowledgementMaxThreads(int value){
		this.acknowledgementMaxThreads = value;
	}

	/**
	 * @see {@link #acknowledgementBatchSize}
	 */
	public void setAcknowledgementBatchSize(int value){
		this.acknowledgementBatchSize = value;
	}

	public boolean isEval() {
		return this.eval;
	}

	public void setEval(boolean isEval) {
		this.eval = isEval;
	}

	public String getCpid() {
		return cpid;
	}

	public void setApplicationId(String appID) {
		if(!StringUtils.isEmpty(appID)) {
			final char cHYPHEN = '-';
			if( appID.indexOf( cHYPHEN ) > -1 ) {
				this.cpid = appID.substring(0, appID.indexOf(cHYPHEN));
			}
			this.applicationId = appID;
		}
	}

	public String getApplicationId(){
		return this.applicationId;
	}

	/**
	 * @see {@link #acknowledgementPushLookupRetryDelay}
	 */
	public void setAcknowledgementPushLookupRetryDelay(int value) {
		this.acknowledgementPushLookupRetryDelay = value;
	}

	/**
	 * @see {@link #databaseType}
	 */
	public void setDatabaseType(String value) {
		this.databaseType = value;
	}

	/**
	 * @see {@link #defaultDeliverBeforeTimestampOffset}
	 */
	public void setDefaultDeliverBeforeTimestampOffset(long value) {
		this.defaultDeliverBeforeTimestampOffset = value;
	}

	/**
	 * @see {@link #maxInClauseValues}
	 */
	public void setMaxInClauseValues(int value) {
		this.maxInClauseValues = value;
	}

	/**
	 * @see {@link #publicPpgAddress}
	 */
	public void setPublicPpgAddress(String value) {
		this.publicPpgAddress = value;
	}

	/**
	 * @see {@link #enterprisePpgAddress}
	 */
	public void setEnterprisePpgAddress(String value){
		this.enterprisePpgAddress = value;
	}

	/**
	 * @see {@link #BDSPpgAddress}
	 */
	public void setBDSPpgAddress(String value) {
		this.BDSPpgAddress = value;
	}

	/**
	 * @see {@link #pushRequestDetailFindMaxResults}
	 */
	public void setPushRequestDetailFindMaxResults(int value) {
		this.pushRequestDetailFindMaxResults = value;
	}

	/**
	 * @see {@link #regeneratePushIdMaxAttempts}
	 */
	public void setRegeneratePushIdMaxAttempts(int value) {
		this.regeneratePushIdMaxAttempts = value;
	}

	/**
	 * @see {@link #subscriptionDeregistrationUrl}
	 */
	public void setSubscriptionDeregistrationUrl(String value) {
		this.subscriptionDeregistrationUrl = value;
	}

	/**
	 * @see {@link #subscriptionFindMaxResults}
	 */
	public void setSubscriptionFindMaxResults(int value) {
		this.subscriptionFindMaxResults = value;
	}

	/**
	 * @see {@link #subscriptionMatchingMaxThreads}
	 */
	public void setSubscriptionMatchingMaxThreads(int value) {
		this.subscriptionMatchingMaxThreads = value;
	}

	/**
	 * @see {@link #subscriptionResumeUrl}
	 */
	public void setSubscriptionResumeUrl(String value) {
		this.subscriptionResumeUrl = value;
	}

	/**
	 * @see {@link #subscriptionSuspendUrl}
	 */
	public void setSubscriptionSuspendUrl(String value) {
		this.subscriptionSuspendUrl = value;
	}

	/**
	 * @see {@link #subscriptionValidationBatchSize}
	 */
	public void setSubscriptionValidationBatchSize(int value){
		if (value <= 0){
			this.subscriptionValidationBatchSize = 1;
		}else{
			this.subscriptionValidationBatchSize = value;
		}
	}

	/**
	 * @see {@link #subscriptionValidationHighWaterMark}
	 */
	public void setSubscriptionValidationHighWaterMark(int value) {
		this.subscriptionValidationHighWaterMark = value;
	}

	/**
	 * @see {@link #httpConnectionTimeout}
	 */
	public int getHttpConnectionTimeout() {
		return this.httpConnectionTimeout;
	}

	/**
	 * @see {@link #httpIsPersistent}
	 */
	public boolean getHttpIsPersistent() {
		return this.httpIsPersistent;
	}

	/**
	 * @see {@link #httpReadTimeout}
	 */
	public int getHttpReadTimeout() {
		return httpReadTimeout;
	}

	/**
	 * @see {@link #httpConnectionTimeout}
	 */
	public void setHttpConnectionTimeout(int value) {
		this.httpConnectionTimeout = value;
	}

	/**
	 * @see {@link #httpIsPersistent}
	 */
	public void setHttpIsPersistent(boolean value) {
		this.httpIsPersistent = value;
	}

	/**
	 * @see {@link #httpReadTimeout}
	 */
	public void setHttpReadTimeout(int value){
		this.httpReadTimeout = value;
	}

	/**
	 * @see {@link #pushAppDeleteUnsubscribeQueueSize}
	 */
	public int getPushAppDeleteUnsubscribeQueueSize(){
		return this.pushAppDeleteUnsubscribeQueueSize;
	}

	/**
	 * @see {@link #pushAppDeleteUnsubscribeQueueSize}
	 */
	public void setPushAppDeleteUnsubscribeQueueSize(int size){
		this.pushAppDeleteUnsubscribeQueueSize = size;
	}

	/**
	 * @see {@link #pushAppDeleteUnsubscribeTimeout}
	 */
	public int getPushAppDeleteUnsubscribeTimeout(){
		return this.pushAppDeleteUnsubscribeTimeout;
	}

	/**
	 * @see {@link #pushAppDeleteUnsubscribeTimeout}
	 */
	public void setPushAppDeleteUnsubscribeTimeout(int timeout){
		this.pushAppDeleteUnsubscribeTimeout = timeout;
	}

	/**
	 * @see {@link #pushAppSubscriberDeleteTimeout}
	 */
	public int getPushAppSubscriberDeleteTimeout(){
		return this.pushAppSubscriberDeleteTimeout;
	}

	/**
	 * @see {@link #pushAppSubscriberDeleteTimeout}
	 */
	public void setPushAppSubscriberDeleteTimeout(int timeout){
		this.pushAppSubscriberDeleteTimeout = timeout;
	}

	/**
	 * @see {@link #subscriptionMatchingQueueSize}
	 */
	public int getSubscriptionMatchingQueueSize() {
		return this.subscriptionMatchingQueueSize;
	}

	/**
	 * @see {@link #subscriptionMatchingQueueSize}
	 */
	public void setSubscriptionMatchingQueueSize(int size) {
		this.subscriptionMatchingQueueSize = size;
	}

	/**
	 * @see {@link #subscriptionPPGSyncBatchSize}
	 */
	public int getSubscriptionPPGSyncBatchSize(){
		return this.subscriptionPPGSyncBatchSize;
	}

	/**
	 * @see {@link #subscriptionPPGSyncBatchSize}
	 */
	public void setSubscriptionPPGSyncBatchSize(int size){
		this.subscriptionPPGSyncBatchSize = size;
	}

	/**
	 * @see {@link #subscriptionQueryUrl}
	 */
	public String getSubscriptionQueryUrl(){
		return this.getBasePpgUrl() + this.subscriptionQueryUrl;
	}

	/**
	 * @see {@link #subscriptionQueryUrl}
	 */
	public void setSubscriptionQueryUrl(String url){
		this.subscriptionQueryUrl = url;
	}

	/**
	 * @see {@link #subscriptionPPGSyncQueueSize}
	 */
	public int getSubscriptionPPGSyncQueueSize(){
		return this.subscriptionPPGSyncQueueSize;
	}

	/**
	 * @see {@link #subscriptionPPGSyncQueueSize}
	 */
	public void setSubscriptionPPGSyncQueueSize(int size){
		this.subscriptionPPGSyncQueueSize = size;
	}

	/**
	 * @see {@link #subscriptionPPGSyncTimeout}
	 */
	public int getSubscriptionPPGSyncTimeout() {
		return this.subscriptionPPGSyncTimeout;
	}

	/**
	 * @see {@link #subscriptionPPGSyncTimeout}
	 */
	public void setSubscriptionPPGSyncTimeout(int timeout){
		this.subscriptionPPGSyncTimeout = timeout;
	}


	/**
	 * @see {@link #parserSpecialCharacters}
	 */
	public char[] getParserSpecialCharacters(){
		return  this.parserSpecialCharacters;
	}

	/**
	 * @see {@link #subscriptionPPGSyncMaxThreads}
	 */
	public void setParserSpecialCharacters(char[] value){
		if(value != null){
			this.parserSpecialCharacters = value.clone();
//			this.parserSpecialCharacters = value;
		}
	}

	/**
	 * @see {@link #subscriptionPPGSyncMaxThreads}
	 */
	public int getSubscriptionPPGSyncMaxThreads() {
		return this.subscriptionPPGSyncMaxThreads;
	}

	/**
	 * @see {@link #subscriptionPPGSyncMaxThreads}
	 */
	public void setSubscriptionPPGSyncMaxThreads(int value){
		this.subscriptionPPGSyncMaxThreads = value;
	}

	/**
	 * @see {@link #pushStatsUpdateFrequency}
	 */
	public int getPushStatsUpdateFrequency(){
		return this.pushStatsUpdateFrequency;
	}

	/**
	 * @see {@link #pushStatsUpdateFrequency}
	 */
	public void setPushStatsUpdateFrequency(int value){
		this.pushStatsUpdateFrequency = value;
	}

	/**
	 * @see {@link #pushStatsUpdateQueueSize}
	 */
	public int getPushStatsUpdateQueueSize(){
		return this.pushStatsUpdateQueueSize;
	}

	/**
	 * @see {@link #pushStatsUpdateQueueSize}
	 */
	public void setPushStatsUpdateQueueSize(int value){
		this.pushStatsUpdateQueueSize = value;
	}

	/**
	 * @see {@link #dtdDeclarationPublic}
	 */
	public String getDtdDeclarationPublic(){
		return this.dtdDeclarationPublic;
	}

	/**
	 * @see {@link #dtdDeclarationPublic}
	 */
	public void setDtdDeclarationPublic(String value){
		this.dtdDeclarationPublic = value;
	}


	/**
	 * @see {@link #dtdDeclarationEnterprise}
	 */
	public String getDtdDeclarationEnterprise(){
		return this.dtdDeclarationEnterprise;
	}

	/**
	 * @see {@link #dtdDeclarationEnterprise}
	 */
	public void setDtdDeclarationEnterprise(String value){
		this.dtdDeclarationEnterprise = value;
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
}
