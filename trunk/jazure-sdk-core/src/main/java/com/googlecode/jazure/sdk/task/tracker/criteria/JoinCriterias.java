package com.googlecode.jazure.sdk.task.tracker.criteria;

import java.util.ArrayList;
import java.util.List;

public abstract class JoinCriterias {

	public static JoinCriteria create(
		final String tableName, 
		final String keyColumn, 
		final String valueColumn, 
		final String key, 
		final String op, 
		final List<String> values) {
		
		return new JoinCriteria() {
			@Override
			public String getSql(String alias, String previousAlias, boolean joinPrevious) {
				StringBuilder sql = new StringBuilder();
				
				String valuePlaceHolder = values.size() == 1 ? " ? " : Criterias.inValuePlaceHolders(values);
				
				String joinTaskSql = String.format(
					" join %s as %s on t.id = %s.task_id and %s.%s = ? and %s.%s %s %s ",
					tableName, alias, alias, alias, keyColumn, alias, valueColumn, op, valuePlaceHolder
				);
				
				sql.append(joinTaskSql);
				
				if (joinPrevious) {
					String joinPreviousSql = String.format(" and %s.task_id = %s.task_id", previousAlias, alias);
					sql.append(joinPreviousSql);
				}
				
				return sql.toString();
			}
			
			@Override
			public Object[] getArgs() {
				List<Object> args = new ArrayList<Object>();
				args.add(key);
				args.addAll(values);
				return args.toArray();
			}
		};
		
	}

}
