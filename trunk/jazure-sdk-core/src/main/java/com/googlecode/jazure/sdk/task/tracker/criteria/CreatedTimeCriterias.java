package com.googlecode.jazure.sdk.task.tracker.criteria;

import java.util.Date;

public class CreatedTimeCriterias {

	public static CreatedTimeCriteria between(final Date start, final Date end) {
		return new CreatedTimeCriteria() {
			@Override
			public String getSql() {
				return " t.created_time between ? and ? ";
			}
			
			@Override
			public Object[] getArgs() {
				return new Object[] {start, end};
			}
		};
	}
	
	public static CreatedTimeCriteria ge(final Date start) {
		return new CreatedTimeCriteria() {
			@Override
			public String getSql() {
				return " t.created_time >= ? ";
			}
			
			@Override
			public Object[] getArgs() {
				return new Object[] {start};
			}
		};
	}
	
	public static CreatedTimeCriteria gt(final Date start) {
		return new CreatedTimeCriteria() {
			@Override
			public String getSql() {
				return " t.created_time > ? ";
			}
			
			@Override
			public Object[] getArgs() {
				return new Object[] {start};
			}
		};
	}
	
	public static CreatedTimeCriteria le(final Date end) {
		return new CreatedTimeCriteria() {
			@Override
			public String getSql() {
				return " t.created_time <= ? ";
			}
			
			@Override
			public Object[] getArgs() {
				return new Object[] {end};
			}
		};
	}
	
	public static CreatedTimeCriteria lt(final Date end) {
		return new CreatedTimeCriteria() {
			@Override
			public String getSql() {
				return " t.created_time < ? ";
			}
			
			@Override
			public Object[] getArgs() {
				return new Object[] {end};
			}
		};
	}


}
