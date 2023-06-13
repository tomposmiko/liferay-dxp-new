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

package com.liferay.commerce.warehouse.web.internal.display.context;

import com.liferay.commerce.frontend.model.HeaderActionModel;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseService;
import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.product.display.context.helper.CPRequestHelper;
import com.liferay.commerce.product.service.CommerceChannelRelService;
import com.liferay.frontend.data.set.model.FDSActionDropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.RenderURL;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andrea Di Giorgi
 * @author Alessio Antonio Rendina
 * @author Crescenzo Rega
 */
public class CommerceInventoryWarehousesDisplayContext {

	public CommerceInventoryWarehousesDisplayContext(
		CommerceChannelRelService commerceChannelRelService,
		CommerceInventoryWarehouseService commerceInventoryWarehouseService,
		HttpServletRequest httpServletRequest, Portal portal,
		ModelResourcePermission<CommerceInventoryWarehouse>
			commerceInventoryWarehouseModelResourcePermission) {

		this.commerceChannelRelService = commerceChannelRelService;

		_commerceInventoryWarehouseService = commerceInventoryWarehouseService;

		this.httpServletRequest = httpServletRequest;

		cpRequestHelper = new CPRequestHelper(httpServletRequest);

		_portal = portal;
		_commerceInventoryWarehouseModelResourcePermission =
			commerceInventoryWarehouseModelResourcePermission;
	}

	public String getAddCommerceWarehouseRenderURL() throws Exception {
		return PortletURLBuilder.createRenderURL(
			cpRequestHelper.getLiferayPortletResponse()
		).setMVCRenderCommandName(
			"/commerce_inventory_warehouse/add_commerce_inventory_warehouse"
		).setWindowState(
			LiferayWindowState.POP_UP
		).buildString();
	}

	public CommerceInventoryWarehouse getCommerceInventoryWarehouse()
		throws PortalException {

		if (_commerceInventoryWarehouse != null) {
			return _commerceInventoryWarehouse;
		}

		long commerceInventoryWarehouseId = ParamUtil.getLong(
			cpRequestHelper.getRenderRequest(), "commerceInventoryWarehouseId");

		if (commerceInventoryWarehouseId > 0) {
			_commerceInventoryWarehouse =
				_commerceInventoryWarehouseService.
					getCommerceInventoryWarehouse(commerceInventoryWarehouseId);
		}

		return _commerceInventoryWarehouse;
	}

	public long getCommerceInventoryWarehouseId() throws PortalException {
		CommerceInventoryWarehouse commerceInventoryWarehouse =
			getCommerceInventoryWarehouse();

		if (commerceInventoryWarehouse == null) {
			return 0;
		}

		return commerceInventoryWarehouse.getCommerceInventoryWarehouseId();
	}

	public String getCountryTwoLettersIsoCode() {
		return ParamUtil.getString(
			cpRequestHelper.getRenderRequest(), "countryTwoLettersISOCode",
			null);
	}

