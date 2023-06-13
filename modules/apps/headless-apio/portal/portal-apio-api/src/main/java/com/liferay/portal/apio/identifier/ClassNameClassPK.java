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

package com.liferay.portal.apio.identifier;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.ClassedModel;

/**
 * Represents an identifier for entities with a class name and class primary
 * key.
 *
 * @author Alejandro Hernández
 */
@ProviderType
public interface ClassNameClassPK {

	/**
	 * Creates and returns a new {@code ClassNameClassPK} from a class name and
	 * class primary key.
	 *
	 * @param  className the class name
	 * @param  classPK the class primary key
	 * @return the new {@code ClassNameClassPK}
	 */
	public static ClassNameClassPK create(String className, long classPK) {
		return new ClassNameClassPK() {

			@Override
			public String getClassName() {
				return className;
			}

			@Override
			public long getClassPK() {
				return classPK;
			}

		};
	}

	/**
	 * Creates and returns a new {@code ClassNameClassPK} from a {@code
	 * ClassedModel}.
	 *
	 * @param  t the {@code ClassedModel}
	 * @return the new {@code ClassNameClassPK}
	 */
	public static <T extends ClassedModel> ClassNameClassPK create(T t) {
		return create(t.getModelClassName(), (long)t.getPrimaryKeyObj());
	}

	/**
	 * Returns the class name.
	 *
	 * @return the class name
	 */
	public String getClassName();

	/**
	 * Returns the class primary key.
	 *
	 * @return the class primary key
	 */
	public long getClassPK();

}