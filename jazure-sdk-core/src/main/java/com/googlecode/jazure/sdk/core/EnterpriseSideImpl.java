package com.googlecode.jazure.sdk.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jazure.sdk.lifecycle.LifeCycleWrapper;
import com.googlecode.jazure.sdk.lifecycle.LifeCycles;

class EnterpriseSideImpl implements EnterpriseSide {
	
	private static Logger logger = LoggerFactory.getLogger(EnterpriseSideImpl.class);
	
	private Console console;
	private LifeCycleWrapper wrapper = LifeCycles.wrapped();
	

	@Override
	public Console getConsole() {
		return this.console;
	}
	
	public Console setConsole(Console console) {
		this.console = console;
		return this.console;
	}

	@Override
	public boolean isRunning() {
		return wrapper.isRunning();
	}

	@Override
	public void start() {
		wrapper.start(new Runnable() {
			@Override
			public void run() {
				logger.info("Starting enterprise side ...");
				console.start();
				logger.info("Started enterprise side.");
			}
		});

	}

	@Override
	public void stop() {
		wrapper.stop(new Runnable() {
			@Override
			public void run() {
				logger.info("Stoping enterprise side ...");
				console.stop();
				logger.info("Stopped enterprise side.");
			}
		});
	}



}
