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

import ClayButton from '@clayui/button';
import ClayLayout from '@clayui/layout';
import React, {useContext} from 'react';

import AppContext from '../../AppContext.es';
import EmptyState from '../empty-state/EmptyState.es';
import RuleItem from './RuleItem.es';

export default ({keywords, toggleRulesEditorVisibility}) => {
	const [
		{
			dataLayout: {dataRules},
		},
	] = useContext(AppContext);

	const filteredDataRules = dataRules
		.map((rule, index) => ({...rule, ruleEditedIndex: index}))
		.filter(({name}) =>
			new RegExp(keywords, 'ig').test(
				name[Liferay.ThemeDisplay.getLanguageId()]
			)
		);

	return (
		<>
			{!filteredDataRules.length ? (
				<EmptyState
					emptyState={{
						button: () => (
							<ClayButton
								displayType="secondary"
								onClick={() => toggleRulesEditorVisibility()}
							>
								{Liferay.Language.get('create-new-rule')}
							</ClayButton>
						),
						description: Liferay.Language.get(
							'there-are-no-rules-description'
						),
						title: Liferay.Language.get('there-are-no-rules'),
					}}
					keywords={keywords}
					small
				/>
			) : (
				<ClayLayout.ContentCol className="rule-list">
					<hr />
					{filteredDataRules.map((rule, index) => (
						<RuleItem
							key={index}
							rule={rule}
							toggleRulesEditorVisibility={
								toggleRulesEditorVisibility
							}
						/>
					))}
				</ClayLayout.ContentCol>
			)}
		</>
	);
};
