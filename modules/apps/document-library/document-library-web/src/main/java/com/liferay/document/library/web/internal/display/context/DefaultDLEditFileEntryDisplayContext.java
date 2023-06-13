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

import com.liferay.document.library.display.context.DLEditFileEntryDisplayContext;
import com.liferay.document.library.display.context.DLFilePicker;
import com.liferay.document.library.kernel.model.DLFileEntryMetadata;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.kernel.model.DLFileEntryTypeConstants;
import com.liferay.document.library.kernel.service.DLFileEntryMetadataLocalServiceUtil;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.document.library.kernel.util.DLValidator;
import com.liferay.document.library.web.internal.display.context.helper.DLRequestHelper;
import com.liferay.document.library.web.internal.display.context.helper.FileEntryDisplayContextHelper;
import com.liferay.document.library.web.internal.display.context.helper.FileVersionDisplayContextHelper;
import com.liferay.document.library.web.internal.settings.DLPortletInstanceSettings;
import com.liferay.dynamic.data.mapping.exception.StorageException;
import com.liferay.dynamic.data.mapping.form.renderer.constants.DDMFormRendererConstants;
import com.liferay.dynamic.data.mapping.form.values.factory.DDMFormValuesFactory;
import com.liferay.dynamic.data.mapping.kernel.DDMForm;
import com.liferay.dynamic.data.mapping.kernel.DDMFormField;
import com.liferay.dynamic.data.mapping.kernel.DDMStructure;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.StorageEngine;
import com.liferay.dynamic.data.mapping.util.DDMBeanTranslator;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.bean.BeanParamUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.constants.FriendlyURLResolverConstants;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.configuration.UploadServletRequestConfigurationProviderUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.RepositoryUtil;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Iván Zaera
 */
