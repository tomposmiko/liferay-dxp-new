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

package com.liferay.portal.template.soy.internal;

import com.google.common.io.CharStreams;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SanitizedContent;
import com.google.template.soy.msgs.SoyMsgBundle;
import com.google.template.soy.tofu.SoyTofu;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.AggregateResourceBundleLoader;
import com.liferay.portal.kernel.util.ClassResourceBundleLoader;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.template.BaseTemplate;
import com.liferay.portal.template.soy.SoyTemplateResource;

import java.io.Reader;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * @author Bruno Basto
 */
public class SoyTemplate extends BaseTemplate {

	public SoyTemplate(
		SoyTemplateResource soyTemplateResource, Map<String, Object> context,
		SoyTemplateContextHelper templateContextHelper,
		SoyTofuCacheHandler soyTofuCacheHandler) {

		super(soyTemplateResource, null, templateContextHelper);

		if (ListUtil.isEmpty(soyTemplateResource.getTemplateResources())) {
			throw new IllegalArgumentException("Template resource is null");
		}

		_templateContextHelper = templateContextHelper;

		_soyContextImpl = new SoyContextImpl(
			context, templateContextHelper.getRestrictedVariables());
		_soyTofuCacheHandler = soyTofuCacheHandler;

		_setBaseContext();
	}

	@Override
	public Object compute(
		String key,
		BiFunction<? super String, ? super Object, ?> remappingBiFunction) {

		return _soyContextImpl.compute(key, remappingBiFunction);
	}

