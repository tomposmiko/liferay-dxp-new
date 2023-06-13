import React from 'react';
import SummarySection from 'experiments/components/summary-section';
import {formatDateToTimeZone} from 'shared/util/date';
import {
	getMetricName,
	modalComplete,
	modalPublishOtherVariant
} from 'experiments/util/experiments';
import {sub} from 'shared/util/lang';
import {toRounded} from 'shared/util/numbers';
import {toThousandsABTesting} from 'experiments/util/experiments';

export default ({
	bestVariant,
	dxpVariants,
	experimentId,
	goal,
	metrics: {completion, elapsedDays},
	pageURL,
	sessions,
	startedDate,
	timeZoneId
}) => {
	const control = dxpVariants.find(({control}) => control);
	const variants = dxpVariants.filter(({control}) => !control);

	return {
		alert: {
			description: Liferay.Language.get(
				'we-recommend-that-you-use-any-of-the-test-candidates-as-they-will-perform-similarly'
			),
			symbol: 'exclamation-circle',
			title: Liferay.Language.get('there-is-no-clear-winner')
		},
		header: {
			Description: () =>
				sub(Liferay.Language.get('started-x'), [
					formatDateToTimeZone(startedDate, 'll', timeZoneId)
				]),
			modals: [
				modalComplete(experimentId, control.dxpVariantId),
				modalPublishOtherVariant(
					variants,
					experimentId,
					pageURL,
					Liferay.Language.get('publish-variant')
				)
			],
			title: Liferay.Language.get('no-clear-winner')
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
							{bestVariant && bestVariant.improvement > 0 && (
								<SummarySection.Variant
									lift={`${toRounded(
										bestVariant.improvement,
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
