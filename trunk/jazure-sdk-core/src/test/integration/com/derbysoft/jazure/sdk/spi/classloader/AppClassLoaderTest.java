package com.derbysoft.jazure.sdk.spi.classloader;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.derbysoft.jazure.sdk.spi.classloader.AppClassLoader;
import com.derbysoft.jazure.sdk.spi.classloader.AppClassLoaders;


public class AppClassLoaderTest {
	
	@Test
	public void test() throws ClassNotFoundException {
		Class<?> clazz = new AppClassLoader(AppClassLoaders.getApproot() + "fetch-engine3").loadClass("com.derbysoft.fetchengine3.task.AvailabilityTask");
		assertNotNull(clazz);
	}
	
}
