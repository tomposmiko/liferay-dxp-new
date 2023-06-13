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
String backURL = ParamUtil.getString(request, "backURL", String.valueOf(renderResponse.createRenderURL()));

ObjectDefinitionsDetailsDisplayContext objectDefinitionsDetailsDisplayContext = (ObjectDefinitionsDetailsDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

ObjectDefinition objectDefinition = objectDefinitionsDetailsDisplayContext.getObjectDefinition();

List<ObjectField> accountEntryRelationshipObjectFields = objectDefinitionsDetailsDisplayContext.getAccountEntryRelationshipObjectFields();
List<ObjectField> nonrelationshipObjectFields = objectDefinitionsDetailsDisplayContext.getNonrelationshipObjectFields();

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(backURL);

renderResponse.setTitle(LanguageUtil.format(request, "edit-x", objectDefinition.getLabel(locale, true), false));
%>

<portlet:actionURL name="/object_definitions/edit_object_definition" var="editObjectDefinitionURL" />

<liferay-frontend:edit-form
	action="<%= editObjectDefinitionURL %>"
	name="fm"
>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

	<c:if test="<%= objectDefinition.isApproved() || objectDefinition.isSystem() %>">
		<aui:input name="enableObjectEntryHistory" type="hidden" value="<%= objectDefinition.isEnableObjectEntryHistory() %>" />
	</c:if>

	<liferay-frontend:edit-form-body>
		<liferay-ui:error exception="<%= ObjectDefinitionAccountEntryRestrictedObjectFieldIdException.class %>" message="if-activated-the-account-restriction-field-must-be-selected" />
		<liferay-ui:error exception="<%= ObjectDefinitionActiveException.class %>" />
		<liferay-ui:error exception="<%= ObjectDefinitionLabelException.class %>" />
		<liferay-ui:error exception="<%= ObjectDefinitionNameException.class %>" />
		<liferay-ui:error exception="<%= ObjectDefinitionNameException.MustBeginWithUpperCaseLetter.class %>" message="the-first-character-of-a-name-must-be-an-upper-case-letter" />
		<liferay-ui:error exception="<%= ObjectDefinitionNameException.MustBeLessThan41Characters.class %>" message="only-41-characters-are-allowed" />
		<liferay-ui:error exception="<%= ObjectDefinitionNameException.MustNotBeDuplicate.class %>" message="this-name-is-already-in-use-try-another-one" />
		<liferay-ui:error exception="<%= ObjectDefinitionNameException.MustOnlyContainLettersAndDigits.class %>" message="name-must-only-contain-letters-and-digits" />
		<liferay-ui:error exception="<%= ObjectDefinitionPluralLabelException.class %>" />
		<liferay-ui:error exception="<%= ObjectDefinitionScopeException.class %>" />
		<liferay-ui:error exception="<%= ObjectDefinitionStatusException.class %>" />
		<liferay-ui:error exception="<%= RequiredObjectFieldException.class %>" message="at-least-one-custom-field-must-be-added" />

		<aui:model-context bean="<%= objectDefinition %>" model="<%= ObjectDefinition.class %>" />

		<h2 class="sheet-title">
			<liferay-ui:message key="information" />
		</h2>

		<liferay-frontend:fieldset-group>
			<clay:sheet-section>
				<h3 class="sheet-subtitle">
					<liferay-ui:message key="object-definition-data" />
				</h3>

				<clay:row>
					<clay:col
						md="11"
					>
						<aui:input name="externalReferenceCode" type="hidden" />
						<aui:input name="objectDefinitionId" type="hidden" />

						<aui:input disabled="<%= objectDefinition.isApproved() || !objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission() %>" label="name" name="shortName" required="<%= true %>" type="text" value="<%= objectDefinition.getShortName() %>" />

						<aui:input disabled="<%= objectDefinition.isSystem() || !objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission() %>" name="label" />

						<aui:input disabled="<%= objectDefinition.isSystem() || !objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission() %>" name="pluralLabel" />

						<aui:input cssClass="disabled" label="object-definition-table-name" name="DBTableName" readonly="true" type="text" />
					</clay:col>
				</clay:row>

				<aui:field-wrapper cssClass="form-group lfr-input-text-container">
					<aui:input disabled="<%= !objectDefinition.isApproved() || objectDefinition.isSystem() || !objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission() %>" label="" labelOff="inactive" labelOn="active" name="active" type="toggle-switch" value="<%= objectDefinition.isActive() %>" />
				</aui:field-wrapper>
			</clay:sheet-section>

			<clay:sheet-section>
				<h3 class="sheet-subtitle">
					<liferay-ui:message key="entry-display" />
				</h3>

				<clay:row>
					<clay:col
						md="11"
					>
						<aui:select disabled="<%= !objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission() %>" name="titleObjectFieldId" showEmptyOption="<%= false %>">

							<%
							for (ObjectField objectField : nonrelationshipObjectFields) {
							%>

								<c:choose>
									<c:when test='<%= Objects.equals(objectField.getName(), "id") %>'>
										<aui:option label='<%= LanguageUtil.get(request, "id") %>' selected="<%= true %>" value="<%= objectField.getObjectFieldId() %>" />
									</c:when>
									<c:otherwise>
										<aui:option label="<%= HtmlUtil.escape(objectField.getLabel(locale)) %>" localizeLabel="<%= false %>" selected="<%= Objects.equals(objectField.getObjectFieldId(), objectDefinition.getTitleObjectFieldId()) %>" value="<%= objectField.getObjectFieldId() %>" />
									</c:otherwise>
								</c:choose>

							<%
							}
							%>

						</aui:select>
					</clay:col>
				</clay:row>

				<clay:row
					cssClass="hide"
				>
					<clay:col
						md="11"
					>
						<aui:select disabled="<%= objectDefinition.isSystem() || !objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission() %>" name="descriptionObjectFieldId" showEmptyOption="<%= true %>">

							<%
							for (ObjectField objectField : nonrelationshipObjectFields) {
							%>

								<aui:option label="<%= HtmlUtil.escape(objectField.getLabel(locale)) %>" selected="<%= Objects.equals(objectField.getObjectFieldId(), objectDefinition.getDescriptionObjectFieldId()) %>" value="<%= objectField.getObjectFieldId() %>" />

							<%
							}
							%>

						</aui:select>
					</clay:col>
				</clay:row>
			</clay:sheet-section>

			<clay:sheet-section>
				<h3 class="sheet-subtitle">
					<liferay-ui:message key="scope" />
				</h3>

				<clay:row>
					<clay:col
						md="11"
					>
						<aui:select disabled="<%= objectDefinition.isApproved() || !objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission() %>" name="scope" onChange='<%= "event.preventDefault(); " + liferayPortletResponse.getNamespace() + "selectScope();" %>' showEmptyOption="<%= false %>">

							<%
							for (ObjectScopeProvider objectScopeProvider : objectDefinitionsDetailsDisplayContext.getObjectScopeProviders()) {
							%>

								<aui:option label="<%= objectScopeProvider.getLabel(locale) %>" selected="<%= Objects.equals(objectScopeProvider.getKey(), objectDefinitionsDetailsDisplayContext.getScope()) %>" value="<%= objectScopeProvider.getKey() %>" />

							<%
							}
							%>

						</aui:select>
					</clay:col>
				</clay:row>

				<clay:row>
					<clay:col
						md="11"
					>
						<aui:select disabled="<%= objectDefinition.isSystem() || !objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission() %>" name="panelCategoryKey" showEmptyOption="<%= false %>">
							<aui:option label='<%= LanguageUtil.get(request, "choose-an-option") %>' selected="<%= true %>" value="" />

							<%
							for (KeyValuePair keyValuePair : objectDefinitionsDetailsDisplayContext.getKeyValuePairs()) {
							%>

								<aui:option label="<%= keyValuePair.getValue() %>" selected="<%= Objects.equals(keyValuePair.getKey(), objectDefinition.getPanelCategoryKey()) %>" value="<%= keyValuePair.getKey() %>" />

							<%
							}
							%>

						</aui:select>
					</clay:col>
				</clay:row>
			</clay:sheet-section>

			<clay:sheet-section
				cssClass='<%= objectDefinition.isSystem() ? "hide" : "" %>'
			>
				<h3 class="sheet-subtitle">
					<liferay-ui:message key="account-restriction" />
				</h3>

				<aui:field-wrapper cssClass="form-group lfr-input-text-container">
					<aui:input disabled="<%= ListUtil.isEmpty(accountEntryRelationshipObjectFields) || (objectDefinition.isAccountEntryRestricted() && objectDefinition.isApproved()) %>" label="" labelOff="inactive" labelOn="active" name="accountEntryRestricted" onChange='<%= "event.preventDefault(); " + liferayPortletResponse.getNamespace() + "handleAccountEntryRestrictionToggleChange();" %>' type="toggle-switch" value="<%= objectDefinition.isAccountEntryRestricted() %>" />
				</aui:field-wrapper>

				<clay:row>
					<clay:col
						md="11"
					>
						<aui:select disabled="<%= ListUtil.isEmpty(accountEntryRelationshipObjectFields) || !objectDefinition.isAccountEntryRestricted() || (objectDefinition.isAccountEntryRestricted() && objectDefinition.isApproved()) %>" name="accountEntryRestrictedObjectFieldId" showEmptyOption="<%= false %>">
							<aui:option label='<%= LanguageUtil.get(request, "choose-an-option") %>' selected="<%= true %>" value="0" />

							<%
							for (ObjectField accountEntryRelationshipObjectField : accountEntryRelationshipObjectFields) {
							%>

								<aui:option label="<%= accountEntryRelationshipObjectField.getLabel(locale) %>" selected="<%= Objects.equals(accountEntryRelationshipObjectField.getObjectFieldId(), objectDefinition.getTitleObjectFieldId()) %>" value="<%= accountEntryRelationshipObjectField.getObjectFieldId() %>" />

							<%
							}
							%>

						</aui:select>
					</clay:col>
				</clay:row>
			</clay:sheet-section>

			<clay:sheet-section>
				<h3 class="sheet-subtitle">
					<liferay-ui:message key="configuration" />
				</h3>

				<aui:field-wrapper cssClass="form-group lfr-input-text-container">
					<aui:input disabled="<%= objectDefinition.isSystem() || !objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission() %>" label="" labelOff="show-widget" labelOn="show-widget" name="portlet" type="toggle-switch" value="<%= objectDefinition.isPortlet() %>" />
				</aui:field-wrapper>

				<c:if test="<%= objectDefinition.isDefaultStorageType() %>">
					<aui:field-wrapper cssClass="form-group lfr-input-text-container">
						<aui:input disabled="<%= objectDefinition.isSystem() || !objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission() %>" label="" labelOff="enable-categorization" labelOn="enable-categorization" name="enableCategorization" type="toggle-switch" value="<%= objectDefinition.isEnableCategorization() %>" />
					</aui:field-wrapper>

					<aui:field-wrapper cssClass="form-group lfr-input-text-container">
						<aui:input disabled="<%= objectDefinition.isEnableComments() || objectDefinition.isSystem() || !objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission() %>" label="" labelOff="enable-comments" labelOn="enable-comments" name="enableComments" type="toggle-switch" value="<%= objectDefinition.isEnableComments() %>" />
					</aui:field-wrapper>

					<aui:field-wrapper cssClass="form-group lfr-input-text-container">
						<aui:input disabled="<%= objectDefinition.isApproved() || objectDefinition.isSystem() %>" label="" labelOff="enable-entry-history" labelOn="enable-entry-history" name="enableObjectEntryHistory" type="toggle-switch" value="<%= objectDefinition.isEnableObjectEntryHistory() %>" />
					</aui:field-wrapper>
				</c:if>
			</clay:sheet-section>

			<c:if test="<%= !objectDefinition.isDefaultStorageType() %>">
				<clay:sheet-section>
					<h3 class="sheet-subtitle">
						<liferay-ui:message key="external-data-source" />
					</h3>

					<clay:row>
						<clay:col
							md="11"
						>
							<aui:select disabled="<%= true %>" name="storageType" showEmptyOption="<%= false %>">
								<aui:option label="<%= LanguageUtil.get(request, objectDefinition.getStorageType()) %>" selected="<%= true %>" value="" />
							</aui:select>
						</clay:col>
					</clay:row>
				</clay:sheet-section>
			</c:if>
		</liferay-frontend:fieldset-group>
	</liferay-frontend:edit-form-body>
</liferay-frontend:edit-form>

<script>
	var accountEntryRestrictionToggleState =
		'<%= objectDefinition.isAccountEntryRestricted() %>' === 'true';

	function <portlet:namespace />handleAccountEntryRestrictionToggleChange() {
		const accountEntryRestrictionSelectElement = document.getElementById(
			'<portlet:namespace />accountEntryRestrictedObjectFieldId'
		);

		accountEntryRestrictionToggleState = !accountEntryRestrictionToggleState;

		if (accountEntryRestrictionToggleState) {
			accountEntryRestrictionSelectElement.className = 'form-control';
			accountEntryRestrictionSelectElement.removeAttribute('disabled');
		}
		else {
			accountEntryRestrictionSelectElement.className =
				'form-control disabled';
			accountEntryRestrictionSelectElement.setAttribute('disabled', '');
			accountEntryRestrictionSelectElement.value = '0';
		}
	}

	function <portlet:namespace />selectScope() {
		const scope = document.getElementById('<portlet:namespace />scope');

		let url = new URL(window.location.href);

		url.searchParams.set('<portlet:namespace />scope', scope.value);

		if (Liferay.SPA) {
			Liferay.SPA.app.navigate(url);
		}
		else {
			window.location.href = url;
		}
	}
</script>