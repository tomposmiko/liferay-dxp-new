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

package com.liferay.knowledge.base.web.internal.info.item.provider;

import com.liferay.info.exception.NoSuchInfoItemException;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.InfoItemIdentifier;
import com.liferay.info.item.provider.InfoItemObjectProvider;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.service.KBArticleLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alicia García
 */
@Component(
	property = {
		"info.item.identifier=com.liferay.info.item.ClassPKInfoItemIdentifier",
		"item.class.name=com.liferay.knowledge.base.model.KBArticle",
		"service.ranking:Integer=100"
	},
	service = InfoItemObjectProvider.class
)
public class KBArticleInfoItemObjectProvider
	implements InfoItemObjectProvider<KBArticle> {

	@Override
	public KBArticle getInfoItem(InfoItemIdentifier infoItemIdentifier)
		throws NoSuchInfoItemException {

		if (!(infoItemIdentifier instanceof ClassPKInfoItemIdentifier)) {
			throw new NoSuchInfoItemException(
				"Unsupported info item identifier type " + infoItemIdentifier);
		}

		KBArticle kbArticle = null;

		String version = infoItemIdentifier.getVersion();

		if (infoItemIdentifier instanceof ClassPKInfoItemIdentifier) {
			ClassPKInfoItemIdentifier classPKInfoItemIdentifier =
				(ClassPKInfoItemIdentifier)infoItemIdentifier;

			kbArticle = _getKbArticle(version, classPKInfoItemIdentifier);
		}

		if ((kbArticle == null) ||
			(!Objects.equals(version, InfoItemIdentifier.VERSION_LATEST) &&
			 kbArticle.isDraft())) {

			throw new NoSuchInfoItemException(
				"Unable to get knowledge base article with info item " +
					"identifier " + infoItemIdentifier);
		}

		return kbArticle;
	}

	@Override
	public KBArticle getInfoItem(long classPK) throws NoSuchInfoItemException {
		ClassPKInfoItemIdentifier classPKInfoItemIdentifier =
			new ClassPKInfoItemIdentifier(classPK);

		return getInfoItem(classPKInfoItemIdentifier);
	}

	private KBArticle _getKbArticle(
		String version, ClassPKInfoItemIdentifier classPKInfoItemIdentifier) {

		KBArticle kbArticle;

		if (Validator.isNull(version) ||
			Objects.equals(
				version, InfoItemIdentifier.VERSION_LATEST_APPROVED)) {

			kbArticle = _kbArticleLocalService.fetchLatestKBArticle(
				classPKInfoItemIdentifier.getClassPK(),
				WorkflowConstants.STATUS_APPROVED);
		}
		else if (Objects.equals(version, InfoItemIdentifier.VERSION_LATEST)) {
			kbArticle = _kbArticleLocalService.fetchLatestKBArticle(
				classPKInfoItemIdentifier.getClassPK(),
				WorkflowConstants.STATUS_ANY);
		}
		else {
			KBArticle latestKBArticle =
				_kbArticleLocalService.fetchLatestKBArticle(
					classPKInfoItemIdentifier.getClassPK(),
					WorkflowConstants.STATUS_ANY);

			kbArticle = _kbArticleLocalService.fetchKBArticle(
				classPKInfoItemIdentifier.getClassPK(),
				latestKBArticle.getGroupId(), GetterUtil.getInteger(version));
		}

		return kbArticle;
	}

	@Reference
	private KBArticleLocalService _kbArticleLocalService;

}