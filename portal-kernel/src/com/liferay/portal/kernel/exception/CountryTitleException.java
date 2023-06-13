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

package com.liferay.portal.kernel.exception;

import com.liferay.petra.string.StringBundler;

/**
 * @author Brian Wing Shun Chan
 */
public class CountryTitleException extends PortalException {

	public CountryTitleException() {
	}

	public CountryTitleException(String msg) {
		super(msg);
	}

	public CountryTitleException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public CountryTitleException(Throwable throwable) {
		super(throwable);
	}

	public static class MustNotExceedMaximumLength
		extends CountryTitleException {

		public MustNotExceedMaximumLength(String title, int titleMaxLength) {
			super(
				StringBundler.concat(
					"Title ", title, " must have fewer than ", titleMaxLength,
					" characters"));
		}

	}

}