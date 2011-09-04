package com.conyers.runkeeper;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.auth.oauth2.draft10.AccessTokenRequest.AuthorizationCodeGrant;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoginAsyncTask extends AsyncTask<String, String, String> {

	private static final Logger logger = LoggerFactory.getLogger(LoginAsyncTask.class);
	private Context context;

	private String code = null;
	
	protected LoginAsyncTask(Context pContext) {
		context = pContext;
	}
	
	@Override
	protected void onPreExecute() {
		logger.trace("LoginAynchTask::onPreExecute called");
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		logger.trace("Top of LoginAsyncTask::doInBackground");
		code = arg0[0];
		return login();
	}
	
	@Override
	protected void onProgressUpdate(String... pUpdate) {
		logger.debug("onProgressUpdate called with pUpdate length: " + pUpdate.length);
		
		for (String _update : pUpdate) {
			Toast.makeText(context, _update, Toast.LENGTH_SHORT);
			logger.debug("Got update of: " + _update);
		}
	}
	
	@Override
	protected void onPostExecute(String pResult) {
		logger.trace("Top of LoginAsyncTask::onPostExecute with value of: " + pResult);
		publishProgress("onPostExecute");
		Intent _intent = new Intent(context,ListWeightActivity.class);
		//_intent.setComponent(new ComponentName(UpdateWeightActivity.class.getPackage().getName(), UpdateWeightActivity.class.getName()));
		_intent.putExtra("accessToken", pResult);
		context.startActivity(_intent);
	}
	
	private String login() {
	
		logger.trace("Top of LoginAsyncTask::login");
		publishProgress("Top of login()");

		try {
		AuthorizationCodeGrant _grantRequest = new AuthorizationCodeGrant(new NetHttpTransport(), new JacksonFactory(), CredentialRequestActivity.TOKEN_URL, CredentialRequestActivity.CLIENT_ID, CredentialRequestActivity.CLIENT_SECRET, code, CredentialRequestActivity.REDIRECT_URI);
		AccessTokenResponse _accessTokenResponse = _grantRequest.execute();
		String _accessToken = _accessTokenResponse.accessToken;
		logger.debug("Received Access Token: " + _accessToken);
		return _accessToken;
	      
	} catch (IOException pIOE) {
		logger.error("Caught IOE", pIOE);
		return null;
	} finally {
		logger.debug("in finally");
	}


	}
	

}