	public PortletURL getEditCommerceWarehouseRenderURL() {
		return PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				cpRequestHelper.getRequest(),
				CPPortletKeys.COMMERCE_INVENTORY_WAREHOUSE,
				PortletRequest.RENDER_PHASE)
		).setMVCRenderCommandName(
			"/commerce_inventory_warehouse/edit_commerce_inventory_warehouse"
		).buildPortletURL();
	}

	public List<HeaderActionModel> getHeaderActionModels() throws Exception {
		List<HeaderActionModel> headerActionModels = new ArrayList<>();

		LiferayPortletResponse liferayPortletResponse =
			cpRequestHelper.getLiferayPortletResponse();

		RenderResponse renderResponse = cpRequestHelper.getRenderResponse();

		RenderURL cancelURL = renderResponse.createRenderURL();

		HeaderActionModel cancelHeaderActionModel = new HeaderActionModel(
			null, cancelURL.toString(), null, "cancel");

		headerActionModels.add(cancelHeaderActionModel);

		HeaderActionModel saveHeaderActionModel = new HeaderActionModel(
			"btn-primary", liferayPortletResponse.getNamespace() + "fm",
			PortletURLBuilder.createActionURL(
				liferayPortletResponse
			).setActionName(
				"/commerce_inventory_warehouse" +
					"/edit_commerce_inventory_warehouse"
			).buildString(),
			null, "save");

		headerActionModels.add(saveHeaderActionModel);

		return headerActionModels;
	}

	public PortletURL getPortletCommerceInventoryWarehouseURL() {
		LiferayPortletResponse liferayPortletResponse =
			cpRequestHelper.getLiferayPortletResponse();

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		String redirect = ParamUtil.getString(httpServletRequest, "redirect");

		if (Validator.isNotNull(redirect)) {
			portletURL.setParameter("redirect", redirect);
		}

		long commerceInventoryWarehouseId = ParamUtil.getLong(
			httpServletRequest, "commerceInventoryWarehouseId");

		if (commerceInventoryWarehouseId > 0) {
			portletURL.setParameter(
				"commerceInventoryWarehouseId",
				String.valueOf(commerceInventoryWarehouseId));
		}

		return portletURL;
	}

	public PortletURL getPortletURL() {
		return PortletURLBuilder.createRenderURL(
			cpRequestHelper.getRenderResponse()
		).setKeywords(
			_getKeywords()
		).setNavigation(
			_getNavigation()
		).setParameter(
			"countryTwoLettersISOCode", getCountryTwoLettersIsoCode()
		).setParameter(
			"delta",
			() -> {
				String delta = ParamUtil.getString(
					cpRequestHelper.getRenderRequest(), "delta");

				if (Validator.isNotNull(delta)) {
					return delta;
				}

				return null;
			}
		).buildPortletURL();
	}

	public CreationMenu getWarehouseCreationMenu() {
		CreationMenu creationMenu = new CreationMenu();

		if (hasManageCommerceInventoryWarehousePermission()) {
			creationMenu.addDropdownItem(
				dropdownItem -> {
					dropdownItem.setHref(getAddCommerceWarehouseRenderURL());
					dropdownItem.setLabel(
						LanguageUtil.get(
							cpRequestHelper.getRequest(), "add-warehouse"));
					dropdownItem.setTarget("modal");
				});
		}

		return creationMenu;
	}

	public List<FDSActionDropdownItem> getWarehouseFDSActionDropdownItems()
		throws PortalException {

		return ListUtil.fromArray(
			new FDSActionDropdownItem(
				PortletURLBuilder.create(
					PortletProviderUtil.getPortletURL(
						cpRequestHelper.getRequest(),
						CommerceInventoryWarehouse.class.getName(),
						PortletProvider.Action.MANAGE)
				).setMVCRenderCommandName(
					"/commerce_inventory_warehouse" +
						"/edit_commerce_inventory_warehouse"
				).setRedirect(
					cpRequestHelper.getCurrentURL()
				).setParameter(
					"commerceInventoryWarehouseId", "{id}"
				).setParameter(
					"screenNavigationCategoryKey", "details"
				).buildString(),
				"pencil", "edit",
				LanguageUtil.get(cpRequestHelper.getRequest(), "edit"), "get",
				null, null),
			new FDSActionDropdownItem(
				null, "trash", "delete",
				LanguageUtil.get(cpRequestHelper.getRequest(), "delete"),
				"delete", "delete", "headless"),
			new FDSActionDropdownItem(
				_getManageWarehousePermissionsURL(), null, "permissions",
				LanguageUtil.get(cpRequestHelper.getRequest(), "permissions"),
				"get", "permissions", "modal-permissions"));
	}

	public boolean hasManageCommerceInventoryWarehousePermission() {
		return true;
	}

	public boolean hasPermission(String actionId) throws PortalException {
		CommerceInventoryWarehouse commerceInventoryWarehouse =
			getCommerceInventoryWarehouse();

		return _commerceInventoryWarehouseModelResourcePermission.contains(
			cpRequestHelper.getPermissionChecker(),
			commerceInventoryWarehouse.getCommerceInventoryWarehouseId(),
			actionId);
	}

	protected CommerceChannelRelService commerceChannelRelService;
	protected final CPRequestHelper cpRequestHelper;
	protected HttpServletRequest httpServletRequest;

	private String _getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(
			cpRequestHelper.getRenderRequest(), "keywords");

		return _keywords;
	}

	private String _getManageWarehousePermissionsURL() throws PortalException {
		PortletURL portletURL = PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				cpRequestHelper.getRequest(),
				"com_liferay_portlet_configuration_web_portlet_" +
					"PortletConfigurationPortlet",
				ActionRequest.RENDER_PHASE)
		).setMVCPath(
			"/edit_permissions.jsp"
		).setRedirect(
			cpRequestHelper.getCurrentURL()
		).setParameter(
			"modelResource", CommerceInventoryWarehouse.class.getName()
		).setParameter(
			"modelResourceDescription", "{name}"
		).setParameter(
			"resourcePrimKey", "{id}"
		).buildPortletURL();

		try {
			portletURL.setWindowState(LiferayWindowState.POP_UP);
		}
		catch (WindowStateException windowStateException) {
			throw new PortalException(windowStateException);
		}

		return portletURL.toString();
	}

	private String _getNavigation() {
		return ParamUtil.getString(
			cpRequestHelper.getRenderRequest(), "navigation");
	}

	private CommerceInventoryWarehouse _commerceInventoryWarehouse;
	private final ModelResourcePermission<CommerceInventoryWarehouse>
		_commerceInventoryWarehouseModelResourcePermission;
	private final CommerceInventoryWarehouseService
		_commerceInventoryWarehouseService;
	private String _keywords;
	private final Portal _portal;

}