public class DefaultDLEditFileEntryDisplayContext
	implements DLEditFileEntryDisplayContext {

	public DefaultDLEditFileEntryDisplayContext(
		DDMBeanTranslator ddmBeanTranslator,
		DDMFormValuesFactory ddmFormValuesFactory,
		DLFileEntryType dlFileEntryType, DLValidator dlValidator,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, StorageEngine storageEngine) {

		this(
			httpServletRequest, dlFileEntryType, dlValidator, null,
			storageEngine, ddmBeanTranslator, ddmFormValuesFactory);
	}

	public DefaultDLEditFileEntryDisplayContext(
		DDMBeanTranslator ddmBeanTranslator,
		DDMFormValuesFactory ddmFormValuesFactory, DLValidator dlValidator,
		FileEntry fileEntry, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, StorageEngine storageEngine) {

		this(
			httpServletRequest, (DLFileEntryType)null, dlValidator, fileEntry,
			storageEngine, ddmBeanTranslator, ddmFormValuesFactory);
	}

	@Override
	public Map<String, Long> getAllMimeTypeSizeLimit() {
		return _dlValidator.getMimeTypeSizeLimit(
			_dlRequestHelper.getSiteGroupId());
	}

	@Override
	public DDMFormValues getDDMFormValues(
			DDMStructure ddmStructure, long fileVersionId)
		throws PortalException {

		if (_isDDMFormValuesEdited(ddmStructure)) {
			HttpServletRequest httpServletRequest =
				_getDDMStructureHttpServletRequest(
					_httpServletRequest, ddmStructure.getStructureId());

			return _ddmFormValuesFactory.create(
				httpServletRequest,
				_ddmBeanTranslator.translate(ddmStructure.getDDMForm()));
		}

		DLFileEntryMetadata fileEntryMetadata =
			DLFileEntryMetadataLocalServiceUtil.fetchFileEntryMetadata(
				ddmStructure.getStructureId(), fileVersionId);

		if (fileEntryMetadata == null) {
			return null;
		}

		return getDDMFormValues(fileEntryMetadata.getDDMStorageId());
	}

	@Override
	public DDMFormValues getDDMFormValues(long classPK)
		throws StorageException {

		return _storageEngine.getDDMFormValues(classPK);
	}

	@Override
	public String getDLFileEntryTypeLanguageId(
		DDMStructure ddmStructure, Locale locale) {

		String languageId = LocaleUtil.toLanguageId(locale);

		if (!ArrayUtil.contains(
				ddmStructure.getAvailableLanguageIds(), languageId)) {

			return ddmStructure.getDefaultLanguageId();
		}

		return languageId;
	}

	@Override
	public DLFilePicker getDLFilePicker(String onFilePickCallback) {
		return null;
	}

	@Override
	public String getFriendlyURLBase() {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		StringBundler sb = new StringBundler(4);

		sb.append("/documents");
		sb.append(FriendlyURLResolverConstants.URL_SEPARATOR_X_FILE_ENTRY);

		Group group = themeDisplay.getScopeGroup();

		sb.append(group.getFriendlyURL());

		sb.append(StringPool.SLASH);

		return sb.toString();
	}

	@Override
	public long getMaximumUploadRequestSize() {
		return UploadServletRequestConfigurationProviderUtil.getMaxSize();
	}

	@Override
	public long getMaximumUploadSize() {
		return _dlValidator.getMaxAllowableSize(
			_dlRequestHelper.getSiteGroupId(), _getMimeType());
	}

	@Override
	public String getPublishButtonLabel() {
		if (_hasFolderWorkflowDefinitionLink()) {
			return "submit-for-workflow";
		}

		DLPortletInstanceSettings dlPortletInstanceSettings =
			_dlRequestHelper.getDLPortletInstanceSettings();

		if (dlPortletInstanceSettings.isEnableFileEntryDrafts() ||
			_fileEntryDisplayContextHelper.isCheckedOut()) {

			return "save";
		}

		return "publish";
	}

	@Override
	public String getSaveButtonLabel() {
		String saveButtonLabel = "save";

		if ((_fileVersion == null) ||
			_fileVersionDisplayContextHelper.isApproved() ||
			_fileVersionDisplayContextHelper.isDraft()) {

			saveButtonLabel = "save-as-draft";
		}

		return saveButtonLabel;
	}

	@Override
	public UUID getUuid() {
		return _UUID;
	}

	@Override
	public boolean isCancelCheckoutDocumentButtonDisabled() {
		return false;
	}

	@Override
	public boolean isCancelCheckoutDocumentButtonVisible()
		throws PortalException {

		return _fileEntryDisplayContextHelper.
			isCancelCheckoutDocumentActionAvailable();
	}

	@Override
	public boolean isCheckinButtonDisabled() {
		return false;
	}

	@Override
	public boolean isCheckinButtonVisible() throws PortalException {
		return _fileEntryDisplayContextHelper.isCheckinActionAvailable();
	}

	@Override
	public boolean isCheckoutDocumentButtonDisabled() {
		return false;
	}

	@Override
	public boolean isCheckoutDocumentButtonVisible() throws PortalException {
		return _fileEntryDisplayContextHelper.
			isCheckoutDocumentActionAvailable();
	}

	@Override
	public boolean isDDMStructureVisible(DDMStructure ddmStructure) {
		DDMForm ddmForm = ddmStructure.getDDMForm();

		List<DDMFormField> ddmFormFields = ddmForm.getDDMFormFields();

		return !ddmFormFields.isEmpty();
	}

	@Override
	public boolean isFolderSelectionVisible() {
		return _showSelectFolder;
	}

	@Override
	public boolean isNeverExpire() throws PortalException {
		if (_neverExpire != null) {
			return _neverExpire;
		}

		_neverExpire = ParamUtil.getBoolean(
			_httpServletRequest, "neverExpire", true);

		if (((_fileEntry != null) &&
			 (_fileEntry.getExpirationDate() != null)) ||
			((_fileVersion != null) &&
			 (_fileVersion.getExpirationDate() != null))) {

			_neverExpire = false;
		}

		return _neverExpire;
	}

	@Override
	public boolean isNeverReview() throws PortalException {
		if (_neverReview != null) {
			return _neverReview;
		}

		_neverReview = ParamUtil.getBoolean(
			_httpServletRequest, "neverReview", true);

		if (((_fileEntry != null) && (_fileEntry.getReviewDate() != null)) ||
			((_fileVersion != null) &&
			 (_fileVersion.getReviewDate() != null))) {

			_neverReview = false;
		}

		return _neverReview;
	}

	@Override
	public boolean isPermissionsVisible() {
		long repositoryId = ParamUtil.getLong(
			_dlRequestHelper.getRequest(), "repositoryId");

		if ((_fileEntry == null) &&
			!RepositoryUtil.isExternalRepository(repositoryId)) {

			return true;
		}

		return false;
	}

	@Override
	public boolean isPublishButtonDisabled() {
		DLPortletInstanceSettings dlPortletInstanceSettings =
			_dlRequestHelper.getDLPortletInstanceSettings();

		if (_fileEntryDisplayContextHelper.isCheckedOutByOther() ||
			(_fileVersionDisplayContextHelper.isPending() &&
			 dlPortletInstanceSettings.isEnableFileEntryDrafts())) {

			return true;
		}

		return false;
	}

	@Override
	public boolean isPublishButtonVisible() {
		return true;
	}

	@Override
	public boolean isSaveButtonDisabled() {
		if (_fileEntryDisplayContextHelper.isCheckedOutByOther()) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isSaveButtonVisible() {
		DLPortletInstanceSettings dlPortletInstanceSettings =
			_dlRequestHelper.getDLPortletInstanceSettings();

		return dlPortletInstanceSettings.isEnableFileEntryDrafts();
	}

	@Override
	public boolean isVersionInfoVisible() {
		return true;
	}

	private DefaultDLEditFileEntryDisplayContext(
		HttpServletRequest httpServletRequest, DLFileEntryType dlFileEntryType,
		DLValidator dlValidator, FileEntry fileEntry,
		StorageEngine storageEngine, DDMBeanTranslator ddmBeanTranslator,
		DDMFormValuesFactory ddmFormValuesFactory) {

		try {
			_httpServletRequest = httpServletRequest;
			_dlValidator = dlValidator;
			_fileEntry = fileEntry;
			_storageEngine = storageEngine;
			_ddmBeanTranslator = ddmBeanTranslator;
			_ddmFormValuesFactory = ddmFormValuesFactory;

			_dlRequestHelper = new DLRequestHelper(httpServletRequest);

			ThemeDisplay themeDisplay =
				(ThemeDisplay)httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			_fileEntryDisplayContextHelper = new FileEntryDisplayContextHelper(
				themeDisplay.getPermissionChecker(), _fileEntry);

			if ((dlFileEntryType == null) && (fileEntry != null)) {
				_dlFileEntryType =
					_fileEntryDisplayContextHelper.getDLFileEntryType();
			}
			else {
				_dlFileEntryType = dlFileEntryType;
			}

			if (fileEntry != null) {
				_fileVersion = fileEntry.getFileVersion();
			}
			else {
				_fileVersion = null;
			}

			_fileVersionDisplayContextHelper =
				new FileVersionDisplayContextHelper(_fileVersion);

			_showSelectFolder = ParamUtil.getBoolean(
				httpServletRequest, "showSelectFolder");
		}
		catch (PortalException portalException) {
			throw new SystemException(
				"Unable to build DefaultDLEditFileEntryDisplayContext for " +
					fileEntry,
				portalException);
		}
	}

	private HttpServletRequest _getDDMStructureHttpServletRequest(
		HttpServletRequest httpServletRequest, long structureId) {

		DynamicServletRequest dynamicServletRequest = new DynamicServletRequest(
			httpServletRequest, new HashMap<>());

		String namespace = structureId + StringPool.UNDERLINE;

		Map<String, String[]> parameterMap =
			httpServletRequest.getParameterMap();

		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String parameterName = entry.getKey();

			if (StringUtil.startsWith(parameterName, namespace)) {
				dynamicServletRequest.setParameterValues(
					parameterName.substring(namespace.length()),
					entry.getValue());
			}
		}

		return dynamicServletRequest;
	}

	private String _getMimeType() {
		if (_fileVersion == null) {
			return null;
		}

		return _fileVersion.getMimeType();
	}

	private boolean _hasFolderWorkflowDefinitionLink() {
		try {
			long folderId = BeanParamUtil.getLong(
				_fileEntry, _dlRequestHelper.getRequest(), "folderId");

			long fileEntryTypeId =
				DLFileEntryTypeConstants.FILE_ENTRY_TYPE_ID_BASIC_DOCUMENT;

			if (_dlFileEntryType != null) {
				fileEntryTypeId = _dlFileEntryType.getFileEntryTypeId();
			}

			return DLUtil.hasWorkflowDefinitionLink(
				_dlRequestHelper.getCompanyId(),
				_dlRequestHelper.getScopeGroupId(), folderId, fileEntryTypeId);
		}
		catch (Exception exception) {
			throw new SystemException(
				"Unable to check if folder has workflow definition link",
				exception);
		}
	}

	private boolean _isDDMFormValuesEdited(DDMStructure ddmStructure) {
		Enumeration<String> enumeration =
			_httpServletRequest.getParameterNames();

		String namespace = ddmStructure.getStructureId() + StringPool.UNDERLINE;

		while (enumeration.hasMoreElements()) {
			String parameterName = enumeration.nextElement();

			if (StringUtil.startsWith(
					parameterName,
					namespace +
						DDMFormRendererConstants.DDM_FORM_FIELD_NAME_PREFIX) &&
				StringUtil.endsWith(parameterName, "_edited") &&
				GetterUtil.getBoolean(
					_httpServletRequest.getParameter(parameterName))) {

				return true;
			}
		}

		return false;
	}

	private static final UUID _UUID = UUID.fromString(
		"63326141-02F6-42B5-AE38-ABC73FA72BB5");

	private final DDMBeanTranslator _ddmBeanTranslator;
	private final DDMFormValuesFactory _ddmFormValuesFactory;
	private final DLFileEntryType _dlFileEntryType;
	private final DLRequestHelper _dlRequestHelper;
	private final DLValidator _dlValidator;
	private final FileEntry _fileEntry;
	private final FileEntryDisplayContextHelper _fileEntryDisplayContextHelper;
	private final FileVersion _fileVersion;
	private final FileVersionDisplayContextHelper
		_fileVersionDisplayContextHelper;
	private final HttpServletRequest _httpServletRequest;
	private Boolean _neverExpire;
	private Boolean _neverReview;
	private final boolean _showSelectFolder;
	private final StorageEngine _storageEngine;

}