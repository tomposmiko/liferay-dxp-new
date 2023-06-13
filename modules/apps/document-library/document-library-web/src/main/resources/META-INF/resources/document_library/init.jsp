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

<%@ include file="/init.jsp" %>
<%@ taglib uri="http://liferay.com/tld/learn" prefix="liferay-learn" %><%@
taglib uri="http://liferay.com/tld/react" prefix="react" %>

<%@ page import="com.liferay.bulk.selection.BulkSelectionRunner" %><%@
page import="com.liferay.document.library.configuration.DLConfiguration" %><%@
page import="com.liferay.document.library.kernel.model.DLVersionNumberIncrease" %><%@
page import="com.liferay.document.library.web.internal.bulk.selection.BulkSelectionRunnerUtil" %><%@
page import="com.liferay.document.library.web.internal.display.context.DLAccessFromDesktopDisplayContext" %><%@
page import="com.liferay.document.library.web.internal.display.context.DLEditFileEntryTypeDataEngineDisplayContext" %><%@
page import="com.liferay.document.library.web.internal.display.context.DLEditFileEntryTypeDisplayContext" %><%@
page import="com.liferay.document.library.web.internal.display.context.DLEditFileShortcutDisplayContext" %><%@
page import="com.liferay.document.library.web.internal.display.context.DLFileEntryAdditionalMetadataSetsDisplayContext" %><%@
page import="com.liferay.document.library.web.internal.display.context.DLFileEntryTypeDetailsDisplayContext" %><%@
page import="com.liferay.document.library.web.internal.display.context.DLViewDisplayContext" %><%@
page import="com.liferay.document.library.web.internal.display.context.FolderActionDisplayContext" %><%@
page import="com.liferay.document.library.web.internal.search.DDMStructureRowChecker" %><%@
page import="com.liferay.document.library.web.internal.util.DLAssetHelperUtil" %><%@
page import="com.liferay.document.library.web.internal.util.RepositoryClassDefinitionUtil" %><%@
page import="com.liferay.dynamic.data.mapping.exception.RequiredStructureException" %><%@
page import="com.liferay.dynamic.data.mapping.util.DDMFormValuesToMapConverter" %><%@
page import="com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList" %><%@
page import="com.liferay.portal.kernel.lock.Lock" %><%@
page import="com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil" %><%@
page import="com.liferay.portal.kernel.util.LinkedHashMapBuilder" %><%@
page import="com.liferay.portal.kernel.util.LocaleUtil" %><%@
page import="com.liferay.portal.kernel.view.count.ViewCountManagerUtil" %><%@
page import="com.liferay.portal.util.RepositoryUtil" %>

<%@ page import="java.util.Collections" %>

<%
DLConfiguration dlConfiguration = ConfigurationProviderUtil.getSystemConfiguration(DLConfiguration.class);
DLRequestHelper dlRequestHelper = new DLRequestHelper(request);
%>

<%@ include file="/document_library/init-ext.jsp" %>