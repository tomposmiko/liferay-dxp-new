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

import {yupResolver} from '@hookform/resolvers/yup';
import * as yup from 'yup';

import i18n from '../i18n';

const MINIMUM_CHARACTERS_PASSWORD = 8;

const userSchema = yup.object({
	alternateName: yup.string().required(),
	emailAddress: yup.string().email().required(),
	familyName: yup.string().required(),
	givenName: yup.string().required(),
});

const passwordStructure = {
	currentPassword: yup.string(),
	password: yup
		.string()
		.required(i18n.translate('this-field-is-required'))
		.min(
			MINIMUM_CHARACTERS_PASSWORD,
			i18n.sub(
				'minimum-x-characters',
				MINIMUM_CHARACTERS_PASSWORD.toString()
			)
		)
		.matches(
			/[a-zA-Z0-9]/,
			i18n.translate('password-may-contain-letters-and-number')
		),
	rePassword: yup
		.string()
		.required(i18n.translate('this-field-is-required'))
		.oneOf(
			[yup.ref('password'), null],
			i18n.translate('passwords-do-not-match')
		),
};

const passwordRequiredStructure = {
	...passwordStructure,
	currentPassword: yup
		.string()
		.required(i18n.translate('this-field-is-required')),
};

const yupSchema = {
	build: yup.object({
		caseIds: yup.array().of(yup.number()),
		categories: yup.mixed(),
		description: yup.string(),
		gitHash: yup.string(),
		id: yup.string(),
		name: yup.string().required(),
		productVersionId: yup.string().required(),
		promoted: yup.boolean(),
		routineId: yup.string().required(),
		template: yup.boolean(),
	}),
	case: yup.object({
		addAnother: yup.boolean(),
		caseTypeId: yup.string().required(),
		componentId: yup.string().required(),
		description: yup.string(),
		descriptionType: yup.string(),
		estimatedDuration: yup.number().min(0),
		name: yup.string().required(),
		priority: yup.string(),
		steps: yup.string(),
		stepsType: yup.string(),
	}),
	caseResult: yup.object({
		buildId: yup.number(),
		caseId: yup.number(),
		commentMBMessage: yup.string(),
		dueStatus: yup.string().required(),
		issues: yup.string(),
		runId: yup.number(),
		startDate: yup.string(),
		userId: yup.number(),
	}),
	caseType: yup.object({
		name: yup.string().required(),
	}),
	component: yup.object({
		id: yup.string(),
		name: yup.string().required(),
		projectId: yup.string(),
		teamId: yup.number(),
	}),
	enviroment: yup.object({
		factorCategoryIds: yup.mixed(),
		factorOptionIds: yup.mixed(),
		routineId: yup.number(),
	}),
	factor: yup.object({
		factorCategoryId: yup.string().required(),
		factorOptionId: yup.string().required(),
		id: yup.string(),
		name: yup.string().required(),
		routineId: yup.number(),
		runId: yup.number(),
	}),
	factorCategory: yup.object({
		id: yup.string(),
		name: yup.string().required(),
	}),
	factorOption: yup.object({
		factorCategoryId: yup.string().required(),
		id: yup.string(),
		name: yup.string().required(),
	}),
	option: yup.object({
		name: yup.string(),
	}),
	password: yup.object(passwordStructure),
	passwordRequired: yup.object(passwordRequiredStructure),
	productVersion: yup.object({
		id: yup.string(),
		name: yup.string().required(),
		projectId: yup.string(),
	}),
	project: yup.object({
		description: yup.string().notRequired(),
		id: yup.string().notRequired(),
		name: yup.string().required(),
	}),
	requirement: yup.object({
		componentId: yup.string().required(),
		description: yup.string().required(),
		descriptionType: yup.string().required(),
		id: yup.string(),
		key: yup.string(),
		linkTitle: yup.string().required(),
		linkURL: yup.string().required(),
		summary: yup.string().required(),
	}),
	routine: yup.object({
		autoanalyze: yup.boolean().required(),
		id: yup.number(),
		name: yup.string().required(),
	}),
	run: yup.object({
		buildId: yup.number(),
		description: yup.string(),
		environmentHash: yup.string(),
		name: yup.string().required(),
		number: yup.number().required(),
	}),
	suite: yup.object({
		autoanalyze: yup.boolean(),
		caseParameters: yup.string(),
		description: yup.string(),
		id: yup.string(),
		name: yup.string().required(),
		smartSuite: yup.string(),
	}),
	team: yup.object({
		id: yup.string(),
		name: yup.string().required(),
		projectId: yup.string(),
		teamId: yup.string(),
	}),
	user: userSchema,
	userWithPassword: userSchema.shape(passwordStructure),
	userWithPasswordRequired: userSchema.shape(passwordRequiredStructure),
};

export {yupResolver};

export default yupSchema;
