package com.googlecode.jazure.sdk.schedule.quartz;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jazure.sdk.schedule.Scheduler;
import com.googlecode.jazure.sdk.schedule.Trigger;

public class QuartzScheduler implements Scheduler {

	private static Logger logger = LoggerFactory.getLogger(QuartzScheduler.class);
	
	private org.quartz.Scheduler underlyingScheduler;
	
	@Override
	public void schedule(Runnable runnable, Trigger trigger) {
		QuartzTrigger quartzTrigger = (QuartzTrigger) trigger;
		underlyingScheduler = QuartzSchedulers.createUniqueScheduler();
		JobDetail jobDetail = new JobDetail(
			runnable.getClass().getSimpleName() + " - job", 
			runnable.getClass().getSimpleName() + " - group", 
			CommandJob.class
		);
		
		jobDetail.getJobDataMap().put(CommandJob.COMMAND_KEY, runnable);
		
		try {
			underlyingScheduler.scheduleJob(jobDetail, quartzTrigger.getUnderlyingTrigger());
			underlyingScheduler.start();
		} catch (SchedulerException e) {
			String msg = "Failed to start job : " + e.getMessage();
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	@Override
	public void shutdown() {
		try {
			underlyingScheduler.shutdown(true);
		} catch (SchedulerException e) {
			logger.error("Failed to shutdown underlyingScheduler [" + underlyingScheduler + "], ignore it", e);
		}
		underlyingScheduler = null;
	}

	@Override
	public Class<? extends Trigger> supportedTrigger() {
		return QuartzTrigger.class;
	}

}
