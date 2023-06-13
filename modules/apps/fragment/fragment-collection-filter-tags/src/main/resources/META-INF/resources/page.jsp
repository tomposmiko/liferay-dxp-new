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

<div class="form-group form-group-sm mb-0">
	<div role="presentation">
		<label class="control-label <%= fragmentCollectionFilterTagsDisplayContext.isShowLabel() ? "" : "sr-only" %>">
			<%= fragmentCollectionFilterTagsDisplayContext.getLabel() %>
		</label>

		<input class="form-control form-control-select form-control-sm w-100" style="min-height: 2.125rem;" type="text" />

		<c:choose>
			<c:when test="<%= fragmentCollectionFilterTagsDisplayContext.isShowHelpText() %>">
				<p class="m-0 mt-1 small text-secondary">
					<%= fragmentCollectionFilterTagsDisplayContext.getHelpText() %>
				</p>
			</c:when>
		</c:choose>
	</div>

	<react:component
		module="js/SelectTags"
		props="<%= fragmentCollectionFilterTagsDisplayContext.getProps() %>"
	/>
</div>