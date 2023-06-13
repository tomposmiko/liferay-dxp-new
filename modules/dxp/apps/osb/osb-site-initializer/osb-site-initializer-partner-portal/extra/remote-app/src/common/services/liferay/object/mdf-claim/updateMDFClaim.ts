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
import MDFRequestDTO from '../../../../interfaces/dto/mdfRequestDTO';
import LiferayFile from '../../../../interfaces/liferayFile';
import MDFClaim from '../../../../interfaces/mdfClaim';
import {getDTOFromMDFClaim} from '../../../../utils/dto/mdf-claim/getDTOFromMDFClaim';
import {LiferayAPIs} from '../../common/enums/apis';
import liferayFetcher from '../../common/utils/fetcher';
import {ResourceName} from '../enum/resourceName';

export default async function updateMDFClaim(
	apiOption: ResourceName,
	mdfClaim: MDFClaim,
	mdfRequest: MDFRequestDTO,
	mdfClaimId: number,
	reimbursementInvoiceDocumentId?: LiferayFile & number,
	externalReferenceCode?: string,
	externalReferenceCodeSF?: string
) {
	return await liferayFetcher.put(
		`/o/${LiferayAPIs.OBJECT}/${apiOption}/${mdfClaimId}`,
		Liferay.authToken,
		getDTOFromMDFClaim(
			mdfClaim,
			mdfRequest,
			externalReferenceCode,
			externalReferenceCodeSF,
			reimbursementInvoiceDocumentId
		)
	);
}
