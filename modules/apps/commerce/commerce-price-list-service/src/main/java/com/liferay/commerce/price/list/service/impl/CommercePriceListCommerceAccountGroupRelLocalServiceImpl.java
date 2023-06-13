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

package com.liferay.commerce.price.list.service.impl;

import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.model.CommercePriceListCommerceAccountGroupRel;
import com.liferay.commerce.price.list.service.base.CommercePriceListCommerceAccountGroupRelLocalServiceBaseImpl;
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
 * @author Alessio Antonio Rendina
 */
@Component(
	property = "model.class.name=com.liferay.commerce.price.list.model.CommercePriceListCommerceAccountGroupRel",
	service = AopService.class
)
public class CommercePriceListCommerceAccountGroupRelLocalServiceImpl
	extends CommercePriceListCommerceAccountGroupRelLocalServiceBaseImpl {

	@Override
	public CommercePriceListCommerceAccountGroupRel
			addCommercePriceListCommerceAccountGroupRel(
				long userId, long commercePriceListId,
				long commerceAccountGroupId, int order,
				ServiceContext serviceContext)
		throws PortalException {

		User user = _userLocalService.getUser(userId);

		long commercePriceListCommerceAccountGroupRelId =
			counterLocalService.increment();

		CommercePriceListCommerceAccountGroupRel
			commercePriceListCommerceAccountGroupRel =
				commercePriceListCommerceAccountGroupRelPersistence.create(
					commercePriceListCommerceAccountGroupRelId);

		commercePriceListCommerceAccountGroupRel.setUuid(
			serviceContext.getUuid());
		commercePriceListCommerceAccountGroupRel.setCompanyId(
			user.getCompanyId());
		commercePriceListCommerceAccountGroupRel.setUserId(user.getUserId());
		commercePriceListCommerceAccountGroupRel.setUserName(
			user.getFullName());
		commercePriceListCommerceAccountGroupRel.setCommercePriceListId(
			commercePriceListId);
		commercePriceListCommerceAccountGroupRel.setCommerceAccountGroupId(
			commerceAccountGroupId);
		commercePriceListCommerceAccountGroupRel.setOrder(order);
		commercePriceListCommerceAccountGroupRel.setExpandoBridgeAttributes(
			serviceContext);

		commercePriceListCommerceAccountGroupRel =
			commercePriceListCommerceAccountGroupRelPersistence.update(
				commercePriceListCommerceAccountGroupRel);

		_reindexPriceList(commercePriceListId);

		return commercePriceListCommerceAccountGroupRel;
	}

	@Override
	public void deleteCommercePriceListAccountGroupRelsByCommercePriceListId(
		long commercePriceListId) {

		commercePriceListCommerceAccountGroupRelPersistence.
			removeByCommercePriceListId(commercePriceListId);
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public CommercePriceListCommerceAccountGroupRel
			deleteCommercePriceListCommerceAccountGroupRel(
				CommercePriceListCommerceAccountGroupRel
					commercePriceListCommerceAccountGroupRel)
		throws PortalException {

		commercePriceListCommerceAccountGroupRelPersistence.remove(
			commercePriceListCommerceAccountGroupRel);

		_expandoRowLocalService.deleteRows(
			commercePriceListCommerceAccountGroupRel.
				getCommercePriceListCommerceAccountGroupRelId());

		_reindexPriceList(
			commercePriceListCommerceAccountGroupRel.getCommercePriceListId());

		return commercePriceListCommerceAccountGroupRel;
	}

	@Override
	public CommercePriceListCommerceAccountGroupRel
			deleteCommercePriceListCommerceAccountGroupRel(
				long commercePriceListCommerceAccountGroupRelId)
		throws PortalException {

		CommercePriceListCommerceAccountGroupRel
			commercePriceListCommerceAccountGroupRel =
				commercePriceListCommerceAccountGroupRelPersistence.
					findByPrimaryKey(
						commercePriceListCommerceAccountGroupRelId);

		return commercePriceListCommerceAccountGroupRelLocalService.
			deleteCommercePriceListCommerceAccountGroupRel(
				commercePriceListCommerceAccountGroupRel);
	}

	@Override
	public void deleteCommercePriceListCommerceAccountGroupRels(
			long commercePriceListId)
		throws PortalException {

		List<CommercePriceListCommerceAccountGroupRel>
			commercePriceListCommerceAccountGroupRels =
				commercePriceListCommerceAccountGroupRelPersistence.
					findByCommercePriceListId(commercePriceListId);

		for (CommercePriceListCommerceAccountGroupRel
				commercePriceListCommerceAccountGroupRel :
					commercePriceListCommerceAccountGroupRels) {

			commercePriceListCommerceAccountGroupRelLocalService.
				deleteCommercePriceListCommerceAccountGroupRel(
					commercePriceListCommerceAccountGroupRel);
		}
	}

	@Override
	public CommercePriceListCommerceAccountGroupRel
		fetchCommercePriceListCommerceAccountGroupRel(
			long commercePriceListId, long commerceAccountGroupId) {

		return commercePriceListCommerceAccountGroupRelPersistence.
			fetchByCAGI_CPI(commercePriceListId, commerceAccountGroupId);
	}

	@Override
	public List<CommercePriceListCommerceAccountGroupRel>
		getCommercePriceListCommerceAccountGroupRels(long commercePriceListId) {

		return commercePriceListCommerceAccountGroupRelPersistence.
			findByCommercePriceListId(commercePriceListId);
	}

	@Override
	public List<CommercePriceListCommerceAccountGroupRel>
		getCommercePriceListCommerceAccountGroupRels(
			long commercePriceListId, int start, int end,
			OrderByComparator<CommercePriceListCommerceAccountGroupRel>
				orderByComparator) {

		return commercePriceListCommerceAccountGroupRelPersistence.
			findByCommercePriceListId(
				commercePriceListId, start, end, orderByComparator);
	}

	@Override
	public List<CommercePriceListCommerceAccountGroupRel>
		getCommercePriceListCommerceAccountGroupRels(
			long commercePriceListId, String name, int start, int end) {

		return commercePriceListCommerceAccountGroupRelFinder.
			findByCommercePriceListId(commercePriceListId, name, start, end);
	}

	@Override
	public int getCommercePriceListCommerceAccountGroupRelsCount(
		long commercePriceListId) {

		return commercePriceListCommerceAccountGroupRelPersistence.
			countByCommercePriceListId(commercePriceListId);
	}

	@Override
	public int getCommercePriceListCommerceAccountGroupRelsCount(
		long commercePriceListId, String name) {

		return commercePriceListCommerceAccountGroupRelFinder.
			countByCommercePriceListId(commercePriceListId, name);
	}

	@Override
	public CommercePriceListCommerceAccountGroupRel
			updateCommercePriceListCommerceAccountGroupRel(
				long commercePriceListCommerceAccountGroupRelId, int order,
				ServiceContext serviceContext)
		throws PortalException {

		CommercePriceListCommerceAccountGroupRel
			commercePriceListCommerceAccountGroupRel =
				commercePriceListCommerceAccountGroupRelPersistence.
					findByPrimaryKey(
						commercePriceListCommerceAccountGroupRelId);

		commercePriceListCommerceAccountGroupRel.setOrder(order);
		commercePriceListCommerceAccountGroupRel.setExpandoBridgeAttributes(
			serviceContext);

		// Commerce price list

		_reindexPriceList(
			commercePriceListCommerceAccountGroupRel.getCommercePriceListId());

		return commercePriceListCommerceAccountGroupRelPersistence.update(
			commercePriceListCommerceAccountGroupRel);
	}

	private void _reindexPriceList(long commercePriceListId)
		throws PortalException {

		Indexer<CommercePriceList> indexer =
			IndexerRegistryUtil.nullSafeGetIndexer(CommercePriceList.class);

		indexer.reindex(CommercePriceList.class.getName(), commercePriceListId);
	}

	@Reference
	private ExpandoRowLocalService _expandoRowLocalService;

	@Reference
	private UserLocalService _userLocalService;

}