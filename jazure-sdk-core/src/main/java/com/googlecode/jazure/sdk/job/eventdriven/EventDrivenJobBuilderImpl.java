package com.googlecode.jazure.sdk.job.eventdriven;

import com.googlecode.jazure.sdk.job.AbstractJobBuilder;
import com.googlecode.jazure.sdk.job.Job;
import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.loader.EventDrivenLoader;
import com.googlecode.jazure.sdk.loader.Loader;


public class EventDrivenJobBuilderImpl<T extends JobConfig> extends AbstractJobBuilder<T> 
	implements EventDrivenJobBuilder<T> {

	private final T jobConfig;
	private EventDrivenLoader<T> eventDrivenLoader;

	public EventDrivenJobBuilderImpl(T jobConfig) {
		this.jobConfig = jobConfig;
	}

	@Override
	protected Job<T> buildInternel() {
		return new EventDrivenJobImpl<T>(jobConfig, eventDrivenLoader, aggregator, correlationStrategy, completionStrategy, taskQueue(), resultQueue());
	}

	@Override
	protected Loader getLoader() {
		return eventDrivenLoader;
	}

	@Override
	public EventDrivenJobBuilder<T> loadAt(EventDrivenLoader<T> eventDrivenLoader) {
		this.eventDrivenLoader = eventDrivenLoader;
		return this;
	}

	@Override
	public JobConfig getJobConfig() {
		return jobConfig;
	}



}
