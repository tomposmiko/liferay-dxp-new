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

package com.liferay.osb.faro.contacts.demo.internal.data.creator;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Matthew Kong
 */
public class SalesforceIndividualsDataCreator extends DataCreator {

	public SalesforceIndividualsDataCreator(
		ContactsEngineClient contactsEngineClient, FaroProject faroProject,
		String dataSourceId) {

		super(
			contactsEngineClient, faroProject, "osbasahsalesforceraw",
			"individuals");

		_dataSourceId = dataSourceId;

		_salesforceAuditEventsDataCreator =
			new SalesforceAuditEventsDataCreator(
				contactsEngineClient, faroProject, "individuals");
	}

	@Override
	public void execute() {
		super.execute();

		_salesforceAuditEventsDataCreator.execute();
	}

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> liferayUser = new HashMap<>();
		Map<String, Object> salesforceAccount = new HashMap<>();

		if (params != null) {
			liferayUser.putAll((Map<String, Object>)params[0]);
			salesforceAccount.putAll((Map<String, Object>)params[1]);
		}

		Object accountPKs = salesforceAccount.get("id");

		String firstName = (String)liferayUser.getOrDefault(
			"firstName", name.firstName());
		String lastName = (String)liferayUser.getOrDefault(
			"lastName", name.lastName());

		Map<String, Object> salesforceIndividual =
			HashMapBuilder.<String, Object>put(
				"dataSourceId", _dataSourceId
			).put(
				"fields",
				HashMapBuilder.<String, Object>put(
					"accountPKs",
					() -> {
						if (accountPKs != null) {
							return Collections.singletonList(accountPKs);
						}

						return null;
					}
				).put(
					"birthDate",
					liferayUser.getOrDefault(
						"birthday", dateAndTime.past(18250, TimeUnit.DAYS))
				).put(
					"city", address.city()
				).put(
					"company",
					salesforceAccount.getOrDefault("Name", company.name())
				).put(
					"contactId",
					() -> {
						if (!salesforceAccount.isEmpty()) {
							return number.randomNumber(8, false);
						}

						return null;
					}
				).put(
					"country", address.country()
				).put(
					"department", commerce.department()
				).put(
					"description", company.buzzword()
				).put(
					"doNotCall", bool.bool()
				).put(
					"email",
					liferayUser.getOrDefault(
						"emailAddress",
						internet.emailAddress(
							firstName + StringPool.PERIOD + lastName))
				).put(
					"fax", phoneNumber.phoneNumber()
				).put(
					"firstName", firstName
				).put(
					"fullName", firstName + StringPool.SPACE + lastName
				).put(
					"industry",
					salesforceAccount.getOrDefault(
						"Industry", company.industry())
				).put(
					"lastName", lastName
				).put(
					"middleName", name.firstName()
				).put(
					"mobilePhone", phoneNumber.cellPhone()
				).put(
					"modifiedDate", formatDate(new Date())
				).put(
					"phone", phoneNumber.phoneNumber()
				).put(
					"postalCode", address.zipCode()
				).put(
					"state", address.state()
				).put(
					"street", address.streetAddress()
				).put(
					"suffix", name.suffix()
				).put(
					"title", name.title()
				)
			).put(
				"id", internet.uuid()
			).build();

		_salesforceAuditEventsDataCreator.create(
			new Object[] {salesforceIndividual});

		return salesforceIndividual;
	}

	private final String _dataSourceId;
	private final SalesforceAuditEventsDataCreator
		_salesforceAuditEventsDataCreator;

}