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

package com.liferay.data.engine.rest.internal.graphql.servlet.v2_0;

import com.liferay.data.engine.rest.internal.graphql.mutation.v2_0.Mutation;
import com.liferay.data.engine.rest.internal.graphql.query.v2_0.Query;
import com.liferay.data.engine.rest.internal.resource.v2_0.DataDefinitionFieldLinkResourceImpl;
import com.liferay.data.engine.rest.internal.resource.v2_0.DataDefinitionResourceImpl;
import com.liferay.data.engine.rest.internal.resource.v2_0.DataLayoutResourceImpl;
import com.liferay.data.engine.rest.internal.resource.v2_0.DataListViewResourceImpl;
import com.liferay.data.engine.rest.internal.resource.v2_0.DataRecordCollectionResourceImpl;
import com.liferay.data.engine.rest.internal.resource.v2_0.DataRecordResourceImpl;
import com.liferay.data.engine.rest.resource.v2_0.DataDefinitionFieldLinkResource;
import com.liferay.data.engine.rest.resource.v2_0.DataDefinitionResource;
import com.liferay.data.engine.rest.resource.v2_0.DataLayoutResource;
import com.liferay.data.engine.rest.resource.v2_0.DataListViewResource;
import com.liferay.data.engine.rest.resource.v2_0.DataRecordCollectionResource;
import com.liferay.data.engine.rest.resource.v2_0.DataRecordResource;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.vulcan.graphql.servlet.ServletData;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * @author Jeyvison Nascimento
 * @generated
 */
@Component(service = ServletData.class)
@Generated("")
public class ServletDataImpl implements ServletData {

	@Activate
	public void activate(BundleContext bundleContext) {
		Mutation.setDataDefinitionResourceComponentServiceObjects(
			_dataDefinitionResourceComponentServiceObjects);
		Mutation.setDataLayoutResourceComponentServiceObjects(
			_dataLayoutResourceComponentServiceObjects);
		Mutation.setDataListViewResourceComponentServiceObjects(
			_dataListViewResourceComponentServiceObjects);
		Mutation.setDataRecordResourceComponentServiceObjects(
			_dataRecordResourceComponentServiceObjects);
		Mutation.setDataRecordCollectionResourceComponentServiceObjects(
			_dataRecordCollectionResourceComponentServiceObjects);

		Query.setDataDefinitionResourceComponentServiceObjects(
			_dataDefinitionResourceComponentServiceObjects);
		Query.setDataDefinitionFieldLinkResourceComponentServiceObjects(
			_dataDefinitionFieldLinkResourceComponentServiceObjects);
		Query.setDataLayoutResourceComponentServiceObjects(
			_dataLayoutResourceComponentServiceObjects);
		Query.setDataListViewResourceComponentServiceObjects(
			_dataListViewResourceComponentServiceObjects);
		Query.setDataRecordResourceComponentServiceObjects(
			_dataRecordResourceComponentServiceObjects);
		Query.setDataRecordCollectionResourceComponentServiceObjects(
			_dataRecordCollectionResourceComponentServiceObjects);
	}

	public String getApplicationName() {
		return "Liferay.Data.Engine.REST";
	}

	@Override
	public Mutation getMutation() {
		return new Mutation();
	}

	@Override
	public String getPath() {
		return "/data-engine-graphql/v2_0";
	}

	@Override
	public Query getQuery() {
		return new Query();
	}

	public ObjectValuePair<Class<?>, String> getResourceMethodObjectValuePair(
		String methodName, boolean mutation) {

		if (mutation) {
			return _resourceMethodObjectValuePairs.get(
				"mutation#" + methodName);
		}

		return _resourceMethodObjectValuePairs.get("query#" + methodName);
	}

