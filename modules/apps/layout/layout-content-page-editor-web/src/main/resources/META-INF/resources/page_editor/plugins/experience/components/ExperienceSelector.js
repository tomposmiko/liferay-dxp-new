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
import ClayIcon from '@clayui/icon';
import {useModal} from '@clayui/modal';
import {useIsMounted} from 'frontend-js-react-web';
import React, {useContext, useEffect, useState} from 'react';

import {LAYOUT_DATA_ITEM_TYPES} from '../../../app/config/constants/layoutDataItemTypes';
import {ConfigContext} from '../../../app/config/index';
import {useDispatch, useSelector} from '../../../app/store/index';
import createExperience from '../thunks/createExperience';
import removeExperience from '../thunks/removeExperience';
import updateExperience from '../thunks/updateExperience';
import updateExperiencePriority from '../thunks/updateExperiencePriority';
import {
	storeModalExperienceState,
	recoverModalExperienceState,
	useDebounceCallback
} from '../utils.js';
import ExperienceModal from './ExperienceModal';
import ExperiencesList from './ExperiencesList';

/**
 * It produces an object with a target and subtarget keys indicating what experiences
 * should change to change the priority of target priority.
 */
function getUpdateExperiencePriorityTargets(
	orderedExperiences,
	targetExperienceId,
	direction
) {
	const targetIndex = orderedExperiences.findIndex(
		experience => experience.segmentsExperienceId === targetExperienceId
	);

	let subtargetIndex;

	if (direction === 'up') {
		subtargetIndex = targetIndex - 1;
	} else {
		subtargetIndex = targetIndex + 1;
	}

	const subtargetExperience = orderedExperiences[subtargetIndex];
	const targetExperience = orderedExperiences[targetIndex];

	return {
		subtarget: {
			priority: targetExperience.priority,
			segmentsExperienceId: subtargetExperience.segmentsExperienceId
		},
		target: {
			priority: subtargetExperience.priority,
			segmentsExperienceId: targetExperience.segmentsExperienceId
		}
	};
}

