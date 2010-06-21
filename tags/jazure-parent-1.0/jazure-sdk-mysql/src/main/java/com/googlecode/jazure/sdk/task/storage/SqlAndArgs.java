package com.googlecode.jazure.sdk.task.storage;

import java.util.ArrayList;
import java.util.List;


public class SqlAndArgs {

	private StringBuilder sqlBuilder = new StringBuilder();
	private List<Object> argsBuilder = new ArrayList<Object>();

	public String getSql() {
		return sqlBuilder.toString();
	}

	public Object[] getArgs() {
		return argsBuilder.toArray();
	}

	public SqlAndArgs appendSql(String sql) {
		sqlBuilder.append(sql);
		return this;
	}
	
	public SqlAndArgs appendAnd() {
		return appendSql(" and ");
	}

	public SqlAndArgs appendSqlAndArgs(String sql, Object[] args) {
		sqlBuilder.append(sql);
		if (args != null) {
			for (Object arg : args) {
				argsBuilder.add(arg);
			}
		}
		return this;
	}
	
}
