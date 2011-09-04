package com.conyers.runkeeper.json;

import java.text.DecimalFormat;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class RKWeightData extends GenericJson {

	
	@Key
	public String uri;
	
	@Key
	public String timestamp;

	@Key
	public Double weight = null;
	
	public String getTimestamp() {
		return timestamp;
	}
	
	@Override
	public String toString() {
		

		return timestamp + ":" + getFormattedWeight();
	}
	
	public Double getPounds() {
		return weight * 2.20462262;
	}
	
	public String getFormattedWeight() {
		DecimalFormat _formatter = new DecimalFormat("#,###.00");
		return weight==null?"No Data":_formatter.format(getPounds());
	}
}
