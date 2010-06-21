package com.googlecode.jazure.sdk.concurrent;

import java.util.concurrent.ThreadFactory;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public class ThreadFactorys {

	public static ThreadFactory customizable(String threadNamePrefix, boolean daemon) {
		CustomizableThreadFactory threadFactory = new CustomizableThreadFactory(threadNamePrefix);
		threadFactory.setDaemon(daemon);
		return threadFactory;
	}

}
