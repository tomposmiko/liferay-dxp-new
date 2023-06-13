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

package com.liferay.data.engine.web.internal.servlet.data.fetcher;

import com.liferay.data.engine.exception.DEDataDefinitionException;
import com.liferay.data.engine.service.DEDataDefinitionRequestBuilder;
import com.liferay.data.engine.service.DEDataDefinitionService;
import com.liferay.data.engine.web.internal.graphql.model.DataDefinition;
import com.liferay.data.engine.web.internal.graphql.model.DataDefinitionType;
import com.liferay.data.engine.web.internal.graphql.model.DeleteDataDefinitionType;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(immediate = true, service = DEDeleteDataDefinitionDataFetcher.class)
public class DEDeleteDataDefinitionDataFetcher
	extends DEBaseDataFetcher implements DataFetcher<DeleteDataDefinitionType> {

	@Override
	public DeleteDataDefinitionType get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		DeleteDataDefinitionType deleteDataDefinitionType =
			new DeleteDataDefinitionType();

		String errorMessage = null;
		String languageId = dataFetchingEnvironment.getArgument("languageId");

		try {
			long dataDefinitionId = GetterUtil.getLong(
				dataFetchingEnvironment.getArgument("dataDefinitionId"));

			deDataDefinitionService.execute(
				DEDataDefinitionRequestBuilder.deleteBuilder(
				).byId(
					dataDefinitionId
				).build());

			DataDefinition dataDefinition = new DataDefinitionType();

			dataDefinition.setDataDefinitionId(
				String.valueOf(dataDefinitionId));

			deleteDataDefinitionType.setDataDefinition(dataDefinition);
		}
		catch (DEDataDefinitionException.MustHavePermission mhp) {
			errorMessage = getMessage(
				languageId, "the-user-must-have-data-definition-permission",
				getActionMessage(languageId, mhp.getActionId()));
		}
		catch (DEDataDefinitionException.NoSuchDataDefinition nsdd) {
			errorMessage = getMessage(
				languageId, "no-data-definition-exists-with-id-x",
				nsdd.getDEDataDefinitionId());
		}
		catch (Exception e) {
			errorMessage = getMessage(
				languageId, "unable-to-delete-data-definition");
		}

		if (errorMessage != null) {
			handleErrorMessage(errorMessage);
		}

		return deleteDataDefinitionType;
	}

	@Override
	public Portal getPortal() {
		return portal;
	}

	@Reference
	protected DEDataDefinitionService deDataDefinitionService;

	@Reference
	protected Portal portal;

}