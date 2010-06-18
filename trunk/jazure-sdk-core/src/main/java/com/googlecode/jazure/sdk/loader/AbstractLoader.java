package com.googlecode.jazure.sdk.loader;

import com.googlecode.jazure.sdk.lifecycle.LifeCycleWrapper;
import com.googlecode.jazure.sdk.lifecycle.LifeCycles;

public abstract class AbstractLoader implements Loader {
	
	protected LifeCycleWrapper wrapper = LifeCycles.wrapped();

	protected void doStart() {
	}
	
	protected void doStop() {
	}
	
}
