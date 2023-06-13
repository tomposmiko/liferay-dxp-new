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

import {ClayButtonWithIcon} from '@clayui/button';
import ClayPanel from '@clayui/panel';
import classNames from 'classnames';

import MDFRequestActivity from '../../../../common/interfaces/mdfRequestActivity';

interface IProps {
	activity: MDFRequestActivity;
	detail?: boolean;
	onRemove: () => void;
	overallCampaign: string;
}

const ActivityPanel = ({
	activity,
	detail,
	onRemove,
	overallCampaign,
}: IProps) => {
	return (
		<ClayPanel
			className="border-brand-primary-lighten-4"
			collapsable={detail}
			displayTitle={
				<ClayPanel.Title
					className={classNames(
						'bg-brand-primary-lighten-6 text-dark',
						{
							'p-4': !detail,
							'py-2': detail,
						}
					)}
				>
					<div className="d-flex justify-content-between">
						<div>
							<div className="mb-1 text-neutral-7 text-paragraph-sm">
								{overallCampaign}
							</div>

							<h5 className="mb-1">{activity.name}</h5>
						</div>

						{!detail && (
							<ClayButtonWithIcon
								displayType={null}
								onClick={onRemove}
								small
								symbol="trash"
							/>
						)}
					</div>

					<div className="align-items-center d-flex justify-content-between">
						<div className="font-weight-semi-bold text-neutral-7 text-paragraph-sm">
							MDF Requested:
						</div>

						<h5 className="mr-4">{activity.mdfRequestAmount}</h5>
					</div>
				</ClayPanel.Title>
			}
			displayType="secondary"
			showCollapseIcon={detail}
		></ClayPanel>
	);
};

export default ActivityPanel;
