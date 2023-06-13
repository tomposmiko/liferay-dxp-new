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
ObjectDefinition objectDefinition = (ObjectDefinition)request.getAttribute(ObjectWebKeys.OBJECT_DEFINITION);
ObjectDefinitionsFieldsDisplayContext objectDefinitionsFieldsDisplayContext = (ObjectDefinitionsFieldsDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
ObjectField objectField = (ObjectField)request.getAttribute(ObjectWebKeys.OBJECT_FIELD);
%>

<liferay-frontend:side-panel-content
	title='<%= LanguageUtil.get(request, "field") %>'
>
	<form action="javascript:;" id="<portlet:namespace />fm">
		<div class="side-panel-content">
			<div class="side-panel-content__body">
				<div class="sheet">
					<h2 class="sheet-title">
						<liferay-ui:message key="basic-info" />
					</h2>

					<aui:model-context bean="<%= objectField %>" model="<%= ObjectField.class %>" />

					<aui:input disabled="<%= !objectDefinitionsFieldsDisplayContext.hasUpdateObjectDefinitionPermission() %>" name="label" required="<%= true %>" value="<%= objectField.getLabel(themeDisplay.getLocale()) %>" />

					<aui:input disabled="<%= objectDefinition.isApproved() || !objectDefinitionsFieldsDisplayContext.hasUpdateObjectDefinitionPermission() || Validator.isNotNull(objectField.getRelationshipType()) %>" name="name" required="<%= true %>" value="<%= objectField.getName() %>" />

					<aui:select disabled="<%= objectDefinition.isApproved() || !objectDefinitionsFieldsDisplayContext.hasUpdateObjectDefinitionPermission() || Validator.isNotNull(objectField.getRelationshipType()) %>" name="type" required="<%= true %>">
						<aui:option label="BigDecimal" selected='<%= Objects.equals(objectField.getType(), "BigDecimal") %>' value="BigDecimal" />
						<aui:option label="Boolean" selected='<%= Objects.equals(objectField.getType(), "Boolean") %>' value="Boolean" />

						<c:choose>
							<c:when test="<%= objectDefinitionsFieldsDisplayContext.isFFClobObjectFieldTypeConfigurationEnabled() %>">
								<aui:option label="Clob" selected='<%= Objects.equals(objectField.getType(), "Clob") %>' value="Clob" />
							</c:when>
						</c:choose>

						<aui:option label="Date" selected='<%= Objects.equals(objectField.getType(), "Date") %>' value="Date" />
						<aui:option label="Double" selected='<%= Objects.equals(objectField.getType(), "Double") %>' value="Double" />
						<aui:option label="Integer" selected='<%= Objects.equals(objectField.getType(), "Integer") %>' value="Integer" />
						<aui:option label="Long" selected='<%= Objects.equals(objectField.getType(), "Long") %>' value="Long" />
						<aui:option label="String" selected='<%= Objects.equals(objectField.getType(), "String") %>' value="String" />
					</aui:select>

					<aui:field-wrapper cssClass="form-group lfr-input-text-container">
						<aui:input disabled="<%= objectDefinition.isApproved() || !objectDefinitionsFieldsDisplayContext.hasUpdateObjectDefinitionPermission() %>" inlineLabel="right" label='<%= LanguageUtil.get(request, "mandatory") %>' labelCssClass="simple-toggle-switch" name="required" type="toggle-switch" value="<%= objectField.getRequired() %>" />
					</aui:field-wrapper>
				</div>

				<div class="mt-4 sheet" id="<portlet:namespace />searchableContainer" style="display: <%= Objects.equals(objectField.getType(), "Blob") ? "none;" : "block;" %>">
					<h2 class="sheet-title">
						<liferay-ui:message key="searchable" />
					</h2>

					<aui:field-wrapper cssClass="form-group lfr-input-text-container">
						<aui:input disabled="<%= objectDefinition.isApproved() || !objectDefinitionsFieldsDisplayContext.hasUpdateObjectDefinitionPermission() %>" inlineLabel="right" label='<%= LanguageUtil.get(request, "searchable") %>' labelCssClass="simple-toggle-switch" name="indexed" type="toggle-switch" value="<%= objectField.getIndexed() %>" />
					</aui:field-wrapper>

					<div id="<portlet:namespace />indexedGroup" style="display: <%= (Objects.equals(objectField.getType(), "String") && objectField.getIndexed()) ? "block;" : "none;" %>">
						<div class="form-group">
							<clay:radio
								checked="<%= objectField.getIndexed() && objectField.getIndexedAsKeyword() %>"
								disabled="<%= objectDefinition.isApproved() || !objectDefinitionsFieldsDisplayContext.hasUpdateObjectDefinitionPermission() %>"
								id='<%= liferayPortletResponse.getNamespace() + "inputIndexedTypeKeyword" %>'
								label='<%= LanguageUtil.get(request, "keyword") %>'
								name="indexedType"
							/>

							<clay:radio
								checked="<%= objectField.getIndexed() && !objectField.getIndexedAsKeyword() %>"
								disabled="<%= objectDefinition.isApproved() || !objectDefinitionsFieldsDisplayContext.hasUpdateObjectDefinitionPermission() %>"
								id='<%= liferayPortletResponse.getNamespace() + "inputIndexedTypeText" %>'
								label='<%= LanguageUtil.get(request, "text") %>'
								name="indexedType"
							/>
						</div>

						<div id="<portlet:namespace />indexedLanguageIdGroup" style="display: <%= (!objectField.getIndexed() || objectField.getIndexedAsKeyword()) ? "none;" : "block;" %>">
							<aui:select disabled="<%= objectDefinition.isApproved() || !objectDefinitionsFieldsDisplayContext.hasUpdateObjectDefinitionPermission() %>" label='<%= LanguageUtil.get(request, "language") %>' name="indexedLanguageId">

								<%
								for (Locale availableLocale : LanguageUtil.getAvailableLocales()) {
								%>

									<aui:option label="<%= availableLocale.getDisplayName(locale) %>" lang="<%= LocaleUtil.toW3cLanguageId(availableLocale) %>" selected="<%= LocaleUtil.toLanguageId(availableLocale).equals(objectField.getIndexedLanguageId()) %>" value="<%= LocaleUtil.toLanguageId(availableLocale) %>" />

								<%
								}
								%>

							</aui:select>
						</div>
					</div>
				</div>
			</div>

			<div class="side-panel-content__footer">
				<aui:button cssClass="btn-cancel mr-1" name="cancel" value='<%= LanguageUtil.get(request, "cancel") %>' />

				<aui:button disabled="<%= !objectDefinitionsFieldsDisplayContext.hasUpdateObjectDefinitionPermission() %>" name="save" type="submit" value='<%= LanguageUtil.get(request, "save") %>' />
			</div>
		</div>
	</form>
</liferay-frontend:side-panel-content>

<liferay-frontend:component
	context='<%=
		HashMapBuilder.<String, Object>put(
			"objectFieldId", objectField.getObjectFieldId()
		).build()
	%>'
	module="js/editObjectField"
/>