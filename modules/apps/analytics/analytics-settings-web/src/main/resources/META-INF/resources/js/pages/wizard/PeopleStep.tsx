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
import React from 'react';

import BasePage from '../../components/BasePage';
import People from '../../components/people/People';
import {ESteps, IGenericStepProps} from './WizardPage';

const Step: React.FC<IGenericStepProps> = ({onChangeStep}) => (
	<BasePage
		description={Liferay.Language.get('sync-people-description')}
		title={Liferay.Language.get('sync-people')}
	>
		<People />

		<BasePage.Footer>
			<ClayButton.Group spaced>
				<ClayButton
					displayType="secondary"
					onClick={() => onChangeStep(ESteps.Property)}
				>
					{Liferay.Language.get('previous')}
				</ClayButton>

				<ClayButton onClick={() => onChangeStep(ESteps.Attributes)}>
					{Liferay.Language.get('next')}
				</ClayButton>
			</ClayButton.Group>
		</BasePage.Footer>
	</BasePage>
);

export default Step;
