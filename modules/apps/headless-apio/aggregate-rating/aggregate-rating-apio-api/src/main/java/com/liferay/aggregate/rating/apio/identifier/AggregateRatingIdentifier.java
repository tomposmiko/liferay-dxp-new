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

package com.liferay.aggregate.rating.apio.identifier;

import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.portal.apio.identifier.ClassNameClassPK;

/**
 * Holds information about an {@code AggregateRating} identifier. It's
 * identified by a {@link ClassNameClassPK} instance, that can be created either
 * through {@code
 * ClassNameClassPK#create(com.liferay.portal.kernel.model.ClassedModel)} or
 * {@link ClassNameClassPK#create(String, long)}.
 *
 * @author Javier Gamarra
 * @review
 */
public interface AggregateRatingIdentifier
	extends Identifier<ClassNameClassPK> {
}