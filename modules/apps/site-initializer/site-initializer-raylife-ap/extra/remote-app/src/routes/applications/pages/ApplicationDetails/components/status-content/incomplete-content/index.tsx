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

type IncompleteContentProps = {
	onClick: () => void;
};

const IncompleteContent = ({onClick}: IncompleteContentProps) => (
	<>
		<div className="d-flex flex-column">
			<div className="action-detail-title pt-3 px-5">
				<h5 className="m-0">Complete Application</h5>
			</div>

			<hr />

			<div className="action-detail-content px-5">
				<p>
					This application is incomplete and missing information to be
					properly quoted.
				</p>

				<p>
					Edit the application to finish the quote process for this
					customer.
				</p>
			</div>
		</div>

		<div className="action-detail-footer d-flex justify-content-end pb-5 pt-3 px-5">
			<ClayButton
				className="text-small-caps"
				displayType="primary"
				onClick={() => onClick()}
			>
				Edit Application
			</ClayButton>
		</div>
	</>
);

export default IncompleteContent;
