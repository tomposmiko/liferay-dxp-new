import * as API from 'shared/api';
import Card from 'shared/components/Card';
import getCN from 'classnames';
import InfoPopover from 'shared/components/InfoPopover';
import React from 'react';
import {DataSource} from 'shared/util/records';
import {isFinite} from 'lodash';
import {sub} from 'shared/util/lang';
import {useParams} from 'react-router-dom';
import {validContactsConfig} from 'shared/util/data-sources';
import {withRequest} from 'shared/hoc';

const EnrichedProfilesBody = ({count}) => (
	<Card.Body className='d-flex flex-column'>
		<div className='total d-flex flex-grow-1 text-center justify-content-center align-items-center'>
			{sub(Liferay.Language.get('x-profiles'), [
				isFinite(count) ? count.toLocaleString() : 0
			])}
		</div>

		<div className='description text-center'>
			{Liferay.Language.get(
				'enriched-with-attributes-or-behaviors-in-the-last-30-days'
			)}
		</div>
	</Card.Body>
);

const EnrichedProfilesBodyWithData = React.memo(
	withRequest(
		({channelId, groupId}) =>
			API.individuals.fetchEnrichedProfilesCount({channelId, groupId}),
		({total}) => ({count: total}),
		{page: false}
	)(EnrichedProfilesBody)
);

const renderInfoPopover = () => (
	<InfoPopover
		content={Liferay.Language.get(
			'total-count-of-individual-profiles-with-enrichment-from-data-source-updates-or-anonymous-profile-resolutions-in-the-last-30-days'
		)}
		title={Liferay.Language.get('enriched-profiles')}
	/>
);

interface IEnrichedProfilesCardProps extends React.HTMLAttributes<HTMLElement> {
	dataSources: DataSource[];
}

const EnrichedProfilesCard: React.FC<IEnrichedProfilesCardProps> = ({
	dataSources
}) => {
	const {channelId, groupId} = useParams();
	const contactsConfigured =
		!dataSources || dataSources.some(validContactsConfig);

	return (
		<Card
			className={getCN('enriched-profiles-card-root', {
				inverted: !contactsConfigured,
				['text-secondary']: contactsConfigured
			})}
		>
			{contactsConfigured ? (
				<>
					<Card.Header className='d-flex justify-content-between'>
						<Card.Title>
							{Liferay.Language.get('enriched-profiles')}
						</Card.Title>

						{renderInfoPopover()}
					</Card.Header>

					<EnrichedProfilesBodyWithData
						channelId={channelId}
						groupId={groupId}
					/>
				</>
			) : (
				<Card.Body>
					<div className='d-flex justify-content-between'>
						<Card.Title>
							{Liferay.Language.get('know-your-audience-better')}
						</Card.Title>

						{renderInfoPopover()}
					</div>

					<p>
						{Liferay.Language.get(
							'know-your-audience-better-by-connecting-people-data-to-enrich-profiles.-get-started-by-syncing-contacts-from-dxp-or-by-adding-a-data-source-with-people-data'
						)}
					</p>
				</Card.Body>
			)}
		</Card>
	);
};

export default EnrichedProfilesCard;
