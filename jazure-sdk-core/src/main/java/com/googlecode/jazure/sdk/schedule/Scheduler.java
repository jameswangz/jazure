package com.googlecode.jazure.sdk.schedule;

public interface Scheduler {

	Class<? extends Trigger> supportedTrigger();
	
	void schedule(Runnable runnable, Trigger trigger);

	void shutdown();

}
