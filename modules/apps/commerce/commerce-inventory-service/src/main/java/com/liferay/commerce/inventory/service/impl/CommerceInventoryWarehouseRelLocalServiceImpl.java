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

package com.liferay.commerce.inventory.service.impl;

import com.liferay.commerce.inventory.exception.DuplicateCommerceInventoryWarehouseRelException;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouseRel;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouseRelTable;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouseTable;
import com.liferay.commerce.inventory.service.base.CommerceInventoryWarehouseRelLocalServiceBaseImpl;
import com.liferay.commerce.model.CommerceOrderType;
import com.liferay.commerce.model.CommerceOrderTypeTable;
import com.liferay.petra.sql.dsl.DSLFunctionFactoryUtil;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.petra.sql.dsl.Table;
import com.liferay.petra.sql.dsl.expression.Expression;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.petra.sql.dsl.query.FromStep;
import com.liferay.petra.sql.dsl.query.GroupByStep;
import com.liferay.petra.sql.dsl.query.JoinStep;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 */
@Component(
	property = "model.class.name=com.liferay.commerce.inventory.model.CommerceInventoryWarehouseRel",
	service = AopService.class
)
public class CommerceInventoryWarehouseRelLocalServiceImpl
	extends CommerceInventoryWarehouseRelLocalServiceBaseImpl {

	@Override
	public CommerceInventoryWarehouseRel addCommerceInventoryWarehouseRel(
			long userId, String className, long classPK,
			long commerceInventoryWarehouseId)
		throws PortalException {

		long classNameId = _classNameLocalService.getClassNameId(className);

		_validate(classNameId, classPK, commerceInventoryWarehouseId);

		CommerceInventoryWarehouseRel commerceInventoryWarehouseRel =
			commerceInventoryWarehouseRelPersistence.create(
				counterLocalService.increment());

		User user = _userLocalService.getUser(userId);

		commerceInventoryWarehouseRel.setCompanyId(user.getCompanyId());
		commerceInventoryWarehouseRel.setUserId(user.getUserId());
		commerceInventoryWarehouseRel.setUserName(user.getFullName());

		commerceInventoryWarehouseRel.setClassNameId(classNameId);
		commerceInventoryWarehouseRel.setClassPK(classPK);
		commerceInventoryWarehouseRel.setCommerceInventoryWarehouseId(
			commerceInventoryWarehouseId);

		commerceInventoryWarehouseRel =
			commerceInventoryWarehouseRelPersistence.update(
				commerceInventoryWarehouseRel);

		_reindexCommerceInventoryWarehouse(commerceInventoryWarehouseId);

		return commerceInventoryWarehouseRel;
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public CommerceInventoryWarehouseRel deleteCommerceInventoryWarehouseRel(
			CommerceInventoryWarehouseRel commerceInventoryWarehouseRel)
		throws PortalException {

		commerceInventoryWarehouseRelPersistence.remove(
			commerceInventoryWarehouseRel);

		_reindexCommerceInventoryWarehouse(
			commerceInventoryWarehouseRel.getCommerceInventoryWarehouseId());

		return commerceInventoryWarehouseRel;
	}

	@Override
	public CommerceInventoryWarehouseRel deleteCommerceInventoryWarehouseRel(
			long commerceInventoryWarehouseRelId)
		throws PortalException {

		CommerceInventoryWarehouseRel commerceInventoryWarehouseRel =
			commerceInventoryWarehouseRelPersistence.findByPrimaryKey(
				commerceInventoryWarehouseRelId);

		return commerceInventoryWarehouseRelLocalService.
			deleteCommerceInventoryWarehouseRel(commerceInventoryWarehouseRel);
	}

	@Override
	public void deleteCommerceInventoryWarehouseRels(
			long commerceInventoryWarehouseId)
		throws PortalException {

		List<CommerceInventoryWarehouseRel> commerceInventoryWarehouseRels =
			commerceInventoryWarehouseRelPersistence.
				findByCommerceInventoryWarehouseId(
					commerceInventoryWarehouseId);

		for (CommerceInventoryWarehouseRel commerceInventoryWarehouseRel :
				commerceInventoryWarehouseRels) {

			commerceInventoryWarehouseRelLocalService.
				deleteCommerceInventoryWarehouseRel(
					commerceInventoryWarehouseRel);
		}
	}

	@Override
	public void deleteCommerceInventoryWarehouseRels(
			String className, long commerceInventoryWarehouseId)
		throws PortalException {

		List<CommerceInventoryWarehouseRel> commerceInventoryWarehouseRels =
			commerceInventoryWarehouseRelPersistence.findByC_C(
				_classNameLocalService.getClassNameId(className),
				commerceInventoryWarehouseId);

		for (CommerceInventoryWarehouseRel commerceInventoryWarehouseRel :
				commerceInventoryWarehouseRels) {

			commerceInventoryWarehouseRelLocalService.
				deleteCommerceInventoryWarehouseRel(
					commerceInventoryWarehouseRel);
		}
	}

	@Override
	public CommerceInventoryWarehouseRel fetchCommerceInventoryWarehouseRel(
		String className, long classPK, long commerceInventoryWarehouseId) {

		return commerceInventoryWarehouseRelPersistence.fetchByC_C_CIWI(
			_classNameLocalService.getClassNameId(className), classPK,
			commerceInventoryWarehouseId);
	}

	@Override
	public List<CommerceInventoryWarehouseRel>
		getCommerceInventoryWarehouseRels(long commerceInventoryWarehouseId) {

		return commerceInventoryWarehouseRelPersistence.
			findByCommerceInventoryWarehouseId(commerceInventoryWarehouseId);
	}

	@Override
	public List<CommerceInventoryWarehouseRel>
		getCommerceInventoryWarehouseRels(
			long commerceInventoryWarehouseId, int start, int end,
			OrderByComparator<CommerceInventoryWarehouseRel>
				orderByComparator) {

		return commerceInventoryWarehouseRelPersistence.
			findByCommerceInventoryWarehouseId(
				commerceInventoryWarehouseId, start, end, orderByComparator);
	}

	@Override
	public int getCommerceInventoryWarehouseRelsCount(
		long commerceInventoryWarehouseId) {

		return commerceInventoryWarehouseRelPersistence.
			countByCommerceInventoryWarehouseId(commerceInventoryWarehouseId);
	}

	@Override
	public List<CommerceInventoryWarehouseRel>
		getCommerceOrderTypeCommerceInventoryWarehouseRels(
			long commerceInventoryWarehouseId, String keywords, int start,
			int end) {

		return dslQuery(
			_getGroupByStep(
				DSLQueryFactoryUtil.selectDistinct(
					CommerceInventoryWarehouseRelTable.INSTANCE),
				CommerceOrderTypeTable.INSTANCE,
				CommerceOrderTypeTable.INSTANCE.commerceOrderTypeId.eq(
					CommerceInventoryWarehouseRelTable.INSTANCE.classPK),
				commerceInventoryWarehouseId, CommerceOrderType.class.getName(),
				keywords, CommerceOrderTypeTable.INSTANCE.name
			).limit(
				start, end
			));
	}

	@Override
	public int getCommerceOrderTypeCommerceInventoryWarehouseRelsCount(
		long commerceInventoryWarehouseId, String keywords) {

		return dslQueryCount(
			_getGroupByStep(
				DSLQueryFactoryUtil.countDistinct(
					CommerceInventoryWarehouseRelTable.INSTANCE.
						commerceInventoryWarehouseRelId),
				CommerceOrderTypeTable.INSTANCE,
				CommerceOrderTypeTable.INSTANCE.commerceOrderTypeId.eq(
					CommerceInventoryWarehouseRelTable.INSTANCE.classPK),
				commerceInventoryWarehouseId, CommerceOrderType.class.getName(),
				keywords, CommerceOrderTypeTable.INSTANCE.name));
	}

	private GroupByStep _getGroupByStep(
		FromStep fromStep, Table innerJoinTable, Predicate innerJoinPredicate,
		Long commerceInventoryWarehouseId, String className, String keywords,
		Expression<String> keywordsPredicateExpression) {

		JoinStep joinStep = fromStep.from(
			CommerceInventoryWarehouseRelTable.INSTANCE
		).innerJoinON(
			CommerceInventoryWarehouseTable.INSTANCE,
			CommerceInventoryWarehouseTable.INSTANCE.
				commerceInventoryWarehouseId.eq(
					CommerceInventoryWarehouseRelTable.INSTANCE.
						commerceInventoryWarehouseId)
		).innerJoinON(
			innerJoinTable, innerJoinPredicate
		);

		return joinStep.where(
			() ->
				CommerceInventoryWarehouseRelTable.INSTANCE.
					commerceInventoryWarehouseId.eq(
						commerceInventoryWarehouseId
					).and(
						CommerceInventoryWarehouseRelTable.INSTANCE.classNameId.
							eq(_classNameLocalService.getClassNameId(className))
					).and(
						() -> {
							if (Validator.isNotNull(keywords)) {
								return Predicate.withParentheses(
									_customSQL.getKeywordsPredicate(
										DSLFunctionFactoryUtil.lower(
											keywordsPredicateExpression),
										_customSQL.keywords(keywords, true)));
							}

							return null;
						}
					));
	}

	private void _reindexCommerceInventoryWarehouse(
			long commerceInventoryWarehouseId)
		throws PortalException {

		Indexer<CommerceInventoryWarehouse> indexer =
			IndexerRegistryUtil.nullSafeGetIndexer(
				CommerceInventoryWarehouse.class);

		indexer.reindex(
			CommerceInventoryWarehouse.class.getName(),
			commerceInventoryWarehouseId);
	}

	private void _validate(
			long classNameId, long classPK, long commerceInventoryWarehouseId)
		throws PortalException {

		CommerceInventoryWarehouseRel commerceInventoryWarehouseRel =
			commerceInventoryWarehouseRelPersistence.fetchByC_C_CIWI(
				classNameId, classPK, commerceInventoryWarehouseId);

		if (commerceInventoryWarehouseRel != null) {
			throw new DuplicateCommerceInventoryWarehouseRelException();
		}
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CustomSQL _customSQL;

	@Reference
	private UserLocalService _userLocalService;

}