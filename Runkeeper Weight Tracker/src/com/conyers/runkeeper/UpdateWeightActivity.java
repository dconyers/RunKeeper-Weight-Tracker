package com.conyers.runkeeper;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conyers.runkeeper.R;


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
		GetRKWeightDataAsyncTask _task = new GetRKWeightDataAsyncTask(accessToken);
		_task.execute(1);
	}
}