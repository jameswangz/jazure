package com.googlecode.jazure.sdk.job;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.googlecode.jazure.sdk.aggregator.Aggregator;
import com.googlecode.jazure.sdk.aggregator.CompletionStrategy;
import com.googlecode.jazure.sdk.aggregator.CorrelatedTasksAggregatingHandler;
import com.googlecode.jazure.sdk.aggregator.CorrelationStrategy;
import com.googlecode.jazure.sdk.core.ProjectConfiguration;
import com.googlecode.jazure.sdk.endpoint.QueueStorageEndpoint;
import com.googlecode.jazure.sdk.event.EventPublisher;
import com.googlecode.jazure.sdk.job.exception.JobNotRunningException;
import com.googlecode.jazure.sdk.lifecycle.LifeCycleWrapper;
import com.googlecode.jazure.sdk.lifecycle.LifeCycledRunnable;
import com.googlecode.jazure.sdk.lifecycle.LifeCycles;
import com.googlecode.jazure.sdk.schedule.Scheduler;
import com.googlecode.jazure.sdk.schedule.Schedulers;
import com.googlecode.jazure.sdk.schedule.SimpleRepeatTriggers;
import com.googlecode.jazure.sdk.task.Task;
import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.task.TaskInvocations;
import com.googlecode.jazure.sdk.task.tracker.TaskInvocationSaveEvent;
import com.googlecode.jazure.sdk.task.tracker.TaskInvocationUpdateEvent;

public abstract class AbstractJob<T extends JobConfig> implements Job<T> {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected final Aggregator<T> aggregator;
	protected final String taskQueue;
	protected final String resultQueue;
	
	protected Queue<TaskInvocation> loaded = new ConcurrentLinkedQueue<TaskInvocation>();
	protected Queue<TaskInvocation> received = new ConcurrentLinkedQueue<TaskInvocation>();
	
	@Inject
	protected EventPublisher eventPublisher;
	@Inject
	protected ProjectConfiguration pc;
	@Inject
	protected QueueStorageEndpoint endpoint;

	protected LifeCycleWrapper wrapper = LifeCycles.wrapped();
	
	protected Scheduler senderScheduler;
	protected Scheduler receiverScheduler;
	protected Scheduler aggregatorScheduler;
	
	protected CorrelatedTasksAggregatingHandler<T> correlatedTasksAggregatingHandler;

	public AbstractJob(
		Aggregator<T> aggregator,
		CorrelationStrategy correlationStrategy,
		CompletionStrategy completionStrategy, 
		String taskQueue,
		String resultQueue) {
		
		this.aggregator = aggregator;		
		//TODO set correlation strategy
		this.correlatedTasksAggregatingHandler = new CorrelatedTasksAggregatingHandler<T>(aggregator, correlationStrategy, completionStrategy);
		this.taskQueue = taskQueue;
		this.resultQueue = resultQueue;		
	}

	protected abstract void startLoader();
	protected abstract void stopLoader();
	
	@Override
	public void start() {
		wrapper.start(new Runnable() {
			@Override
			public void run() {
				initializeSchedulers();
				startLoader();
				scheduleSend();
				scheduleReceive();
				scheduleAggregate();
				eventPublisher.publishEvent(new JobStartedEvent(AbstractJob.this));
			}
		});
	}

	private void initializeSchedulers() {
		senderScheduler = Schedulers.newFixRateScheduler(1, threadNamePrefix("CloudSender"));
		receiverScheduler = Schedulers.newFixRateScheduler(1, threadNamePrefix("CloudReceiver"));
		aggregatorScheduler = Schedulers.newFixRateScheduler(1, threadNamePrefix("Aggregator"));
	}

	protected String threadNamePrefix(String component) {
		return "JAzure - " + component + " - " + getJobConfig().getId();
	}
	
	private void scheduleSend() {
		senderScheduler.schedule(decorate(new Runnable() {
			@Override
			public void run() {
				TaskInvocation invocation = loaded.poll();
				if (invocation == null) {
					return;
				}
				
				sendTask(invocation);
			}
		}), SimpleRepeatTriggers.create(pc.getBusPollInterval()));
	}

	protected void handleLoaded(Collection<Task> tasks) {
		List<TaskInvocation> invocations = TaskInvocations.createPending(pc, getJobConfig(), tasks, taskQueue, resultQueue);					
		
		for (TaskInvocation invocation : invocations) {
			eventPublisher.publishEvent(new TaskInvocationSaveEvent(invocation));
			loaded.add(invocation);
		}
	}
	
	private void sendTask(TaskInvocation invocation) {
		endpoint.send(invocation);
		eventPublisher.publishEvent(new TaskInvocationUpdateEvent(invocation.executing()));
	}
	
	private void scheduleReceive() {
		receiverScheduler.schedule(decorate(new Runnable() {
			@Override
			public void run() {
				TaskInvocation result = endpoint.receive(resultQueue);
				if (result == null) {
					return;
				}
				received.add(result);
			}
		}), SimpleRepeatTriggers.create(pc.getBusPollInterval()));
	}
	
	private void scheduleAggregate() {
		aggregatorScheduler.schedule(decorate(new Runnable() {
			@Override
			public void run() {
				TaskInvocation result = received.poll();
				if (result == null) {
					return;
				}
				
				eventPublisher.publishEvent(new TaskInvocationUpdateEvent(result.completed()));		
				if (aggregator.aggregateSupported(result)) {
					aggregator.aggregate(result);	
				}
				if (aggregator.aggregateCorrelatedSupported(getJobConfig())) {
					correlatedTasksAggregatingHandler.processCorrelatated(getJobConfig(), result);
				}
			}
		}), SimpleRepeatTriggers.create(pc.getBusPollInterval()));
	}
	

	
	@Override
	public void stop() {
		wrapper.stop(new Runnable() {
			@Override
			public void run() {
				stopLoader();
				senderScheduler.shutdown();
				receiverScheduler.shutdown();
				aggregatorScheduler.shutdown();
				destorySchedulers();
				eventPublisher.publishEvent(new JobStoppedEvent(AbstractJob.this));
			}
		});
	}


	private void destorySchedulers() {
		senderScheduler = null;
		receiverScheduler = null;
		aggregatorScheduler = null;
	}
	
	@Override
	public boolean isRunning() {
		return wrapper.isRunning();
	}

	@Override
	public Aggregator<T> getAggregator() {
		return aggregator;
	}
	
	protected LifeCycledRunnable decorate(Runnable runnable) {
		return LifeCycles.run(runnable, this);
	}

	@Override
	public Job<T> executeTask(TaskInvocation invocation) throws JobNotRunningException {
		if (!isRunning()) {
			throw new JobNotRunningException("Job [" + getJobConfig().getId() + "] is not running");
		}
		sendTask(invocation);
		return this;
	}
	


}
