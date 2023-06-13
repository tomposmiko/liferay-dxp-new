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

package com.liferay.apio.architect.internal.writer;

import static com.liferay.apio.architect.internal.unsafe.Unsafe.unsafeCast;
import static com.liferay.apio.architect.internal.url.URLCreator.createAbsoluteURL;
import static com.liferay.apio.architect.internal.url.URLCreator.createBinaryURL;
import static com.liferay.apio.architect.internal.url.URLCreator.createNestedCollectionURL;
import static com.liferay.apio.architect.internal.url.URLCreator.createSingleURL;

import static org.slf4j.LoggerFactory.getLogger;

import com.liferay.apio.architect.alias.representor.FieldFunction;
import com.liferay.apio.architect.alias.representor.NestedListFieldFunction;
import com.liferay.apio.architect.consumer.TriConsumer;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.internal.alias.BaseRepresentorFunction;
import com.liferay.apio.architect.internal.alias.PathFunction;
import com.liferay.apio.architect.internal.alias.SingleModelFunction;
import com.liferay.apio.architect.internal.list.FunctionalList;
import com.liferay.apio.architect.internal.request.RequestInfo;
import com.liferay.apio.architect.internal.response.control.Fields;
import com.liferay.apio.architect.internal.single.model.SingleModelImpl;
import com.liferay.apio.architect.internal.unsafe.Unsafe;
import com.liferay.apio.architect.related.RelatedCollection;
import com.liferay.apio.architect.related.RelatedModel;
import com.liferay.apio.architect.representor.BaseRepresentor;
import com.liferay.apio.architect.single.model.SingleModel;
import com.liferay.apio.architect.uri.Path;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;

/**
 * Writes the different fields declared on a {@link Representor}.
 *
 * @author Alejandro Hernández
 * @param  <T> the model's type
 */
public class FieldsWriter<T> {

	/**
	 * Returns the {@link SingleModel} version of a {@link RelatedModel}.
	 *
	 * @param  relatedModel the related model
	 * @param  parentSingleModel the related model's parent single model
	 * @return the single model version of the related model
	 */
	public static <T, S, U> Optional<SingleModel<U>> getSingleModel(
		RelatedModel<T, S> relatedModel, SingleModel<T> parentSingleModel,
		SingleModelFunction singleModelFunction) {

		Function<T, S> modelToIdentifierFunction =
			relatedModel.getModelToIdentifierFunction();

		return modelToIdentifierFunction.andThen(
			s -> singleModelFunction.apply(s, relatedModel.getIdentifierClass())
		).apply(
			parentSingleModel.getModel()
		).map(
			Unsafe::unsafeCast
		);
	}

	public FieldsWriter(
		SingleModel<T> singleModel, RequestInfo requestInfo,
		BaseRepresentor<T> baseRepresentor, Path path,
		FunctionalList<String> embeddedPathElements,
		SingleModelFunction singleModelFunction, PathFunction pathFunction) {

		_singleModel = singleModel;
		_requestInfo = requestInfo;
		_baseRepresentor = baseRepresentor;
		_path = path;
		_embeddedPathElements = embeddedPathElements;
		_singleModelFunction = singleModelFunction;
		_pathFunction = pathFunction;
	}

	/**
	 * Returns the {@link Fields} predicate from the internal {@link
	 * RequestInfo}. If no {@code Fields} information is provided to the {@code
	 * RequestInfo}, this method returns an always-successful predicate.
	 *
	 * @return the {@code Fields} predicate, if {@code Fields} information
	 *         exists; an always-successful predicate otherwise
	 */
	public Predicate<String> getFieldsPredicate() {
		Fields fields = _requestInfo.getFields();

		return fields.apply(_baseRepresentor.getTypes());
	}

