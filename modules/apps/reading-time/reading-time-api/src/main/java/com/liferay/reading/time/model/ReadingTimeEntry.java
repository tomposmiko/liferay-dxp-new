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

package com.liferay.reading.time.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

/**
 * The extended model interface for the ReadingTimeEntry service. Represents a row in the &quot;ReadingTimeEntry&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see ReadingTimeEntryModel
 * @see com.liferay.reading.time.model.impl.ReadingTimeEntryImpl
 * @see com.liferay.reading.time.model.impl.ReadingTimeEntryModelImpl
 * @generated
 */
@ImplementationClassName("com.liferay.reading.time.model.impl.ReadingTimeEntryImpl")
@ProviderType
public interface ReadingTimeEntry extends ReadingTimeEntryModel, PersistedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to {@link com.liferay.reading.time.model.impl.ReadingTimeEntryImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<ReadingTimeEntry, Long> READING_TIME_ENTRY_ID_ACCESSOR =
		new Accessor<ReadingTimeEntry, Long>() {
			@Override
			public Long get(ReadingTimeEntry readingTimeEntry) {
				return readingTimeEntry.getReadingTimeEntryId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<ReadingTimeEntry> getTypeClass() {
				return ReadingTimeEntry.class;
			}
		};
}