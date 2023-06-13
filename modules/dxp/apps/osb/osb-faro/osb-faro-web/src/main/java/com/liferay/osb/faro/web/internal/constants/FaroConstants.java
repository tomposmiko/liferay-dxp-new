/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.web.internal.constants;

import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.Map;

/**
 * @author Shinn Lok
 */
public class FaroConstants {

	public static final String APPLICATION_ASAH = "asah";

	public static final String APPLICATION_CONTACTS = "contacts";

	public static final String APPLICATION_MAIN = "main";

	public static final int TYPE_ACCOUNT = 0;

	public static final int TYPE_ASSET = 5;

	public static final int TYPE_DATA_SOURCE = 1;

	public static final int TYPE_INDIVIDUAL = 2;

	public static final int TYPE_PAGE = 6;

	public static final int TYPE_SEGMENT_ACCOUNTS = 3;

	public static final int TYPE_SEGMENT_INDIVIDUALS = 4;

	public static Map<String, String> getApplications() {
		return _applications;
	}

	public static int[] getContactsTypes() {
		return new int[] {
			TYPE_ACCOUNT, TYPE_INDIVIDUAL, TYPE_SEGMENT_ACCOUNTS,
			TYPE_SEGMENT_INDIVIDUALS
		};
	}

	public static Map<String, Integer> getTypes() {
		return _types;
	}

	private static final Map<String, String> _applications = HashMapBuilder.put(
		APPLICATION_CONTACTS, APPLICATION_CONTACTS
	).put(
		APPLICATION_MAIN, APPLICATION_MAIN
	).build();
	private static final Map<String, Integer> _types = HashMapBuilder.put(
		"account", TYPE_ACCOUNT
	).put(
		"accountsSegment", TYPE_SEGMENT_ACCOUNTS
	).put(
		"asset", TYPE_ASSET
	).put(
		"dataSource", TYPE_DATA_SOURCE
	).put(
		"individual", TYPE_INDIVIDUAL
	).put(
		"individualsSegment", TYPE_SEGMENT_INDIVIDUALS
	).put(
		"page", TYPE_PAGE
	).build();

}