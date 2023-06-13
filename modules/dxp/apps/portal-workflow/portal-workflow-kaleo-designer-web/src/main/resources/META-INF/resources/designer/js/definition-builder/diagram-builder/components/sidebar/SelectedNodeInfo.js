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
import ClayIcon from '@clayui/icon';
import PropTypes from 'prop-types';
import React, {useContext} from 'react';

import {DefinitionBuilderContext} from '../../../DefinitionBuilderContext';
import {DiagramBuilderContext} from '../../DiagramBuilderContext';
import SidebarPanel from './SidebarPanel';
import {isIdDuplicated} from './utils';

export default function SelectedNodeInfo({errors, setErrors}) {
	const {defaultLanguageId, selectedLanguageId} = useContext(
		DefinitionBuilderContext
	);
	const {
		elements,
		selectedNode,
		selectedNodeNewId,
		setSelectedNode,
		setSelectedNodeNewId,
	} = useContext(DiagramBuilderContext);

	return (
		<SidebarPanel panelTitle={Liferay.Language.get('information')}>
			<ClayForm.Group className={errors.label ? 'has-error' : ''}>
				<label htmlFor="nodeLabel">
					{Liferay.Language.get('label')}

					<span className="ml-1 mr-1 text-warning">*</span>

					<span title={Liferay.Language.get('label-name')}>
						<ClayIcon
							className="text-muted"
							symbol="question-circle-full"
						/>
					</span>
				</label>

				<ClayInput
					id="nodeLabel"
					onChange={({target}) => {
						if (target.value.trim() === '') {
							setErrors({...errors, label: true});
						}
						else {
							setErrors({...errors, label: false});
						}

						const key =
							selectedLanguageId !== ''
								? selectedLanguageId
								: defaultLanguageId;

						setSelectedNode({
							...selectedNode,
							data: {
								...selectedNode.data,
								label: {
									...selectedNode.data.label,
									[key]: target.value,
								},
							},
						});
					}}
					type="text"
					value={
						(selectedLanguageId
							? selectedNode?.data.label[selectedLanguageId]
							: selectedNode?.data.label[defaultLanguageId]) || ''
					}
				/>

				<ClayForm.FeedbackItem>
					{errors.label && (
						<>
							<ClayForm.FeedbackIndicator symbol="exclamation-full" />

							{Liferay.Language.get('this-field-is-required')}
						</>
					)}
				</ClayForm.FeedbackItem>
			</ClayForm.Group>

			<ClayForm.Group
				className={
					errors.id.duplicated || errors.id.empty ? 'has-error' : ''
				}
			>
				<label htmlFor="nodeId">
					<span>
						{`${Liferay.Language.get(
							'node'
						)} ${Liferay.Language.get('id')}`}
					</span>

					<span className="ml-1 mr-1 text-warning">*</span>

					<span
						title={Liferay.Language.get(
							'id-is-the-node-identifier'
						)}
					>
						<ClayIcon
							className="text-muted"
							symbol="question-circle-full"
						/>
					</span>
				</label>

				<ClayInput
					id="nodeId"
					onChange={({target}) => {
						if (target.value.trim() === '') {
							setErrors({
								...errors,
								id: {...errors.id, empty: true},
							});
						}
						else {
							setErrors({
								...errors,
								id: {...errors.id, empty: false},
							});
						}

						if (isIdDuplicated(elements, target.value.trim())) {
							setErrors({
								...errors,
								id: {...errors.id, duplicated: true},
							});
						}
						else {
							setErrors({
								...errors,
								id: {...errors.id, duplicated: false},
							});
						}

						setSelectedNodeNewId(target.value);
					}}
					type="text"
					value={(selectedNodeNewId ?? selectedNode?.id) || ''}
				/>

				<ClayForm.FeedbackItem>
					{(errors.id.duplicated || errors.id.empty) && (
						<>
							<ClayForm.FeedbackIndicator symbol="exclamation-full" />

							{errors.id.duplicated
								? Liferay.Language.get(
										'a-node-with-that-id-already-exists'
								  )
								: Liferay.Language.get(
										'this-field-is-required'
								  )}
						</>
					)}
				</ClayForm.FeedbackItem>
			</ClayForm.Group>

			<ClayForm.Group>
				<label htmlFor="nodeDescription">
					{Liferay.Language.get('description')}
				</label>

				<ClayInput
					component="textarea"
					id="nodeDescription"
					onChange={({target}) =>
						setSelectedNode({
							...selectedNode,
							data: {
								...selectedNode.data,
								description: target.value,
							},
						})
					}
					type="text"
					value={selectedNode?.data.description || ''}
				/>
			</ClayForm.Group>
		</SidebarPanel>
	);
}

SelectedNodeInfo.propTypes = {
	errors: PropTypes.object.isRequired,
	setErrors: PropTypes.func.isRequired,
};
