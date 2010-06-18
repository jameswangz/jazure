package com.googlecode.jazure.sdk.task.storage;

import java.util.Date;

import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.task.tracker.PaginatedList;
import com.googlecode.jazure.sdk.task.tracker.TaskCondition;
import com.googlecode.jazure.sdk.task.tracker.TaskRetrievalFailureException;


public interface TaskStorage {

	void save(TaskInvocation invocation);

	void update(TaskInvocation invocation);

	PaginatedList<TaskInvocation> getTasks(TaskCondition condition);

	TaskInvocation load(String id) throws TaskRetrievalFailureException;
	
	/**
	 * Clean all tasks before specified timePoint
	 * @param timePoint time point, exclusive
	 * @return deleted task count
	 */
	int clearBefore(Date timePoint);
	
}
