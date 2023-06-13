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

package com.liferay.adaptive.media.image.internal.configuration.test;

import com.liferay.adaptive.media.image.configuration.AMImageConfigurationEntry;
import com.liferay.adaptive.media.image.configuration.AMImageConfigurationHelper;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Sergio González
 */
@RunWith(Arquillian.class)
public class AMImageEnableConfigurationTest
	extends BaseAMImageConfigurationTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testEnableAllConfigurationEntries() throws Exception {
		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "onedesc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");

		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "two", "twodesc", "2",
			HashMapBuilder.put(
				"max-height", "200"
			).put(
				"max-width", "200"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "2");

		AMImageConfigurationEntry firstAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1");

		assertDisabled(firstAMImageConfigurationEntry);

		AMImageConfigurationEntry secondAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "2");

		assertDisabled(secondAMImageConfigurationEntry);

		_amImageConfigurationHelper.enableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");
		_amImageConfigurationHelper.enableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "2");

		firstAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1");

		assertEnabled(firstAMImageConfigurationEntry);

		secondAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "2");

		assertEnabled(secondAMImageConfigurationEntry);
	}

	@Test
	public void testEnableConfigurationEntryWithExistingDisabledConfiguration()
		throws Exception {

		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "onedesc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");

		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "two", "twodesc", "2",
			HashMapBuilder.put(
				"max-height", "200"
			).put(
				"max-width", "200"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "2");

		_amImageConfigurationHelper.enableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");

		AMImageConfigurationEntry amImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1");

		assertEnabled(amImageConfigurationEntry);

		amImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "2");

		assertDisabled(amImageConfigurationEntry);
	}

	@Test
	public void testEnableEnabledConfigurationEntry() throws Exception {
		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "desc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		AMImageConfigurationEntry amImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1");

		assertEnabled(amImageConfigurationEntry);

		_amImageConfigurationHelper.enableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");

		amImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1");

		assertEnabled(amImageConfigurationEntry);
	}

	@Test
	public void testEnableFirstConfigurationEntry() throws Exception {
		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "onedesc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");

		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "two", "twodesc", "2",
			HashMapBuilder.put(
				"max-height", "200"
			).put(
				"max-width", "200"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "2");

		AMImageConfigurationEntry firstAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1");

		assertDisabled(firstAMImageConfigurationEntry);

		AMImageConfigurationEntry secondAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "2");

		assertDisabled(secondAMImageConfigurationEntry);

		_amImageConfigurationHelper.enableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");

		firstAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1");

		assertEnabled(firstAMImageConfigurationEntry);

		secondAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "2");

		assertDisabled(secondAMImageConfigurationEntry);
	}

	@Test
	public void testEnableNonexistantConfigurationEntry() throws Exception {
		String uuid = RandomTestUtil.randomString();

		_amImageConfigurationHelper.enableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), uuid);

		Assert.assertNull(
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), uuid));
	}

	@Test
	public void testEnableSecondConfigurationEntry() throws Exception {
		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "onedesc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");

		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "two", "twodesc", "2",
			HashMapBuilder.put(
				"max-height", "200"
			).put(
				"max-width", "200"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "2");

		AMImageConfigurationEntry firstAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1");

		assertDisabled(firstAMImageConfigurationEntry);

		AMImageConfigurationEntry secondAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "2");

		assertDisabled(secondAMImageConfigurationEntry);

		_amImageConfigurationHelper.enableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "2");

		firstAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1");

		assertDisabled(firstAMImageConfigurationEntry);

		secondAMImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "2");

		assertEnabled(secondAMImageConfigurationEntry);
	}

	@Test
	public void testEnableUniqueConfigurationEntry() throws Exception {
		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "desc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");

		AMImageConfigurationEntry amImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1");

		assertDisabled(amImageConfigurationEntry);

		_amImageConfigurationHelper.enableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");

		amImageConfigurationEntry =
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1");

		assertEnabled(amImageConfigurationEntry);
	}

	@Override
	protected AMImageConfigurationHelper getAMImageConfigurationHelper() {
		return _amImageConfigurationHelper;
	}

	@Inject
	private AMImageConfigurationHelper _amImageConfigurationHelper;

}