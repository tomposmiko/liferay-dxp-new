/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayForm, {ClayInput} from '@clayui/form';
import React, {useContext, useState} from 'react';

import {DiagramBuilderContext} from '../../../../../DiagramBuilderContext';
import SidebarPanel from '../../../SidebarPanel';

const ResourceActions = () => {
	const {setSelectedItem} = useContext(DiagramBuilderContext);
	const [resourceActions, setResourceActions] = useState('');

	const onChange = ({target: {value}}) => {
		setSelectedItem((previousValue) => ({
			...previousValue,
			data: {
				...previousValue.data,
				assignments: {
					assignmentType: ['resourceActions'],
					resourceAction: value,
				},
			},
		}));

		setResourceActions(value);
	};

	return (
		<SidebarPanel panelTitle={Liferay.Language.get('resource-actions')}>
			<ClayForm.Group>
				<label htmlFor="resource-actions">
					{Liferay.Language.get('resource-actions')}

					<span className="ml-1 mr-1 text-warning">*</span>
				</label>

				<ClayInput
					component="textarea"
					id="resource-actions"
					onChange={onChange}
					type="text"
					value={resourceActions}
				/>
			</ClayForm.Group>
		</SidebarPanel>
	);
};

export default ResourceActions;
