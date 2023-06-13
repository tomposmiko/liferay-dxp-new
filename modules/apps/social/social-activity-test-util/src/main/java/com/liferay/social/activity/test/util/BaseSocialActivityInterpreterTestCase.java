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

package com.liferay.social.activity.test.util;

import com.liferay.portal.events.ServicePreAction;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.social.kernel.model.SocialActivity;
import com.liferay.social.kernel.model.SocialActivityFeedEntry;
import com.liferay.social.kernel.model.SocialActivityInterpreter;
import com.liferay.social.kernel.service.SocialActivityLocalServiceUtil;
import com.liferay.trash.TrashHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Zsolt Berentey
 */
public abstract class BaseSocialActivityInterpreterTestCase {

	@Before
	public void setUp() throws Exception {
		group = GroupTestUtil.addGroup();

		HttpServletRequest httpServletRequest = new MockHttpServletRequest();

		httpServletRequest.setAttribute(
			WebKeys.COMPANY_ID, TestPropsValues.getCompanyId());
		httpServletRequest.setAttribute(
			WebKeys.CURRENT_URL, "http://localhost:80/web/guest/home");
		httpServletRequest.setAttribute(
			WebKeys.USER, TestPropsValues.getUser());

		ServicePreAction servicePreAction = new ServicePreAction();

		servicePreAction.run(httpServletRequest, new MockHttpServletResponse());

		serviceContext = ServiceContextFactory.getInstance(httpServletRequest);
	}

	@Test
	public void testActivityInterpreter() throws Exception {
		addActivities();

		List<SocialActivity> originalActivities = getActivities();

		renameModels();

		_checkRenaming(originalActivities);

		if (isSupportsTrash()) {
			moveModelsToTrash();

			_checkLinks();

			restoreModelsFromTrash();
		}

		_checkInterpret();
	}

	protected abstract void addActivities() throws Exception;

	protected List<SocialActivity> getActivities() throws Exception {
		return new ArrayList<>(
			SocialActivityLocalServiceUtil.getGroupActivities(
				group.getGroupId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS));
	}

	protected abstract SocialActivityInterpreter getActivityInterpreter();

	protected SocialActivityInterpreter getActivityInterpreter(
		String portletId, String className) {

		try {
			Bundle bundle = FrameworkUtil.getBundle(getClass());

			BundleContext bundleContext = bundle.getBundleContext();

			for (ServiceReference<SocialActivityInterpreter> serviceReference :
					bundleContext.getServiceReferences(
						SocialActivityInterpreter.class,
						"(javax.portlet.name=" + portletId + ")")) {

				SocialActivityInterpreter socialActivityInterpreter =
					bundleContext.getService(serviceReference);

				try {
					if (ArrayUtil.contains(
							socialActivityInterpreter.getClassNames(),
							className)) {

						return socialActivityInterpreter;
					}
				}
				finally {
					bundleContext.ungetService(serviceReference);
				}
			}

			throw new IllegalStateException(
				"No activity interpreter found for class " + className);
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	protected abstract int[] getActivityTypes();

	protected boolean hasActivityType(int activityType) {
		for (int curActivityType : getActivityTypes()) {
			if (curActivityType == activityType) {
				return true;
			}
		}

		return false;
	}

	protected boolean hasClassName(
		SocialActivityInterpreter activityInterpreter, String className) {

		for (String curClassName : activityInterpreter.getClassNames()) {
			if (curClassName.equals(className)) {
				return true;
			}
		}

		return false;
	}

	protected boolean isSupportsRename(String className) {
		return true;
	}

	protected boolean isSupportsTrash() {
		return true;
	}

	protected abstract void moveModelsToTrash() throws Exception;

	protected abstract void renameModels() throws Exception;

	protected abstract void restoreModelsFromTrash() throws Exception;

	@DeleteAfterTestRun
	protected Group group;

	protected ServiceContext serviceContext;

	@Inject
	protected TrashHelper trashHelper;

	private void _checkInterpret() throws Exception {
		List<SocialActivity> activities = getActivities();

		Assert.assertFalse(activities.toString(), activities.isEmpty());

		SocialActivityInterpreter activityInterpreter =
			getActivityInterpreter();

		for (SocialActivity activity : activities) {
			if (hasClassName(activityInterpreter, activity.getClassName()) &&
				hasActivityType(activity.getType())) {

				SocialActivityFeedEntry activityFeedEntry =
					activityInterpreter.interpret(activity, serviceContext);

				Assert.assertNotNull(activityFeedEntry);

				String title = activityFeedEntry.getTitle();

				Assert.assertFalse(
					"Title contains parameters: " + title,
					title.matches("\\{\\d\\}"));
			}
		}
	}

	private void _checkLinks() throws Exception {
		List<SocialActivity> activities = getActivities();

		Assert.assertFalse(activities.toString(), activities.isEmpty());

		SocialActivityInterpreter activityInterpreter =
			getActivityInterpreter();

		for (SocialActivity activity : activities) {
			if (hasClassName(activityInterpreter, activity.getClassName()) &&
				hasActivityType(activity.getType())) {

				SocialActivityFeedEntry activityFeedEntry =
					activityInterpreter.interpret(activity, serviceContext);

				PortletURL portletURL = trashHelper.getViewContentURL(
					serviceContext.getRequest(), activity.getClassName(),
					activity.getClassPK());

				if (Validator.isNull(activityFeedEntry.getLink()) &&
					(portletURL == null)) {

					continue;
				}

				Assert.assertEquals(
					portletURL.toString(), activityFeedEntry.getLink());
			}
		}
	}

	private void _checkRenaming(List<SocialActivity> originalActivities)
		throws Exception {

		Assert.assertFalse(
			originalActivities.toString(), originalActivities.isEmpty());

		Set<Long> originalActivitiesIds = _getActivitiesIds(originalActivities);
		String originalTitle = _getFirstActivityTitle(originalActivities);

		List<SocialActivity> activities = getActivities();

		Assert.assertFalse(activities.toString(), activities.isEmpty());

		for (SocialActivity activity : activities) {
			if (!originalActivitiesIds.contains(activity.getActivityId())) {
				String title = activity.getExtraDataValue(
					"title", serviceContext.getLocale());

				if (isSupportsRename(activity.getClassName()) &&
					Validator.isNotNull(title)) {

					Assert.assertNotEquals(originalTitle, title);
				}
			}
		}
	}

	private Set<Long> _getActivitiesIds(List<SocialActivity> activities) {
		Set<Long> activitiesIds = new HashSet<>();

		for (SocialActivity activity : activities) {
			activitiesIds.add(activity.getActivityId());
		}

		return activitiesIds;
	}

	private String _getFirstActivityTitle(List<SocialActivity> activities)
		throws Exception {

		SocialActivity activity = activities.get(0);

		return activity.getExtraDataValue("title", serviceContext.getLocale());
	}

}