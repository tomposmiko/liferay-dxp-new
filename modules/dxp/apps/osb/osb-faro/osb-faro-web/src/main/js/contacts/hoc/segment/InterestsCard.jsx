import Card from 'shared/components/Card';
import ClayIcon from '@clayui/icon';
import ClayLink from '@clayui/link';
import getInterestsQuery from 'contacts/queries/InterestsQuery';
import React from 'react';
import URLConstants from 'shared/util/url-constants';
import {compositionListColumns} from 'shared/util/table-columns';
import {CompositionTypes} from 'shared/util/constants';
import {
	getMapResultToProps,
	mapCardPropsToOptions
} from 'contacts/hoc/mappers/interests-query';
import {graphql} from '@apollo/react-hoc';
import {Routes, toRoute} from 'shared/util/router';
import {withTableData} from 'shared/hoc';

const withData = () =>
	graphql(getInterestsQuery(CompositionTypes.SegmentInterests), {
		options: mapCardPropsToOptions,
		props: getMapResultToProps(CompositionTypes.SegmentInterests)
	});

const TableWithData = withTableData(withData, {
	emptyDescription: (
		<>
			<span className='mr-1'>
				{Liferay.Language.get(
					'check-back-later-to-verify-if-data-has-been-received-from-your-data-sources'
				)}
			</span>

			<a
				href={URLConstants.SegmentsTopInterestsDocumentationLink}
				key='DOCUMENTATION'
				target='_blank'
			>
				{Liferay.Language.get('learn-more-about-interests')}
			</a>
		</>
	),
	emptyTitle: Liferay.Language.get('there-are-no-interests-found'),
	getColumns: ({channelId, groupId, id, maxCount, totalCount}) => [
		compositionListColumns.getName({
			label: Liferay.Language.get('topic'),
			maxWidth: 200,
			routeFn: ({data: {name}}) =>
				name &&
				toRoute(Routes.CONTACTS_SEGMENT_INTEREST_DETAILS, {
					channelId,
					groupId,
					id,
					interestId: name
				}),
			sortable: false
		}),
		compositionListColumns.getRelativeMetricBar({
			label: Liferay.Language.get('segment-members'),
			maxCount,
			totalCount
		}),
		compositionListColumns.getPercentOf({
			metricName: Liferay.Language.get('active-members'),
			totalCount
		})
	],
	rowIdentifier: 'name'
});

const InterestsCard = ({channelId, groupId, id}) => (
	<Card className='interests-card-root' minHeight={536}>
		<Card.Header>
			<Card.Title>{Liferay.Language.get('top-interests')}</Card.Title>
		</Card.Header>

		<TableWithData
			channelId={channelId}
			groupId={groupId}
			id={id}
			rowBordered={false}
		/>

		<Card.Footer>
			<ClayLink
				className='button-root'
				displayType='unstyled'
				href={toRoute(Routes.CONTACTS_SEGMENT_INTERESTS, {
					channelId,
					groupId,
					id
				})}
				small
			>
				{Liferay.Language.get('view-all-interests')}

				<ClayIcon className='icon-root ml-2' symbol='angle-right' />
			</ClayLink>
		</Card.Footer>
	</Card>
);

export default InterestsCard;
