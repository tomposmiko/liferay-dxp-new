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
Portlet portlet = PortletLocalServiceUtil.getPortletById(portletDisplay.getId());

String refererWebDAVToken = WebDAVUtil.getStorageToken(portlet);

String redirect = ParamUtil.getString(request, "redirect");

DLEditDDMStructureDisplayContext dlEditDDMStructureDisplayContext = new DLEditDDMStructureDisplayContext(request, liferayPortletResponse);

com.liferay.dynamic.data.mapping.model.DDMStructure ddmStructure = dlEditDDMStructureDisplayContext.getDDMStructure();

long ddmStructureId = BeanParamUtil.getLong(ddmStructure, request, "structureId");

long groupId = BeanParamUtil.getLong(ddmStructure, request, "groupId", scopeGroupId);

boolean localizeTitle = true;
String title = LanguageUtil.format(request, "new-x", LanguageUtil.get(resourceBundle, "metadata-set"), false);

if (ddmStructure != null) {
	localizeTitle = false;
	title = LanguageUtil.format(request, "edit-x", ddmStructure.getName(locale), false);
}

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

renderResponse.setTitle(title);
%>

<portlet:actionURL name="/document_library/add_ddm_structure" var="addDDMStructureURL" />

<portlet:actionURL name="/document_library/update_ddm_structure" var="updateDDMStructureURL" />

