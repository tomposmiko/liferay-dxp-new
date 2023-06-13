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

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import {fetch, navigate, objectToFormData} from 'frontend-js-web';
import React, {useState} from 'react';

import CollapsablePanel from '../components/form/CollapsablePanel';
import TextField from '../components/form/TextField';
import {ManageCollaborators} from '../components/manage-collaborators-modal/ManageCollaborators';
import {showNotification} from '../util/util';

export default function PublicationTemplateEditView({
	actionUrl,
	collaboratorsProps,
	ctCollectionTemplateId,
	description,
	getTemplateCollaboratorsURL,
	name,
	namespace,
	publicationDescription,
	publicationName,
	redirect,
	saveButtonLabel,
	tokens,
}) {
	const [showModal, setShowModal] = useState(false);
	const [collaboratorData, setCollaboratorData] = useState(null);
	const [nameField, setNameField] = useState(name);
	const [descriptionField, setDescriptionField] = useState(description);
	const [publicationNameField, setPublicationNameField] = useState(
		publicationName
	);
	const [
		publicationDescriptionField,
		setPublicationDescriptionField,
	] = useState(publicationDescription);

	const afterSubmitNotification = () => {
		setShowModal(false);
		setCollaboratorData(null);
	};

	const handleSubmit = () => {
		const bodyContent = objectToFormData({
			[`${namespace}name`]: nameField,
			[`${namespace}ctCollectionTemplateId`]: ctCollectionTemplateId,
			[`${namespace}description`]: descriptionField,
			[`${namespace}publicationName`]: publicationNameField,
			[`${namespace}publicationDescription`]: publicationDescriptionField,
			[`${namespace}publicationsUserRoleUserIds`]: collaboratorData
				? collaboratorData['publicationsUserRoleUserIds']
				: null,
			[`${namespace}roleValues`]: collaboratorData
				? collaboratorData['roleValues']
				: null,
			[`${namespace}userIds`]: collaboratorData
				? collaboratorData['userIds']
				: null,
		});

		fetch(actionUrl, {
			body: bodyContent,
			method: 'POST',
		})
			.then((response) => {
				if (response.status === 200) {
					const successMessage =
						ctCollectionTemplateId > 0
							? 'Successfully edited template'
							: 'Successfully added template';

					showNotification(
						successMessage,
						false,
						afterSubmitNotification
					);

					if (response.redirected) {
						navigate(response.url);
					}

					return;
				}

				showNotification(
					response.statusText,
					true,
					afterSubmitNotification
				);

				if (response.redirected) {
					navigate(response.url);
				}
			})
			.catch((error) => {
				showNotification(error.message, true, afterSubmitNotification);
			});
	};

	return (
		<div className="sheet sheet-lg">
			<TextField
				ariaLabel={Liferay.Language.get(
					'publication-template-name-placeholder'
				)}
				componentType="input"
				fieldValue={nameField}
				label="Name"
				onChange={(event) => {
					setNameField(event.target.value);
				}}
				placeholderValue={Liferay.Language.get(
					'publication-template-name-placeholder'
				)}
				required={true}
				validateLength={true}
			/>

			<TextField
				ariaLabel={Liferay.Language.get(
					'publication-template-description-placeholder'
				)}
				componentType="textarea"
				fieldValue={descriptionField}
				label="Description"
				onChange={(event) => {
					setDescriptionField(event.target.value);
				}}
				placeholderValue={Liferay.Language.get(
					'publication-template-description-placeholder'
				)}
				required={false}
			/>

			<CollapsablePanel title="Publication Information">
				<ClayAlert
					className="alert-autofit-stacked alert-indicator-start"
					displayType="info"
					title={Liferay.Language.get('info')}
				>
					{Liferay.Language.get('publication-template-token-help')}

					<ul>
						{tokens.map((token, i) => (
							<li key={i}>{token}</li>
						))}
					</ul>
				</ClayAlert>

				<TextField
					ariaLabel={Liferay.Language.get(
						'publication-name-placeholder'
					)}
					componentType="input"
					fieldValue={publicationNameField}
					label="Publication Name"
					onChange={(event) => {
						setPublicationNameField(event.target.value);
					}}
					placeholderValue={Liferay.Language.get(
						'publication-name-placeholder'
					)}
					required={true}
				/>

				<TextField
					ariaLabel={Liferay.Language.get(
						'publication-description-placeholder'
					)}
					componentType="textarea"
					fieldValue={publicationDescriptionField}
					label="Publication Description"
					onChange={(event) => {
						setPublicationDescriptionField(event.target.value);
					}}
					placeholderValue={Liferay.Language.get(
						'publication-description-placeholder'
					)}
					required={false}
				/>
			</CollapsablePanel>

			<CollapsablePanel
				helpTooltip={Liferay.Language.get(
					'publication-collaborators-help'
				)}
				title="Publication Collaborators"
			>
				<ManageCollaborators
					getTemplateCollaboratorsURL={
						ctCollectionTemplateId !== 0
							? getTemplateCollaboratorsURL
							: null
					}
					isPublicationTemplate={true}
					setCollaboratorData={setCollaboratorData}
					setShowModal={setShowModal}
					showModal={showModal}
					trigger={
						<ClayButton
							displayType="info"
							onClick={() => setShowModal(true)}
							small
							type="button"
							value="asdf"
						>
							Invite Users
						</ClayButton>
					}
					{...collaboratorsProps}
				/>
			</CollapsablePanel>

			<div className="button-group">
				<ClayButton
					disabled={
						!nameField.length ||
						!publicationNameField.length ||
						nameField.length > 75
					}
					displayType="primary"
					id="saveButton"
					onClick={() => handleSubmit()}
					type="submit"
				>
					{saveButtonLabel}
				</ClayButton>

				<ClayButton
					displayType="secondary"
					onClick={() => navigate(redirect)}
					type="cancel"
				>
					{Liferay.Language.get('cancel')}
				</ClayButton>
			</div>
		</div>
	);
}
