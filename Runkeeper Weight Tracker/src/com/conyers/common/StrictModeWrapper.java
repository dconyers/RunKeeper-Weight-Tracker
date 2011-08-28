package com.conyers.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.StrictMode;

public class StrictModeWrapper {

	private static final Logger logger = LoggerFactory.getLogger(StrictModeWrapper.class);
	
	public static void init(Context pContext) {
		
		logger.debug("Top of StrictModeWrapper::init()");
		// check if android:debuggable is set
		int _appFlags = pContext.getApplicationInfo().flags;
		if ((_appFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().
					detectDiskReads().
					detectDiskWrites().
					detectNetwork().
					penaltyLog().
					build()
					);
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().
					detectLeakedSqlLiteObjects().
					penaltyLog().
					penaltyDeath().
					build()
					);
		}
	}
}
