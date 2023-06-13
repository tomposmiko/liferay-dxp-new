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

package com.liferay.portal.kernel.test.util;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.portal.kernel.util.Http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.Cookie;

/**
 * @author Mikel Lorza
 */
public class MockHttp implements Http {

	public MockHttp(
		Map<String, UnsafeSupplier<String, Exception>> unsafeSuppliers) {

		if (unsafeSuppliers != null) {
			_unsafeSuppliers = Collections.unmodifiableMap(unsafeSuppliers);
		}
		else {
			_unsafeSuppliers = Collections.emptyMap();
		}
	}

	@Override
	public Cookie[] getCookies() {
		return null;
	}

	@Override
	public boolean hasProxyConfig() {
		return false;
	}

	@Override
	public boolean isNonProxyHost(String host) {
		return false;
	}

	@Override
	public boolean isProxyHost(String host) {
		return false;
	}

	@Override
	public byte[] URLtoByteArray(Options options) throws IOException {
		return _toBytes(options.getLocation(), options);
	}

	@Override
	public byte[] URLtoByteArray(String location) throws IOException {
		return URLtoByteArray(location, false);
	}

	@Override
	public byte[] URLtoByteArray(String location, boolean post)
		throws IOException {

		return _toBytes(location, null);
	}

	@Override
	public InputStream URLtoInputStream(Options options) throws IOException {
		return new ByteArrayInputStream(
			_toBytes(options.getLocation(), options));
	}

	@Override
	public InputStream URLtoInputStream(String location) throws IOException {
		return URLtoInputStream(location, false);
	}

	@Override
	public InputStream URLtoInputStream(String location, boolean post)
		throws IOException {

		return new ByteArrayInputStream(_toBytes(location, null));
	}

	@Override
	public String URLtoString(Options options) throws IOException {
		return new String(_toBytes(options.getLocation(), options));
	}

	@Override
	public String URLtoString(String location) throws IOException {
		return URLtoString(location, false);
	}

	@Override
	public String URLtoString(String location, boolean post)
		throws IOException {

		return new String(_toBytes(location, null));
	}

	@Override
	public String URLtoString(URL url) throws IOException {
		return new String(_toBytes(url, null));
	}

	private byte[] _toBytes(String location, Options options)
		throws IOException {

		return _toBytes(new URL(location), options);
	}

	private byte[] _toBytes(URL url, Options options) throws IOException {
		Response response = new Response();

		options.setResponse(response);

		UnsafeSupplier<String, Exception> unsafeSupplier = _unsafeSuppliers.get(
			url.getPath());

		if (unsafeSupplier != null) {
			response.setResponseCode(200);

			try {
				String string = unsafeSupplier.get();

				return string.getBytes();
			}
			catch (Exception exception) {
				if (options == null) {
					throw new IOException(exception);
				}
			}
		}

		response.setResponseCode(400);

		return "error".getBytes();
	}

	private final Map<String, UnsafeSupplier<String, Exception>>
		_unsafeSuppliers;

}