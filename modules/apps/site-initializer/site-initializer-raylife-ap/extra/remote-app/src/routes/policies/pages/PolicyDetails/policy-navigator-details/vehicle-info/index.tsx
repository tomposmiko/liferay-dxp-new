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

import addImageFallback from '../../../../../../common/utils/addImageFallback';
import {getWebDavUrl} from '../../../../../../common/utils/webdav';

import '../index.scss';
import {PolicyDetailsType} from '../types';

type VehicleNavigatorType = {
	dataJSON: {form: []};
};

const VehicleInfo = ({dataJSON: applicationData}: VehicleNavigatorType) => {
	return (
		<div>
			{applicationData?.form.map(
				(currentVehicle: PolicyDetailsType, index) => (
					<div
						className="bg-neutral-0 h-100 pl-6 policy-detail-border pr-6 py-6"
						key={index}
					>
						<div className="d-flex flex-row flex-wrap justify-content-between">
							{index !== 0 && (
								<div className="align-self-start col-12 layout-line mb-6 mt-1"></div>
							)}

							<div className="align-self-start w-25">
								<h5>
									{`${currentVehicle?.year} ${
										currentVehicle?.make
									} ${currentVehicle?.model ?? 'No data'}`}
								</h5>

								<img
									className="w-75"
									onError={addImageFallback}
									src={`${getWebDavUrl()}/${currentVehicle?.model.replace(
										/ /g,
										''
									)}.svg`}
								/>
							</div>

							<div className="align-self-start">
								<p className="mb-1 text-neutral-7">
									Primary Use
								</p>

								<div>
									{currentVehicle?.primaryUsage
										? currentVehicle?.primaryUsage
										: 'No data'}
								</div>
							</div>

							<div className="align-self-start">
								<p className="mb-1 text-neutral-7 w-100">
									Est. Annual Mileage
								</p>

								<div>
									{currentVehicle?.annualMileage
										? currentVehicle?.annualMileage
										: 'No data'}
								</div>
							</div>

							<div className="align-self-start">
								<p className="mb-1 text-neutral-7 w-100">
									Ownership Status
								</p>

								<div>
									{currentVehicle?.ownership
										? currentVehicle?.ownership
										: 'No data'}
								</div>
							</div>
						</div>

						<div>
							<div className="align-self-start mt-3">
								<p className="mb-1 text-neutral-7 w-100">
									Features
								</p>

								<div>
									{currentVehicle?.features
										? currentVehicle?.features
										: 'No data'}
								</div>
							</div>
						</div>
					</div>
				)
			)}
		</div>
	);
};

export default VehicleInfo;
