package com.conyers.runkeeper.json;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class RKWeightData extends GenericJson {

	
	@Key
	public String uri;
	
	@Key
	public String timestamp;

	@Key
	public Double weight = null;
	
}
