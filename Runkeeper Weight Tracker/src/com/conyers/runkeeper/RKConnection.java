/**
 * 
 */
package com.conyers.runkeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author dconyers
 *
 */
public class RKConnection {

//  This is the post message as captured by fiddler upon longin request.
//	
//	POST /login HTTP/1.1
//	Host: runkeeper.com
//	User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0
//	Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//	Accept-Language: en-us,en;q=0.5
//	Accept-Encoding: gzip, deflate
//	Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
//	Connection: keep-alive
//	Referer: http://runkeeper.com/login
//	Content-Type: application/x-www-form-urlencoded
//	Content-Length: 95
//
//	_eventName=login&redirectUrl=&submit=Submit+Query&email=dogconyers%40yahoo.com&password=dc1on23
	
	private static final Logger logger = LoggerFactory.getLogger(RKConnection.class); 
		
	public static final String URL = "http://runkeeper.com";

	private DefaultHttpClient httpClient;
	private BasicHttpContext httpContext;
	private CookieStore cookieStore;        

	
	public RKConnection() {
		httpClient   = new DefaultHttpClient();
		httpContext  = new BasicHttpContext();
		cookieStore  = new BasicCookieStore();       
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
	}
	
	public boolean connect() {
		boolean _success = false;
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		try {
			_success = requestLogin();
			updateWeight();
		} catch (Exception _exception) {
			logger.error("requestLogin returned exception", _exception);
		}
		return _success;
	}
	
	public boolean requestLogin() throws Exception {
		
		logger.trace("Top of requestLogin");
		
		HttpPost httppost = new HttpPost(URL + "/login");
        // Add your data
		// _eventName=login&redirectUrl=&submit=Submit+Query&email=dogconyers%40yahoo.com&password=dc1on23
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("_eventName", "login"));
        nameValuePairs.add(new BasicNameValuePair("email", "dogconyers@yahoo.com"));
        nameValuePairs.add(new BasicNameValuePair("password", "dc1on23"));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        
        // Execute HTTP Post Request
        HttpResponse _response = httpClient.execute(httppost, httpContext);
        logger.debug("status line is: " +  _response.getStatusLine());
        
        logger.debug("Cookies: " + cookieStore.toString());
        
        logger.trace("Bot of requestLogin");
        
        return true;

	}
	
	public boolean updateWeight() throws Exception {
		
		logger.trace("Top of updateWeight");
		
		HttpPost httppost = new HttpPost(URL + "/bodyMeasurements");
        // Add your data
		// _eventName=login&redirectUrl=&submit=Submit+Query&email=dogconyers%40yahoo.com&password=dc1on23
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("_eventName", "track"));
        nameValuePairs.add(new BasicNameValuePair("redirectActionBean", "runkeeper.web.HomeActionBean"));
        nameValuePairs.add(new BasicNameValuePair("weight", "117"));
        nameValuePairs.add(new BasicNameValuePair("bodyFat", "14"));
        
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        
        // Execute HTTP Post Request
        HttpResponse _response = httpClient.execute(httppost, httpContext);
        logger.debug("status line is: " +  _response.getStatusLine());
        
        logger.debug("Cookies: " + cookieStore.toString());
        
        logger.trace("Bot of updateWeight");
        
        return true;

	}
		
}
