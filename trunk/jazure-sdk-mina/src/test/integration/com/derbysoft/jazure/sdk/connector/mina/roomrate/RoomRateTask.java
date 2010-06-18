package com.derbysoft.jazure.sdk.connector.mina.roomrate;

import com.derbysoft.jazure.sdk.concurrent.ConcurrentPolicy;
import com.derbysoft.jazure.sdk.concurrent.FixedConcurrentPolicy;
import com.derbysoft.jazure.sdk.schedule.FixedSchedulePolicy;
import com.derbysoft.jazure.sdk.schedule.SchedulePolicy;
import com.derbysoft.jazure.sdk.task.AbstractTask;
import com.derbysoft.jazure.sdk.task.Result;

public class RoomRateTask extends AbstractTask {

	private static final long serialVersionUID = -6955362421282346543L;

	public static final String SWITCH_URL_KEY = "dswitchUrl";
	public static final String REQUEST_KEY = "request";
	
	private ConcurrentPolicy concurrentPolicy = new FixedConcurrentPolicy();
	private SchedulePolicy schedulePolicy = new FixedSchedulePolicy();

	@Override
	public Result execute() {
		RoomRateService service = new RoomRateServiceFactory(getParameter(SWITCH_URL_KEY, String.class)).getService();
		AvailabilityRS rs = service.getAvailability(getParameter(REQUEST_KEY, AvailabilityRQ.class));
		return new AvailabilityResult(rs);
	}

	@Override
	public String getType() {
		return "RoomRate";
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
