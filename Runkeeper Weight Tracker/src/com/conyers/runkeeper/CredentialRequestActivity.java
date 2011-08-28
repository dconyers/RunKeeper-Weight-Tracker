package com.conyers.runkeeper;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conyers.runkeeper.R;
import com.google.api.client.auth.oauth2.draft10.AccessTokenRequest.AuthorizationCodeGrant;
import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.auth.oauth2.draft10.AuthorizationRequestUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class CredentialRequestActivity extends Activity {

	private static final Logger logger = LoggerFactory.getLogger(CredentialRequestActivity.class);
	
	private static final String AUTH_URL = "https://runkeeper.com/apps/authorize";
	private static final String TOKEN_URL = "https://runkeeper.com/apps/token";
	private static final String CLIENT_ID = "7752fbae004c452c9482ecad693d4381";
	private static final String CLIENT_SECRET = "f87c1ef1f4f44de9ad2b92946fa896d0";
	public static final String REDIRECT_URI = "http://localhost";
	
	private String code = null;

	public static String accessToken = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
        logger.debug("Top of HelloActivity::onCreate");
        setContentView(R.layout.credential_request_activity);

        
        Button _submitButton = (Button)findViewById(R.id.LoginButton);
        if (_submitButton == null) {
        	logger.error("Failed to find submit button");
        	return;
        }
        _submitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // do something when the button is clicked
              	processLoginRequest();
              }
          });

	}
	
	public String getAccessToken() {
		return null;
	}
	
	private void processLoginRequest() {
		
		
				
		logger.trace("Top of processLoginRequest");
		TextView _responseTextView = (TextView)findViewById(R.id.ResponseTextView);
		_responseTextView.setText("Attempting Login");
		
		AuthorizationRequestUrl _requestURL = new AuthorizationRequestUrl(AUTH_URL, CLIENT_ID);
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
            public void onPageStarted(WebView view, String url,android.graphics.Bitmap bitmap)  {  
        		logger.debug("top of onPageStarted for WebView w/URL: " + url);
            }
        	@Override  
            public void onPageFinished(WebView view, String pURL)  {  
        		logger.debug("top of onPageFinished for WebView w/URL: " + pURL);
            	
        		if (code != null) {
        			logger.debug("already got code, returning.");
        			return;
        		}
        		
            	if (pURL.startsWith(REDIRECT_URI)) {
            		try {
						
            			if (pURL.indexOf("code=")!=-1) {
            			
	            			code = extractCodeFromUrl(pURL);
							
	            			AuthorizationCodeGrant _grantRequest = new AuthorizationCodeGrant(new NetHttpTransport(), new JacksonFactory(), TOKEN_URL, CLIENT_ID, CLIENT_SECRET, code, REDIRECT_URI);
            				AccessTokenResponse _accessTokenResponse = _grantRequest.execute();
			  		      
            				accessToken = _accessTokenResponse.accessToken;
            				logger.debug("Access token returned: " + accessToken);
            				//CredentialStore credentialStore = new SharedPreferencesCredentialStore(prefs);
            				//credentialStore.write(accessTokenResponse);
            				view.setVisibility(View.INVISIBLE);

            				startActivity(new Intent(CredentialRequestActivity.this,UpdateWeightActivity.class));
            			} else if (pURL.indexOf("error=")!=-1) {
            				logger.error("Got error strong: " + pURL);
            				view.setVisibility(View.INVISIBLE);
            			}
            		} catch (IOException pIOE) {
            			logger.error("Caught IOE", pIOE);
            			TextView _responseTextView = (TextView)findViewById(R.id.ResponseTextView);
            			_responseTextView.setText("Login failed");
            		} finally {
            			logger.debug("in finally");
            		}
            	}
            	logger.debug("bot of onPageFinished");
  		      
            }
			private String extractCodeFromUrl(String url) {
				return url.substring(REDIRECT_URI.length()+7,url.length());
			}  
        });  
        
        _webView.loadUrl(_resultURL);		
        logger.debug("after loadURL with: " + _resultURL);

		return;
	}


}
