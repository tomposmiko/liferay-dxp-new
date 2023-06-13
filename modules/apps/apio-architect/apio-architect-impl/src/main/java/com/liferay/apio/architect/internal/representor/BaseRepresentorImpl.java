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

package com.liferay.apio.architect.internal.representor;

import static com.liferay.apio.architect.internal.date.DateTransformer.asString;

import com.liferay.apio.architect.alias.BinaryFunction;
import com.liferay.apio.architect.alias.representor.FieldFunction;
import com.liferay.apio.architect.alias.representor.NestedFieldFunction;
import com.liferay.apio.architect.alias.representor.NestedListFieldFunction;
import com.liferay.apio.architect.file.BinaryFile;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.internal.related.RelatedCollectionImpl;
import com.liferay.apio.architect.internal.related.RelatedModelImpl;
import com.liferay.apio.architect.internal.unsafe.Unsafe;
import com.liferay.apio.architect.language.AcceptLanguage;
import com.liferay.apio.architect.related.RelatedCollection;
import com.liferay.apio.architect.related.RelatedModel;
import com.liferay.apio.architect.representor.BaseRepresentor;
import com.liferay.apio.architect.representor.NestedRepresentor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Alejandro Hernández
 */
public abstract class BaseRepresentorImpl<T> implements BaseRepresentor<T> {

