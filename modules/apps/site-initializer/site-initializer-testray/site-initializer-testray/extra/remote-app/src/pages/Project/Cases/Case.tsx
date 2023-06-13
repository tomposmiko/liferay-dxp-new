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

import {useOutletContext, useParams} from 'react-router-dom';

import Container from '../../../components/Layout/Container';
import QATable from '../../../components/Table/QATable';
import i18n from '../../../i18n';
import {TestrayCase} from '../../../services/rest';
import dayjs from '../../../util/date';
import {searchUtil} from '../../../util/search';
import CaseResultHistory from './CaseResultHistory';
import useCaseActions from './useCaseActions';

const Case = () => {
	const {caseId, projectId} = useParams();
	const {testrayCase}: {testrayCase: TestrayCase} = useOutletContext();
	const {actions} = useCaseActions();

	return (
		<>
			<Container collapsable title={i18n.translate('details')}>
				<QATable
					items={[
						{
							title: i18n.translate('type'),
							value: testrayCase.caseType?.name,
						},
						{
							title: i18n.translate('priority'),
							value: testrayCase.priority,
						},
						{
							title: i18n.translate('main-component'),
							value: testrayCase.component?.name,
						},
						{
							title: i18n.translate('description'),
							value: testrayCase.description,
						},
						{
							title: i18n.translate('estimated-duration'),
							value: testrayCase.estimatedDuration,
						},
						{
							title: i18n.translate('steps'),
							value: testrayCase.steps,
						},
						{
							title: i18n.translate('date-created'),
							value: dayjs(testrayCase.dateCreated).format('lll'),
						},
						{
							title: i18n.translate('date-modified'),
							value: dayjs(testrayCase.dateModified).format(
								'lll'
							),
						},
						{
							title: i18n.translate('all-issues-found'),
							value: '-',
						},
					]}
				/>
			</Container>

			<Container className="mt-3">
				<CaseResultHistory
					listViewProps={{
						variables: {
							filter: searchUtil.eq('caseId', caseId as string),
						},
					}}
					tableProps={{
						actions,
						navigateTo: ({build, id}) =>
							`/project/${projectId}/routines/${build?.routine?.id}/build/${build?.id}/case-result/${id}`,
					}}
				/>
			</Container>
		</>
	);
};

export default Case;
