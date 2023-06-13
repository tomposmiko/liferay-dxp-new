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

package com.liferay.change.tracking.service.impl;

import com.liferay.change.tracking.constants.CTActionKeys;
import com.liferay.change.tracking.constants.CTConstants;
import com.liferay.change.tracking.model.CTAutoResolutionInfo;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.model.CTCollectionTable;
import com.liferay.change.tracking.service.CTProcessLocalService;
import com.liferay.change.tracking.service.base.CTCollectionServiceBaseImpl;
import com.liferay.change.tracking.service.persistence.CTAutoResolutionInfoPersistence;
import com.liferay.petra.sql.dsl.DSLFunctionFactoryUtil;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.dao.orm.WildcardMode;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.GroupTable;
import com.liferay.portal.kernel.model.UserGroupRoleTable;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.InlineSQLHelper;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Preston Crary
 */
@Component(
	property = {
		"json.web.service.context.name=ct",
		"json.web.service.context.path=CTCollection"
	},
	service = AopService.class
)
public class CTCollectionServiceImpl extends CTCollectionServiceBaseImpl {

	@Override
	public CTCollection addCTCollection(
			long companyId, long userId, String name, String description)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), null, CTActionKeys.ADD_PUBLICATION);

		return ctCollectionLocalService.addCTCollection(
			companyId, userId, name, description);
	}

	@Override
	public void deleteCTAutoResolutionInfo(long ctAutoResolutionInfoId)
		throws PortalException {

		CTAutoResolutionInfo ctAutoResolutionInfo =
			_ctAutoResolutionInfoPersistence.fetchByPrimaryKey(
				ctAutoResolutionInfoId);

		if (ctAutoResolutionInfo == null) {
			return;
		}

		_ctCollectionModelResourcePermission.check(
			getPermissionChecker(), ctAutoResolutionInfo.getCtCollectionId(),
			ActionKeys.UPDATE);

		ctCollectionLocalService.deleteCTAutoResolutionInfo(
			ctAutoResolutionInfoId);
	}

	@Override
	public CTCollection deleteCTCollection(CTCollection ctCollection)
		throws PortalException {

		_ctCollectionModelResourcePermission.check(
			getPermissionChecker(), ctCollection, ActionKeys.DELETE);

		return ctCollectionLocalService.deleteCTCollection(ctCollection);
	}

	@Override
	public void discardCTEntries(
			long ctCollectionId, long modelClassNameId, long modelClassPK)
		throws PortalException {

		CTCollection ctCollection = ctCollectionPersistence.findByPrimaryKey(
			ctCollectionId);

		_ctCollectionModelResourcePermission.check(
			getPermissionChecker(), ctCollection, ActionKeys.UPDATE);

		ctCollectionLocalService.discardCTEntries(
			ctCollectionId, modelClassNameId, modelClassPK, false);
	}

	@Override
	public void discardCTEntry(
			long ctCollectionId, long modelClassNameId, long modelClassPK)
		throws PortalException {

		CTCollection ctCollection = ctCollectionPersistence.findByPrimaryKey(
			ctCollectionId);

		_ctCollectionModelResourcePermission.check(
			getPermissionChecker(), ctCollection, ActionKeys.UPDATE);

		ctCollectionLocalService.discardCTEntry(
			ctCollectionId, modelClassNameId, modelClassPK, false);
	}

	@Override
	public List<CTCollection> getCTCollections(
		long companyId, int[] statuses, int start, int end,
		OrderByComparator<CTCollection> orderByComparator) {

		if (statuses == null) {
			return ctCollectionPersistence.filterFindByCompanyId(
				companyId, start, end, orderByComparator);
		}

		return ctCollectionPersistence.filterFindByC_S(
			companyId, statuses, start, end, orderByComparator);
	}

	@Override
	public List<CTCollection> getCTCollections(
		long companyId, int[] statuses, String keywords, int start, int end,
		OrderByComparator<CTCollection> orderByComparator) {

		DSLQuery dslQuery = DSLQueryFactoryUtil.select(
			CTCollectionTable.INSTANCE
		).from(
			CTCollectionTable.INSTANCE
		).where(
			_getPredicate(companyId, statuses, keywords)
		).orderBy(
			CTCollectionTable.INSTANCE, orderByComparator
		).limit(
			start, end
		);

		return ctCollectionPersistence.dslQuery(dslQuery);
	}

	@Override
	public int getCTCollectionsCount(
		long companyId, int[] statuses, String keywords) {

		DSLQuery dslQuery = DSLQueryFactoryUtil.count(
		).from(
			CTCollectionTable.INSTANCE
		).where(
			_getPredicate(companyId, statuses, keywords)
		);

		return ctCollectionPersistence.dslQueryCount(dslQuery);
	}

	@Override
	public void publishCTCollection(long userId, long ctCollectionId)
		throws PortalException {

		_ctCollectionModelResourcePermission.check(
			getPermissionChecker(), ctCollectionId, CTActionKeys.PUBLISH);

		_ctProcessLocalService.addCTProcess(userId, ctCollectionId);
	}

	@Override
	public CTCollection undoCTCollection(
			long ctCollectionId, long userId, String name, String description)
		throws PortalException {

		PermissionChecker permissionChecker = getPermissionChecker();

		_ctCollectionModelResourcePermission.check(
			permissionChecker, ctCollectionId, ActionKeys.VIEW);

		_portletResourcePermission.check(
			permissionChecker, null, CTActionKeys.ADD_PUBLICATION);

		return ctCollectionLocalService.undoCTCollection(
			ctCollectionId, userId, name, description);
	}

	@Override
	public CTCollection updateCTCollection(
			long userId, long ctCollectionId, String name, String description)
		throws PortalException {

		_ctCollectionModelResourcePermission.check(
			getPermissionChecker(), ctCollectionId, ActionKeys.UPDATE);

		return ctCollectionLocalService.updateCTCollection(
			userId, ctCollectionId, name, description);
	}

	private Predicate _getPredicate(
		long companyId, int[] statuses, String keywords) {

		Predicate predicate = CTCollectionTable.INSTANCE.companyId.eq(
			companyId
		).and(
			() -> {
				if (!ArrayUtil.isEmpty(statuses)) {
					return CTCollectionTable.INSTANCE.status.in(
						ArrayUtil.toArray(statuses));
				}

				return null;
			}
		);

		String[] keywordsArray = _customSQL.keywords(
			keywords, true, WildcardMode.SURROUND);

		predicate = predicate.and(
			Predicate.withParentheses(
				Predicate.or(
					_customSQL.getKeywordsPredicate(
						DSLFunctionFactoryUtil.lower(
							CTCollectionTable.INSTANCE.name),
						keywordsArray),
					_customSQL.getKeywordsPredicate(
						DSLFunctionFactoryUtil.lower(
							CTCollectionTable.INSTANCE.description),
						keywordsArray))));

		Predicate permissionWherePredicate =
			_inlineSQLHelper.getPermissionWherePredicate(
				CTCollection.class, CTCollectionTable.INSTANCE.ctCollectionId);

		if (permissionWherePredicate == null) {
			return predicate;
		}

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		return predicate.and(
			permissionWherePredicate.or(
				CTCollectionTable.INSTANCE.ctCollectionId.in(
					DSLQueryFactoryUtil.selectDistinct(
						GroupTable.INSTANCE.classPK
					).from(
						GroupTable.INSTANCE
					).innerJoinON(
						UserGroupRoleTable.INSTANCE,
						UserGroupRoleTable.INSTANCE.groupId.eq(
							GroupTable.INSTANCE.groupId)
					).where(
						GroupTable.INSTANCE.companyId.eq(
							permissionChecker.getCompanyId()
						).and(
							GroupTable.INSTANCE.classNameId.eq(
								_classNameLocalService.getClassNameId(
									CTCollection.class.getName()))
						).and(
							UserGroupRoleTable.INSTANCE.userId.eq(
								permissionChecker.getUserId())
						)
					))
			).withParentheses());
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CTAutoResolutionInfoPersistence _ctAutoResolutionInfoPersistence;

	@Reference(
		target = "(model.class.name=com.liferay.change.tracking.model.CTCollection)"
	)
	private ModelResourcePermission<CTCollection>
		_ctCollectionModelResourcePermission;

	@Reference
	private CTProcessLocalService _ctProcessLocalService;

	@Reference
	private CustomSQL _customSQL;

	@Reference
	private InlineSQLHelper _inlineSQLHelper;

	@Reference(target = "(resource.name=" + CTConstants.RESOURCE_NAME + ")")
	private PortletResourcePermission _portletResourcePermission;

}