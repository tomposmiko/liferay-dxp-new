import getDevicesMapper from 'cerebro-shared/hocs/mappers/devices';
import URLConstants from 'shared/util/url-constants';
import {BROWSER_FRAGMENT, DEVICE_FRAGMENT} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';
import {graphql} from '@apollo/react-hoc';
import {withDevicesCard} from 'shared/hoc/DevicesCard';

const BROWSER_DEVICE = gql`
	query FormsMetrics(
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
		form(
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
			submissionsMetric {
				...browserFragment
				...deviceFragment

				previousValue
				value
			}
		}
	}

	${BROWSER_FRAGMENT}
	${DEVICE_FRAGMENT}
`;

/**
 * HOC
 * @description Forms Devices
 */
const withFormsDevices = () =>
	graphql(
		BROWSER_DEVICE,
		getDevicesMapper(result => result.form.submissionsMetric)
	);

export default withDevicesCard(withFormsDevices, {
	documentationTitle: Liferay.Language.get(
		'learn-more-about-submissions-by-technology'
	),
	documentationUrl: URLConstants.SitesDashboardFormsSubmissionsByTechnology,
	title: Liferay.Language.get(
		'there-are-no-submissions-on-the-selected-period'
	)
});
