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

package com.liferay.expando.exportimport.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.adapter.StagedExpandoColumn;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.exportimport.staged.model.repository.StagedModelRepository;
import com.liferay.exportimport.staged.model.repository.StagedModelRepositoryRegistryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.adapter.ModelAdapterUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portlet.expando.util.test.ExpandoTestUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Balázs Sáfrány-Kovalik
 */
@RunWith(Arquillian.class)
public class StagedExpandoColumnStagedModelRepositoryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_expandoTable = ExpandoTestUtil.addTable(
			PortalUtil.getClassNameId(User.class), "CUSTOM_FIELDS");

		_stagedModelRepository =
			(StagedModelRepository<StagedExpandoColumn>)
				StagedModelRepositoryRegistryUtil.getStagedModelRepository(
					StagedExpandoColumn.class.getName());
	}

	@Test
	public void testExportImportKeepsCheckboxValues() throws Exception {
		ExpandoColumn originalExpandoColumn =
			_expandoColumnLocalService.addColumn(
				_expandoTable.getTableId(), RandomTestUtil.randomString(),
				ExpandoColumnConstants.STRING, RandomTestUtil.randomString());

		StagedExpandoColumn stagedExpandoColumn = ModelAdapterUtil.adapt(
			originalExpandoColumn, ExpandoColumn.class,
			StagedExpandoColumn.class);

		_expandoColumnLocalService.deleteColumn(
			originalExpandoColumn.getColumnId());

		StagedExpandoColumn importedStagedExpandoColumn =
			_stagedModelRepository.addStagedModel(null, stagedExpandoColumn);

		ExpandoColumn importedExpandoColumn =
			_expandoColumnLocalService.getExpandoColumn(
				importedStagedExpandoColumn.getColumnId());

		Assert.assertEquals(
			originalExpandoColumn.getDefaultData(),
			importedExpandoColumn.getDefaultData());
	}

	@Inject
	private ExpandoColumnLocalService _expandoColumnLocalService;

	@DeleteAfterTestRun
	private ExpandoTable _expandoTable;

	private StagedModelRepository<StagedExpandoColumn> _stagedModelRepository;

}