package com.googlecode.jazure.examples.eventdriven;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.jazure.examples.eventdriven.inner.AggregatorExample;
import com.googlecode.jazure.examples.eventdriven.inner.EventDrivenJobConfig;
import com.googlecode.jazure.examples.eventdriven.inner.ParameteredLoaderExample;
import com.googlecode.jazure.sdk.JAzure;
import com.googlecode.jazure.sdk.core.Console;
import com.googlecode.jazure.sdk.core.EnterpriseSide;
import com.googlecode.jazure.sdk.core.Module;
import com.googlecode.jazure.sdk.core.ProjectConfiguration;
import com.googlecode.jazure.sdk.endpoint.vm.VmQueueStorageEndpoint;
import com.googlecode.jazure.sdk.job.eventdriven.EventDrivenJob;
import com.googlecode.jazure.sdk.task.storage.Mysql5TaskStorageIntegrationTest;

public class EventDrivenJobExample {

//	private static Logger logger = LoggerFactory.getLogger(EventDrivenJobExample.class);
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		final EventDrivenJobConfig eventDrivenJobConfig = new EventDrivenJobConfig();
		
		final EnterpriseSide es = JAzure.createEnterprise(new Module() {
			@Override
			public void configure(Console console) {
				console.configProject(ProjectConfiguration.named("EventDrivenJobExample"))
					   .storeTaskIn(Mysql5TaskStorageIntegrationTest.getStorage())
					   .connectBy(new VmQueueStorageEndpoint());
					   
				console.addEventDrivenJobConfig(eventDrivenJobConfig)
					   .loadAt(new ParameteredLoaderExample())
					   .aggregateWith(new AggregatorExample());
			}
		});
		
		EventDrivenJob<EventDrivenJobConfig> eventDrivenJob = (EventDrivenJob<EventDrivenJobConfig>) es.getConsole().getJob(eventDrivenJobConfig);
		eventDrivenJob.start();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ParameteredLoaderExample.TASK_COUNT, 10);
		parameters.put(ParameteredLoaderExample.URL, "http://www.google.com/");
		parameters.put(ParameteredLoaderExample.COMPONENT, "gmail");
		eventDrivenJob.fireLoad(parameters);
		
		Thread.sleep(60000);
	}
	
}
