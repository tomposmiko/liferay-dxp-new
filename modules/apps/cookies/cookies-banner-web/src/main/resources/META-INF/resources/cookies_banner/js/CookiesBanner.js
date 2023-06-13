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

import {checkConsent, getOpener, openModal} from 'frontend-js-web';

import {
	acceptAllCookies,
	declineAllCookies,
	getCookie,
	setCookie,
	setUserConfigCookie,
	userConfigCookieName,
} from '../../js/CookiesUtil';

let openCookieConsentModal = () => {
	console.warn(
		'OpenCookieConsentModal was called, but cookie feature is not enabled'
	);
};

export default function ({
	configurationNamespace,
	configurationURL,
	includeDeclineAllButton,
	namespace,
	optionalConsentCookieTypeNames,
	requiredConsentCookieTypeNames,
	title,
}) {
	const acceptAllButton = document.getElementById(
		`${namespace}acceptAllButton`
	);
	const configurationButton = document.getElementById(
		`${namespace}configurationButton`
	);
	const declineAllButton = document.getElementById(
		`${namespace}declineAllButton`
	);
	const cookieBanner = document.querySelector('.cookies-banner');
	const editMode = document.body.classList.contains('has-edit-mode-menu');

	if (!editMode) {
		setBannerVisibility(cookieBanner);

		const cookiePreferences = {};

		optionalConsentCookieTypeNames.forEach(
			(optionalConsentCookieTypeName) => {
				cookiePreferences[optionalConsentCookieTypeName] =
					getCookie(optionalConsentCookieTypeName) || 'false';
			}
		);

		Liferay.on('cookiePreferenceUpdate', (event) => {
			cookiePreferences[event.key] = event.value;
		});

		acceptAllButton.addEventListener('click', () => {
			cookieBanner.style.display = 'none';

			acceptAllCookies(
				optionalConsentCookieTypeNames,
				requiredConsentCookieTypeNames
			);

			setUserConfigCookie();
		});

		openCookieConsentModal = ({
			alertDisplayType,
			alertMessage,
			customTitle,
			onCloseFunction,
		}) => {
			let url = configurationURL;

			if (alertDisplayType) {
				url = `${url}&_${configurationNamespace}_alertDisplayType=${alertDisplayType}`;
			}

			if (alertMessage) {
				url = `${url}&_${configurationNamespace}_alertMessage=${alertMessage}`;
			}

			openModal({
				buttons: [
					{
						displayType: 'secondary',
						label: Liferay.Language.get('confirm'),
						onClick() {
							Object.entries(cookiePreferences).forEach(
								([key, value]) => {
									setCookie(key, value);
								}
							);

							requiredConsentCookieTypeNames.forEach(
								(requiredConsentCookieTypeName) => {
									setCookie(
										requiredConsentCookieTypeName,
										'true'
									);
								}
							);

							setUserConfigCookie();

							setBannerVisibility(cookieBanner);

							getOpener().Liferay.fire('closeModal');
						},
					},
					{
						displayType: 'secondary',
						label: Liferay.Language.get('accept-all'),
						onClick() {
							acceptAllCookies(
								optionalConsentCookieTypeNames,
								requiredConsentCookieTypeNames
							);

							setUserConfigCookie();

							setBannerVisibility(cookieBanner);

							getOpener().Liferay.fire('closeModal');
						},
					},
					{
						className: includeDeclineAllButton ? '' : 'd-none',
						displayType: 'secondary',
						label: Liferay.Language.get('decline-all'),
						onClick() {
							declineAllCookies(
								optionalConsentCookieTypeNames,
								requiredConsentCookieTypeNames
							);

							setUserConfigCookie();

							setBannerVisibility(cookieBanner);

							getOpener().Liferay.fire('closeModal');
						},
					},
				],
				displayType: 'primary',
				height: '70vh',
				id: 'cookiesBannerConfiguration',
				onClose: onCloseFunction || undefined,
				size: 'lg',
				title: customTitle || title,
				url,
			});
		};

		configurationButton.addEventListener('click', () => {
			openCookieConsentModal({});
		});

		declineAllButton.addEventListener('click', () => {
			cookieBanner.style.display = 'none';

			declineAllCookies(
				optionalConsentCookieTypeNames,
				requiredConsentCookieTypeNames
			);

			setUserConfigCookie();
		});
	}
}

function checkCookieConsentForTypes(cookieTypes, modalOptions) {
	return new Promise((resolve, reject) => {
		if (isCookieTypesAccepted(cookieTypes)) {
			resolve();
		}
		else {
			openCookieConsentModal({
				alertDisplayType: modalOptions?.alertDisplayType || 'info',
				alertMessage: modalOptions?.alertMessage || null,
				customTitle: modalOptions?.customTitle || null,
				onCloseFunction: () =>
					isCookieTypesAccepted(cookieTypes) ? resolve() : reject(),
			});
		}
	});
}

function isCookieTypesAccepted(cookieTypes) {
	if (!Array.isArray(cookieTypes)) {
		cookieTypes = [cookieTypes];
	}

	return cookieTypes.every((cookieType) => checkConsent(cookieType));
}

function setBannerVisibility(cookieBanner) {
	if (getCookie(userConfigCookieName)) {
		cookieBanner.style.display = 'none';
	}
	else {
		cookieBanner.style.display = 'block';
	}
}

export {checkCookieConsentForTypes, openCookieConsentModal};
