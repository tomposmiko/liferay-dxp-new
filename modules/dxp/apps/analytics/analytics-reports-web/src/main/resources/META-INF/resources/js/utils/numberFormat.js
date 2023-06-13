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

/**
 * Example:
 * en-US: `123456` => `123,456`
 * es-ES: `123456` => `123.456`
 */

const DEFAULT_COMPACT_THRESHOLD = 10000;

export function numberFormat(languageTag, number, options = {}) {
	const {compactThreshold = DEFAULT_COMPACT_THRESHOLD, useCompact} = options;

	const formatOptions = {};

	if (useCompact && number >= compactThreshold) {
		formatOptions.notation = 'compact';
	}

	return Intl.NumberFormat(languageTag, formatOptions).format(number);
}
