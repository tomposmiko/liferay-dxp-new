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

package com.liferay.headless.commerce.admin.account.internal.resource.v1_0;

import com.liferay.account.exception.NoSuchEntryException;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountEntryOrganizationRel;
import com.liferay.account.service.AccountEntryOrganizationRelService;
import com.liferay.account.service.AccountEntryService;
import com.liferay.headless.commerce.admin.account.dto.v1_0.Account;
import com.liferay.headless.commerce.admin.account.dto.v1_0.AccountOrganization;
import com.liferay.headless.commerce.admin.account.internal.util.v1_0.AccountOrganizationUtil;
import com.liferay.headless.commerce.admin.account.resource.v1_0.AccountOrganizationResource;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.fields.NestedField;
import com.liferay.portal.vulcan.fields.NestedFieldSupport;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/account-organization.properties",
	scope = ServiceScope.PROTOTYPE,
	service = {AccountOrganizationResource.class, NestedFieldSupport.class}
)
public class AccountOrganizationResourceImpl
	extends BaseAccountOrganizationResourceImpl implements NestedFieldSupport {

	@Override
	public Response deleteAccountByExternalReferenceCodeAccountOrganization(
			String externalReferenceCode, Long organizationId)
		throws Exception {

		AccountEntry accountEntry =
			_accountEntryService.fetchAccountEntryByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (accountEntry == null) {
			throw new NoSuchEntryException(
				"Unable to find account with external reference code " +
					externalReferenceCode);
		}

		_accountEntryOrganizationRelService.deleteAccountEntryOrganizationRel(
			accountEntry.getAccountEntryId(), organizationId);

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public Response deleteAccountIdAccountOrganization(
			Long id, Long organizationId)
		throws Exception {

		_accountEntryOrganizationRelService.deleteAccountEntryOrganizationRel(
			id, organizationId);

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public AccountOrganization
			getAccountByExternalReferenceCodeAccountOrganization(
				String externalReferenceCode, Long organizationId)
		throws Exception {

		AccountEntry accountEntry =
			_accountEntryService.fetchAccountEntryByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (accountEntry == null) {
			throw new NoSuchEntryException(
				"Unable to find account with external reference code " +
					externalReferenceCode);
		}

		AccountEntryOrganizationRel accountEntryOrganizationRel =
			_accountEntryOrganizationRelService.getAccountEntryOrganizationRel(
				accountEntry.getAccountEntryId(), organizationId);

		return _accountOrganizationDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				accountEntryOrganizationRel.getPrimaryKey(),
				contextAcceptLanguage.getPreferredLocale()));
	}

	@Override
	public Page<AccountOrganization>
			getAccountByExternalReferenceCodeAccountOrganizationsPage(
				String externalReferenceCode, Pagination pagination)
		throws Exception {

		AccountEntry accountEntry =
			_accountEntryService.fetchAccountEntryByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (accountEntry == null) {
			throw new NoSuchEntryException(
				"Unable to find account with external reference code " +
					externalReferenceCode);
		}

		List<AccountEntryOrganizationRel> accountEntryOrganizationRels =
			_accountEntryOrganizationRelService.getAccountEntryOrganizationRels(
				accountEntry.getAccountEntryId(), pagination.getStartPosition(),
				pagination.getEndPosition());

		long totalItems =
			_accountEntryOrganizationRelService.
				getAccountEntryOrganizationRelsCount(
					accountEntry.getAccountEntryId());

		return Page.of(
			_toAccountOrganizations(accountEntryOrganizationRels), pagination,
			totalItems);
	}

	@Override
	public AccountOrganization getAccountIdAccountOrganization(
			Long id, Long organizationId)
		throws Exception {

		AccountEntryOrganizationRel accountEntryOrganizationRel =
			_accountEntryOrganizationRelService.getAccountEntryOrganizationRel(
				id, organizationId);

		return _accountOrganizationDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				accountEntryOrganizationRel.getPrimaryKey(),
				contextAcceptLanguage.getPreferredLocale()));
	}

	@NestedField(parentClass = Account.class, value = "organizations")
	@Override
	public Page<AccountOrganization> getAccountIdAccountOrganizationsPage(
			Long id, Pagination pagination)
		throws Exception {

		List<AccountEntryOrganizationRel> accountEntryOrganizationRels =
			_accountEntryOrganizationRelService.getAccountEntryOrganizationRels(
				id, pagination.getStartPosition(), pagination.getEndPosition());

		long totalItems =
			_accountEntryOrganizationRelService.
				getAccountEntryOrganizationRelsCount(id);

		return Page.of(
			_toAccountOrganizations(accountEntryOrganizationRels), pagination,
			totalItems);
	}

	@Override
	public AccountOrganization
			postAccountByExternalReferenceCodeAccountOrganization(
				String externalReferenceCode,
				AccountOrganization accountOrganization)
		throws Exception {

		AccountEntry accountEntry =
			_accountEntryService.fetchAccountEntryByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (accountEntry == null) {
			throw new NoSuchEntryException(
				"Unable to find account with external reference code " +
					externalReferenceCode);
		}

		AccountEntryOrganizationRel accountOrganizationRel =
			_accountEntryOrganizationRelService.addAccountEntryOrganizationRel(
				accountEntry.getAccountEntryId(),
				AccountOrganizationUtil.getOrganizationId(
					_organizationLocalService, accountOrganization,
					contextCompany.getCompanyId()));

		return _accountOrganizationDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				accountOrganizationRel.getPrimaryKey(),
				contextAcceptLanguage.getPreferredLocale()));
	}

	@Override
	public AccountOrganization postAccountIdAccountOrganization(
			Long id, AccountOrganization accountOrganization)
		throws Exception {

		AccountEntryOrganizationRel accountOrganizationRel =
			_accountEntryOrganizationRelService.addAccountEntryOrganizationRel(
				id,
				AccountOrganizationUtil.getOrganizationId(
					_organizationLocalService, accountOrganization,
					contextCompany.getCompanyId()));

		return _accountOrganizationDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				accountOrganizationRel.getPrimaryKey(),
				contextAcceptLanguage.getPreferredLocale()));
	}

	private List<AccountOrganization> _toAccountOrganizations(
			List<AccountEntryOrganizationRel> accountEntryOrganizationRels)
		throws Exception {

		List<AccountOrganization> accountOrganizations = new ArrayList<>();

		for (AccountEntryOrganizationRel accountEntryOrganizationRel :
				accountEntryOrganizationRels) {

			accountOrganizations.add(
				_accountOrganizationDTOConverter.toDTO(
					new DefaultDTOConverterContext(
						accountEntryOrganizationRel.getPrimaryKey(),
						contextAcceptLanguage.getPreferredLocale())));
		}

		return accountOrganizations;
	}

	@Reference
	private AccountEntryOrganizationRelService
		_accountEntryOrganizationRelService;

	@Reference
	private AccountEntryService _accountEntryService;

	@Reference(
		target = "(component.name=com.liferay.headless.commerce.admin.account.internal.dto.v1_0.converter.AccountOrganizationDTOConverter)"
	)
	private DTOConverter<AccountEntryOrganizationRel, AccountOrganization>
		_accountOrganizationDTOConverter;

	@Reference
	private OrganizationLocalService _organizationLocalService;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

}