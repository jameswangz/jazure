package com.googlecode.jazure.sdk.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Retrier {

	private static Logger logger = LoggerFactory.getLogger(Retrier.class);
	
	private int times = 0;
	private long interval = 1000;

	private Retrier(int times) {
		this.times = times;
	}

	public static Retrier times(int times) {
		return new Retrier(times);
	}

	public Retrier interval(long interval) {
		this.interval = interval;
		return this;
	}

	public Result execute(Task task) {
		Result result = null;
		Throwable lastError = null;
		
		while (retry()) {
			int remainingTimes = times;
			times--;
			try {
				result = task.execute();
				break;
			} catch (Throwable t) {
				lastError = t;
				logError(remainingTimes, t);
				if (retry()) {
					sleepInbetweenRecoveryAttempts(interval);
				} else {
					break;
				}
			}	
		}
		
		if (result == null) {
			logger.warn("Retry times reached, return failed result");
			result = FailedResult.fromError(lastError);
		}
		
		return result;
	}

	private void logError(int remainingTimes, Throwable t) {
		StringBuilder builder = new StringBuilder("Execute task failed");
		if (retry()) {
			builder.append(", will retry in " + interval + " ms, remaining times [" + remainingTimes + "]");
		}
		logger.error(builder.toString(), t);
	}

	private boolean retry() {
		return times >= 0;
	}

	/**
	 * Sleep according to the specified recovery interval.
	 * Called in between recovery attempts.
	 */
	protected void sleepInbetweenRecoveryAttempts(long interval) {
		if (interval > 0) {
			try {
				Thread.sleep(interval);
			}
			catch (InterruptedException interEx) {
				// Re-interrupt current thread, to allow other threads to react.
				Thread.currentThread().interrupt();
			}
		}
	}
	
}
