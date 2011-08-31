package com.conyers.Hello.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import android.test.AndroidTestCase;

public class RKTest extends AndroidTestCase {

	private static final Logger logger = LoggerFactory.getLogger(RKTest.class); 

	
	public RKTest() {
		super();
		logger.trace ("Top of HelloTest Constructor");
		System.out.println("HelloTest Constructor Called");
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		System.out.println("setUp() called");
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		System.out.println("tearDown() called");
	}
	

	/**
	 * Test method for {@link com.conyers.runkeeper.RKConnection#connect()}.
	 */
	public void testConnect() {
		try {
		System.out.println("Property is: " + System.getProperty("log4j.configuration"));
		logger.debug("Before calling RKCOnnection constructor");
		//RKConnection _connection = new RKConnection();
		logger.debug("After calling RKConnection constructor, before connect");
		//assertEquals(_connection.connect(), true);
		logger.debug("After connect");
		}
		catch (Exception _exc) {
			logger.error("Got exception: " + _exc);
		}
	}

}
