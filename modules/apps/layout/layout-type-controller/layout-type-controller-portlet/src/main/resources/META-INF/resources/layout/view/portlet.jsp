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

<c:choose>
	<c:when test="<%= themeDisplay.isStatePopUp() || themeDisplay.isWidget() || layoutTypePortlet.hasStateMax() %>">

		<%
		String ppid = ParamUtil.getString(request, "p_p_id");

		String templateId = null;
		String templateContent = null;
		String langType = null;

		if (themeDisplay.isStatePopUp() || themeDisplay.isWidget()) {
			templateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "pop_up";
			templateContent = LayoutTemplateLocalServiceUtil.getContent("pop_up", true, theme.getThemeId());
			langType = LayoutTemplateLocalServiceUtil.getLangType("pop_up", true, theme.getThemeId());
		}
		else {
			ppid = StringUtil.split(layoutTypePortlet.getStateMax())[0];

			templateId = theme.getThemeId() + LayoutTemplateConstants.STANDARD_SEPARATOR + "max";
			templateContent = LayoutTemplateLocalServiceUtil.getContent("max", true, theme.getThemeId());
			langType = LayoutTemplateLocalServiceUtil.getLangType("max", true, theme.getThemeId());
		}

		if (Validator.isNotNull(templateContent)) {
			RuntimePageUtil.processTemplate(request, response, ppid, new StringTemplateResource(templateId, templateContent), langType);
		}
		%>

	</c:when>
	<c:otherwise>

		<%
		PortletLayoutDisplayContext portletLayoutDisplayContext = new PortletLayoutDisplayContext(request);

		JSONArray structureJSONArray = portletLayoutDisplayContext.getStructureJSONArray();

		for (int i = 0; i < structureJSONArray.length(); i++) {
			JSONObject rowJSONObject = structureJSONArray.getJSONObject(i);

			int type = rowJSONObject.getInt("type", FragmentConstants.TYPE_COMPONENT);
		%>

			<c:choose>
				<c:when test="<%= type == FragmentConstants.TYPE_COMPONENT %>">

					<%
					String backgroundColorCssClass = StringPool.BLANK;
					String backgroundImage = StringPool.BLANK;
					boolean columnSpacing = true;
					String containerType = StringPool.BLANK;
					long paddingHorizontal = 3L;
					long paddingVertical = 3L;

					JSONObject rowConfigJSONObject = rowJSONObject.getJSONObject("config");

					if (rowConfigJSONObject != null) {
						backgroundColorCssClass = rowConfigJSONObject.getString("backgroundColorCssClass");
						backgroundImage = portletLayoutDisplayContext.getBackgroundImage(rowConfigJSONObject);
						columnSpacing = rowConfigJSONObject.getBoolean("columnSpacing", true);
						containerType = rowConfigJSONObject.getString("containerType");
						paddingHorizontal = rowConfigJSONObject.getLong("paddingHorizontal", paddingHorizontal);
						paddingVertical = rowConfigJSONObject.getLong("paddingVertical", paddingVertical);
					}
					%>

					<section class="bg-<%= backgroundColorCssClass %>" style="<%= Validator.isNotNull(backgroundImage) ? "background-image: url(" + backgroundImage + "); background-position: 50% 50%; background-repeat: no-repeat; background-size: cover;" : StringPool.BLANK %>">
						<div class="<%= Objects.equals(containerType, "fluid") ? "container-fluid" : "container" %> <%= (paddingHorizontal != 3L) ? "px-" + paddingHorizontal : "" %> py-<%= paddingVertical %>">
							<div class="row <%= !columnSpacing ? "no-gutters" : StringPool.BLANK %>">

								<%
								JSONArray columnsJSONArray = rowJSONObject.getJSONArray("columns");

								for (int j = 0; j < columnsJSONArray.length(); j++) {
									JSONObject columnJSONObject = columnsJSONArray.getJSONObject(j);

									String size = columnJSONObject.getString("size");
								%>

									<div class="<%= Validator.isNotNull(size) ? "col-md-" + size : "col-md" %>">

										<%
										JSONArray fragmentEntryLinkIdsJSONArray = columnJSONObject.getJSONArray("fragmentEntryLinkIds");

										for (int k = 0; k < fragmentEntryLinkIdsJSONArray.length(); k++) {
										%>

											<c:choose>
												<c:when test='<%= Objects.equals(fragmentEntryLinkIdsJSONArray.getString(k), "drop-zone") %>'>

													<%
													String themeId = theme.getThemeId();

													String layoutTemplateId = layoutTypePortlet.getLayoutTemplateId();

													if (Validator.isNull(layoutTemplateId)) {
														layoutTemplateId = PropsValues.DEFAULT_LAYOUT_TEMPLATE_ID;
													}

													LayoutTemplate layoutTemplate = LayoutTemplateLocalServiceUtil.getLayoutTemplate(layoutTemplateId, false, theme.getThemeId());

													if (layoutTemplate != null) {
														themeId = layoutTemplate.getThemeId();
													}

													String templateId = themeId + LayoutTemplateConstants.CUSTOM_SEPARATOR + layoutTypePortlet.getLayoutTemplateId();
													String templateContent = LayoutTemplateLocalServiceUtil.getContent(layoutTypePortlet.getLayoutTemplateId(), false, theme.getThemeId());
													String langType = LayoutTemplateLocalServiceUtil.getLangType(layoutTypePortlet.getLayoutTemplateId(), false, theme.getThemeId());

													if (Validator.isNotNull(templateContent)) {
														RuntimePageUtil.processTemplate(request, response, new StringTemplateResource(templateId, templateContent), langType);
													}
													%>

												</c:when>
												<c:otherwise>

													<%
													long fragmentEntryLinkId = fragmentEntryLinkIdsJSONArray.getLong(k);

													if (fragmentEntryLinkId <= 0) {
														continue;
													}

													FragmentEntryLink fragmentEntryLink = FragmentEntryLinkLocalServiceUtil.fetchFragmentEntryLink(fragmentEntryLinkId);

													if (fragmentEntryLink == null) {
														continue;
													}

													FragmentRendererController fragmentRendererController = (FragmentRendererController)request.getAttribute(FragmentActionKeys.FRAGMENT_RENDERER_CONTROLLER);

													DefaultFragmentRendererContext defaultFragmentRendererContext = new DefaultFragmentRendererContext(fragmentEntryLink);

													defaultFragmentRendererContext.setLocale(locale);
													%>

													<%= fragmentRendererController.render(defaultFragmentRendererContext, request, response) %>
												</c:otherwise>
											</c:choose>

										<%
										}
										%>

									</div>

								<%
								}
								%>

							</div>
						</div>
					</section>
				</c:when>
				<c:otherwise>
					<section>

						<%
						JSONArray columnsJSONArray = rowJSONObject.getJSONArray("columns");

						for (int j = 0; j < columnsJSONArray.length(); j++) {
							JSONObject columnJSONObject = columnsJSONArray.getJSONObject(j);

							JSONArray fragmentEntryLinkIdsJSONArray = columnJSONObject.getJSONArray("fragmentEntryLinkIds");

							for (int k = 0; k < fragmentEntryLinkIdsJSONArray.length(); k++) {
								long fragmentEntryLinkId = fragmentEntryLinkIdsJSONArray.getLong(k);

								if (fragmentEntryLinkId <= 0) {
									continue;
								}

								FragmentEntryLink fragmentEntryLink = FragmentEntryLinkLocalServiceUtil.fetchFragmentEntryLink(fragmentEntryLinkId);

								if (fragmentEntryLink == null) {
									continue;
								}

								FragmentRendererController fragmentRendererController = (FragmentRendererController)request.getAttribute(FragmentActionKeys.FRAGMENT_RENDERER_CONTROLLER);

								DefaultFragmentRendererContext defaultFragmentRendererContext = new DefaultFragmentRendererContext(fragmentEntryLink);

								defaultFragmentRendererContext.setLocale(locale);
						%>

								<%= fragmentRendererController.render(defaultFragmentRendererContext, request, response) %>

						<%
							}
						}
						%>

					</section>
				</c:otherwise>
			</c:choose>

		<%
		}
		%>

	</c:otherwise>
</c:choose>

<liferay-ui:layout-common />