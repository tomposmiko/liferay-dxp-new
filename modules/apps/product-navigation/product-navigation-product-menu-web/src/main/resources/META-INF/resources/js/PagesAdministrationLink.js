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

import ClayLink from '@clayui/link';
import PropTypes from 'prop-types';
import React from 'react';

export default function PagesAdministrationLink({
	administrationPortletURL,
	hasAdministrationPortletPermission,
}) {
	return (
		hasAdministrationPortletPermission && (
			<div className="pages-administration-link">
				<ClayLink className="ml-2" href={administrationPortletURL}>
					{Liferay.Language.get('go-to-pages-administration')}
				</ClayLink>
			</div>
		)
	);
}

PagesAdministrationLink.propTypes = {
	administrationPortletURL: PropTypes.array.isRequired,
	hasAdministrationPortletPermission: PropTypes.bool.isRequired,
};
