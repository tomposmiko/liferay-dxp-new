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

package com.liferay.document.library.item.selector.web.internal.display.context;

import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetVocabularyService;
import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.item.selector.web.internal.DLItemSelectorView;
import com.liferay.document.library.kernel.model.DLFileEntryConstants;
import com.liferay.document.library.kernel.model.DLFileShortcutConstants;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryTypeLocalServiceUtil;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.item.selector.ItemSelectorReturnTypeResolver;
import com.liferay.item.selector.ItemSelectorReturnTypeResolverHandler;
import com.liferay.item.selector.criteria.info.item.criterion.InfoItemItemSelectorCriterion;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.dao.search.SearchPaginationUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.repository.Repository;
import com.liferay.portal.kernel.repository.RepositoryProviderUtil;
import com.liferay.portal.kernel.repository.capabilities.FileEntryTypeCapability;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchResult;
import com.liferay.portal.kernel.search.SearchResultUtil;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.staging.StagingGroupHelper;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Roberto Díaz
 */
public class DLItemSelectorViewDisplayContext<T extends ItemSelectorCriterion> {

	public DLItemSelectorViewDisplayContext(
		HttpServletRequest httpServletRequest, T itemSelectorCriterion,
		DLItemSelectorView<T> dlItemSelectorView,
		ItemSelectorReturnTypeResolverHandler
			itemSelectorReturnTypeResolverHandler,
		String itemSelectedEventName, boolean search, PortletURL portletURL,
		AssetVocabularyService assetVocabularyService,
		ClassNameLocalService classNameLocalService,
		StagingGroupHelper stagingGroupHelper) {

		_httpServletRequest = httpServletRequest;
		_itemSelectorCriterion = itemSelectorCriterion;
		_dlItemSelectorView = dlItemSelectorView;
		_itemSelectorReturnTypeResolverHandler =
			itemSelectorReturnTypeResolverHandler;
		_itemSelectedEventName = itemSelectedEventName;
		_search = search;
		_portletURL = portletURL;
		_assetVocabularyService = assetVocabularyService;
		_classNameLocalService = classNameLocalService;
		_stagingGroupHelper = stagingGroupHelper;

		_themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public String[] getExtensions() {
		return _dlItemSelectorView.getExtensions();
	}

	public long getFolderId() {
		if (_folderId != null) {
			return _folderId;
		}

		_folderId = ParamUtil.getLong(
			_httpServletRequest, "folderId",
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		return _folderId;
	}

	public String getItemSelectedEventName() {
		return _itemSelectedEventName;
	}

	public T getItemSelectorCriterion() {
		return _itemSelectorCriterion;
	}

	public ItemSelectorReturnTypeResolver getItemSelectorReturnTypeResolver() {
		return _itemSelectorReturnTypeResolverHandler.
			getItemSelectorReturnTypeResolver(
				_itemSelectorCriterion, _dlItemSelectorView, FileEntry.class);
	}

	public String[] getMimeTypes() {
		if (_mimeTypes != null) {
			return _mimeTypes;
		}

		String[] mimeTypes = _dlItemSelectorView.getMimeTypes();

		ItemSelectorCriterion itemSelectorCriterion =
			getItemSelectorCriterion();

		if (itemSelectorCriterion instanceof InfoItemItemSelectorCriterion) {
			InfoItemItemSelectorCriterion infoItemItemSelectorCriterion =
				(InfoItemItemSelectorCriterion)itemSelectorCriterion;

			String[] infoItemSelectorMimeTypes =
				infoItemItemSelectorCriterion.getMimeTypes();

			if (ArrayUtil.isNotEmpty(infoItemSelectorMimeTypes)) {
				mimeTypes = infoItemItemSelectorCriterion.getMimeTypes();
			}
		}

		_mimeTypes = mimeTypes;

		return _mimeTypes;
	}

	public PortletURL getPortletURL(
			LiferayPortletResponse liferayPortletResponse)
		throws PortletException {

		PortletURL portletURL = PortletURLUtil.clone(
			_portletURL, liferayPortletResponse);

		portletURL.setParameter("folderId", String.valueOf(getFolderId()));
		portletURL.setParameter("selectedTab", String.valueOf(getTitle()));

		return portletURL;
	}

	public List<Object> getRepositoryEntries() throws Exception {
		if (isSearch()) {
			Hits hits = _getHits();

			Document[] docs = hits.getDocs();

			List<Object> repositoryEntries = new ArrayList<>(docs.length);

			List<SearchResult> searchResults =
				SearchResultUtil.getSearchResults(
					hits, _themeDisplay.getLocale());

			for (SearchResult searchResult : searchResults) {
				String className = searchResult.getClassName();

				if (className.equals(DLFileEntryConstants.getClassName())) {
					repositoryEntries.add(
						DLAppServiceUtil.getFileEntry(
							searchResult.getClassPK()));
				}
				else if (className.equals(
							DLFileShortcutConstants.getClassName())) {

					repositoryEntries.add(
						DLAppServiceUtil.getFileShortcut(
							searchResult.getClassPK()));
				}
				else if (className.equals(DLFolderConstants.getClassName())) {
					repositoryEntries.add(
						DLAppServiceUtil.getFolder(searchResult.getClassPK()));
				}
			}

			return repositoryEntries;
		}

		String orderByCol = ParamUtil.getString(
			_httpServletRequest, "orderByCol", "title");
		String orderByType = ParamUtil.getString(
			_httpServletRequest, "orderByType", "asc");

		int[] startAndEnd = _getStartAndEnd();

		Repository repository = _getRepository();

		if (_isFilterByFileEntryType() &&
			repository.isCapabilityProvided(FileEntryTypeCapability.class)) {

			FileEntryTypeCapability fileEntryTypeCapability =
				repository.getCapability(FileEntryTypeCapability.class);

			return (List)
				fileEntryTypeCapability.
					getFoldersAndFileEntriesAndFileShortcuts(
						getStagingAwareGroupId(), getFolderId(), getMimeTypes(),
						_getFileEntryTypeId(), false,
						WorkflowConstants.STATUS_APPROVED, startAndEnd[0],
						startAndEnd[1],
						DLUtil.getRepositoryModelOrderByComparator(
							orderByCol, orderByType, true));
		}

		return DLAppServiceUtil.getFoldersAndFileEntriesAndFileShortcuts(
			getStagingAwareGroupId(), getFolderId(),
			WorkflowConstants.STATUS_APPROVED, getMimeTypes(), false, false,
			startAndEnd[0], startAndEnd[1],
			DLUtil.getRepositoryModelOrderByComparator(
				orderByCol, orderByType, true));
	}

	public int getRepositoryEntriesCount() throws PortalException {
		if (isSearch()) {
			Hits hits = _getHits();

			return hits.getLength();
		}

		Repository repository = _getRepository();

		if (_isFilterByFileEntryType() &&
			repository.isCapabilityProvided(FileEntryTypeCapability.class)) {

			FileEntryTypeCapability fileEntryTypeCapability =
				repository.getCapability(FileEntryTypeCapability.class);

			return fileEntryTypeCapability.
				getFoldersAndFileEntriesAndFileShortcutsCount(
					getStagingAwareGroupId(), getFolderId(), getMimeTypes(),
					_getFileEntryTypeId(), false,
					WorkflowConstants.STATUS_APPROVED);
		}

		return DLAppServiceUtil.getFoldersAndFileEntriesAndFileShortcutsCount(
			getStagingAwareGroupId(), getFolderId(),
			WorkflowConstants.STATUS_APPROVED, getMimeTypes(), false, false);
	}

	public long getStagingAwareGroupId() {
		if (_groupId != null) {
			return _groupId;
		}

		long groupId = _themeDisplay.getScopeGroupId();

		if (_stagingGroupHelper.isStagingGroup(groupId) &&
			!_stagingGroupHelper.isStagedPortlet(
				groupId, DLPortletKeys.DOCUMENT_LIBRARY)) {

			Group group = _stagingGroupHelper.fetchLiveGroup(groupId);

			if (group != null) {
				groupId = group.getGroupId();
			}
		}

		_groupId = groupId;

		return groupId;
	}

	public String getTitle() {
		return _dlItemSelectorView.getTitle(_themeDisplay.getLocale());
	}

	public PortletURL getUploadURL(
			LiferayPortletResponse liferayPortletResponse)
		throws PortalException {

		if (!isShowDragAndDropZone()) {
			return null;
		}

		List<AssetVocabulary> assetVocabularies =
			_assetVocabularyService.getGroupVocabularies(
				getStagingAwareGroupId());

		if (!assetVocabularies.isEmpty()) {
			long classNameId = _classNameLocalService.getClassNameId(
				DLFileEntryConstants.getClassName());
			long defaultFileEntryTypeId =
				DLFileEntryTypeLocalServiceUtil.getDefaultFileEntryTypeId(
					getFolderId());

			for (AssetVocabulary assetVocabulary : assetVocabularies) {
				if (assetVocabulary.isRequired(
						classNameId, defaultFileEntryTypeId)) {

					return null;
				}
			}
		}

		PortletURL portletURL = liferayPortletResponse.createActionURL(
			PortletKeys.DOCUMENT_LIBRARY);

		portletURL.setParameter(
			ActionRequest.ACTION_NAME, "/document_library/upload_file_entry");
		portletURL.setParameter("folderId", String.valueOf(getFolderId()));

		return portletURL;
	}

	public boolean isSearch() {
		return _search;
	}

	public boolean isShowDragAndDropZone() throws PortalException {
		if (_showDragAndDropZone != null) {
			return _showDragAndDropZone;
		}

		long defaultFileEntryTypeId =
			DLFileEntryTypeLocalServiceUtil.getDefaultFileEntryTypeId(
				getFolderId());

		if (DLUtil.hasWorkflowDefinitionLink(
				_themeDisplay.getCompanyId(), _themeDisplay.getScopeGroupId(),
				getFolderId(), defaultFileEntryTypeId)) {

			_showDragAndDropZone = false;
		}
		else {
			_showDragAndDropZone = true;
		}

		return _showDragAndDropZone;
	}

	private long _getFileEntryTypeId() {
		ItemSelectorCriterion itemSelectorCriterion =
			getItemSelectorCriterion();

		InfoItemItemSelectorCriterion infoItemItemSelectorCriterion =
			(InfoItemItemSelectorCriterion)itemSelectorCriterion;

		return GetterUtil.getLong(
			infoItemItemSelectorCriterion.getItemSubtype());
	}

	private Hits _getHits() throws PortalException {
		if (_hits != null) {
			return _hits;
		}

		_hits = DLAppServiceUtil.search(
			getStagingAwareGroupId(), _getSearchContext());

		return _hits;
	}

	private Repository _getRepository() throws PortalException {
		if (_repository != null) {
			return _repository;
		}

		Repository repository = null;

		if (getFolderId() != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			repository = RepositoryProviderUtil.getFolderRepository(
				getFolderId());
		}
		else {
			repository = RepositoryProviderUtil.getRepository(
				getStagingAwareGroupId());
		}

		_repository = repository;

		return _repository;
	}

	private SearchContext _getSearchContext() throws PortalException {
		if (_searchContext != null) {
			return _searchContext;
		}

		int[] startAndEnd = _getStartAndEnd();

		SearchContext searchContext = SearchContextFactory.getInstance(
			_httpServletRequest);

		Repository repository = _getRepository();

		if (_isFilterByFileEntryType() &&
			repository.isCapabilityProvided(FileEntryTypeCapability.class)) {

			searchContext.setAttribute(
				"fileEntryTypeId", _getFileEntryTypeId());
		}

		searchContext.setAttribute("mimeTypes", getMimeTypes());
		searchContext.setEnd(startAndEnd[1]);
		searchContext.setFolderIds(new long[] {getFolderId()});
		searchContext.setGroupIds(new long[] {getStagingAwareGroupId()});
		searchContext.setStart(startAndEnd[0]);

		_searchContext = searchContext;

		return _searchContext;
	}

	private int[] _getStartAndEnd() {
		if (_startAndEnd != null) {
			return _startAndEnd;
		}

		int cur = ParamUtil.getInteger(
			_httpServletRequest, SearchContainer.DEFAULT_CUR_PARAM,
			SearchContainer.DEFAULT_CUR);
		int delta = ParamUtil.getInteger(
			_httpServletRequest, SearchContainer.DEFAULT_DELTA_PARAM,
			SearchContainer.DEFAULT_DELTA);

		_startAndEnd = SearchPaginationUtil.calculateStartAndEnd(cur, delta);

		return _startAndEnd;
	}

	private boolean _isFilterByFileEntryType() {
		if (_filterByFileEntryType != null) {
			return _filterByFileEntryType;
		}

		boolean filterByFileEntryType = false;

		ItemSelectorCriterion itemSelectorCriterion =
			getItemSelectorCriterion();

		if (itemSelectorCriterion instanceof InfoItemItemSelectorCriterion) {
			InfoItemItemSelectorCriterion infoItemItemSelectorCriterion =
				(InfoItemItemSelectorCriterion)itemSelectorCriterion;

			if (Validator.isNotNull(
					infoItemItemSelectorCriterion.getItemSubtype())) {

				filterByFileEntryType = true;
			}
		}

		_filterByFileEntryType = filterByFileEntryType;

		return _filterByFileEntryType;
	}

	private final AssetVocabularyService _assetVocabularyService;
	private final ClassNameLocalService _classNameLocalService;
	private final DLItemSelectorView<T> _dlItemSelectorView;
	private Boolean _filterByFileEntryType;
	private Long _folderId;
	private Long _groupId;
	private Hits _hits;
	private final HttpServletRequest _httpServletRequest;
	private final String _itemSelectedEventName;
	private final T _itemSelectorCriterion;
	private final ItemSelectorReturnTypeResolverHandler
		_itemSelectorReturnTypeResolverHandler;
	private String[] _mimeTypes;
	private final PortletURL _portletURL;
	private Repository _repository;
	private final boolean _search;
	private SearchContext _searchContext;
	private Boolean _showDragAndDropZone;
	private final StagingGroupHelper _stagingGroupHelper;
	private int[] _startAndEnd;
	private final ThemeDisplay _themeDisplay;

}