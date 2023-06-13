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

import React from 'react';

import i18n from '../../i18n';
import {Action} from '../../types';

type UseTestflowAssign = {
	setUserIds: React.Dispatch<React.SetStateAction<number[]>>;
};

const useTestFlowAssign = ({setUserIds}: UseTestflowAssign) => {
	const actions: Action[] = [
		{
			action: ({id}: {id: number}) => {
				setUserIds((userIds: number[]) =>
					userIds.filter((userId) => userId !== id)
				);
			},
			name: i18n.translate('remove'),
		},
	];

	return {
		actions,
	};
};

export default useTestFlowAssign;
