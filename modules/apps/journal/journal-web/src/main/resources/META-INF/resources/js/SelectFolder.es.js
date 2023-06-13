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

import {ClayButtonWithIcon} from '@clayui/button';
import {ClayInput} from '@clayui/form';
import ClayLayout from '@clayui/layout';
import {Treeview} from 'frontend-js-components-web';
import React, {useCallback, useMemo, useState} from 'react';

const SelectFolder = ({itemSelectorSaveEvent, nodes}) => {
	const [filterQuery, setFilterQuery] = useState('');

	const nodesById = useMemo(() => {
		const result = {};

		function visit(node) {
			result[node.id] = node;

			if (node.children) {
				node.children.forEach(visit);
			}
		}

		nodes.forEach(visit);

		return result;
	}, [nodes]);

	const handleQueryChange = useCallback((event) => {
		const value = event.target.value;

		setFilterQuery(value);
	}, []);

	const handleSelectionChange = (selectedNodeIds) => {
		const node = nodesById[[...selectedNodeIds][0]];

		if (node) {
			Liferay.Util.getOpener().Liferay.fire(itemSelectorSaveEvent, {
				data: {
					folderId: node.id,
					folderName: node.name,
				},
			});
		}
	};

	return (
		<ClayLayout.ContainerFluid className="select-folder">
			<nav className="collapse-basic-search navbar navbar-default navbar-no-collapse">
				<ClayInput.Group className="basic-search">
					<ClayInput.GroupItem prepend>
						<ClayInput
							aria-label={Liferay.Language.get('search')}
							onChange={handleQueryChange}
							placeholder={`${Liferay.Language.get('search')}`}
							type="text"
						/>
					</ClayInput.GroupItem>

					<ClayInput.GroupItem append shrink>
						<ClayButtonWithIcon
							displayType="unstyled"
							symbol="search"
						/>
					</ClayInput.GroupItem>
				</ClayInput.Group>
			</nav>

			<Treeview
				NodeComponent={Treeview.Card}
				filterQuery={filterQuery}
				nodes={nodes}
				onSelectedNodesChange={handleSelectionChange}
			/>
		</ClayLayout.ContainerFluid>
	);
};

export default SelectFolder;
