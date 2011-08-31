package com.conyers.runkeeper;


import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conyers.runkeeper.R;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UpdateWeightActivity extends Activity {
	
	private static final Logger logger = LoggerFactory.getLogger(UpdateWeightActivity.class); 
	
	private String accessToken = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        accessToken = this.getIntent().getExtras().getString("accessToken");
        logger.debug("onCreate pulled accessToken of: " + accessToken + " from Intent");
        
        logger.debug("Top of HelloActivity::onCreate");
        setContentView(R.layout.main);

        View _view = findViewById(R.id.WeightLabel);
        // Create an anonymous implementation of OnClickListener

        logger.debug("_view is: " + _view);
        if (_view == null) {
        	logger.error("Failed to find view");
        	return;
        }
        
        Button _submitButton = (Button)findViewById(R.id.SubmitButton);
        _submitButton.setOnClickListener(new OnClickListener() {
        	@Override
            public void onClick(View v) {
                // do something when the button is clicked
              	updateWeight(R.id.BodyFatTextField, R.id.WeightTextField);
              }
          });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        logger.debug("onStart() called");
        // The activity is about to become visible.
    }
    @Override
    protected void onResume() {
    	logger.debug("onResume called");
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }
    @Override
    protected void onPause() {
        super.onPause();
        logger.debug("onPause called");
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
        super.onStop();
        logger.debug("onStop called");
        // The activity is no longer visible (it is now "stopped")
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        logger.debug("onDestroy called");
        // The activity is about to be destroyed.
    }

    // Private Methods
	private void updateWeight(int bodyfattextfield, int weighttextfield) {

		TextView _text = (TextView)findViewById(R.id.textView1);
		_text.setText("Test - should be updating weight at time: " + new Date());
		
		HttpTransport _transport = new NetHttpTransport();
		final JsonFactory jsonFactory = new JacksonFactory();
		
	    HttpRequestFactory _factory = _transport.createRequestFactory(new HttpRequestInitializer() {

	    	@Override
	        public void initialize(HttpRequest request) {
	          // set the parser
	          JsonCParser parser = new JsonCParser();
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

	    try {
	    	HttpRequest _request = _factory.buildGetRequest(new GenericUrl("http://api.runkeeper.com/weight"));
	    	logger.debug("request is: " + _request.headers.toString());
	    	HttpResponse _response = _request.execute();
	    	logger.debug("response: " + _response.parseAsString());
	    } catch (IOException pIOE) {
	    	logger.error("Got IOException", pIOE);
	    }

	    

	}


	
}