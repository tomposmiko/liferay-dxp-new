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

/// <reference types="react" />

import '../css/FDSEntries.scss';
declare type HeadlessResource = {
	bundleLabel: string;
	entityClassName: string;
	name: string;
	version: string;
};
interface IFDSEntriesProps {
	apiURL: string;
	fdsViewsURL: string;
	headlessResources: Array<HeadlessResource>;
	namespace: string;
}
declare const FDSEntries: ({
	apiURL,
	fdsViewsURL,
	headlessResources,
	namespace,
}: IFDSEntriesProps) => JSX.Element;
export default FDSEntries;
