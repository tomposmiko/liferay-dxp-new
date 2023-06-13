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

package com.liferay.portal.vulcan.internal.jaxrs.writer.interceptor;

import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.vulcan.internal.extension.EntityExtensionHandler;
import com.liferay.portal.vulcan.internal.extension.EntityExtensionThreadLocal;
import com.liferay.portal.vulcan.internal.jaxrs.context.resolver.EntityExtensionHandlerContextResolver;
import com.liferay.portal.vulcan.internal.jaxrs.extension.ExtendedEntity;

import java.io.Serializable;

import java.util.Collections;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Providers;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Javier de Arcos
 */
public class EntityExtensionWriterInterceptorTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		EntityExtensionThreadLocal.setExtendedProperties(null);

		ReflectionTestUtil.setFieldValue(
			_entityExtensionWriterInterceptor, "_company", _company);
		ReflectionTestUtil.setFieldValue(
			_entityExtensionWriterInterceptor, "_providers", _providers);
	}

	@Test
	public void testAroundWrite() throws Exception {
		Map<String, Serializable> extendedProperties = Collections.singletonMap(
			RandomTestUtil.randomString(), RandomTestUtil.randomString());

		EntityExtensionThreadLocal.setExtendedProperties(extendedProperties);

		Mockito.when(
			_company.getCompanyId()
		).thenReturn(
			_COMPANY_ID
		);

		Mockito.when(
			_entityExtensionHandlerContextResolver.getContext(Mockito.any())
		).thenReturn(
			_entityExtensionHandler
		);

		Mockito.when(
			_providers.getContextResolver(
				Mockito.eq(EntityExtensionHandler.class),
				Mockito.any(MediaType.class))
		).thenReturn(
			_entityExtensionHandlerContextResolver
		);

		Mockito.when(
			_writerInterceptorContext.getEntity()
		).thenReturn(
			_TEST_ENTITY
		);

		Mockito.when(
			_writerInterceptorContext.getMediaType()
		).thenReturn(
			MediaType.APPLICATION_JSON_TYPE
		);

		Mockito.when(
			_writerInterceptorContext.getType()
		).thenReturn(
			(Class)TestEntity.class
		);

		_entityExtensionWriterInterceptor.aroundWriteTo(
			_writerInterceptorContext);

		Mockito.verify(
			_entityExtensionHandler
		).getExtendedProperties(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_TEST_ENTITY)
		);

		Mockito.verify(
			_entityExtensionHandler
		).getFilteredPropertyNames(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_TEST_ENTITY)
		);

		Mockito.verify(
			_entityExtensionHandlerContextResolver
		).getContext(
			Mockito.eq(TestEntity.class)
		);

		Mockito.verify(
			_providers
		).getContextResolver(
			Mockito.eq(EntityExtensionHandler.class),
			Mockito.eq(MediaType.APPLICATION_JSON_TYPE)
		);

		Mockito.verify(
			_writerInterceptorContext
		).setEntity(
			Mockito.any(ExtendedEntity.class)
		);

		Mockito.verify(
			_writerInterceptorContext
		).setGenericType(
			ExtendedEntity.class
		);
	}

	@Test
	public void testAroundWriteWithNoEntityExtensionHandler() throws Exception {
		Mockito.when(
			_writerInterceptorContext.getMediaType()
		).thenReturn(
			MediaType.APPLICATION_JSON_TYPE
		);

		Mockito.when(
			_writerInterceptorContext.getType()
		).thenReturn(
			(Class)TestEntity.class
		);

		_entityExtensionWriterInterceptor.aroundWriteTo(
			_writerInterceptorContext);

		Mockito.verify(
			_writerInterceptorContext
		).proceed();

		Mockito.verify(
			_writerInterceptorContext, Mockito.never()
		).setEntity(
			Mockito.any()
		);

		Mockito.verify(
			_writerInterceptorContext, Mockito.never()
		).setGenericType(
			Mockito.any()
		);
	}

	@Test
	public void testAroundWriteWithNoExtendedProperties() throws Exception {
		Mockito.when(
			_company.getCompanyId()
		).thenReturn(
			_COMPANY_ID
		);

		Mockito.when(
			_entityExtensionHandlerContextResolver.getContext(Mockito.any())
		).thenReturn(
			_entityExtensionHandler
		);

		Mockito.when(
			_providers.getContextResolver(
				Mockito.eq(EntityExtensionHandler.class),
				Mockito.any(MediaType.class))
		).thenReturn(
			_entityExtensionHandlerContextResolver
		);
		Mockito.when(
			_writerInterceptorContext.getEntity()
		).thenReturn(
			_TEST_ENTITY
		);

		Mockito.when(
			_writerInterceptorContext.getMediaType()
		).thenReturn(
			MediaType.APPLICATION_JSON_TYPE
		);

		Mockito.when(
			_writerInterceptorContext.getType()
		).thenReturn(
			(Class)TestEntity.class
		);

		_entityExtensionWriterInterceptor.aroundWriteTo(
			_writerInterceptorContext);

		Mockito.verify(
			_entityExtensionHandler
		).getExtendedProperties(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_TEST_ENTITY)
		);

		Mockito.verify(
			_entityExtensionHandler
		).getFilteredPropertyNames(
			Mockito.eq(_COMPANY_ID), Mockito.eq(_TEST_ENTITY)
		);

		Mockito.verify(
			_entityExtensionHandlerContextResolver
		).getContext(
			Mockito.eq(TestEntity.class)
		);

		Mockito.verify(
			_providers
		).getContextResolver(
			Mockito.eq(EntityExtensionHandler.class),
			Mockito.eq(MediaType.APPLICATION_JSON_TYPE)
		);

		Mockito.verify(
			_writerInterceptorContext
		).proceed();

		Mockito.verify(
			_writerInterceptorContext
		).setEntity(
			Mockito.any(ExtendedEntity.class)
		);

		Mockito.verify(
			_writerInterceptorContext
		).setGenericType(
			ExtendedEntity.class
		);
	}

	private static final long _COMPANY_ID = 11111;

	private static final TestEntity _TEST_ENTITY = new TestEntity();

	private final Company _company = Mockito.mock(Company.class);
	private final EntityExtensionHandler _entityExtensionHandler = Mockito.mock(
		EntityExtensionHandler.class);
	private final EntityExtensionHandlerContextResolver
		_entityExtensionHandlerContextResolver = Mockito.mock(
			EntityExtensionHandlerContextResolver.class);
	private final EntityExtensionWriterInterceptor
		_entityExtensionWriterInterceptor =
			new EntityExtensionWriterInterceptor();
	private final Providers _providers = Mockito.mock(Providers.class);
	private final WriterInterceptorContext _writerInterceptorContext =
		Mockito.mock(WriterInterceptorContext.class);

	private static final class TestEntity {
	}

}