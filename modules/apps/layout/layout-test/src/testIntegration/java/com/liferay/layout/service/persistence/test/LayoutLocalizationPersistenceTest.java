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

package com.liferay.layout.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.layout.exception.NoSuchLayoutLocalizationException;
import com.liferay.layout.model.LayoutLocalization;
import com.liferay.layout.service.LayoutLocalizationLocalServiceUtil;
import com.liferay.layout.service.persistence.LayoutLocalizationPersistence;
import com.liferay.layout.service.persistence.LayoutLocalizationUtil;
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
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;

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
public class LayoutLocalizationPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.layout.service"));

	@Before
	public void setUp() {
		_persistence = LayoutLocalizationUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<LayoutLocalization> iterator = _layoutLocalizations.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		LayoutLocalization layoutLocalization = _persistence.create(pk);

		Assert.assertNotNull(layoutLocalization);

		Assert.assertEquals(layoutLocalization.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		LayoutLocalization newLayoutLocalization = addLayoutLocalization();

		_persistence.remove(newLayoutLocalization);

		LayoutLocalization existingLayoutLocalization =
			_persistence.fetchByPrimaryKey(
				newLayoutLocalization.getPrimaryKey());

		Assert.assertNull(existingLayoutLocalization);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addLayoutLocalization();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		LayoutLocalization newLayoutLocalization = _persistence.create(pk);

		newLayoutLocalization.setMvccVersion(RandomTestUtil.nextLong());

		newLayoutLocalization.setCtCollectionId(RandomTestUtil.nextLong());

		newLayoutLocalization.setUuid(RandomTestUtil.randomString());

		newLayoutLocalization.setGroupId(RandomTestUtil.nextLong());

		newLayoutLocalization.setCompanyId(RandomTestUtil.nextLong());

		newLayoutLocalization.setCreateDate(RandomTestUtil.nextDate());

		newLayoutLocalization.setModifiedDate(RandomTestUtil.nextDate());

		newLayoutLocalization.setContent(RandomTestUtil.randomString());

		newLayoutLocalization.setLanguageId(RandomTestUtil.randomString());

		newLayoutLocalization.setPlid(RandomTestUtil.nextLong());

		newLayoutLocalization.setLastPublishDate(RandomTestUtil.nextDate());

		_layoutLocalizations.add(_persistence.update(newLayoutLocalization));

		LayoutLocalization existingLayoutLocalization =
			_persistence.findByPrimaryKey(
				newLayoutLocalization.getPrimaryKey());

		Assert.assertEquals(
			existingLayoutLocalization.getMvccVersion(),
			newLayoutLocalization.getMvccVersion());
		Assert.assertEquals(
			existingLayoutLocalization.getCtCollectionId(),
			newLayoutLocalization.getCtCollectionId());
		Assert.assertEquals(
			existingLayoutLocalization.getUuid(),
			newLayoutLocalization.getUuid());
		Assert.assertEquals(
			existingLayoutLocalization.getLayoutLocalizationId(),
			newLayoutLocalization.getLayoutLocalizationId());
		Assert.assertEquals(
			existingLayoutLocalization.getGroupId(),
			newLayoutLocalization.getGroupId());
		Assert.assertEquals(
			existingLayoutLocalization.getCompanyId(),
			newLayoutLocalization.getCompanyId());
		Assert.assertEquals(
			Time.getShortTimestamp(existingLayoutLocalization.getCreateDate()),
			Time.getShortTimestamp(newLayoutLocalization.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingLayoutLocalization.getModifiedDate()),
			Time.getShortTimestamp(newLayoutLocalization.getModifiedDate()));
		Assert.assertEquals(
			existingLayoutLocalization.getContent(),
			newLayoutLocalization.getContent());
		Assert.assertEquals(
			existingLayoutLocalization.getLanguageId(),
			newLayoutLocalization.getLanguageId());
		Assert.assertEquals(
			existingLayoutLocalization.getPlid(),
			newLayoutLocalization.getPlid());
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingLayoutLocalization.getLastPublishDate()),
			Time.getShortTimestamp(newLayoutLocalization.getLastPublishDate()));
	}

	@Test
	public void testCountByUuid() throws Exception {
		_persistence.countByUuid("");

		_persistence.countByUuid("null");

		_persistence.countByUuid((String)null);
	}

	@Test
	public void testCountByUUID_G() throws Exception {
		_persistence.countByUUID_G("", RandomTestUtil.nextLong());

		_persistence.countByUUID_G("null", 0L);

		_persistence.countByUUID_G((String)null, 0L);
	}

	@Test
	public void testCountByUuid_C() throws Exception {
		_persistence.countByUuid_C("", RandomTestUtil.nextLong());

		_persistence.countByUuid_C("null", 0L);

		_persistence.countByUuid_C((String)null, 0L);
	}

	@Test
	public void testCountByPlid() throws Exception {
		_persistence.countByPlid(RandomTestUtil.nextLong());

		_persistence.countByPlid(0L);
	}

	@Test
	public void testCountByL_P() throws Exception {
		_persistence.countByL_P("", RandomTestUtil.nextLong());

		_persistence.countByL_P("null", 0L);

		_persistence.countByL_P((String)null, 0L);
	}

	@Test
	public void testCountByG_L_P() throws Exception {
		_persistence.countByG_L_P(
			RandomTestUtil.nextLong(), "", RandomTestUtil.nextLong());

		_persistence.countByG_L_P(0L, "null", 0L);

		_persistence.countByG_L_P(0L, (String)null, 0L);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		LayoutLocalization newLayoutLocalization = addLayoutLocalization();

		LayoutLocalization existingLayoutLocalization =
			_persistence.findByPrimaryKey(
				newLayoutLocalization.getPrimaryKey());

		Assert.assertEquals(existingLayoutLocalization, newLayoutLocalization);
	}

	@Test(expected = NoSuchLayoutLocalizationException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<LayoutLocalization> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"LayoutLocalization", "mvccVersion", true, "ctCollectionId", true,
			"uuid", true, "layoutLocalizationId", true, "groupId", true,
			"companyId", true, "createDate", true, "modifiedDate", true,
			"languageId", true, "plid", true, "lastPublishDate", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		LayoutLocalization newLayoutLocalization = addLayoutLocalization();

		LayoutLocalization existingLayoutLocalization =
			_persistence.fetchByPrimaryKey(
				newLayoutLocalization.getPrimaryKey());

		Assert.assertEquals(existingLayoutLocalization, newLayoutLocalization);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		LayoutLocalization missingLayoutLocalization =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingLayoutLocalization);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		LayoutLocalization newLayoutLocalization1 = addLayoutLocalization();
		LayoutLocalization newLayoutLocalization2 = addLayoutLocalization();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newLayoutLocalization1.getPrimaryKey());
		primaryKeys.add(newLayoutLocalization2.getPrimaryKey());

		Map<Serializable, LayoutLocalization> layoutLocalizations =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, layoutLocalizations.size());
		Assert.assertEquals(
			newLayoutLocalization1,
			layoutLocalizations.get(newLayoutLocalization1.getPrimaryKey()));
		Assert.assertEquals(
			newLayoutLocalization2,
			layoutLocalizations.get(newLayoutLocalization2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, LayoutLocalization> layoutLocalizations =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(layoutLocalizations.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		LayoutLocalization newLayoutLocalization = addLayoutLocalization();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newLayoutLocalization.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, LayoutLocalization> layoutLocalizations =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, layoutLocalizations.size());
		Assert.assertEquals(
			newLayoutLocalization,
			layoutLocalizations.get(newLayoutLocalization.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, LayoutLocalization> layoutLocalizations =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(layoutLocalizations.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		LayoutLocalization newLayoutLocalization = addLayoutLocalization();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newLayoutLocalization.getPrimaryKey());

		Map<Serializable, LayoutLocalization> layoutLocalizations =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, layoutLocalizations.size());
		Assert.assertEquals(
			newLayoutLocalization,
			layoutLocalizations.get(newLayoutLocalization.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			LayoutLocalizationLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<LayoutLocalization>() {

				@Override
				public void performAction(
					LayoutLocalization layoutLocalization) {

					Assert.assertNotNull(layoutLocalization);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		LayoutLocalization newLayoutLocalization = addLayoutLocalization();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			LayoutLocalization.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"layoutLocalizationId",
				newLayoutLocalization.getLayoutLocalizationId()));

		List<LayoutLocalization> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		LayoutLocalization existingLayoutLocalization = result.get(0);

		Assert.assertEquals(existingLayoutLocalization, newLayoutLocalization);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			LayoutLocalization.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"layoutLocalizationId", RandomTestUtil.nextLong()));

		List<LayoutLocalization> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		LayoutLocalization newLayoutLocalization = addLayoutLocalization();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			LayoutLocalization.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("layoutLocalizationId"));

		Object newLayoutLocalizationId =
			newLayoutLocalization.getLayoutLocalizationId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"layoutLocalizationId",
				new Object[] {newLayoutLocalizationId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingLayoutLocalizationId = result.get(0);

		Assert.assertEquals(
			existingLayoutLocalizationId, newLayoutLocalizationId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			LayoutLocalization.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("layoutLocalizationId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"layoutLocalizationId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		LayoutLocalization newLayoutLocalization = addLayoutLocalization();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(
				newLayoutLocalization.getPrimaryKey()));
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

		LayoutLocalization newLayoutLocalization = addLayoutLocalization();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			LayoutLocalization.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"layoutLocalizationId",
				newLayoutLocalization.getLayoutLocalizationId()));

		List<LayoutLocalization> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(LayoutLocalization layoutLocalization) {
		Assert.assertEquals(
			layoutLocalization.getUuid(),
			ReflectionTestUtil.invoke(
				layoutLocalization, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "uuid_"));
		Assert.assertEquals(
			Long.valueOf(layoutLocalization.getGroupId()),
			ReflectionTestUtil.<Long>invoke(
				layoutLocalization, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "groupId"));

		Assert.assertEquals(
			layoutLocalization.getLanguageId(),
			ReflectionTestUtil.invoke(
				layoutLocalization, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "languageId"));
		Assert.assertEquals(
			Long.valueOf(layoutLocalization.getPlid()),
			ReflectionTestUtil.<Long>invoke(
				layoutLocalization, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "plid"));

		Assert.assertEquals(
			Long.valueOf(layoutLocalization.getGroupId()),
			ReflectionTestUtil.<Long>invoke(
				layoutLocalization, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "groupId"));
		Assert.assertEquals(
			layoutLocalization.getLanguageId(),
			ReflectionTestUtil.invoke(
				layoutLocalization, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "languageId"));
		Assert.assertEquals(
			Long.valueOf(layoutLocalization.getPlid()),
			ReflectionTestUtil.<Long>invoke(
				layoutLocalization, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "plid"));
	}

	protected LayoutLocalization addLayoutLocalization() throws Exception {
		long pk = RandomTestUtil.nextLong();

		LayoutLocalization layoutLocalization = _persistence.create(pk);

		layoutLocalization.setMvccVersion(RandomTestUtil.nextLong());

		layoutLocalization.setCtCollectionId(RandomTestUtil.nextLong());

		layoutLocalization.setUuid(RandomTestUtil.randomString());

		layoutLocalization.setGroupId(RandomTestUtil.nextLong());

		layoutLocalization.setCompanyId(RandomTestUtil.nextLong());

		layoutLocalization.setCreateDate(RandomTestUtil.nextDate());

		layoutLocalization.setModifiedDate(RandomTestUtil.nextDate());

		layoutLocalization.setContent(RandomTestUtil.randomString());

		layoutLocalization.setLanguageId(RandomTestUtil.randomString());

		layoutLocalization.setPlid(RandomTestUtil.nextLong());

		layoutLocalization.setLastPublishDate(RandomTestUtil.nextDate());

		_layoutLocalizations.add(_persistence.update(layoutLocalization));

		return layoutLocalization;
	}

	private List<LayoutLocalization> _layoutLocalizations =
		new ArrayList<LayoutLocalization>();
	private LayoutLocalizationPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}