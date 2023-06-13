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

<%@ include file="/fragment/renderer/product_card/init.jsp" %>

<%
CPSku cpSku = cpContentHelper.getDefaultCPSku(cpCatalogEntry);
%>

<div class="cp-renderer">
	<c:if test="<%= showAddToCartButton %>">
		<liferay-util:dynamic-include key="com.liferay.commerce.product.content.web#/add_to_cart#pre" />
	</c:if>

	<div class="card d-flex flex-column product-card">
		<div class="card-item-first position-relative">

			<%
			String productDetailURL = cpContentHelper.getFriendlyURL(cpCatalogEntry, themeDisplay);
			%>

			<a href="<%= productDetailURL %>">
				<c:if test="<%= showImage %>">

					<%
					String cpDefinitionCDNURL = cpContentHelper.getCPDefinitionCDNURL(cpCatalogEntry.getCPDefinitionId(), request);
					%>

					<c:choose>
						<c:when test="<%= Validator.isNotNull(cpDefinitionCDNURL) %>">
							<img class="img-fluid product-card-picture" src="<%= cpDefinitionCDNURL %>" />
						</c:when>
						<c:otherwise>
							<liferay-adaptive-media:img
								class="img-fluid product-card-picture"
								fileVersion="<%= cpContentHelper.getCPDefinitionImageFileVersion(cpCatalogEntry.getCPDefinitionId(), request) %>"
							/>
						</c:otherwise>
					</c:choose>
				</c:if>

				<c:if test="<%= showAvailabilityLabel || showDiscontinuedLabel %>">
					<div class="aspect-ratio-item-bottom-left">
						<c:if test="<%= showAvailabilityLabel %>">
							<commerce-ui:availability-label
								CPCatalogEntry="<%= cpCatalogEntry %>"
							/>
						</c:if>

						<c:if test="<%= showDiscontinuedLabel %>">
							<commerce-ui:discontinued-label
								CPCatalogEntry="<%= cpCatalogEntry %>"
							/>
						</c:if>
					</div>
				</c:if>
			</a>
		</div>

		<div class="card-body d-flex flex-column justify-content-between py-2">
			<div class="cp-information">
				<c:if test="<%= showSku %>">
					<p class="card-subtitle" title="<%= (cpSku == null) ? StringPool.BLANK : cpSku.getSku() %>">
						<span class="text-truncate-inline">
							<span class="text-truncate"><%= (cpSku == null) ? StringPool.BLANK : cpSku.getSku() %></span>
						</span>
					</p>
				</c:if>

				<c:if test="<%= showName %>">
					<p class="card-title" title="<%= cpCatalogEntry.getName() %>">
						<a href="<%= productDetailURL %>">
							<span class="text-truncate-inline">
								<span class="text-truncate"><%= cpCatalogEntry.getName() %></span>
							</span>
						</a>
					</p>
				</c:if>

				<c:if test="<%= showPrice %>">
					<p class="card-text">
						<span class="text-truncate-inline">
							<span class="d-flex flex-row text-truncate">
								<commerce-ui:price
									compact="<%= true %>"
									CPCatalogEntry="<%= cpCatalogEntry %>"
								/>
							</span>
						</span>
					</p>
				</c:if>
			</div>

			<div>
				<c:if test="<%= showAddToCartButton %>">
					<c:choose>
						<c:when test="<%= (cpSku == null) || cpContentHelper.hasCPDefinitionOptionRels(cpCatalogEntry.getCPDefinitionId()) %>">
							<div class="add-to-cart d-flex my-2 pt-5" id="<%= PortalUtil.generateRandomKey(request, "taglib") + StringPool.UNDERLINE %>add_to_cart">
								<a class="btn btn-block btn-secondary" href="<%= productDetailURL %>" role="button" style="margin-top: 0.35rem;">
									<liferay-ui:message key="view-all-variants" />
								</a>
							</div>
						</c:when>
						<c:otherwise>
							<div class="mt-2">
								<commerce-ui:add-to-cart
									alignment="full-width"
									CPCatalogEntry="<%= cpCatalogEntry %>"
									inline="<%= false %>"
									size="md"
									skuOptions="[]"
								/>
							</div>
						</c:otherwise>
					</c:choose>
				</c:if>

				<div class="autofit-float autofit-row autofit-row-center compare-wishlist">
					<c:if test="<%= showCompareCheckbox %>">
						<div class="autofit-col autofit-col-expand compare-checkbox">
							<div class="autofit-section">
								<div class="custom-checkbox custom-control custom-control-primary">
									<div class="custom-checkbox custom-control">
										<commerce-ui:compare-checkbox
											CPCatalogEntry="<%= cpCatalogEntry %>"
											label='<%= LanguageUtil.get(request, "compare") %>'
										/>
									</div>
								</div>
							</div>
						</div>
					</c:if>

					<c:if test="<%= showAddToWishListButton %>">
						<div class="autofit-col">
							<div class="autofit-section">
								<commerce-ui:add-to-wish-list
									CPCatalogEntry="<%= cpCatalogEntry %>"
								/>
							</div>
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</div>

	<c:if test="<%= showAddToCartButton %>">
		<liferay-util:dynamic-include key="com.liferay.commerce.product.content.web#/add_to_cart#post" />
	</c:if>
</div>