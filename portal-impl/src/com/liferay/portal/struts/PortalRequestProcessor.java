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

package com.liferay.portal.struts;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.LayoutPermissionException;
import com.liferay.portal.kernel.exception.PortletActiveException;
import com.liferay.portal.kernel.exception.UserActiveException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletPreferencesIds;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserTracker;
import com.liferay.portal.kernel.model.UserTrackerPath;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.portal.kernel.portlet.InvokerPortlet;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.LiferayRenderRequest;
import com.liferay.portal.kernel.portlet.LiferayRenderResponse;
import com.liferay.portal.kernel.portlet.PortletConfigFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletInstanceFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.auth.InterruptedPortletRequestWhitelistUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.service.persistence.UserTrackerPathUtil;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.struts.LastPath;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.liveusers.LiveUsers;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.LiferayPortletUtil;
import com.liferay.portlet.RenderRequestFactory;
import com.liferay.portlet.RenderResponseFactory;

import java.io.IOException;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.action.InvalidCancelException;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

/**
 * @author Brian Wing Shun Chan
 * @author Jorge Ferrer
 * @author Wesley Gong
 * @author Mika Koivisto
 * @author Neil Griffin
 */
public class PortalRequestProcessor {

	public static final String INCLUDE_PATH_INFO =
		"javax.servlet.include.path_info";

	public static final String INCLUDE_SERVLET_PATH =
		"javax.servlet.include.servlet_path";

	public PortalRequestProcessor(
		ActionServlet actionServlet, ModuleConfig moduleConfig) {

		_actionServlet = actionServlet;
		_moduleConfig = moduleConfig;

		ServletContext servletContext = actionServlet.getServletContext();

		_definitions = (Map<String, Definition>)servletContext.getAttribute(
			TilesUtil.DEFINITIONS);

		// auth.forward.last.path.

		_lastPaths = new HashSet<>(
			Arrays.asList(
				PropsUtil.getArray(PropsKeys.AUTH_FORWARD_LAST_PATHS)));

		_lastPaths.add(_PATH_PORTAL_LAYOUT);

		// auth.public.path.

		_publicPaths = new HashSet<>();

		_publicPaths.add(_PATH_C);
		_publicPaths.add(_PATH_PORTAL_API_JSONWS);
		_publicPaths.add(_PATH_PORTAL_FLASH);
		_publicPaths.add(_PATH_PORTAL_J_LOGIN);
		_publicPaths.add(_PATH_PORTAL_LAYOUT);
		_publicPaths.add(_PATH_PORTAL_LICENSE);
		_publicPaths.add(_PATH_PORTAL_LOGIN);
		_publicPaths.add(_PATH_PORTAL_RENDER_PORTLET);
		_publicPaths.add(_PATH_PORTAL_RESILIENCY);
		_publicPaths.add(_PATH_PORTAL_TCK);
		_publicPaths.add(_PATH_PORTAL_UPDATE_LANGUAGE);
		_publicPaths.add(_PATH_PORTAL_UPDATE_PASSWORD);
		_publicPaths.add(_PATH_PORTAL_VERIFY_EMAIL_ADDRESS);
		_publicPaths.add(PropsValues.AUTH_LOGIN_DISABLED_PATH);

		_trackerIgnorePaths = new HashSet<>(
			Arrays.asList(
				PropsUtil.getArray(PropsKeys.SESSION_TRACKER_IGNORE_PATHS)));
	}

