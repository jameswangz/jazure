package com.googlecode.jazure.sdk.task.tracker;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public interface TaskTracker {

	TaskInvocation load(String id) throws TaskRetrievalFailureException;
	
	PaginatedList<TaskInvocation> getTasks(TaskCondition condition, ConditionPostFilter filter);
	
}
