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

package com.liferay.portal.kernel.search;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.util.ServiceProxyFactory;

import java.util.Set;

/**
 * @author Michael C. Han
 */
public class SearchEngineHelperUtil {

	public static String[] getEntryClassNames() {
		return _searchEngineHelper.getEntryClassNames();
	}

	public static SearchEngine getSearchEngine(String searchEngineId) {
		return _searchEngineHelper.getSearchEngine(searchEngineId);
	}

	public static SearchEngineHelper getSearchEngineHelper() {
		return _searchEngineHelper;
	}

	public static Set<String> getSearchEngineIds() {
		return _searchEngineHelper.getSearchEngineIds();
	}

	public static String getSearchReaderDestinationName(String searchEngineId) {
		return StringBundler.concat(
			DestinationNames.SEARCH_READER, StringPool.SLASH, searchEngineId);
	}

	public static String getSearchWriterDestinationName(String searchEngineId) {
		return StringBundler.concat(
			DestinationNames.SEARCH_WRITER, StringPool.SLASH, searchEngineId);
	}

	public static void initialize(long companyId) {
		_searchEngineHelper.initialize(companyId);
	}

	public static void removeCompany(long companyId) {
		_searchEngineHelper.removeCompany(companyId);
	}

	public static SearchEngine removeSearchEngine(String searchEngineId) {
		return _searchEngineHelper.removeSearchEngine(searchEngineId);
	}

	public static void setSearchEngine(
		String searchEngineId, SearchEngine searchEngine) {

		_searchEngineHelper.setSearchEngine(searchEngineId, searchEngine);

		searchEngine.initialize(CompanyConstants.SYSTEM);
	}

	private static volatile SearchEngineHelper _searchEngineHelper =
		ServiceProxyFactory.newServiceTrackedInstance(
			SearchEngineHelper.class, SearchEngineHelperUtil.class,
			"_searchEngineHelper", false);

}