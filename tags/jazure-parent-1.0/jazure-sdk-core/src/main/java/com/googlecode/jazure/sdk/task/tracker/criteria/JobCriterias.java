package com.googlecode.jazure.sdk.task.tracker.criteria;

public class JobCriterias {

	public static JobCriteria idEq(final String id) {
		return new JobCriteria() {
			@Override
			public String getSql() {
				return " t.job_id = ? ";
			}
			
			@Override
			public Object[] getArgs() {
				return new Object[] {id};
			}
		};
	}

}
