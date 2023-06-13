import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Constants from 'shared/util/constants';
import Icon from 'shared/components/Icon';
import React from 'react';
import RecommendationPageAssetsQuery from '../queries/RecommendationPageAssetsQuery';
import RuleItem from './RuleItem';
import Spinner from 'shared/components/Spinner';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {EXCLUDE, Filter} from '../utils/utils';
import {get} from 'lodash';
import {Modal} from 'shared/types';
import {sub} from 'shared/util/lang';
import {useQuery} from '@apollo/react-hooks';

const {
	pagination: {orderDescending}
} = Constants;

interface ITrainingItemProps {
	close: Modal.close;
	name: string;
	open: Modal.open;
	value: string;
}

const TrainingItem: React.FC<ITrainingItemProps> = ({
	close,
	name,
	open,
	value
}) => (
	<div className='training-item-root d-flex align-items-baseline'>
		<Button
			borderless
			display='secondary'
			onClick={() => {
				open(modalTypes.MATCHING_PAGES_MODAL, {
					itemFilters: [{name, value}],
					onClose: close
				});
			}}
		>
			<Icon symbol='view' />
		</Button>

		<RuleItem name={name} value={value} />
	</div>
);

interface ITrainingItemsCardProps {
	close: Modal.close;
	itemFilters: Filter[];
	open: Modal.open;
}

const TrainingItemsCard: React.FC<ITrainingItemsCardProps> = ({
	close,
	itemFilters,
	open
}) => {
	const {data, loading} = useQuery(RecommendationPageAssetsQuery, {
		variables: {
			propertyFilters: itemFilters.map(({name, value}) => ({
				filter: value,
				negate: name === EXCLUDE
			})),
			size: 0,
			sort: {
				column: 'title',
				type: orderDescending.toUpperCase()
			},
			start: 0
		}
	});

	const renderTotalTrainingUrls = () => {
		if (loading) {
			return <Spinner key='LOADING_SPINNER' size='sm' />;
		}

		return get(data, ['pageAssets', 'total'], 0).toLocaleString();
	};

	return (
		<Card className='training-items-card-root'>
			<Card.Header>
				<Card.Title>
					{Liferay.Language.get('training-items')}
				</Card.Title>
			</Card.Header>

			<Card.Body>
				{itemFilters.map(({name, value}) => (
					<TrainingItem
						close={close}
						key={`${name}-${value}`}
						name={name}
						open={open}
						value={value}
					/>
				))}

				<div className='total-training-urls d-flex align-items-center'>
					<Button
						borderless
						display='secondary'
						onClick={() => {
							open(modalTypes.MATCHING_PAGES_MODAL, {
								itemFilters,
								onClose: close
							});
						}}
					>
						<Icon symbol='view' />
					</Button>

					{sub(
						Liferay.Language.get('total-training-urls-x'),
						[renderTotalTrainingUrls()],
						false
					)}
				</div>
			</Card.Body>
		</Card>
	);
};

export default connect(null, {close, open})(TrainingItemsCard);
