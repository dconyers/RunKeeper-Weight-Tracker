package com.conyers.runkeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conyers.common.StrictModeWrapper;

import android.app.Application;

public class RunKeeperWeightTracker extends Application {

	private static final Logger logger = LoggerFactory.getLogger(RunKeeperWeightTracker.class);
	
	@Override
	public void onCreate() {
		logger.debug("Top of RunKeeperWeighTracker::onCreate()");
		super.onCreate();
		try {
			StrictModeWrapper.init(this);
		} catch (Throwable pThrowable) {
			logger.error("Failed to init StrictModeWrapper", pThrowable);
		}
	}
}
