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

package com.liferay.document.library.model;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.model.ShardedModel;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the DLStorageQuota service. Represents a row in the &quot;DLStorageQuota&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.document.library.model.impl.DLStorageQuotaModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.document.library.model.impl.DLStorageQuotaImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DLStorageQuota
 * @generated
 */
@ProviderType
public interface DLStorageQuotaModel
	extends BaseModel<DLStorageQuota>, MVCCModel, ShardedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a dl storage quota model instance should use the {@link DLStorageQuota} interface instead.
	 */

	/**
	 * Returns the primary key of this dl storage quota.
	 *
	 * @return the primary key of this dl storage quota
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this dl storage quota.
	 *
	 * @param primaryKey the primary key of this dl storage quota
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this dl storage quota.
	 *
	 * @return the mvcc version of this dl storage quota
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this dl storage quota.
	 *
	 * @param mvccVersion the mvcc version of this dl storage quota
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the dl storage quota ID of this dl storage quota.
	 *
	 * @return the dl storage quota ID of this dl storage quota
	 */
	public long getDlStorageQuotaId();

	/**
	 * Sets the dl storage quota ID of this dl storage quota.
	 *
	 * @param dlStorageQuotaId the dl storage quota ID of this dl storage quota
	 */
	public void setDlStorageQuotaId(long dlStorageQuotaId);

	/**
	 * Returns the company ID of this dl storage quota.
	 *
	 * @return the company ID of this dl storage quota
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this dl storage quota.
	 *
	 * @param companyId the company ID of this dl storage quota
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the storage size of this dl storage quota.
	 *
	 * @return the storage size of this dl storage quota
	 */
	public long getStorageSize();

	/**
	 * Sets the storage size of this dl storage quota.
	 *
	 * @param storageSize the storage size of this dl storage quota
	 */
	public void setStorageSize(long storageSize);

	@Override
	public DLStorageQuota cloneWithOriginalValues();

	public default String toXmlString() {
		return null;
	}

}