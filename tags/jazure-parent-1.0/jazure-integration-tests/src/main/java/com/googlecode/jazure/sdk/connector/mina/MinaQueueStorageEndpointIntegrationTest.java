package com.googlecode.jazure.sdk.connector.mina;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jazure.examples.rate.inner.HiltonJobConfig;
import com.googlecode.jazure.sdk.connector.mina.roomrate.AvailabilityRQ;
import com.googlecode.jazure.sdk.connector.mina.roomrate.RoomRateTask;
import com.googlecode.jazure.sdk.core.ProjectConfiguration;
import com.googlecode.jazure.sdk.endpoint.mina.MinaQueueStorageEndpoint;
import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.task.Task;
import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.task.TaskInvocations;


public class MinaQueueStorageEndpointIntegrationTest {
	
	private static Logger logger = LoggerFactory.getLogger(MinaQueueStorageEndpointIntegrationTest.class);
	

	@Test
	public void test() {
		final MinaQueueStorageEndpoint endpoint = 
			new MinaQueueStorageEndpoint(new InetSocketAddress("localhost", 11111));
		endpoint.start();
		
		Collection<Task> tasks = new ArrayList<Task>();
		
		String dswitchUrl = "http://dswitch2";
		
		for (int i = 0; i < 100; i++) {
			tasks.add(
				new RoomRateTask()
					.addParameter(RoomRateTask.SWITCH_URL_KEY, dswitchUrl)
					.addParameter(RoomRateTask.REQUEST_KEY, new AvailabilityRQ())
			);
		}
		
		final String resultQueue = "result queue";
		JobConfig jobConfig = new HiltonJobConfig(Arrays.asList("passport1"), 0);
		List<TaskInvocation> pendingTasks = TaskInvocations.createPending(ProjectConfiguration.named("test project"), jobConfig , tasks, "task queu", resultQueue);
		
		for (TaskInvocation task : pendingTasks) {
			endpoint.send(
				task
			);	
		}
		
		
		Receiver.times(100).period(10).receive(new Runnable() {
			@Override
			public void run() {
				TaskInvocation invocation = endpoint.receive(resultQueue);
				logger.debug("task : " + invocation.getTask());
				logger.debug("result :" + invocation.getResult());
			}
		});
		
		
		endpoint.stop();
	}

	
	
	public static final class Receiver {

		private final int times;
		private int period;

		private Receiver(int times) {
			this.times = times;
		}

		public static Receiver times(int times) {
			return new Receiver(times);
		}
		
		public Receiver period(int period) {
			this.period = period;
			return this;
		}
		
		public void receive(Runnable runnable) {
			for (int i = 0; i < times; i++) {
				runnable.run();
				try {
					Thread.sleep(period);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	
}
