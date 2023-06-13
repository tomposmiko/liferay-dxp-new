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

package com.liferay.osb.faro.engine.client.model.provider;

import com.liferay.osb.faro.engine.client.model.Provider;

import java.util.List;

/**
 * @author Matthew Kong
 */
public class LiferayProvider implements Provider {

	public static final String TYPE = "LIFERAY";

	public AnalyticsConfiguration getAnalyticsConfiguration() {
		return _analyticsConfiguration;
	}

	public ContactsConfiguration getContactsConfiguration() {
		return _contactsConfiguration;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	public void setAnalyticsConfiguration(
		AnalyticsConfiguration analyticsConfiguration) {

		_analyticsConfiguration = analyticsConfiguration;
	}

	public void setContactsConfiguration(
		ContactsConfiguration contactsConfiguration) {

		_contactsConfiguration = contactsConfiguration;
	}

	public static class AnalyticsConfiguration {

		public String getAnalyticsKey() {
			return _analyticsKey;
		}

		public List<Container> getSites() {
			return _sites;
		}

		public boolean isEnableAllSites() {
			return _enableAllSites;
		}

		public void setAnalyticsKey(String analyticsKey) {
			_analyticsKey = analyticsKey;
		}

		public void setEnableAllSites(boolean enableAllSites) {
			_enableAllSites = enableAllSites;
		}

		public void setSites(List<Container> sites) {
			_sites = sites;
		}

		private String _analyticsKey;
		private boolean _enableAllSites;
		private List<Container> _sites;

	}

	public static class ContactsConfiguration {

		public List<Container> getOrganizations() {
			return _organizations;
		}

		public List<Container> getUserGroups() {
			return _userGroups;
		}

		public boolean isEnableAllContacts() {
			return _enableAllContacts;
		}

		public void setEnableAllContacts(boolean enableAllContacts) {
			_enableAllContacts = enableAllContacts;
		}

		public void setOrganizations(List<Container> organizations) {
			_organizations = organizations;
		}

		public void setUserGroups(List<Container> userGroups) {
			_userGroups = userGroups;
		}

		private boolean _enableAllContacts;
		private List<Container> _organizations;
		private List<Container> _userGroups;

	}

	public static class Container {

		public Container() {
		}

		public String getId() {
			return _id;
		}

		public boolean isEnableAllChildren() {
			return _enableAllChildren;
		}

		public void setEnableAllChildren(boolean enableAllChildren) {
			_enableAllChildren = enableAllChildren;
		}

		public void setId(String id) {
			_id = id;
		}

		private boolean _enableAllChildren;
		private String _id;

	}

	private AnalyticsConfiguration _analyticsConfiguration;
	private ContactsConfiguration _contactsConfiguration;

}