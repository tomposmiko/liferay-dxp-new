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

import {StyleErrorsContextProvider} from '@liferay/layout-content-page-editor-web';
import React from 'react';

import LayoutPreview from './LayoutPreview';
import Sidebar from './Sidebar';
import {StyleBookContextProvider} from './StyleBookContext';
import Toolbar from './Toolbar';
import {config, initializeConfig} from './config';
import {DRAFT_STATUS} from './constants/draftStatusConstants';
import {LAYOUT_TYPES} from './constants/layoutTypes';
import {useCloseProductMenu} from './useCloseProductMenu';

const StyleBookEditor = () => {
	useCloseProductMenu();

	return (
		<div className="cadmin style-book-editor">
			<StyleErrorsContextProvider>
				<Toolbar />

				<div className="d-flex">
					<LayoutPreview />

					<Sidebar />
				</div>
			</StyleErrorsContextProvider>
		</div>
	);
};

export default function ({
	fragmentCollectionPreviewURL = '',
	frontendTokenDefinition = [],
	frontendTokensValues = {},
	isPrivateLayoutsEnabled,
	namespace,
	previewOptions,
	publishURL,
	redirectURL,
	saveDraftURL,
	styleBookEntryId,
	themeName,
} = {}) {
	initializeConfig({
		fragmentCollectionPreviewURL,
		frontendTokenDefinition,
		isPrivateLayoutsEnabled,
		namespace,
		previewOptions,
		publishURL,
		redirectURL,
		saveDraftURL,
		styleBookEntryId,
		themeName,
	});

	return (
		<StyleBookContextProvider
			initialState={{
				draftStatus: DRAFT_STATUS.notSaved,
				frontendTokensValues,
				previewLayout: getMostRecentLayout(config.previewOptions),
				previewLayoutType: config.previewOptions.find((type) =>
					type.data.recentLayouts.find(
						(layout) =>
							layout ===
							getMostRecentLayout(config.previewOptions)
					)
				)?.type,
				redoHistory: [],
				undoHistory: [],
			}}
		>
			<StyleBookEditor />
		</StyleBookContextProvider>
	);
}

function getMostRecentLayout(previewOptions) {
	const types = [
		LAYOUT_TYPES.page,
		LAYOUT_TYPES.master,
		LAYOUT_TYPES.pageTemplate,
		LAYOUT_TYPES.displayPageTemplate,
		LAYOUT_TYPES.fragmentCollection,
	];

	for (let i = 0; i < types.length; i++) {
		const layouts = previewOptions.find(
			(option) => option.type === types[i]
		).data.recentLayouts;

		if (layouts.length) {
			return layouts[0];
		}
	}

	return null;
}
