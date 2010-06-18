package com.googlecode.jazure.sdk.schedule.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommandJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(CommandJob.class);
	
	public static final String COMMAND_KEY = "command";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			Runnable command = (Runnable) context.getJobDetail().getJobDataMap().get(COMMAND_KEY);
			command.run();			
		} catch (RuntimeException e) {
			logger.error("Error raised during job excution : " + e.getMessage() + ", just ignore it", e);
		}
	}

}
