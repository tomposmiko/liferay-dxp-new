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

package com.liferay.journal.internal.transformer;

import com.liferay.asset.display.page.util.AssetDisplayPageUtil;
import com.liferay.asset.kernel.service.AssetTagLocalServiceUtil;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.info.item.InfoItemReference;
import com.liferay.journal.configuration.JournalServiceConfiguration;
import com.liferay.journal.constants.JournalStructureConstants;
import com.liferay.journal.internal.util.JournalUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.util.JournalHelper;
import com.liferay.layout.display.page.LayoutDisplayPageObjectProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProviderRegistry;
import com.liferay.petra.io.unsync.UnsyncStringWriter;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.mobile.device.Device;
import com.liferay.portal.kernel.mobile.device.UnknownDevice;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.portlet.PortletRequestModel;
import com.liferay.portal.kernel.portlet.constants.FriendlyURLResolverConstants;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.PipingServletResponse;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateHandler;
import com.liferay.portal.kernel.template.TemplateHandlerRegistryUtil;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.templateparser.TemplateNode;
import com.liferay.portal.kernel.templateparser.TransformException;
import com.liferay.portal.kernel.templateparser.TransformerListener;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.webserver.WebServerServletTokenUtil;
import com.liferay.portal.kernel.xml.Attribute;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 * @author Wesley Gong
 * @author Angelo Jefferson
 * @author Hugo Huijser
 * @author Marcellus Tavares
 * @author Juan Fernández
 * @author Eduardo García
 */
public class JournalTransformer {

