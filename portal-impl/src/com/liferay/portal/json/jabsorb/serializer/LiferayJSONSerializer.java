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

package com.liferay.portal.json.jabsorb.serializer;

import com.liferay.petra.lang.ClassLoaderPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;

import org.jabsorb.JSONSerializer;
import org.jabsorb.serializer.ObjectMatch;
import org.jabsorb.serializer.SerializerState;
import org.jabsorb.serializer.UnmarshallException;

import org.json.JSONObject;

/**
 * @author Tomas Polesovsky
 */
public class LiferayJSONSerializer extends JSONSerializer {

	@Override
	public ObjectMatch tryUnmarshall(
			SerializerState serializerState,
			@SuppressWarnings("rawtypes") Class clazz, Object jsonObj)
		throws UnmarshallException {

		if (!(serializerState instanceof LiferaySerializerState)) {
			serializerState = new LiferaySerializerState();
		}

		return super.tryUnmarshall(serializerState, clazz, jsonObj);
	}

	@Override
	public Object unmarshall(
			SerializerState serializerState,
			@SuppressWarnings("rawtypes") Class clazz, Object jsonObj)
		throws UnmarshallException {

		if (!(serializerState instanceof LiferaySerializerState)) {
			serializerState = new LiferaySerializerState();
		}

		return super.unmarshall(serializerState, clazz, jsonObj);
	}

	@Override
	protected Class getClassFromHint(Object object) throws UnmarshallException {
		if (object == null) {
			return null;
		}

		if (object instanceof JSONObject) {
			String className = StringPool.BLANK;

			try {
				JSONObject jsonObject = (JSONObject)object;

				className = jsonObject.getString("javaClass");

				if (jsonObject.has("contextName")) {
					String contextName = jsonObject.getString("contextName");

					ClassLoader classLoader = ClassLoaderPool.getClassLoader(
						contextName);

					if (classLoader != null) {
						return Class.forName(className, true, classLoader);
					}

					if (_log.isWarnEnabled()) {
						_log.warn(
							StringBundler.concat(
								"Unable to get class loader for class ",
								className, " in context ", contextName));
					}
				}
			}
			catch (Exception e) {
				throw new UnmarshallException(
					"Unable to get class " + className, e);
			}
		}

		return super.getClassFromHint(object);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayJSONSerializer.class);

}