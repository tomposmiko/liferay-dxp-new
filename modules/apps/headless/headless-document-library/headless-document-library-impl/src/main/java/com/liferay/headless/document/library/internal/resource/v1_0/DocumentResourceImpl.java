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

package com.liferay.headless.document.library.internal.resource.v1_0;

import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetTagLocalService;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.model.DLVersionNumberIncrease;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.headless.common.spi.service.context.ServiceContextUtil;
import com.liferay.headless.document.library.dto.v1_0.Document;
import com.liferay.headless.document.library.internal.dto.v1_0.converter.DocumentDTOConverter;
import com.liferay.headless.document.library.internal.odata.entity.v1_0.DocumentEntityModel;
import com.liferay.headless.document.library.resource.v1_0.DocumentResource;
import com.liferay.headless.web.experience.dto.v1_0.converter.DefaultDTOConverterContext;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.multipart.BinaryFile;
import com.liferay.portal.vulcan.multipart.MultipartBody;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.SearchUtil;

import java.util.Optional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/document.properties",
	scope = ServiceScope.PROTOTYPE, service = DocumentResource.class
)
public class DocumentResourceImpl
	extends BaseDocumentResourceImpl implements EntityModelResource {

	@Override
	public void deleteDocument(Long documentId) throws Exception {
		_dlAppService.deleteFileEntry(documentId);
	}

	@Override
	public Page<Document> getContentSpaceDocumentsPage(
			Long contentSpaceId, Boolean flatten, String search, Filter filter,
			Pagination pagination, Sort[] sorts)
		throws Exception {

		return _getDocumentsPage(
			booleanQuery -> {
				BooleanFilter booleanFilter =
					booleanQuery.getPreBooleanFilter();

				if (!GetterUtil.getBoolean(flatten)) {
					booleanFilter.add(
						new TermFilter(
							Field.FOLDER_ID,
							String.valueOf(
								DLFolderConstants.DEFAULT_PARENT_FOLDER_ID)),
						BooleanClauseOccur.MUST);
				}

				if (contentSpaceId != null) {
					booleanFilter.add(
						new TermFilter(
							Field.GROUP_ID, String.valueOf(contentSpaceId)),
						BooleanClauseOccur.MUST);
				}
			},
			search, filter, pagination, sorts);
	}

	@Override
	public Document getDocument(Long documentId) throws Exception {
		FileEntry fileEntry = _dlAppService.getFileEntry(documentId);

		return _toDocument(fileEntry);
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap) {
		return _entityModel;
	}

	@Override
	public Page<Document> getFolderDocumentsPage(
			Long folderId, String search, Filter filter, Pagination pagination,
			Sort[] sorts)
		throws Exception {

		return _getDocumentsPage(
			booleanQuery -> {
				if (folderId != null) {
					BooleanFilter booleanFilter =
						booleanQuery.getPreBooleanFilter();

					booleanFilter.add(
						new TermFilter(
							Field.FOLDER_ID, String.valueOf(folderId)),
						BooleanClauseOccur.MUST);
				}
			},
			search, filter, pagination, sorts);
	}

	@Override
	public Document patchDocument(Long documentId, MultipartBody multipartBody)
		throws Exception {

		FileEntry existingFileEntry = _dlAppService.getFileEntry(documentId);

		BinaryFile binaryFile = Optional.ofNullable(
			multipartBody.getBinaryFile("file")
		).orElse(
			new BinaryFile(
				existingFileEntry.getMimeType(),
				existingFileEntry.getFileName(),
				existingFileEntry.getContentStream(),
				existingFileEntry.getSize())
		);

		Optional<Document> documentOptional =
			multipartBody.getValueAsInstanceOptional(
				"document", Document.class);

		String[] keywords = documentOptional.map(
			Document::getKeywords
		).orElseGet(
			() -> _assetTagLocalService.getTagNames(
				DLFileEntry.class.getName(), documentId)
		);

		Long[] categoryIds = documentOptional.map(
			Document::getTaxonomyCategoryIds
		).orElseGet(
			() -> ArrayUtil.toArray(
				_assetCategoryLocalService.getCategoryIds(
					DLFileEntry.class.getName(), documentId))
		);

		FileEntry fileEntry = _dlAppService.updateFileEntry(
			documentId, binaryFile.getFileName(), binaryFile.getContentType(),
			documentOptional.map(
				Document::getTitle
			).orElse(
				existingFileEntry.getTitle()
			),
			documentOptional.map(
				Document::getDescription
			).orElse(
				existingFileEntry.getDescription()
			),
			null, DLVersionNumberIncrease.AUTOMATIC,
			binaryFile.getInputStream(), binaryFile.getSize(),
			ServiceContextUtil.createServiceContext(
				keywords, categoryIds, existingFileEntry.getGroupId(),
				documentOptional.map(
					Document::getViewableBy
				).map(
					Document.ViewableBy::getValue
				).orElse(
					null
				)));

		return _toDocument(fileEntry);
	}

	@Override
	public Document postContentSpaceDocument(
			Long contentSpaceId, MultipartBody multipartBody)
		throws Exception {

		return _addDocument(contentSpaceId, 0L, contentSpaceId, multipartBody);
	}

	@Override
	public Document postFolderDocument(
			Long folderId, MultipartBody multipartBody)
		throws Exception {

		Folder folder = _dlAppService.getFolder(folderId);

		return _addDocument(
			folder.getRepositoryId(), folderId, folder.getGroupId(),
			multipartBody);
	}

	@Override
	public Document putDocument(Long documentId, MultipartBody multipartBody)
		throws Exception {

		Optional<Document> documentOptional =
			multipartBody.getValueAsInstanceOptional(
				"document", Document.class);

		if ((multipartBody.getBinaryFile("file") == null) &&
			!documentOptional.isPresent()) {

			throw new BadRequestException("No document or file found in body");
		}

		FileEntry existingFileEntry = _dlAppService.getFileEntry(documentId);

		BinaryFile binaryFile = Optional.ofNullable(
			multipartBody.getBinaryFile("file")
		).orElse(
			new BinaryFile(
				existingFileEntry.getMimeType(),
				existingFileEntry.getFileName(),
				existingFileEntry.getContentStream(),
				existingFileEntry.getSize())
		);

		FileEntry fileEntry = _dlAppService.updateFileEntry(
			documentId, binaryFile.getFileName(), binaryFile.getContentType(),
			documentOptional.map(
				Document::getTitle
			).orElse(
				existingFileEntry.getTitle()
			),
			documentOptional.map(
				Document::getDescription
			).orElse(
				null
			),
			null, DLVersionNumberIncrease.AUTOMATIC,
			binaryFile.getInputStream(), binaryFile.getSize(),
			ServiceContextUtil.createServiceContext(
				documentOptional.map(
					Document::getKeywords
				).orElse(
					new String[0]
				),
				documentOptional.map(
					Document::getTaxonomyCategoryIds
				).orElse(
					new Long[0]
				),
				existingFileEntry.getGroupId(),
				documentOptional.map(
					Document::getViewableByAsString
				).orElse(
					Document.ViewableBy.OWNER.getValue()
				)));

		return _toDocument(fileEntry);
	}

	private Document _addDocument(
			Long repositoryId, long folderId, Long groupId,
			MultipartBody multipartBody)
		throws Exception {

		BinaryFile binaryFile = multipartBody.getBinaryFile("file");

		if (binaryFile == null) {
			throw new BadRequestException("No file found in body");
		}

		Optional<Document> documentOptional =
			multipartBody.getValueAsInstanceOptional(
				"document", Document.class);

		FileEntry fileEntry = _dlAppService.addFileEntry(
			repositoryId, folderId, binaryFile.getFileName(),
			binaryFile.getContentType(),
			documentOptional.map(
				Document::getTitle
			).orElse(
				binaryFile.getFileName()
			),
			documentOptional.map(
				Document::getDescription
			).orElse(
				null
			),
			null, binaryFile.getInputStream(), binaryFile.getSize(),
			ServiceContextUtil.createServiceContext(
				documentOptional.map(
					Document::getKeywords
				).orElse(
					null
				),
				documentOptional.map(
					Document::getTaxonomyCategoryIds
				).orElse(
					null
				),
				groupId,
				documentOptional.map(
					Document::getViewableByAsString
				).orElse(
					Document.ViewableBy.OWNER.getValue()
				)));

		return _toDocument(fileEntry);
	}

	private Page<Document> _getDocumentsPage(
			UnsafeConsumer<BooleanQuery, Exception> booleanQueryUnsafeConsumer,
			String search, Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		return SearchUtil.search(
			booleanQueryUnsafeConsumer, filter, DLFileEntry.class, search,
			pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				Field.ENTRY_CLASS_PK),
			searchContext -> searchContext.setCompanyId(
				contextCompany.getCompanyId()),
			document -> _toDocument(
				_dlAppService.getFileEntry(
					GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK)))),
			sorts);
	}

	private Document _toDocument(FileEntry fileEntry) throws Exception {
		return _documentDTOConverter.toDTO(
			new DefaultDTOConverterContext(null, fileEntry.getFileEntryId()));
	}

	private static final EntityModel _entityModel = new DocumentEntityModel();

	@Reference
	private AssetCategoryLocalService _assetCategoryLocalService;

	@Reference
	private AssetTagLocalService _assetTagLocalService;

	@Reference
	private DLAppService _dlAppService;

	@Reference
	private DocumentDTOConverter _documentDTOConverter;

	@Context
	private User _user;

}