	/**
	 * Writes the relative URL fields for the model's application. This method
	 * uses a consumer so each {@code javax.ws.rs.ext.MessageBodyWriter} can
	 * write each field differently.
	 *
	 * @param biConsumer the consumer that writes each field
	 */
	public void writeApplicationRelativeURLFields(
		BiConsumer<String, String> biConsumer) {

		writeFields(
			BaseRepresentor::getApplicationRelativeURLFunctions,
			writeField(
				relativeURL -> createAbsoluteURL(
					_requestInfo.getApplicationURL(), relativeURL),
				biConsumer));
	}

	/**
	 * Writes binary resources. This method uses a {@code BiConsumer} so each
	 * {@code javax.ws.rs.ext.MessageBodyWriter} can write each binary
	 * differently.
	 *
	 * @param biConsumer the {@code BiConsumer} called to write each binary
	 */
	public void writeBinaries(BiConsumer<String, String> biConsumer) {
		Function<String, String> urlFunction = binaryId -> createBinaryURL(
			_requestInfo.getApplicationURL(), binaryId, _path);

		writeFields(
			BaseRepresentor::getBinaryFunctions,
			(key, binaryFile) -> biConsumer.accept(
				key, urlFunction.apply(key)));
	}

	/**
	 * Writes the model's boolean fields. This method uses a {@code BiConsumer}
	 * so each {@code javax.ws.rs.ext.MessageBodyWriter} can write each field
	 * differently.
	 *
	 * @param biConsumer the {@code BiConsumer} called to write each field
	 */
	public void writeBooleanFields(BiConsumer<String, Boolean> biConsumer) {
		writeFields(
			BaseRepresentor::getBooleanFunctions, writeField(biConsumer));
	}

	/**
	 * Writes the model's boolean list fields. This method uses a {@code
	 * BiConsumer} so each {@code javax.ws.rs.ext.MessageBodyWriter} can write
	 * each field differently.
	 *
	 * @param biConsumer the {@code BiConsumer} called to write each field
	 */
	public void writeBooleanListFields(
		BiConsumer<String, List<Boolean>> biConsumer) {

		writeFields(
			BaseRepresentor::getBooleanListFunctions, writeField(biConsumer));
	}

	/**
	 * Returns a consumer for entries of a {@code Map<String, Function<T, S>}.
	 * The consumer uses a value function to get the final value, then uses the
	 * bi-consumer provided as the second parameter to process the key and the
	 * final value. This consumer is called only when the final data isn't empty
	 * or {@code null}.
	 *
	 * @param  biConsumer the consumer used to process the key-value pair
	 * @return the consumer for entries of a {@code Map<String, Function<T, S>}
	 */
	public <U> BiConsumer<String, U> writeField(
		BiConsumer<String, U> biConsumer) {

		return writeField(Function.identity(), biConsumer);
	}

	/**
	 * Returns a consumer for entries of a {@code Map<String, S>}. The consumer
	 * uses the function provided as the first parameter to get the final value,
	 * then uses the bi-consumer provided as the second parameter to process the
	 * key and the final value. This consumer is called only when the final data
	 * isn't empty or {@code null}.
	 *
	 * @param  function the function used to get the final value
	 * @param  biConsumer the consumer used to process the key-value pair
	 * @return the consumer for entries of a {@code Map<String, S>}
	 */
	public <U, V> BiConsumer<String, U> writeField(
		Function<U, V> function, BiConsumer<String, V> biConsumer) {

		return (key, u) -> {
			V data = function.apply(u);

			if (data instanceof String) {
				if ((data != null) && !((String)data).isEmpty()) {
					biConsumer.accept(key, data);
				}
			}
			else if (data != null) {
				biConsumer.accept(key, data);
			}
		};
	}

