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

package com.liferay.headless.form.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.headless.form.dto.v1_0.Creator;
import com.liferay.headless.form.dto.v1_0.FieldValues;
import com.liferay.headless.form.dto.v1_0.Form;
import com.liferay.headless.form.dto.v1_0.FormRecord;
import com.liferay.headless.form.dto.v1_0.Options;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseFormRecordResourceTestCase {

	@Before
	public void setUp() throws Exception {
		testGroup = GroupTestUtil.addGroup();

		_resourceURL = new URL(
			"http://localhost:8080/o/headless-form/v1.0");
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testGetFormRecord() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPutFormRecord() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetFormFormRecordsPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPostFormFormRecord() throws Exception {
			Assert.assertTrue(true);
	}

	protected void assertResponseCode(int expectedResponseCode, Http.Response actualResponse) {
		Assert.assertEquals(expectedResponseCode, actualResponse.getResponseCode());
	}

	protected FormRecord invokeGetFormRecord(
				Long formRecordId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/form-records/{form-record-id}", formRecordId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), FormRecordImpl.class);
	}

	protected Http.Response invokeGetFormRecordResponse(
				Long formRecordId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/form-records/{form-record-id}", formRecordId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected FormRecord invokePutFormRecord(
				Long formRecordId,FormRecord formRecord)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(formRecord), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/form-records/{form-record-id}", formRecordId));

				options.setPut(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), FormRecordImpl.class);
	}

	protected Http.Response invokePutFormRecordResponse(
				Long formRecordId,FormRecord formRecord)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(formRecord), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/form-records/{form-record-id}", formRecordId));

				options.setPut(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Page<FormRecord> invokeGetFormFormRecordsPage(
				Long formId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}/form-records", formId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<FormRecordImpl>>() {});
	}

	protected Http.Response invokeGetFormFormRecordsPageResponse(
				Long formId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}/form-records", formId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected FormRecord invokePostFormFormRecord(
				Long formId,FormRecord formRecord)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(formRecord), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}/form-records", formId));

				options.setPost(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), FormRecordImpl.class);
	}

	protected Http.Response invokePostFormFormRecordResponse(
				Long formId,FormRecord formRecord)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(formRecord), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}/form-records", formId));

				options.setPost(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}

	protected void assertEquals(FormRecord formRecord1, FormRecord formRecord2) {
		Assert.assertTrue(formRecord1 + " does not equal " + formRecord2, equals(formRecord1, formRecord2));
	}

	protected void assertEquals(List<FormRecord> formRecords1, List<FormRecord> formRecords2) {
		Assert.assertEquals(formRecords1.size(), formRecords2.size());

		for (int i = 0; i < formRecords1.size(); i++) {
			FormRecord formRecord1 = formRecords1.get(i);
			FormRecord formRecord2 = formRecords2.get(i);

			assertEquals(formRecord1, formRecord2);
	}
	}

	protected boolean equals(FormRecord formRecord1, FormRecord formRecord2) {
		if (formRecord1 == formRecord2) {
			return true;
	}

		return false;
	}

	protected FormRecord randomFormRecord() {
		return new FormRecordImpl() {
			{

						dateCreated = RandomTestUtil.nextDate();
						dateModified = RandomTestUtil.nextDate();
						datePublished = RandomTestUtil.nextDate();
						draft = RandomTestUtil.randomBoolean();
						formId = RandomTestUtil.randomLong();
						id = RandomTestUtil.randomLong();
	}
		};
	}

	protected Group testGroup;

	protected static class FormRecordImpl implements FormRecord {

	public Creator getCreator() {
				return creator;
	}

	public void setCreator(Creator creator) {
				this.creator = creator;
	}

	@JsonIgnore
	public void setCreator(
				UnsafeSupplier<Creator, Throwable> creatorUnsafeSupplier) {

				try {
					creator = creatorUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Creator creator;
	public Date getDateCreated() {
				return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
				this.dateCreated = dateCreated;
	}

	@JsonIgnore
	public void setDateCreated(
				UnsafeSupplier<Date, Throwable> dateCreatedUnsafeSupplier) {

				try {
					dateCreated = dateCreatedUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Date dateCreated;
	public Date getDateModified() {
				return dateModified;
	}

	public void setDateModified(Date dateModified) {
				this.dateModified = dateModified;
	}

	@JsonIgnore
	public void setDateModified(
				UnsafeSupplier<Date, Throwable> dateModifiedUnsafeSupplier) {

				try {
					dateModified = dateModifiedUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Date dateModified;
	public Date getDatePublished() {
				return datePublished;
	}

	public void setDatePublished(Date datePublished) {
				this.datePublished = datePublished;
	}

	@JsonIgnore
	public void setDatePublished(
				UnsafeSupplier<Date, Throwable> datePublishedUnsafeSupplier) {

				try {
					datePublished = datePublishedUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Date datePublished;
	public Boolean getDraft() {
				return draft;
	}

	public void setDraft(Boolean draft) {
				this.draft = draft;
	}

	@JsonIgnore
	public void setDraft(
				UnsafeSupplier<Boolean, Throwable> draftUnsafeSupplier) {

				try {
					draft = draftUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Boolean draft;
	public FieldValues[] getFieldValues() {
				return fieldValues;
	}

	public void setFieldValues(FieldValues[] fieldValues) {
				this.fieldValues = fieldValues;
	}

	@JsonIgnore
	public void setFieldValues(
				UnsafeSupplier<FieldValues[], Throwable> fieldValuesUnsafeSupplier) {

				try {
					fieldValues = fieldValuesUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected FieldValues[] fieldValues;
	public Form getForm() {
				return form;
	}

	public void setForm(Form form) {
				this.form = form;
	}

	@JsonIgnore
	public void setForm(
				UnsafeSupplier<Form, Throwable> formUnsafeSupplier) {

				try {
					form = formUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Form form;
	public Long getFormId() {
				return formId;
	}

	public void setFormId(Long formId) {
				this.formId = formId;
	}

	@JsonIgnore
	public void setFormId(
				UnsafeSupplier<Long, Throwable> formIdUnsafeSupplier) {

				try {
					formId = formIdUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long formId;
	public Long getId() {
				return id;
	}

	public void setId(Long id) {
				this.id = id;
	}

	@JsonIgnore
	public void setId(
				UnsafeSupplier<Long, Throwable> idUnsafeSupplier) {

				try {
					id = idUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long id;

	public String toString() {
			StringBundler sb = new StringBundler();

			sb.append("{");

					sb.append("creator=");

				sb.append(creator);
					sb.append(", dateCreated=");

				sb.append(dateCreated);
					sb.append(", dateModified=");

				sb.append(dateModified);
					sb.append(", datePublished=");

				sb.append(datePublished);
					sb.append(", draft=");

				sb.append(draft);
					sb.append(", fieldValues=");

				sb.append(fieldValues);
					sb.append(", form=");

				sb.append(form);
					sb.append(", formId=");

				sb.append(formId);
					sb.append(", id=");

				sb.append(id);

			sb.append("}");

			return sb.toString();
	}

	}

	protected static class Page<T> {

	public Collection<T> getItems() {
			return new ArrayList<>(items);
	}

	public int getItemsPerPage() {
			return itemsPerPage;
	}

	public int getLastPageNumber() {
			return lastPageNumber;
	}

	public int getPageNumber() {
			return pageNumber;
	}

	public int getTotalCount() {
			return totalCount;
	}

	@JsonProperty
	protected Collection<T> items;

	@JsonProperty
	protected int itemsPerPage;

	@JsonProperty
	protected int lastPageNumber;

	@JsonProperty
	protected int pageNumber;

	@JsonProperty
	protected int totalCount;

	}

	private Http.Options _createHttpOptions() {
		Http.Options options = new Http.Options();

		options.addHeader("Accept", "application/json");

		String userNameAndPassword = "test@liferay.com:test";

		String encodedUserNameAndPassword = Base64.encode(userNameAndPassword.getBytes());

		options.addHeader("Authorization", "Basic " + encodedUserNameAndPassword);

		options.addHeader("Content-Type", "application/json");

		return options;
	}

	private String _toPath(String template, Object value) {
		return template.replaceFirst("\\{.*\\}", String.valueOf(value));
	}

	private final static ObjectMapper _inputObjectMapper = new ObjectMapper() {
		{
			setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}
	};
	private final static ObjectMapper _outputObjectMapper = new ObjectMapper();

	private URL _resourceURL;

}