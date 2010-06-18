package com.googlecode.jazure.sdk.lifecycle;



public abstract class LifeCycles {

	public static LifeCycleWrapper wrapped() {
		return new LifeCycleWrapper() {
		
			private boolean running;

			@Override
			public boolean isRunning() {
				return running;
			}

			@Override
			public void start(Runnable startCommand) {
				if (running) {
					throw new IllegalStateException("Already running!");
				}
				running = true;
				startCommand.run();
			}

			@Override
			public void stop(Runnable stopCommand) {
				if (!running) {
					throw new IllegalStateException("Already stopped!");
				}
				running = false;
				stopCommand.run();
			}
		};
	}

	public static LifeCycledRunnable run(Runnable runnable, LifeCycle lifeCycle) {
		return new LifeCycledRunnable(runnable, lifeCycle);
	}

}
