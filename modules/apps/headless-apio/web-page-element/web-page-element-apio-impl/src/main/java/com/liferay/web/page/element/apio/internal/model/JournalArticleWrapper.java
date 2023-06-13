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

package com.liferay.web.page.element.apio.internal.model;

import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.theme.ThemeDisplay;

/**
 * JournalArticle wrapper that includes a {@code ThemeDisplay} object.
 *
 * @author Eduardo Perez
 * @review
 */
public class JournalArticleWrapper
	extends com.liferay.journal.model.JournalArticleWrapper {

	/**
	 * Creates a new {@link JournalArticleWrapper}.
	 *
	 * @param  journalArticle the journalArticle being wrapped
	 * @param  themeDisplay the {@link ThemeDisplay} of the current request
	 * @review
	 */
	public JournalArticleWrapper(
		JournalArticle journalArticle, ThemeDisplay themeDisplay) {

		super(journalArticle);

		_themeDisplay = themeDisplay;
	}

	public ThemeDisplay getThemeDisplay() {
		return _themeDisplay;
	}

	private final ThemeDisplay _themeDisplay;

}