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

package com.liferay.commerce.internal.object.system;

import com.liferay.commerce.pricing.model.CommercePricingClass;
import com.liferay.commerce.pricing.model.CommercePricingClassTable;
import com.liferay.commerce.pricing.service.CommercePricingClassLocalService;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductGroup;
import com.liferay.headless.commerce.admin.catalog.resource.v1_0.ProductGroupResource;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.model.ObjectField;
import com.liferay.object.system.BaseSystemObjectDefinitionManager;
import com.liferay.object.system.JaxRsApplicationDescriptor;
import com.liferay.object.system.SystemObjectDefinitionManager;
import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.Table;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gleice Lisbino
 */
@Component(enabled = true, service = SystemObjectDefinitionManager.class)
public class CommercePricingClassSystemObjectDefinitionManager
	extends BaseSystemObjectDefinitionManager {

	@Override
	public long addBaseModel(User user, Map<String, Object> values)
		throws Exception {

		ProductGroupResource productGroupResource = _buildProductGroupResource(
			user);

		ProductGroup productGroup = productGroupResource.postProductGroup(
			_toProductGroup(values));

		setExtendedProperties(
			ProductGroup.class.getName(), productGroup, user, values);

		return productGroup.getId();
	}

	@Override
	public BaseModel<?> deleteBaseModel(BaseModel<?> baseModel)
		throws PortalException {

		return _commercePricingClassLocalService.deleteCommercePricingClass(
			(CommercePricingClass)baseModel);
	}

	public BaseModel<?> fetchBaseModelByExternalReferenceCode(
		String externalReferenceCode, long companyId) {

		return _commercePricingClassLocalService.
			fetchCommercePricingClassByExternalReferenceCode(
				externalReferenceCode, companyId);
	}

	@Override
	public BaseModel<?> getBaseModelByExternalReferenceCode(
			String externalReferenceCode, long companyId)
		throws PortalException {

		return _commercePricingClassLocalService.
			getCommercePricingClassByExternalReferenceCode(
				externalReferenceCode, companyId);
	}

	@Override
	public String getExternalReferenceCode(long primaryKey)
		throws PortalException {

		CommercePricingClass commercePricingClass =
			_commercePricingClassLocalService.getCommercePricingClass(
				primaryKey);

		return commercePricingClass.getExternalReferenceCode();
	}

	@Override
	public JaxRsApplicationDescriptor getJaxRsApplicationDescriptor() {
		return new JaxRsApplicationDescriptor(
			"Liferay.Headless.Commerce.Admin.Catalog",
			"headless-commerce-admin-catalog", "product-groups", "v1.0");
	}

	@Override
	public Map<Locale, String> getLabelMap() {
		return createLabelMap("commerce-product-group");
	}

	@Override
	public Class<?> getModelClass() {
		return CommercePricingClass.class;
	}

	@Override
	public List<ObjectField> getObjectFields() {
		return Arrays.asList(
			createObjectField(
				"Text", "String", "description", "description", false, true),
			createObjectField(
				"Integer", "Integer", "number-of-products", "productsCount",
				false, true),
			createObjectField("Text", "String", "title", "title", true, true));
	}

	@Override
	public Map<Locale, String> getPluralLabelMap() {
		return createLabelMap("commerce-product-groups");
	}

	@Override
	public Column<?, Long> getPrimaryKeyColumn() {
		return CommercePricingClassTable.INSTANCE.commercePricingClassId;
	}

	@Override
	public String getScope() {
		return ObjectDefinitionConstants.SCOPE_COMPANY;
	}

	@Override
	public Table getTable() {
		return CommercePricingClassTable.INSTANCE;
	}

	@Override
	public String getTitleObjectFieldName() {
		return "title";
	}

	@Override
	public int getVersion() {
		return 2;
	}

	@Override
	public void updateBaseModel(
			long primaryKey, User user, Map<String, Object> values)
		throws Exception {

		ProductGroupResource productGroupResource = _buildProductGroupResource(
			user);

		productGroupResource.patchProductGroup(
			primaryKey, _toProductGroup(values));

		setExtendedProperties(
			ProductGroup.class.getName(), JSONUtil.put("id", primaryKey), user,
			values);
	}

	private ProductGroupResource _buildProductGroupResource(User user) {
		ProductGroupResource.Builder builder =
			_productGroupResourceFactory.create();

		return builder.checkPermissions(
			false
		).preferredLocale(
			user.getLocale()
		).user(
			user
		).build();
	}

	private ProductGroup _toProductGroup(Map<String, Object> values) {
		return new ProductGroup() {
			{
				description = getLanguageIdMap("description", values);
				externalReferenceCode = GetterUtil.getString(
					values.get("externalReferenceCode"));
				productsCount = GetterUtil.getInteger(
					values.get("productsCount"));
				title = getLanguageIdMap("title", values);
			}
		};
	}

	@Reference
	private CommercePricingClassLocalService _commercePricingClassLocalService;

	@Reference
	private ProductGroupResource.Factory _productGroupResourceFactory;

}