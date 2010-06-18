package com.googlecode.jazure.sdk.task.tracker.criteria;

public interface JoinCriteria extends Criteria {

	String getSql(String alias, String previousAlias, boolean joinPrevious);
	
}
