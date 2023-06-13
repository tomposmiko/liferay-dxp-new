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
CommerceChannelAccountEntryRelDisplayContext commerceChannelAccountEntryRelDisplayContext = (CommerceChannelAccountEntryRelDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

AccountEntry accountEntry = commerceChannelAccountEntryRelDisplayContext.getAccountEntry();
%>

<clay:sheet-section
	id='<%= liferayPortletResponse.getNamespace() + "defaultCommerceDiscounts" %>'
>
	<clay:content-row
		containerElement="h3"
		cssClass="sheet-subtitle"
	>
		<clay:content-col
			containerElement="span"
			expand="<%= true %>"
		>
			<span class="heading-text"><liferay-ui:message key="discounts" /></span>
		</clay:content-col>
	</clay:content-row>

	<div id="<portlet:namespace />defaultCommerceDiscounts">
		<frontend-data-set:classic-display
			contextParams='<%=
				HashMapBuilder.<String, String>put(
					"accountEntryId", String.valueOf(accountEntry.getAccountEntryId())
				).put(
					"type", String.valueOf(CommerceChannelAccountEntryRelConstants.TYPE_DISCOUNT)
				).build()
			%>'
			creationMenu="<%= commerceChannelAccountEntryRelDisplayContext.getCreationMenu(CommerceChannelAccountEntryRelConstants.TYPE_DISCOUNT) %>"
			dataProviderKey="<%= CommercePricingFDSNames.ACCOUNT_ENTRY_DISCOUNTS %>"
			id="<%= CommercePricingFDSNames.ACCOUNT_ENTRY_DISCOUNTS %>"
			itemsPerPage="<%= 10 %>"
			showSearch="<%= false %>"
			style="fluid"
		/>
	</div>
</clay:sheet-section>