	public void process(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		String path = _findPath(request, response);

		ActionMapping actionMapping =
			(ActionMapping)_moduleConfig.findActionConfig(path);

		if ((actionMapping == null) &&
			(StrutsActionRegistryUtil.getAction(path) == null)) {

			String lastPath = _getLastPath(request);

			if (_log.isDebugEnabled()) {
				_log.debug("Last path " + lastPath);
			}

			response.sendRedirect(lastPath);

			return;
		}

		_process(request, response);

		try {
			if (_isPortletPath(path)) {
				_cleanUp(request);
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	private void _cleanUp(HttpServletRequest request) throws Exception {

		// Clean up portlet objects that may have been created by defineObjects
		// for portlets that are called directly from a Struts path

		LiferayRenderRequest liferayRenderRequest =
			(LiferayRenderRequest)LiferayPortletUtil.getLiferayPortletRequest(
				(PortletRequest)request.getAttribute(
					JavaConstants.JAVAX_PORTLET_REQUEST));

		if (liferayRenderRequest != null) {
			liferayRenderRequest.cleanUp();
		}
	}

	private void _defineObjects(
			HttpServletRequest request, HttpServletResponse response,
			Portlet portlet)
		throws Exception {

		String portletId = portlet.getPortletId();

		ServletContext servletContext = (ServletContext)request.getAttribute(
			WebKeys.CTX);

		InvokerPortlet invokerPortlet = PortletInstanceFactoryUtil.create(
			portlet, servletContext);

		PortletPreferencesIds portletPreferencesIds =
			PortletPreferencesFactoryUtil.getPortletPreferencesIds(
				request, portletId);

		PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.getStrictPreferences(
				portletPreferencesIds);

		PortletConfig portletConfig = PortletConfigFactoryUtil.create(
			portlet, servletContext);

		PortletContext portletContext = portletConfig.getPortletContext();

		LiferayRenderRequest liferayRenderRequest = RenderRequestFactory.create(
			request, portlet, invokerPortlet, portletContext,
			WindowState.MAXIMIZED, PortletMode.VIEW, portletPreferences);

		LiferayRenderResponse liferayRenderResponse =
			RenderResponseFactory.create(liferayRenderRequest, response);

		liferayRenderRequest.defineObjects(
			portletConfig, liferayRenderResponse);

		request.setAttribute(WebKeys.PORTLET_STRUTS_EXECUTE, Boolean.TRUE);
	}

	private ActionMapping _findMapping(
			HttpServletRequest request, HttpServletResponse response,
			String path)
		throws IOException {

		ActionMapping actionMapping =
			(ActionMapping)_moduleConfig.findActionConfig(path);

		if (actionMapping != null) {
			request.setAttribute(Globals.MAPPING_KEY, actionMapping);

			return actionMapping;
		}

		for (ActionConfig actionConfig : _moduleConfig.findActionConfigs()) {
			if (actionConfig.getUnknown()) {
				request.setAttribute(Globals.MAPPING_KEY, actionConfig);

				return (ActionMapping)actionConfig;
			}
		}

		MessageResources messageResources = _actionServlet.getInternal();

		response.sendError(
			HttpServletResponse.SC_NOT_FOUND,
			messageResources.getMessage("processInvalid"));

		return null;
	}

	private String _findPath(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		String path = (String)request.getAttribute(INCLUDE_PATH_INFO);

		if (path == null) {
			path = request.getPathInfo();
		}

		if ((path != null) && (path.length() > 0)) {
			return path;
		}

		path = (String)request.getAttribute(INCLUDE_SERVLET_PATH);

		if (path == null) {
			path = request.getServletPath();
		}

		String prefix = _moduleConfig.getPrefix();

		if (!path.startsWith(prefix)) {
			MessageResources messageResources = _actionServlet.getInternal();

			String message = messageResources.getMessage("processPath");

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);

			return null;
		}

		path = path.substring(prefix.length());

		int periodIndex = path.lastIndexOf(CharPool.PERIOD);
		int slashIndex = path.lastIndexOf(CharPool.SLASH);

		if ((periodIndex >= 0) && (periodIndex > slashIndex)) {
			path = path.substring(0, periodIndex);
		}

		return path;
	}

	private String _getFriendlyTrackerPath(
			String path, ThemeDisplay themeDisplay, HttpServletRequest request)
		throws Exception {

		if (!path.equals(_PATH_PORTAL_LAYOUT)) {
			return null;
		}

		long plid = ParamUtil.getLong(request, "p_l_id");

		if (plid == 0) {
			return null;
		}

		Layout layout = LayoutLocalServiceUtil.getLayout(plid);

		String layoutFriendlyURL = PortalUtil.getLayoutFriendlyURL(
			layout, themeDisplay);

		String portletId = ParamUtil.getString(request, "p_p_id");

		if (Validator.isNull(portletId)) {
			return layoutFriendlyURL;
		}

		long companyId = PortalUtil.getCompanyId(request);

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			companyId, portletId);

		if (portlet == null) {
			String strutsPath = path.substring(
				1, path.lastIndexOf(CharPool.SLASH));

			portlet = PortletLocalServiceUtil.getPortletByStrutsPath(
				companyId, strutsPath);
		}

		if ((portlet == null) || !portlet.isActive()) {
			return layoutFriendlyURL.concat(StringPool.QUESTION).concat(
				request.getQueryString());
		}

		FriendlyURLMapper friendlyURLMapper =
			portlet.getFriendlyURLMapperInstance();

		if (friendlyURLMapper == null) {
			return layoutFriendlyURL.concat(StringPool.QUESTION).concat(
				request.getQueryString());
		}

		String namespace = PortalUtil.getPortletNamespace(portletId);

		LiferayPortletURL portletURL = PortletURLFactoryUtil.create(
			request, portlet, layout, PortletRequest.RENDER_PHASE);

		Map<String, String[]> parameterMap = request.getParameterMap();

		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String key = entry.getKey();

			if (key.startsWith(namespace)) {
				key = key.substring(namespace.length());

				portletURL.setParameter(key, entry.getValue());
			}
		}

		String portletFriendlyURL = friendlyURLMapper.buildPath(portletURL);

		if (portletFriendlyURL != null) {
			return layoutFriendlyURL.concat(portletFriendlyURL);
		}

		return layoutFriendlyURL.concat(StringPool.QUESTION).concat(
			request.getQueryString());
	}

