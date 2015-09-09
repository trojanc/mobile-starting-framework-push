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

import org.apache.commons.beanutils.BeanUtils;
import org.kuali.mobility.push.entity.Sender;
import org.kuali.mobility.push.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;

/**
 * Created by charl on 2014/07/14.
 */

public class SenderServiceRestImpl implements SenderServiceRest {

	@Autowired
	@Qualifier("senderService")
	private SenderService senderService;


	@Resource(name="kmeProperties")
	private Properties kmeProperties;

	@Override
	@GET
	@Path("/all")
	public List<Sender> findAllUnhiddenSenders() {
		return senderService.findAllUnhiddenSenders();
	}




	@GET
	@Path("/byId/{id}")
	public Sender getSenderById(@PathParam("id") final String id){
		Sender sender = senderService.findSenderById(Long.parseLong(id));
		return sender;
	}

	@GET
	@Path("/byShortName/{shortname}")
	public Sender getSenderByShortName(@PathParam("shortname") final String shortname){
		Sender sender = senderService.findSenderByShortName(shortname);
		return sender;
	}

	@GET
	@Path("/byKey/{key}")
	public Sender getSenderByKey(@PathParam("key") final String key){
		Sender sender = senderService.findSenderBySenderKey(key);
		return sender;
	}

	@DELETE
	@Path("/delete/byId/{id}")
	public Response deleteSenderById(@PathParam("id") final String id){
		boolean result = senderService.removeSender(Long.parseLong(id));
		if(result){
			return Response.status(Response.Status.OK.getStatusCode()).build();
		}else{
			return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
		}
	}

	@POST
	@Path("/save")
	public Response saveSender(Sender sender){
		senderService.saveSender(sender);
		return Response.status(Response.Status.OK.getStatusCode()).build();
	}

	/**
	 * Creates and returns an alphanumeric key of length specified. Checks key for uniqueness before returning.
	 * @return String
	 */
	@GET
	@Path("/key")
	public String getKey(){
		int keyLength = Integer.valueOf(kmeProperties.getProperty("push.sender.default.key.length", "20"));
		String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String text = "";
		do{
			text = "";
			for( int i = 0; i < keyLength; i++ ){
				int j = (int) Math.floor(Math.random() * possible.length());
				text += possible.charAt(j);
			}
		}while(senderService.isValidSenderKey(text));
		return text;
	}

	public void setSenderService(SenderService senderService){
		this.senderService = senderService;
	}
}