	@Override
	public List<FieldFunction<T, String>> getApplicationRelativeURLFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("APPLICATION_RELATIVE_URL")
		).<List<FieldFunction<T, String>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	@Override
	public Optional<BinaryFunction<T>> getBinaryFunction(String binaryId) {
		return Optional.ofNullable(binaryFunctions.get(binaryId));
	}

	@Override
	public List<FieldFunction<T, BinaryFile>> getBinaryFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("BINARY")
		).<List<FieldFunction<T, BinaryFile>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	@Override
	public List<FieldFunction<T, Boolean>> getBooleanFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("BOOLEAN")
		).<List<FieldFunction<T, Boolean>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	@Override
	public List<FieldFunction<T, List<Boolean>>> getBooleanListFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("BOOLEAN_LIST")
		).<List<FieldFunction<T, List<Boolean>>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	@Override
	public List<FieldFunction<T, String>> getLinkFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("LINK")
		).<List<FieldFunction<T, String>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	@Override
	public List<FieldFunction<T, Function<AcceptLanguage, String>>>
		getLocalizedStringFunctions() {

		return Optional.ofNullable(
			fieldFunctions.get("LOCALIZED")
		).<List<FieldFunction<T, Function<AcceptLanguage, String>>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	@Override
	public List<NestedFieldFunction<T, ?>> getNestedFieldFunctions() {
		return nestedFieldFunctions;
	}

	@Override
	public List<NestedListFieldFunction<T, ?>> getNestedListFieldFunctions() {
		return nestedListFieldFunctions;
	}

	@Override
	public List<FieldFunction<T, Number>> getNumberFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("NUMBER")
		).<List<FieldFunction<T, Number>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	@Override
	public List<FieldFunction<T, List<Number>>> getNumberListFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("NUMBER_LIST")
		).<List<FieldFunction<T, List<Number>>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	@Override
	public String getPrimaryType() {
		return primaryType;
	}

	@Override
	public Stream<RelatedCollection<T, ? extends Identifier>>
		getRelatedCollections() {

		return Stream.of(
			relatedCollections, supplier.get()
		).filter(
			Objects::nonNull
		).flatMap(
			Collection::stream
		);
	}

	@Override
	public List<RelatedModel<T, ?>> getRelatedModels() {
		return relatedModels;
	}

	@Override
	public List<FieldFunction<T, String>> getRelativeURLFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("RELATIVE_URL")
		).<List<FieldFunction<T, String>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	@Override
	public List<FieldFunction<T, String>> getStringFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("STRING")
		).<List<FieldFunction<T, String>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	@Override
	public List<FieldFunction<T, List<String>>> getStringListFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("STRING_LIST")
		).<List<FieldFunction<T, List<String>>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	@Override
	public List<String> getTypes() {
		return types;
	}

	protected BaseRepresentorImpl(
		Function<Class<? extends Identifier<?>>, String> nameFunction,
		Supplier<List<RelatedCollection<T, ?>>> supplier) {

		_nameFunction = nameFunction;
		this.supplier = supplier;
	}

	/**
	 * Adds a relative URL function to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param function the function used to get the URL
	 */
	protected void addApplicationRelativeURLFunction(
		String key, Function<T, String> function) {

		_addFieldFunction(key, function, "APPLICATION_RELATIVE_URL");
	}

	/**
	 * Adds a binary function to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the binary resource's name
	 * @param binaryFunction the function used to get the binaries
	 */
	protected void addBinaryFunction(
		String key, BinaryFunction<T> binaryFunction) {

		binaryFunctions.put(key, binaryFunction);

		_addFieldFunction(key, binaryFunction, "BINARY");
	}

	/**
	 * Adds a boolean function to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param function the function used to get the boolean
	 */
	protected void addBooleanFunction(
		String key, Function<T, Boolean> function) {

		_addFieldFunction(key, function, "BOOLEAN");
	}

	/**
	 * Adds a boolean list function to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param function the function used to get the boolean list
	 */
	protected void addBooleanListFunction(
		String key, Function<T, List<Boolean>> function) {

		_addFieldFunction(key, function, "BOOLEAN_LIST");
	}

	/**
	 * Adds a language function to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param function the function used to get the language function
	 */
	protected void addLanguageFunction(
		String key, Function<T, Function<AcceptLanguage, String>> function) {

		_addFieldFunction(key, function, "LOCALIZED");
	}

	/**
	 * Adds a link function to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param function the function used to get the link
	 */
	protected void addLinkFunction(String key, Function<T, String> function) {
		_addFieldFunction(key, function, "LINK");
	}

	/**
	 * Adds a nested field to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param transformFunction the function that transforms the model into the
	 *        model used inside the {@link NestedRepresentor}
	 * @param function the function used to get the nested representor
	 */
	protected <S> void addNestedField(
		String key, Function<T, S> transformFunction,
		Function<NestedRepresentor.Builder<S>, NestedRepresentor<S>> function) {

		NestedFieldFunction<T, S> nestedFieldFunction = function.andThen(
			nestedRepresentor -> new NestedFieldFunction<T, S>() {

				@Override
				public S apply(T t) {
					return transformFunction.apply(t);
				}

				@Override
				public String getKey() {
					return key;
				}

				@Override
				public NestedRepresentor<S> getNestedRepresentor() {
					return nestedRepresentor;
				}

			}
		).apply(
			new NestedRepresentorImpl.BuilderImpl(_nameFunction)
		);

		nestedFieldFunctions.add(nestedFieldFunction);
	}

	/**
	 * Adds a nested list field to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param transformFunction the function that transforms the model into the
	 *        list whose models are used inside the {@link NestedRepresentor}
	 * @param function the function that creates the nested representor for each
	 *        model
	 */
	protected <S> void addNestedListField(
		String key, Function<T, List<S>> transformFunction,
		Function<NestedRepresentor.Builder<S>, NestedRepresentor<S>> function) {

		NestedListFieldFunction<T, S> nestedFieldFunction = function.andThen(
			nestedRepresentor -> new NestedListFieldFunction<T, S>() {

				@Override
				public List<S> apply(T t) {
					return transformFunction.apply(t);
				}

				@Override
				public String getKey() {
					return key;
				}

				@Override
				public NestedRepresentor<S> getNestedRepresentor() {
					return nestedRepresentor;
				}

			}
		).apply(
			new NestedRepresentorImpl.BuilderImpl(_nameFunction)
		);

		nestedListFieldFunctions.add(nestedFieldFunction);
	}

	/**
	 * Adds a number function to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param function the function used to get the number
	 */
	protected void addNumberFunction(String key, Function<T, Number> function) {
		_addFieldFunction(key, function, "NUMBER");
	}

	/**
	 * Adds a number list function to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param function the function used to get the number list
	 */
	protected void addNumberListFunction(
		String key, Function<T, List<Number>> function) {

		_addFieldFunction(key, function, "NUMBER_LIST");
	}

	protected <S extends Identifier<?>> void addRelatedCollection(
		String key, Class<S> itemIdentifierClass,
		Function<T, ?> modelToIdentifierFunction) {

		RelatedCollection<T, ?> relatedCollection = new RelatedCollectionImpl(
			key, itemIdentifierClass, modelToIdentifierFunction);

		relatedCollections.add(relatedCollection);
	}

	/**
	 * Adds information about a related model.
	 *
	 * @param key the relation's name
	 * @param identifierClass the related model's identifier class
	 * @param modelToIdentifierFunction the function used to get the related
	 *        model's identifier
	 */
	protected <S> void addRelatedModel(
		String key, Class<? extends Identifier<S>> identifierClass,
		Function<T, S> modelToIdentifierFunction) {

		RelatedModel<T, S> relatedModel = new RelatedModelImpl<>(
			key, identifierClass, modelToIdentifierFunction,
			() -> _nameFunction.apply(identifierClass));

		relatedModels.add(relatedModel);
	}

	/**
	 * Adds a relative URL function to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param function the function used to get the URL
	 */
	protected void addRelativeURLFunction(
		String key, Function<T, String> function) {

		_addFieldFunction(key, function, "RELATIVE_URL");
	}

	/**
	 * Adds a string function to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param function the function used to get the string
	 */
	protected void addStringFunction(String key, Function<T, String> function) {
		_addFieldFunction(key, function, "STRING");
	}

	/**
	 * Adds a string list function to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param key the field's name
	 * @param function the function used to get the string list
	 */
	protected void addStringListFunction(
		String key, Function<T, List<String>> function) {

		_addFieldFunction(key, function, "STRING_LIST");
	}

	/**
	 * Adds the types to the {@link
	 * com.liferay.apio.architect.representor.Representor}.
	 *
	 * @param type the first type
	 * @param types the rest of the types
	 */
	protected void addTypes(String type, String... types) {
		primaryType = type;

		this.types.add(type);
		Collections.addAll(this.types, types);
	}

	protected final Map<String, BinaryFunction<T>> binaryFunctions =
		new LinkedHashMap<>();
	protected final Map<String, List<FieldFunction<T, ?>>> fieldFunctions =
		new LinkedHashMap<>();
	protected final List<NestedFieldFunction<T, ?>> nestedFieldFunctions =
		new ArrayList<>();
	protected final List<NestedListFieldFunction<T, ?>>
		nestedListFieldFunctions = new ArrayList<>();
	protected String primaryType;
	protected final List<RelatedCollection<T, ?>> relatedCollections =
		new ArrayList<>();
	protected final List<RelatedModel<T, ?>> relatedModels = new ArrayList<>();
	protected final Supplier<List<RelatedCollection<T, ?>>> supplier;
	protected final List<String> types = new ArrayList<>();

	protected abstract static class BaseBuilderImpl
		<T, S extends BaseRepresentorImpl<T>> {

		public abstract class BaseFirstStepImpl
			<U extends BaseRepresentor<T>, V extends BaseFirstStep<T, U, V>>
				implements BaseFirstStep<T, U, V> {

			@Override
			public V addApplicationRelativeURL(
				String key, Function<T, String> function) {

				baseRepresentor.addApplicationRelativeURLFunction(
					key, function);

				return _this;
			}

			@Override
			public V addBinary(String key, BinaryFunction<T> binaryFunction) {
				baseRepresentor.addBinaryFunction(key, binaryFunction);

				return _this;
			}

			@Override
			public V addBoolean(String key, Function<T, Boolean> function) {
				baseRepresentor.addBooleanFunction(key, function);

				return _this;
			}

			@Override
			public V addBooleanList(
				String key, Function<T, List<Boolean>> function) {

				baseRepresentor.addBooleanListFunction(key, function);

				return _this;
			}

			@Override
			public V addDate(String key, Function<T, Date> function) {
				Function<Date, String> formatFunction = date -> {
					if (date == null) {
						return null;
					}

					return asString(date);
				};

				baseRepresentor.addStringFunction(
					key, function.andThen(formatFunction));

				return _this;
			}

			@Override
			public V addLink(String key, String url) {
				baseRepresentor.addLinkFunction(key, __ -> url);

				return _this;
			}

			@Override
			public <W> V addLinkedModel(
				String key, Class<? extends Identifier<W>> identifierClass,
				Function<T, W> modelToIdentifierFunction) {

				baseRepresentor.addRelatedModel(
					key, identifierClass, modelToIdentifierFunction);

				return _this;
			}

			@Override
			public V addLocalizedStringByLanguage(
				String key,
				BiFunction<T, AcceptLanguage, String> stringFunction) {

				baseRepresentor.addLanguageFunction(
					key,
					t -> acceptLanguage -> stringFunction.apply(
						t, acceptLanguage));

				return _this;
			}

			@Override
			public V addLocalizedStringByLocale(
				String key, BiFunction<T, Locale, String> stringFunction) {

				return addLocalizedStringByLanguage(
					key,
					(t, acceptLanguage) -> stringFunction.apply(
						t, acceptLanguage.getPreferredLocale()));
			}

			@Override
			public <W> V addNested(
				String key, Function<T, W> transformFunction,
				Function<NestedRepresentor.Builder<W>, NestedRepresentor<W>>
					function) {

				baseRepresentor.addNestedField(
					key, transformFunction, function);

				return _this;
			}

			@Override
			public <W> V addNestedList(
				String key, Function<T, List<W>> transformFunction,
				Function<NestedRepresentor.Builder<W>, NestedRepresentor<W>>
					function) {

				baseRepresentor.addNestedListField(
					key, transformFunction, function);

				return _this;
			}

			@Override
			public V addNumber(String key, Function<T, Number> function) {
				baseRepresentor.addNumberFunction(key, function);

				return _this;
			}

			@Override
			public V addNumberList(
				String key, Function<T, List<Number>> function) {

				baseRepresentor.addNumberListFunction(key, function);

				return _this;
			}

			@Override
			public <W, S extends Identifier<?>> V addRelatedCollection(
				String key, Class<S> itemIdentifierClass,
				Function<T, W> modelToIdentifierFunction) {

				baseRepresentor.addRelatedCollection(
					key, itemIdentifierClass, modelToIdentifierFunction);

				return _this;
			}

			@Override
			public V addRelativeURL(String key, Function<T, String> function) {
				baseRepresentor.addRelativeURLFunction(key, function);

				return _this;
			}

			@Override
			public V addString(String key, Function<T, String> function) {
				baseRepresentor.addStringFunction(key, function);

				return _this;
			}

			@Override
			public V addStringList(
				String key, Function<T, List<String>> function) {

				baseRepresentor.addStringListFunction(key, function);

				return _this;
			}

			@Override
			public U build() {
				return (U)baseRepresentor;
			}

			protected BaseFirstStepImpl() {
				_this = getThis();
			}

			/**
			 * Returns the generic instance of this builder's step
			 *
			 * <p>
			 * All descendants should return {@code this}.
			 * </p>
			 *
			 * @return the generic instance of this builder's step
			 */
			protected abstract V getThis();

			private final V _this;

		}

		protected BaseBuilderImpl(S baseRepresentor) {
			this.baseRepresentor = baseRepresentor;
		}

		protected final S baseRepresentor;

	}

	private <S> void _addFieldFunction(
		String key, Function<T, S> function, String mapKey) {

		List<FieldFunction<T, ?>> list = fieldFunctions.computeIfAbsent(
			mapKey, __ -> new ArrayList<>());

		FieldFunction<T, S> fieldFunction = new FieldFunction<T, S>() {

			@Override
			public S apply(T t) {
				return function.apply(t);
			}

			@Override
			public String getKey() {
				return key;
			}

		};

		list.add(fieldFunction);
	}

	private final Function<Class<? extends Identifier<?>>, String>
		_nameFunction;

}