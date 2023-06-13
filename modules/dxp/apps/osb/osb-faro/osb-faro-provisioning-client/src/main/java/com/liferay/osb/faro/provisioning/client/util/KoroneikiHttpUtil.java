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

package com.liferay.osb.faro.provisioning.client.util;

import com.liferay.osb.faro.provisioning.client.constants.KoroneikiConstants;
import com.liferay.osb.koroneiki.phloem.rest.client.dto.v1_0.Account;
import com.liferay.osb.koroneiki.phloem.rest.client.dto.v1_0.Contact;
import com.liferay.osb.koroneiki.phloem.rest.client.dto.v1_0.ContactRole;
import com.liferay.osb.koroneiki.phloem.rest.client.dto.v1_0.Product;
import com.liferay.osb.koroneiki.phloem.rest.client.http.HttpInvoker;
import com.liferay.osb.koroneiki.phloem.rest.client.pagination.Page;
import com.liferay.osb.koroneiki.phloem.rest.client.pagination.Pagination;
import com.liferay.osb.koroneiki.phloem.rest.client.resource.v1_0.AccountResource;
import com.liferay.osb.koroneiki.phloem.rest.client.resource.v1_0.ContactResource;
import com.liferay.osb.koroneiki.phloem.rest.client.resource.v1_0.ContactRoleResource;
import com.liferay.osb.koroneiki.phloem.rest.client.resource.v1_0.ProductResource;
import com.liferay.osb.koroneiki.phloem.rest.client.serdes.v1_0.AccountSerDes;
import com.liferay.osb.koroneiki.phloem.rest.client.serdes.v1_0.ContactRoleSerDes;
import com.liferay.osb.koroneiki.phloem.rest.client.serdes.v1_0.ContactSerDes;
import com.liferay.osb.koroneiki.phloem.rest.client.serdes.v1_0.ProductSerDes;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.HttpComponentsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Marcos Martins
 */
public class KoroneikiHttpUtil {

	public static void assignAccountContactRole(
			String accountKey, String contactRoleKey, String contactUuid)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			_accountResource.putAccountContactByUuidContactUuidRoleHttpResponse(
				StringPool.BLANK, StringPool.BLANK, accountKey, contactUuid,
				new String[] {contactRoleKey});

