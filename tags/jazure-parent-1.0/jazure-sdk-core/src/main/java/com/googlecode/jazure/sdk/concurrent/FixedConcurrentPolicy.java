package com.googlecode.jazure.sdk.concurrent;

public class FixedConcurrentPolicy implements ConcurrentPolicy {

	private static final long serialVersionUID = -3602239659789293584L;

	public static final int DEFAULT_CORE_SIZE = 1;

	private final int coreSize;

	public FixedConcurrentPolicy() {
		this(DEFAULT_CORE_SIZE);
	}
	
	public FixedConcurrentPolicy(int coreSize) {
		this.coreSize = coreSize;
	}

	@Override
	public int getCoreSize() {
		return coreSize;
	}

}
