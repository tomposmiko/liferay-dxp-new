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

import ClayIcon from '@clayui/icon';
import {Link} from 'react-router-dom';

import Container from '../../../components/Layout/Container';
import ListView from '../../../components/ListView/ListView';
import {getTestrayRequirements} from '../../../graphql/queries';
import {Liferay} from '../../../services/liferay/liferay';

const Requirements = () => (
	<Container title="Requirements">
		<ListView
			query={getTestrayRequirements}
			tableProps={{
				columns: [
					{
						key: 'key',
						render: (key: string, {testrayRequirementId}: any) => (
							<Link to={`${testrayRequirementId}`}>{key}</Link>
						),
						value: 'Key',
					},
					{
						key: 'linkTitle',
						render: (
							linkTitle: string,
							{linkURL}: {linkURL: string}
						) => (
							<a
								href={linkURL}
								rel="noopener noreferrer"
								target="_blank"
							>
								{linkTitle}

								<ClayIcon className="ml-2" symbol="shortcut" />
							</a>
						),
						value: 'Link',
					},
					{key: 'team', value: 'Team'},
					{key: 'component', value: 'Component'},
					{key: 'components', value: 'Jira Components'},
					{key: 'summary', value: 'Summary'},
					{key: 'description', value: 'Description'},
				],
			}}
			transformData={(data) => data?.c?.testrayRequirements}
			variables={{scopeKey: Liferay.ThemeDisplay.getScopeGroupId()}}
		/>
	</Container>
);

export default Requirements;
