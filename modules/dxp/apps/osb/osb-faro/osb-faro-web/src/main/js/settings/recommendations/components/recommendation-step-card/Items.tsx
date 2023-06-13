import Button from 'shared/components/Button';
import Constants from 'shared/util/constants';
import Icon from 'shared/components/Icon';
import React, {useEffect} from 'react';
import RecommendationPageAssetsQuery from '../../queries/RecommendationPageAssetsQuery';
import RuleItem from '../RuleItem';
import Spinner from 'shared/components/Spinner';
import Table from 'shared/components/table';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {FieldArray} from 'formik';
import {Filter, getPropertiesFromItems} from '../../utils/utils';
import {get} from 'lodash';
import {Modal} from 'shared/types';
import {useLazyQuery, useQuery} from '@apollo/react-hooks';

const {
	pagination: {orderDescending}
} = Constants;

const CountCell: React.FC<{
	className: string;
	data: Filter;
	close: Modal.close;
	open: Modal.open;
}> = ({className, close, data: {name, value}, open}) => {
	const {data, loading} = useQuery(RecommendationPageAssetsQuery, {
		variables: {
			propertyFilters: [
				{
					filter: value,
					negate: false
				}
			],
			size: 0,
			sort: {
				column: 'title',
				type: orderDescending.toUpperCase()
			},
			start: 0
		}
	});

	if (loading) {
		return (
			<td className={className}>
				<Spinner size='sm' />
			</td>
		);
	}

	return (
		<td className={className}>
			<Button
				className='matching-pages-modal-button'
				display='unstyled'
				onClick={() => {
					open(modalTypes.MATCHING_PAGES_MODAL, {
						itemFilters: [{name, value}],
						onClose: close
					});
				}}
			>
				{get(data, ['pageAssets', 'total'], 0).toLocaleString()}
			</Button>
		</td>
	);
};

const RuleCell: React.FC<{
	className: string;
	data: Filter;
}> = ({className, data: {name, value}}) => (
	<td className={className}>
		<RuleItem name={name} value={value} />
	</td>
);

interface IItemsProps {
	close: Modal.close;
	groupId: string;
	itemFilters: Filter[];
	open: Modal.open;
}

const Items: React.FC<IItemsProps> = ({close, groupId, itemFilters, open}) => {
	const [getPageAssetsTotal, {data, loading: pagesTotalLoading}] =
		useLazyQuery(RecommendationPageAssetsQuery);

	useEffect(() => {
		getPageAssetsTotal({
			variables: {
				propertyFilters: getPropertiesFromItems(itemFilters),
				size: 0,
				sort: {
					column: 'title',
					type: orderDescending.toUpperCase()
				},
				start: 0
			}
		});
	}, [itemFilters]);

	const renderTotalPages = (): React.ReactNode => {
		if (pagesTotalLoading) {
			return (
				<div>
					<Spinner size='sm' />
				</div>
			);
		}

		return (
			<div>
				<Button
					className='matching-pages-modal-button'
					display='unstyled'
					onClick={() => {
						open(modalTypes.MATCHING_PAGES_MODAL, {
							itemFilters,
							onClose: close,
							useNegateValue: true
						});
					}}
				>
					{get(data, ['pageAssets', 'total'], 0).toLocaleString()}
				</Button>
			</div>
		);
	};

	return (
		<div className='items-root'>
			<div className='title'>{Liferay.Language.get('add-items')}</div>

			<div className='secondary-info'>
				{Liferay.Language.get(
					'create-rules-to-match-your-urls-and-page-metadata.-if-you-dont-define-rules,-the-recommendation-model-will-use-all-urls-from-this-workspace-to-train-the-recommendations-model'
				)}
			</div>

			<FieldArray name='itemFilters'>
				{arrayHelpers => (
					<>
						<Button
							className='new-rule-button'
							onClick={() => {
								// Maybe add a toast alert to inform the user that this already exists therefore it was not added

								open(modalTypes.NEW_RULE_MODAL, {
									groupId,
									onClose: close,
									onSubmit: filter => {
										if (
											!itemFilters.find(
												item => item.id === filter.id
											)
										) {
											arrayHelpers.push(filter);
										}

										close();
									}
								});
							}}
						>
							{Liferay.Language.get('new-rule')}
						</Button>

						{!!itemFilters.length && (
							<>
								<Table
									columns={[
										{
											accessor: 'name',
											cellRenderer: RuleCell,
											className:
												'rule-cell table-cell-expand',
											label: Liferay.Language.get('rule'),
											sortable: false
										},
										{
											accessor: 'value',
											cellRenderer: CountCell,
											cellRendererProps: {close, open},
											className:
												'count-cell table-column-text-end',
											label: Liferay.Language.get(
												'matching-items'
											),
											sortable: false
										}
									]}
									items={itemFilters}
									renderInlineRowActions={({data, items}) => (
										<span>
											<Button
												borderless
												display='secondary'
												onClick={() => {
													arrayHelpers.remove(
														items.findIndex(
															(itemData: {
																[
																	key: string
																]: any;
															}) =>
																itemData.id ===
																data.id
														)
													);
												}}
												outline
											>
												<Icon symbol='times' />
											</Button>
										</span>
									)}
									rowIdentifier={['name', 'value']}
								/>

								<div className='total-included-pages d-flex justify-content-between'>
									<div>
										{Liferay.Language.get(
											'total-included-pages'
										)}
									</div>

									{renderTotalPages()}
								</div>
							</>
						)}
					</>
				)}
			</FieldArray>
		</div>
	);
};

export default connect(null, {close, open})(Items);
