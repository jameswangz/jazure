package com.googlecode.jazure.cloud;

import java.util.concurrent.Callable;

import com.googlecode.jazure.cloud.server.MinaServer;
import com.googlecode.jazure.sdk.util.Watch;

public class JAzureServerBootStraper {
	
	public static void main(String[] args) throws Exception {
		Watch.named("JAzureServerBootStraper").task("start server").watch(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				new MinaServer(11111).start();
				return null;
			}
		});
	}
	
	
}
