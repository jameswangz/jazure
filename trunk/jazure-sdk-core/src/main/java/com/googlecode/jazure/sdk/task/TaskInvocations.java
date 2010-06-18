package com.googlecode.jazure.sdk.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.googlecode.jazure.sdk.core.ProjectConfiguration;
import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.task.TaskInvocation.TaskMetaData;
import com.googlecode.jazure.sdk.task.TaskInvocation.TimeTrace;

public abstract class TaskInvocations {

	public static List<TaskInvocation> createPending(
		ProjectConfiguration pc, 
		JobConfig jobConfig, 
		Collection<Task> tasks, 
		String taskQueue, 
		String resultQueue) {
		
		List<TaskInvocation> invocations = new ArrayList<TaskInvocation>();
		
		String correlationId = UUID.randomUUID().toString();
		int sequenceSize = tasks.size();
		int sequenceNumber = 0;
		
		for (Task task : tasks) {
			String id = UUID.randomUUID().toString();
			invocations.add(
				new TaskInvocation(
					new TaskMetaData(pc, jobConfig, taskQueue, resultQueue, null), 
					id, 
					new TaskCorrelation(correlationId, sequenceSize, ++sequenceNumber),
					task, 
					Status.PENDING, 
					null, 
					new TimeTrace(new Date())
				)
			);
		}
		
		return invocations;
	}


}