	private static final Map<String, ObjectValuePair<Class<?>, String>>
		_resourceMethodObjectValuePairs =
			new HashMap<String, ObjectValuePair<Class<?>, String>>() {
				{
					put(
						"mutation#createDataDefinitionByContentType",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"postDataDefinitionByContentType"));
					put(
						"mutation#deleteDataDefinition",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"deleteDataDefinition"));
					put(
						"mutation#deleteDataDefinitionBatch",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"deleteDataDefinitionBatch"));
					put(
						"mutation#patchDataDefinition",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"patchDataDefinition"));
					put(
						"mutation#updateDataDefinition",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"putDataDefinition"));
					put(
						"mutation#updateDataDefinitionBatch",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"putDataDefinitionBatch"));
					put(
						"mutation#updateDataDefinitionPermissionsPage",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"putDataDefinitionPermissionsPage"));
					put(
						"mutation#createSiteDataDefinitionByContentType",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"postSiteDataDefinitionByContentType"));
					put(
						"mutation#deleteDataDefinitionDataLayout",
						new ObjectValuePair<>(
							DataLayoutResourceImpl.class,
							"deleteDataDefinitionDataLayout"));
					put(
						"mutation#createDataDefinitionDataLayout",
						new ObjectValuePair<>(
							DataLayoutResourceImpl.class,
							"postDataDefinitionDataLayout"));
					put(
						"mutation#createDataDefinitionDataLayoutBatch",
						new ObjectValuePair<>(
							DataLayoutResourceImpl.class,
							"postDataDefinitionDataLayoutBatch"));
					put(
						"mutation#deleteDataLayout",
						new ObjectValuePair<>(
							DataLayoutResourceImpl.class, "deleteDataLayout"));
					put(
						"mutation#deleteDataLayoutBatch",
						new ObjectValuePair<>(
							DataLayoutResourceImpl.class,
							"deleteDataLayoutBatch"));
					put(
						"mutation#updateDataLayout",
						new ObjectValuePair<>(
							DataLayoutResourceImpl.class, "putDataLayout"));
					put(
						"mutation#updateDataLayoutBatch",
						new ObjectValuePair<>(
							DataLayoutResourceImpl.class,
							"putDataLayoutBatch"));
					put(
						"mutation#createDataLayoutContext",
						new ObjectValuePair<>(
							DataLayoutResourceImpl.class,
							"postDataLayoutContext"));
					put(
						"mutation#deleteDataDefinitionDataListView",
						new ObjectValuePair<>(
							DataListViewResourceImpl.class,
							"deleteDataDefinitionDataListView"));
					put(
						"mutation#createDataDefinitionDataListView",
						new ObjectValuePair<>(
							DataListViewResourceImpl.class,
							"postDataDefinitionDataListView"));
					put(
						"mutation#createDataDefinitionDataListViewBatch",
						new ObjectValuePair<>(
							DataListViewResourceImpl.class,
							"postDataDefinitionDataListViewBatch"));
					put(
						"mutation#deleteDataListView",
						new ObjectValuePair<>(
							DataListViewResourceImpl.class,
							"deleteDataListView"));
					put(
						"mutation#deleteDataListViewBatch",
						new ObjectValuePair<>(
							DataListViewResourceImpl.class,
							"deleteDataListViewBatch"));
					put(
						"mutation#updateDataListView",
						new ObjectValuePair<>(
							DataListViewResourceImpl.class, "putDataListView"));
					put(
						"mutation#updateDataListViewBatch",
						new ObjectValuePair<>(
							DataListViewResourceImpl.class,
							"putDataListViewBatch"));
					put(
						"mutation#createDataDefinitionDataRecord",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class,
							"postDataDefinitionDataRecord"));
					put(
						"mutation#createDataDefinitionDataRecordBatch",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class,
							"postDataDefinitionDataRecordBatch"));
					put(
						"mutation#createDataRecordCollectionDataRecord",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class,
							"postDataRecordCollectionDataRecord"));
					put(
						"mutation#createDataRecordCollectionDataRecordBatch",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class,
							"postDataRecordCollectionDataRecordBatch"));
					put(
						"mutation#deleteDataRecord",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class, "deleteDataRecord"));
					put(
						"mutation#deleteDataRecordBatch",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class,
							"deleteDataRecordBatch"));
					put(
						"mutation#patchDataRecord",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class, "patchDataRecord"));
					put(
						"mutation#updateDataRecord",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class, "putDataRecord"));
					put(
						"mutation#updateDataRecordBatch",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class,
							"putDataRecordBatch"));
					put(
						"mutation#createDataDefinitionDataRecordCollection",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"postDataDefinitionDataRecordCollection"));
					put(
						"mutation#createDataDefinitionDataRecordCollectionBatch",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"postDataDefinitionDataRecordCollectionBatch"));
					put(
						"mutation#deleteDataRecordCollection",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"deleteDataRecordCollection"));
					put(
						"mutation#deleteDataRecordCollectionBatch",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"deleteDataRecordCollectionBatch"));
					put(
						"mutation#updateDataRecordCollection",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"putDataRecordCollection"));
					put(
						"mutation#updateDataRecordCollectionBatch",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"putDataRecordCollectionBatch"));
					put(
						"mutation#updateDataRecordCollectionPermissionsPage",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"putDataRecordCollectionPermissionsPage"));

					put(
						"query#dataDefinitionByContentTypeContentType",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"getDataDefinitionByContentTypeContentTypePage"));
					put(
						"query#dataDefinitionDataDefinitionFieldFieldTypes",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"getDataDefinitionDataDefinitionFieldFieldTypes"));
					put(
						"query#dataDefinition",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"getDataDefinition"));
					put(
						"query#dataDefinitionPermissions",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"getDataDefinitionPermissionsPage"));
					put(
						"query#siteDataDefinitionByContentTypeContentType",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"getSiteDataDefinitionByContentTypeContentTypePage"));
					put(
						"query#dataDefinitionByContentTypeByDataDefinitionKey",
						new ObjectValuePair<>(
							DataDefinitionResourceImpl.class,
							"getSiteDataDefinitionByContentTypeByDataDefinitionKey"));
					put(
						"query#dataDefinitionDataDefinitionFieldLinks",
						new ObjectValuePair<>(
							DataDefinitionFieldLinkResourceImpl.class,
							"getDataDefinitionDataDefinitionFieldLinksPage"));
					put(
						"query#dataDefinitionDataLayouts",
						new ObjectValuePair<>(
							DataLayoutResourceImpl.class,
							"getDataDefinitionDataLayoutsPage"));
					put(
						"query#dataLayout",
						new ObjectValuePair<>(
							DataLayoutResourceImpl.class, "getDataLayout"));
					put(
						"query#dataLayoutByContentTypeByDataLayoutKey",
						new ObjectValuePair<>(
							DataLayoutResourceImpl.class,
							"getSiteDataLayoutByContentTypeByDataLayoutKey"));
					put(
						"query#dataDefinitionDataListViews",
						new ObjectValuePair<>(
							DataListViewResourceImpl.class,
							"getDataDefinitionDataListViewsPage"));
					put(
						"query#dataListView",
						new ObjectValuePair<>(
							DataListViewResourceImpl.class, "getDataListView"));
					put(
						"query#dataDefinitionDataRecords",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class,
							"getDataDefinitionDataRecordsPage"));
					put(
						"query#dataRecordCollectionDataRecords",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class,
							"getDataRecordCollectionDataRecordsPage"));
					put(
						"query#dataRecordCollectionDataRecordExport",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class,
							"getDataRecordCollectionDataRecordExport"));
					put(
						"query#dataRecord",
						new ObjectValuePair<>(
							DataRecordResourceImpl.class, "getDataRecord"));
					put(
						"query#dataDefinitionDataRecordCollection",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"getDataDefinitionDataRecordCollection"));
					put(
						"query#dataDefinitionDataRecordCollections",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"getDataDefinitionDataRecordCollectionsPage"));
					put(
						"query#dataRecordCollection",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"getDataRecordCollection"));
					put(
						"query#dataRecordCollectionPermissions",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"getDataRecordCollectionPermissionsPage"));
					put(
						"query#dataRecordCollectionPermissionByCurrentUser",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"getDataRecordCollectionPermissionByCurrentUser"));
					put(
						"query#dataRecordCollectionByDataRecordCollectionKey",
						new ObjectValuePair<>(
							DataRecordCollectionResourceImpl.class,
							"getSiteDataRecordCollectionByDataRecordCollectionKey"));
				}
			};

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<DataDefinitionResource>
		_dataDefinitionResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<DataLayoutResource>
		_dataLayoutResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<DataListViewResource>
		_dataListViewResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<DataRecordResource>
		_dataRecordResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<DataRecordCollectionResource>
		_dataRecordCollectionResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<DataDefinitionFieldLinkResource>
		_dataDefinitionFieldLinkResourceComponentServiceObjects;

}