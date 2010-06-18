package com.googlecode.jazure.sdk.job.exception;

import com.googlecode.jazure.sdk.core.JAzureException;

public class JobNotFoundException extends JAzureException {

	private static final long serialVersionUID = -5648433254250983933L;

	public JobNotFoundException() {
		super();
	}

	public JobNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public JobNotFoundException(String message) {
		super(message);
	}

	public JobNotFoundException(Throwable cause) {
		super(cause);
	}

	
}
