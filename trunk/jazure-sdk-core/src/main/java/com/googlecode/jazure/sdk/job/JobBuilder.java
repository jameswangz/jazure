package com.googlecode.jazure.sdk.job;

import com.googlecode.jazure.sdk.aggregator.Aggregator;
import com.googlecode.jazure.sdk.aggregator.CompletionStrategy;
import com.googlecode.jazure.sdk.aggregator.CorrelationStrategy;

public interface JobBuilder<T extends JobConfig> {

	JobBuilder<T> aggregateWith(Aggregator<T> aggregator);
	
	JobBuilder<T> correlationStrategy(CorrelationStrategy correlationStrategy);
	
	JobBuilder<T> completionStrategy(CompletionStrategy completionStrategy);
	
	Job<T> build();

}