		if ((httpResponse.getStatusCode() !=
				HttpServletResponse.SC_NO_CONTENT) &&
			(httpResponse.getStatusCode() != HttpServletResponse.SC_OK)) {

			throw new Exception(
				httpResponse.getContent() + StringPool.NEW_LINE +
					httpResponse.getMessage());
		}
	}

	public static Account fetchAccount(String accountKey) throws Exception {
		HttpInvoker.HttpResponse httpResponse =
			_accountResource.getAccountHttpResponse(accountKey);

		if (httpResponse.getStatusCode() == HttpServletResponse.SC_OK) {
			return AccountSerDes.toDTO(httpResponse.getContent());
		}

		return null;
	}

	public static Contact fetchContact(String emailAddress) throws Exception {
		HttpInvoker.HttpResponse httpResponse =
			_contactResource.getContactByEmailAddresEmailAddressHttpResponse(
				emailAddress);

		if (httpResponse.getStatusCode() == HttpServletResponse.SC_OK) {
			return ContactSerDes.toDTO(httpResponse.getContent());
		}

		return null;
	}

	public static ContactRole fetchContactRole(
			String name, ContactRole.Type type)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			_contactRoleResource.getContactRoleHttpResponse(
				HttpComponentsUtil.encodePath(type.toString()),
				HttpComponentsUtil.encodePath(
					KoroneikiConstants.translateContactRoleName(name)));

		if (httpResponse.getStatusCode() == HttpServletResponse.SC_OK) {
			return ContactRoleSerDes.toDTO(httpResponse.getContent());
		}

		return null;
	}

	public static Product fetchProduct(String productName) throws Exception {
		HttpInvoker.HttpResponse httpResponse =
			_productResource.getProductByNameProductNameHttpResponse(
				HttpComponentsUtil.encodePath(productName));

		if (httpResponse.getStatusCode() == HttpServletResponse.SC_OK) {
			return ProductSerDes.toDTO(httpResponse.getContent());
		}

		return null;
	}

	public static List<ContactRole> getAccountContactRoles(
			String accountKey, String contactUuid, int page, int pageSize)
		throws Exception {

		Page<ContactRole> contactRolesPage =
			_contactRoleResource.
				getAccountAccountKeyContactByUuidContactUuidRolesPage(
					accountKey, contactUuid, Pagination.of(page, pageSize));

		if ((contactRolesPage != null) &&
			(contactRolesPage.getItems() != null)) {

			return new ArrayList<>(contactRolesPage.getItems());
		}

		return Collections.emptyList();
	}

	public static List<Account> getAccounts(
			String domain, String entityId, String entityName, int page,
			int pageSize)
		throws Exception {

		Page<Account> accountsPage =
			_accountResource.getAccountByExternalLinkDomainEntityNameEntityPage(
				domain, entityName, entityId, Pagination.of(page, pageSize));

		if ((accountsPage != null) && (accountsPage.getItems() != null)) {
			return new ArrayList<>(accountsPage.getItems());
		}

		return Collections.emptyList();
	}

	public static int getAccountsCount(String filter) throws Exception {
		HttpInvoker.HttpResponse httpResponse =
			_accountResource.getAccountsPageHttpResponse(
				null, filter, null, null);

		if (httpResponse.getStatusCode() == HttpServletResponse.SC_OK) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
				httpResponse.getContent());

			return jsonObject.getInt("totalCount");
		}

		return 0;
	}

	public static Contact postContact(Contact contact) throws Exception {
		return _contactResource.postContact(
			StringPool.BLANK, StringPool.BLANK, contact);
	}

	public static List<Account> searchAccounts(
			String filter, int page, int size)
		throws Exception {

		Page<Account> accountsPage = _accountResource.getAccountsPage(
			null, filter, Pagination.of(page, size), null);

		if ((accountsPage != null) && (accountsPage.getItems() != null)) {
			return new ArrayList<>(accountsPage.getItems());
		}

		return Collections.emptyList();
	}

	public static void unassignAccountContactRole(
			String accountKey, String contactRoleKey, String contactUuid)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse =
			_accountResource.
				deleteAccountContactByUuidContactUuidRoleHttpResponse(
					StringPool.BLANK, StringPool.BLANK, accountKey, contactUuid,
					new String[] {contactRoleKey});

		if ((httpResponse.getStatusCode() !=
				HttpServletResponse.SC_NO_CONTENT) &&
			(httpResponse.getStatusCode() != HttpServletResponse.SC_OK)) {

			throw new Exception(
				httpResponse.getContent() + StringPool.NEW_LINE +
					httpResponse.getMessage());
		}
	}

	private static final Integer _OSB_API_PORT = Integer.valueOf(
		System.getenv("OSB_API_PORT"));

	private static final String _OSB_API_PROTOCOL = System.getenv(
		"OSB_API_PROTOCOL");

	private static final String _OSB_API_TOKEN = System.getenv("OSB_API_TOKEN");

	private static final String _OSB_API_TOKEN_KEY = "API_Token";

	private static final String _OSB_API_URL = System.getenv("OSB_API_URL");

	private static final AccountResource _accountResource;
	private static final ContactResource _contactResource;
	private static final ContactRoleResource _contactRoleResource;
	private static final ProductResource _productResource;

	static {
		AccountResource.Builder accountResourceBuilder =
			AccountResource.builder();

		_accountResource = accountResourceBuilder.endpoint(
			_OSB_API_URL, _OSB_API_PORT, _OSB_API_PROTOCOL
		).header(
			_OSB_API_TOKEN_KEY, _OSB_API_TOKEN
		).parameter(
			"nestedFields", "productPurchases"
		).build();

		ContactResource.Builder contactResourceBuilder =
			ContactResource.builder();

		_contactResource = contactResourceBuilder.endpoint(
			_OSB_API_URL, _OSB_API_PORT, _OSB_API_PROTOCOL
		).header(
			_OSB_API_TOKEN_KEY, _OSB_API_TOKEN
		).build();

		ContactRoleResource.Builder contactRoleResourceBuilder =
			ContactRoleResource.builder();

		_contactRoleResource = contactRoleResourceBuilder.endpoint(
			_OSB_API_URL, _OSB_API_PORT, _OSB_API_PROTOCOL
		).header(
			_OSB_API_TOKEN_KEY, _OSB_API_TOKEN
		).build();

		ProductResource.Builder productResourceBuilder =
			ProductResource.builder();

		_productResource = productResourceBuilder.endpoint(
			_OSB_API_URL, _OSB_API_PORT, _OSB_API_PROTOCOL
		).header(
			_OSB_API_TOKEN_KEY, _OSB_API_TOKEN
		).build();
	}

}