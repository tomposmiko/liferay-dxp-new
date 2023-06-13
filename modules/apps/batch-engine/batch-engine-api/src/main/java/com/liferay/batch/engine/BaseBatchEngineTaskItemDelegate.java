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

package com.liferay.batch.engine;

import com.liferay.batch.engine.strategy.BatchEngineImportStrategy;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.odata.entity.EntityModel;

import java.io.Serializable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.UriInfo;

/**
 * @author Ivica Cardic
 * @author Igor Beslic
 */
public abstract class BaseBatchEngineTaskItemDelegate<T>
	implements BatchEngineTaskItemDelegate<T> {

	@Override
	public void create(
			Collection<T> items, Map<String, Serializable> parameters)
		throws Exception {

		batchEngineImportStrategy.apply(
			items, item -> createItem(item, parameters));
	}

	public void createItem(T item, Map<String, Serializable> parameters)
		throws Exception {
	}

	@Override
	public void delete(
			Collection<T> items, Map<String, Serializable> parameters)
		throws Exception {

		for (T item : items) {
			deleteItem(item, parameters);
		}
	}

	public void deleteItem(T item, Map<String, Serializable> parameters)
		throws Exception {
	}

	@Override
	public Set<String> getAvailableCreateStrategies() {
		return _availableCreateStrategies;
	}

	@Override
	public Set<String> getAvailableUpdateStrategies() {
		return _availableUpdateStrategies;
	}

	@Override
	public EntityModel getEntityModel(Map<String, List<String>> multivaluedMap)
		throws Exception {

		return null;
	}

	@Override
	public boolean hasCreateStrategy(String createStrategy) {
		if (_availableCreateStrategies.contains(createStrategy)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean hasUpdateStrategy(String updateStrategy) {
		if (_availableUpdateStrategies.contains(updateStrategy)) {
			return true;
		}

		return false;
	}

	@Override
	public void setBatchEngineImportStrategy(
		BatchEngineImportStrategy batchEngineImportStrategy) {

		this.batchEngineImportStrategy = batchEngineImportStrategy;
	}

	@Override
	public void setContextCompany(Company contextCompany) {
		this.contextCompany = contextCompany;
	}

	@Override
	public void setContextUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	@Override
	public void setContextUser(User contextUser) {
		this.contextUser = contextUser;
	}

	@Override
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	@Override
	public void update(
			Collection<T> items, Map<String, Serializable> parameters)
		throws Exception {

		for (T item : items) {
			updateItem(item, parameters);
		}
	}

	public void updateItem(T item, Map<String, Serializable> parameters)
		throws Exception {
	}

	protected BatchEngineImportStrategy batchEngineImportStrategy;
	protected Company contextCompany;
	protected User contextUser;
	protected String languageId;
	protected UriInfo uriInfo;

	private final Set<String> _availableCreateStrategies =
		Collections.unmodifiableSet(SetUtil.fromArray("INSERT"));
	private final Set<String> _availableUpdateStrategies =
		Collections.unmodifiableSet(SetUtil.fromArray("UPDATE"));

}