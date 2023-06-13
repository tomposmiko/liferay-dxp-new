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

package com.liferay.knowledge.base.web.internal.item.selector;

import com.liferay.info.item.selector.InfoItemSelectorView;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.ItemSelectorView;
import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.item.selector.PortletItemSelectorView;
import com.liferay.item.selector.criteria.InfoItemItemSelectorReturnType;
import com.liferay.item.selector.criteria.info.item.criterion.InfoItemItemSelectorCriterion;
import com.liferay.knowledge.base.constants.KBFolderConstants;
import com.liferay.knowledge.base.constants.KBPortletKeys;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.service.KBArticleLocalService;
import com.liferay.knowledge.base.util.comparator.KBArticlePriorityComparator;
import com.liferay.knowledge.base.web.internal.display.context.KBArticleItemSelectorViewDisplayContext;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.IOException;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alicia García
 */
@Component(
	property = "item.selector.view.order:Integer=600",
	service = ItemSelectorView.class
)
public class KBArticleItemSelectorView
	implements InfoItemSelectorView,
			   PortletItemSelectorView<InfoItemItemSelectorCriterion> {

	@Override
	public String getClassName() {
		return KBArticle.class.getName();
	}

	@Override
	public Class<InfoItemItemSelectorCriterion>
		getItemSelectorCriterionClass() {

		return InfoItemItemSelectorCriterion.class;
	}

	@Override
	public List<String> getPortletIds() {
		return _portletIds;
	}

	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public List<ItemSelectorReturnType> getSupportedItemSelectorReturnTypes() {
		return _supportedItemSelectorReturnTypes;
	}

	@Override
	public String getTitle(Locale locale) {
		return _language.get(locale, "kb-articles");
	}

	@Override
	public boolean isVisible(ThemeDisplay themeDisplay) {
		if (GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-125653"))) {
			return true;
		}

		return false;
	}

	@Override
	public void renderHTML(
			ServletRequest servletRequest, ServletResponse servletResponse,
			InfoItemItemSelectorCriterion infoItemItemSelectorCriterion,
			PortletURL portletURL, String itemSelectedEventName, boolean search)
		throws IOException, ServletException {

		KBArticleItemSelectorViewDisplayContext
			kbArticleItemSelectorViewDisplayContext =
				new KBArticleItemSelectorViewDisplayContext(
					(HttpServletRequest)servletRequest,
					infoItemItemSelectorCriterion, itemSelectedEventName,
					portletURL, search);

		servletRequest.setAttribute(
			KBArticleItemSelectorViewDisplayContext.class.getName(),
			kbArticleItemSelectorViewDisplayContext);

		ServletContext servletContext = getServletContext();

		RequestDispatcher requestDispatcher =
			servletContext.getRequestDispatcher(
				"/item/selector/select_kb_articles.jsp");

		requestDispatcher.include(servletRequest, servletResponse);
	}

	private static final List<String> _portletIds = Collections.singletonList(
		KBPortletKeys.KNOWLEDGE_BASE_ARTICLE);
	private static final List<ItemSelectorReturnType>
		_supportedItemSelectorReturnTypes = Collections.singletonList(
			new InfoItemItemSelectorReturnType());

	@Reference
	private KBArticleLocalService _kbArticleLocalService;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.knowledge.base.web)"
	)
	private ServletContext _servletContext;

	private class KBArticleItemDescriptor
		implements ItemSelectorViewDescriptor.ItemDescriptor {

		public KBArticleItemDescriptor(
			KBArticle kbArticle, HttpServletRequest httpServletRequest) {

			_kbArticle = kbArticle;
			_httpServletRequest = httpServletRequest;
		}

		@Override
		public String getIcon() {
			return null;
		}

		@Override
		public String getImageURL() {
			return null;
		}

		@Override
		public Date getModifiedDate() {
			return _kbArticle.getModifiedDate();
		}

		@Override
		public String getPayload() {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)_httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			return JSONUtil.put(
				"className", KBArticle.class.getName()
			).put(
				"classNameId", _portal.getClassNameId(KBArticle.class.getName())
			).put(
				"classPK", _kbArticle.getResourcePrimKey()
			).put(
				"title", _kbArticle.getTitle()
			).put(
				"type",
				ResourceActionsUtil.getModelResource(
					themeDisplay.getLocale(), KBArticle.class.getName())
			).put(
				"version", _kbArticle.getVersion()
			).toString();
		}

		@Override
		public String getSubtitle(Locale locale) {
			Date modifiedDate = _kbArticle.getModifiedDate();

			return _language.format(
				locale, "x-ago-by-x",
				new Object[] {
					_language.getTimeDescription(
						locale,
						System.currentTimeMillis() - modifiedDate.getTime(),
						true),
					HtmlUtil.escape(_kbArticle.getUserName())
				});
		}

		@Override
		public String getTitle(Locale locale) {
			return _kbArticle.getTitle();
		}

		@Override
		public long getUserId() {
			return _kbArticle.getUserId();
		}

		@Override
		public String getUserName() {
			return _kbArticle.getUserName();
		}

		private HttpServletRequest _httpServletRequest;
		private final KBArticle _kbArticle;

	}

	private class KBArticleItemSelectorViewDescriptor
		implements ItemSelectorViewDescriptor<KBArticle> {

		public KBArticleItemSelectorViewDescriptor(
			HttpServletRequest httpServletRequest, PortletURL portletURL) {

			_httpServletRequest = httpServletRequest;
			_portletURL = portletURL;
		}

		@Override
		public ItemDescriptor getItemDescriptor(KBArticle cpDefinition) {
			return new KBArticleItemDescriptor(
				cpDefinition, _httpServletRequest);
		}

		@Override
		public ItemSelectorReturnType getItemSelectorReturnType() {
			return new InfoItemItemSelectorReturnType();
		}

		@Override
		public SearchContainer<KBArticle> getSearchContainer() {
			SearchContainer<KBArticle> entriesSearchContainer =
				new SearchContainer<>(
					(PortletRequest)_httpServletRequest.getAttribute(
						JavaConstants.JAVAX_PORTLET_REQUEST),
					_portletURL, null, "no-entries-were-found");

			ThemeDisplay themeDisplay =
				(ThemeDisplay)_httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			entriesSearchContainer.setResultsAndTotal(
				() -> _kbArticleLocalService.getKBArticles(
					themeDisplay.getScopeGroupId(),
					KBFolderConstants.DEFAULT_PARENT_FOLDER_ID,
					WorkflowConstants.STATUS_APPROVED,
					entriesSearchContainer.getStart(),
					entriesSearchContainer.getEnd(),
					new KBArticlePriorityComparator(true)),
				_kbArticleLocalService.getKBArticlesCount());

			return entriesSearchContainer;
		}

		private HttpServletRequest _httpServletRequest;
		private final PortletURL _portletURL;

	}

}