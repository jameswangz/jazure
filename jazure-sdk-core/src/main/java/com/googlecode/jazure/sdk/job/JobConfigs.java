package com.googlecode.jazure.sdk.job;

import com.google.common.base.Function;

public abstract class JobConfigs {
	
	public static Function<JobConfig, String> idFunction() {
		return new Function<JobConfig, String>() {
			public String apply(JobConfig jobConfig) {
				return jobConfig.getId();
			}
		};
	}
	
}
