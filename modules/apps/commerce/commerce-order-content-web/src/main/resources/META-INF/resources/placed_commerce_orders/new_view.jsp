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
CommerceOrder commerceOrder = commerceOrderContentDisplayContext.getCommerceOrder();
%>

<liferay-portlet:renderURL var="editPaymentTermsURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcRenderCommandName" value="/commerce_order_content/view_commerce_order_payment_terms" />
	<portlet:param name="commerceOrderId" value="<%= String.valueOf(commerceOrderContentDisplayContext.getCommerceOrderId()) %>" />
</liferay-portlet:renderURL>

<commerce-ui:modal
	id="payment-terms-modal"
	refreshPageOnClose="<%= true %>"
	size="xl"
	url="<%= editPaymentTermsURL %>"
/>

<liferay-portlet:renderURL var="editDeliveryTermsURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcRenderCommandName" value="/commerce_order_content/view_commerce_order_delivery_terms" />
	<portlet:param name="commerceOrderId" value="<%= String.valueOf(commerceOrderContentDisplayContext.getCommerceOrderId()) %>" />
</liferay-portlet:renderURL>

<commerce-ui:modal
	id="delivery-terms-modal"
	refreshPageOnClose="<%= true %>"
	size="xl"
	url="<%= editDeliveryTermsURL %>"
/>

