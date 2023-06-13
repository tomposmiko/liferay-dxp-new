import {gql} from 'apollo-boost';

export const AUDIENCE_REPORT_FRAGMENT = gql`
	fragment audienceReportFragment on Metric {
		anonymousUsersCount
		knownUsersCount
		nonsegmentedKnownUsersCount
		segment {
			metrics {
				value
				valueKey
			}
			total
		}
		segmentedAnonymousUsersCount
		segmentedKnownUsersCount
	}
`;

export const BROWSER_FRAGMENT = gql`
	fragment browserFragment on Metric {
		browser {
			metrics {
				value
				valueKey
			}
			value
			valueKey
		}
	}
`;

export const COMPOSITION_FRAGMENT = gql`
	fragment compositionFragment on CompositionBag {
		compositions {
			name
			count
		}
		maxCount
		total
		totalCount
	}
`;

export const DEVICE_FRAGMENT = gql`
	fragment deviceFragment on Metric {
		device {
			metrics {
				value
				valueKey
			}
			value
			valueKey
		}
	}
`;

export const GEOLOCATION_FRAGMENT = gql`
	fragment geolocationFragment on Metric {
		geolocation {
			metrics {
				value
				valueKey
			}
			value
			valueKey
		}
	}
`;

export const INDIVIDUALS_FRAGMENT = gql`
	fragment individualsFragment on Metric {
		individuals(keywords: $keywords, size: $size, start: $start) {
			individuals {
				id
				name
				email
			}
			total
		}
	}
`;

export const METRIC_TABS_FRAGMENT = gql`
	fragment TabsFragment on Metric {
		previousValue
		trend {
			percentage
			trendClassification
		}
		value
	}
`;

export const METRIC_HISTOGRAM_FRAGMENT = gql`
	fragment HistogramFragment on Metric {
		...TabsFragment
		histogram {
			asymmetricComparison
			metrics {
				key
				previousValue
				previousValueKey
				value
				valueKey
				trend {
					percentage
					trendClassification
				}
			}
			total
		}
	}

	${METRIC_TABS_FRAGMENT}
`;

export const METRIC_FRAGMENT = gql`
	fragment metricFragment on Metric {
		histogram {
			asymmetricComparison
			metrics {
				key
				previousValue
				previousValueKey
				value
				valueKey
				trend {
					percentage
					trendClassification
				}
			}
			total
		}
		previousValue
		trend {
			percentage
			trendClassification
		}
		value
	}
`;

export const TREND_FRAGMENT = gql`
	fragment trendFragment on Metric {
		histogram {
			asymmetricComparison
			metrics {
				key
				valueKey
				value
			}
			total
		}
		value
		trend {
			percentage
		}
	}
`;

export const TREND_FRAGMENT_EVENT_METRIC = gql`
	fragment trendFragmentEventMetric on Metric {
		histogram {
			metrics {
				key
				value
				valueKey
			}
			total
		}
		value
	}
`;
