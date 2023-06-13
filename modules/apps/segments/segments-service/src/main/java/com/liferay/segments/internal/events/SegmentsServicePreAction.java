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

package com.liferay.segments.internal.events;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.segments.constants.SegmentsConstants;
import com.liferay.segments.constants.SegmentsWebKeys;
import com.liferay.segments.internal.configuration.SegmentsServiceConfiguration;
import com.liferay.segments.internal.context.RequestContextMapper;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.segments.model.SegmentsExperienceModel;
import com.liferay.segments.provider.SegmentsEntryProvider;
import com.liferay.segments.service.SegmentsExperienceLocalService;
import com.liferay.segments.simulator.SegmentsEntrySimulator;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Eduardo García
 */
@Component(
	configurationPid = "com.liferay.segments.internal.configuration.SegmentsServiceConfiguration",
	service = {}
)
public class SegmentsServicePreAction extends Action {

	@Override
	public void run(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws ActionException {

		try {
			doRun(httpServletRequest);
		}
		catch (Exception e) {
			throw new ActionException(e);
		}
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		SegmentsServiceConfiguration segmentsServiceConfiguration =
			ConfigurableUtil.createConfigurable(
				SegmentsServiceConfiguration.class, properties);

		if (segmentsServiceConfiguration.segmentationEnabled()) {
			_serviceRegistration = bundleContext.registerService(
				LifecycleAction.class, this,
				MapUtil.singletonDictionary(
					"key", "servlet.service.events.pre"));
		}
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	protected void doRun(HttpServletRequest httpServletRequest) {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (!themeDisplay.isLifecycleRender()) {
			return;
		}

		long[] segmentsEntryIds = new long[0];

		Layout layout = themeDisplay.getLayout();

		if (!layout.isTypeControlPanel()) {
			segmentsEntryIds = _getSegmentsEntryIds(
				httpServletRequest, themeDisplay.getScopeGroupId(),
				themeDisplay.getUserId());
		}

		segmentsEntryIds = ArrayUtil.append(
			segmentsEntryIds, SegmentsConstants.SEGMENTS_ENTRY_ID_DEFAULT);

		httpServletRequest.setAttribute(
			SegmentsWebKeys.SEGMENTS_ENTRY_IDS, segmentsEntryIds);

		long[] segmentsExperienceIds = _getSegmentsExperienceIds(
			layout.getGroupId(), segmentsEntryIds,
			_portal.getClassNameId(Layout.class.getName()), layout.getPlid());

		httpServletRequest.setAttribute(
			SegmentsWebKeys.SEGMENTS_EXPERIENCE_IDS,
			ArrayUtil.append(
				segmentsExperienceIds,
				SegmentsConstants.SEGMENTS_EXPERIENCE_ID_DEFAULT));
	}

	private long[] _getSegmentsEntryIds(
		HttpServletRequest httpServletRequest, long groupId, long userId) {

		if ((_segmentsEntrySimulator != null) &&
			_segmentsEntrySimulator.isSimulationActive(userId)) {

			return _segmentsEntrySimulator.getSimulatedSegmentsEntryIds(userId);
		}

		try {
			return _segmentsEntryProvider.getSegmentsEntryIds(
				groupId, User.class.getName(), userId,
				_requestContextMapper.map(httpServletRequest));
		}
		catch (PortalException pe) {
			if (_log.isWarnEnabled()) {
				_log.warn(pe.getMessage());
			}

			return new long[0];
		}
	}

	private long[] _getSegmentsExperienceIds(
		long groupId, long[] segmentsEntryIds, long classNameId, long classPK) {

		List<SegmentsExperience> segmentsExperiences =
			_segmentsExperienceLocalService.getSegmentsExperiences(
				groupId, segmentsEntryIds, classNameId, classPK, true,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Stream<SegmentsExperience> stream = segmentsExperiences.stream();

		return stream.mapToLong(
			SegmentsExperienceModel::getSegmentsExperienceId
		).toArray();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SegmentsServicePreAction.class);

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private RequestContextMapper _requestContextMapper;

	@Reference
	private SegmentsEntryProvider _segmentsEntryProvider;

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(model.class.name=com.liferay.portal.kernel.model.User)"
	)
	private volatile SegmentsEntrySimulator _segmentsEntrySimulator;

	@Reference
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

	private ServiceRegistration<LifecycleAction> _serviceRegistration;

}