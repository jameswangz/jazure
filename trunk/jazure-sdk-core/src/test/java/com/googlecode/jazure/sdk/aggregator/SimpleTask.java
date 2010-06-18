package com.googlecode.jazure.sdk.aggregator;

import com.googlecode.jazure.sdk.concurrent.ConcurrentPolicy;
import com.googlecode.jazure.sdk.schedule.SchedulePolicy;
import com.googlecode.jazure.sdk.task.AbstractTask;
import com.googlecode.jazure.sdk.task.Result;

public class SimpleTask extends AbstractTask {

	private static final long serialVersionUID = 2031565128550906947L;

	@Override
	public Result execute() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public ConcurrentPolicy getConcurrentPolicy() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public SchedulePolicy getSchedulePolicy() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getType() {
		throw new UnsupportedOperationException("Not yet implemented");
	}



}
