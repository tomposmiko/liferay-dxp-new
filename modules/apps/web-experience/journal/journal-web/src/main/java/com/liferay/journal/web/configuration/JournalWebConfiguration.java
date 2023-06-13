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

package com.liferay.journal.web.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Jürgen Kappler
 */
@ExtendedObjectClassDefinition(category = "web-content")
@Meta.OCD(
	id = "com.liferay.journal.web.configuration.JournalWebConfiguration",
	localization = "content/Language", name = "journal-web-configuration-name"
)
public interface JournalWebConfiguration {

	@Meta.AD(
		deflt = "false",
		description = "changeable-default-language-description",
		name = "changeable-default-language", required = false
	)
	public boolean changeableDefaultLanguage();

	@Meta.AD(
		deflt = "descriptive", name = "default-display-view", required = false
	)
	public String defaultDisplayView();

	@Meta.AD(
		deflt = "icon|descriptive|list", name = "display-views",
		required = false
	)
	public String[] displayViews();

	@Meta.AD(
		deflt = "true", name = "journal-article-force-autogenerate-id",
		required = false
	)
	public boolean journalArticleForceAutogenerateId();

	/**
	 * @deprecated As of 2.0.0, with no direct replacement
	 */
	@Deprecated
	@Meta.AD(
		deflt = "true",
		description = "journal-article-search-with-index-description",
		name = "journal-articles-search-with-index", required = false
	)
	public boolean journalArticlesSearchWithIndex();

	@Meta.AD(
		deflt = "false",
		description = "journal-browse-by-structures-sorted-by-name-help",
		name = "journal-browse-by-structures-sorted-by-name", required = false
	)
	public boolean journalBrowseByStructuresSortedByName();

	@Meta.AD(
		deflt = "true", name = "journal-feed-force-autogenerate-id",
		required = false
	)
	public boolean journalFeedForceAutogenerateId();

	@Meta.AD(deflt = "7", name = "max-add-menu-items", required = false)
	public int maxAddMenuItems();

	@Meta.AD(
		deflt = "true", name = "publish-to-live-by-default", required = false
	)
	public boolean publishToLiveByDefault();

	@Meta.AD(
		deflt = "true", name = "publish-version-history-by-default",
		required = false
	)
	public boolean publishVersionHistoryByDefault();

	@Meta.AD(
		deflt = "false", name = "show-ancestor-scopes-by-default",
		required = false
	)
	public boolean showAncestorScopesByDefault();

}