	private String _getLastPath(HttpServletRequest request) {
		HttpSession session = request.getSession();

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Boolean httpsInitial = (Boolean)session.getAttribute(
			WebKeys.HTTPS_INITIAL);

		String portalURL = null;

		if (PropsValues.COMPANY_SECURITY_AUTH_REQUIRES_HTTPS &&
			!PropsValues.SESSION_ENABLE_PHISHING_PROTECTION &&
			(httpsInitial != null) && !httpsInitial.booleanValue()) {

			portalURL = PortalUtil.getPortalURL(request, false);
		}
		else {
			portalURL = PortalUtil.getPortalURL(request);
		}

		StringBundler sb = new StringBundler(7);

		sb.append(portalURL);
		sb.append(themeDisplay.getPathMain());
		sb.append(_PATH_PORTAL_LAYOUT);

		if (!PropsValues.AUTH_FORWARD_BY_LAST_PATH) {
			if (request.getRemoteUser() != null) {

				// If we do not forward by last path and the user is logged in,
				// forward to the user's default layout to prevent a lagging
				// loop

				sb.append(StringPool.QUESTION);
				sb.append("p_l_id");
				sb.append(StringPool.EQUAL);
				sb.append(LayoutConstants.DEFAULT_PLID);
			}

			return sb.toString();
		}

		LastPath lastPath = (LastPath)session.getAttribute(WebKeys.LAST_PATH);

		if (lastPath == null) {
			return sb.toString();
		}

		String parameters = lastPath.getParameters();

		// Only test for existing mappings for last paths that were set when the
		// user accessed a layout directly instead of through its friendly URL

		String contextPath = lastPath.getContextPath();

		if (contextPath.equals(themeDisplay.getPathMain())) {
			ActionMapping actionMapping =
				(ActionMapping)_moduleConfig.findActionConfig(
					lastPath.getPath());

			if ((actionMapping == null) || parameters.isEmpty()) {
				return sb.toString();
			}
		}

		StringBundler lastPathSB = new StringBundler(4);

		lastPathSB.append(portalURL);
		lastPathSB.append(lastPath.getContextPath());
		lastPathSB.append(lastPath.getPath());
		lastPathSB.append(parameters);

		return lastPathSB.toString();
	}

	private Action _getOriginalAction(
		HttpServletResponse response, ActionMapping actionMapping) {

		return _actions.computeIfAbsent(
			actionMapping.getType(),
			classNameKey -> {
				try {
					Action action = (Action)RequestUtils.applicationInstance(
						classNameKey);

					if (action.getServlet() == null) {
						action.setServlet(_actionServlet);
					}

					return action;
				}
				catch (Exception e) {
					MessageResources messageResources =
						_actionServlet.getInternal();

					try {
						response.sendError(
							HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							messageResources.getMessage(
								"actionCreate", actionMapping.getPath()));
					}
					catch (IOException ioe) {
						ReflectionUtil.throwException(ioe);
					}

					return null;
				}
			});
	}

	private void _internalModuleRelativeForward(
			String uri, HttpServletRequest request,
			HttpServletResponse response)
		throws IOException, ServletException {

		Definition definition = _definitions.get(uri);

		if (definition != null) {
			request.setAttribute(TilesUtil.DEFINITION, definition);

			uri = definition.getPath();
		}

		StrutsUtil.forward(
			uri, _actionServlet.getServletContext(), request, response);
	}

	private boolean _isPortletPath(String path) {
		if ((path != null) && !path.equals(_PATH_C) &&
			!path.startsWith(_PATH_COMMON) &&
			!path.contains(_PATH_J_SECURITY_CHECK) &&
			!path.startsWith(_PATH_PORTAL)) {

			return true;
		}

		return false;
	}

