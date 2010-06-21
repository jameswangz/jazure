package com.googlecode.jazure.sdk.task.tracker;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jazure.sdk.lifecycle.LifeCycle;
import com.googlecode.jazure.sdk.lifecycle.LifeCycleWrapper;
import com.googlecode.jazure.sdk.lifecycle.LifeCycles;
import com.googlecode.jazure.sdk.schedule.Scheduler;
import com.googlecode.jazure.sdk.schedule.Schedulers;
import com.googlecode.jazure.sdk.schedule.SimpleRepeatTrigger;
import com.googlecode.jazure.sdk.task.storage.TaskStorage;

public class FarAwayTaskCleaner implements LifeCycle {

	private static Logger logger = LoggerFactory.getLogger(FarAwayTaskCleaner.class);
	
	private final TaskStorage taskStorage;
	private final int hoursToLive;
	private final long executionPeriod;
	private LifeCycleWrapper lifeCycleWrapper = LifeCycles.wrapped();
	private Scheduler scheduler;
	
	public FarAwayTaskCleaner(TaskStorage taskStorage, int hoursToLive, long executionPeriod) {
		this.taskStorage = taskStorage;
		this.hoursToLive = hoursToLive;
		this.executionPeriod = executionPeriod;
	}

	@Override
	public boolean isRunning() {
		return lifeCycleWrapper.isRunning();
	}

	@Override
	public void start() {
		lifeCycleWrapper.start(new Runnable() {
			@Override
			public void run() {
				logger.info("Starting FarAwayTaskCleaner");
				
				scheduler = Schedulers.newFixRateScheduler("JAzure - FarAwayTaskCleaner");
				scheduler.schedule(
					new Runnable() {
						@Override
						public void run() {
							Date timePoint = DateUtils.addHours(new Date(), -hoursToLive);
							logger.info("Cleaning tasks before " + timePoint);
							int count = taskStorage.clearBefore(timePoint);
							logger.info("Cleaned " + count + " tasks before " + timePoint);
						}
					}, 
					new SimpleRepeatTrigger(0, executionPeriod)
				);
				
				logger.info("Started FarAwayTaskCleaner");
			}
		});
	}

	@Override
	public void stop() {
		lifeCycleWrapper.stop(new Runnable() {
			@Override
			public void run() {
				logger.info("Stopping FarAwayTaskCleaner");
				
				scheduler.shutdown();
				scheduler = null;
				
				logger.info("Stopped FarAwayTaskCleaner");
			}
		});
	}

}
