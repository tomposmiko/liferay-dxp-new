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

import ClayAlert from '@clayui/alert';
import {ButtonWithIcon} from '@clayui/core';
import {Align} from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import ClayModal, {useModal} from '@clayui/modal';
import React, {useEffect, useState} from 'react';
import i18n from '../../../../../common/I18n';
import {Button, ButtonDropDown} from '../../../../../common/components';
import SetupDXPCloudForm from '../../../../../common/containers/setup-forms/SetupDXPCloudForm';
import {useAppPropertiesContext} from '../../../../../common/contexts/AppPropertiesContext';
import {
	getAccountSubscriptionGroups,
	getCommerceOrderItems,
} from '../../../../../common/services/liferay/graphql/queries';
import getActivationStatusDateRange from '../../../../../common/utils/getActivationStatusDateRange';
import {ALERT_UPDATE_DXP_CLOUD_STATUS} from '../../../containers/ActivationKeysTable/utils/constants';
import {useCustomerPortal} from '../../../context';
import {actionTypes} from '../../../context/reducer';
import {
	AUTO_CLOSE_ALERT_TIME,
	STATUS_TAG_TYPES,
	STATUS_TAG_TYPE_NAMES,
} from '../../../utils/constants';
import ModalDXPCActivationStatus from '../../ModalDXPCActivationStatus';
import AlreadySubmittedFormModal from '../AlreadySubmittedModal';
import ActivationStatusLayout from '../Layout';

const submittedModalTexts = {
	paragraph: i18n.translate(
		'return-to-the-product-activation-page-to-view-the-current-activation-status'
	),
	subtitle: i18n.translate(
		'we-ll-need-a-few-details-to-finish-building-your-dxp-environment'
	),
	text: i18n.translate(
		'another-user-already-submitted-the-dxp-cloud-activation-request'
	),
	title: i18n.translate('set-up-dxp-cloud'),
};

const SetupDXPCloudModal = ({
	observer,
	onClose,
	project,
	subscriptionGroupId,
}) => {
	const [formAlreadySubmitted, setFormAlreadySubmitted] = useState(false);
	const {client} = useAppPropertiesContext();

	return (
		<ClayModal center observer={observer}>
			{formAlreadySubmitted ? (
				<AlreadySubmittedFormModal
					onClose={onClose}
					submittedModalTexts={submittedModalTexts}
				/>
			) : (
				<SetupDXPCloudForm
					client={client}
					handlePage={onClose}
					leftButton={i18n.translate('cancel')}
					project={project}
					setFormAlreadySubmitted={setFormAlreadySubmitted}
					subscriptionGroupId={subscriptionGroupId}
				/>
			)}
		</ClayModal>
	);
};

