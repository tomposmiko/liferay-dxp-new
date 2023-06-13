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
import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import ClayModal, {useModal} from '@clayui/modal';
import ClayPopover from '@clayui/popover';
import {
	StyleErrorsModal,
	useHasStyleErrors,
} from '@liferay/layout-content-page-editor-web';
import classNames from 'classnames';
import {ALIGN_POSITIONS, align} from 'frontend-js-web';
import React, {useContext, useLayoutEffect, useRef, useState} from 'react';

import PreviewSelector from './PreviewSelector';
import {StyleBookContext} from './StyleBookContext';
import {config} from './config';
import {DRAFT_STATUS} from './constants/draftStatusConstants';

const STATUS_TO_LABEL = {
	[DRAFT_STATUS.draftSaved]: Liferay.Language.get('draft-saved'),
	[DRAFT_STATUS.notSaved]: '',
	[DRAFT_STATUS.saving]: `${Liferay.Language.get('saving')}...`,
};

export default function Toolbar() {
	const {previewLayout} = useContext(StyleBookContext);

	return (
		<div className="management-bar navbar style-book-editor__toolbar">
			<ClayLayout.ContainerFluid>
				<ul className="navbar-nav start">
					{previewLayout?.url && (
						<li className="nav-item">
							<span className="style-book-editor__page-preview-text">
								{`${Liferay.Language.get('preview')}`}
							</span>

							<PreviewSelector />
						</li>
					)}
				</ul>

				<ul className="end navbar-nav">
					<li className="mr-2 nav-item">
						<DraftStatus />
					</li>

					<li className="mx-2 nav-item">
						<HelpInformation />
					</li>

					<li className="ml-2 nav-item">
						<PublishButton />
					</li>
				</ul>
			</ClayLayout.ContainerFluid>
		</div>
	);
}

function DraftStatus() {
	const {draftStatus} = useContext(StyleBookContext);

	return (
		<div>
			{draftStatus === DRAFT_STATUS.draftSaved && (
				<ClayIcon
					className="mt-0 style-book-editor__status-icon"
					symbol="check-circle"
				/>
			)}

			<span
				className={classNames('ml-1 style-book-editor__status-text', {
					'text-success': draftStatus === DRAFT_STATUS.draftSaved,
				})}
			>
				{STATUS_TO_LABEL[draftStatus]}
			</span>
		</div>
	);
}

function HelpInformation() {
	const [isShowPopover, setIsShowPopover] = useState(false);
	const popoverRef = useRef(null);
	const helpIconRef = useRef(null);

	useLayoutEffect(() => {
		if (isShowPopover) {
			align(
				popoverRef.current,
				helpIconRef.current,
				ALIGN_POSITIONS.Bottom,
				false
			);
		}
	}, [isShowPopover]);

	return (
		<span className="d-block text-secondary">
			<ClayIcon
				onMouseEnter={() => setIsShowPopover(true)}
				onMouseLeave={() => setIsShowPopover(false)}
				ref={helpIconRef}
				symbol="question-circle"
			/>

			{isShowPopover && (
				<ClayPopover
					alignPosition="bottom"
					header={Liferay.Language.get('help-information')}
					ref={popoverRef}
				>
					{Liferay.Language.get(
						'edit-the-style-book-using-the-sidebar-form.-you-can-preview-the-changes-instantly'
					)}
				</ClayPopover>
			)}
		</span>
	);
}

function PublishButton() {
	const formRef = useRef();
	const hasStyleErrors = useHasStyleErrors();
	const [openPublishModal, setOpenPublishModal] = useState(false);
	const [openStyleErrorsModal, setOpenStyleErrorsModal] = useState(false);

	const {
		observer: observerPublishModal,
		onClose: onClosePublishModal,
	} = useModal({
		onClose: () => setOpenPublishModal(false),
	});

	const handleSubmit = (event) => {
		if (config.tokenReuseEnabled) {
			if (formRef.current) {
				formRef.current.submit();
			}

			return;
		}

		if (
			!confirm(
				Liferay.Language.get(
					'once-published,-these-changes-will-affect-all-instances-of-the-site-using-these-properties'
				)
			)
		) {
			event.preventDefault();
		}
	};

	return (
		<>
			<form action={config.publishURL} method="POST" ref={formRef}>
				<input
					name={`${config.namespace}redirect`}
					type="hidden"
					value={config.redirectURL}
				/>

				<input
					name={`${config.namespace}styleBookEntryId`}
					type="hidden"
					value={config.styleBookEntryId}
				/>

				<ClayButton
					disabled={config.pending}
					displayType="primary"
					onClick={
						config.tokenReuseEnabled
							? hasStyleErrors
								? () => setOpenStyleErrorsModal(true)
								: () => setOpenPublishModal(true)
							: handleSubmit
					}
					small
					type={config.tokenReuseEnabled ? 'button' : 'submit'}
				>
					{Liferay.Language.get('publish')}
				</ClayButton>
			</form>

			{config.tokenReuseEnabled && (
				<>
					{openStyleErrorsModal && hasStyleErrors && (
						<StyleErrorsModal
							onCloseModal={() => setOpenStyleErrorsModal(false)}
							onSubmit={() => {
								setOpenStyleErrorsModal(false);
								setOpenPublishModal(true);
							}}
						/>
					)}

					{openPublishModal && (
						<ClayModal
							observer={observerPublishModal}
							size="lg"
							status="info"
						>
							<ClayModal.Header>
								{Liferay.Language.get('publishing-info')}
							</ClayModal.Header>

							<ClayModal.Body>
								<p>
									{Liferay.Language.get(
										'once-published-these-changes-will-affect-all-instances-of-the-site-using-these-properties-do-you-want-to-publish-now'
									)}
								</p>
							</ClayModal.Body>

							<ClayModal.Footer
								last={
									<ClayButton.Group spaced>
										<ClayButton
											displayType="secondary"
											onClick={onClosePublishModal}
										>
											{Liferay.Language.get('cancel')}
										</ClayButton>

										<ClayButton
											displayType="info"
											onClick={handleSubmit}
											type="submit"
										>
											{Liferay.Language.get('continue')}
										</ClayButton>
									</ClayButton.Group>
								}
							/>
						</ClayModal>
					)}
				</>
			)}
		</>
	);
}
