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

import {Liferay} from '../..';
import LiferayFile from '../../../../interfaces/liferayFile';
import MDFClaimBudget from '../../../../interfaces/mdfClaimBudget';
import getDTOFromMDFClaimBudget from '../../../../utils/dto/mdf-claim-budget/getDTOFromMDFClaimBudget';
import {LiferayAPIs} from '../../common/enums/apis';
import liferayFetcher from '../../common/utils/fetcher';

export default async function updateMDFClaimActivityBudget(
	mdfClaimBudget: MDFClaimBudget,
	mdfClaimActivityId?: number,
	mdfClaimBudgetId?: number,
	companyId?: number,
	budgetInvoiceId?: LiferayFile & number
) {
	return await liferayFetcher.put(
		`/o/${LiferayAPIs.OBJECT}/mdfclaimbudgets/${mdfClaimBudgetId}`,
		Liferay.authToken,
		getDTOFromMDFClaimBudget(
			mdfClaimBudget,
			mdfClaimActivityId,
			companyId,
			budgetInvoiceId
		)
	);
}
