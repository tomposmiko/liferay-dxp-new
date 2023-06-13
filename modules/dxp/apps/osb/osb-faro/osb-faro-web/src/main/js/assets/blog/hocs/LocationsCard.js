import getLocationsMapper, {
	getLocationsMapperCountries
} from 'cerebro-shared/hocs/mappers/locations';
import URLConstants from 'shared/util/url-constants';
import {GEOLOCATION_FRAGMENT} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';
import {graphql} from '@apollo/react-hoc';
import {withLocationsCard} from 'cerebro-shared/hocs/LocationsCard';

const GEOLOCATION_QUERY = gql`
	query BlogsMetrics(
		$assetId: String!
		$channelId: String
		$devices: String
		$location: String
		$rangeEnd: String
		$rangeKey: Int
		$rangeStart: String
		$title: String
		$touchpoint: String
	) {
		blog(
			assetId: $assetId
			canonicalUrl: $touchpoint
			channelId: $channelId
			country: $location
			deviceType: $devices
			rangeEnd: $rangeEnd
			rangeKey: $rangeKey
			rangeStart: $rangeStart
			title: $title
		) {
			assetId
			assetTitle
			viewsMetric {
				...geolocationFragment

				previousValue
				value
			}
		}
	}

	${GEOLOCATION_FRAGMENT}
`;
/**
 * HOC
 * @description Blogs Locations
 */
const withBlogsLocations = () =>
	graphql(
		GEOLOCATION_QUERY,
		getLocationsMapper(result => result.blog.viewsMetric)
	);

/**
 * HOC
 * @description Blogs Countries
 */
const withBlogsLocationsCountries = () =>
	graphql(
		GEOLOCATION_QUERY,
		getLocationsMapperCountries(result => result.blog.viewsMetric)
	);

export default withLocationsCard(
	withBlogsLocations,
	withBlogsLocationsCountries,
	{
		documentationTitle: Liferay.Language.get(
			'learn-more-about-views-by-location'
		),
		documentationUrl: URLConstants.SitesDashboardBlogsViewsByLocation,
		title: Liferay.Language.get('there-are-no-views-on-the-selected-period')
	}
);
