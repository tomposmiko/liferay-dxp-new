/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import ClayLink from '@clayui/link';
import {useModal} from '@clayui/modal';
import ClayNavigationBar from '@clayui/navigation-bar';
import ClayToolbar from '@clayui/toolbar';
import {ClayTooltipProvider} from '@clayui/tooltip';
import PropTypes from 'prop-types';
import React, {useState} from 'react';

import EditTitleModal from '../shared/EditTitleModal';

export default function PageToolbar({
	children,
	description,
	isSubmitting,
	onCancel,
	onChangeTab,
	onChangeTitleAndDescription,
	onSubmit,
	tab,
	tabs,
	title,
}) {
	const [modalFieldFocus, setModalFieldFocus] = useState('title');
	const [modalVisible, setModalVisible] = useState(false);

	const {observer, onClose} = useModal({
		onClose: () => setModalVisible(false),
	});

	const _handleClickEdit = (fieldFocus) => () => {
		setModalFieldFocus(fieldFocus);

		setModalVisible(true);
	};

	return (
		<div className="page-toolbar-root">
			<ClayToolbar
				aria-label={Liferay.Language.get('page-toolbar')}
				light
			>
				<ClayLayout.ContainerFluid>
					<ClayToolbar.Nav>
						<ClayToolbar.Item className="text-left" expand>
							{modalVisible && (
								<EditTitleModal
									initialDescription={description}
									initialTitle={title}
									modalFieldFocus={modalFieldFocus}
									observer={observer}
									onClose={onClose}
									onSubmit={onChangeTitleAndDescription}
								/>
							)}

							<div>
								<ClayButton
									aria-label={Liferay.Language.get(
										'edit-title'
									)}
									className="entry-heading-edit-button"
									displayType="unstyled"
									monospaced={false}
									onClick={_handleClickEdit('title')}
								>
									<div className="entry-title text-truncate">
										{title}

										<ClayIcon
											className="entry-heading-edit-icon"
											symbol="pencil"
										/>
									</div>
								</ClayButton>

								<ClayButton
									aria-label={Liferay.Language.get(
										'edit-description'
									)}
									className="entry-heading-edit-button"
									displayType="unstyled"
									monospaced={false}
									onClick={_handleClickEdit('description')}
								>
									<ClayTooltipProvider>
										<div
											className="entry-description text-truncate"
											data-tooltip-align="bottom"
											title={description}
										>
											{description || (
												<span className="entry-description-blank">
													{Liferay.Language.get(
														'no-description'
													)}
												</span>
											)}

											<ClayIcon
												className="entry-heading-edit-icon"
												symbol="pencil"
											/>
										</div>
									</ClayTooltipProvider>
								</ClayButton>
							</div>
						</ClayToolbar.Item>

						{children}

						{!!children && (
							<ClayToolbar.Item>
								<div className="tbar-divider" />
							</ClayToolbar.Item>
						)}

						<ClayToolbar.Item>
							<ClayLink
								displayType="secondary"
								href={onCancel}
								outline="secondary"
							>
								{Liferay.Language.get('cancel')}
							</ClayLink>
						</ClayToolbar.Item>

						<ClayToolbar.Item>
							<ClayButton
								disabled={isSubmitting}
								onClick={onSubmit}
								small
								type="submit"
							>
								{Liferay.Language.get('save')}
							</ClayButton>
						</ClayToolbar.Item>
					</ClayToolbar.Nav>
				</ClayLayout.ContainerFluid>
			</ClayToolbar>

			{onChangeTab && (
				<ClayNavigationBar
					aria-label={Liferay.Language.get('navigation')}
					triggerLabel={tabs[tab]}
				>
					{Object.keys(tabs).map((tabKey) => (
						<ClayNavigationBar.Item
							active={tab === tabKey}
							key={tabKey}
						>
							<ClayButton
								block
								className="nav-link"
								displayType="unstyled"
								onClick={() => onChangeTab(tabKey)}
								small
							>
								<span className="navbar-text-truncate">
									{tabs[tabKey]}
								</span>
							</ClayButton>
						</ClayNavigationBar.Item>
					))}
				</ClayNavigationBar>
			)}
		</div>
	);
}

PageToolbar.propTypes = {
	description: PropTypes.string,
	isSubmitting: PropTypes.bool,
	onCancel: PropTypes.string.isRequired,
	onChangeTab: PropTypes.func,
	onChangeTitleAndDescription: PropTypes.func,
	onSubmit: PropTypes.func.isRequired,
	tab: PropTypes.string,
	tabs: PropTypes.object,
	title: PropTypes.string,
};
