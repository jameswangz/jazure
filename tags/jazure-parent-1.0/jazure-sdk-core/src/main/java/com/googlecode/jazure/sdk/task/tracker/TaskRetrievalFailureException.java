package com.googlecode.jazure.sdk.task.tracker;

import com.googlecode.jazure.sdk.core.JAzureException;

public class TaskRetrievalFailureException extends JAzureException {

	private static final long serialVersionUID = -7204835843741737873L;

	public TaskRetrievalFailureException() {
		super();
	}

	public TaskRetrievalFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskRetrievalFailureException(String message) {
		super(message);
	}

	public TaskRetrievalFailureException(Throwable cause) {
		super(cause);
	}
	
}
