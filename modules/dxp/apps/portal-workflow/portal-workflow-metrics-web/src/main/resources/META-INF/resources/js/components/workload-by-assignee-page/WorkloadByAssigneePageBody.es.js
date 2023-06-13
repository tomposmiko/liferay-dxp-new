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

import React from 'react';

import EmptyState from '../../shared/components/list/EmptyState.es';
import ReloadButton from '../../shared/components/list/ReloadButton.es';
import LoadingState from '../../shared/components/loading/LoadingState.es';
import PaginationBar from '../../shared/components/pagination-bar/PaginationBar.es';
import PromisesResolver from '../../shared/components/promises-resolver/PromisesResolver.es';
import {Table} from './WorkloadByAssigneePageTable.es';

const Body = ({data, filtered, processId, taskKeys}) => {
	return (
		<div className="container-fluid-1280 mt-4">
			<PromisesResolver.Pending>
				<Body.Loading />
			</PromisesResolver.Pending>

			<PromisesResolver.Resolved>
				{data.totalCount ? (
					<>
						<Body.Table
							items={data.items}
							processId={processId}
							taskKeys={taskKeys}
						/>

						<PaginationBar
							page={data.page}
							pageBuffer={4}
							pageCount={data.items.length}
							pageSize={data.pageSize}
							totalCount={data.totalCount}
						/>
					</>
				) : (
					<Body.Empty filtered={filtered} />
				)}
			</PromisesResolver.Resolved>

			<PromisesResolver.Rejected>
				<Body.Error />
			</PromisesResolver.Rejected>
		</div>
	);
};

const EmptyView = ({filtered}) => {
	const emptyMessage = filtered
		? Liferay.Language.get('no-results-were-found')
		: Liferay.Language.get('there-is-no-data-at-the-moment');

	const emptyType = filtered ? 'not-found' : 'empty';

	return <EmptyState message={emptyMessage} type={emptyType} />;
};

const ErrorView = () => {
	return (
		<EmptyState
			actionButton={<ReloadButton />}
			hideAnimation={true}
			message={Liferay.Language.get(
				'there-was-a-problem-retrieving-data-please-try-reloading-the-page'
			)}
		/>
	);
};

const LoadingView = () => {
	return <LoadingState className="border-0 pb-6 pt-6 sheet" />;
};

Body.Empty = EmptyView;
Body.Error = ErrorView;
Body.Loading = LoadingView;
Body.Table = Table;

export {Body};
