package com.googlecode.jazure.sdk.task.tracker.criteria;

import java.util.Collection;

public abstract class Criterias {

	public static String inValuePlaceHolders(Collection<?> collection) {
		StringBuilder sql = new StringBuilder();
		sql.append("(");
		int size = collection.size();
		for (int i = 0; i < size; i++) {
			sql.append("?");
			if (i < size - 1) {
				sql.append(", ");
			}
		}
		sql.append(")");
		return sql.toString();
	}

}
