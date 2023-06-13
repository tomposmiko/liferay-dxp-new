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

package com.liferay.notification.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.notification.exception.NoSuchNotificationTemplateAttachmentException;
import com.liferay.notification.model.NotificationTemplateAttachment;
import com.liferay.notification.service.NotificationTemplateAttachmentLocalServiceUtil;
import com.liferay.notification.service.persistence.NotificationTemplateAttachmentPersistence;
import com.liferay.notification.service.persistence.NotificationTemplateAttachmentUtil;
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
public class NotificationTemplateAttachmentPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED, "com.liferay.notification.service"));

	@Before
	public void setUp() {
		_persistence = NotificationTemplateAttachmentUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<NotificationTemplateAttachment> iterator =
			_notificationTemplateAttachments.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		NotificationTemplateAttachment notificationTemplateAttachment =
			_persistence.create(pk);

		Assert.assertNotNull(notificationTemplateAttachment);

		Assert.assertEquals(notificationTemplateAttachment.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		NotificationTemplateAttachment newNotificationTemplateAttachment =
			addNotificationTemplateAttachment();

		_persistence.remove(newNotificationTemplateAttachment);

		NotificationTemplateAttachment existingNotificationTemplateAttachment =
			_persistence.fetchByPrimaryKey(
				newNotificationTemplateAttachment.getPrimaryKey());

		Assert.assertNull(existingNotificationTemplateAttachment);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addNotificationTemplateAttachment();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		NotificationTemplateAttachment newNotificationTemplateAttachment =
			_persistence.create(pk);

		newNotificationTemplateAttachment.setMvccVersion(
			RandomTestUtil.nextLong());

		newNotificationTemplateAttachment.setCompanyId(
			RandomTestUtil.nextLong());

		newNotificationTemplateAttachment.setNotificationTemplateId(
			RandomTestUtil.nextLong());

		newNotificationTemplateAttachment.setObjectFieldId(
			RandomTestUtil.nextLong());

		_notificationTemplateAttachments.add(
			_persistence.update(newNotificationTemplateAttachment));

		NotificationTemplateAttachment existingNotificationTemplateAttachment =
			_persistence.findByPrimaryKey(
				newNotificationTemplateAttachment.getPrimaryKey());

		Assert.assertEquals(
			existingNotificationTemplateAttachment.getMvccVersion(),
			newNotificationTemplateAttachment.getMvccVersion());
		Assert.assertEquals(
			existingNotificationTemplateAttachment.
				getNotificationTemplateAttachmentId(),
			newNotificationTemplateAttachment.
				getNotificationTemplateAttachmentId());
		Assert.assertEquals(
			existingNotificationTemplateAttachment.getCompanyId(),
			newNotificationTemplateAttachment.getCompanyId());
		Assert.assertEquals(
			existingNotificationTemplateAttachment.getNotificationTemplateId(),
			newNotificationTemplateAttachment.getNotificationTemplateId());
		Assert.assertEquals(
			existingNotificationTemplateAttachment.getObjectFieldId(),
			newNotificationTemplateAttachment.getObjectFieldId());
	}

	@Test
	public void testCountByNotificationTemplateId() throws Exception {
		_persistence.countByNotificationTemplateId(RandomTestUtil.nextLong());

		_persistence.countByNotificationTemplateId(0L);
	}

	@Test
	public void testCountByNTI_OFI() throws Exception {
		_persistence.countByNTI_OFI(
			RandomTestUtil.nextLong(), RandomTestUtil.nextLong());

		_persistence.countByNTI_OFI(0L, 0L);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		NotificationTemplateAttachment newNotificationTemplateAttachment =
			addNotificationTemplateAttachment();

		NotificationTemplateAttachment existingNotificationTemplateAttachment =
			_persistence.findByPrimaryKey(
				newNotificationTemplateAttachment.getPrimaryKey());

		Assert.assertEquals(
			existingNotificationTemplateAttachment,
			newNotificationTemplateAttachment);
	}

	@Test(expected = NoSuchNotificationTemplateAttachmentException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<NotificationTemplateAttachment>
		getOrderByComparator() {

		return OrderByComparatorFactoryUtil.create(
			"NTemplateAttachment", "mvccVersion", true,
			"notificationTemplateAttachmentId", true, "companyId", true,
			"notificationTemplateId", true, "objectFieldId", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		NotificationTemplateAttachment newNotificationTemplateAttachment =
			addNotificationTemplateAttachment();

		NotificationTemplateAttachment existingNotificationTemplateAttachment =
			_persistence.fetchByPrimaryKey(
				newNotificationTemplateAttachment.getPrimaryKey());

		Assert.assertEquals(
			existingNotificationTemplateAttachment,
			newNotificationTemplateAttachment);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		NotificationTemplateAttachment missingNotificationTemplateAttachment =
			_persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingNotificationTemplateAttachment);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		NotificationTemplateAttachment newNotificationTemplateAttachment1 =
			addNotificationTemplateAttachment();
		NotificationTemplateAttachment newNotificationTemplateAttachment2 =
			addNotificationTemplateAttachment();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newNotificationTemplateAttachment1.getPrimaryKey());
		primaryKeys.add(newNotificationTemplateAttachment2.getPrimaryKey());

		Map<Serializable, NotificationTemplateAttachment>
			notificationTemplateAttachments = _persistence.fetchByPrimaryKeys(
				primaryKeys);

		Assert.assertEquals(2, notificationTemplateAttachments.size());
		Assert.assertEquals(
			newNotificationTemplateAttachment1,
			notificationTemplateAttachments.get(
				newNotificationTemplateAttachment1.getPrimaryKey()));
		Assert.assertEquals(
			newNotificationTemplateAttachment2,
			notificationTemplateAttachments.get(
				newNotificationTemplateAttachment2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, NotificationTemplateAttachment>
			notificationTemplateAttachments = _persistence.fetchByPrimaryKeys(
				primaryKeys);

		Assert.assertTrue(notificationTemplateAttachments.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		NotificationTemplateAttachment newNotificationTemplateAttachment =
			addNotificationTemplateAttachment();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newNotificationTemplateAttachment.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, NotificationTemplateAttachment>
			notificationTemplateAttachments = _persistence.fetchByPrimaryKeys(
				primaryKeys);

		Assert.assertEquals(1, notificationTemplateAttachments.size());
		Assert.assertEquals(
			newNotificationTemplateAttachment,
			notificationTemplateAttachments.get(
				newNotificationTemplateAttachment.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, NotificationTemplateAttachment>
			notificationTemplateAttachments = _persistence.fetchByPrimaryKeys(
				primaryKeys);

		Assert.assertTrue(notificationTemplateAttachments.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		NotificationTemplateAttachment newNotificationTemplateAttachment =
			addNotificationTemplateAttachment();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newNotificationTemplateAttachment.getPrimaryKey());

		Map<Serializable, NotificationTemplateAttachment>
			notificationTemplateAttachments = _persistence.fetchByPrimaryKeys(
				primaryKeys);

		Assert.assertEquals(1, notificationTemplateAttachments.size());
		Assert.assertEquals(
			newNotificationTemplateAttachment,
			notificationTemplateAttachments.get(
				newNotificationTemplateAttachment.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			NotificationTemplateAttachmentLocalServiceUtil.
				getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<NotificationTemplateAttachment>() {

				@Override
				public void performAction(
					NotificationTemplateAttachment
						notificationTemplateAttachment) {

					Assert.assertNotNull(notificationTemplateAttachment);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		NotificationTemplateAttachment newNotificationTemplateAttachment =
			addNotificationTemplateAttachment();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			NotificationTemplateAttachment.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"notificationTemplateAttachmentId",
				newNotificationTemplateAttachment.
					getNotificationTemplateAttachmentId()));

		List<NotificationTemplateAttachment> result =
			_persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		NotificationTemplateAttachment existingNotificationTemplateAttachment =
			result.get(0);

		Assert.assertEquals(
			existingNotificationTemplateAttachment,
			newNotificationTemplateAttachment);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			NotificationTemplateAttachment.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"notificationTemplateAttachmentId", RandomTestUtil.nextLong()));

		List<NotificationTemplateAttachment> result =
			_persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		NotificationTemplateAttachment newNotificationTemplateAttachment =
			addNotificationTemplateAttachment();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			NotificationTemplateAttachment.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("notificationTemplateAttachmentId"));

		Object newNotificationTemplateAttachmentId =
			newNotificationTemplateAttachment.
				getNotificationTemplateAttachmentId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"notificationTemplateAttachmentId",
				new Object[] {newNotificationTemplateAttachmentId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingNotificationTemplateAttachmentId = result.get(0);

		Assert.assertEquals(
			existingNotificationTemplateAttachmentId,
			newNotificationTemplateAttachmentId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			NotificationTemplateAttachment.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("notificationTemplateAttachmentId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"notificationTemplateAttachmentId",
				new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		NotificationTemplateAttachment newNotificationTemplateAttachment =
			addNotificationTemplateAttachment();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(
				newNotificationTemplateAttachment.getPrimaryKey()));
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

		NotificationTemplateAttachment newNotificationTemplateAttachment =
			addNotificationTemplateAttachment();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			NotificationTemplateAttachment.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"notificationTemplateAttachmentId",
				newNotificationTemplateAttachment.
					getNotificationTemplateAttachmentId()));

		List<NotificationTemplateAttachment> result =
			_persistence.findWithDynamicQuery(dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(
		NotificationTemplateAttachment notificationTemplateAttachment) {

		Assert.assertEquals(
			Long.valueOf(
				notificationTemplateAttachment.getNotificationTemplateId()),
			ReflectionTestUtil.<Long>invoke(
				notificationTemplateAttachment, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "notificationTemplateId"));
		Assert.assertEquals(
			Long.valueOf(notificationTemplateAttachment.getObjectFieldId()),
			ReflectionTestUtil.<Long>invoke(
				notificationTemplateAttachment, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "objectFieldId"));
	}

	protected NotificationTemplateAttachment addNotificationTemplateAttachment()
		throws Exception {

		long pk = RandomTestUtil.nextLong();

		NotificationTemplateAttachment notificationTemplateAttachment =
			_persistence.create(pk);

		notificationTemplateAttachment.setMvccVersion(
			RandomTestUtil.nextLong());

		notificationTemplateAttachment.setCompanyId(RandomTestUtil.nextLong());

		notificationTemplateAttachment.setNotificationTemplateId(
			RandomTestUtil.nextLong());

		notificationTemplateAttachment.setObjectFieldId(
			RandomTestUtil.nextLong());

		_notificationTemplateAttachments.add(
			_persistence.update(notificationTemplateAttachment));

		return notificationTemplateAttachment;
	}

	private List<NotificationTemplateAttachment>
		_notificationTemplateAttachments =
			new ArrayList<NotificationTemplateAttachment>();
	private NotificationTemplateAttachmentPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}