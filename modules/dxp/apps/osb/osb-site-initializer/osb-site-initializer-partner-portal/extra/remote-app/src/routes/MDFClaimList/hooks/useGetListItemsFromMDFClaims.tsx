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

import {useMemo} from 'react';

import {MDFClaimColumnKey} from '../../../common/enums/mdfClaimColumnKey';
import useGetMDFClaim from '../../../common/services/liferay/object/mdf-claim/useGetMDFClaim';
import {customFormatDateOptions} from '../../../common/utils/constants/customFormatDateOptions';
import getDateCustomFormat from '../../../common/utils/getDateCustomFormat';
import getMDFClaimAmountClaimedInfo from '../utils/getMDFBudgetInfos';

export default function useGetListItemsFromMDFClaims(
	page: number,
	pageSize: number,
	filtersTerm: string
) {
	const swrResponse = useGetMDFClaim(page, pageSize, filtersTerm);

	const listItems = useMemo(
		() =>
			swrResponse.data?.items.map((item) => ({
				[MDFClaimColumnKey.REQUEST_ID]: String(item.id),
				[MDFClaimColumnKey.PARTNER]:
					item.r_accountToMdfClaims_accountEntry?.name,
				[MDFClaimColumnKey.STATUS]: item.claimStatus,
				[MDFClaimColumnKey.TYPE]: item.partial ? 'Partial' : 'Full',
				...getMDFClaimAmountClaimedInfo(item.amountClaimed),
				[MDFClaimColumnKey.DATE_SUBMITTED]: getDateCustomFormat(
					item.dateCreated as Date,
					customFormatDateOptions.SHORT_MONTH
				),
			})),
		[swrResponse.data?.items]
	);

	return {
		...swrResponse,
		data: {
			...swrResponse.data,
			items: listItems,
		},
	};
}
