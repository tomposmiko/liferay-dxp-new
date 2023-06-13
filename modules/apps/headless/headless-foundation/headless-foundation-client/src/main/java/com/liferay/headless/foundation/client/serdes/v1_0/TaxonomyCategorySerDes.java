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

package com.liferay.headless.foundation.client.serdes.v1_0;

import com.liferay.headless.foundation.client.dto.v1_0.TaxonomyCategory;
import com.liferay.headless.foundation.client.json.BaseJSONParser;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class TaxonomyCategorySerDes {

	public static TaxonomyCategory toDTO(String json) {
		TaxonomyCategoryJSONParser taxonomyCategoryJSONParser =
			new TaxonomyCategoryJSONParser();

		return taxonomyCategoryJSONParser.parseToDTO(json);
	}

	public static TaxonomyCategory[] toDTOs(String json) {
		TaxonomyCategoryJSONParser taxonomyCategoryJSONParser =
			new TaxonomyCategoryJSONParser();

		return taxonomyCategoryJSONParser.parseToDTOs(json);
	}

	public static String toJSON(TaxonomyCategory taxonomyCategory) {
		if (taxonomyCategory == null) {
			return "{}";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"availableLanguages\": ");

		if (taxonomyCategory.getAvailableLanguages() == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0; i < taxonomyCategory.getAvailableLanguages().length;
				 i++) {

				sb.append("\"");
				sb.append(taxonomyCategory.getAvailableLanguages()[i]);
				sb.append("\"");

				if ((i + 1) < taxonomyCategory.getAvailableLanguages().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append(", ");

		sb.append("\"creator\": ");

		sb.append(taxonomyCategory.getCreator());
		sb.append(", ");

		sb.append("\"dateCreated\": ");

		sb.append("\"");
		sb.append(taxonomyCategory.getDateCreated());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"dateModified\": ");

		sb.append("\"");
		sb.append(taxonomyCategory.getDateModified());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"description\": ");

		sb.append("\"");
		sb.append(taxonomyCategory.getDescription());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"id\": ");

		sb.append(taxonomyCategory.getId());
		sb.append(", ");

		sb.append("\"name\": ");

		sb.append("\"");
		sb.append(taxonomyCategory.getName());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"numberOfTaxonomyCategories\": ");

		sb.append(taxonomyCategory.getNumberOfTaxonomyCategories());
		sb.append(", ");

		sb.append("\"parentTaxonomyCategory\": ");

		sb.append(taxonomyCategory.getParentTaxonomyCategory());
		sb.append(", ");

		sb.append("\"parentTaxonomyVocabulary\": ");

		sb.append(taxonomyCategory.getParentTaxonomyVocabulary());
		sb.append(", ");

		sb.append("\"parentVocabularyId\": ");

		sb.append(taxonomyCategory.getParentVocabularyId());
		sb.append(", ");

		sb.append("\"viewableBy\": ");

		sb.append("\"");
		sb.append(taxonomyCategory.getViewableBy());
		sb.append("\"");

		sb.append("}");

		return sb.toString();
	}

	public static String toJSON(
		Collection<TaxonomyCategory> taxonomyCategories) {

		if (taxonomyCategories == null) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("[");

		for (TaxonomyCategory taxonomyCategory : taxonomyCategories) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append(toJSON(taxonomyCategory));
		}

		sb.append("]");

		return sb.toString();
	}

	private static class TaxonomyCategoryJSONParser
		extends BaseJSONParser<TaxonomyCategory> {

		protected TaxonomyCategory createDTO() {
			return new TaxonomyCategory();
		}

		protected TaxonomyCategory[] createDTOArray(int size) {
			return new TaxonomyCategory[size];
		}

		protected void setField(
			TaxonomyCategory taxonomyCategory, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "availableLanguages")) {
				if (jsonParserFieldValue != null) {
					taxonomyCategory.setAvailableLanguages(
						toStrings((Object[])jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "creator")) {
				if (jsonParserFieldValue != null) {
					taxonomyCategory.setCreator(
						CreatorSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateCreated")) {
				if (jsonParserFieldValue != null) {
					taxonomyCategory.setDateCreated((Date)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateModified")) {
				if (jsonParserFieldValue != null) {
					taxonomyCategory.setDateModified(
						(Date)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "description")) {
				if (jsonParserFieldValue != null) {
					taxonomyCategory.setDescription(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					taxonomyCategory.setId((Long)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					taxonomyCategory.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "numberOfTaxonomyCategories")) {

				if (jsonParserFieldValue != null) {
					taxonomyCategory.setNumberOfTaxonomyCategories(
						(Number)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "parentTaxonomyCategory")) {

				if (jsonParserFieldValue != null) {
					taxonomyCategory.setParentTaxonomyCategory(
						ParentTaxonomyCategorySerDes.toDTO(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "parentTaxonomyVocabulary")) {

				if (jsonParserFieldValue != null) {
					taxonomyCategory.setParentTaxonomyVocabulary(
						ParentTaxonomyVocabularySerDes.toDTO(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "parentVocabularyId")) {

				if (jsonParserFieldValue != null) {
					taxonomyCategory.setParentVocabularyId(
						(Long)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "viewableBy")) {
				if (jsonParserFieldValue != null) {
					taxonomyCategory.setViewableBy(
						TaxonomyCategory.ViewableBy.create(
							(String)jsonParserFieldValue));
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}