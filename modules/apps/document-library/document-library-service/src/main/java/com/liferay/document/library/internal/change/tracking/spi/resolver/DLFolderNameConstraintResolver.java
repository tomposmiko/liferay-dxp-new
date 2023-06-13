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

package com.liferay.document.library.internal.change.tracking.spi.resolver;

import com.liferay.change.tracking.spi.resolver.ConstraintResolver;
import com.liferay.change.tracking.spi.resolver.context.ConstraintResolverContext;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import com.liferay.portal.language.LanguageResources;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Samuel Trong Tran
 */
@Component(service = ConstraintResolver.class)
public class DLFolderNameConstraintResolver
	implements ConstraintResolver<DLFolder> {

	@Override
	public String getConflictDescriptionKey() {
		return "duplicate-folder-name";
	}

	@Override
	public Class<DLFolder> getModelClass() {
		return DLFolder.class;
	}

	@Override
	public String getResolutionDescriptionKey() {
		if (_resolved) {
			return "duplicate-folder-was-removed";
		}

		return "rename-the-folder-in-the-publication";
	}

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		return LanguageResources.getResourceBundle(locale);
	}

	@Override
	public String[] getUniqueIndexColumnNames() {
		return new String[] {"groupId", "parentFolderId", "name"};
	}

	@Override
	public void resolveConflict(
		ConstraintResolverContext<DLFolder> constraintResolverContext) {

		DLFolder sourceDLFolder = constraintResolverContext.getSourceCTModel();
		DLFolder targetDLFolder = constraintResolverContext.getTargetCTModel();

		if (StringUtil.equals(
				sourceDLFolder.getName(), TempFileEntryUtil.class.getName()) &&
			StringUtil.equals(
				targetDLFolder.getName(), TempFileEntryUtil.class.getName())) {

			try {
				_dlFolderLocalService.deleteFolder(sourceDLFolder);

				_resolved = true;
			}
			catch (PortalException portalException) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to delete source document library folder " +
							sourceDLFolder.getFolderId(),
						portalException);
				}
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DLFolderNameConstraintResolver.class);

	@Reference
	private DLFolderLocalService _dlFolderLocalService;

	private boolean _resolved;

}