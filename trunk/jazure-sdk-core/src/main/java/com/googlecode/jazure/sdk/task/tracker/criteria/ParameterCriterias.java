package com.googlecode.jazure.sdk.task.tracker.criteria;

import java.util.Collections;
import java.util.List;


public abstract class ParameterCriterias {

	public static ParameterCriteria eq(String paramName, String paramValue) {
		return create(paramName, " = ", Collections.singletonList(paramValue));
	}
	
	public static ParameterCriteria ne(String paramName, String paramValue) {
		return create(paramName, " != ", Collections.singletonList(paramValue));
	}
	
	public static ParameterCriteria like(String paramName, String paramValue) {
		return like(paramName, paramValue, Matcher.ANY_WHERE);
	}

	public static ParameterCriteria like(String paramName, String paramValue, Matcher matcher) {
		return create(paramName, " like ", Collections.singletonList(matcher.matched(paramValue)));
	}
	
	public static ParameterCriteria gt(String paramName, String paramValue) {
		return create(paramName, " > ", Collections.singletonList(paramValue));
	}

	public static ParameterCriteria ge(String paramName, String paramValue) {
		return create(paramName, " >= ", Collections.singletonList(paramValue));
	}

	public static ParameterCriteria lt(String paramName, String paramValue) {
		return create(paramName, " < ", Collections.singletonList(paramValue));
	}
	
	public static ParameterCriteria le(String paramName, String paramValue) {
		return create(paramName, " <= ", Collections.singletonList(paramValue));
	}

	public static ParameterCriteria in(String paramName, List<String> paramValues) {
		return create(paramName, " in ", paramValues);
	}
	
	private static ParameterCriteria create(String name, String op, List<String> values) {
		final JoinCriteria delegate = JoinCriterias.create(
			"jazure_task_param", 
			"param_key", 
			"param_value", 
			name, 
			op, 
			values
		);
		
		return new ParameterCriteria() {
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
