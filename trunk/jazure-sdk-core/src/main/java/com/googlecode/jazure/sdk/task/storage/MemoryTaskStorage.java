package com.googlecode.jazure.sdk.task.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.task.tracker.PaginatedList;
import com.googlecode.jazure.sdk.task.tracker.Paginater;
import com.googlecode.jazure.sdk.task.tracker.TaskCondition;
import com.googlecode.jazure.sdk.task.tracker.TaskNotFoundException;
import com.googlecode.jazure.sdk.task.tracker.TaskRetrievalFailureException;


public class MemoryTaskStorage implements TaskStorage {
	
	private List<TaskInvocation> tasks = new ArrayList<TaskInvocation>();
	
	@Override
	public void save(TaskInvocation invocation) {
		tasks.add(invocation);
	}
	
	@Override
	public void update(final TaskInvocation invocation) {
		TaskInvocation found = (TaskInvocation) CollectionUtils.find(tasks, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				TaskInvocation foreach = (TaskInvocation) object;
				return foreach.getId().equals(invocation.getId());
			}
		});
		
		if (found == null) {
			throw new TaskNotFoundException(invocation);
		}
		
		found.replaceWith(invocation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaginatedList<TaskInvocation> getTasks(final TaskCondition condition) {
//		List<TaskInvocation> matched = (List<TaskInvocation>) CollectionUtils.select(tasks, new Predicate() {
//			@Override
//			public boolean evaluate(Object object) {
//				TaskInvocation foreach = (TaskInvocation) object;
//				return condition.typeMatched(foreach.getTask().getType())
//						&& condition.statusMatched(foreach.getStatus());
//			}
//		});
//		
//		List<TaskInvocation> results = paginate(matched, condition.getPaginater());
//		return PaginatedLists.proxy(results, condition.getPaginater().setTotalCount(matched.size()));
		
		//TODO implement
		throw new UnsupportedOperationException("Not yet implement");
	}

	private List<TaskInvocation> paginate(List<TaskInvocation> matched, Paginater paginater) {
		List<TaskInvocation> results = new ArrayList<TaskInvocation>();
		int offerset = paginater.getOfferset();
		int maxResults = paginater.getMaxResults();
		int expectedTo = offerset + maxResults;
		int fullsize = matched.size();
		if (fullsize > offerset) {
			int toIndex = fullsize < expectedTo ? fullsize : expectedTo;
			results.addAll(matched.subList(offerset, toIndex));
		}
		return results;
	}

	@Override
	public TaskInvocation load(final String id)  throws TaskRetrievalFailureException {
		TaskInvocation found = (TaskInvocation) CollectionUtils.find(tasks, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				TaskInvocation task = (TaskInvocation) object;
				return StringUtils.equals(id, task.getId());
			}
		});
		
		if (found == null) {
			throw new TaskRetrievalFailureException("Failed to retrieve task [" + id + "] ");
		}
		return found;
	}

	@Override
	public int clearBefore(Date timePoint) {
		int count = 0;
		
		for (Iterator<TaskInvocation> iter = tasks.iterator(); iter.hasNext(); ) {
			TaskInvocation task = iter.next();
			if (task.getTimeTrace().getCreatedTime().before(timePoint)) {
				iter.remove();
				count++;
			}
		}
		
		return count;
	}

	
	

}
