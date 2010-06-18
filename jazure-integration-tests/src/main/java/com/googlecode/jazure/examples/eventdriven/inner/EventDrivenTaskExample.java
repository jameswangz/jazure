package com.googlecode.jazure.examples.eventdriven.inner;

import com.googlecode.jazure.sdk.concurrent.ConcurrentPolicy;
import com.googlecode.jazure.sdk.concurrent.FixedConcurrentPolicy;
import com.googlecode.jazure.sdk.schedule.FixedSchedulePolicy;
import com.googlecode.jazure.sdk.schedule.SchedulePolicy;
import com.googlecode.jazure.sdk.task.AbstractTask;
import com.googlecode.jazure.sdk.task.Result;

public class EventDrivenTaskExample extends AbstractTask {

	private static final long serialVersionUID = -6409815901723448615L;

	FixedConcurrentPolicy fixedConcurrentPolicy = new FixedConcurrentPolicy();
	
	FixedSchedulePolicy fixedSchedulePolicy = new FixedSchedulePolicy();
	
	@Override
	public Result execute() {
		String url = getParameter(ParameteredLoaderExample.URL, String.class);
		String component = getParameter(ParameteredLoaderExample.COMPONENT, String.class);
		return new EventDrivenResultExample("visisted : " + url + " - " + component);
	}

	@Override
	public String getType() {
		return "Event driven task example";
	}

	@Override
	public ConcurrentPolicy getConcurrentPolicy() {
		return fixedConcurrentPolicy;
	}

	@Override
	public SchedulePolicy getSchedulePolicy() {
		return fixedSchedulePolicy;
	}

}
