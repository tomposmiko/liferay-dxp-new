import {CHART_COLOR_NAMES} from 'shared/components/Chart';
import {getPercentage} from 'shared/util/util';
import {getVariables, safeResultToProps} from 'shared/util/mappers';
import {sub} from 'shared/util/lang';
import {toRounded, toThousands} from 'shared/util/numbers';

const {martellD4, martellL4, mormont, stark} = CHART_COLOR_NAMES;

/**
 * Get formatted Segments Data
 * @param {object} param
 * @param {string} color
 */
const getSegmentsData = ({segments, total, totalOthers}, color) => {
	const MAX_BARS = 6;
	const MAX_VALUE_EMPTY_STATE = 30;
	const TOOLTIP_HEADER = [
		{
			label: Liferay.Language.get('segment'),
			weight: 'semibold'
		},
		{
			label: ''
		},
		{
			align: 'right',
			label: '%',
			weight: 'semibold'
		}
	];

	/**
	 * Get formatted tooltip column
	 * @param {object} item
	 */
	const getTooltipColumns = ({value, valueKey}) => [
		{
			label:
				valueKey === 'others'
					? Liferay.Language.get('other-segments')
					: valueKey,
			truncated: true,
			weight: 'semibold',
			width: 160
		},
		{
			align: 'right',
			label: `${toThousands(value)}`
		},
		{
			align: 'right',
			label: `${toRounded(getPercentage(value, total))}%`,
			weight: 'semibold',
			width: 50
		}
	];

	/**
	 * Convert value to percentage based on total
	 * @param {number} value
	 */
	const getValue = value => parseInt(toRounded(getPercentage(value, total)));

	/**
	 * Sum all the keys value of the array
	 * @param {array} arr
	 */
	const sumArrValues = arr =>
		arr.map(({value}) => value).reduce((a, b) => a + b);

	let items = segments.slice(0, MAX_BARS).map(({value, valueKey}) => ({
		columns: [
			{
				icon: 'ac-segment',
				label: valueKey
			}
		],
		progress: [
			{
				color,
				value: getValue(value)
			}
		],
		tooltip: {
			header: TOOLTIP_HEADER,
			rows: [
				{
					columns: getTooltipColumns({value, valueKey})
				}
			]
		}
	}));

	// Max value
	let maxValue = Math.max(...segments.map(({value}) => value));

	if (segments.length > MAX_BARS) {
		const otherArrItems = segments.slice(MAX_BARS - 1);

		const value = sumArrValues(otherArrItems);

		items = [
			...items.slice(0, MAX_BARS - 1),
			{
				columns: [
					{
						icon: 'ac-segment',
						label: sub(Liferay.Language.get('x-more-segments'), [
							totalOthers - (MAX_BARS - 1)
						])
					}
				],
				progress: [
					{
						color,
						value: getValue(value)
					}
				],
				tooltip: {
					header: TOOLTIP_HEADER,
					rows: otherArrItems.map(item => ({
						columns: getTooltipColumns(item)
					}))
				}
			}
		];

		// Update max value
		maxValue = value > maxValue ? value : maxValue;
	}

	return {
		disableScroll: true,
		formatSpacement: false,
		grid: {
			maxValue: segments.length
				? getValue(maxValue)
				: MAX_VALUE_EMPTY_STATE,
			minValue: 0,
			show: true,
			type: 'percentage'
		},
		items
	};
};

/**
 * MAPPER
 * @description Get Segments Mapper
 * @param {function} getMetric
 */
const getAudienceReportMapper = (getMetric, pathUrl) => {
	const mapResultToProps = safeResultToProps(result => {
		const {
			anonymousUsersCount,
			knownUsersCount,
			nonsegmentedKnownUsersCount,
			segment: {metrics, total: totalOthers},
			segmentedAnonymousUsersCount,
			segmentedKnownUsersCount
		} = getMetric(result);

		const knownIndividualsData = [
			{
				color: martellL4,
				count: segmentedKnownUsersCount,
				label: Liferay.Language.get('segmented')
			},
			{
				color: martellD4,
				count: nonsegmentedKnownUsersCount,
				label: Liferay.Language.get('unsegmented')
			}
		];

		const uniqueVisitorsData = [
			{
				color: mormont,
				count: anonymousUsersCount,
				label: Liferay.Language.get('anonymous-individuals')
			},
			{
				color: stark,
				count: knownUsersCount,
				label: Liferay.Language.get('known-individuals')
			}
		];

		const segments = getSegmentsData(
			{
				segments: [
					...metrics
						.sort((a, b) => b.value - a.value)
						.filter(({valueKey}) => valueKey !== 'others'),
					...metrics.filter(({valueKey}) => valueKey === 'others')
				],
				total: segmentedAnonymousUsersCount + segmentedKnownUsersCount,
				totalOthers
			},
			martellL4
		);

		const knownIndividualsTotal = knownIndividualsData.reduce(
			(total, {count}) => total + count,
			0
		);

		const knownIndividuals = {
			data: knownIndividualsData,
			empty: {
				message: sub(
					Liferay.Language.get(
						'x-segmented-visitors-interacted-with-this-content'
					),
					[0]
				),
				show: knownIndividualsTotal === 0
			},
			total: knownIndividualsTotal
		};

		const uniqueVisitorsTotal = uniqueVisitorsData.reduce(
			(total, {count}) => total + count,
			0
		);

		const uniqueVisitors = {
			data: uniqueVisitorsData,
			empty: {
				message: sub(
					Liferay.Language.get(
						'x-visitors-interacted-with-this-content'
					),
					[0]
				),
				show: uniqueVisitorsTotal === 0
			},
			total: uniqueVisitorsTotal
		};

		return {
			knownIndividuals,
			pathUrl,
			segments,
			uniqueVisitors
		};
	});

	/**
	 * Map Props to Options
	 * @param {object} param0 props
	 * @param {object} param1 context
	 */
	const mapPropsToOptions = ({
		filters,
		interval,
		rangeSelectors,
		router: {params}
	}) => getVariables({filters, interval, params, rangeSelectors});

	return {
		options: mapPropsToOptions,
		props: mapResultToProps
	};
};

export {getAudienceReportMapper};
export default getAudienceReportMapper;
