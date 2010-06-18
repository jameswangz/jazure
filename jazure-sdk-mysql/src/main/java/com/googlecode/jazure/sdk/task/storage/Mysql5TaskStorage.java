package com.googlecode.jazure.sdk.task.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.googlecode.jazure.sdk.core.ProjectConfiguration;
import com.googlecode.jazure.sdk.job.JobConfig;
import com.googlecode.jazure.sdk.task.Result;
import com.googlecode.jazure.sdk.task.Status;
import com.googlecode.jazure.sdk.task.Task;
import com.googlecode.jazure.sdk.task.TaskCorrelation;
import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.task.TaskInvocation.TaskMetaData;
import com.googlecode.jazure.sdk.task.TaskInvocation.TimeTrace;
import com.googlecode.jazure.sdk.task.storage.TaskStorage;
import com.googlecode.jazure.sdk.task.tracker.PaginatedList;
import com.googlecode.jazure.sdk.task.tracker.PaginatedLists;
import com.googlecode.jazure.sdk.task.tracker.Paginater;
import com.googlecode.jazure.sdk.task.tracker.TaskCondition;
import com.googlecode.jazure.sdk.task.tracker.TaskRetrievalFailureException;
import com.googlecode.jazure.sdk.task.tracker.criteria.BasicCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.ParameterCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.ResultCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.ResultCriterias;

public class Mysql5TaskStorage implements TaskStorage {

	private PlatformTransactionManager tm;
	private JdbcTemplate jdbcTemplate;
	
	public Mysql5TaskStorage(PlatformTransactionManager tm, DataSource datasource) {
		this.tm = tm;
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public void save(final TaskInvocation invocation) {
		new TransactionTemplate(tm).execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				Task task = invocation.getTask();
				
				String taskSql = "insert into jazure_task (id, correlation_id, sequence_size, sequence_number, project_configuration, job_config, job_id, task, type, status, grid_worker, result, task_queue, result_queue, created_time) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				Object[] taskArgs = new Object[] {
					invocation.getId(), 
					invocation.getCorrelation().getCorrelationId(),
					invocation.getCorrelation().getSequenceSize(),
					invocation.getCorrelation().getSequenceNumber(),
					invocation.getMetaData().getProjectConfiguration(),
					invocation.getMetaData().getJobConfig(),
					invocation.getMetaData().getJobConfig().getId(),
					task, 
					task.getType(), 
					invocation.getStatus().name(), 
					invocation.getMetaData().getGridWorker(), 
					invocation.getResult(),
					invocation.getMetaData().getTaskQueue(),
					invocation.getMetaData().getResultQueue(),
					invocation.getTimeTrace().getCreatedTime()
				};
				
				jdbcTemplate.update(taskSql, taskArgs);
				insertParams(invocation, task);
				return null;
			}

