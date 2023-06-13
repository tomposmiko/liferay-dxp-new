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
boolean branchingEnabled = GetterUtil.getBoolean((String)renderRequest.getAttribute(StagingProcessesWebKeys.BRANCHING_ENABLED));
LayoutRevision layoutRevision = (LayoutRevision)renderRequest.getAttribute(WebKeys.LAYOUT_REVISION);
List<LayoutSetBranch> layoutSetBranches = (List<LayoutSetBranch>)renderRequest.getAttribute(StagingProcessesWebKeys.LAYOUT_SET_BRANCHES);
liveGroup = (Group)renderRequest.getAttribute(StagingProcessesWebKeys.LIVE_GROUP);
Layout liveLayout = (Layout)renderRequest.getAttribute(StagingProcessesWebKeys.LIVE_LAYOUT);
String liveURL = (String)renderRequest.getAttribute(StagingProcessesWebKeys.LIVE_URL);
String remoteSiteURL = (String)renderRequest.getAttribute(StagingProcessesWebKeys.REMOTE_SITE_URL);
String remoteURL = (String)renderRequest.getAttribute(StagingProcessesWebKeys.REMOTE_URL);
stagingGroup = (Group)renderRequest.getAttribute(StagingProcessesWebKeys.STAGING_GROUP);
String stagingURL = (String)renderRequest.getAttribute(StagingProcessesWebKeys.STAGING_URL);

if (liveLayout != null) {
	request.setAttribute("view.jsp-typeSettingsProperties", liveLayout.getTypeSettingsProperties());
}
%>

