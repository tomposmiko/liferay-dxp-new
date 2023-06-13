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

import Table from '../../../../../../../../common/components/Table';
import {TypeActivityKey} from '../../../../../../../../common/enums/TypeActivityKey';
import MDFRequestActivity from '../../../../../../../../common/interfaces/mdfRequestActivity';
import {Liferay} from '../../../../../../../../common/services/liferay';
import ActivityContent from './components/ActivityContent';
import getContentMarketFields from './utils/getContentMarketFields';
import getDigitalMarketFields from './utils/getDigitalMarketFields';
import getEventFields from './utils/getEventFields';
import getMiscellaneousMarketing from './utils/getMiscellaneousMarketing';

interface IProps {
	mdfRequestActivity: MDFRequestActivity;
}

interface Item {
	[key: string]: string | undefined;
}

type TypeOfActivityComponent = {
	[key in TypeActivityKey]: Item[];
};

const ActivityReviewEntry = ({mdfRequestActivity}: IProps) => {
	const fieldsByTypeActivity: TypeOfActivityComponent = {
		[TypeActivityKey.DIGITAL_MARKETING]: getDigitalMarketFields(
			mdfRequestActivity
		),
		[TypeActivityKey.CONTENT_MARKETING]: getContentMarketFields(
			mdfRequestActivity
		),
		[TypeActivityKey.EVENT]: getEventFields(mdfRequestActivity),
		[TypeActivityKey.MISCELLANEOUS_MARKETING]: getMiscellaneousMarketing(
			mdfRequestActivity
		),
	};

	return (
		<>
			<Table<Item>
				borderless
				className="bg-brand-primary-lighten-6 border-top table-striped"
				columns={[
					{
						columnKey: 'title',
						label: 'Campaign Activity',
					},
					{
						columnKey: 'value',
						label: '',
					},
				]}
				rows={[
					{
						title: 'Activity name',
						value: mdfRequestActivity.name,
					},
					{
						title: 'Type of Activity',
						value: mdfRequestActivity.typeActivity.name,
					},
					{
						title: 'Tactic',
						value: mdfRequestActivity.tactic.name,
					},
					...fieldsByTypeActivity[
						mdfRequestActivity.typeActivity.key as TypeActivityKey
					],
					{
						title: 'Start Date',
						value:
							mdfRequestActivity.startDate &&
							new Date(
								mdfRequestActivity.startDate
							).toLocaleDateString(
								Liferay.ThemeDisplay.getBCP47LanguageId()
							),
					},
					{
						title: 'End Date',
						value:
							mdfRequestActivity.endDate &&
							new Date(
								mdfRequestActivity.endDate
							).toLocaleDateString(
								Liferay.ThemeDisplay.getBCP47LanguageId()
							),
					},
				]}
				truncate
			/>

			<ActivityContent mdfRequestActivity={mdfRequestActivity} />
		</>
	);
};
export default ActivityReviewEntry;
