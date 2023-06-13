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

<%
ImportDisplayContext importDisplayContext = new ImportDisplayContext(request, renderRequest);
%>

<portlet:actionURL name="/layout_page_template_admin/import" var="importURL">
	<portlet:param name="redirect" value="<%= currentURL %>" />
	<portlet:param name="portletResource" value="<%= portletDisplay.getId() %>" />
</portlet:actionURL>

<liferay-frontend:edit-form
	action="<%= importURL %>"
	enctype="multipart/form-data"
	name="fm"
>
	<liferay-frontend:edit-form-body>
		<liferay-ui:message key="import-help" />

		<a href="https://portal.liferay.dev/docs" target="_blank">
			<liferay-ui:message key="read-more" />
		</a>

		<br /><br />

		<liferay-frontend:fieldset>
			<aui:input label="file" name="file" type="file">
				<aui:validator name="required" />

				<aui:validator name="acceptFiles">
					'zip'
				</aui:validator>
			</aui:input>

			<aui:input checked="<%= true %>" label="overwrite-existing-page-templates" name="overwrite" type="checkbox" />
		</liferay-frontend:fieldset>

		<%
		Map<LayoutsImporterResultEntry.Status, List<LayoutsImporterResultEntry>> layoutsImporterResultEntryMap = importDisplayContext.getLayoutsImporterResultEntryMap();
		%>

		<c:if test="<%= MapUtil.isNotEmpty(layoutsImporterResultEntryMap) %>">

			<%
			String dialogType = importDisplayContext.getDialogType();
			%>

			<div class="alert alert-<%= dialogType %> <%= dialogType %>-dialog">
				<span class="font-weight-bold"><%= importDisplayContext.getDialogMessage() %></span>

				<ul>

					<%
					Map<Integer, List<LayoutsImporterResultEntry>> importedLayoutsImporterResultEntriesMap = importDisplayContext.getImportedLayoutsImporterResultEntriesMap();
					%>

					<c:if test="<%= MapUtil.isNotEmpty(importedLayoutsImporterResultEntriesMap) %>">

						<%
						for (Map.Entry<Integer, List<LayoutsImporterResultEntry>> entrySet : importedLayoutsImporterResultEntriesMap.entrySet()) {
						%>

							<li>
								<span class="font-italic"><%= HtmlUtil.escape(importDisplayContext.getSuccessMessage(entrySet)) %></span>
							</li>

						<%
						}
						%>

					</c:if>

					<%
					List<LayoutsImporterResultEntry> layoutsImporterResultEntriesWithWarnings = importDisplayContext.getLayoutsImporterResultEntriesWithWarnings();
					%>

					<c:if test="<%= ListUtil.isNotEmpty(layoutsImporterResultEntriesWithWarnings) %>">

						<%
						for (LayoutsImporterResultEntry layoutsImporterResultEntry : layoutsImporterResultEntriesWithWarnings) {
							String[] warningMessages = layoutsImporterResultEntry.getWarningMessages();
						%>

							<li>
								<span class="font-italic"><%= HtmlUtil.escape(importDisplayContext.getWarningMessage(layoutsImporterResultEntry.getName())) %></span>

								<ul>

									<%
									for (String warningMessage : warningMessages) {
									%>

										<li><span class="font-italic"><%= HtmlUtil.escape(warningMessage) %></span></li>

									<%
									}
									%>

								</ul>
							</li>

						<%
						}
						%>

					</c:if>

					<%
					int i = 0;

					List<LayoutsImporterResultEntry> notImportedLayoutsImporterResultEntries = importDisplayContext.getNotImportedLayoutsImporterResultEntries();
					%>

					<c:if test="<%= ListUtil.isNotEmpty(notImportedLayoutsImporterResultEntries) %>">

						<%
						for (; (i < notImportedLayoutsImporterResultEntries.size()) && (i < 10); i++) {
							LayoutsImporterResultEntry layoutsImporterResultEntry = notImportedLayoutsImporterResultEntries.get(i);
						%>

							<li>
								<span class="font-italic"><%= HtmlUtil.escape(layoutsImporterResultEntry.getErrorMessage()) %></span>
							</li>

						<%
						}
						%>

					</c:if>
				</ul>

				<c:if test="<%= notImportedLayoutsImporterResultEntries.size() > 10 %>">
					<span><%= LanguageUtil.format(request, "x-more-entries-could-also-not-be-imported", "<strong>" + (notImportedLayoutsImporterResultEntries.size() - i) + "</strong>", false) %></span>
				</c:if>
			</div>
		</c:if>
	</liferay-frontend:edit-form-body>

	<liferay-frontend:edit-form-footer>
		<liferay-frontend:edit-form-buttons
			submitLabel="import"
		/>
	</liferay-frontend:edit-form-footer>
</liferay-frontend:edit-form>