	@Override
	public Object computeIfAbsent(
		String key, Function<? super String, ?> mappingFunction) {

		return _soyContextImpl.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public Object computeIfPresent(
		String key,
		BiFunction<? super String, ? super Object, ?> remappingBiFunction) {

		return _soyContextImpl.computeIfPresent(key, remappingBiFunction);
	}

	@Override
	public void forEach(BiConsumer<? super String, ? super Object> action) {
		_soyContextImpl.forEach(action);
	}

	@Override
	public Object getOrDefault(Object key, Object defaultValue) {
		return _soyContextImpl.getOrDefault(key, defaultValue);
	}

	@Override
	public Object merge(
		String key, Object value,
		BiFunction<? super Object, ? super Object, ?> remappingBiFunction) {

		return _soyContextImpl.merge(key, value, remappingBiFunction);
	}

	@Override
	public void prepare(HttpServletRequest httpServletRequest) {
		Map<String, Object> injectedDataObjects = new HashMap<>();

		_templateContextHelper.prepare(injectedDataObjects, httpServletRequest);

		for (Map.Entry<String, Object> entry : injectedDataObjects.entrySet()) {
			_soyContextImpl.putInjectedData(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Object putIfAbsent(String key, Object value) {
		return _soyContextImpl.putIfAbsent(key, value);
	}

	@Override
	public boolean remove(Object key, Object value) {
		return _soyContextImpl.remove(key, value);
	}

	@Override
	public Object replace(String key, Object value) {
		return _soyContextImpl.replace(key, value);
	}

	@Override
	public boolean replace(String key, Object oldValue, Object newValue) {
		return _soyContextImpl.replace(key, oldValue, newValue);
	}

	@Override
	public void replaceAll(
		BiFunction<? super String, ? super Object, ?> function) {

		_soyContextImpl.replaceAll(function);
	}

	protected SoyMsgBundleBridge createSoyMsgBundleBridge(
		List<TemplateResource> templateResources, SoyFileSet soyFileSet,
		Locale locale) {

		SoyMsgBundle soyMsgBundle = soyFileSet.extractMsgs();

		ResourceBundle languageResourceBundle = _getLanguageResourceBundle(
			templateResources, locale);

		return new SoyMsgBundleBridge(
			soyMsgBundle, locale, languageResourceBundle);
	}

	protected SoyFileSet getSoyFileSet(List<TemplateResource> templateResources)
		throws Exception {

		SoyFileSet.Builder builder = SoyFileSet.builder();

		Set<String> templateIds = new HashSet<>();

		for (TemplateResource templateResource : templateResources) {
			if (templateIds.contains(templateResource.getTemplateId())) {
				continue;
			}

			templateIds.add(templateResource.getTemplateId());

			String templateContent = getTemplateContent(templateResource);

			builder.add(templateContent, templateResource.getTemplateId());
		}

		return builder.build();
	}

	protected Optional<SoyMsgBundle> getSoyMsgBundle(
		List<TemplateResource> templateResources, SoyFileSet soyFileSet,
		SoyTofuCacheBag soyTofuCacheBag) {

		Locale locale = (Locale)get("locale");

		if (locale != null) {
			SoyMsgBundle soyMsgBundle = soyTofuCacheBag.getMessageBundle(
				locale);

			if (soyMsgBundle == null) {
				soyMsgBundle = createSoyMsgBundleBridge(
					templateResources, soyFileSet, locale);

				soyTofuCacheBag.putMessageBundle(locale, soyMsgBundle);
			}

			return Optional.of(soyMsgBundle);
		}

		return Optional.empty();
	}

	protected SoyTofuCacheBag getSoyTofuCacheBag(
			List<TemplateResource> templateResources)
		throws Exception {

		SoyTofuCacheBag soyTofuCacheBag = _soyTofuCacheHandler.get(
			templateResources);

		if (soyTofuCacheBag == null) {
			SoyFileSet soyFileSet = getSoyFileSet(templateResources);

			SoyTofu soyTofu = soyFileSet.compileToTofu();

			soyTofuCacheBag = _soyTofuCacheHandler.add(
				templateResources, soyFileSet, soyTofu);
		}

		return soyTofuCacheBag;
	}

	protected String getTemplateContent(TemplateResource templateResource)
		throws Exception {

		Reader reader = templateResource.getReader();

		return CharStreams.toString(reader);
	}

	@Override
	protected void handleException(
			TemplateResource templateResource,
			TemplateResource errorTemplateResource, Exception exception,
			Writer writer)
		throws TemplateException {

		put("exception", exception.getMessage());

		SoyTemplateResource soyTemplateResource =
			(SoyTemplateResource)templateResource;

		StringBundler sb = new StringBundler();

		for (TemplateResource innerTemplateResource :
				soyTemplateResource.getTemplateResources()) {

			if (innerTemplateResource instanceof StringTemplateResource) {
				StringTemplateResource stringTemplateResource =
					(StringTemplateResource)innerTemplateResource;

				sb.append(stringTemplateResource.getContent());
			}
		}

		put("script", sb.toString());

		try {
			processTemplate(errorTemplateResource, writer);
		}
		catch (Exception e) {
			throw new TemplateException(
				"Unable to process Soy template " +
					errorTemplateResource.getTemplateId(),
				e);
		}
	}

	@Override
	protected void processTemplate(
			TemplateResource templateResource, Writer writer)
		throws Exception {

		String namespace = GetterUtil.getString(
			get(TemplateConstants.NAMESPACE));

		if (Validator.isNull(namespace)) {
			throw new TemplateException("Namespace is not specified");
		}

		List<TemplateResource> templateResources;

		if (templateResource instanceof SoyTemplateResource) {
			SoyTemplateResource soyTemplateResource =
				(SoyTemplateResource)templateResource;

			templateResources = soyTemplateResource.getTemplateResources();
		}
		else {
			templateResources = Collections.singletonList(templateResource);
		}

		SoyTofuCacheBag soyTofuCacheBag = getSoyTofuCacheBag(templateResources);

		SoyTofu soyTofu = soyTofuCacheBag.getSoyTofu();

		SoyTofu.Renderer renderer = soyTofu.newRenderer(namespace);

		renderer.setData(_soyContextImpl.createSoyTemplateRecord());
		renderer.setIjData(_soyContextImpl.createInjectedSoyTemplateRecord());

		SoyFileSet soyFileSet = soyTofuCacheBag.getSoyFileSet();

		Optional<SoyMsgBundle> soyMsgBundle = getSoyMsgBundle(
			templateResources, soyFileSet, soyTofuCacheBag);

		if (soyMsgBundle.isPresent()) {
			renderer.setMsgBundle(soyMsgBundle.get());
		}

		boolean renderStrict = GetterUtil.getBoolean(
			get(TemplateConstants.RENDER_STRICT), true);

		if (renderStrict) {
			SanitizedContent sanitizedContent = renderer.renderStrict();

			writer.write(sanitizedContent.stringValue());
		}
		else {
			writer.write(renderer.render());
		}
	}

	private ResourceBundle _getLanguageResourceBundle(
		List<TemplateResource> templateResources, Locale locale) {

		Set<Bundle> templateResourceBundles = new HashSet<>();

		for (TemplateResource templateResource : templateResources) {
			try {
				Bundle templateResourceBundle =
					SoyProviderCapabilityBundleRegister.getTemplateBundle(
						templateResource.getTemplateId());

				templateResourceBundles.add(templateResourceBundle);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					String templateId = templateResource.getTemplateId();

					_log.debug(
						"Unable to get language resource bundle for template " +
							StringUtil.quote(templateId),
						e);
				}
			}
		}

		List<ResourceBundleLoader> resourceBundleLoaders = new ArrayList<>(
			templateResourceBundles.size() + 1);

		for (Bundle templateResourceBundle : templateResourceBundles) {
			BundleWiring bundleWiring = templateResourceBundle.adapt(
				BundleWiring.class);

			resourceBundleLoaders.add(
				new ClassResourceBundleLoader(
					"content.Language", bundleWiring.getClassLoader()));
		}

		resourceBundleLoaders.add(LanguageUtil.getPortalResourceBundleLoader());

		AggregateResourceBundleLoader aggregateResourceBundleLoader =
			new AggregateResourceBundleLoader(
				resourceBundleLoaders.toArray(new ResourceBundleLoader[0]));

		return aggregateResourceBundleLoader.loadResourceBundle(locale);
	}

	/**
	 * Point base context to Soy context just in case someone tries to access
	 * it. That shouldn't happen because it would break the SoyContext
	 * abstraction and that would mean that some code is incorrect, but it is
	 * better than failing.
	 *
	 * @review
	 */
	private void _setBaseContext() {
		context = _soyContextImpl;
	}

	private static final Log _log = LogFactoryUtil.getLog(SoyTemplate.class);

	private final SoyContextImpl _soyContextImpl;
	private final SoyTofuCacheHandler _soyTofuCacheHandler;
	private final SoyTemplateContextHelper _templateContextHelper;

}