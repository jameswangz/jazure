package com.googlecode.jazure.sdk.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.googlecode.functionalcollections.FunctionalIterable;
import com.googlecode.functionalcollections.FunctionalIterables;
import com.googlecode.jazure.sdk.endpoint.QueueStorageEndpoint;
import com.googlecode.jazure.sdk.event.ConcurrentEventPublisher;
import com.googlecode.jazure.sdk.event.EventListener;
import com.googlecode.jazure.sdk.event.EventPublisher;
import com.googlecode.jazure.sdk.job.AbstractJobBuilder;
import com.googlecode.jazure.sdk.job.Job;
import com.googlecode.jazure.sdk.job.JobBuilder;
import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.job.eventdriven.EventDrivenJobBuilder;
import com.googlecode.jazure.sdk.job.eventdriven.EventDrivenJobBuilderImpl;
import com.googlecode.jazure.sdk.job.exception.DuplicateJobException;
import com.googlecode.jazure.sdk.job.exception.JobAlreadyRunningException;
import com.googlecode.jazure.sdk.job.exception.JobNotFoundException;
import com.googlecode.jazure.sdk.job.exception.JobNotRunningException;
import com.googlecode.jazure.sdk.job.polling.PollingJobBuilder;
import com.googlecode.jazure.sdk.job.polling.PollingJobBuilderImpl;
import com.googlecode.jazure.sdk.job.polling.PollingJobConfig;
import com.googlecode.jazure.sdk.lifecycle.LifeCycleWrapper;
import com.googlecode.jazure.sdk.lifecycle.LifeCycles;
import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.task.storage.TaskStorage;
import com.googlecode.jazure.sdk.task.tracker.FarAwayTaskCleaner;
import com.googlecode.jazure.sdk.task.tracker.TaskInvocationListener;
import com.googlecode.jazure.sdk.task.tracker.TaskTracker;
import com.googlecode.jazure.sdk.task.tracker.WriteableTaskTracker;

class ConsoleImpl implements Console {

	private static Logger logger = LoggerFactory.getLogger(ConsoleImpl.class);
	
	private List<AbstractJobBuilder<?>> jobBuilders = new ArrayList<AbstractJobBuilder<?>>();
	private List<Job<?>> jobs = new ArrayList<Job<?>>();
	private TaskStorage taskStorage;
	private ProjectConfiguration projectConfiguration;
	private QueueStorageEndpoint queueStorageEndpoint;
	private Injector injector;
	private LifeCycleWrapper lifeCycleWrapper = LifeCycles.wrapped();
	private FarAwayTaskCleaner farAwayTaskCleaner;
	
	@Override
	public synchronized <T extends PollingJobConfig> PollingJobBuilder<T> addPollingJobConfig(T jobConfig) throws DuplicateJobException {
		checkDuplicate(jobConfig);
		PollingJobBuilderImpl<T> jobBuilder = new PollingJobBuilderImpl<T>(jobConfig);
		this.jobBuilders.add(jobBuilder);
		return jobBuilder;
	}
	
	@Override
	public synchronized <T extends JobConfig> EventDrivenJobBuilder<T> addEventDrivenJobConfig(T jobConfig) throws DuplicateJobException {
		checkDuplicate(jobConfig);
		EventDrivenJobBuilderImpl<T> jobBuilder = new EventDrivenJobBuilderImpl<T>(jobConfig);
		this.jobBuilders.add(jobBuilder);
		return jobBuilder;
	}

	private void checkDuplicate(final JobConfig jobConfig) throws DuplicateJobException {
		String jobId = jobConfig.getId();
		boolean exists = FunctionalIterables.make(jobBuilders).any(predicateOfBuilder(jobId));
		if (exists) {
			throw new DuplicateJobException("Duplicate job config [" + jobId + "]");
		}
	}

	@Override
	public Console removeJobConfig(JobConfig jobConfig) throws JobNotFoundException, JobAlreadyRunningException {
		String jobId = jobConfig.getId();
		JobBuilder<?> jobBuilder = loadJobBuilder(jobId);
		Job<?> job = loadJob(jobId);
		if (job.isRunning()) {
			throw new JobAlreadyRunningException("Job [" + jobId + "] already running now, please stop it before remove job config.");
		}
		jobBuilders.remove(jobBuilder);
		return this;
	}
	
	@Override
	public TaskTracker getTaskTracker() {
		return injector.getInstance(WriteableTaskTracker.class);
	}

	@Override
	public List<Job<?>> getJobs() {
		return this.jobs;
	}

	@Override
	public Console rebuildJobs() {
		buildJobs();
		return this;
	}	
	
	@Override
	public <T extends JobConfig> Job<T> getJob(T jobConfig) throws JobNotFoundException {
		return loadJob(jobConfig.getId());
	}
	
	private void buildJobs() {
		logger.info("Building jobs ...");
		
		for (AbstractJobBuilder<?> jobBuilder : jobBuilders) {
			jobBuilder.injector(injector);
		}
		
		FunctionalIterable<String> existsJobIds = FunctionalIterables.make(jobs).map(jobIdOf());
		FunctionalIterable<String> configedJobIds = FunctionalIterables.make(jobBuilders).map(jobIdOfBuilder());
		addJobs(Sets.difference(configedJobIds.toSet(), existsJobIds.toSet()));
		removeJobs(Sets.difference(existsJobIds.toSet(), configedJobIds.toSet()));
		
		logger.info("Built jobs successfully.");
	}

	private static Function<Job<?>, String> jobIdOf() {
		return new Function<Job<?>, String>() {
			public String apply(Job<?> from) {
				return from.getJobConfig().getId();
			}
		};
	}

