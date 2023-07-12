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
JournalArticle article = journalDisplayContext.getArticle();

JournalEditArticleDisplayContext journalEditArticleDisplayContext = new JournalEditArticleDisplayContext(request, liferayPortletResponse, article);

DDMStructure ddmStructure = journalEditArticleDisplayContext.getDDMStructure();
DDMTemplate ddmTemplate = journalEditArticleDisplayContext.getDDMTemplate();
%>

<aui:input name="ddmTemplateKey" type="hidden" value="<%= (ddmTemplate != null) ? ddmTemplate.getTemplateKey() : StringPool.BLANK %>" />

<c:choose>
	<c:when test="<%= ListUtil.isNotEmpty(ddmStructure.getTemplates()) %>">
		<p class="text-secondary"><liferay-ui:message key="this-template-will-be-used-when-showing-the-content-within-a-widget" /></p>

		<div class="form-group input-group mb-2">
			<div class="input-group-item">
				<input class="field form-control lfr-input-text" id="<portlet:namespace />ddmTemplateName" readonly="readonly" title="<%= LanguageUtil.get(request, "template-name") %>" type="text" value="<%= (ddmTemplate != null) ? HtmlUtil.escape(ddmTemplate.getName(locale)) : LanguageUtil.get(request, "no-template") %>" />
			</div>

			<c:if test="<%= (article != null) && !article.isNew() && (journalEditArticleDisplayContext.getClassNameId() == JournalArticleConstants.CLASS_NAME_ID_DEFAULT) %>">
				<div class="input-group-item input-group-item-shrink">
					<clay:button
						displayType="secondary"
						icon="view"
						id='<%= liferayPortletResponse.getNamespace() + "previewWithTemplate" %>'
					/>
				</div>
			</c:if>
		</div>

		<div class="form-group">
			<aui:button id="selectDDMTemplate" value="select" />

			<c:if test="<%= (ddmTemplate != null) && DDMTemplatePermission.contains(permissionChecker, ddmTemplate, ActionKeys.UPDATE) %>">
				<aui:button id="editDDMTemplate" value="edit" />
			</c:if>

			<c:if test="<%= ddmTemplate != null %>">
				<aui:button id="clearDDMTemplate" value="clear" />
			</c:if>
		</div>
	</c:when>
	<c:otherwise>
		<p class="text-secondary"><liferay-ui:message key="there-are-no-templates" /></p>
	</c:otherwise>
</c:choose>

