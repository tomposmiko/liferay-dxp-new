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

export type ConstantListType = {[keys: string]: string};

type ConstantsType = {
	APPLICATION_STATUS: {[keys: string]: {INDEX: number; NAME: string}};
	DEVICES: ConstantListType;
	MONTHS_ABREVIATIONS: string[];
	US_STATES: ConstantListType[];
};

export const CONSTANTS: ConstantsType = {
	APPLICATION_STATUS: {
		approved: {INDEX: 8, NAME: 'approved'},
		bound: {INDEX: 6, NAME: 'bound'},
		inInvestigation: {INDEX: 7, NAME: 'InInvestigation'},
		incomplete: {INDEX: 1, NAME: 'incomplete'},
		open: {INDEX: 0, NAME: 'open'},
		quoted: {INDEX: 2, NAME: 'quoted'},
		rejected: {INDEX: 5, NAME: 'rejected'},
		reviewed: {INDEX: 4, NAME: 'reviewed'},
		underwriting: {INDEX: 3, NAME: 'underwriting'},
	},

	DEVICES: {
		DESKTOP: 'DESKTOP',
		PHONE: 'PHONE',
		TABLET: 'TABLET',
	},

	MONTHS_ABREVIATIONS: [
		'Jan',
		'Feb',
		'Mar',
		'Apr',
		'May',
		'Jun',
		'Jul',
		'Aug',
		'Sep',
		'Oct',
		'Nov',
		'Dec',
	],

	US_STATES: [
		{
			label: '',
			value: '',
		},
		{
			label: 'CHOOSE AN OPTION',
			value: 'CHOOSE AN OPTION',
		},
		{
			label: 'CA',
			value: 'CA',
		},
		{
			label: 'NV',
			value: 'NV',
		},
		{
			label: 'NY',
			value: 'NY',
		},
	],
};

export const productAutoERC = 'RAYAP-001';
