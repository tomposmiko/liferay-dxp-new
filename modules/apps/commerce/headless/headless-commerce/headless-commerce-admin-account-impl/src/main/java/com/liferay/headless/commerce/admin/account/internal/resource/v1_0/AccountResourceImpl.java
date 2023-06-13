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

import com.liferay.account.constants.AccountConstants;
import com.liferay.account.exception.NoSuchEntryException;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountEntryOrganizationRel;
import com.liferay.account.model.AccountEntryUserRel;
import com.liferay.account.model.AccountGroup;
import com.liferay.account.model.AccountGroupRel;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountEntryOrganizationRelService;
import com.liferay.account.service.AccountEntryService;
import com.liferay.account.service.AccountEntryUserRelService;
import com.liferay.account.service.AccountGroupRelService;
import com.liferay.account.service.AccountGroupService;
import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.commerce.account.exception.NoSuchAccountGroupException;
import com.liferay.commerce.account.util.CommerceAccountHelper;
import com.liferay.commerce.constants.CommerceAddressConstants;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.headless.commerce.admin.account.dto.v1_0.Account;
import com.liferay.headless.commerce.admin.account.dto.v1_0.AccountAddress;
import com.liferay.headless.commerce.admin.account.dto.v1_0.AccountMember;
import com.liferay.headless.commerce.admin.account.dto.v1_0.AccountOrganization;
import com.liferay.headless.commerce.admin.account.internal.odata.entity.v1_0.AccountEntityModel;
import com.liferay.headless.commerce.admin.account.internal.util.v1_0.AccountMemberUtil;
import com.liferay.headless.commerce.admin.account.internal.util.v1_0.AccountOrganizationUtil;
import com.liferay.headless.commerce.admin.account.resource.v1_0.AccountResource;
import com.liferay.headless.commerce.core.util.ExpandoUtil;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Country;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.CountryService;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.service.RegionLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.multipart.MultipartBody;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.util.SearchUtil;

import java.io.IOException;

import java.util.Collections;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/account.properties",
	scope = ServiceScope.PROTOTYPE, service = AccountResource.class
)
public class AccountResourceImpl extends BaseAccountResourceImpl {

