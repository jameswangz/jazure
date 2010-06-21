package com.googlecode.jazure.sdk.spi.classloader;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AppClassLoader extends URLClassLoader {
	
	private static Logger logger = LoggerFactory.getLogger(AppClassLoader.class);
	
	public AppClassLoader(String appPath) {
		super(new URL[] {}, Thread.currentThread().getContextClassLoader());
		
		logger.info("Creating AppClassloader for [" + appPath + "]");
		
		Set<URL> jars = listJars(new File(appPath));
		for (URL jar : jars) {
			logger.info("Loading jar : " + jar);
			addURL(jar);
		}
		
		logger.info("Succesful created AppClassloader for [" + appPath + "]");
	}

	private Set<URL> listJars(File folder) {
		Set<URL> urls = new LinkedHashSet<URL>();
		
		File[] files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".jar");
			}
		});	
		
		if (files == null || files.length == 0) {
			logger.warn("No availabile jars in " + folder + ", forgot upload ?");
			return urls;
		}
		
		for (File file : files) {
			try {
				urls.add(file.toURI().toURL());
			} catch (MalformedURLException e) {
				logger.error("Failed to get url " + e.getMessage(), e);
			}
		}
		
		return urls;
	}

}
