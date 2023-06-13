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

package com.liferay.headless.admin.list.type.internal.graphql.servlet.v1_0;

import com.liferay.headless.admin.list.type.internal.graphql.mutation.v1_0.Mutation;
import com.liferay.headless.admin.list.type.internal.graphql.query.v1_0.Query;
import com.liferay.headless.admin.list.type.internal.resource.v1_0.ListTypeDefinitionResourceImpl;
import com.liferay.headless.admin.list.type.internal.resource.v1_0.ListTypeEntryResourceImpl;
import com.liferay.headless.admin.list.type.resource.v1_0.ListTypeDefinitionResource;
import com.liferay.headless.admin.list.type.resource.v1_0.ListTypeEntryResource;
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
 * @author Gabriel Albuquerque
 * @generated
 */
@Component(service = ServletData.class)
@Generated("")
public class ServletDataImpl implements ServletData {

	@Activate
	public void activate(BundleContext bundleContext) {
		Mutation.setListTypeDefinitionResourceComponentServiceObjects(
			_listTypeDefinitionResourceComponentServiceObjects);
		Mutation.setListTypeEntryResourceComponentServiceObjects(
			_listTypeEntryResourceComponentServiceObjects);

		Query.setListTypeDefinitionResourceComponentServiceObjects(
			_listTypeDefinitionResourceComponentServiceObjects);
		Query.setListTypeEntryResourceComponentServiceObjects(
			_listTypeEntryResourceComponentServiceObjects);
	}

	public String getApplicationName() {
		return "Liferay.Headless.Admin.List.Type";
	}

	@Override
	public Mutation getMutation() {
		return new Mutation();
	}

	@Override
	public String getPath() {
		return "/headless-admin-list-type-graphql/v1_0";
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
						"mutation#createListTypeDefinition",
						new ObjectValuePair<>(
							ListTypeDefinitionResourceImpl.class,
							"postListTypeDefinition"));
					put(
						"mutation#createListTypeDefinitionBatch",
						new ObjectValuePair<>(
							ListTypeDefinitionResourceImpl.class,
							"postListTypeDefinitionBatch"));
					put(
						"mutation#updateListTypeDefinitionByExternalReferenceCode",
						new ObjectValuePair<>(
							ListTypeDefinitionResourceImpl.class,
							"putListTypeDefinitionByExternalReferenceCode"));
					put(
						"mutation#deleteListTypeDefinition",
						new ObjectValuePair<>(
							ListTypeDefinitionResourceImpl.class,
							"deleteListTypeDefinition"));
					put(
						"mutation#deleteListTypeDefinitionBatch",
						new ObjectValuePair<>(
							ListTypeDefinitionResourceImpl.class,
							"deleteListTypeDefinitionBatch"));
					put(
						"mutation#patchListTypeDefinition",
						new ObjectValuePair<>(
							ListTypeDefinitionResourceImpl.class,
							"patchListTypeDefinition"));
					put(
						"mutation#updateListTypeDefinition",
						new ObjectValuePair<>(
							ListTypeDefinitionResourceImpl.class,
							"putListTypeDefinition"));
					put(
						"mutation#updateListTypeDefinitionBatch",
						new ObjectValuePair<>(
							ListTypeDefinitionResourceImpl.class,
							"putListTypeDefinitionBatch"));
					put(
						"mutation#createListTypeDefinitionListTypeEntry",
						new ObjectValuePair<>(
							ListTypeEntryResourceImpl.class,
							"postListTypeDefinitionListTypeEntry"));
					put(
						"mutation#createListTypeDefinitionListTypeEntryBatch",
						new ObjectValuePair<>(
							ListTypeEntryResourceImpl.class,
							"postListTypeDefinitionListTypeEntryBatch"));
					put(
						"mutation#deleteListTypeEntry",
						new ObjectValuePair<>(
							ListTypeEntryResourceImpl.class,
							"deleteListTypeEntry"));
					put(
						"mutation#deleteListTypeEntryBatch",
						new ObjectValuePair<>(
							ListTypeEntryResourceImpl.class,
							"deleteListTypeEntryBatch"));
					put(
						"mutation#updateListTypeEntry",
						new ObjectValuePair<>(
							ListTypeEntryResourceImpl.class,
							"putListTypeEntry"));
					put(
						"mutation#updateListTypeEntryBatch",
						new ObjectValuePair<>(
							ListTypeEntryResourceImpl.class,
							"putListTypeEntryBatch"));
					put(
						"mutation#updateListTypeEntryByExternalReferenceCode",
						new ObjectValuePair<>(
							ListTypeEntryResourceImpl.class,
							"putListTypeEntryByExternalReferenceCode"));

					put(
						"query#listTypeDefinitions",
						new ObjectValuePair<>(
							ListTypeDefinitionResourceImpl.class,
							"getListTypeDefinitionsPage"));
					put(
						"query#listTypeDefinitionByExternalReferenceCode",
						new ObjectValuePair<>(
							ListTypeDefinitionResourceImpl.class,
							"getListTypeDefinitionByExternalReferenceCode"));
					put(
						"query#listTypeDefinition",
						new ObjectValuePair<>(
							ListTypeDefinitionResourceImpl.class,
							"getListTypeDefinition"));
					put(
						"query#listTypeDefinitionListTypeEntries",
						new ObjectValuePair<>(
							ListTypeEntryResourceImpl.class,
							"getListTypeDefinitionListTypeEntriesPage"));
					put(
						"query#listTypeEntry",
						new ObjectValuePair<>(
							ListTypeEntryResourceImpl.class,
							"getListTypeEntry"));
					put(
						"query#listTypeEntryByExternalReferenceCode",
						new ObjectValuePair<>(
							ListTypeEntryResourceImpl.class,
							"getListTypeEntryByExternalReferenceCode"));
				}
			};

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<ListTypeDefinitionResource>
		_listTypeDefinitionResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<ListTypeEntryResource>
		_listTypeEntryResourceComponentServiceObjects;

}