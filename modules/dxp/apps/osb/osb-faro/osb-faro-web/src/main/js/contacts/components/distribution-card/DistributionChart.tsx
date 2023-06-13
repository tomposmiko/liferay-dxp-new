import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import Promise from 'metal-promise';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import {ANIMATION_DURATION, AXIS, getTextWidth} from 'shared/util/recharts';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {
	Bar,
	CartesianGrid,
	Cell,
	ComposedChart,
	ResponsiveContainer,
	XAxis,
	YAxis
} from 'recharts';
import {connect, ConnectedProps} from 'react-redux';
import {DistributionTab} from 'shared/util/records';
import {FieldTypes} from 'shared/util/constants';
import {getBarColor} from 'shared/util/charts';
import {hasChanges} from 'shared/util/react';
import {List, Map} from 'immutable';
import {noop, pickBy} from 'lodash';
import {RootState} from 'shared/store';

const BAR_WIDTH = 30;
const CHART_DATA_ID = 'count';
const CHART_PADDING = 60;
const MAX_BARS = 10;

const connector = connect(
	(state: RootState, {distributionKey}: {distributionKey: string}) => {
		const distributionIMap = state.getIn(
			['distributions', distributionKey],
			Map()
		);

		return {
			error: distributionIMap.get('error', false),
			individualFieldDistributionIList: distributionIMap
				.getIn(['data', 'items'], List())
				.slice(0, 11),
			loading: distributionIMap.get('loading', true)
		};
	}
);

type PropsFromRedux = ConnectedProps<typeof connector>;

interface IDistributionChartProps extends PropsFromRedux {
	channelId: string;
	distributionKey: string;
	error: boolean;
	fetchDistribution: (params: object) => typeof Promise;
	groupId: string;
	id: string;
	individualFieldDistributionIList: List<Map<string, any>>;
	loading: boolean;
	noResultsRenderer?: () => React.ReactElement;
	selectedTab: DistributionTab;
	viewAllLink: string;
}

@hasRequest
class DistributionChart extends React.Component<
	IDistributionChartProps,
	{hoverIndex: number}
> {
	state = {
		hoverIndex: -1
	};

	componentDidMount() {
		this.handleFetchChartData();
	}

	componentDidUpdate(prevProps) {
		if (hasChanges(prevProps, this.props, 'selectedTab')) {
			this.handleFetchChartData();
		}
	}

	formatChartData(fieldDistributions, histogram) {
		return fieldDistributions.map(({count, values}) => ({
			count,
			graphValue: histogram ? (values[0] + values[1]) / 2 : values[0],
			values
		}));
	}

	getYAxisTicks(fieldDistributions, histogram) {
		return [
			...fieldDistributions.map(item => item.values[0]),
			histogram &&
				fieldDistributions.length &&
				fieldDistributions[fieldDistributions.length - 1].values[1]
		].filter(Boolean);
	}

	@autoCancel
	@autobind
	handleFetchChartData() {
		const {
			channelId,
			fetchDistribution,
			groupId,
			id,
			selectedTab: {context, numberOfBins, propertyId}
		} = this.props;

		return fetchDistribution(
			pickBy({
				channelId,
				context,
				count: MAX_BARS,
				fieldMappingFieldName: propertyId,
				groupId,
				id,
				individualSegmentId: id,
				numberOfBins
			})
		).catch(noop);
	}

	render() {
		const {
			props: {
				error,
				individualFieldDistributionIList,
				loading,
				noResultsRenderer,
				selectedTab: {propertyType},
				viewAllLink
			},
			state: {hoverIndex}
		} = this;

		const individualFieldDistribution =
			individualFieldDistributionIList.toJS();

		const histogram = propertyType === FieldTypes.Number;

		const yAxisTicks = this.getYAxisTicks(
			individualFieldDistribution,
			histogram
		);

		const formattedChartData = this.formatChartData(
			individualFieldDistribution,
			histogram
		);

		const fieldDistributionsCount = individualFieldDistribution.length;

		const yAxisDomain = histogram
			? [yAxisTicks[0], yAxisTicks[yAxisTicks.length - 1]]
			: [0, 'auto'];

		const yAxisWidth = yAxisTicks.reduce((acc, item) => {
			const textWidth = getTextWidth(item.toString());

			return textWidth > acc ? textWidth : acc;
		}, 60);

		return (
			<>
				<Card.Body alignCenter>
					{error && (
						<ErrorDisplay
							onReload={this.handleFetchChartData}
							spacer
						/>
					)}

					{loading && <Spinner spacer />}

					{!error && !loading && (
						<>
							{!fieldDistributionsCount &&
								noResultsRenderer &&
								noResultsRenderer()}

							{!!fieldDistributionsCount && (
								<ResponsiveContainer
									height={
										BAR_WIDTH * MAX_BARS + CHART_PADDING
									}
								>
									<ComposedChart
										data={formattedChartData}
										layout='vertical'
									>
										<CartesianGrid
											horizontal={false}
											stroke={AXIS.gridStroke}
											strokeDasharray='3 3'
										/>

										<YAxis
											axisLine={{
												stroke: AXIS.borderStroke
											}}
											dataKey='graphValue'
											domain={yAxisDomain}
											tickFormatter={val => val}
											ticks={yAxisTicks}
											type={
												histogram
													? 'number'
													: 'category'
											}
											width={yAxisWidth}
										/>

										<YAxis
											axisLine={{
												stroke: AXIS.borderStroke
											}}
											dataKey='graphValue'
											domain={yAxisDomain}
											orientation='right'
											tick={false}
											tickLine={false}
											yAxisId='right'
										/>

										<XAxis
											axisLine={{
												stroke: AXIS.borderStroke
											}}
											dataKey={CHART_DATA_ID}
											interval='preserveStart'
											orientation='top'
											scale='linear'
											tickLine={false}
											type='number'
										/>

										<XAxis
											axisLine={{
												stroke: AXIS.borderStroke
											}}
											dataKey={CHART_DATA_ID}
											tick={false}
											tickLine={false}
											xAxisId='bottom'
										/>

										<Bar
											animationDuration={
												ANIMATION_DURATION.bar
											}
											dataKey={CHART_DATA_ID}
											onMouseEnter={(e, index) =>
												this.setState({
													hoverIndex: index
												})
											}
											onMouseLeave={() =>
												this.setState({
													hoverIndex: -1
												})
											}
										>
											{formattedChartData.map(
												(item, index) => (
													<Cell
														fill={getBarColor(
															index,
															hoverIndex
														)}
														key={`cell-${index}`}
													/>
												)
											)}
										</Bar>
									</ComposedChart>
								</ResponsiveContainer>
							)}
						</>
					)}
				</Card.Body>

				<Card.Footer>
					<Button
						display='link'
						href={viewAllLink}
						icon='angle-right'
						iconAlignment='right'
						size='sm'
					>
						{Liferay.Language.get('explore-breakdown')}
					</Button>
				</Card.Footer>
			</>
		);
	}
}

export default connector(DistributionChart);
