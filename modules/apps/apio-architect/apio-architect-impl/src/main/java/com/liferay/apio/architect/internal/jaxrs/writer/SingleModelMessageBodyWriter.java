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

package com.liferay.apio.architect.internal.jaxrs.writer;

import static com.liferay.apio.architect.internal.unsafe.Unsafe.unsafeCast;

import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.internal.jaxrs.writer.base.BaseMessageBodyWriter;
import com.liferay.apio.architect.internal.message.json.SingleModelMessageMapper;
import com.liferay.apio.architect.internal.request.RequestInfo;
import com.liferay.apio.architect.internal.wiring.osgi.manager.message.json.SingleModelMessageMapperManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.representable.RepresentableManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.uri.mapper.PathIdentifierMapperManager;
import com.liferay.apio.architect.internal.writer.SingleModelWriter;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.Optional;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Request;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Writes single models by using the {@link SingleModelMessageMapper} that
 * corresponds to the media type.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(liferay.apio.architect.application=true)",
		"osgi.jaxrs.extension=true", "osgi.jaxrs.name=Writer.SingleModel"
	},
	service = MessageBodyWriter.class
)
@Provider
public class SingleModelMessageBodyWriter<T>
	extends BaseMessageBodyWriter<SingleModel<T>, SingleModelMessageMapper<T>> {

	@Override
	public boolean canWrite(Class<?> clazz) {
		return SingleModel.class.isAssignableFrom(clazz);
	}

	@Override
	public Optional<SingleModelMessageMapper<T>> getMessageMapperOptional(
		Request request) {

		return _singleModelMessageMapperManager.
			getSingleModelMessageMapperOptional(request);
	}

	@Override
	protected String write(
		SingleModel<T> singleModel,
		SingleModelMessageMapper<T> singleModelMessageMapper,
		RequestInfo requestInfo) {

		Credentials credentials = providerManager.provideMandatory(
			request, Credentials.class);

		SingleModelWriter<T> singleModelWriter = SingleModelWriter.create(
			builder -> builder.singleModel(
				singleModel
			).modelMessageMapper(
				singleModelMessageMapper
			).pathFunction(
				_pathIdentifierMapperManager::mapToPath
			).resourceNameFunction(
				nameManager::getNameOptional
			).representorFunction(
				name -> unsafeCast(
					_representableManager.getRepresentorOptional(name))
			).requestInfo(
				requestInfo
			).singleModelFunction(
				this::getSingleModelOptional
			).actionSemanticsFunction(
				resource -> actionManager.getActionSemantics(
					resource, credentials, requestInfo.getHttpServletRequest())
			).build());

		Optional<String> optional = singleModelWriter.write();

		return optional.orElseThrow(NotFoundException::new);
	}

	@Reference
	private PathIdentifierMapperManager _pathIdentifierMapperManager;

	@Reference
	private RepresentableManager _representableManager;

	@Reference
	private SingleModelMessageMapperManager _singleModelMessageMapperManager;

}