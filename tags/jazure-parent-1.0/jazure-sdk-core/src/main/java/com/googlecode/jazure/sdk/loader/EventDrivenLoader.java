package com.googlecode.jazure.sdk.loader;

import java.util.Collection;
import java.util.Map;

import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.task.Task;

public interface EventDrivenLoader<T extends JobConfig> extends Loader {
	
	Collection<Task> createTasks(T jobConfig, Map<?, ?> parameters);
	
}