<clay:container-fluid>
	<aui:form action="<%= (ddmStructure == null) ? addDDMStructureURL : updateDDMStructureURL %>" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + liferayPortletResponse.getNamespace() + "saveDDMStructure();" %>'>
		<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
		<aui:input name="ddmStructureId" type="hidden" value="<%= ddmStructureId %>" />
		<aui:input name="groupId" type="hidden" value="<%= groupId %>" />
		<aui:input name="definition" type="hidden" />
		<aui:input name="status" type="hidden" />

		<liferay-ui:error exception="<%= DDMFormLayoutValidationException.class %>" message="please-enter-a-valid-form-layout" />

		<liferay-ui:error exception="<%= DDMFormLayoutValidationException.MustNotDuplicateFieldName.class %>">

			<%
			DDMFormLayoutValidationException.MustNotDuplicateFieldName mndfn = (DDMFormLayoutValidationException.MustNotDuplicateFieldName)errorException;
			%>

			<liferay-ui:message arguments="<%= HtmlUtil.escape(StringUtil.merge(mndfn.getDuplicatedFieldNames(), StringPool.COMMA_AND_SPACE)) %>" key="the-definition-field-name-x-was-defined-more-than-once" translateArguments="<%= false %>" />
		</liferay-ui:error>

		<liferay-ui:error exception="<%= DDMFormValidationException.class %>" message="please-enter-a-valid-form-definition" />

		<liferay-ui:error exception="<%= DDMFormValidationException.MustNotDuplicateFieldName.class %>">

			<%
			DDMFormValidationException.MustNotDuplicateFieldName mndfn = (DDMFormValidationException.MustNotDuplicateFieldName)errorException;
			%>

			<liferay-ui:message arguments="<%= HtmlUtil.escape(mndfn.getFieldName()) %>" key="the-definition-field-name-x-was-defined-more-than-once" translateArguments="<%= false %>" />
		</liferay-ui:error>

		<liferay-ui:error exception="<%= DDMFormValidationException.MustSetFieldsForForm.class %>" message="please-add-at-least-one-field" />

		<liferay-ui:error exception="<%= DDMFormValidationException.MustSetOptionsForField.class %>">

			<%
			DDMFormValidationException.MustSetOptionsForField msoff = (DDMFormValidationException.MustSetOptionsForField)errorException;
			%>

			<liferay-ui:message arguments="<%= HtmlUtil.escape(msoff.getFieldName()) %>" key="at-least-one-option-should-be-set-for-field-x" translateArguments="<%= false %>" />
		</liferay-ui:error>

		<liferay-ui:error exception="<%= DDMFormValidationException.MustSetValidCharactersForFieldName.class %>">

			<%
			DDMFormValidationException.MustSetValidCharactersForFieldName msvcffn = (DDMFormValidationException.MustSetValidCharactersForFieldName)errorException;
			%>

			<liferay-ui:message arguments="<%= HtmlUtil.escape(msvcffn.getFieldName()) %>" key="invalid-characters-were-defined-for-field-name-x" translateArguments="<%= false %>" />
		</liferay-ui:error>

		<liferay-ui:error exception="<%= LocaleException.class %>">

			<%
			LocaleException le = (LocaleException)errorException;
			%>

			<c:if test="<%= le.getType() == LocaleException.TYPE_CONTENT %>">
				<liferay-ui:message arguments="<%= new String[] {StringUtil.merge(le.getSourceAvailableLocales(), StringPool.COMMA_AND_SPACE), StringUtil.merge(le.getTargetAvailableLocales(), StringPool.COMMA_AND_SPACE)} %>" key="the-default-language-x-does-not-match-the-portal's-available-languages-x" />
			</c:if>
		</liferay-ui:error>

		<liferay-ui:error exception="<%= StructureDefinitionException.class %>" message="please-enter-a-valid-definition" />
		<liferay-ui:error exception="<%= StructureDuplicateElementException.class %>" message="please-enter-unique-structure-field-names-(including-field-names-inherited-from-the-parent-structure)" />
		<liferay-ui:error exception="<%= StructureNameException.class %>" message="please-enter-a-valid-name" />

		<aui:model-context bean="<%= ddmStructure %>" model="<%= com.liferay.dynamic.data.mapping.model.DDMStructure.class %>" />

		<aui:fieldset-group markupView="lexicon">
			<aui:fieldset>
				<aui:field-wrapper>
					<c:if test="<%= (ddmStructure != null) && (DDMStorageLinkLocalServiceUtil.getStructureStorageLinksCount(ddmStructure.getPrimaryKey()) > 0) %>">
						<div class="alert alert-warning">
							<liferay-ui:message key="there-are-content-references-to-this-structure.-you-may-lose-data-if-a-field-name-is-renamed-or-removed" />
						</div>
					</c:if>

					<c:if test="<%= (ddmStructure != null) && (groupId != scopeGroupId) %>">
						<div class="alert alert-warning">
							<liferay-ui:message key="this-structure-does-not-belong-to-this-site.-you-may-affect-other-sites-if-you-edit-this-structure" />
						</div>
					</c:if>
				</aui:field-wrapper>

				<aui:input autoFocus="<%= windowState.equals(LiferayWindowState.POP_UP) %>" name="name" />

				<liferay-ui:panel-container
					cssClass="lfr-structure-entry-details-container"
					extended="<%= false %>"
					id="structureDetailsPanelContainer"
					persistState="<%= true %>"
				>
					<liferay-ui:panel
						collapsible="<%= true %>"
						defaultState="closed"
						extended="<%= false %>"
						id="structureDetailsSectionPanel"
						markupView="lexicon"
						persistState="<%= true %>"
						title='<%= LanguageUtil.get(request, "details") %>'
					>
						<clay:row
							cssClass="lfr-ddm-types-form-column"
						>
							<aui:input name="storageType" type="hidden" value="<%= StorageType.JSON.getValue() %>" />
						</clay:row>

						<aui:input name="description" />

						<aui:field-wrapper label='<%= LanguageUtil.format(request, "parent-x", HtmlUtil.escape(LanguageUtil.get(resourceBundle, "metadata-set")), false) %>'>
							<aui:input name="parentDDMStructureId" type="hidden" value="<%= dlEditDDMStructureDisplayContext.getParentDDMStructureId() %>" />

							<aui:input cssClass="lfr-input-text" disabled="<%= true %>" label="" name="parentDDMStructureName" type="text" value="<%= dlEditDDMStructureDisplayContext.getParentDDMStructureName() %>" />

							<aui:button onClick='<%= liferayPortletResponse.getNamespace() + "openParentDDMStructureSelector();" %>' value="select" />

							<aui:button disabled="<%= Validator.isNull(dlEditDDMStructureDisplayContext.getParentDDMStructureName()) %>" name="removeParentDDMStructureButton" onClick='<%= liferayPortletResponse.getNamespace() + "removeParentDDMStructure();" %>' value="remove" />
						</aui:field-wrapper>

						<c:if test="<%= ddmStructure != null %>">
							<portlet:resourceURL id="/dynamic_data_mapping/get_structure" var="getStructureURL">
								<portlet:param name="structureId" value="<%= String.valueOf(ddmStructure.getStructureId()) %>" />
							</portlet:resourceURL>

							<aui:input name="url" type="resource" value="<%= getStructureURL.toString() %>" />

							<c:if test="<%= Validator.isNotNull(refererWebDAVToken) %>">
								<aui:input name="webDavURL" type="resource" value="<%= ddmStructure.getWebDavURL(themeDisplay, refererWebDAVToken) %>" />
							</c:if>
						</c:if>
					</liferay-ui:panel>
				</liferay-ui:panel-container>

				<liferay-util:include page="/document_library/ddm/ddm_form_builder.jsp" servletContext="<%= application %>" />
			</aui:fieldset>
		</aui:fieldset-group>
	</aui:form>

	<aui:button-row>
		<aui:button onClick='<%= liferayPortletResponse.getNamespace() + "saveDDMStructure();" %>' primary="<%= true %>" value='<%= LanguageUtil.get(request, "save") %>' />

		<aui:button href="<%= redirect %>" type="cancel" />
	</aui:button-row>
