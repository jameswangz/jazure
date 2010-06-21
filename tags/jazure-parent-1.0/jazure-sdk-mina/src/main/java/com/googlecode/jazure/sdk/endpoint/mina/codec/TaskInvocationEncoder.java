package com.googlecode.jazure.sdk.endpoint.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public class TaskInvocationEncoder implements ProtocolEncoder {

	public static final Charset CHARSET = Charset.forName("UTF-8");
	public static final int INT_LENGTH = 4;
	
	private final boolean client;

	private int maxObjectSize = Integer.MAX_VALUE; // 2GB
	
	public TaskInvocationEncoder(boolean client) {
		this.client = client;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		IoBuffer buffer = IoBuffer.allocate(64).setAutoExpand(true);
		int oldPos = buffer.position();
		buffer.skip(INT_LENGTH);
		
		if (client) {
			TaskInvocation invocation = (TaskInvocation) message;
			String projectName = invocation.getMetaData().getProjectConfiguration().getProjectName();
			byte[] bytes = projectName.getBytes(CHARSET);
			int length = bytes.length;
			buffer.putInt(length);
			buffer.put(bytes);
		}
		
      	buffer.putObject(message);

        int objectSize = buffer.position() - INT_LENGTH;
        if (objectSize > maxObjectSize) {
            throw new IllegalArgumentException(
                    "The encoded object is too big: " + objectSize + " (> "
                            + maxObjectSize + ')');
        }

        // Fill the length field
        int newPos = buffer.position();
        buffer.position(oldPos);
        int totalLength = newPos - oldPos - INT_LENGTH;
		buffer.putInt(totalLength);
        buffer.position(newPos);
        
        buffer.flip();
        out.write(buffer);
	}

	@Override
	public void dispose(IoSession session) throws Exception {
	}
	
}
