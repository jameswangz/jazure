package com.googlecode.jazure.sdk;

import com.googlecode.jazure.sdk.core.EnterpriseSide;
import com.googlecode.jazure.sdk.core.EnterpriseSideBuilder;
import com.googlecode.jazure.sdk.core.Module;

/**
 * 
 * The main idea is coming from Microsoft Azure Platform, please visit 
 * <a href="http://www.infoq.com/articles/Grid-Azure-David-Pallmann">infoq article</a> for details.
 *
 */
public class JAzure {

	public static EnterpriseSide createEnterprise(Module module) {
		return EnterpriseSideBuilder.module(module).build();
	}

}
