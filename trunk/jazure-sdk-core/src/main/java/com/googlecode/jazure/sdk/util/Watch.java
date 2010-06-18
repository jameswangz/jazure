package com.googlecode.jazure.sdk.util;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

public class Watch {

	private static Logger logger = LoggerFactory.getLogger(Watch.class);
	
	private final String name;
	private String taskName;

	private Watch(String name) {
		this.name = name;
	}

	public static Watch named(String name) {
		return new Watch(name);
	}

	public Watch task(String taskName) {
		this.taskName = taskName;
		return this;
	}

	public <V> V watch(Callable<V> callable) throws Exception {
		StopWatch sw = new StopWatch(name);
		sw.start(taskName);
		V result = callable.call();
		sw.stop();
		logger.info(sw.prettyPrint());
		return result;
	}
	
}
