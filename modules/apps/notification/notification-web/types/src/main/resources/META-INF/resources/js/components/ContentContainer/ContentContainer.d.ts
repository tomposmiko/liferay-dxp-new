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

import {FormError} from '@liferay/object-js-components-web';
import React from 'react';
interface ContentContainerProps {
	baseResourceURL: string;
	editorConfig: object;
	errors: FormError<NotificationTemplate>;
	objectDefinitions: ObjectDefinition[];
	selectedLocale: Locale;
	setSelectedLocale: React.Dispatch<
		React.SetStateAction<Liferay.Language.Locale>
	>;
	setValues: (values: Partial<NotificationTemplate>) => void;
	values: NotificationTemplate;
}
export default function ContentContainer({
	baseResourceURL,
	editorConfig,
	errors,
	objectDefinitions,
	selectedLocale,
	setSelectedLocale,
	setValues,
	values,
}: ContentContainerProps): JSX.Element;
export {};
