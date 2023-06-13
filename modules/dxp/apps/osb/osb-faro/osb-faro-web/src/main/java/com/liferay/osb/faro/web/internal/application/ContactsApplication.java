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

package com.liferay.osb.faro.web.internal.application;

import com.liferay.osb.faro.web.internal.constants.FaroConstants;
import com.liferay.osb.faro.web.internal.controller.contacts.AccountController;
import com.liferay.osb.faro.web.internal.controller.contacts.ActivityController;
import com.liferay.osb.faro.web.internal.controller.contacts.ActivityGroupController;
import com.liferay.osb.faro.web.internal.controller.contacts.ContactsCardController;
import com.liferay.osb.faro.web.internal.controller.contacts.ContactsCardTemplateController;
import com.liferay.osb.faro.web.internal.controller.contacts.ContactsLayoutController;
import com.liferay.osb.faro.web.internal.controller.contacts.ContactsLayoutTemplateController;
import com.liferay.osb.faro.web.internal.controller.contacts.DataSourceController;
import com.liferay.osb.faro.web.internal.controller.contacts.FieldController;
import com.liferay.osb.faro.web.internal.controller.contacts.FieldMappingController;
import com.liferay.osb.faro.web.internal.controller.contacts.IndividualController;
import com.liferay.osb.faro.web.internal.controller.contacts.IndividualSegmentController;
import com.liferay.osb.faro.web.internal.controller.contacts.InterestController;
import com.liferay.osb.faro.web.internal.controller.contacts.PagesVisitedController;
import com.liferay.osb.faro.web.internal.controller.contacts.SessionController;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Kong
 */
@ApplicationPath("/" + FaroConstants.APPLICATION_CONTACTS)
@Component(
	immediate = true, property = "jaxrs.application=true",
	service = Application.class
)
public class ContactsApplication extends BaseApplication {

	@Override
	public Set<Object> getControllers() {
		Set<Object> controllers = new HashSet<>();

		controllers.add(_accountController);
		controllers.add(_activityController);
		controllers.add(_activityGroupController);
		controllers.add(_contactsCardController);
		controllers.add(_contactsCardTemplateController);
		controllers.add(_contactsLayoutController);
		controllers.add(_contactsLayoutTemplateController);
		controllers.add(_dataSourceController);
		controllers.add(_fieldController);
		controllers.add(_fieldMappingController);
		controllers.add(_individualController);
		controllers.add(_individualSegmentController);
		controllers.add(_interestController);
		controllers.add(_pagesVisitedController);
		controllers.add(_sessionController);

		return controllers;
	}

	@Reference
	private AccountController _accountController;

	@Reference
	private ActivityController _activityController;

	@Reference
	private ActivityGroupController _activityGroupController;

	@Reference
	private ContactsCardController _contactsCardController;

	@Reference
	private ContactsCardTemplateController _contactsCardTemplateController;

	@Reference
	private ContactsLayoutController _contactsLayoutController;

	@Reference
	private ContactsLayoutTemplateController _contactsLayoutTemplateController;

	@Reference
	private DataSourceController _dataSourceController;

	@Reference
	private FieldController _fieldController;

	@Reference
	private FieldMappingController _fieldMappingController;

	@Reference
	private IndividualController _individualController;

	@Reference
	private IndividualSegmentController _individualSegmentController;

	@Reference
	private InterestController _interestController;

	@Reference
	private PagesVisitedController _pagesVisitedController;

	@Reference
	private SessionController _sessionController;

}