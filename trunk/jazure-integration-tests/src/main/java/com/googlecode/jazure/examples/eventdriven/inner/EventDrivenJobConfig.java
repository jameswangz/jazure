package com.googlecode.jazure.examples.eventdriven.inner;

import com.googlecode.jazure.sdk.job.JobConfig;

public class EventDrivenJobConfig implements JobConfig {

	private static final long serialVersionUID = -6736881071885964479L;

	@Override
	public String getId() {
		return "event driven job example";
	}

}
