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

package com.liferay.layout.content.page.editor.web.internal.model.listener;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.asset.model.AssetEntryUsage;
import com.liferay.asset.service.AssetEntryUsageLocalService;
import com.liferay.dynamic.data.mapping.kernel.DDMTemplate;
import com.liferay.dynamic.data.mapping.kernel.DDMTemplateManager;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLinkLocalService;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.display.template.PortletDisplayTemplate;

import java.util.Iterator;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(service = ModelListener.class)
public class FragmentEntryLinkModelListener
	extends BaseModelListener<FragmentEntryLink> {

	@Override
	public void onAfterRemove(FragmentEntryLink fragmentEntryLink)
		throws ModelListenerException {

		_assetEntryUsageLocalService.deleteAssetEntryUsages(
			_portal.getClassNameId(FragmentEntryLink.class),
			String.valueOf(fragmentEntryLink.getFragmentEntryLinkId()),
			fragmentEntryLink.getClassPK());

		_ddmTemplateLinkLocalService.deleteTemplateLink(
			_portal.getClassNameId(FragmentEntryLink.class),
			fragmentEntryLink.getFragmentEntryLinkId());
	}

	@Override
	public void onAfterUpdate(FragmentEntryLink fragmentEntryLink)
		throws ModelListenerException {

		_assetEntryUsageLocalService.deleteAssetEntryUsages(
			_portal.getClassNameId(FragmentEntryLink.class),
			String.valueOf(fragmentEntryLink.getFragmentEntryLinkId()),
			fragmentEntryLink.getClassPK());

		_ddmTemplateLinkLocalService.deleteTemplateLink(
			_portal.getClassNameId(FragmentEntryLink.class),
			fragmentEntryLink.getFragmentEntryLinkId());

		try {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
				fragmentEntryLink.getEditableValues());

			Iterator<String> keysIterator = jsonObject.keys();

			while (keysIterator.hasNext()) {
				String key = keysIterator.next();

				JSONObject editableProcessorJSONObject =
					jsonObject.getJSONObject(key);

				if (editableProcessorJSONObject == null) {
					continue;
				}

				Iterator<String> editableKeysIterator =
					editableProcessorJSONObject.keys();

				while (editableKeysIterator.hasNext()) {
					String editableKey = editableKeysIterator.next();

					JSONObject editableJSONObject =
						editableProcessorJSONObject.getJSONObject(editableKey);

					String fieldId = editableJSONObject.getString("fieldId");

					String mappedField = editableJSONObject.getString(
						"mappedField", fieldId);

					if (Validator.isNotNull(mappedField)) {
						_updateDDMTemplateLink(fragmentEntryLink, mappedField);
					}

					long classPK = GetterUtil.getLong(
						editableJSONObject.getLong("classPK"));
					long classNameId = GetterUtil.getLong(
						editableJSONObject.getLong("classNameId"));

					_updateAssetEntryUsage(
						fragmentEntryLink, classNameId, classPK);
				}
			}
		}
		catch (PortalException pe) {
			throw new ModelListenerException(pe);
		}
	}

	private void _updateAssetEntryUsage(
		FragmentEntryLink fragmentEntryLink, long classNameId, long classPK) {

		AssetEntry assetEntry = _assetEntryLocalService.fetchEntry(
			classNameId, classPK);

		if (assetEntry == null) {
			return;
		}

		AssetEntryUsage assetEntryUsage =
			_assetEntryUsageLocalService.fetchAssetEntryUsage(
				assetEntry.getEntryId(),
				_portal.getClassNameId(FragmentEntryLink.class),
				String.valueOf(fragmentEntryLink.getFragmentEntryLinkId()),
				fragmentEntryLink.getClassPK());

		if (assetEntryUsage != null) {
			return;
		}

		ServiceContext serviceContext = Optional.ofNullable(
			ServiceContextThreadLocal.getServiceContext()
		).orElse(
			new ServiceContext()
		);

		_assetEntryUsageLocalService.addAssetEntryUsage(
			fragmentEntryLink.getGroupId(), assetEntry.getEntryId(),
			_portal.getClassNameId(FragmentEntryLink.class),
			String.valueOf(fragmentEntryLink.getFragmentEntryLinkId()),
			fragmentEntryLink.getClassPK(), serviceContext);
	}

	private void _updateDDMTemplateLink(
		FragmentEntryLink fragmentEntryLink, String mappedField) {

		if (mappedField.startsWith(
				PortletDisplayTemplate.DISPLAY_STYLE_PREFIX)) {

			String ddmTemplateKey = mappedField.substring(
				PortletDisplayTemplate.DISPLAY_STYLE_PREFIX.length());

			DDMTemplate ddmTemplate = _ddmTemplateManager.fetchTemplate(
				fragmentEntryLink.getGroupId(),
				_portal.getClassNameId(DDMStructure.class), ddmTemplateKey);

			if (ddmTemplate != null) {
				_ddmTemplateLinkLocalService.addTemplateLink(
					_portal.getClassNameId(FragmentEntryLink.class),
					fragmentEntryLink.getFragmentEntryLinkId(),
					ddmTemplate.getTemplateId());
			}
		}
	}

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private AssetEntryUsageLocalService _assetEntryUsageLocalService;

	@Reference
	private DDMTemplateLinkLocalService _ddmTemplateLinkLocalService;

	@Reference
	private DDMTemplateManager _ddmTemplateManager;

	@Reference
	private Portal _portal;

}