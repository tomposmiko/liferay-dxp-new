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

package com.liferay.fragment.renderer;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.info.form.InfoForm;
import com.liferay.info.item.InfoItemReference;

import java.util.Locale;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Jorge Ferrer
 */
@ProviderType
public interface FragmentRendererContext {

	public InfoItemReference getContextInfoItemReference();

	public String getFragmentElementId();

	public FragmentEntryLink getFragmentEntryLink();

	public InfoForm getInfoForm();

	public Locale getLocale();

	public String getMode();

	public long getPreviewClassNameId();

	public long getPreviewClassPK();

	public int getPreviewType();

	public String getPreviewVersion();

	public long[] getSegmentsEntryIds();

	public boolean isEditMode();

	public boolean isIndexMode();

	public boolean isUseCachedContent();

	public boolean isViewMode();

}