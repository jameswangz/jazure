package com.googlecode.jazure.sdk.job.eventdriven;

import java.util.Map;

import com.googlecode.jazure.sdk.job.Job;
import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.loader.EventDrivenLoader;

public interface EventDrivenJob<T extends JobConfig> extends Job<T> {
	
	EventDrivenLoader<T> getLoader();

	EventDrivenJob<T> fireLoad(Map<?, ?> parameters);
	
}
