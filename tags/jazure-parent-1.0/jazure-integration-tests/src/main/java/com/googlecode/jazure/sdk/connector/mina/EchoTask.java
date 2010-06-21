package com.googlecode.jazure.sdk.connector.mina;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.jazure.sdk.concurrent.ConcurrentPolicy;
import com.googlecode.jazure.sdk.concurrent.FixedConcurrentPolicy;
import com.googlecode.jazure.sdk.schedule.FixedSchedulePolicy;
import com.googlecode.jazure.sdk.schedule.SchedulePolicy;
import com.googlecode.jazure.sdk.task.AbstractTask;
import com.googlecode.jazure.sdk.task.Result;

public class EchoTask extends AbstractTask {

	private static final long serialVersionUID = -1170532750426327798L;
	
	private ConcurrentPolicy concurrentPolicy = new FixedConcurrentPolicy();
	private SchedulePolicy schedulePolicy = new FixedSchedulePolicy();

	@Override
	public Result execute() {
		return new EchoResult();
	}

	@Override
	public String getType() {
		return "Echo";
	}
	
	@Override
	public ConcurrentPolicy getConcurrentPolicy() {
		return concurrentPolicy;
	}

	@Override
	public SchedulePolicy getSchedulePolicy() {
		return schedulePolicy;
	}

	private static final class EchoResult implements Result {

		private static final long serialVersionUID = -6008161370445519735L;

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
