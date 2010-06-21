package com.googlecode.jazure.sdk.job.eventdriven;

import java.util.Collection;
import java.util.Map;

import com.googlecode.jazure.sdk.aggregator.Aggregator;
import com.googlecode.jazure.sdk.aggregator.CompletionStrategy;
import com.googlecode.jazure.sdk.aggregator.CorrelationStrategy;
import com.googlecode.jazure.sdk.job.AbstractJob;
import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.loader.EventDrivenLoader;
import com.googlecode.jazure.sdk.task.Task;

class EventDrivenJobImpl<T extends JobConfig> extends AbstractJob<T> implements EventDrivenJob<T> {

	private final EventDrivenLoader<T> eventDrivenLoader;
	private final T jobConfig;

	public EventDrivenJobImpl(
		T jobConfig, 
		EventDrivenLoader<T> eventDrivenLoader, 
		Aggregator<T> aggregator,
		CorrelationStrategy correlationStrategy,
		CompletionStrategy completionStrategy, 
		String taskQueue, 
		String resultQueue) {
		
		super(aggregator, correlationStrategy, completionStrategy, taskQueue, resultQueue);
		this.jobConfig = jobConfig;
		this.eventDrivenLoader = eventDrivenLoader;
	}

	@Override
	protected void startLoader() {
	}

	@Override
	protected void stopLoader() {
	}

	@Override
	public EventDrivenJob<T> fireLoad(Map<?, ?> parameters) {
		Collection<Task> tasks = eventDrivenLoader.createTasks(jobConfig, parameters);
		handleLoaded(tasks);
		return this;
	}

	@Override
	public EventDrivenLoader<T> getLoader() {
		return eventDrivenLoader;
	}

	@Override
	public T getJobConfig() {
		return jobConfig;
	}


}
