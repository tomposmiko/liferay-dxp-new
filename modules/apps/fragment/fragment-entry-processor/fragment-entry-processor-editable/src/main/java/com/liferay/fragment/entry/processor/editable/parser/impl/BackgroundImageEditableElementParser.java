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

package com.liferay.fragment.entry.processor.editable.parser.impl;

import com.liferay.fragment.entry.processor.editable.EditableFragmentEntryProcessor;
import com.liferay.fragment.entry.processor.editable.parser.EditableElementParser;
import com.liferay.fragment.exception.FragmentEntryContentException;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import org.jsoup.nodes.Element;

import org.osgi.service.component.annotations.Component;

/**
 * @author Pavel Savinov
 */
@Component(
	immediate = true, property = "type=background-image",
	service = EditableElementParser.class
)
public class BackgroundImageEditableElementParser
	implements EditableElementParser {

	@Override
	public String getFieldTemplate() {
		return _TMPL_IMAGE_FIELD_TEMPLATE;
	}

	@Override
	public String getValue(Element element) {
		return StringPool.BLANK;
	}

	@Override
	public boolean isCss() {
		return true;
	}

	@Override
	public void replace(Element element, String value) {
	}

	@Override
	public void validate(Element element) throws FragmentEntryContentException {
	}

	private static final String _TMPL_IMAGE_FIELD_TEMPLATE = StringUtil.read(
		EditableFragmentEntryProcessor.class,
		"/META-INF/resources/fragment/entry/processor/editable" +
			"/background_image_field_template.tmpl");

}