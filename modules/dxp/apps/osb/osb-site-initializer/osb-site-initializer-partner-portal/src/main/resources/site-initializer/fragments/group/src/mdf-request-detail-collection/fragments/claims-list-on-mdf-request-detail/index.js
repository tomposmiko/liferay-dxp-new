/* eslint-disable @liferay/empty-line-between-elements */
/* eslint-disable @liferay/portal/no-global-fetch */
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

import ClayAlert from '@clayui/alert';
import React, {useEffect, useState} from 'react';

const findRequestIdUrl = (paramsUrl) => {
	const splitParamsUrl = paramsUrl.split('?');

	return splitParamsUrl[0];
};

function getIntlNumberFormat() {
	return new Intl.NumberFormat(Liferay.ThemeDisplay.getBCP47LanguageId(), {
		currency: 'USD',
		style: 'currency',
	});
}

function getSiteVariables() {
	const currentPath = Liferay.currentURL.split('/');
	const mdfRequestId = findRequestIdUrl(currentPath.at(-1));
	const SITE_URL = Liferay.ThemeDisplay.getLayoutRelativeURL()
		.split('/')
		.slice(0, 3)
		.join('/');

	return [mdfRequestId, SITE_URL];
}

const ClaimStatus = {
	APPROVED: 'approved',
	CLAIM_PAID: 'claimPaid',
	DRAFT: 'draft',
	EXPIRED: 'expired',
	IN_FINANCE_REVIEW: 'inFinanceReview',
	MORE_INFO_REQUEST: 'moreInfoRequested',
	PENDING: 'pendingMarketingReview',
	REJECT: 'rejected',
};

const statusClassName = {
	[ClaimStatus.DRAFT]: 'label label-tonal-dark ml-2',
	[ClaimStatus.PENDING]: 'label label-tonal-warning ml-2',
	[ClaimStatus.APPROVED]: 'label label-tonal-success ml-2',
	[ClaimStatus.MORE_INFO_REQUEST]: 'label label-tonal-warning ml-2',
	[ClaimStatus.REJECT]: 'label label-tonal-danger ml-2',
	[ClaimStatus.EXPIRED]: 'label label-tonal-danger ml-2',
	[ClaimStatus.CLAIM_PAID]: 'label label-tonal-info ml-2',
	[ClaimStatus.IN_FINANCE_REVIEW]: 'label label-tonal-light ml-2',
};

const Panel = ({mdfClaims}) => {
	const [, SITE_URL] = getSiteVariables();

	return (
		<div>
			<div className="text-neutral-7 text-paragraph-xs">
				Type: {mdfClaims.partial ? 'Partial' : 'Full'}
			</div>

			<div className="mb-1 mt-1 text-neutral-9 text-paragraph-sm">
				Claim ({mdfClaims.id})
			</div>

			<div className="align-items-baseline d-flex justify-content-between">
				<div className="align-items-baseline d-flex">
					<p className="font-weight-bold text-neutral-9 text-paragraph-sm">
						Claimed USD
						{getIntlNumberFormat().format(mdfClaims.amountClaimed)}
					</p>

					<div
						className={
							statusClassName[mdfClaims.mdfClaimStatus.key]
						}
					>
						{mdfClaims.mdfClaimStatus.name}
					</div>
				</div>

				<button
					className="btn btn-secondary btn-sm"
					onClick={() =>
						Liferay.Util.navigate(`${SITE_URL}/l/${mdfClaims.id}`)
					}
				>
					View
				</button>
			</div>
		</div>
	);
};

export default function () {
	const [claims, setClaims] = useState();
	const [request, setRequest] = useState();
	const [loading, setLoading] = useState();

	const [mdfRequestId, SITE_URL] = getSiteVariables();

	useEffect(() => {
		const getClaimFromMDFRequest = async () => {
			const response = await fetch(
				`/o/c/mdfclaims?nestedFields=mdfClmToMDFClmActs,mdfClmActToMDFClmBgts&nestedFieldsDepth=2&filter=(r_mdfReqToMDFClms_c_mdfRequestId eq '${mdfRequestId}')`,
				{
					headers: {
						'accept': 'application/json',
						'x-csrf-token': Liferay.authToken,
					},
				}
			);

			const mdfrequest = await fetch(`/o/c/mdfrequests/${mdfRequestId}`, {
				headers: {
					'accept': 'application/json',
					'x-csrf-token': Liferay.authToken,
				},
			});

			if (response.ok && mdfrequest.ok) {
				setClaims(await response.json());
				setRequest(await mdfrequest.json());

				setLoading(false);

				return;
			}

			Liferay.Util.openToast({
				message: 'An unexpected error occured.',
				type: 'danger',
			});
		};

		if (!isNaN(mdfRequestId)) {
			getClaimFromMDFRequest();
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	if (loading) {
		return <>Loading...</>;
	}

	const filteredClaims = claims?.items.filter((claim) => {
		const ignoreStatus = [
			ClaimStatus.DRAFT,
			ClaimStatus.EXPIRED,
			ClaimStatus.REJECT,
		];

		return !ignoreStatus.includes(claim.mdfClaimStatus.key);
	});

	return (
		<>
			{request?.mdfRequestStatus.key === 'approved' ? (
				<div>
					{!!claims?.items.length && (
						<div>
							{claims?.items.map((mdfClaims, index) => (
								<div key={index}>
									<Panel mdfClaims={mdfClaims} />

									<hr></hr>
								</div>
							))}
						</div>
					)}

					<div className="align-items-start d-flex justify-content-between">
						<div>
							<h6 className="font-weight-normal text-neutral-9">
								Get Reimbursed
							</h6>

							{filteredClaims.length < 2 ? (
								<h6 className="font-weight-normal text-neutral-8">
									You can submit up to{' '}
									{2 - filteredClaims.length} claim(s).
								</h6>
							) : (
								<h6 className="font-weight-normal text-neutral-8">
									You already submitted 2 claims.
								</h6>
							)}
						</div>

						{filteredClaims.length < 2 && (
							<button
								className="btn btn-primary"
								onClick={() =>
									Liferay.Util.navigate(
										`${SITE_URL}/marketing/mdf-claim/new/#/mdfrequest/${mdfRequestId}`
									)
								}
								type="button"
							>
								New Claim
							</button>
						)}
					</div>
				</div>
			) : (
				<ClayAlert displayType="info" title="Info:">
					Waiting for Manager approval
				</ClayAlert>
			)}
		</>
	);
}
