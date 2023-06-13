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

import ClayForm, {ClaySelect} from '@clayui/form';
import classNames from 'classnames';
import React from 'react';

import ErrorFeedback from './ErrorFeedback';
import FeedbackMessage from './FeedbackMessage';
import RequiredMask from './RequiredMask';

interface ISelectProps extends React.HTMLAttributes<HTMLElement> {
	disabled?: boolean;
	error?: string;
	feedbackMessage?: string;
	label: string;
	options: string[];
	required?: boolean;
}

const Select: React.FC<ISelectProps> = ({
	className,
	disabled = false,
	error,
	feedbackMessage,
	id,
	label,
	onChange,
	options: optionsProps,
	required = false,
	...otherProps
}) => {
	const options = [Liferay.Language.get('choose-an-option'), ...optionsProps];

	return (
		<ClayForm.Group
			className={classNames(className, {
				'has-error': error,
			})}
		>
			<label className={classNames({disabled})} htmlFor={id}>
				{label}

				{required && <RequiredMask />}
			</label>

			<ClaySelect
				{...otherProps}
				defaultValue={options[0]}
				disabled={disabled}
				id={id}
				onChange={onChange}
				placeholder={Liferay.Language.get('choose-an-option')}
			>
				{options.map((label, index) => (
					<ClaySelect.Option
						key={index}
						label={label}
						value={index}
					/>
				))}
			</ClaySelect>

			{error && <ErrorFeedback error={error} />}

			{feedbackMessage && (
				<FeedbackMessage feedbackMessage={feedbackMessage} />
			)}
		</ClayForm.Group>
	);
};

export default Select;
