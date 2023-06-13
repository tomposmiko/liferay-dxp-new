/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.message.boards.uad.constants;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class MBUADConstants {
	public static final String APPLICATION_NAME = "MB";
	public static final String CLASS_NAME_MB_CATEGORY = "com.liferay.message.boards.model.MBCategory";
	public static final String CLASS_NAME_MB_MESSAGE = "com.liferay.message.boards.model.MBMessage";
	public static final String CLASS_NAME_MB_THREAD = "com.liferay.message.boards.model.MBThread";
	public static final String[] USER_ID_FIELD_NAMES_MB_CATEGORY = {
			"userId", "statusByUserId"
		};
	public static final String[] USER_ID_FIELD_NAMES_MB_MESSAGE = {
			"userId", "statusByUserId"
		};
	public static final String[] USER_ID_FIELD_NAMES_MB_THREAD = {
			"userId", "rootMessageUserId", "lastPostByUserId", "statusByUserId"
		};
}