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

package com.liferay.structure.apio.client.test.activator;

import com.liferay.dynamic.data.mapping.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestHelper;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.apio.test.util.AuthConfigurationTestUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * @author Ruben Pulido
 */
public class ContentStructureApioTestBundleActivator
	implements BundleActivator {

	public static final String SITE_NAME =
		ContentStructureApioTestBundleActivator.class.getSimpleName() + "Site";

	@Override
	public void start(BundleContext bundleContext) {
		_autoCloseables = new ArrayList<>();

		_bundleContext = bundleContext;

		_serviceReference = bundleContext.getServiceReference(
			DDMFormJSONDeserializer.class);

		_ddmFormJSONDeserializer = bundleContext.getService(_serviceReference);

		try {
			AuthConfigurationTestUtil.deployOAuthConfiguration(_bundleContext);

			_prepareTest();
		}
		catch (Exception e) {
			_cleanUp();

			throw new RuntimeException(e);
		}
	}

	@Override
	public void stop(BundleContext bundleContext) {
		_cleanUp();

		_bundleContext.ungetService(_serviceReference);
	}

	protected DDMForm deserialize(String content) {
		try {
			return _ddmFormJSONDeserializer.deserialize(content);
		}
		catch (PortalException pe) {
			throw new RuntimeException(pe);
		}
	}

	private DDMStructure _addDDMStructure(Group group, String fileName)
		throws Exception {

		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(
					"com.liferay.journal.model.JournalArticle"),
				group);

		return ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(
				"com.liferay.journal.model.JournalArticle"),
			null, ContentStructureApioTestBundleActivator.SITE_NAME,
			deserialize(_read(fileName)), StorageType.JSON.getValue(),
			DDMStructureConstants.TYPE_DEFAULT);
	}

	private void _cleanUp() {
		Collections.reverse(_autoCloseables);

		for (AutoCloseable autoCloseable : _autoCloseables) {
			try {
				autoCloseable.close();
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}
	}

	private void _prepareTest() throws Exception {
		User user = UserTestUtil.getAdminUser(TestPropsValues.getCompanyId());
		Map<Locale, String> nameMap = Collections.singletonMap(
			LocaleUtil.getDefault(), SITE_NAME);

		Group group = GroupLocalServiceUtil.addGroup(
			user.getUserId(), GroupConstants.DEFAULT_PARENT_GROUP_ID, null, 0,
			GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap, nameMap,
			GroupConstants.TYPE_SITE_OPEN, true,
			GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION,
			StringPool.SLASH + FriendlyURLNormalizerUtil.normalize(SITE_NAME),
			true, true, ServiceContextTestUtil.getServiceContext());

		_autoCloseables.add(() -> GroupLocalServiceUtil.deleteGroup(group));

		DDMStructure ddmStructure = _addDDMStructure(
			group, "test-structure.json");

		_autoCloseables.add(
			() -> DDMStructureLocalServiceUtil.deleteStructure(ddmStructure));
	}

	private String _read(String fileName) throws Exception {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(
			"/com/liferay/structure/apio/client/test/activator/" + fileName);

		return StringUtil.read(inputStream);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ContentStructureApioTestBundleActivator.class);

	private List<AutoCloseable> _autoCloseables;
	private BundleContext _bundleContext;
	private DDMFormJSONDeserializer _ddmFormJSONDeserializer;
	private ServiceReference<DDMFormJSONDeserializer> _serviceReference;

}