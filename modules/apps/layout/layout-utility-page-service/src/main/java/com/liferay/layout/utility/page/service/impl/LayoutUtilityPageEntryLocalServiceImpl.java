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

package com.liferay.layout.utility.page.service.impl;

import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.utility.page.exception.LayoutUtilityPageEntryNameException;
import com.liferay.layout.utility.page.model.LayoutUtilityPageEntry;
import com.liferay.layout.utility.page.service.base.LayoutUtilityPageEntryLocalServiceBaseImpl;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.ColorScheme;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.ModelHintsUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.ThemeLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.File;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.layout.utility.page.model.LayoutUtilityPageEntry",
	service = AopService.class
)
public class LayoutUtilityPageEntryLocalServiceImpl
	extends LayoutUtilityPageEntryLocalServiceBaseImpl {

	@Override
	public LayoutUtilityPageEntry addLayoutUtilityPageEntry(
			String externalReferenceCode, long userId, long groupId, long plid,
			long previewFileEntryId, boolean defaultLayoutUtilityPageEntry,
			String name, String type, long masterLayoutPlid)
		throws PortalException {

		_validateName(groupId, 0, name, type);

		LayoutUtilityPageEntry layoutUtilityPageEntry =
			layoutUtilityPageEntryPersistence.create(
				counterLocalService.increment());

		if (Validator.isNotNull(externalReferenceCode)) {
			layoutUtilityPageEntry.setExternalReferenceCode(
				externalReferenceCode);
		}
		else {
			layoutUtilityPageEntry.setExternalReferenceCode(
				_getExternalReferenceCode(groupId, name));
		}

		layoutUtilityPageEntry.setGroupId(groupId);

		User user = _userLocalService.getUser(userId);

		layoutUtilityPageEntry.setCompanyId(user.getCompanyId());
		layoutUtilityPageEntry.setUserId(user.getUserId());
		layoutUtilityPageEntry.setUserName(user.getFullName());

		if (plid == 0) {
			Layout layout = _addLayout(
				userId, groupId, name, masterLayoutPlid,
				ServiceContextThreadLocal.getServiceContext());

			if (layout != null) {
				plid = layout.getPlid();
			}
		}

		layoutUtilityPageEntry.setPlid(plid);

		layoutUtilityPageEntry.setPreviewFileEntryId(previewFileEntryId);
		layoutUtilityPageEntry.setDefaultLayoutUtilityPageEntry(
			defaultLayoutUtilityPageEntry);
		layoutUtilityPageEntry.setName(name);
		layoutUtilityPageEntry.setType(type);

		layoutUtilityPageEntry = layoutUtilityPageEntryPersistence.update(
			layoutUtilityPageEntry);

		_resourceLocalService.addResources(
			layoutUtilityPageEntry.getCompanyId(),
			layoutUtilityPageEntry.getGroupId(),
			layoutUtilityPageEntry.getUserId(),
			LayoutUtilityPageEntry.class.getName(),
			layoutUtilityPageEntry.getLayoutUtilityPageEntryId(), false, true,
			true);

		return layoutUtilityPageEntry;
	}

	@Override
	public LayoutUtilityPageEntry copyLayoutUtilityPageEntry(
			long userId, long groupId, long layoutUtilityPageEntryId,
			ServiceContext serviceContext)
		throws Exception {

		LayoutUtilityPageEntry sourceLayoutUtilityPageEntry =
			layoutUtilityPageEntryPersistence.findByPrimaryKey(
				layoutUtilityPageEntryId);

		String name = _getUniqueCopyName(
			groupId, sourceLayoutUtilityPageEntry.getName(),
			sourceLayoutUtilityPageEntry.getType(), serviceContext.getLocale());

		long masterLayoutPlid = 0;

		Layout layout = _layoutLocalService.fetchLayout(
			sourceLayoutUtilityPageEntry.getPlid());

		if (layout != null) {
			masterLayoutPlid = layout.getMasterLayoutPlid();
		}

		LayoutUtilityPageEntry layoutUtilityPageEntry =
			addLayoutUtilityPageEntry(
				null, userId, serviceContext.getScopeGroupId(), 0, 0, false,
				name, sourceLayoutUtilityPageEntry.getType(), masterLayoutPlid);

		long previewFileEntryId = _copyPreviewFileEntryId(
			layoutUtilityPageEntry.getLayoutUtilityPageEntryId(),
			sourceLayoutUtilityPageEntry.getPreviewFileEntryId(),
			serviceContext);

		if (previewFileEntryId > 0) {
			return layoutUtilityPageEntryLocalService.
				updateLayoutUtilityPageEntry(
					layoutUtilityPageEntry.getLayoutUtilityPageEntryId(),
					previewFileEntryId);
		}

		return layoutUtilityPageEntry;
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public LayoutUtilityPageEntry deleteLayoutUtilityPageEntry(
			LayoutUtilityPageEntry layoutUtilityPageEntry)
		throws PortalException {

		// Layout page template

		layoutUtilityPageEntryPersistence.remove(layoutUtilityPageEntry);

		// Resources

		_resourceLocalService.deleteResource(
			layoutUtilityPageEntry.getCompanyId(),
			LayoutUtilityPageEntry.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			layoutUtilityPageEntry.getLayoutUtilityPageEntryId());

		// Layout

		Layout layout = _layoutLocalService.fetchLayout(
			layoutUtilityPageEntry.getPlid());

		LayoutSet layoutSet = _layoutSetLocalService.fetchLayoutSet(
			layoutUtilityPageEntry.getGroupId(), false);

		if ((layout != null) && (layoutSet != null)) {
			_layoutLocalService.deleteLayout(layout);
		}

		// Portlet file entry

		if (layoutUtilityPageEntry.getPreviewFileEntryId() > 0) {
			_portletFileRepository.deletePortletFileEntry(
				layoutUtilityPageEntry.getPreviewFileEntryId());
		}

		return layoutUtilityPageEntry;
	}

	@Override
	public LayoutUtilityPageEntry deleteLayoutUtilityPageEntry(
			long layoutUtilityPageEntryId)
		throws PortalException {

		LayoutUtilityPageEntry layoutUtilityPageEntry =
			layoutUtilityPageEntryLocalService.fetchLayoutUtilityPageEntry(
				layoutUtilityPageEntryId);

		return layoutUtilityPageEntryLocalService.deleteLayoutUtilityPageEntry(
			layoutUtilityPageEntry);
	}

	@Override
	public LayoutUtilityPageEntry fetchDefaultLayoutUtilityPageEntry(
		long groupId, String type) {

		return layoutUtilityPageEntryPersistence.fetchByG_D_T_First(
			groupId, true, type, null);
	}

	@Override
	public LayoutUtilityPageEntry fetchLayoutUtilityPageEntry(
		long groupId, String name, String type) {

		return layoutUtilityPageEntryPersistence.fetchByG_N_T(
			groupId, name, type);
	}

	@Override
	public LayoutUtilityPageEntry getDefaultLayoutUtilityPageEntry(
			long groupId, String type)
		throws PortalException {

		return layoutUtilityPageEntryPersistence.findByG_D_T_First(
			groupId, true, type, null);
	}

	@Override
	public List<LayoutUtilityPageEntry> getLayoutUtilityPageEntries(
		long groupId) {

		return layoutUtilityPageEntryPersistence.findByGroupId(groupId);
	}

	@Override
	public List<LayoutUtilityPageEntry> getLayoutUtilityPageEntries(
		long groupId, int start, int end,
		OrderByComparator<LayoutUtilityPageEntry> orderByComparator) {

		return layoutUtilityPageEntryPersistence.findByGroupId(
			groupId, start, end, orderByComparator);
	}

	@Override
	public List<LayoutUtilityPageEntry> getLayoutUtilityPageEntries(
		long groupId, String type, int start, int end,
		OrderByComparator<LayoutUtilityPageEntry> orderByComparator) {

		return layoutUtilityPageEntryPersistence.findByG_T(
			groupId, type, start, end, orderByComparator);
	}

	@Override
	public int getLayoutUtilityPageEntriesCount(long groupId) {
		return layoutUtilityPageEntryPersistence.countByGroupId(groupId);
	}

	@Override
	public LayoutUtilityPageEntry setDefaultLayoutUtilityPageEntry(
			long layoutUtilityPageEntryId)
		throws PortalException {

		LayoutUtilityPageEntry layoutUtilityPageEntry =
			layoutUtilityPageEntryPersistence.findByPrimaryKey(
				layoutUtilityPageEntryId);

		LayoutUtilityPageEntry defaultLayoutUtilityPageEntry =
			layoutUtilityPageEntryPersistence.fetchByG_D_T_First(
				layoutUtilityPageEntry.getGroupId(), true,
				layoutUtilityPageEntry.getType(), null);

		if (defaultLayoutUtilityPageEntry != null) {
			defaultLayoutUtilityPageEntry.setDefaultLayoutUtilityPageEntry(
				false);

			layoutUtilityPageEntryPersistence.update(
				defaultLayoutUtilityPageEntry);
		}

		layoutUtilityPageEntry.setDefaultLayoutUtilityPageEntry(true);

		return layoutUtilityPageEntryPersistence.update(layoutUtilityPageEntry);
	}

	@Override
	public LayoutUtilityPageEntry updateLayoutUtilityPageEntry(
		long layoutUtilityPageEntryId, long previewFileEntryId) {

		LayoutUtilityPageEntry layoutUtilityPageEntry =
			layoutUtilityPageEntryPersistence.fetchByPrimaryKey(
				layoutUtilityPageEntryId);

		layoutUtilityPageEntry.setModifiedDate(new Date());
		layoutUtilityPageEntry.setPreviewFileEntryId(previewFileEntryId);

		return layoutUtilityPageEntryPersistence.update(layoutUtilityPageEntry);
	}

	@Override
	public LayoutUtilityPageEntry updateLayoutUtilityPageEntry(
			long layoutUtilityPageEntryId, String name)
		throws PortalException {

		LayoutUtilityPageEntry layoutUtilityPageEntry =
			layoutUtilityPageEntryPersistence.fetchByPrimaryKey(
				layoutUtilityPageEntryId);

		_validateName(
			layoutUtilityPageEntry.getGroupId(), layoutUtilityPageEntryId, name,
			layoutUtilityPageEntry.getType());

		layoutUtilityPageEntry.setName(name);

		return layoutUtilityPageEntryPersistence.update(layoutUtilityPageEntry);
	}

	private Layout _addLayout(
			long userId, long groupId, String name, long masterLayoutPlid,
			ServiceContext serviceContext)
		throws PortalException {

		Map<Locale, String> titleMap = Collections.singletonMap(
			LocaleUtil.getSiteDefault(), name);

		UnicodeProperties typeSettingsUnicodeProperties =
			new UnicodeProperties();

		if (masterLayoutPlid > 0) {
			typeSettingsUnicodeProperties.setProperty(
				"lfr-theme:regular:show-footer", Boolean.FALSE.toString());
			typeSettingsUnicodeProperties.setProperty(
				"lfr-theme:regular:show-header", Boolean.FALSE.toString());
			typeSettingsUnicodeProperties.setProperty(
				"lfr-theme:regular:show-header-search",
				Boolean.FALSE.toString());
			typeSettingsUnicodeProperties.setProperty(
				"lfr-theme:regular:wrap-widget-page-content",
				Boolean.FALSE.toString());
		}

		String typeSettings = typeSettingsUnicodeProperties.toString();

		serviceContext.setAttribute(
			"layout.instanceable.allowed", Boolean.TRUE);

		Layout layout = _layoutLocalService.addLayout(
			userId, groupId, true, 0, 0, 0, titleMap, titleMap, null, null,
			null, LayoutConstants.TYPE_CONTENT, typeSettings, true, true,
			new HashMap<>(), masterLayoutPlid, serviceContext);

		Layout draftLayout = layout.fetchDraftLayout();

		if (masterLayoutPlid > 0) {
			LayoutSet layoutSet = _layoutSetLocalService.getLayoutSet(
				groupId, false);

			String themeId = layoutSet.getThemeId();

			String colorSchemeId = _getColorSchemeId(
				layout.getCompanyId(), themeId);

			_layoutLocalService.updateLookAndFeel(
				groupId, true, draftLayout.getLayoutId(), themeId,
				colorSchemeId, StringPool.BLANK);

			layout = _layoutLocalService.updateLookAndFeel(
				groupId, true, layout.getLayoutId(), themeId, colorSchemeId,
				StringPool.BLANK);
		}

		return _layoutLocalService.updateStatus(
			userId, layout.getPlid(), WorkflowConstants.STATUS_DRAFT,
			serviceContext);
	}

	private long _copyPreviewFileEntryId(
			long layoutUtilityPageEntryId, long previewFileEntryId,
			ServiceContext serviceContext)
		throws Exception {

		if (previewFileEntryId <= 0) {
			return previewFileEntryId;
		}

		FileEntry portletFileEntry = _portletFileRepository.getPortletFileEntry(
			previewFileEntryId);

		Folder folder = portletFileEntry.getFolder();

		String fileName =
			layoutUtilityPageEntryId + "_preview." +
				portletFileEntry.getExtension();

		FileEntry fileEntry = _portletFileRepository.addPortletFileEntry(
			portletFileEntry.getGroupId(), serviceContext.getUserId(),
			LayoutUtilityPageEntry.class.getName(), layoutUtilityPageEntryId,
			LayoutAdminPortletKeys.GROUP_PAGES, folder.getFolderId(),
			_file.getBytes(portletFileEntry.getContentStream()), fileName,
			portletFileEntry.getMimeType(), false);

		return fileEntry.getFileEntryId();
	}

	private String _getColorSchemeId(long companyId, String themeId) {
		ColorScheme colorScheme = _themeLocalService.getColorScheme(
			companyId, themeId, StringPool.BLANK);

		return colorScheme.getColorSchemeId();
	}

	private String _getExternalReferenceCode(long groupId, String name) {
		String externalReferenceCode = StringUtil.toLowerCase(name.trim());

		externalReferenceCode = StringUtil.replace(
			externalReferenceCode, CharPool.SPACE, CharPool.DASH);

		int count = 0;

		while (true) {
			LayoutUtilityPageEntry layoutUtilityPageEntry =
				layoutUtilityPageEntryPersistence.fetchByERC_G(
					externalReferenceCode, groupId);

			if (layoutUtilityPageEntry == null) {
				return externalReferenceCode;
			}

			externalReferenceCode =
				externalReferenceCode + CharPool.DASH + count++;
		}
	}

	private String _getUniqueCopyName(
		long groupId, String sourceName, String type, Locale locale) {

		String copy = _language.get(locale, "copy");

		String name = StringUtil.appendParentheticalSuffix(sourceName, copy);

		for (int i = 1;; i++) {
			LayoutUtilityPageEntry layoutUtilityPageEntry =
				layoutUtilityPageEntryPersistence.fetchByG_N_T(
					groupId, name, type);

			if (layoutUtilityPageEntry == null) {
				break;
			}

			name = StringUtil.appendParentheticalSuffix(
				sourceName, copy + StringPool.SPACE + i);
		}

		return name;
	}

	private void _validateName(
			long groupId, long layoutUtilityPageEntryId, String name,
			String type)
		throws PortalException {

		if (Validator.isNull(name)) {
			throw new LayoutUtilityPageEntryNameException.MustNotBeNull(
				groupId);
		}

		int nameMaxLength = ModelHintsUtil.getMaxLength(
			LayoutUtilityPageEntry.class.getName(), "name");

		if (name.length() > nameMaxLength) {
			throw new LayoutUtilityPageEntryNameException.
				MustNotExceedMaximumSize(nameMaxLength);
		}

		for (char c : _BLACKLIST_CHAR) {
			if (name.indexOf(c) >= 0) {
				throw new LayoutUtilityPageEntryNameException.
					MustNotContainInvalidCharacters(c);
			}
		}

		LayoutUtilityPageEntry duplicatedLayoutUtilityPageEntry =
			layoutUtilityPageEntryPersistence.fetchByG_N_T(groupId, name, type);

		if ((duplicatedLayoutUtilityPageEntry != null) &&
			(duplicatedLayoutUtilityPageEntry.getLayoutUtilityPageEntryId() !=
				layoutUtilityPageEntryId)) {

			throw new LayoutUtilityPageEntryNameException.MustNotBeDuplicate(
				groupId, name);
		}
	}

	private static final char[] _BLACKLIST_CHAR = {
		';', '/', '?', ':', '@', '=', '&', '\"', '<', '>', '#', '%', '{', '}',
		'|', '\\', '^', '~', '[', ']', '`'
	};

	@Reference
	private DLFileEntryLocalService _dlFileEntryLocalService;

	@Reference
	private File _file;

	@Reference
	private Language _language;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutSetLocalService _layoutSetLocalService;

	@Reference
	private PortletFileRepository _portletFileRepository;

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private ThemeLocalService _themeLocalService;

	@Reference
	private UserLocalService _userLocalService;

}