import AcquisitionsCard from 'sites/components/AcquisitionsCard';
import CohortAnalysisCard from 'sites/hocs/CohortAnalysisCard';
import DevicesCard from 'sites/hocs/DevicesCard';
import InterestsCard from 'sites/hocs/InterestsCard';
import LocationsCard from 'sites/hocs/LocationsCard';
import React, {FC} from 'react';
import SearchTermsCard from 'sites/hocs/SearchTermsCard';
import SiteMetricsCard from 'sites/components/SiteMetricCard';
import TopPagesCard from 'sites/components/TopPagesCard';
import VisitorsByTimeCard from 'sites/hocs/VisitorsByTimeCard';
import {CompositionTypes} from 'shared/util/constants';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useParams} from 'react-router-dom';

interface IOverviewProps extends React.HTMLAttributes<HTMLDivElement> {
	channelName: string;
}

const Overview: FC<IOverviewProps> = ({channelName}) => {
	const {channelId, groupId} = useParams();

	return (
		<div className='sites-dashboard-overview-root overview-root'>
			<div className='row'>
				<div className='col-xl-12'>
					<SiteMetricsCard
						label={
							sub(Liferay.Language.get('x-activities'), [
								channelName
							]) as string
						}
						showIntervals
					/>
				</div>
			</div>

			<div className='row'>
				<div className='col-xl-6'>
					<TopPagesCard
						className='top-pages-card-root table-tabs-root'
						footer={{
							href: toRoute(Routes.SITES_TOUCHPOINTS, {
								channelId,
								groupId
							}),
							label: Liferay.Language.get('view-pages')
						}}
						label={Liferay.Language.get('top-pages')}
						legacyDropdownRangeKey={false}
					/>
				</div>

				<div className='col-xl-6'>
					<AcquisitionsCard
						className='acquisitions-card-root table-tabs-root'
						compositionBagName={CompositionTypes.Acquisitions}
						label={Liferay.Language.get('acquisitions')}
						legacyDropdownRangeKey={false}
					/>
				</div>
			</div>

			<div className='row'>
				<div className='col-xl-4'>
					<VisitorsByTimeCard
						className='visitors-by-time-card'
						label={Liferay.Language.get('visitors-by-day-and-time')}
					/>
				</div>

				<div className='col-xl-4'>
					<SearchTermsCard />
				</div>

				<div className='col-xl-4'>
					<InterestsCard />
				</div>
			</div>

			<div className='row'>
				<div className='col-xl-6'>
					<LocationsCard
						label={Liferay.Language.get('sessions-by-location')}
						legacyDropdownRangeKey={false}
						metricLabel={Liferay.Language.get('sessions')}
					/>
				</div>

				<div className='col-xl-6'>
					<DevicesCard
						label={Liferay.Language.get('session-technology')}
						legacyDropdownRangeKey={false}
						metricLabel={Liferay.Language.get('sessions')}
					/>
				</div>
			</div>

			<div className='row'>
				<div className='col-xl-12'>
					<CohortAnalysisCard />
				</div>
			</div>
		</div>
	);
};

export default Overview;
