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

import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.layout.content.page.editor.web.internal.segments.SegmentsExperienceUtil;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.segments.constants.SegmentsExperienceConstants;
import com.liferay.segments.constants.SegmentsExperimentConstants;
import com.liferay.segments.model.SegmentsExperiment;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sarai Díaz
 * @author David Arques
 */
@Component(immediate = true, service = ModelListener.class)
public class SegmentsExperimentModelListener
	extends BaseModelListener<SegmentsExperiment> {

	@Override
	public void onAfterUpdate(SegmentsExperiment segmentsExperiment)
		throws ModelListenerException {

		if (!_requiresDefaultExperienceReplacement(segmentsExperiment)) {
			return;
		}

		try {
			SegmentsExperienceUtil.copySegmentsExperienceData(
				segmentsExperiment.getClassNameId(),
				segmentsExperiment.getClassPK(), _fragmentEntryLinkLocalService,
				segmentsExperiment.getGroupId(),
				_layoutPageTemplateStructureLocalService, _portletLocalService,
				_portletPreferencesLocalService,
				segmentsExperiment.getWinnerSegmentsExperienceId(),
				SegmentsExperienceConstants.ID_DEFAULT);

			Layout draftLayout = _layoutLocalService.fetchLayout(
				_portal.getClassNameId(Layout.class.getName()),
				segmentsExperiment.getClassPK());

			if (draftLayout != null) {
				SegmentsExperienceUtil.copySegmentsExperienceData(
					draftLayout.getClassNameId(), draftLayout.getPlid(),
					_fragmentEntryLinkLocalService,
					segmentsExperiment.getGroupId(),
					_layoutPageTemplateStructureLocalService,
					_portletLocalService, _portletPreferencesLocalService,
					segmentsExperiment.getWinnerSegmentsExperienceId(),
					SegmentsExperienceConstants.ID_DEFAULT);
			}
		}
		catch (PortalException pe) {
			throw new ModelListenerException(
				"Unable to update segments experiment " +
					segmentsExperiment.getSegmentsExperimentId(),
				pe);
		}
	}

	private boolean _requiresDefaultExperienceReplacement(
		SegmentsExperiment segmentsExperiment) {

		if ((segmentsExperiment.getSegmentsExperienceId() ==
				SegmentsExperienceConstants.ID_DEFAULT) &&
			(segmentsExperiment.getStatus() ==
				SegmentsExperimentConstants.STATUS_COMPLETED) &&
			(segmentsExperiment.getWinnerSegmentsExperienceId() !=
				SegmentsExperienceConstants.ID_DEFAULT)) {

			return true;
		}

		return false;
	}

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

	@Reference
	private PortletPreferencesLocalService _portletPreferencesLocalService;

}