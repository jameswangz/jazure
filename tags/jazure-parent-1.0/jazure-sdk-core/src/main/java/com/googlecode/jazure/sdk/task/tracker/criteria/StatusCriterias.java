package com.googlecode.jazure.sdk.task.tracker.criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.jazure.sdk.task.Status;

public abstract class StatusCriterias {

	public static StatusCriteria eq(final Status status) {
		return new StatusCriteria() {
			@Override
			public String getSql() {
				return " t.status = ? ";
			}
			
			@Override
			public Object[] getArgs() {
				return new Object[] { status.name() };
			}
		};
	}
	
	public static StatusCriteria in(final Collection<Status> statuses) {
		return new StatusCriteria() {
			@Override
			public String getSql() {
				return new StringBuilder(" t.status in ").append(Criterias.inValuePlaceHolders(statuses)).toString();
			}
			
			@Override
			public Object[] getArgs() {
				List<Object> args = new ArrayList<Object>();
				for (Status status : statuses) {
					args.add(status.name());
				}
				return args.toArray();
			}
		};
	}

}
