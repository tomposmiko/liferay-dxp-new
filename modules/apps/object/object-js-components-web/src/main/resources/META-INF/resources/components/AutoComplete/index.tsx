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

import ClayDropDown from '@clayui/drop-down';
import React, {useState} from 'react';

import {FieldBase} from '../FieldBase';

import './index.scss';
import {getLocalizableLabel} from '../../utils/string';
import {CustomSelect} from './CustomSelect';
interface AutoCompleteProps<
	T extends {
		label?: LocalizedValue<string> | string;
		name?: string;
		value?: string;
	}
> extends React.HTMLAttributes<HTMLElement> {
	children: (item: T) => React.ReactNode;
	contentRight?: React.ReactNode;
	creationLanguageId: Locale;
	disabled?: boolean;
	emptyStateMessage: string;
	error?: string;
	feedbackMessage?: string;
	hasEmptyItem?: boolean;
	items: T[];
	label: string;
	onChangeQuery: (value: string) => void;
	onSelectEmptyStateItem?: (emptyStateItem: EmptyStateItem) => void;
	onSelectItem: (item: T) => void;
	placeholder?: string;
	query: string;
	required?: boolean;
	tooltip?: string;
	value?: string;
}

type EmptyStateItem = {
	id: string;
	label: string;
};

export default function AutoComplete<
	T extends {
		label?: LocalizedValue<string> | string;
		name?: string;
		value?: string;
	}
>({
	children,
	className,
	contentRight,
	creationLanguageId,
	disabled,
	emptyStateMessage,
	error,
	feedbackMessage,
	hasEmptyItem,
	id,
	items,
	label,
	onChangeQuery,
	onSelectEmptyStateItem,
	onSelectItem,
	placeholder,
	query,
	required = false,
	tooltip,
	value,
}: AutoCompleteProps<T>) {
	const [active, setActive] = useState<boolean>(false);

	const emptyStateItem = {
		id: '',
		label: Liferay.Language.get('choose-an-option'),
	};

	return (
		<FieldBase
			className={className}
			disabled={disabled}
			errorMessage={error}
			helpMessage={feedbackMessage}
			id={id}
			label={label}
			required={required}
			tooltip={tooltip}
		>
			<ClayDropDown
				active={!disabled && active}
				onActiveChange={(value: boolean) =>
					!disabled ? setActive(value) : setActive(false)
				}
				trigger={
					<CustomSelect
						contentRight={<>{value && contentRight}</>}
						disabled={disabled}
						placeholder={
							placeholder ??
							Liferay.Language.get('choose-an-option')
						}
						value={value}
					/>
				}
			>
				<ClayDropDown.Search
					onChange={onChangeQuery}
					placeholder={Liferay.Language.get('search')}
					value={query}
				/>

				{!items.length ? (
					<ClayDropDown.ItemList>
						<ClayDropDown.Item>
							{emptyStateMessage}
						</ClayDropDown.Item>
					</ClayDropDown.ItemList>
				) : (
					<ClayDropDown.ItemList>
						{hasEmptyItem && (
							<ClayDropDown.Item
								onClick={() => {
									setActive(false);

									if (onSelectEmptyStateItem) {
										onSelectEmptyStateItem(emptyStateItem);
									}
								}}
							>
								<div className="d-flex justify-content-between">
									<div>{emptyStateItem.label}</div>
								</div>
							</ClayDropDown.Item>
						)}

						{items.map((item, index) => {
							return (
								<ClayDropDown.Item
									active={
										typeof item.label !== 'string'
											? value ===
											  getLocalizableLabel(
													creationLanguageId,
													item.label
											  )
											: value === item.name
									}
									key={index}
									onClick={() => {
										setActive(false);
										onSelectItem(item);
									}}
								>
									{children && children(item)}
								</ClayDropDown.Item>
							);
						})}
					</ClayDropDown.ItemList>
				)}
			</ClayDropDown>
		</FieldBase>
	);
}
