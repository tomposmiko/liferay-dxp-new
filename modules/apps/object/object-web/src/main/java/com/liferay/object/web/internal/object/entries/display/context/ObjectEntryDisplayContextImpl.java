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

package com.liferay.object.web.internal.object.entries.display.context;

import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldValidation;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldValidationExpression;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.model.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.util.NumericDDMFormFieldUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.criteria.InfoItemItemSelectorReturnType;
import com.liferay.item.selector.criteria.info.item.criterion.InfoItemItemSelectorCriterion;
import com.liferay.object.constants.ObjectActionKeys;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectFieldSettingConstants;
import com.liferay.object.constants.ObjectRelationshipConstants;
import com.liferay.object.constants.ObjectWebKeys;
import com.liferay.object.display.context.ObjectEntryDisplayContext;
import com.liferay.object.exception.NoSuchObjectLayoutException;
import com.liferay.object.field.business.type.ObjectFieldBusinessType;
import com.liferay.object.field.business.type.ObjectFieldBusinessTypeRegistry;
import com.liferay.object.field.render.ObjectFieldRenderingContext;
import com.liferay.object.field.setting.util.ObjectFieldSettingUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectLayout;
import com.liferay.object.model.ObjectLayoutBox;
import com.liferay.object.model.ObjectLayoutColumn;
import com.liferay.object.model.ObjectLayoutRow;
import com.liferay.object.model.ObjectLayoutTab;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.rest.dto.v1_0.FileEntry;
import com.liferay.object.rest.dto.v1_0.ListEntry;
import com.liferay.object.rest.dto.v1_0.ObjectEntry;
import com.liferay.object.rest.manager.v1_0.ObjectEntryManager;
import com.liferay.object.rest.manager.v1_0.ObjectEntryManagerRegistry;
import com.liferay.object.scope.ObjectScopeProvider;
import com.liferay.object.scope.ObjectScopeProviderRegistry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectEntryService;
import com.liferay.object.service.ObjectEntryServiceUtil;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectLayoutLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.object.web.internal.display.context.helper.ObjectRequestHelper;
import com.liferay.object.web.internal.security.permission.resource.util.ObjectDefinitionResourcePermissionUtil;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.taglib.servlet.PipingServletResponseFactory;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletRequest;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Marco Leo
 */
