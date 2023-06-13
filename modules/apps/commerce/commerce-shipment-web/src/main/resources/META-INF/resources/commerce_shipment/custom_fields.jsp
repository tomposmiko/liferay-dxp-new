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
CommerceShipmentDisplayContext commerceShipmentDisplayContext = (CommerceShipmentDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

long commerceShipmentId = commerceShipmentDisplayContext.getCommerceShipmentId();
%>

<liferay-portlet:actionURL name="/commerce_shipment/edit_commerce_shipment" var="editCommerceShipmentURL" />

<div class="sheet">
	<div class="panel-group panel-group-flush">
		<aui:form action="<%= editCommerceShipmentURL %>" method="post" name="shipmentCustomFieldFm">
			<aui:fieldset>
				<aui:input name="<%= Constants.CMD %>" type="hidden" value="customFields" />
				<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
				<aui:input name="commerceShipmentId" type="hidden" value="<%= commerceShipmentId %>" />

				<liferay-ui:error-marker
					key="<%= WebKeys.ERROR_SECTION %>"
					value="custom-fields"
				/>

				<aui:model-context bean="<%= commerceShipmentDisplayContext.getCommerceShipment() %>" model="<%= CommerceShipment.class %>" />

				<liferay-expando:custom-attribute-list
					className="<%= CommerceShipment.class.getName() %>"
					classPK="<%= commerceShipmentId %>"
					editable="<%= true %>"
					label="<%= true %>"
				/>

				<aui:button-row>
					<aui:button type="submit" />
				</aui:button-row>
			</aui:fieldset>
		</aui:form>
	</div>
</div>