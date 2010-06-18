package com.googlecode.jazure.sdk.job.polling;

import com.googlecode.jazure.sdk.job.AbstractJobBuilder;
import com.googlecode.jazure.sdk.job.Job;
import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.loader.Loader;
import com.googlecode.jazure.sdk.loader.PollingLoader;


public class PollingJobBuilderImpl<T extends PollingJobConfig> extends AbstractJobBuilder<T>
	implements PollingJobBuilder<T> {

	private final T jobConfig;
	private PollingLoader<T> pollingLoader;

	public PollingJobBuilderImpl(T jobConfig) {
		this.jobConfig = jobConfig;
	}

	@Override
	protected Job<T> buildInternel() {
		return new PollingJobImpl<T>(jobConfig, pollingLoader, aggregator, correlationStrategy, completionStrategy, taskQueue(), resultQueue());
	}

	@Override
	public PollingJobBuilder<T> loadAt(PollingLoader<T> pollingLoader) {
		this.pollingLoader = pollingLoader;
		return this;
	}

	@Override
	protected Loader getLoader() {
		return pollingLoader;
	}

	@Override
	public JobConfig getJobConfig() {
		return jobConfig;
	}

}
