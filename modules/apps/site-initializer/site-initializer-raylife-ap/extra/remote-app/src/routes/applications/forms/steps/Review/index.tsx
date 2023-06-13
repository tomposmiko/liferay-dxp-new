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

import ClayTable from '@clayui/table';
import React, {useContext} from 'react';

import {NewApplicationAutoContext} from '../../../context/NewApplicationAutoContextProvider';

const Review = () => {
	const [state] = useContext(NewApplicationAutoContext);

	const {
		steps: {
			coverage: {form},
		},
	} = state;

	const vehiclesInfoFormsCoverage = state?.steps?.coverage?.form?.vehicles;

	const coverages = [
		{
			name: 'BODILY INJURY',
			premium: '$167.00 ',
			protectionAmount: `${form.bodilyInjury}`,
		},
		{
			name: 'PROPERTY DAMAGE',
			premium: '$211.00 ',
			protectionAmount: `${form.propertyDamage}`,
		},
		{
			name: 'UNINSURED MOTORIST BODILY INJURY',
			premium: '$111.00 ',
			protectionAmount: `${form.uninsuredOrUnderinsuredMBI}`,
		},
		{
			name: 'UNINSURED MOTORIST PROPERTY DAMAGE',
			premium: '$98.00 ',
			protectionAmount: `${form.uninsuredOrUnderinsuredMPD}`,
		},
		{
			name: 'MEDICAL',
			premium: '$115.00 ',
			protectionAmount: `${form.medical}`,
		},
	];

	const vehiclesCoverageTable = vehiclesInfoFormsCoverage.map(
		(_vehicleInfoForm: any, index: number) => (
			<>
				<ClayTable.Row className="no-gutters row text-uppercase">
					<ClayTable.Cell className="col-6" headingTitle>
						<p className="text-secondary">
							Vehicle {index + 1}: Comprehensive
						</p>
					</ClayTable.Cell>

					<ClayTable.Cell className="col-4">
						<p className="text-secondary">
							{_vehicleInfoForm.comprehensive}
						</p>
					</ClayTable.Cell>

					<ClayTable.Cell className="col">
						<p className="text-secondary">$85.00</p>
					</ClayTable.Cell>
				</ClayTable.Row>

				<ClayTable.Row className="no-gutters row text-uppercase">
					<ClayTable.Cell className="col-6" headingTitle>
						<p className="text-secondary">
							Vehicle {index + 1}: Collision
						</p>
					</ClayTable.Cell>

					<ClayTable.Cell className="col-4">
						<p className="text-secondary">
							{_vehicleInfoForm.collision}
						</p>
					</ClayTable.Cell>

					<ClayTable.Cell className="col">
						<p className="text-secondary">$97.00</p>
					</ClayTable.Cell>
				</ClayTable.Row>
			</>
		)
	);

	return (
		<div className="mx-8">
			<h5 className="text-center">Review Quote</h5>

			<ClayTable borderless hover={false}>
				<ClayTable.Head className="border-bottom">
					<ClayTable.Row className="no-gutters row text-uppercase">
						<ClayTable.Cell className="col-6">
							<span className="text-dark">
								<h6>Coverages</h6>
							</span>
						</ClayTable.Cell>

						<ClayTable.Cell className="col-4">
							<span className="text-dark">
								<h6>Protection Amount</h6>
							</span>
						</ClayTable.Cell>

						<ClayTable.Cell className="col">
							<span className="text-dark">
								<h6>Premium</h6>
							</span>
						</ClayTable.Cell>
					</ClayTable.Row>
				</ClayTable.Head>

				<ClayTable.Body className="table-borderless">
					{coverages.map((coverage: any) => (
						<>
							<ClayTable.Row className="no-gutters row">
								<ClayTable.Cell className="col-6" headingTitle>
									<p className="text-secondary">
										{coverage.name}
									</p>
								</ClayTable.Cell>

								<ClayTable.Cell className="col-4">
									<p className="text-secondary">
										{coverage.protectionAmount}
									</p>
								</ClayTable.Cell>

								<ClayTable.Cell className="col">
									<p className="text-secondary">
										{coverage.premium}
									</p>
								</ClayTable.Cell>
							</ClayTable.Row>
						</>
					))}
				</ClayTable.Body>

				<ClayTable.Body className="border-0 table-borderless">
					{vehiclesCoverageTable}
				</ClayTable.Body>

				<ClayTable.Head className="border-top">
					<ClayTable.Row className="no-gutters row">
						<ClayTable.Cell
							className="col-6 text-uppercase"
							headingCell
						>
							<span className="text-dark">
								<h6>Total Premium</h6>
							</span>
						</ClayTable.Cell>

						<ClayTable.Cell
							className="col-4"
							headingCell
						></ClayTable.Cell>

						<ClayTable.Cell className="col" headingCell>
							<span className="text-dark">
								<h6>$611.00</h6>
							</span>
						</ClayTable.Cell>
					</ClayTable.Row>
				</ClayTable.Head>
			</ClayTable>
		</div>
	);
};
export default Review;
