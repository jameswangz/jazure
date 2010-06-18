package com.googlecode.jazure.examples.rate.inner;

import java.io.Serializable;
import java.math.BigDecimal;

public class RoomRate implements Serializable  {

	private static final long serialVersionUID = -1064365264267796066L;

	private final String roomtypecode;
	private final BigDecimal price;
	private final DateSpan dateSpan;

	public RoomRate(String roomtypecode, BigDecimal price, DateSpan dateSpan) {
		this.roomtypecode = roomtypecode;
		this.price = price;
		this.dateSpan = dateSpan;
	}

}
