package com.googlecode.jazure.examples.rate.inner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.googlecode.jazure.sdk.concurrent.ConcurrentPolicy;
import com.googlecode.jazure.sdk.concurrent.FixedConcurrentPolicy;
import com.googlecode.jazure.sdk.loader.AbstractPollingLoader;
import com.googlecode.jazure.sdk.schedule.FixedSchedulePolicy;
import com.googlecode.jazure.sdk.schedule.SchedulePolicy;
import com.googlecode.jazure.sdk.task.AbstractTask;
import com.googlecode.jazure.sdk.task.Result;
import com.googlecode.jazure.sdk.task.Task;

public class HiltonLoader extends AbstractPollingLoader<HiltonJobConfig> {

	private static Random random = new Random();

	
	@Override
	public Collection<Task> createTasks(HiltonJobConfig jobConfig) {
		List<Task> tasks = new ArrayList<Task>();
		
		boolean exceptionThrown = false;
		
		for (final String passport : jobConfig.getPassports()) {
			exceptionThrown = !exceptionThrown;
			tasks.add(
				new HiltonTask()
				.addParameter(HiltonTask.PASSPORT_KEY, passport)
				.addParameter(HiltonTask.EXCEPTION_THROWN_KEY, exceptionThrown)
			);		
		}
		
		return tasks;
	}
	
	public static final class HiltonTask extends AbstractTask {

		private static final long serialVersionUID = -4257287092875687185L;

		public static final String TYPE = "Hilton";

		public static final String PASSPORT_KEY = "passport";
		public static final String EXCEPTION_THROWN_KEY = "exceptionThrown";
		public static final String PRICE_KEY = "price";
		public static final String START_DATE_KEY = "startDate";
		public static final String END_DATE_KEY = "endDate";
		
		private ConcurrentPolicy concurrentPolicy = new FixedConcurrentPolicy();
		private SchedulePolicy schedulePolicy = new FixedSchedulePolicy();
		
		@Override
		public Result execute() {
			HiltonResult result = callService(getPassport());
			return result;
		}

		private HiltonResult callService(String passport) {
			Boolean exceptionThrown = getParameter(EXCEPTION_THROWN_KEY, Boolean.class);
			if (!exceptionThrown) {
				return new HiltonResult(passport, new RoomRate("AAA", new BigDecimal(random.nextInt(1000)), new DateSpan("2009-01-01", "2009-01-02")));
			} 
			throw new RuntimeException("Test exception");
		}

		@Override
		public String getType() {
			return TYPE;
		}
		
		public String getPassport() {
			return getParameter(PASSPORT_KEY, String.class);
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

}
