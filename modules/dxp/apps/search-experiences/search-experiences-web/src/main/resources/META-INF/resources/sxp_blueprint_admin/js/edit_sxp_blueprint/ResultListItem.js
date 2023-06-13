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

import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import ClayLink from '@clayui/link';
import ClayList from '@clayui/list';
import getCN from 'classnames';
import moment from 'moment';
import React, {useContext, useState} from 'react';

import {PreviewModalWithCopyDownload} from '../shared/PreviewModal';
import ThemeContext from '../shared/ThemeContext';

const getResultDefaultKeys = (locale) => [
	'entryClassName',
	`content_${locale}`,
	'createDate',
	'modified',
	'userName',
];

const SXP_BLUEPRINT_FIELD_PREFIX = '_';
const RESULTS_SHOW_KEYS = ['assetEntryId'];
const DATE_KEYS = ['createDate', 'modified'];
const TRUNCATE_LENGTH = 700;

const sxpBlueprintFieldPrefixRegex = new RegExp(
	`^(${SXP_BLUEPRINT_FIELD_PREFIX})`
);
const bracketsQuotesRegex = new RegExp(/[[\]"]/, 'g');

function localizeDate(property, value) {
	if (DATE_KEYS.includes(property)) {
		return moment(moment(value, 'YYYYMMDDHHmmss'))
			.locale(Liferay.ThemeDisplay.getBCP47LanguageId() || 'en-US')
			.format('lll');
	}

	return value;
}

function removeSXPBlueprintFieldPrefix(value) {
	return value.replace(sxpBlueprintFieldPrefixRegex, '');
}

function removeBrackets(value) {
	return value.replace(bracketsQuotesRegex, '');
}

function truncateString(value) {
	return value.length > TRUNCATE_LENGTH
		? value.substring(0, TRUNCATE_LENGTH).concat('...')
		: value;
}

function ResultListItem({item}) {
	const {locale} = useContext(ThemeContext);

	const [collapse, setCollapse] = useState(true);

	const _renderListRow = (property, value) =>
		value && (
			<ClayLayout.Row justify="start" key={property}>
				<ClayLayout.Col className="semibold" size={4}>
					{removeSXPBlueprintFieldPrefix(property)}
				</ClayLayout.Col>

				<ClayLayout.Col
					className={getCN({'text-truncate': collapse})}
					size={8}
				>
					{truncateString(
						typeof value === 'object'
							? localizeDate(
									property,
									removeBrackets(JSON.stringify(value))
							  )
							: localizeDate(property, value)
					)}
				</ClayLayout.Col>
			</ClayLayout.Row>
		);

	return (
		<ClayList.Item
			className="result-list-item"
			flex
			key={item[`title_${locale}`]}
		>
			<ClayList.ItemField>
				<PreviewModalWithCopyDownload
					fileName="score_explanation.json"
					size="lg"
					text={JSON.stringify(
						item?._explanation?.details || [],
						null,
						2
					)}
					title={Liferay.Language.get('score-explanation')}
				>
					<ClayButton className="score" displayType="unstyled" small>
						{item._score.toFixed(2)}
					</ClayButton>
				</PreviewModalWithCopyDownload>
			</ClayList.ItemField>

			<ClayList.ItemField expand>
				<ClayList.ItemTitle>
					{item.viewURL ? (
						<ClayLink href={item.viewURL} target="_blank">
							{item[`title_${locale}`] || item.fullName}

							<ClayIcon
								className="shortcut-icon"
								symbol="shortcut"
							/>
						</ClayLink>
					) : (
						item[`title_${locale}`] || item.fullName
					)}
				</ClayList.ItemTitle>

				{getResultDefaultKeys(locale).map((property) =>
					_renderListRow(property, item[property])
				)}

				{!collapse && (
					<>
						{RESULTS_SHOW_KEYS.map((property) =>
							_renderListRow(property, item[property])
						)}

						<div className="list-group-header">
							<span className="list-group-header-title">
								{Liferay.Language.get('document-fields')}
							</span>
						</div>

						{Object.keys(item.fields)
							.sort()
							.map((property) =>
								_renderListRow(property, item.fields[property])
							)}
					</>
				)}
			</ClayList.ItemField>

			<ClayList.ItemField>
				<ClayButton
					aria-label={
						collapse
							? Liferay.Language.get('expand')
							: Liferay.Language.get('collapse')
					}
					displayType="unstyled"
					onClick={() => setCollapse(!collapse)}
				>
					<ClayIcon
						symbol={collapse ? 'angle-right' : 'angle-down'}
					/>
				</ClayButton>
			</ClayList.ItemField>
		</ClayList.Item>
	);
}

export default ResultListItem;
