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
import ClayForm from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import {sub} from 'frontend-js-web';
import React, {FormEventHandler, useState} from 'react';

import InviteUserFormGroup from './InviteUsersFormGroup';
import {InputGroup, MultiSelectItem, ValidatableMultiSelectItem} from './types';

const deduplicatePredicate = (
	multiSelectItem: MultiSelectItem,
	index: number,
	array: MultiSelectItem[]
) => index === array.findIndex((item) => item.value === multiSelectItem.value);

interface IProps {
	accountEntryId: number;
	availableAccountRoles: MultiSelectItem[];
	inviteAccountUsersURL: string;
	portletNamespace: string;
	redirectURL: string;
}

function InviteUsersForm({
	accountEntryId,
	availableAccountRoles,
	inviteAccountUsersURL,
	portletNamespace,
	redirectURL,
}: IProps) {
	const [inputGroups, setInputGroups] = useState<InputGroup[]>([
		{
			accountRoles: [],
			emailAddresses: [],
			id: 'inputGroup-0',
		},
	]);

	const formId = `${portletNamespace}inviteUserForm`;

	function closeModal(modalConfig = {}) {
		const openerWindow = Liferay.Util.getOpener();

		openerWindow.Liferay.fire('closeModal', modalConfig);
	}

	function getInputGroup(inputGroupId: string) {
		const inputGroup = inputGroups.find((item) => item.id === inputGroupId);

		if (inputGroup) {
			return inputGroup;
		}

		throw new Error(`No input group found for id ${inputGroupId}`);
	}

	function setAccountRoles(
		inputGroupId: string,
		accountRoles: MultiSelectItem[]
	) {
		const inputGroup = getInputGroup(inputGroupId);

		inputGroup.accountRoles = accountRoles
			.filter(deduplicatePredicate)
			.map((accountRole) => {
				let errorMessage = '';

				if (
					!availableAccountRoles.some(
						(availableAccountRole) =>
							availableAccountRole.label === accountRole.label
					)
				) {
					errorMessage = sub(
						Liferay.Language.get('x-is-not-a-valid-role'),
						accountRole.label
					);
				}

				return {...accountRole, errorMessage};
			});

		setInputGroups([...inputGroups]);
	}

	async function setEmailAddresses(
		inputGroupId: string,
		emailAddresses: MultiSelectItem[]
	) {
		const inputGroup = getInputGroup(inputGroupId);

		const promises = emailAddresses.filter(deduplicatePredicate).map(
			(emailAddress) =>
				new Promise<ValidatableMultiSelectItem>((resolve) => {
					Liferay.Util.fetch(
						`/o/com-liferay-account-admin-web/validate-email-address/`,
						{
							body: Liferay.Util.objectToFormData({
								accountEntryId,
								emailAddress: emailAddress.label,
							}),
							method: 'POST',
						}
					)
						.then((response) => response.json())
						.then(({errorMessage}) =>
							resolve({...emailAddress, errorMessage})
						);
				})
		);

		inputGroup.emailAddresses = await Promise.all(promises);

		setInputGroups([...inputGroups]);
	}

	const submitForm: FormEventHandler<HTMLFormElement> = async (event) => {
		event.preventDefault();

		const form = event.currentTarget;

		const error = form.querySelector('.has-error');

		if (!error) {
			const formData = new FormData(form);

			formData.append(
				`${portletNamespace}count`,
				String(inputGroups.length)
			);

			const response = await Liferay.Util.fetch(inviteAccountUsersURL, {
				body: formData,
				method: 'POST',
			});

			const {success} = await response.json();

			if (success) {
				closeModal({
					id: `${portletNamespace}inviteUsersDialog`,
					redirect: redirectURL,
				});
			}
			else {
				Liferay.Util.openToast({
					message: Liferay.Language.get(
						'your-request-failed-to-complete'
					),
					title: Liferay.Language.get('error'),
					type: 'danger',
				});
			}
		}
	};

	return (
		<ClayForm
			className="lfr-form-content"
			id={formId}
			onSubmit={submitForm}
		>
			{inputGroups.map((inputGroup, index) => (
				<InviteUserFormGroup
					accountRoles={inputGroup.accountRoles}
					availableAccountRoles={availableAccountRoles}
					emailAddresses={inputGroup.emailAddresses}
					id={inputGroup.id}
					index={index}
					key={inputGroup.id}
					onAccountRoleItemsChange={(items) =>
						setAccountRoles(inputGroup.id, items)
					}
					onEmailAddressItemsChange={(items) =>
						setEmailAddresses(inputGroup.id, items)
					}
					portletNamespace={portletNamespace}
				/>
			))}

			<ClayLayout.SheetFooter>
				<ClayButton
					displayType="secondary"
					onClick={() => {
						setInputGroups([
							...inputGroups,
							{
								accountRoles: [],
								emailAddresses: [],
								id: `inputGroup-${inputGroups.length}`,
							},
						]);
					}}
				>
					<span className="inline-item inline-item-before">
						<ClayIcon symbol="plus" />
					</span>

					{Liferay.Language.get('add-entry')}
				</ClayButton>
			</ClayLayout.SheetFooter>
		</ClayForm>
	);
}

export default InviteUsersForm;
