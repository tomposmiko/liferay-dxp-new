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

import ClayButton, {ClayButtonWithIcon} from '@clayui/button';
import ClayForm, {ClayCheckbox, ClayInput} from '@clayui/form';
import ClayList from '@clayui/list';
import ClayManagementToolbar from '@clayui/management-toolbar';
import ClayModal from '@clayui/modal';
import React, {FormEvent, useContext, useEffect, useState} from 'react';

import ViewContext, {TYPES} from './context';

import './ModalAddColumnsObjectCustomView.scss';
interface IProps extends React.HTMLAttributes<HTMLElement> {
	observer: any;
	onClose: () => void;
}

const defaultLanguageId = Liferay.ThemeDisplay.getDefaultLanguageId();

const ModalAddColumnsObjectCustomView: React.FC<IProps> = ({
	observer,
	onClose,
}) => {
	const [{objectFields}, dispatch] = useContext(ViewContext);

	const [filteredItems, setFilteredItems] = useState(objectFields);
	const [fieldsChecked, setFieldsChecked] = useState(false);
	const [allFieldsChecked, setAllFieldsChecked] = useState(false);
	const [query, setQuery] = useState('');

	useEffect(() => {
		const checkedItems = filteredItems.filter(
			({checked}) => checked === true
		);

		setAllFieldsChecked(checkedItems.length === filteredItems.length);

		setFieldsChecked(checkedItems.length > 0);
	}, [fieldsChecked, filteredItems]);

	useEffect(() => {
		setFilteredItems((filteredItems) =>
			filteredItems.map((field) => {
				const filtered =
					!query ||
					field.label[defaultLanguageId]
						.toLowerCase()
						.includes(query.toLowerCase());

				return {...field, filtered};
			})
		);
	}, [query]);

	const handleAllFieldsChecked = (checked: boolean) => {
		const setSelection = (checked: boolean) => {
			if (allFieldsChecked) {
				return false;
			}
			else if (fieldsChecked) {
				return true;
			}
			else if (!checked) {
				return false;
			}
			else {
				setFieldsChecked(true);

				return true;
			}
		};

		setFilteredItems(
			filteredItems.map((field) => {
				return {
					...field,
					checked: setSelection(checked),
				};
			})
		);
	};

	const toggleFieldCheckbox = (name: String) => {
		const newfiltredItems = filteredItems.map((field) => {
			return field.name === name
				? {
						...field,
						checked: !field.checked,
				  }
				: field;
		});

		setFilteredItems(newfiltredItems);
	};

	const onSubmit = (event: FormEvent) => {
		event.preventDefault();

		dispatch({
			payload: {
				filteredItems,
			},
			type: TYPES.ADD_OBJECT_VIEW_COLUMN,
		});

		onClose();
	};

	return (
		<ClayModal
			className="lfr-object__object-view-modal-add-columns"
			observer={observer}
		>
			<ClayForm onSubmit={(event) => onSubmit(event)}>
				<ClayModal.Header>
					{Liferay.Language.get('add-columns')}
				</ClayModal.Header>

				<ClayModal.Body>
					<div className="lfr-object__object-view-modal-add-columns-selection-title">
						{Liferay.Language.get('select-the-columns')}
					</div>

					<ClayManagementToolbar>
						<ClayManagementToolbar.ItemList>
							<ClayManagementToolbar.Item>
								<ClayCheckbox
									checked={fieldsChecked}
									indeterminate={
										!allFieldsChecked && fieldsChecked
									}
									onChange={({target}) =>
										handleAllFieldsChecked(target.checked)
									}
								/>
							</ClayManagementToolbar.Item>
						</ClayManagementToolbar.ItemList>

						<ClayManagementToolbar.Search>
							<ClayInput.Group>
								<ClayInput.GroupItem>
									<ClayInput
										aria-label="Search"
										className="form-control input-group-inset input-group-inset-after"
										defaultValue=""
										onChange={({target}) =>
											setQuery(target.value)
										}
										placeholder={Liferay.Language.get(
											'search'
										)}
										type="text"
									/>

									<ClayInput.GroupInsetItem after tag="span">
										<ClayButtonWithIcon
											className="navbar-breakpoint-d-none"
											displayType="unstyled"
											onClick={() => {}}
											symbol="times"
										/>

										<ClayButtonWithIcon
											displayType="unstyled"
											symbol="search"
											type="submit"
										/>
									</ClayInput.GroupInsetItem>
								</ClayInput.GroupItem>
							</ClayInput.Group>
						</ClayManagementToolbar.Search>
					</ClayManagementToolbar>

					<ClayList className="lfr-object__object-view-modal-add-columns-list">
						{filteredItems?.map((field, index) => (
							<ClayList.Item
								className={field?.filtered ? '' : 'hide'}
								flex
								key={`list-item-${index}`}
							>
								<ClayCheckbox
									checked={field.checked}
									label={field.label[defaultLanguageId]}
									onChange={() => {
										toggleFieldCheckbox(field.name);
									}}
								/>
							</ClayList.Item>
						))}
					</ClayList>
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
								{Liferay.Language.get('save')}
							</ClayButton>
						</ClayButton.Group>
					}
				/>
			</ClayForm>
		</ClayModal>
	);
};

export default ModalAddColumnsObjectCustomView;
