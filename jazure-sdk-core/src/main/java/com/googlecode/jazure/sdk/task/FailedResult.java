package com.googlecode.jazure.sdk.task;

import java.util.HashMap;
import java.util.Map;

public class FailedResult implements Result {

	private static final long serialVersionUID = 6034877256367465305L;
	
	private String exceptionMessage;
	private StackTraceElement[] exceptionStackTrace;

	private FailedResult(Throwable t) {
		this.exceptionMessage = t.getMessage();
		this.exceptionStackTrace = t.getStackTrace();
	}

	public static Result fromError(Throwable t) {
		return new FailedResult(t);
	}
	
	@Override
	public boolean successful() {
		return false;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public StackTraceElement[] getExceptionStackTrace() {
		return exceptionStackTrace;
	}

	@Override
	public Map<String, String> keyValues() {
		return new HashMap<String, String>();
	}

	

}
