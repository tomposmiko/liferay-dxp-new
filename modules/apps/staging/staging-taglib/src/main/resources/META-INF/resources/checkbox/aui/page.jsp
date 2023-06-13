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

<%@ include file="/checkbox/init.jsp" %>

<div class="form-group">
	<div class="custom-checkbox custom-control">
		<label>
			<aui:input checked="<%= checked %>" cssClass="custom-control-input" data-qa-id="<%= name %>" disabled="<%= disabled %>" id="<%= id %>" ignoreRequestValue="<%= true %>" label="" name="<%= name %>" type="checkbox" wrappedField="<%= true %>" />

			<%@ include file="/checkbox/extended_label.jspf" %>
		</label>
	</div>
</div>