	private static Function<AbstractJobBuilder<?>, String> jobIdOfBuilder() {
		return new Function<AbstractJobBuilder<?>, String>() {
			public String apply(AbstractJobBuilder<?> from) {
				return from.getJobConfig().getId();
			}
		};
	}

	private void addJobs(Collection<String> jobIdsToBeAdd) {
		for (String jobId : jobIdsToBeAdd) {
			logger.info("Adding job : " + jobId);
			jobs.add(loadJobBuilder(jobId).build());
		}
	}

	private JobBuilder<?> loadJobBuilder(final String jobId) throws JobNotFoundException {
		JobBuilder<?> detected = FunctionalIterables.make(jobBuilders).detect(predicateOfBuilder(jobId));
		if (detected == null) {
			throw new JobNotFoundException("Job [" + jobId + "] not found");
		}
		return detected;
	}

	private static Predicate<AbstractJobBuilder<?>> predicateOfBuilder(final String jobId) {
		return new Predicate<AbstractJobBuilder<?>>() {
			public boolean apply(AbstractJobBuilder<?> input) {
				return input.getJobConfig().getId().equals(jobId);
			}
		};
	}

	private void removeJobs(Collection<String> jobIdsToBeRemoved) 
		throws JobNotFoundException, JobAlreadyRunningException {
		
		List<Job<?>> toBeRemoved = Lists.newArrayList();
		
		for (String jobId : jobIdsToBeRemoved) {
			Job<?> job = loadJob(jobId);
			if (job.isRunning()) {
				throw new JobAlreadyRunningException("Job [" + jobId + "] already running now, please stop it before remove job config.");
			}
			toBeRemoved.add(job);
		}
		
		jobs.removeAll(toBeRemoved);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends JobConfig> Job<T> loadJob(final String jobId) throws JobNotFoundException {
		Job<?> detected = FunctionalIterables.make(jobs).detect(predicateOf(jobId));
		
		if (detected == null) {
			throw new JobNotFoundException("Job [" + jobId + "] not found.");
		}
		
		return (Job<T>) detected;
	}

	@SuppressWarnings("unchecked")
	private static Predicate<Job> predicateOf(final String jobId) {
		return new Predicate<Job>() {
			public boolean apply(Job input) {
				return input.getJobConfig().getId().equals(jobId);
			}
		};
	}

	public void initialize() {
		logger.info("Initializing console ...");
		
		Preconditions.checkNotNull(taskStorage, "Please set task storage first");
		farAwayTaskCleaner = new FarAwayTaskCleaner(
			taskStorage, 
			projectConfiguration.getHoursToLiveOfTask(),
			projectConfiguration.getPeriodOfTaskCleaning()
		);
		
		injector = Guice.createInjector(new Module() {
			@Override
			public void configure(Binder binder) {
				Multibinder<EventListener> listenerBinder = Multibinder.newSetBinder(binder, EventListener.class);
				binder.bind(WriteableTaskTracker.class).toInstance(new WriteableTaskTracker(taskStorage));
				listenerBinder.addBinding().to(TaskInvocationListener.class);		
				for (EventListener listener : queueStorageEndpoint.listeners()) {
					listenerBinder.addBinding().toInstance(listener);
				}
				
				binder.bind(EventPublisher.class).to(ConcurrentEventPublisher.class).in(Scopes.SINGLETON);
				binder.bind(ProjectConfiguration.class).toInstance(projectConfiguration);
				binder.bind(QueueStorageEndpoint.class).toInstance(queueStorageEndpoint);
				queueStorageEndpoint.setConsole(ConsoleImpl.this);
			}
		});
		
		buildJobs();		
		queueStorageEndpoint.start();
		farAwayTaskCleaner.start();

		logger.info("Initialized console successfully.");
	}

	@Override
	public Console storeTaskIn(TaskStorage taskStorage) {
		this.taskStorage = taskStorage;
		return this;
	}

	@Override
	public Console configProject(ProjectConfiguration projectConfiguration) {
		this.projectConfiguration = projectConfiguration;
		return this;
	}
	
	@Override
	public ProjectConfiguration projectConfiguration() {
		return this.projectConfiguration;
	}

	@Override
	public Console connectBy(QueueStorageEndpoint queueStorageEndpoint) {
		this.queueStorageEndpoint = queueStorageEndpoint;
		return this;
	}

	@Override
	public boolean isRunning() {
		return lifeCycleWrapper.isRunning();
	}

	@Override
	public void start() {
		lifeCycleWrapper.start(new Runnable() {
			@Override
			public void run() {
				List<Job<?>> jobs = getJobs();
				for (Job<?> job : jobs) {
					if (!job.isRunning()) {
						job.start();
					}
				}
			}
		});
	}

	@Override
	public void stop() {
		lifeCycleWrapper.stop(new Runnable() {
			@Override
			public void run() {
				List<Job<?>> jobs = getJobs();
				for (Job<?> job : jobs) {
					if (job.isRunning()) {
						job.stop();
					}
				}
				
				queueStorageEndpoint.stop();
				farAwayTaskCleaner.stop();
			}
		});
	}
	
	@Override
	public boolean executable(TaskInvocation invocation) {
		try {
			return getJob(invocation.getMetaData().getJobConfig()).isRunning();
		} catch (JobNotFoundException e) {
			return false;
		}
	}
	
	@Override
	public Console executeTask(TaskInvocation invocation) throws JobNotFoundException, JobNotRunningException {
		getJob(invocation.getMetaData().getJobConfig()).executeTask(invocation);
		return this;
	}


}
