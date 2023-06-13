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
import yupSchema from '../../schema/yup';
import {SearchBuilder} from '../../util/search';
import Rest from './Rest';
import {APIResponse, TestrayCaseType} from './types';

type CaseType = typeof yupSchema.caseType.__outputType;

class TestrayCaseTypeImpl extends Rest<CaseType, TestrayCaseType> {
	constructor() {
		super({
			adapter: ({name}) => ({
				name,
			}),
			uri: 'casetypes',
		});
	}

	protected async validate(caseType: CaseType, id?: number) {
		const searchBuilder = new SearchBuilder();

		if (id) {
			searchBuilder.ne('id', id).and();
		}

		const filter = searchBuilder.eq('name', caseType.name).build();

		const response = await this.fetcher<APIResponse<TestrayCaseType>>(
			`/casetypes?filter=${filter}`
		);

		if (response?.totalCount) {
			throw new Error(i18n.sub('the-x-name-already-exists', 'case-type'));
		}
	}

	protected async beforeCreate(caseType: CaseType): Promise<void> {
		await this.validate(caseType);
	}

	protected async beforeUpdate(
		id: number,
		caseType: CaseType
	): Promise<void> {
		await this.validate(caseType, id);
	}
}

export const testrayCaseTypeImpl = new TestrayCaseTypeImpl();
