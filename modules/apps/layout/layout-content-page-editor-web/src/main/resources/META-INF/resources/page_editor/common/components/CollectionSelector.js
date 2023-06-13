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

import PropTypes from 'prop-types';
import React from 'react';

import {config} from '../../app/config/index';
import ItemSelector from './ItemSelector';

export default function CollectionSelector({
	collectionTitle,
	itemSelectorURL,
	label,
	onCollectionSelect,
}) {
	const eventName = `${config.portletNamespace}selectInfoList`;

	return (
		<ItemSelector
			eventName={eventName}
			itemSelectorURL={itemSelectorURL || config.infoListSelectorURL}
			label={label}
			onItemSelect={onCollectionSelect}
			quickMappedInfoItems={config.selectedMappingTypes?.linkedCollection}
			selectedItemTitle={collectionTitle}
			showMappedItems={!!config.selectedMappingTypes?.linkedCollection}
		/>
	);
}

CollectionSelector.propTypes = {
	collectionTitle: PropTypes.string,
	label: PropTypes.string,
	onCollectionSelect: PropTypes.func.isRequired,
};
