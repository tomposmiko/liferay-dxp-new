import ClayButton from '@clayui/button';

import cash from '../../assets/images/cash.svg';
import cloudUpload from '../../assets/images/cloud-upload.svg';
import {GateCard} from '../../components/Card/GateCard';
import {Footer} from '../../components/Footer/Footer';
import {Header} from '../../components/Header/Header';

import './PublisherGatePage.scss';

export function PublisherGatePage() {
	return (
		<div className="publisher-gate-page-container">
			<div className="publisher-gate-page-body">
				<Header
					description="We are happy to have you interested in the Liferay Marketplace. At the moment, we are working on enhancing the experience for our customers in the Marketplace and access is invite only. If you are an existing Liferay customer, please keep an eye out for an announcement related to the new Marketplace in the coming months!"
					title="Becoming a Liferay Marketplace Customer"
				/>

				<GateCard
					description="The Liferay Marketplace is the premier place for Liferay customers to find pre-built, pre-approved app extensions to quickly extend the Liferay platform to new and legacy technologies."
					image={{
						description: 'Cloud Upload',
						svg: cloudUpload,
					}}
					label="Free"
					link="Learn More"
					title="Publish Apps to the Liferay Marketplace"
				/>

				<GateCard
					description="The Liferay Marketplace gives you the opportunity to monetize your app or solutions from a single use case to many, while engaging with new customer opportunities and generating ongoing revenue."
					image={{
						description: 'Cash',
						svg: cash,
					}}
					link="Learn More"
					title="Monetize Your Apps and Solutions"
				/>

				<hr className="publisher-gate-page-divider" />

				<div className="publisher-gate-page-button-container">
					<div>
						<ClayButton
							onClick={() => {
								window.location.href =
									'http://marketplace.liferay.com/';
							}}
						>
							Go Back to Marketplace
						</ClayButton>
					</div>
				</div>
			</div>

			<Footer />
		</div>
	);
}
