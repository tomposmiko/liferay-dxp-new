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

package com.liferay.layout.page.template.admin.web.internal.headless.delivery.dto.v1_0.structure.exporter;

import com.liferay.headless.delivery.dto.v1_0.ClassNameReference;
import com.liferay.headless.delivery.dto.v1_0.ClassPKReference;
import com.liferay.headless.delivery.dto.v1_0.CollectionConfig;
import com.liferay.headless.delivery.dto.v1_0.PageCollectionDefinition;
import com.liferay.headless.delivery.dto.v1_0.PageElement;
import com.liferay.info.list.provider.item.selector.criterion.InfoListProviderItemSelectorReturnType;
import com.liferay.item.selector.criteria.InfoListItemSelectorReturnType;
import com.liferay.layout.util.structure.CollectionStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Validator;

import java.util.Objects;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jürgen Kappler
 */
@Component(service = LayoutStructureItemExporter.class)
public class CollectionLayoutStructureItemExporter
	extends BaseStyledLayoutStructureItemExporter {

	@Override
	public String getClassName() {
		return CollectionStyledLayoutStructureItem.class.getName();
	}

	@Override
	public PageElement getPageElement(
		long groupId, LayoutStructureItem layoutStructureItem,
		boolean saveInlineContent, boolean saveMappingConfiguration) {

		CollectionStyledLayoutStructureItem
			collectionStyledLayoutStructureItem =
				(CollectionStyledLayoutStructureItem)layoutStructureItem;

		return new PageElement() {
			{
				definition = new PageCollectionDefinition() {
					{
						collectionConfig = _getCollectionConfig(
							collectionStyledLayoutStructureItem);
						listItemStyle =
							collectionStyledLayoutStructureItem.
								getListItemStyle();
						listStyle =
							collectionStyledLayoutStructureItem.getListStyle();
						numberOfColumns =
							collectionStyledLayoutStructureItem.
								getNumberOfColumns();
						numberOfItems =
							collectionStyledLayoutStructureItem.
								getNumberOfItems();
						templateKey =
							collectionStyledLayoutStructureItem.
								getTemplateKey();

						setFragmentStyle(
							() -> {
								JSONObject itemConfigJSONObject =
									collectionStyledLayoutStructureItem.
										getItemConfigJSONObject();

								return toFragmentStyle(
									itemConfigJSONObject.getJSONObject(
										"styles"),
									saveMappingConfiguration);
							});

						setFragmentViewports(
							() -> {
								JSONObject itemConfigJSONObject =
									collectionStyledLayoutStructureItem.
										getItemConfigJSONObject();

								return getFragmentViewPorts(
									itemConfigJSONObject);
							});
					}
				};
				type = PageElement.Type.COLLECTION;
			}
		};
	}

	private CollectionConfig _getCollectionConfig(
		CollectionStyledLayoutStructureItem
			collectionStyledLayoutStructureItem) {

		JSONObject jsonObject =
			collectionStyledLayoutStructureItem.getCollectionJSONObject();

		if (jsonObject == null) {
			return null;
		}

		String type = jsonObject.getString("type");

		if (Validator.isNull(type)) {
			return null;
		}

		if (Objects.equals(
				type, InfoListItemSelectorReturnType.class.getName())) {

			return new CollectionConfig() {
				{
					collectionReference = new ClassPKReference() {
						{
							className = portal.getClassName(
								jsonObject.getInt("classNameId"));
							classPK = jsonObject.getLong("classPK");
						}
					};
					collectionType = CollectionType.COLLECTION;
				}
			};
		}
		else if (Objects.equals(
					type,
					InfoListProviderItemSelectorReturnType.class.getName())) {

			return new CollectionConfig() {
				{
					collectionReference = new ClassNameReference() {
						{
							className = jsonObject.getString("key");
						}
					};
					collectionType =
						CollectionConfig.CollectionType.COLLECTION_PROVIDER;
				}
			};
		}

		return null;
	}

}