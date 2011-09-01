package com.conyers.runkeeper;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conyers.runkeeper.json.RKWeightData;
import com.conyers.runkeeper.json.WeightFeed;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

import android.os.AsyncTask;

public class GetRKWeightDataAsyncTask extends
AsyncTask<Integer, String, String> {

	private static final Logger logger = LoggerFactory.getLogger(GetRKWeightDataAsyncTask.class);

	private String accessToken;

	HttpRequestFactory factory;
	
	protected GetRKWeightDataAsyncTask(String pAccessToken) {
		accessToken = pAccessToken;
	}
	
	@Override
	protected void onPreExecute() {
		HttpTransport _transport = new NetHttpTransport();
		final JsonFactory jsonFactory = new JacksonFactory();

		factory = _transport.createRequestFactory(new HttpRequestInitializer() {

			@Override
			public void initialize(HttpRequest request) {
				// set the parser

				JsonHttpParser parser = new JsonHttpParser();
				parser.contentType="application/vnd.com.runkeeper.weightfeed+json;charset=ISO-8859-1";
				parser.jsonFactory = jsonFactory;
				request.addParser(parser);
				
				parser = new JsonHttpParser();
				parser.contentType="application/vnd.com.runkeeper.weight+json;charset=ISO-8859-1";
				parser.jsonFactory = jsonFactory;
				request.addParser(parser);

				
				// set up the Google headers
				HttpHeaders _headers = new HttpHeaders();
				logger.debug("Header auth is: " + accessToken);
				_headers.authorization= "Bearer " + accessToken;
				_headers.accept="application/vnd.com.runkeeper.WeightFeed+json";
				request.headers = _headers;
			}
		});
		
	}
	
	@Override
	protected String doInBackground(Integer... params) {
		try {
			HttpRequest _request = factory.buildGetRequest(new GenericUrl("http://api.runkeeper.com/weight"));
			logger.debug("request is: " + _request.headers.toString());
			HttpResponse _response = _request.execute();
			WeightFeed _return =_response.parseAs(WeightFeed.class);
			logger.debug("Weight feed returned: " + _return.size + " items");
			
			for (RKWeightData _data : _return.items) {
				logger.debug("data from: " + _data.timestamp + " is: " + _data.uri);
				
				//parser.contentType="application/vnd.com.runkeeper.weight+json;charset=ISO-8859-1";
				_request = factory.buildGetRequest(new GenericUrl("http://api.runkeeper.com" + _data.uri));
				_request.headers.accept = "application/vnd.com.runkeeper.Weight+json";
				logger.debug("Request is: " + _request.headers.toString());
				_response = _request.execute();
				RKWeightData _wd =_response.parseAs(RKWeightData.class);
				logger.debug("Weight is: " + _wd.weight);
			}
		} catch (IOException pIOE) {
			logger.error("Got IOException", pIOE);
		}

		return null;
	}

}
