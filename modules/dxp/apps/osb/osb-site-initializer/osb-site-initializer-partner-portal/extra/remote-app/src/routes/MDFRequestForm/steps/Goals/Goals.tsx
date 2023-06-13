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

import Button from '@clayui/button';
import {useFormikContext} from 'formik';
import {useCallback, useEffect} from 'react';

import PRMForm from '../../../../common/components/PRMForm';
import PRMFormik from '../../../../common/components/PRMFormik';
import PRMFormikPageProps from '../../../../common/components/PRMFormik/interfaces/prmFormikPageProps';
import {LiferayPicklistName} from '../../../../common/enums/liferayPicklistName';
import MDFRequest from '../../../../common/interfaces/mdfRequest';
import {StepType} from '../../enums/stepType';
import MDFRequestStepProps from '../../interfaces/mdfRequestStepProps';
import useCountryCompanyExtender from './hooks/useCountryCompanyExtender';
import useDynamicFieldEntries from './hooks/useDynamicFieldEntries';
import optionSelection from './utils/optionSelection';

const Goals = ({
	onCancel,
	onContinue,
	onSaveAsDraft,
}: PRMFormikPageProps & MDFRequestStepProps<MDFRequest>) => {
	const {
		isSubmitting,
		isValid,
		setFieldValue,
		values,
		...formikHelpers
	} = useFormikContext<MDFRequest>();

	const {companiesEntries, fieldEntries} = useDynamicFieldEntries();

	const {setSelectedAccountEntryId} = useCountryCompanyExtender(
		useCallback((country) => setFieldValue('country', country), [
			setFieldValue,
		])
	);

	useEffect(() => {
		if (values.r_accountToMDFRequests_accountEntryId) {
			setSelectedAccountEntryId(
				values.r_accountToMDFRequests_accountEntryId
			);
		}
	}, [
		setSelectedAccountEntryId,
		values.r_accountToMDFRequests_accountEntryId,
	]);

	const countryOptions = fieldEntries[LiferayPicklistName.REGIONS];
	const onCountrySelected = optionSelection(
		countryOptions,
		(countryOptionSelected) =>
			setFieldValue('country', countryOptionSelected)
	);

	const additionalOptions =
		fieldEntries[LiferayPicklistName.ADDITIONAL_OPTIONS];
	const onAdditionalOptionSelected = optionSelection(
		additionalOptions,
		(additionalOptionSelected) =>
			setFieldValue('additionalOption', additionalOptionSelected)
	);

	return (
		<PRMForm className="mb-4" name="Goals" title="Campaign Information">
			<PRMForm.Section title="Partner">
				<PRMForm.Group>
					<PRMFormik.Field
						component={PRMForm.Select}
						label="Company Name"
						name="r_accountToMDFRequests_accountEntryId"
						options={companiesEntries}
						required
					/>

					<PRMFormik.Field
						component={PRMForm.Select}
						label="Country"
						name="country"
						onChange={onCountrySelected}
						options={fieldEntries[LiferayPicklistName.REGIONS]}
						required
					/>
				</PRMForm.Group>
			</PRMForm.Section>

			<PRMForm.Section title="Campaign">
				<PRMFormik.Field
					component={PRMForm.InputText}
					label="Provide a name and short description of the overall campaign"
					name="overallCampaign"
					required
				/>

				<PRMFormik.Field
					component={PRMForm.CheckboxGroup}
					items={
						fieldEntries[
							LiferayPicklistName.LIFERAY_BUSINESS_SALES_GOALS
						]
					}
					label="Select Liferay business/sales goals this Campaign serves (choose up to three)"
					name="liferayBusinessSalesGoals"
					required
				/>
			</PRMForm.Section>

			<PRMForm.Section title="Target Market">
				<PRMFormik.Field
					component={PRMForm.CheckboxGroup}
					items={fieldEntries[LiferayPicklistName.TARGET_MARKETS]}
					label="Please select the target market(s) for this campaign (choose up to three)"
					name="targetMarkets"
					required
				/>

				<PRMFormik.Field
					component={PRMForm.RadioGroup}
					items={additionalOptions}
					label="Additional options? Choose one if applicable"
					name="additionalOption"
					onChange={onAdditionalOptionSelected}
				/>

				<PRMFormik.Field
					component={PRMForm.CheckboxGroup}
					items={
						fieldEntries[LiferayPicklistName.TARGET_AUDIENCE_ROLES]
					}
					label="Choose your target audience/role (Select all that apply)"
					name="targetAudienceRoles"
					required
				/>
			</PRMForm.Section>

			<PRMForm.Footer>
				<div className="d-flex mr-auto">
					<Button
						disabled={isSubmitting}
						displayType={null}
						onClick={() => onSaveAsDraft?.(values, formikHelpers)}
					>
						Save as Draft
					</Button>
				</div>

				<div>
					<Button
						className="mr-4"
						displayType="secondary"
						onClick={onCancel}
					>
						Cancel
					</Button>

					<Button
						disabled={!isValid || isSubmitting}
						onClick={() =>
							onContinue?.(formikHelpers, StepType.ACTIVITIES)
						}
					>
						Continue
					</Button>
				</div>
			</PRMForm.Footer>
		</PRMForm>
	);
};

export default Goals;
