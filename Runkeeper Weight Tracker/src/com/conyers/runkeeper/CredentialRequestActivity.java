package com.conyers.runkeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conyers.runkeeper.R;
import com.google.api.client.auth.oauth2.draft10.AuthorizationRequestUrl;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class CredentialRequestActivity extends Activity {

	private static final Logger logger = LoggerFactory.getLogger(CredentialRequestActivity.class);
	
	public static final String REDIRECT_URI = "http://localhost";
	
	private String code = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
        logger.debug("Top of CredentialRequestActivity::onCreate");
        setContentView(R.layout.credential_request_activity);
	}

	@Override
	public void onResume() {
		super.onResume();
		setContentView(R.layout.credential_request_activity);
		logger.trace("Top of CredentialRequestActivity::onResume");
		processLoginRequest();
	}
	
	private void processLoginRequest() {
		
		logger.trace("Top of processLoginRequest");
		TextView _responseTextView = (TextView)findViewById(R.id.ResponseTextView);
		if (_responseTextView == null) {
			logger.error("Failed to get responseTextView reference in processLoginRequest");
			throw new IllegalStateException("How did this happen?");
		}
		_responseTextView.setText("Attempting Login");
		
		AuthorizationRequestUrl _requestURL = new AuthorizationRequestUrl(Constants.AUTHORIZE_URL, Constants.CONSUMER_KEY);
		_requestURL.redirectUri = REDIRECT_URI;
		
		WebView _webView = new WebView(this);
		_webView.getSettings().setJavaScriptEnabled(true);  
        _webView.setVisibility(View.VISIBLE);
        setContentView(_webView);
        
        String _resultURL = _requestURL.build();
        logger.debug("requestURL is: " + _resultURL);
        
        /* WebViewClient must be set BEFORE calling loadUrl! */  
        _webView.setWebViewClient(new WebViewClient() {  
        	
        	@Override
        	public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
        		logger.debug("On page started for url: " + url);
        		CookieManager _cookieManager = CookieManager.getInstance();
        		logger.debug("On Page started - Cookie is: " + _cookieManager.getCookie("runkeeper.com"));
        	}
        	
        	@Override
        	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        		logger.error("Got onReceivedError. Code: " + errorCode + " Description: " + description + " Failuring URL: " + failingUrl);
        	}
        	
        	@Override  
            public void onPageFinished(WebView view, String pURL)  {
        		super.onPageFinished(view, pURL);
        		logger.debug("top of onPageFinished for WebView w/URL: " + pURL);
        		CookieManager _cookieManager = CookieManager.getInstance();
        		CookieSyncManager.getInstance().sync();
        		logger.debug("On Page Finished - Cookie is: " + _cookieManager.getCookie("runkeeper.com"));
        		if (code != null) {
        			logger.debug("already got code, returning.");
        			return;
        		}
        		
            	if (pURL.startsWith(REDIRECT_URI)) {
            			if (pURL.indexOf("code=")!=-1) {
            			
	            			code = extractCodeFromUrl(pURL);
            				view.setVisibility(View.INVISIBLE);
	            			LoginAsyncTask _task = new LoginAsyncTask(CredentialRequestActivity.this);
	                		_task.execute(code);
            			} else if (pURL.indexOf("error=")!=-1) {
            				logger.error("Got error strong: " + pURL);
            				view.setVisibility(View.INVISIBLE);
            			}
            	}
            	logger.debug("bot of onPageFinished");
            }
			private String extractCodeFromUrl(String url) {
				return url.substring(REDIRECT_URI.length()+7,url.length());
			}  
        });

        _webView.loadUrl(_resultURL);		

		return;
	}
}
