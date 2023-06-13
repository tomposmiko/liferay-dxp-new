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

import PRMForm from '../../../../common/components/PRMForm';
import PRMFormikPageProps from '../../../../common/components/PRMFormik/interfaces/prmFormikPageProps';
import Table from '../../../../common/components/Table';
import DealRegistration from '../../../../common/interfaces/dealRegistration';
import {StepType} from '../../enums/stepType';
import DealRegistrationStepProps from '../../interfaces/dealRegistrationStepProps';

const Review = ({
	onCancel,
	onPrevious,
	onSaveAsDraft,
}: PRMFormikPageProps & DealRegistrationStepProps) => {
	const {isSubmitting, values, ...formikHelpers} = useFormikContext<
		DealRegistration
	>();

	return (
		<>
			<PRMForm
				className="bg-neutral-0"
				description="Lorem ipsum dolor sit amet, consectetur adipiscing elit ut aliquam, purus sit amet luctus"
				name="review"
				title="Review Deal Registration"
			>
				<Table
					borderless
					className="bg-brand-primary-lighten-6 border-top table-striped"
					columns={[
						{
							columnKey: 'title',
							label: 'General Details',
						},
						{
							columnKey: 'value',
							label: '',
						},
					]}
					rows={[
						{
							title: 'Partner Account Name',
							value: values.partnerAccount.name,
						},
						{
							title: 'MDF Activity Associated',
							value: values.mdfActivityAssociated.name,
						},
					]}
				/>

				<Table
					borderless
					className="bg-brand-primary-lighten-6 border-top table-striped"
					columns={[
						{
							columnKey: 'title',
							label: 'Prospect Information',
						},
						{
							columnKey: 'value',
							label: '',
						},
					]}
					rows={[
						{
							title: 'Account Name',
							value: values.prospect?.accountName,
						},
						{
							title: 'Industry',
							value: values.prospect?.industry?.name,
						},
						{
							title: 'Address',
							value: values.prospect?.address,
						},
						{
							title: 'City',
							value: values.prospect?.city,
						},
						{
							title: 'Postal Code',
							value: values.prospect?.postalCode,
						},
						{
							title: 'State',
							value: values.prospect?.state?.name,
						},
						{
							title: 'Country',
							value: values.prospect?.country?.name,
						},
					]}
				/>

				<Table
					borderless
					className="bg-brand-primary-lighten-6 border-top table-striped"
					columns={[
						{
							columnKey: 'title',
							label: 'Primary Prospect Contact',
						},
						{
							columnKey: 'value',
							label: '',
						},
					]}
					rows={[
						{
							title: 'First Name',
							value: values.primaryProspect?.firstName,
						},
						{
							title: 'Last Name',
							value: values.primaryProspect?.lastName,
						},
						{
							title: 'Email Address',
							value: values.primaryProspect?.emailAddress,
						},
						{
							title: 'Phone',
							value: values.primaryProspect?.phone,
						},
						{
							title: 'Department',
							value: values.primaryProspect?.department?.name,
						},
						{
							title: 'Job Role',
							value: values.primaryProspect?.jobRole?.name,
						},
					]}
				/>

				<Table
					borderless
					className="bg-brand-primary-lighten-6 border-top table-striped"
					columns={[
						{
							columnKey: 'title',
							label: 'Additional Contacts',
						},
						{
							columnKey: 'value',
							label: '',
						},
					]}
					rows={[
						{
							title: 'First Name',
							value: values.additionalContact?.firstName,
						},
						{
							title: 'Last Name',
							value: values.additionalContact?.lastName,
						},
						{
							title: 'Email Address',
							value: values.additionalContact?.emailAddress,
						},
					]}
				/>

				<Table
					borderless
					className="bg-brand-primary-lighten-6 border-top table-striped"
					columns={[
						{
							columnKey: 'title',
							label: 'Deal Information',
						},
						{
							columnKey: 'value',
							label: '',
						},
					]}
					rows={[
						{
							title: 'Additional Information',
							value:
								values.additionalInformationAboutTheOpportunity,
						},
					]}
				/>

				<Table
					borderless
					className="bg-brand-primary-lighten-6 border-top table-striped"
					columns={[
						{
							columnKey: 'title',
							label: 'Project Information',
						},
						{
							columnKey: 'value',
							label: '',
						},
					]}
					rows={[
						{
							title: 'Project Need',
							value: values.projectNeed.join(', '),
						},
						{
							title: 'Project Solution Categories',
							value: values.projectCategories.join(', '),
						},
					]}
				/>

				<Table
					borderless
					className="bg-brand-primary-lighten-6 border-top table-striped"
					columns={[
						{
							columnKey: 'title',
							label: 'Business Objectives',
						},
						{
							columnKey: 'value',
							label: '',
						},
					]}
					rows={[
						{
							title: 'Project Timeline',
							value: values.projectTimeline,
						},
					]}
				/>

				<PRMForm.Footer>
					<div className="d-flex mr-auto">
						<Button
							disabled={isSubmitting}
							displayType={null}
							onClick={() =>
								onSaveAsDraft?.(values, formikHelpers)
							}
						>
							Save as Draft
						</Button>

						<Button
							className="mr-4"
							displayType="secondary"
							onClick={onCancel}
						>
							Cancel
						</Button>
					</div>

					<div className="d-flex">
						<Button
							className="mr-4"
							displayType="secondary"
							onClick={() => onPrevious?.(StepType.GENERAL)}
						>
							Back
						</Button>

						<Button disabled={isSubmitting} type="submit">
							Proceed
						</Button>
					</div>
				</PRMForm.Footer>
			</PRMForm>
		</>
	);
};

export default Review;
