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

package com.liferay.message.boards.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.message.boards.exception.NoSuchSuspiciousActivityException;
import com.liferay.message.boards.model.MBSuspiciousActivity;
import com.liferay.message.boards.service.MBSuspiciousActivityLocalServiceUtil;
import com.liferay.message.boards.service.persistence.MBSuspiciousActivityPersistence;
import com.liferay.message.boards.service.persistence.MBSuspiciousActivityUtil;
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
public class MBSuspiciousActivityPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.message.boards.service"));

	@Before
	public void setUp() {
		_persistence = MBSuspiciousActivityUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<MBSuspiciousActivity> iterator =
			_mbSuspiciousActivities.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		MBSuspiciousActivity mbSuspiciousActivity = _persistence.create(pk);

		Assert.assertNotNull(mbSuspiciousActivity);

		Assert.assertEquals(mbSuspiciousActivity.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		MBSuspiciousActivity newMBSuspiciousActivity =
			addMBSuspiciousActivity();

		_persistence.remove(newMBSuspiciousActivity);

		MBSuspiciousActivity existingMBSuspiciousActivity =
			_persistence.fetchByPrimaryKey(
				newMBSuspiciousActivity.getPrimaryKey());

		Assert.assertNull(existingMBSuspiciousActivity);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addMBSuspiciousActivity();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		MBSuspiciousActivity newMBSuspiciousActivity = _persistence.create(pk);

		newMBSuspiciousActivity.setMvccVersion(RandomTestUtil.nextLong());

		newMBSuspiciousActivity.setCtCollectionId(RandomTestUtil.nextLong());

		newMBSuspiciousActivity.setUuid(RandomTestUtil.randomString());

		newMBSuspiciousActivity.setGroupId(RandomTestUtil.nextLong());

		newMBSuspiciousActivity.setCompanyId(RandomTestUtil.nextLong());

		newMBSuspiciousActivity.setUserId(RandomTestUtil.nextLong());

		newMBSuspiciousActivity.setUserName(RandomTestUtil.randomString());

		newMBSuspiciousActivity.setCreateDate(RandomTestUtil.nextDate());

		newMBSuspiciousActivity.setModifiedDate(RandomTestUtil.nextDate());

		newMBSuspiciousActivity.setMessageId(RandomTestUtil.nextLong());

		newMBSuspiciousActivity.setThreadId(RandomTestUtil.nextLong());

		newMBSuspiciousActivity.setDescription(RandomTestUtil.randomString());

		newMBSuspiciousActivity.setType(RandomTestUtil.randomString());

		newMBSuspiciousActivity.setValidated(RandomTestUtil.randomBoolean());

		_mbSuspiciousActivities.add(
			_persistence.update(newMBSuspiciousActivity));

		MBSuspiciousActivity existingMBSuspiciousActivity =
			_persistence.findByPrimaryKey(
				newMBSuspiciousActivity.getPrimaryKey());

		Assert.assertEquals(
			existingMBSuspiciousActivity.getMvccVersion(),
			newMBSuspiciousActivity.getMvccVersion());
		Assert.assertEquals(
			existingMBSuspiciousActivity.getCtCollectionId(),
			newMBSuspiciousActivity.getCtCollectionId());
		Assert.assertEquals(
			existingMBSuspiciousActivity.getUuid(),
			newMBSuspiciousActivity.getUuid());
		Assert.assertEquals(
			existingMBSuspiciousActivity.getSuspiciousActivityId(),
			newMBSuspiciousActivity.getSuspiciousActivityId());
		Assert.assertEquals(
			existingMBSuspiciousActivity.getGroupId(),
			newMBSuspiciousActivity.getGroupId());
		Assert.assertEquals(
			existingMBSuspiciousActivity.getCompanyId(),
			newMBSuspiciousActivity.getCompanyId());
		Assert.assertEquals(
			existingMBSuspiciousActivity.getUserId(),
			newMBSuspiciousActivity.getUserId());
		Assert.assertEquals(
			existingMBSuspiciousActivity.getUserName(),
			newMBSuspiciousActivity.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingMBSuspiciousActivity.getCreateDate()),
			Time.getShortTimestamp(newMBSuspiciousActivity.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(
				existingMBSuspiciousActivity.getModifiedDate()),
			Time.getShortTimestamp(newMBSuspiciousActivity.getModifiedDate()));
		Assert.assertEquals(
			existingMBSuspiciousActivity.getMessageId(),
			newMBSuspiciousActivity.getMessageId());
		Assert.assertEquals(
			existingMBSuspiciousActivity.getThreadId(),
			newMBSuspiciousActivity.getThreadId());
		Assert.assertEquals(
			existingMBSuspiciousActivity.getDescription(),
			newMBSuspiciousActivity.getDescription());
		Assert.assertEquals(
			existingMBSuspiciousActivity.getType(),
			newMBSuspiciousActivity.getType());
		Assert.assertEquals(
			existingMBSuspiciousActivity.isValidated(),
			newMBSuspiciousActivity.isValidated());
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
	public void testCountByMessageId() throws Exception {
		_persistence.countByMessageId(RandomTestUtil.nextLong());

		_persistence.countByMessageId(0L);
	}

	@Test
	public void testCountByThreadId() throws Exception {
		_persistence.countByThreadId(RandomTestUtil.nextLong());

		_persistence.countByThreadId(0L);
	}

	@Test
	public void testCountByU_M() throws Exception {
		_persistence.countByU_M(
			RandomTestUtil.nextLong(), RandomTestUtil.nextLong());

		_persistence.countByU_M(0L, 0L);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		MBSuspiciousActivity newMBSuspiciousActivity =
			addMBSuspiciousActivity();

		MBSuspiciousActivity existingMBSuspiciousActivity =
			_persistence.findByPrimaryKey(
				newMBSuspiciousActivity.getPrimaryKey());

		Assert.assertEquals(
			existingMBSuspiciousActivity, newMBSuspiciousActivity);
	}

	@Test(expected = NoSuchSuspiciousActivityException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<MBSuspiciousActivity> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"MBSuspiciousActivity", "mvccVersion", true, "ctCollectionId", true,
			"uuid", true, "suspiciousActivityId", true, "groupId", true,
			"companyId", true, "userId", true, "userName", true, "createDate",
			true, "modifiedDate", true, "messageId", true, "threadId", true,
			"description", true, "type", true, "validated", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		MBSuspiciousActivity newMBSuspiciousActivity =
			addMBSuspiciousActivity();

		MBSuspiciousActivity existingMBSuspiciousActivity =
			_persistence.fetchByPrimaryKey(
				newMBSuspiciousActivity.getPrimaryKey());

		Assert.assertEquals(
			existingMBSuspiciousActivity, newMBSuspiciousActivity);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		MBSuspiciousActivity missingMBSuspiciousActivity =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingMBSuspiciousActivity);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		MBSuspiciousActivity newMBSuspiciousActivity1 =
			addMBSuspiciousActivity();
		MBSuspiciousActivity newMBSuspiciousActivity2 =
			addMBSuspiciousActivity();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newMBSuspiciousActivity1.getPrimaryKey());
		primaryKeys.add(newMBSuspiciousActivity2.getPrimaryKey());

		Map<Serializable, MBSuspiciousActivity> mbSuspiciousActivities =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, mbSuspiciousActivities.size());
		Assert.assertEquals(
			newMBSuspiciousActivity1,
			mbSuspiciousActivities.get(
				newMBSuspiciousActivity1.getPrimaryKey()));
		Assert.assertEquals(
			newMBSuspiciousActivity2,
			mbSuspiciousActivities.get(
				newMBSuspiciousActivity2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, MBSuspiciousActivity> mbSuspiciousActivities =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(mbSuspiciousActivities.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		MBSuspiciousActivity newMBSuspiciousActivity =
			addMBSuspiciousActivity();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newMBSuspiciousActivity.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, MBSuspiciousActivity> mbSuspiciousActivities =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, mbSuspiciousActivities.size());
		Assert.assertEquals(
			newMBSuspiciousActivity,
			mbSuspiciousActivities.get(
				newMBSuspiciousActivity.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, MBSuspiciousActivity> mbSuspiciousActivities =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(mbSuspiciousActivities.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		MBSuspiciousActivity newMBSuspiciousActivity =
			addMBSuspiciousActivity();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newMBSuspiciousActivity.getPrimaryKey());

		Map<Serializable, MBSuspiciousActivity> mbSuspiciousActivities =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, mbSuspiciousActivities.size());
		Assert.assertEquals(
			newMBSuspiciousActivity,
			mbSuspiciousActivities.get(
				newMBSuspiciousActivity.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			MBSuspiciousActivityLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<MBSuspiciousActivity>() {

				@Override
				public void performAction(
					MBSuspiciousActivity mbSuspiciousActivity) {

					Assert.assertNotNull(mbSuspiciousActivity);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		MBSuspiciousActivity newMBSuspiciousActivity =
			addMBSuspiciousActivity();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			MBSuspiciousActivity.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"suspiciousActivityId",
				newMBSuspiciousActivity.getSuspiciousActivityId()));

		List<MBSuspiciousActivity> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		MBSuspiciousActivity existingMBSuspiciousActivity = result.get(0);

		Assert.assertEquals(
			existingMBSuspiciousActivity, newMBSuspiciousActivity);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			MBSuspiciousActivity.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"suspiciousActivityId", RandomTestUtil.nextLong()));

		List<MBSuspiciousActivity> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		MBSuspiciousActivity newMBSuspiciousActivity =
			addMBSuspiciousActivity();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			MBSuspiciousActivity.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("suspiciousActivityId"));

		Object newSuspiciousActivityId =
			newMBSuspiciousActivity.getSuspiciousActivityId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"suspiciousActivityId",
				new Object[] {newSuspiciousActivityId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingSuspiciousActivityId = result.get(0);

		Assert.assertEquals(
			existingSuspiciousActivityId, newSuspiciousActivityId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			MBSuspiciousActivity.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("suspiciousActivityId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"suspiciousActivityId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		MBSuspiciousActivity newMBSuspiciousActivity =
			addMBSuspiciousActivity();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(
				newMBSuspiciousActivity.getPrimaryKey()));
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

		MBSuspiciousActivity newMBSuspiciousActivity =
			addMBSuspiciousActivity();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			MBSuspiciousActivity.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"suspiciousActivityId",
				newMBSuspiciousActivity.getSuspiciousActivityId()));

		List<MBSuspiciousActivity> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(
		MBSuspiciousActivity mbSuspiciousActivity) {

		Assert.assertEquals(
			mbSuspiciousActivity.getUuid(),
			ReflectionTestUtil.invoke(
				mbSuspiciousActivity, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "uuid_"));
		Assert.assertEquals(
			Long.valueOf(mbSuspiciousActivity.getGroupId()),
			ReflectionTestUtil.<Long>invoke(
				mbSuspiciousActivity, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "groupId"));

		Assert.assertEquals(
			Long.valueOf(mbSuspiciousActivity.getUserId()),
			ReflectionTestUtil.<Long>invoke(
				mbSuspiciousActivity, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "userId"));
		Assert.assertEquals(
			Long.valueOf(mbSuspiciousActivity.getMessageId()),
			ReflectionTestUtil.<Long>invoke(
				mbSuspiciousActivity, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "messageId"));
	}

	protected MBSuspiciousActivity addMBSuspiciousActivity() throws Exception {
		long pk = RandomTestUtil.nextLong();

		MBSuspiciousActivity mbSuspiciousActivity = _persistence.create(pk);

		mbSuspiciousActivity.setMvccVersion(RandomTestUtil.nextLong());

		mbSuspiciousActivity.setCtCollectionId(RandomTestUtil.nextLong());

		mbSuspiciousActivity.setUuid(RandomTestUtil.randomString());

		mbSuspiciousActivity.setGroupId(RandomTestUtil.nextLong());

		mbSuspiciousActivity.setCompanyId(RandomTestUtil.nextLong());

		mbSuspiciousActivity.setUserId(RandomTestUtil.nextLong());

		mbSuspiciousActivity.setUserName(RandomTestUtil.randomString());

		mbSuspiciousActivity.setCreateDate(RandomTestUtil.nextDate());

		mbSuspiciousActivity.setModifiedDate(RandomTestUtil.nextDate());

		mbSuspiciousActivity.setMessageId(RandomTestUtil.nextLong());

		mbSuspiciousActivity.setThreadId(RandomTestUtil.nextLong());

		mbSuspiciousActivity.setDescription(RandomTestUtil.randomString());

		mbSuspiciousActivity.setType(RandomTestUtil.randomString());

		mbSuspiciousActivity.setValidated(RandomTestUtil.randomBoolean());

		_mbSuspiciousActivities.add(_persistence.update(mbSuspiciousActivity));

		return mbSuspiciousActivity;
	}

	private List<MBSuspiciousActivity> _mbSuspiciousActivities =
		new ArrayList<MBSuspiciousActivity>();
	private MBSuspiciousActivityPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}