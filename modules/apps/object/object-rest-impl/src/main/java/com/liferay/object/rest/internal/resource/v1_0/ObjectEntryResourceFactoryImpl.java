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

package com.liferay.object.rest.internal.resource.v1_0;

import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.rest.internal.security.permission.LiberalPermissionChecker;
import com.liferay.object.rest.resource.v1_0.ObjectEntryResource;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.odata.filter.ExpressionConvert;
import com.liferay.portal.odata.filter.FilterParserProvider;
import com.liferay.portal.odata.sort.SortParserProvider;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alejandro Tardín
 */
public class ObjectEntryResourceFactoryImpl
	implements ObjectEntryResource.Factory {

	public ObjectEntryResourceFactoryImpl(
		CompanyLocalService companyLocalService,
		PermissionCheckerFactory defaultPermissionCheckerFactory,
		ExpressionConvert<Filter> expressionConvert,
		FilterParserProvider filterParserProvider,
		GroupLocalService groupLocalService, ObjectDefinition objectDefinition,
		Supplier<ObjectEntryResourceImpl> objectEntryResourceImplSupplier,
		ResourceActionLocalService resourceActionLocalService,
		ResourcePermissionLocalService resourcePermissionLocalService,
		RoleLocalService roleLocalService,
		SortParserProvider sortParserProvider,
		UserLocalService userLocalService) {

		_companyLocalService = companyLocalService;
		_defaultPermissionCheckerFactory = defaultPermissionCheckerFactory;
		_expressionConvert = expressionConvert;
		_filterParserProvider = filterParserProvider;
		_groupLocalService = groupLocalService;
		_objectDefinition = objectDefinition;
		_objectEntryResourceImplSupplier = objectEntryResourceImplSupplier;
		_resourceActionLocalService = resourceActionLocalService;
		_resourcePermissionLocalService = resourcePermissionLocalService;
		_roleLocalService = roleLocalService;
		_sortParserProvider = sortParserProvider;
		_userLocalService = userLocalService;
	}

	@Override
	public ObjectEntryResource.Builder create() {
		return new ObjectEntryResource.Builder() {

			@Override
			public ObjectEntryResource build() {
				if (_user == null) {
					throw new IllegalArgumentException("User is not set");
				}

				return _objectEntryResourceProxyProviderFunction.apply(
					(proxy, method, arguments) -> _invoke(
						method, arguments, _checkPermissions,
						_httpServletRequest, _httpServletResponse,
						_preferredLocale, _user));
			}

			@Override
			public ObjectEntryResource.Builder checkPermissions(
				boolean checkPermissions) {

				_checkPermissions = checkPermissions;

				return this;
			}

			@Override
			public ObjectEntryResource.Builder httpServletRequest(
				HttpServletRequest httpServletRequest) {

				_httpServletRequest = httpServletRequest;

				return this;
			}

			@Override
			public ObjectEntryResource.Builder httpServletResponse(
				HttpServletResponse httpServletResponse) {

				_httpServletResponse = httpServletResponse;

				return this;
			}

			@Override
			public ObjectEntryResource.Builder preferredLocale(
				Locale preferredLocale) {

				_preferredLocale = preferredLocale;

				return this;
			}

			@Override
			public ObjectEntryResource.Builder user(User user) {
				_user = user;

				return this;
			}

			private boolean _checkPermissions = true;
			private HttpServletRequest _httpServletRequest;
			private HttpServletResponse _httpServletResponse;
			private Locale _preferredLocale;
			private User _user;

		};
	}

	private static Function<InvocationHandler, ObjectEntryResource>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			ObjectEntryResource.class.getClassLoader(),
			ObjectEntryResource.class);

		try {
			Constructor<ObjectEntryResource> constructor =
				(Constructor<ObjectEntryResource>)proxyClass.getConstructor(
					InvocationHandler.class);

			return invocationHandler -> {
				try {
					return constructor.newInstance(invocationHandler);
				}
				catch (ReflectiveOperationException
							reflectiveOperationException) {

					throw new InternalError(reflectiveOperationException);
				}
			};
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new InternalError(noSuchMethodException);
		}
	}

	private Object _invoke(
			Method method, Object[] arguments, boolean checkPermissions,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Locale preferredLocale,
			User user)
		throws Throwable {

		String name = PrincipalThreadLocal.getName();

		PrincipalThreadLocal.setName(user.getUserId());

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (checkPermissions) {
			PermissionThreadLocal.setPermissionChecker(
				_defaultPermissionCheckerFactory.create(user));
		}
		else {
			PermissionThreadLocal.setPermissionChecker(
				new LiberalPermissionChecker(user));
		}

		ObjectEntryResourceImpl objectEntryResourceImpl =
			_objectEntryResourceImplSupplier.get();

		objectEntryResourceImpl.setContextAcceptLanguage(
			new AcceptLanguageImpl(httpServletRequest, preferredLocale, user));
		objectEntryResourceImpl.setContextCompany(
			_companyLocalService.getCompany(user.getCompanyId()));
		objectEntryResourceImpl.setContextHttpServletRequest(
			httpServletRequest);
		objectEntryResourceImpl.setContextHttpServletResponse(
			httpServletResponse);
		objectEntryResourceImpl.setContextUser(user);
		objectEntryResourceImpl.setExpressionConvert(_expressionConvert);
		objectEntryResourceImpl.setFilterParserProvider(_filterParserProvider);
		objectEntryResourceImpl.setGroupLocalService(_groupLocalService);
		objectEntryResourceImpl.setObjectDefinition(_objectDefinition);
		objectEntryResourceImpl.setResourceActionLocalService(
			_resourceActionLocalService);
		objectEntryResourceImpl.setResourcePermissionLocalService(
			_resourcePermissionLocalService);
		objectEntryResourceImpl.setRoleLocalService(_roleLocalService);
		objectEntryResourceImpl.setSortParserProvider(_sortParserProvider);

		try {
			return method.invoke(objectEntryResourceImpl, arguments);
		}
		catch (InvocationTargetException invocationTargetException) {
			throw invocationTargetException.getTargetException();
		}
		finally {
			PrincipalThreadLocal.setName(name);

			PermissionThreadLocal.setPermissionChecker(permissionChecker);
		}
	}

	private static final Function<InvocationHandler, ObjectEntryResource>
		_objectEntryResourceProxyProviderFunction = _getProxyProviderFunction();

	private final CompanyLocalService _companyLocalService;
	private final PermissionCheckerFactory _defaultPermissionCheckerFactory;
	private final ExpressionConvert<Filter> _expressionConvert;
	private final FilterParserProvider _filterParserProvider;
	private final GroupLocalService _groupLocalService;
	private final ObjectDefinition _objectDefinition;
	private final Supplier<ObjectEntryResourceImpl>
		_objectEntryResourceImplSupplier;
	private final ResourceActionLocalService _resourceActionLocalService;
	private final ResourcePermissionLocalService
		_resourcePermissionLocalService;
	private final RoleLocalService _roleLocalService;
	private final SortParserProvider _sortParserProvider;
	private final UserLocalService _userLocalService;

	private class AcceptLanguageImpl implements AcceptLanguage {

		public AcceptLanguageImpl(
			HttpServletRequest httpServletRequest, Locale preferredLocale,
			User user) {

			_httpServletRequest = httpServletRequest;
			_preferredLocale = preferredLocale;
			_user = user;
		}

		@Override
		public List<Locale> getLocales() {
			return Arrays.asList(getPreferredLocale());
		}

		@Override
		public String getPreferredLanguageId() {
			return LocaleUtil.toLanguageId(getPreferredLocale());
		}

		@Override
		public Locale getPreferredLocale() {
			if (_preferredLocale != null) {
				return _preferredLocale;
			}

			if (_httpServletRequest != null) {
				Locale locale = (Locale)_httpServletRequest.getAttribute(
					WebKeys.LOCALE);

				if (locale != null) {
					return locale;
				}
			}

			return _user.getLocale();
		}

		@Override
		public boolean isAcceptAllLanguages() {
			return false;
		}

		private final HttpServletRequest _httpServletRequest;
		private final Locale _preferredLocale;
		private final User _user;

	}

}