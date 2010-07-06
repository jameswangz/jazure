package com.googlecode.jazure.sdk.task.storage;

import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.googlecode.jazure.examples.rate.inner.DateSpan;
import com.googlecode.jazure.examples.rate.inner.HiltonJobConfig;
import com.googlecode.jazure.examples.rate.inner.HiltonResult;
import com.googlecode.jazure.examples.rate.inner.RoomRate;
import com.googlecode.jazure.examples.rate.inner.HiltonLoader.HiltonTask;
import com.googlecode.jazure.sdk.core.ProjectConfiguration;
import com.googlecode.jazure.sdk.task.FailedResult;
import com.googlecode.jazure.sdk.task.Result;
import com.googlecode.jazure.sdk.task.Status;
import com.googlecode.jazure.sdk.task.TaskCorrelation;
import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.task.TaskInvocation.TaskMetaData;
import com.googlecode.jazure.sdk.task.TaskInvocation.TimeTrace;
import com.googlecode.jazure.sdk.task.tracker.PaginatedList;
import com.googlecode.jazure.sdk.task.tracker.Paginater;
import com.googlecode.jazure.sdk.task.tracker.TaskCondition;
import com.googlecode.jazure.sdk.task.tracker.criteria.CreatedTimeCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.CreatedTimeCriterias;
import com.googlecode.jazure.sdk.task.tracker.criteria.JobCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.JobCriterias;
import com.googlecode.jazure.sdk.task.tracker.criteria.ParameterCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.ParameterCriterias;
import com.googlecode.jazure.sdk.task.tracker.criteria.ResultCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.ResultCriterias;
import com.googlecode.jazure.sdk.task.tracker.criteria.StatusCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.StatusCriterias;
import com.googlecode.jazure.sdk.task.tracker.criteria.TypeCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.TypeCriterias;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.Driver;


public class Mysql5TaskStorageIntegrationTest {
	
	private static Logger logger = LoggerFactory.getLogger(Mysql5TaskStorageIntegrationTest.class);
	
	private String id;
	
	@Before
	public void before() {
		id = UUID.randomUUID().toString();
		save(id);
	}
	
	public void save(String id) {
		TaskInvocation invocation = new TaskInvocation(
			new TaskMetaData(
				ProjectConfiguration.named("test project"), 
				new HiltonJobConfig(Arrays.asList("passport1"), 0), 
				"task queue",
				"result queue", 
				"test worker"
			),
			id,
			new TaskCorrelation(
				UUID.randomUUID().toString(), 
				10, 
				0
			), 
			new HiltonTask()
				.addParameter(HiltonTask.PASSPORT_KEY, "test passport")
				.addParameter(HiltonTask.START_DATE_KEY, "2009-07-22")
				.addParameter(HiltonTask.END_DATE_KEY, "2009-12-22")
				.addParameter(HiltonTask.PRICE_KEY, new BigDecimal(300)), 
			Status.PENDING, 
			null,
			new TimeTrace(new Date())
		);
		
		getStorage().save(invocation);
	}
	
	@Test
	public void getDDL() {
		logger.debug(getStorage().getDDL());
	}

	@Test
	public void load() {
		TaskInvocation invocation = getStorage().load(id);
		logger.debug(invocation.toString());
	}
	
	@Test
	public void update() {
		Mysql5TaskStorage storage = getStorage();
		TaskInvocation invocation = storage.load(id);
		logger.debug("Before update : " + invocation);
		Result result = new HiltonResult("test passport", new RoomRate("foo room type", new BigDecimal(100), new DateSpan("2009-06-01", "2009-06-03")));
		storage.update(invocation.successful().setResult(result));
		invocation = storage.load(id);
		logger.debug("After update : " + invocation);
		//simulate re-execute
		storage.update(invocation.successful().setResult(result));
	}
	
	@Test
	public void getTasks() {
		get(TaskCondition.NO_CONDITION);
		
		JobCriteria jobCriteria = JobCriterias.idEq(HiltonJobConfig.ID);
		StatusCriteria statusCriteria = StatusCriterias.in(Arrays.asList(Status.values()));
		TypeCriteria typeCriteria = TypeCriterias.eq(HiltonTask.TYPE);
		Date date = new Date();
		
		List<CreatedTimeCriteria> createdTimeCriterias = new ArrayList<CreatedTimeCriteria>();
		createdTimeCriterias.add(CreatedTimeCriterias.ge(DateUtils.addDays(date, -1)));
		createdTimeCriterias.add(CreatedTimeCriterias.le(DateUtils.addDays(date, 1)));
		
		List<ParameterCriteria> parameterCriterias = new ArrayList<ParameterCriteria>();
		parameterCriterias.add(ParameterCriterias.like(HiltonTask.PASSPORT_KEY, "test passport"));
		parameterCriterias.add(ParameterCriterias.gt(HiltonTask.PRICE_KEY, "200"));
		parameterCriterias.add(ParameterCriterias.ge(HiltonTask.START_DATE_KEY, "2009-07-21"));
		parameterCriterias.add(ParameterCriterias.le(HiltonTask.END_DATE_KEY, "2009-12-22"));
		
		List<ResultCriteria> resultCriterias = new ArrayList<ResultCriteria>();
		List<Class<? extends Result>> resultTypes = new ArrayList<Class<? extends Result>>();
		resultTypes.add(FailedResult.class);
		resultTypes.add(HiltonResult.class);
		resultCriterias.add(ResultCriterias.typeIn(resultTypes));
		resultCriterias.add(ResultCriterias.eq(HiltonResult.HAS_ROOM_RATE, Boolean.TRUE.toString()));
		
	
		TaskCondition taskCondition = new TaskCondition(
			Collections.singletonList(jobCriteria),
			Collections.singletonList(statusCriteria), 
			Collections.singletonList(typeCriteria), 
			createdTimeCriterias,
			parameterCriterias,
			resultCriterias,
			Paginater.offerset(0).maxResults(100)
		);
		get(taskCondition);
	}

	@Test
	public void clearBefore() {
		getStorage().clearBefore(DateUtils.addDays(new Date(), -1));
	}
	
	private void get(TaskCondition condition) {
		PaginatedList<TaskInvocation> tasks = getStorage().getTasks(condition);
		
		if (condition.getPaginater() != null) {
			logger.debug("Total count " + tasks.getPaginater().getTotalCount());
		} else {
			logger.debug("Total count " + tasks.size());
		}
		
		for (TaskInvocation invocation : tasks) {
			logger.debug("Next -> " + invocation.toString());
		}
	}
	
	
	public static Mysql5TaskStorage getStorage() {
		DataSource datasource = getDataSource();
		PlatformTransactionManager tm = new DataSourceTransactionManager(datasource);
		return new Mysql5TaskStorage(tm, datasource);
	}

	private static DataSource getDataSource() {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(Driver.class.getName());
		} catch (PropertyVetoException e) {
			throw new RuntimeException(e);
		}
		dataSource.setJdbcUrl("jdbc:mysql://10.200.107.19/jazure?characterEncoding=UTF8");
		dataSource.setUser("root");
		dataSource.setPassword("derbysoft");
		return dataSource;
	}
	
}
