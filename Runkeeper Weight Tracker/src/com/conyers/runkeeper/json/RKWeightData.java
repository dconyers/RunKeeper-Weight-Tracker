package com.conyers.runkeeper.json;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Key;

public class RKWeightData extends GenericJson {

	private static final Logger logger = LoggerFactory.getLogger(RKWeightData.class);
	
	private SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
	
	@Key
	public String uri;
	
	@Key
	public String timestamp;

	@Key
	public Double weight = null;
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public Date getDate() {
		try {
			return formatter.parse(timestamp);
		} catch (ParseException pParseException) {
			logger.error("getDate generated ParseException with string: " + timestamp, pParseException);
			return null;
		}
	}
	
	public RKWeightData() {
		super();
	}
	
	public RKWeightData(Double pWeightInPounds, Date pRecordTime) {
		super();
		timestamp = formatter.format(pRecordTime);
		setWeightInPounds(pWeightInPounds);
	}
	
	@Override
	public String toString() {
		return timestamp + ":" + getFormattedWeight();
	}
	
	public Double getPounds() {
		return weight * 2.20462262;
	}
	
	public void setWeightInPounds(Double pPounds) {
		weight = pPounds / 2.20462262;
	}
	
	public String getFormattedWeight() {
		DecimalFormat _formatter = new DecimalFormat("#,###.00");
		return weight==null?"No Data":_formatter.format(getPounds());
	}
	

	public JsonHttpContent toContent() {
		JsonHttpContent result = new JsonHttpContent();
	    result.jsonFactory = new JacksonFactory();
	    result.data = this;
	    return result;
	  }

}
