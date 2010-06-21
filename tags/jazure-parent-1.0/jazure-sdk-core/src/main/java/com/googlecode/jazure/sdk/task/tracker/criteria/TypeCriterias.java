package com.googlecode.jazure.sdk.task.tracker.criteria;

import java.util.Collection;

public abstract class TypeCriterias {

	public static TypeCriteria eq(String type) {
		return createSingleParam(" = ", type);
	}
	
	public static TypeCriteria ne(String type) {
		return createSingleParam(" != ", type);
	}

	public static TypeCriteria like(String type) {
		return like(type, Matcher.ANY_WHERE);
	}

	public static TypeCriteria like(String type, Matcher matcher) {
		return createSingleParam(" like ", matcher.matched(type));
	}

	private static TypeCriteria createSingleParam(final String op, final String type) {
		return new TypeCriteria() {
			@Override
			public String getSql() {
				return String.format(" t.type %s ? ", op);
			}
			
			@Override
			public Object[] getArgs() {
				return new Object[] {type};
			}
		};
	}
	
	public static TypeCriteria in(final Collection<String> types) {
		return new TypeCriteria() {
			@Override
			public String getSql() {
				return new StringBuilder(" t.type in ").append(Criterias.inValuePlaceHolders(types)).toString();
			}
			
			@Override
			public Object[] getArgs() {
				return types.toArray();
			};
		};
	}

}
