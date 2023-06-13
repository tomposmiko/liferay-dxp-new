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

<%@ include file="/price/init.jsp" %>

<%
String containerId = StringPool.BLANK;

if (Validator.isNotNull(namespace) || (Validator.isNull(namespace) && !compact)) {
	containerId = namespace + "price";
}
%>

<span class="<%= Validator.isNotNull(namespace) ? namespace + "price price" : "price" %><%= compact ? " compact" : StringPool.BLANK %>" id="<%= containerId %>">
	<liferay-util:include page="/price/default.jsp" servletContext="<%= application %>" />

	<c:choose>
		<c:when test="<%= compact %>">
			<c:choose>
				<c:when test="<%= Validator.isNull(priceModel.getDiscount()) %>">
					<c:if test="<%= Validator.isNotNull(priceModel.getPromoPrice()) %>">
						<liferay-util:include page="/price/promo.jsp" servletContext="<%= application %>" />
					</c:if>
				</c:when>
				<c:otherwise>
					<c:if test="<%= Validator.isNotNull(priceModel.getFinalPrice()) %>">
						<liferay-util:include page="/price/discount.jsp" servletContext="<%= application %>" />
					</c:if>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<c:if test="<%= Validator.isNotNull(priceModel.getPromoPrice()) %>">
				<liferay-util:include page="/price/promo.jsp" servletContext="<%= application %>" />
			</c:if>

			<c:if test="<%= Validator.isNotNull(priceModel.getFinalPrice()) %>">
				<liferay-util:include page="/price/discount.jsp" servletContext="<%= application %>" />
			</c:if>
		</c:otherwise>
	</c:choose>
</span>

<c:if test="<%= Validator.isNotNull(containerId) %>">
	<aui:script require="commerce-frontend-js/components/price/entry as Price">
		const componentId = '<%= containerId %>';

		const initialProps = {
			displayDiscountLevels: <%= displayDiscountLevels %>,
			namespace: '<%= namespace %>',
			netPrice: <%= netPrice %>,
			price: <%= jsonSerializer.serializeDeep(priceModel) %>,
			standalone: true,
		};

		Price.default(componentId, componentId, initialProps);
	</aui:script>
</c:if>