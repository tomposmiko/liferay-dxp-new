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

import MDFRequestDTO from '../../../interfaces/dto/mdfRequestDTO';
import MDFRequest from '../../../interfaces/mdfRequest';
import {Liferay} from '../../../services/liferay';

export function getDTOFromMDFRequest(
	mdfRequest: MDFRequest,
	externalReferenceCodeSF?: string
): MDFRequestDTO {
	return {
		accountExternalReferenceCodeSF:
			mdfRequest.accountExternalReferenceCodeSF,
		additionalOption: mdfRequest.additionalOption,
		companyName: mdfRequest.company?.name,
		country: mdfRequest.country,
		currency: mdfRequest.currency,
		emailAddress: Liferay.ThemeDisplay.getUserEmailAddress(),
		externalReferenceCodeSF,
		liferayBusinessSalesGoals: mdfRequest.liferayBusinessSalesGoals?.join(
			'; '
		),
		liferaysUserIdSF: Number(Liferay.ThemeDisplay.getUserId()),
		maxDateActivity: mdfRequest.maxDateActivity,
		mdfRequestStatus: mdfRequest.mdfRequestStatus,
		minDateActivity: mdfRequest.minDateActivity,
		overallCampaignDescription: mdfRequest.overallCampaignDescription,
		overallCampaignName: mdfRequest.overallCampaignName,
		r_accToMDFReqs_accountEntryId: mdfRequest.company?.id,
		r_usrToMDFReqs_userId: Number(Liferay.ThemeDisplay.getUserId()),
		targetAudienceRoles: mdfRequest.targetAudienceRoles?.join('; '),
		targetMarkets: mdfRequest.targetMarkets?.join('; '),
		totalCostOfExpense: mdfRequest.totalCostOfExpense,
		totalMDFRequestAmount: mdfRequest.totalMDFRequestAmount,
	};
}
