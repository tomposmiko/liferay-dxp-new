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

import ClayAlert from '@clayui/alert';
import Button from '@clayui/button';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import {useFormikContext} from 'formik';
import {useCallback, useMemo} from 'react';

import PRMForm from '../../../../common/components/PRMForm';
import PRMFormik from '../../../../common/components/PRMFormik';
import PRMFormikPageProps from '../../../../common/components/PRMFormik/interfaces/prmFormikPageProps';
import {LiferayPicklistName} from '../../../../common/enums/liferayPicklistName';
import useCompanyOptions from '../../../../common/hooks/useCompanyOptions';
import MDFRequest from '../../../../common/interfaces/mdfRequest';
import Role from '../../../../common/interfaces/role';
import {Status} from '../../../../common/utils/constants/status';
import getPicklistOptions from '../../../../common/utils/getPicklistOptions';
import {isLiferayManager} from '../../../../common/utils/isLiferayManager';
import isObjectEmpty from '../../../../common/utils/isObjectEmpty';
import {isPartnerManager} from '../../../../common/utils/isPartnerManager';
import {StepType} from '../../enums/stepType';
import MDFRequestStepProps from '../../interfaces/mdfRequestStepProps';
import useDynamicFieldEntries from './hooks/useDynamicFieldEntries';

const Goals = ({
	onCancel,
	onContinue,
	onSaveAsDraft,
}: PRMFormikPageProps & MDFRequestStepProps) => {
	const {
		errors,
		isSubmitting,
		isValid,
		setFieldValue,
		values,
		...formikHelpers
	} = useFormikContext<MDFRequest>();
	const {
		accountRoleEntries,
		companiesEntries,
		fieldEntries,
		roleEntries,
	} = useDynamicFieldEntries();

	const {companyOptions, onCompanySelected} = useCompanyOptions(
		companiesEntries,
		useCallback(
			(country, company, currency, accountExternalReferenceCode) => {
				setFieldValue('company', company);
				setFieldValue('country', country);
				setFieldValue('currency', currency);
				setFieldValue(
					'accountExternalReferenceCode',
					accountExternalReferenceCode
				);
			},
			[setFieldValue]
		),
		fieldEntries[LiferayPicklistName.CURRENCIES],
		!isObjectEmpty(values.currency) ? values.currency : undefined,
		fieldEntries[LiferayPicklistName.REGIONS],
		!isObjectEmpty(values.country) ? values.country : undefined,
		!isObjectEmpty(values.company) ? values.company : undefined
	);

	const {
		onSelected: onCountrySelected,
		options: countryOptions,
	} = getPicklistOptions(
		fieldEntries[LiferayPicklistName.REGIONS],
		(selected) => setFieldValue('country', selected)
	);

	const {
		onSelected: onAdditionalOptionSelected,
		options: additionalOptions,
	} = getPicklistOptions(
		fieldEntries[LiferayPicklistName.ADDITIONAL_OPTIONS],
		(selected) => setFieldValue('additionalOption', selected)
	);

	const {
		onSelected: onCurrencySelected,
		options: currencyOptions,
	} = getPicklistOptions(
		fieldEntries[LiferayPicklistName.CURRENCIES],
		(selected) => setFieldValue('currency', selected)
	);

	const companyCurrencies =
		currencyOptions &&
		values.currency &&
		currencyOptions.filter(
			(currency) => currency.value === values.currency.key
		);

	const goalsErrors = useMemo(() => {
		delete errors.activities;

		return errors;
	}, [errors]);

	const isPartnerManagerRole = useMemo(() => {
		const roles = accountRoleEntries(values.company?.id);

		return isPartnerManager(roles as Role[]);
	}, [accountRoleEntries, values.company?.id]);

	const getRequestPage = () => {
		if (!fieldEntries || !roleEntries || !companiesEntries) {
			return <ClayLoadingIndicator />;
		}

		const userAccountRolesCanEdit = isLiferayManager(roleEntries);

		if (
			values.id &&
			roleEntries &&
			!isPartnerManagerRole &&
			!userAccountRolesCanEdit &&
			values.mdfRequestStatus?.key !== 'draft' &&
			values.mdfRequestStatus?.key !== 'moreInfoRequested'
		) {
			return (
				<PRMForm name="" title="MDF Request">
					<div className="d-flex justify-content-center mt-4">
						<ClayAlert
							className="m-0 w-100"
							displayType="info"
							title="Info:"
						>
							This MDF Request can not be edited.
						</ClayAlert>
					</div>

					<PRMForm.Footer>
						<div className="d-flex mr-auto">
							<Button
								className="mr-4"
								displayType="secondary"
								onClick={() => onCancel()}
							>
								Cancel
							</Button>
						</div>
					</PRMForm.Footer>
				</PRMForm>
			);
		}

		return (
			<PRMForm name="Goals" title="Campaign Information">
				<PRMForm.Section title="Partner">
					<PRMForm.Group>
						<PRMFormik.Field
							component={PRMForm.Select}
							label="Company Name"
							name="company"
							onChange={onCompanySelected}
							options={companyOptions}
							required
						/>

						<PRMFormik.Field
							component={PRMForm.Select}
							label="Country"
							name="country"
							onChange={onCountrySelected}
							options={countryOptions}
							required
						/>

						<PRMFormik.Field
							component={PRMForm.Select}
							label="Currency"
							name="currency"
							onChange={onCurrencySelected}
							options={companyCurrencies}
							required
						/>
					</PRMForm.Group>
				</PRMForm.Section>

				<PRMForm.Section title="Campaign">
					<PRMFormik.Field
						component={PRMForm.InputText}
						label="Provide the name of the campaign"
						name="overallCampaignName"
						required
					/>

					<PRMFormik.Field
						component={PRMForm.InputText}
						label="Provide a short description of the overall campaign"
						name="overallCampaignDescription"
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
							fieldEntries[
								LiferayPicklistName.TARGET_AUDIENCE_ROLES
							]
						}
						label="Choose your target audience/role (Select all that apply)"
						name="targetAudienceRoles"
						required
					/>
				</PRMForm.Section>

				<PRMForm.Footer>
					<div className="d-flex justify-content-end mr-auto">
						<Button
							className="inline-item inline-item-after"
							disabled={isSubmitting}
							displayType={null}
							onClick={() =>
								onSaveAsDraft?.(values, formikHelpers)
							}
						>
							Save as Draft
							{isSubmitting &&
								values.mdfRequestStatus === Status.DRAFT && (
									<ClayLoadingIndicator className="inline-item inline-item-after ml-2" />
								)}
						</Button>
					</div>

					<div className="d-flex justify-content-between px-2 px-md-0">
						<Button
							className="mr-4"
							displayType="secondary"
							onClick={onCancel}
						>
							Cancel
						</Button>

						<Button
							disabled={
								(!isValid && !isObjectEmpty(goalsErrors)) ||
								isSubmitting
							}
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

	return getRequestPage();
};
export default Goals;
