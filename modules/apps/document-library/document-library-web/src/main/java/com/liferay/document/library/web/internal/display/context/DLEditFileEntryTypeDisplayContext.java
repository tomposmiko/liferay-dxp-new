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

package com.liferay.document.library.web.internal.display.context;

import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.web.internal.configuration.FFDocumentLibraryDDMEditorConfigurationUtil;
import com.liferay.dynamic.data.mapping.constants.DDMConstants;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStorageLinkLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.bean.BeanParamUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.PortletURL;

/**
 * @author Cristina González
 */
public class DLEditFileEntryTypeDisplayContext {

	public DLEditFileEntryTypeDisplayContext(
		DDM ddm, DDMStorageLinkLocalService ddmStorageLinkLocalService,
		DDMStructureLocalService ddmStructureLocalService, Language language,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		_ddm = ddm;
		_ddmStorageLinkLocalService = ddmStorageLinkLocalService;
		_ddmStructureLocalService = ddmStructureLocalService;
		_language = language;
		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;
	}

	public List<Map<String, Object>> getAdditionalPanels(
		String npmResolvedPackageName) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)_liferayPortletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		List<Map<String, Object>> additionalPanels = new ArrayList<>();

		additionalPanels.add(
			HashMapBuilder.<String, Object>put(
				"icon", "cog"
			).put(
				"label",
				LanguageUtil.get(themeDisplay.getLocale(), "properties")
			).put(
				"pluginEntryPoint",
				npmResolvedPackageName +
					"/document_library/js/ddm/panels/index.es"
			).put(
				"sidebarPanelId", "properties"
			).put(
				"url",
				() -> {
					PortletURL editBasicInfoURL =
						_liferayPortletResponse.createRenderURL();

					editBasicInfoURL.setParameter(
						"mvcPath", "/document_library/ddm/details.jsp");
					editBasicInfoURL.setWindowState(
						LiferayWindowState.EXCLUSIVE);

					return editBasicInfoURL.toString();
				}
			).build());

		additionalPanels.add(
			HashMapBuilder.<String, Object>put(
				"icon", "document"
			).put(
				"label",
				LanguageUtil.get(
					themeDisplay.getLocale(), "additional-metadata-fields")
			).put(
				"pluginEntryPoint",
				npmResolvedPackageName +
					"/document_library/js/ddm/panels/index.es"
			).put(
				"sidebarPanelId", "additionalMetadataFields"
			).put(
				"url",
				() -> {
					PortletURL editAdditionalMetadataFieldsURL =
						_liferayPortletResponse.createRenderURL();

					editAdditionalMetadataFieldsURL.setParameter(
						"mvcPath",
						"/document_library/ddm/additional_metadata_fields.jsp");
					editAdditionalMetadataFieldsURL.setWindowState(
						LiferayWindowState.EXCLUSIVE);

					return editAdditionalMetadataFieldsURL.toString();
				}
			).build());

		DLFileEntryType fileEntryType =
			(DLFileEntryType)_liferayPortletRequest.getAttribute(
				WebKeys.DOCUMENT_LIBRARY_FILE_ENTRY_TYPE);

		if (fileEntryType == null) {
			additionalPanels.add(
				HashMapBuilder.<String, Object>put(
					"icon", "lock"
				).put(
					"label",
					LanguageUtil.get(themeDisplay.getLocale(), "permissions")
				).put(
					"pluginEntryPoint",
					npmResolvedPackageName +
						"/document_library/js/ddm/panels/index.es"
				).put(
					"sidebarPanelId", "permissions"
				).put(
					"url",
					() -> {
						PortletURL editPermissionsURL =
							_liferayPortletResponse.createRenderURL();

						editPermissionsURL.setParameter(
							"mvcPath", "/document_library/ddm/permissions.jsp");
						editPermissionsURL.setWindowState(
							LiferayWindowState.EXCLUSIVE);

						return editPermissionsURL.toString();
					}
				).build());
		}

		return additionalPanels;
	}

	public String getAvailableFields() {
		return DDMConstants.AVAILABLE_FIELDS;
	}

	public Locale[] getAvailableLocales() throws PortalException {
		if (_availableLocales != null) {
			return _availableLocales;
		}

		_availableLocales = Optional.ofNullable(
			_getDDMForm()
		).map(
			DDMForm::getAvailableLocales
		).map(
			availableLocales -> availableLocales.toArray(new Locale[0])
		).orElseGet(
			() -> new Locale[] {LocaleUtil.getSiteDefault()}
		);

		return _availableLocales;
	}

	public String getAvailableLocalesString() throws PortalException {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray(
			Stream.of(
				getAvailableLocales()
			).map(
				_language::getLanguageId
			).collect(
				Collectors.toList()
			));

		return jsonArray.toString();
	}

	public String getDefaultLanguageId() throws PortalException {
		return Optional.ofNullable(
			_getDDMForm()
		).map(
			ddmForm -> LocaleUtil.toLanguageId(ddmForm.getDefaultLocale())
		).orElseGet(
			() -> LocaleUtil.toLanguageId(LocaleUtil.getSiteDefault())
		);
	}

	public String getFieldsJSONArrayString() {
		if (_fieldsJSONArrayString != null) {
			return _fieldsJSONArrayString;
		}

		DDMStructure ddmStructure = _getDDMStructure();

		long ddmStructureId = BeanParamUtil.getLong(
			ddmStructure, _liferayPortletRequest, "structureId");

		String definition = BeanParamUtil.getString(
			ddmStructure, _liferayPortletRequest, "definition");

		_fieldsJSONArrayString = Optional.ofNullable(
			_ddm.getDDMFormFieldsJSONArray(
				_ddmStructureLocalService.fetchDDMStructure(ddmStructureId),
				definition)
		).map(
			JSONArray::toString
		).orElse(
			StringPool.BLANK
		);

		return _fieldsJSONArrayString;
	}

	public String getLocalesMapString() {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_liferayPortletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Locale locale = LocaleUtil.fromLanguageId(
			_language.getLanguageId(_liferayPortletRequest));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		for (Locale availableLocale :
				_language.getAvailableLocales(themeDisplay.getSiteGroupId())) {

			jsonObject.put(
				LocaleUtil.toLanguageId(availableLocale),
				availableLocale.getDisplayName(locale));
		}

		return jsonObject.toString();
	}

	public boolean isChangeableDefaultLanguage() throws PortalException {
		return Optional.ofNullable(
			_getDDMForm()
		).map(
			ddmForm -> !Objects.equals(
				LocaleUtil.getSiteDefault(), ddmForm.getDefaultLocale())
		).orElse(
			false
		);
	}

	public boolean isFieldNameEditionDisabled() {
		DDMStructure ddmStructure = _getDDMStructure();

		if (ddmStructure == null) {
			return false;
		}

		int count = _ddmStorageLinkLocalService.getStructureStorageLinksCount(
			ddmStructure.getStructureId());

		if (count > 0) {
			return true;
		}

		return false;
	}

	public boolean useDataEngineEditor() {
		return FFDocumentLibraryDDMEditorConfigurationUtil.
			useDataEngineEditor();
	}

	private DDMForm _getDDMForm() throws PortalException {
		if (_ddmForm != null) {
			return _ddmForm;
		}

		String definition = BeanParamUtil.getString(
			_getDDMStructure(), _liferayPortletRequest, "definition");

		if (Validator.isNotNull(definition)) {
			_ddmForm = _ddm.getDDMForm(definition);
		}

		return _ddmForm;
	}

	private DDMStructure _getDDMStructure() {
		return (DDMStructure)_liferayPortletRequest.getAttribute(
			WebKeys.DOCUMENT_LIBRARY_DYNAMIC_DATA_MAPPING_STRUCTURE);
	}

	private Locale[] _availableLocales;
	private final DDM _ddm;
	private DDMForm _ddmForm;
	private final DDMStorageLinkLocalService _ddmStorageLinkLocalService;
	private final DDMStructureLocalService _ddmStructureLocalService;
	private String _fieldsJSONArrayString;
	private final Language _language;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;

}