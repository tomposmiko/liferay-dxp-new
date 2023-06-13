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

package com.liferay.frontend.js.portlet.extender.internal.portlet.action;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.dynamic.data.mapping.render.DDMFormRendererUtil;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.InputStream;
import java.io.PrintWriter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Gustavo Mantuan
 */
public class PortletExtenderConfigurationAction
	extends DefaultConfigurationAction {

	public PortletExtenderConfigurationAction(
			DDM ddm, JSONObject preferencesJSONObject)
		throws PortalException {

		_ddmForm = ddm.getDDMForm(preferencesJSONObject.toJSONString());
		_preferencesJSONObject = preferencesJSONObject;

		_populateFieldNames();
	}

	@Override
	public void include(
			PortletConfig portletConfig, HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		_setPortletPreferencesToDDMFormValues(themeDisplay, portletDisplay);

		PrintWriter printWriter = response.getWriter();

		JSONArray fieldsJSONArray = _preferencesJSONObject.getJSONArray(
			"fields");

		printWriter.println(
			StringUtil.replace(
				_TPL_CONFIGURATION_FORM,
				new String[] {
					"[$ACTION_URL$]", "[$CONSTANTS_CMD$]",
					"[$CONSTANTS_UPDATE$]", "[$CURRENT_TIME_MILLIS$]",
					"[$DDM_FORM_HTML$]", "[$FIELDS_JSON_ARRAY$]",
					"[$PORTLET_NAMESPACE$]", "[$SAVE_LABEL$]"
				},
				new String[] {
					_getActionURL(request, portletDisplay), Constants.CMD,
					Constants.UPDATE,
					String.valueOf(System.currentTimeMillis()),
					DDMFormRendererUtil.render(
						_ddmForm,
						_getDDMFormFieldRenderingContext(
							request, response, themeDisplay, portletDisplay)),
					fieldsJSONArray.toString(), portletDisplay.getNamespace(),
					LanguageUtil.get(themeDisplay.getLocale(), "save")
				}));
	}

	@Override
	public void processAction(
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		Map<String, String[]> parameters = actionRequest.getParameterMap();

		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			String key = entry.getKey();

			String name = key.split("_INSTANCE")[0];

			if (!_fieldNames.contains(name)) {
				continue;
			}

			String value = entry.getValue()[0];

			setPreference(actionRequest, name, value);
		}

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	private static String _loadTemplate(String name) {
		try (InputStream inputStream =
				PortletExtenderConfigurationAction.class.getResourceAsStream(
					"dependencies/" + name)) {

			return StringUtil.read(inputStream);
		}
		catch (Exception e) {
			_log.error("Unable to read template " + name, e);
		}

		return StringPool.BLANK;
	}

	private String _getActionURL(
			HttpServletRequest request, PortletDisplay portletDisplay)
		throws Exception {

		PortletURL actionURL = PortletURLFactoryUtil.create(
			request, portletDisplay.getPortletName(),
			PortletRequest.ACTION_PHASE);

		actionURL.setParameter(ActionRequest.ACTION_NAME, "editConfiguration");
		actionURL.setParameter("mvcPath", "/edit_configuration.jsp");
		actionURL.setParameter("p_auth", AuthTokenUtil.getToken(request));
		actionURL.setParameter("p_p_mode", PortletMode.VIEW.toString());
		actionURL.setParameter("portletConfiguration", Boolean.TRUE.toString());
		actionURL.setParameter(
			"portletResource", portletDisplay.getPortletResource());
		actionURL.setParameter("previewWidth", StringPool.BLANK);
		actionURL.setParameter("returnToFullPageURL", "/");
		actionURL.setParameter("settingsScope", "portletInstance");
		actionURL.setWindowState(LiferayWindowState.POP_UP);

		return actionURL.toString();
	}

	private DDMFormFieldRenderingContext _getDDMFormFieldRenderingContext(
		HttpServletRequest request, HttpServletResponse response,
		ThemeDisplay themeDisplay, PortletDisplay portletDisplay) {

		DDMFormFieldRenderingContext ddmFormFieldRenderingContext =
			new DDMFormFieldRenderingContext();

		ddmFormFieldRenderingContext.setHttpServletRequest(request);
		ddmFormFieldRenderingContext.setHttpServletResponse(response);
		ddmFormFieldRenderingContext.setLocale(themeDisplay.getLocale());
		ddmFormFieldRenderingContext.setMode("edit");
		ddmFormFieldRenderingContext.setPortletNamespace(
			portletDisplay.getNamespace());
		ddmFormFieldRenderingContext.setReadOnly(false);
		ddmFormFieldRenderingContext.setShowEmptyFieldLabel(true);
		ddmFormFieldRenderingContext.setViewMode(true);

		return ddmFormFieldRenderingContext;
	}

	private void _populateFieldNames() {
		JSONArray fieldsJSONArray = _preferencesJSONObject.getJSONArray(
			"fields");

		for (int i = 0; i < fieldsJSONArray.length(); i++) {
			JSONObject fieldJSONObject = fieldsJSONArray.getJSONObject(i);

			_fieldNames.add(fieldJSONObject.getString("name"));
		}
	}

	private void _setPortletPreferencesToDDMFormValues(
			ThemeDisplay themeDisplay, PortletDisplay portletDisplay)
		throws PortalException {

		PortletPreferences portletPreferences =
			PortletPreferencesFactoryUtil.getExistingPortletSetup(
				themeDisplay.getLayout(), portletDisplay.getPortletResource());

		Map<String, String[]> portletPreferencesMap =
			portletPreferences.getMap();

		portletPreferencesMap.forEach(
			(key, values) -> {
				for (DDMFormField ddmFormField : _ddmForm.getDDMFormFields()) {
					LocalizedValue predefinedValue = new LocalizedValue();

					predefinedValue.setDefaultLocale(themeDisplay.getLocale());

					if (key.contains(ddmFormField.getName())) {
						if ("select".equals(ddmFormField.getType())) {
							JSONArray valuesJSONArray =
								JSONFactoryUtil.createJSONArray();

							for (String value : values) {
								valuesJSONArray.put(value);
							}

							predefinedValue.addString(
								themeDisplay.getLocale(),
								valuesJSONArray.toString());
						}
						else {
							predefinedValue.addString(
								themeDisplay.getLocale(), values[0]);
						}

						ddmFormField.setPredefinedValue(predefinedValue);
						ddmFormField.setProperty(
							"predefinedValue", predefinedValue);
					}
				}
			});
	}

	private static final String _TPL_CONFIGURATION_FORM;

	private static final Log _log = LogFactoryUtil.getLog(
		PortletExtenderConfigurationAction.class);

	static {
		_TPL_CONFIGURATION_FORM = _loadTemplate("configuration_form.html.tpl");
	}

	private final DDMForm _ddmForm;
	private final Set<String> _fieldNames = new HashSet<>();
	private final JSONObject _preferencesJSONObject;

}