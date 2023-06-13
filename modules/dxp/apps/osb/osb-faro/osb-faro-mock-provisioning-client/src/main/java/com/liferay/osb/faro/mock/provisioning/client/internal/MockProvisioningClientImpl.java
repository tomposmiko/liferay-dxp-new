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

package com.liferay.osb.faro.mock.provisioning.client.internal;

import com.liferay.osb.faro.provisioning.client.ProvisioningClient;
import com.liferay.osb.faro.provisioning.client.constants.ProductConstants;
import com.liferay.osb.faro.provisioning.client.model.OSBAccountEntry;
import com.liferay.osb.faro.provisioning.client.model.OSBOfferingEntry;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Kong
 */
@Component(
	property = "service.ranking:Integer=100", service = ProvisioningClient.class
)
public class MockProvisioningClientImpl extends BaseMockProvisioningClientImpl {

	@Override
	public void addCorpProjectUsers(String corpProjectUuid, String[] userUuids)
		throws Exception {

		if (corpProjectUuid.equals(_PROJECT_ID) ||
			corpProjectUuid.equals(_MOCK_PROJECT_ID)) {

			return;
		}

		super.addCorpProjectUsers(corpProjectUuid, userUuids);
	}

	@Override
	public void addUserCorpProjectRoles(
			String corpProjectUuid, String[] userUuids, String roleName)
		throws Exception {

		if (corpProjectUuid.equals(_PROJECT_ID) ||
			corpProjectUuid.equals(_MOCK_PROJECT_ID)) {

			return;
		}

		super.addUserCorpProjectRoles(corpProjectUuid, userUuids, roleName);
	}

	@Override
	public List<OSBAccountEntry> getOSBAccountEntries(
			String userUuid, String[] productEntryIds)
		throws Exception {

		if (Validator.isNotNull(_MOCK_OSB_ACCOUNT_ENTRY)) {
			return Collections.singletonList(_mockOSBAccountEntry);
		}

		User user = _userLocalService.fetchUserByUuidAndCompanyId(
			userUuid, _portal.getDefaultCompanyId());

		if ((user != null) &&
			StringUtil.equals(user.getEmailAddress(), "test@liferay.com")) {

			return Collections.emptyList();
		}

		return super.getOSBAccountEntries(userUuid, productEntryIds);
	}

	@Override
	public OSBAccountEntry getOSBAccountEntry(String corpProjectUuid)
		throws Exception {

		if (corpProjectUuid.equals(_MOCK_PROJECT_ID)) {
			return _mockOSBAccountEntry;
		}

		if (corpProjectUuid.equals(_PROJECT_ID)) {
			return _osbAccountEntry;
		}

		return super.getOSBAccountEntry(corpProjectUuid);
	}

	protected static String encodeAuthorizationFields(
		String userName, String password) {

		String authorizationString = StringBundler.concat(
			userName, StringPool.COLON, password);

		return new String(
			Base64.encodeBase64(
				authorizationString.getBytes(StandardCharsets.UTF_8)),
			StandardCharsets.UTF_8);
	}

	private static final String _MOCK_OSB_ACCOUNT_ENTRY = System.getenv(
		"FARO_MOCK_OSB_ACCOUNT_ENTRY");

	private static final String _MOCK_PROJECT_ID = System.getenv(
		"MOCK_PROJECT_ID");

	private static final String _PROJECT_ID = System.getenv("FARO_PROJECT_ID");

	private static final OSBAccountEntry _mockOSBAccountEntry =
		new OSBAccountEntry() {
			{
				List<OSBOfferingEntry> osbOfferingEntries = new ArrayList<>();

				OSBOfferingEntry enterpriseOSBOfferingEntry =
					new OSBOfferingEntry();

				enterpriseOSBOfferingEntry.setProductEntryId(
					ProductConstants.BUSINESS_PRODUCT_ENTRY_ID);
				enterpriseOSBOfferingEntry.setQuantity(1);
				enterpriseOSBOfferingEntry.setStartDate(
					new Date(1546329600000L));
				enterpriseOSBOfferingEntry.setStatus(
					ProductConstants.OSB_OFFERING_ENTRY_STATUS_ACTIVE);
				enterpriseOSBOfferingEntry.setSupportEndDate(
					new Date(2000000000000L));

				osbOfferingEntries.add(enterpriseOSBOfferingEntry);

				setCorpEntryName("Mock Project");
				setCorpProjectUuid(_MOCK_PROJECT_ID);
				setDossieraAccountKey("Mock Project");
				setName("Mock Project");
				setOfferingEntries(osbOfferingEntries);
			}
		};

	private static final OSBAccountEntry _osbAccountEntry =
		new OSBAccountEntry() {
			{
				List<OSBOfferingEntry> osbOfferingEntries = new ArrayList<>();

				OSBOfferingEntry enterpriseOSBOfferingEntry =
					new OSBOfferingEntry();

				enterpriseOSBOfferingEntry.setProductEntryId(
					ProductConstants.ENTERPRISE_PRODUCT_ENTRY_ID);
				enterpriseOSBOfferingEntry.setQuantity(1);
				enterpriseOSBOfferingEntry.setStartDate(
					new Date(1546329600000L));
				enterpriseOSBOfferingEntry.setSupportEndDate(
					new Date(2000000000000L));

				osbOfferingEntries.add(enterpriseOSBOfferingEntry);

				OSBOfferingEntry enterpriseContactsOSBOfferingEntry =
					new OSBOfferingEntry();

				enterpriseContactsOSBOfferingEntry.setProductEntryId(
					ProductConstants.ENTERPRISE_CONTACTS_PRODUCT_ENTRY_ID);
				enterpriseContactsOSBOfferingEntry.setQuantity(2);

				osbOfferingEntries.add(enterpriseContactsOSBOfferingEntry);

				OSBOfferingEntry enterpriseTrackedPagesOSBOfferingEntry =
					new OSBOfferingEntry();

				enterpriseTrackedPagesOSBOfferingEntry.setProductEntryId(
					ProductConstants.ENTERPRISE_TRACKED_PAGES_PRODUCT_ENTRY_ID);
				enterpriseTrackedPagesOSBOfferingEntry.setQuantity(3);

				osbOfferingEntries.add(enterpriseTrackedPagesOSBOfferingEntry);

				setCorpEntryName("Liferay Demo");
				setCorpProjectUuid(_PROJECT_ID);
				setDossieraAccountKey("Liferay Demo");
				setName("Liferay Demo");
				setOfferingEntries(osbOfferingEntries);
			}
		};

	@Reference
	private Portal _portal;

	@Reference
	private UserLocalService _userLocalService;

}