	/**
	 * Writes a {@code Map<String, S>} returned by a {@link Representor}
	 * function. This method uses a consumer so each caller can decide what to
	 * do with each entry. Each member of the map is filtered using the {@link
	 * Fields} predicate provided by {@link #getFieldsPredicate()}.
	 *
	 * @param representorFunction the {@code Representor} function that returns
	 *        the map being written
	 * @param biConsumer the consumer used to process each filtered entry
	 */
	public <U> void writeFields(
		Function<BaseRepresentor<T>, List<FieldFunction<T, U>>>
			representorFunction,
		BiConsumer<String, U> biConsumer) {

		List<FieldFunction<T, U>> list = representorFunction.apply(
			_baseRepresentor);

		Stream<FieldFunction<T, U>> stream = list.stream();

		stream.filter(
			fieldFunction -> {
				Predicate<String> fieldsPredicate = getFieldsPredicate();

				return fieldsPredicate.test(fieldFunction.getKey());
			}
		).forEach(
			fieldFunction -> _tryToWriteField(
				fieldFunction.getKey(),
				key -> {
					U u = fieldFunction.apply(_singleModel.getModel());

					biConsumer.accept(key, u);
				})
		);
	}

	/**
	 * Writes the model's links. This method uses a {@code BiConsumer} so each
	 * {@code javax.ws.rs.ext.MessageBodyWriter} can write each link
	 * differently.
	 *
	 * @param biConsumer the {@code BiConsumer} called to write each link
	 */
	public void writeLinks(BiConsumer<String, String> biConsumer) {
		writeFields(
			BaseRepresentor::getLinkFunctions,
			writeField(Function.identity(), biConsumer));
	}

	/**
	 * Writes a model's localized string fields. This method uses a {@code
	 * BiConsumer} so each {@code javax.ws.rs.ext.MessageBodyWriter} can write
	 * each field differently.
	 *
	 * @param biConsumer the {@code BiConsumer} called to write each field
	 */
	public void writeLocalizedStringFields(
		BiConsumer<String, String> biConsumer) {

		writeFields(
			BaseRepresentor::getLocalizedStringFunctions,
			writeField(
				function -> function.apply(_requestInfo.getAcceptLanguage()),
				biConsumer));
	}

	public <S> void writeNestedLists(
		BaseRepresentorFunction baseRepresentorFunction,
		SingleModel<S> singleModel,
		BiConsumer<NestedListFieldFunction, List<?>> biConsumer) {

		baseRepresentorFunction.apply(
			singleModel.getResourceName()
		).<BaseRepresentor<S>>map(
			Unsafe::unsafeCast
		).map(
			BaseRepresentor::getNestedListFieldFunctions
		).map(
			List::stream
		).orElseGet(
			Stream::empty
		).forEach(
			nestedListFieldFunction -> {
				Predicate<String> fieldsPredicate = getFieldsPredicate();

				String key = nestedListFieldFunction.getKey();

				if (!fieldsPredicate.test(key)) {
					return;
				}

				List<?> list = nestedListFieldFunction.apply(
					singleModel.getModel());

				if (list == null) {
					return;
				}

				biConsumer.accept(nestedListFieldFunction, list);
			}
		);
	}

	public <S, U> void writeNestedResources(
		BaseRepresentorFunction baseRepresentorFunction,
		SingleModel<U> singleModel, FunctionalList<String> embeddedPathElements,
		TriConsumer
			<SingleModel<S>, FunctionalList<String>, BaseRepresentorFunction>
				triConsumer) {

		baseRepresentorFunction.apply(
			singleModel.getResourceName()
		).<BaseRepresentor<U>>map(
			Unsafe::unsafeCast
		).map(
			BaseRepresentor::getNestedFieldFunctions
		).map(
			List::stream
		).orElseGet(
			Stream::empty
		).forEach(
			nestedFieldFunction -> {
				Predicate<String> fieldsPredicate = getFieldsPredicate();

				String key = nestedFieldFunction.getKey();

				if (!fieldsPredicate.test(key)) {
					return;
				}

				Object mappedModel = nestedFieldFunction.apply(
					singleModel.getModel());

				if (mappedModel == null) {
					return;
				}

				FunctionalList<String> embeddedNestedPathElements =
					new FunctionalList<>(
						embeddedPathElements, nestedFieldFunction.getKey());

				SingleModelImpl nestedSingleModel = new SingleModelImpl<>(
					mappedModel, "", Collections.emptyList());

				BaseRepresentorFunction nestedRepresentorFunction =
					__ -> Optional.of(
						nestedFieldFunction.getNestedRepresentor());

				triConsumer.accept(
					nestedSingleModel, embeddedNestedPathElements,
					nestedRepresentorFunction);
			}
		);
	}

