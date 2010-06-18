package com.googlecode.jazure.sdk.task;

import java.io.Serializable;
import java.util.Map;

import com.googlecode.jazure.sdk.concurrent.ConcurrentPolicy;
import com.googlecode.jazure.sdk.schedule.SchedulePolicy;


/**
 * A task should be stateful to be serialized
 *
 */
public interface Task extends Serializable, Searchable {
	
	Task addParameter(String key, Serializable value);
	
	Map<String, Serializable> getParameters();
	
	<V extends Serializable> V getParameter(String key, Class<V> valueType);
	
	Result execute();

	String getType();
	
	SchedulePolicy getSchedulePolicy();

	ConcurrentPolicy getConcurrentPolicy();
}
