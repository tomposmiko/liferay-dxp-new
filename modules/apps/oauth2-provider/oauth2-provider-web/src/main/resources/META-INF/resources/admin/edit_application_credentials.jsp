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

<%@ include file="/admin/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

OAuth2Application oAuth2Application = oAuth2AdminPortletDisplayContext.getOAuth2Application();

String clientId = (oAuth2Application == null) ? "" : oAuth2Application.getClientId();
String clientSecret = (oAuth2Application == null) ? "" : oAuth2Application.getClientSecret();
%>

<portlet:actionURL name="/admin/update_oauth2_application" var="updateOAuth2ApplicationURL">
	<portlet:param name="mvcRenderCommandName" value="/admin/update_oauth2_application" />
	<portlet:param name="oAuth2ApplicationId" value='<%= (oAuth2Application == null) ? "" : String.valueOf(oAuth2Application.getOAuth2ApplicationId()) %>' />
	<portlet:param name="backURL" value="<%= redirect %>" />
</portlet:actionURL>

<aui:form action="<%= updateOAuth2ApplicationURL %>" id="oauth2-application-fm" method="post" name="oauth2-application-fm">
	<div class="container-fluid container-fluid-max-xl container-view">
		<div class="sheet">
			<div class="row">
				<div class="col-lg-12">
					<liferay-ui:error exception="<%= DuplicateOAuth2ApplicationClientIdException.class %>" focusField="clientId" message="client-id-already-exists" />

					<liferay-ui:error exception="<%= OAuth2ApplicationClientGrantTypeException.class %>">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationClientGrantTypeException)errorException).getMessage()) %>" key="grant-type-x-is-unsupported-for-this-client-type" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationHomePageURLException.class %>" focusField="homePageURL" message="home-page-url-is-invalid" />
					<liferay-ui:error exception="<%= OAuth2ApplicationHomePageURLSchemeException.class %>" focusField="homePageURL" message="home-page-url-scheme-is-invalid" />
					<liferay-ui:error exception="<%= OAuth2ApplicationNameException.class %>" focusField="name" message="missing-application-name" />
					<liferay-ui:error exception="<%= OAuth2ApplicationPrivacyPolicyURLException.class %>" focusField="privacyPolicyURL" message="privacy-policy-url-is-invalid" />

					<liferay-ui:error exception="<%= OAuth2ApplicationPrivacyPolicyURLSchemeException.class %>" focusField="privacyPolicyURL">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationPrivacyPolicyURLSchemeException)errorException).getMessage()) %>" key="privacy-policy-url-scheme-is-invalid" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationRedirectURIException.class %>" focusField="redirectURIs">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationRedirectURIException)errorException).getMessage()) %>" key="redirect-uri-x-is-invalid" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationRedirectURIMissingException.class %>" focusField="redirectURIs">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationRedirectURIMissingException)errorException).getMessage()) %>" key="redirect-uri-is-missing-for-grant-type-x" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationRedirectURIFragmentException.class %>" focusField="redirectURIs">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationRedirectURIFragmentException)errorException).getMessage()) %>" key="redirect-uri-x-fragment-is-invalid" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationRedirectURIPathException.class %>" focusField="redirectURIs">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationRedirectURIPathException)errorException).getMessage()) %>" key="redirect-uri-x-path-is-invalid" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationRedirectURISchemeException.class %>" focusField="redirectURIs">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationRedirectURISchemeException)errorException).getMessage()) %>" key="redirect-uri-x-scheme-is-invalid" />
					</liferay-ui:error>

					<aui:model-context bean="<%= oAuth2Application %>" model="<%= OAuth2Application.class %>" />

					<c:if test="<%= oAuth2Application != null %>">
						<aui:fieldset style="margin-bottom: 1em; border-bottom: 2px solid #F0F0F0;">
							<aui:input name="clientId" readonly="true" required="<%= true %>" type="text" />

							<aui:field-wrapper>
								<aui:button href="" icon="icon-pencil" onClick='<%= renderResponse.getNamespace() + "showEditClientIdModal();" %>' value="" />
							</aui:field-wrapper>

							<aui:input name="originalClientId" type="hidden" value="<%= clientId %>" />

							<aui:input name="clientSecret" readonly="true" type="password" value="<%= clientSecret %>" />

							<aui:field-wrapper>
								<aui:button href="" icon="icon-pencil" onClick='<%= renderResponse.getNamespace() + "showEditClientSecretModal();" %>' value="" />
							</aui:field-wrapper>

							<aui:input name="originalClientSecret" type="hidden" value="<%= clientSecret %>" />
						</aui:fieldset>
					</c:if>
				</div>
			</div>

			<div class="row">
				<c:choose>
					<c:when test="<%= oAuth2Application != null %>">
						<div class="col-lg-9">
							<liferay-util:include page="/admin/edit_application_left_column.jsp" servletContext="<%= application %>" />
						</div>

						<div class="col-lg-3">
							<aui:fieldset label="icon">

								<%
								String thumbnailURL = oAuth2AdminPortletDisplayContext.getThumbnailURL(oAuth2Application);
								%>

								<c:choose>
									<c:when test="<%= oAuth2AdminPortletDisplayContext.hasUpdatePermission(oAuth2Application) %>">
										<liferay-ui:logo-selector
											currentLogoURL="<%= thumbnailURL %>"
											defaultLogo="<%= oAuth2Application.getIconFileEntryId() == 0 %>"
											defaultLogoURL="<%= oAuth2AdminPortletDisplayContext.getDefaultIconURL() %>"
											tempImageFileName="<%= String.valueOf(oAuth2Application.getClientId()) %>"
										/>
									</c:when>
									<c:otherwise>
										<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="portrait" />" src="<%= HtmlUtil.escapeAttribute(thumbnailURL) %>" />
									</c:otherwise>
								</c:choose>
							</aui:fieldset>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-lg-12">
							<liferay-util:include page="/admin/edit_application_left_column.jsp" servletContext="<%= application %>" />
						</div>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="row">
				<div class="col-lg-12">
					<aui:button-row>
						<aui:button cssClass="btn-lg" type="submit" />

						<aui:button cssClass="btn-lg" href="<%= portletDisplay.getURLBack() %>" type="cancel" />
					</aui:button-row>
				</div>
			</div>
		</div>
	</div>
