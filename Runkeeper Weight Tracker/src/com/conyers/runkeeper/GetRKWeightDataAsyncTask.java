package com.conyers.runkeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conyers.runkeeper.json.RKWeightData;
import com.conyers.runkeeper.json.WeightFeed;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GetRKWeightDataAsyncTask extends
AsyncTask<Integer, String, RKWeightData[]> {

	private static final Logger logger = LoggerFactory.getLogger(GetRKWeightDataAsyncTask.class);

	private String accessToken;

	HttpRequestFactory factory;
	
	ListActivity listActivity = null;
	
	protected GetRKWeightDataAsyncTask(String pAccessToken, ListActivity pListActivity) {
		accessToken = pAccessToken;
		listActivity = pListActivity;
	}
	
	@Override
	protected void onPreExecute() {
		HttpTransport _transport = new NetHttpTransport();
		final JsonFactory jsonFactory = new JacksonFactory();

		factory = _transport.createRequestFactory(new HttpRequestInitializer() {

			@Override
			public void initialize(HttpRequest request) {
				// set the parser

				JsonHttpParser parser = new JsonHttpParser();
				parser.contentType="application/vnd.com.runkeeper.weightfeed+json;charset=ISO-8859-1";
				parser.jsonFactory = jsonFactory;
				request.addParser(parser);
				
				parser = new JsonHttpParser();
				parser.contentType="application/vnd.com.runkeeper.weight+json;charset=ISO-8859-1";
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
		
	}
	
	@Override
	protected void onPostExecute(RKWeightData[] pWeightData) {
		logger.debug("Top of onPostExecute with array size: " + pWeightData.length);
		listActivity.setListAdapter(new MySimpleArrayAdapter(listActivity, pWeightData));
	}
	
	
	
	@Override
	protected RKWeightData[] doInBackground(Integer... params) {
		try {
			HttpRequest _request = factory.buildGetRequest(new GenericUrl("http://api.runkeeper.com/weight"));
			logger.debug("request is: " + _request.headers.toString());
			HttpResponse _response = _request.execute();
			WeightFeed _return =_response.parseAs(WeightFeed.class);
			logger.debug("Weight feed returned: " + _return.size + " items");
			
			List<RKWeightData> _list = new ArrayList<RKWeightData>();
			
			for (RKWeightData _data : _return.items) {
				logger.debug("data from: " + _data.timestamp + " is: " + _data.uri);
				
				//parser.contentType="application/vnd.com.runkeeper.weight+json;charset=ISO-8859-1";
				_request = factory.buildGetRequest(new GenericUrl("http://api.runkeeper.com" + _data.uri));
				_request.headers.accept = "application/vnd.com.runkeeper.Weight+json";
				logger.debug("Request is: " + _request.headers.toString());
				_response = _request.execute();
				RKWeightData _wd =_response.parseAs(RKWeightData.class);
				logger.debug("WD is: " + _wd);
				
				if (_wd.weight != null) {
					_list.add(_wd);
				}
				logger.debug("Weight is: " + _wd.weight);
			}
			return _list.toArray(new RKWeightData[0]);
		} catch (IOException pIOE) {
			logger.error("Got IOException", pIOE);
		}

		return null;
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<RKWeightData> {
		private final Activity context;
		private final RKWeightData[] weightData;

		public MySimpleArrayAdapter(Activity pContext, RKWeightData[] pWeightData) {
			super(pContext, R.layout.rk_list_row_layout, pWeightData);
			this.context = pContext;
			this.weightData = pWeightData;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			logger.debug("Top of getView for position: " + position);
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.rk_list_row_layout, null, true);
			TextView dateTextView = (TextView) rowView.findViewById(R.id.DateTextView);
			TextView weightTextView = (TextView) rowView.findViewById(R.id.WeightTextView);
			dateTextView.setText(weightData[position].getTimestamp());
			
			weightTextView.setText(weightData[position].getFormattedWeight());
			logger.debug("returning rowview");
			return rowView;
		}
	}
	
}
