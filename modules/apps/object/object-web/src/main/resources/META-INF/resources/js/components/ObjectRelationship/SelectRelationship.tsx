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
	API,
	AutoComplete,
	getLocalizableLabel,
	stringIncludesQuery,
} from '@liferay/object-js-components-web';
import React, {useEffect, useMemo, useState} from 'react';

interface IProps {
	error?: string;
	objectDefinitionExternalReferenceCode: string;
	onChange: (objectFieldName: string) => void;
	value?: string;
}

export default function SelectRelationship({
	error,
	objectDefinitionExternalReferenceCode,
	onChange,
	value,
	...otherProps
}: IProps) {
	const [creationLanguageId, setCreationLanguageId] = useState<
		Liferay.Language.Locale
	>();
	const [fields, setFields] = useState<ObjectField[]>([]);
	const [query, setQuery] = useState<string>('');
	const options = useMemo(
		() =>
			fields.map(({label, name}) => {
				return {
					label: getLocalizableLabel(
						creationLanguageId as Liferay.Language.Locale,
						label,
						name
					),
					name,
				};
			}),
		[creationLanguageId, fields]
	);

	const filteredOptions = useMemo(() => {
		if (options) {
			return options.filter((option) =>
				stringIncludesQuery(option.label, query)
			);
		}
	}, [options, query]);

	const selectedValue = useMemo(() => {
		return fields.find(({name}) => name === value);
	}, [fields, value]);

	useEffect(() => {
		if (objectDefinitionExternalReferenceCode) {
			const makeFetch = async () => {
				const items = await API.getObjectFieldsByExternalReferenceCode(
					objectDefinitionExternalReferenceCode
				);

				const objectDefinition = await API.getObjectDefinitionByExternalReferenceCode(
					objectDefinitionExternalReferenceCode
				);

				setCreationLanguageId(objectDefinition.defaultLanguageId);

				const options = items.filter(
					({businessType}) => businessType === 'Relationship'
				);

				setCreationLanguageId(objectDefinition.defaultLanguageId);

				setFields(options);
			};

			makeFetch();
		}
		else {
			setFields([]);
		}
	}, [objectDefinitionExternalReferenceCode]);

	return (
		<AutoComplete<LabelNameObject>
			creationLanguageId={creationLanguageId as Liferay.Language.Locale}
			emptyStateMessage={Liferay.Language.get('no-parameters-were-found')}
			error={error}
			items={filteredOptions ?? []}
			label={Liferay.Language.get('parameter')}
			onChangeQuery={setQuery}
			onSelectItem={({name}) => {
				onChange(
					fields.find(({name: fieldName}) => fieldName === name)
						?.name!
				);
			}}
			query={query}
			required
			tooltip={Liferay.Language.get(
				'choose-a-relationship-field-from-the-selected-object'
			)}
			value={getLocalizableLabel(
				creationLanguageId as Liferay.Language.Locale,
				selectedValue?.label,
				selectedValue?.name
			)}
			{...otherProps}
		>
			{({label, name}) => (
				<div className="d-flex justify-content-between">
					{label ?? name}
				</div>
			)}
		</AutoComplete>
	);
}
