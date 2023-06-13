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

package com.liferay.portal.vulcan.internal.extension;

import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.vulcan.extension.ExtensionProvider;
import com.liferay.portal.vulcan.extension.PropertyDefinition;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.validation.ValidationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Javier de Arcos
 */
public class EntityExtensionHandlerTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		_entityExtensionHandler = new EntityExtensionHandler(
			_CLASS_NAME,
			Arrays.asList(
				_mockedExtensionProvider1, _mockedExtensionProvider2));
	}

	@Test
	public void testGetExtendedProperties() throws Exception {
		Map<String, Serializable> testMap1 = Collections.singletonMap(
			"test1", "test");

		Mockito.when(
			_mockedExtensionProvider1.getExtendedProperties(
				Mockito.anyLong(), Mockito.anyObject())
		).thenReturn(
			testMap1
		);

		Map<String, Serializable> testMap2 = Collections.singletonMap(
			"test2", 5);

		Mockito.when(
			_mockedExtensionProvider2.getExtendedProperties(
				Mockito.anyLong(), Mockito.anyObject())
		).thenReturn(
			testMap2
		);

		Map<String, Serializable> extendedProperties =
			_entityExtensionHandler.getExtendedProperties(_COMPANY_ID, _OBJECT);

		Mockito.verify(
			_mockedExtensionProvider1
		).getExtendedProperties(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_OBJECT)
		);

		Mockito.verify(
			_mockedExtensionProvider2
		).getExtendedProperties(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_OBJECT)
		);

		Assert.assertEquals(
			extendedProperties.toString(), 2, extendedProperties.size());
		Assert.assertEquals("test", extendedProperties.get("test1"));
		Assert.assertEquals(5, extendedProperties.get("test2"));
	}

	@Test
	public void testGetFilteredPropertyNames() {
		Set<String> testSet1 = Collections.singleton("test1");

		Mockito.doReturn(
			testSet1
		).when(
			_mockedExtensionProvider1
		).getFilteredPropertyNames(
			Mockito.anyLong(), Mockito.anyObject()
		);

		Set<String> testSet2 = Collections.singleton("test2");

		Mockito.doReturn(
			testSet2
		).when(
			_mockedExtensionProvider2
		).getFilteredPropertyNames(
			Mockito.anyLong(), Mockito.anyObject()
		);

		Set<String> filteredProperties =
			_entityExtensionHandler.getFilteredPropertyNames(
				_COMPANY_ID, _OBJECT);

		Mockito.verify(
			_mockedExtensionProvider1
		).getFilteredPropertyNames(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_OBJECT)
		);

		Mockito.verify(
			_mockedExtensionProvider2
		).getFilteredPropertyNames(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_OBJECT)
		);

		Assert.assertEquals(
			filteredProperties.toString(), 2, filteredProperties.size());
		Assert.assertTrue(filteredProperties.contains("test1"));
		Assert.assertTrue(filteredProperties.contains("test2"));
	}

	@Test
	public void testSetExtendedProperties() throws Exception {
		Map<String, Serializable> testExtendedProperties =
			HashMapBuilder.<String, Serializable>put(
				"test1", "test"
			).put(
				"test2", 5
			).build();

		Mockito.when(
			_mockedExtensionProvider1.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			Collections.singletonMap("test1", null)
		);

		Mockito.when(
			_mockedExtensionProvider2.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			Collections.singletonMap("test2", null)
		);

		_entityExtensionHandler.setExtendedProperties(
			_COMPANY_ID, _OBJECT, testExtendedProperties);

		Mockito.verify(
			_mockedExtensionProvider1
		).getExtendedPropertyDefinitions(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_CLASS_NAME)
		);

		Mockito.verify(
			_mockedExtensionProvider2
		).getExtendedPropertyDefinitions(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_CLASS_NAME)
		);

		Mockito.verify(
			_mockedExtensionProvider1
		).setExtendedProperties(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_OBJECT),
			Mockito.eq(Collections.singletonMap("test1", "test"))
		);

		Mockito.verify(
			_mockedExtensionProvider2
		).setExtendedProperties(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_OBJECT),
			Mockito.eq(Collections.singletonMap("test2", 5))
		);
	}

	@Test
	public void testValidate() {
		ExtensionProvider extensionProviderMock1 = Mockito.mock(
			ExtensionProvider.class);
		ExtensionProvider extensionProviderMock2 = Mockito.mock(
			ExtensionProvider.class);

		PropertyDefinition propertyDefinition1 = new PropertyDefinition(
			"field1", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition2 = new PropertyDefinition(
			"field2", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition3 = new PropertyDefinition(
			"field3", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition4 = new PropertyDefinition(
			"field4", PropertyDefinition.PropertyType.TEXT, false);

		Mockito.when(
			extensionProviderMock1.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			HashMapBuilder.put(
				propertyDefinition1.getPropertyName(), propertyDefinition1
			).put(
				propertyDefinition2.getPropertyName(), propertyDefinition2
			).build()
		);

		Mockito.when(
			extensionProviderMock2.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			HashMapBuilder.put(
				propertyDefinition3.getPropertyName(), propertyDefinition3
			).put(
				propertyDefinition4.getPropertyName(), propertyDefinition4
			).build()
		);

		EntityExtensionHandler entityExtensionHandler =
			new EntityExtensionHandler(
				_CLASS_NAME,
				Arrays.asList(extensionProviderMock1, extensionProviderMock2));

		entityExtensionHandler.validate(
			_COMPANY_ID,
			HashMapBuilder.<String, Serializable>put(
				"field1", "value1"
			).<String, Serializable>put(
				"field2", "value2"
			).<String, Serializable>put(
				"field3", "value3"
			).<String, Serializable>put(
				"field4", "value4"
			).build(),
			false);

		Mockito.verify(
			extensionProviderMock1
		).getExtendedPropertyDefinitions(
			_COMPANY_ID, _CLASS_NAME
		);

		Mockito.verify(
			extensionProviderMock2
		).getExtendedPropertyDefinitions(
			_COMPANY_ID, _CLASS_NAME
		);
	}

	@Test(expected = ValidationException.class)
	public void testValidateInvalidProperty() {
		ExtensionProvider extensionProviderMock1 = Mockito.mock(
			ExtensionProvider.class);
		ExtensionProvider extensionProviderMock2 = Mockito.mock(
			ExtensionProvider.class);

		PropertyDefinition propertyDefinition1 = new PropertyDefinition(
			"field1", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition2 = new PropertyDefinition(
			"field2", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition3 = new PropertyDefinition(
			"field3", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition4 = new PropertyDefinition(
			"field4", PropertyDefinition.PropertyType.TEXT, true);

		Mockito.when(
			extensionProviderMock1.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			HashMapBuilder.put(
				propertyDefinition1.getPropertyName(), propertyDefinition1
			).put(
				propertyDefinition2.getPropertyName(), propertyDefinition2
			).build()
		);

		Mockito.when(
			extensionProviderMock2.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			HashMapBuilder.put(
				propertyDefinition3.getPropertyName(), propertyDefinition3
			).put(
				propertyDefinition4.getPropertyName(), propertyDefinition4
			).build()
		);

		EntityExtensionHandler entityExtensionHandler =
			new EntityExtensionHandler(
				_CLASS_NAME,
				Arrays.asList(extensionProviderMock1, extensionProviderMock2));

		entityExtensionHandler.validate(
			_COMPANY_ID,
			HashMapBuilder.<String, Serializable>put(
				"field1", 1L
			).<String, Serializable>put(
				"field2", "value2"
			).<String, Serializable>put(
				"field3", "value3"
			).<String, Serializable>put(
				"field4", "value4"
			).build(),
			false);

		Mockito.verify(
			extensionProviderMock1
		).getExtendedPropertyDefinitions(
			_COMPANY_ID, _CLASS_NAME
		);

		Mockito.verify(
			extensionProviderMock2
		).getExtendedPropertyDefinitions(
			_COMPANY_ID, _CLASS_NAME
		);
	}

	@Test(expected = ValidationException.class)
	public void testValidateMissingRequiredProperty() {
		ExtensionProvider extensionProviderMock1 = Mockito.mock(
			ExtensionProvider.class);
		ExtensionProvider extensionProviderMock2 = Mockito.mock(
			ExtensionProvider.class);

		PropertyDefinition propertyDefinition1 = new PropertyDefinition(
			"field1", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition2 = new PropertyDefinition(
			"field2", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition3 = new PropertyDefinition(
			"field3", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition4 = new PropertyDefinition(
			"field4", PropertyDefinition.PropertyType.TEXT, true);

		Mockito.when(
			extensionProviderMock1.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			HashMapBuilder.put(
				propertyDefinition1.getPropertyName(), propertyDefinition1
			).put(
				propertyDefinition2.getPropertyName(), propertyDefinition2
			).build()
		);

		Mockito.when(
			extensionProviderMock2.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			HashMapBuilder.put(
				propertyDefinition3.getPropertyName(), propertyDefinition3
			).put(
				propertyDefinition4.getPropertyName(), propertyDefinition4
			).build()
		);

		EntityExtensionHandler entityExtensionHandler =
			new EntityExtensionHandler(
				_CLASS_NAME,
				Arrays.asList(extensionProviderMock1, extensionProviderMock2));

		entityExtensionHandler.validate(
			_COMPANY_ID,
			HashMapBuilder.<String, Serializable>put(
				"field1", "value1"
			).<String, Serializable>put(
				"field2", "value2"
			).<String, Serializable>put(
				"field3", "value3"
			).build(),
			false);

		Mockito.verify(
			extensionProviderMock1
		).getExtendedPropertyDefinitions(
			_COMPANY_ID, _CLASS_NAME
		);

		Mockito.verify(
			extensionProviderMock2
		).getExtendedPropertyDefinitions(
			_COMPANY_ID, _CLASS_NAME
		);
	}

	@Test
	public void testValidateMissingRequiredPropertyInPartialUpdate() {
		ExtensionProvider extensionProviderMock1 = Mockito.mock(
			ExtensionProvider.class);
		ExtensionProvider extensionProviderMock2 = Mockito.mock(
			ExtensionProvider.class);

		PropertyDefinition propertyDefinition1 = new PropertyDefinition(
			"field1", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition2 = new PropertyDefinition(
			"field2", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition3 = new PropertyDefinition(
			"field3", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition4 = new PropertyDefinition(
			"field4", PropertyDefinition.PropertyType.TEXT, true);

		Mockito.when(
			extensionProviderMock1.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			HashMapBuilder.put(
				propertyDefinition1.getPropertyName(), propertyDefinition1
			).put(
				propertyDefinition2.getPropertyName(), propertyDefinition2
			).build()
		);

		Mockito.when(
			extensionProviderMock2.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			HashMapBuilder.put(
				propertyDefinition3.getPropertyName(), propertyDefinition3
			).put(
				propertyDefinition4.getPropertyName(), propertyDefinition4
			).build()
		);

		EntityExtensionHandler entityExtensionHandler =
			new EntityExtensionHandler(
				_CLASS_NAME,
				Arrays.asList(extensionProviderMock1, extensionProviderMock2));

		entityExtensionHandler.validate(
			_COMPANY_ID,
			HashMapBuilder.<String, Serializable>put(
				"field1", "value1"
			).<String, Serializable>put(
				"field2", "value2"
			).<String, Serializable>put(
				"field3", "value3"
			).build(),
			true);

		Mockito.verify(
			extensionProviderMock1
		).getExtendedPropertyDefinitions(
			_COMPANY_ID, _CLASS_NAME
		);

		Mockito.verify(
			extensionProviderMock2
		).getExtendedPropertyDefinitions(
			_COMPANY_ID, _CLASS_NAME
		);
	}

	@Test(expected = ValidationException.class)
	public void testValidateUnknownProperty() {
		ExtensionProvider extensionProviderMock1 = Mockito.mock(
			ExtensionProvider.class);
		ExtensionProvider extensionProviderMock2 = Mockito.mock(
			ExtensionProvider.class);

		PropertyDefinition propertyDefinition1 = new PropertyDefinition(
			"field1", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition2 = new PropertyDefinition(
			"field2", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition3 = new PropertyDefinition(
			"field3", PropertyDefinition.PropertyType.TEXT, false);
		PropertyDefinition propertyDefinition4 = new PropertyDefinition(
			"field4", PropertyDefinition.PropertyType.TEXT, true);

		Mockito.when(
			extensionProviderMock1.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			HashMapBuilder.put(
				propertyDefinition1.getPropertyName(), propertyDefinition1
			).put(
				propertyDefinition2.getPropertyName(), propertyDefinition2
			).build()
		);

		Mockito.when(
			extensionProviderMock2.getExtendedPropertyDefinitions(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			HashMapBuilder.put(
				propertyDefinition3.getPropertyName(), propertyDefinition3
			).put(
				propertyDefinition4.getPropertyName(), propertyDefinition4
			).build()
		);

		EntityExtensionHandler entityExtensionHandler =
			new EntityExtensionHandler(
				_CLASS_NAME,
				Arrays.asList(extensionProviderMock1, extensionProviderMock2));

		entityExtensionHandler.validate(
			_COMPANY_ID,
			HashMapBuilder.<String, Serializable>put(
				"field1", "value1"
			).<String, Serializable>put(
				"field2", "value2"
			).<String, Serializable>put(
				"field3", "value3"
			).<String, Serializable>put(
				"field4", "value4"
			).<String, Serializable>put(
				"unknownField", "value5"
			).build(),
			false);

		Mockito.verify(
			extensionProviderMock1
		).getExtendedPropertyDefinitions(
			_COMPANY_ID, _CLASS_NAME
		);

		Mockito.verify(
			extensionProviderMock2
		).getExtendedPropertyDefinitions(
			_COMPANY_ID, _CLASS_NAME
		);
	}

	private static final String _CLASS_NAME =
		"com.liferay.test.model.TestModel";

	private static final long _COMPANY_ID = 11111;

	private static final Object _OBJECT = new Object();

	private EntityExtensionHandler _entityExtensionHandler;

	@Mock
	private ExtensionProvider _mockedExtensionProvider1;

	@Mock
	private ExtensionProvider _mockedExtensionProvider2;

}