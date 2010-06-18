package com.googlecode.jazure.sdk.schedule.quartz;

import com.googlecode.jazure.sdk.schedule.Scheduler;
import com.googlecode.jazure.sdk.schedule.Trigger;

public class QuartzTrigger implements Trigger {

	private final org.quartz.Trigger underlyingTrigger;

	public QuartzTrigger(org.quartz.Trigger underlyingTrigger) {
		this.underlyingTrigger = underlyingTrigger;
	}

	@Override
	public Scheduler createScheduler(String threadNamePrefix) {
		return new QuartzScheduler();
	}

	public org.quartz.Trigger getUnderlyingTrigger() {
		return underlyingTrigger;
	}

}
