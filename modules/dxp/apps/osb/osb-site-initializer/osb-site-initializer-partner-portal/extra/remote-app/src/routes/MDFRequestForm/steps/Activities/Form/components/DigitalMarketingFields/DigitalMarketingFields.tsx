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

import PRMForm from '../../../../../../../common/components/PRMForm';
import PRMFormik from '../../../../../../../common/components/PRMFormik';
import getBooleanEntries from '../../../../../../../common/utils/getBooleanEntries';

interface IProps {
	index: number;
}

const DigitalMarketingFields = ({index}: IProps) => (
	<>
		<PRMFormik.Field
			component={PRMForm.InputText}
			label="Overall message/content/CTA"
			name={`activities[${index}].overallMessageContentCTA`}
			required
		/>
		<PRMFormik.Field
			component={PRMForm.InputText}
			label="Any specific sites to be used"
			name={`activities[${index}].specificSites`}
			required
		/>
		<PRMFormik.Field
			component={PRMForm.InputText}
			label="Keywords for PPC campaigns (must be approved by Liferay prior to execution)"
			name={`activities[${index}].keywordsForPPCCampaigns`}
		/>
		<PRMFormik.Field
			component={PRMForm.InputText}
			label="Ad (any size/type)"
			name={`activities[${index}].ad`}
		/>
		<PRMFormik.Field
			component={PRMForm.RadioGroup}
			items={getBooleanEntries()}
			label="Do you require any assets from Liferay?"
			name={`activities[${index}].assetsLiferayRequired`}
			required
			small
		/>
		<PRMFormik.Field
			component={PRMForm.InputText}
			label="How will the Liferay brand be used in the campaign?"
			name={`activities[${index}].howLiferayBrandUsed`}
			required
		/>
	</>
);

export default DigitalMarketingFields;
