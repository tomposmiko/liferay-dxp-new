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

<%@ include file="/header/init.jsp" %>

<%
boolean isWorkflowedModel = false;

if (bean instanceof WorkflowedModel) {
	isWorkflowedModel = true;

	if (transitionPortletURL != null) {
		actions.addAll(0, HeaderHelperUtil.getWorkflowTransitionHeaderActionModels(themeDisplay.getUserId(), themeDisplay.getCompanyId(), model.getName(), beanId, transitionPortletURL));
	}
}

String myWorkflowTasksPortletNamespace = PortalUtil.getPortletNamespace(PortletKeys.MY_WORKFLOW_TASK);
%>

<div class="bg-white border-bottom commerce-header<%= fullWidth ? " container-fluid" : StringPool.BLANK %><%= Validator.isNotNull(wrapperCssClasses) ? StringPool.SPACE + wrapperCssClasses : StringPool.BLANK %> side-panel-top-anchor">
	<div class="container<%= Validator.isNotNull(cssClasses) ? StringPool.SPACE + cssClasses : StringPool.BLANK %>">
		<div class="d-lg-flex py-2">
			<div class="align-items-center d-flex flex-grow-1">
				<div class="flex-grow-1 row">
					<c:if test="<%= Validator.isNotNull(thumbnailUrl) %>">
						<div class="col-auto">
							<span class="sticker sticker-primary sticker-xl">
								<span class="sticker-overlay">
									<img alt="thumbnail" class="img-fluid" src="<%= thumbnailUrl %>" />
								</span>
							</span>
						</div>
					</c:if>

					<div class="col">
						<div class="row">
							<div class="col-auto">
								<h3 class="commerce-header-title mb-0 truncate-text">
									<%= HtmlUtil.escape(title) %>
								</h3>

								<c:if test="<%= isWorkflowedModel %>">

									<%
									WorkflowedModel workflowedModel = (WorkflowedModel)bean;
									%>

									<c:if test="<%= workflowedModel != null %>">
										<div>
											<aui:workflow-status bean="<%= bean %>" model="<%= model %>" showHelpMessage="<%= false %>" showIcon="<%= false %>" showLabel="<%= false %>" status="<%= workflowedModel.getStatus() %>" />
										</div>
									</c:if>
								</c:if>
							</div>

							<div class="col d-flex flex-column justify-content-center separator-left">
								<c:if test="<%= Validator.isNotNull(beanIdLabel) %>">
									<small class="d-block">
										<span class="header-info-title">
											<liferay-ui:message key="<%= beanIdLabel %>" />:
										</span>

										<strong class="header-info-value">
											<%= beanId %>
										</strong>

										<span class="lfr-portal-tooltip ml-1 text-secondary" title="<%= LanguageUtil.get(request, "identification-number") %>">
											<clay:icon
												symbol="question-circle"
											/>
										</span>
									</small>
								</c:if>

								<c:if test="<%= Validator.isNotNull(externalReferenceCode) || Validator.isNotNull(externalReferenceCodeEditUrl) %>">
									<small class="d-block">
										<span class="header-info-title">
											<liferay-ui:message key="erc" />:
										</span>

										<strong class="header-info-value">
											<%= externalReferenceCode %>
										</strong>

										<span class="lfr-portal-tooltip ml-1 text-secondary" title="<%= LanguageUtil.get(request, "external-reference-code") %>">
											<clay:icon
												symbol="question-circle"
											/>
										</span>

										<c:if test="<%= Validator.isNotNull(externalReferenceCodeEditUrl) %>">
											<clay:button
												cssClass="h-auto ml-1 p-0 text-secondary w-auto"
												displayType="link"
												icon="pencil"
												id="erc-edit-modal-opener"
												small="<%= true %>"
											/>

											<aui:script require="commerce-frontend-js/utilities/eventsDefinitions as events">
												document
													.querySelector('#erc-edit-modal-opener')
													.addEventListener('click', function (e) {
														e.preventDefault();
														Liferay.fire(events.OPEN_MODAL, {id: 'erc-edit-modal'});
													});
											</aui:script>

											<commerce-ui:modal
												id="erc-edit-modal"
												refreshPageOnClose="<%= true %>"
												title='<%= LanguageUtil.format(request, "edit-x", "external-reference-code") %>'
												url="<%= externalReferenceCodeEditUrl %>"
											/>
										</c:if>
									</small>
								</c:if>
							</div>
						</div>
					</div>

					<div class="col-auto d-lg-none">
						<clay:button
							cssClass="navbar-toggler"
							data-target="#navbarNavAltMarkup"
							data-toggle="liferay-collapse"
							icon="bars"
							style="secondary"
							type="button"
						/>
					</div>
				</div>
			</div>

			<div class="collapse d-lg-flex" id="navbarNavAltMarkup">
				<div class="align-self-center row">
					<c:if test="<%= Validator.isNotNull(reviewWorkflowTask) %>">

						<%
						boolean assignedToCurrentUser = false;

						if (reviewWorkflowTask.getAssigneeUserId() == user.getUserId()) {
							assignedToCurrentUser = true;
						}

						String assignee = PortalUtil.getUserName(reviewWorkflowTask.getAssigneeUserId(), "nobody");

						if (assignedToCurrentUser) {
							assignee = "me";
						}
						%>

						<div class="border-right col col-12 col-lg-auto d-flex mt-3 mt-lg-0">
							<small class="d-block">
								<span class="header-info-title mr-1">
									<liferay-ui:message key="assigned-to" />:
								</span>

								<button aria-expanded="false" aria-haspopup="true" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown" onclick="<portlet:namespace />toggleDropdown();" type="button">
									<liferay-ui:message key="<%= assignee %>" />

									<clay:icon
										symbol="caret-bottom"
									/>
								</button>

								<div class="dropdown-menu dropdown-menu-right" id="<portlet:namespace />commerce-dropdown-assigned-to">
									<c:if test="<%= !assignedToCurrentUser %>">
										<clay:button
											elementClasses="dropdown-item transition-link"
											id='<%= liferayPortletResponse.getNamespace() + "assign-to-me-modal-opener" %>'
											label='<%= LanguageUtil.get(request, "assign-to-me") %>'
											size="lg"
											style="secondary"
										/>

										<liferay-portlet:renderURL portletName="<%= PortletKeys.MY_WORKFLOW_TASK %>" var="assignToMeURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
											<portlet:param name="mvcPath" value="/workflow_task_assign.jsp" />
											<portlet:param name="hideDefaultSuccessMessage" value="<%= Boolean.TRUE.toString() %>" />
											<portlet:param name="workflowTaskId" value="<%= String.valueOf(reviewWorkflowTask.getWorkflowTaskId()) %>" />
											<portlet:param name="assigneeUserId" value="<%= String.valueOf(user.getUserId()) %>" />
										</liferay-portlet:renderURL>

										<aui:script>
											document
												.querySelector('#<portlet:namespace />assign-to-me-modal-opener')
												.addEventListener('click', function (e) {
													Liferay.Util.openWindow({
														dialog: {
															destroyOnHide: true,
															height: 430,
															resizable: false,
															width: 896,
														},
														dialogIframe: {
															bodyCssClass: 'dialog-with-footer task-dialog',
														},
														id: '<%= myWorkflowTasksPortletNamespace %>assignToDialog',
														title: '<liferay-ui:message key="assign-to-me" />',
														uri: '<%= assignToMeURL %>',
													});
												});
										</aui:script>
									</c:if>

									<clay:button
										elementClasses="dropdown-item transition-link"
										id='<%= liferayPortletResponse.getNamespace() + "assign-to-modal-opener" %>'
										label='<%= LanguageUtil.get(request, "assign-to-...") %>'
										size="lg"
										style="secondary"
									/>

									<liferay-portlet:renderURL portletName="<%= PortletKeys.MY_WORKFLOW_TASK %>" var="assignToURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
										<portlet:param name="mvcPath" value="/workflow_task_assign.jsp" />
										<portlet:param name="hideDefaultSuccessMessage" value="<%= Boolean.TRUE.toString() %>" />
										<portlet:param name="workflowTaskId" value="<%= String.valueOf(reviewWorkflowTask.getWorkflowTaskId()) %>" />
									</liferay-portlet:renderURL>

									<aui:script>
										document
											.querySelector('#<portlet:namespace />assign-to-modal-opener')
											.addEventListener('click', function (e) {
												Liferay.Util.openWindow({
													dialog: {
														destroyOnHide: true,
														height: 430,
														resizable: false,
														width: 896,
													},
													dialogIframe: {
														bodyCssClass: 'dialog-with-footer task-dialog',
													},
													id: '<%= myWorkflowTasksPortletNamespace %>assignToDialog',
													title: '<liferay-ui:message key="assign-to-..." />',
													uri: '<%= assignToURL %>',
												});
											});

										function <%= myWorkflowTasksPortletNamespace %>refreshPortlet() {
											window.location.reload();
										}

										Liferay.provide(window, '<portlet:namespace />toggleDropdown', function () {
											var dropdownElement = window.document.querySelector(
												'#<portlet:namespace />commerce-dropdown-assigned-to'
											);

											if (dropdownElement) {
												if (dropdownElement.classList.contains('show')) {
													dropdownElement.classList.remove('show');
												}
												else {
													dropdownElement.classList.add('show');
												}
											}
										});
									</aui:script>
								</div>
							</small>
						</div>
					</c:if>

					<c:if test="<%= Validator.isNotNull(actions) && !actions.isEmpty() %>">
						<div class="col-12 col-lg-auto header-actions mt-3 mt-lg-0">

							<%
							for (HeaderActionModel action : actions) {
								String buttonClasses = "btn ";

								if (Validator.isNotNull(action.getAdditionalClasses())) {
									buttonClasses += action.getAdditionalClasses();
								}
								else {
									buttonClasses += "btn-default";
								}

								boolean submitCheck = Validator.isNull(action.getId());

								String actionId = Validator.isNotNull(action.getId()) ? action.getId() : "header-action_" + PortalUtil.generateRandomKey(request, "taglib_step_tracker");
							%>

								<clay:link
									elementClasses="<%= buttonClasses %>"
									href="<%= Validator.isNotNull(action.getHref()) ? action.getHref() : StringPool.POUND %>"
									id="<%= actionId %>"
									label="<%= LanguageUtil.get(request, action.getLabel()) %>"
								/>

								<c:if test="<%= submitCheck && Validator.isNotNull(action.getFormId()) %>">
									<aui:script>
										document
											.getElementById('<%= actionId %>')
											.addEventListener('click', function (e) {
												e.preventDefault();
												var form = document.getElementById('<%= action.getFormId() %>');
												if (!form) {
													throw new Error(
														'Form with id: ' + <%= action.getFormId() %> + ' not found!'
													);
												}
												submitForm(form);
											});
									</aui:script>
								</c:if>

							<%
							}
							%>

						</div>
					</c:if>

					<c:if test="<%= Validator.isNotNull(dropdownItems) || Validator.isNotNull(previewUrl) %>">
						<div class="align-items-center col-auto d-flex pl-3">
							<c:if test="<%= Validator.isNotNull(dropdownItems) && (dropdownItems.size() > 0) %>">
								<div id="dropdown-header-container">
									<liferay-ui:icon
										icon="ellipsis-v"
										markupView="lexicon"
									/>
								</div>

								<aui:script require="commerce-frontend-js/components/dropdown/entry as dropdown">
									dropdown.default('dropdown-header', 'dropdown-header-container', {
										items: <%= jsonSerializer.serializeDeep(dropdownItems) %>,
										spritemap:
											'<%= themeDisplay.getPathThemeImages() + "/lexicon/icons.svg" %>',
									});
								</aui:script>
							</c:if>

							<c:if test="<%= Validator.isNotNull(previewUrl) %>">
								<clay:link
									elementClasses="btn btn-outline-borderless btn-outline-secondary btn-sm text-primary"
									href="<%= previewUrl %>"
									icon="shortcut"
								/>
							</c:if>
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</div>
</div>

<aui:script require="commerce-frontend-js/utilities/debounce as debounce">
	var commerceHeader = document.querySelector('.commerce-header');
	var pageHeader = document.querySelector('.page-header');

	function updateMenuDistanceFromTop() {
		if (!commerceHeader || !commerceHeader.getClientRects()[0]) return;
		var distanceFromTop = commerceHeader.getClientRects()[0].bottom;
		pageHeader.style.top = distanceFromTop + 'px';
	}

	var debouncedUpdateMenuDistanceFromTop = debounce.default(
		updateMenuDistanceFromTop,
		200
	);

	if (pageHeader) {
		pageHeader.classList.add('sticky-header-menu');
		updateMenuDistanceFromTop();
		window.addEventListener('resize', debouncedUpdateMenuDistanceFromTop);

		Liferay.once('beforeNavigate', function () {
			window.removeEventListener(
				'resize',
				debouncedUpdateMenuDistanceFromTop
			);
		});
	}
</aui:script>