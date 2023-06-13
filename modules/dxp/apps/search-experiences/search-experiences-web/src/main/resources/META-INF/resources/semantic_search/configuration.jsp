<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/react" prefix="react" %>

<%@ page import="com.liferay.learn.LearnMessageUtil" %><%@
page import="com.liferay.portal.kernel.util.HashMapBuilder" %><%@
page import="com.liferay.search.experiences.web.internal.display.context.SemanticSearchCompanyConfigurationDisplayContext" %>

<portlet:defineObjects />

<%
SemanticSearchCompanyConfigurationDisplayContext semanticSearchCompanyConfigurationDisplayContext = (SemanticSearchCompanyConfigurationDisplayContext)request.getAttribute(SemanticSearchCompanyConfigurationDisplayContext.class.getName());
%>

<div>
	<span aria-hidden="true" class="loading-animation"></span>

	<react:component
		module="semantic_search/js/configuration/index"
		props='<%=
			HashMapBuilder.<String, Object>put(
				"availableEmbeddingVectorDimensions", semanticSearchCompanyConfigurationDisplayContext.getAvailableEmbeddingVectorDimensions()
			).put(
				"availableLanguageDisplayNames", semanticSearchCompanyConfigurationDisplayContext.getAvailableLanguageDisplayNames()
			).put(
				"availableModelClassNames", semanticSearchCompanyConfigurationDisplayContext.getAvailableModelClassNames()
			).put(
				"availableTextEmbeddingProviders", semanticSearchCompanyConfigurationDisplayContext.getAvailableTextEmbeddingProviders()
			).put(
				"availableTextTruncationStrategies", semanticSearchCompanyConfigurationDisplayContext.getAvailableTextTruncationStrategies()
			).put(
				"formName", liferayPortletResponse.getNamespace() + "fm"
			).put(
				"initialTextEmbeddingCacheTimeout", semanticSearchCompanyConfigurationDisplayContext.getTextEmbeddingCacheTimeout()
			).put(
				"initialTextEmbeddingProviderConfigurationJSONs", semanticSearchCompanyConfigurationDisplayContext.getTextEmbeddingProviderConfigurationJSONs()
			).put(
				"initialTextEmbeddingsEnabled", semanticSearchCompanyConfigurationDisplayContext.isTextEmbeddingsEnabled()
			).put(
				"learnMessages", LearnMessageUtil.getJSONObject("search-experiences-web")
			).put(
				"namespace", liferayPortletResponse.getNamespace()
			).put(
				"redirectURL", String.valueOf(liferayPortletResponse.createRenderURL())
			).build()
		%>'
	/>
</div>

<aui:script>
	function <portlet:namespace />removeExistingFormSubmitButtons() {
		const formElement = document.getElementById('<portlet:namespace />fm');

		if (formElement) {
			const submitButtonGroupElement = formElement.querySelector(
				'.button-holder'
			);

			if (submitButtonGroupElement) {
				submitButtonGroupElement.remove();
			}
		}
	}

	<portlet:namespace />removeExistingFormSubmitButtons();
</aui:script>