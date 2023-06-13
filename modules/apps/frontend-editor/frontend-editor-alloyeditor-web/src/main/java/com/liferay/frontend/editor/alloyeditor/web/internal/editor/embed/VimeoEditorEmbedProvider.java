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

package com.liferay.frontend.editor.alloyeditor.web.internal.editor.embed;

import com.liferay.frontend.editor.api.embed.EditorEmbedProvider;
import com.liferay.frontend.editor.api.embed.EditorEmbedProviderTypeConstants;
import com.liferay.petra.string.StringBundler;

import org.osgi.service.component.annotations.Component;

/**
 * @author Sergio González
 */
@Component(
	immediate = true,
	property = "type=" + EditorEmbedProviderTypeConstants.VIDEO,
	service = EditorEmbedProvider.class
)
public class VimeoEditorEmbedProvider implements EditorEmbedProvider {

	@Override
	public String getId() {
		return "vimeo";
	}

	@Override
	public String getTpl() {
		return StringBundler.concat(
			"<iframe allowfullscreen frameborder=\"0\" height=\"315\" ",
			"mozallowfullscreen ",
			"src=\"https://player.vimeo.com/video/{embedId}\" ",
			"webkitallowfullscreen width=\"560\"></iframe>");
	}

	@Override
	public String[] getURLSchemes() {
		return new String[] {
			"https?:\\/\\/(?:www\\.)?vimeo\\.com\\/album\\/.*\\/video\\/(\\S*)",
			"https?:\\/\\/(?:www\\.)?vimeo\\.com\\/channels\\/.*\\/(\\S*)",
			"https?:\\/\\/(?:www\\.)?vimeo\\.com\\/groups\\/.*\\/videos\\" +
				"/(\\S*)",
			"https?:\\/\\/(?:www\\.)?vimeo\\.com\\/(\\S*)$"
		};
	}

}