const ActivationStatusDXPCloud = ({
	dxpCloudEnvironment,
	project,
	subscriptionGroupDXPCloud,
	userAccount,
}) => {
	const [projectIdValue, setProjectIdValue] = useState('');
	const [
		subscriptionGroupActivationStatus,
		setSubscriptionGroupActivationStatus,
	] = useState(subscriptionGroupDXPCloud?.activationStatus);
	const [, dispatch] = useCustomerPortal();
	const {client, liferayWebDAV} = useAppPropertiesContext();
	const [hasFinishedUpdate, setHasFinishedUpdate] = useState(false);
	const [activationStatusDate, setActivationStatusDate] = useState('');
	const [visibleSetup, setVisibleSetup] = useState(false);
	const setupModalProps = useModal({
		onClose: () => setVisibleSetup(false),
	});

	const [visibleStatus, setVisibleStatus] = useState(false);
	const activationStatusModalProps = useModal({
		onClose: () => setVisibleStatus(false),
	});

	const projectID = dxpCloudEnvironment?.projectId;

	const onCloseSetupModal = async (isSuccess) => {
		setVisibleSetup(false);

		if (isSuccess) {
			const getSubscriptionGroups = async (accountKey) => {
				const {data: dataSubscriptionGroups} = await client.query({
					query: getAccountSubscriptionGroups,
					variables: {
						filter: `accountKey eq '${accountKey}' and hasActivation eq true`,
					},
				});

				if (dataSubscriptionGroups) {
					const items =
						dataSubscriptionGroups?.c?.accountSubscriptionGroups
							?.items;
					dispatch({
						payload: items,
						type: actionTypes.UPDATE_SUBSCRIPTION_GROUPS,
					});

					setSubscriptionGroupActivationStatus(
						STATUS_TAG_TYPE_NAMES.inProgress
					);
				}
			};

			getSubscriptionGroups(project.accountKey);
		}
	};

	const currentActivationStatus = {
		[STATUS_TAG_TYPE_NAMES.active]: {
			buttonLink: (
				<a
					className="font-weight-semi-bold m-0 p-0 text-brand-primary text-paragraph"
					href={`https://console.liferay.cloud/projects/${dxpCloudEnvironment?.projectId}/overview`}
					rel="noopener noreferrer"
					target="_blank"
				>
					{i18n.translate('go-to-product-console')}

					<ClayIcon className="ml-1" symbol="order-arrow-right" />
				</a>
			),
			id: STATUS_TAG_TYPES.active,
			subtitle: i18n.translate(
				'your-dxp-cloud-environments-are-ready-go-to-the-product-console-to-view-dxp-cloud-details'
			),
			title: i18n.translate('activation-status'),
		},
		[STATUS_TAG_TYPE_NAMES.inProgress]: {
			dropdownIcon: userAccount.isStaff && userAccount.isProvisioning && (
				<ButtonDropDown
					align={Align.BottomRight}
					customDropDownButton={
						<ButtonWithIcon
							displayType="null"
							small
							symbol="caret-bottom"
						/>
					}
					items={[
						{
							label: i18n.translate('set-to-active'),
							onClick: () => setVisibleStatus(true),
						},
					]}
					menuElementAttrs={{
						className: 'p-0 cp-activation-key-icon rounded-xs',
					}}
				/>
			),
			id: STATUS_TAG_TYPES.inProgress,
			subtitle: i18n.translate(
				'your-dxp-cloud-environments-are-being-set-up-and-will-be-available-soon'
			),
			title: i18n.translate('activation-status'),
		},
		[STATUS_TAG_TYPE_NAMES.notActivated]: {
			buttonLink: userAccount.isAdmin && (
				<Button
					appendIcon="order-arrow-right"
					className="btn btn-link font-weight-semi-bold p-0 text-brand-primary text-paragraph"
					displayType="link"
					onClick={() => setVisibleSetup(true)}
				>
					{i18n.translate('finish-activation')}
				</Button>
			),
			id: STATUS_TAG_TYPES.notActivated,
			subtitle: i18n.translate(
				'almost-there-setup-dxp-cloud-by-finishing-the-activation-form'
			),
			title: i18n.translate('activation-status'),
		},
	};

	const activationStatus =
		currentActivationStatus[
			subscriptionGroupActivationStatus ||
				STATUS_TAG_TYPE_NAMES.notActivated
		];

	useEffect(() => {
		const fetchCommerceOrderItems = async () => {
			const filterAccountSubscriptionERC = `customFields/accountSubscriptionGroupERC eq '${project.accountKey}_dxp-cloud'`;
			const {data} = await client.query({
				query: getCommerceOrderItems,
				variables: {
					filter: filterAccountSubscriptionERC,
				},
			});

			if (data) {
				const activationStatusDateRange = getActivationStatusDateRange(
					data?.orderItems?.items
				);
				setActivationStatusDate(activationStatusDateRange);
			}
		};

		fetchCommerceOrderItems();
	}, [client, project]);

	return (
		<>
			{visibleSetup && (
				<SetupDXPCloudModal
					{...setupModalProps}
					onClose={onCloseSetupModal}
					project={project}
					subscriptionGroupId={
						subscriptionGroupDXPCloud.accountSubscriptionGroupId
					}
				/>
			)}
			<ActivationStatusLayout
				activationStatus={activationStatus}
				activationStatusDate={activationStatusDate}
				iconPath={`${liferayWebDAV}/assets/navigation-menu/dxp_icon.svg`}
				project={project}
				subscriptionGroupActivationStatus={
					subscriptionGroupActivationStatus
				}
			/>
			{visibleStatus && (
				<ModalDXPCActivationStatus
					{...activationStatusModalProps}
					accountKey={project.accountKey}
					projectID={projectID}
					projectIdValue={projectIdValue}
					setHasFinishedUpdate={setHasFinishedUpdate}
					setProjectIdValue={setProjectIdValue}
					setSubscriptionGroupActivationStatus={
						setSubscriptionGroupActivationStatus
					}
				/>
			)}
			{hasFinishedUpdate && (
				<ClayAlert.ToastContainer>
					<ClayAlert
						autoClose={AUTO_CLOSE_ALERT_TIME.success}
						displayType="success"
						onClose={() => setHasFinishedUpdate(false)}
					>
						{ALERT_UPDATE_DXP_CLOUD_STATUS.success}
					</ClayAlert>
				</ClayAlert.ToastContainer>
			)}
		</>
	);
};

export default ActivationStatusDXPCloud;
