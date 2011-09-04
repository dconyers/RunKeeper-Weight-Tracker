package com.conyers.runkeeper;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conyers.runkeeper.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
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

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
    	  
    	super.onCreateOptionsMenu(pMenu);
    	
    	MenuInflater _inflater = getMenuInflater();
    	_inflater.inflate(R.menu.menu, pMenu);
    	
    	MenuItem _menuItem = (MenuItem)pMenu.findItem(R.id.Logout);
    	_menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				logger.trace("onMenuItem Click for Logout button");
				
		        CookieManager cookieManager = CookieManager.getInstance();
		        cookieManager.removeSessionCookie();   
		        cookieManager.removeAllCookie();
		        CookieSyncManager.getInstance().sync();
		        
				Intent _intent = new Intent(UpdateWeightActivity.this,CredentialRequestActivity.class);
				startActivity(_intent);
		        
				return true;
			}
    	});
    	
    	return true;
    }
    
    // Private Methods
	private void updateWeight(int bodyfattextfield, int weighttextfield) {

		TextView _text = (TextView)findViewById(R.id.textView1);
		_text.setText("Test - should be updating weight at time: " + new Date());
		// TODO - this needs to be handled differently here.
		//GetRKWeightDataAsyncTask _task = new GetRKWeightDataAsyncTask(accessToken);
		//_task.execute(1);
	}
}