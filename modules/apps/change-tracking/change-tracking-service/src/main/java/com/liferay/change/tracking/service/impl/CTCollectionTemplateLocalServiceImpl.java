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

import com.liferay.change.tracking.constants.CTConstants;
import com.liferay.change.tracking.exception.CTCollectionDescriptionException;
import com.liferay.change.tracking.exception.CTCollectionNameException;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.model.CTCollectionTemplate;
import com.liferay.change.tracking.service.base.CTCollectionTemplateLocalServiceBaseImpl;
import com.liferay.json.storage.service.JSONStorageEntryLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ModelHintsUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Digester;
import com.liferay.portal.kernel.util.DigesterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Validator;

import java.time.Instant;
import java.time.LocalDate;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.change.tracking.model.CTCollectionTemplate",
	service = AopService.class
)
public class CTCollectionTemplateLocalServiceImpl
	extends CTCollectionTemplateLocalServiceBaseImpl {

	@Override
	public CTCollectionTemplate addCTCollectionTemplate(
			long userId, String name, String description, String json)
		throws PortalException {

		_validate(name, description);

		long ctCollectionTemplateId = counterLocalService.increment(
			CTCollectionTemplate.class.getName());

		CTCollectionTemplate ctCollectionTemplate =
			ctCollectionTemplatePersistence.create(ctCollectionTemplateId);

		User user = _userLocalService.getUser(userId);

		ctCollectionTemplate.setCompanyId(user.getCompanyId());

		ctCollectionTemplate.setUserId(userId);
		ctCollectionTemplate.setName(name);
		ctCollectionTemplate.setDescription(description);

		ctCollectionTemplate = ctCollectionTemplatePersistence.update(
			ctCollectionTemplate);

		_resourceLocalService.addResources(
			ctCollectionTemplate.getCompanyId(), 0,
			ctCollectionTemplate.getUserId(),
			CTCollectionTemplate.class.getName(),
			ctCollectionTemplate.getCtCollectionTemplateId(), false, false,
			false);

		_jsonStorageEntryLocalService.addJSONStorageEntries(
			user.getCompanyId(),
			_classNameLocalService.getClassNameId(
				CTCollectionTemplate.class.getName()),
			ctCollectionTemplateId, json);

		return ctCollectionTemplate;
	}

	@Override
	public CTCollectionTemplate fetchCTCollectionTemplate(
		long ctCollectionTemplateId) {

		return ctCollectionTemplatePersistence.fetchByPrimaryKey(
			ctCollectionTemplateId);
	}

	@Override
	public List<CTCollectionTemplate> getCTCollectionTemplates(
		long companyId, int start, int end) {

		return ctCollectionTemplatePersistence.findByCompanyId(
			companyId, start, end);
	}

	public Set<String> getTokens() {
		Map<String, String> tokensMap = _getTokensMap(0);

		return tokensMap.keySet();
	}

	@Override
	public String parseTokens(long ctCollectionTemplateId, String s) {
		if (s.contains(StringPool.DOLLAR_AND_OPEN_CURLY_BRACE)) {
			StringBundler sb = new StringBundler();

			int current = 0;

			Map<String, String> tokensMap = _getTokensMap(
				ctCollectionTemplateId);

			while (current < s.length()) {
				int x = s.indexOf(
					StringPool.DOLLAR_AND_OPEN_CURLY_BRACE, current);

				if (x == -1) {
					sb.append(s.substring(current));

					break;
				}

				int y = s.indexOf(StringPool.CLOSE_CURLY_BRACE, x);

				sb.append(s.substring(current, x));

				String token = s.substring(x, y + 1);

				sb.append(tokensMap.get(token));

				current = y + 1;
			}

			return sb.toString();
		}

		return s;
	}

	@Override
	public CTCollectionTemplate updateCTCollectionTemplate(
			long ctCollectionTemplateId, String name, String description,
			String json)
		throws PortalException {

		_validate(name, description);

		CTCollectionTemplate ctCollectionTemplate =
			ctCollectionTemplatePersistence.findByPrimaryKey(
				ctCollectionTemplateId);

		ctCollectionTemplate.setName(name);
		ctCollectionTemplate.setDescription(description);

		ctCollectionTemplate = ctCollectionTemplatePersistence.update(
			ctCollectionTemplate);

		_jsonStorageEntryLocalService.updateJSONStorageEntries(
			ctCollectionTemplate.getCompanyId(),
			_classNameLocalService.getClassNameId(
				CTCollectionTemplate.class.getName()),
			ctCollectionTemplateId, json);

		return ctCollectionTemplate;
	}

	private Map<String, String> _getTokensMap(long ctCollectionTemplateId) {
		return HashMapBuilder.put(
			"${CURRENT_USERNAME}",
			() -> {
				ServiceContext serviceContext =
					ServiceContextThreadLocal.getServiceContext();

				User user = _userLocalService.fetchUser(
					serviceContext.getUserId());

				if (user != null) {
					return user.getScreenName();
				}

				return StringPool.BLANK;
			}
		).put(
			"${RANDOM_HASH}",
			() -> {
				Instant now = Instant.now();

				return DigesterUtil.digestHex(
					Digester.MD5, String.valueOf(ctCollectionTemplateId),
					String.valueOf(now.getEpochSecond()));
			}
		).put(
			"${TODAY_DATE}", String.valueOf(LocalDate.now())
		).build();
	}

	private void _validate(String name, String description)
		throws PortalException {

		if (Validator.isNull(name)) {
			throw new CTCollectionNameException();
		}

		int nameMaxLength = ModelHintsUtil.getMaxLength(
			CTCollection.class.getName(), "name");

		if (name.length() > nameMaxLength) {
			throw new CTCollectionNameException("Name is too long");
		}

		int descriptionMaxLength = ModelHintsUtil.getMaxLength(
			CTCollection.class.getName(), "description");

		if ((description != null) &&
			(description.length() > descriptionMaxLength)) {

			throw new CTCollectionDescriptionException(
				"Description is too long");
		}
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private JSONStorageEntryLocalService _jsonStorageEntryLocalService;

	@Reference(target = "(resource.name=" + CTConstants.RESOURCE_NAME + ")")
	private PortletResourcePermission _portletResourcePermission;

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private UserLocalService _userLocalService;

}