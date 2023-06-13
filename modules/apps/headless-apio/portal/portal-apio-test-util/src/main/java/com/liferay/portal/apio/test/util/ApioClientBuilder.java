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

package com.liferay.portal.apio.test.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.AuthenticationSpecification;
import io.restassured.specification.PreemptiveAuthSpec;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Cristina González
 */
public class ApioClientBuilder {

	public static RequestSpecification given() {
		return new RequestSpecification(
			Authentication.EMPTY, Collections.emptyMap());
	}

	public static class RequestSpecification {

		public RequestSpecification(
			Authentication authentication, Map<String, String> headers) {

			this(authentication, headers, Body.EMPTY);
		}

		public RequestSpecification(
			Authentication authentication, Map<String, String> headers,
			Body body) {

			this(authentication, headers, body, Collections.emptyList());
		}

		public RequestSpecification(
			Authentication authentication, Map<String, String> headers,
			Body body, List<Multipart> multiparts) {

			_authentication = authentication;

			if (headers == null) {
				_headers = Collections.emptyMap();
			}
			else {
				_headers = Collections.unmodifiableMap(headers);
			}

			if (body == null) {
				_body = Body.EMPTY;
			}
			else {
				_body = body;
			}

			if (multiparts == null) {
				_multiparts = Collections.emptyList();
			}
			else {
				_multiparts = multiparts;
			}
		}

		public RequestSpecification(
			Authentication authentication, Map<String, String> headers,
			List<Multipart> multiparts) {

			this(authentication, headers, Body.EMPTY, multiparts);
		}

		public RequestSpecification basicAuth(String user, String password) {
			return new RequestSpecification(
				new BasicAuthentication(user, password), _headers, _body);
		}

		public RequestSpecification body(String body) {
			return new RequestSpecification(
				_authentication, _headers, new BodyImpl(body));
		}

		public RequestSpecification header(String name, String value) {
			HashMap<String, String> headers = new HashMap<>(_headers);

			headers.put(name, value);

			return new RequestSpecification(_authentication, headers, _body);
		}

		public RequestSpecification multipart(String key, Object value) {
			List<Multipart> multiparts = new ArrayList<>(_multiparts);

			multiparts.add(new MultipartImpl(key, value));

			return new RequestSpecification(
				_authentication, _headers, multiparts);
		}

		public Response when() {
			return new Response(null, this);
		}

		protected io.restassured.specification.RequestSpecification
			getRestAssuredRequestSpecification() {

			io.restassured.specification.RequestSpecification
				requestSpecification = _body.body(
					_authentication.auth(RestAssured.given()));

			for (Multipart multipart : _multiparts) {
				multipart.multipart(requestSpecification);
			}

			return requestSpecification.headers(_headers);
		}

		private final Authentication _authentication;
		private final Body _body;
		private final Map<String, String> _headers;
		private final List<Multipart> _multiparts;

	}

	public static class Response {

		public Response(
			ValidatableResponse validatableResponse,
			RequestSpecification requestSpecification) {

			_validatableResponse = validatableResponse;
			_requestSpecification = requestSpecification;
		}

		public Response delete(String href) {
			io.restassured.specification.RequestSpecification
				requestSpecification =
					_requestSpecification.getRestAssuredRequestSpecification();

			io.restassured.response.Response response =
				requestSpecification.delete(href);

			return new Response(response.then(), _requestSpecification);
		}

		public Response follow(String jsonPath) {
			ExtractableResponse extractableResponse =
				_validatableResponse.extract();

			return get(extractableResponse.path(jsonPath));
		}

		public Response get(String href) {
			io.restassured.specification.RequestSpecification
				requestSpecification =
					_requestSpecification.getRestAssuredRequestSpecification();

			io.restassured.response.Response response =
				requestSpecification.get(href);

			return new Response(response.then(), _requestSpecification);
		}

		public Response post(String href) {
			io.restassured.specification.RequestSpecification
				requestSpecification =
					_requestSpecification.getRestAssuredRequestSpecification();

			io.restassured.response.Response response =
				requestSpecification.post(href);

			return new Response(response.then(), _requestSpecification);
		}

		public Response put(String href) {
			io.restassured.specification.RequestSpecification
				requestSpecification =
					_requestSpecification.getRestAssuredRequestSpecification();

			io.restassured.response.Response response =
				requestSpecification.put(href);

			return new Response(response.then(), _requestSpecification);
		}

		public ValidatableResponse then() {
			return _validatableResponse;
		}

		private final RequestSpecification _requestSpecification;
		private final ValidatableResponse _validatableResponse;

	}

	public interface Authentication {

		public static Authentication EMPTY =
			requestSpecification -> requestSpecification;

		public io.restassured.specification.RequestSpecification auth(
			io.restassured.specification.RequestSpecification
				requestSpecification);

	}

	public interface Body {

		public static Body EMPTY = requestSpecification -> requestSpecification;

		public io.restassured.specification.RequestSpecification body(
			io.restassured.specification.RequestSpecification
				requestSpecification);

	}

	public interface Multipart {

		public static Multipart EMPTY =
			requestSpecification -> requestSpecification;

		public io.restassured.specification.RequestSpecification multipart(
			io.restassured.specification.RequestSpecification
				requestSpecification);

	}

	protected static class BasicAuthentication implements Authentication {

		@Override
		public io.restassured.specification.RequestSpecification auth(
			io.restassured.specification.RequestSpecification
				requestSpecification) {

			AuthenticationSpecification authenticationSpecification =
				requestSpecification.auth();

			PreemptiveAuthSpec preemptiveAuthSpec =
				authenticationSpecification.preemptive();

			return preemptiveAuthSpec.basic(_user, _password);
		}

		protected BasicAuthentication(String user, String password) {
			_user = user;
			_password = password;
		}

		private final String _password;
		private final String _user;

	}

	protected static class BodyImpl implements Body {

		@Override
		public io.restassured.specification.RequestSpecification body(
			io.restassured.specification.RequestSpecification
				requestSpecification) {

			return requestSpecification.body(_body);
		}

		protected BodyImpl(String body) {
			_body = body;
		}

		private String _body;

	}

	protected static class MultipartImpl implements Multipart {

		@Override
		public io.restassured.specification.RequestSpecification multipart(
			io.restassured.specification.RequestSpecification
				requestSpecification) {

			if (_value instanceof File) {
				return requestSpecification.multiPart(_key, (File)_value);
			}
			else if (_value instanceof String) {
				return requestSpecification.multiPart(_key, (String)_value);
			}

			return requestSpecification.multiPart(_key, _value);
		}

		protected MultipartImpl(String key, Object value) {
			_key = key;
			_value = value;
		}

		private final String _key;
		private final Object _value;

	}

}