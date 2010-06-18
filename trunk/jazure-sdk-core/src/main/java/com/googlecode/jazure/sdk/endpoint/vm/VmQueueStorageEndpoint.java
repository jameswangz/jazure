package com.googlecode.jazure.sdk.endpoint.vm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.internal.Preconditions;
import com.googlecode.jazure.sdk.concurrent.ThreadFactorys;
import com.googlecode.jazure.sdk.core.Console;
import com.googlecode.jazure.sdk.core.ProjectConfiguration;
import com.googlecode.jazure.sdk.endpoint.QueueStorageEndpoint;
import com.googlecode.jazure.sdk.event.EventListener;
import com.googlecode.jazure.sdk.job.Job;
import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.job.JobStoppedEvent;
import com.googlecode.jazure.sdk.job.exception.JobNotFoundException;
import com.googlecode.jazure.sdk.lifecycle.LifeCycleWrapper;
import com.googlecode.jazure.sdk.lifecycle.LifeCycles;
import com.googlecode.jazure.sdk.schedule.Scheduler;
import com.googlecode.jazure.sdk.schedule.Schedulers;
import com.googlecode.jazure.sdk.schedule.SimpleRepeatTriggers;
import com.googlecode.jazure.sdk.task.Result;
import com.googlecode.jazure.sdk.task.Retrier;
import com.googlecode.jazure.sdk.task.Task;
import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.util.CategoryBufferedMap;

public class VmQueueStorageEndpoint implements QueueStorageEndpoint {
	
	private static Logger logger = LoggerFactory.getLogger(VmQueueStorageEndpoint.class);
	
	private Console console;
	private Queue<TaskInvocation> taskQueue = new ConcurrentLinkedQueue<TaskInvocation>();
	private CategoryBufferedMap<String, TaskInvocation> resultBuffer = new CategoryBufferedMap<String, TaskInvocation>();
	private LifeCycleWrapper lifeCycleWrapper = LifeCycles.wrapped();
	private Scheduler mainExecutor;
	private ConcurrentMap<String, ExecutorService> taskExecutors = new ConcurrentHashMap<String, ExecutorService>();
	
	@Override
	public void send(TaskInvocation task) {
		checkState();
		taskQueue.add(task);			
	}

	private void checkState() {
		Preconditions.checkState(isRunning(), "Not running!");
	}
	
	@Override
	public TaskInvocation receive(String resultQueueName) {
		checkState();
		return resultBuffer.remove(resultQueueName);
	}

	@Override
	public void start() {
		lifeCycleWrapper.start(new Runnable() {
			public void run() {
				mainExecutor = Schedulers.newFixRateScheduler("JAzure - VmEndpoint - executor");
				mainExecutor.schedule(new Runnable() {
					public void run() {
							try {
								final TaskInvocation invocation = taskQueue.poll();
								if (invocation == null) {
									return;
								}
								
								JobConfig jobConfig = invocation.getMetaData().getJobConfig();
								String jobId = jobConfig.getId();
								Task task = invocation.getTask();
								try {
									Job<?> job = console.getJob(jobConfig);
									if (!job.isRunning()) {
										if (logger.isDebugEnabled()) {
											logger.debug("Job [" + jobId + "] has been stopped, processing next task.. ");
										}
										return;
									}
								} catch (JobNotFoundException e) {
									if (logger.isDebugEnabled()) {
										logger.debug("Job [" + jobId + "] has been removed, processing next task.. ");
									}
									return;
								}
								
								ExecutorService executor = initializeJobExecutorIfNecessary(jobId, task);
								executor.submit(taskOf(invocation));
							} catch (Throwable t) {
								logger.error("Exception raised : " + t.getMessage() + ", ignore it...", t);
								return;
							}
						}
				}, SimpleRepeatTriggers.create(console.projectConfiguration().getBusPollInterval()));
			}
		});
	}

	private ExecutorService initializeJobExecutorIfNecessary(String jobId, Task task) {
		taskExecutors.putIfAbsent(
			jobId, 
			Executors.newFixedThreadPool(
				task.getConcurrentPolicy().getCoreSize(), 
				ThreadFactorys.customizable("JAzure - TaskExecutor - " + jobId, true)
			)
		);
		return taskExecutors.get(jobId);
	}
	
	private Runnable taskOf(final TaskInvocation invocation) {
		return new Runnable() {
			public void run() {
				ProjectConfiguration pc = invocation.getMetaData().getProjectConfiguration();
				Result result = Retrier.times(pc.getRetryTimes())
									   .interval(pc.getRetryInterval())
									   .execute(invocation.getTask());
				invocation.setResult(result).getMetaData().setGridWorker("Vm Worker");
				resultBuffer.put(invocation.getMetaData().getResultQueue(), invocation);
				try {
					Thread.sleep(invocation.getTask().getSchedulePolicy().getPeriod());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		};
	}
	
	@Override
	public boolean isRunning() {
		return lifeCycleWrapper.isRunning();
	}

	@Override
	public void stop() {
		lifeCycleWrapper.stop(new Runnable() {
			public void run() {
				mainExecutor.shutdown();
				destoryExecutors(new Predicate() {
					public boolean evaluate(Object object) {
						return true;
					}
				});
				resultBuffer.clear();
			}
		});
	}

	private void destoryExecutors(Predicate predicate) {
		Iterator<Entry<String, ExecutorService>> iter = taskExecutors.entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<String, ExecutorService> entry = iter.next();
			String jobId = entry.getKey();
			if (predicate.evaluate(jobId)) {
				logger.info("Destroying task executor of [" + jobId + "]");
				ExecutorService executor = entry.getValue();
				executor.shutdownNow();
				executor = null;	
				iter.remove();
				logger.info("Destroied task executor of [" + jobId + "]");
			}
		}
	}
	
	@Override
	public Collection<? extends EventListener> listeners() {
		Collection<EventListener> listeners = new ArrayList<EventListener>();
		listeners.add(new DestroyTaskExecutorListener());
		return listeners;
	}
	
	public class DestroyTaskExecutorListener implements EventListener {

		@Override
		public void onEvent(EventObject event) {
			if (JobStoppedEvent.class.isInstance(event)) {
				final JobConfig jobConfig = ((JobStoppedEvent) event).getSource().getJobConfig();
				destoryExecutors(new Predicate() {
					public boolean evaluate(Object object) {
						return jobConfig.getId().equals(object);
					}
				});
			}
		}
	}

	@Override
	public void setConsole(Console console) {
		this.console = console;
	}
	
}
