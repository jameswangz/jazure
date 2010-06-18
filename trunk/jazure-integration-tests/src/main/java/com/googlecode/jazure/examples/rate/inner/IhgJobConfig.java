package com.googlecode.jazure.examples.rate.inner;

import org.apache.commons.lang.time.DateUtils;

import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.job.polling.PollingJobConfig;
import com.googlecode.jazure.sdk.schedule.SimpleRepeatTrigger;
import com.googlecode.jazure.sdk.schedule.Trigger;


public class IhgJobConfig implements JobConfig, PollingJobConfig {

	private static final long serialVersionUID = -7577134561813732866L;

	@Override
	public String getId() {
		return "ihg job";
	}

	@Override
	public Trigger getTrigger() {
		return new SimpleRepeatTrigger(0, DateUtils.MILLIS_PER_MINUTE * 1);
	}

}
