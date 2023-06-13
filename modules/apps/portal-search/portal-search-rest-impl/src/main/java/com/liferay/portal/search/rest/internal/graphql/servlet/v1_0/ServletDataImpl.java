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

package com.liferay.portal.search.rest.internal.graphql.servlet.v1_0;

import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.search.rest.internal.graphql.mutation.v1_0.Mutation;
import com.liferay.portal.search.rest.internal.graphql.query.v1_0.Query;
import com.liferay.portal.search.rest.internal.resource.v1_0.SearchResponseResourceImpl;
import com.liferay.portal.search.rest.internal.resource.v1_0.SuggestionResourceImpl;
import com.liferay.portal.search.rest.resource.v1_0.SearchResponseResource;
import com.liferay.portal.search.rest.resource.v1_0.SuggestionResource;
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
 * @author Petteri Karttunen
 * @generated
 */
@Component(service = ServletData.class)
@Generated("")
public class ServletDataImpl implements ServletData {

	@Activate
	public void activate(BundleContext bundleContext) {
		Mutation.setSearchResponseResourceComponentServiceObjects(
			_searchResponseResourceComponentServiceObjects);
		Mutation.setSuggestionResourceComponentServiceObjects(
			_suggestionResourceComponentServiceObjects);
	}

	public String getApplicationName() {
		return "Liferay.Portal.Search.REST";
	}

	@Override
	public Mutation getMutation() {
		return new Mutation();
	}

	@Override
	public String getPath() {
		return "/portal-search-rest-graphql/v1_0";
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
						"mutation#createSearch",
						new ObjectValuePair<>(
							SearchResponseResourceImpl.class, "postSearch"));
					put(
						"mutation#createSuggestionsPage",
						new ObjectValuePair<>(
							SuggestionResourceImpl.class,
							"postSuggestionsPage"));
				}
			};

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SearchResponseResource>
		_searchResponseResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SuggestionResource>
		_suggestionResourceComponentServiceObjects;

}