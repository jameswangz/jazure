package com.googlecode.jazure.sdk.job.polling;

import com.googlecode.jazure.sdk.job.Job;
import com.googlecode.jazure.sdk.loader.PollingLoader;

public interface PollingJob<T extends PollingJobConfig> extends Job<T> {
	
	PollingLoader<T> getLoader();
	
}
