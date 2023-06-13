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

package com.liferay.object.web.internal.info.item.provider;

import com.liferay.info.exception.NoSuchFormVariationException;
import com.liferay.info.field.InfoField;
import com.liferay.info.field.InfoFieldSet;
import com.liferay.info.field.InfoFieldSetEntry;
import com.liferay.info.field.type.FileInfoFieldType;
import com.liferay.info.field.type.MultiselectInfoFieldType;
import com.liferay.info.field.type.NumberInfoFieldType;
import com.liferay.info.field.type.RelationshipInfoFieldType;
import com.liferay.info.field.type.SelectInfoFieldType;
import com.liferay.info.field.type.TextInfoFieldType;
import com.liferay.info.field.type.URLInfoFieldType;
import com.liferay.info.form.InfoForm;
import com.liferay.info.item.field.reader.InfoItemFieldReaderFieldSetProvider;
import com.liferay.info.item.provider.InfoItemFormProvider;
import com.liferay.info.localized.InfoLocalizedValue;
import com.liferay.info.localized.bundle.FunctionInfoLocalizedValue;
import com.liferay.list.type.model.ListTypeEntry;
import com.liferay.list.type.service.ListTypeEntryLocalService;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectFieldValidationConstants;
import com.liferay.object.exception.NoSuchObjectDefinitionException;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectFieldSetting;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.rest.context.path.RESTContextPathResolver;
import com.liferay.object.rest.context.path.RESTContextPathResolverRegistry;
import com.liferay.object.scope.ObjectScopeProvider;
import com.liferay.object.scope.ObjectScopeProviderRegistry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectFieldSettingLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.object.web.internal.configuration.util.ObjectConfigurationUtil;
import com.liferay.object.web.internal.info.item.ObjectEntryInfoItemFields;
import com.liferay.object.web.internal.util.ObjectFieldDBTypeUtil;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.template.info.item.provider.TemplateInfoItemFieldSetProvider;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jorge Ferrer
 * @author Guilherme Camacho
 */
