package com.googlecode.jazure.cloud.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jazure.sdk.core.ProjectConfiguration;
import com.googlecode.jazure.sdk.spi.classloader.AppClassLoaders;
import com.googlecode.jazure.sdk.task.Result;
import com.googlecode.jazure.sdk.task.Retrier;
import com.googlecode.jazure.sdk.task.TaskInvocation;

public class MinaServerHandler extends IoHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(MinaServerHandler.class);
	
	private static String MACHAINE_NAME = "Unknown";
	
	static {
		try {
			MACHAINE_NAME = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			logger.error("Unable to get localhost : " + e.getMessage(), e);
		}
	} 
	
	@Override
	public void messageReceived(IoSession session, Object message)
		throws Exception {

		TaskInvocation task = (TaskInvocation) message;
		if (logger.isDebugEnabled()) {
			logger.debug("Received task : " + task);
		}
		
		TaskInvocation result = execute(task);
		logger.debug("Writing message : " + result);
		session.write(result).awaitUninterruptibly();
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.debug("Sending message : " + message);
	}
	

	private TaskInvocation execute(TaskInvocation invocation) {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		
		try {
			ProjectConfiguration pc = invocation.getMetaData().getProjectConfiguration();

			ClassLoader appClassLoader = AppClassLoaders.lookup(pc.getProjectName());
			if (appClassLoader != null) {
				Thread.currentThread().setContextClassLoader(appClassLoader);
			} else {
				logger.warn("Project [" + pc.getProjectName() + "] not found, forgot upload ?");
			}
			
			try {
				Thread.sleep(invocation.getTask().getSchedulePolicy().getPeriod());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			
			Result result = Retrier.times(pc.getRetryTimes())
								   .interval(pc.getRetryInterval())
								   .execute(invocation.getTask());
			
			invocation.setResult(result).getMetaData().setGridWorker("Worker " + MACHAINE_NAME);
			return invocation;
		} finally {
			Thread.currentThread().setContextClassLoader(contextClassLoader);
		}
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

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
		throws Exception {

		logger.error("Caught an exception : " + cause.getMessage(), cause);
	}
	
	

}