</clay:container-fluid>

<aui:script>
	function <portlet:namespace />openParentDDMStructureSelector() {
		Liferay.Util.openSelectionModal({
			id: '<portlet:namespace />selectDDMStructure',
			onSelect: function (selectedItem) {
				var form = document.<portlet:namespace />fm;

				Liferay.Util.setFormValues(form, {
					parentDDMStructureId: selectedItem.ddmstructureid,
					parentDDMStructureName: Liferay.Util.unescape(
						selectedItem.name
					),
				});

				var removeParentDDMStructureButton = Liferay.Util.getFormElement(
					form,
					'removeParentDDMStructureButton'
				);

				if (removeParentDDMStructureButton) {
					Liferay.Util.toggleDisabled(
						removeParentDDMStructureButton,
						false
					);
				}
			},
			selectEventName: '<portlet:namespace />selectDDMStructure',
			title: '<%= UnicodeLanguageUtil.get(request, "select-structure") %>',
			url:
				'<portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>"><portlet:param name="mvcPath" value="/document_library/ddm/select_ddm_structure.jsp" /><portlet:param name="ddmStructureId" value="<%= String.valueOf(dlEditDDMStructureDisplayContext.getDDMStructureId()) %>" /></portlet:renderURL>',
		});
	}

	function <portlet:namespace />removeParentDDMStructure() {
		var form = document.<portlet:namespace />fm;

		Liferay.Util.setFormValues(form, {
			parentDDMStructureId: '',
			parentDDMStructureName: '',
		});

		var removeParentDDMStructureButton = Liferay.Util.getFormElement(
			form,
			'removeParentDDMStructureButton'
		);

		if (removeParentDDMStructureButton) {
			Liferay.Util.toggleDisabled(removeParentDDMStructureButton, true);
		}
	}

	function <portlet:namespace />saveDDMStructure() {
		Liferay.Util.postForm(document.<portlet:namespace />fm, {
			data: {
				definition: <portlet:namespace />formBuilder.getContentValue(),
			},
		});
	}
</aui:script>