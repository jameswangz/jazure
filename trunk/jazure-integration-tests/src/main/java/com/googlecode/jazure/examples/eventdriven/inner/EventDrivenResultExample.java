package com.googlecode.jazure.examples.eventdriven.inner;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.jazure.sdk.task.Result;

public class EventDrivenResultExample implements Result {

	private static final long serialVersionUID = 1250836550816184362L;

	private final String result;

	public EventDrivenResultExample(String result) {
		this.result = result;
	}

	@Override
	public boolean successed() {
		return true;
	}

	public String getResult() {
		return result;
	}

	@Override
	public Map<String, String> keyValues() {
		return new HashMap<String, String>();
	}

}