public class ObjectEntryInfoItemFormProvider
	implements InfoItemFormProvider<ObjectEntry> {

	public ObjectEntryInfoItemFormProvider(
		ObjectDefinition objectDefinition,
		InfoItemFieldReaderFieldSetProvider infoItemFieldReaderFieldSetProvider,
		ListTypeEntryLocalService listTypeEntryLocalService,
		ObjectDefinitionLocalService objectDefinitionLocalService,
		ObjectFieldLocalService objectFieldLocalService,
		ObjectFieldSettingLocalService objectFieldSettingLocalService,
		ObjectRelationshipLocalService objectRelationshipLocalService,
		ObjectScopeProviderRegistry objectScopeProviderRegistry,
		RESTContextPathResolverRegistry restContextPathResolverRegistry,
		TemplateInfoItemFieldSetProvider templateInfoItemFieldSetProvider,
		UserLocalService userLocalService) {

		_objectDefinition = objectDefinition;
		_infoItemFieldReaderFieldSetProvider =
			infoItemFieldReaderFieldSetProvider;
		_listTypeEntryLocalService = listTypeEntryLocalService;
		_objectDefinitionLocalService = objectDefinitionLocalService;
		_objectFieldLocalService = objectFieldLocalService;
		_objectFieldSettingLocalService = objectFieldSettingLocalService;
		_objectRelationshipLocalService = objectRelationshipLocalService;
		_objectScopeProviderRegistry = objectScopeProviderRegistry;
		_restContextPathResolverRegistry = restContextPathResolverRegistry;
		_templateInfoItemFieldSetProvider = templateInfoItemFieldSetProvider;
		_userLocalService = userLocalService;
	}

	@Override
	public InfoForm getInfoForm() {
		try {
			return _getInfoForm(0);
		}
		catch (NoSuchFormVariationException noSuchFormVariationException) {
			throw new RuntimeException(noSuchFormVariationException);
		}
	}

	@Override
	public InfoForm getInfoForm(ObjectEntry objectEntry) {
		long objectDefinitionId = objectEntry.getObjectDefinitionId();

		try {
			return _getInfoForm(objectDefinitionId);
		}
		catch (PortalException portalException) {
			throw new RuntimeException(
				StringBundler.concat(
					"Unable to get object definition ", objectDefinitionId,
					" for object entry ", objectEntry.getObjectEntryId()),
				portalException);
		}
	}

	@Override
	public InfoForm getInfoForm(String formVariationKey)
		throws NoSuchFormVariationException {

		long objectDefinitionId = GetterUtil.getLong(formVariationKey);

		if (objectDefinitionId == 0) {
			objectDefinitionId = _objectDefinition.getObjectDefinitionId();
		}

		return _getInfoForm(objectDefinitionId);
	}

	@Override
	public InfoForm getInfoForm(String formVariationKey, long groupId)
		throws NoSuchFormVariationException {

		return getInfoForm(formVariationKey);
	}

	private InfoField<?> _addAttributes(
		InfoField.FinalStep finalStep, ObjectField objectField) {

		if (Objects.equals(
				objectField.getBusinessType(),
				ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT)) {

			finalStep.attribute(
				FileInfoFieldType.ALLOWED_FILE_EXTENSIONS,
				_getAcceptedFileExtensions(objectField)
			).attribute(
				FileInfoFieldType.FILE_SOURCE, _getFileSourceType(objectField)
			).attribute(
				FileInfoFieldType.MAX_FILE_SIZE,
				_getMaximumFileSize(objectField)
			);
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_DECIMAL)) {

			finalStep.attribute(NumberInfoFieldType.DECIMAL, true);
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_INTEGER)) {

			finalStep.attribute(
				NumberInfoFieldType.MAX_VALUE,
				BigDecimal.valueOf(
					ObjectFieldValidationConstants.
						BUSINESS_TYPE_INTEGER_VALUE_MAX)
			).attribute(
				NumberInfoFieldType.MIN_VALUE,
				BigDecimal.valueOf(
					ObjectFieldValidationConstants.
						BUSINESS_TYPE_INTEGER_VALUE_MIN)
			);
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_LONG_INTEGER)) {

			finalStep.attribute(
				NumberInfoFieldType.MAX_VALUE,
				BigDecimal.valueOf(
					ObjectFieldValidationConstants.BUSINESS_TYPE_LONG_VALUE_MAX)
			).attribute(
				NumberInfoFieldType.MIN_VALUE,
				BigDecimal.valueOf(
					ObjectFieldValidationConstants.BUSINESS_TYPE_LONG_VALUE_MIN)
			);
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_LONG_TEXT)) {

			finalStep.attribute(
				TextInfoFieldType.MAX_LENGTH, _getMaxLength(objectField, 65000)
			).attribute(
				TextInfoFieldType.MULTILINE, true
			);
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_MULTISELECT_PICKLIST)) {

			finalStep.attribute(
				MultiselectInfoFieldType.OPTIONS,
				TransformUtil.transform(
					_listTypeEntryLocalService.getListTypeEntries(
						objectField.getListTypeDefinitionId()),
					listTypeEntry -> new MultiselectInfoFieldType.Option(
						Objects.equals(
							objectField.getDefaultValue(),
							listTypeEntry.getKey()),
						new FunctionInfoLocalizedValue<>(
							listTypeEntry::getName),
						listTypeEntry.getKey())));
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_PICKLIST)) {

			finalStep.attribute(
				SelectInfoFieldType.MULTIPLE,
				objectField.compareBusinessType(
					ObjectFieldConstants.BUSINESS_TYPE_MULTISELECT_PICKLIST)
			).attribute(
				SelectInfoFieldType.OPTIONS, _getOptions(objectField)
			);
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_PRECISION_DECIMAL)) {

			finalStep.attribute(
				NumberInfoFieldType.DECIMAL, true
			).attribute(
				NumberInfoFieldType.DECIMAL_PART_MAX_LENGTH, 16
			).attribute(
				NumberInfoFieldType.MAX_VALUE,
				new BigDecimal(
					ObjectFieldValidationConstants.
						BUSINESS_TYPE_PRECISION_DECIMAL_VALUE_MAX)
			).attribute(
				NumberInfoFieldType.MIN_VALUE,
				new BigDecimal(
					ObjectFieldValidationConstants.
						BUSINESS_TYPE_PRECISION_DECIMAL_VALUE_MIN)
			);
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_RELATIONSHIP)) {

			finalStep.attribute(
				RelationshipInfoFieldType.LABEL_FIELD_NAME,
				_getRelationshipLabelFieldName(objectField)
			).attribute(
				RelationshipInfoFieldType.URL, _getRelationshipURL(objectField)
			).attribute(
				RelationshipInfoFieldType.VALUE_FIELD_NAME, "id"
			);
		}
		else if (Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_TEXT)) {

			finalStep.attribute(
				TextInfoFieldType.MAX_LENGTH, _getMaxLength(objectField, 280));
		}

		return finalStep.build();
	}

	private String _getAcceptedFileExtensions(ObjectField objectField) {
		ObjectFieldSetting acceptedFileExtensionsObjectFieldSetting =
			_objectFieldSettingLocalService.fetchObjectFieldSetting(
				objectField.getObjectFieldId(), "acceptedFileExtensions");

		if (acceptedFileExtensionsObjectFieldSetting == null) {
			return StringPool.BLANK;
		}

		return acceptedFileExtensionsObjectFieldSetting.getValue();
	}

	private List<InfoFieldSetEntry>
		_getAttachmentObjectDefinitionInfoFieldSetEntries(
			long objectDefinitionId) {

		if (!FeatureFlagManagerUtil.isEnabled("LPS-176083")) {
			return Collections.emptyList();
		}

		List<InfoFieldSetEntry> infoFieldSetEntries = new ArrayList<>();

		for (ObjectField objectField :
				_objectFieldLocalService.getObjectFields(
					objectDefinitionId, false)) {

			if (!Objects.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT)) {

				continue;
			}

			infoFieldSetEntries.add(
				InfoFieldSet.builder(
				).infoFieldSetEntry(
					InfoField.builder(
					).infoFieldType(
						URLInfoFieldType.INSTANCE
					).namespace(
						ObjectField.class.getSimpleName()
					).name(
						objectField.getObjectFieldId() + "#downloadURL"
					).labelInfoLocalizedValue(
						InfoLocalizedValue.localize(
							ObjectEntryInfoItemFields.class, "download-url")
					).build()
				).infoFieldSetEntry(
					InfoField.builder(
					).infoFieldType(
						TextInfoFieldType.INSTANCE
					).namespace(
						ObjectField.class.getSimpleName()
					).name(
						objectField.getObjectFieldId() + "#fileName"
					).labelInfoLocalizedValue(
						InfoLocalizedValue.localize(
							ObjectEntryInfoItemFields.class, "file-name")
					).build()
				).infoFieldSetEntry(
					InfoField.builder(
					).infoFieldType(
						TextInfoFieldType.INSTANCE
					).namespace(
						ObjectField.class.getSimpleName()
					).name(
						objectField.getObjectFieldId() + "#mimeType"
					).labelInfoLocalizedValue(
						InfoLocalizedValue.localize(
							ObjectEntryInfoItemFields.class, "mime-type")
					).build()
				).infoFieldSetEntry(
					InfoField.builder(
					).infoFieldType(
						TextInfoFieldType.INSTANCE
					).namespace(
						ObjectField.class.getSimpleName()
					).name(
						objectField.getObjectFieldId() + "#size"
					).labelInfoLocalizedValue(
						InfoLocalizedValue.localize(
							ObjectEntryInfoItemFields.class, "size")
					).build()
				).labelInfoLocalizedValue(
					InfoLocalizedValue.<String>builder(
					).values(
						objectField.getLabelMap()
					).build()
				).name(
					objectField.getName()
				).build());
		}

		return infoFieldSetEntries;
	}

	private InfoFieldSet _getBasicInformationInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			ObjectEntryInfoItemFields.authorInfoField
		).infoFieldSetEntry(
			ObjectEntryInfoItemFields.createDateInfoField
		).infoFieldSetEntry(
			ObjectEntryInfoItemFields.externalReferenceCodeInfoField
		).infoFieldSetEntry(
			ObjectEntryInfoItemFields.modifiedDateInfoField
		).infoFieldSetEntry(
			ObjectEntryInfoItemFields.objectEntryIdInfoField
		).infoFieldSetEntry(
			ObjectEntryInfoItemFields.publishDateInfoField
		).infoFieldSetEntry(
			ObjectEntryInfoItemFields.statusInfoField
		).infoFieldSetEntry(
			ObjectEntryInfoItemFields.userProfileImageInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(getClass(), "basic-information")
		).name(
			"basic-information"
		).build();
	}

	private InfoFieldSet _getDisplayPageInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			ObjectEntryInfoItemFields.displayPageURLInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(getClass(), "configuration")
		).name(
			"configuration"
		).build();
	}

	private FileInfoFieldType.FileSourceType _getFileSourceType(
		ObjectField objectField) {

		ObjectFieldSetting objectFieldSetting =
			_objectFieldSettingLocalService.fetchObjectFieldSetting(
				objectField.getObjectFieldId(), "fileSource");

		if (objectFieldSetting == null) {
			return null;
		}

		if (Objects.equals(
				objectFieldSetting.getValue(), "documentsAndMedia")) {

			return FileInfoFieldType.FileSourceType.DOCUMENTS_AND_MEDIA;
		}
		else if (Objects.equals(
					objectFieldSetting.getValue(), "userComputer")) {

			return FileInfoFieldType.FileSourceType.USER_COMPUTER;
		}

		return null;
	}

	private long _getGroupId(
		HttpServletRequest httpServletRequest,
		ObjectDefinition objectDefinition) {

		try {
			ObjectScopeProvider objectScopeProvider =
				_objectScopeProviderRegistry.getObjectScopeProvider(
					objectDefinition.getScope());

			return objectScopeProvider.getGroupId(httpServletRequest);
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return 0L;
		}
	}

	private InfoForm _getInfoForm(long objectDefinitionId)
		throws NoSuchFormVariationException {

		String modelClassName =
			ObjectDefinition.class.getName() + "#" + objectDefinitionId;

		return InfoForm.builder(
		).infoFieldSetEntry(
			_getBasicInformationInfoFieldSet()
		).<NoSuchFormVariationException>infoFieldSetEntry(
			unsafeConsumer -> {
				if (objectDefinitionId != 0) {
					unsafeConsumer.accept(
						_getObjectDefinitionInfoFieldSet(objectDefinitionId));
				}
			}
		).infoFieldSetEntries(
			_getAttachmentObjectDefinitionInfoFieldSetEntries(
				objectDefinitionId)
		).infoFieldSetEntry(
			_templateInfoItemFieldSetProvider.getInfoFieldSet(modelClassName)
		).infoFieldSetEntry(
			_getDisplayPageInfoFieldSet()
		).infoFieldSetEntry(
			_infoItemFieldReaderFieldSetProvider.getInfoFieldSet(modelClassName)
		).labelInfoLocalizedValue(
			InfoLocalizedValue.<String>builder(
			).values(
				_objectDefinition.getLabelMap()
			).build()
		).name(
			modelClassName
		).build();
	}

	private long _getMaximumFileSize(ObjectField objectField) {
		ObjectFieldSetting objectFieldSetting =
			_objectFieldSettingLocalService.fetchObjectFieldSetting(
				objectField.getObjectFieldId(), "maximumFileSize");

		long maximumFileSizeForGuestUsers =
			ObjectConfigurationUtil.maximumFileSizeForGuestUsers();

		if (objectFieldSetting == null) {
			return maximumFileSizeForGuestUsers;
		}

		long maximumFileSize = GetterUtil.getLong(
			objectFieldSetting.getValue());

		if ((maximumFileSizeForGuestUsers < maximumFileSize) &&
			_isDefaultUser()) {

			maximumFileSize = maximumFileSizeForGuestUsers;
		}

		return maximumFileSize;
	}

	private long _getMaxLength(ObjectField objectField, long defaultMaxLength) {
		ObjectFieldSetting objectFieldSetting =
			_objectFieldSettingLocalService.fetchObjectFieldSetting(
				objectField.getObjectFieldId(), "maxLength");

		if (objectFieldSetting == null) {
			return defaultMaxLength;
		}

		return GetterUtil.getLong(
			objectFieldSetting.getValue(), defaultMaxLength);
	}

	private InfoFieldSet _getObjectDefinitionInfoFieldSet(
			long objectDefinitionId)
		throws NoSuchFormVariationException {

		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.fetchObjectDefinition(
				objectDefinitionId);

		if (objectDefinition == null) {
			throw new NoSuchFormVariationException(
				String.valueOf(objectDefinitionId),
				new NoSuchObjectDefinitionException());
		}

		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			unsafeConsumer -> {
				for (ObjectField objectField :
						_objectFieldLocalService.getObjectFields(
							objectDefinitionId, false)) {

					if (Validator.isNotNull(
							objectField.getRelationshipType())) {

						ObjectRelationship objectRelationship =
							_objectRelationshipLocalService.
								fetchObjectRelationshipByObjectFieldId2(
									objectField.getObjectFieldId());

						ObjectDefinition relatedObjectDefinition =
							_objectDefinitionLocalService.fetchObjectDefinition(
								objectRelationship.getObjectDefinitionId1());

						if ((relatedObjectDefinition == null) ||
							!relatedObjectDefinition.isActive()) {

							continue;
						}
					}

					unsafeConsumer.accept(
						_addAttributes(
							InfoField.builder(
							).infoFieldType(
								ObjectFieldDBTypeUtil.getInfoFieldType(
									objectField)
							).namespace(
								ObjectField.class.getSimpleName()
							).name(
								objectField.getName()
							).editable(
								true
							).labelInfoLocalizedValue(
								InfoLocalizedValue.<String>builder(
								).values(
									objectField.getLabelMap()
								).build()
							).required(
								objectField.isRequired()
							),
							objectField));
				}
			}
		).labelInfoLocalizedValue(
			InfoLocalizedValue.<String>builder(
			).values(
				objectDefinition.getLabelMap()
			).build()
		).name(
			objectDefinition.getName()
		).build();
	}

	private List<SelectInfoFieldType.Option> _getOptions(
		ObjectField objectField) {

		List<SelectInfoFieldType.Option> options = new ArrayList<>();

		List<ListTypeEntry> listTypeEntries =
			_listTypeEntryLocalService.getListTypeEntries(
				objectField.getListTypeDefinitionId());

		for (ListTypeEntry listTypeEntry : listTypeEntries) {
			options.add(
				new SelectInfoFieldType.Option(
					Objects.equals(
						objectField.getDefaultValue(), listTypeEntry.getKey()),
					new FunctionInfoLocalizedValue<>(listTypeEntry::getName),
					listTypeEntry.getKey()));
		}

		return options;
	}

	private String _getRelationshipLabelFieldName(ObjectField objectField) {
		ObjectRelationship objectRelationship =
			_objectRelationshipLocalService.
				fetchObjectRelationshipByObjectFieldId2(
					objectField.getObjectFieldId());

		ObjectDefinition relatedObjectDefinition =
			_objectDefinitionLocalService.fetchObjectDefinition(
				objectRelationship.getObjectDefinitionId1());

		if (relatedObjectDefinition == null) {
			return "id";
		}

		ObjectField titleObjectField =
			_objectFieldLocalService.fetchObjectField(
				relatedObjectDefinition.getTitleObjectFieldId());

		if (titleObjectField == null) {
			return "id";
		}
		else if (Objects.equals(titleObjectField.getName(), "createDate")) {
			return "dateCreated";
		}
		else if (Objects.equals(titleObjectField.getName(), "modifiedDate")) {
			return "dateModified";
		}

		return titleObjectField.getName();
	}

	private String _getRelationshipURL(ObjectField objectField) {
		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		if (serviceContext == null) {
			return StringPool.BLANK;
		}

		ObjectRelationship objectRelationship =
			_objectRelationshipLocalService.
				fetchObjectRelationshipByObjectFieldId2(
					objectField.getObjectFieldId());

		ObjectDefinition relatedObjectDefinition =
			_objectDefinitionLocalService.fetchObjectDefinition(
				objectRelationship.getObjectDefinitionId1());

		if (relatedObjectDefinition == null) {
			return StringPool.BLANK;
		}

		RESTContextPathResolver restContextPathResolver =
			_restContextPathResolverRegistry.getRESTContextPathResolver(
				relatedObjectDefinition.getClassName());

		String restContextPath = restContextPathResolver.getRESTContextPath(
			_getGroupId(serviceContext.getRequest(), relatedObjectDefinition));

		return PortalUtil.getPortalURL(serviceContext.getRequest()) +
			restContextPath;
	}

	private boolean _isDefaultUser() {
		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		if (serviceContext == null) {
			return true;
		}

		User user = _userLocalService.fetchUser(serviceContext.getUserId());

		if ((user == null) || user.isDefaultUser()) {
			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ObjectEntryInfoItemFormProvider.class);

	private final InfoItemFieldReaderFieldSetProvider
		_infoItemFieldReaderFieldSetProvider;
	private final ListTypeEntryLocalService _listTypeEntryLocalService;
	private final ObjectDefinition _objectDefinition;
	private final ObjectDefinitionLocalService _objectDefinitionLocalService;
	private final ObjectFieldLocalService _objectFieldLocalService;
	private final ObjectFieldSettingLocalService
		_objectFieldSettingLocalService;
	private final ObjectRelationshipLocalService
		_objectRelationshipLocalService;
	private final ObjectScopeProviderRegistry _objectScopeProviderRegistry;
	private final RESTContextPathResolverRegistry
		_restContextPathResolverRegistry;
	private final TemplateInfoItemFieldSetProvider
		_templateInfoItemFieldSetProvider;
	private final UserLocalService _userLocalService;

}