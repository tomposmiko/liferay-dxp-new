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

import {useCallback, useState} from 'react';

const isCustomFilter = (filter) => filter.key === 'custom';

const useCustomFormState = () => {
	const [formVisible, setFormVisible] = useState(false);

	const onClickFilter = useCallback(
		(handleClick) => (currentItem) => {
			if (isCustomFilter(currentItem)) {
				setFormVisible(true);
			}
			else {
				handleClick(currentItem);
			}

			document.dispatchEvent(new Event('mousedown'));

			return true;
		},
		[]
	);

	return {
		formVisible,
		onClickFilter,
		setFormVisible,
	};
};

export {useCustomFormState};