	public String transform(
			JournalArticle article, DDMTemplate ddmTemplate,
			JournalHelper journalHelper, String languageId,
			LayoutDisplayPageProviderRegistry layoutDisplayPageProviderRegistry,
			List<TransformerListener> transformerListeners,
			PortletRequestModel portletRequestModel, boolean propagateException,
			String script, ThemeDisplay themeDisplay, String viewMode)
		throws Exception {

		// Setup listeners

		if (_log.isDebugEnabled()) {
			_log.debug("Language " + languageId);
		}

		if (Validator.isNull(viewMode)) {
			viewMode = Constants.VIEW;
		}

		Map<String, String> tokens = JournalUtil.getTokens(
			article, ddmTemplate, portletRequestModel, themeDisplay);

		List<TemplateNode> templateNodes = new ArrayList<>();

		_addAllReservedEls(
			article, languageId, templateNodes, themeDisplay, tokens);

		if (_logTokens.isDebugEnabled()) {
			String tokensString = PropertiesUtil.list(tokens);

			_logTokens.debug(tokensString);
		}

		Document document = article.getDocumentByLocale(languageId);

		document = document.clone();

		if (_logTransformBefore.isDebugEnabled()) {
			_logTransformBefore.debug(document);
		}

		for (TransformerListener transformerListener : transformerListeners) {

			// Modify XML

			if (_logXmlBeforeListener.isDebugEnabled()) {
				_logXmlBeforeListener.debug(document);
			}

			if (transformerListener != null) {
				document = transformerListener.onXml(
					document, languageId, tokens);

				if (_logXmlAfterListener.isDebugEnabled()) {
					_logXmlAfterListener.debug(document);
				}
			}

			// Modify script

			if (_logScriptBeforeListener.isDebugEnabled()) {
				_logScriptBeforeListener.debug(script);
			}

			if (transformerListener != null) {
				script = transformerListener.onScript(
					script, document, languageId, tokens);

				if (_logScriptAfterListener.isDebugEnabled()) {
					_logScriptAfterListener.debug(script);
				}
			}
		}

		// Transform

		String templateKey = "DEFAULT_TEMPLATE";

		if (ddmTemplate != null) {
			templateKey = ddmTemplate.getTemplateKey();
		}

		long companyId = article.getCompanyId();
		long companyGroupId = 0;
		long articleGroupId = article.getGroupId();
		long classNameId = 0;

		if (tokens != null) {
			companyGroupId = GetterUtil.getLong(tokens.get("company_group_id"));
			classNameId = GetterUtil.getLong(
				tokens.get(TemplateConstants.CLASS_NAME_ID));
		}

		long scopeGroupId = 0;
		long siteGroupId = 0;

		if (themeDisplay != null) {
			companyId = themeDisplay.getCompanyId();
			companyGroupId = themeDisplay.getCompanyGroupId();
			scopeGroupId = themeDisplay.getScopeGroupId();
			siteGroupId = themeDisplay.getSiteGroupId();
		}

		Template template = _getTemplate(
			_getTemplateId(
				templateKey, companyId, companyGroupId, articleGroupId),
			script);

		PortletRequest originalPortletRequest = null;
		PortletResponse originalPortletResponse = null;

		HttpServletRequest httpServletRequest = null;

		if ((themeDisplay != null) && (themeDisplay.getRequest() != null)) {
			httpServletRequest = themeDisplay.getRequest();

			if (portletRequestModel != null) {
				originalPortletRequest =
					(PortletRequest)httpServletRequest.getAttribute(
						JavaConstants.JAVAX_PORTLET_REQUEST);
				originalPortletResponse =
					(PortletResponse)httpServletRequest.getAttribute(
						JavaConstants.JAVAX_PORTLET_RESPONSE);

				httpServletRequest.setAttribute(
					JavaConstants.JAVAX_PORTLET_REQUEST,
					portletRequestModel.getPortletRequest());
				httpServletRequest.setAttribute(
					JavaConstants.JAVAX_PORTLET_RESPONSE,
					portletRequestModel.getPortletResponse());
				httpServletRequest.setAttribute(
					PortletRequest.LIFECYCLE_PHASE,
					portletRequestModel.getLifecycle());
			}

			template.prepare(httpServletRequest);
		}

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		try {
			Locale locale = LocaleUtil.fromLanguageId(languageId);

			templateNodes.addAll(
				_getTemplateNodes(
					themeDisplay, document.getRootElement(),
					article.getDDMStructure(), locale));

			templateNodes.addAll(
				includeBackwardsCompatibilityTemplateNodes(templateNodes, -1));

			for (TemplateNode templateNode : templateNodes) {
				template.put(templateNode.getName(), templateNode);
			}

			if (portletRequestModel != null) {
				template.put("requestMap", portletRequestModel.toMap());
			}

			template.put("articleGroupId", articleGroupId);
			template.put("articleLocale", locale);
			template.put("company", _getCompany(themeDisplay, companyId));
			template.put("companyId", companyId);
			template.put("device", _getDevice(themeDisplay));

			Map<String, String> friendlyURLMap = _getFriendlyURLMap(
				article, journalHelper, layoutDisplayPageProviderRegistry,
				themeDisplay);

			template.put(
				"friendlyURL", _getFriendlyURL(friendlyURLMap, languageId));
			template.put("friendlyURLs", friendlyURLMap);

			template.put("locale", _getLocale(themeDisplay, locale));
			template.put(
				"permissionChecker",
				PermissionThreadLocal.getPermissionChecker());
			template.put(
				"randomNamespace",
				StringUtil.randomId() + StringPool.UNDERLINE);
			template.put("scopeGroupId", scopeGroupId);
			template.put("siteGroupId", siteGroupId);

			String templatesPath = _getTemplatesPath(
				companyId, articleGroupId, classNameId);

			template.put("templatesPath", templatesPath);

			template.put("viewMode", viewMode);

			TemplateHandler templateHandler =
				TemplateHandlerRegistryUtil.getTemplateHandler(
					JournalArticle.class.getName());

			template.putAll(templateHandler.getCustomContextObjects());

			if (themeDisplay != null) {
				template.prepareTaglib(
					themeDisplay.getRequest(),
					new PipingServletResponse(
						themeDisplay.getResponse(), unsyncStringWriter));
			}

			// Deprecated variables

			template.put("groupId", articleGroupId);
			template.put("journalTemplatesPath", templatesPath);

			if (propagateException) {
				template.processTemplate(unsyncStringWriter);
			}
			else {
				template.processTemplate(
					unsyncStringWriter, this::_getErrorTemplateResource);
			}
		}
		catch (Exception exception) {
			if (exception instanceof DocumentException) {
				throw new TransformException(
					"Unable to read XML document", exception);
			}
			else if (exception instanceof IOException) {
				throw new TransformException(
					"Error reading template", exception);
			}
			else if (exception instanceof TransformException) {
				throw (TransformException)exception;
			}

			throw new TransformException("Unhandled exception", exception);
		}
		finally {
			if ((httpServletRequest != null) && (portletRequestModel != null)) {
				httpServletRequest.setAttribute(
					JavaConstants.JAVAX_PORTLET_REQUEST,
					originalPortletRequest);
				httpServletRequest.setAttribute(
					JavaConstants.JAVAX_PORTLET_RESPONSE,
					originalPortletResponse);
			}
		}

		String output = unsyncStringWriter.toString();

		// Postprocess output

		for (TransformerListener transformerListener : transformerListeners) {

			// Modify output

			if (_logOutputBeforeListener.isDebugEnabled()) {
				_logOutputBeforeListener.debug(output);
			}

			output = transformerListener.onOutput(output, languageId, tokens);

			if (_logOutputAfterListener.isDebugEnabled()) {
				_logOutputAfterListener.debug(output);
			}
		}

		if (_logTransfromAfter.isDebugEnabled()) {
			_logTransfromAfter.debug(output);
		}

		return output;
	}

