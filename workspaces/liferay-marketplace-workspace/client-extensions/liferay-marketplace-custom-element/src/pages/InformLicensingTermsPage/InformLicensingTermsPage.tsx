import cancelIcon from '../../assets/icons/cancel-icon.svg';
import pendingActionsIcon from '../../assets/icons/pending-actions-icon.svg';
import scheduleIcon from '../../assets/icons/schedule-icon.svg';
import taskCheckedIcon from '../../assets/icons/task-checked-icon.svg';
import {Header} from '../../components/Header/Header';
import {RadioCard} from '../../components/RadioCard/RadioCard';
import {Section} from '../../components/Section/Section';

import './InformLicensingTermsPage.scss';
import {NewAppPageFooterButtons} from '../../components/NewAppPageFooterButtons/NewAppPageFooterButtons';
import {useAppContext} from '../../manage-app-state/AppManageState';
import {TYPES} from '../../manage-app-state/actionTypes';
import {
	createAppLicensePrice,
	createProductSubscriptionConfiguration,
} from '../../utils/api';

interface InformLicensingTermsPageProps {
	onClickBack: () => void;
	onClickContinue: () => void;
}

export function InformLicensingTermsPage({
	onClickBack,
	onClickContinue,
}: InformLicensingTermsPageProps) {
	const [{appERC, appLicense, appProductId, dayTrial, priceModel}, dispatch] =
		useAppContext();

	return (
		<div className="informing-licensing-terms-page-container">
			<Header
				description="Define the licensing approach for your app. This will impact users' licensing renew experience."
				title="Inform licensing terms"
			/>

			<Section
				label="App License"
				required
				tooltip="More Info"
				tooltipText="More Info"
			>
				<div className="informing-licensing-terms-page-app-license-container">
					<RadioCard
						description="The app is offered in the Marketplace with no charge."
						icon={scheduleIcon}
						onChange={() => {
							dispatch({
								payload: {value: 'perpetual'},
								type: TYPES.UPDATE_APP_LICENSE,
							});
						}}
						selected={appLicense === 'perpetual'}
						title="Perpetual License"
						tooltip="More Info"
					/>

					<RadioCard
						description="License must be renewed annually."
						disabled={priceModel === 'free'}
						icon={pendingActionsIcon}
						onChange={() => {
							dispatch({
								payload: {value: 'non-perpetual'},
								type: TYPES.UPDATE_APP_LICENSE,
							});
						}}
						selected={appLicense === 'non-perpetual'}
						title="Non-perpetual license"
						tooltip="More Info"
					/>
				</div>
			</Section>

			<Section
				label="30-day Trial"
				required
				tooltip="More Info"
				tooltipText="More Info"
			>
				<div className="informing-licensing-terms-page-day-trial-container">
					<RadioCard
						description="Offer a 30-day free trial for this app"
						icon={taskCheckedIcon}
						onChange={() => {
							dispatch({
								payload: {value: 'yes'},
								type: TYPES.UPDATE_APP_TRIAL_INFO,
							});
						}}
						selected={dayTrial === 'yes'}
						title="Yes"
						tooltip="More Info"
					/>

					<RadioCard
						description="Do not offer a 30-day free trial"
						icon={cancelIcon}
						onChange={() => {
							dispatch({
								payload: {value: 'no'},
								type: TYPES.UPDATE_APP_TRIAL_INFO,
							});
						}}
						selected={dayTrial === 'no'}
						title="No"
						tooltip="More Info"
					/>
				</div>
			</Section>

			<NewAppPageFooterButtons
				onClickBack={() => onClickBack()}
				onClickContinue={() => {
					const submitLicenseTermsPage = async () => {
						if (priceModel === 'free') {
							createAppLicensePrice({
								appProductId,
								body: {
									price: 0,
									sku: 'default',
								},
							});
						}

						if (dayTrial === 'yes') {
							createAppLicensePrice({
								appProductId,
								body: {
									price: 0,
									sku: 'trial',
								},
							});
						}

						if (appLicense === 'non-perpetual') {
							createProductSubscriptionConfiguration({
								body: {
									length: 1,
									numberOfLength: 0,
									subscriptionType: 'yearly',
									subscriptionTypeSettings: {
										month: '0',
										monthDay: '1',
										yearlyMode: '0',
									},
								},
								externalReferenceCode: appERC,
							});
						}
					};

					submitLicenseTermsPage();

					onClickContinue();
				}}
				showBackButton
			/>
		</div>
	);
}
