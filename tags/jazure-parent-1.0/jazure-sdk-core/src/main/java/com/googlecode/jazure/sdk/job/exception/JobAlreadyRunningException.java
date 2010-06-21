package com.googlecode.jazure.sdk.job.exception;

import com.googlecode.jazure.sdk.core.JAzureException;

public class JobAlreadyRunningException extends JAzureException {

	private static final long serialVersionUID = 6456167823870701334L;

	public JobAlreadyRunningException() {
		super();
	}

	public JobAlreadyRunningException(String message, Throwable cause) {
		super(message, cause);
	}

	public JobAlreadyRunningException(String message) {
		super(message);
	}

	public JobAlreadyRunningException(Throwable cause) {	
		super(cause);
	}
	
	
}
