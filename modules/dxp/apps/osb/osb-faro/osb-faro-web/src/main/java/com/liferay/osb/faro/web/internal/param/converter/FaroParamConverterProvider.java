/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.web.internal.param.converter;

import com.liferay.osb.faro.web.internal.param.FaroParam;
import com.liferay.osb.faro.web.internal.util.JSONUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

/**
 * @author Matthew Kong
 */
@Provider
public class FaroParamConverterProvider implements ParamConverterProvider {

	@Override
	public <T> ParamConverter<T> getConverter(
		final Class<T> rawType, final Type genericType,
		final Annotation[] annotations) {

		if (genericType.equals(String.class)) {
			return null;
		}

		ParamConverter<T> paramConverter =
			(ParamConverter<T>)_paramConverters.get(genericType.getTypeName());

		if (paramConverter == null) {
			paramConverter = new ParamConverter<T>() {

				@Override
				public T fromString(final String value) {
					try {
						if (rawType.equals(long.class) &&
							_containsPathParam(annotations, "groupId")) {

							Group group =
								GroupLocalServiceUtil.fetchFriendlyURLGroup(
									PortalUtil.getDefaultCompanyId(),
									StringPool.SLASH + value);

							if (group != null) {
								return JSONUtil.readValue(
									String.valueOf(group.getGroupId()),
									rawType);
							}
						}

						if (rawType.equals(FaroParam.class)) {
							if (Validator.isNull(value)) {
								return (T)new FaroParam<T>();
							}

							return (T)new FaroParam(
								JSONUtil.readContainedTypeValue(
									value, genericType));
						}

						if (Validator.isNull(value)) {
							if (rawType.equals(double.class) ||
								rawType.equals(int.class) ||
								rawType.equals(long.class)) {

								return JSONUtil.readValue(
									String.valueOf(0), rawType);
							}

							return null;
						}

						return JSONUtil.readValue(value, rawType);
					}
					catch (Exception exception) {
						throw new ProcessingException(
							StringBundler.concat(
								"Unable to deserialize value ", value, " to ",
								rawType.getSimpleName()),
							exception);
					}
				}

				@Override
				public String toString(final T value) {
					try {
						return JSONUtil.writeValueAsString(value);
					}
					catch (Exception exception) {
						throw new ProcessingException(exception);
					}
				}

			};

			_paramConverters.put(genericType.getTypeName(), paramConverter);
		}

		return paramConverter;
	}

	private boolean _containsPathParam(Annotation[] annotations, String param) {
		for (Annotation annotation : annotations) {
			if (annotation instanceof PathParam) {
				PathParam pathParam = (PathParam)annotation;

				if (StringUtil.equals(pathParam.value(), param)) {
					return true;
				}
			}
		}

		return false;
	}

	private static final Map<String, ParamConverter<?>> _paramConverters =
		new HashMap<>();

}