	protected List<TemplateNode> includeBackwardsCompatibilityTemplateNodes(
		List<TemplateNode> templateNodes, int parentOffset) {

		List<TemplateNode> backwardsCompatibilityTemplateNodes =
			new ArrayList<>();

		parentOffset++;

		for (TemplateNode templateNode : templateNodes) {
			if (!Objects.equals(
					templateNode.getType(),
					DDMFormFieldTypeConstants.FIELDSET)) {

				if (parentOffset > 0) {
					backwardsCompatibilityTemplateNodes.add(
						(TemplateNode)templateNode.clone());
				}

				continue;
			}

			List<TemplateNode> childTemplateNodes = templateNode.getChildren();

			if (ListUtil.isEmpty(childTemplateNodes)) {
				continue;
			}

			String fieldSetName = templateNode.getName();

			if (!fieldSetName.endsWith("FieldSet")) {
				continue;
			}

			String name = fieldSetName.substring(
				0, fieldSetName.indexOf("FieldSet"));

			TemplateNode mainChildTemplateNode = templateNode.getChild(name);

			if (mainChildTemplateNode == null) {
				backwardsCompatibilityTemplateNodes.addAll(
					includeBackwardsCompatibilityTemplateNodes(
						childTemplateNodes, parentOffset));

				continue;
			}

			if (Objects.equals(
					mainChildTemplateNode.getType(),
					DDMFormFieldTypeConstants.FIELDSET)) {

				backwardsCompatibilityTemplateNodes.addAll(
					includeBackwardsCompatibilityTemplateNodes(
						Arrays.asList(mainChildTemplateNode), parentOffset));

				continue;
			}

			List<TemplateNode> newChildTemplateNodes = new ArrayList<>(
				childTemplateNodes);

			newChildTemplateNodes.remove(mainChildTemplateNode);

			List<TemplateNode> newSiblingsTemplateNodes = new ArrayList<>(
				mainChildTemplateNode.getSiblings());

			if (!newSiblingsTemplateNodes.isEmpty()) {
				newSiblingsTemplateNodes.remove(mainChildTemplateNode);
			}

			mainChildTemplateNode = (TemplateNode)mainChildTemplateNode.clone();

			mainChildTemplateNode.appendChildren(
				includeBackwardsCompatibilityTemplateNodes(
					newChildTemplateNodes, parentOffset));

			List<TemplateNode> siblingsTemplateNodes =
				templateNode.getSiblings();

			if (!siblingsTemplateNodes.isEmpty()) {
				newSiblingsTemplateNodes.addAll(
					ListUtil.subList(
						siblingsTemplateNodes, 1,
						siblingsTemplateNodes.size()));
			}

			List<TemplateNode> mainChildSiblingsTemplateNodes =
				mainChildTemplateNode.getSiblings();

			mainChildSiblingsTemplateNodes.clear();

			mainChildSiblingsTemplateNodes.add(mainChildTemplateNode);

			mainChildSiblingsTemplateNodes.addAll(
				includeBackwardsCompatibilityTemplateNodes(
					newSiblingsTemplateNodes, parentOffset));

			backwardsCompatibilityTemplateNodes.add(mainChildTemplateNode);
		}

		return backwardsCompatibilityTemplateNodes;
	}

