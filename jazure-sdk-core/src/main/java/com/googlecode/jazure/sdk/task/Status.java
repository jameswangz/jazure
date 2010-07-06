package com.googlecode.jazure.sdk.task;

public enum Status {
	
	PENDING {
		@Override
		public boolean isCompleted() {
			return false;
		}
	}, 
	EXECUTING {
		@Override
		public boolean isCompleted() {
			return false;
		}
	}, 
	SUCCESSFUL {
		@Override
		public boolean isCompleted() {
			return true;
		}
	}, 
	FAILED {
		@Override
		public boolean isCompleted() {
			return true;
		}
	};
	
	public abstract boolean isCompleted();
	
}
