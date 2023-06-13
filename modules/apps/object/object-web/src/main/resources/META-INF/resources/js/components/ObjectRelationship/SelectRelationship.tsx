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

import {API, Select} from '@liferay/object-js-components-web';
import React, {useEffect, useMemo, useState} from 'react';

const defaultLanguageId = Liferay.ThemeDisplay.getDefaultLanguageId();

export default function SelectRelationship({
	error,
	objectDefinitionId,
	onChange,
	value,
	...otherProps
}: IProps) {
	const [fields, setFields] = useState<ObjectField[]>([]);
	const options = useMemo(
		() =>
			fields.map(({label, name}) => {
				return {
					label: label[defaultLanguageId]!,
					name,
				};
			}),
		[fields]
	);
	const selectedValue = useMemo(() => {
		return fields.find(({id}) => id === value);
	}, [fields, value]);

	useEffect(() => {
		if (objectDefinitionId) {
			API.getObjectFields(objectDefinitionId).then((fields) => {
				const options = fields.filter(
					({businessType}) => businessType === 'Relationship'
				);
				setFields(options);
			});
		}
		else {
			setFields([]);
		}
	}, [objectDefinitionId]);

	return (
		<Select
			error={error}
			label={Liferay.Language.get('parameter')}
			onChange={(event) => {
				onChange?.(
					fields.find(({name}) => name === event.target.value)?.id!
				);
			}}
			options={options}
			required
			tooltip={Liferay.Language.get(
				'choose-a-relationship-field-from-the-selected-object'
			)}
			value={selectedValue?.label[defaultLanguageId]}
			{...otherProps}
		/>
	);
}

interface IProps {
	error?: string;
	objectDefinitionId?: number;
	onChange?: (objectFieldId: number) => void;
	value?: number;
}
