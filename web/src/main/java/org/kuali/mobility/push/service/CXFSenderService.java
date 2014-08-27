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


package org.kuali.mobility.push.service;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.kuali.mobility.push.entity.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Implementation of the CXF Sender Service
 * 
 * @author Kuali Mobility Team (mobility.dev@kuali.org)
 * @since 3.0
 */
@Service
public class CXFSenderService {

	/** A reference to a logger for this service */
	private static final Logger LOG = LoggerFactory.getLogger(CXFSenderService.class);
	
	@Resource(name="kmeProperties")
	private Properties kmeProperties;
	
	/** A reference to the SenderService */
	@Autowired
	private SenderService senderService;

	
	@GET
	@Path("/all")
	public String getAllSenders(){
		String results = "{\"senders\":[SENDERS_ARRAY]}";
		List<Sender> senders = this.getSenderService().findAllSenders();
		LOG.info(senders.size() + " SENDERS");
		Iterator<Sender> it = senders.iterator();
		String senderArray = "";
		while(it.hasNext()){
			senderArray += it.next().toJson() + ",";
		}
		senderArray = senderArray.substring(0, senderArray.length()-1);
		LOG.info(senderArray);
		results = results.replaceAll("SENDERS_ARRAY",senderArray);
		LOG.info(results);
		return results;
	}
	
	@GET
	@Path("/byId/{id}")
	public String getSenderById(@PathParam("id") final String id){
		Sender sender = this.getSenderService().findSenderById(Long.parseLong(id));
		LOG.info(sender.toString());
		return sender.toJson();
	}
	
	@GET
	@Path("/byShortName/{shortname}")
	public String getSenderByShortName(@PathParam("shortname") final String shortname){
		Sender sender = this.getSenderService().findSenderByShortName(shortname);
		LOG.info(sender.toString());
		return sender.toJson();
	}

	@GET
	@Path("/byKey/{key}")
	public String getSenderByKey(@PathParam("key") final String key){
		Sender sender = this.getSenderService().findSenderBySenderKey(key);
		LOG.info(sender.toString());
		return sender.toJson();
	}

	@GET
	@Path("/delete/byId/{id}")
	public Response deleteSenderById(@PathParam("id") final String id){
		boolean result = this.getSenderService().removeSender(Long.parseLong(id));
		if(result){
			return Response.status(Response.Status.OK.getStatusCode()).build();
		}else{
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		}  
	}

	@POST
	@Path("/delete")
	public Response deleteSender(@RequestBody String data){
		LOG.info("---- DATA: " + data);
		if (data == null) {
			return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
		}

		JSONObject queryParams;
		try {
			queryParams = (JSONObject) JSONSerializer.toJSON(data);
			LOG.info(queryParams.toString());
		} catch (JSONException je) {
			LOG.error("JSONException in :" + data + " : " + je.getMessage());
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		}
		
		if(!"".equals(queryParams.getString("id"))){
			try{
				if(this.getSenderService().removeSender(Long.parseLong(queryParams.getString("id")))){
					return Response.status(Response.Status.OK.getStatusCode()).build();
				}
			}catch(Exception e){
				LOG.error("Exception while trying to update device", e);
				return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
			}
		}else{
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		}
		return Response.status(Response.Status.OK.getStatusCode()).build();
	}
	
	@POST
	@Path("/save")
	public Response saveSender(@RequestBody String data){
		LOG.info("---- DATA: " + data);
		if (data == null) {
			return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
		}

		JSONObject queryParams;
		try {
			queryParams = (JSONObject) JSONSerializer.toJSON(data);
			LOG.info(queryParams.toString());
		} catch (JSONException je) {
			LOG.error("JSONException in :" + data + " : " + je.getMessage());
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		}
		
		try {
			if("".equals(queryParams.getString("id"))){
				Sender sender = new Sender();
				sender.setName(queryParams.getString("name"));
				sender.setShortName(queryParams.getString("shortName"));
				sender.setDescription(queryParams.getString("description"));
				sender.setSenderKey(queryParams.getString("senderKey"));
				sender.setHidden(queryParams.getBoolean("hidden"));
				sender.setPostedTimestamp(new Timestamp(System.currentTimeMillis()));
				this.getSenderService().saveSender(sender);
			}else{
				Sender temp = this.getSenderService().findSenderById(Long.parseLong(queryParams.getString("id")));
				if(temp != null){
					LOG.info("-----Sender already exists." + temp.toString());
					temp.setName(queryParams.getString("name"));
					temp.setShortName(queryParams.getString("shortName"));
					temp.setDescription(queryParams.getString("description"));
					temp.setSenderKey(queryParams.getString("senderKey"));
					temp.setHidden(queryParams.getBoolean("hidden"));
					temp.setPostedTimestamp(new Timestamp(System.currentTimeMillis()));
					this.getSenderService().saveSender(temp);
				}
			}
		} catch (Exception e) {
			LOG.error("Exception while trying to update device", e);
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		}
		
		return Response.status(Response.Status.OK.getStatusCode()).build();
	}
	
	
	/**
	 * Creates and returns an alphanumeric key of length specified. Checks key for uniqueness before returning. 
	 * @return String
	 */
	@GET
	@Path("/key")
	public String getKey(){
		int keyLength = Integer.valueOf(getKmeProperties().getProperty("push.sender.default.key.length", "20"));
		String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String text = "";
		do{
			text = "";
			for( int i = 0; i < keyLength; i++ ){
				int j = (int) Math.floor(Math.random() * possible.length());
				text += possible.charAt(j);
			}
		}while(this.getSenderService().isValidSenderKey(text));
		return text;		
	}
	
	
	
	public Properties getKmeProperties() {
		return kmeProperties;
	}

	public void setKmeProperties(Properties kmeProperties) {
		this.kmeProperties = kmeProperties;
	}

	public SenderService getSenderService() {
		return senderService;
	}

	public void setSenderService(SenderService senderService) {
		this.senderService = senderService;
	}
	
	
	
}
