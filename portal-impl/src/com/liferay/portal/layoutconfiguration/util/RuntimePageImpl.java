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

package com.liferay.portal.layoutconfiguration.util;

import com.liferay.petra.io.unsync.UnsyncStringWriter;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.layoutconfiguration.util.RuntimePage;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.LayoutTemplate;
import com.liferay.portal.kernel.model.LayoutTemplateConstants;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.service.LayoutTemplateLocalServiceUtil;
import com.liferay.portal.kernel.servlet.PipingServletResponse;
import com.liferay.portal.kernel.servlet.PluginContextListener;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.layoutconfiguration.util.velocity.TemplateProcessor;
import com.liferay.portlet.internal.PortletBagUtil;
import com.liferay.portlet.internal.PortletTypeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 * @author Shuyang Zhou
 */
public class RuntimePageImpl implements RuntimePage {

	@Override
	public LayoutTemplate getLayoutTemplate(String velocityTemplateId) {
		String separator = LayoutTemplateConstants.CUSTOM_SEPARATOR;
		boolean standard = false;

		if (velocityTemplateId.contains(
				LayoutTemplateConstants.STANDARD_SEPARATOR)) {

			separator = LayoutTemplateConstants.STANDARD_SEPARATOR;
			standard = true;
		}

		String layoutTemplateId = null;

		String themeId = null;

		int pos = velocityTemplateId.indexOf(separator);

		if (pos != -1) {
			layoutTemplateId = velocityTemplateId.substring(
				pos + separator.length());

			themeId = velocityTemplateId.substring(0, pos);
		}

		pos = layoutTemplateId.indexOf(
			LayoutTemplateConstants.INSTANCE_SEPARATOR);

		if (pos != -1) {
			layoutTemplateId = layoutTemplateId.substring(
				pos + LayoutTemplateConstants.INSTANCE_SEPARATOR.length() + 1);

			pos = layoutTemplateId.indexOf(StringPool.UNDERLINE);

			layoutTemplateId = layoutTemplateId.substring(pos + 1);
		}

		return LayoutTemplateLocalServiceUtil.getLayoutTemplate(
			layoutTemplateId, standard, themeId);
	}

	@Override
	public StringBundler getProcessedTemplate(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String portletId,
			String templateId, String content)
		throws Exception {

		return doDispatch(
			httpServletRequest, httpServletResponse, portletId, templateId,
			content, TemplateConstants.LANG_TYPE_VM);
	}

	@Override
	public void processTemplate(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String portletId,
			String templateId, String content)
		throws Exception {

		StringBundler sb = doDispatch(
			httpServletRequest, httpServletResponse, portletId, templateId,
			content, TemplateConstants.LANG_TYPE_VM);

		sb.writeTo(httpServletResponse.getWriter());
	}

	@Override
	public void processTemplate(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String portletId,
			String templateId, String content, String langType)
		throws Exception {

		StringBundler sb = doDispatch(
			httpServletRequest, httpServletResponse, portletId, templateId,
			content, langType);

		sb.writeTo(httpServletResponse.getWriter());
	}

	protected StringBundler doDispatch(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String portletId,
			String templateId, String content, String langType)
		throws Exception {

		ClassLoader pluginClassLoader = null;

		LayoutTemplate layoutTemplate = getLayoutTemplate(templateId);

		if (layoutTemplate != null) {
			String pluginServletContextName = GetterUtil.getString(
				layoutTemplate.getServletContextName());

			ServletContext pluginServletContext = ServletContextPool.get(
				pluginServletContextName);

			if (pluginServletContext != null) {
				pluginClassLoader =
					(ClassLoader)pluginServletContext.getAttribute(
						PluginContextListener.PLUGIN_CLASS_LOADER);
			}
		}

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		try {
			if ((pluginClassLoader != null) &&
				(pluginClassLoader != contextClassLoader)) {

				currentThread.setContextClassLoader(pluginClassLoader);
			}

			return doProcessTemplate(
				httpServletRequest, httpServletResponse, portletId, templateId,
				content, langType, false);
		}
		finally {
			if ((pluginClassLoader != null) &&
				(pluginClassLoader != contextClassLoader)) {

				currentThread.setContextClassLoader(contextClassLoader);
			}
		}
	}

