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

package com.liferay.layout.page.template.admin.web.internal.headless.delivery.dto.v1_0.converter;

import com.liferay.fragment.contributor.FragmentCollectionContributorTracker;
import com.liferay.fragment.entry.processor.util.EditableFragmentEntryProcessorUtil;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.processor.PortletRegistry;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.fragment.util.configuration.FragmentEntryConfigurationParser;
import com.liferay.headless.delivery.dto.v1_0.ClassPKReference;
import com.liferay.headless.delivery.dto.v1_0.ContextReference;
import com.liferay.headless.delivery.dto.v1_0.Fragment;
import com.liferay.headless.delivery.dto.v1_0.FragmentField;
import com.liferay.headless.delivery.dto.v1_0.FragmentFieldBackgroundImage;
import com.liferay.headless.delivery.dto.v1_0.FragmentFieldHTML;
import com.liferay.headless.delivery.dto.v1_0.FragmentFieldImage;
import com.liferay.headless.delivery.dto.v1_0.FragmentFieldText;
import com.liferay.headless.delivery.dto.v1_0.FragmentImage;
import com.liferay.headless.delivery.dto.v1_0.FragmentInlineValue;
import com.liferay.headless.delivery.dto.v1_0.FragmentLink;
import com.liferay.headless.delivery.dto.v1_0.FragmentMappedValue;
import com.liferay.headless.delivery.dto.v1_0.FragmentStyle;
import com.liferay.headless.delivery.dto.v1_0.FragmentViewport;
import com.liferay.headless.delivery.dto.v1_0.Mapping;
import com.liferay.headless.delivery.dto.v1_0.PageFragmentInstanceDefinition;
import com.liferay.headless.delivery.dto.v1_0.WidgetInstance;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.InfoItemServiceTracker;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.info.item.provider.InfoItemObjectProvider;
import com.liferay.layout.util.structure.FragmentStyledLayoutStructureItem;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONDeserializer;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rubén Pulido
 */
@Component(service = PageFragmentInstanceDefinitionDTOConverter.class)
public class PageFragmentInstanceDefinitionDTOConverter {

	public PageFragmentInstanceDefinition toDTO(
		FragmentStyledLayoutStructureItem fragmentStyledLayoutStructureItem) {

		return toDTO(fragmentStyledLayoutStructureItem, true, true);
	}

	public PageFragmentInstanceDefinition toDTO(
		FragmentStyledLayoutStructureItem fragmentStyledLayoutStructureItem,
		boolean saveInlineContent, boolean saveMapping) {

		return toDTO(fragmentStyledLayoutStructureItem, null, null, true, true);
	}

	public PageFragmentInstanceDefinition toDTO(
		FragmentStyledLayoutStructureItem fragmentStyledLayoutStructureItem,
		FragmentStyle pageFragmentInstanceDefinitionFragmentStyle,
		FragmentViewport[] pageFragmentInstanceDefinitionFragmentViewports,
		boolean saveInlineContent, boolean saveMapping) {

		FragmentEntryLink fragmentEntryLink =
			_fragmentEntryLinkLocalService.fetchFragmentEntryLink(
				fragmentStyledLayoutStructureItem.getFragmentEntryLinkId());

		if (fragmentEntryLink == null) {
			return null;
		}

		String rendererKey = fragmentEntryLink.getRendererKey();

		FragmentEntry fragmentEntry = _getFragmentEntry(
			_fragmentCollectionContributorTracker,
			fragmentEntryLink.getFragmentEntryId(), rendererKey);

		return new PageFragmentInstanceDefinition() {
			{
				fragment = new Fragment() {
					{
						key = _getFragmentKey(fragmentEntry, rendererKey);
					}
				};
				fragmentConfig = _getFragmentConfig(fragmentEntryLink);
				fragmentFields = _getFragmentFields(
					fragmentEntryLink, saveInlineContent, saveMapping);
				fragmentStyle = pageFragmentInstanceDefinitionFragmentStyle;
				fragmentViewports =
					pageFragmentInstanceDefinitionFragmentViewports;
				indexed = fragmentStyledLayoutStructureItem.isIndexed();
				widgetInstances = _getWidgetInstances(fragmentEntryLink);
			}
		};
	}

	private List<String> _getAvailableLanguageIds() {
		Set<Locale> availableLocales = LanguageUtil.getAvailableLocales();

		Stream<Locale> stream = availableLocales.stream();

		return stream.map(
			availableLocale -> LanguageUtil.getLanguageId(availableLocale)
		).collect(
			Collectors.toList()
		);
	}

