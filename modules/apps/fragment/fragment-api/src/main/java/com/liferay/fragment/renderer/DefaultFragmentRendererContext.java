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

import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.info.form.InfoForm;
import com.liferay.info.item.InfoItemReference;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.util.Locale;
import java.util.Objects;

/**
 * @author Jorge Ferrer
 */
public class DefaultFragmentRendererContext implements FragmentRendererContext {

	public DefaultFragmentRendererContext(FragmentEntryLink fragmentEntryLink) {
		_fragmentEntryLink = fragmentEntryLink;

		_fragmentEntryElementId = "fragment-" + PortalUUIDUtil.generate();
	}

	@Override
	public InfoItemReference getContextInfoItemReference() {
		return _infoItemReference;
	}

	@Override
	public String getFragmentElementId() {
		return _fragmentEntryElementId;
	}

	@Override
	public FragmentEntryLink getFragmentEntryLink() {
		return _fragmentEntryLink;
	}

	@Override
	public InfoForm getInfoForm() {
		return _infoForm;
	}

	@Override
	public Locale getLocale() {
		return _locale;
	}

	@Override
	public String getMode() {
		return _mode;
	}

	@Override
	public long getPreviewClassNameId() {
		return _previewClassNameId;
	}

	@Override
	public long getPreviewClassPK() {
		return _previewClassPK;
	}

	@Override
	public int getPreviewType() {
		return _previewType;
	}

	@Override
	public String getPreviewVersion() {
		return _previewVersion;
	}

	@Override
	public long[] getSegmentsEntryIds() {
		return _segmentsSegmentsEntryIds;
	}

	@Override
	public boolean isEditMode() {
		if (Objects.equals(getMode(), FragmentEntryLinkConstants.EDIT)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isIndexMode() {
		if (Objects.equals(getMode(), FragmentEntryLinkConstants.INDEX)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isUseCachedContent() {
		return _useCachedContent;
	}

	@Override
	public boolean isViewMode() {
		if (Objects.equals(getMode(), FragmentEntryLinkConstants.VIEW)) {
			return true;
		}

		return false;
	}

	public void setContextInfoItemReference(
		InfoItemReference infoItemReference) {

		_infoItemReference = infoItemReference;
	}

	public void setInfoForm(InfoForm infoForm) {
		_infoForm = infoForm;
	}

	public void setLocale(Locale locale) {
		_locale = locale;
	}

	public void setMode(String mode) {
		_mode = mode;
	}

	public void setPreviewClassNameId(long previewClassNameId) {
		_previewClassNameId = previewClassNameId;
	}

	public void setPreviewClassPK(long previewClassPK) {
		_previewClassPK = previewClassPK;
	}

	public void setPreviewType(int previewType) {
		_previewType = previewType;
	}

	public void setPreviewVersion(String previewVersion) {
		_previewVersion = previewVersion;
	}

	public void setSegmentsEntryIds(long[] segmentsSegmentsEntryIds) {
		_segmentsSegmentsEntryIds = segmentsSegmentsEntryIds;
	}

	public void setUseCachedContent(boolean useCachedContent) {
		_useCachedContent = useCachedContent;
	}

	private final String _fragmentEntryElementId;
	private final FragmentEntryLink _fragmentEntryLink;
	private InfoForm _infoForm;
	private InfoItemReference _infoItemReference;
	private Locale _locale = LocaleUtil.getMostRelevantLocale();
	private String _mode = FragmentEntryLinkConstants.VIEW;
	private long _previewClassNameId;
	private long _previewClassPK;
	private int _previewType;
	private String _previewVersion;
	private long[] _segmentsSegmentsEntryIds = new long[0];
	private boolean _useCachedContent = true;

}