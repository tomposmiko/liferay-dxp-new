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

import com.liferay.osb.faro.engine.client.constants.FieldMappingConstants;
import com.liferay.osb.faro.engine.client.model.Field;
import com.liferay.osb.faro.engine.client.model.FieldMapping;
import com.liferay.osb.faro.engine.client.model.FieldMappingMap;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.engine.client.util.OrderByField;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.controller.FaroController;
import com.liferay.osb.faro.web.internal.exception.FaroException;
import com.liferay.osb.faro.web.internal.model.display.FaroResultsDisplay;
import com.liferay.osb.faro.web.internal.model.display.contacts.FieldMappingDisplay;
import com.liferay.osb.faro.web.internal.model.display.contacts.FieldMappingValuesDisplay;
import com.liferay.osb.faro.web.internal.util.FieldMappingUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;

/**
 * @author Matthew Kong
 */
@Component(
	immediate = true,
	service = {FaroController.class, FieldMappingController.class}
)
@Path("/{groupId}/field_mapping")
@Produces(MediaType.APPLICATION_JSON)
public class FieldMappingController extends BaseFaroController {

	@Path("/defaults")
	@POST
	@RolesAllowed(StringPool.BLANK)
	public void addDefaultFieldMappings(@PathParam("groupId") long groupId)
		throws Exception {

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByGroupId(groupId);

		contactsEngineClient.addFieldMappings(
			faroProject, null, FieldMappingConstants.CONTEXT_ORGANIZATION,
			FieldMappingConstants.OWNER_TYPE_ACCOUNT,
			FieldMappingUtil.getNewFieldMappingMaps(
				contactsEngineClient, faroProject,
				FieldMappingConstants.CONTEXT_ORGANIZATION,
				FieldMappingConstants.getSalesforceAccountFieldMappingMaps()));

		List<FieldMappingMap> fieldMappingMaps = new ArrayList<>();

		fieldMappingMaps.addAll(
			FieldMappingConstants.getDefaultFieldMappingMaps());
		fieldMappingMaps.addAll(
			FieldMappingConstants.getLiferayFieldMappingMaps());
		fieldMappingMaps.addAll(
			FieldMappingConstants.getSalesforceIndividualFieldMappingMaps());

		contactsEngineClient.addFieldMappings(
			faroProject, null, FieldMappingConstants.CONTEXT_DEMOGRAPHICS,
			FieldMappingConstants.OWNER_TYPE_INDIVIDUAL,
			FieldMappingUtil.getNewFieldMappingMaps(
				contactsEngineClient, faroProject,
				FieldMappingConstants.CONTEXT_DEMOGRAPHICS, fieldMappingMaps));
	}

	@POST
	@RolesAllowed(RoleConstants.SITE_ADMINISTRATOR)
	public FieldMappingDisplay create(
			@PathParam("groupId") long groupId, @FormParam("name") String name,
			@FormParam("type") String type)
		throws Exception {

		validateCreate(name);

		return new FieldMappingDisplay(
			contactsEngineClient.addFieldMapping(
				faroProjectLocalService.getFaroProjectByGroupId(groupId),
				FieldMappingConstants.CONTEXT_DEMOGRAPHICS,
				Collections.emptyMap(), name, type,
				FieldMappingConstants.OWNER_TYPE_INDIVIDUAL, false));
	}

	@GET
	@Path("/{fieldName}")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FieldMappingDisplay get(
			@PathParam("groupId") long groupId,
			@PathParam("fieldName") String fieldName)
		throws Exception {

		return new FieldMappingDisplay(
			contactsEngineClient.getFieldMapping(
				faroProjectLocalService.getFaroProjectByGroupId(groupId),
				fieldName));
	}

	@GET
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	@SuppressWarnings("unchecked")
	public FaroResultsDisplay search(
			@PathParam("groupId") long groupId,
			@QueryParam("context") String context,
			@QueryParam("displayName") String displayName,
			@QueryParam("ownerType") String ownerType,
			@QueryParam("query") String query, @QueryParam("cur") int cur,
			@QueryParam("delta") int delta,
			@QueryParam("orderByType") String orderByType)
		throws Exception {

		List<OrderByField> orderByFields = null;

		if (Validator.isNotNull(orderByType)) {
			orderByFields = Collections.singletonList(
				new OrderByField("displayName", orderByType, true));
		}

		Results<FieldMapping> results = contactsEngineClient.getFieldMappings(
			faroProjectLocalService.getFaroProjectByGroupId(groupId), context,
			displayName, ownerType, query, cur, delta, orderByFields);

		Function<FieldMapping, FieldMappingDisplay> function =
			FieldMappingDisplay::new;

		return new FaroResultsDisplay(results, function);
	}

	@Path("/search")
	@POST
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public FaroResultsDisplay searchByForm(
			@PathParam("groupId") long groupId,
			@FormParam("context") String context,
			@FormParam("displayName") String displayName,
			@FormParam("ownerType") String ownerType,
			@FormParam("query") String query, @FormParam("cur") int cur,
			@FormParam("delta") int delta,
			@FormParam("orderByType") String orderByType)
		throws Exception {

		return search(
			groupId, context, displayName, ownerType, query, cur, delta,
			orderByType);
	}

	@GET
	@Path("/suggestions")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	@SuppressWarnings("unchecked")
	public FaroResultsDisplay searchSuggestions(
			@PathParam("groupId") long groupId,
			@QueryParam("query") String query, @QueryParam("cur") int cur,
			@QueryParam("delta") int delta)
		throws Exception {

		List<FieldMappingValuesDisplay> fieldMappingValuesDisplays =
			new ArrayList<>();

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByGroupId(groupId);

		Results<FieldMapping> results = contactsEngineClient.getFieldMappings(
			faroProject, FieldMappingConstants.CONTEXT_DEMOGRAPHICS, null, null,
			query, cur, delta, null);

		List<FieldMapping> fieldMappings = results.getItems();

		Stream<FieldMapping> stream = fieldMappings.stream();

		List<List<Field>> fieldsList = contactsEngineClient.getFieldsList(
			faroProject, FieldMappingConstants.CONTEXT_DEMOGRAPHICS,
			stream.map(
				FieldMapping::getFieldName
			).collect(
				Collectors.toList()
			),
			1, 1, null);

		for (int i = 0; i < fieldMappings.size(); i++) {
			fieldMappingValuesDisplays.add(
				new FieldMappingValuesDisplay(
					fieldMappings.get(i), fieldsList.get(i)));
		}

		return new FaroResultsDisplay(
			fieldMappingValuesDisplays, results.getTotal());
	}

	protected void validateCreate(String name) {
		Matcher matcher = _pattern.matcher(name);

		if (!matcher.find()) {
			throw new FaroException("Invalid field mapping name: " + name);
		}
	}

	private static final Pattern _pattern = Pattern.compile(
		"^[A-Za-z_][\\w]{0,126}[A-Za-z0-9]$");

}