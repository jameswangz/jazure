package com.googlecode.jazure.sdk.task.tracker.criteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.jazure.sdk.task.Result;

public abstract class ResultCriterias {

	public static final String RESERVED_RESULT_TYPE_KEY = "system reserved - resultType";

	public static ResultCriteria typeIn(List<Class<? extends Result>> resultTypes) {
		List<String> values = new ArrayList<String>();
		
		for (Class<? extends Result> resultType : resultTypes) {
			values.add(resultType.getName());
		}
		
		return create(RESERVED_RESULT_TYPE_KEY, " in ", values );
	}

	public static ResultCriteria typeEq(Class<? extends Result> resultType) {
		return create(RESERVED_RESULT_TYPE_KEY, " = ", Collections.singletonList(resultType.getName()));
	}
	
	public static ResultCriteria eq(String key, String value) {
		return create(key, " = ", Collections.singletonList(value));
	}
	
	public static ResultCriteria ne(String key, String value) {
		return create(key, " != ", Collections.singletonList(value));
	}
	
	public static ResultCriteria like(String key, String value) {
		return like(key, value, Matcher.ANY_WHERE);
	}
	
	public static ResultCriteria like(String key, String value, Matcher matcher) {
		return create(key, " like ", Collections.singletonList(matcher.matched(value)));
	}
	
	public static ResultCriteria ge(String key, String value) {
		return create(key, " >= ", Collections.singletonList(value));
	}
	
	public static ResultCriteria gt(String key, String value) {
		return create(key, " > ", Collections.singletonList(value));
	}
	
	public static ResultCriteria le(String key, String value) {
		return create(key, " <= ", Collections.singletonList(value));
	}
	
	public static ResultCriteria lt(String key, String value) {
		return create(key, " < ", Collections.singletonList(value));
	}
	
	public static ResultCriteria in(String key, List<String> values) {
		return create(key, " in ", values);
	}
	
	private static ResultCriteria create(final String key, final String op, final List<String> values) {
		final JoinCriteria delegate = JoinCriterias.create(
			"jazure_task_result", 
			"result_key", 
			"result_value", 
			key, 
			op, 
			values
		);
		
		return new ResultCriteria() {
			@Override
			public Object[] getArgs() {
				return delegate.getArgs();
			}
			
			@Override
			public String getSql(String alias, String previousAlias, boolean joinPrevious) {
				return delegate.getSql(alias, previousAlias, joinPrevious);
			}
		};
	}
}