<div class="row">
	<div class="col-12">
		<commerce-ui:header
			actions="<%= commerceOrderContentDisplayContext.getHeaderActionModels() %>"
			bean="<%= commerceOrder %>"
			beanIdLabel="id"
			dropdownItems="<%= commerceOrderContentDisplayContext.getDropdownItems() %>"
			externalReferenceCode="<%= commerceOrder.getExternalReferenceCode() %>"
			model="<%= CommerceOrder.class %>"
			thumbnailUrl="<%= commerceOrderContentDisplayContext.getCommerceAccountThumbnailURL() %>"
			title="<%= String.valueOf(commerceOrder.getCommerceOrderId()) %>"
			transitionPortletURL="<%= commerceOrderContentDisplayContext.getTransitionOrderPortletURL() %>"
		/>
	</div>

	<c:if test="<%= !commerceOrder.isOpen() %>">
		<div class="col-12 mb-4">
			<commerce-ui:step-tracker
				spritemap="<%= FrontendIconsUtil.getSpritemap(themeDisplay) %>"
				steps="<%= commerceOrderContentDisplayContext.getOrderSteps() %>"
			/>
		</div>
	</c:if>

	<div class="col-12">
		<commerce-ui:panel
			elementClasses="flex-fill"
			title='<%= LanguageUtil.get(request, "details") %>'
		>
			<div class="row vertically-divided">
				<div class="col-xl-4">

					<%
					CommerceAccount commerceAccount = commerceOrder.getCommerceAccount();
					%>

					<commerce-ui:info-box
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "account-info") %>'
					>
						<c:choose>
							<c:when test="<%= Validator.isNull(commerceAccount) %>">
								<span class="text-muted">
									<%= StringPool.BLANK %>
								</span>
							</c:when>
							<c:otherwise>
								<p class="mb-0"><%= commerceAccount.getName() %></p>
								<p class="mb-0">#<%= commerceAccount.getCommerceAccountId() %></p>
							</c:otherwise>
						</c:choose>
					</commerce-ui:info-box>

					<%
					String purchaseOrderNumber = commerceOrder.getPurchaseOrderNumber();
					%>

					<commerce-ui:info-box
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "purchase-order-number") %>'
					>
						<%= HtmlUtil.escape(purchaseOrderNumber) %>
					</commerce-ui:info-box>

					<commerce-ui:info-box
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "channel") %>'
					>
						<%= HtmlUtil.escape(commerceOrderContentDisplayContext.fetchCommerceChannel().getName()) %>
					</commerce-ui:info-box>
				</div>

				<div class="col-xl-4">

					<%
					CommerceAddress billingCommerceAddress = commerceOrder.getBillingAddress();
					%>

					<c:if test="<%= commerceOrderContentDisplayContext.hasViewBillingAddressPermission(permissionChecker, commerceAccount) %>">
						<commerce-ui:info-box
							elementClasses="py-3"
							title='<%= LanguageUtil.get(request, "billing-address") %>'
						>
							<p class="mb-0">
								<%= billingCommerceAddress.getStreet1() %>
							</p>

							<c:if test="<%= !Validator.isBlank(billingCommerceAddress.getStreet2()) %>">
								<p class="mb-0">
									<%= billingCommerceAddress.getStreet2() %>
								</p>
							</c:if>

							<c:if test="<%= !Validator.isBlank(billingCommerceAddress.getStreet2()) %>">
								<p class="mb-0">
									<%= billingCommerceAddress.getStreet3() %>
								</p>
							</c:if>

							<p class="mb-0">
								<%= commerceOrderContentDisplayContext.getDescriptiveAddress(billingCommerceAddress) %>
							</p>
						</commerce-ui:info-box>
					</c:if>

					<%
					CommerceAddress shippingCommerceAddress = commerceOrder.getShippingAddress();
					%>

					<commerce-ui:info-box
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "shipping-address") %>'
					>
						<p class="mb-0">
							<%= shippingCommerceAddress.getStreet1() %>
						</p>

						<c:if test="<%= !Validator.isBlank(shippingCommerceAddress.getStreet2()) %>">
							<p class="mb-0">
								<%= shippingCommerceAddress.getStreet2() %>
							</p>
						</c:if>

						<c:if test="<%= !Validator.isBlank(shippingCommerceAddress.getStreet3()) %>">
							<p class="mb-0">
								<%= shippingCommerceAddress.getStreet3() %>
							</p>
						</c:if>

						<p class="mb-0">
							<%= commerceOrderContentDisplayContext.getDescriptiveAddress(shippingCommerceAddress) %>
						</p>
					</commerce-ui:info-box>

					<commerce-ui:info-box
						actionLabel='<%= (commerceOrderContentDisplayContext.hasManageCommerceOrderPaymentTermsPermission() && (commerceOrder.getPaymentCommerceTermEntryId() > 0)) ? LanguageUtil.get(request, "view") : null %>'
						actionTargetId="payment-terms-modal"
						actionUrl="<%= (commerceOrderContentDisplayContext.hasManageCommerceOrderPaymentTermsPermission() && (commerceOrder.getPaymentCommerceTermEntryId() > 0)) ? editPaymentTermsURL : null %>"
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "payment-terms") %>'
					>
						<c:if test="<%= commerceOrder.getPaymentCommerceTermEntryId() > 0 %>">
							<p class="mb-0">
								<%= commerceOrder.getPaymentCommerceTermEntryName() %>
							</p>
						</c:if>
					</commerce-ui:info-box>

					<commerce-ui:info-box
						actionLabel='<%= (commerceOrderContentDisplayContext.hasManageCommerceOrderDeliveryTermsPermission() && (commerceOrder.getDeliveryCommerceTermEntryId() > 0)) ? LanguageUtil.get(request, "view") : null %>'
						actionTargetId="delivery-terms-modal"
						actionUrl="<%= (commerceOrderContentDisplayContext.hasManageCommerceOrderDeliveryTermsPermission() && (commerceOrder.getDeliveryCommerceTermEntryId() > 0)) ? editDeliveryTermsURL : null %>"
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "delivery-terms") %>'
					>
						<c:if test="<%= commerceOrder.getDeliveryCommerceTermEntryId() > 0 %>">
							<p class="mb-0">
								<%= commerceOrder.getDeliveryCommerceTermEntryName() %>
							</p>
						</c:if>
					</commerce-ui:info-box>
				</div>

				<div class="col-xl-4">
					<c:if test="<%= commerceOrder.getOrderDate() != null %>">
						<commerce-ui:info-box
							elementClasses="py-3"
							title='<%= LanguageUtil.get(request, "order-date") %>'
						>
							<%= commerceOrderContentDisplayContext.formatCommerceOrderDate(commerceOrder.getOrderDate()) %>
						</commerce-ui:info-box>
					</c:if>

					<%
					Date requestedDeliveryDate = commerceOrder.getRequestedDeliveryDate();
					%>

					<commerce-ui:info-box
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "requested-delivery-date") %>'
					>
						<%= commerceOrderContentDisplayContext.formatCommerceOrderDate(requestedDeliveryDate) %>
					</commerce-ui:info-box>

					<commerce-ui:info-box
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "order-type") %>'
					>
						<%= HtmlUtil.escape(commerceOrderContentDisplayContext.getCommerceOrderTypeName(LanguageUtil.getLanguageId(locale))) %>
					</commerce-ui:info-box>

					<portlet:renderURL var="viewCommerceOrderNotesURL">
						<portlet:param name="mvcRenderCommandName" value="/commerce_order_content/view_commerce_order_notes" />
						<portlet:param name="redirect" value="<%= currentURL %>" />
						<portlet:param name="commerceOrderId" value="<%= String.valueOf(commerceOrder.getCommerceOrderId()) %>" />
					</portlet:renderURL>

					<%
					List<CommerceOrderNote> commerceOrderNotes = commerceOrderContentDisplayContext.getCommerceOrderNotes(commerceOrder);
					%>

					<commerce-ui:info-box
						actionLabel='<%= (commerceOrderNotes.size() > 0) ? LanguageUtil.get(request, "view") : null %>'
						actionUrl="<%= (commerceOrderNotes.size() > 0) ? viewCommerceOrderNotesURL : null %>"
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "notes") %>'
					/>
				</div>
			</div>
		</commerce-ui:panel>
	</div>

	<div class="col-12">
		<commerce-ui:panel
			bodyClasses="p-0"
			title='<%= LanguageUtil.get(request, "items") %>'
		>
			<frontend-data-set:classic-display
				contextParams='<%=
					HashMapBuilder.<String, String>put(
						"commerceOrderId", String.valueOf(commerceOrder.getCommerceOrderId())
					).build()
				%>'
				dataProviderKey="<%= CommerceOrderFDSNames.PLACED_ORDER_ITEMS %>"
				id="<%= CommerceOrderFDSNames.PLACED_ORDER_ITEMS %>"
				itemsPerPage="<%= 10 %>"
				nestedItemsKey="orderItemId"
				nestedItemsReferenceKey="orderItems"
			/>
		</commerce-ui:panel>
	</div>

	<div class="col-12">
		<commerce-ui:panel
			title='<%= LanguageUtil.get(request, "order-summary") %>'
		>
			<div id="summary-root"></div>

			<aui:script require="commerce-frontend-js/components/summary/entry as summary">
				summary.default('summary', 'summary-root', {
					apiUrl:
						'/o/headless-commerce-admin-order/v1.0/orders/<%= commerceOrderContentDisplayContext.getCommerceOrderId() %>',
					datasetDisplayId: '<%= CommerceOrderFDSNames.PLACED_ORDER_ITEMS %>',
					portletId: '<%= portletDisplay.getRootPortletId() %>',
				});
			</aui:script>
		</commerce-ui:panel>
	</div>
</div>