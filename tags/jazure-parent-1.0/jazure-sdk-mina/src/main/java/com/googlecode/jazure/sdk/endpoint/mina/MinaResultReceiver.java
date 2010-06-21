package com.googlecode.jazure.sdk.endpoint.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jazure.sdk.task.TaskInvocation;
import com.googlecode.jazure.sdk.util.CategoryBufferedMap;

public class MinaResultReceiver extends IoHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(MinaResultReceiver.class);
	
	private final MinaQueueStorageEndpoint endpoint;

	private CategoryBufferedMap<String, TaskInvocation> resultBuffer = new CategoryBufferedMap<String, TaskInvocation>();
	
	public MinaResultReceiver(MinaQueueStorageEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public void messageReceived(IoSession session, Object message)
		throws Exception {
		
		TaskInvocation result = (TaskInvocation) message;
		resultBuffer.put(result.getMetaData().getResultQueue(), result);
	}

	public TaskInvocation receive(String resultQueue) {
		return resultBuffer.remove(resultQueue);
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
    		reconnectIfNecessary();
	}

	private void reconnectIfNecessary() {
		if (!endpoint.isRunning()) {
	    		logger.warn("Endpoint is not running, do nothing");
	    		return;
	    	}
    	
	    	logger.warn("Session closed, try reconnect to server");
	    	endpoint.reconnect();
	}

	public void clear() {
		resultBuffer.clear();
	}
	

}
