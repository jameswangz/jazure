package com.googlecode.jazure.sdk.job.polling;

import java.util.Collection;

import com.googlecode.jazure.sdk.aggregator.Aggregator;
import com.googlecode.jazure.sdk.aggregator.CompletionStrategy;
import com.googlecode.jazure.sdk.aggregator.CorrelationStrategy;
import com.googlecode.jazure.sdk.job.AbstractJob;
import com.googlecode.jazure.sdk.loader.PollingLoader;
import com.googlecode.jazure.sdk.schedule.Scheduler;
import com.googlecode.jazure.sdk.task.Task;


class PollingJobImpl<T extends PollingJobConfig> extends AbstractJob<T> implements PollingJob<T> {
	
	private final T jobConfig;
	private final PollingLoader<T> pollingLoader;
	private Scheduler loaderScheduler;

	public PollingJobImpl(
		T jobConfig, 
		PollingLoader<T> pollingLoader,
		Aggregator<T> aggregator, 
		CorrelationStrategy correlationStrategy,
		CompletionStrategy completionStrategy, 
		String taskQueue, 
		String resultQueue) {
		
		super(aggregator, correlationStrategy, completionStrategy, taskQueue, resultQueue);
		this.jobConfig = jobConfig;
		this.pollingLoader = pollingLoader;
	}

	@Override
	public PollingLoader<T> getLoader() {
		return pollingLoader;
	}

	@Override
	protected void startLoader() {
		loaderScheduler = jobConfig.getTrigger().createScheduler(threadNamePrefix("Loader"));
		pollingLoader.start();
		scheduleLoad();
	}

	@Override
	protected void stopLoader() {
		pollingLoader.stop();
		loaderScheduler.shutdown();
		loaderScheduler = null;
	}
	
	private void scheduleLoad() {
		loaderScheduler.schedule(decorate(new Runnable() {
			@Override
			public void run() {
				Collection<Task> tasks = pollingLoader.createTasks(jobConfig);
				if (tasks == null || tasks.isEmpty()) {
					return;
				}
				
				handleLoaded(tasks);
			}
		}), jobConfig.getTrigger());
	}
	

	@Override
	public T getJobConfig() {
		return jobConfig;
	}
	

}