	private boolean _isPublicPath(String path) {
		if ((path != null) &&
			(_publicPaths.contains(path) || path.startsWith(_PATH_COMMON) ||
			 AuthPublicPathRegistry.contains(path))) {

			return true;
		}

		return false;
	}

	private void _process(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		String path = _processPath(request, response);

		if (path == null) {
			return;
		}

		_processLocale(request);

		response.setContentType("text/html; charset=UTF-8");

		_processCachedMessages(request);

		ActionMapping actionMapping = _processMapping(request, response, path);

		if (actionMapping == null) {
			return;
		}

		if (!_processRoles(request, response, actionMapping)) {
			return;
		}

		ActionForm actionForm = _processActionForm(request, actionMapping);

		if (actionForm != null) {
			actionForm.setServlet(_actionServlet);

			actionForm.reset(actionMapping, request);

			if (actionMapping.getMultipartClass() != null) {
				request.setAttribute(
					Globals.MULTIPART_KEY, actionMapping.getMultipartClass());
			}

			RequestUtils.populate(
				actionForm, actionMapping.getPrefix(),
				actionMapping.getSuffix(), request);
		}

		try {
			if (!_processValidate(
					request, response, actionForm, actionMapping)) {

				return;
			}
		}
		catch (InvalidCancelException ice) {
			ActionForward actionForward = _processException(
				request, response, ice, actionForm, actionMapping);

			if (actionForward != null) {
				_internalModuleRelativeForward(
					actionForward.getPath(), request, response);
			}

			return;
		}
		catch (IOException ioe) {
			throw ioe;
		}
		catch (ServletException se) {
			throw se;
		}

		if (!_processForward(request, response, actionMapping)) {
			return;
		}

		if (!_processInclude(request, response, actionMapping)) {
			return;
		}

		Action action = _processActionCreate(response, actionMapping);

		if (action == null) {
			return;
		}

		ActionForward actionForward = _processActionPerform(
			request, response, action, actionForm, actionMapping);

		if (actionForward != null) {
			_internalModuleRelativeForward(
				actionForward.getPath(), request, response);
		}
	}

	private Action _processActionCreate(
			HttpServletResponse response, ActionMapping actionMapping)
		throws IOException {

		ActionAdapter actionAdapter =
			(ActionAdapter)StrutsActionRegistryUtil.getAction(
				actionMapping.getPath());

		if (actionAdapter != null) {
			ActionConfig actionConfig = _moduleConfig.findActionConfig(
				actionMapping.getPath());

			if (actionConfig != null) {
				actionAdapter.setOriginalAction(
					_getOriginalAction(response, actionMapping));
			}

			return actionAdapter;
		}

		return _getOriginalAction(response, actionMapping);
	}

	private ActionForm _processActionForm(
		HttpServletRequest request, ActionMapping actionMapping) {

		ActionForm actionForm = RequestUtils.createActionForm(
			request, actionMapping, _moduleConfig, _actionServlet);

		if (actionForm == null) {
			return null;
		}

		if ("request".equals(actionMapping.getScope())) {
			request.setAttribute(actionMapping.getAttribute(), actionForm);
		}
		else {
			HttpSession session = request.getSession();

			session.setAttribute(actionMapping.getAttribute(), actionForm);
		}

		return actionForm;
	}

	private ActionForward _processActionPerform(
			HttpServletRequest request, HttpServletResponse response,
			Action action, ActionForm actionForm, ActionMapping actionMapping)
		throws IOException, ServletException {

		try {
			return action.execute(actionMapping, actionForm, request, response);
		}
		catch (Exception e) {
			return _processException(
				request, response, e, actionForm, actionMapping);
		}
	}

	private void _processCachedMessages(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return;
		}

		ActionMessages actionMessages = (ActionMessages)session.getAttribute(
			Globals.MESSAGE_KEY);

		if ((actionMessages != null) && actionMessages.isAccessed()) {
			session.removeAttribute(Globals.MESSAGE_KEY);
		}

		actionMessages = (ActionMessages)session.getAttribute(
			Globals.ERROR_KEY);

