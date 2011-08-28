package com.conyers.runkeeper;


public class Constants {

	public static final String CONSUMER_KEY 	= "7752fbae004c452c9482ecad693d4381";
	public static final String CONSUMER_SECRET 	= "f87c1ef1f4f44de9ad2b92946fa896d0";

	public static final String SCOPE 			= "https://runkeeper.com";
	public static final String REQUEST_URL 		= "https://runkeeper.com";
	public static final String ACCESS_URL 		= "https://runkeeper.com/apps/token";  
	public static final String AUTHORIZE_URL 	= "https://runkeeper.com/apps/authorize";

	public static final String ENCODING 		= "UTF-8";
	
	public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

	public static final String PREF_KEY_OAUTH_TOKEN_SECRET = "oauth_token_secret";
	public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
}
