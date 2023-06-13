/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import ClayButton from '@clayui/button';
import ClayModal from '@clayui/modal';
import ClayTabs from '@clayui/tabs';
import React, {useState} from 'react';

import {TProperty} from '../components/PropertiesTable';
import ChannelTab from './ChannelTab';
import SitesTab from './SitesTab';

interface IAssignModalProps {
	observer: any;
	onCloseModal: () => void;
	property: TProperty;
}

export enum ETabs {
	Channel = 0,
	Sites = 1,
}

const AssignModal: React.FC<IAssignModalProps> = ({
	observer,
	onCloseModal,
	property,
}) => {
	const [activeTabKeyValue, setActiveTabKeyValue] = useState<ETabs>(
		ETabs.Channel
	);

	return (
		<ClayModal center observer={observer} size="lg">
			<ClayModal.Header>
				{Liferay.Language.get('assign-to')} {property.name}
			</ClayModal.Header>

			<ClayModal.Body>
				<ClayTabs modern>
					<ClayTabs.Item
						active={activeTabKeyValue === ETabs.Channel}
						innerProps={{
							'aria-controls': 'tabpanel-1',
						}}
						onClick={() => setActiveTabKeyValue(ETabs.Channel)}
					>
						{Liferay.Language.get('channel')}
					</ClayTabs.Item>

					<ClayTabs.Item
						active={activeTabKeyValue === ETabs.Sites}
						innerProps={{
							'aria-controls': 'tabpanel-2',
						}}
						onClick={() => setActiveTabKeyValue(ETabs.Sites)}
					>
						{Liferay.Language.get('sites')}
					</ClayTabs.Item>

					<ClayTabs.Content activeIndex={activeTabKeyValue} fade>
						<ClayTabs.TabPane aria-labelledby="tab-1">
							<ChannelTab />
						</ClayTabs.TabPane>

						<ClayTabs.TabPane aria-labelledby="tab-2">
							<SitesTab />
						</ClayTabs.TabPane>
					</ClayTabs.Content>
				</ClayTabs>
			</ClayModal.Body>

			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton
							displayType="secondary"
							onClick={() => onCloseModal()}
						>
							{Liferay.Language.get('cancel')}
						</ClayButton>

						<ClayButton displayType="primary">
							{Liferay.Language.get('assign')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</ClayModal>
	);
};

export default AssignModal;
