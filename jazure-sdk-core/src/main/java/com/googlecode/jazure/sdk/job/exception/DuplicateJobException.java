package com.googlecode.jazure.sdk.job.exception;

import com.googlecode.jazure.sdk.core.JAzureException;

public class DuplicateJobException extends JAzureException {

	private static final long serialVersionUID = -2082815754712900272L;

	public DuplicateJobException() {
		super();
	}

	public DuplicateJobException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateJobException(String message) {
		super(message);
	}

	public DuplicateJobException(Throwable cause) {
		super(cause);
	}
	
	
}