	private void _addAllReservedEls(
		JournalArticle article, String languageId,
		List<TemplateNode> templateNodes, ThemeDisplay themeDisplay,
		Map<String, String> tokens) {

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_ID, templateNodes,
			themeDisplay, tokens, article.getArticleId());

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_VERSION, templateNodes,
			themeDisplay, tokens, String.valueOf(article.getVersion()));

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_TITLE, templateNodes,
			themeDisplay, tokens, article.getTitle(languageId));

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_URL_TITLE, templateNodes,
			themeDisplay, tokens, article.getUrlTitle());

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_DESCRIPTION,
			templateNodes, themeDisplay, tokens,
			article.getDescription(languageId));

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_CREATE_DATE,
			templateNodes, themeDisplay, tokens,
			Time.getRFC822(article.getCreateDate()));

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_MODIFIED_DATE,
			templateNodes, themeDisplay, tokens,
			Time.getRFC822(article.getModifiedDate()));

		if (article.getDisplayDate() != null) {
			_addReservedEl(
				JournalStructureConstants.RESERVED_ARTICLE_DISPLAY_DATE,
				templateNodes, themeDisplay, tokens,
				Time.getRFC822(article.getDisplayDate()));
		}

		String smallImageURL = StringPool.BLANK;

		if (Validator.isNotNull(article.getSmallImageURL())) {
			smallImageURL = article.getSmallImageURL();
		}
		else if ((themeDisplay != null) && article.isSmallImage()) {
			smallImageURL = StringBundler.concat(
				themeDisplay.getPathImage(), "/journal/article?img_id=",
				article.getSmallImageId(), "&t=",
				WebServerServletTokenUtil.getToken(article.getSmallImageId()));
		}

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_SMALL_IMAGE_URL,
			templateNodes, themeDisplay, tokens, smallImageURL);

		String[] assetTagNames = AssetTagLocalServiceUtil.getTagNames(
			JournalArticle.class.getName(), article.getResourcePrimKey());

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_ASSET_TAG_NAMES,
			templateNodes, themeDisplay, tokens,
			StringUtil.merge(assetTagNames));

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_AUTHOR_ID, templateNodes,
			themeDisplay, tokens, String.valueOf(article.getUserId()));

		String userName = StringPool.BLANK;
		String userEmailAddress = StringPool.BLANK;
		String userComments = StringPool.BLANK;
		String userJobTitle = StringPool.BLANK;

		User user = UserLocalServiceUtil.fetchUserById(article.getUserId());

		if (user != null) {
			userName = user.getFullName();
			userEmailAddress = user.getEmailAddress();
			userComments = user.getComments();
			userJobTitle = user.getJobTitle();
		}

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_AUTHOR_NAME,
			templateNodes, themeDisplay, tokens, userName);

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_AUTHOR_EMAIL_ADDRESS,
			templateNodes, themeDisplay, tokens, userEmailAddress);

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_AUTHOR_COMMENTS,
			templateNodes, themeDisplay, tokens, userComments);

		_addReservedEl(
			JournalStructureConstants.RESERVED_ARTICLE_AUTHOR_JOB_TITLE,
			templateNodes, themeDisplay, tokens, userJobTitle);
	}

	private void _addReservedEl(
		String name, List<TemplateNode> templateNodes,
		ThemeDisplay themeDisplay, Map<String, String> tokens, String value) {

		// Template nodes

		templateNodes.add(
			new TemplateNode(
				themeDisplay, name, value, StringPool.BLANK, new HashMap<>()));

		// Tokens

		tokens.put(
			StringUtil.replace(name, CharPool.DASH, CharPool.UNDERLINE), value);
	}

	private String _convertToReferenceIfNeeded(
		String data, DDMFormField ddmFormField) {

		if (Validator.isNull(data)) {
			return data;
		}

		DDMFormFieldOptions ddmFormFieldOptions =
			ddmFormField.getDDMFormFieldOptions();

		Map<String, String> optionsReferences =
			ddmFormFieldOptions.getOptionsReferences();

		String type = ddmFormField.getType();

		if (Objects.equals(type, DDMFormFieldTypeConstants.CHECKBOX_MULTIPLE) ||
			(Objects.equals(type, DDMFormFieldTypeConstants.SELECT) &&
			 ddmFormField.isMultiple())) {

			try {
				JSONArray nextJSONArray = JSONFactoryUtil.createJSONArray();

				JSONArray jsonArray = JSONFactoryUtil.createJSONArray(data);

				for (Object element : jsonArray) {
					String optionValue = (String)element;

					nextJSONArray.put(
						optionsReferences.getOrDefault(
							optionValue, optionValue));
				}

				return nextJSONArray.toString();
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}
			}
		}
		else if (Objects.equals(type, DDMFormFieldTypeConstants.GRID)) {
			try {
				JSONObject nextJSONObject = JSONFactoryUtil.createJSONObject();

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject(data);

				DDMFormFieldOptions rowsDDMFormFieldOptions =
					(DDMFormFieldOptions)ddmFormField.getProperty("rows");

				Map<String, String> rowOptionsReferences =
					rowsDDMFormFieldOptions.getOptionsReferences();

				DDMFormFieldOptions columnsDDMFormFieldOptions =
					(DDMFormFieldOptions)ddmFormField.getProperty("columns");

				Map<String, String> columnsReferences =
					columnsDDMFormFieldOptions.getOptionsReferences();

				for (String key : jsonObject.keySet()) {
					String value = jsonObject.getString(key);

					nextJSONObject.put(
						rowOptionsReferences.getOrDefault(key, key),
						columnsReferences.getOrDefault(value, value));
				}

				return nextJSONObject.toString();
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}
			}
		}
		else if (Objects.equals(type, DDMFormFieldTypeConstants.RADIO) ||
				 Objects.equals(type, DDMFormFieldTypeConstants.SELECT)) {

			return optionsReferences.getOrDefault(data, data);
		}

		return data;
	}

	private TemplateNode _createTemplateNode(
			DDMFormField ddmFormField, Element dynamicElementElement,
			Locale locale, ThemeDisplay themeDisplay)
		throws Exception {

		String data = StringPool.BLANK;

		Element dynamicContentElement = dynamicElementElement.element(
			"dynamic-content");

		if (dynamicContentElement != null) {
			data = dynamicContentElement.getText();
		}

		String type = dynamicElementElement.attributeValue(
			"type", ddmFormField.getType());

		Map<String, String> attributes = new HashMap<>();

		if (type.equals(DDMFormFieldTypeConstants.IMAGE)) {
			JSONObject dataJSONObject = JSONFactoryUtil.createJSONObject(data);

			Iterator<String> iterator = dataJSONObject.keys();

			while (iterator.hasNext()) {
				String key = iterator.next();

				String value = dataJSONObject.getString(key);

				attributes.put(key, value);
			}
		}
		else if (type.equals(DDMFormFieldTypeConstants.SELECT) &&
				 ddmFormField.isMultiple()) {

			JSONArray dataJSONArray = JSONFactoryUtil.createJSONArray();

			Iterator<Element> iterator = dynamicContentElement.elementIterator(
				"option");

			while (iterator.hasNext()) {
				Element optionElement = iterator.next();

				dataJSONArray.put(optionElement.getData());
			}

			data = JSONUtil.toString(dataJSONArray);
		}

		if (dynamicContentElement != null) {
			for (Attribute attribute : dynamicContentElement.attributes()) {
				attributes.put(attribute.getName(), attribute.getValue());
			}
		}

		TemplateNode templateNode = new TemplateNode(
			themeDisplay, ddmFormField.getFieldReference(),
			_convertToReferenceIfNeeded(
				StringUtil.stripCDATA(data), ddmFormField),
			type, attributes);

		if ((dynamicElementElement.element("dynamic-element") == null) &&
			(dynamicContentElement != null) &&
			(dynamicContentElement.element("option") != null)) {

			List<Element> optionElements = dynamicContentElement.elements(
				"option");

			for (Element optionElement : optionElements) {
				templateNode.appendOption(
					_convertToReferenceIfNeeded(
						StringUtil.stripCDATA(optionElement.getText()),
						ddmFormField));
			}
		}

		DDMFormFieldOptions ddmFormFieldOptions =
			ddmFormField.getDDMFormFieldOptions();

		Map<String, LocalizedValue> options = ddmFormFieldOptions.getOptions();
		Map<String, String> optionsReferences =
			ddmFormFieldOptions.getOptionsReferences();

		for (Map.Entry<String, LocalizedValue> entry : options.entrySet()) {
			String optionValue = StringUtil.stripCDATA(entry.getKey());

			String optionReference = optionsReferences.getOrDefault(
				optionValue, optionValue);

			LocalizedValue localizedLabel = entry.getValue();

			String optionLabel = localizedLabel.getString(locale);

			templateNode.appendOptionMap(optionReference, optionLabel);
		}

		return templateNode;
	}

	private Company _getCompany(ThemeDisplay themeDisplay, long companyId)
		throws Exception {

		if (themeDisplay != null) {
			return themeDisplay.getCompany();
		}

		return CompanyLocalServiceUtil.getCompany(companyId);
	}

	private Device _getDevice(ThemeDisplay themeDisplay) {
		if (themeDisplay != null) {
			return themeDisplay.getDevice();
		}

		return UnknownDevice.getInstance();
	}

	private TemplateResource _getErrorTemplateResource() {
		try {
			JournalServiceConfiguration journalServiceConfiguration =
				ConfigurationProviderUtil.getCompanyConfiguration(
					JournalServiceConfiguration.class,
					CompanyThreadLocal.getCompanyId());

			return new StringTemplateResource(
				TemplateConstants.LANG_TYPE_FTL,
				journalServiceConfiguration.errorTemplateFTL());
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return null;
	}

	private String _getFriendlyURL(
		Map<String, String> friendlyURLMap, String languageId) {

		String friendlyURL = friendlyURLMap.get(languageId);

		if (Validator.isNotNull(friendlyURL)) {
			return friendlyURL;
		}

		friendlyURL = friendlyURLMap.get(
			LocaleUtil.toLanguageId(LocaleUtil.getSiteDefault()));

		if (Validator.isNotNull(friendlyURL)) {
			return friendlyURL;
		}

		return StringPool.BLANK;
	}

	private Map<String, String> _getFriendlyURLMap(
			JournalArticle article, JournalHelper journalHelper,
			LayoutDisplayPageProviderRegistry layoutDisplayPageProviderRegistry,
			ThemeDisplay themeDisplay)
		throws PortalException {

		Map<String, String> friendlyURLMap = new HashMap<>();

		LayoutDisplayPageProvider<?> layoutDisplayPageProvider =
			layoutDisplayPageProviderRegistry.
				getLayoutDisplayPageProviderByClassName(
					JournalArticle.class.getName());

		if (layoutDisplayPageProvider == null) {
			return friendlyURLMap;
		}

		LayoutDisplayPageObjectProvider<?> layoutDisplayPageObjectProvider =
			layoutDisplayPageProvider.getLayoutDisplayPageObjectProvider(
				new InfoItemReference(
					JournalArticle.class.getName(),
					article.getResourcePrimKey()));

		if ((themeDisplay == null) ||
			(layoutDisplayPageObjectProvider == null) ||
			(themeDisplay.getSiteGroup() == null) ||
			!AssetDisplayPageUtil.hasAssetDisplayPage(
				themeDisplay.getScopeGroupId(),
				layoutDisplayPageObjectProvider.getClassNameId(),
				layoutDisplayPageObjectProvider.getClassPK(),
				layoutDisplayPageObjectProvider.getClassTypeId())) {

			return friendlyURLMap;
		}

		Map<Locale, String> friendlyURLs = article.getFriendlyURLMap();

		for (Locale locale : friendlyURLs.keySet()) {
			friendlyURLMap.put(
				LocaleUtil.toLanguageId(locale),
				journalHelper.createURLPattern(
					article, locale, false,
					FriendlyURLResolverConstants.URL_SEPARATOR_JOURNAL_ARTICLE,
					themeDisplay));
		}

		return friendlyURLMap;
	}

	private Locale _getLocale(ThemeDisplay themeDisplay, Locale locale)
		throws Exception {

		if (themeDisplay != null) {
			return themeDisplay.getLocale();
		}

		return locale;
	}

	private Template _getTemplate(String templateId, String script)
		throws Exception {

		TemplateResource templateResource = new StringTemplateResource(
			templateId, script);

		return TemplateManagerUtil.getTemplate(
			TemplateConstants.LANG_TYPE_FTL, templateResource, true);
	}

	private String _getTemplateId(
		String templateId, long companyId, long companyGroupId, long groupId) {

		StringBundler sb = new StringBundler(5);

		sb.append(companyId);
		sb.append(StringPool.POUND);

		if (companyGroupId > 0) {
			sb.append(companyGroupId);
		}
		else {
			sb.append(groupId);
		}

		sb.append(StringPool.POUND);
		sb.append(templateId);

		return sb.toString();
	}

	private List<TemplateNode> _getTemplateNodes(
			ThemeDisplay themeDisplay, Element element,
			DDMStructure ddmStructure, Locale locale)
		throws Exception {

		List<TemplateNode> templateNodes = new ArrayList<>();

		Map<String, TemplateNode> prototypeTemplateNodes = new HashMap<>();

		List<Element> dynamicElementElements = element.elements(
			"dynamic-element");

		for (Element dynamicElementElement : dynamicElementElements) {
			String name = dynamicElementElement.attributeValue("name");

			if (Validator.isNull(name)) {
				throw new TransformException(
					"Element missing \"name\" attribute");
			}

			DDMFormField ddmFormField = ddmStructure.getDDMFormField(name);

			if (ddmFormField == null) {
				String data = StringPool.BLANK;

				Element dynamicContentElement = dynamicElementElement.element(
					"dynamic-content");

				if (dynamicContentElement != null) {
					data = dynamicContentElement.getText();
				}

				templateNodes.add(
					new TemplateNode(
						themeDisplay, name, StringUtil.stripCDATA(data),
						StringPool.BLANK, new HashMap<>()));

				continue;
			}

			TemplateNode templateNode = _createTemplateNode(
				ddmFormField, dynamicElementElement, locale, themeDisplay);

			if (dynamicElementElement.element("dynamic-element") != null) {
				templateNode.appendChildren(
					_getTemplateNodes(
						themeDisplay, dynamicElementElement, ddmStructure,
						locale));
			}

			TemplateNode prototypeTemplateNode = prototypeTemplateNodes.get(
				name);

			if (prototypeTemplateNode == null) {
				prototypeTemplateNode = templateNode;

				prototypeTemplateNodes.put(name, prototypeTemplateNode);

				templateNodes.add(templateNode);
			}

			prototypeTemplateNode.appendSibling(templateNode);
		}

		return templateNodes;
	}

	private String _getTemplatesPath(
		long companyId, long groupId, long classNameId) {

		return StringBundler.concat(
			TemplateConstants.TEMPLATE_SEPARATOR, StringPool.SLASH, companyId,
			StringPool.SLASH, groupId, StringPool.SLASH, classNameId);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JournalTransformer.class);

	private static final Log _logOutputAfterListener = LogFactoryUtil.getLog(
		JournalTransformer.class.getName() + ".OutputAfterListener");
	private static final Log _logOutputBeforeListener = LogFactoryUtil.getLog(
		JournalTransformer.class.getName() + ".OutputBeforeListener");
	private static final Log _logScriptAfterListener = LogFactoryUtil.getLog(
		JournalTransformer.class.getName() + ".ScriptAfterListener");
	private static final Log _logScriptBeforeListener = LogFactoryUtil.getLog(
		JournalTransformer.class.getName() + ".ScriptBeforeListener");
	private static final Log _logTokens = LogFactoryUtil.getLog(
		JournalTransformer.class.getName() + ".Tokens");
	private static final Log _logTransformBefore = LogFactoryUtil.getLog(
		JournalTransformer.class.getName() + ".TransformBefore");
	private static final Log _logTransfromAfter = LogFactoryUtil.getLog(
		JournalTransformer.class.getName() + ".TransformAfter");
	private static final Log _logXmlAfterListener = LogFactoryUtil.getLog(
		JournalTransformer.class.getName() + ".XmlAfterListener");
	private static final Log _logXmlBeforeListener = LogFactoryUtil.getLog(
		JournalTransformer.class.getName() + ".XmlBeforeListener");

}