package com.googlecode.jazure.sdk.schedule.quartz;

import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.utils.PropertiesParser;
import org.springframework.beans.DirectFieldAccessor;

abstract class QuartzSchedulers {
	
	private static AtomicInteger schdulerIndex = new AtomicInteger();
	
	public static Scheduler createUniqueScheduler() {
		try {
			StdSchedulerFactory schedFactory = new StdSchedulerFactory();
			schedFactory.initialize();
			//set unique scheduler name to avoid StdSchedulerFactory reuse the cached scheduler
			setSchedulerName(schedFactory, "Scheduler [" + schdulerIndex.incrementAndGet() + "]");
			return schedFactory.getScheduler();			
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @see StdSchedulerFactory#getScheduler()
	 */
	private static void setSchedulerName(StdSchedulerFactory schedFactory, String schedulerName) {
		DirectFieldAccessor accessor = new DirectFieldAccessor(schedFactory);
		PropertiesParser propertiesParser = (PropertiesParser) accessor.getPropertyValue("cfg");
		propertiesParser.getUnderlyingProperties().setProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, schedulerName);
	}
	
	
}