	/**
	 * Writes a model's number fields. This method uses a {@code BiConsumer} so
	 * each {@code javax.ws.rs.ext.MessageBodyWriter} can write each field
	 * differently.
	 *
	 * @param biConsumer the {@code BiConsumer} called to write each field
	 */
	public void writeNumberFields(BiConsumer<String, Number> biConsumer) {
		writeFields(
			BaseRepresentor::getNumberFunctions, writeField(biConsumer));
	}

	/**
	 * Writes the model's number list fields. This method uses a {@code
	 * BiConsumer} so each {@code javax.ws.rs.ext.MessageBodyWriter} can write
	 * each field differently.
	 *
	 * @param biConsumer the {@code BiConsumer} called to write each field
	 */
	public void writeNumberListFields(
		BiConsumer<String, List<Number>> biConsumer) {

		writeFields(
			BaseRepresentor::getNumberListFunctions, writeField(biConsumer));
	}

	/**
	 * Writes the related collection's URL, using a {@code BiConsumer}.
	 *
	 * @param relatedCollection the related collection
	 * @param parentEmbeddedPathElements the list of embedded path elements
	 * @param biConsumer the {@code BiConsumer} that writes the related
	 *        collection URL
	 */
	public <U extends Identifier> void writeRelatedCollection(
		RelatedCollection<T, U> relatedCollection, String resourceName,
		FunctionalList<String> parentEmbeddedPathElements,
		BiConsumer<String, FunctionalList<String>> biConsumer) {

		Predicate<String> fieldsPredicate = getFieldsPredicate();

		String key = relatedCollection.getKey();

		if (!fieldsPredicate.test(key)) {
			return;
		}

		Path path = Try.fromFallible(
			relatedCollection::getModelToIdentifierFunction
		).map(
			function -> function.apply(_singleModel.getModel())
		).map(
			model -> _pathFunction.apply(resourceName, model)
		).map(
			Optional::get
		).orElse(
			_path
		);

		_writeCollectionPath(
			resourceName, parentEmbeddedPathElements, biConsumer, key, path);
	}

	/**
	 * Writes the related collections contained in the {@link Representor} this
	 * writer handles. This method uses a consumer so each {@code
	 * javax.ws.rs.ext.MessageBodyWriter} can write the related model
	 * differently.
	 *
	 * @param nameFunction the function that gets a class's {@code
	 *        com.liferay.apio.architect.resource.CollectionResource} name
	 * @param biConsumer the consumer that writes a linked related model's URL
	 */
	public void writeRelatedCollections(
		Function<String, Optional<String>> nameFunction,
		BiConsumer<String, FunctionalList<String>> biConsumer) {

		BaseRepresentor<T> representor = _baseRepresentor;

		Stream<RelatedCollection<T, ?>> stream =
			representor.getRelatedCollections();

		stream.forEach(
			relatedCollection -> {
				Class<?> identifierClass =
					relatedCollection.getIdentifierClass();

				Optional<String> optional = nameFunction.apply(
					identifierClass.getName());

				optional.ifPresent(
					name -> writeRelatedCollection(
						relatedCollection, name, _embeddedPathElements,
						biConsumer));
			});
	}

