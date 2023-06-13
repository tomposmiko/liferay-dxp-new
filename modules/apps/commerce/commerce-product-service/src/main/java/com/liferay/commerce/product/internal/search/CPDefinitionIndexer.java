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

package com.liferay.commerce.product.internal.search;

import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountGroupRel;
import com.liferay.account.service.AccountGroupRelLocalService;
import com.liferay.commerce.media.CommerceMediaResolver;
import com.liferay.commerce.price.list.constants.CommercePriceListConstants;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalService;
import com.liferay.commerce.product.constants.CPField;
import com.liferay.commerce.product.links.CPDefinitionLinkTypeRegistry;
import com.liferay.commerce.product.model.CPAttachmentFileEntry;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionLink;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.model.CPDefinitionSpecificationOptionValue;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CPOption;
import com.liferay.commerce.product.model.CPSpecificationOption;
import com.liferay.commerce.product.model.CProduct;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CPDefinitionLinkLocalService;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CommerceCatalogService;
import com.liferay.commerce.product.service.CommerceChannelRelLocalService;
import com.liferay.commerce.util.CommerceBigDecimalUtil;
import com.liferay.friendly.url.model.FriendlyURLEntry;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalService;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.facet.util.RangeParserUtil;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.RangeTermFilter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.search.filter.TermsFilter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.MultiMatchQuery;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.expando.ExpandoBridgeIndexer;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
@Component(service = Indexer.class)
public class CPDefinitionIndexer extends BaseIndexer<CPDefinition> {

	public static final String CLASS_NAME = CPDefinition.class.getName();

	public CPDefinitionIndexer() {
		setDefaultSelectedFieldNames(
			Field.COMPANY_ID, Field.ENTRY_CLASS_NAME, Field.ENTRY_CLASS_PK,
			Field.GROUP_ID, Field.MODIFIED_DATE, Field.NAME,
			Field.SCOPE_GROUP_ID, Field.UID);
	}

	@Override
	public String getClassName() {
		return CLASS_NAME;
	}

