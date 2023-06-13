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

package com.liferay.adaptive.media.image.internal.handler;

import com.liferay.adaptive.media.AMAttribute;
import com.liferay.adaptive.media.AdaptiveMedia;
import com.liferay.adaptive.media.exception.AMRuntimeException;
import com.liferay.adaptive.media.handler.AMRequestHandler;
import com.liferay.adaptive.media.image.configuration.AMImageConfigurationEntry;
import com.liferay.adaptive.media.image.configuration.AMImageConfigurationHelper;
import com.liferay.adaptive.media.image.finder.AMImageFinder;
import com.liferay.adaptive.media.image.internal.configuration.AMImageAttributeMapping;
import com.liferay.adaptive.media.image.internal.processor.AMImage;
import com.liferay.adaptive.media.image.internal.util.Tuple;
import com.liferay.adaptive.media.image.processor.AMImageAttribute;
import com.liferay.adaptive.media.image.processor.AMImageProcessor;
import com.liferay.adaptive.media.processor.AMAsyncProcessor;
import com.liferay.adaptive.media.processor.AMAsyncProcessorLocator;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.IOException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 * @author Alejandro Tardín
 */
@Component(
	property = "adaptive.media.handler.pattern=image",
	service = AMRequestHandler.class
)
public class AMImageRequestHandler
	implements AMRequestHandler<AMImageProcessor> {

	@Override
	public AdaptiveMedia<AMImageProcessor> handleRequest(
			HttpServletRequest httpServletRequest)
		throws IOException, ServletException {

		Tuple<FileVersion, AMImageAttributeMapping> interpretedPath =
			_interpretPath(httpServletRequest.getPathInfo());

		if (interpretedPath == null) {
			return null;
		}

		AdaptiveMedia<AMImageProcessor> adaptiveMedia = _findAdaptiveMedia(
			interpretedPath.first, interpretedPath.second);

		if (adaptiveMedia != null) {
			_processAMImage(
				adaptiveMedia, interpretedPath.first, interpretedPath.second);
		}

		return adaptiveMedia;
	}

	private AdaptiveMedia<AMImageProcessor> _createRawAdaptiveMedia(
		FileVersion fileVersion) {

		return new AMImage(
			() -> {
				try {
					return fileVersion.getContentStream(false);
				}
				catch (PortalException portalException) {
					throw new AMRuntimeException(portalException);
				}
			},
			AMImageAttributeMapping.fromFileVersion(fileVersion), null);
	}

	private AdaptiveMedia<AMImageProcessor> _findAdaptiveMedia(
		FileVersion fileVersion,
		AMImageAttributeMapping amImageAttributeMapping) {

		try {
			String configurationUuid = amImageAttributeMapping.getValue(
				AMAttribute.getConfigurationUuidAMAttribute());

			if (configurationUuid == null) {
				return null;
			}

			AMImageConfigurationEntry amImageConfigurationEntry =
				_amImageConfigurationHelper.getAMImageConfigurationEntry(
					fileVersion.getCompanyId(), configurationUuid);

			if (amImageConfigurationEntry == null) {
				return null;
			}

			Optional<AdaptiveMedia<AMImageProcessor>> adaptiveMediaOptional =
				_findExactAdaptiveMedia(fileVersion, amImageConfigurationEntry);

			if (adaptiveMediaOptional.isPresent()) {
				return adaptiveMediaOptional.get();
			}

			adaptiveMediaOptional = _findClosestAdaptiveMedia(
				fileVersion, amImageConfigurationEntry);

			if (adaptiveMediaOptional.isPresent()) {
				return adaptiveMediaOptional.get();
			}

			return _createRawAdaptiveMedia(fileVersion);
		}
		catch (PortalException portalException) {
			throw new AMRuntimeException(portalException);
		}
	}

	private Optional<AdaptiveMedia<AMImageProcessor>> _findClosestAdaptiveMedia(
		FileVersion fileVersion,
		AMImageConfigurationEntry amImageConfigurationEntry) {

		Map<String, String> properties =
			amImageConfigurationEntry.getProperties();

		Integer configurationWidth = GetterUtil.getInteger(
			properties.get("max-width"));

		Integer configurationHeight = GetterUtil.getInteger(
			properties.get("max-height"));

		try {
			List<AdaptiveMedia<AMImageProcessor>> adaptiveMedias =
				_amImageFinder.getAdaptiveMedias(
					amImageQueryBuilder -> amImageQueryBuilder.forFileVersion(
						fileVersion
					).with(
						AMImageAttribute.AM_IMAGE_ATTRIBUTE_WIDTH,
						configurationWidth
					).with(
						AMImageAttribute.AM_IMAGE_ATTRIBUTE_HEIGHT,
						configurationHeight
					).done());

			if (adaptiveMedias.isEmpty()) {
				return Optional.empty();
			}

			Collections.sort(
				adaptiveMedias, _getComparator(configurationWidth));

			return Optional.of(adaptiveMedias.get(0));
		}
		catch (PortalException portalException) {
			throw new AMRuntimeException(portalException);
		}
	}

	private Optional<AdaptiveMedia<AMImageProcessor>> _findExactAdaptiveMedia(
			FileVersion fileVersion,
			AMImageConfigurationEntry amImageConfigurationEntry)
		throws PortalException {

		List<AdaptiveMedia<AMImageProcessor>> adaptiveMedias =
			_amImageFinder.getAdaptiveMedias(
				amImageQueryBuilder -> amImageQueryBuilder.forFileVersion(
					fileVersion
				).forConfiguration(
					amImageConfigurationEntry.getUUID()
				).done());

		if (adaptiveMedias.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(adaptiveMedias.get(0));
	}

	private Comparator<AdaptiveMedia<AMImageProcessor>> _getComparator(
		Integer configurationWidth) {

		return Comparator.comparingInt(
			adaptiveMedia -> _getDistance(configurationWidth, adaptiveMedia));
	}

	private Integer _getDistance(
		int width, AdaptiveMedia<AMImageProcessor> adaptiveMedia) {

		Integer imageWidth = adaptiveMedia.getValue(
			AMImageAttribute.AM_IMAGE_ATTRIBUTE_WIDTH);

		if (imageWidth == null) {
			return Integer.MAX_VALUE;
		}

		return Math.abs(imageWidth - width);
	}

	private Tuple<FileVersion, AMImageAttributeMapping> _interpretPath(
		String pathInfo) {

		try {
			Tuple<FileVersion, Map<String, String>> fileVersionPropertiesTuple =
				_pathInterpreter.interpretPath(pathInfo);

			if (fileVersionPropertiesTuple == null) {
				return null;
			}

			FileVersion fileVersion = fileVersionPropertiesTuple.first;

			if (fileVersion.getStatus() == WorkflowConstants.STATUS_IN_TRASH) {
				return null;
			}

			Map<String, String> properties = fileVersionPropertiesTuple.second;

			AMAttribute<Object, Long> contentLengthAMAttribute =
				AMAttribute.getContentLengthAMAttribute();

			properties.put(
				contentLengthAMAttribute.getName(),
				String.valueOf(fileVersion.getSize()));

			AMAttribute<Object, String> contentTypeAMAttribute =
				AMAttribute.getContentTypeAMAttribute();

			properties.put(
				contentTypeAMAttribute.getName(), fileVersion.getMimeType());

			AMAttribute<Object, String> fileNameAMAttribute =
				AMAttribute.getFileNameAMAttribute();

			properties.put(
				fileNameAMAttribute.getName(), fileVersion.getFileName());

			AMImageAttributeMapping amImageAttributeMapping =
				AMImageAttributeMapping.fromProperties(properties);

			return Tuple.of(fileVersion, amImageAttributeMapping);
		}
		catch (AMRuntimeException | NumberFormatException exception) {
			_log.error(exception);

			return null;
		}
	}

	private void _processAMImage(
		AdaptiveMedia<AMImageProcessor> adaptiveMedia, FileVersion fileVersion,
		AMImageAttributeMapping amImageAttributeMapping) {

		String adaptiveMediaConfigurationUuid = adaptiveMedia.getValue(
			AMAttribute.getConfigurationUuidAMAttribute());

		String attributeMappingConfigurationUuid =
			amImageAttributeMapping.getValue(
				AMAttribute.getConfigurationUuidAMAttribute());

		if (Objects.equals(
				adaptiveMediaConfigurationUuid,
				attributeMappingConfigurationUuid)) {

			return;
		}

		try {
			AMAsyncProcessor<FileVersion, ?> amAsyncProcessor =
				_amAsyncProcessorLocator.locateForClass(FileVersion.class);

			amAsyncProcessor.triggerProcess(
				fileVersion, String.valueOf(fileVersion.getFileVersionId()));
		}
		catch (PortalException portalException) {
			_log.error(
				"Unable to create lazy adaptive media for file version " +
					fileVersion.getFileVersionId(),
				portalException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AMImageRequestHandler.class);

	@Reference
	private AMAsyncProcessorLocator _amAsyncProcessorLocator;

	@Reference
	private AMImageConfigurationHelper _amImageConfigurationHelper;

	@Reference
	private AMImageFinder _amImageFinder;

	@Reference
	private PathInterpreter _pathInterpreter;

}