</aui:form>

<div class="hidden">
	<div id="<portlet:namespace />edit-client-id-modal">
		<div>
			<div class="portlet-msg-error">
				<clay:icon
					symbol="info-panel-open"
				/>

				<b><liferay-ui:message key="warning" />:</b>

				<liferay-ui:message key="if-changed-clients-with-the-old-client-id-will-no-longer-be-able-to-request-new-tokens-after-you-save-the-application-details" />
			</div>

			<div class="padlock" id="<portlet:namespace />clientIdPadlock">
				<div class="open" style="display:none">
					<clay:icon symbol="unlock" /><liferay-ui:message key="changed" />
				</div>

				<div class="closed">
					<clay:icon symbol="lock" /><liferay-ui:message key="unchanged" />
				</div>
			</div>

			<aui:input label="client-id" name="newClientId" onKeyup='<%= renderResponse.getNamespace() + "updatePadlock('clientIdPadlock', this.value, '" + HtmlUtil.escapeJS(clientId) + "')" %>' type="text" value="<%= clientId %>" />

			<aui:button-row>
				<aui:button href="" icon="icon-undo" onClick='<%= renderResponse.getNamespace() + "setControlEqualTo('newClientId', 'originalClientId')" %>' value="revert" />
			</aui:button-row>
		</div>
	</div>

	<div id="<portlet:namespace />edit-client-secret-modal">
		<div>
			<div class="portlet-msg-error">
				<clay:icon
					symbol="info-panel-open"
				/>

				<b><liferay-ui:message key="warning" />:</b>
				<liferay-ui:message key="if-changed-clients-with-the-old-client-secret-will-no-longer-be-able-to-request-new-tokens-after-you-save-the-application-details" />
			</div>

			<div class="padlock" id="<portlet:namespace />clientSecretPadlock">
				<div class="open" style="display:none">
					<clay:icon symbol="unlock" /><liferay-ui:message key="changed" />
				</div>

				<div class="closed">
					<clay:icon symbol="lock" /><liferay-ui:message key="unchanged" />
				</div>
			</div>

			<aui:input label="client-secret" name="newClientSecret" onKeyup='<%= renderResponse.getNamespace() + "updatePadlock('clientSecretPadlock', this.value, '" + HtmlUtil.escapeJS(clientSecret) + "')" %>' type="text" value="<%= clientSecret %>" />

			<aui:button-row>
				<aui:button href="" icon="icon-plus" onClick='<%= renderResponse.getNamespace() + "generateRandomSecret()" %>' value="generate-new-secret" />

				<aui:button href="" icon="icon-undo" onClick='<%= renderResponse.getNamespace() + "setControlEqualTo('newClientSecret', 'originalClientSecret')" %>' value="revert" />
			</aui:button-row>
		</div>
	</div>
</div>

