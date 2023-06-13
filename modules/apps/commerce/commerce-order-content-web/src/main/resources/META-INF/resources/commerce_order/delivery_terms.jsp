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

long deliveryCommerceTermEntryId = commerceOrder.getDeliveryCommerceTermEntryId();
%>

<commerce-ui:modal-content
	showSubmitButton="<%= false %>"
	title='<%= LanguageUtil.get(request, "delivery-terms") %>'
>
	<label class="control-label <%= (deliveryCommerceTermEntryId == 0) ? " d-none" : "" %>" id="name-label"><%= LanguageUtil.get(request, "name") %></label>

	<div>
		<%= commerceOrder.getDeliveryCommerceTermEntryName() %>
	</div>

	<label class="control-label <%= (deliveryCommerceTermEntryId == 0) ? " d-none" : "" %>" id="description-label"><%= LanguageUtil.get(request, "description") %></label>

	<div id="description-container">
		<%= commerceOrder.getDeliveryCommerceTermEntryDescription() %>
	</div>
</commerce-ui:modal-content>