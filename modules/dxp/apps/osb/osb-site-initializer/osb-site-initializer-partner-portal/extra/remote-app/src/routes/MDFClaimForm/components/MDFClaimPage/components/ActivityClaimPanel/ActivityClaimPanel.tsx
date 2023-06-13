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

import ClayIcon from '@clayui/icon';
import Link from '@clayui/link';
import ClayPanel from '@clayui/panel';
import {FormikContextType} from 'formik';
import {useCallback, useState} from 'react';

import PRMForm from '../../../../../../common/components/PRMForm';
import InputMultipleFilesListing from '../../../../../../common/components/PRMForm/components/fields/InputMultipleFilesListing';
import PRMFormik from '../../../../../../common/components/PRMFormik';
import {useWebDAV} from '../../../../../../common/context/WebDAV';
import MDFClaim from '../../../../../../common/interfaces/mdfClaim';
import MDFClaimActivity from '../../../../../../common/interfaces/mdfClaimActivity';
import {Status} from '../../../../../../common/utils/constants/status';
import getIntlNumberFormat from '../../../../../../common/utils/getIntlNumberFormat';
import BudgetClaimPanel from './components/BudgetClaimPanel';
import PanelBody from './components/PanelBody';
import PanelHeader from './components/PanelHeader';
import useBudgetsAmount from './hooks/useBudgetsAmount';

interface IProps {
	activity: MDFClaimActivity;
	activityIndex: number;
	overallCampaignDescription: string;
}

const ActivityStatus = {
	ACTIVE: 'active',
	APPROVED: 'approved',
	CLAIMED: 'claimed',
	EXPIRED: 'expired',
	SUBMITTED: 'submitted',
	UNCLAIMED: 'unclaimed',
};

const activityStatusClassName = {
	[ActivityStatus.ACTIVE]: 'label label-tonal-info ml-2',
	[ActivityStatus.SUBMITTED]: 'label label-tonal-warning ml-2',
	[ActivityStatus.APPROVED]: 'label label-tonal-success ml-2',
	[ActivityStatus.EXPIRED]: 'label label-tonal-danger ml-2',
};

const activityClaimStatusClassName = {
	[ActivityStatus.CLAIMED]: 'ml-3 label label-tonal-info ml-2',
	[ActivityStatus.UNCLAIMED]: 'ml-3 label label-tonal-warning ml-2',
};

const ActivityClaimPanel = ({
	activity,
	activityIndex,
	overallCampaignDescription,
	setFieldValue,
}: IProps & Pick<FormikContextType<MDFClaim>, 'setFieldValue'>) => {
	const [expanded, setExpanded] = useState<boolean>(!activity.selected);
	const webDAV = useWebDAV();

	useBudgetsAmount(
		activity.budgets,
		useCallback(
			(amountValue) =>
				setFieldValue(
					`activities[${activityIndex}].totalCost`,
					amountValue
				),
			[activityIndex, setFieldValue]
		)
	);
	const displayActivityClaimCheckbox =
		activity.activityStatus?.key !== Status.EXPIRED.key &&
		!activity.claimed;

	return (
		<>
			<ClayPanel
				className="border-brand-primary-lighten-2 mb-4 text-neutral-7"
				displayType="secondary"
				expanded={activity.selected && expanded}
			>
				<PanelHeader
					expanded={activity.selected && expanded}
					onClick={() => {
						if (activity.selected) {
							setExpanded(
								(previousExpanded) => !previousExpanded
							);
						}
					}}
				>
					{displayActivityClaimCheckbox && (
						<PRMFormik.Field
							component={PRMForm.Checkbox}
							name={`activities[${activityIndex}].selected`}
						/>
					)}

					<div className="flex-grow-1 mx-3">
						<p className="mb-1 text-neutral-7 text-paragraph-sm">
							{overallCampaignDescription}
						</p>

						<h5 className="text-neutral-10">
							{activity.name} ({activity.id})
						</h5>

						<div className="align-items-center d-sm-flex mb-1 text-neutral-7 text-weight-semi-bold">
							<p className="mb-0">
								Claim Status:
								<div
									className={
										activityClaimStatusClassName[
											activity.claimed
												? 'claimed'
												: 'unclaimed'
										]
									}
								>
									{activity.claimed ? 'Claimed' : 'Unclaimed'}
								</div>
							</p>
						</div>

						<div className="align-items-center d-sm-flex mb-1 text-neutral-7 text-weight-semi-bold">
							<p className="mb-0">
								Request Status:
								<div
									className={
										activityStatusClassName[
											activity.activityStatus
												?.key as string
										]
									}
								>
									{activity.activityStatus?.name}
								</div>
							</p>
						</div>

						<div className="d-flex justify-content-end">
							<h5 className="mb-0 text-neutral-10">
								{getIntlNumberFormat().format(
									activity.totalCost
								)}
							</h5>
						</div>
					</div>
				</PanelHeader>

				<PanelBody expanded={activity.selected && expanded}>
					<ClayPanel.Body className="mx-2 pt-4 px-5">
						{activity.budgets?.map((budget, index) => (
							<BudgetClaimPanel
								activityIndex={activityIndex}
								budget={budget}
								budgetIndex={index}
								key={`${budget.id}-${index}`}
								setFieldValue={setFieldValue}
							/>
						))}

						<PRMFormik.Field
							component={PRMForm.InputText}
							label="Metrics"
							name={`activities[${activityIndex}].metrics`}
							textArea
						/>

						<div className="align-items-center d-flex justify-content-between">
							<PRMFormik.Field
								component={PRMForm.InputFile}
								description="You can downloaded the Excel Template, fill it out, and upload it back here"
								displayType="secondary"
								label="List of Qualified Leads"
								name={`activities[${activityIndex}].listQualifiedLeads`}
								onAccept={(value: File) =>
									setFieldValue(
										`activities[${activityIndex}].listQualifiedLeads`,
										value
									)
								}
								outline
								small
							/>

							<div className="mb-3">
								<Link
									button
									displayType="secondary"
									download
									href={`${webDAV}/claim/qualified_leads_template.xlsx`}
									small
									target="_blank"
								>
									<span className="inline-item inline-item-before">
										<ClayIcon symbol="download" />
									</span>
									Download template
								</Link>
							</div>
						</div>

						<InputMultipleFilesListing
							description="Drag and drop your files here to upload."
							label="All Contents"
							name={`activities[${activityIndex}].documents`}
							onAccept={(value: File[]) =>
								setFieldValue(
									`activities[${activityIndex}].documents`,
									activity.documents
										? activity.documents.concat(value)
										: value
								)
							}
							value={activity.documents}
						/>
					</ClayPanel.Body>
				</PanelBody>
			</ClayPanel>
		</>
	);
};

export default ActivityClaimPanel;
