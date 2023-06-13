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

package com.liferay.dynamic.data.mapping.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.ShardedModel;

/**
 * The base model interface for the DDMDataProviderInstanceLink service. Represents a row in the &quot;DDMDataProviderInstanceLink&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.dynamic.data.mapping.model.impl.DDMDataProviderInstanceLinkModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.dynamic.data.mapping.model.impl.DDMDataProviderInstanceLinkImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DDMDataProviderInstanceLink
 * @generated
 */
@ProviderType
public interface DDMDataProviderInstanceLinkModel extends BaseModel<DDMDataProviderInstanceLink>,
	ShardedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a ddm data provider instance link model instance should use the {@link DDMDataProviderInstanceLink} interface instead.
	 */

	/**
	 * Returns the primary key of this ddm data provider instance link.
	 *
	 * @return the primary key of this ddm data provider instance link
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this ddm data provider instance link.
	 *
	 * @param primaryKey the primary key of this ddm data provider instance link
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the data provider instance link ID of this ddm data provider instance link.
	 *
	 * @return the data provider instance link ID of this ddm data provider instance link
	 */
	public long getDataProviderInstanceLinkId();

	/**
	 * Sets the data provider instance link ID of this ddm data provider instance link.
	 *
	 * @param dataProviderInstanceLinkId the data provider instance link ID of this ddm data provider instance link
	 */
	public void setDataProviderInstanceLinkId(long dataProviderInstanceLinkId);

	/**
	 * Returns the company ID of this ddm data provider instance link.
	 *
	 * @return the company ID of this ddm data provider instance link
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this ddm data provider instance link.
	 *
	 * @param companyId the company ID of this ddm data provider instance link
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the data provider instance ID of this ddm data provider instance link.
	 *
	 * @return the data provider instance ID of this ddm data provider instance link
	 */
	public long getDataProviderInstanceId();

	/**
	 * Sets the data provider instance ID of this ddm data provider instance link.
	 *
	 * @param dataProviderInstanceId the data provider instance ID of this ddm data provider instance link
	 */
	public void setDataProviderInstanceId(long dataProviderInstanceId);

	/**
	 * Returns the structure ID of this ddm data provider instance link.
	 *
	 * @return the structure ID of this ddm data provider instance link
	 */
	public long getStructureId();

	/**
	 * Sets the structure ID of this ddm data provider instance link.
	 *
	 * @param structureId the structure ID of this ddm data provider instance link
	 */
	public void setStructureId(long structureId);
}