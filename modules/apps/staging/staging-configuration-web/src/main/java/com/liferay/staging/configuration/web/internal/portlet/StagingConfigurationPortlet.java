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

package com.liferay.staging.configuration.web.internal.portlet;

import com.liferay.change.tracking.configuration.CTSettingsConfiguration;
import com.liferay.exportimport.kernel.service.StagingLocalService;
import com.liferay.exportimport.kernel.staging.Staging;
import com.liferay.exportimport.kernel.staging.constants.StagingConstants;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManager;
import com.liferay.portal.kernel.backgroundtask.constants.BackgroundTaskConstants;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.exception.NoSuchBackgroundTaskException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.staging.constants.StagingConfigurationPortletKeys;
import com.liferay.staging.constants.StagingProcessesPortletKeys;

import java.io.IOException;

import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Levente Hudák
 */
@Component(
	configurationPid = "com.liferay.change.tracking.configuration.CTSettingsConfiguration",
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.css-class-wrapper=portlet-staging-configuration",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.show-portlet-access-denied=false",
		"com.liferay.portlet.show-portlet-inactive=false",
		"com.liferay.portlet.system=true",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Staging Configuration",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/META-INF/resources/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + StagingConfigurationPortletKeys.STAGING_CONFIGURATION,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.version=3.0"
	},
	service = Portlet.class
)
public class StagingConfigurationPortlet extends MVCPortlet {

	public void deleteBackgroundTask(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortalException {

		try {
			long backgroundTaskId = ParamUtil.getLong(
				actionRequest, BackgroundTaskConstants.BACKGROUND_TASK_ID);

			_backgroundTaskManager.deleteBackgroundTask(backgroundTaskId);

			sendRedirect(actionRequest, actionResponse);
		}
		catch (Exception exception) {
			if (exception instanceof NoSuchBackgroundTaskException ||
				exception instanceof PrincipalException) {

				SessionErrors.add(actionRequest, exception.getClass());

				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}
			}
			else {
				throw exception;
			}
		}
	}

	public void editStagingConfiguration(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortalException, PortletException {

		hideDefaultSuccessMessage(actionRequest);

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		int stagingType = ParamUtil.getInteger(actionRequest, "stagingType");

		if (stagingType != StagingConstants.TYPE_NOT_STAGED) {
			CTSettingsConfiguration ctSettingsConfiguration =
				_getCTSettingsConfiguration(themeDisplay.getCompanyId());

			if (ctSettingsConfiguration.enabled()) {
				SessionErrors.add(actionRequest, "publicationsEnabled");

				return;
			}
		}

		long liveGroupId = ParamUtil.getLong(actionRequest, "liveGroupId");

		Group liveGroup = _groupLocalService.getGroup(liveGroupId);

		boolean branchingPublic = ParamUtil.getBoolean(
			actionRequest, "branchingPublic");
		boolean branchingPrivate = ParamUtil.getBoolean(
			actionRequest, "branchingPrivate");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			actionRequest);

		boolean stagedGroup = true;

		if (stagingType == StagingConstants.TYPE_LOCAL_STAGING) {
			stagedGroup = liveGroup.hasStagingGroup();

			try {
				_stagingLocalService.enableLocalStaging(
					themeDisplay.getUserId(), liveGroup, branchingPublic,
					branchingPrivate, serviceContext);
			}
			catch (Exception exception) {
				SessionErrors.add(
					actionRequest, exception.getClass(), exception);

				return;
			}
		}
		else if (stagingType == StagingConstants.TYPE_REMOTE_STAGING) {
			String remoteAddress = ParamUtil.getString(
				actionRequest, "remoteAddress");
			int remotePort = ParamUtil.getInteger(actionRequest, "remotePort");
			String remotePathContext = ParamUtil.getString(
				actionRequest, "remotePathContext");
			boolean secureConnection = ParamUtil.getBoolean(
				actionRequest, "secureConnection");
			long remoteGroupId = ParamUtil.getLong(
				actionRequest, "remoteGroupId");

			stagedGroup = liveGroup.isStagedRemotely();

			try {
				_staging.validateRemoteGroupIsSame(
					liveGroup.getGroupId(), remoteGroupId, remoteAddress,
					remotePort, remotePathContext, secureConnection);

				_stagingLocalService.enableRemoteStaging(
					themeDisplay.getUserId(), liveGroup, branchingPublic,
					branchingPrivate, remoteAddress, remotePort,
					remotePathContext, secureConnection, remoteGroupId,
					serviceContext);

				boolean overrideRemoteSiteURL = ParamUtil.getBoolean(
					actionRequest, "overrideRemoteSiteURL");
				String remoteSiteURL = ParamUtil.getString(
					actionRequest, "remoteSiteURL");

				_staging.setRemoteSiteURL(
					liveGroup, overrideRemoteSiteURL, remoteSiteURL);
			}
			catch (Exception exception) {
				SessionErrors.add(
					actionRequest, exception.getClass(), exception);

				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}

				return;
			}
		}
		else if (stagingType == StagingConstants.TYPE_NOT_STAGED) {
			_stagingLocalService.disableStaging(liveGroup, serviceContext);
		}

