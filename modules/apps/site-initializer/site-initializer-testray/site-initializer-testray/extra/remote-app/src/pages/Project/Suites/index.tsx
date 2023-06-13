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

import {useNavigate, useParams} from 'react-router-dom';

import Container from '../../../components/Layout/Container';
import ListView from '../../../components/ListView';
import SearchBuilder from '../../../core/SearchBuilder';
import i18n from '../../../i18n';
import useSuiteActions from './useSuiteActions';

const Suites = () => {
	const navigate = useNavigate();
	const {projectId} = useParams();

	const {actions} = useSuiteActions();

	return (
		<Container>
			<ListView
				managementToolbarProps={{
					addButton: () => navigate('create'),
					filterSchema: 'suites',
					title: i18n.translate('suites'),
				}}
				resource="/suites"
				tableProps={{
					actions,
					columns: [
						{
							clickable: true,
							key: 'name',
							sorteable: true,
							value: i18n.translate('suite-name'),
						},
						{
							key: 'description',
							value: i18n.translate('description'),
						},
						{
							key: 'caseParameters',
							render: (caseParameters) =>
								i18n.translate(
									caseParameters ? 'smart' : 'static'
								),
							value: i18n.translate('type'),
						},
					],
					navigateTo: (suite) =>
						`/project/${projectId}/suites/${suite.id}`,
				}}
				variables={{
					filter: SearchBuilder.eq('projectId', projectId as string),
				}}
			/>
		</Container>
	);
};

export default Suites;
