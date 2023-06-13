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

import ClayForm, {ClayInput, ClaySelect} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayModal from '@clayui/modal';
import ClayMultiSelect from '@clayui/multi-select';
import {fetch} from 'frontend-js-web';
import React, {useEffect, useMemo, useState} from 'react';

const ORGANIZATIONS_ROOT_ENDPOINT = '/o/headless-admin-user/v1.0/organizations';

const orgUrl = new URL(
	`${themeDisplay.getPathContext()}${ORGANIZATIONS_ROOT_ENDPOINT}`,
	themeDisplay.getPortalURL()
);

export default function AccountCreationModalBody({
	accountData,
	accountTypes,
	setAccountData,
}) {
	const [organizationQuery, setOrganizationQuery] = useState('');
	const [organizationData, setOrganizationData] = useState([]);
	const [organizationError, setOrganizationError] = useState(false);

	useEffect(() => {
		fetch(orgUrl.toString())
			.then((response) => response.json())
			.then((data) => setOrganizationData(data.items));
	}, []);

	const organizations = useMemo(
		() =>
			organizationData.map(({id, name}) => {
				return {label: name, value: id};
			}),
		[organizationData]
	);

	const filterOrganizations = (organizations, accountOrganizations) => {
		if (!accountOrganizations.length) {
			return organizations;
		}

		const accountValues = accountOrganizations.map((item) => item.value);

		return organizations.filter(
			(org) => !accountValues.includes(org.value)
		);
	};

	return (
		<ClayModal.Body>
			<ClayForm.Group>
				<label htmlFor="accountName">
					{Liferay.Language.get('account-name')}

					<span className="inline-item inline-item-after reference-mark">
						<ClayIcon symbol="asterisk" />

						<span className="hide-accessible sr-only">
							{Liferay.Language.get('required')}
						</span>
					</span>
				</label>

				<ClayInput
					maxLength={100}
					name="accountName"
					onChange={(event) =>
						setAccountData({
							...accountData,
							name: event.target.value,
						})
					}
					required
					type="text"
					value={accountData.name}
				/>
			</ClayForm.Group>

			<ClayForm.Group className={organizationError && 'has-error'}>
				<label htmlFor="accountOrganizations">
					{Liferay.Language.get('organizations')}
				</label>

				<ClayMultiSelect
					closeButtonAriaLabel={Liferay.Language.get('remove')}
					items={accountData.organizations}
					name="accountOrganizations"
					onChange={setOrganizationQuery}
					onItemsChange={(newItems) => {
						setOrganizationError(false);

						const validItems = [];

						newItems.map((item) => {
							if (
								organizationData.find(
									(organization) =>
										organization.name.toLowerCase() ===
										item.label.toLowerCase()
								)
							) {
								validItems.push(item);
							}
							else {
								setOrganizationError(true);
							}
						});

						setAccountData({
							...accountData,
							organizations: validItems,
						});
					}}
					sourceItems={filterOrganizations(
						organizations,
						accountData.organizations
					)}
					value={organizationQuery}
				/>

				{organizationError && (
					<ClayForm.FeedbackGroup>
						<ClayForm.FeedbackItem>
							<ClayForm.FeedbackIndicator symbol="info-circle" />

							{Liferay.Language.get('select-from-list')}
						</ClayForm.FeedbackItem>
					</ClayForm.FeedbackGroup>
				)}
			</ClayForm.Group>

			<ClayForm.Group>
				<label htmlFor="accountType">
					{Liferay.Language.get('type')}
				</label>

				<ClaySelect name="accountType">
					{accountTypes.map((type) => (
						<ClaySelect.Option
							key={type}
							label={`${type[0].toUpperCase()}${type
								.substr(1)
								.toLowerCase()}`}
							onChange={(event) =>
								setAccountData({
									...accountData,
									type: event.target.value,
								})
							}
							value={accountData.type}
						/>
					))}
				</ClaySelect>
			</ClayForm.Group>

			<ClayForm.Group>
				<label htmlFor="accountTaxId">
					<span>{Liferay.Language.get('tax-id')}</span>

					<span
						className="label-icon lfr-portal-tooltip ml-2"
						data-tooltip-align="top"
						title={Liferay.Language.get('tax-id-help')}
					>
						<ClayIcon symbol="question-circle-full" />
					</span>
				</label>

				<ClayInput
					name="accountTaxId"
					onChange={(event) =>
						setAccountData({
							...accountData,
							taxId: event.target.value,
						})
					}
					type="text"
					value={accountData.taxId}
				/>
			</ClayForm.Group>

			<ClayForm.Group>
				<label htmlFor="accountERC">
					{Liferay.Language.get('external-reference-code')}
				</label>

				<ClayInput
					name="accountERC"
					onChange={(event) =>
						setAccountData({
							...accountData,
							externalReferenceCode: event.target.value,
						})
					}
					type="text"
					value={accountData.externalReferenceCode}
				/>
			</ClayForm.Group>

			<ClayForm.Group>
				<label htmlFor="accountDescription">
					{Liferay.Language.get('description')}
				</label>

				<ClayInput
					component="textarea"
					name="accountDescription"
					onChange={(event) =>
						setAccountData({
							...accountData,
							description: event.target.value,
						})
					}
					type="text"
					value={accountData.description}
				/>
			</ClayForm.Group>
		</ClayModal.Body>
	);
}
