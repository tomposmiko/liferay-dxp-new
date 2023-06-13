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

window.themeDisplay = {
	getBCP47LanguageId: () => 'en-US',
	getDefaultLanguageId: () => 'en_US',
	getLanguageId: () => 'en_US',
	getPathContext: () => '/',
	getPortalURL: () => 'http://localhost:8080/',
};

window.Liferay = {
	...(window.Liferay || {}),
	Language: {
		...(window.Liferay.Language || {}),
		available: {
			ar_SA: 'Arabic (Saudi Arabia)',
			ca_ES: 'Catalan (Spain)',
			de_DE: 'German (Germany)',
			en_US: 'English (United States)',
			es_ES: 'Spanish (Spain)',
			fi_FI: 'Finnish (Finland)',
			fr_FR: 'French (France)',
			hu_HU: 'Hungarian (Hungary)',
			ja_JP: 'Japanese (Japan)',
			nl_NL: 'Dutch (Netherlands)',
			pt_BR: 'Portuguese (Brazil)',
			sv_SE: 'Swedish (Sweden)',
			zh_CN: 'Chinese (China)',
		},
		get: (key) => key,
	},
	ThemeDisplay: window.themeDisplay,
};
