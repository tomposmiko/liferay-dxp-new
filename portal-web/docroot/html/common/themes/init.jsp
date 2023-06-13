<%--
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
--%>

<%@ include file="/html/common/init.jsp" %>

<%@ page import="com.liferay.portal.kernel.cookies.constants.CookiesConstants" %><%@
page import="com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil" %><%@
page import="com.liferay.portal.kernel.model.Portlet" %><%@
page import="com.liferay.portal.kernel.model.portlet.PortletDependency" %><%@
page import="com.liferay.portal.kernel.portlet.render.PortletRenderUtil" %><%@
page import="com.liferay.portal.kernel.servlet.BrowserMetadata" %><%@
page import="com.liferay.portal.kernel.upload.configuration.UploadServletRequestConfigurationProviderUtil" %><%@
page import="com.liferay.portal.util.LayoutTypeAccessPolicyTracker" %><%@
page import="com.liferay.portlet.PortletTreeSet" %><%@
page import="com.liferay.portlet.internal.RenderStateUtil" %><%@
page import="com.liferay.taglib.aui.ScriptTag" %>

<%@ page import="java.util.Iterator" %><%@
page import="java.util.concurrent.ConcurrentHashMap" %>