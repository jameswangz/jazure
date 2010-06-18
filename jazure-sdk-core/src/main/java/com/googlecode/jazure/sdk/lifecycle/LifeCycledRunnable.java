package com.googlecode.jazure.sdk.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LifeCycledRunnable implements Runnable {

	private Logger logger = LoggerFactory.getLogger(LifeCycledRunnable.class);
	
	private final Runnable runnable;
	private final LifeCycle lifeCycle;
	
	LifeCycledRunnable(Runnable runnable, LifeCycle lifeCycle) {
		this.runnable = runnable;
		this.lifeCycle = lifeCycle;
	}
	
	@Override
	public void run() {
		try {
			if (!lifeCycle.isRunning()) {
				return;
			}
			runnable.run();
		} catch (Throwable t) {
			logger.error("Error raised : " + t.getMessage(), t);
		}
	}
	
}