public class ObjectEntryDisplayContextImpl
	implements ObjectEntryDisplayContext {

	public ObjectEntryDisplayContextImpl(
		DDMFormRenderer ddmFormRenderer, HttpServletRequest httpServletRequest,
		ItemSelector itemSelector,
		ObjectDefinitionLocalService objectDefinitionLocalService,
		ObjectEntryManagerRegistry objectEntryManagerRegistry,
		ObjectEntryLocalService objectEntryLocalService,
		ObjectEntryService objectEntryService,
		ObjectFieldBusinessTypeRegistry objectFieldBusinessTypeRegistry,
		ObjectFieldLocalService objectFieldLocalService,
		ObjectLayoutLocalService objectLayoutLocalService,
		ObjectRelationshipLocalService objectRelationshipLocalService,
		ObjectScopeProviderRegistry objectScopeProviderRegistry) {

		_ddmFormRenderer = ddmFormRenderer;
		_itemSelector = itemSelector;
		_objectDefinitionLocalService = objectDefinitionLocalService;
		_objectEntryManagerRegistry = objectEntryManagerRegistry;
		_objectEntryLocalService = objectEntryLocalService;
		_objectEntryService = objectEntryService;
		_objectFieldBusinessTypeRegistry = objectFieldBusinessTypeRegistry;
		_objectFieldLocalService = objectFieldLocalService;
		_objectLayoutLocalService = objectLayoutLocalService;
		_objectRelationshipLocalService = objectRelationshipLocalService;
		_objectScopeProviderRegistry = objectScopeProviderRegistry;

		_objectRequestHelper = new ObjectRequestHelper(httpServletRequest);
		_readOnly = (Boolean)httpServletRequest.getAttribute(
			ObjectWebKeys.OBJECT_ENTRY_READ_ONLY);
		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public ObjectDefinition getObjectDefinition1() {
		HttpServletRequest httpServletRequest =
			_objectRequestHelper.getRequest();

		return (ObjectDefinition)httpServletRequest.getAttribute(
			ObjectWebKeys.OBJECT_DEFINITION);
	}

	@Override
	public ObjectDefinition getObjectDefinition2() throws PortalException {
		ObjectLayoutTab objectLayoutTab = getObjectLayoutTab();

		ObjectRelationship objectRelationship =
			_objectRelationshipLocalService.getObjectRelationship(
				objectLayoutTab.getObjectRelationshipId());

		return _objectDefinitionLocalService.getObjectDefinition(
			objectRelationship.getObjectDefinitionId2());
	}

	@Override
	public com.liferay.object.model.ObjectEntry getObjectEntry()
		throws PortalException {

		return _toServiceBuilderObjectEntry(_getObjectEntry());
	}

	@Override
	public ObjectLayout getObjectLayout() throws PortalException {
		ObjectDefinition objectDefinition = getObjectDefinition1();

		try {
			return _objectLayoutLocalService.getDefaultObjectLayout(
				objectDefinition.getObjectDefinitionId());
		}
		catch (NoSuchObjectLayoutException noSuchObjectLayoutException) {
			if (_log.isDebugEnabled()) {
				_log.debug(noSuchObjectLayoutException);
			}

			return null;
		}
	}

	@Override
	public ObjectLayoutBox getObjectLayoutBox(String type)
		throws PortalException {

		ObjectDefinition objectDefinition = getObjectDefinition1();

		if (!StringUtil.equals(
				objectDefinition.getStorageType(),
				ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT)) {

			return null;
		}

		ObjectLayoutTab objectLayoutTab = getObjectLayoutTab();

		if (objectLayoutTab == null) {
			return null;
		}

		for (ObjectLayoutBox objectLayoutBox :
				objectLayoutTab.getObjectLayoutBoxes()) {

			if (StringUtil.equals(objectLayoutBox.getType(), type)) {
				return objectLayoutBox;
			}
		}

		return null;
	}

	@Override
	public ObjectLayoutTab getObjectLayoutTab() throws PortalException {
		ObjectLayout objectLayout = getObjectLayout();

		if (objectLayout == null) {
			return null;
		}

		List<ObjectLayoutTab> objectLayoutTabs =
			objectLayout.getObjectLayoutTabs();

		long objectLayoutTabId = ParamUtil.getLong(
			_objectRequestHelper.getRequest(), "screenNavigationCategoryKey");

		if (objectLayoutTabId == 0) {
			return objectLayoutTabs.get(0);
		}

		for (ObjectLayoutTab objectLayoutTab : objectLayoutTabs) {
			if (objectLayoutTab.getObjectLayoutTabId() == objectLayoutTabId) {
				return objectLayoutTab;
			}
		}

		return objectLayoutTabs.get(0);
	}

	@Override
	public ObjectRelationship getObjectRelationship() throws PortalException {
		ObjectLayoutTab objectLayoutTab = getObjectLayoutTab();

		return _objectRelationshipLocalService.getObjectRelationship(
			objectLayoutTab.getObjectRelationshipId());
	}

	@Override
	public String getObjectRelationshipERCObjectFieldName() {
		HttpServletRequest httpServletRequest =
			_objectRequestHelper.getRequest();

		return httpServletRequest.getParameter(
			ObjectFieldSettingConstants.
				NAME_OBJECT_RELATIONSHIP_ERC_OBJECT_FIELD_NAME);
	}

	@Override
	public String getParentObjectEntryId() {
		HttpServletRequest httpServletRequest =
			_objectRequestHelper.getRequest();

		return httpServletRequest.getParameter("parentObjectEntryERC");
	}

	@Override
	public CreationMenu getRelatedModelCreationMenu(
			ObjectRelationship objectRelationship)
		throws PortalException {

		if (_readOnly || isGuestUser()) {
			return null;
		}

		CreationMenu creationMenu = new CreationMenu();

		ObjectDefinition objectDefinition1 = getObjectDefinition1();

		ObjectDefinition objectDefinition2 =
			_objectDefinitionLocalService.getObjectDefinition(
				objectRelationship.getObjectDefinitionId2());

		ObjectScopeProvider objectScopeProvider =
			_objectScopeProviderRegistry.getObjectScopeProvider(
				objectDefinition2.getScope());

		if (!objectDefinition1.isUnmodifiableSystemObject() &&
			!objectDefinition2.isUnmodifiableSystemObject() &&
			ObjectEntryServiceUtil.hasPortletResourcePermission(
				objectScopeProvider.getGroupId(
					_objectRequestHelper.getRequest()),
				objectDefinition2.getObjectDefinitionId(),
				ObjectActionKeys.ADD_OBJECT_ENTRY) &&
			!(StringUtil.equals(
				objectDefinition1.getScope(),
				ObjectDefinitionConstants.SCOPE_COMPANY) &&
			  StringUtil.equals(
				  objectDefinition2.getScope(),
				  ObjectDefinitionConstants.SCOPE_SITE)) &&
			StringUtil.equals(
				objectRelationship.getType(),
				ObjectRelationshipConstants.TYPE_ONE_TO_MANY)) {

			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			creationMenu.addDropdownItem(
				dropdownItem -> {
					dropdownItem.setHref(
						PortletURLBuilder.create(
							PortalUtil.getControlPanelPortletURL(
								_objectRequestHelper.getRequest(),
								serviceContext.getScopeGroup(),
								objectDefinition2.getPortletId(), 0, 0,
								PortletRequest.RENDER_PHASE)
						).setMVCRenderCommandName(
							"/object_entries/edit_object_entry"
						).setBackURL(
							_objectRequestHelper.getCurrentURL()
						).setParameter(
							ObjectFieldSettingConstants.
								NAME_OBJECT_RELATIONSHIP_ERC_OBJECT_FIELD_NAME,
							ObjectFieldSettingUtil.getValue(
								ObjectFieldSettingConstants.
									NAME_OBJECT_RELATIONSHIP_ERC_OBJECT_FIELD_NAME,
								_objectFieldLocalService.getObjectField(
									objectRelationship.getObjectFieldId2()))
						).setParameter(
							"objectDefinitionId",
							objectDefinition2.getObjectDefinitionId()
						).setParameter(
							"parentObjectEntryERC",
							() -> {
								ObjectEntry objectEntry = _getObjectEntry();

								return String.valueOf(
									objectEntry.getExternalReferenceCode());
							}
						).setWindowState(
							WindowState.MAXIMIZED
						).buildString());
					dropdownItem.setLabel(
						LanguageUtil.get(
							_objectRequestHelper.getRequest(), "create-new"));
				});
		}

		LiferayPortletResponse liferayPortletResponse =
			_objectRequestHelper.getLiferayPortletResponse();

		creationMenu.addDropdownItem(
			dropdownItem -> {
				dropdownItem.setHref(
					liferayPortletResponse.getNamespace() +
						"selectRelatedModel");
				dropdownItem.setLabel(
					LanguageUtil.get(
						_objectRequestHelper.getRequest(),
						"select-existing-one"));
				dropdownItem.setTarget("event");
			});

		return creationMenu;
	}

	@Override
	public String getRelatedObjectEntryItemSelectorURL(
			ObjectRelationship objectRelationship)
		throws PortalException {

		RequestBackedPortletURLFactory requestBackedPortletURLFactory =
			RequestBackedPortletURLFactoryUtil.create(
				_objectRequestHelper.getRequest());

		LiferayPortletResponse liferayPortletResponse =
			_objectRequestHelper.getLiferayPortletResponse();

		InfoItemItemSelectorCriterion infoItemItemSelectorCriterion =
			new InfoItemItemSelectorCriterion();

		infoItemItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			Collections.<ItemSelectorReturnType>singletonList(
				new InfoItemItemSelectorReturnType()));

		ObjectDefinition objectDefinition2 =
			_objectDefinitionLocalService.getObjectDefinition(
				objectRelationship.getObjectDefinitionId2());

		infoItemItemSelectorCriterion.setItemType(
			objectDefinition2.getClassName());

		return PortletURLBuilder.create(
			_itemSelector.getItemSelectorURL(
				requestBackedPortletURLFactory,
				liferayPortletResponse.getNamespace() +
					"selectRelatedModalEntry",
				infoItemItemSelectorCriterion)
		).setParameter(
			"groupId", _getGroupId()
		).setParameter(
			"objectDefinitionId",
			() -> {
				ObjectDefinition objectDefinition1 =
					_objectDefinitionLocalService.getObjectDefinition(
						objectRelationship.getObjectDefinitionId1());

				return objectDefinition1.getObjectDefinitionId();
			}
		).setParameter(
			"objectEntryId",
			() -> {
				ObjectEntry objectEntry = _getObjectEntry();

				return GetterUtil.getLong(objectEntry.getId());
			}
		).setParameter(
			"objectRelationshipId", objectRelationship.getObjectRelationshipId()
		).setParameter(
			"objectRelationshipType", objectRelationship.getType()
		).buildString();
	}

	@Override
	public Map<String, String> getRelationshipContextParams()
		throws PortalException {

		return HashMapBuilder.put(
			"objectEntryId", String.valueOf(_objectEntry.getId())
		).put(
			"objectRelationshipId",
			() -> {
				ObjectLayoutTab objectLayoutTab = getObjectLayoutTab();

				return String.valueOf(
					objectLayoutTab.getObjectRelationshipId());
			}
		).put(
			"readOnly", String.valueOf(_readOnly || isGuestUser())
		).build();
	}

	@Override
	public boolean isGuestUser() {
		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker == null) {
			return true;
		}

		User user = permissionChecker.getUser();

		return user.isGuestUser();
	}

	@Override
	public boolean isReadOnly() {
		if (_readOnly) {
			return true;
		}

		try {
			ObjectEntry objectEntry = _getObjectEntry();

			if (objectEntry == null) {
				return false;
			}

			return !ObjectDefinitionResourcePermissionUtil.
				hasModelResourcePermission(
					getObjectDefinition1(), objectEntry, _objectEntryService,
					ActionKeys.UPDATE);
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}

		return false;
	}

	@Override
	public boolean isShowObjectEntryForm() throws PortalException {
		if ((getObjectEntry() == null) || (getObjectLayoutTab() == null)) {
			return true;
		}

		HttpServletRequest httpServletRequest =
			_objectRequestHelper.getRequest();

		return GetterUtil.getBoolean(
			httpServletRequest.getAttribute(
				ObjectWebKeys.REGULAR_OBJECT_LAYOUT_TAB));
	}

	@Override
	public String renderDDMForm(PageContext pageContext)
		throws PortalException {

		ObjectEntry objectEntry = _getObjectEntry();
		ObjectLayoutTab objectLayoutTab = getObjectLayoutTab();

		DDMForm ddmForm = _getDDMForm(objectEntry, objectLayoutTab);

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setContainerId("editObjectEntry");

		if (objectEntry != null) {
			ddmFormRenderingContext.addProperty(
				"objectEntryId", objectEntry.getId());

			DDMFormValues ddmFormValues = _getDDMFormValues(
				ddmForm, objectEntry);

			if (ddmFormValues != null) {
				ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
			}
		}

		ddmFormRenderingContext.setGroupId(_getGroupId());
		ddmFormRenderingContext.setHttpServletRequest(
			_objectRequestHelper.getRequest());
		ddmFormRenderingContext.setHttpServletResponse(
			PipingServletResponseFactory.createPipingServletResponse(
				pageContext));
		ddmFormRenderingContext.setLocale(_objectRequestHelper.getLocale());

		LiferayPortletResponse liferayPortletResponse =
			_objectRequestHelper.getLiferayPortletResponse();

		ddmFormRenderingContext.setPortletNamespace(
			liferayPortletResponse.getNamespace());

		ddmFormRenderingContext.setShowRequiredFieldsWarning(true);

		if (objectLayoutTab == null) {
			return _ddmFormRenderer.render(ddmForm, ddmFormRenderingContext);
		}

		return _ddmFormRenderer.render(
			ddmForm, _getDDMFormLayout(ddmForm, objectLayoutTab),
			ddmFormRenderingContext);
	}

	private void _addDDMFormFields(
			DDMForm ddmForm, ObjectEntry objectEntry,
			List<ObjectField> objectFields, ObjectLayoutTab objectLayoutTab,
			boolean readOnly)
		throws PortalException {

		for (ObjectLayoutBox objectLayoutBox :
				objectLayoutTab.getObjectLayoutBoxes()) {

			List<DDMFormField> nestedDDMFormFields = _getNestedDDMFormFields(
				objectEntry, objectFields, objectLayoutBox, readOnly);

			if (nestedDDMFormFields.isEmpty()) {
				continue;
			}

			ddmForm.addDDMFormField(
				new DDMFormField(
					String.valueOf(objectLayoutBox.getPrimaryKey()),
					"fieldset") {

					{
						setLabel(
							new LocalizedValue() {
								{
									addString(
										_objectRequestHelper.getLocale(),
										objectLayoutBox.getName(
											_objectRequestHelper.getLocale()));
								}
							});
						setLocalizable(false);
						setNestedDDMFormFields(nestedDDMFormFields);
						setProperty(
							"collapsible", objectLayoutBox.isCollapsable());
						setProperty("rows", _getRows(objectLayoutBox));
						setReadOnly(false);
						setRepeatable(false);
						setRequired(false);
						setShowLabel(true);
					}
				});
		}
	}

	private ObjectFieldRenderingContext _createObjectFieldRenderingContext(
			ObjectEntry objectEntry)
		throws PortalException {

		ObjectFieldRenderingContext objectFieldRenderingContext =
			new ObjectFieldRenderingContext();

		objectFieldRenderingContext.setGroupId(
			_objectRequestHelper.getScopeGroupId());
		objectFieldRenderingContext.setHttpServletRequest(
			_objectRequestHelper.getRequest());
		objectFieldRenderingContext.setLocale(_objectRequestHelper.getLocale());

		if (objectEntry != null) {
			objectFieldRenderingContext.setExternalReferenceCode(
				objectEntry.getExternalReferenceCode());
			objectFieldRenderingContext.setProperties(
				objectEntry.getProperties());
		}

		objectFieldRenderingContext.setPortletId(
			_objectRequestHelper.getPortletId());
		objectFieldRenderingContext.setUserId(_objectRequestHelper.getUserId());

		return objectFieldRenderingContext;
	}

	private DDMForm _getDDMForm(
			ObjectEntry objectEntry, ObjectLayoutTab objectLayoutTab)
		throws PortalException {

		DDMForm ddmForm = new DDMForm();

		ddmForm.addAvailableLocale(_objectRequestHelper.getLocale());

		ObjectDefinition objectDefinition = getObjectDefinition1();

		boolean readOnly = _readOnly;

		if (!readOnly && (objectEntry != null)) {
			readOnly =
				!ObjectDefinitionResourcePermissionUtil.
					hasModelResourcePermission(
						objectDefinition, objectEntry, _objectEntryService,
						ActionKeys.UPDATE);
		}

		List<ObjectField> objectFields =
			_objectFieldLocalService.getCustomObjectFields(
				objectDefinition.getObjectDefinitionId());

		if (objectLayoutTab == null) {
			for (ObjectField objectField : objectFields) {
				if (!_isActive(objectField)) {
					continue;
				}

				if (objectField.compareBusinessType(
						ObjectFieldConstants.BUSINESS_TYPE_AGGREGATION) ||
					objectField.compareBusinessType(
						ObjectFieldConstants.BUSINESS_TYPE_FORMULA)) {

					ddmForm.addDDMFormField(
						_getDDMFormField(objectEntry, objectField, true));
				}
				else {
					ddmForm.addDDMFormField(
						_getDDMFormField(objectEntry, objectField, readOnly));
				}
			}
		}
		else {
			_addDDMFormFields(
				ddmForm, objectEntry, objectFields, objectLayoutTab, readOnly);
		}

		ddmForm.setDefaultLocale(_objectRequestHelper.getLocale());

		return ddmForm;
	}

	private DDMFormField _getDDMFormField(
			ObjectEntry objectEntry, ObjectField objectField, boolean readOnly)
		throws PortalException {

		// TODO Store the type and the object field type in the database

		ObjectFieldBusinessType objectFieldBusinessType =
			_objectFieldBusinessTypeRegistry.getObjectFieldBusinessType(
				objectField.getBusinessType());

		DDMFormField ddmFormField = new DDMFormField(
			objectField.getName(),
			objectFieldBusinessType.getDDMFormFieldTypeName());

		Map<String, Object> properties = objectFieldBusinessType.getProperties(
			objectField, _createObjectFieldRenderingContext(objectEntry));

		ddmFormField.setDDMFormFieldValidation(
			_getDDMFormFieldValidation(
				objectField.getBusinessType(), objectField.getName(),
				properties));

		LocalizedValue ddmFormFieldLabelLocalizedValue = new LocalizedValue(
			_objectRequestHelper.getLocale());

		ddmFormFieldLabelLocalizedValue.addString(
			_objectRequestHelper.getLocale(),
			objectField.getLabel(_objectRequestHelper.getLocale()));

		ddmFormField.setLabel(ddmFormFieldLabelLocalizedValue);

		properties.forEach(
			(key, value) -> ddmFormField.setProperty(key, value));

		ddmFormField.setProperty(
			"objectFieldId", String.valueOf(objectField.getObjectFieldId()));

		if (Validator.isNotNull(objectField.getRelationshipType())) {
			ObjectRelationship objectRelationship =
				_objectRelationshipLocalService.
					fetchObjectRelationshipByObjectFieldId2(
						objectField.getObjectFieldId());

			ddmFormField.setProperty(
				"objectDefinitionId",
				String.valueOf(objectRelationship.getObjectDefinitionId1()));

			long parameterObjectFieldId =
				objectRelationship.getParameterObjectFieldId();

			if (parameterObjectFieldId > 0) {
				ObjectField parameterObjectField =
					_objectFieldLocalService.getObjectField(
						parameterObjectFieldId);

				ddmFormField.setProperty(
					"parameterObjectFieldName", parameterObjectField.getName());
			}
		}
		else if (StringUtil.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT) &&
				 (objectEntry != null)) {

			ObjectDefinition objectDefinition = getObjectDefinition1();

			ddmFormField.setProperty(
				"objectDefinitionExternalReferenceCode",
				objectDefinition.getExternalReferenceCode());

			ddmFormField.setProperty(
				"objectEntryExternalReferenceCode",
				objectEntry.getExternalReferenceCode());
		}

		if (GetterUtil.getBoolean(
				ddmFormField.getProperty(
					"accountEntryRestrictedObjectField")) &&
			(objectEntry != null)) {

			ddmFormField.setReadOnly(true);
		}
		else {
			ddmFormField.setReadOnly(readOnly);
		}

		ddmFormField.setRequired(objectField.isRequired());

		return ddmFormField;
	}

	private DDMFormFieldValidation _getDDMFormFieldValidation(
		String businessType, String objectFieldName,
		Map<String, Object> properties) {

		int defaultMaxLength = 0;

		if (Objects.equals(
				businessType, ObjectFieldConstants.BUSINESS_TYPE_LONG_TEXT)) {

			defaultMaxLength = 65000;
		}
		else if (Objects.equals(
					businessType, ObjectFieldConstants.BUSINESS_TYPE_TEXT)) {

			defaultMaxLength = 280;
		}

		if ((defaultMaxLength > 0) &&
			GetterUtil.getBoolean(properties.get("showCounter"))) {

			DDMFormFieldValidation ddmFormFieldValidation =
				new DDMFormFieldValidation();

			DDMFormFieldValidationExpression ddmFormFieldValidationExpression =
				new DDMFormFieldValidationExpression();

			int maxLength = GetterUtil.getInteger(
				properties.get("maxLength"), defaultMaxLength);

			ddmFormFieldValidationExpression.setValue(
				StringBundler.concat(
					"length(", objectFieldName, ") <= ", maxLength));

			ddmFormFieldValidation.setDDMFormFieldValidationExpression(
				ddmFormFieldValidationExpression);

			return ddmFormFieldValidation;
		}

		return null;
	}

	private DDMFormLayout _getDDMFormLayout(
		DDMForm ddmForm, ObjectLayoutTab objectLayoutTab) {

		DDMFormLayout ddmFormLayout = new DDMFormLayout();

		DDMFormLayoutPage ddmFormLayoutPage = new DDMFormLayoutPage();

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(false);

		for (ObjectLayoutBox objectLayoutBox :
				objectLayoutTab.getObjectLayoutBoxes()) {

			if (!ddmFormFieldsMap.containsKey(
					String.valueOf(objectLayoutBox.getPrimaryKey()))) {

				continue;
			}

			DDMFormLayoutRow ddmFormLayoutRow = new DDMFormLayoutRow();

			DDMFormLayoutColumn ddmFormLayoutColumn = new DDMFormLayoutColumn();

			ddmFormLayoutColumn.setDDMFormFieldNames(
				ListUtil.fromArray(
					String.valueOf(objectLayoutBox.getPrimaryKey())));

			ddmFormLayoutColumn.setSize(12);

			ddmFormLayoutRow.addDDMFormLayoutColumn(ddmFormLayoutColumn);

			ddmFormLayoutPage.addDDMFormLayoutRow(ddmFormLayoutRow);
		}

		ddmFormLayout.addDDMFormLayoutPage(ddmFormLayoutPage);

		return ddmFormLayout;
	}

	private DDMFormValues _getDDMFormValues(
		DDMForm ddmForm, ObjectEntry objectEntry) {

		Map<String, Object> values = objectEntry.getProperties();

		if (values.isEmpty()) {
			return null;
		}

		_setDateDDMFormFieldValue(ddmForm.getDDMFormFields(), values);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		ddmFormValues.addAvailableLocale(_objectRequestHelper.getLocale());

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(false);

		ddmFormValues.setDDMFormFieldValues(
			TransformUtil.transform(
				ddmFormFieldsMap.values(),
				ddmFormField -> {
					DDMFormFieldValue ddmFormFieldValue =
						new DDMFormFieldValue();

					ddmFormFieldValue.setName(ddmFormField.getName());
					ddmFormFieldValue.setNestedDDMFormFields(
						_getNestedDDMFormFieldValues(
							ddmFormField.getNestedDDMFormFields(), values));

					if (!StringUtil.equals(
							ddmFormField.getType(),
							DDMFormFieldTypeConstants.FIELDSET)) {

						_setDDMFormFieldValueValue(
							ddmFormField, ddmFormFieldValue, values);
					}

					// TODO Temporary workaround for LPS-171782

					if (GetterUtil.getBoolean(
							ddmFormField.getProperty(
								"accountEntryRestrictedObjectField"))) {

						Value value = ddmFormFieldValue.getValue();

						ddmFormField.setReadOnly(
							Validator.isNotNull(
								value.getString(LocaleUtil.ROOT)));
					}

					return ddmFormFieldValue;
				}));

		ddmFormValues.setDefaultLocale(_objectRequestHelper.getLocale());

		return ddmFormValues;
	}

	private DTOConverterContext _getDTOConverterContext() {
		return new DefaultDTOConverterContext(
			false, null, null, _objectRequestHelper.getRequest(), null,
			_themeDisplay.getLocale(), null, _themeDisplay.getUser());
	}

	private long _getGroupId() {
		ObjectDefinition objectDefinition = getObjectDefinition1();

		ObjectScopeProvider objectScopeProvider =
			_objectScopeProviderRegistry.getObjectScopeProvider(
				objectDefinition.getScope());

		try {
			return objectScopeProvider.getGroupId(
				_objectRequestHelper.getRequest());
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return 0L;
		}
	}

	private List<DDMFormField> _getNestedDDMFormFields(
			ObjectEntry objectEntry, List<ObjectField> objectFields,
			ObjectLayoutBox objectLayoutBox, boolean readOnly)
		throws PortalException {

		List<DDMFormField> nestedDDMFormFields = new ArrayList<>();

		for (ObjectLayoutRow objectLayoutRow :
				objectLayoutBox.getObjectLayoutRows()) {

			for (ObjectLayoutColumn objectLayoutColumn :
					objectLayoutRow.getObjectLayoutColumns()) {

				ObjectField currentObjectField = null;

				for (ObjectField objectField : objectFields) {
					if (objectField.getObjectFieldId() ==
							objectLayoutColumn.getObjectFieldId()) {

						currentObjectField = objectField;

						break;
					}
				}

				if ((currentObjectField == null) ||
					!_isActive(currentObjectField)) {

					continue;
				}

				_objectFieldNames.put(
					objectLayoutColumn.getObjectFieldId(),
					currentObjectField.getName());

				if (currentObjectField.compareBusinessType(
						ObjectFieldConstants.BUSINESS_TYPE_AGGREGATION) ||
					currentObjectField.compareBusinessType(
						ObjectFieldConstants.BUSINESS_TYPE_FORMULA)) {

					nestedDDMFormFields.add(
						_getDDMFormField(
							objectEntry, currentObjectField, true));
				}
				else {
					nestedDDMFormFields.add(
						_getDDMFormField(
							objectEntry, currentObjectField, readOnly));
				}
			}
		}

		return nestedDDMFormFields;
	}

	private List<DDMFormFieldValue> _getNestedDDMFormFieldValues(
		List<DDMFormField> ddmFormFields, Map<String, Object> values) {

		return TransformUtil.transform(
			ddmFormFields,
			ddmFormField -> {
				DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

				ddmFormFieldValue.setName(ddmFormField.getName());

				_setDDMFormFieldValueValue(
					ddmFormField, ddmFormFieldValue, values);

				return ddmFormFieldValue;
			});
	}

	private ObjectEntry _getObjectEntry() throws PortalException {
		if (_objectEntry != null) {
			return _objectEntry;
		}

		ObjectDefinition objectDefinition = getObjectDefinition1();

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				objectDefinition.getStorageType());

		String externalReferenceCode = ParamUtil.getString(
			_objectRequestHelper.getRequest(), "externalReferenceCode");

		if (_readOnly && Validator.isNull(externalReferenceCode)) {
			HttpServletRequest httpServletRequest =
				_objectRequestHelper.getRequest();

			externalReferenceCode = (String)httpServletRequest.getAttribute(
				ObjectWebKeys.OBJECT_ENTRY_EXTERNAL_REFERENCE_CODE);
		}

		try {
			_objectEntry = objectEntryManager.getObjectEntry(
				_getDTOConverterContext(), externalReferenceCode,
				_objectRequestHelper.getCompanyId(), objectDefinition,
				String.valueOf(_getGroupId()));
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(exception);
			}
		}

		return _objectEntry;
	}

	private String _getRows(ObjectLayoutBox objectLayoutBox) {
		JSONArray rowsJSONArray = JSONFactoryUtil.createJSONArray();

		for (ObjectLayoutRow objectLayoutRow :
				objectLayoutBox.getObjectLayoutRows()) {

			JSONArray columnsJSONArray = JSONFactoryUtil.createJSONArray();

			for (ObjectLayoutColumn objectLayoutColumn :
					objectLayoutRow.getObjectLayoutColumns()) {

				columnsJSONArray.put(
					JSONUtil.put(
						"fields",
						JSONUtil.put(
							_objectFieldNames.get(
								objectLayoutColumn.getObjectFieldId()))
					).put(
						"size", objectLayoutColumn.getSize()
					));
			}

			rowsJSONArray.put(JSONUtil.put("columns", columnsJSONArray));
		}

		return rowsJSONArray.toString();
	}

	private Object _getValue(
		DDMFormField ddmFormField, Map<String, Object> values) {

		try {
			ObjectField objectField = _objectFieldLocalService.getObjectField(
				GetterUtil.getLong(ddmFormField.getProperty("objectFieldId")));

			ObjectFieldBusinessType objectFieldBusinessType =
				_objectFieldBusinessTypeRegistry.getObjectFieldBusinessType(
					objectField.getBusinessType());

			return objectFieldBusinessType.getValue(objectField, values);
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return null;
		}
	}

	private boolean _isActive(ObjectField objectField) throws PortalException {
		if (Validator.isNull(objectField.getRelationshipType())) {
			return true;
		}

		if (Validator.isNotNull(getObjectRelationshipERCObjectFieldName()) &&
			Objects.equals(
				getObjectRelationshipERCObjectFieldName(),
				ObjectFieldSettingUtil.getValue(
					ObjectFieldSettingConstants.
						NAME_OBJECT_RELATIONSHIP_ERC_OBJECT_FIELD_NAME,
					objectField))) {

			return false;
		}

		ObjectRelationship objectRelationship =
			_objectRelationshipLocalService.
				fetchObjectRelationshipByObjectFieldId2(
					objectField.getObjectFieldId());

		ObjectDefinition relatedObjectDefinition =
			_objectDefinitionLocalService.getObjectDefinition(
				objectRelationship.getObjectDefinitionId1());

		return relatedObjectDefinition.isActive();
	}

	private void _removeTimeFromDateString(
		DDMFormField ddmFormField, Map<String, Object> values) {

		Object value = values.get(ddmFormField.getName());

		if (value == null) {
			return;
		}

		String valueString = String.valueOf(value);

		values.put(
			ddmFormField.getName(),
			valueString.replaceAll(
				" [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}.[0-9]", ""));
	}

	private void _setDateDDMFormFieldValue(
		List<DDMFormField> ddmFormFields, Map<String, Object> values) {

		for (DDMFormField ddmFormField : ddmFormFields) {
			if (StringUtil.equals(ddmFormField.getType(), "date")) {
				_removeTimeFromDateString(ddmFormField, values);
			}
			else if (StringUtil.equals(ddmFormField.getType(), "fieldset")) {
				_setDateDDMFormFieldValue(
					ddmFormField.getNestedDDMFormFields(), values);
			}
		}
	}

	private void _setDDMFormFieldValueValue(
		DDMFormField ddmFormField, DDMFormFieldValue ddmFormFieldValue,
		Map<String, Object> values) {

		Object value = _getValue(ddmFormField, values);

		if (value == null) {
			ddmFormFieldValue.setValue(
				new UnlocalizedValue(GetterUtil.DEFAULT_STRING));
		}
		else if (value instanceof ArrayList) {
			ddmFormFieldValue.setValue(
				new UnlocalizedValue(
					StringBundler.concat(
						StringPool.OPEN_BRACKET,
						StringUtil.merge(
							ListUtil.toList(
								(List<ListEntry>)value, ListEntry::getKey),
							StringPool.COMMA_AND_SPACE),
						StringPool.CLOSE_BRACKET)));
		}
		else if (value instanceof FileEntry) {
			FileEntry fileEntry = (FileEntry)value;

			ddmFormFieldValue.setValue(
				new UnlocalizedValue(String.valueOf(fileEntry.getId())));
		}
		else if (value instanceof ListEntry) {
			ListEntry listEntry = (ListEntry)value;

			ddmFormFieldValue.setValue(
				new UnlocalizedValue(listEntry.getKey()));
		}
		else {
			if (value instanceof Double) {
				DecimalFormat decimalFormat =
					NumericDDMFormFieldUtil.getDecimalFormat(
						_objectRequestHelper.getLocale());

				value = decimalFormat.format(value);
			}

			ddmFormFieldValue.setValue(
				new UnlocalizedValue(String.valueOf(value)));
		}
	}

	private com.liferay.object.model.ObjectEntry _toServiceBuilderObjectEntry(
		ObjectEntry objectEntry) {

		if (objectEntry == null) {
			return null;
		}

		ObjectDefinition objectDefinition = getObjectDefinition1();

		com.liferay.object.model.ObjectEntry serviceBuilderObjectEntry =
			_objectEntryLocalService.createObjectEntry(0L);

		serviceBuilderObjectEntry.setExternalReferenceCode(
			objectEntry.getExternalReferenceCode());
		serviceBuilderObjectEntry.setObjectEntryId(
			GetterUtil.getLong(objectEntry.getId()));
		serviceBuilderObjectEntry.setObjectDefinitionId(
			objectDefinition.getObjectDefinitionId());

		return serviceBuilderObjectEntry;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ObjectEntryDisplayContextImpl.class);

	private final DDMFormRenderer _ddmFormRenderer;
	private final ItemSelector _itemSelector;
	private final ObjectDefinitionLocalService _objectDefinitionLocalService;
	private ObjectEntry _objectEntry;
	private final ObjectEntryLocalService _objectEntryLocalService;
	private final ObjectEntryManagerRegistry _objectEntryManagerRegistry;
	private final ObjectEntryService _objectEntryService;
	private final ObjectFieldBusinessTypeRegistry
		_objectFieldBusinessTypeRegistry;
	private final ObjectFieldLocalService _objectFieldLocalService;
	private final Map<Long, String> _objectFieldNames = new HashMap<>();
	private final ObjectLayoutLocalService _objectLayoutLocalService;
	private final ObjectRelationshipLocalService
		_objectRelationshipLocalService;
	private final ObjectRequestHelper _objectRequestHelper;
	private final ObjectScopeProviderRegistry _objectScopeProviderRegistry;
	private final boolean _readOnly;
	private final ThemeDisplay _themeDisplay;

}