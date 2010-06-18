package com.googlecode.jazure.sdk.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.googlecode.jazure.sdk.concurrent.ThreadFactorys;

public abstract class Schedulers {
	
	public static Scheduler newFixRateScheduler(String threadNamePrefix) {
		return newFixRateScheduler(1, threadNamePrefix);
	}
	
	public static Scheduler newFixRateScheduler(int coreSize, String threadNamePrefix) {
		return newFixRateScheduler(coreSize, threadNamePrefix, true);
	}
	
	public static Scheduler newFixRateScheduler(int coreSize, String threadNamePrefix, boolean daemon) {
		return new SimpleScheduler(coreSize, threadNamePrefix, daemon);
	}
	
	private static final class SimpleScheduler implements Scheduler {
		
		private ScheduledExecutorService ses;
		
		public SimpleScheduler(int coreSize, String threadNamePrefix, boolean daemon) {
			ses = Executors.newScheduledThreadPool(coreSize, ThreadFactorys.customizable(threadNamePrefix, daemon));
		}

		@Override
		public void schedule(Runnable command, Trigger trigger) {
			SimpleRepeatTrigger spt = (SimpleRepeatTrigger) trigger;
			ses.scheduleAtFixedRate(
				command, 
				spt.getInitialDelay(), 
				spt.getPeriod(), 
				SimpleRepeatTrigger.TIME_UNIT
			);
		}

		@Override
		public void shutdown() {
			ses.shutdown();
		}

		@Override
		public Class<? extends Trigger> supportedTrigger() {
			return SimpleRepeatTrigger.class;
		}

	}

	
	
}