const ExperienceSelector = ({
	experiences,
	segments,
	selectId,
	selectedExperience
}) => {
	const config = useContext(ConfigContext);
	const dispatch = useDispatch();
	const layoutData = useSelector(state => state.layoutData);
	const layoutDataList = useSelector(state => state.layoutDataList);

	const {
		classPK,
		defaultSegmentsExperienceId,
		editSegmentsEntryURL,
		hasEditSegmentsEntryPermission,
		hasUpdatePermissions,
		selectedSegmentsEntryId
	} = config;

	const isMounted = useIsMounted();
	const [open, setOpen] = useState(false);
	const [openModal, setOpenModal] = useState(false);
	const [editingExperience, setEditingExperience] = useState({});

	const {observer: modalObserver, onClose: onModalClose} = useModal({
		onClose: () => {
			setOpenModal(false);
		}
	});

	const [debouncedSetOpen] = useDebounceCallback(value => {
		if (isMounted()) {
			setOpen(value);
		}
	}, 100);

	const handleDropdownButtonClick = () => debouncedSetOpen(!open);
	const handleDropdownButtonBlur = () => debouncedSetOpen(false);
	const handleDropdownBlur = () => debouncedSetOpen(false);
	const handleDropdownFocus = () => debouncedSetOpen(true);

	const handleNewSegmentClick = ({
		experienceId,
		experienceName,
		segmentId
	}) => {
		storeModalExperienceState({
			classPK,
			experienceId,
			experienceName,
			segmentId
		});

		Liferay.Util.navigate(editSegmentsEntryURL);
	};

	useEffect(() => {
		if (classPK) {
			const modalExperienceState = recoverModalExperienceState();

			if (
				modalExperienceState &&
				classPK === modalExperienceState.classPK
			) {
				setOpenModal(true);
				setEditingExperience({
					name: modalExperienceState.experienceName,
					segmentsEntryId:
						selectedSegmentsEntryId ||
						modalExperienceState.segmentId,
					segmentsExperienceId: modalExperienceState.experienceId
				});
			}
		}
	}, [classPK, selectedSegmentsEntryId]);

	const handleExperienceCreation = ({
		name,
		segmentsEntryId,
		segmentsExperienceId
	}) => {
		if (segmentsExperienceId) {
			return dispatch(
				updateExperience(
					{name, segmentsEntryId, segmentsExperienceId},
					config
				)
			)
				.then(() => {
					if (isMounted()) {
						setEditingExperience({});
						onModalClose();
					}

					Liferay.Util.openToast({
						title: Liferay.Language.get(
							'the-experience-was-updated-successfully'
						),
						type: 'success'
					});
				})
				.catch(() => {
					if (isMounted()) {
						setEditingExperience({
							error: Liferay.Language.get(
								'an-unexpected-error-occurred-while-updating-the-experience'
							),
							name,
							segmentsEntryId,
							segmentsExperienceId
						});
					}
				});
		} else {
			return dispatch(
				createExperience(
					{
						name,
						segmentsEntryId
					},
					config
				)
			)
				.then(() => {
					if (isMounted()) {
						onModalClose();
					}

					Liferay.Util.openToast({
						title: Liferay.Language.get(
							'the-experience-was-created-successfully'
						),
						type: 'success'
					});
				})
				.catch(_error => {
					if (isMounted()) {
						setEditingExperience({
							error: Liferay.Language.get(
								'an-unexpected-error-occurred-while-creating-the-experience'
							),
							name,
							segmentsEntryId,
							segmentsExperienceId
						});
					}
				});
		}
	};

	const handleOnNewExperiecneClick = () => setOpenModal(true);

	const handleEditExperienceClick = experienceData => {
		const {name, segmentsEntryId, segmentsExperienceId} = experienceData;

		setOpenModal(true);

		setEditingExperience({
			name,
			segmentsEntryId,
			segmentsExperienceId
		});
	};

	const deleteExperience = id => {
		const getLayoutDataFragmentEntryLinkIds = _layoutData =>
			Object.values(_layoutData.items)
				.filter(item => item.type === LAYOUT_DATA_ITEM_TYPES.fragment)
				.map(item => item.config.fragmentEntryLinkId);

		const upToDateLayoutDataList = layoutDataList
			.filter(
				({segmentsExperienceId}) =>
					segmentsExperienceId !==
					selectedExperience.segmentsExperienceId
			)
			.concat({
				layoutData,
				segmentsExperienceId: selectedExperience.segmentsExperienceId
			});

		const otherExperiencesFragmentEntryLinkIds = upToDateLayoutDataList
			.filter(entry => entry.segmentsExperienceId !== id)
			.map(({layoutData}) =>
				getLayoutDataFragmentEntryLinkIds(layoutData)
			)
			.reduce(
				(acc, fragmentEntryLinkIds) => acc.concat(fragmentEntryLinkIds),
				[]
			);

		const uniqueFragmentEntryLinks = getLayoutDataFragmentEntryLinkIds(
			upToDateLayoutDataList.find(
				entry => entry.segmentsExperienceId === id
			).layoutData
		).filter(
			fragmentEntryLinkId =>
				!otherExperiencesFragmentEntryLinkIds.includes(
					fragmentEntryLinkId
				)
		);

		dispatch(
			removeExperience(
				{
					fragmentEntryLinkIds: uniqueFragmentEntryLinks,
					segmentsExperienceId: id,
					selectedExperienceId:
						selectedExperience.segmentsExperienceId
				},
				config
			)
		).catch(_error => {
			// TODO handle error
		});
	};

	const decreasePriority = id => {
		const {subtarget, target} = getUpdateExperiencePriorityTargets(
			experiences,
			id,
			'down'
		);

		dispatch(
			updateExperiencePriority(
				{
					subtarget,
					target
				},
				config
			)
		);
	};
	const increasePriority = id => {
		const {subtarget, target} = getUpdateExperiencePriorityTargets(
			experiences,
			id,
			'up'
		);

		dispatch(
			updateExperiencePriority(
				{
					subtarget,
					target
				},
				config
			)
		);
	};

	return (
		<>
			<ClayButton
				className="align-items-end d-inline-flex form-control-select justify-content-between mr-2 text-left text-truncate"
				displayType="secondary"
				id={selectId}
				onBlur={handleDropdownButtonBlur}
				onClick={handleDropdownButtonClick}
				small={true}
				type="button"
			>
				<span className="text-truncate">{selectedExperience.name}</span>

				{selectedExperience.hasLockedSegmentsExperiment && (
					<ClayIcon symbol="lock" />
				)}
			</ClayButton>

			{/** render this via createPortal **/}
			{open && (
				<div
					className="dropdown-menu p-4 rounded toggled"
					onBlur={handleDropdownBlur}
					onFocus={handleDropdownFocus}
					tabIndex="-1"
				>
					<ExperiencesSelectorHeader
						canCreateExperiences={true}
						onNewExperience={handleOnNewExperiecneClick}
						showEmptyStateMessage={experiences.length <= 1}
					/>

					{experiences.length > 1 && (
						<ExperiencesList
							activeExperienceId={
								selectedExperience.segmentsExperienceId
							}
							defaultExperienceId={defaultSegmentsExperienceId}
							experiences={experiences}
							hasUpdatePermissions={hasUpdatePermissions}
							onDeleteExperience={deleteExperience}
							onEditExperience={handleEditExperienceClick}
							onPriorityDecrease={decreasePriority}
							onPriorityIncrease={increasePriority}
						/>
					)}
				</div>
			)}

			{openModal && (
				<ExperienceModal
					errorMessage={editingExperience.error}
					experienceId={editingExperience.segmentsExperienceId}
					hasSegmentsPermission={hasEditSegmentsEntryPermission}
					initialName={editingExperience.name}
					observer={modalObserver}
					onClose={onModalClose}
					onErrorDismiss={() => setEditingExperience({error: null})}
					onNewSegmentClick={handleNewSegmentClick}
					onSubmit={handleExperienceCreation}
					segmentId={editingExperience.segmentsEntryId}
					segments={segments}
				/>
			)}
		</>
	);
};

const ExperiencesSelectorHeader = ({
	canCreateExperiences,
	onNewExperience,
	showEmptyStateMessage
}) => {
	return (
		<>
			<div className="align-items-end d-flex justify-content-between mb-4">
				<h3 className="mb-0">
					{Liferay.Language.get('select-experience')}
				</h3>

				{canCreateExperiences === true && (
					<ClayButton
						aria-label={Liferay.Language.get('new-experience')}
						displayType="secondary"
						onClick={onNewExperience}
						small
					>
						{Liferay.Language.get('new-experience')}
					</ClayButton>
				)}
			</div>

			{canCreateExperiences && (
				<p className="mb-4 text-secondary">
					{showEmptyStateMessage
						? Liferay.Language.get(
								'experience-help-message-empty-state'
						  )
						: Liferay.Language.get(
								'experience-help-message-started-state'
						  )}
				</p>
			)}

			<ClayAlert
				className="mt-4 mx-0"
				displayType="warning"
				title={Liferay.Language.get('warning')}
			>
				{Liferay.Language.get(
					'changes-to-experiences-are-applied-immediately'
				)}
			</ClayAlert>
		</>
	);
};

export default ExperienceSelector;
