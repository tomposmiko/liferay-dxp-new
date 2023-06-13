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
import com.liferay.change.tracking.model.CTCollectionTable;
import com.liferay.change.tracking.model.CTCollectionTemplate;
import com.liferay.change.tracking.model.CTCollectionTemplateTable;
import com.liferay.change.tracking.service.base.CTCollectionTemplateServiceBaseImpl;
import com.liferay.petra.sql.dsl.DSLFunctionFactoryUtil;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.dao.orm.WildcardMode;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = {
		"json.web.service.context.name=ct",
		"json.web.service.context.path=CTCollectionTemplate"
	},
	service = AopService.class
)
public class CTCollectionTemplateServiceImpl
	extends CTCollectionTemplateServiceBaseImpl {

	@Override
	public CTCollectionTemplate addCTCollectionTemplate(
			String name, String description, String json)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), null, CTActionKeys.ADD_PUBLICATION);

		return ctCollectionTemplateLocalService.addCTCollectionTemplate(
			getUserId(), name, description, json);
	}

	public List<CTCollectionTemplate> getCTCollectionTemplates(
		String keywords, int start, int end,
		OrderByComparator<CTCollectionTemplate> orderByComparator) {

		String[] keywordsArray = _customSQL.keywords(
			keywords, true, WildcardMode.SURROUND);

		DSLQuery dslQuery = DSLQueryFactoryUtil.select(
			CTCollectionTemplateTable.INSTANCE
		).from(
			CTCollectionTemplateTable.INSTANCE
		).where(
			CTCollectionTemplateTable.INSTANCE.companyId.eq(
				CompanyThreadLocal.getCompanyId()
			).and(
				Predicate.or(
					_customSQL.getKeywordsPredicate(
						DSLFunctionFactoryUtil.lower(
							CTCollectionTable.INSTANCE.name),
						keywordsArray),
					_customSQL.getKeywordsPredicate(
						DSLFunctionFactoryUtil.lower(
							CTCollectionTable.INSTANCE.description),
						keywordsArray))
			)
		).orderBy(
			CTCollectionTemplateTable.INSTANCE, orderByComparator
		).limit(
			start, end
		);

		return ctCollectionTemplatePersistence.dslQuery(dslQuery);
	}

	public int getCTCollectionTemplatesCount(String keywords) {
		String[] keywordsArray = _customSQL.keywords(
			keywords, true, WildcardMode.SURROUND);

		DSLQuery dslQuery = DSLQueryFactoryUtil.count(
		).from(
			CTCollectionTemplateTable.INSTANCE
		).where(
			CTCollectionTemplateTable.INSTANCE.companyId.eq(
				CompanyThreadLocal.getCompanyId()
			).and(
				Predicate.or(
					_customSQL.getKeywordsPredicate(
						DSLFunctionFactoryUtil.lower(
							CTCollectionTable.INSTANCE.name),
						keywordsArray),
					_customSQL.getKeywordsPredicate(
						DSLFunctionFactoryUtil.lower(
							CTCollectionTable.INSTANCE.description),
						keywordsArray))
			)
		);

		return ctCollectionTemplatePersistence.dslQueryCount(dslQuery);
	}

	@Override
	public CTCollectionTemplate updateCTCollectionTemplate(
			long ctCollectionTemplateId, String name, String description,
			String json)
		throws PortalException {

		_ctCollectionTemplateModelResourcePermission.check(
			getPermissionChecker(), ctCollectionTemplateId, ActionKeys.UPDATE);

		return ctCollectionTemplateLocalService.updateCTCollectionTemplate(
			ctCollectionTemplateId, name, description, json);
	}

	@Reference(
		target = "(model.class.name=com.liferay.change.tracking.model.CTCollectionTemplate)"
	)
	private ModelResourcePermission<CTCollectionTemplate>
		_ctCollectionTemplateModelResourcePermission;

	@Reference
	private CustomSQL _customSQL;

	@Reference(target = "(resource.name=" + CTConstants.RESOURCE_NAME + ")")
	private PortletResourcePermission _portletResourcePermission;

	@Reference
	private UserLocalService _userLocalService;

}