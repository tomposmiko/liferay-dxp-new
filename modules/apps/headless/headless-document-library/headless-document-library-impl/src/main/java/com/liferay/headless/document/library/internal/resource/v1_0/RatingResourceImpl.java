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

package com.liferay.headless.document.library.internal.resource.v1_0;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.headless.common.spi.resource.SPIRatingResource;
import com.liferay.headless.document.library.dto.v1_0.Rating;
import com.liferay.headless.document.library.internal.dto.v1_0.util.RatingUtil;
import com.liferay.headless.document.library.resource.v1_0.RatingResource;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.ratings.kernel.service.RatingsEntryLocalService;

import javax.ws.rs.core.Context;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/rating.properties",
	scope = ServiceScope.PROTOTYPE, service = RatingResource.class
)
public class RatingResourceImpl extends BaseRatingResourceImpl {

	@Override
	public void deleteRating(Long ratingId) throws Exception {
		SPIRatingResource<Rating> spiRatingResource = _getSPIRatingResource();

		spiRatingResource.deleteRating(ratingId);
	}

	@Override
	public Page<Rating> getDocumentRatingsPage(Long documentId)
		throws Exception {

		SPIRatingResource<Rating> spiRatingResource = _getSPIRatingResource();

		return spiRatingResource.getRatingsPage(
			DLFileEntry.class.getName(), documentId);
	}

	@Override
	public Rating getRating(Long ratingId) throws Exception {
		SPIRatingResource<Rating> spiRatingResource = _getSPIRatingResource();

		return spiRatingResource.getRating(ratingId);
	}

	@Override
	public Rating postDocumentRating(Long documentId, Rating rating)
		throws Exception {

		SPIRatingResource<Rating> spiRatingResource = _getSPIRatingResource();

		return spiRatingResource.postRating(
			DLFileEntry.class.getName(), documentId,
			GetterUtil.getDouble(rating.getRatingValue()));
	}

	@Override
	public Rating putRating(Long ratingId, Rating rating) throws Exception {
		SPIRatingResource<Rating> spiRatingResource = _getSPIRatingResource();

		return spiRatingResource.putRating(
			ratingId, GetterUtil.getDouble(rating.getRatingValue()));
	}

	private SPIRatingResource<Rating> _getSPIRatingResource() {
		return new SPIRatingResource<>(
			_ratingsEntryLocalService,
			ratingsEntry -> RatingUtil.toRating(
				_portal, ratingsEntry, _userLocalService),
			_user);
	}

	@Reference
	private Portal _portal;

	@Reference
	private RatingsEntryLocalService _ratingsEntryLocalService;

	@Context
	private User _user;

	@Reference
	private UserLocalService _userLocalService;

}