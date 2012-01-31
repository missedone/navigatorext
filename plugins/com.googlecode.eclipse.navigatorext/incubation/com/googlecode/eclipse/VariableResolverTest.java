/**
 * Copyright 2004 - 2007 Blue Bamboo International Inc. 
 *           All rights reserved.
 *
 *
 * Blue Bamboo PROPRIETARY/CONFIDENTIAL.
 *
 */

package com.googlecode.eclipse;

import static org.junit.Assert.*;

import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.junit.Test;

public class VariableResolverTest {

	@Test
	public void resolve() throws Exception {
		IStringVariableManager stringVariableManager = VariablesPlugin.getDefault().getStringVariableManager();
		String var = "${datetime}_test";
		String val = stringVariableManager.performStringSubstitution(var);
		System.out.println(val);
		assertNotSame(var, val);
		var = "${datetime:MMdd-HH-mm-ss}_test";
		val = stringVariableManager.performStringSubstitution(var);
		System.out.println(val);
		assertNotSame(var, val);
	}
}
