package com.googlecode.jazure.sdk.loader;

import java.util.Collection;

import com.googlecode.jazure.sdk.job.polling.PollingJobConfig;
import com.googlecode.jazure.sdk.lifecycle.LifeCycle;
import com.googlecode.jazure.sdk.task.Task;

public interface PollingLoader<T extends PollingJobConfig> extends Loader, LifeCycle {

	Collection<Task> createTasks(T pollingJobConfig);

}