	protected StringBundler doProcessTemplate(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String portletId,
			String templateId, String content, String langType,
			boolean restricted)
		throws Exception {

		TemplateProcessor processor = new TemplateProcessor(
			httpServletRequest, httpServletResponse, portletId);

		TemplateManager templateManager =
			TemplateManagerUtil.getTemplateManager(langType);

		Template template = templateManager.getTemplate(
			new StringTemplateResource(templateId, content), restricted);

		template.put("processor", processor);

		// Velocity variables

		template.prepare(httpServletRequest);

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		template.prepareTaglib(
			httpServletRequest,
			new PipingServletResponse(httpServletResponse, unsyncStringWriter));

		try {
			template.processTemplate(unsyncStringWriter);
		}
		catch (Exception exception) {
			_log.error(exception);

			throw exception;
		}

		Map<Integer, List<PortletRenderer>> portletRenderersMap =
			processor.getPortletRenderers();

		Map<String, Map<String, Object>> portletHeaderRequestMap =
			new HashMap<>();

		for (Map.Entry<Integer, List<PortletRenderer>> entry :
				portletRenderersMap.entrySet()) {

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Processing portlets with render weight " + entry.getKey());
			}

			List<PortletRenderer> portletRenderers = entry.getValue();

			StopWatch stopWatch = new StopWatch();

			stopWatch.start();

			if (_log.isDebugEnabled()) {
				_log.debug("Start serial header phase");
			}

			for (PortletRenderer portletRenderer : portletRenderers) {
				Portlet portletModel = portletRenderer.getPortlet();

				if (!portletModel.isReady()) {
					continue;
				}

				javax.portlet.Portlet portlet =
					PortletBagUtil.getPortletInstance(
						httpServletRequest.getServletContext(), portletModel,
						portletModel.getRootPortletId());

				if (!PortletTypeUtil.isHeaderPortlet(portlet)) {
					continue;
				}

				Map<String, Object> headerRequestMap =
					portletRenderer.renderHeaders(
						httpServletRequest, httpServletResponse,
						portletModel.getHeaderRequestAttributePrefixes());

				String rendererPortletId = portletModel.getPortletId();

				portletHeaderRequestMap.put(
					rendererPortletId, headerRequestMap);

				if (_log.isDebugEnabled()) {
					_log.debug(
						StringBundler.concat(
							"Serially rendered headers for portlet ",
							rendererPortletId, " in ", stopWatch.getTime(),
							" ms"));
				}
			}

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Finished serial header phase in " + stopWatch.getTime() +
						" ms");
			}
		}

		Map<String, StringBundler> contentsMap = new HashMap<>();

		for (Map.Entry<Integer, List<PortletRenderer>> entry :
				portletRenderersMap.entrySet()) {

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Processing portlets with render weight " + entry.getKey());
			}

			List<PortletRenderer> portletRenderers = entry.getValue();

			StopWatch stopWatch = new StopWatch();

			stopWatch.start();

			if (_log.isDebugEnabled()) {
				_log.debug("Start serial rendering");
			}

			for (PortletRenderer portletRenderer : portletRenderers) {
				Portlet portlet = portletRenderer.getPortlet();

				String rendererPortletId = portlet.getPortletId();

				contentsMap.put(
					rendererPortletId,
					portletRenderer.render(
						httpServletRequest, httpServletResponse,
						portletHeaderRequestMap.get(rendererPortletId)));

				if (_log.isDebugEnabled()) {
					_log.debug(
						StringBundler.concat(
							"Serially rendered portlet ", rendererPortletId,
							" in ", stopWatch.getTime(), " ms"));
				}
			}

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Finished serial rendering in " + stopWatch.getTime() +
						" ms");
			}
		}

		return StringUtil.replaceWithStringBundler(
			unsyncStringWriter.toString(), "[$TEMPLATE_PORTLET_", "$]",
			contentsMap);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RuntimePageImpl.class);

}