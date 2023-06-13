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

import ClayButton from '@clayui/button';
import ClayForm, {ClayInput} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayModal from '@clayui/modal';

import '@testing-library/jest-dom/extend-expect';
import {fireEvent, render, screen} from '@testing-library/react';
import getCN from 'classnames';
import React, {useState} from 'react';

import {MAX_LENGTH, MIN_LENGTH} from '../../../utils/constants';

interface IModalProps {
	observer?: any;
	onCancel: () => void;
	onSubmit: () => void;
}

// NOTE: to render properly in the tests, this Component is sligthly different from properties/CreatePropertyModal.tsx

const Component: React.FC<IModalProps> = ({onCancel}) => {
	const [propertyName, setPropertyName] = useState('');
	const [submitting] = useState(false);

	const isValid = propertyName.length >= MIN_LENGTH;

	return (
		<>
			<ClayForm onSubmit={(event) => event.preventDefault()}>
				<ClayModal.Header>
					{Liferay.Language.get('new-property')}
				</ClayModal.Header>

				<ClayModal.Body className="pb-0 pt-3">
					<ClayForm.Group
						className={getCN(
							{
								'has-error': propertyName && !isValid,
							},
							'mb-3'
						)}
					>
						<label htmlFor="basicInputText">
							{Liferay.Language.get('property-name')}
						</label>

						<ClayInput
							id="inputPropertyName"
							maxLength={MAX_LENGTH}
							onChange={({target: {value}}) =>
								setPropertyName(value)
							}
							type="text"
							value={propertyName}
						/>

						{propertyName && !isValid && (
							<ClayForm.FeedbackGroup>
								<ClayForm.FeedbackItem>
									<ClayIcon
										className="mr-1"
										symbol="warning-full"
									/>

									<span>
										{Liferay.Language.get(
											'property-name-does-not-meet-minimum-length-required'
										)}
									</span>
								</ClayForm.FeedbackItem>
							</ClayForm.FeedbackGroup>
						)}
					</ClayForm.Group>
				</ClayModal.Body>

				<ClayModal.Footer
					last={
						<ClayButton.Group spaced>
							<ClayButton
								displayType="secondary"
								onClick={onCancel}
							>
								{Liferay.Language.get('cancel')}
							</ClayButton>

							<ClayButton
								disabled={
									!propertyName || submitting || !isValid
								}
								onClick={() => {}}
							>
								{/* {submitting && <Loading inline />} */}

								{Liferay.Language.get('create')}
							</ClayButton>
						</ClayButton.Group>
					}
				/>
			</ClayForm>
		</>
	);
};

describe('CreatePropertyModal', () => {
	it('renders component without crashing it', () => {
		render(<Component onCancel={() => {}} onSubmit={() => {}} />);

		expect(screen.getByText(/new-property/i)).toBeInTheDocument();

		expect(screen.getByText(/property-name/i)).toBeInTheDocument();

		expect(
			screen.getByRole('button', {name: /cancel/i})
		).toBeInTheDocument();

		expect(
			screen.getByRole('button', {name: /create/i})
		).toBeInTheDocument();
	});

	it('renders component, writes a character in the input and checks if the minimum length message is displayed', () => {
		render(<Component onCancel={() => {}} onSubmit={() => {}} />);

		expect(screen.getByText(/new-property/i)).toBeInTheDocument();

		expect(screen.getByText(/property-name/i)).toBeInTheDocument();

		const textInput = screen.getByRole('textbox');

		expect(textInput).toBeInTheDocument();

		expect(textInput).toHaveValue('');

		fireEvent.change(textInput, {target: {value: 't'}});

		expect(textInput).toHaveValue('t');

		const createButton = screen.getByRole('button', {name: /create/i});

		expect(createButton).toHaveAttribute('disabled');

		expect(
			screen.getByText(
				/property-name-does-not-meet-minimum-length-required/i
			)
		).toBeInTheDocument();
	});

	it('renders component, writes a character in the input and checks if the minimum length message is NOT displayed', () => {
		render(<Component onCancel={() => {}} onSubmit={() => {}} />);

		expect(screen.getByText(/new-property/i)).toBeInTheDocument();

		expect(screen.getByText(/property-name/i)).toBeInTheDocument();

		const textInput = screen.getByRole('textbox');

		expect(textInput).toBeInTheDocument();

		expect(textInput).toHaveValue('');

		const createButton = screen.getByRole('button', {name: /create/i});

		expect(createButton).toHaveAttribute('disabled');

		fireEvent.change(textInput, {target: {value: 'test'}});

		expect(textInput).toHaveValue('test');

		expect(createButton).not.toHaveAttribute('disabled');
	});
});