		if ((actionMessages != null) && actionMessages.isAccessed()) {
			session.removeAttribute(Globals.ERROR_KEY);
		}
	}

	private ActionForward _processException(
			HttpServletRequest request, HttpServletResponse response,
			Exception exception, ActionForm actionForm,
			ActionMapping actionMapping)
		throws IOException, ServletException {

		ExceptionConfig exceptionConfig = actionMapping.findException(
			exception.getClass());

		if (exceptionConfig == null) {
			if (exception instanceof IOException) {
				throw (IOException)exception;
			}
			else if (exception instanceof ServletException) {
				throw (ServletException)exception;
			}
			else {
				throw new ServletException(exception);
			}
		}

		try {
			ExceptionHandler exceptionHandler =
				(ExceptionHandler)RequestUtils.applicationInstance(
					exceptionConfig.getHandler());

			return exceptionHandler.execute(
				exception, exceptionConfig, actionMapping, actionForm, request,
				response);
		}
		catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private boolean _processForward(
			HttpServletRequest request, HttpServletResponse response,
			ActionMapping actionMapping)
		throws IOException, ServletException {

		String forward = actionMapping.getForward();

		if (forward == null) {
			return true;
		}

		String actionIdPath = RequestUtils.actionIdURL(
			forward, _moduleConfig, _actionServlet);

		if (actionIdPath != null) {
			forward = actionIdPath;
		}

		_internalModuleRelativeForward(forward, request, response);

		return false;
	}

	private boolean _processInclude(
			HttpServletRequest request, HttpServletResponse response,
			ActionMapping actionMapping)
		throws IOException, ServletException {

		String include = actionMapping.getInclude();

		if (include == null) {
			return true;
		}

		String actionIdPath = RequestUtils.actionIdURL(
			include, _moduleConfig, _actionServlet);

		if (actionIdPath != null) {
			include = actionIdPath;
		}

		StrutsUtil.include(
			_moduleConfig.getPrefix() + include,
			_actionServlet.getServletContext(), request, response);

		return false;
	}

	private void _processLocale(HttpServletRequest request) {
		HttpSession session = request.getSession();

		if (session.getAttribute(Globals.LOCALE_KEY) != null) {
			return;
		}

		Locale locale = request.getLocale();

		if (locale != null) {
			session.setAttribute(Globals.LOCALE_KEY, locale);
		}
	}

	private ActionMapping _processMapping(
			HttpServletRequest request, HttpServletResponse response,
			String path)
		throws IOException {

		if (path == null) {
			return null;
		}

		Action action = StrutsActionRegistryUtil.getAction(path);

		if (action != null) {
			ActionMapping actionMapping =
				(ActionMapping)_moduleConfig.findActionConfig(path);

			if (actionMapping == null) {
				actionMapping = new ActionMapping();

				actionMapping.setModuleConfig(_moduleConfig);
				actionMapping.setPath(path);

				request.setAttribute(Globals.MAPPING_KEY, actionMapping);
			}

			return actionMapping;
		}

		ActionMapping actionMapping = _findMapping(request, response, path);

		if (actionMapping == null) {
			MessageResources messageResources = _actionServlet.getInternal();

			String msg = messageResources.getMessage("processInvalid");

			_log.error("User ID " + request.getRemoteUser());
			_log.error("Current URL " + PortalUtil.getCurrentURL(request));
			_log.error("Referer " + request.getHeader("Referer"));
			_log.error("Remote address " + request.getRemoteAddr());

			_log.error(msg + " " + path);
		}

		return actionMapping;
	}

	private String _processPath(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		String path = GetterUtil.getString(_findPath(request, response));

		HttpSession session = request.getSession();

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		// Current users

		UserTracker userTracker = LiveUsers.getUserTracker(
			themeDisplay.getCompanyId(), session.getId());

		if ((userTracker != null) && !path.equals(_PATH_C) &&
			!path.contains(_PATH_J_SECURITY_CHECK) &&
			!path.contains(_PATH_PORTAL_PROTECTED) &&
			!_trackerIgnorePaths.contains(path)) {

			String fullPath = null;

			try {
				if (PropsValues.SESSION_TRACKER_FRIENDLY_PATHS_ENABLED) {
					fullPath = _getFriendlyTrackerPath(
						path, themeDisplay, request);
				}
			}
			catch (Exception e) {
				_log.error(e, e);
			}

			String fullPathWithoutQueryString = fullPath;

			if (Validator.isNull(fullPath)) {
				String queryString = request.getQueryString();

				fullPathWithoutQueryString = path;

				if (Validator.isNotNull(queryString)) {
					fullPath = path.concat(StringPool.QUESTION).concat(
						queryString);
				}
				else {
					fullPath = path;
				}
			}

			int pos = fullPathWithoutQueryString.indexOf(StringPool.QUESTION);

			if (pos != -1) {
				fullPathWithoutQueryString =
					fullPathWithoutQueryString.substring(0, pos);
			}

			if (!_trackerIgnorePaths.contains(fullPathWithoutQueryString)) {
				UserTrackerPath userTrackerPath = UserTrackerPathUtil.create(0);

				userTrackerPath.setUserTrackerId(
					userTracker.getUserTrackerId());
				userTrackerPath.setPath(fullPath);
				userTrackerPath.setPathDate(new Date());

				userTracker.addPath(userTrackerPath);
			}
		}

		String remoteUser = request.getRemoteUser();

		User user = null;

		try {
			user = PortalUtil.getUser(request);
		}
		catch (Exception e) {
		}

		// Last path

		if (_lastPaths.contains(path) && !_trackerIgnorePaths.contains(path)) {
			boolean saveLastPath = ParamUtil.getBoolean(
				request, "saveLastPath", true);

			if (themeDisplay.isLifecycleResource() ||
				themeDisplay.isStateExclusive() ||
				themeDisplay.isStatePopUp() ||
				!StringUtil.equalsIgnoreCase(
					request.getMethod(), HttpMethods.GET)) {

				saveLastPath = false;
			}

			// Save last path

			if (saveLastPath) {

				// Was a last path set by another servlet that dispatched to the
				// MainServlet? If so, use that last path instead.

				LastPath lastPath = (LastPath)request.getAttribute(
					WebKeys.LAST_PATH);

				if (lastPath == null) {
					lastPath = new LastPath(
						themeDisplay.getPathMain(), path,
						HttpUtil.parameterMapToString(
							request.getParameterMap()));
				}

				session.setAttribute(WebKeys.LAST_PATH, lastPath);
			}
		}

		// Setup wizard

		if (PropsValues.SETUP_WIZARD_ENABLED) {
			if (!path.equals(_PATH_PORTAL_LICENSE) &&
				!path.equals(_PATH_PORTAL_STATUS)) {

				return _PATH_PORTAL_SETUP_WIZARD;
			}
		}
		else if (path.equals(_PATH_PORTAL_SETUP_WIZARD)) {
			return _PATH_PORTAL_LAYOUT;
		}

		if ((remoteUser != null) || (user != null)) {

			// Authenticated users can always log out

			if (path.equals(_PATH_PORTAL_LOGOUT)) {
				return path;
			}

			// Authenticated users can always extend or confirm their session

			if (path.equals(_PATH_PORTAL_EXPIRE_SESSION) ||
				path.equals(_PATH_PORTAL_EXTEND_SESSION)) {

				return path;
			}

			// Authenticated users can always update their language

			if (path.equals(_PATH_PORTAL_UPDATE_LANGUAGE)) {
				return path;
			}

			// Authenticated users can always agree to terms of use

			if (path.equals(_PATH_PORTAL_UPDATE_TERMS_OF_USE)) {
				return path;
			}
		}

		// Authenticated users must still exist in the system

		if ((remoteUser != null) && (user == null)) {
			return _PATH_PORTAL_LOGOUT;
		}

		long companyId = PortalUtil.getCompanyId(request);
		String portletId = ParamUtil.getString(request, "p_p_id");

		// Authenticated users must be active

		if (user != null) {
			if (!user.isActive()) {
				SessionErrors.add(session, UserActiveException.class.getName());

				return _PATH_PORTAL_ERROR;
			}

			if (!path.equals(_PATH_PORTAL_JSON_SERVICE) &&
				!path.equals(_PATH_PORTAL_RENDER_PORTLET) &&
				!themeDisplay.isImpersonated() &&
				!InterruptedPortletRequestWhitelistUtil.
					isPortletInvocationWhitelisted(
						companyId, portletId,
						PortalUtil.getStrutsAction(request))) {

				// Authenticated users should agree to Terms of Use

				if (!user.isTermsOfUseComplete()) {
					return _PATH_PORTAL_TERMS_OF_USE;
				}

				// Authenticated users should have a verified email address

				if (!user.isEmailAddressVerificationComplete()) {
					if (path.equals(_PATH_PORTAL_UPDATE_EMAIL_ADDRESS)) {
						return _PATH_PORTAL_UPDATE_EMAIL_ADDRESS;
					}

					return _PATH_PORTAL_VERIFY_EMAIL_ADDRESS;
				}

				// Authenticated users must have a current password

				if (user.isPasswordReset()) {
					try {
						PasswordPolicy passwordPolicy =
							user.getPasswordPolicy();

						if ((passwordPolicy == null) ||
							passwordPolicy.isChangeable()) {

							return _PATH_PORTAL_UPDATE_PASSWORD;
						}
					}
					catch (Exception e) {
						_log.error(e, e);

						return _PATH_PORTAL_UPDATE_PASSWORD;
					}
				}
				else if (path.equals(_PATH_PORTAL_UPDATE_PASSWORD)) {
					return _PATH_PORTAL_LAYOUT;
				}

				// Authenticated users must have an email address

				if (!user.isEmailAddressComplete()) {
					return _PATH_PORTAL_UPDATE_EMAIL_ADDRESS;
				}

				// Authenticated users should have a reminder query

				if (!user.isDefaultUser() && !user.isReminderQueryComplete()) {
					return _PATH_PORTAL_UPDATE_REMINDER_QUERY;
				}
			}
		}
		else if (!_isPublicPath(path)) {

			// Users must sign in

			SessionErrors.add(session, PrincipalException.class.getName());

			return _PATH_PORTAL_LOGIN;
		}

		ActionMapping actionMapping =
			(ActionMapping)_moduleConfig.findActionConfig(path);

		if (actionMapping == null) {
			Action strutsAction = StrutsActionRegistryUtil.getAction(path);

			if (strutsAction == null) {
				return null;
			}
		}
		else {
			path = actionMapping.getPath();
		}

		// Define the portlet objects

		if (_isPortletPath(path)) {
			try {
				Portlet portlet = null;

				if (Validator.isNotNull(portletId)) {
					portlet = PortletLocalServiceUtil.getPortletById(
						companyId, portletId);
				}

				if (portlet == null) {
					String strutsPath = path.substring(
						1, path.lastIndexOf(CharPool.SLASH));

					portlet = PortletLocalServiceUtil.getPortletByStrutsPath(
						companyId, strutsPath);
				}

				if ((portlet != null) && portlet.isActive()) {
					_defineObjects(request, response, portlet);
				}
			}
			catch (Exception e) {
				request.setAttribute(PageContext.EXCEPTION, e);

				path = _PATH_COMMON_ERROR;
			}
		}

		// Authenticated users must have access to at least one layout

		if (SessionErrors.contains(
				session, LayoutPermissionException.class.getName())) {

			return _PATH_PORTAL_ERROR;
		}

		return path;
	}

	private boolean _processRoles(
			HttpServletRequest request, HttpServletResponse response,
			ActionMapping actionMapping)
		throws IOException, ServletException {

		String path = actionMapping.getPath();

		if (_isPublicPath(path)) {
			return true;
		}

		boolean authorized = true;

		User user = null;

		try {
			user = PortalUtil.getUser(request);
		}
		catch (Exception e) {
		}

		if ((user != null) && _isPortletPath(path)) {
			try {

				// Authenticated users can always log out

				if (path.equals(_PATH_PORTAL_LOGOUT)) {
					return true;
				}

				Portlet portlet = null;

				String portletId = ParamUtil.getString(request, "p_p_id");

				if (Validator.isNotNull(portletId)) {
					portlet = PortletLocalServiceUtil.getPortletById(
						user.getCompanyId(), portletId);
				}

				String strutsPath = path.substring(
					1, path.lastIndexOf(CharPool.SLASH));

				if (portlet != null) {
					if (!strutsPath.equals(portlet.getStrutsPath())) {
						throw new PrincipalException.MustBePortletStrutsPath(
							strutsPath, portletId);
					}
				}
				else {
					portlet = PortletLocalServiceUtil.getPortletByStrutsPath(
						user.getCompanyId(), strutsPath);
				}

				if ((portlet != null) && portlet.isActive() &&
					!portlet.isSystem()) {

					ThemeDisplay themeDisplay =
						(ThemeDisplay)request.getAttribute(
							WebKeys.THEME_DISPLAY);

					Layout layout = themeDisplay.getLayout();
					PermissionChecker permissionChecker =
						themeDisplay.getPermissionChecker();

					if (!PortletPermissionUtil.contains(
							permissionChecker, layout, portlet,
							ActionKeys.VIEW)) {

						throw new PrincipalException.MustHavePermission(
							permissionChecker, Portlet.class.getName(),
							portlet.getPortletId(), ActionKeys.VIEW);
					}
				}
				else if ((portlet != null) && !portlet.isActive()) {
					SessionErrors.add(
						request, PortletActiveException.class.getName());

					authorized = false;
				}
			}
			catch (Exception e) {
				SessionErrors.add(request, PrincipalException.class.getName());

				authorized = false;
			}
		}

		if (!authorized) {
			ForwardConfig forwardConfig = actionMapping.findForward(
				_PATH_PORTAL_ERROR);

			if (forwardConfig != null) {
				_internalModuleRelativeForward(
					forwardConfig.getPath(), request, response);
			}

			return false;
		}

		return true;
	}

	private boolean _processValidate(
			HttpServletRequest request, HttpServletResponse response,
			ActionForm actionForm, ActionMapping actionMapping)
		throws InvalidCancelException, IOException, ServletException {

		if (actionForm == null) {
			return true;
		}

		if (!actionMapping.getValidate()) {
			return true;
		}

		ActionMessages actionMessages = actionForm.validate(
			actionMapping, request);

		if ((actionMessages == null) || actionMessages.isEmpty()) {
			return true;
		}

		MultipartRequestHandler multipartRequestHandler =
			actionForm.getMultipartRequestHandler();

		if (multipartRequestHandler != null) {
			multipartRequestHandler.rollback();
		}

		String input = actionMapping.getInput();

		if (input == null) {
			MessageResources messageResources = _actionServlet.getInternal();

			response.sendError(
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				messageResources.getMessage(
					"noInput", actionMapping.getPath()));

			return false;
		}

		request.setAttribute(Globals.ERROR_KEY, actionMessages);

		_internalModuleRelativeForward(input, request, response);

		return false;
	}

	private static final String _PATH_C = "/c";

	private static final String _PATH_COMMON = "/common";

	private static final String _PATH_COMMON_ERROR = "/common/error";

	private static final String _PATH_J_SECURITY_CHECK = "/j_security_check";

	private static final String _PATH_PORTAL = "/portal";

	private static final String _PATH_PORTAL_API_JSONWS = "/portal/api/jsonws";

	private static final String _PATH_PORTAL_ERROR = "/portal/error";

	private static final String _PATH_PORTAL_EXPIRE_SESSION =
		"/portal/expire_session";

	private static final String _PATH_PORTAL_EXTEND_SESSION =
		"/portal/extend_session";

	private static final String _PATH_PORTAL_FLASH = "/portal/flash";

	private static final String _PATH_PORTAL_J_LOGIN = "/portal/j_login";

	private static final String _PATH_PORTAL_JSON_SERVICE =
		"/portal/json_service";

	private static final String _PATH_PORTAL_LAYOUT = "/portal/layout";

	private static final String _PATH_PORTAL_LICENSE = "/portal/license";

	private static final String _PATH_PORTAL_LOGIN = "/portal/login";

	private static final String _PATH_PORTAL_LOGOUT = "/portal/logout";

	private static final String _PATH_PORTAL_PROTECTED = "/portal/protected";

	private static final String _PATH_PORTAL_RENDER_PORTLET =
		"/portal/render_portlet";

	private static final String _PATH_PORTAL_RESILIENCY = "/portal/resiliency";

	private static final String _PATH_PORTAL_SETUP_WIZARD =
		"/portal/setup_wizard";

	private static final String _PATH_PORTAL_STATUS = "/portal/status";

	private static final String _PATH_PORTAL_TCK = "/portal/tck";

	private static final String _PATH_PORTAL_TERMS_OF_USE =
		"/portal/terms_of_use";

	private static final String _PATH_PORTAL_UPDATE_EMAIL_ADDRESS =
		"/portal/update_email_address";

	private static final String _PATH_PORTAL_UPDATE_LANGUAGE =
		"/portal/update_language";

	private static final String _PATH_PORTAL_UPDATE_PASSWORD =
		"/portal/update_password";

	private static final String _PATH_PORTAL_UPDATE_REMINDER_QUERY =
		"/portal/update_reminder_query";

	private static final String _PATH_PORTAL_UPDATE_TERMS_OF_USE =
		"/portal/update_terms_of_use";

	private static final String _PATH_PORTAL_VERIFY_EMAIL_ADDRESS =
		"/portal/verify_email_address";

	private static final Log _log = LogFactoryUtil.getLog(
		PortalRequestProcessor.class);

	private final Map<String, Action> _actions = new ConcurrentHashMap<>();
	private final ActionServlet _actionServlet;
	private final Map<String, Definition> _definitions;
	private final Set<String> _lastPaths;
	private final ModuleConfig _moduleConfig;
	private final Set<String> _publicPaths;
	private final Set<String> _trackerIgnorePaths;

}