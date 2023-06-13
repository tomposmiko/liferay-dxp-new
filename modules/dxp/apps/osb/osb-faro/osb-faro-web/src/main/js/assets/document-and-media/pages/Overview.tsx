import AudienceReportCard from 'assets/document-and-media/hocs/AudienceReportCard';
import DevicesCard from 'assets/document-and-media/hocs/DevicesCard';
import DocumentsAndMediaMetricCard from 'assets/document-and-media/components/DocumentsAndMediaMetricCard';
import LocationsCard from 'assets/document-and-media/hocs/LocationsCard';
import React from 'react';
import TouchpointsListCard from 'assets/hocs/TouchpointsListCard';

const Overview = () => (
	<>
		<div className='row'>
			<div className='col-sm-12'>
				<DocumentsAndMediaMetricCard
					label={Liferay.Language.get('visitors-behavior')}
				/>
			</div>
		</div>

		<div className='row'>
			<div className='col-sm-12'>
				<AudienceReportCard
					knownIndividualsTitle={Liferay.Language.get(
						'segmented-downloads'
					)}
					label={Liferay.Language.get('audience')}
					legacyDropdownRangeKey={false}
					metricAction={Liferay.Language.get(
						'download'
					).toLowerCase()}
					segmentsTitle={Liferay.Language.get('downloaded-segments')}
					uniqueVisitorsTitle={Liferay.Language.get('downloads')}
				/>
			</div>
		</div>

		<div className='row'>
			<div className='col-lg-6 col-md-12'>
				<LocationsCard
					label={Liferay.Language.get('downloads-by-location')}
					legacyDropdownRangeKey={false}
					metricLabel={Liferay.Language.get('downloads')}
				/>
			</div>

			<div className='col-lg-6 col-md-12'>
				<DevicesCard
					label={Liferay.Language.get('downloads-by-technology')}
					legacyDropdownRangeKey={false}
					metricLabel={Liferay.Language.get('downloads')}
				/>
			</div>
		</div>

		<div className='row'>
			<div className='col-sm-12'>
				<TouchpointsListCard
					assetType='DOCUMENT'
					label={Liferay.Language.get('asset-appears-on')}
					legacyDropdownRangeKey={false}
				/>
			</div>
		</div>
	</>
);

export default Overview;
