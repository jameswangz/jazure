package com.derbysoft.jazure.sdk.connector.mina.roomrate;

import java.util.HashMap;
import java.util.Map;

import com.derbysoft.jazure.sdk.task.Result;

public class AvailabilityResult implements Result {

	private static final long serialVersionUID = -1065712162132101828L;

	private final AvailabilityRS rs;

	public AvailabilityResult(AvailabilityRS rs) {
		this.rs = rs;
	}

	@Override
	public boolean successed() {
		return true;
	}

	@Override
	public Map<String, String> keyValues() {
		return new HashMap<String, String>();
	}

}
