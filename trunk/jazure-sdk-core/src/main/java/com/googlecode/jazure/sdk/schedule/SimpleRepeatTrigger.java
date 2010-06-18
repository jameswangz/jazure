package com.googlecode.jazure.sdk.schedule;

import java.util.concurrent.TimeUnit;

public class SimpleRepeatTrigger implements Trigger {

	private static final long serialVersionUID = -4995749134933498723L;
	
	public static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
	
	public static final long DEFULAT_INITIAL_DELAY = 0;
	public static final long DEFAULT_PERIOD = 10;
	
	private long initialDelay;
	private long period;

	public SimpleRepeatTrigger() {
		initialDelay = DEFULAT_INITIAL_DELAY;
		period = DEFAULT_PERIOD;
	}
	
	public SimpleRepeatTrigger(long initialDelay, long period) {
		this.initialDelay = initialDelay;
		this.period = period;
	}

	public long getInitialDelay() {
		return this.initialDelay;
	}

	public long getPeriod() {
		return this.period;
	}

	@Override
	public Scheduler createScheduler(String threadNamePrefix) {
		return Schedulers.newFixRateScheduler(threadNamePrefix);
	}

	
}
