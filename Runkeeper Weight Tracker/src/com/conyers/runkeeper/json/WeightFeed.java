package com.conyers.runkeeper.json;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.Key;

public class WeightFeed {

	@Key
	public List<RKWeightData> items = new ArrayList<RKWeightData>();
	
	@Key
	public int size;
}
