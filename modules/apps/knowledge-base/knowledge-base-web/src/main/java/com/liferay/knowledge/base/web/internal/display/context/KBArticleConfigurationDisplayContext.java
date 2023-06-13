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

package com.liferay.knowledge.base.web.internal.display.context;

import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.criteria.InfoItemItemSelectorReturnType;
import com.liferay.item.selector.criteria.info.item.criterion.InfoItemItemSelectorCriterion;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.service.KBArticleService;
import com.liferay.knowledge.base.web.internal.configuration.KBArticlePortletInstanceConfiguration;
import com.liferay.knowledge.base.web.internal.social.SocialBookmarksUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.util.ParameterMapUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.taglib.portlet.ActionURLTag;

import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Adolfo Pérez
 */
public class KBArticleConfigurationDisplayContext {

	public KBArticleConfigurationDisplayContext(
		HttpServletRequest httpServletRequest, ItemSelector itemSelector,
		KBArticleService kbArticleService,
		LiferayPortletResponse liferayPortletResponse, Portal portal) {

		_httpServletRequest = httpServletRequest;
		_itemSelector = itemSelector;
		_kbArticleService = kbArticleService;
		_liferayPortletResponse = liferayPortletResponse;
		_portal = portal;
	}

	public Map<String, Object> getComponentContext()
		throws ConfigurationException {

		return HashMapBuilder.<String, Object>put(
			"eventName", _getItemSelectedEventName()
		).put(
			"namespace", _liferayPortletResponse.getNamespace()
		).put(
			"selectKBObjectURL", _getSelectKBObjectURL()
		).build();
	}

	public String getComponentId() {
		return _liferayPortletResponse.getNamespace() +
			"PortletConfigurationComponent";
	}

	public String getConfigurationActionURL() throws Exception {
		PortletURL portletURL = ActionURLTag.doTag(
			PortletRequest.ACTION_PHASE, null, null, null, null, null, null,
			null, null, LayoutConstants.DEFAULT_PLID,
			LayoutConstants.DEFAULT_PLID, null, null, null, 0, 0, true, null,
			null, _httpServletRequest);

		return portletURL.toString();
	}

	public String getKBArticleTitle() throws PortalException {
		KBArticle kbArticle = _kbArticleService.fetchLatestKBArticle(
			getResourcePrimKey(), WorkflowConstants.STATUS_APPROVED);

		if (kbArticle == null) {
			return StringPool.BLANK;
		}

		return kbArticle.getTitle();
	}

	public long getResourcePrimKey() throws ConfigurationException {
		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return kbArticlePortletInstanceConfiguration.resourcePrimKey();
	}

	public String getSocialBookmarksDisplayStyle()
		throws ConfigurationException {

		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return kbArticlePortletInstanceConfiguration.
			socialBookmarksDisplayStyle();
	}

	public String getSocialBookmarksTypes() throws ConfigurationException {
		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return SocialBookmarksUtil.getSocialBookmarksTypes(
			kbArticlePortletInstanceConfiguration.socialBookmarksTypes());
	}

	public boolean isKBArticleAssetLinksEnabled()
		throws ConfigurationException {

		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return kbArticlePortletInstanceConfiguration.
			enableKBArticleAssetLinks();
	}

	public boolean isKBArticleDescriptionEnabled()
		throws ConfigurationException {

		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return kbArticlePortletInstanceConfiguration.
			enableKBArticleDescription();
	}

	public boolean isKBArticleHistoryEnabled() throws ConfigurationException {
		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return kbArticlePortletInstanceConfiguration.enableKBArticleHistory();
	}

	public boolean isKBArticlePrintEnabled() throws ConfigurationException {
		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return kbArticlePortletInstanceConfiguration.enableKBArticlePrint();
	}

	public boolean isKBArticleRatingsEnabled() throws ConfigurationException {
		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return kbArticlePortletInstanceConfiguration.enableKBArticleRatings();
	}

	public boolean isKBArticleSubscriptionsEnabled()
		throws ConfigurationException {

		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return kbArticlePortletInstanceConfiguration.
			enableKBArticleSubscriptions();
	}

	public boolean isKBArticleViewCountIncrementEnabled()
		throws ConfigurationException {

		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return kbArticlePortletInstanceConfiguration.
			enableKBArticleViewCountIncrement();
	}

	public boolean isShowKBArticleAssetEntries() throws ConfigurationException {
		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return kbArticlePortletInstanceConfiguration.
			showKBArticleAssetEntries();
	}

	public boolean isShowKBArticleAttachments() throws ConfigurationException {
		KBArticlePortletInstanceConfiguration
			kbArticlePortletInstanceConfiguration =
				_getKBArticlePortletInstanceConfiguration();

		return kbArticlePortletInstanceConfiguration.showKBArticleAttachments();
	}

	private String _getItemSelectedEventName() {
		return _liferayPortletResponse.getNamespace() + "selectKBObject";
	}

	private KBArticlePortletInstanceConfiguration
			_getKBArticlePortletInstanceConfiguration()
		throws ConfigurationException {

		if (_kbArticlePortletInstanceConfiguration != null) {
			return _kbArticlePortletInstanceConfiguration;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		_kbArticlePortletInstanceConfiguration =
			ParameterMapUtil.setParameterMap(
				KBArticlePortletInstanceConfiguration.class,
				portletDisplay.getPortletInstanceConfiguration(
					KBArticlePortletInstanceConfiguration.class),
				_httpServletRequest.getParameterMap(), "preferences--", "--");

		return _kbArticlePortletInstanceConfiguration;
	}

	private String _getSelectKBObjectURL() throws ConfigurationException {
		InfoItemItemSelectorCriterion infoItemItemSelectorCriterion =
			new InfoItemItemSelectorCriterion();

		infoItemItemSelectorCriterion.setItemType(KBArticle.class.getName());
		infoItemItemSelectorCriterion.setMultiSelection(false);
		infoItemItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			new InfoItemItemSelectorReturnType());

		PortletURL portletURL = _itemSelector.getItemSelectorURL(
			RequestBackedPortletURLFactoryUtil.create(_httpServletRequest),
			_getItemSelectedEventName(), infoItemItemSelectorCriterion);

		return portletURL.toString();
	}

	private final HttpServletRequest _httpServletRequest;
	private final ItemSelector _itemSelector;
	private KBArticlePortletInstanceConfiguration
		_kbArticlePortletInstanceConfiguration;
	private final KBArticleService _kbArticleService;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final Portal _portal;

}