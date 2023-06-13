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

import {ERROR_MESSAGES} from '../errorMessages';
import isEmpty from '../functions/is_empty';

export default function validateRequired(
	configValue,
	type,
	required = true,
	nullable = false
) {
	if (!required || nullable) {
		return;
	}

	if (isEmpty(configValue, type)) {
		return ERROR_MESSAGES.REQUIRED;
	}
}