	private List<FragmentField> _getBackgroundImageFragmentFields(
		JSONObject jsonObject, boolean saveMapping) {

		List<FragmentField> fragmentFields = new ArrayList<>();

		Set<String> backgroundImageIds = jsonObject.keySet();

		for (String backgroundImageId : backgroundImageIds) {
			JSONObject imageJSONObject = jsonObject.getJSONObject(
				backgroundImageId);

			Map<String, String> localeMap = _toLocaleMap(imageJSONObject);

			fragmentFields.add(
				new FragmentField() {
					{
						id = backgroundImageId;
						value = _toFragmentFieldBackgroundImage(
							imageJSONObject, localeMap, saveMapping);
					}
				});
		}

		return fragmentFields;
	}

	private Map<String, Object> _getFragmentConfig(
		FragmentEntryLink fragmentEntryLink) {

		try {
			return new HashMap<String, Object>() {
				{
					JSONObject jsonObject =
						_fragmentEntryConfigurationParser.
							getConfigurationJSONObject(
								fragmentEntryLink.getConfiguration(),
								fragmentEntryLink.getEditableValues(),
								new long[] {0L});

					Set<String> keys = jsonObject.keySet();

					Iterator<String> iterator = keys.iterator();

					while (iterator.hasNext()) {
						String key = iterator.next();

						Object value = jsonObject.get(key);

						if (value instanceof JSONObject) {
							JSONObject valueJSONObject = (JSONObject)value;

							if (valueJSONObject.has("color")) {
								value = valueJSONObject.getString("color");
							}
							else {
								JSONDeserializer<Map<String, Object>>
									jsonDeserializer =
										JSONFactoryUtil.
											createJSONDeserializer();

								value = jsonDeserializer.deserialize(
									value.toString());
							}
						}

						put(key, value);
					}
				}
			};
		}
		catch (JSONException jsonException) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsonException);
			}

			return null;
		}
	}

	private FragmentEntry _getFragmentEntry(
		FragmentCollectionContributorTracker
			fragmentCollectionContributorTracker,
		long fragmentEntryId, String rendererKey) {

		FragmentEntry fragmentEntry =
			_fragmentEntryLocalService.fetchFragmentEntry(fragmentEntryId);

		if (fragmentEntry != null) {
			return fragmentEntry;
		}

		Map<String, FragmentEntry> fragmentEntries =
			fragmentCollectionContributorTracker.getFragmentEntries();

		return fragmentEntries.get(rendererKey);
	}

	private FragmentField[] _getFragmentFields(
		FragmentEntryLink fragmentEntryLink, boolean saveInlineContent,
		boolean saveMapping) {

		if (!saveInlineContent && !saveMapping) {
			return new FragmentField[0];
		}

		JSONObject editableValuesJSONObject = null;

		try {
			editableValuesJSONObject = JSONFactoryUtil.createJSONObject(
				fragmentEntryLink.getEditableValues());
		}
		catch (JSONException jsonException) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsonException);
			}

			return null;
		}

		List<FragmentField> fragmentFields = new ArrayList<>();

		fragmentFields.addAll(
			_getBackgroundImageFragmentFields(
				editableValuesJSONObject.getJSONObject(
					"com.liferay.fragment.entry.processor.background.image." +
						"BackgroundImageFragmentEntryProcessor"),
				saveMapping));

		JSONObject jsonObject = editableValuesJSONObject.getJSONObject(
			"com.liferay.fragment.entry.processor.editable." +
				"EditableFragmentEntryProcessor");

		if (jsonObject != null) {
			Map<String, String> editableTypes =
				EditableFragmentEntryProcessorUtil.getEditableTypes(
					fragmentEntryLink.getHtml());

			fragmentFields.addAll(
				_getTextFragmentFields(editableTypes, jsonObject, saveMapping));
		}

		return fragmentFields.toArray(new FragmentField[0]);
	}

	private String _getFragmentKey(
		FragmentEntry fragmentEntry, String rendererKey) {

		if (fragmentEntry != null) {
			return fragmentEntry.getFragmentEntryKey();
		}

		return rendererKey;
	}

	private Function<Object, String> _getImageURLTransformerFunction() {
		return object -> {
			if (object instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject)object;

				return jsonObject.getString("url");
			}

			return StringPool.BLANK;
		};
	}

	private List<FragmentField> _getTextFragmentFields(
		Map<String, String> editableTypes, JSONObject jsonObject,
		boolean saveMapping) {

		List<FragmentField> fragmentFields = new ArrayList<>();

		Set<String> textIds = jsonObject.keySet();

		for (String textId : textIds) {
			fragmentFields.add(
				_toFragmentField(
					editableTypes, jsonObject, saveMapping, textId));
		}

		return fragmentFields;
	}

	private WidgetInstance[] _getWidgetInstances(
		FragmentEntryLink fragmentEntryLink) {

		List<String> fragmentEntryLinkPortletIds =
			_portletRegistry.getFragmentEntryLinkPortletIds(fragmentEntryLink);

		if (ListUtil.isNull(fragmentEntryLinkPortletIds)) {
			return null;
		}

		List<WidgetInstance> widgetInstances = new ArrayList<>();

		for (String fragmentEntryLinkPortletId : fragmentEntryLinkPortletIds) {
			widgetInstances.add(
				_widgetInstanceDTOConverter.toDTO(
					fragmentEntryLink, fragmentEntryLinkPortletId));
		}

		return widgetInstances.toArray(new WidgetInstance[0]);
	}

	private boolean _isSaveFragmentMappedValue(
		JSONObject jsonObject, boolean saveMapping) {

		if (saveMapping && jsonObject.has("classNameId") &&
			jsonObject.has("classPK") && jsonObject.has("fieldId")) {

			return true;
		}

		if (saveMapping && jsonObject.has("collectionFieldId")) {
			return true;
		}

		if (saveMapping && jsonObject.has("mappedField")) {
			return true;
		}

		return false;
	}

	private FragmentInlineValue _toDefaultMappingValue(
		JSONObject jsonObject, Function<Object, String> transformerFunction) {

		long classNameId = jsonObject.getLong("classNameId");

		if (classNameId == 0) {
			return null;
		}

		String className = null;

		try {
			className = _portal.getClassName(classNameId);
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to get class name for default mapping value",
					exception);
			}
		}

		if (Validator.isNull(className)) {
			return null;
		}

		InfoItemFieldValuesProvider<Object> infoItemFieldValuesProvider =
			_infoItemServiceTracker.getFirstInfoItemService(
				InfoItemFieldValuesProvider.class, className);

		InfoItemObjectProvider<Object> infoItemObjectProvider =
			_infoItemServiceTracker.getFirstInfoItemService(
				InfoItemObjectProvider.class, className);

		if ((infoItemFieldValuesProvider == null) ||
			(infoItemObjectProvider == null)) {

			return null;
		}

		long classPK = jsonObject.getLong("classPK");

		try {
			Object infoItem = infoItemObjectProvider.getInfoItem(
				new ClassPKInfoItemIdentifier(classPK));

			if (infoItem == null) {
				return null;
			}

			InfoFieldValue<Object> infoFieldValue =
				infoItemFieldValuesProvider.getInfoItemFieldValue(
					infoItem, jsonObject.getString("fieldId"));

			if (infoFieldValue == null) {
				return null;
			}

			Object infoFieldValueValue = infoFieldValue.getValue(
				LocaleUtil.getMostRelevantLocale());

			if (transformerFunction != null) {
				infoFieldValueValue = transformerFunction.apply(
					infoFieldValueValue);
			}

			String valueString = GetterUtil.getString(infoFieldValueValue);

			if (Validator.isNull(valueString)) {
				return null;
			}

			return new FragmentInlineValue() {
				{
					value = valueString;
				}
			};
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to get default mapped value", exception);
			}
		}

		return null;
	}

	private FragmentInlineValue _toDescriptionFragmentInlineValue(
		JSONObject jsonObject) {

		JSONObject configJSONObject = jsonObject.getJSONObject("config");

		if (configJSONObject == null) {
			return null;
		}

		String alt = configJSONObject.getString("alt");

		if (Validator.isNull(alt)) {
			return null;
		}

		return new FragmentInlineValue() {
			{
				value = alt;
			}
		};
	}

	private FragmentField _toFragmentField(
		Map<String, String> editableTypes, JSONObject jsonObject,
		boolean saveMapping, String textId) {

		JSONObject textJSONObject = jsonObject.getJSONObject(textId);

		return new FragmentField() {
			{
				id = textId;

				setValue(
					() -> {
						String type = editableTypes.getOrDefault(
							textId, "text");

						if (Objects.equals(type, "html")) {
							return _toFragmentFieldHTML(
								textJSONObject, saveMapping);
						}

						if (Objects.equals(type, "image")) {
							return _toFragmentFieldImage(
								textJSONObject, saveMapping);
						}

						return _toFragmentFieldText(
							textJSONObject, saveMapping);
					});
			}
		};
	}

	private FragmentFieldBackgroundImage _toFragmentFieldBackgroundImage(
		JSONObject jsonObject, Map<String, String> localeMap,
		boolean saveMapping) {

		return new FragmentFieldBackgroundImage() {
			{
				backgroundFragmentImage = new FragmentImage() {
					{
						title = _toTitleFragmentInlineValue(
							jsonObject, localeMap);

						setUrl(
							() -> {
								if (_isSaveFragmentMappedValue(
										jsonObject, saveMapping)) {

									return _toFragmentMappedValue(
										_toDefaultMappingValue(
											jsonObject,
											_getImageURLTransformerFunction()),
										jsonObject);
								}

								return new FragmentInlineValue() {
									{
										value_i18n = localeMap;
									}
								};
							});
					}
				};
			}
		};
	}

	private FragmentFieldHTML _toFragmentFieldHTML(
		JSONObject jsonObject, boolean saveMapping) {

		return new FragmentFieldHTML() {
			{
				setHtml(
					() -> {
						if (_isSaveFragmentMappedValue(
								jsonObject, saveMapping)) {

							return _toFragmentMappedValue(
								_toDefaultMappingValue(jsonObject, null),
								jsonObject);
						}

						return new FragmentInlineValue() {
							{
								value_i18n = _toLocaleMap(jsonObject);
							}
						};
					});
			}
		};
	}

	private FragmentFieldImage _toFragmentFieldImage(
		JSONObject jsonObject, boolean saveMapping) {

		Map<String, String> localeMap = _toLocaleMap(jsonObject);

		return new FragmentFieldImage() {
			{
				fragmentImage = new FragmentImage() {
					{
						description = _toDescriptionFragmentInlineValue(
							jsonObject);
						title = _toTitleFragmentInlineValue(
							jsonObject, localeMap);

						setUrl(
							() -> {
								if (_isSaveFragmentMappedValue(
										jsonObject, saveMapping)) {

									return _toFragmentMappedValue(
										_toDefaultMappingValue(
											jsonObject,
											_getImageURLTransformerFunction()),
										jsonObject);
								}

								return new FragmentInlineValue() {
									{
										value_i18n = localeMap;
									}
								};
							});
					}
				};
				fragmentLink = _toFragmentLink(jsonObject, saveMapping);
			}
		};
	}

	private FragmentFieldText _toFragmentFieldText(
		JSONObject jsonObject, boolean saveMapping) {

		return new FragmentFieldText() {
			{
				fragmentLink = _toFragmentLink(jsonObject, saveMapping);

				setText(
					() -> {
						if (_isSaveFragmentMappedValue(
								jsonObject, saveMapping)) {

							return _toFragmentMappedValue(
								_toDefaultMappingValue(jsonObject, null),
								jsonObject);
						}

						Map<String, String> localeMap = _toLocaleMap(
							jsonObject);

						if (MapUtil.isEmpty(localeMap)) {
							return null;
						}

						return new FragmentInlineValue() {
							{
								value_i18n = localeMap;
							}
						};
					});
			}
		};
	}

	private FragmentLink _toFragmentLink(
		JSONObject jsonObject, boolean saveMapping) {

		JSONObject configJSONObject = jsonObject.getJSONObject("config");

		if (configJSONObject.isNull("href") &&
			!_isSaveFragmentMappedValue(configJSONObject, saveMapping)) {

			return null;
		}

		return new FragmentLink() {
			{
				setHref(
					() -> {
						if (_isSaveFragmentMappedValue(
								configJSONObject, saveMapping)) {

							return _toFragmentMappedValue(
								_toDefaultMappingValue(configJSONObject, null),
								configJSONObject);
						}

						return new FragmentInlineValue() {
							{
								value = configJSONObject.getString("href");
							}
						};
					});

				setTarget(
					() -> {
						String target = configJSONObject.getString("target");

						if (Validator.isNull(target)) {
							return null;
						}

						return Target.create(
							StringUtil.upperCaseFirstLetter(
								target.substring(1)));
					});
			}
		};
	}

	private FragmentMappedValue _toFragmentMappedValue(
		FragmentInlineValue fragmentInlineValue, JSONObject jsonObject) {

		return new FragmentMappedValue() {
			{
				mapping = new Mapping() {
					{
						defaultFragmentInlineValue = fragmentInlineValue;
						itemReference = _toItemReference(jsonObject);

						setFieldKey(
							() -> {
								String collectionFieldId = jsonObject.getString(
									"collectionFieldId");

								if (Validator.isNotNull(collectionFieldId)) {
									return collectionFieldId;
								}

								String fieldId = jsonObject.getString(
									"fieldId");

								if (Validator.isNotNull(fieldId)) {
									return fieldId;
								}

								String mappedField = jsonObject.getString(
									"mappedField");

								if (Validator.isNotNull(mappedField)) {
									return mappedField;
								}

								return null;
							});
					}
				};
			}
		};
	}

	private String _toItemClassName(JSONObject jsonObject) {
		String classNameIdString = jsonObject.getString("classNameId");

		if (Validator.isNull(classNameIdString)) {
			return null;
		}

		long classNameId = 0;

		try {
			classNameId = Long.parseLong(classNameIdString);
		}
		catch (NumberFormatException numberFormatException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					String.format(
						"Item class name could not be set since class name " +
							"ID %s could not be parsed to a long",
						classNameIdString),
					numberFormatException);
			}

			return null;
		}

		String className = null;

		try {
			className = _portal.getClassName(classNameId);
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Item class name could not be set since no class name " +
						"could be obtained for class name ID " + classNameId,
					exception);
			}

			return null;
		}

		return className;
	}

	private Long _toitemClassPK(JSONObject jsonObject) {
		String classPKString = jsonObject.getString("classPK");

		if (Validator.isNull(classPKString)) {
			return null;
		}

		Long classPK = null;

		try {
			classPK = Long.parseLong(classPKString);
		}
		catch (NumberFormatException numberFormatException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					String.format(
						"Item class PK could not be set since class PK %s " +
							"could not be parsed to a long",
						classPKString),
					numberFormatException);
			}

			return null;
		}

		return classPK;
	}

	private Object _toItemReference(JSONObject jsonObject) {
		String collectionFieldId = jsonObject.getString("collectionFieldId");
		String fieldId = jsonObject.getString("fieldId");
		String mappedField = jsonObject.getString("mappedField");

		if (Validator.isNull(collectionFieldId) && Validator.isNull(fieldId) &&
			Validator.isNull(mappedField)) {

			return null;
		}

		if (Validator.isNotNull(collectionFieldId)) {
			return new ContextReference() {
				{
					contextSource = ContextSource.COLLECTION_ITEM;
				}
			};
		}

		if (Validator.isNotNull(mappedField)) {
			return new ContextReference() {
				{
					contextSource = ContextSource.DISPLAY_PAGE_ITEM;
				}
			};
		}

		return new ClassPKReference() {
			{
				className = _toItemClassName(jsonObject);
				classPK = _toitemClassPK(jsonObject);
			}
		};
	}

	private Map<String, String> _toLocaleMap(JSONObject jsonObject) {
		return new HashMap<String, String>() {
			{
				List<String> availableLanguageIds = _getAvailableLanguageIds();

				Set<String> keys = jsonObject.keySet();

				Iterator<String> iterator = keys.iterator();

				while (iterator.hasNext()) {
					String key = iterator.next();

					if (availableLanguageIds.contains(key)) {
						put(key, jsonObject.getString(key));
					}
				}
			}
		};
	}

	private FragmentInlineValue _toTitleFragmentInlineValue(
		JSONObject jsonObject, Map<String, String> map) {

		JSONObject configJSONObject = jsonObject.getJSONObject("config");

		if (configJSONObject == null) {
			return null;
		}

		String imageTitle = configJSONObject.getString("imageTitle");

		if (Validator.isNull(imageTitle) || map.containsValue(imageTitle)) {
			return null;
		}

		return new FragmentInlineValue() {
			{
				value = imageTitle;
			}
		};
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PageFragmentInstanceDefinitionDTOConverter.class);

	@Reference
	private FragmentCollectionContributorTracker
		_fragmentCollectionContributorTracker;

	@Reference
	private FragmentEntryConfigurationParser _fragmentEntryConfigurationParser;

	@Reference
	private FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;

	@Reference
	private FragmentEntryLocalService _fragmentEntryLocalService;

	@Reference
	private InfoItemServiceTracker _infoItemServiceTracker;

	@Reference
	private Portal _portal;

	@Reference
	private PortletRegistry _portletRegistry;

	@Reference
	private WidgetInstanceDTOConverter _widgetInstanceDTOConverter;

}