			private void insertParams(final TaskInvocation invocation, Task task) {
				writeChildTable(invocation.getId(), task.keyValues(), "jazure_task_param", "param_key", "param_value");
			}
		});
	}

	private void writeChildTable(final String id, Map<String, String> keyValues, String tableName, String keyColumn, String valueColumn) {
		for (Map.Entry<String, String> keyValue : keyValues.entrySet()) {
			String paramSql = String.format("insert into %s (task_id, %s, %s) values (?, ?, ?)", tableName, keyColumn, valueColumn);
			Object[] paramArgs = new Object[] { id, keyValue.getKey(), keyValue.getValue() };
			jdbcTemplate.update(paramSql, paramArgs);
		}
	}
	
	@Override
	public void update(final TaskInvocation invocation) {
		new TransactionTemplate(tm).execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				Result result = invocation.getResult();
				String sql = "update jazure_task set status = ?, grid_worker = ?, result = ?, last_modified_time = ? where id = ?";
				Object[] args = new Object[] {
					invocation.getStatus().name(), 
					invocation.getMetaData().getGridWorker(), 
					result,
					invocation.getTimeTrace().getLastModifiedTime(),
					invocation.getId()
				};
				jdbcTemplate.update(sql, args);
				
				clearResults(invocation);
				insertResults(invocation, result);
				return null;
			}

			/**
			 * Need clear before insert because this task may be re-executing,
			 * result history feature is not supported current time.
			 */
			private void clearResults(final TaskInvocation invocation) {
				jdbcTemplate.update("delete from jazure_task_result where task_id = ? ", new Object[] {invocation.getId()});
			}
			
			private void insertResults(final TaskInvocation invocation, Result result) {
				if (result == null) {
					return;
				}
				Map<String, String> keyValues = result.keyValues();
				keyValues.put(ResultCriterias.RESERVED_RESULT_TYPE_KEY, result.getClass().getName());
				writeChildTable(invocation.getId(), keyValues, "jazure_task_result", "result_key", "result_value");
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaginatedList<TaskInvocation> getTasks(final TaskCondition condition) {
		SqlAndArgs saas = new SqlAndArgs().appendSql(" select t.* from jazure_task as t ");
		SqlAndArgs counter = new SqlAndArgs().appendSql(" select count(*) from jazure_task as t ");

		String alias;
		String previousAlias = null;
		boolean joinPrevious = false;
		
		int paramIndex = 0;
		
		if (condition.hasParameterCriterias()) {
			List<ParameterCriteria> parameterCriterias = condition.getParameterCriterias();
			for (ParameterCriteria parameterCriteria : parameterCriterias) {
				alias = "tp" + paramIndex;
				saas.appendSqlAndArgs(parameterCriteria.getSql(alias, previousAlias, joinPrevious), parameterCriteria.getArgs());
				counter.appendSqlAndArgs(parameterCriteria.getSql(alias, previousAlias, joinPrevious), parameterCriteria.getArgs());
				previousAlias = alias;
				joinPrevious = true;
				paramIndex++;
			}
		}
		
		int resultIndex = 0;
		
		if (condition.hasResultCriterias()) {
			List<ResultCriteria> resultCriterias = condition.getResultCriterias();
			for (ResultCriteria resultCriteria : resultCriterias) {
				alias = "tr" + resultIndex;
				saas.appendSqlAndArgs(resultCriteria.getSql(alias, previousAlias, joinPrevious), resultCriteria.getArgs());
				counter.appendSqlAndArgs(resultCriteria.getSql(alias, previousAlias, joinPrevious), resultCriteria.getArgs());
				previousAlias = alias;
				resultIndex++;
			}
		}
		
		saas.appendSql(" where 1 = 1 ");
		counter.appendSql(" where 1 = 1 ");
		
		List<BasicCriteria> basicCriterias = condition.getNotNullBasicCriterias();
		
		for (BasicCriteria basicCriteria : basicCriterias) {
			saas.appendAnd().appendSqlAndArgs(basicCriteria.getSql(), basicCriteria.getArgs());
			counter.appendAnd().appendSqlAndArgs(basicCriteria.getSql(), basicCriteria.getArgs());
		}
		
		saas.appendSql(" order by t.created_time desc ");
		
		if (condition.paginaterSensitive()) {
			Paginater paginater = condition.getPaginater();
			saas.appendSqlAndArgs(" limit ?, ? ", new Object[] { paginater.getOfferset(), paginater.getMaxResults() });
		}
		
		List<TaskInvocation> results = jdbcTemplate.query(saas.getSql(), saas.getArgs(), new TaskInvocationMapper());
		
		if (condition.paginaterSensitive()) {
			long totalCount = jdbcTemplate.queryForLong(counter.getSql(), counter.getArgs());
			condition.getPaginater().setTotalCount(totalCount);
		}
		
		return PaginatedLists.proxy(results, condition.getPaginater());
	}

	public String getDDL() {
		InputStream in = getClass().getResourceAsStream("jazure-mysql5-ddl.sql");
		if (in == null) {
			throw new RuntimeException("Couldn't find jazure-mysql5-ddl.sql");
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder ddl = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				ddl.append(line);
			}
			in.close();
			return ddl.toString();
		} catch (IOException e) {
			throw new RuntimeException("Failed to read ddl", e);
		}
	}

	@Override
	public TaskInvocation load(final String id) throws TaskRetrievalFailureException {
		String sql = "select * from jazure_task where id = ? ";
		Object[] args = new Object[] { id };
		try {
			return (TaskInvocation) jdbcTemplate.queryForObject(sql, args, new TaskInvocationMapper());
		} catch (DataAccessException e) {
			throw new TaskRetrievalFailureException("Failed to retrieve task [" + id + "]", e);
		}
	}
	
	@Override
	public int clearBefore(final Date timePoint) {
		return (Integer) new TransactionTemplate(tm).execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				SqlAndArgs saas = new SqlAndArgs()
					.appendSqlAndArgs("delete from jazure_task where created_time < ?", new Object[] {timePoint});
				return jdbcTemplate.update(saas.getSql(), saas.getArgs());
			}
		});
	}
	
	private static final class TaskInvocationMapper implements RowMapper {
		
		private static Logger logger = LoggerFactory.getLogger(TaskInvocationMapper.class);
		
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new TaskInvocation(
				new TaskMetaData(
					deserialize(rs, "project_configuration", ProjectConfiguration.class), 
					deserialize(rs, "job_config", JobConfig.class),
					rs.getString("task_queue"), 
					rs.getString("result_queue"), 
					rs.getString("grid_worker")
				), 
				rs.getString("id"), 
				new TaskCorrelation(
					rs.getString("correlation_id"), 
					rs.getInt("sequence_size"), 
					rs.getInt("sequence_number")
				),
				deserialize(rs, "task", Task.class),
				Status.valueOf(rs.getString("status")), 
				deserialize(rs, "result", Result.class), 
				new TimeTrace(rs.getTimestamp("created_time")).setLastModifiedTime(rs.getTimestamp("last_modified_time"))
			);
		}

		@SuppressWarnings("unchecked")
		private <T> T deserialize(ResultSet rs, String column, Class<T> clazz) {
			try {
				InputStream binaryStream = rs.getBinaryStream(column);
				if (binaryStream == null) {
					return null;
				}
				return (T) new ObjectInputStream(binaryStream).readObject();
			} catch (Exception e) {
				logger.error("Failed to deserialize object : " + e.getMessage(), e);
				return null;
			}
		}
	}




}
