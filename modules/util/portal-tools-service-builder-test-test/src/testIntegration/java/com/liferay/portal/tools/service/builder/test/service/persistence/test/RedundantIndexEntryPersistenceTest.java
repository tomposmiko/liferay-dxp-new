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

package com.liferay.portal.tools.service.builder.test.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;
import com.liferay.portal.tools.service.builder.test.exception.NoSuchRedundantIndexEntryException;
import com.liferay.portal.tools.service.builder.test.model.RedundantIndexEntry;
import com.liferay.portal.tools.service.builder.test.service.RedundantIndexEntryLocalServiceUtil;
import com.liferay.portal.tools.service.builder.test.service.persistence.RedundantIndexEntryPersistence;
import com.liferay.portal.tools.service.builder.test.service.persistence.RedundantIndexEntryUtil;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @generated
 */
@RunWith(Arquillian.class)
public class RedundantIndexEntryPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED,
				"com.liferay.portal.tools.service.builder.test.service"));

	@Before
	public void setUp() {
		_persistence = RedundantIndexEntryUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<RedundantIndexEntry> iterator =
			_redundantIndexEntries.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		RedundantIndexEntry redundantIndexEntry = _persistence.create(pk);

		Assert.assertNotNull(redundantIndexEntry);

		Assert.assertEquals(redundantIndexEntry.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		RedundantIndexEntry newRedundantIndexEntry = addRedundantIndexEntry();

		_persistence.remove(newRedundantIndexEntry);

		RedundantIndexEntry existingRedundantIndexEntry =
			_persistence.fetchByPrimaryKey(
				newRedundantIndexEntry.getPrimaryKey());

		Assert.assertNull(existingRedundantIndexEntry);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addRedundantIndexEntry();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		RedundantIndexEntry newRedundantIndexEntry = _persistence.create(pk);

		newRedundantIndexEntry.setCompanyId(RandomTestUtil.nextLong());

		newRedundantIndexEntry.setName(RandomTestUtil.randomString());

		_redundantIndexEntries.add(_persistence.update(newRedundantIndexEntry));

		RedundantIndexEntry existingRedundantIndexEntry =
			_persistence.findByPrimaryKey(
				newRedundantIndexEntry.getPrimaryKey());

		Assert.assertEquals(
			existingRedundantIndexEntry.getRedundantIndexEntryId(),
			newRedundantIndexEntry.getRedundantIndexEntryId());
		Assert.assertEquals(
			existingRedundantIndexEntry.getCompanyId(),
			newRedundantIndexEntry.getCompanyId());
		Assert.assertEquals(
			existingRedundantIndexEntry.getName(),
			newRedundantIndexEntry.getName());
	}

	@Test
	public void testCountByC_N() throws Exception {
		_persistence.countByC_N(RandomTestUtil.nextLong(), "");

		_persistence.countByC_N(0L, "null");

		_persistence.countByC_N(0L, (String)null);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		RedundantIndexEntry newRedundantIndexEntry = addRedundantIndexEntry();

		RedundantIndexEntry existingRedundantIndexEntry =
			_persistence.findByPrimaryKey(
				newRedundantIndexEntry.getPrimaryKey());

		Assert.assertEquals(
			existingRedundantIndexEntry, newRedundantIndexEntry);
	}

	@Test(expected = NoSuchRedundantIndexEntryException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<RedundantIndexEntry> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"RedundantIndexEntry", "redundantIndexEntryId", true, "companyId",
			true, "name", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		RedundantIndexEntry newRedundantIndexEntry = addRedundantIndexEntry();

		RedundantIndexEntry existingRedundantIndexEntry =
			_persistence.fetchByPrimaryKey(
				newRedundantIndexEntry.getPrimaryKey());

		Assert.assertEquals(
			existingRedundantIndexEntry, newRedundantIndexEntry);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		RedundantIndexEntry missingRedundantIndexEntry =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingRedundantIndexEntry);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		RedundantIndexEntry newRedundantIndexEntry1 = addRedundantIndexEntry();
		RedundantIndexEntry newRedundantIndexEntry2 = addRedundantIndexEntry();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newRedundantIndexEntry1.getPrimaryKey());
		primaryKeys.add(newRedundantIndexEntry2.getPrimaryKey());

		Map<Serializable, RedundantIndexEntry> redundantIndexEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, redundantIndexEntries.size());
		Assert.assertEquals(
			newRedundantIndexEntry1,
			redundantIndexEntries.get(newRedundantIndexEntry1.getPrimaryKey()));
		Assert.assertEquals(
			newRedundantIndexEntry2,
			redundantIndexEntries.get(newRedundantIndexEntry2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, RedundantIndexEntry> redundantIndexEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(redundantIndexEntries.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		RedundantIndexEntry newRedundantIndexEntry = addRedundantIndexEntry();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newRedundantIndexEntry.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, RedundantIndexEntry> redundantIndexEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, redundantIndexEntries.size());
		Assert.assertEquals(
			newRedundantIndexEntry,
			redundantIndexEntries.get(newRedundantIndexEntry.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, RedundantIndexEntry> redundantIndexEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(redundantIndexEntries.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		RedundantIndexEntry newRedundantIndexEntry = addRedundantIndexEntry();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newRedundantIndexEntry.getPrimaryKey());

		Map<Serializable, RedundantIndexEntry> redundantIndexEntries =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, redundantIndexEntries.size());
		Assert.assertEquals(
			newRedundantIndexEntry,
			redundantIndexEntries.get(newRedundantIndexEntry.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			RedundantIndexEntryLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<RedundantIndexEntry>() {

				@Override
				public void performAction(
					RedundantIndexEntry redundantIndexEntry) {

					Assert.assertNotNull(redundantIndexEntry);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		RedundantIndexEntry newRedundantIndexEntry = addRedundantIndexEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			RedundantIndexEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"redundantIndexEntryId",
				newRedundantIndexEntry.getRedundantIndexEntryId()));

		List<RedundantIndexEntry> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		RedundantIndexEntry existingRedundantIndexEntry = result.get(0);

		Assert.assertEquals(
			existingRedundantIndexEntry, newRedundantIndexEntry);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			RedundantIndexEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"redundantIndexEntryId", RandomTestUtil.nextLong()));

		List<RedundantIndexEntry> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		RedundantIndexEntry newRedundantIndexEntry = addRedundantIndexEntry();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			RedundantIndexEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("redundantIndexEntryId"));

		Object newRedundantIndexEntryId =
			newRedundantIndexEntry.getRedundantIndexEntryId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"redundantIndexEntryId",
				new Object[] {newRedundantIndexEntryId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingRedundantIndexEntryId = result.get(0);

		Assert.assertEquals(
			existingRedundantIndexEntryId, newRedundantIndexEntryId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			RedundantIndexEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("redundantIndexEntryId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"redundantIndexEntryId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		RedundantIndexEntry newRedundantIndexEntry = addRedundantIndexEntry();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(
				newRedundantIndexEntry.getPrimaryKey()));
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromDatabase()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(true);
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromSession()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(false);
	}

	private void _testResetOriginalValuesWithDynamicQuery(boolean clearSession)
		throws Exception {

		RedundantIndexEntry newRedundantIndexEntry = addRedundantIndexEntry();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			RedundantIndexEntry.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"redundantIndexEntryId",
				newRedundantIndexEntry.getRedundantIndexEntryId()));

		List<RedundantIndexEntry> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(
		RedundantIndexEntry redundantIndexEntry) {

		Assert.assertEquals(
			Long.valueOf(redundantIndexEntry.getCompanyId()),
			ReflectionTestUtil.<Long>invoke(
				redundantIndexEntry, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "companyId"));
		Assert.assertEquals(
			redundantIndexEntry.getName(),
			ReflectionTestUtil.invoke(
				redundantIndexEntry, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "name"));
	}

	protected RedundantIndexEntry addRedundantIndexEntry() throws Exception {
		long pk = RandomTestUtil.nextLong();

		RedundantIndexEntry redundantIndexEntry = _persistence.create(pk);

		redundantIndexEntry.setCompanyId(RandomTestUtil.nextLong());

		redundantIndexEntry.setName(RandomTestUtil.randomString());

		_redundantIndexEntries.add(_persistence.update(redundantIndexEntry));

		return redundantIndexEntry;
	}

	private List<RedundantIndexEntry> _redundantIndexEntries =
		new ArrayList<RedundantIndexEntry>();
	private RedundantIndexEntryPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}