	/**
	 * Writes a related model. This method uses three consumers: one that writes
	 * the model's info, one that writes its URL if it's a linked related model,
	 * and one that writes its URL if it's an embedded related model. Therefore,
	 * each {@code javax.ws.rs.ext.MessageBodyWriter} can write the related
	 * model differently.
	 *
	 * @param relatedModel the related model instance
	 * @param pathFunction the function that gets a single model's path
	 * @param modelBiConsumer the consumer that writes the related model's
	 *        information
	 * @param linkedURLBiConsumer the consumer that writes a linked related
	 *        model's URL
	 * @param embeddedURLBiConsumer the consumer that writes an embedded related
	 *        model's url
	 */
	public <U> void writeRelatedModel(
		RelatedModel<T, U> relatedModel, PathFunction pathFunction,
		BiConsumer<SingleModel<?>, FunctionalList<String>> modelBiConsumer,
		BiConsumer<String, FunctionalList<String>> linkedURLBiConsumer,
		BiConsumer<String, FunctionalList<String>> embeddedURLBiConsumer) {

		writeRelatedModel(
			relatedModel, pathFunction,
			(url, embeddedPathElements) -> {
				Predicate<String> embedded = _requestInfo.getEmbedded();

				Stream<String> stream = Stream.concat(
					Stream.of(embeddedPathElements.head()),
					embeddedPathElements.tailStream());

				String embeddedPath = String.join(
					".", stream.collect(Collectors.toList()));

				if (embedded.test(embeddedPath)) {
					Optional<SingleModel<U>> singleModelOptional =
						getSingleModel(
							relatedModel, _singleModel,
							unsafeCast(_singleModelFunction));

					if (!singleModelOptional.isPresent()) {
						return;
					}

					SingleModel<U> singleModel = singleModelOptional.get();

					embeddedURLBiConsumer.accept(url, embeddedPathElements);
					modelBiConsumer.accept(singleModel, embeddedPathElements);
				}
				else {
					linkedURLBiConsumer.accept(url, embeddedPathElements);
				}
			});
	}

	/**
	 * Writes a related model. This method uses a consumer so each {@code
	 * javax.ws.rs.ext.MessageBodyWriter} can write the related model
	 * differently.
	 *
	 * @param relatedModel the related model instance
	 * @param pathFunction the function that gets the path of a {@link
	 *        SingleModel}
	 * @param biConsumer the consumer that writes a related model's URL and
	 *        embedded path elements
	 */
	public <U> void writeRelatedModel(
		RelatedModel<T, U> relatedModel, PathFunction pathFunction,
		BiConsumer<String, FunctionalList<String>> biConsumer) {

		Predicate<String> fieldsPredicate = getFieldsPredicate();

		String key = relatedModel.getKey();

		if (!fieldsPredicate.test(key)) {
			return;
		}

		FunctionalList<String> embeddedPathElements = new FunctionalList<>(
			_embeddedPathElements, key);

		Function<T, U> modelToIdentifierFunction =
			relatedModel.getModelToIdentifierFunction();

		U relatedIdentifier = modelToIdentifierFunction.apply(
			_singleModel.getModel());

		if (relatedIdentifier == null) {
			return;
		}

		pathFunction.apply(
			relatedModel.getIdentifierName(), relatedIdentifier
		).map(
			path -> createSingleURL(_requestInfo.getApplicationURL(), path)
		).ifPresent(
			url -> _tryToWriteField(
				key, __ -> biConsumer.accept(url, embeddedPathElements))
		);
	}

	/**
	 * Writes the related models contained in the {@link Representor} this
	 * writer handles. This method uses three consumers: one that writes the
	 * model's info, one that writes its URL if it's a linked related model, and
	 * one that writes its URL if it's an embedded related model. Therefore,
	 * each {@code javax.ws.rs.ext.MessageBodyWriter} can write the related
	 * model differently.
	 *
	 * @param modelBiConsumer the consumer that writes the related model's
	 *        information
	 * @param linkedURLBiConsumer the consumer that writes a linked related
	 *        model's URL
	 * @param embeddedURLBiConsumer the consumer that writes an embedded related
	 *        model's URL
	 */
	public void writeRelatedModels(
		PathFunction pathFunction,
		BiConsumer<SingleModel<?>, FunctionalList<String>> modelBiConsumer,
		BiConsumer<String, FunctionalList<String>> linkedURLBiConsumer,
		BiConsumer<String, FunctionalList<String>> embeddedURLBiConsumer) {

		List<RelatedModel<T, ?>> embeddedRelatedModels =
			_baseRepresentor.getRelatedModels();

		embeddedRelatedModels.forEach(
			relatedModel -> writeRelatedModel(
				relatedModel, pathFunction, modelBiConsumer,
				linkedURLBiConsumer, embeddedURLBiConsumer));
	}

