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

import {useContext} from 'react';

import AppContext from '../../../AppContext.es';
import {UPDATE_FIELDSETS} from '../../../actions.es';
import DataLayoutBuilderContext from '../../../data-layout-builder/DataLayoutBuilderContext.es';
import {addItem} from '../../../utils/client.es';
import {errorToast, successToast} from '../../../utils/toast.es';

export default ({availableLanguageIds, childrenContext}) => {
	const [{fieldSets}, dispatch] = useContext(AppContext);
	const {state: childrenState} = childrenContext;
	const [dataLayoutBuilder] = useContext(DataLayoutBuilderContext);
	const {contentType, fieldSetContentType} = dataLayoutBuilder.props;

	return (name) => {
		const {
			dataDefinition: {dataDefinitionFields},
			dataLayout: {dataLayoutPages},
		} = childrenState;

		const fieldSet = {
			availableLanguageIds,
			dataDefinitionFields,
			defaultDataLayout: {
				dataLayoutPages,
				name,
			},
			name,
		};

		return addItem(
			`/o/data-engine/v2.0/data-definitions/by-content-type/${
				fieldSetContentType || contentType
			}`,
			fieldSet
		)
			.then((dataDefinitionFieldSet) => {
				dispatch({
					payload: {
						fieldSets: [...fieldSets, dataDefinitionFieldSet],
					},
					type: UPDATE_FIELDSETS,
				});

				successToast(Liferay.Language.get('fieldset-saved'));

				return Promise.resolve();
			})
			.catch(({message}) => errorToast(message));
	};
};
