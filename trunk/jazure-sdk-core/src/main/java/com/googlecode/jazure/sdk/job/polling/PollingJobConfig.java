package com.googlecode.jazure.sdk.job.polling;

import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.schedule.Trigger;

public interface PollingJobConfig extends JobConfig {

	Trigger getTrigger();

}
