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

import React, {useState} from 'react';

import '../../css/definition-builder/main.scss';
import {DefinitionBuilderContextProvider} from './DefinitionBuilderContext';
import DiagramBuilder from './diagram-builder/DiagramBuilder';
import {defaultNodes} from './diagram-builder/components/nodes/utils';
import UpperToolbar from './shared/components/toolbar/UpperToolbar';
import SourceBuilder from './source-builder/SourceBuilder';

export default function (props) {
	const [elements, setElements] = useState(defaultNodes);
	const [selectedLanguageId, setSelectedLanguageId] = useState('');
	const [sourceView, setSourceView] = useState(false);
	const [definitionTitle, setDefinitionTitle] = useState(props.title);
	const defaultLanguageId = themeDisplay.getLanguageId();

	const contextProps = {
		defaultLanguageId,
		definitionTitle,
		elements,
		selectedLanguageId,
		setDefinitionTitle,
		setElements,
		setSelectedLanguageId,
		setSourceView,
		sourceView,
	};

	return (
		<DefinitionBuilderContextProvider {...contextProps}>
			<div className="definition-builder-app">
				<UpperToolbar {...props} />

				{sourceView ? (
					<SourceBuilder version={props.version} />
				) : (
					<DiagramBuilder version={props.version} />
				)}
			</div>
		</DefinitionBuilderContextProvider>
	);
}
