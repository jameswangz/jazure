package com.googlecode.jazure.sdk.schedule;


public class FixedSchedulePolicy implements SchedulePolicy {

	private static final long serialVersionUID = -4995749134933498723L;

	public static final long DEFULAT_INITIAL_DELAY = 0;
	public static final long DEFAULT_PERIOD = 10;
	
	private long initialDelay;
	private long period;

	public FixedSchedulePolicy() {
		initialDelay = DEFULAT_INITIAL_DELAY;
		period = DEFAULT_PERIOD;
	}
	
	public FixedSchedulePolicy(long initialDelay, long period) {
		this.initialDelay = initialDelay;
		this.period = period;
	}

	public long getInitialDelay() {
		return this.initialDelay;
	}

	public long getPeriod() {
		return this.period;
	}

}
