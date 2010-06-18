package com.googlecode.jazure.sdk.job.polling;

import com.googlecode.jazure.sdk.job.JobBuilder;
import com.googlecode.jazure.sdk.loader.PollingLoader;

public interface PollingJobBuilder<T extends PollingJobConfig> extends JobBuilder<T> {
	
	PollingJobBuilder<T> loadAt(PollingLoader<T> pollingLoader);


}