<c:if test="<%= themeDisplay.isShowStagingIcon() %>">
	<c:if test="<%= liveGroup != null %>">
		<ul class="control-menu-nav">
			<li class="control-menu-nav-item dropdown staging-options-toggle visible-xs">
				<a class="control-menu-icon dropdown-toggle" data-toggle="dropdown" href="javascript:;" value="staging">
					<span class="control-menu-icon-label">
						<c:choose>
							<c:when test="<%= group.isStagingGroup() || group.isStagedRemotely() %>">
								<c:if test="<%= stagingGroup != null %>">
									<liferay-ui:message key="staging" />
								</c:if>
							</c:when>
							<c:otherwise>
								<liferay-ui:message key="live" />
							</c:otherwise>
						</c:choose>
					</span>

					<aui:icon image="caret-double-l" markupView="lexicon" />
				</a>

				<ul class="dropdown-menu">
					<li>
						<a href="#" id="viewPageStagingOptions">
							<liferay-ui:message key="view-page-staging-options" />
						</a>
					</li>

					<c:if test="<%= !group.isStagingGroup() && !group.isStagedRemotely() && (stagingGroup != null) %>">
						<li>
							<a href="<%= HtmlUtil.escape(stagingURL) %>">
								<liferay-ui:message key="go-to-staging" />
							</a>
						</li>
					</c:if>

					<c:if test="<%= group.isStagingGroup() %>">
						<c:choose>
							<c:when test="<%= group.isStagedRemotely() %>">
								<li>
									<a href="<%= HtmlUtil.escape(remoteURL) %>">
										<liferay-ui:message key="go-to-remote-live" />
									</a>
								</li>
							</c:when>
							<c:when test="<%= group.isStagingGroup() && Validator.isNotNull(liveURL) %>">
								<li>
									<a href="<%= HtmlUtil.escape(liveURL) %>">
										<liferay-ui:message key="go-to-live" />
									</a>
								</li>
							</c:when>
						</c:choose>
					</c:if>
				</ul>
			</li>

			<c:choose>
				<c:when test="<%= group.isStagingGroup() || group.isStagedRemotely() %>">
					<c:if test="<%= stagingGroup != null %>">
						<li class="active control-menu-link control-menu-nav-item hidden-xs staging-link">
							<a class="control-menu-icon" id="stagingLink" value="staging">
								<aui:icon image="staging" label="staging" markupView="lexicon" />
							</a>
						</li>
					</c:if>
				</c:when>
				<c:otherwise>
					<li class="control-menu-link control-menu-nav-item hidden-xs staging-link">
						<a class="control-menu-icon" href="<%= (layoutSetBranches != null) ? null : stagingURL %>" value="staging">
							<aui:icon image="staging" label="staging" markupView="lexicon" />
						</a>
					</li>
				</c:otherwise>
			</c:choose>

			<c:if test="<%= !group.isStagingGroup() || Validator.isNotNull(liveURL) %>">
				<li class="control-menu-nav-item hidden-xs staging-divider">
					<span> / </span>
				</li>
			</c:if>

			<c:choose>
				<c:when test="<%= group.isStagedRemotely() %>">
					<li class="control-menu-link control-menu-nav-item hidden-xs live-link">
						<c:choose>
							<c:when test="<%= !remoteSiteURL.isEmpty() %>">
								<a class="control-menu-icon" href="<%= HtmlUtil.escape(remoteSiteURL) %>" value="go-to-remote-live">
									<aui:icon image="home" label="go-to-remote-live" markupView="lexicon" />
								</a>
							</c:when>
							<c:when test="<%= SessionErrors.contains(renderRequest, AuthException.class) %>">
								<a class="control-menu-icon" value="go-to-remote-live">
									<aui:icon image="home" label="go-to-remote-live" markupView="lexicon" />
								</a>

								<liferay-ui:icon
									icon="exclamation-full"
									markupView="lexicon"
									message="an-error-occurred-while-authenticating-user"
									toolTip="<%= true %>"
								/>
							</c:when>
							<c:when test="<%= SessionErrors.contains(renderRequest, RemoteExportException.class) %>">
								<a class="control-menu-icon" value="go-to-remote-live">
									<aui:icon image="home" label="go-to-remote-live" markupView="lexicon" />
								</a>

								<liferay-ui:icon
									icon="exclamation-full"
									markupView="lexicon"
									message="the-connection-to-the-remote-live-site-cannot-be-established-due-to-a-network-problem"
									toolTip="<%= true %>"
								/>
							</c:when>
							<c:otherwise>
								<a class="control-menu-icon" value="go-to-remote-live">
									<aui:icon image="home" label="go-to-remote-live" markupView="lexicon" />
								</a>

								<liferay-ui:icon
									icon="exclamation-full"
									markupView="lexicon"
									message="an-unexpected-error-occurred"
									toolTip="<%= true %>"
								/>
							</c:otherwise>
						</c:choose>
					</li>
				</c:when>
				<c:when test="<%= group.isStagingGroup() %>">
					<c:if test="<%= Validator.isNotNull(liveURL) %>">
						<li class="control-menu-link control-menu-nav-item hidden-xs live-link">
							<a class="control-menu-icon" href="<%= HtmlUtil.escape(liveURL) %>" value="live">
								<aui:icon image="live" label="live" markupView="lexicon" />
							</a>
						</li>
					</c:if>
				</c:when>
				<c:otherwise>
					<li class="active control-menu-link control-menu-nav-item hidden-xs live-link">
						<a class="control-menu-icon taglib-icon" id="liveLink" value="live">
							<aui:icon image="live" label="live" markupView="lexicon" />
						</a>
					</li>
				</c:otherwise>
			</c:choose>

			<li class="control-menu-nav-item staging-bar">
				<div class="control-menu-level-2">
					<div class="container-fluid-1280">
						<div class="control-menu-level-2-heading visible-xs">
							<liferay-ui:message key="staging-options" />

							<button aria-labelledby="Close" class="close" id="closeStagingOptions" type="button">
								<aui:icon image="times" markupView="lexicon" />
							</button>
						</div>

						<ul class="control-menu-level-2-nav control-menu-nav">
							<c:choose>
								<c:when test="<%= group.isStagingGroup() || group.isStagedRemotely() %>">
									<c:if test="<%= stagingGroup != null %>">
										<liferay-ui:error exception="<%= AuthException.class %>">
											<liferay-ui:message arguments="<%= user.getScreenName() %>" key="an-error-occurred-while-authenticating-user-x-on-the-remote-server" />
										</liferay-ui:error>

										<liferay-ui:error exception="<%= Exception.class %>" message="an-unexpected-error-occurred" />

										<c:choose>
											<c:when test="<%= branchingEnabled %>">
												<c:if test="<%= !layoutRevision.isIncomplete() %>">
													<liferay-util:include page="/view_layout_set_branch_details.jsp" servletContext="<%= application %>" />

													<liferay-util:include page="/view_layout_branch_details.jsp" servletContext="<%= application %>" />
												</c:if>

												<li class="staging-layout-revision-details" id="<portlet:namespace />layoutRevisionDetails">
													<aui:model-context bean="<%= layoutRevision %>" model="<%= LayoutRevision.class %>" />

													<liferay-util:include page="/view_layout_revision_details.jsp" servletContext="<%= application %>" />
												</li>
											</c:when>
											<c:otherwise>
												<liferay-staging:menu
													cssClass="publish-link"
													onlyActions="<%= true %>"
												/>

												<li>
													<c:choose>
														<c:when test="<%= liveLayout == null %>">
															<span class="last-publication-branch">
																<liferay-ui:message arguments='<%= "<strong>" + HtmlUtil.escape(layout.getName(locale)) + "</strong>" %>' key="page-x-has-not-been-published-to-live-yet" translateArguments="<%= false %>" />
															</span>
														</c:when>
														<c:otherwise>
															<liferay-util:include page="/last_publication_date_message.jsp" servletContext="<%= application %>" />
														</c:otherwise>
													</c:choose>
												</li>
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:when>
								<c:otherwise>
									<li class="control-menu-nav-item staging-message">
										<div class="alert alert-warning hide warning-content" id="<portlet:namespace />warningMessage">
											<liferay-ui:message key="an-inital-staging-publication-is-in-progress" />
										</div>

										<liferay-util:include page="/last_publication_date_message.jsp" servletContext="<%= application %>" />
									</li>
								</c:otherwise>
							</c:choose>
						</ul>
					</div>
				</div>
			</li>
		</ul>
	</c:if>

	<c:if test="<%= !branchingEnabled %>">
		<aui:script use="liferay-staging">
			Liferay.StagingBar.init(
				{
					namespace: '<portlet:namespace />',
					portletId: '<%= portletDisplay.getId() %>'
				}
			);
		</aui:script>
	</c:if>

	<aui:script use="aui-base">
		$('#viewPageStagingOptions').on(
			'click',
			function(event) {
				event.preventDefault();

				$('.control-menu-level-2').addClass('open');
			}
		);

		$('#closeStagingOptions').on(
			'click',
			function(event) {
				event.preventDefault();

				$('.control-menu-level-2').removeClass('open');
			}
		);

		var stagingLink = A.one('#<portlet:namespace />stagingLink');
		var warningMessage = A.one('#<portlet:namespace />warningMessage');

		var checkBackgroundTasks = function() {
			Liferay.Service(
				'/backgroundtask.backgroundtask/get-background-tasks-count',
				{
					completed: false,
					groupId: '<%= liveGroup.getGroupId() %>',
					taskExecutorClassName: '<%= BackgroundTaskExecutorNames.LAYOUT_STAGING_BACKGROUND_TASK_EXECUTOR %>'
				},
				function(obj) {
					var incomplete = obj > 0;

					if (stagingLink) {
						stagingLink.toggle(!incomplete);
					}

					if (warningMessage) {
						warningMessage.toggle(incomplete);
					}

					if (incomplete) {
						setTimeout(checkBackgroundTasks, 5000);
					}
				}
			);
		};

		checkBackgroundTasks();
	</aui:script>
</c:if>