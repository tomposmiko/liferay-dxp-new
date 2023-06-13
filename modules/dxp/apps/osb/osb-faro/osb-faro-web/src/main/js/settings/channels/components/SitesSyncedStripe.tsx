import getCN from 'classnames';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import {getPluralMessage, sub} from 'shared/util/lang';
import {isNumber} from 'lodash';

interface ISitesSyncedStripeProps {
	sitesSyncedCount?: number;
}

const SitesSyncedStripe: React.FC<ISitesSyncedStripeProps> = ({
	sitesSyncedCount
}) => (
	<div
		className={getCN('sites-synced-stripe-root', {
			empty: !sitesSyncedCount
		})}
	>
		<div className='title d-flex align-items-center'>
			{sub(
				Liferay.Language.get('sites-synced-x'),
				[
					isNumber(sitesSyncedCount) ? (
						getPluralMessage(
							Liferay.Language.get('x-site'),
							Liferay.Language.get('x-sites'),
							sitesSyncedCount
						)
					) : (
						<Spinner key='LOADING_SPINNER' light size='sm' />
					)
				],
				false
			)}
		</div>

		<div>
			{sub(
				Liferay.Language.get(
					'manage-sites-synced-to-this-property-by-going-to-x-in-your-dxp-instance'
				),
				[
					<b key='INSTANCE_SETTINGS'>
						{Liferay.Language.get(
							'instance-settings-analytics-cloud-fragment'
						)}
					</b>
				],
				false
			)}
		</div>
	</div>
);

export default SitesSyncedStripe;
