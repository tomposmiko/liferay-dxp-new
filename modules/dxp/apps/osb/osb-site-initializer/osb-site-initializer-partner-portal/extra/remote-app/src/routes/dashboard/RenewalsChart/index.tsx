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
import ClayButton from '@clayui/button';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import classNames from 'classnames';
import {useEffect, useState} from 'react';

import Container from '../../../common/components/dashboard/components/Container';
import {status} from '../../../common/components/dashboard/utils/constants/statusColumns';
import getFilteredRenewals from '../../../common/components/dashboard/utils/getFilteredRenewalsData';
import {siteURL} from '../../../common/components/dashboard/utils/siteURL';
import {Liferay} from '../../../common/services/liferay';

export default function () {
	const [data, setData] = useState();
	const [isLoading, setIsLoading] = useState(false);

	const getRenewalsData = async () => {
		setIsLoading(true);
		// eslint-disable-next-line @liferay/portal/no-global-fetch
		const response = await fetch(
			'/o/c/opportunitysfs?pageSize=200&sort=closeDate:asc',
			{
				headers: {
					'accept': 'application/json',
					'x-csrf-token': Liferay.authToken,
				},
			}
		);
		if (response.ok) {
			const renewalsData = await response.json();

			setData(renewalsData);
			setIsLoading(false);

			return;
		}

		Liferay.Util.openToast({
			message: 'An unexpected error occured.',
			type: 'danger',
		});
	};

	useEffect(() => {
		getRenewalsData();
	}, []);

	const renewalsData = getFilteredRenewals(data);

	const getCurrentStatusColor = (item: any) => {
		if (item?.expirationDays <= 5) {
			return status[5];
		}
		else if (item?.expirationDays <= 15) {
			return status[15];
		}
		else if (item?.expirationDays <= 30) {
			return status[30];
		}
	};

	const buildChart = () => {
		if (isLoading) {
			return <ClayLoadingIndicator className="mb-10 mt-9" size="md" />;
		}

		if (!renewalsData.length && !isLoading) {
			return (
				<ClayAlert
					className="mb-8 mt-8 mx-auto text-center w-50"
					displayType="info"
					title="Info:"
				>
					No Data Available
				</ClayAlert>
			);
		}

		return (
			<div className="align-items-start d-flex flex-column justify-content-center mt-3">
				{renewalsData?.map((item, index) => {
					getCurrentStatusColor(item);

					return (
						<div
							className="align-items-center d-flex flex-row justify-content-center mb-4"
							key={index}
						>
							<div
								className={classNames(
									'mr-3 status-bar-vertical',
									getCurrentStatusColor(item)
								)}
							></div>

							<div>
								<div className="font-weight-semi-bold">
									{item.opportunityName}
								</div>

								<div>
									Expires in &nbsp;
									<span className="font-weight-semi-bold">
										{item.expirationDays} days.
									</span>
									&nbsp;
									<span className="ml-2">
										{item.closeDate}
									</span>
								</div>
							</div>
						</div>
					);
				})}
			</div>
		);
	};

	return (
		<Container
			className="renewal-chart-card-height"
			footer={
				<ClayButton
					className="border-brand-primary-darken-1 mt-2 text-brand-primary-darken-1"
					displayType="secondary"
					onClick={() =>
						Liferay.Util.navigate(
							`${siteURL}/sales/renewal-opportunities`
						)
					}
					type="button"
				>
					View all
				</ClayButton>
			}
			title="Renewals"
		>
			{buildChart()}
		</Container>
	);
}
