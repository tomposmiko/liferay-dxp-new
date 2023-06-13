import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import ClayNavigationBar from '@clayui/navigation-bar';
import classNames from 'classnames';
import {useEffect, useState} from 'react';

import arrowDown from '../../assets/icons/arrow-down.svg';
import arrowLeft from '../../assets/icons/arrow-left.svg';
import circleFullIcon from '../../assets/icons/circle_fill.svg';
import circleInfoIcon from '../../assets/icons/info-circle-icon.svg';
import {DashboardListItems} from '../../components/DashboardNavigation/DashboardNavigation';
import {AppProps} from '../../components/DashboardTable/DashboardTable';
import {useAppContext} from '../../manage-app-state/AppManageState';
import {TYPES} from '../../manage-app-state/actionTypes';
import {ReviewAndSubmitAppPage} from '../ReviewAndSubmitAppPage/ReviewAndSubmitAppPage';

import './AppDetailsPage.scss';

interface AppDetailsPageProps {
	dashboardNavigationItems: DashboardListItems[];
	selectedApp: AppProps;
	setSelectedApp: (value: AppProps | undefined) => void;
}

export function AppDetailsPage({
	dashboardNavigationItems,
	selectedApp,
	setSelectedApp,
}: AppDetailsPageProps) {
	const [navigationBarActive, setNavigationBarActive] =
		useState('App Details');

	const [_, dispatch] = useAppContext();

	useEffect(() => {
		dispatch({
			payload: {
				value: {
					appERC: selectedApp.externalReferenceCode,
					appProductId: selectedApp.productId,
				},
			},
			type: TYPES.SUBMIT_APP_PROFILE,
		});
	}, [selectedApp]);

	return (
		<div className="app-details-page-container">
			<button
				className="app-details-page-back-button"
				onClick={() => {
					dashboardNavigationItems.forEach(({itemName, items}) => {
						if (itemName === 'apps') {
							items?.forEach((item) => {
								if (item.name === selectedApp.name) {
									item.selected = false;
								}
							});
						}
					});

					setSelectedApp(undefined);
				}}
			>
				<div>
					<img
						alt="arrow left"
						className="app-details-page-back-button-icon"
						src={arrowLeft}
					/>
					Back to Apps
				</div>
			</button>

			<ClayAlert
				className="app-details-page-alert-container"
				displayType="info"
			>
				<div className="app-details-page-alert-items-container">
					<img
						alt="Circle Info "
						className="app-details-page-alert-icon"
						src={circleInfoIcon}
					/>

					<span className="app-details-page-alert-text">
						This submission is currently under review by Liferay.
						Once the process is complete, you will be able to
						publish it to the marketplace. Meanwhile, any
						information or data from this app submission cannot be
						updated.
					</span>
				</div>
			</ClayAlert>

			<div className="app-details-page-app-info-main-container">
				<div className="app-details-page-app-info-left-container">
					<div>
						<img
							alt="App Logo"
							className="app-details-page-app-info-logo"
							src={selectedApp.thumbnail}
						/>
					</div>

					<div>
						<span className="app-details-page-app-info-title">
							{selectedApp.name}
						</span>

						<div className="app-details-page-app-info-subtitle-container">
							<span className="app-details-page-app-info-subtitle-text">
								v{selectedApp.version}
							</span>

							<img
								alt="status icon"
								className={classNames(
									'app-details-page-app-info-subtitle-icon',
									{
										'app-details-page-app-info-subtitle-icon-hidden':
											selectedApp.status === 'Hidden',
										'app-details-page-app-info-subtitle-icon-pending':
											selectedApp.status === 'Pending',
										'app-details-page-app-info-subtitle-icon-published':
											selectedApp.status === 'Published',
									}
								)}
								src={circleFullIcon}
							/>

							<span className="app-details-page-app-info-subtitle-text">
								{selectedApp.status}
							</span>
						</div>
					</div>
				</div>

				<div className="app-details-page-app-info-buttons-container">
					<button className="app-details-page-app-info-button-preview-app-page">
						Preview App Page
					</button>

					<button className="app-details-page-app-info-button-manage">
						Manage
						<img
							alt="Arrow Down"
							className="app-details-page-app-info-button-manage-icon"
							src={arrowDown}
						/>
					</button>
				</div>
			</div>

			<div>
				<ClayNavigationBar
					className="app-details-page-navigation-bar"
					triggerLabel={navigationBarActive}
				>
					<ClayNavigationBar.Item
						active={navigationBarActive === 'App Details'}
					>
						<ClayButton
							onClick={() =>
								setNavigationBarActive('App Details')
							}
						>
							<span>App Details</span>
						</ClayButton>
					</ClayNavigationBar.Item>
				</ClayNavigationBar>

				<ReviewAndSubmitAppPage
					onClickBack={() => {}}
					onClickContinue={() => {}}
					readonly
				/>
			</div>
		</div>
	);
}
