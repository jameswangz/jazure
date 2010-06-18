package com.googlecode.jazure.examples.rate.inner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.SimpleTrigger;

import com.googlecode.jazure.sdk.job.polling.PollingJobConfig;
import com.googlecode.jazure.sdk.schedule.Trigger;
import com.googlecode.jazure.sdk.schedule.quartz.QuartzTrigger;


public class HiltonJobConfig implements PollingJobConfig {

	private static final long serialVersionUID = -2541060635172484766L;

	public static final String ID = "Hilton job";

	private List<String> passports = new ArrayList<String>();

	private final int idIndex;
	
	public HiltonJobConfig(List<String> passports, int idIndex) {
		this.passports = passports;
		this.idIndex = idIndex;
	}

	public List<String> getPassports() {
		return passports;
	}

	@Override
	public String getId() {
		return ID + idIndex;
	}

	@Override
	public Trigger getTrigger() {
		return new QuartzTrigger(createTrigger("hilton"));
	}

	private static org.quartz.Trigger createTrigger(String name) {
		SimpleTrigger simpleTrigger = new SimpleTrigger(name, null, SimpleTrigger.REPEAT_INDEFINITELY, 10000);
		simpleTrigger.setStartTime(new Date());
		return simpleTrigger;
	}

}
