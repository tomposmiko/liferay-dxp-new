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

import com.liferay.osb.faro.contacts.model.ContactsLayoutTemplate;
import com.liferay.osb.faro.contacts.model.constants.JSONConstants;
import com.liferay.osb.faro.contacts.service.ContactsCardTemplateLocalService;
import com.liferay.osb.faro.contacts.service.ContactsLayoutTemplateLocalService;
import com.liferay.osb.faro.web.internal.card.template.ContactsCardTemplateManagerHelper;
import com.liferay.osb.faro.web.internal.constants.FaroConstants;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.controller.FaroController;
import com.liferay.osb.faro.web.internal.exception.FaroException;
import com.liferay.osb.faro.web.internal.model.display.contacts.card.template.ContactsCardTemplateDisplay;
import com.liferay.osb.faro.web.internal.model.display.contacts.card.template.ContactsLayoutTemplateDisplay;
import com.liferay.osb.faro.web.internal.model.display.contacts.card.template.ContactsLayoutTemplateSettingDisplay;
import com.liferay.osb.faro.web.internal.param.FaroParam;
import com.liferay.osb.faro.web.internal.util.ContactsLayoutHelper;
import com.liferay.osb.faro.web.internal.util.JSONUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
	immediate = true,
	service = {ContactsLayoutTemplateController.class, FaroController.class}
)
@Path("/{groupId}/contacts_layout_template")
@Produces(MediaType.APPLICATION_JSON)
public class ContactsLayoutTemplateController extends BaseFaroController {

	@POST
	@RolesAllowed(StringPool.BLANK)
	public ContactsLayoutTemplateDisplay create(
			@PathParam("groupId") long groupId,
			@DefaultValue(JSONConstants.NULL_JSON_ARRAY) @FormParam("settings")
				FaroParam<List<List<ContactsLayoutTemplateSettingDisplay>>>
					contactsLayoutTemplateSettingDisplayFaroParam,
			@FormParam("name") String name, @FormParam("type") int type)
		throws Exception {

		validateUpdateContactsCardTemplate(
			contactsLayoutTemplateSettingDisplayFaroParam.getValue(), type);

		ContactsLayoutTemplate contactsLayoutTemplate =
			_contactsLayoutTemplateLocalService.addContactsLayoutTemplate(
				groupId, getUserId(),
				_contactsLayoutHelper.addHeaderContactsCardTemplateIds(
					groupId, type),
				name,
				JSONUtil.writeValueAsString(
					contactsLayoutTemplateSettingDisplayFaroParam.getValue()),
				type);

		return new ContactsLayoutTemplateDisplay(
			contactsLayoutTemplate, _contactsCardTemplateManagerUtil);
	}

	@DELETE
	@Path("/{id}")
	@RolesAllowed(StringPool.BLANK)
	public ContactsLayoutTemplateDisplay delete(@PathParam("id") long id)
		throws Exception {

		return new ContactsLayoutTemplateDisplay(
			_contactsLayoutTemplateLocalService.deleteContactsLayoutTemplate(
				id),
			_contactsCardTemplateManagerUtil);
	}

	@GET
	@Path("/{id}")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public ContactsLayoutTemplateDisplay get(@PathParam("id") long id)
		throws Exception {

		return new ContactsLayoutTemplateDisplay(
			_contactsLayoutTemplateLocalService.getContactsLayoutTemplate(id),
			_contactsCardTemplateManagerUtil);
	}

