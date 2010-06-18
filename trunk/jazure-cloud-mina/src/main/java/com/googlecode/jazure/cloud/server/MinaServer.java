package com.googlecode.jazure.cloud.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.core.session.IoEventType;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.googlecode.jazure.sdk.endpoint.mina.codec.TaskInvocationCodecFactory;
import com.googlecode.jazure.sdk.lifecycle.LifeCycleWrapper;
import com.googlecode.jazure.sdk.lifecycle.LifeCycles;
import com.googlecode.jazure.sdk.spi.classloader.AppClassLoaders;

public class MinaServer implements JAzureServer {

	private SocketAcceptor acceptor;
	private final int port;
	private LifeCycleWrapper lifeCycleWrapper = LifeCycles.wrapped();
	
	public MinaServer(int port) {
		this.port = port;
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
				AppClassLoaders.initialize();
				startAcceptor();
			}
		});
	}

	private void startAcceptor() {
		acceptor = new NioSocketAcceptor(new NioProcessor(Executors.newFixedThreadPool(10)));
		acceptor.setDefaultLocalAddress(new InetSocketAddress(port));
		acceptor.setHandler(new MinaServerHandler());
		acceptor.getFilterChain().addLast("Logging", new LoggingFilter());
		
		acceptor.getFilterChain().addLast(
			"codec", 
			new ProtocolCodecFilter(new TaskInvocationCodecFactory(false))
		);
		
		// important configuration, use separate executors to avoid block 
		acceptor.getFilterChain().addLast("ReceiveMessageExecutor", new ExecutorFilter(IoEventType.MESSAGE_RECEIVED));
		acceptor.getFilterChain().addLast("WriteMessageExecutor", new ExecutorFilter(IoEventType.WRITE));
		acceptor.getFilterChain().addLast("SendMessageExecutor", new ExecutorFilter(IoEventType.MESSAGE_SENT));
		try {
			acceptor.bind();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void stop() {
		lifeCycleWrapper.stop(new Runnable() {
			@Override
			public void run() {
				acceptor.dispose();
			}
		});
	}
	
}
