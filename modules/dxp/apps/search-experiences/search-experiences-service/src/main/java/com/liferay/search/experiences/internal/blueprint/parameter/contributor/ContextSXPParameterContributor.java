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

package com.liferay.search.experiences.internal.blueprint.parameter.contributor;

import com.liferay.portal.kernel.change.tracking.CTCollectionThreadLocal;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.search.experiences.blueprint.parameter.BooleanSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.LongSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.SXPParameter;
import com.liferay.search.experiences.blueprint.parameter.StringSXPParameter;
import com.liferay.search.experiences.blueprint.parameter.contributor.SXPParameterContributorDefinition;
import com.liferay.search.experiences.rest.dto.v1_0.SXPBlueprint;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Petteri Karttunen
 */
public class ContextSXPParameterContributor implements SXPParameterContributor {

	public ContextSXPParameterContributor(
		GroupLocalService groupLocalService, Language language,
		LayoutLocalService layoutLocalService) {

		_groupLocalService = groupLocalService;
		_language = language;
		_layoutLocalService = layoutLocalService;
	}

	@Override
	public void contribute(
		SearchContext searchContext, SXPBlueprint sxpBlueprint,
		Set<SXPParameter> sxpParameters) {

		sxpParameters.add(
			new LongSXPParameter(
				"context.company_id", true, searchContext.getCompanyId()));
		sxpParameters.add(
			new LongSXPParameter(
				"context.ct_collection_id", true,
				CTCollectionThreadLocal.getCTCollectionId()));

		Locale locale = searchContext.getLocale();

		sxpParameters.add(
			new StringSXPParameter(
				"context.language", true, locale.getLanguage()));
		sxpParameters.add(
			new StringSXPParameter(
				"context.language_id", true,
				"_" + _language.getLanguageId(locale)));

		Layout layout = searchContext.getLayout();

		if (layout != null) {
			sxpParameters.add(
				new StringSXPParameter(
					"context.layout_locale_name", true,
					layout.getName(locale, true)));
			sxpParameters.add(
				new LongSXPParameter("plid", true, layout.getPlid()));
		}

		Long scopeGroupId = (Long)searchContext.getAttribute(
			"search.experiences.scope_group_id");

		if (scopeGroupId != null) {
			sxpParameters.add(
				new LongSXPParameter(
					"context.scope_group_id", true, scopeGroupId));

			Group group = _groupLocalService.fetchGroup(scopeGroupId);

			if (group != null) {
				sxpParameters.add(
					new BooleanSXPParameter(
						"context.is_staging_group", true,
						group.isStagingGroup()));
			}
		}
	}

	@Override
	public String getSXPParameterCategoryNameKey() {
		return "context";
	}

	@Override
	public List<SXPParameterContributorDefinition>
		getSXPParameterContributorDefinitions(long companyId) {

		return Arrays.asList(
			new SXPParameterContributorDefinition(
				LongSXPParameter.class, "company-id", "context.company_id"),
			new SXPParameterContributorDefinition(
				LongSXPParameter.class, "ct-collection-id",
				"context.ct_collection_id"),
			new SXPParameterContributorDefinition(
				BooleanSXPParameter.class, "staging-group",
				"context.is_staging_group"),
			new SXPParameterContributorDefinition(
				StringSXPParameter.class, "language", "context.language"),
			new SXPParameterContributorDefinition(
				StringSXPParameter.class, "language-id", "context.language_id"),
			new SXPParameterContributorDefinition(
				StringSXPParameter.class, "layout-locale-name",
				"context.layout_locale_name"),
			new SXPParameterContributorDefinition(
				LongSXPParameter.class, "plid", "context.plid"),
			new SXPParameterContributorDefinition(
				LongSXPParameter.class, "scope-group-id",
				"context.scope_group_id"));
	}

	private final GroupLocalService _groupLocalService;
	private final Language _language;
	private final LayoutLocalService _layoutLocalService;

}