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

package com.liferay.headless.form.internal.resource.v1_0;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordService;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.headless.form.dto.v1_0.FormRecord;
import com.liferay.headless.form.dto.v1_0.FormRecordForm;
import com.liferay.headless.form.internal.dto.v1_0.util.FormRecordUtil;
import com.liferay.headless.form.resource.v1_0.FormRecordFormResource;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.core.Context;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/form-record-form.properties",
	scope = ServiceScope.PROTOTYPE, service = FormRecordFormResource.class
)
public class FormRecordFormResourceImpl extends BaseFormRecordFormResourceImpl {

	@Override
	public FormRecord postFormFormRecord(
			Long formId, FormRecordForm formRecordForm)
		throws Exception {

		DDMFormInstance ddmFormInstance =
			_ddmFormInstanceService.getFormInstance(formId);

		return FormRecordUtil.toFormRecord(
			_ddmFormInstanceRecordService.addFormInstanceRecord(
				ddmFormInstance.getGroupId(),
				ddmFormInstance.getFormInstanceId(),
				_createDDMFormValues(
					ddmFormInstance, formRecordForm.getFieldValues(),
					contextAcceptLanguage.getPreferredLocale()),
				_createServiceContext(formRecordForm.getDraft())),
			contextAcceptLanguage.getPreferredLocale(), _portal,
			_userLocalService);
	}

	@Override
	public FormRecord putFormRecord(
			Long formRecordId, FormRecordForm formRecordForm)
		throws Exception {

		DDMFormInstanceRecord ddmFormInstanceRecord =
			_ddmFormInstanceRecordService.getFormInstanceRecord(formRecordId);

		return FormRecordUtil.toFormRecord(
			_ddmFormInstanceRecordService.updateFormInstanceRecord(
				formRecordId, false,
				_createDDMFormValues(
					ddmFormInstanceRecord.getFormInstance(),
					formRecordForm.getFieldValues(),
					contextAcceptLanguage.getPreferredLocale()),
				_createServiceContext(formRecordForm.getDraft())),
			contextAcceptLanguage.getPreferredLocale(), _portal,
			_userLocalService);
	}

	private DDMFormValues _createDDMFormValues(
			DDMFormInstance ddmFormInstance, String fieldValues, Locale locale)
		throws Exception {

		DDMStructure ddmStructure = ddmFormInstance.getStructure();

		DDMForm ddmForm = ddmStructure.getDDMForm();

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(true);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		ddmFormValues.addAvailableLocale(locale);
		ddmFormValues.setDefaultLocale(locale);
		ddmFormValues.setDDMFormFieldValues(
			JSONUtil.toList(
				JSONFactoryUtil.createJSONArray(fieldValues),
				jsonObject -> _toDDMFormFieldValue(
					ddmFormFieldsMap, jsonObject)));

		return ddmFormValues;
	}

	private ServiceContext _createServiceContext(boolean draft)
		throws PortalException {

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDMFormInstanceRecord.class.getName(), _httpServletRequest);

		if (draft) {
			serviceContext.setAttribute(
				"status", WorkflowConstants.STATUS_DRAFT);
			serviceContext.setAttribute("validateDDMFormValues", Boolean.FALSE);
			serviceContext.setWorkflowAction(
				WorkflowConstants.ACTION_SAVE_DRAFT);
		}
		else {
			serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);
		}

		return serviceContext;
	}

	private DDMFormFieldValue _toDDMFormFieldValue(
		Map<String, DDMFormField> ddmFormFieldsMap, JSONObject jsonObject) {

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		String name = jsonObject.getString("name");

		ddmFormFieldValue.setName(name);

		Value value = _VALUE;

		DDMFormField ddmFormField = ddmFormFieldsMap.get(name);

		if (ddmFormField != null) {
			value = Optional.ofNullable(
				jsonObject.get("value")
			).map(
				Object::toString
			).map(
				stringValue -> {
					if (ddmFormField.isLocalizable()) {
						return new LocalizedValue() {
							{
								addString(
									contextAcceptLanguage.getPreferredLocale(),
									stringValue);
							}
						};
					}

					return _VALUE;
				}
			).orElse(
				_VALUE
			);
		}

		ddmFormFieldValue.setValue(value);

		return ddmFormFieldValue;
	}

	private static final Value _VALUE = new UnlocalizedValue((String)null);

	@Reference
	private DDMFormInstanceRecordService _ddmFormInstanceRecordService;

	@Reference
	private DDMFormInstanceService _ddmFormInstanceService;

	@Context
	private HttpServletRequest _httpServletRequest;

	@Reference
	private Portal _portal;

	@Reference
	private UserLocalService _userLocalService;

}