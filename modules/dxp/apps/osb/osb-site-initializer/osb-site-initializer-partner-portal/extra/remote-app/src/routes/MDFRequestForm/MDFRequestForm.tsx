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

import {FormikHelpers, setNestedObjectValues} from 'formik';
import {useState} from 'react';

import PRMFormik from '../../common/components/PRMFormik';
import {PRMPageRoute} from '../../common/enums/prmPageRoute';
import {RequestStatus} from '../../common/enums/requestStatus';
import MDFRequest from '../../common/interfaces/mdfRequest';
import createMDFRequest from '../../common/services/liferay/object/mdf-requests/createMDFRequest';
import liferayNavigate from '../../common/utils/liferayNavigate';
import {StepType} from './enums/stepType';
import Activities from './steps/Activities';
import Goals from './steps/Goals';
import goalsSchema from './steps/Goals/schema/yup';
import isObjectEmpty from './utils/isObjectEmpty';

const initialFormValues: MDFRequest = {
	activities: [],
	additionalOption: {},
	country: {},
	liferayBusinessSalesGoals: [],
	overallCampaign: '',
	r_accountToMDFRequests_accountEntryId: '',
	requestStatus: RequestStatus.DRAFT,
	targetAudienceRoles: [],
	targetMarkets: [],
};

type StepComponent = {
	[key in StepType]?: JSX.Element;
};

const onSubmit = () => {};

const onSaveAsDraft = async (
	values: MDFRequest,
	formikHelpers: Omit<FormikHelpers<MDFRequest>, 'setFieldValue'>
) => {
	formikHelpers.setSubmitting(true);

	const dtoMDFRequest = await createMDFRequest(values);

	if (dtoMDFRequest) {
		liferayNavigate(PRMPageRoute.MDF_REQUESTS_LISTING);
	}
};

const onCancel = () => liferayNavigate(PRMPageRoute.MDF_REQUESTS_LISTING);

const MDFRequestForm = () => {
	const [step, setStep] = useState<StepType>(StepType.GOALS);

	const onContinue = async (
		formikHelpers: Omit<FormikHelpers<MDFRequest>, 'setFieldValue'>,
		nextStep: StepType
	) => {
		const validationErrors = await formikHelpers.validateForm();

		if (isObjectEmpty(validationErrors)) {
			setStep(nextStep);

			return;
		}

		formikHelpers.setTouched(setNestedObjectValues(validationErrors, true));
	};

	const onPrevious = () => setStep(StepType.GOALS);

	const StepFormComponent: StepComponent = {
		[StepType.GOALS]: (
			<Goals
				onCancel={onCancel}
				onContinue={onContinue}
				onSaveAsDraft={onSaveAsDraft}
				validationSchema={goalsSchema}
			/>
		),
		[StepType.ACTIVITIES]: (
			<PRMFormik.Array
				component={Activities}
				name="activities"
				onCancel={onCancel}
				onContinue={onContinue}
				onPrevious={onPrevious}
				onSaveAsDraft={onSaveAsDraft}
			/>
		),
	};

	return (
		<PRMFormik initialValues={initialFormValues} onSubmit={onSubmit}>
			{StepFormComponent[step]}
		</PRMFormik>
	);
};

export default MDFRequestForm;
