package com.googlecode.jazure.sdk.endpoint.mina.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


public class TaskInvocationCodecFactory implements ProtocolCodecFactory {
	
	private ProtocolDecoder decoder;
	private ProtocolEncoder encoder;

	public TaskInvocationCodecFactory(boolean client) {
		decoder = new TaskInvocationDecoder(client);
		encoder = new TaskInvocationEncoder(client);
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

}