	@Override
	public Response deleteAccount(Long id) throws Exception {
		_accountEntryService.deleteAccountEntry(id);

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Response deleteAccountByExternalReferenceCode(
			String externalReferenceCode)
		throws Exception {

		AccountEntry accountEntry =
			_accountEntryService.fetchAccountEntryByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (accountEntry == null) {
			throw new NoSuchEntryException(
				"Unable to find account with external reference code " +
					externalReferenceCode);
		}

		_accountEntryService.deleteAccountEntry(
			accountEntry.getAccountEntryId());

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Response deleteAccountGroupByExternalReferenceCodeAccount(
			String accountExternalReferenceCode, String externalReferenceCode)
		throws Exception {

		AccountGroup accountGroup =
			_accountGroupService.fetchAccountGroupByExternalReferenceCode(
				externalReferenceCode, contextCompany.getCompanyId());

		if (accountGroup == null) {
			throw new NoSuchAccountGroupException(
				"Unable to find account group with external reference code " +
					externalReferenceCode);
		}

		AccountEntry accountEntry =
			_accountEntryService.fetchAccountEntryByExternalReferenceCode(
				contextCompany.getCompanyId(), accountExternalReferenceCode);

		if (accountEntry == null) {
			throw new NoSuchEntryException(
				"Unable to find Account with external reference code: " +
					accountExternalReferenceCode);
		}

		AccountGroupRel accountGroupRel =
			_accountGroupRelService.fetchAccountGroupRel(
				accountGroup.getAccountGroupId(), AccountEntry.class.getName(),
				accountEntry.getAccountEntryId());

		_accountGroupRelService.deleteAccountGroupRel(
			accountGroupRel.getAccountGroupRelId());

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Account getAccount(Long id) throws Exception {
		return _accountDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				GetterUtil.getLong(id),
				contextAcceptLanguage.getPreferredLocale()));
	}

	@Override
	public Account getAccountByExternalReferenceCode(
			String externalReferenceCode)
		throws Exception {

		AccountEntry accountEntry =
			_accountEntryService.fetchAccountEntryByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (accountEntry == null) {
			throw new NoSuchEntryException(
				"Unable to find account with external reference code " +
					externalReferenceCode);
		}

		return _accountDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				accountEntry.getAccountEntryId(),
				contextAcceptLanguage.getPreferredLocale()));
	}

	@Override
	public Page<Account> getAccountsPage(
			String search, Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		return SearchUtil.search(
			Collections.emptyMap(),
			booleanQuery -> booleanQuery.getPreBooleanFilter(), filter,
			AccountEntry.class.getName(), search, pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				Field.ENTRY_CLASS_PK),
			searchContext -> searchContext.setCompanyId(
				contextCompany.getCompanyId()),
			sorts,
			document -> _toAccount(
				_accountEntryLocalService.getAccountEntry(
					GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK)))));
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap)
		throws Exception {

		return _entityModel;
	}

	@Override
	public Response patchAccount(Long id, Account account) throws Exception {
		_updateAccount(id, account);

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Response patchAccountByExternalReferenceCode(
			String externalReferenceCode, Account account)
		throws Exception {

		AccountEntry accountEntry =
			_accountEntryService.fetchAccountEntryByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (accountEntry == null) {
			throw new NoSuchEntryException(
				"Unable to find account with external reference code " +
					externalReferenceCode);
		}

		_updateAccount(accountEntry.getAccountEntryId(), account);

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Account postAccount(Account account) throws Exception {
		AccountEntry accountEntry =
			_accountEntryService.addOrUpdateAccountEntry(
				account.getExternalReferenceCode(), contextUser.getUserId(),
				AccountConstants.ACCOUNT_ENTRY_ID_DEFAULT, account.getName(),
				null, null, _getEmailAddress(account, null), null,
				account.getTaxId(),
				GetterUtil.get(
					_toAccountEntryType(account.getType()),
					AccountConstants.ACCOUNT_ENTRY_TYPE_PERSON),
				_toAccountEntryStatus(
					GetterUtil.getBoolean(account.getActive(), true)),
				_serviceContextHelper.getServiceContext());

		if (_isValidId(account.getDefaultBillingAccountAddressId())) {
			_accountEntryLocalService.updateDefaultBillingAddressId(
				accountEntry.getAccountEntryId(),
				account.getDefaultBillingAccountAddressId());
		}

		if (_isValidId(account.getDefaultShippingAccountAddressId())) {
			_accountEntryLocalService.updateDefaultShippingAddressId(
				accountEntry.getAccountEntryId(),
				account.getDefaultShippingAccountAddressId());
		}

		// Expando

		Map<String, ?> customFields = account.getCustomFields();

		if ((customFields != null) && !customFields.isEmpty()) {
			ExpandoUtil.updateExpando(
				contextCompany.getCompanyId(), AccountEntry.class,
				accountEntry.getPrimaryKey(), customFields);
		}

		// Update nested resources

		_updateNestedResources(
			account, accountEntry, _serviceContextHelper.getServiceContext());

		return _accountDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				accountEntry.getAccountEntryId(),
				contextAcceptLanguage.getPreferredLocale()));
	}

	@Override
	public Response postAccountByExternalReferenceCodeLogo(
			String externalReferenceCode, MultipartBody multipartBody)
		throws Exception {

		AccountEntry accountEntry =
			_accountEntryService.fetchAccountEntryByExternalReferenceCode(
				contextCompany.getCompanyId(), externalReferenceCode);

		if (accountEntry == null) {
			throw new NoSuchEntryException(
				"Unable to find account with external reference code " +
					externalReferenceCode);
		}

		updateAccountLogo(accountEntry, multipartBody);

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Response postAccountGroupByExternalReferenceCodeAccount(
			String externalReferenceCode, Account account)
		throws Exception {

		AccountGroup accountGroup =
			_accountGroupService.fetchAccountGroupByExternalReferenceCode(
				externalReferenceCode, contextCompany.getCompanyId());

		if (accountGroup == null) {
			throw new NoSuchAccountGroupException(
				"Unable to find account group with external reference code " +
					externalReferenceCode);
		}

		AccountEntry accountEntry = null;

		if (account.getId() != null) {
			accountEntry = _accountEntryService.fetchAccountEntry(
				account.getId());
		}
		else if (account.getExternalReferenceCode() != null) {
			accountEntry =
				_accountEntryService.fetchAccountEntryByExternalReferenceCode(
					contextCompany.getCompanyId(),
					account.getExternalReferenceCode());
		}

		if (accountEntry == null) {
			throw new NoSuchEntryException(
				"Unable to find Account with external reference code: " +
					account.getExternalReferenceCode());
		}

		_accountGroupRelService.addAccountGroupRel(
			accountGroup.getAccountGroupId(), AccountEntry.class.getName(),
			accountEntry.getAccountEntryId());

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Response postAccountLogo(Long id, MultipartBody multipartBody)
		throws Exception {

		updateAccountLogo(
			_accountEntryLocalService.getAccountEntry(id), multipartBody);

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	public void updateAccountLogo(
			AccountEntry accountEntry, MultipartBody multipartBody)
		throws IOException, PortalException {

		_accountEntryService.updateAccountEntry(
			accountEntry.getAccountEntryId(),
			accountEntry.getParentAccountEntryId(), accountEntry.getName(),
			accountEntry.getDescription(), true,
			StringUtil.split(accountEntry.getDomains()),
			accountEntry.getEmailAddress(),
			multipartBody.getBinaryFileAsBytes("logo"),
			accountEntry.getTaxIdNumber(), accountEntry.getStatus(),
			_serviceContextHelper.getServiceContext(
				accountEntry.getAccountEntryGroupId()));
	}

	private String _getEmailAddress(
		Account account, AccountEntry accountEntry) {

		String[] emailAddresses = new String[0];

		if (account.getEmailAddresses() != null) {
			emailAddresses = account.getEmailAddresses();
		}

		if (emailAddresses.length > 0) {
			return emailAddresses[0];
		}

		if (accountEntry == null) {
			return "";
		}

		return accountEntry.getEmailAddress();
	}

	private long _getRegionId(Country country, AccountAddress accountAddress)
		throws Exception {

		if (Validator.isNull(accountAddress.getRegionISOCode()) ||
			(country == null)) {

			return 0;
		}

		Region region = _regionLocalService.fetchRegion(
			country.getCountryId(), accountAddress.getRegionISOCode());

		if (region == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"Unable to find region with ISO code ",
						accountAddress.getRegionISOCode(), " for country ",
						country.getCountryId()));
			}

			return 0;
		}

		return region.getRegionId();
	}

	private boolean _isValidId(Long value) {
		if ((value == null) || (value <= 0)) {
			return false;
		}

		return true;
	}

	private Account _toAccount(AccountEntry accountEntry) throws Exception {
		if (accountEntry == null) {
			return null;
		}

		return _accountDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				accountEntry.getAccountEntryId(),
				contextAcceptLanguage.getPreferredLocale()));
	}

	private Integer _toAccountEntryStatus(Boolean active) {
		if (active == null) {
			return WorkflowConstants.STATUS_ANY;
		}

		if (active) {
			return WorkflowConstants.STATUS_APPROVED;
		}

		return WorkflowConstants.STATUS_INACTIVE;
	}

	private String _toAccountEntryType(int commerceAccountType) {
		if (commerceAccountType ==
				CommerceAccountConstants.ACCOUNT_TYPE_BUSINESS) {

			return AccountConstants.ACCOUNT_ENTRY_TYPE_BUSINESS;
		}
		else if (commerceAccountType ==
					CommerceAccountConstants.ACCOUNT_TYPE_GUEST) {

			return AccountConstants.ACCOUNT_ENTRY_TYPE_GUEST;
		}
		else if (commerceAccountType ==
					CommerceAccountConstants.ACCOUNT_TYPE_PERSONAL) {

			return AccountConstants.ACCOUNT_ENTRY_TYPE_PERSON;
		}

		return null;
	}

	private AccountEntry _updateAccount(Long id, Account account)
		throws Exception {

		AccountEntry accountEntry = _accountEntryLocalService.getAccountEntry(
			id);

		ServiceContext serviceContext = _serviceContextHelper.getServiceContext(
			accountEntry.getAccountEntryGroupId());

		accountEntry.setDefaultBillingAddressId(
			GetterUtil.getLong(
				account.getDefaultBillingAccountAddressId(),
				accountEntry.getDefaultBillingAddressId()));
		accountEntry.setDefaultShippingAddressId(
			GetterUtil.getLong(
				account.getDefaultShippingAccountAddressId(),
				accountEntry.getDefaultShippingAddressId()));
		accountEntry.setEmailAddress(_getEmailAddress(account, accountEntry));
		accountEntry.setName(account.getName());
		accountEntry.setTaxIdNumber(
			GetterUtil.get(account.getTaxId(), accountEntry.getTaxIdNumber()));
		accountEntry.setStatus(
			_toAccountEntryStatus(
				GetterUtil.getBoolean(account.getActive(), true)));

		_accountEntryService.updateAccountEntry(accountEntry);

		// Expando

		Map<String, ?> customFields = account.getCustomFields();

		if ((customFields != null) && !customFields.isEmpty()) {
			ExpandoUtil.updateExpando(
				serviceContext.getCompanyId(), AccountEntry.class,
				accountEntry.getPrimaryKey(), customFields);
		}

		// Update nested resources

		_updateNestedResources(account, accountEntry, serviceContext);

		return accountEntry;
	}

	private AccountEntry _updateNestedResources(
			Account account, AccountEntry accountEntry,
			ServiceContext serviceContext)
		throws Exception {

		// Account addresses

		AccountAddress[] accountAddresses = account.getAccountAddresses();

		if (accountAddresses != null) {
			for (AccountAddress accountAddress : accountAddresses) {
				Country country = _countryService.fetchCountryByA2(
					accountEntry.getCompanyId(),
					accountAddress.getCountryISOCode());

				if (country == null) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							StringBundler.concat(
								"Unable to import account address with ",
								"country ISO code ", account.getName(),
								" and account name ",
								accountAddress.getCountryISOCode()));
					}

					continue;
				}

				long accountAddressId = GetterUtil.getLong(
					accountAddress.getId());

				if (accountAddressId > 0) {
					CommerceAddress exisitingCommerceAddress =
						_commerceAddressService.getCommerceAddress(
							accountAddressId);

					_commerceAddressService.updateCommerceAddress(
						exisitingCommerceAddress.getCommerceAddressId(),
						accountAddress.getName(),
						accountAddress.getDescription(),
						accountAddress.getStreet1(),
						accountAddress.getStreet2(),
						accountAddress.getStreet3(), accountAddress.getCity(),
						accountAddress.getZip(),
						_getRegionId(country, accountAddress),
						country.getCountryId(), accountAddress.getPhoneNumber(),
						GetterUtil.getInteger(
							accountAddress.getType(),
							CommerceAddressConstants.
								ADDRESS_TYPE_BILLING_AND_SHIPPING),
						serviceContext);

					if (GetterUtil.get(
							accountAddress.getDefaultBilling(), false)) {

						_accountEntryLocalService.updateDefaultBillingAddressId(
							accountEntry.getAccountEntryId(),
							exisitingCommerceAddress.getCommerceAddressId());
					}

					if (GetterUtil.get(
							accountAddress.getDefaultShipping(), false)) {

						_accountEntryLocalService.
							updateDefaultShippingAddressId(
								accountEntry.getAccountEntryId(),
								exisitingCommerceAddress.
									getCommerceAddressId());
					}

					continue;
				}

				CommerceAddress commerceAddress =
					_commerceAddressService.addCommerceAddress(
						GetterUtil.getString(
							accountAddress.getExternalReferenceCode(), null),
						AccountEntry.class.getName(),
						accountEntry.getAccountEntryId(),
						accountAddress.getName(),
						accountAddress.getDescription(),
						accountAddress.getStreet1(),
						accountAddress.getStreet2(),
						accountAddress.getStreet3(), accountAddress.getCity(),
						accountAddress.getZip(),
						_getRegionId(country, accountAddress),
						country.getCountryId(), accountAddress.getPhoneNumber(),
						GetterUtil.getInteger(
							accountAddress.getType(),
							CommerceAddressConstants.
								ADDRESS_TYPE_BILLING_AND_SHIPPING),
						serviceContext);

				if (GetterUtil.get(accountAddress.getDefaultBilling(), false)) {
					_accountEntryLocalService.updateDefaultBillingAddressId(
						accountEntry.getAccountEntryId(),
						commerceAddress.getCommerceAddressId());
				}

				if (GetterUtil.get(
						accountAddress.getDefaultShipping(), false)) {

					_accountEntryLocalService.updateDefaultShippingAddressId(
						accountEntry.getAccountEntryId(),
						commerceAddress.getCommerceAddressId());
				}
			}
		}

		// Account members

		AccountMember[] accountMembers = account.getAccountMembers();

		if (accountMembers != null) {
			for (AccountMember accountMember : accountMembers) {
				User user = AccountMemberUtil.getUser(
					_userLocalService, accountMember,
					contextCompany.getCompanyId());

				AccountEntryUserRel accountEntryUserRel =
					_accountEntryUserRelService.fetchAccountEntryUserRel(
						accountEntry.getAccountEntryId(), user.getUserId());

				if (accountEntryUserRel != null) {
					continue;
				}

				AccountMemberUtil.addAccountEntryUserRel(
					_accountEntryModelResourcePermission,
					_accountEntryUserRelService, accountMember, accountEntry,
					_commerceAccountHelper, user, serviceContext);
			}
		}

		// Account organizations

		AccountOrganization[] accountOrganizations =
			account.getAccountOrganizations();

		if (accountOrganizations != null) {
			for (AccountOrganization accountOrganization :
					accountOrganizations) {

				long organizationId = AccountOrganizationUtil.getOrganizationId(
					_organizationLocalService, accountOrganization,
					contextCompany.getCompanyId());

				AccountEntryOrganizationRel accountEntryOrganizationRel =
					_accountEntryOrganizationRelService.
						fetchAccountEntryOrganizationRel(
							accountEntry.getAccountEntryId(), organizationId);

				if (accountEntryOrganizationRel != null) {
					continue;
				}

				_accountEntryOrganizationRelService.
					addAccountEntryOrganizationRel(
						accountEntry.getAccountEntryId(), organizationId);
			}
		}

		return accountEntry;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AccountResourceImpl.class);

	private static final EntityModel _entityModel = new AccountEntityModel();

	@Reference(
		target = "(component.name=com.liferay.headless.commerce.admin.account.internal.dto.v1_0.converter.AccountDTOConverter)"
	)
	private DTOConverter<AccountEntry, Account> _accountDTOConverter;

	@Reference
	private AccountEntryLocalService _accountEntryLocalService;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(model.class.name=com.liferay.account.model.AccountEntry)"
	)
	private volatile ModelResourcePermission<AccountEntry>
		_accountEntryModelResourcePermission;

	@Reference
	private AccountEntryOrganizationRelService
		_accountEntryOrganizationRelService;

	@Reference
	private AccountEntryService _accountEntryService;

	@Reference
	private AccountEntryUserRelService _accountEntryUserRelService;

	@Reference
	private AccountGroupRelService _accountGroupRelService;

	@Reference
	private AccountGroupService _accountGroupService;

	@Reference
	private CommerceAccountHelper _commerceAccountHelper;

	@Reference
	private CommerceAddressService _commerceAddressService;

	@Reference
	private CountryService _countryService;

	@Reference
	private OrganizationLocalService _organizationLocalService;

	@Reference
	private RegionLocalService _regionLocalService;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

	@Reference
	private UserLocalService _userLocalService;

}