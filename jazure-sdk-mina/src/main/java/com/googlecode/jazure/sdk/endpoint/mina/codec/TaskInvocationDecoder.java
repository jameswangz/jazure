package com.googlecode.jazure.sdk.endpoint.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jazure.sdk.spi.classloader.AppClassLoaders;

public class TaskInvocationDecoder extends CumulativeProtocolDecoder {

	private static Logger logger = LoggerFactory.getLogger(TaskInvocationDecoder.class);
	
	private int maxObjectSize = 1048576 * 1024; // 10MB

	private final boolean client;
	   
    public TaskInvocationDecoder(boolean client) {
		this.client = client;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        if (!in.prefixedDataAvailable(TaskInvocationEncoder.INT_LENGTH, maxObjectSize)) {
            return false;
        }
        
        //skip total length 
        in.skip(TaskInvocationEncoder.INT_LENGTH);
        
        ClassLoader classLoader = null;
        if (client) {
        		classLoader = Thread.currentThread().getContextClassLoader();
        } else {
	        	int projectNameLength = in.getInt();
	        	byte[] projectNameBytes = new byte[projectNameLength];
	        	in.get(projectNameBytes);
	        	String projectName = new String(projectNameBytes, TaskInvocationEncoder.CHARSET);
	    		classLoader = AppClassLoaders.lookup(projectName);
	    		if (classLoader == null) {
	    			logger.warn("Application [" + projectName + "] not found, forgot upload?");
	    		}
        }
        
        if (classLoader != null) {
	        	int positionBeforeReadObject = in.position();
	        	try {
	        		out.write(in.getObject(classLoader));
	        	} catch (Throwable t) {
	        		logger.error("Unable to deserialize object, will discard it : " + t.getMessage(), t);
	        		in.position(positionBeforeReadObject);
	        		discardObject(in);
	        	}
        } else {
        		discardObject(in);
        }
        
        return true;
    }

	private void discardObject(IoBuffer in) {
	    	int objectSize = in.getInt();
	    	in.skip(objectSize);
	    	logger.warn("Discarded " + objectSize + " bytes request.");
	}


}
