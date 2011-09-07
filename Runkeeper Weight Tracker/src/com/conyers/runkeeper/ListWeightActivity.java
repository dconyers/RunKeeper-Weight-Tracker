package com.conyers.runkeeper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conyers.runkeeper.R;



import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class ListWeightActivity extends ListActivity {
	
	private static final Logger logger = LoggerFactory.getLogger(ListWeightActivity.class); 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		GetRKWeightDataAsyncTask _task = new GetRKWeightDataAsyncTask(this);
		_task.execute(1);

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
		        
				Intent _intent = new Intent(ListWeightActivity.this,CredentialRequestActivity.class);
				startActivity(_intent);
		        
				return true;
			}
    	});

    	_menuItem = (MenuItem)pMenu.findItem(R.id.AddWeight);
    	_menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				logger.trace("onMenuItem Click for AddWeight button");
				
				Intent _intent = new Intent(ListWeightActivity.this,UpdateWeightActivity.class);
				startActivity(_intent);
		        
				return true;
			}
    	});
    	
    	return true;
    }
}