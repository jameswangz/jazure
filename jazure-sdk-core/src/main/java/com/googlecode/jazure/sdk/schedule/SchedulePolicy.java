package com.googlecode.jazure.sdk.schedule;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public interface SchedulePolicy extends Serializable {

	TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
	
	long getInitialDelay();
	
	long getPeriod();
	
}
