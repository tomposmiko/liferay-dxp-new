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

package com.liferay.dynamic.data.mapping.test.util;

import com.liferay.dynamic.data.mapping.constants.DDMTemplateConstants;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Locale;
import java.util.Map;

/**
 * @author Eudaldo Alonso
 */
public class DDMTemplateTestUtil {

	public static DDMTemplate addTemplate(
			long structureId, long resourceClassNameId)
		throws Exception {

		return addTemplate(
			structureId, resourceClassNameId, TemplateConstants.LANG_TYPE_VM,
			getSampleTemplateVM(), LocaleUtil.getSiteDefault());
	}

	public static DDMTemplate addTemplate(
			long structureId, long resourceClassNameId, Locale defaultLocale)
		throws Exception {

		return addTemplate(
			structureId, resourceClassNameId, TemplateConstants.LANG_TYPE_VM,
			getSampleTemplateVM(), defaultLocale);
	}

	public static DDMTemplate addTemplate(
			long groupId, long structureId, long resourceClassNameId)
		throws Exception {

		return addTemplate(
			groupId, structureId, resourceClassNameId,
			TemplateConstants.LANG_TYPE_VM, getSampleTemplateVM(),
			LocaleUtil.getSiteDefault());
	}

	public static DDMTemplate addTemplate(
			long groupId, long structureId, long resourceClassNameId,
			Locale defaultLocale)
		throws Exception {

		return addTemplate(
			groupId, structureId, resourceClassNameId,
			TemplateConstants.LANG_TYPE_VM, getSampleTemplateVM(),
			defaultLocale);
	}

	public static DDMTemplate addTemplate(
			long groupId, long classNameId, long classPK,
			long resourceClassNameId)
		throws Exception {

		return addTemplate(
			groupId, classNameId, classPK, resourceClassNameId,
			TemplateConstants.LANG_TYPE_VM, getSampleTemplateVM(),
			LocaleUtil.getSiteDefault());
	}

	public static DDMTemplate addTemplate(
			long groupId, long classNameId, long classPK,
			long resourceClassNameId, String language, String script,
			Locale defaultLocale)
		throws Exception {

		return addTemplate(
			groupId, classNameId, classPK, resourceClassNameId, null, language,
			script, defaultLocale);
	}

	public static DDMTemplate addTemplate(
			long groupId, long classNameId, long classPK,
			long resourceClassNameId, String templateKey, String language,
			String script, Locale defaultLocale)
		throws Exception {

		Map<Locale, String> nameMap = HashMapBuilder.put(
			defaultLocale, "Test Template"
		).build();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		return DDMTemplateLocalServiceUtil.addTemplate(
			TestPropsValues.getUserId(), groupId, classNameId, classPK,
			resourceClassNameId, templateKey, nameMap, null,
			DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, null, language, script,
			false, false, null, null, serviceContext);
	}

	public static DDMTemplate addTemplate(
			long groupId, long structureId, long resourceClassNameId,
			String language, String script, Locale defaultLocale)
		throws Exception {

		return addTemplate(
			groupId, PortalUtil.getClassNameId(DDMStructure.class), structureId,
			resourceClassNameId, language, script, defaultLocale);
	}

	public static DDMTemplate addTemplate(
			long structureId, long resourceClassNameId, String language,
			String script)
		throws Exception {

		return addTemplate(
			TestPropsValues.getGroupId(), structureId, resourceClassNameId,
			language, script, LocaleUtil.getSiteDefault());
	}

	public static DDMTemplate addTemplate(
			long structureId, long resourceClassNameId, String language,
			String script, Locale defaultLocale)
		throws Exception {

		return addTemplate(
			TestPropsValues.getGroupId(), structureId, resourceClassNameId,
			language, script, defaultLocale);
	}

	public static String getSampleTemplateVM() {
		return "$name.getData()";
	}

}