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

package com.liferay.headless.delivery.internal.resource.v1_0;

import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.headless.common.spi.service.context.ServiceContextUtil;
import com.liferay.headless.delivery.dto.v1_0.StructuredContentFolder;
import com.liferay.headless.delivery.dto.v1_0.converter.DefaultDTOConverterContext;
import com.liferay.headless.delivery.internal.dto.v1_0.converter.StructuredContentFolderDTOConverter;
import com.liferay.headless.delivery.internal.dto.v1_0.util.CustomFieldsUtil;
import com.liferay.headless.delivery.internal.dto.v1_0.util.EntityFieldsUtil;
import com.liferay.headless.delivery.internal.odata.entity.v1_0.StructuredContentFolderEntityModel;
import com.liferay.headless.delivery.resource.v1_0.StructuredContentFolderResource;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.model.JournalFolderConstants;
import com.liferay.journal.service.JournalFolderService;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.SearchUtil;

import java.io.Serializable;

import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/structured-content-folder.properties",
	scope = ServiceScope.PROTOTYPE,
	service = StructuredContentFolderResource.class
)
public class StructuredContentFolderResourceImpl
	extends BaseStructuredContentFolderResourceImpl
	implements EntityModelResource {

	@Override
	public void deleteStructuredContentFolder(Long structuredContentFolderId)
		throws Exception {

		_journalFolderService.deleteFolder(structuredContentFolderId);
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap) {
		return new StructuredContentFolderEntityModel(
			EntityFieldsUtil.getEntityFields(
				_portal.getClassNameId(JournalFolder.class.getName()),
				contextCompany.getCompanyId(), _expandoColumnLocalService,
				_expandoTableLocalService));
	}

	@Override
	public Page<StructuredContentFolder> getSiteStructuredContentFoldersPage(
			Long siteId, Boolean flatten, String search, Filter filter,
			Pagination pagination, Sort[] sorts)
		throws Exception {

		Long parentStructuredContentFolderId = null;

		if (!GetterUtil.getBoolean(flatten)) {
			parentStructuredContentFolderId =
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID;
		}

		return _getFoldersPage(
			siteId, parentStructuredContentFolderId, search, filter, pagination,
			sorts);
	}

	@Override
	public StructuredContentFolder getStructuredContentFolder(
			Long structuredContentFolderId)
		throws Exception {

		return _toStructuredContentFolder(
			_journalFolderService.getFolder(structuredContentFolderId));
	}

	@Override
	public Page<StructuredContentFolder>
			getStructuredContentFolderStructuredContentFoldersPage(
				Long parentStructuredContentFolderId, String search,
				Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		JournalFolder journalFolder = _journalFolderService.getFolder(
			parentStructuredContentFolderId);

		return _getFoldersPage(
			journalFolder.getGroupId(), parentStructuredContentFolderId, search,
			filter, pagination, sorts);
	}

	@Override
	public StructuredContentFolder postSiteStructuredContentFolder(
			Long siteId, StructuredContentFolder structuredContentFolder)
		throws Exception {

		return _addStructuredContentFolder(
			siteId, JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			structuredContentFolder);
	}

	@Override
	public StructuredContentFolder
			postStructuredContentFolderStructuredContentFolder(
				Long parentStructuredContentFolderId,
				StructuredContentFolder structuredContentFolder)
		throws Exception {

		JournalFolder journalFolder = _journalFolderService.getFolder(
			parentStructuredContentFolderId);

		return _addStructuredContentFolder(
			journalFolder.getGroupId(), parentStructuredContentFolderId,
			structuredContentFolder);
	}

	@Override
	public StructuredContentFolder putStructuredContentFolder(
			Long structuredContentFolderId,
			StructuredContentFolder structuredContentFolder)
		throws Exception {

		JournalFolder journalFolder = _journalFolderService.getFolder(
			structuredContentFolderId);

		return _toStructuredContentFolder(
			_journalFolderService.updateFolder(
				journalFolder.getGroupId(), structuredContentFolderId,
				journalFolder.getParentFolderId(),
				structuredContentFolder.getName(),
				structuredContentFolder.getDescription(), false,
				ServiceContextUtil.createServiceContext(
					_getExpandoBridgeAttributes(structuredContentFolder),
					journalFolder.getGroupId(),
					structuredContentFolder.getViewableByAsString())));
	}

	@Override
	public void putStructuredContentFolderSubscribe(
			Long structuredContentFolderId)
		throws Exception {

		JournalFolder journalFolder = _journalFolderService.getFolder(
			structuredContentFolderId);

		_journalFolderService.subscribe(
			journalFolder.getGroupId(), journalFolder.getFolderId());
	}

	@Override
	public void putStructuredContentFolderUnsubscribe(
			Long structuredContentFolderId)
		throws Exception {

		JournalFolder journalFolder = _journalFolderService.getFolder(
			structuredContentFolderId);

		_journalFolderService.unsubscribe(
			journalFolder.getGroupId(), journalFolder.getFolderId());
	}

	private StructuredContentFolder _addStructuredContentFolder(
			Long siteId, Long parentFolderId,
			StructuredContentFolder structuredContentFolder)
		throws Exception {

		return _toStructuredContentFolder(
			_journalFolderService.addFolder(
				siteId, parentFolderId, structuredContentFolder.getName(),
				structuredContentFolder.getDescription(),
				ServiceContextUtil.createServiceContext(
					_getExpandoBridgeAttributes(structuredContentFolder),
					siteId, structuredContentFolder.getViewableByAsString())));
	}

	private Map<String, Serializable> _getExpandoBridgeAttributes(
		StructuredContentFolder structuredContentFolder) {

		return CustomFieldsUtil.toMap(
			JournalFolder.class.getName(), contextCompany.getCompanyId(),
			structuredContentFolder.getCustomFields(),
			contextAcceptLanguage.getPreferredLocale());
	}

	private Page<StructuredContentFolder> _getFoldersPage(
			Long siteId, Long parentStructuredContentFolderId, String search,
			Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		return SearchUtil.search(
			booleanQuery -> {
				if (parentStructuredContentFolderId != null) {
					BooleanFilter booleanFilter =
						booleanQuery.getPreBooleanFilter();

					booleanFilter.add(
						new TermFilter(
							Field.FOLDER_ID,
							String.valueOf(parentStructuredContentFolderId)),
						BooleanClauseOccur.MUST);
				}
			},
			filter, JournalFolder.class, search, pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				Field.ENTRY_CLASS_PK),
			searchContext -> {
				searchContext.setCompanyId(contextCompany.getCompanyId());
				searchContext.setGroupIds(new long[] {siteId});
			},
			document -> _toStructuredContentFolder(
				_journalFolderService.getFolder(
					GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK)))),
			sorts);
	}

	private StructuredContentFolder _toStructuredContentFolder(
			JournalFolder journalFolder)
		throws Exception {

		return _structuredContentFolderDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				contextAcceptLanguage.getPreferredLocale(),
				journalFolder.getFolderId(), contextUriInfo, contextUser));
	}

	@Reference
	private ExpandoColumnLocalService _expandoColumnLocalService;

	@Reference
	private ExpandoTableLocalService _expandoTableLocalService;

	@Reference
	private JournalFolderService _journalFolderService;

	@Reference
	private Portal _portal;

	@Reference
	private StructuredContentFolderDTOConverter
		_structuredContentFolderDTOConverter;

}