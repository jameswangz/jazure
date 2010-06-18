package com.googlecode.jazure.sdk.schedule;

public interface Trigger {
	
	Scheduler createScheduler(String threadNamePrefix);
	
}
