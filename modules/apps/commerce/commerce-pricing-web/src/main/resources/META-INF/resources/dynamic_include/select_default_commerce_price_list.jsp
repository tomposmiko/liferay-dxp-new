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

CommerceChannelAccountEntryRel commerceChannelAccountEntryRel = commerceChannelAccountEntryRelDisplayContext.fetchCommerceChannelAccountEntryRel();
%>

<commerce-ui:modal-content
	submitButtonLabel='<%= LanguageUtil.get(request, "save") %>'
	title="<%= commerceChannelAccountEntryRelDisplayContext.getModalTitle() %>"
>
	<portlet:actionURL name="/commerce_pricing/edit_account_entry_default_commerce_discount" var="editAccountEntryDefaultCommerceDiscountActionURL" />

	<aui:form action="<%= editAccountEntryDefaultCommerceDiscountActionURL %>" method="post" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="accountEntryId" type="hidden" value="<%= accountEntry.getAccountEntryId() %>" />
		<aui:input name="commerceChannelAccountEntryRelId" type="hidden" value="<%= (commerceChannelAccountEntryRel == null) ? 0 : commerceChannelAccountEntryRel.getCommerceChannelAccountEntryRelId() %>" />
		<aui:input name="type" type="hidden" value="<%= commerceChannelAccountEntryRelDisplayContext.getType() %>" />

		<liferay-ui:error exception="<%= DuplicateCommerceChannelAccountEntryRelException.class %>" message="there-is-already-a-price-list-defined-for-the-selected-channel" />

		<aui:model-context bean="<%= commerceChannelAccountEntryRelDisplayContext.fetchCommerceChannelAccountEntryRel() %>" model="<%= CommerceChannelAccountEntryRel.class %>" />

		<aui:select label="channel" name="commerceChannelId">
			<aui:option label="<%= LanguageUtil.get(request, commerceChannelAccountEntryRelDisplayContext.getCommerceChannelsEmptyOptionKey()) %>" selected="<%= commerceChannelAccountEntryRelDisplayContext.isCommerceChannelSelected(0) %>" value="0" />

			<%
			for (CommerceChannel commerceChannel : commerceChannelAccountEntryRelDisplayContext.getFilteredCommerceChannels()) {
			%>

				<aui:option label="<%= commerceChannel.getName() %>" selected="<%= commerceChannelAccountEntryRelDisplayContext.isCommerceChannelSelected(commerceChannel.getCommerceChannelId()) %>" value="<%= commerceChannel.getCommerceChannelId() %>" />

			<%
			}
			%>

		</aui:select>

		<aui:input checked="<%= (commerceChannelAccountEntryRel == null) ? false : commerceChannelAccountEntryRel.isOverrideEligibility() %>" helpMessage="override-eligibility-help" label="override-eligibility" name="overrideEligibility" type="toggle-switch" />

		<aui:select label="price-list" name="classPK" required="<%= true %>">

			<%
			for (CommercePriceList commercePriceList : commerceChannelAccountEntryRelDisplayContext.getCommercePriceLists()) {
			%>

				<aui:option label="<%= commercePriceList.getName() %>" selected="<%= commerceChannelAccountEntryRelDisplayContext.isEntrySelected(commercePriceList.getCommercePriceListId()) %>" value="<%= commercePriceList.getCommercePriceListId() %>" />

			<%
			}
			%>

		</aui:select>
	</aui:form>
</commerce-ui:modal-content>