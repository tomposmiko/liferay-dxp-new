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

package com.liferay.headless.commerce.admin.account.resource.v1_0;

import com.liferay.headless.commerce.admin.account.dto.v1_0.AccountChannelEntry;
import com.liferay.headless.commerce.admin.account.dto.v1_0.User;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.odata.filter.ExpressionConvert;
import com.liferay.portal.odata.filter.FilterParserProvider;
import com.liferay.portal.odata.sort.SortParserProvider;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.UriInfo;

import org.osgi.annotation.versioning.ProviderType;

/**
 * To access this resource, run:
 *
 *     curl -u your@email.com:yourpassword -D - http://localhost:8080/o/headless-commerce-admin-account/v1.0
 *
 * @author Alessio Antonio Rendina
 * @generated
 */
@Generated("")
@ProviderType
public interface AccountChannelEntryResource {

	public void deleteAccountChannelBillingAddressId(Long id) throws Exception;

	public AccountChannelEntry getAccountChannelBillingAddressId(Long id)
		throws Exception;

	public AccountChannelEntry patchAccountChannelBillingAddressId(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public void deleteAccountChannelCurrencyId(Long id) throws Exception;

	public AccountChannelEntry getAccountChannelCurrencyId(Long id)
		throws Exception;

	public AccountChannelEntry patchAccountChannelCurrencyId(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public void deleteAccountChannelDeliveryTermId(Long id) throws Exception;

	public AccountChannelEntry getAccountChannelDeliveryTermId(Long id)
		throws Exception;

	public AccountChannelEntry patchAccountChannelDeliveryTermId(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public void deleteAccountChannelDiscountId(Long id) throws Exception;

	public AccountChannelEntry getAccountChannelDiscountId(Long id)
		throws Exception;

	public AccountChannelEntry patchAccountChannelDiscountId(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public void deleteAccountChannelPaymentTermId(Long id) throws Exception;

	public AccountChannelEntry getAccountChannelPaymentTermId(Long id)
		throws Exception;

	public AccountChannelEntry patchAccountChannelPaymentTermId(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public void deleteAccountChannelPriceListId(Long id) throws Exception;

	public AccountChannelEntry getAccountChannelPriceListId(Long id)
		throws Exception;

	public AccountChannelEntry patchAccountChannelPriceListId(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public void deleteAccountChannelShippingAddressId(Long id) throws Exception;

	public AccountChannelEntry getAccountChannelShippingAddressId(Long id)
		throws Exception;

	public AccountChannelEntry patchAccountChannelShippingAddressId(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public void deleteAccountChannelUserId(Long id) throws Exception;

	public AccountChannelEntry getAccountChannelUserId(Long id)
		throws Exception;

	public AccountChannelEntry patchAccountChannelUserId(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry>
			getAccountByExternalReferenceCodeAccountChannelBillingAddressesPage(
				String externalReferenceCode, Pagination pagination)
		throws Exception;

	public AccountChannelEntry
			postAccountByExternalReferenceCodeAccountChannelBillingAddress(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry>
			getAccountByExternalReferenceCodeAccountChannelCurrenciesPage(
				String externalReferenceCode, Pagination pagination)
		throws Exception;

	public AccountChannelEntry
			postAccountByExternalReferenceCodeAccountChannelCurrency(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry>
			getAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage(
				String externalReferenceCode, Pagination pagination)
		throws Exception;

	public AccountChannelEntry
			postAccountByExternalReferenceCodeAccountChannelDeliveryTerm(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry>
			getAccountByExternalReferenceCodeAccountChannelDiscountsPage(
				String externalReferenceCode, Pagination pagination)
		throws Exception;

	public AccountChannelEntry
			postAccountByExternalReferenceCodeAccountChannelDiscount(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry>
			getAccountByExternalReferenceCodeAccountChannelPaymentTermsPage(
				String externalReferenceCode, Pagination pagination)
		throws Exception;

	public AccountChannelEntry
			postAccountByExternalReferenceCodeAccountChannelPaymentTerm(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry>
			getAccountByExternalReferenceCodeAccountChannelPriceListsPage(
				String externalReferenceCode, Pagination pagination)
		throws Exception;

	public AccountChannelEntry
			postAccountByExternalReferenceCodeAccountChannelPriceList(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry>
			getAccountByExternalReferenceCodeAccountChannelShippingAddressesPage(
				String externalReferenceCode, Pagination pagination)
		throws Exception;

	public AccountChannelEntry
			postAccountByExternalReferenceCodeAccountChannelShippingAddress(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry>
			getAccountByExternalReferenceCodeAccountChannelUsersPage(
				String externalReferenceCode, Pagination pagination)
		throws Exception;

	public AccountChannelEntry
			postAccountByExternalReferenceCodeAccountChannelUser(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry>
			getAccountIdAccountChannelBillingAddressesPage(
				Long id, Pagination pagination)
		throws Exception;

	public AccountChannelEntry postAccountIdAccountChannelBillingAddress(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry> getAccountIdAccountChannelCurrenciesPage(
			Long id, Pagination pagination)
		throws Exception;

	public AccountChannelEntry postAccountIdAccountChannelCurrency(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry>
			getAccountIdAccountChannelDeliveryTermsPage(
				Long id, Pagination pagination)
		throws Exception;

	public AccountChannelEntry postAccountIdAccountChannelDeliveryTerm(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry> getAccountIdAccountChannelDiscountsPage(
			Long id, Pagination pagination)
		throws Exception;

	public AccountChannelEntry postAccountIdAccountChannelDiscount(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry> getAccountIdAccountChannelPaymentTermsPage(
			Long id, Pagination pagination)
		throws Exception;

	public AccountChannelEntry postAccountIdAccountChannelPaymentTerm(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry> getAccountIdAccountChannelPriceListsPage(
			Long id, Pagination pagination)
		throws Exception;

	public AccountChannelEntry postAccountIdAccountChannelPriceList(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry>
			getAccountIdAccountChannelShippingAddressesPage(
				Long id, Pagination pagination)
		throws Exception;

	public AccountChannelEntry postAccountIdAccountChannelShippingAddress(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public Page<AccountChannelEntry> getAccountIdAccountChannelUsersPage(
			Long id, Pagination pagination)
		throws Exception;

	public AccountChannelEntry postAccountIdAccountChannelUser(
			Long id, AccountChannelEntry accountChannelEntry)
		throws Exception;

	public default void setContextAcceptLanguage(
		AcceptLanguage contextAcceptLanguage) {
	}

	public void setContextCompany(
		com.liferay.portal.kernel.model.Company contextCompany);

	public default void setContextHttpServletRequest(
		HttpServletRequest contextHttpServletRequest) {
	}

	public default void setContextHttpServletResponse(
		HttpServletResponse contextHttpServletResponse) {
	}

	public default void setContextUriInfo(UriInfo contextUriInfo) {
	}

	public void setContextUser(
		com.liferay.portal.kernel.model.User contextUser);

	public void setExpressionConvert(
		ExpressionConvert<Filter> expressionConvert);

	public void setFilterParserProvider(
		FilterParserProvider filterParserProvider);

	public void setGroupLocalService(GroupLocalService groupLocalService);

	public void setResourceActionLocalService(
		ResourceActionLocalService resourceActionLocalService);

	public void setResourcePermissionLocalService(
		ResourcePermissionLocalService resourcePermissionLocalService);

	public void setRoleLocalService(RoleLocalService roleLocalService);

	public void setSortParserProvider(SortParserProvider sortParserProvider);

	public void setVulcanBatchEngineImportTaskResource(
		VulcanBatchEngineImportTaskResource
			vulcanBatchEngineImportTaskResource);

	public default Filter toFilter(String filterString) {
		return toFilter(
			filterString, Collections.<String, List<String>>emptyMap());
	}

	public default Filter toFilter(
		String filterString, Map<String, List<String>> multivaluedMap) {

		return null;
	}

	public default Sort[] toSorts(String sortsString) {
		return new Sort[0];
	}

	@ProviderType
	public interface Builder {

		public AccountChannelEntryResource build();

		public Builder checkPermissions(boolean checkPermissions);

		public Builder httpServletRequest(
			HttpServletRequest httpServletRequest);

		public Builder httpServletResponse(
			HttpServletResponse httpServletResponse);

		public Builder preferredLocale(Locale preferredLocale);

		public Builder user(com.liferay.portal.kernel.model.User user);

	}

	@ProviderType
	public interface Factory {

		public Builder create();

	}

}