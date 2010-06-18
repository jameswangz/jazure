package com.googlecode.jazure.sdk.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.core.task.TaskExecutor;

public class ThreadPoolExecutor implements TaskExecutor {

	private static final int DEFAULT_CORE_SIZE = 1;
	
	private String threadNamePrefix;
	private int coreSize = DEFAULT_CORE_SIZE;
	private boolean daemon = true;
	private ExecutorService executor;

	public ThreadPoolExecutor(String threadNamePrefix) {
		this(threadNamePrefix, DEFAULT_CORE_SIZE, true);
	}
	
	public ThreadPoolExecutor(String threadNamePrefix, boolean daemon) {
		this(threadNamePrefix, DEFAULT_CORE_SIZE, daemon);
	}
	
	public ThreadPoolExecutor(String threadNamePrefix, int coreSize, boolean daemon) {
		this.threadNamePrefix = threadNamePrefix;
		this.daemon = daemon;
		this.coreSize = coreSize;
		initializeExecutor();
	}
	
	private void initializeExecutor() {
		executor = Executors.newFixedThreadPool(coreSize, ThreadFactorys.customizable(threadNamePrefix, daemon));
	}

	@Override
	public void execute(Runnable task) {
		executor.submit(task);
	}
	
	public void shutdown() {
		executor.shutdown();
	}

}
