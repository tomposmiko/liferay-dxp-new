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

import i18n from '../../i18n';
import {CategoryOptions} from '../../pages/Project/Routines/Builds/BuildForm/Stack/StackList';
import yupSchema from '../../schema/yup';
import {TEST_STATUS} from '../../util/constants';
import {searchUtil} from '../../util/search';
import Rest from './Rest';
import {testrayCaseResultRest} from './TestrayCaseResult';
import {testrayFactorRest} from './TestrayFactor';
import {testrayRunImpl} from './TestrayRun';

import type {
	APIResponse,
	TestrayBuild,
	TestrayCaseResult,
	TestrayRoutine,
} from './types';

type Build = typeof yupSchema.build.__outputType & {projectId: number};

class TestrayBuildImpl extends Rest<Build, TestrayBuild> {
	constructor() {
		super({
			adapter: ({
				active,
				description,
				gitHash,
				name,
				productVersionId: r_productVersionToBuilds_c_productVersionId,
				projectId: r_projectToBuilds_c_projectId,
				promoted,
				routineId: r_routineToBuilds_c_routineId,
				template,
			}) => ({
				active,
				description,
				gitHash,
				name,
				promoted,
				r_productVersionToBuilds_c_productVersionId,
				r_projectToBuilds_c_projectId,
				r_routineToBuilds_c_routineId,
				template,
			}),
			nestedFields: 'productVersion&nestedFieldsDepth=2',
			transformData: (testrayBuild) => ({
				...testrayBuild,
				creator: testrayBuild?.creator || {},
				productVersion:
					testrayBuild?.r_productVersionToBuilds_c_productVersion,
			}),
			uri: 'builds',
		});
	}

	public async create(data: Build): Promise<TestrayBuild> {
		const build = await super.create(data);

		const caseIds = data.caseIds || [];
		const runs = data.factorStacks || [];

		let runIndex = 1;

		for (const run of runs) {
			const factorOptions = (Object.values(
				run
			) as CategoryOptions[]).filter(Boolean);

			const factorOptionsList = factorOptions
				.filter(({factorOption}) => Boolean(factorOption))
				.map(({factorOption}) => factorOption);

			const testrayRunName = factorOptionsList.join(' | ');

			if (!testrayRunName) {
				continue;
			}

			const testrayRun = await testrayRunImpl.create({
				buildId: build.id,
				description: undefined,
				environmentHash: testrayRunName,
				name: testrayRunName,
				number: runIndex,
			});

			for (const factorOption of factorOptions) {
				if (
					factorOption.factorCategoryId &&
					factorOption.factorOptionId
				) {
					await testrayFactorRest.create({
						factorCategoryId: factorOption.factorCategoryId?.toString(),
						factorOptionId: factorOption.factorOptionId?.toString(),
						name: '',
						routineId: undefined,
						runId: testrayRun.id,
					});
				}
			}

			await testrayCaseResultRest.createBatch(
				caseIds.map((caseId) => ({
					buildId: build.id,
					caseId,
					commentMBMessage: undefined,
					dueStatus: TEST_STATUS.Untested.toString(),
					issues: undefined,
					runId: testrayRun.id,
					startDate: undefined,
					userId: 0,
				}))
			);

			runIndex++;
		}

		return build;
	}

	public async hasBuildsInProjectId(projectId: number): Promise<boolean> {
		const routineResponse = await this.fetcher<APIResponse<TestrayRoutine>>(
			`/routines?filter=${searchUtil.eq(
				'projectId',
				projectId
			)}&fields=id`
		);

		const [routine] = routineResponse?.items || [];

		if (!routine) {
			return false;
		}

		const buildResponse = await this.fetcher<APIResponse<TestrayBuild>>(
			`/${this.uri}?filter=${searchUtil.eq(
				'routineId',
				routine.id
			)}&fields=id`
		);

		return !!buildResponse?.totalCount;
	}

	protected async beforeCreate(build: Build) {
		const response = await this.fetcher(
			`/builds?filter=${searchUtil.eq('name', build.name)}`
		);

		if (response?.items.length) {
			throw new Error(i18n.sub('the-x-name-already-exists', 'build'));
		}
	}

	public async getCurrentCaseIds(buildId: string | number) {
		const response = await this.fetcher(
			`/caseresults?filter=${searchUtil.eq(
				'buildId',
				buildId
			)}&pageSize=1000&fields=r_caseToCaseResult_c_caseId`
		);

		const caseIds: number[] =
			response?.items.map(
				(item: TestrayCaseResult) => item.r_caseToCaseResult_c_caseId
			) || [];

		return caseIds;
	}
}

export const testrayBuildImpl = new TestrayBuildImpl();
