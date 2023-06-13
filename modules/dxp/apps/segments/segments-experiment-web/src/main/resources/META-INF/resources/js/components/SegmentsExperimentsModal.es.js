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

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import {ClaySelect} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayModal from '@clayui/modal';
import {useIsMounted} from '@liferay/frontend-js-react-web';
import PropTypes from 'prop-types';
import React, {useState} from 'react';

import {SegmentsExperimentGoal} from '../types.es';
import LoadingButton from './LoadingButton/LoadingButton.es';
import ValidatedInput from './ValidatedInput/ValidatedInput.es';

function SegmentsExperimentsModal({
	description = '',
	error,
	goal,
	goals = [],
	name = '',
	onClose,
	onSave,
	segmentsExperienceId,
	segmentsExperimentId,
	title,
}) {
	const [loading, setLoading] = useState(false);
	const [inputDescription, setInputDescription] = useState(description);
	const [inputGoal, setInputGoal] = useState(
		goal?.value || goals?.[0]?.value
	);
	const [inputName, setInputName] = useState(name);
	const [invalidForm, setInvalidForm] = useState(false);
	const isMounted = useIsMounted();

	const onSubmit = (event) => {
		event.preventDefault();

		if (!invalidForm && !loading) {
			const goalTarget = (inputGoal === 'click' && goal?.target) || '';

			setLoading(true);

			onSave({
				description: inputDescription,
				goal: inputGoal,
				goalTarget,
				name: inputName,
				segmentsExperienceId,
				segmentsExperimentId,
			}).finally(() => {
				if (isMounted()) {
					setLoading(false);
				}
			});
		}
	};

	return (
		<form onSubmit={onSubmit}>
			<ClayModal.Header>{title}</ClayModal.Header>

			<ClayModal.Body>
				{error && (
					<ClayAlert
						displayType="danger"
						title={Liferay.Language.get('error')}
					>
						{error}
					</ClayAlert>
				)}

				<ValidatedInput
					autofocus
					errorMessage={Liferay.Language.get('test-name-is-required')}
					label={Liferay.Language.get('test-name')}
					onChange={(event) => setInputName(event.target.value)}
					onValidationChange={setInvalidForm}
					required
					value={inputName}
				/>

				<div className="form-group">
					<label>{Liferay.Language.get('description')}</label>

					<textarea
						className="form-control"
						maxLength="4000"
						onChange={(event) =>
							setInputDescription(event.target.value)
						}
						placeholder={Liferay.Language.get(
							'description-placeholder'
						)}
						value={inputDescription}
					/>
				</div>

				{Boolean(goals.length) && (
					<div className="form-group">
						<label className="w100">
							{Liferay.Language.get('select-goal')}

							<ClayIcon
								className="lexicon-icon-sm ml-1 reference-mark text-warning"
								style={{verticalAlign: 'super'}}
								symbol="asterisk"
							/>

							<ClaySelect
								className="mt-1"
								defaultValue={inputGoal}
								onChange={(event) =>
									setInputGoal(event.target.value)
								}
								required
							>
								{goals.map((goal) => (
									<ClaySelect.Option
										key={goal.value}
										label={goal.label}
										value={goal.value}
									/>
								))}
							</ClaySelect>
						</label>
					</div>
				)}
			</ClayModal.Body>

			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton
							displayType="secondary"
							onClick={onClose}
							type="button"
						>
							{Liferay.Language.get('cancel')}
						</ClayButton>

						<LoadingButton
							disabled={invalidForm || loading}
							displayType="primary"
							loading={loading}
							type="submit"
						>
							{Liferay.Language.get('save')}
						</LoadingButton>
					</ClayButton.Group>
				}
			/>
		</form>
	);
}

SegmentsExperimentsModal.propTypes = {
	description: PropTypes.string,
	error: PropTypes.string,
	goal: SegmentsExperimentGoal,
	goals: PropTypes.arrayOf(SegmentsExperimentGoal),
	name: PropTypes.string,
	onClose: PropTypes.func.isRequired,
	onSave: PropTypes.func.isRequired,
	segmentsExperienceId: PropTypes.string,
	segmentsExperimentId: PropTypes.string,
	title: PropTypes.string.isRequired,
};

export default SegmentsExperimentsModal;
