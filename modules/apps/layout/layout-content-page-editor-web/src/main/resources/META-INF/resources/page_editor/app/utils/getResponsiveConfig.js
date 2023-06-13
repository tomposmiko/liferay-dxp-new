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

import {VIEWPORT_SIZES} from '../config/constants/viewportSizes';

const ORDERED_VIEWPORT_SIZES = [
	VIEWPORT_SIZES.desktop,
	VIEWPORT_SIZES.tablet,
	VIEWPORT_SIZES.landscapeMobile,
	VIEWPORT_SIZES.portraitMobile,
];

export function getResponsiveConfig(config, viewportSize) {
	const viewportSizeIndex = ORDERED_VIEWPORT_SIZES.indexOf(viewportSize);

	let responsiveConfig = {};

	for (let i = 0; i <= viewportSizeIndex; i++) {
		const viewPortSizeConfig =
			ORDERED_VIEWPORT_SIZES[i] === VIEWPORT_SIZES.desktop
				? config
				: config[ORDERED_VIEWPORT_SIZES[i]];

		responsiveConfig = {
			...responsiveConfig,
			...viewPortSizeConfig,
			gutters: config.gutters,
			styles: {
				...responsiveConfig.styles,
				...viewPortSizeConfig.styles,
			},
		};
	}

	return responsiveConfig;
}
