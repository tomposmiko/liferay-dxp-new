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

package com.liferay.commerce.discount.service.impl;

import com.liferay.commerce.discount.exception.CommerceDiscountRuleTypeException;
import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.discount.model.CommerceDiscountRule;
import com.liferay.commerce.discount.rule.type.CommerceDiscountRuleType;
import com.liferay.commerce.discount.rule.type.CommerceDiscountRuleTypeRegistry;
import com.liferay.commerce.discount.service.base.CommerceDiscountRuleLocalServiceBaseImpl;
import com.liferay.commerce.discount.service.persistence.CommerceDiscountPersistence;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false,
	property = "model.class.name=com.liferay.commerce.discount.model.CommerceDiscountRule",
	service = AopService.class
)
public class CommerceDiscountRuleLocalServiceImpl
	extends CommerceDiscountRuleLocalServiceBaseImpl {

	@Override
	public CommerceDiscountRule addCommerceDiscountRule(
			long commerceDiscountId, String type, String typeSettings,
			ServiceContext serviceContext)
		throws PortalException {

		return commerceDiscountRuleLocalService.addCommerceDiscountRule(
			commerceDiscountId, type, type, typeSettings, serviceContext);
	}

	@Override
	public CommerceDiscountRule addCommerceDiscountRule(
			long commerceDiscountId, String name, String type,
			String typeSettings, ServiceContext serviceContext)
		throws PortalException {

		// Commerce discount rule

		User user = _userLocalService.getUser(serviceContext.getUserId());

		_validate(type);

		long commerceDiscountRuleId = counterLocalService.increment();

		CommerceDiscountRule commerceDiscountRule =
			commerceDiscountRulePersistence.create(commerceDiscountRuleId);

		commerceDiscountRule.setCompanyId(user.getCompanyId());
		commerceDiscountRule.setUserId(user.getUserId());
		commerceDiscountRule.setUserName(user.getFullName());
		commerceDiscountRule.setName(name);
		commerceDiscountRule.setCommerceDiscountId(commerceDiscountId);
		commerceDiscountRule.setType(type);

		UnicodeProperties settingsUnicodeProperties =
			commerceDiscountRule.getSettingsProperties();

		settingsUnicodeProperties.put(type, typeSettings);

		commerceDiscountRule.setSettingsProperties(settingsUnicodeProperties);

		commerceDiscountRule = commerceDiscountRulePersistence.update(
			commerceDiscountRule);

		// Commerce discount

		_reindexCommerceDiscount(commerceDiscountId);

		return commerceDiscountRule;
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public CommerceDiscountRule deleteCommerceDiscountRule(
			CommerceDiscountRule commerceDiscountRule)
		throws PortalException {

		// Commerce discount rule

		commerceDiscountRulePersistence.remove(commerceDiscountRule);

		// Commerce discount

		_reindexCommerceDiscount(commerceDiscountRule.getCommerceDiscountId());

		return commerceDiscountRule;
	}

	@Override
	public CommerceDiscountRule deleteCommerceDiscountRule(
			long commerceDiscountRuleId)
		throws PortalException {

		CommerceDiscountRule commerceDiscountRule =
			commerceDiscountRulePersistence.findByPrimaryKey(
				commerceDiscountRuleId);

		return commerceDiscountRuleLocalService.deleteCommerceDiscountRule(
			commerceDiscountRule);
	}

	@Override
	public void deleteCommerceDiscountRules(long commerceDiscountId)
		throws PortalException {

		List<CommerceDiscountRule> commerceDiscountRules =
			commerceDiscountRulePersistence.findByCommerceDiscountId(
				commerceDiscountId);

		for (CommerceDiscountRule commerceDiscountRule :
				commerceDiscountRules) {

			commerceDiscountRuleLocalService.deleteCommerceDiscountRule(
				commerceDiscountRule);
		}
	}

	@Override
	public List<CommerceDiscountRule> getCommerceDiscountRules(
		long commerceDiscountId, int start, int end,
		OrderByComparator<CommerceDiscountRule> orderByComparator) {

		return commerceDiscountRulePersistence.findByCommerceDiscountId(
			commerceDiscountId, start, end, orderByComparator);
	}

	@Override
	public List<CommerceDiscountRule> getCommerceDiscountRules(
			long commerceDiscountId, String name, int start, int end)
		throws PortalException {

		return commerceDiscountRuleFinder.findByCommerceDiscountId(
			commerceDiscountId, name, start, end);
	}

	@Override
	public int getCommerceDiscountRulesCount(long commerceDiscountId) {
		return commerceDiscountRulePersistence.countByCommerceDiscountId(
			commerceDiscountId);
	}

	@Override
	public int getCommerceDiscountRulesCount(
			long commerceDiscountId, String name)
		throws PortalException {

		return commerceDiscountRuleFinder.countByCommerceDiscountId(
			commerceDiscountId, name);
	}

	@Override
	public CommerceDiscountRule updateCommerceDiscountRule(
			long commerceDiscountRuleId, String type, String typeSettings)
		throws PortalException {

		// Commerce discount rule

		CommerceDiscountRule commerceDiscountRule =
			commerceDiscountRulePersistence.findByPrimaryKey(
				commerceDiscountRuleId);

		_validate(type);

		commerceDiscountRule.setType(type);

		UnicodeProperties unicodeProperties =
			commerceDiscountRule.getSettingsProperties();

		unicodeProperties.put(type, typeSettings);

		commerceDiscountRule.setSettingsProperties(unicodeProperties);

		commerceDiscountRule = commerceDiscountRulePersistence.update(
			commerceDiscountRule);

		// Commerce discount

		_reindexCommerceDiscount(commerceDiscountRule.getCommerceDiscountId());

		return commerceDiscountRule;
	}

	@Override
	public CommerceDiscountRule updateCommerceDiscountRule(
			long commerceDiscountRuleId, String name, String type,
			String typeSettings)
		throws PortalException {

		// Commerce discount rule

		CommerceDiscountRule commerceDiscountRule =
			commerceDiscountRulePersistence.findByPrimaryKey(
				commerceDiscountRuleId);

		_validate(type);

		commerceDiscountRule.setName(name);
		commerceDiscountRule.setType(type);

		UnicodeProperties unicodeProperties =
			commerceDiscountRule.getSettingsProperties();

		unicodeProperties.put(type, typeSettings);

		commerceDiscountRule.setSettingsProperties(unicodeProperties);

		commerceDiscountRule = commerceDiscountRulePersistence.update(
			commerceDiscountRule);

		// Commerce discount

		_reindexCommerceDiscount(commerceDiscountRule.getCommerceDiscountId());

		return commerceDiscountRule;
	}

	private void _reindexCommerceDiscount(long commerceDiscountId)
		throws PortalException {

		CommerceDiscount commerceDiscount =
			_commerceDiscountPersistence.findByPrimaryKey(commerceDiscountId);

		Indexer<CommerceDiscount> indexer =
			IndexerRegistryUtil.nullSafeGetIndexer(CommerceDiscount.class);

		indexer.reindex(commerceDiscount);
	}

	private void _validate(String type) throws PortalException {
		CommerceDiscountRuleType commerceDiscountRuleType =
			_commerceDiscountRuleTypeRegistry.getCommerceDiscountRuleType(type);

		if (commerceDiscountRuleType == null) {
			throw new CommerceDiscountRuleTypeException();
		}
	}

	@Reference
	private CommerceDiscountPersistence _commerceDiscountPersistence;

	@Reference
	private CommerceDiscountRuleTypeRegistry _commerceDiscountRuleTypeRegistry;

	@Reference
	private UserLocalService _userLocalService;

}