		String redirect = ParamUtil.getString(actionRequest, "redirect");

		if (!stagedGroup) {

			// Staging was turned on

			PortletURL portletURL = null;

			if (stagingType == StagingConstants.TYPE_LOCAL_STAGING) {
				portletURL = PortletURLBuilder.create(
					_portal.getControlPanelPortletURL(
						actionRequest, liveGroup.getStagingGroup(),
						StagingProcessesPortletKeys.STAGING_PROCESSES, 0, 0,
						PortletRequest.RENDER_PHASE)
				).setParameter(
					"localStagingEnabled", true
				).buildPortletURL();
			}
			else if (stagingType == StagingConstants.TYPE_REMOTE_STAGING) {
				portletURL = PortletURLBuilder.create(
					_portal.getControlPanelPortletURL(
						actionRequest, liveGroup,
						StagingProcessesPortletKeys.STAGING_PROCESSES, 0, 0,
						PortletRequest.RENDER_PHASE)
				).setParameter(
					"remoteStagingEnabled", true
				).buildPortletURL();
			}

			if (portletURL != null) {
				redirect = portletURL.toString();
			}
		}
		else if ((stagingType == StagingConstants.TYPE_NOT_STAGED) ||
				 (stagingType == StagingConstants.TYPE_REMOTE_STAGING)) {

			// Staging was turned off or remote staging configuration was
			// modified

			PortletURL portletURL = PortletURLBuilder.create(
				_portal.getControlPanelPortletURL(
					actionRequest, liveGroup,
					StagingProcessesPortletKeys.STAGING_PROCESSES, 0, 0,
					PortletRequest.RENDER_PHASE)
			).setParameter(
				"showStagingConfiguration", true
			).buildPortletURL();

			if (portletURL != null) {
				redirect = portletURL.toString();
			}

			if (stagingType == StagingConstants.TYPE_NOT_STAGED) {
				SessionMessages.add(actionRequest, "stagingDisabled");
			}
			else {
				SessionMessages.add(actionRequest, "remoteStagingModified");
			}
		}
		else {

			// Local staging configuration was modified

			PortletURL portletURL = PortletURLBuilder.create(
				_portal.getControlPanelPortletURL(
					actionRequest, liveGroup.getStagingGroup(),
					StagingProcessesPortletKeys.STAGING_PROCESSES, 0, 0,
					PortletRequest.RENDER_PHASE)
			).setParameter(
				"showStagingConfiguration", true
			).buildPortletURL();

			if (portletURL != null) {
				redirect = portletURL.toString();
			}

			SessionMessages.add(actionRequest, "localStagingModified");
		}

		actionRequest.setAttribute(WebKeys.REDIRECT, redirect);

		sendRedirect(actionRequest, actionResponse);
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_defaultCTSettingsConfiguration = ConfigurableUtil.createConfigurable(
			CTSettingsConfiguration.class, properties);
	}

	@Override
	protected boolean isSessionErrorException(Throwable throwable) {
		if (throwable instanceof LocaleException) {
			return true;
		}

		return super.isSessionErrorException(throwable);
	}

	@Reference
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	@Reference(unbind = "-")
	protected void setStaging(Staging staging) {
		_staging = staging;
	}

	@Reference
	protected void setStagingLocalService(
		StagingLocalService stagingLocalService) {

		_stagingLocalService = stagingLocalService;
	}

	protected void unsetGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = null;
	}

	protected void unsetStagingLocalService(
		StagingLocalService stagingLocalService) {

		_stagingLocalService = null;
	}

	private CTSettingsConfiguration _getCTSettingsConfiguration(
		long companyId) {

		try {
			return _configurationProvider.getCompanyConfiguration(
				CTSettingsConfiguration.class, companyId);
		}
		catch (ConfigurationException configurationException) {
			_log.error(configurationException);
		}

		return _defaultCTSettingsConfiguration;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		StagingConfigurationPortlet.class);

	@Reference
	private BackgroundTaskManager _backgroundTaskManager;

	@Reference
	private ConfigurationProvider _configurationProvider;

	private volatile CTSettingsConfiguration _defaultCTSettingsConfiguration;
	private GroupLocalService _groupLocalService;

	@Reference
	private Portal _portal;

	private Staging _staging;
	private StagingLocalService _stagingLocalService;

}