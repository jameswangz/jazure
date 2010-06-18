package com.googlecode.jazure.examples.rate.inner;

import java.io.Serializable;

public class DateSpan implements Serializable {

	private static final long serialVersionUID = -6829755384314061789L;

	private final String start;
	private final String end;

	public DateSpan(String start, String end) {
		this.start = start;
		this.end = end;
	}

}
