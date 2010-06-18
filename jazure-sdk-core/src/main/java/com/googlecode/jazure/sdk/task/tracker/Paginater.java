package com.googlecode.jazure.sdk.task.tracker;

public class Paginater {

	private int offerset;
	private int maxResults;
	private long totalCount;

	public Paginater() {
	}
	
	public Paginater(int offerset) {
		this.offerset = offerset;
	}

	public static Paginater offerset(int offerset) {
		return new Paginater(offerset);
	}

	public Paginater maxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public long getTotalCount() {
		return this.totalCount;
	}

	public Paginater setTotalCount(long totalCount) {
		this.totalCount = totalCount;
		return this;
	}
	
	public int getOfferset() {
		return offerset;
	}

	public int getMaxResults() {
		return maxResults;
	}

}
