package com.googlecode.jazure.examples.eventdriven.inner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.googlecode.jazure.sdk.loader.AbstractLoader;
import com.googlecode.jazure.sdk.loader.EventDrivenLoader;
import com.googlecode.jazure.sdk.task.Task;

public class ParameteredLoaderExample extends AbstractLoader implements EventDrivenLoader<EventDrivenJobConfig> {

	public static final String URL = "url";
	public static final String COMPONENT = "comopent";
	public static final String TASK_COUNT = "times";

	@Override
	public Collection<Task> createTasks(EventDrivenJobConfig jobConfig, Map<?, ?> parameters) {
		int count = ((Integer) parameters.get(TASK_COUNT)).intValue();
		
		Collection<Task> tasks = new ArrayList<Task>();
		
		for (int i = 0; i < count; i++) {
			tasks.add(new EventDrivenTaskExample()
						.addParameter(URL, (String) parameters.get(URL))
						.addParameter(COMPONENT, (String) parameters.get(COMPONENT))
					 );
		}
		
		return tasks;
	}


}
