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
import ClayForm, {ClayInput} from '@clayui/form';
import ClayModal, {useModal} from '@clayui/modal';
import {API, Input} from '@liferay/object-js-components-web';
import {fetch} from 'frontend-js-web';
import React, {FormEvent, useEffect, useRef, useState} from 'react';

import {ModalImportWarning} from './ModalImportWarning';
interface ModalImportListTypeDefinitionProps {
	importListTypeDefinitionURL: string;
	nameMaxLength: string;
	portletNamespace: string;
}

type TFile = {
	fileName?: string;
	inputFile?: File | null;
	inputFileValue?: string;
};

export default function ModalImportListTypeDefinition({
	importListTypeDefinitionURL,
	nameMaxLength,
	portletNamespace,
}: ModalImportListTypeDefinitionProps) {
	const [error, setError] = useState<string>('');
	const [externalReferenceCode, setExternalReferenceCode] = useState<string>(
		''
	);
	const [importFormData, setImportFormData] = useState<FormData>();
	const [visible, setVisible] = useState(false);
	const [warningModalVisible, setWarningModalVisible] = useState(false);
	const inputFileRef = useRef() as React.MutableRefObject<HTMLInputElement>;
	const [name, setName] = useState('');
	const importListTypeDefinitionModalComponentId = `${portletNamespace}importListTypeDefinitionModal`;
	const importListTypeDefinitionFormId = `${portletNamespace}importListTypeDefinitionForm`;
	const nameInputId = `${portletNamespace}name`;
	const listTypeDefinitionJSONInputId = `${portletNamespace}listTypeDefinitionJSON`;
	const [{fileName, inputFile, inputFileValue}, setFile] = useState<TFile>(
		{}
	);
	const {observer, onClose} = useModal({
		onClose: () => {
			setError('');
			setVisible(false);
			setExternalReferenceCode('');
			setFile({
				fileName: '',
				inputFile: null,
				inputFileValue: '',
			});
			setName('');
			setImportFormData(undefined);
		},
	});

	const warningModalBody: string[] = [
		Liferay.Language.get(
			'there-is-a-picklist-with-the-same-external-reference-code-as-the-imported-one'
		),
		Liferay.Language.get(
			'before-importing-the-new-picklist-you-may-want-to-back-up-its-entries-to-prevent-data-loss'
		),
		Liferay.Language.get('do-you-want-to-proceed-with-the-import-process'),
	];

	const handleImport = async (formData: FormData) => {
		try {
			await API.save(importListTypeDefinitionURL, formData, 'POST');

			window.location.reload();
		}
		catch (error) {
			setError((error as Error).message);
		}
	};

	const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
		event.preventDefault();

		const formData = new FormData(event.currentTarget);
		const response = await fetch(
			`/o/headless-admin-list-type/v1.0/list-type-definitions/by-external-reference-code/${externalReferenceCode}`
		);

		if (!response.ok) {
			handleImport(formData);
		}
		else {
			setImportFormData(formData);
			setVisible(false);
			setWarningModalVisible(true);
		}
	};

	useEffect(() => {
		Liferay.component(
			importListTypeDefinitionModalComponentId,
			{
				open: () => {
					setVisible(true);
				},
			},
			{
				destroyOnNavigate: true,
			}
		);

		return () =>
			Liferay.destroyComponent(importListTypeDefinitionModalComponentId);
	}, [importListTypeDefinitionModalComponentId, setVisible]);

	return visible ? (
		<ClayModal center observer={observer}>
			<ClayModal.Header>
				{Liferay.Language.get('import-picklist')}
			</ClayModal.Header>

			<ClayModal.Body>
				<ClayForm

					// @ts-ignore

					id={importListTypeDefinitionFormId}
					onSubmit={handleSubmit}
				>
					{error && (
						<ClayAlert
							displayType="danger"
							title={`${Liferay.Language.get('error')}:`}
						>
							{error}
						</ClayAlert>
					)}

					<ClayAlert
						displayType="info"
						title={`${Liferay.Language.get('info')}:`}
					>
						{Liferay.Language.get(
							'the-import-process-will-run-in-the-background-and-may-take-a-few-minutes'
						)}
					</ClayAlert>

					<ClayForm.Group>
						<label htmlFor={nameInputId}>
							{Liferay.Language.get('name')}
						</label>

						<ClayInput
							id={nameInputId}
							maxLength={Number(nameMaxLength)}
							name={nameInputId}
							onChange={(event) => setName(event.target.value)}
							type="text"
							value={name}
						/>
					</ClayForm.Group>

					<ClayForm.Group>
						<label htmlFor={listTypeDefinitionJSONInputId}>
							{Liferay.Language.get('json-file')}
						</label>

						<ClayInput.Group>
							<ClayInput.GroupItem prepend>
								<ClayInput
									disabled
									id={listTypeDefinitionJSONInputId}
									type="text"
									value={fileName}
								/>
							</ClayInput.GroupItem>

							<ClayInput.GroupItem append shrink>
								<ClayButton
									displayType="secondary"
									onClick={() => inputFileRef.current.click()}
								>
									{Liferay.Language.get('select')}
								</ClayButton>
							</ClayInput.GroupItem>

							{inputFile && (
								<ClayInput.GroupItem shrink>
									<ClayButton
										displayType="secondary"
										onClick={() => {
											setExternalReferenceCode('');
											setFile({
												fileName: '',
												inputFile: null,
												inputFileValue: '',
											});
										}}
									>
										{Liferay.Language.get('clear')}
									</ClayButton>
								</ClayInput.GroupItem>
							)}
						</ClayInput.Group>
					</ClayForm.Group>

					{externalReferenceCode && (
						<Input
							disabled
							feedbackMessage={Liferay.Language.get(
								'unique-key-for-referencing-the-picklist-definition'
							)}
							id="externalReferenceCode"
							label={Liferay.Language.get(
								'external-reference-code'
							)}
							name="externalReferenceCode"
							value={externalReferenceCode}
						/>
					)}

					<input
						className="d-none"
						name={listTypeDefinitionJSONInputId}
						onChange={({target}) => {
							const inputFile = target.files?.item(0);

							setFile({
								fileName: inputFile?.name,
								inputFile,
								inputFileValue: target.value,
							});

							const fileReader = new FileReader();

							fileReader.onload = () => {
								try {
									const objectDefinitionJSON = JSON.parse(
										fileReader.result as string
									) as {externalReferenceCode: string};

									setExternalReferenceCode(
										objectDefinitionJSON.externalReferenceCode
									);
								}
								catch (error) {
									setExternalReferenceCode('');
								}
							};
							fileReader.readAsText(inputFile!);
						}}
						ref={inputFileRef}
						type="file"
						value={inputFileValue}
					/>
				</ClayForm>
			</ClayModal.Body>

			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton displayType="secondary" onClick={onClose}>
							{Liferay.Language.get('cancel')}
						</ClayButton>

						<ClayButton
							disabled={!inputFile || !name}
							form={importListTypeDefinitionFormId}
							type="submit"
						>
							{Liferay.Language.get('import')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</ClayModal>
	) : warningModalVisible ? (
		<ModalImportWarning
			handleImport={() => handleImport(importFormData as FormData)}
			header={Liferay.Language.get('update-existing-picklist')}
			onClose={() => {
				setWarningModalVisible(false);
				setImportFormData(undefined);
			}}
			paragraphs={warningModalBody}
		/>
	) : null;
}
