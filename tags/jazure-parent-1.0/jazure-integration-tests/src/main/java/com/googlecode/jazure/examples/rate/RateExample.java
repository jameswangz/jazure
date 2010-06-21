package com.googlecode.jazure.examples.rate;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jazure.examples.rate.inner.HiltonAggregator;
import com.googlecode.jazure.examples.rate.inner.HiltonJobConfig;
import com.googlecode.jazure.examples.rate.inner.HiltonLoader;
import com.googlecode.jazure.examples.rate.inner.IhgAggregator;
import com.googlecode.jazure.examples.rate.inner.IhgJobConfig;
import com.googlecode.jazure.examples.rate.inner.IhgLoader;
import com.googlecode.jazure.examples.rate.inner.HiltonLoader.HiltonTask;
import com.googlecode.jazure.sdk.JAzure;
import com.googlecode.jazure.sdk.core.Console;
import com.googlecode.jazure.sdk.core.EnterpriseSide;
import com.googlecode.jazure.sdk.core.Module;
import com.googlecode.jazure.sdk.core.ProjectConfiguration;
import com.googlecode.jazure.sdk.endpoint.vm.VmQueueStorageEndpoint;
import com.googlecode.jazure.sdk.task.Task;
import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.task.storage.Mysql5TaskStorageIntegrationTest;
import com.googlecode.jazure.sdk.task.tracker.ConditionPostFilter;


public class RateExample {
	
	private static Logger logger = LoggerFactory.getLogger(RateExample.class);
	
	public static void main(String[] args) throws Exception {
		final EnterpriseSide es = JAzure.createEnterprise(new Module() {
			@Override
			public void configure(Console console) {
				console.configProject(ProjectConfiguration.named("RateExample").retryTimes(2))
					   .storeTaskIn(Mysql5TaskStorageIntegrationTest.getStorage())
//					   .storeTaskIn(new MemoryTaskStorage())
//					   .connectBy(new MinaQueueStorageEndpoint(new InetSocketAddress("localhost", 11111)));
					   .connectBy(new VmQueueStorageEndpoint());
					   
				
				AtomicInteger idIndex = new AtomicInteger();

				for (int i = 0; i < 1; i++) {
					console.addPollingJobConfig(new HiltonJobConfig(Arrays.asList(new String[] {"passport1", "passport2"}), idIndex.incrementAndGet()))					    
					   .loadAt(new HiltonLoader())
					   .aggregateWith(new HiltonAggregator());					
				}
				
				console.addPollingJobConfig(new IhgJobConfig())
				       .loadAt(new IhgLoader())
				       .aggregateWith(new IhgAggregator());
			}
		});
		
		es.start();
		
//		Thread.sleep(1000000);
//		
//		TaskTracker taskTracker = es.getConsole().getTaskTracker();
//		Paginater paginater = Paginater.offerset(5).maxResults(100);
//		PaginatedList<TaskInvocation> tasks = taskTracker.getTasks(
//			new TaskCondition(null, "Hilton", paginater), new HiltonConditionFilter("passport1")
//		);
//		
//		logger.debug("Total conditional count : " + tasks.getPaginater().getTotalCount());
//		
//		for (TaskInvocation invocation : tasks) {
//			logger.debug("id " + invocation.getId());
//			logger.debug(invocation.getTask().getType());
//			logger.debug("status " + invocation.getStatus().name());
//			logger.debug("gridworker " + invocation.getMetaData().getGridWorker());
//			if (invocation.isCompleted()) {
//				Result result = invocation.getResult();
//				logger.debug("result : " + result == null ? "null" : result.toString());
//			}
//		}
//		
//		Thread.sleep(60000);
//		
//		//re execute failed invocation
//		PaginatedList<TaskInvocation> failedTasks = taskTracker.getTasks(
//				new TaskCondition(Status.FAILED, HiltonTask.TYPE, paginater), new HiltonConditionFilter("passport1")
//		);
//		
//		for (TaskInvocation task : failedTasks) {
//			es.getConsole().executeTask(task);
//		}
//		
//		Thread.sleep(10000000);
//		
//		es.stop();
	}
	
	private static final class HiltonConditionFilter implements ConditionPostFilter {
		
		private String passport;

		public HiltonConditionFilter(String passport) {
			this.passport = passport;
		}

		@Override
		public boolean accept(TaskInvocation invocation) {
			Task task = invocation.getTask();
			if (HiltonTask.class.isInstance(task)) {
				return passport.equals(((HiltonTask) task).getPassport());
			}
			return true;
		}
	}
	

}
