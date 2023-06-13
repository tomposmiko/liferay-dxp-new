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

import {
	ContactInfoFormTypes,
	CoverageFormTypes,
	DriverInfoFormTypes,
	VehicleInfoFormTypes,
} from '../../routes/applications/context/NewApplicationAutoContextProvider';

export type NewApplicationFormStepsType = {
	contactInfo: {
		form: ContactInfoFormTypes;
		index: number;
		name: string;
	};
	coverage: {
		form: CoverageFormTypes;
		index: number;
		name: string;
	};
	driverInfo: {
		form: DriverInfoFormTypes[];
		index: number;
		name: string;
	};
	review: {
		index: number;
		name: string;
	};
	vehicleInfo: {
		form: VehicleInfoFormTypes[];
		index: number;
		name: string;
	};
};
