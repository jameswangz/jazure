package com.googlecode.jazure.sdk.task.tracker;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public interface ConditionPostFilter {

	boolean accept(TaskInvocation invocation);

}
