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

import {Link} from 'react-router-dom';

import Container from '../../components/Layout/Container';
import ListView from '../../components/ListView/ListView';
import {getTestrayProjects} from '../../graphql/queries';
import {Liferay} from '../../services/liferay/liferay';

const Home = () => {
	return (
		<Container title="Projects">
			<ListView
				query={getTestrayProjects}
				tableProps={{
					columns: [
						{
							key: 'name',
							render: (value: string, item: any) => (
								<Link
									to={`/project/${item.testrayProjectId}/routines`}
								>
									{value}
								</Link>
							),
							value: 'Project',
						},
						{key: 'description', value: 'Description'},
					],
				}}
				transformData={(data) => data?.c?.testrayProjects}
				variables={{scopeKey: Liferay.ThemeDisplay.getSiteGroupId()}}
			/>
		</Container>
	);
};

export default Home;
