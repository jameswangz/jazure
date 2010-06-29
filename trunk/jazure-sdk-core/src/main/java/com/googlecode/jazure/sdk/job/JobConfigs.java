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
	
	@SuppressWarnings("serial")
	public static Function<String, JobConfig> newFromIdFunction() {
		return new Function<String, JobConfig>() {
			public JobConfig apply(final String from) {
				return new JobConfig() {
					public String getId() {
						return from;
					}
				};
			}
		};
	}
	
	public static JobConfig newFromId(String id) {
		return newFromIdFunction().apply(id);
	}
	
}
