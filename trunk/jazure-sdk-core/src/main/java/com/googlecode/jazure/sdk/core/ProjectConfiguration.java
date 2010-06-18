package com.googlecode.jazure.sdk.core;

import java.io.Serializable;

import org.apache.commons.lang.time.DateUtils;

import com.google.inject.internal.Preconditions;
import com.googlecode.jazure.sdk.task.tracker.FarAwayTaskCleaner;

public class ProjectConfiguration implements Serializable {
	
	private static final long serialVersionUID = 4040271265197095139L;

	public static final int NO_RETRY = 0;
	public static final long ONE_SECOND = 1000L;
	public static final long DEFAULT_BUS_POLL_INTERVAL = ONE_SECOND;
	public static final int DEFAULT_HOURS_TO_LIVE = 24;
	public static final long DEFAULT_PERIOD_OF_TASK_CLEANING = DateUtils.MILLIS_PER_HOUR / 2;

	private final String projectName;
	private int retryTimes = NO_RETRY;
	private long retryInterval = ONE_SECOND;
	private int hoursToLiveOfTask = DEFAULT_HOURS_TO_LIVE;
	private long periodOfTaskCleaning = DEFAULT_PERIOD_OF_TASK_CLEANING;
	private long busPollInterval = DEFAULT_BUS_POLL_INTERVAL;

	private ProjectConfiguration(String projectName) {
		this.projectName = projectName;
	}

	public static ProjectConfiguration named(String projectName) {
		return new ProjectConfiguration(projectName);
	}

	public String getProjectName() {
		return projectName;
	}

	/**
	 * Retry times when task execute failed
	 * @param retryTimes retry times
	 * @return project configuration
	 */
	public ProjectConfiguration retryTimes(int retryTimes) {
		Preconditions.checkArgument(retryTimes >= 0, "Invalid retryTimes %s", retryTimes);
		this.retryTimes = retryTimes;
		return this;
	}
	
	/**
	 * Retry interval when task execute failed in millisecond
	 * @param retryInterval retry interval
	 * @return project configuration
	 */
	public ProjectConfiguration retryInterval(long retryInterval) {
		Preconditions.checkArgument(retryTimes > 0, "Invalid retryInterval %s", retryInterval);
		this.retryInterval = retryInterval;
		return this;
	}
	
	/**
	 * Set hours to live of task, {@link FarAwayTaskCleaner} will clean the out-of-date tasks silently
	 * @param hoursToLiveOfTask hours to live of task
	 * @return project configuration
	 * @see FarAwayTaskCleaner
	 */
	public ProjectConfiguration hoursToLiveOfTask(int hoursToLiveOfTask) {
		Preconditions.checkArgument(hoursToLiveOfTask > 0, "Invalid hoursToLiveOfTask %s", hoursToLiveOfTask);
		this.hoursToLiveOfTask = hoursToLiveOfTask;
		return this;
	}
	
	public ProjectConfiguration periodOfTaskCleaning(long periodOfTaskCleaning) {
		Preconditions.checkArgument(periodOfTaskCleaning > 0, "Invalid periodOfTaskCleaning %s", periodOfTaskCleaning);
		this.periodOfTaskCleaning = periodOfTaskCleaning;
		return this;
	}
	
	public ProjectConfiguration busPollInterval(long busPollInterval) {
		Preconditions.checkArgument(busPollInterval > 0, "Invalid busPollInterval %s", busPollInterval);
		this.busPollInterval = busPollInterval;
		return this;
	}
	
	public int getRetryTimes() {
		return retryTimes;
	}

	public long getRetryInterval() {
		return retryInterval;
	}
	
	public int getHoursToLiveOfTask() {
		return hoursToLiveOfTask;
	}

	public long getPeriodOfTaskCleaning() {
		return periodOfTaskCleaning;
	}

	public long getBusPollInterval() {
		return busPollInterval;
	}
	
	
}
