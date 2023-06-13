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
import ClayForm from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayModal, {useModal} from '@clayui/modal';
import ClayPopover from '@clayui/popover';
import React, {useState} from 'react';

import useControlledState from '../../../core/hooks/useControlledState';
import {FRAGMENT_CLASS_PLACEHOLDER} from '../../config/constants/fragmentClassPlaceholder';
import {useId} from '../../utils/useId';
import CodeMirrorEditor from '../CodeMirrorEditor';

const defaultValue = `.${FRAGMENT_CLASS_PLACEHOLDER} {\n\n}`;

export default function CustomCSSField({field, onValueSelect, value}) {
	const id = useId();

	const [customCSS, setCustomCSS] = useControlledState(value || defaultValue);
	const [editorModalOpen, setEditorModalOpen] = useState(false);

	const onSelect = (content) => {
		if (defaultValue.trim() === content?.trim()) {
			if (value) {
				onValueSelect(field.name, '');
			}

			return;
		}

		if (value !== content) {
			onValueSelect(field.name, content);
		}
	};

	return (
		<>
			<ClayForm.Group className="page-editor__custom-css-field" small>
				<div className="align-items-end d-flex justify-content-between">
					<label htmlFor={id}>
						{Liferay.Language.get('custom-css')} <CustomCSSHelp />
					</label>

					<ClayButtonWithIcon
						className="mb-2 p-0 page-editor__custom-css-field__expand-button text-secondary"
						displayType="unstyled"
						monospaced
						onClick={() => setEditorModalOpen(true)}
						small
						symbol="expand"
						title={Liferay.Language.get('expand')}
					/>
				</div>

				<textarea
					className="form-control text-3"
					id={id}
					onBlur={() => {
						onSelect(customCSS);
					}}
					onChange={(event) => setCustomCSS(event.target.value)}
					value={customCSS}
				/>
			</ClayForm.Group>

			<CustomCSSEditorModal
				customCSS={customCSS}
				onClose={() => {
					setEditorModalOpen(false);
				}}
				onSave={(content) => {
					setCustomCSS(content);
					onSelect(content);
				}}
				visible={editorModalOpen}
			/>
		</>
	);
}

function CustomCSSHelp() {
	const id = useId();

	const [showPopover, setShowPopover] = useState(false);

	return (
		<ClayPopover
			alignPosition="top"
			className="position-fixed"
			disableScroll
			header={Liferay.Language.get('custom-css')}
			id={id}
			onShowChange={setShowPopover}
			role="tooltip"
			show={showPopover}
			trigger={
				<span
					aria-describedby={id}
					onBlur={() => setShowPopover(false)}
					onFocus={() => setShowPopover(true)}
					onMouseEnter={() => setShowPopover(true)}
					onMouseLeave={() => setShowPopover(false)}
					tabIndex="0"
				>
					<ClayIcon
						className="text-secondary"
						symbol="info-panel-open"
					/>
				</span>
			}
		>
			{Liferay.Language.get(
				'you-can-add-your-own-css-and-include-variables-to-use-existing-tokens'
			)}
		</ClayPopover>
	);
}

function CustomCSSEditorModal({
	customCSS,
	onClose: onCloseCallback,
	onSave,
	visible,
}) {
	const [content, setContent] = useControlledState(customCSS);

	const {observer, onClose} = useModal({
		onClose: () => {
			onCloseCallback();
		},
	});

	return (
		visible && (
			<ClayModal observer={observer} size="full-screen">
				<ClayModal.Header>
					{Liferay.Language.get('custom-css')}
				</ClayModal.Header>

				<ClayModal.Body>
					<CodeMirrorEditor
						className="page-editor__custom-css-field__editor-modal"
						initialContent={content}
						mode="css"
						onChange={setContent}
					/>
				</ClayModal.Body>

				<ClayModal.Footer
					last={
						<ClayButton.Group spaced>
							<ClayButton
								displayType="secondary"
								onClick={() => {
									onClose();
								}}
							>
								{Liferay.Language.get('cancel')}
							</ClayButton>

							<ClayButton
								onClick={() => {
									onSave(content);
									onClose();
								}}
							>
								{Liferay.Language.get('add')}
							</ClayButton>
						</ClayButton.Group>
					}
				/>
			</ClayModal>
		)
	);
}
