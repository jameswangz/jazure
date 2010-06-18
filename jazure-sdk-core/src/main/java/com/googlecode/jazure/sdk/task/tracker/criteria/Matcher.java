package com.googlecode.jazure.sdk.task.tracker.criteria;

public enum Matcher {
	
	EXACT {
		@Override
		public String matched(String value) {
			return value;
		}
	},
	
	START {
		@Override
		public String matched(String value) {
			return ANY + value;
		}
	},
	
	END {
		@Override
		public String matched(String value) {
			return value + ANY;
		}
	},
	
	ANY_WHERE {
		@Override
		public String matched(String value) {
			return ANY + value + ANY;
		}
	};

	private static final String ANY = "%";

	public abstract String matched(String value);

}
