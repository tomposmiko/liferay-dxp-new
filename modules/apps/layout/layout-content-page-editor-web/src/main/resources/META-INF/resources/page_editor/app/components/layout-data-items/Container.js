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

import classNames from 'classnames';
import React, {useContext, useEffect, useState} from 'react';

import {LAYOUT_DATA_ITEM_DEFAULT_CONFIGURATIONS} from '../../config/constants/layoutDataItemDefaultConfigurations';
import {LAYOUT_DATA_ITEM_TYPES} from '../../config/constants/layoutDataItemTypes';
import {ConfigContext} from '../../config/index';
import InfoItemService from '../../services/InfoItemService';
import {useDispatch} from '../../store/index';

const Container = React.forwardRef(({children, className, item}, ref) => {
	const {
		backgroundColorCssClass,
		backgroundImage,
		paddingBottom,
		paddingHorizontal,
		paddingTop,
		type
	} = {
		...LAYOUT_DATA_ITEM_DEFAULT_CONFIGURATIONS[
			LAYOUT_DATA_ITEM_TYPES.container
		],
		...item.config
	};

	const config = useContext(ConfigContext);
	const dispatch = useDispatch();

	const [backgroundImageValue, setBackgroundImageValue] = useState('');

	useEffect(() => {
		if (typeof backgroundImage.url === 'string') {
			setBackgroundImageValue(backgroundImage.url);
		} else if (backgroundImage.fieldId) {
			InfoItemService.getAssetFieldValue({
				classNameId: backgroundImage.classNameId,
				classPK: backgroundImage.classPK,
				config,
				fieldId: backgroundImage.fieldId,
				onNetworkStatus: dispatch
			}).then(response => {
				const {fieldValue} = response;

				if (fieldValue && fieldValue.url !== backgroundImageValue) {
					setBackgroundImageValue(fieldValue.url);
				}
			});
		} else {
			setBackgroundImageValue('');
		}
	}, [backgroundImage, backgroundImageValue, config, dispatch, item]);

	return (
		<div
			className={classNames(
				className,
				`pb-${paddingBottom} pt-${paddingTop}`,
				{
					[`bg-${backgroundColorCssClass}`]: !!backgroundColorCssClass,
					[`px-${paddingHorizontal}`]: paddingHorizontal !== 3
				}
			)}
			ref={ref}
			style={
				backgroundImageValue
					? {
							backgroundImage: `url(${backgroundImageValue})`,
							backgroundPosition: '50% 50%',
							backgroundRepeat: 'no-repeat',
							backgroundSize: 'cover'
					  }
					: {}
			}
		>
			<div
				className={classNames('px-0', {
					container: type === 'fixed',
					'container-fluid': type === 'fluid'
				})}
			>
				{children}
			</div>
		</div>
	);
});

export default Container;
