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

package com.liferay.fragment.contributor;

import com.liferay.fragment.constants.FragmentEntryTypeConstants;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
public abstract class BaseFragmentCollectionContributor
	implements FragmentCollectionContributor {

	@Override
	public List<FragmentEntry> getFragmentEntries(int type) {
		return _fragmentEntries.getOrDefault(type, Collections.emptyList());
	}

	@Override
	public String getName() {
		return _name;
	}

	public abstract ServletContext getServletContext();

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundle = bundleContext.getBundle();

		readAndCheckFragmentCollectionStructure();
	}

	protected void readAndCheckFragmentCollectionStructure() {
		try {
			JSONObject jsonObject = _getStructure("collection.json");

			String name = jsonObject.getString("name");
			JSONArray jsonArray = jsonObject.getJSONArray("fragments");

			if (Validator.isNotNull(name) && (jsonArray.length() > 0)) {
				_name = name;

				Iterator<String> iterator = jsonArray.iterator();

				while (iterator.hasNext()) {
					FragmentEntry fragmentEntry = _getFragmentEntry(
						iterator.next());

					List<FragmentEntry> fragmentEntryList =
						_fragmentEntries.getOrDefault(
							fragmentEntry.getType(), new ArrayList<>());

					fragmentEntryList.add(fragmentEntry);

					_fragmentEntries.put(
						fragmentEntry.getType(), fragmentEntryList);
				}
			}
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}
		}
	}

	@Reference
	protected FragmentEntryLocalService fragmentEntryLocalService;

	private String _getFileContent(String path, String fileName)
		throws Exception {

		Class<?> resourceClass = getClass();

		StringBundler sb = new StringBundler(4);

		sb.append("dependencies/");
		sb.append(path);
		sb.append("/");
		sb.append(fileName);

		return StringUtil.read(
			resourceClass.getResourceAsStream(sb.toString()));
	}

	private FragmentEntry _getFragmentEntry(String path) throws Exception {
		JSONObject jsonObject = _getStructure(path + "/fragment.json");

		String name = jsonObject.getString("name");
		String fragmentEntryKey = _getFragmentEntryKey(jsonObject);
		String css = _getFileContent(path, jsonObject.getString("cssPath"));
		String html = _getFileContent(path, jsonObject.getString("htmlPath"));
		String js = _getFileContent(path, jsonObject.getString("jsPath"));
		String thumbnailURL = _getImagePreviewURL(
			jsonObject.getString("thumbnail"));
		int type = FragmentEntryTypeConstants.getTypeFromLabel(
			jsonObject.getString("type"));

		FragmentEntry fragmentEntry =
			fragmentEntryLocalService.createFragmentEntry(0L);

		fragmentEntry.setFragmentEntryKey(fragmentEntryKey);
		fragmentEntry.setName(name);
		fragmentEntry.setCss(css);
		fragmentEntry.setHtml(html);
		fragmentEntry.setJs(js);
		fragmentEntry.setType(type);
		fragmentEntry.setImagePreviewURL(thumbnailURL);

		return fragmentEntry;
	}

	private String _getFragmentEntryKey(JSONObject jsonObject) {
		String fragmentEntryKey = jsonObject.getString("fragmentEntryKey");

		return String.join(
			StringPool.DASH, getFragmentCollectionKey(), fragmentEntryKey);
	}

	private String _getImagePreviewURL(String fileName) {
		URL url = _bundle.getResource(
			"META-INF/resources/thumbnails/" + fileName);

		if (url == null) {
			return StringPool.BLANK;
		}

		ServletContext servletContext = getServletContext();

		return servletContext.getContextPath() + "/thumbnails/" + fileName;
	}

	private JSONObject _getStructure(String path) throws Exception {
		Class<?> resourceClass = getClass();

		String structure = StringUtil.read(
			resourceClass.getResourceAsStream("dependencies/" + path));

		return JSONFactoryUtil.createJSONObject(structure);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseFragmentCollectionContributor.class);

	private Bundle _bundle;
	private final Map<Integer, List<FragmentEntry>> _fragmentEntries =
		new HashMap<>();
	private String _name;

}