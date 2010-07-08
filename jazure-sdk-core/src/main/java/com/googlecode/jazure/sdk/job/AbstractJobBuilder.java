package com.googlecode.jazure.sdk.job;

import com.google.inject.Injector;
import com.google.inject.internal.Preconditions;
import com.googlecode.jazure.sdk.aggregator.Aggregator;
import com.googlecode.jazure.sdk.aggregator.CompletionStrategy;
import com.googlecode.jazure.sdk.aggregator.CorrelationStrategy;
import com.googlecode.jazure.sdk.loader.Loader;

public abstract class AbstractJobBuilder<T extends JobConfig> implements JobBuilder<T> {

	protected Aggregator<? super T> aggregator;
	protected Injector injector;
	protected CorrelationStrategy correlationStrategy;
	protected CompletionStrategy completionStrategy;

	public abstract JobConfig getJobConfig();
	protected abstract Loader getLoader();
	protected abstract Job<T> buildInternel();
	
	@Override
	public JobBuilder<T> aggregateWith(Aggregator<? super T> aggregator) {
		this.aggregator = aggregator;
		return this;
	}
	
	@Override
	public JobBuilder<T> correlationStrategy(CorrelationStrategy correlationStrategy) {
		this.correlationStrategy = correlationStrategy;
		return this;
	}
	
	@Override
	public JobBuilder<T> completionStrategy(CompletionStrategy completionStrategy) {
		this.completionStrategy = completionStrategy;
		return this;
	}

	public AbstractJobBuilder<T> injector(Injector injector) {
		this.injector = injector;
		return this;
	}
	

	@Override
	public Job<T> build() {
		preBuild();
		Job<T> job = buildInternel();
		injector.injectMembers(job);
		return job;
	}

	private void preBuild() {
		Preconditions.checkNotNull(getJobConfig(), "jobConfig required");
		Preconditions.checkNotNull(getLoader(), "loader required");
		Preconditions.checkNotNull(aggregator, "aggregator required");
	}

	protected String taskQueue() {
		return getJobConfig().getId() + " task queue ";
	}

	protected String resultQueue() {
		return getJobConfig().getId() + " result queue ";
	}



}
