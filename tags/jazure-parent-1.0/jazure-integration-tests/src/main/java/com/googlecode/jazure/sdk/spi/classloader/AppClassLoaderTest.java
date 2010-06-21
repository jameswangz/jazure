package com.googlecode.jazure.sdk.spi.classloader;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public class AppClassLoaderTest {
	
	@Test
	public void test() throws ClassNotFoundException {
		Class<?> clazz = new AppClassLoader(AppClassLoaders.getApproot() + "fetch-engine3").loadClass("com.googlecode.fetchengine3.task.AvailabilityTask");
		assertNotNull(clazz);
	}
	
}
