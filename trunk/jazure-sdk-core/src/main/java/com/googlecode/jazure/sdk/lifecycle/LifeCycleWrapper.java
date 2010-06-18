package com.googlecode.jazure.sdk.lifecycle;

public interface LifeCycleWrapper {

	boolean isRunning();

	void start(Runnable startCommand);

	void stop(Runnable stopCommand);

}
