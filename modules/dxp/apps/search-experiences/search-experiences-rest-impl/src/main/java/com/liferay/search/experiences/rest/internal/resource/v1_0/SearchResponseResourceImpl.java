/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.rest.internal.resource.v1_0;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.document.Field;
import com.liferay.portal.search.searcher.SearchRequest;
import com.liferay.portal.search.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.searcher.Searcher;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.search.experiences.blueprint.search.request.enhancer.SXPBlueprintSearchRequestEnhancer;
import com.liferay.search.experiences.rest.dto.v1_0.Document;
import com.liferay.search.experiences.rest.dto.v1_0.DocumentField;
import com.liferay.search.experiences.rest.dto.v1_0.SearchResponse;
import com.liferay.search.experiences.rest.resource.v1_0.SearchResponseResource;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Brian Wing Shun Chan
 * @author André de Oliveira
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/search-response.properties",
	scope = ServiceScope.PROTOTYPE, service = SearchResponseResource.class
)
public class SearchResponseResourceImpl extends BaseSearchResponseResourceImpl {

	@Override
	public SearchResponse postSearch(
			String queryString, String sxpBlueprintJSON, Pagination pagination)
		throws Exception {

		try {
			return toSearchResponse(
				_searcher.search(
					_searchRequestBuilderFactory.builder(
					).companyId(
						contextCompany.getCompanyId()
					).emptySearchEnabled(
						true
					).includeResponseString(
						true
					).from(
						pagination.getStartPosition()
					).queryString(
						queryString
					).size(
						pagination.getPageSize()
					).withSearchRequestBuilder(
						searchRequestBuilder -> {
							if (Validator.isNotNull(sxpBlueprintJSON)) {
								_sxpBlueprintSearchRequestEnhancer.enhance(
									searchRequestBuilder, sxpBlueprintJSON);
							}
						}
					).build()));
		}
		catch (RuntimeException runtimeException) {
			if ((runtimeException.getClass() == RuntimeException.class) &&
				Validator.isBlank(runtimeException.getMessage()) &&
				ArrayUtil.isNotEmpty(runtimeException.getSuppressed())) {

				OutputStream outputStream = new ByteArrayOutputStream();

				runtimeException.printStackTrace(new PrintStream(outputStream));

				throw new RuntimeException(outputStream.toString());
			}

			throw runtimeException;
		}
	}

	protected SearchResponse toSearchResponse(
			com.liferay.portal.search.searcher.SearchResponse searchResponse)
		throws Exception {

		SearchRequest portalSearchRequest = searchResponse.getRequest();

		return new SearchResponse() {
			{
				documents = _toDocuments(searchResponse.getDocumentsStream());
				page = portalSearchRequest.getFrom();
				pageSize = portalSearchRequest.getSize();
				request = _createJSONObject(searchResponse.getRequestString());
				requestString = searchResponse.getRequestString();
				response = _createJSONObject(
					searchResponse.getResponseString());
				responseString = searchResponse.getResponseString();
				totalHits = searchResponse.getTotalHits();
			}

			private JSONObject _createJSONObject(String string) {
				try {
					return JSONFactoryUtil.createJSONObject(string);
				}
				catch (JSONException jsonException) {
					return null;
				}
			}

		};
	}

	private Map<String, DocumentField> _toDocumentFields(
		Map<String, Field> fields) {

		Map<String, DocumentField> documentFields = new LinkedHashMap<>();

		MapUtil.isNotEmptyForEach(
			fields,
			(name, field) -> {
				List<Object> valuesList = field.getValues();

				documentFields.put(
					name,
					new DocumentField() {
						{
							values = valuesList.toArray();
						}
					});
			});

		return documentFields;
	}

	private Document[] _toDocuments(
		Stream<com.liferay.portal.search.document.Document> stream) {

		List<Document> documents = new ArrayList<>();

		stream.forEach(
			document -> documents.add(
				new Document() {
					{
						documentFields = _toDocumentFields(
							document.getFields());
					}
				}));

		return documents.toArray(new Document[0]);
	}

	@Reference
	private Searcher _searcher;

	@Reference
	private SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Reference
	private SXPBlueprintSearchRequestEnhancer
		_sxpBlueprintSearchRequestEnhancer;

}