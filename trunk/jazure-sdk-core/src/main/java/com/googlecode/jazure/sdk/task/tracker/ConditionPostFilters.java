package com.googlecode.jazure.sdk.task.tracker;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public abstract class ConditionPostFilters {
	
	public static ConditionPostFilter alwaysTrue() {
		return ALWAYS_TRUE_FILTER.INSTANCE;
	}
	
	enum ALWAYS_TRUE_FILTER implements ConditionPostFilter {
		
		INSTANCE;

		@Override
		public boolean accept(TaskInvocation invocation) {
			return true;
		}

		@Override
		public String toString() {
			return "Always true condition post filter.";
		}
		
	}
}
