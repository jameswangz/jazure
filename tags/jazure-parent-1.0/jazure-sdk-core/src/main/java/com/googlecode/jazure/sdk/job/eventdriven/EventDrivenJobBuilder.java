package com.googlecode.jazure.sdk.job.eventdriven;

import com.googlecode.jazure.sdk.job.JobBuilder;
import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.loader.EventDrivenLoader;

public interface EventDrivenJobBuilder<T extends JobConfig> extends JobBuilder<T> {

	EventDrivenJobBuilder<T> loadAt(EventDrivenLoader<T> eventDrivenLoader);

}
