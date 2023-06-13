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
import com.liferay.headless.form.dto.v1_0.Form;
import com.liferay.headless.form.dto.v1_0.FormRecord;
import com.liferay.headless.form.dto.v1_0.FormStructure;
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
public abstract class BaseFormResourceTestCase {

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
	public void testGetContentSpaceFormsPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetForm() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPostFormEvaluateContext() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetFormFetchLatestDraft() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPostFormUploadFile() throws Exception {
			Assert.assertTrue(true);
	}

	protected void assertResponseCode(int expectedResponseCode, Http.Response actualResponse) {
		Assert.assertEquals(expectedResponseCode, actualResponse.getResponseCode());
	}

	protected Page<Form> invokeGetContentSpaceFormsPage(
				Long contentSpaceId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/content-spaces/{content-space-id}/form", contentSpaceId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<FormImpl>>() {});
	}

	protected Http.Response invokeGetContentSpaceFormsPageResponse(
				Long contentSpaceId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/content-spaces/{content-space-id}/form", contentSpaceId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Form invokeGetForm(
				Long formId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}", formId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), FormImpl.class);
	}

	protected Http.Response invokeGetFormResponse(
				Long formId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}", formId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Form invokePostFormEvaluateContext(
				Long formId,Form form)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(form), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}/evaluate-context", formId));

				options.setPost(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), FormImpl.class);
	}

	protected Http.Response invokePostFormEvaluateContextResponse(
				Long formId,Form form)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(form), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}/evaluate-context", formId));

				options.setPost(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Form invokeGetFormFetchLatestDraft(
				Long formId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}/fetch-latest-draft", formId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), FormImpl.class);
	}

	protected Http.Response invokeGetFormFetchLatestDraftResponse(
				Long formId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}/fetch-latest-draft", formId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Form invokePostFormUploadFile(
				Long formId,Form form)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(form), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}/upload-file", formId));

				options.setPost(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), FormImpl.class);
	}

	protected Http.Response invokePostFormUploadFileResponse(
				Long formId,Form form)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(form), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/forms/{form-id}/upload-file", formId));

				options.setPost(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}

	protected void assertEquals(Form form1, Form form2) {
		Assert.assertTrue(form1 + " does not equal " + form2, equals(form1, form2));
	}

	protected void assertEquals(List<Form> forms1, List<Form> forms2) {
		Assert.assertEquals(forms1.size(), forms2.size());

		for (int i = 0; i < forms1.size(); i++) {
			Form form1 = forms1.get(i);
			Form form2 = forms2.get(i);

			assertEquals(form1, form2);
	}
	}

	protected boolean equals(Form form1, Form form2) {
		if (form1 == form2) {
			return true;
	}

		return false;
	}

	protected Form randomForm() {
		return new FormImpl() {
			{

						contentSpace = RandomTestUtil.randomLong();
						dateCreated = RandomTestUtil.nextDate();
						dateModified = RandomTestUtil.nextDate();
						datePublished = RandomTestUtil.nextDate();
						defaultLanguage = RandomTestUtil.randomString();
						description = RandomTestUtil.randomString();
						id = RandomTestUtil.randomLong();
						name = RandomTestUtil.randomString();
						structureId = RandomTestUtil.randomLong();
	}
		};
	}

	protected Group testGroup;

	protected static class FormImpl implements Form {

	public String[] getAvailableLanguages() {
				return availableLanguages;
	}

	public void setAvailableLanguages(String[] availableLanguages) {
				this.availableLanguages = availableLanguages;
	}

	@JsonIgnore
	public void setAvailableLanguages(
				UnsafeSupplier<String[], Throwable> availableLanguagesUnsafeSupplier) {

				try {
					availableLanguages = availableLanguagesUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String[] availableLanguages;
	public Long getContentSpace() {
				return contentSpace;
	}

	public void setContentSpace(Long contentSpace) {
				this.contentSpace = contentSpace;
	}

	@JsonIgnore
	public void setContentSpace(
				UnsafeSupplier<Long, Throwable> contentSpaceUnsafeSupplier) {

				try {
					contentSpace = contentSpaceUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long contentSpace;
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
	public String getDefaultLanguage() {
				return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
				this.defaultLanguage = defaultLanguage;
	}

	@JsonIgnore
	public void setDefaultLanguage(
				UnsafeSupplier<String, Throwable> defaultLanguageUnsafeSupplier) {

				try {
					defaultLanguage = defaultLanguageUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String defaultLanguage;
	public String getDescription() {
				return description;
	}

	public void setDescription(String description) {
				this.description = description;
	}

	@JsonIgnore
	public void setDescription(
				UnsafeSupplier<String, Throwable> descriptionUnsafeSupplier) {

				try {
					description = descriptionUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String description;
	public FormRecord[] getFormRecords() {
				return formRecords;
	}

	public void setFormRecords(FormRecord[] formRecords) {
				this.formRecords = formRecords;
	}

	@JsonIgnore
	public void setFormRecords(
				UnsafeSupplier<FormRecord[], Throwable> formRecordsUnsafeSupplier) {

				try {
					formRecords = formRecordsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected FormRecord[] formRecords;
	public Long[] getFormRecordsIds() {
				return formRecordsIds;
	}

	public void setFormRecordsIds(Long[] formRecordsIds) {
				this.formRecordsIds = formRecordsIds;
	}

	@JsonIgnore
	public void setFormRecordsIds(
				UnsafeSupplier<Long[], Throwable> formRecordsIdsUnsafeSupplier) {

				try {
					formRecordsIds = formRecordsIdsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long[] formRecordsIds;
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
	public String getName() {
				return name;
	}

	public void setName(String name) {
				this.name = name;
	}

	@JsonIgnore
	public void setName(
				UnsafeSupplier<String, Throwable> nameUnsafeSupplier) {

				try {
					name = nameUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String name;
	public FormStructure getStructure() {
				return structure;
	}

	public void setStructure(FormStructure structure) {
				this.structure = structure;
	}

	@JsonIgnore
	public void setStructure(
				UnsafeSupplier<FormStructure, Throwable> structureUnsafeSupplier) {

				try {
					structure = structureUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected FormStructure structure;
	public Long getStructureId() {
				return structureId;
	}

	public void setStructureId(Long structureId) {
				this.structureId = structureId;
	}

	@JsonIgnore
	public void setStructureId(
				UnsafeSupplier<Long, Throwable> structureIdUnsafeSupplier) {

				try {
					structureId = structureIdUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long structureId;

	public String toString() {
			StringBundler sb = new StringBundler();

			sb.append("{");

					sb.append("availableLanguages=");

				sb.append(availableLanguages);
					sb.append(", contentSpace=");

				sb.append(contentSpace);
					sb.append(", creator=");

				sb.append(creator);
					sb.append(", dateCreated=");

				sb.append(dateCreated);
					sb.append(", dateModified=");

				sb.append(dateModified);
					sb.append(", datePublished=");

				sb.append(datePublished);
					sb.append(", defaultLanguage=");

				sb.append(defaultLanguage);
					sb.append(", description=");

				sb.append(description);
					sb.append(", formRecords=");

				sb.append(formRecords);
					sb.append(", formRecordsIds=");

				sb.append(formRecordsIds);
					sb.append(", id=");

				sb.append(id);
					sb.append(", name=");

				sb.append(name);
					sb.append(", structure=");

				sb.append(structure);
					sb.append(", structureId=");

				sb.append(structureId);

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