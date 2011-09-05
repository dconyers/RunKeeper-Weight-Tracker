package com.conyers.runkeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conyers.common.StrictModeWrapper;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

import android.app.Application;

public class RunKeeperWeightTracker extends Application {

	private static final Logger logger = LoggerFactory.getLogger(RunKeeperWeightTracker.class);
	
	private static RunKeeperWeightTracker instance = null;

	private HttpRequestFactory factory;

	private String accessToken = null;
	
	public RunKeeperWeightTracker() {
		super();

		if (instance != null) {
			throw new IllegalStateException("RunKeeperWeightTracker already instantiated");
		}
		
		instance = this;
	}
	
	public static RunKeeperWeightTracker getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		logger.debug("Top of RunKeeperWeightTracker::onCreate()");
		super.onCreate();
		try {
			StrictModeWrapper.init(this);
		} catch (Throwable pThrowable) {
			logger.error("Failed to init StrictModeWrapper", pThrowable);
		}
	}
	
	public String getAccessToken() {
			return accessToken;
	}
	
	public void setAccessToken(String pAccessToken) {
		accessToken = pAccessToken;
	}
	
	public synchronized HttpRequestFactory getHttpRequestFactory() {
		if (factory != null) {
			return factory;
		}
		
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

		return factory;
	}

	
}
