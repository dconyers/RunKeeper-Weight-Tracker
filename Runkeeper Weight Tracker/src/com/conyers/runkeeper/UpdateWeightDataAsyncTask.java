package com.conyers.runkeeper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conyers.runkeeper.json.RKWeightData;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;

import android.os.AsyncTask;

public class UpdateWeightDataAsyncTask extends
AsyncTask<RKWeightData, String, RKWeightData> {

	private static final Logger logger = LoggerFactory.getLogger(UpdateWeightDataAsyncTask.class);

	UpdateWeightActivity activity = null;
	
	protected UpdateWeightDataAsyncTask(UpdateWeightActivity pActivity) {
		activity = pActivity;
	}
	
	@Override
	protected void onPreExecute() {
	}
	
	@Override
	protected void onPostExecute(RKWeightData pWeightData) {
	}
	
	@Override
	protected RKWeightData doInBackground(RKWeightData... pRKWeightData) {
		try {

			HttpRequestFactory _factory;

			_factory = RunKeeperWeightTracker.getInstance().getHttpRequestFactory();		
			HttpRequest _request = _factory.buildPostRequest(new GenericUrl(Constants.API_URL + "/weight"), pRKWeightData[0].toContent());
			
			logger.debug("Content is: " + pRKWeightData[0].toContent());
			pRKWeightData[0].toContent().writeTo(System.out);
			OutputStream _foo = new ByteArrayOutputStream();
			pRKWeightData[0].toContent().writeTo(_foo);
			logger.debug("Foo is: " + _foo);
			logger.debug("request is: " + _request.headers.toString());
			_request.headers.contentType="application/vnd.com.runkeeper.NewWeight+json;charset=ISO-8859-1";
			HttpResponse _response = _request.execute();
			String _return =_response.parseAsString();
			logger.debug("Weight feed returned: " + _return);
			
			return pRKWeightData[0];
		} catch (IOException pIOE) {
			logger.error("Got IOException", pIOE);
		} catch (Exception pAnyException) {
			logger.error("Caught Unexpected exception", pAnyException);
		}
		return null;
	}
}
