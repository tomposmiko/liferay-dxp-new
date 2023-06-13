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

import MDFRequestDTO from '../../../common/interfaces/dto/mdfRequestDTO';
import MDFClaim from '../../../common/interfaces/mdfClaim';
import {ResourceName} from '../../../common/services/liferay/object/enum/resourceName';
import createMDFClaim from '../../../common/services/liferay/object/mdf-claim/createMDFClaim';

export default async function createMDFClaimProxyAPI(
	mdfClaim: MDFClaim,
	mdfRequest: MDFRequestDTO
) {
	const dtoMDFClaimSFResponse = await createMDFClaim(
		ResourceName.MDF_CLAIM_SALESFORCE,
		mdfClaim,
		mdfRequest
	);

	if (dtoMDFClaimSFResponse.externalReferenceCode) {
		const dtoMDFClaimResponse = await createMDFClaim(
			ResourceName.MDF_CLAIM_DXP,
			mdfClaim,
			mdfRequest,
			dtoMDFClaimSFResponse.externalReferenceCode
		);

		return dtoMDFClaimResponse;
	}
}