<aui:script>
	<c:if test="<%= (article != null) && !article.isNew() && (journalEditArticleDisplayContext.getClassNameId() == JournalArticleConstants.CLASS_NAME_ID_DEFAULT) %>">
		<portlet:renderURL var="previewArticleContentTemplateURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
			<portlet:param name="mvcPath" value="/preview_article_content_template.jsp" />
			<portlet:param name="groupId" value="<%= String.valueOf(article.getGroupId()) %>" />
			<portlet:param name="articleId" value="<%= String.valueOf(article.getArticleId()) %>" />
			<portlet:param name="version" value="<%= String.valueOf(article.getVersion()) %>" />
		</portlet:renderURL>

		var previewWithTemplate = document.getElementById(
			'<portlet:namespace />previewWithTemplate'
		);

		if (previewWithTemplate) {
			previewWithTemplate.addEventListener('click', function (event) {
				var url = '<%= previewArticleContentTemplateURL %>';

				<%
				long ddmTemplateId = 0;

				if (ddmTemplate != null) {
					if (ddmTemplate.getTemplateId() == 0) {
						ddmTemplateId = -1;
					}
					else {
						ddmTemplateId = ddmTemplate.getTemplateId();
					}
				}
				%>

				var ddmTemplateId = '<%= ddmTemplateId %>';

				if (
					document.<portlet:namespace />fm1.<portlet:namespace />ddmTemplateId
						.value != ''
				) {
					ddmTemplateId =
						document.<portlet:namespace />fm1
							.<portlet:namespace />ddmTemplateId.value;
				}

				url = Liferay.Util.addParams(
					'<portlet:namespace />ddmTemplateId=' + ddmTemplateId,
					url
				);

				var languageId = '<%= themeDisplay.getLanguageId() %>';

				var inputComponent = Liferay.component(
					'<portlet:namespace />titleMapAsXML'
				);

				if (inputComponent) {
					languageId = inputComponent.getSelectedLanguageId();
				}

				url = Liferay.Util.addParams(
					'<portlet:namespace />languageId=' + languageId,
					url
				);

				Liferay.Util.openSelectionModal({
					onSelect: function (selectedItem) {
						changeDDMTemplate(selectedItem);
					},
					selectEventName: '<portlet:namespace />preview',
					title: '<liferay-ui:message key="preview" />',
					url: url,
				});
			});
		}
	</c:if>

	function changeDDMTemplate(newDDMTemplate) {
		var oldDDMTemplateId =
			'<%= (ddmTemplate != null) ? ddmTemplate.getTemplateId() : 0 %>';

		if (
			document.<portlet:namespace />fm1.<portlet:namespace />ddmTemplateId
				.value != ''
		) {
			oldDDMTemplateId =
				document.<portlet:namespace />fm1.<portlet:namespace />ddmTemplateId
					.value;
		}

		if (!newDDMTemplate || oldDDMTemplateId != newDDMTemplate.ddmtemplateid) {
			if (
				confirm(
					'<%= UnicodeLanguageUtil.get(request, "editing-the-current-template-deletes-all-unsaved-content") %>'
				)
			) {
				var uri = '<%= themeDisplay.getURLCurrent() %>';

				var ddmTemplateId =
					(newDDMTemplate && newDDMTemplate.ddmtemplateid) || -1;

				uri = Liferay.Util.addParams(
					'<portlet:namespace />ddmTemplateId=' + ddmTemplateId,
					uri
				);

				document.<portlet:namespace />fm1.<portlet:namespace />ddmTemplateId.value = ddmTemplateId;

				document.<portlet:namespace />fm1.<portlet:namespace />ddmTemplateKey.value =
					(newDDMTemplate && newDDMTemplate.ddmtemplatekey) || '';

				document.<portlet:namespace />fm1.<portlet:namespace />ddmTemplateName.value =
					(newDDMTemplate && newDDMTemplate.name) || '';

				submitForm(document.<portlet:namespace />fm1, uri, false, false);
			}
		}
	}

	var clearDDMTemplateButton = document.getElementById(
		'<portlet:namespace />clearDDMTemplate'
	);

	if (clearDDMTemplateButton) {
		clearDDMTemplateButton.addEventListener('click', function (event) {
			changeDDMTemplate();
		});
	}

	var selectDDMTemplateButton = document.getElementById(
		'<portlet:namespace />selectDDMTemplate'
	);

	if (selectDDMTemplateButton) {
		selectDDMTemplateButton.addEventListener('click', function (event) {
			Liferay.Util.openSelectionModal({
				onSelect: function (selectedItem) {
					changeDDMTemplate(selectedItem);
				},
				selectEventName: '<portlet:namespace />selectDDMTemplate',
				title: '<%= UnicodeLanguageUtil.get(request, "templates") %>',
				url:
					'<portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>"><portlet:param name="mvcPath" value="/select_ddm_template.jsp" /><portlet:param name="ddmStructureId" value="<%= String.valueOf(ddmStructure.getStructureId()) %>" /></portlet:renderURL>',
			});
		});
	}

	var editDDMTemplateLink = document.getElementById(
		'<portlet:namespace />editDDMTemplate'
	);

	if (editDDMTemplateLink) {
		editDDMTemplateLink.addEventListener('click', function (event) {
			if (
				confirm(
					'<%= UnicodeLanguageUtil.get(request, "editing-the-current-template-deletes-all-unsaved-content") %>'
				)
			) {
				Liferay.Util.navigate(
					'<portlet:renderURL><portlet:param name="mvcPath" value="/edit_ddm_template.jsp" /><portlet:param name="redirect" value="<%= themeDisplay.getURLCurrent() %>" /><portlet:param name="ddmTemplateId" value="<%= (ddmTemplate != null) ? String.valueOf(ddmTemplate.getTemplateId()) : StringPool.BLANK %>" /></portlet:renderURL>'
				);
			}
		});
	}
</aui:script>