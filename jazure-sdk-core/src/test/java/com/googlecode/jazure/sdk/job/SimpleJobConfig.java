package com.googlecode.jazure.sdk.job;


public class SimpleJobConfig implements JobConfig {

	private static final long serialVersionUID = 1802302446443059871L;

	private String id;
	
	public SimpleJobConfig(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
