package com.googlecode.jazure.sdk.task;

import java.io.Serializable;


public class TaskCorrelation implements Serializable {

	private static final long serialVersionUID = -48923222322134599L;

	private final String correlationId;
	private final int sequenceSize;
	private final int sequenceNumber;

	public TaskCorrelation(String correlationId, int sequenceSize, int sequenceNumber) {
		this.correlationId = correlationId;
		this.sequenceSize = sequenceSize;
		this.sequenceNumber = sequenceNumber;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public int getSequenceSize() {
		return sequenceSize;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	
	

}
