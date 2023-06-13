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

package com.liferay.fragment.entry.processor.editable;

import com.liferay.fragment.entry.processor.editable.mapper.EditableElementMapper;
import com.liferay.fragment.entry.processor.editable.parser.EditableElementParser;
import com.liferay.fragment.entry.processor.helper.FragmentEntryProcessorHelper;
import com.liferay.fragment.entry.processor.util.EditableFragmentEntryProcessorUtil;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.processor.FragmentEntryProcessor;
import com.liferay.fragment.processor.FragmentEntryProcessorContext;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	property = "fragment.entry.processor.priority:Integer=2",
	service = FragmentEntryProcessor.class
)
public class EditableFragmentEntryProcessor implements FragmentEntryProcessor {

	@Override
	public JSONArray getDataAttributesJSONArray() {
		JSONArray jsonArray = JSONUtil.put("lfr-editable-id");

		for (String key : _editableElementParserServiceTrackerMap.keySet()) {
			jsonArray.put("lfr-editable-type:" + key);
		}

		return jsonArray;
	}

	@Override
	public JSONObject getDefaultEditableValuesJSONObject(
		String html, String configuration) {

		return _getDefaultEditableValuesJSONObject(html);
	}

	@Override
	public String processFragmentEntryLinkHTML(
			FragmentEntryLink fragmentEntryLink, String html,
			FragmentEntryProcessorContext fragmentEntryProcessorContext)
		throws PortalException {

		JSONObject jsonObject = _jsonFactory.createJSONObject(
			fragmentEntryLink.getEditableValues());

		if (jsonObject.length() == 0) {
			Class<?> clazz = getClass();

			jsonObject.put(
				clazz.getName(),
				getDefaultEditableValuesJSONObject(
					html, fragmentEntryLink.getConfiguration()));
		}

		Document document = _getDocument(html);

		Map<Long, InfoItemFieldValues> infoDisplaysFieldValues =
			new HashMap<>();

		for (Element element :
				document.select("lfr-editable,*[data-lfr-editable-id]")) {

			EditableElementParser editableElementParser =
				_getEditableElementParser(element);

			if (editableElementParser == null) {
				continue;
			}

			String id = EditableFragmentEntryProcessorUtil.getElementId(
				element);

			Class<?> clazz = getClass();

			JSONObject editableValuesJSONObject = jsonObject.getJSONObject(
				clazz.getName());

			if ((editableValuesJSONObject == null) ||
				!editableValuesJSONObject.has(id)) {

				continue;
			}

			JSONObject editableValueJSONObject =
				editableValuesJSONObject.getJSONObject(id);

			String value = StringPool.BLANK;

			JSONObject mappedValueConfigJSONObject =
				_jsonFactory.createJSONObject();

			if (_fragmentEntryProcessorHelper.isMapped(
					editableValueJSONObject) ||
				_fragmentEntryProcessorHelper.isMappedCollection(
					editableValueJSONObject) ||
				_fragmentEntryProcessorHelper.isMappedDisplayPage(
					editableValueJSONObject)) {

				Object fieldValue = _fragmentEntryProcessorHelper.getFieldValue(
					editableValueJSONObject, infoDisplaysFieldValues,
					fragmentEntryProcessorContext);

				if (fieldValue != null) {
					String fieldId = StringPool.BLANK;

					if (_fragmentEntryProcessorHelper.isMappedDisplayPage(
							editableValueJSONObject)) {

						fieldId = editableValueJSONObject.getString(
							"mappedField");
					}
					else if (_fragmentEntryProcessorHelper.isMapped(
								editableValueJSONObject)) {

						fieldId = editableValueJSONObject.getString("fieldId");
					}
					else {
						fieldId = editableValueJSONObject.getString(
							"collectionFieldId");
					}

					mappedValueConfigJSONObject =
						editableElementParser.getFieldTemplateConfigJSONObject(
							fieldId, fragmentEntryProcessorContext.getLocale(),
							fieldValue);

					value = editableElementParser.parseFieldValue(fieldValue);
				}
				else {
					value = editableValueJSONObject.getString("defaultValue");
				}
			}
			else {
				value = _fragmentEntryProcessorHelper.getEditableValue(
					editableValueJSONObject,
					fragmentEntryProcessorContext.getLocale());
			}

			JSONObject configJSONObject = JSONUtil.merge(
				editableValueJSONObject.getJSONObject("config"),
				mappedValueConfigJSONObject);

			JSONObject localizedJSONObject = configJSONObject.getJSONObject(
				LocaleUtil.toLanguageId(
					fragmentEntryProcessorContext.getLocale()));

			String mapperType = configJSONObject.getString(
				"mapperType", element.attr("type"));

			if ((localizedJSONObject != null) &&
				(localizedJSONObject.length() > 0)) {

				configJSONObject = localizedJSONObject;
			}

			editableElementParser.replace(element, value, configJSONObject);

			if (!fragmentEntryProcessorContext.isEditMode()) {
				if (Validator.isNull(mapperType)) {
					mapperType = element.attr("data-lfr-editable-type");
				}

				EditableElementMapper editableElementMapper =
					_editableElementMapperServiceTrackerMap.getService(
						mapperType);

				if (editableElementMapper != null) {
					editableElementMapper.map(
						element, configJSONObject,
						fragmentEntryProcessorContext);
				}
			}
		}

		if (fragmentEntryProcessorContext.isViewMode()) {
			for (Element element : document.select("lfr-editable")) {
				element.removeAttr("id");
				element.removeAttr("type");

				String tagName = element.attr("view-tag-name");

				if (!Objects.equals(tagName, "span")) {
					tagName = "div";
				}

				element.tagName(tagName);

				element.removeAttr("view-tag-name");
			}
		}

		Element bodyElement = document.body();

		if (!infoDisplaysFieldValues.containsKey(
				fragmentEntryProcessorContext.getPreviewClassPK())) {

			return bodyElement.html();
		}

		Element previewElement = new Element("div");

		previewElement.attr("style", "border: 1px solid #0B5FFF");

		bodyElement = previewElement.html(bodyElement.html());

		return bodyElement.outerHtml();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_editableElementMapperServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, EditableElementMapper.class, "type");
		_editableElementParserServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, EditableElementParser.class, "type");
	}

	@Deactivate
	protected void deactivate() {
		_editableElementParserServiceTrackerMap.close();
		_editableElementMapperServiceTrackerMap.close();
	}

	private JSONObject _getDefaultEditableValuesJSONObject(String html) {
		JSONObject defaultEditableValuesJSONObject =
			_jsonFactory.createJSONObject();

		Document document = _getDocument(html);

		for (Element element :
				document.select("lfr-editable,*[data-lfr-editable-id]")) {

			EditableElementParser editableElementParser =
				_getEditableElementParser(element);

			if (editableElementParser == null) {
				continue;
			}

			JSONObject defaultValueJSONObject = JSONUtil.put(
				"config", editableElementParser.getAttributes(element)
			).put(
				"defaultValue", editableElementParser.getValue(element)
			);

			defaultEditableValuesJSONObject.put(
				EditableFragmentEntryProcessorUtil.getElementId(element),
				defaultValueJSONObject);
		}

		return defaultEditableValuesJSONObject;
	}

	private Document _getDocument(String html) {
		Document document = Jsoup.parseBodyFragment(html);

		Document.OutputSettings outputSettings = new Document.OutputSettings();

		outputSettings.prettyPrint(false);

		document.outputSettings(outputSettings);

		return document;
	}

	private EditableElementParser _getEditableElementParser(Element element) {
		String type = EditableFragmentEntryProcessorUtil.getElementType(
			element);

		return _editableElementParserServiceTrackerMap.getService(type);
	}

	private ServiceTrackerMap<String, EditableElementMapper>
		_editableElementMapperServiceTrackerMap;
	private ServiceTrackerMap<String, EditableElementParser>
		_editableElementParserServiceTrackerMap;

	@Reference
	private FragmentEntryProcessorHelper _fragmentEntryProcessorHelper;

	@Reference
	private JSONFactory _jsonFactory;

}