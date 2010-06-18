package com.googlecode.jazure.sdk.job;

import java.util.EventObject;

public class JobStartedEvent extends EventObject {

	private static final long serialVersionUID = -2888368101610722369L;

	public JobStartedEvent(Object source) {
		super(source);
	}

	@Override
	public Job<?> getSource() {
		return (Job<?>) source;
	}
	
	
	
}
