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

package com.liferay.commerce.order.content.web.internal.importer.type;

import com.liferay.commerce.configuration.CommerceOrderImporterTypeConfiguration;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.exception.CommerceOrderImporterTypeException;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.content.web.internal.importer.type.util.CommerceOrderImporterTypeUtil;
import com.liferay.commerce.order.importer.item.CommerceOrderImporterItem;
import com.liferay.commerce.order.importer.type.CommerceOrderImporterType;
import com.liferay.commerce.price.CommerceOrderPriceCalculation;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.io.IOException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	configurationPid = "com.liferay.commerce.configuration.CommerceOrderImporterTypeConfiguration",
	enabled = false, immediate = true,
	property = "commerce.order.importer.type.key=" + CommerceOrdersCommerceOrderImporterTypeImpl.KEY,
	service = CommerceOrderImporterType.class
)
public class CommerceOrdersCommerceOrderImporterTypeImpl
	implements CommerceOrderImporterType {

	public static final String KEY = "orders";

	@Override
	public Object getCommerceOrderImporterItem(
			HttpServletRequest httpServletRequest)
		throws PortalException {

		long selectedCommerceOrderId = ParamUtil.getLong(
			httpServletRequest, getCommerceOrderImporterItemParamName());

		if (selectedCommerceOrderId > 0) {
			return _commerceOrderService.getCommerceOrder(
				selectedCommerceOrderId);
		}

		return null;
	}

	@Override
	public String getCommerceOrderImporterItemParamName() {
		return "selectedCommerceOrderId";
	}

	@Override
	public List<CommerceOrderImporterItem> getCommerceOrderImporterItems(
			CommerceOrder commerceOrder, Object object)
		throws Exception {

		if ((object == null) || !(object instanceof CommerceOrder)) {
			throw new CommerceOrderImporterTypeException();
		}

		CommerceOrder selectedCommerceOrder = (CommerceOrder)object;

		return CommerceOrderImporterTypeUtil.getCommerceOrderImporterItems(
			_commerceContextFactory, commerceOrder,
			selectedCommerceOrder.getCommerceOrderItems(),
			_commerceOrderItemService, _commerceOrderPriceCalculation,
			_commerceOrderService, _userLocalService);
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.format(resourceBundle, "import-from-x", KEY);
	}

	@Override
	public boolean isActive(CommerceOrder commerceOrder)
		throws PortalException {

		return _commerceOrderImporterTypeConfiguration.enabled();
	}

	@Override
	public void render(
			CommerceOrder commerceOrder, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		_jspRenderer.renderJSP(
			httpServletRequest, httpServletResponse,
			"/pending_commerce_orders/importer_type/commerce_orders.jsp");
	}

	@Override
	public void renderCommerceOrderPreview(
			CommerceOrder commerceOrder, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		_jspRenderer.renderJSP(
			httpServletRequest, httpServletResponse,
			"/pending_commerce_orders/importer_type/common/preview.jsp");
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_commerceOrderImporterTypeConfiguration =
			ConfigurableUtil.createConfigurable(
				CommerceOrderImporterTypeConfiguration.class, properties);
	}

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	private volatile CommerceOrderImporterTypeConfiguration
		_commerceOrderImporterTypeConfiguration;

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

	@Reference
	private CommerceOrderPriceCalculation _commerceOrderPriceCalculation;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private UserLocalService _userLocalService;

}