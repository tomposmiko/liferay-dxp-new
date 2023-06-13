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

package com.liferay.segments.criteria.contributor;

import aQute.bnd.annotation.ConsumerType;

import com.liferay.segments.criteria.Criteria;
import com.liferay.segments.criteria.Field;

import java.util.List;
import java.util.Locale;

import javax.portlet.PortletRequest;

/**
 * Provides an interface for extending the segment {@link Criteria} by adding
 * more filters.
 *
 * @author Eduardo García
 * @review
 */
@ConsumerType
public interface SegmentsCriteriaContributor {

	/**
	 * Contributes a criterion to a segment criteria.
	 *
	 * @param criteria the criteria
	 * @param filterString the filter of the criterion as a string
	 * @param conjunction the conjunction of the criterion
	 * @review
	 */
	public default void contribute(
		Criteria criteria, String filterString,
		Criteria.Conjunction conjunction) {

		criteria.addCriterion(getKey(), getType(), filterString, conjunction);
		criteria.addFilter(getType(), filterString, conjunction);
	}

	/**
	 * Returns the contributed criterion from a criteria.
	 *
	 * @param  criteria the criteria
	 * @return the contributed criterion.
	 * @review
	 */
	public default Criteria.Criterion getCriterion(Criteria criteria) {
		return criteria.getCriterion(getKey());
	}

	/**
	 * Returns the list of fields that are supported by this contributor.
	 *
	 * @param  portletRequest the portlet request
	 * @return the list of fields that are supported by this contributor
	 * @review
	 */
	public List<Field> getFields(PortletRequest portletRequest);

	/**
	 * Returns the contributor's key. This key must be unique.
	 *
	 * @return the contributor's key
	 * @review
	 */
	public String getKey();

	/**
	 * Returns the label displayed in the user interface.
	 *
	 * @return the label
	 * @review
	 */
	public String getLabel(Locale locale);

	/**
	 * Returns the contributor's type. See {@link Criteria.Type}.
	 *
	 * @return the contributor's type
	 * @review
	 */
	public Criteria.Type getType();

}