/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayButton from '@clayui/button';
import ClayEmptyState from '@clayui/empty-state';
import {ClayCheckbox} from '@clayui/form';
import ClayModal, {useModal} from '@clayui/modal';
import ClayTable from '@clayui/table';
import {ManagementToolbar} from 'frontend-js-components-web';
import React, {useState} from 'react';

import sub from '../../utils/language/sub';

function SearchableTypesModal({
	initialSelectedTypes,
	observer,
	onClose,
	onFetchSearchableTypes,
	onFrameworkConfigChange,
	searchableTypes,
}) {
	const [modalSelectedTypes, setModalSelectedTypes] = useState(
		initialSelectedTypes
	);

	const searchableTypesClassNames = searchableTypes.map(
		({className}) => className
	);

	const _handleModalDone = () => {
		onClose();

		onFrameworkConfigChange({
			searchableAssetTypes: modalSelectedTypes,
		});
	};

	const _handleRowCheck = (type) => () => {
		const isSelected = modalSelectedTypes.includes(type);

		setModalSelectedTypes(
			isSelected
				? modalSelectedTypes.filter((item) => item !== type)
				: [...modalSelectedTypes, type]
		);
	};

	return (
		<ClayModal
			className="modal-height-xl sxp-searchable-types-modal-root"
			observer={observer}
			size="lg"
		>
			<ClayModal.Header>
				{Liferay.Language.get('select-types')}
			</ClayModal.Header>

			{searchableTypes.length ? (
				<>
					<ManagementToolbar.Container
						className={
							!!modalSelectedTypes.length &&
							'management-bar-primary'
						}
					>
						<div className="navbar-form navbar-form-autofit navbar-overlay">
							<ManagementToolbar.ItemList>
								<ManagementToolbar.Item>
									<ClayCheckbox
										checked={!!modalSelectedTypes.length}
										indeterminate={
											!!modalSelectedTypes.length &&
											modalSelectedTypes.length !==
												searchableTypes.length
										}
										onChange={() =>
											setModalSelectedTypes(
												!modalSelectedTypes.length
													? searchableTypesClassNames
													: []
											)
										}
									/>
								</ManagementToolbar.Item>

								<ManagementToolbar.Item>
									{modalSelectedTypes.length ? (
										<>
											<span className="component-text">
												{sub(
													Liferay.Language.get(
														'x-of-x-selected'
													),
													[
														modalSelectedTypes.length,
														searchableTypes.length,
													],
													false
												)}
											</span>

											{modalSelectedTypes.length <
												searchableTypes.length && (
												<ClayButton
													displayType="link"
													onClick={() => {
														setModalSelectedTypes(
															searchableTypesClassNames
														);
													}}
													small
												>
													{Liferay.Language.get(
														'select-all'
													)}
												</ClayButton>
											)}
										</>
									) : (
										<span className="component-text">
											{Liferay.Language.get('select-all')}
										</span>
									)}
								</ManagementToolbar.Item>
							</ManagementToolbar.ItemList>
						</div>
					</ManagementToolbar.Container>

					<ClayModal.Body scrollable>
						<ClayTable>
							<ClayTable.Body>
								{searchableTypes.map(
									({className, displayName}) => {
										const isSelected = modalSelectedTypes.includes(
											className
										);

										return (
											<ClayTable.Row
												active={isSelected}
												key={className}
												onClick={_handleRowCheck(
													className
												)}
											>
												<ClayTable.Cell>
													<ClayCheckbox
														checked={isSelected}
														onChange={_handleRowCheck(
															className
														)}
													/>
												</ClayTable.Cell>

												<ClayTable.Cell
													expanded
													headingTitle
												>
													{displayName}
												</ClayTable.Cell>
											</ClayTable.Row>
										);
									}
								)}
							</ClayTable.Body>
						</ClayTable>
					</ClayModal.Body>
				</>
			) : (
				<ClayModal.Body>
					<ClayEmptyState
						description={Liferay.Language.get(
							'an-error-has-occurred-and-we-were-unable-to-load-the-results'
						)}
						imgSrc="/o/admin-theme/images/states/empty_state.gif"
						title={Liferay.Language.get('no-items-were-found')}
					>
						<ClayButton
							displayType="secondary"
							onClick={onFetchSearchableTypes}
						>
							{Liferay.Language.get('refresh')}
						</ClayButton>
					</ClayEmptyState>
				</ClayModal.Body>
			)}

			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton displayType="secondary" onClick={onClose}>
							{Liferay.Language.get('cancel')}
						</ClayButton>

						<ClayButton onClick={_handleModalDone}>
							{Liferay.Language.get('done')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</ClayModal>
	);
}

export default function ({
	children,
	initialSelectedTypes,
	onFetchSearchableTypes,
	onFrameworkConfigChange,
	searchableTypes,
}) {
	const {observer, onOpenChange, open} = useModal();

	const _handleClose = () => {
		onOpenChange(false);
	};

	return (
		<>
			{open && (
				<SearchableTypesModal
					initialSelectedTypes={initialSelectedTypes}
					observer={observer}
					onClose={_handleClose}
					onFetchSearchableTypes={onFetchSearchableTypes}
					onFrameworkConfigChange={onFrameworkConfigChange}
					searchableTypes={searchableTypes}
				/>
			)}

			<span onClick={() => onOpenChange(!open)}>{children}</span>
		</>
	);
}
