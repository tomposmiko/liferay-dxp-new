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

import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.discount.model.CommerceDiscountAccountRel;
import com.liferay.commerce.discount.service.base.CommerceDiscountAccountRelLocalServiceBaseImpl;
import com.liferay.expando.kernel.service.ExpandoRowLocalService;
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

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Alberti
 * @see CommerceDiscountAccountRelLocalServiceBaseImpl
 */
@Component(
	enabled = false,
	property = "model.class.name=com.liferay.commerce.discount.model.CommerceDiscountAccountRel",
	service = AopService.class
)
public class CommerceDiscountAccountRelLocalServiceImpl
	extends CommerceDiscountAccountRelLocalServiceBaseImpl {

	@Override
	public CommerceDiscountAccountRel addCommerceDiscountAccountRel(
			long userId, long commerceDiscountId, long commerceAccountId,
			ServiceContext serviceContext)
		throws PortalException {

		User user = _userLocalService.getUser(userId);

		long commerceDiscountAccountRelId = counterLocalService.increment();

		CommerceDiscountAccountRel commerceDiscountAccountRel =
			commerceDiscountAccountRelPersistence.create(
				commerceDiscountAccountRelId);

		commerceDiscountAccountRel.setCompanyId(user.getCompanyId());
		commerceDiscountAccountRel.setUserId(user.getUserId());
		commerceDiscountAccountRel.setUserName(user.getFullName());
		commerceDiscountAccountRel.setCommerceAccountId(commerceAccountId);
		commerceDiscountAccountRel.setCommerceDiscountId(commerceDiscountId);

		commerceDiscountAccountRel =
			commerceDiscountAccountRelPersistence.update(
				commerceDiscountAccountRel);

		_reindexCommerceDiscount(commerceDiscountId);

		return commerceDiscountAccountRel;
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public CommerceDiscountAccountRel deleteCommerceDiscountAccountRel(
			CommerceDiscountAccountRel commerceDiscountAccountRel)
		throws PortalException {

		commerceDiscountAccountRelPersistence.remove(
			commerceDiscountAccountRel);

		_expandoRowLocalService.deleteRows(
			commerceDiscountAccountRel.getCommerceDiscountAccountRelId());

		_reindexCommerceDiscount(
			commerceDiscountAccountRel.getCommerceDiscountId());

		return commerceDiscountAccountRel;
	}

	@Override
	public CommerceDiscountAccountRel deleteCommerceDiscountAccountRel(
			long commerceDiscountAccountRelId)
		throws PortalException {

		CommerceDiscountAccountRel commerceDiscountAccountRel =
			commerceDiscountAccountRelPersistence.findByPrimaryKey(
				commerceDiscountAccountRelId);

		return commerceDiscountAccountRelLocalService.
			deleteCommerceDiscountAccountRel(commerceDiscountAccountRel);
	}

	/**
	 * @deprecated As of Athanasius (7.3.x)
	 */
	@Deprecated
	@Override
	public void deleteCommerceDiscountAccountRelsBycommerceAccountId(
		long commerceAccountId) {

		commerceDiscountAccountRelPersistence.removeByCommerceAccountId(
			commerceAccountId);
	}

	@Override
	public void deleteCommerceDiscountAccountRelsByCommerceDiscountId(
			long commerceDiscountId)
		throws PortalException {

		List<CommerceDiscountAccountRel> commerceDiscountAccountRels =
			commerceDiscountAccountRelPersistence.findByCommerceDiscountId(
				commerceDiscountId);

		for (CommerceDiscountAccountRel commerceDiscountAccountRel :
				commerceDiscountAccountRels) {

			commerceDiscountAccountRelLocalService.
				deleteCommerceDiscountAccountRel(commerceDiscountAccountRel);
		}
	}

	@Override
	public CommerceDiscountAccountRel fetchCommerceDiscountAccountRel(
		long commerceAccountId, long commerceDiscountId) {

		return commerceDiscountAccountRelPersistence.fetchByCAI_CDI(
			commerceAccountId, commerceDiscountId);
	}

	@Override
	public List<CommerceDiscountAccountRel> getCommerceDiscountAccountRels(
		long commerceDiscountId, int start, int end,
		OrderByComparator<CommerceDiscountAccountRel> orderByComparator) {

		return commerceDiscountAccountRelPersistence.findByCommerceDiscountId(
			commerceDiscountId, start, end, orderByComparator);
	}

	@Override
	public List<CommerceDiscountAccountRel> getCommerceDiscountAccountRels(
		long commerceDiscountId, String name, int start, int end) {

		return commerceDiscountAccountRelFinder.findByCommerceDiscountId(
			commerceDiscountId, name, start, end);
	}

	@Override
	public int getCommerceDiscountAccountRelsCount(long commerceDiscountId) {
		return commerceDiscountAccountRelPersistence.countByCommerceDiscountId(
			commerceDiscountId);
	}

	@Override
	public int getCommerceDiscountAccountRelsCount(
		long commerceDiscountId, String name) {

		return commerceDiscountAccountRelFinder.countByCommerceDiscountId(
			commerceDiscountId, name);
	}

	private void _reindexCommerceDiscount(long commerceDiscountId)
		throws PortalException {

		Indexer<CommerceDiscount> indexer =
			IndexerRegistryUtil.nullSafeGetIndexer(CommerceDiscount.class);

		indexer.reindex(CommerceDiscount.class.getName(), commerceDiscountId);
	}

	@Reference
	private ExpandoRowLocalService _expandoRowLocalService;

	@Reference
	private UserLocalService _userLocalService;

}