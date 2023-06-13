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

import {ClayToggle} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import {useState} from 'react';

import ScheduleUpdates from './ScheduleOptions';

const WhatsNewSettings = () => {
	const [realUpdateToggled, setRealUpdateToggled] = useState(true);
	const [unassignedToggled, setUnassignedToggled] = useState(true);
	const [openToggled, setOpenToggle] = useState(true);
	const [abandonedToggled, setAbandonedToggled] = useState(true);

	return (
		<div className="dashboard-whats-new-modal-container">
			<div className="d-flex font-weight-bolder justify-content-between mt-5 pb-2 px-5">
				Real time updates
				<ClayToggle
					onToggle={setRealUpdateToggled}
					toggled={realUpdateToggled}
				/>
			</div>

			<hr className="mb-5"></hr>

			{realUpdateToggled === false && <ScheduleUpdates />}

			<div className="font-weight-bolder mt-3 pb-2 px-5">Display</div>

			<div className="font-weight-lighter mt-3 pb-5 px-5 text-neutral-8">
				<div className="d-flex dashboard-whats-new-modal-lines dashboard-whats-new-modal-title justify-content-between mb-3">
					<div className="align-items-center d-flex flex-row mt-3">
						<ClayIcon
							className="mr-5 whats-new-modal-icon"
							symbol="drag"
						></ClayIcon>
						Unassigned
					</div>

					<div className="mt-3">
						<ClayToggle
							onToggle={setUnassignedToggled}
							toggled={unassignedToggled}
						/>
					</div>
				</div>

				<div className="d-flex dashboard-whats-new-modal-lines dashboard-whats-new-modal-title justify-content-between mb-3">
					<div className="align-items-center d-flex flex-row mt-3">
						<ClayIcon
							className="mr-5 whats-new-modal-icon"
							symbol="drag"
						></ClayIcon>
						Abandoned
					</div>

					<div className="mt-3">
						<ClayToggle
							onToggle={setAbandonedToggled}
							toggled={abandonedToggled}
						/>
					</div>
				</div>

				<div className="d-flex dashboard-whats-new-modal-lines dashboard-whats-new-modal-title justify-content-between mb-3">
					<div className="align-items-center d-flex flex-row mt-3">
						<ClayIcon
							className="mr-5 whats-new-modal-icon"
							symbol="drag"
						></ClayIcon>
						Open
					</div>

					<div className="mt-3">
						<ClayToggle
							onToggle={setOpenToggle}
							toggled={openToggled}
						/>
					</div>
				</div>
			</div>
		</div>
	);
};

export default WhatsNewSettings;
