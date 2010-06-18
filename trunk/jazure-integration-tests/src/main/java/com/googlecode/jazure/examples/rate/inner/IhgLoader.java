package com.googlecode.jazure.examples.rate.inner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.jazure.sdk.concurrent.ConcurrentPolicy;
import com.googlecode.jazure.sdk.concurrent.FixedConcurrentPolicy;
import com.googlecode.jazure.sdk.loader.AbstractPollingLoader;
import com.googlecode.jazure.sdk.schedule.FixedSchedulePolicy;
import com.googlecode.jazure.sdk.schedule.SchedulePolicy;
import com.googlecode.jazure.sdk.task.AbstractTask;
import com.googlecode.jazure.sdk.task.Result;
import com.googlecode.jazure.sdk.task.Task;

public class IhgLoader extends AbstractPollingLoader<IhgJobConfig> {

	@Override
	public Collection<Task> createTasks(IhgJobConfig jobConfig) {
		List<Task> tasks = new ArrayList<Task>();
		
		for (int i = 0; i < 10; i++) {
			tasks.add(new IhgTask().addParameter(IhgTask.PASSPORT_KEY, "ihg hotel " + i));
		}
		
		return tasks;
	}

	public static final class IhgTask extends AbstractTask {

		private static final long serialVersionUID = -3984319424355406285L;

		public static final String PASSPORT_KEY = "passport";
		private ConcurrentPolicy concurrentPolicy = new FixedConcurrentPolicy();
		private SchedulePolicy schedulePolicy = new FixedSchedulePolicy();

		@Override
		public Result execute() {
			return new IhgResult(getParameter(PASSPORT_KEY, String.class));
		}

		@Override
		public String getType() {
			return "IHG";
		}

		@Override
		public ConcurrentPolicy getConcurrentPolicy() {
			return concurrentPolicy;
		}

		@Override
		public SchedulePolicy getSchedulePolicy() {
			return schedulePolicy;
		}
	}
	
	private static final class IhgResult implements Result {

		private static final long serialVersionUID = 778980604504092945L;

		private final String passport;

		public IhgResult(String passport) {
			this.passport = passport;
		}

		@Override
		public boolean successed() {
			return true;
		}

		@Override
		public Map<String, String> keyValues() {
			return new HashMap<String, String>();
		}
		
	}
	

}
