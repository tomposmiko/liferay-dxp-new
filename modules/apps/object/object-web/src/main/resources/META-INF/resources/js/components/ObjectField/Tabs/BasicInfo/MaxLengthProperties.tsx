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

import ClayForm from '@clayui/form';
import {Input, Toggle} from '@liferay/object-js-components-web';
import {sub} from 'frontend-js-web';
import React, {useEffect, useRef} from 'react';
import {createTextMaskInputElement} from 'text-mask-core';

import {createAutoCorrectedNumberPipe} from '../../../../utils/createAutoCorrectedNumberPipe';
import {normalizeFieldSettings} from '../../../../utils/fieldSettings';
import {ObjectFieldErrors} from '../../ObjectFieldFormBase';

interface IMaxLengthPropertiesProps {
	disabled?: boolean;
	errors: ObjectFieldErrors;
	objectField: Partial<ObjectField>;
	objectFieldSettings: ObjectFieldSetting[];
	onSettingsChange: (setting: ObjectFieldSetting) => void;
	setValues: (values: Partial<ObjectField>) => void;
}

export function MaxLengthProperties({
	disabled,
	errors,
	objectField,
	objectFieldSettings,
	onSettingsChange,
	setValues,
}: IMaxLengthPropertiesProps) {
	const [defaultMaxLength, defaultMaxLengthText] =
		objectField.businessType === 'Text' ? [280, '280'] : [65000, '65,000'];

	const settings = normalizeFieldSettings(objectFieldSettings);

	const inputRef = useRef(null);
	const maskRef = useRef();

	useEffect(() => {
		if (settings.showCounter) {
			maskRef.current = createTextMaskInputElement({
				guide: false,
				inputElement: inputRef.current,
				keepCharPositions: true,
				mask:
					objectField.businessType === 'Text'
						? [/\d/, /\d/, /\d/]
						: [/\d/, /\d/, /\d/, /\d/, /\d/],
				pipe: createAutoCorrectedNumberPipe(defaultMaxLength, 1),
				showMask: true,
			});
		}
	}, [defaultMaxLength, objectField.businessType, settings.showCounter]);

	return (
		<>
			<ClayForm.Group>
				<Toggle
					disabled={disabled}
					label={Liferay.Language.get('limit-characters')}
					name="showCounter"
					onToggle={(value) => {
						const updatedSettings: ObjectFieldSetting[] = [
							{name: 'showCounter', value},
						];

						if (value) {
							updatedSettings.push({
								name: 'maxLength',
								value: defaultMaxLength,
							});
						}

						setValues({objectFieldSettings: updatedSettings});
					}}
					toggled={!!settings.showCounter}
					tooltip={Liferay.Language.get(
						'when-enabled-a-character-counter-will-be-shown-to-the-user'
					)}
				/>
			</ClayForm.Group>
			<ClayForm.Group>
				{settings.showCounter && (
					<Input
						disabled={disabled}
						error={errors.maxLength}
						feedbackMessage={sub(
							Liferay.Language.get(
								'set-the-maximum-number-of-characters-accepted-this-value-cant-be-less-than-x-or-greater-than-x'
							),
							'1',
							defaultMaxLengthText
						)}
						label={Liferay.Language.get(
							'maximum-number-of-characters'
						)}
						onChange={({target: {value}}) =>
							onSettingsChange({
								name: 'maxLength',
								value: value && Number(value),
							})
						}
						onInput={({target: {value}}: any) =>
							(maskRef.current as any).update(value)
						}
						ref={inputRef}
						required
						value={`${settings.maxLength}`}
					/>
				)}
			</ClayForm.Group>
		</>
	);
}