	/**
	 * Writes the model's relative URL fields. This method uses a consumer so
	 * each {@code javax.ws.rs.ext.MessageBodyWriter} can write each field
	 * differently.
	 *
	 * @param biConsumer the consumer that writes each field
	 */
	public void writeRelativeURLFields(BiConsumer<String, String> biConsumer) {
		writeFields(
			BaseRepresentor::getRelativeURLFunctions,
			writeField(
				relativeURL -> createAbsoluteURL(
					_requestInfo.getServerURL(), relativeURL),
				biConsumer));
	}

	/**
	 * Writes the the handled resource's single URL. This method uses a consumer
	 * so each {@code javax.ws.rs.ext.MessageBodyWriter} can write the URL
	 * differently.
	 *
	 * @param urlConsumer the consumer that writes the URL
	 */
	public void writeSingleURL(Consumer<String> urlConsumer) {
		String url = createSingleURL(_requestInfo.getApplicationURL(), _path);

		urlConsumer.accept(url);
	}

	/**
	 * Writes the model's string fields. This method uses a consumer so each
	 * {@code javax.ws.rs.ext.MessageBodyWriter} can write each field
	 * differently.
	 *
	 * @param biConsumer the consumer that writes each field
	 */
	public void writeStringFields(BiConsumer<String, String> biConsumer) {
		writeFields(
			BaseRepresentor::getStringFunctions, writeField(biConsumer));
	}

	/**
	 * Writes the model's string list fields. This method uses a {@code
	 * BiConsumer} so each {@code javax.ws.rs.ext.MessageBodyWriter} can write
	 * each field differently.
	 *
	 * @param biConsumer the {@code BiConsumer} called to write each field
	 */
	public void writeStringListFields(
		BiConsumer<String, List<String>> biConsumer) {

		writeFields(
			BaseRepresentor::getStringListFunctions, writeField(biConsumer));
	}

	/**
	 * Writes the model's types. This method uses a consumer so each {@link
	 * javax.ws.rs.ext.MessageBodyWriter} can write the types differently.
	 *
	 * @param consumer the consumer that writes the types
	 */
	public void writeTypes(Consumer<List<String>> consumer) {
		consumer.accept(_baseRepresentor.getTypes());
	}

	private void _tryToWriteField(String key, Consumer<String> consumer) {
		try {
			consumer.accept(key);
		}
		catch (Exception e) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Unable to write field" + key, e);
			}
		}
	}

	private void _writeCollectionPath(
		String resourceName, FunctionalList<String> parentEmbeddedPathElements,
		BiConsumer<String, FunctionalList<String>> biConsumer, String key,
		Path path) {

		String url = createNestedCollectionURL(
			_requestInfo.getApplicationURL(), path, resourceName);

		FunctionalList<String> embeddedPathElements = new FunctionalList<>(
			parentEmbeddedPathElements, key);

		_tryToWriteField(
			key, __ -> biConsumer.accept(url, embeddedPathElements));
	}

	private final BaseRepresentor<T> _baseRepresentor;
	private final FunctionalList<String> _embeddedPathElements;
	private final Logger _logger = getLogger(getClass());
	private final Path _path;
	private final PathFunction _pathFunction;
	private final RequestInfo _requestInfo;
	private final SingleModel<T> _singleModel;
	private final SingleModelFunction _singleModelFunction;

}