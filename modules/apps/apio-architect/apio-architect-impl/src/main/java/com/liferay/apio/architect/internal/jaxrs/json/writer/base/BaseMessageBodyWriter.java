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

package com.liferay.apio.architect.internal.jaxrs.json.writer.base;

import static java.util.Collections.singletonList;

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;

import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.internal.message.json.MessageMapper;
import com.liferay.apio.architect.internal.request.RequestInfo;
import com.liferay.apio.architect.internal.response.control.Embedded;
import com.liferay.apio.architect.internal.response.control.Fields;
import com.liferay.apio.architect.internal.unsafe.Unsafe;
import com.liferay.apio.architect.internal.url.ApplicationURL;
import com.liferay.apio.architect.internal.url.ServerURL;
import com.liferay.apio.architect.internal.wiring.osgi.manager.provider.ProviderManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.representable.NameManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.router.ItemRouterManager;
import com.liferay.apio.architect.language.AcceptLanguage;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.single.model.SingleModel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import java.nio.charset.StandardCharsets;

import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.ext.MessageBodyWriter;

import org.osgi.service.component.annotations.Reference;

/**
 * Defines the base {@code MessageBodyWriter} for those who write using a {@link
 * MessageMapper}.
 *
 * @author Alejandro Hernández
 */
public abstract class BaseMessageBodyWriter<T, S extends MessageMapper>
	implements MessageBodyWriter<T> {

	/**
	 * Whether the current {@code MessageBodyWriter} can write the actual class.
	 *
	 * @param  clazz the class of the element being written
	 * @param  genericType the generic type of the element being written
	 * @return {@code true} if the type is supported; {@code false} otherwise
	 */
	public abstract boolean canWrite(Class<?> clazz, Type genericType);

	/**
	 * Returns the message mapper used to write the actual element, if present;
	 * returns {@code Optional#empty()} otherwise.
	 *
	 * @param  request the current request
	 * @return the message mapper, if present; {@code Optional#empty()}
	 *         otherwise
	 */
	public abstract Optional<S> getMessageMapperOptional(Request request);

	@Override
	public long getSize(
		T documentation, Class<?> aClass, Type type, Annotation[] annotations,
		MediaType mediaType) {

		return -1;
	}

	@Override
	public boolean isWriteable(
		Class<?> aClass, Type genericType, Annotation[] annotations,
		MediaType mediaType) {

		return canWrite(aClass, genericType);
	}

	@Override
	public void writeTo(
			T t, Class<?> aClass, Type type, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream outputStream)
		throws IOException, WebApplicationException {

		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
			outputStream, StandardCharsets.UTF_8);

		PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);

		Optional<S> optional = getMessageMapperOptional(_request);

		S s = optional.orElseThrow(NotSupportedException::new);

		RequestInfo requestInfo = RequestInfo.create(
			builder -> builder.httpServletRequest(
				_httpServletRequest
			).serverURL(
				providerManager.provideMandatory(
					_httpServletRequest, ServerURL.class)
			).applicationURL(
				providerManager.provideMandatory(
					_httpServletRequest, ApplicationURL.class)
			).embedded(
				providerManager.provideOptional(
					_httpServletRequest, Embedded.class
				).orElse(
					__ -> false
				)
			).fields(
				providerManager.provideOptional(
					_httpServletRequest, Fields.class
				).orElse(
					__ -> string -> true
				)
			).language(
				providerManager.provideOptional(
					_httpServletRequest, AcceptLanguage.class
				).orElse(
					Locale::getDefault
				)
			).build());

		String result = write(t, s, requestInfo);

		httpHeaders.put(CONTENT_TYPE, singletonList(s.getMediaType()));

		printWriter.println(result);

		printWriter.close();
	}

	/**
	 * Returns a {@link SingleModel} identified by the supplied identifier, if
	 * present; returns {@code Optional#empty()} otherwise.
	 *
	 * @param  identifier the single model identifier
	 * @param  identifierClass the resource identifier class
	 * @return the {@code SingleModel}, if present; {@code Optional#empty()}
	 *         otherwise
	 */
	protected Optional<SingleModel> getSingleModelOptional(
		Object identifier, Class<? extends Identifier> identifierClass) {

		return Try.success(
			identifierClass.getName()
		).mapOptional(
			nameManager::getNameOptional
		).mapOptional(
			itemRouterManager::getItemRoutesOptional
		).mapOptional(
			ItemRoutes::getItemFunctionOptional
		).map(
			function -> function.apply(_httpServletRequest)
		).flatMap(
			function -> function.apply(identifier)
		).<SingleModel>map(
			Unsafe::unsafeCast
		).map(
			Optional::of
		).orElseGet(
			Optional::empty
		);
	}

	/**
	 * Writes the element to a {@code String} by using the supplied message
	 * mapper and the current {@link RequestInfo}.
	 *
	 * @param  t the element being written
	 * @param  s the message mapper
	 * @param  requestInfo the current request info
	 * @return the {@code String} containing the element's representation
	 */
	protected abstract String write(T t, S s, RequestInfo requestInfo);

	@Reference
	protected ItemRouterManager itemRouterManager;

	@Reference
	protected NameManager nameManager;

	@Reference
	protected ProviderManager providerManager;

	@Context
	private HttpServletRequest _httpServletRequest;

	@Context
	private Request _request;

}