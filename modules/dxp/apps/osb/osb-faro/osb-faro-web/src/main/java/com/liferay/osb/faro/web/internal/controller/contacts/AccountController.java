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

package com.liferay.osb.faro.web.internal.controller.contacts;

import com.liferay.osb.faro.contacts.model.constants.JSONConstants;
import com.liferay.osb.faro.engine.client.model.Account;
import com.liferay.osb.faro.engine.client.model.Field;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.engine.client.util.OrderByField;
import com.liferay.osb.faro.web.internal.constants.FaroConstants;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.controller.FaroController;
import com.liferay.osb.faro.web.internal.model.display.FaroResultsDisplay;
import com.liferay.osb.faro.web.internal.model.display.contacts.AccountDisplay;
import com.liferay.osb.faro.web.internal.param.FaroParam;
import com.liferay.osb.faro.web.internal.search.FaroSearchContext;
import com.liferay.osb.faro.web.internal.util.PhotoURLHelper;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.RoleConstants;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Kong
 */
@Component(
	immediate = true, service = {AccountController.class, FaroController.class}
)
@Path("/{groupId}/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountController extends BaseFaroController {

	@GET
	@Path("/{id}")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public AccountDisplay get(
			@PathParam("groupId") long groupId, @PathParam("id") String id)
		throws Exception {

		return new AccountDisplay(
			contactsEngineClient.getAccount(
				faroProjectLocalService.getFaroProjectByGroupId(groupId), id),
			_photoURLHelper);
	}

	@GET
	@Path("/{id}/details")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public Map<String, List<Field>> getDetails(
			@PathParam("groupId") long groupId, @PathParam("id") String id)
		throws Exception {

		Account account = contactsEngineClient.getAccount(
			faroProjectLocalService.getFaroProjectByGroupId(groupId), id);

		return account.getOrganization();
	}

	@GET
	@Path("/distribution")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay getDistribution(
			@PathParam("groupId") long groupId,
			@QueryParam("channelId") String channelId,
			@QueryParam("fieldMappingFieldName") String fieldMappingFieldName,
			@QueryParam("filter") String filter,
			@QueryParam("individualSegmentId") String individualSegmentId,
			@QueryParam("count") int count,
			@QueryParam("numberOfBins") int numberOfBins,
			@DefaultValue(StringPool.BLANK) @QueryParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		return new FaroResultsDisplay(
			contactsEngineClient.getAccountsDistribution(
				faroProjectLocalService.getFaroProjectByGroupId(groupId),
				channelId, fieldMappingFieldName, filter, individualSegmentId,
				count, numberOfBins, orderByFieldsFaroParam.getValue()));
	}

	@Override
	public int[] getEntityTypes() {
		return _ENTITY_TYPES.clone();
	}

	@Override
	public FaroResultsDisplay search(
			long groupId, FaroSearchContext faroSearchContext)
		throws Exception {

		return search(
			groupId, null, null, null, null, faroSearchContext.getQuery(),
			Collections.emptyList(), faroSearchContext.getCur(),
			faroSearchContext.getDelta(), faroSearchContext.getOrderByFields());
	}

	@GET
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay search(
			@PathParam("groupId") long groupId,
			@QueryParam("channelId") String channelId,
			@QueryParam("dataSourceId") String dataSourceId,
			@QueryParam("individualSegmentId") String individualSegmentId,
			@QueryParam("filter") String filter,
			@QueryParam("query") String query,
			@DefaultValue(JSONConstants.NULL_JSON_ARRAY)
			@QueryParam("includePropertyNames")
			FaroParam
				<List<String>> includePropertyNamesFaroParam,
			@QueryParam("cur") int cur, @QueryParam("delta") int delta,
			@DefaultValue(StringPool.BLANK) @QueryParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		return search(
			groupId, channelId, dataSourceId, individualSegmentId, filter,
			query, includePropertyNamesFaroParam.getValue(), cur, delta,
			orderByFieldsFaroParam.getValue());
	}

	@Path("/search")
	@POST
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay searchByForm(
			@PathParam("groupId") long groupId,
			@FormParam("channelId") String channelId,
			@FormParam("dataSourceId") String dataSourceId,
			@FormParam("individualSegmentId") String individualSegmentId,
			@FormParam("filter") String filter,
			@FormParam("query") String query,
			@DefaultValue(JSONConstants.NULL_JSON_ARRAY)
			@FormParam("includePropertyNames")
			FaroParam
				<List<String>> includePropertyNamesFaroParam,
			@FormParam("cur") int cur, @FormParam("delta") int delta,
			@DefaultValue(StringPool.BLANK) @FormParam("orderByFields")
				FaroParam<List<OrderByField>> orderByFieldsFaroParam)
		throws Exception {

		return search(
			groupId, channelId, dataSourceId, individualSegmentId, filter,
			query, includePropertyNamesFaroParam.getValue(), cur, delta,
			orderByFieldsFaroParam.getValue());
	}

	@GET
	@Path("/field_values")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	@SuppressWarnings("unchecked")
	public FaroResultsDisplay searchValues(
			@PathParam("groupId") long groupId,
			@QueryParam("channelId") long channelId,
			@QueryParam("fieldMappingFieldName") String fieldMappingFieldName,
			@QueryParam("query") String query, @QueryParam("cur") int cur,
			@QueryParam("delta") int delta)
		throws Exception {

		return new FaroResultsDisplay(
			contactsEngineClient.getFieldValues(
				faroProjectLocalService.getFaroProjectByGroupId(groupId),
				channelId, query, fieldMappingFieldName, cur, delta));
	}

	@SuppressWarnings("unchecked")
	protected FaroResultsDisplay search(
			long groupId, String channelId, String dataSourceId,
			String individualSegmentId, String filter, String query,
			List<String> includePropertyNames, int cur, int delta,
			List<OrderByField> orderByFields)
		throws Exception {

		Results<Account> results = contactsEngineClient.getAccounts(
			faroProjectLocalService.getFaroProjectByGroupId(groupId), channelId,
			dataSourceId, individualSegmentId, filter, query,
			Collections.singletonList("accountName"), cur, delta,
			orderByFields);

		Function<Account, AccountDisplay> function = account -> {
			AccountDisplay accountDisplay = new AccountDisplay(account, null);

			accountDisplay.addProperties(includePropertyNames);

			return accountDisplay;
		};

		return new FaroResultsDisplay(results, function);
	}

	private static final int[] _ENTITY_TYPES = {FaroConstants.TYPE_ACCOUNT};

	@Reference
	private PhotoURLHelper _photoURLHelper;

}