	@GET
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public List<ContactsLayoutTemplateDisplay> getList(
		@PathParam("groupId") long groupId, @QueryParam("type") int type) {

		List<ContactsLayoutTemplate> contactsLayoutTemplates =
			_contactsLayoutTemplateLocalService.getContactsLayoutTemplates(
				groupId, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		Stream<ContactsLayoutTemplate> stream =
			contactsLayoutTemplates.stream();

		return stream.map(
			this::getLayoutTemplateDisplay
		).filter(
			Objects::nonNull
		).collect(
			Collectors.toList()
		);
	}

	@Path("/{id}")
	@PUT
	@RolesAllowed(StringPool.BLANK)
	public ContactsLayoutTemplateDisplay update(
			@PathParam("groupId") long groupId, @PathParam("id") long id,
			@DefaultValue(JSONConstants.NULL_JSON_ARRAY) @FormParam("settings")
				FaroParam<List<List<ContactsLayoutTemplateSettingDisplay>>>
					contactsLayoutTemplateSettingDisplayFaroParam,
			@FormParam("name") String name, @FormParam("type") int type)
		throws Exception {

		if (id == 0) {
			return create(
				groupId, contactsLayoutTemplateSettingDisplayFaroParam, name,
				type);
		}

		validateUpdateContactsCardTemplate(
			contactsLayoutTemplateSettingDisplayFaroParam.getValue(), type);

		ContactsLayoutTemplate contactsLayoutTemplate =
			_contactsLayoutTemplateLocalService.getContactsLayoutTemplate(id);

		contactsLayoutTemplate.setName(name);
		contactsLayoutTemplate.setSettings(
			JSONUtil.writeValueAsString(
				contactsLayoutTemplateSettingDisplayFaroParam.getValue()));
		contactsLayoutTemplate.setType(type);

		return new ContactsLayoutTemplateDisplay(
			_contactsLayoutTemplateLocalService.updateContactsLayoutTemplate(
				contactsLayoutTemplate),
			_contactsCardTemplateManagerUtil);
	}

	protected ContactsLayoutTemplateDisplay getLayoutTemplateDisplay(
		ContactsLayoutTemplate contactsLayoutTemplate) {

		try {
			return new ContactsLayoutTemplateDisplay(
				contactsLayoutTemplate, _contactsCardTemplateManagerUtil);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			return null;
		}
	}

	protected void validateUpdateContactsCardTemplate(
			List<List<ContactsLayoutTemplateSettingDisplay>>
				contactsLayoutTemplateSettingDisplaysList,
			int type)
		throws Exception {

		for (List<ContactsLayoutTemplateSettingDisplay>
				contactsLayoutTemplateSettingDisplays :
					contactsLayoutTemplateSettingDisplaysList) {

			int totalSize = 0;

			for (ContactsLayoutTemplateSettingDisplay
					contactsLayoutTemplateSettingDisplay :
						contactsLayoutTemplateSettingDisplays) {

				ContactsCardTemplateDisplay contactsCardTemplateDisplay =
					_contactsCardTemplateManagerUtil.
						getContactsCardTemplateDisplay(
							_contactsCardTemplateLocalService.
								getContactsCardTemplate(
									contactsLayoutTemplateSettingDisplay.
										getContactsCardTemplateId()));

				if (!ArrayUtil.contains(
						contactsCardTemplateDisplay.getSupportedSizes(),
						contactsLayoutTemplateSettingDisplay.getSize())) {

					throw new FaroException(
						"The size is unsupported: " +
							contactsLayoutTemplateSettingDisplay.getSize());
				}

				totalSize += contactsLayoutTemplateSettingDisplay.getSize();

				if (totalSize > 4) {
					throw new FaroException(
						"The row has exceeded the maximum size: " + totalSize);
				}
			}
		}

		Map<String, Integer> types = FaroConstants.getTypes();

		if (!types.containsValue(type)) {
			throw new FaroException("Invalid layout template type: " + type);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ContactsLayoutTemplateController.class);

	@Reference
	private ContactsCardTemplateLocalService _contactsCardTemplateLocalService;

	@Reference
	private ContactsCardTemplateManagerHelper _contactsCardTemplateManagerUtil;

	@Reference
	private ContactsLayoutHelper _contactsLayoutHelper;

	@Reference
	private ContactsLayoutTemplateLocalService
		_contactsLayoutTemplateLocalService;

}