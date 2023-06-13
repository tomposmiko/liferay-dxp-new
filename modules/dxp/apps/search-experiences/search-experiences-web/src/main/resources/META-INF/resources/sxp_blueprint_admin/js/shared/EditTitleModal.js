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

import ClayButton from '@clayui/button';
import ClayForm, {ClayInput} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayModal from '@clayui/modal';
import React, {useRef, useState} from 'react';

export default function EditTitleModal({
	initialDescription,
	initialTitle,
	modalFieldFocus,
	observer,
	onClose,
	onSubmit,
}) {
	const [description, setDescription] = useState(initialDescription);
	const [hasError, setHasError] = useState(false);
	const [title, setTitle] = useState(initialTitle);

	const titleInputRef = useRef();

	const _handleSubmit = (event) => {
		event.preventDefault();

		if (!title) {
			setHasError(true);

			titleInputRef.current.focus();
		}
		else {
			onSubmit({description, title});

			onClose();
		}
	};

	return (
		<ClayModal
			className="sxp-edit-title-modal-root"
			observer={observer}
			size="md"
		>
			<ClayForm onSubmit={_handleSubmit}>
				<ClayModal.Body>
					<ClayForm.Group className={hasError ? 'has-error' : ''}>
						<label htmlFor="title">
							{Liferay.Language.get('title')}

							<ClayIcon
								className="ml-1 reference-mark"
								focusable="false"
								role="presentation"
								symbol="asterisk"
							/>
						</label>

						<ClayInput
							autoFocus={modalFieldFocus === 'title'}
							id="title"
							onBlur={({currentTarget}) => {
								setHasError(!currentTarget.value);
							}}
							onChange={({target: {value}}) => setTitle(value)}
							ref={titleInputRef}
							type="text"
							value={title}
						/>

						{hasError && (
							<ClayForm.FeedbackGroup>
								<ClayForm.FeedbackItem>
									<ClayForm.FeedbackIndicator symbol="exclamation-full" />

									{Liferay.Language.get(
										'this-field-is-required'
									)}
								</ClayForm.FeedbackItem>
							</ClayForm.FeedbackGroup>
						)}
					</ClayForm.Group>

					<ClayForm.Group>
						<label htmlFor="description">
							{Liferay.Language.get('description')}
						</label>

						<ClayInput
							autoFocus={modalFieldFocus === 'description'}
							component="textarea"
							id="description"
							onChange={({target: {value}}) =>
								setDescription(value)
							}
							type="text"
							value={description}
						/>
					</ClayForm.Group>
				</ClayModal.Body>

				<ClayModal.Footer
					last={
						<ClayButton.Group spaced>
							<ClayButton
								displayType="secondary"
								onClick={onClose}
							>
								{Liferay.Language.get('cancel')}
							</ClayButton>

							<ClayButton displayType="primary" type="submit">
								{Liferay.Language.get('done')}
							</ClayButton>
						</ClayButton.Group>
					}
				/>
			</ClayForm>
		</ClayModal>
	);
}
