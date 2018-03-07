/*
 * Copyright (c) 2015 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.predix.solsvc.experience.datasource.datagrid.service;

import java.util.ResourceBundle;

/**
 * 
 * @author 212421693
 */
public class ColumnsTags {
	private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private ColumnsTags() {
	}

	
	/**
	 * @param key -
	 * @return -
	 */
	public static String getString(String key) {
		
		return RESOURCE_BUNDLE.getString(key);
		
	}
}
