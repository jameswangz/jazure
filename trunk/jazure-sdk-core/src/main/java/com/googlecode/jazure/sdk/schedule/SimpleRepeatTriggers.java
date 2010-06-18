package com.googlecode.jazure.sdk.schedule;

public abstract class SimpleRepeatTriggers {

	public static SimpleRepeatTrigger create(long period) {
		return new SimpleRepeatTrigger(0, period);
	}

}
