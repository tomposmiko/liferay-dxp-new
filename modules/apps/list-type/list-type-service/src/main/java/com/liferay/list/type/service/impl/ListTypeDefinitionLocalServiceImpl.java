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

package com.liferay.list.type.service.impl;

import com.liferay.list.type.exception.ListTypeDefinitionNameException;
import com.liferay.list.type.exception.RequiredListTypeDefinitionException;
import com.liferay.list.type.model.ListTypeDefinition;
import com.liferay.list.type.model.ListTypeEntry;
import com.liferay.list.type.service.ListTypeEntryLocalService;
import com.liferay.list.type.service.base.ListTypeDefinitionLocalServiceBaseImpl;
import com.liferay.list.type.service.persistence.ListTypeEntryPersistence;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsUtil;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gabriel Albuquerque
 */
@Component(
	property = "model.class.name=com.liferay.list.type.model.ListTypeDefinition",
	service = AopService.class
)
public class ListTypeDefinitionLocalServiceImpl
	extends ListTypeDefinitionLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public ListTypeDefinition addListTypeDefinition(
			String externalReferenceCode, long userId)
		throws PortalException {

		ListTypeDefinition listTypeDefinition =
			listTypeDefinitionPersistence.create(
				counterLocalService.increment());

		return _addListTypeDefinition(
			listTypeDefinition, externalReferenceCode, userId,
			Collections.singletonMap(
				LocaleUtil.getDefault(), externalReferenceCode));
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public ListTypeDefinition addListTypeDefinition(
			String externalReferenceCode, long userId,
			Map<Locale, String> nameMap, List<ListTypeEntry> listTypeEntries)
		throws PortalException {

		_validateName(nameMap, LocaleUtil.getSiteDefault());

		ListTypeDefinition listTypeDefinition =
			listTypeDefinitionPersistence.create(
				counterLocalService.increment());

		listTypeDefinition = _addListTypeDefinition(
			listTypeDefinition, externalReferenceCode, userId, nameMap);

		_addListTypeEntries(
			userId, listTypeDefinition.getListTypeDefinitionId(),
			listTypeEntries);

		return listTypeDefinition;
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public ListTypeDefinition deleteListTypeDefinition(
			ListTypeDefinition listTypeDefinition)
		throws PortalException {

		int count =
			_objectFieldLocalService.getObjectFieldsCountByListTypeDefinitionId(
				listTypeDefinition.getListTypeDefinitionId());

		if (count > 0) {
			throw new RequiredListTypeDefinitionException();
		}

		_resourceLocalService.deleteResource(
			listTypeDefinition, ResourceConstants.SCOPE_INDIVIDUAL);

		listTypeDefinition = listTypeDefinitionPersistence.remove(
			listTypeDefinition);

		_listTypeEntryPersistence.removeByListTypeDefinitionId(
			listTypeDefinition.getListTypeDefinitionId());

		return listTypeDefinition;
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	public ListTypeDefinition deleteListTypeDefinition(
			long listTypeDefinitionId)
		throws PortalException {

		ListTypeDefinition listTypeDefinition =
			listTypeDefinitionPersistence.findByPrimaryKey(
				listTypeDefinitionId);

		return deleteListTypeDefinition(listTypeDefinition);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public ListTypeDefinition updateListTypeDefinition(
			String externalReferenceCode, long listTypeDefinitionId,
			long userId, Map<Locale, String> nameMap,
			List<ListTypeEntry> listTypeEntries)
		throws PortalException {

		_validateName(nameMap, LocaleUtil.getSiteDefault());

		ListTypeDefinition listTypeDefinition =
			listTypeDefinitionPersistence.findByPrimaryKey(
				listTypeDefinitionId);

		listTypeDefinition.setExternalReferenceCode(externalReferenceCode);
		listTypeDefinition.setNameMap(nameMap);

		listTypeDefinition = listTypeDefinitionPersistence.update(
			listTypeDefinition);

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-167536"))) {
			return listTypeDefinition;
		}

		_listTypeEntryLocalService.deleteListTypeEntryByListTypeDefinitionId(
			listTypeDefinitionId);

		_addListTypeEntries(userId, listTypeDefinitionId, listTypeEntries);

		return listTypeDefinition;
	}

	private ListTypeDefinition _addListTypeDefinition(
			ListTypeDefinition listTypeDefinition, String externalReferenceCode,
			long userId, Map<Locale, String> nameMap)
		throws PortalException {

		listTypeDefinition.setExternalReferenceCode(externalReferenceCode);

		User user = _userLocalService.getUser(userId);

		listTypeDefinition.setCompanyId(user.getCompanyId());
		listTypeDefinition.setUserId(user.getUserId());
		listTypeDefinition.setUserName(user.getFullName());

		listTypeDefinition.setNameMap(nameMap);

		listTypeDefinition = listTypeDefinitionPersistence.update(
			listTypeDefinition);

		_resourceLocalService.addResources(
			listTypeDefinition.getCompanyId(), 0,
			listTypeDefinition.getUserId(), ListTypeDefinition.class.getName(),
			listTypeDefinition.getListTypeDefinitionId(), false, true, true);

		return listTypeDefinition;
	}

	private void _addListTypeEntries(
			long userId, long listTypeDefinitionId,
			List<ListTypeEntry> listTypeEntries)
		throws PortalException {

		for (ListTypeEntry listTypeEntry : listTypeEntries) {
			_listTypeEntryLocalService.addListTypeEntry(
				listTypeEntry.getExternalReferenceCode(), userId,
				listTypeDefinitionId, listTypeEntry.getKey(),
				listTypeEntry.getNameMap());
		}
	}

	private void _validateName(
			Map<Locale, String> nameMap, Locale defaultLocale)
		throws PortalException {

		if ((nameMap == null) || Validator.isNull(nameMap.get(defaultLocale))) {
			throw new ListTypeDefinitionNameException(
				"Name is null for locale " + defaultLocale.getDisplayName());
		}
	}

	@Reference
	private ListTypeEntryLocalService _listTypeEntryLocalService;

	@Reference
	private ListTypeEntryPersistence _listTypeEntryPersistence;

	@Reference
	private ObjectFieldLocalService _objectFieldLocalService;

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private UserLocalService _userLocalService;

}