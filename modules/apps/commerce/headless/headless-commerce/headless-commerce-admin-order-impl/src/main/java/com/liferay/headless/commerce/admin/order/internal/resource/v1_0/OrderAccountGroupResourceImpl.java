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

package com.liferay.headless.commerce.admin.order.internal.resource.v1_0;

import com.liferay.account.model.AccountGroup;
import com.liferay.account.service.AccountGroupService;
import com.liferay.commerce.order.rule.model.COREntryRel;
import com.liferay.commerce.order.rule.service.COREntryRelService;
import com.liferay.headless.commerce.admin.order.dto.v1_0.OrderAccountGroup;
import com.liferay.headless.commerce.admin.order.dto.v1_0.OrderRuleAccountGroup;
import com.liferay.headless.commerce.admin.order.resource.v1_0.OrderAccountGroupResource;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.fields.NestedField;
import com.liferay.portal.vulcan.fields.NestedFieldSupport;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/order-account-group.properties",
	scope = ServiceScope.PROTOTYPE,
	service = {NestedFieldSupport.class, OrderAccountGroupResource.class}
)
public class OrderAccountGroupResourceImpl
	extends BaseOrderAccountGroupResourceImpl implements NestedFieldSupport {

	@NestedField(
		parentClass = OrderRuleAccountGroup.class, value = "accountGroup"
	)
	@Override
	public OrderAccountGroup getOrderRuleAccountGroupAccountGroup(Long id)
		throws Exception {

		COREntryRel corEntryRel = _corEntryRelService.getCOREntryRel(id);

		AccountGroup accountGroup = _accountGroupService.getAccountGroup(
			corEntryRel.getClassPK());

		return _toAccountGroup(accountGroup.getAccountGroupId());
	}

	private OrderAccountGroup _toAccountGroup(long accountGroupId)
		throws Exception {

		return _orderAccountGroupDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				accountGroupId, contextAcceptLanguage.getPreferredLocale()));
	}

	@Reference
	private AccountGroupService _accountGroupService;

	@Reference
	private COREntryRelService _corEntryRelService;

	@Reference(
		target = "(component.name=com.liferay.headless.commerce.admin.order.internal.dto.v1_0.converter.OrderAccountGroupDTOConverter)"
	)
	private DTOConverter<AccountGroup, OrderAccountGroup>
		_orderAccountGroupDTOConverter;

}