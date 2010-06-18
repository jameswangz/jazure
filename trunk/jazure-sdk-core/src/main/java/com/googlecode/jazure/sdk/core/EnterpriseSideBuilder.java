package com.googlecode.jazure.sdk.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EnterpriseSideBuilder {

	private static Logger logger = LoggerFactory.getLogger(EnterpriseSideBuilder.class);
	
	private final Module module;

	public EnterpriseSideBuilder(Module module) {
		this.module = module;
	}

	public static EnterpriseSideBuilder module(Module module) {
		return new EnterpriseSideBuilder(module);
	}

	public EnterpriseSide build() {
		EnterpriseSideImpl enterpriseSide = new EnterpriseSideImpl();
		ConsoleImpl console = new ConsoleImpl();
		logger.info("Configuring module ...");
		module.configure(enterpriseSide.setConsole(console));
		logger.info("Configured module.");
		console.initialize();
		return enterpriseSide;
	}

}
