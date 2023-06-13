import React from 'react';
import SummarySection from 'experiments/components/summary-section';
import {formatDateToTimeZone} from 'shared/util/date';
import {
	getMetricName,
	mergedVariants,
	modalComplete,
	modalPublishOtherVariant,
	modalPublishVariant
} from 'experiments/util/experiments';
import {sub} from 'shared/util/lang';
import {toRounded} from 'shared/util/numbers';
import {toThousandsABTesting} from 'experiments/util/experiments';

type Alert = {
	description?: string;
	symbol?: string;
	title?: Array<string> | string;
};

export default ({
	dxpVariants,
	experimentId,
	goal,
	metrics: {completion, elapsedDays, variantMetrics},
	pageURL,
	sessions,
	startedDate,
	timeZoneId,
	winnerDXPVariantId
}) => {
	const winnerVariant = mergedVariants(dxpVariants, variantMetrics).find(
		({dxpVariantId}) => dxpVariantId === winnerDXPVariantId
	);

	let alert: Alert = {
		symbol: 'check-circle'
	};

	let modals = [];

	if (winnerVariant) {
		if (winnerVariant.control) {
			const variants = dxpVariants.filter(({control}) => !control);

			modals = [
				modalComplete(experimentId, winnerVariant.dxpVariantId),
				modalPublishOtherVariant(variants, experimentId, pageURL)
			];

			alert = {
				...alert,
				description: Liferay.Language.get(
					'we-recommend-that-you-keep-control-published-and-complete-this-test'
				),
				title: sub(
					Liferay.Language.get(
						'control-has-outperformed-all-variants-by-at-least-x'
					),
					[`${toRounded(winnerVariant.improvement, 2)}%`]
				)
			};
		} else {
			const control = dxpVariants.find(({control}) => control);
			const variants = dxpVariants.filter(
				({control, dxpVariantId}) =>
					!control && dxpVariantId !== winnerDXPVariantId
			);

			modals = [
				modalPublishVariant(
					winnerVariant.dxpVariantId,
					winnerVariant.dxpVariantName,
					experimentId,
					pageURL
				),
				variants.length &&
					modalPublishOtherVariant(variants, experimentId, pageURL),
				modalComplete(experimentId, control.dxpVariantId)
			].filter(Boolean);

			alert = {
				...alert,
				description: Liferay.Language.get(
					'we-recommend-that-you-publish-the-winning-variant'
				),
				title: sub(
					Liferay.Language.get(
						'x-has-outperformed-control-by-at-least-x'
					),
					[
						winnerVariant.dxpVariantName,
						`${toRounded(winnerVariant.improvement, 2)}%`
					]
				)
			};
		}
	}

	return {
		alert,
		header: {
			Description: () =>
				sub(Liferay.Language.get('started-x'), [
					formatDateToTimeZone(startedDate, 'll', timeZoneId)
				]),

			modals,
			title: Liferay.Language.get('winner-declared')
		},
		sections: [
			{
				Body: () => (
					<SummarySection
						title={Liferay.Language.get('test-completion')}
					>
						<SummarySection.Heading
							value={`${toRounded(completion)}%`}
						/>
						<SummarySection.ProgressBar
							value={parseInt(toRounded(completion))}
						/>
					</SummarySection>
				)
			},
			{
				Body: () => (
					<SummarySection
						title={Liferay.Language.get('days-running')}
					>
						<SummarySection.Heading value={String(elapsedDays)} />
					</SummarySection>
				)
			},
			{
				Body: () => (
					<SummarySection
						title={Liferay.Language.get('total-test-sessions')}
					>
						<SummarySection.Heading
							value={toThousandsABTesting(sessions)}
						/>
					</SummarySection>
				)
			},
			{
				Body: () =>
					goal && (
						<SummarySection
							title={Liferay.Language.get('test-metric')}
						>
							<SummarySection.MetricType
								value={getMetricName(goal.metric)}
							/>
							{winnerVariant && winnerVariant.improvement > 0 && (
								<SummarySection.Variant
									lift={`${toRounded(
										winnerVariant.improvement,
										2
									)}%`}
									status='up'
								/>
							)}
						</SummarySection>
					)
			}
		]
	};
};