	@Override
	public void postProcessContextBooleanFilter(
			BooleanFilter contextBooleanFilter, SearchContext searchContext)
		throws Exception {

		int[] statuses = GetterUtil.getIntegerValues(
			searchContext.getAttribute(Field.STATUS));

		if (ArrayUtil.isEmpty(statuses)) {
			int status = GetterUtil.getInteger(
				searchContext.getAttribute(Field.STATUS),
				WorkflowConstants.STATUS_APPROVED);

			statuses = new int[] {status};
		}

		if (!ArrayUtil.contains(statuses, WorkflowConstants.STATUS_ANY)) {
			TermsFilter statusesTermsFilter = new TermsFilter(Field.STATUS);

			statusesTermsFilter.addValues(ArrayUtil.toStringArray(statuses));

			contextBooleanFilter.add(
				statusesTermsFilter, BooleanClauseOccur.MUST);
		}

		Map<String, Serializable> attributes = searchContext.getAttributes();

		if (attributes.containsKey(CPField.BASE_PRICE)) {
			BooleanFilter priceRangeBooleanFilter = new BooleanFilter();

			String[] basePriceRanges = GetterUtil.getStringValues(
				attributes.get(CPField.BASE_PRICE));

			for (String basePriceRange : basePriceRanges) {
				String[] basePriceRangeParts = RangeParserUtil.parserRange(
					basePriceRange);

				RangeTermFilter rangeTermFilter = new RangeTermFilter(
					CPField.BASE_PRICE, true, true, basePriceRangeParts[0],
					basePriceRangeParts[1]);

				priceRangeBooleanFilter.add(
					rangeTermFilter, BooleanClauseOccur.SHOULD);
			}

			contextBooleanFilter.add(
				priceRangeBooleanFilter, BooleanClauseOccur.MUST);
		}

		if (attributes.containsKey(CPField.PUBLISHED)) {
			boolean published = GetterUtil.getBoolean(
				attributes.get(CPField.PUBLISHED));

			contextBooleanFilter.addRequiredTerm(CPField.PUBLISHED, published);
		}

		if (attributes.containsKey(CPField.SUBSCRIPTION_ENABLED)) {
			boolean subscriptionEnabled = GetterUtil.getBoolean(
				attributes.get(CPField.SUBSCRIPTION_ENABLED));

			contextBooleanFilter.addRequiredTerm(
				CPField.SUBSCRIPTION_ENABLED, subscriptionEnabled);
		}

		contextBooleanFilter.addRequiredTerm(Field.HIDDEN, false);

		String definitionLinkType = GetterUtil.getString(
			attributes.get("definitionLinkType"));

		long definitionLinkCPDefinitionId = GetterUtil.getLong(
			attributes.get("definitionLinkCPDefinitionId"));

		if (Validator.isNotNull(definitionLinkType) &&
			(definitionLinkCPDefinitionId > 0)) {

			TermsFilter linkFilter = new TermsFilter(definitionLinkType);

			linkFilter.addValue(String.valueOf(definitionLinkCPDefinitionId));

			contextBooleanFilter.add(linkFilter, BooleanClauseOccur.MUST);
		}

		if (attributes.containsKey("excludedCPDefinitionId")) {
			String excludedCPDefinitionId = String.valueOf(
				attributes.get("excludedCPDefinitionId"));

			contextBooleanFilter.addTerm(
				Field.ENTRY_CLASS_PK, excludedCPDefinitionId,
				BooleanClauseOccur.MUST_NOT);
		}

		if (GetterUtil.getBoolean(attributes.get("secure"))) {
			long commerceChannelId = GetterUtil.getLong(
				attributes.get("commerceChannelGroupId"));

			BooleanFilter commerceChannelBooleanFilter = new BooleanFilter();

			BooleanFilter commerceChannelFilterEnableBooleanFilter =
				new BooleanFilter();

			commerceChannelFilterEnableBooleanFilter.addTerm(
				CPField.CHANNEL_FILTER_ENABLED, Boolean.TRUE.toString(),
				BooleanClauseOccur.MUST);

			if (commerceChannelId > 0) {
				commerceChannelFilterEnableBooleanFilter.addTerm(
					CPField.COMMERCE_CHANNEL_GROUP_IDS,
					String.valueOf(commerceChannelId), BooleanClauseOccur.MUST);
			}
			else {
				commerceChannelFilterEnableBooleanFilter.addTerm(
					CPField.COMMERCE_CHANNEL_GROUP_IDS, "-1",
					BooleanClauseOccur.MUST);
			}

			commerceChannelBooleanFilter.add(
				commerceChannelFilterEnableBooleanFilter,
				BooleanClauseOccur.SHOULD);
			commerceChannelBooleanFilter.addTerm(
				CPField.CHANNEL_FILTER_ENABLED, Boolean.FALSE.toString(),
				BooleanClauseOccur.SHOULD);

			contextBooleanFilter.add(
				commerceChannelBooleanFilter, BooleanClauseOccur.MUST);

			long[] accountGroupIds = GetterUtil.getLongValues(
				searchContext.getAttribute("commerceAccountGroupIds"), null);

			BooleanFilter accountGroupsBooleanFilter = new BooleanFilter();

			BooleanFilter accountGroupsFilterEnableBooleanFilter =
				new BooleanFilter();

			accountGroupsFilterEnableBooleanFilter.addTerm(
				CPField.ACCOUNT_GROUP_FILTER_ENABLED, Boolean.TRUE.toString(),
				BooleanClauseOccur.MUST);

			if ((accountGroupIds != null) && (accountGroupIds.length > 0)) {
				BooleanFilter accountGroupIdsBooleanFilter =
					new BooleanFilter();

				for (long accountGroupId : accountGroupIds) {
					Filter termFilter = new TermFilter(
						"commerceAccountGroupIds",
						String.valueOf(accountGroupId));

					accountGroupIdsBooleanFilter.add(
						termFilter, BooleanClauseOccur.SHOULD);
				}

				accountGroupsFilterEnableBooleanFilter.add(
					accountGroupIdsBooleanFilter, BooleanClauseOccur.MUST);
			}
			else {
				accountGroupsFilterEnableBooleanFilter.addTerm(
					"commerceAccountGroupIds", "-1", BooleanClauseOccur.MUST);
			}

			accountGroupsBooleanFilter.add(
				accountGroupsFilterEnableBooleanFilter,
				BooleanClauseOccur.SHOULD);
			accountGroupsBooleanFilter.addTerm(
				CPField.ACCOUNT_GROUP_FILTER_ENABLED, Boolean.FALSE.toString(),
				BooleanClauseOccur.SHOULD);

			boolean ignoreAccountGroup = GetterUtil.getBoolean(
				attributes.get("ignoreCommerceAccountGroup"));

			if (!ignoreAccountGroup) {
				contextBooleanFilter.add(
					accountGroupsBooleanFilter, BooleanClauseOccur.MUST);
			}
		}
		else {
			long[] commerceCatalogIds = _getUserCommerceCatalogIds(
				searchContext);

			if (commerceCatalogIds.length > 0) {
				_addCommerceCatalogIdFilters(
					contextBooleanFilter, commerceCatalogIds);
			}
			else {
				long[] groupIds = searchContext.getGroupIds();

				if ((groupIds == null) || (groupIds.length == 0)) {
					contextBooleanFilter.addTerm(
						Field.GROUP_ID, "-1", BooleanClauseOccur.MUST);
				}
			}
		}
	}

	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, BooleanFilter fullQueryBooleanFilter,
			SearchContext searchContext)
		throws Exception {

		addSearchTerm(
			searchQuery, searchContext, CPField.EXTERNAL_REFERENCE_CODE, false);
		addSearchLocalizedTerm(
			searchQuery, searchContext, CPField.SHORT_DESCRIPTION, false);
		addSearchTerm(searchQuery, searchContext, CPField.SKUS, false);
		addSearchLocalizedTerm(
			searchQuery, searchContext, CPField.SPECIFICATION_VALUES_NAMES,
			false);
		addSearchLocalizedTerm(
			searchQuery, searchContext, Field.CONTENT, false);
		addSearchLocalizedTerm(
			searchQuery, searchContext, Field.DESCRIPTION, false);
		addSearchTerm(searchQuery, searchContext, Field.ENTRY_CLASS_PK, false);
		addSearchTerm(searchQuery, searchContext, Field.NAME, false);
		addSearchLocalizedTerm(searchQuery, searchContext, Field.NAME, false);
		addSearchTerm(searchQuery, searchContext, Field.USER_NAME, false);

		LinkedHashMap<String, Object> params =
			(LinkedHashMap<String, Object>)searchContext.getAttribute("params");

		if (params != null) {
			String expandoAttributes = (String)params.get("expandoAttributes");

			if (Validator.isNotNull(expandoAttributes)) {
				addSearchExpando(searchQuery, searchContext, expandoAttributes);
			}
		}

		String keywords = searchContext.getKeywords();

		if (Validator.isNotNull(keywords)) {
			try {
				keywords = StringUtil.toLowerCase(keywords);

				BooleanQuery booleanQuery = new BooleanQueryImpl();

				booleanQuery.add(
					new TermQueryImpl(CPField.SKUS + ".1_10_ngram", keywords),
					BooleanClauseOccur.SHOULD);

				MultiMatchQuery multiMatchQuery = new MultiMatchQuery(keywords);

				multiMatchQuery.addFields(
					CPField.SKUS, CPField.SKUS + ".reverse");
				multiMatchQuery.setType(MultiMatchQuery.Type.PHRASE_PREFIX);

				booleanQuery.add(multiMatchQuery, BooleanClauseOccur.SHOULD);

				if (searchContext.isAndSearch()) {
					searchQuery.add(booleanQuery, BooleanClauseOccur.MUST);
				}
				else {
					searchQuery.add(booleanQuery, BooleanClauseOccur.SHOULD);
				}
			}
			catch (ParseException parseException) {
				throw new SystemException(parseException);
			}
		}
	}

	@Override
	protected void doDelete(CPDefinition cpDefinition) throws Exception {
		deleteDocument(
			cpDefinition.getCompanyId(), cpDefinition.getCPDefinitionId());
	}

	@Override
	protected Document doGetDocument(CPDefinition cpDefinition)
		throws Exception {

		if (_log.isDebugEnabled()) {
			_log.debug("Indexing definition " + cpDefinition);
		}

		Document document = getBaseModelDocument(CLASS_NAME, cpDefinition);

		String cpDefinitionDefaultLanguageId =
			_localization.getDefaultLanguageId(cpDefinition.getName());

		long classNameId = _classNameLocalService.getClassNameId(
			CProduct.class);

		Map<String, String> languageIdToUrlTitleMap = new HashMap<>();

		try {
			FriendlyURLEntry friendlyURLEntry =
				_friendlyURLEntryLocalService.getMainFriendlyURLEntry(
					classNameId, cpDefinition.getCProductId());

			languageIdToUrlTitleMap =
				friendlyURLEntry.getLanguageIdToUrlTitleMap();
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		document.addKeyword(
			CPField.ACCOUNT_GROUP_FILTER_ENABLED,
			cpDefinition.isAccountGroupFilterEnabled());

		BigDecimal basePrice = _getBasePrice(cpDefinition.getCPInstances());

		if (basePrice != null) {
			document.addNumber(CPField.BASE_PRICE, basePrice);
		}

		document.addKeyword(
			CPField.CHANNEL_FILTER_ENABLED,
			cpDefinition.isChannelFilterEnabled());
		document.addNumber(
			CPField.COMMERCE_CHANNEL_GROUP_IDS,
			TransformUtil.transformToLongArray(
				_commerceChannelRelLocalService.getCommerceChannelRels(
					cpDefinition.getModelClassName(),
					cpDefinition.getCPDefinitionId(), QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null),
				commerceChannelRel -> {
					CommerceChannel commerceChannel =
						commerceChannelRel.getCommerceChannel();

					return commerceChannel.getGroupId();
				}));

		long cpAttachmentFileEntryId = 0;

		CPAttachmentFileEntry cpAttachmentFileEntry =
			_cpDefinitionLocalService.getDefaultImageCPAttachmentFileEntry(
				cpDefinition.getCPDefinitionId());

		if (cpAttachmentFileEntry != null) {
			document.addNumber(
				CPField.DEFAULT_IMAGE_FILE_ENTRY_ID,
				cpAttachmentFileEntry.getFileEntryId());

			cpAttachmentFileEntryId =
				cpAttachmentFileEntry.getCPAttachmentFileEntryId();
		}

		if (cpAttachmentFileEntryId == 0) {
			document.addKeyword(
				CPField.DEFAULT_IMAGE_FILE_URL,
				_commerceMediaResolver.getDefaultURL(
					cpDefinition.getGroupId()));
		}
		else {
			document.addKeyword(
				CPField.DEFAULT_IMAGE_FILE_URL,
				_commerceMediaResolver.getURL(
					AccountConstants.ACCOUNT_ENTRY_ID_GUEST,
					cpAttachmentFileEntryId, false, false, false));
		}

		document.addNumber(CPField.DEPTH, cpDefinition.getDepth());
		document.addDateSortable(
			CPField.DISPLAY_DATE, cpDefinition.getDisplayDate());

		CProduct cProduct = cpDefinition.getCProduct();

		document.addKeyword(
			CPField.EXTERNAL_REFERENCE_CODE,
			cProduct.getExternalReferenceCode());

		document.addNumber(CPField.HEIGHT, cpDefinition.getHeight());
		document.addKeyword(
			CPField.IS_IGNORE_SKU_COMBINATIONS,
			cpDefinition.isIgnoreSKUCombinations());
		document.addText(
			CPField.META_DESCRIPTION,
			cpDefinition.getMetaDescription(cpDefinitionDefaultLanguageId));
		document.addText(
			CPField.META_KEYWORDS,
			cpDefinition.getMetaKeywords(cpDefinitionDefaultLanguageId));
		document.addText(
			CPField.META_TITLE,
			cpDefinition.getMetaTitle(cpDefinitionDefaultLanguageId));

		List<CPDefinitionOptionRel> cpDefinitionOptionRels =
			cpDefinition.getCPDefinitionOptionRels();

		document.addNumber(
			CPField.OPTION_IDS,
			TransformUtil.transformToArray(
				_getCPOptions(cpDefinitionOptionRels), CPOption::getCPOptionId,
				Long.class));
		document.addText(
			CPField.OPTION_NAMES,
			TransformUtil.transformToArray(
				_getCPOptions(cpDefinitionOptionRels), CPOption::getKey,
				String.class));

		document.addKeyword(CPField.PRODUCT_ID, cpDefinition.getCProductId());
		document.addKeyword(
			CPField.PRODUCT_TYPE_NAME, cpDefinition.getProductTypeName());
		document.addKeyword(CPField.PUBLISHED, cpDefinition.isPublished());
		document.addText(
			CPField.SHORT_DESCRIPTION,
			cpDefinition.getShortDescription(cpDefinitionDefaultLanguageId));

		List<CPDefinitionSpecificationOptionValue>
			cpDefinitionSpecificationOptionValues =
				_getFilteredCPDefinitionSpecificationOptionValues(
					cpDefinition.getCPDefinitionSpecificationOptionValues());

		document.addNumber(
			CPField.SPECIFICATION_IDS,
			TransformUtil.transformToArray(
				cpDefinitionSpecificationOptionValues,
				CPDefinitionSpecificationOptionValue::
					getCPSpecificationOptionId,
				Long.class));
		document.addText(
			CPField.SPECIFICATION_NAMES,
			TransformUtil.transformToArray(
				cpDefinitionSpecificationOptionValues,
				CPDefinitionSpecificationOptionValue ->
					_getCPSpecificationOptionKey(
						CPDefinitionSpecificationOptionValue.
							getCPSpecificationOption()),
				String.class));
		document.addText(
			CPField.SPECIFICATION_VALUES_NAMES,
			TransformUtil.transformToArray(
				_getFilteredCPDefinitionSpecificationOptionValues(
					cpDefinitionSpecificationOptionValues),
				CPDefinitionSpecificationOptionValue ->
					CPDefinitionSpecificationOptionValue.getValue(
						cpDefinitionDefaultLanguageId),
				String.class));

		document.addText(
			CPField.SKUS,
			_cpInstanceLocalService.getSKUs(cpDefinition.getCPDefinitionId()));
		document.addKeyword(
			CPField.SUBSCRIPTION_ENABLED, cpDefinition.isSubscriptionEnabled());

		List<String> languageIds =
			_cpDefinitionLocalService.getCPDefinitionLocalizationLanguageIds(
				cpDefinition.getCPDefinitionId());

		for (String languageId : languageIds) {
			String description = cpDefinition.getDescription(languageId);
			String metaDescription = cpDefinition.getMetaDescription(
				languageId);
			String metaKeywords = cpDefinition.getMetaKeywords(languageId);
			String metaTitle = cpDefinition.getMetaTitle(languageId);
			String name = cpDefinition.getName(languageId);
			String shortDescription = cpDefinition.getShortDescription(
				languageId);
			String urlTitle = languageIdToUrlTitleMap.get(languageId);

			document.addText(
				_localization.getLocalizedName(
					CPField.META_DESCRIPTION, languageId),
				metaDescription);
			document.addText(
				_localization.getLocalizedName(
					CPField.META_KEYWORDS, languageId),
				metaKeywords);
			document.addText(
				_localization.getLocalizedName(CPField.META_TITLE, languageId),
				metaTitle);
			document.addText(
				_localization.getLocalizedName(
					CPField.SHORT_DESCRIPTION, languageId),
				shortDescription);
			document.addText(Field.CONTENT, description);
			document.addText(
				_localization.getLocalizedName(Field.DESCRIPTION, languageId),
				description);
			document.addText(
				_localization.getLocalizedName(Field.NAME, languageId), name);
			document.addText(
				_localization.getLocalizedName(Field.URL, languageId),
				urlTitle);
		}

		document.addText(
			Field.DESCRIPTION,
			cpDefinition.getDescription(cpDefinitionDefaultLanguageId));
		document.addKeyword(Field.HIDDEN, _isHidden(cpDefinition, cProduct));
		document.addText(
			Field.NAME, cpDefinition.getName(cpDefinitionDefaultLanguageId));
		document.addText(
			Field.URL,
			languageIdToUrlTitleMap.get(cpDefinitionDefaultLanguageId));

		document.addNumber(
			"commerceAccountGroupIds",
			TransformUtil.transformToLongArray(
				_accountGroupRelLocalService.getAccountGroupRels(
					CPDefinition.class.getName(),
					cpDefinition.getCPDefinitionId(), QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null),
				AccountGroupRel::getAccountGroupId));

		CommerceCatalog commerceCatalog = cpDefinition.getCommerceCatalog();

		document.addKeyword(
			"commerceCatalogId", commerceCatalog.getCommerceCatalogId());

		document.addText("defaultLanguageId", cpDefinitionDefaultLanguageId);

		for (CPDefinitionOptionRel cpDefinitionOptionRel :
				cpDefinitionOptionRels) {

			if (!cpDefinitionOptionRel.isFacetable()) {
				continue;
			}

			CPOption cpOption = cpDefinitionOptionRel.getCPOption();

			List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels =
				cpDefinitionOptionRel.getCPDefinitionOptionValueRels();

			List<String> optionValueIds = new ArrayList<>();

			Set<Locale> availableLocales = _language.getAvailableLocales(
				cpDefinitionOptionRel.getGroupId());

			for (Locale locale : availableLocales) {
				String languageId = _language.getLanguageId(locale);

				List<String> localizedOptionValues = new ArrayList<>();

				for (CPDefinitionOptionValueRel cpDefinitionOptionValueRel :
						cpDefinitionOptionValueRels) {

					optionValueIds.add(cpDefinitionOptionValueRel.getKey());

					String localizedOptionValue =
						cpDefinitionOptionValueRel.getName(languageId);

					if (Validator.isBlank(localizedOptionValue)) {
						localizedOptionValue =
							cpDefinitionOptionValueRel.getName(
								cpDefinitionDefaultLanguageId);
					}

					localizedOptionValues.add(localizedOptionValue);
				}

				document.addText(
					StringBundler.concat(
						languageId, "_ATTRIBUTE_", cpOption.getKey(),
						"_VALUES_NAMES"),
					ArrayUtil.toStringArray(localizedOptionValues));
			}

			document.addText(
				"ATTRIBUTE_" + cpOption.getKey() + "_VALUES_IDS",
				ArrayUtil.toStringArray(optionValueIds));
		}

		for (CPDefinitionSpecificationOptionValue
				cpDefinitionSpecificationOptionValue :
					cpDefinitionSpecificationOptionValues) {

			CPSpecificationOption cpSpecificationOption =
				cpDefinitionSpecificationOptionValue.getCPSpecificationOption();

			String specificationOptionValue =
				cpDefinitionSpecificationOptionValue.getValue(
					cpDefinitionDefaultLanguageId);

			Set<Locale> availableLocales = _language.getAvailableLocales(
				cpDefinitionSpecificationOptionValue.getGroupId());

			for (Locale locale : availableLocales) {
				String languageId = _language.getLanguageId(locale);

				String localizedSpecificationOptionValue =
					cpDefinitionSpecificationOptionValue.getValue(languageId);

				if (Validator.isBlank(localizedSpecificationOptionValue)) {
					localizedSpecificationOptionValue =
						specificationOptionValue;
				}

				String localeSpecificationValueName = StringBundler.concat(
					languageId, "_SPECIFICATION_",
					cpSpecificationOption.getKey(), "_VALUE_NAME");

				Field field = document.getField(localeSpecificationValueName);

				if (field != null) {
					String[] currentValues = field.getValues();

					List<String> valuesArrayList = new ArrayList<>(
						Arrays.asList(currentValues));

					valuesArrayList.add(localizedSpecificationOptionValue);

					String[] valuesArray = valuesArrayList.toArray(
						new String[0]);

					document.addText(localeSpecificationValueName, valuesArray);
				}
				else {
					document.addText(
						localeSpecificationValueName,
						localizedSpecificationOptionValue);
				}
			}

			String specificationValueName =
				"SPECIFICATION_" + cpSpecificationOption.getKey() +
					"_VALUE_NAME";

			Field field = document.getField(specificationValueName);

			if (field != null) {
				String[] currentValues = field.getValues();

				List<String> valuesArrayList = new ArrayList<>(
					Arrays.asList(currentValues));

				valuesArrayList.add(specificationOptionValue);

				String[] valuesArray = valuesArrayList.toArray(new String[0]);

				document.addText(specificationValueName, valuesArray);
			}
			else {
				document.addText(
					specificationValueName, specificationOptionValue);
			}

			String specificationValueId =
				"SPECIFICATION_" + cpSpecificationOption.getKey() + "_VALUE_ID";

			long cpDefinitionSpecificationOptionValueId =
				cpDefinitionSpecificationOptionValue.
					getCPDefinitionSpecificationOptionValueId();

			field = document.getField(specificationValueId);

			if (field != null) {
				String[] currentValues = field.getValues();

				List<String> valuesArrayList = new ArrayList<>(
					Arrays.asList(currentValues));

				valuesArrayList.add(
					String.valueOf(cpDefinitionSpecificationOptionValueId));

				String[] valuesArray = valuesArrayList.toArray(new String[0]);

				document.addNumber(specificationValueId, valuesArray);
			}
			else {
				document.addNumber(
					specificationValueId,
					cpDefinitionSpecificationOptionValueId);
			}
		}

		List<String> types = _cpDefinitionLinkTypeRegistry.getTypes();

		for (String type : types) {
			if (Validator.isNull(type)) {
				continue;
			}

			String[] linkedProductIds = _getReverseCPDefinitionIds(
				cProduct.getCProductId(), type);

			document.addKeyword(type, linkedProductIds);
		}

		_expandoBridgeIndexer.addAttributes(
			document, cpDefinition.getExpandoBridge());

		if (_log.isDebugEnabled()) {
			_log.debug("Document " + cpDefinition + " indexed successfully");
		}

		return document;
	}

	@Override
	protected Summary doGetSummary(
		Document document, Locale locale, String snippet,
		PortletRequest portletRequest, PortletResponse portletResponse) {

		Summary summary = createSummary(
			document, Field.NAME, Field.DESCRIPTION);

		summary.setMaxContentLength(200);

		return summary;
	}

	@Override
	protected void doReindex(CPDefinition cpDefinition) throws Exception {
		_indexWriterHelper.updateDocument(
			cpDefinition.getCompanyId(), getDocument(cpDefinition));
	}

	@Override
	protected void doReindex(String className, long classPK) throws Exception {
		doReindex(_cpDefinitionLocalService.getCPDefinition(classPK));
	}

	@Override
	protected void doReindex(String[] ids) throws Exception {
		long companyId = GetterUtil.getLong(ids[0]);

		_reindexCPDefinitions(companyId);
	}

	private void _addCommerceCatalogIdFilters(
		BooleanFilter contextBooleanFilter, long[] commerceCatalogIds) {

		TermsFilter termsFilter = new TermsFilter("commerceCatalogId");

		termsFilter.addValues(ArrayUtil.toStringArray(commerceCatalogIds));

		contextBooleanFilter.add(termsFilter, BooleanClauseOccur.MUST);
	}

	private BigDecimal _getBasePrice(List<CPInstance> cpInstances) {
		if (cpInstances.size() == 1) {
			CPInstance cpInstance = cpInstances.get(0);

			BigDecimal price = cpInstance.getPrice();
			BigDecimal promoPrice = cpInstance.getPromoPrice();

			if ((promoPrice.compareTo(BigDecimal.ZERO) > 0) &&
				CommerceBigDecimalUtil.lt(promoPrice, price)) {

				return promoPrice;
			}

			return price;
		}
		else if (!cpInstances.isEmpty()) {
			BigDecimal lowestPrice = BigDecimal.ZERO;

			CommercePriceEntry commercePriceEntry = null;

			for (CPInstance cpInstance : cpInstances) {
				if (!cpInstance.isApproved()) {
					continue;
				}

				commercePriceEntry =
					_commercePriceEntryLocalService.
						getInstanceBaseCommercePriceEntry(
							cpInstance.getCPInstanceUuid(),
							CommercePriceListConstants.TYPE_PRICE_LIST);

				if (commercePriceEntry == null) {
					continue;
				}

				BigDecimal price = commercePriceEntry.getPrice();

				if (lowestPrice.compareTo(BigDecimal.ZERO) == 0) {
					lowestPrice = price;
				}

				BigDecimal promoPrice = cpInstance.getPromoPrice();

				if ((promoPrice.compareTo(BigDecimal.ZERO) > 0) &&
					CommerceBigDecimalUtil.lt(promoPrice, price)) {

					price = promoPrice;
				}

				if (CommerceBigDecimalUtil.lt(price, lowestPrice)) {
					lowestPrice = price;
				}
			}

			return lowestPrice;
		}

		return null;
	}

	private List<CPOption> _getCPOptions(
			List<CPDefinitionOptionRel> cpDefinitionOptionRels)
		throws Exception {

		List<CPOption> cpOptions = new ArrayList<>();

		for (CPDefinitionOptionRel cpDefinitionOptionRel :
				cpDefinitionOptionRels) {

			if (!cpDefinitionOptionRel.isFacetable()) {
				continue;
			}

			cpOptions.add(cpDefinitionOptionRel.getCPOption());
		}

		return cpOptions;
	}

	private String _getCPSpecificationOptionKey(
		CPSpecificationOption cpSpecificationOption) {

		return cpSpecificationOption.getKey();
	}

	private List<CPDefinitionSpecificationOptionValue>
			_getFilteredCPDefinitionSpecificationOptionValues(
				List<CPDefinitionSpecificationOptionValue>
					cpDefinitionSpecificationOptionValues)
		throws Exception {

		List<CPDefinitionSpecificationOptionValue>
			filteredCPDefinitionSpecificationOptionValues = new ArrayList<>();

		for (CPDefinitionSpecificationOptionValue
				cpDefinitionSpecificationOptionValue :
					cpDefinitionSpecificationOptionValues) {

			CPSpecificationOption cpSpecificationOption =
				cpDefinitionSpecificationOptionValue.getCPSpecificationOption();

			if (!cpSpecificationOption.isFacetable()) {
				continue;
			}

			filteredCPDefinitionSpecificationOptionValues.add(
				cpDefinitionSpecificationOptionValue);
		}

		return filteredCPDefinitionSpecificationOptionValues;
	}

	private String[] _getReverseCPDefinitionIds(long cProductId, String type) {
		List<CPDefinitionLink> cpDefinitionLinks =
			_cpDefinitionLinkLocalService.getReverseCPDefinitionLinks(
				cProductId, type);

		String[] reverseCPDefinitionIdsArray =
			new String[cpDefinitionLinks.size()];

		List<String> reverseCPDefinitionIds = new ArrayList<>();

		for (CPDefinitionLink cpDefinitionLink : cpDefinitionLinks) {
			reverseCPDefinitionIds.add(
				String.valueOf(cpDefinitionLink.getCPDefinitionId()));
		}

		return reverseCPDefinitionIds.toArray(reverseCPDefinitionIdsArray);
	}

	private long[] _getUserCommerceCatalogIds(SearchContext searchContext) {
		return TransformUtil.transformToLongArray(
			_commerceCatalogService.getCommerceCatalogs(
				searchContext.getCompanyId(), QueryUtil.ALL_POS,
				QueryUtil.ALL_POS),
			CommerceCatalog::getCommerceCatalogId);
	}

	private boolean _isHidden(CPDefinition cpDefinition, CProduct cProduct) {
		if ((cpDefinition.getCPDefinitionId() !=
				cProduct.getPublishedCPDefinitionId()) &&
			_cpDefinitionLocalService.isVersionable(
				cpDefinition.getCPDefinitionId())) {

			return true;
		}

		return false;
	}

	private void _reindexCPDefinitions(long companyId) throws Exception {
		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			_cpDefinitionLocalService.getIndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setCompanyId(companyId);
		indexableActionableDynamicQuery.setPerformActionMethod(
			(CPDefinition cpDefinition) -> {
				try {
					indexableActionableDynamicQuery.addDocuments(
						getDocument(cpDefinition));
				}
				catch (PortalException portalException) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							"Unable to index commerce product definition " +
								cpDefinition.getCPDefinitionId(),
							portalException);
					}
				}
			});

		indexableActionableDynamicQuery.performActions();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CPDefinitionIndexer.class);

	@Reference
	private AccountGroupRelLocalService _accountGroupRelLocalService;

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CommerceCatalogService _commerceCatalogService;

	@Reference
	private CommerceChannelRelLocalService _commerceChannelRelLocalService;

	@Reference
	private CommerceMediaResolver _commerceMediaResolver;

	@Reference
	private CommercePriceEntryLocalService _commercePriceEntryLocalService;

	@Reference
	private CPDefinitionLinkLocalService _cpDefinitionLinkLocalService;

	@Reference
	private CPDefinitionLinkTypeRegistry _cpDefinitionLinkTypeRegistry;

	@Reference
	private CPDefinitionLocalService _cpDefinitionLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private ExpandoBridgeIndexer _expandoBridgeIndexer;

	@Reference
	private FriendlyURLEntryLocalService _friendlyURLEntryLocalService;

	@Reference
	private IndexWriterHelper _indexWriterHelper;

	@Reference
	private Language _language;

	@Reference
	private Localization _localization;

}