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

import Container from '../../../components/Layout/Container';
import ListView from '../../../components/ListView/ListView';
import ProgressBar from '../../../components/ProgressBar';
import {getTestrayRoutines} from '../../../graphql/queries';
import {Liferay} from '../../../services/liferay/liferay';
import {progress} from '../../../util/mock';

const Routines = () => {
	return (
		<Container title="Routines">
			<ListView
				query={getTestrayRoutines}
				tableProps={{
					columns: [
						{
							key: 'name',
							render: (routine: string) => (
								<Link to="build">{routine}</Link>
							),
							value: 'ROUTINE',
						},
						{key: 'execution_date', value: 'EXECUTION DATE'},
						{key: 'failed', value: 'FAILED'},
						{key: 'blocked', value: 'BLOCKED'},
						{key: 'test_fix', value: 'TEST FIX'},
						{
							key: 'metrics',
							render: () => <ProgressBar items={progress[0]} />,
							value: 'METRICS',
						},
					],
				}}
				transformData={(data) => data?.c?.testrayRoutines}
				variables={{scopeKey: Liferay.ThemeDisplay.getSiteGroupId()}}
			/>
		</Container>
	);
};

export default Routines;
