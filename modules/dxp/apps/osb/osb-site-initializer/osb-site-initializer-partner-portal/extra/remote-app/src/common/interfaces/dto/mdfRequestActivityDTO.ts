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

import MDFRequestActivity from '../mdfRequestActivity';
import MDFRequestActivityDescription from '../mdfRequestActivityDescription';
import MDFClaimActivityDTO from './mdfClaimActivityDTO';
import MDFRequestBudgetDTO from './mdfRequestBudgetDTO';

type MDFRequestActivityDTO = Omit<
	MDFRequestActivity,
	'activityDescription' | 'budgets'
> &
	Omit<MDFRequestActivityDescription, 'leadFollowUpStrategies'> & {
		actToBgts?: MDFRequestBudgetDTO[];
		actToMDFClmActs?: MDFClaimActivityDTO[];
		externalReferenceCodeSF?: string;
		leadFollowUpStrategies?: string;
		mdfRequestExternalReferenceCodeSF?: string;
		r_accToActs_accountEntryId?: number;
		r_mdfReqToActs_c_mdfRequestId?: number;
	};

export default MDFRequestActivityDTO;
