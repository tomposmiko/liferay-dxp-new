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

package com.liferay.portal.search.tuning.synonyms.web.internal.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.search.tuning.synonyms.index.name.SynonymSetIndexName;
import com.liferay.portal.search.tuning.synonyms.web.internal.BaseSynonymsWebTestCase;
import com.liferay.portal.search.tuning.synonyms.web.internal.index.SynonymSet;
import com.liferay.portal.search.tuning.synonyms.web.internal.synchronizer.IndexToFilterSynchronizer;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Wade Cao
 */
public class EditSynonymSetsMVCActionCommandTest
	extends BaseSynonymsWebTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_editSynonymSetsMVCActionCommand =
			new EditSynonymSetsMVCActionCommand();

		ReflectionTestUtil.setFieldValue(
			_editSynonymSetsMVCActionCommand, "_indexNameBuilder",
			_indexNameBuilder);
		ReflectionTestUtil.setFieldValue(
			_editSynonymSetsMVCActionCommand, "portal", portal);
		ReflectionTestUtil.setFieldValue(
			_editSynonymSetsMVCActionCommand, "_indexToFilterSynchronizer",
			_indexToFilterSynchronizer);
		ReflectionTestUtil.setFieldValue(
			_editSynonymSetsMVCActionCommand, "_synonymSetIndexNameBuilder",
			synonymSetIndexNameBuilder);
		ReflectionTestUtil.setFieldValue(
			_editSynonymSetsMVCActionCommand, "_synonymSetIndexReader",
			synonymSetIndexReader);
		ReflectionTestUtil.setFieldValue(
			_editSynonymSetsMVCActionCommand, "_synonymSetStorageAdapter",
			synonymSetStorageAdapter);
	}

	@Test
	public void testDoProcessAction() throws Exception {
		setUpPortal(_httpServletRequest);
		setUpPortalUtil();
		setUpPortletRequest(_actionRequest);

		_editSynonymSetsMVCActionCommand.doProcessAction(
			_actionRequest, _actionResponse);

		Mockito.verify(
			_actionRequest, Mockito.times(3)
		).getAttribute(
			Mockito.anyString()
		);
	}

	@Test
	public void testGetSynonymSet() {
		Assert.assertNull(
			_editSynonymSetsMVCActionCommand.getSynonymSet(
				Mockito.mock(SynonymSetIndexName.class), _actionRequest));

		setUpPortletRequestParameterValue(
			_actionRequest, "synonymSetId", "synonymSetIdValue");
		setUpSynonymSetIndexReader("id", "car,automobile");

		SynonymSet synonymSet = _editSynonymSetsMVCActionCommand.getSynonymSet(
			Mockito.mock(SynonymSetIndexName.class), _actionRequest);

		Assert.assertEquals("car,automobile", synonymSet.getSynonyms());
		Assert.assertEquals("id", synonymSet.getSynonymSetDocumentId());
	}

	@Test
	public void testUpdateSynonymSet() throws Exception {
		_editSynonymSetsMVCActionCommand.updateSynonymSet(_actionRequest);

		Mockito.verify(
			_indexToFilterSynchronizer, Mockito.times(1)
		).copyToFilter(
			Mockito.any(), Mockito.nullable(String.class), Mockito.anyBoolean()
		);
	}

	@Test
	public void testUpdateSynonymSetIndex() throws PortalException {
		_editSynonymSetsMVCActionCommand.updateSynonymSetIndex(
			Mockito.mock(SynonymSetIndexName.class), "car,automobile", null);

		Mockito.verify(
			synonymSetStorageAdapter, Mockito.times(1)
		).create(
			Mockito.any(), Mockito.any()
		);

		SynonymSet.SynonymSetBuilder synonymSetBuilder =
			new SynonymSet.SynonymSetBuilder();

		_editSynonymSetsMVCActionCommand.updateSynonymSetIndex(
			Mockito.mock(SynonymSetIndexName.class), "car,automobile",
			synonymSetBuilder.synonyms(
				"car,atumobile"
			).synonymSetDocumentId(
				"id-1"
			).build());

		Mockito.verify(
			synonymSetStorageAdapter, Mockito.times(1)
		).update(
			Mockito.any(), Mockito.any()
		);
	}

	private final ActionRequest _actionRequest = Mockito.mock(
		ActionRequest.class);
	private final ActionResponse _actionResponse = Mockito.mock(
		ActionResponse.class);
	private EditSynonymSetsMVCActionCommand _editSynonymSetsMVCActionCommand;
	private final HttpServletRequest _httpServletRequest = Mockito.mock(
		HttpServletRequest.class);
	private final IndexNameBuilder _indexNameBuilder = Mockito.mock(
		IndexNameBuilder.class);
	private final IndexToFilterSynchronizer _indexToFilterSynchronizer =
		Mockito.mock(IndexToFilterSynchronizer.class);

}