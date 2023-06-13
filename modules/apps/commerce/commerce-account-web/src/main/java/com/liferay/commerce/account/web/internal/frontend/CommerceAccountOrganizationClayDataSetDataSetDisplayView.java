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

package com.liferay.commerce.account.web.internal.frontend;

import com.liferay.commerce.account.constants.CommerceAccountActionKeys;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.model.CommerceAccountOrganizationRel;
import com.liferay.commerce.account.service.CommerceAccountOrganizationRelService;
import com.liferay.commerce.account.web.internal.model.Organization;
import com.liferay.frontend.taglib.clay.data.Filter;
import com.liferay.frontend.taglib.clay.data.Pagination;
import com.liferay.frontend.taglib.clay.data.set.ClayDataSetActionProvider;
import com.liferay.frontend.taglib.clay.data.set.ClayDataSetDisplayView;
import com.liferay.frontend.taglib.clay.data.set.provider.ClayDataSetDataProvider;
import com.liferay.frontend.taglib.clay.data.set.view.table.BaseTableClayDataSetDisplayView;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchema;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaBuilder;
import com.liferay.frontend.taglib.clay.data.set.view.table.ClayTableSchemaBuilderFactory;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false, immediate = true,
	property = {
		"clay.data.provider.key=" + CommerceAccountOrganizationClayDataSetDataSetDisplayView.NAME,
		"clay.data.set.display.name=" + CommerceAccountOrganizationClayDataSetDataSetDisplayView.NAME
	},
	service = {
		ClayDataSetActionProvider.class, ClayDataSetDataProvider.class,
		ClayDataSetDisplayView.class
	}
)
public class CommerceAccountOrganizationClayDataSetDataSetDisplayView
	extends BaseTableClayDataSetDisplayView
	implements ClayDataSetActionProvider,
			   ClayDataSetDataProvider<Organization> {

	public static final String NAME = "commerceAccountOrganizations";

	@Override
	public ClayTableSchema getClayTableSchema() {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.create();

		clayTableSchemaBuilder.addClayTableSchemaField("name", "name");
		clayTableSchemaBuilder.addClayTableSchemaField("path", "path");

		return clayTableSchemaBuilder.build();
	}

	@Override
	public List<DropdownItem> getDropdownItems(
			HttpServletRequest httpServletRequest, long groupId, Object model)
		throws PortalException {

		return DropdownItemListBuilder.add(
			() -> {
				long commerceAccountId = ParamUtil.getLong(
					httpServletRequest, "commerceAccountId");

				return _modelResourcePermission.contains(
					PermissionThreadLocal.getPermissionChecker(),
					commerceAccountId,
					CommerceAccountActionKeys.MANAGE_ORGANIZATIONS);
			},
			dropdownItem -> {
				Organization organization = (Organization)model;

				dropdownItem.setHref(
					"javascript:deleteCommerceAccountOrganization('" +
						organization.getOrganizationId() + "')");

				dropdownItem.setLabel(
					LanguageUtil.get(httpServletRequest, "delete"));
			}
		).build();
	}

	@Override
	public List<Organization> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<Organization> organizations = new ArrayList<>();

		long commerceAccountId = ParamUtil.getLong(
			httpServletRequest, "commerceAccountId");

		List<CommerceAccountOrganizationRel> commerceAccountOrganizationRels =
			_commerceAccountOrganizationRelService.
				getCommerceAccountOrganizationRels(
					commerceAccountId, pagination.getStartPosition(),
					pagination.getEndPosition());

		for (CommerceAccountOrganizationRel commerceAccountOrganizationRel :
				commerceAccountOrganizationRels) {

			com.liferay.portal.kernel.model.Organization organization =
				commerceAccountOrganizationRel.getOrganization();

			organizations.add(
				new Organization(
					organization.getOrganizationId(),
					commerceAccountOrganizationRel.getCommerceAccountId(),
					organization.getName(),
					getPath(organization.getTreePath())));
		}

		return organizations;
	}

	@Override
	public int getItemsCount(
			HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		long commerceAccountId = ParamUtil.getLong(
			httpServletRequest, "commerceAccountId");

		return _commerceAccountOrganizationRelService.
			getCommerceAccountOrganizationRelsCount(commerceAccountId);
	}

	protected String getPath(String treePath) throws PortalException {
		String[] organizationIds = StringUtil.split(
			treePath, CharPool.FORWARD_SLASH);

		StringBundler sb = new StringBundler(organizationIds.length * 2);

		for (int i = 1; i < organizationIds.length; i++) {
			sb.append(CharPool.FORWARD_SLASH);

			com.liferay.portal.kernel.model.Organization organization =
				_organizationLocalService.getOrganization(
					GetterUtil.getLong(organizationIds[i]));

			sb.append(organization.getName());
		}

		return sb.toString();
	}

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;

	@Reference
	private CommerceAccountOrganizationRelService
		_commerceAccountOrganizationRelService;

	@Reference(
		target = "(model.class.name=com.liferay.commerce.account.model.CommerceAccount)"
	)
	private ModelResourcePermission<CommerceAccount> _modelResourcePermission;

	@Reference
	private OrganizationLocalService _organizationLocalService;

}