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
import MDFRequest from '../../../../interfaces/mdfRequest';
import {getDTOFromMDFRequest} from '../../../../utils/dto/mdf-request/getDTOFromMDFRequest';
import {LiferayAPIs} from '../../common/enums/apis';
import liferayFetcher from '../../common/utils/fetcher';
import {ResourceName} from '../enum/resourceName';

export default async function updateMDFRequest(
	apiOption: ResourceName,
	mdfRequest: MDFRequest,
	mdfRequestId: number,
	externalReferenceCode?: string,
	externalReferenceCodeSF?: string
) {
	return await liferayFetcher.put(
		`/o/${LiferayAPIs.OBJECT}/${apiOption}/${mdfRequestId}`,
		Liferay.authToken,
		getDTOFromMDFRequest(
			mdfRequest,
			externalReferenceCode,
			externalReferenceCodeSF
		)
	);
}
