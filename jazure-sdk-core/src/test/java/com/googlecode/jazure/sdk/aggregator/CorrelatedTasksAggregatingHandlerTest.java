package com.googlecode.jazure.sdk.aggregator;

import static org.jbehave.Ensure.ensureThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.googlecode.jazure.sdk.job.SimpleJobConfig;
import com.googlecode.jazure.sdk.task.TaskCorrelation;
import com.googlecode.jazure.sdk.task.TaskInvocation;


public class CorrelatedTasksAggregatingHandlerTest {
	
	private static final int SEQUENCE_SIZE = 100;
	private static final int GROUP_SIZE = 10;
	private Set<Integer> groups = new HashSet<Integer>();
	
	@Test
	public void defaultBehaviour() {
		SimpleJobConfig jobConfig = new SimpleJobConfig("1");
		SimpleAggregator aggregator = new SimpleAggregator();
		
		CorrelatedTasksAggregatingHandler<SimpleJobConfig> handler = new CorrelatedTasksAggregatingHandler<SimpleJobConfig>(aggregator, null, null);
		
		for (int sequenceNumber = 0 ; sequenceNumber < SEQUENCE_SIZE; sequenceNumber++) {
			TaskCorrelation correlation = new TaskCorrelation("1", SEQUENCE_SIZE, sequenceNumber);
			TaskInvocation result = new TaskInvocation(null, null, correlation, null, null, null, null);
			handler.processCorrelatated(jobConfig, result);
		}	
		
		ensureThat(aggregator.aggregateCollelatedCalled());
	}

	@Test
	public void fineGrainedStrategy() {
		SimpleJobConfig jobConfig = new SimpleJobConfig("1");
		SimpleCounterAggregator aggregator = new SimpleCounterAggregator();
		
		FineGrainedStrategy strategy = new FineGrainedStrategy() {
			protected Object correlationKeyAppended(TaskInvocation result) {
				return result.getTask().getParameter("Group", Integer.class);
			}
			protected int correlatedTasksSize(TaskInvocation anyTask) {
				return GROUP_SIZE;
			}
		};
		
		CorrelatedTasksAggregatingHandler<SimpleJobConfig> handler = new CorrelatedTasksAggregatingHandler<SimpleJobConfig>(
			aggregator, 
			strategy, 
			strategy
		);
		
		int group = 0;
		for (int sequenceNumber = 0 ; sequenceNumber < SEQUENCE_SIZE; sequenceNumber++) {
			if (sequenceNumber % GROUP_SIZE == 0) {
				group++;
			}
			TaskCorrelation correlation = new TaskCorrelation("1", SEQUENCE_SIZE, sequenceNumber);
			TaskInvocation result = new TaskInvocation(null, null, correlation, new SimpleTask().addParameter("Group", group), null, null, null);
			handler.processCorrelatated(jobConfig, result);
			groups.add(group);
		}	
		
		ensureThat(aggregateCorrelatedCalled10Times(aggregator));
	}

	private boolean aggregateCorrelatedCalled10Times(SimpleCounterAggregator aggregator) {
		return aggregator.aggregateCorrelatedCalledTimes().equals(SEQUENCE_SIZE / groups.size());
	}
	
}
