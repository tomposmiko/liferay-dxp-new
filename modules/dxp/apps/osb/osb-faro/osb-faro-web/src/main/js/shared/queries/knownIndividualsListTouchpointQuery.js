import {gql} from 'apollo-boost';
import {INDIVIDUALS_FRAGMENT} from 'shared/queries/fragments';

/**
 * Known Individuals List Asset Query
 * @description Create a GraphQL query
 * @param {string} queryName
 * @param {string} metricName
 * @returns GraphQL query
 */
export default (queryName, metricName) => gql`
		query KnownIndividualsListTouchpointQuery(
			$channelId: String
			$devices: String
			$keywords: String
			$location: String
			$rangeEnd: String
			$rangeKey: Int
			$rangeStart: String
			$size: Int!
			$start: Int!
			$title: String
			$touchpoint: String
		) {
			${queryName}(
				channelId: $channelId
				canonicalUrl: $touchpoint
				deviceType: $devices
				country: $location
				rangeEnd: $rangeEnd
				rangeKey: $rangeKey
				rangeStart: $rangeStart
				title: $title
			) {
				${metricName} {
					...individualsFragment
				}
			}
		}

		${INDIVIDUALS_FRAGMENT}
	`;
