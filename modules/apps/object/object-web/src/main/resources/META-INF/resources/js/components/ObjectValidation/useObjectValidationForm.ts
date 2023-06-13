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

import {
	FormError,
	REQUIRED_MSG,
	invalidateRequired,
	useForm,
} from '@liferay/object-js-components-web';
import {ChangeEventHandler} from 'react';

interface IUseObjectValidationForm {
	initialValues: Partial<ObjectValidation>;
	onSubmit: (validation: ObjectValidation) => void;
}

export type ObjectValidationErrors = FormError<ObjectValidation>;

export interface TabProps {
	disabled: boolean;
	errors: ObjectValidationErrors;
	handleChange: ChangeEventHandler<HTMLInputElement>;
	setValues: (values: Partial<ObjectValidation>) => void;
	values: Partial<ObjectValidation>;
}

const defaultLanguageId = Liferay.ThemeDisplay.getDefaultLanguageId();

export function useObjectValidationForm({
	initialValues,
	onSubmit,
}: IUseObjectValidationForm) {
	const validate = (validation: Partial<ObjectValidation>) => {
		const errors: ObjectValidationErrors = {};
		const label = validation.name?.[defaultLanguageId];
		const errorMessage = validation.errorLabel?.[defaultLanguageId];
		const script = validation.script;

		if (invalidateRequired(label)) {
			errors.name = REQUIRED_MSG;
		}

		if (invalidateRequired(errorMessage)) {
			errors.errorLabel = REQUIRED_MSG;
		}

		if (invalidateRequired(script)) {
			errors.script = REQUIRED_MSG;
		}

		if (
			validation.engine === 'groovy' &&
			!!validation.lineCount &&
			validation.lineCount > 2987
		) {
			errors.script = Liferay.Language.get(
				'the-maximum-number-of-lines-available-is-2987'
			);
		}

		return errors;
	};

	const {errors, handleChange, handleSubmit, setValues, values} = useForm<
		ObjectValidation
	>({
		initialValues,
		onSubmit,
		validate,
	});

	return {errors, handleChange, handleSubmit, setValues, values};
}
