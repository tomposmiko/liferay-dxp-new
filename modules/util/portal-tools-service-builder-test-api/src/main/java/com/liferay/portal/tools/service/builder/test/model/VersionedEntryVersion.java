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

package com.liferay.portal.tools.service.builder.test.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.util.Accessor;

/**
 * The extended model interface for the VersionedEntryVersion service. Represents a row in the &quot;VersionedEntryVersion&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see VersionedEntryVersionModel
 * @see com.liferay.portal.tools.service.builder.test.model.impl.VersionedEntryVersionImpl
 * @see com.liferay.portal.tools.service.builder.test.model.impl.VersionedEntryVersionModelImpl
 * @generated
 */
@ImplementationClassName("com.liferay.portal.tools.service.builder.test.model.impl.VersionedEntryVersionImpl")
@ProviderType
public interface VersionedEntryVersion extends VersionedEntryVersionModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to {@link com.liferay.portal.tools.service.builder.test.model.impl.VersionedEntryVersionImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<VersionedEntryVersion, Long> VERSIONED_ENTRY_VERSION_ID_ACCESSOR =
		new Accessor<VersionedEntryVersion, Long>() {
			@Override
			public Long get(VersionedEntryVersion versionedEntryVersion) {
				return versionedEntryVersion.getVersionedEntryVersionId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<VersionedEntryVersion> getTypeClass() {
				return VersionedEntryVersion.class;
			}
		};
}