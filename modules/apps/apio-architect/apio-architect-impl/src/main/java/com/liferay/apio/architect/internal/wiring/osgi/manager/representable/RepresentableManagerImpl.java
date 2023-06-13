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

package com.liferay.apio.architect.internal.wiring.osgi.manager.representable;

import static com.liferay.apio.architect.internal.unsafe.Unsafe.unsafeCast;
import static com.liferay.apio.architect.internal.wiring.osgi.manager.TypeArgumentProperties.KEY_PRINCIPAL_TYPE_ARGUMENT;
import static com.liferay.apio.architect.internal.wiring.osgi.manager.cache.ManagerCache.INSTANCE;
import static com.liferay.apio.architect.internal.wiring.osgi.manager.util.ManagerUtil.getGenericClassFromProperty;
import static com.liferay.apio.architect.internal.wiring.osgi.manager.util.ManagerUtil.getTypeParamTry;

import static org.slf4j.LoggerFactory.getLogger;

import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.internal.annotation.ActionRouterRepresentorManager;
import com.liferay.apio.architect.internal.representor.RepresentorImpl.BuilderImpl;
import com.liferay.apio.architect.internal.unsafe.Unsafe;
import com.liferay.apio.architect.internal.wiring.osgi.error.ApioDeveloperError;
import com.liferay.apio.architect.internal.wiring.osgi.manager.base.BaseManager;
import com.liferay.apio.architect.internal.wiring.osgi.validator.NameValidator;
import com.liferay.apio.architect.related.RelatedCollection;
import com.liferay.apio.architect.representor.Representable;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.representor.Representor.Builder;
import com.liferay.osgi.service.tracker.collections.map.ServiceReferenceMapper.Emitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;

/**
 * @author Alejandro Hernández
 */
@Component(
	service = {
		IdentifierClassManager.class, NameManager.class,
		RepresentableManager.class
	}
)
public class RepresentableManagerImpl
	extends BaseManager<Representable, Class<Identifier>>
	implements NameManager, IdentifierClassManager, RepresentableManager {

	public RepresentableManagerImpl() {
		super(Representable.class);
	}

	@Override
	public <T extends Identifier> Optional<Class<T>> getIdentifierClassOptional(
		String name) {

		return INSTANCE.getIdentifierClassOptional(
			name, this::_computeRepresentables);
	}

	@Override
	public Optional<String> getNameOptional(String className) {
		return INSTANCE.getNameOptional(
			className, this::_computeRepresentables);
	}

	@Override
	public <T> Optional<Representor<T>> getRepresentorOptional(String name) {
		return INSTANCE.getRepresentorOptional(
			name, this::_computeRepresentables);
	}

	@Override
	public Map<String, Representor> getRepresentors() {
		return INSTANCE.getRepresentorMap(this::_computeRepresentables);
	}

	@Override
	protected void emit(
		ServiceReference<Representable> serviceReference,
		Emitter<Class<Identifier>> emitter) {

		Representable representable = bundleContext.getService(
			serviceReference);

		if (representable == null) {
			return;
		}

		Try<Class<Object>> classTry = getGenericClassFromProperty(
			serviceReference, KEY_PRINCIPAL_TYPE_ARGUMENT);

		classTry.recoverWith(
			__ -> getTypeParamTry(representable, Representable.class, 2)
		).<Class<Identifier>>map(
			Unsafe::unsafeCast
		).voidFold(
			__ -> _logger.warn(
				"Unable to get identifier class from: {}",
				representable.getClass()),
			emitter::emit
		);
	}

	private void _computeRepresentables() {
		Map<String, List<RelatedCollection<?, ?>>> relatedCollections =
			new HashMap<>();

		forEachService(
			(clazz, representable) -> {
				String name = representable.getName();

				if (!_nameValidator.validate(name)) {
					_logger.warn(
						"Invalid representable name {}. {}", name,
						_nameValidator.getValidationError());

					return;
				}

				Optional<Map<String, String>> optional =
					INSTANCE.getNamesOptional();

				Optional<String> classNameOptional = optional.map(
					Map::entrySet
				).map(
					Collection::stream
				).orElseGet(
					Stream::empty
				).filter(
					entry -> Objects.equals(entry.getValue(), name)
				).map(
					Entry::getKey
				).findFirst();

				if (classNameOptional.isPresent()) {
					String className = classNameOptional.get();

					_logger.warn(
						"Representable registered under {} has name {}, but " +
							"it is already in use by Representable " +
								"registered under {}",
						clazz, name, className);

					return;
				}

				Representor<Object> representor = _getRepresentor(
					unsafeCast(representable), unsafeCast(clazz),
					unsafeCast(relatedCollections));

				INSTANCE.putName(clazz.getName(), name);
				INSTANCE.putIdentifierClass(name, clazz);
				INSTANCE.putRepresentor(name, representor);
			});

		_actionRouterRepresentorManager.computeRepresentors(
			this::_getNameOrFail, relatedCollections);
	}

	private String _getNameOrFail(Class<? extends Identifier<?>> clazz) {
		Optional<String> optional = getNameOptional(clazz.getName());

		return optional.orElseThrow(
			() -> new ApioDeveloperError.IdentifierNameNotFoundInPathFunction(
				clazz.getName()));
	}

	private <T, S, U extends Identifier<S>> Representor<T> _getRepresentor(
		Representable<T, S, U> representable, Class<U> clazz,
		Map<String, List<RelatedCollection<T, ?>>> relatedCollections) {

		Supplier<List<RelatedCollection<T, ?>>> relatedCollectionsSupplier =
			() -> relatedCollections.get(clazz.getName());

		BiConsumer<Class<?>, RelatedCollection<T, ?>> biConsumer =
			(identifierClass, relatedCollection) -> {
				List<RelatedCollection<T, ?>> list =
					relatedCollections.computeIfAbsent(
						identifierClass.getName(), __ -> new ArrayList<>());

				list.add(relatedCollection);
			};

		Builder<T, S> builder = new BuilderImpl<>(
			clazz, this::_getNameOrFail, biConsumer,
			relatedCollectionsSupplier);

		return representable.representor(builder);
	}

	@Reference
	private ActionRouterRepresentorManager _actionRouterRepresentorManager;

	private Logger _logger = getLogger(getClass());

	@Reference
	private NameValidator _nameValidator;

}