package com.googlecode.jazure.sdk.loader;

import com.googlecode.jazure.sdk.job.polling.PollingJobConfig;


public abstract class AbstractPollingLoader<T extends PollingJobConfig> extends AbstractLoader implements PollingLoader<T> {

	@Override
	public boolean isRunning() {
		return wrapper.isRunning();
	}
	
	@Override
	public void start() {
		wrapper.start(new Runnable() {
			@Override
			public void run() {
				doStart();
			}
		});
	}

	@Override
	public void stop() {
		wrapper.stop(new Runnable() {
			@Override
			public void run() {
				doStop();
			}
		});
	}

}
