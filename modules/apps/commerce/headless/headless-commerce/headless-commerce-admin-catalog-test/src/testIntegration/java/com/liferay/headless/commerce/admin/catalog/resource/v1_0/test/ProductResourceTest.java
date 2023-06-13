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

package com.liferay.headless.commerce.admin.catalog.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalServiceUtil;
import com.liferay.commerce.product.type.simple.constants.SimpleCPTypeConstants;
import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.Product;
import com.liferay.headless.commerce.core.util.LanguageUtils;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Zoltán Takács
 * @author Crescenzo Rega
 */
@RunWith(Arquillian.class)
public class ProductResourceTest extends BaseProductResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_commerceCatalog = CommerceCatalogLocalServiceUtil.addCommerceCatalog(
			null, RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			LocaleUtil.US.getDisplayLanguage(),
			ServiceContextTestUtil.getServiceContext(testCompany.getGroupId()));
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();

		List<CPDefinition> cpDefinitions =
			_cpDefinitionLocalService.getCPDefinitions(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (CPDefinition cpDefinition : cpDefinitions) {
			_cpDefinitionLocalService.deleteCPDefinition(cpDefinition);
		}
	}

	@Ignore
	@Override
	@Test
	public void testDeleteProduct() throws Exception {
	}

	@Ignore
	@Override
	@Test
	public void testGetProduct() throws Exception {
	}

	@Ignore
	@Override
	@Test
	public void testGetProductsPageWithFilterStringEquals() throws Exception {
	}

	@Ignore
	@Override
	@Test
	public void testGetProductsPageWithSortDateTime() throws Exception {
	}

	@Ignore
	@Override
	@Test
	public void testGetProductsPageWithSortInteger() throws Exception {
	}

	@Ignore
	@Override
	@Test
	public void testGetProductsPageWithSortString() throws Exception {
	}

	@Ignore
	@Override
	@Test
	public void testGraphQLDeleteProduct() throws Exception {
	}

	@Ignore
	@Override
	@Test
	public void testGraphQLGetProduct() throws Exception {
	}

	@Ignore
	@Override
	@Test
	public void testGraphQLGetProductsPage() throws Exception {
	}

	@Ignore
	@Override
	@Test
	public void testPatchProduct() throws Exception {
	}

	@Ignore
	@Override
	@Test
	public void testPatchProductByExternalReferenceCode() throws Exception {
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {
			"active", "catalogId", "description", "externalReferenceCode",
			"name", "productType", "shortDescription"
		};
	}

	@Override
	protected Product randomProduct() throws Exception {
		return new Product() {
			{
				active = true;
				catalogId = _commerceCatalog.getCommerceCatalogId();
				description = LanguageUtils.getLanguageIdMap(
					RandomTestUtil.randomLocaleStringMap());
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				name = LanguageUtils.getLanguageIdMap(
					RandomTestUtil.randomLocaleStringMap());
				productType = SimpleCPTypeConstants.NAME;
				shortDescription = LanguageUtils.getLanguageIdMap(
					RandomTestUtil.randomLocaleStringMap());
			}
		};
	}

	@Override
	protected Product testDeleteProduct_addProduct() throws Exception {
		return productResource.postProduct(randomProduct());
	}

	@Override
	protected Product testDeleteProductByExternalReferenceCode_addProduct()
		throws Exception {

		return productResource.postProduct(randomProduct());
	}

	@Override
	protected Product testGetProduct_addProduct() throws Exception {
		return productResource.postProduct(randomProduct());
	}

	@Override
	protected Product testGetProductByExternalReferenceCode_addProduct()
		throws Exception {

		return productResource.postProduct(randomProduct());
	}

	@Override
	protected Product testGetProductsPage_addProduct(Product product)
		throws Exception {

		return productResource.postProduct(product);
	}

	@Override
	protected Product testGraphQLProduct_addProduct() throws Exception {
		return productResource.postProduct(randomProduct());
	}

	@Override
	protected Product testPostProduct_addProduct(Product product)
		throws Exception {

		return productResource.postProduct(product);
	}

	@Override
	protected Product testPostProductByExternalReferenceCodeClone_addProduct(
			Product product)
		throws Exception {

		return productResource.postProduct(product);
	}

	@Override
	protected Product testPostProductClone_addProduct(Product product)
		throws Exception {

		return productResource.postProduct(product);
	}

	@DeleteAfterTestRun
	private CommerceCatalog _commerceCatalog;

	@Inject
	private CPDefinitionLocalService _cpDefinitionLocalService;

}