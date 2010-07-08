package com.googlecode.jazure.sdk.aggregator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.task.TaskInvocation;



public class CorrelatedTasksAggregatingHandler<T extends JobConfig> {

	private static Logger logger = LoggerFactory.getLogger(CorrelatedTasksAggregatingHandler.class);
	
	private final Aggregator<? super T> aggregator;
	private final CorrelationStrategy correlationStrategy;
	private final CompletionStrategy completionStrategy;
	
	private BlockingQueue<Object> trackedCorrelationIds = new LinkedBlockingQueue<Object>();
	private ConcurrentMap<Object, TaskInvocationBarrier> barriers = new ConcurrentHashMap<Object, TaskInvocationBarrier>();

	public CorrelatedTasksAggregatingHandler(
		Aggregator<? super T> aggregator, 
		CorrelationStrategy correlationStrategy, 
		CompletionStrategy completionStrategy) {
		
		this.aggregator = aggregator;
		this.correlationStrategy = correlationStrategy == null ? new DefaultCorrelationStrategy() : correlationStrategy;
		this.completionStrategy = completionStrategy == null ? new SequenceSizeCompletionStrategy() : completionStrategy;
	}

	public void processCorrelatated(T jobConfig, TaskInvocation result) {
		Object correlationKey = correlationStrategy.getCorrelationKey(result);
		if (trackedCorrelationIds.contains(correlationKey)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Handling of Task group with correlationKey '" + correlationKey
						+ "' has already completed or timed out.");
			}
		} else {
			barriers.putIfAbsent(correlationKey, new TaskInvocationBarrier());
			TaskInvocationBarrier barrier = barriers.get(correlationKey);
			
			synchronized (barrier) {
				barrier.addIfNotCompleted(result);
				
				if (barrier.processing()) {
					if (completionStrategy.isComplete(barrier.getInvocations())) {
						barrier.completed();
					}
				}
				
				if (barrier.isComplete()) {
					removeBarrier(correlationKey);
					aggregator.aggregateCorrelated(jobConfig, correlationKey, barrier.getInvocations());
				}						
			}
		}
	}
	
	private void removeBarrier(Object correlationKey) {
		if (barriers.remove(correlationKey) != null) {
			synchronized (trackedCorrelationIds) {
				boolean added = trackedCorrelationIds.offer(correlationKey);
				if (!added) {
					trackedCorrelationIds.poll();
					trackedCorrelationIds.offer(correlationKey);
				}
			}
		}
	}

}