<aui:script use="aui-io-request,aui-modal,liferay-form,node">
	<portlet:namespace />generateRandomSecret = function() {
		var io = A.io.request(
			'<liferay-portlet:resourceURL copyCurrentRenderParameters="<%= false %>" id="/admin/generate_random_secret" />',
			{
				dataType: 'string',
				on: {
					complete: function(event, id, obj) {
						var responseText = obj.responseText;

						var newClientSecretField = A.one('#<portlet:namespace />newClientSecret');

						<portlet:namespace />updateComponent(newClientSecretField, responseText);
					}
				}
			}
		);
	}

	<portlet:namespace />getSelectedClientProfile = function() {
		return A.one('#<portlet:namespace />clientProfile option:selected');
	}

	<portlet:namespace />isConfidentialClientRequired = function() {
		var selectedClientProfile = <portlet:namespace />getSelectedClientProfile();
		return A.all('#<portlet:namespace />allowedGrantTypes .client-profile-' + selectedClientProfile.val() + ' input:checked[data-issupportsconfidentialclients="true"][data-issupportspublicclients="false"]').size() > 0;
	}

	<portlet:namespace />isRedirectURIRequired = function() {
		var selectedClientProfile = <portlet:namespace />getSelectedClientProfile();
		return A.all('#<portlet:namespace />allowedGrantTypes .client-profile-' + selectedClientProfile.val() + ' input:checked[data-isredirect="true"]').size() > 0;
	}

	<portlet:namespace />setControlEqualTo = function(targetControlId, srcControlId) {
		var targetControl = A.one('#<portlet:namespace />' + targetControlId);
		var srcControl = A.one('#<portlet:namespace />' + srcControlId);

		<portlet:namespace />updateComponent(targetControl, srcControl.val());
	}

	<portlet:namespace />showEditClientIdModal = function() {
		var bodyContentDiv = A.one('#<portlet:namespace />edit-client-id-modal');
		var clientIdPadlock = A.one('#<portlet:namespace />clientIdPadlock');
		var applyField = A.one('#<portlet:namespace />newClientId');
		var populateField = A.one('#<portlet:namespace />clientId');

		<portlet:namespace />showModal('<%= UnicodeLanguageUtil.get(request, "edit-client-id") %>', bodyContentDiv, clientIdPadlock, applyField, populateField);
	}

	<portlet:namespace />showEditClientSecretModal = function() {
		var bodyContentDiv = A.one('#<portlet:namespace />edit-client-secret-modal');
		var clientSecretPadlock = A.one('#<portlet:namespace />clientSecretPadlock');
		var applyField = A.one('#<portlet:namespace />newClientSecret');
		var populateField = A.one('#<portlet:namespace />clientSecret')

		<portlet:namespace />showModal('<%= UnicodeLanguageUtil.get(request, "edit-client-secret") %>', bodyContentDiv, clientSecretPadlock, applyField, populateField);
	}

	<portlet:namespace />showModal = function(title, bodyContent, footerContent, applyField, populateField) {

		var modal = new A.Modal(
			{
				bodyContent: bodyContent,
				centered: true,
				cssClass: 'edit-client-credentials-modal',
				destroyOnHide: true,
				footerContent: footerContent,
				headerContent: title,
				modal: true,
				visible: false,
				zIndex: Liferay.zIndex.OVERLAY
			}
		).render();

		modal.addToolbar(
			[
				{
					label: '<liferay-ui:message key="cancel" />',
					on: {
						click: function() {
							modal.hide();
						}
					}
				},
				{
					cssClass: 'btn-primary',
					label: '<liferay-ui:message key="apply" />',
					on: {
						click: function() {
							<portlet:namespace />updateComponent(populateField, applyField.val());
							modal.hide();
						}
					}
				}
			]);

		modal.show();
	}

	<portlet:namespace />updateAllowedGrantTypes = function(clientProfile) {
		A.all('#<portlet:namespace />allowedGrantTypes .allowedGrantType').hide();
		A.all('#<portlet:namespace />allowedGrantTypes .allowedGrantType.client-profile-' + clientProfile).show();
	}

	<portlet:namespace />updateComponent = function(component, newValue) {
		component.val(newValue);
		component.simulate('keyup');
		component.simulate('change');
	}

	<portlet:namespace />updatePadlock = function(padlockId, newValue, originalValue) {
		var padlock = A.one('#<portlet:namespace />' + padlockId);
		if (newValue != originalValue) {
			padlock.one('div.closed').hide();
			padlock.one('div.open').show();
		}
		else {
			padlock.one('div.open').hide();
			padlock.one('div.closed').show();
		}
	}

	var clientProfile = A.one('#<portlet:namespace />clientProfile');

	clientProfile.delegate(
		'change',
		function(event) {
			var newClientProfileValue = event.currentTarget.val();
			<portlet:namespace />updateAllowedGrantTypes(newClientProfileValue);
		},
		'#<portlet:namespace />clientProfile');

	<portlet:namespace />updateAllowedGrantTypes(clientProfile.val());

	var form = Liferay.Form.get('<portlet:namespace />oauth2-application-fm');

	var oldFieldRules = form.get('fieldRules');
	var newFieldRules = [
		{
			body: function(val, fieldNode, ruleValue) {
				return <portlet:namespace />isConfidentialClientRequired();
			},
			custom: false,
			fieldName: '<portlet:namespace />clientSecret',
			validatorName: 'required'
		},
		{
			body: function(val, fieldNode, ruleValue) {
				return <portlet:namespace />isRedirectURIRequired();
			},
			custom: false,
			fieldName: '<portlet:namespace />redirectURIs',
			validatorName: 'required'
		}
	];

	var fieldRules = oldFieldRules.concat(newFieldRules);

	form.set('fieldRules', fieldRules);
</aui:script>