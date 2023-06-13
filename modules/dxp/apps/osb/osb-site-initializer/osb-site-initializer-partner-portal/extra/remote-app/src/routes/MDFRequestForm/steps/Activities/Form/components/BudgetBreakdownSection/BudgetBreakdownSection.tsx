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

import Button, {ClayButtonWithIcon} from '@clayui/button';
import ClayIcon from '@clayui/icon';
import {ArrayHelpers} from 'formik';
import React, {useEffect, useState} from 'react';

import PRMForm from '../../../../../../../common/components/PRMForm';
import PRMFormik from '../../../../../../../common/components/PRMFormik';
import MDFRequestBudget from '../../../../../../../common/interfaces/mdfRequestBudget';
import BudgetResumeCard from './components/BudgetResumeCard';
import getNewBudget from './utils/getNewBudget';

interface IProps {
	arrayHelpers: ArrayHelpers;
	budgets: MDFRequestBudget[];
	currentActivityIndex: number;
	expenseEntries: React.OptionHTMLAttributes<HTMLOptionElement>[];
	setFieldValue: (
		field: string,
		value: any,
		shouldValidate?: boolean | undefined
	) => void;
}

const BudgetBreakdownSection = ({
	arrayHelpers,
	budgets = [],
	currentActivityIndex,
	expenseEntries,
	setFieldValue,
}: IProps) => {
	const [budgetsAmount, setBudgetsAmount] = useState<number>(0);

	const onExpenseSelected = (
		event: React.ChangeEvent<HTMLInputElement>,
		currentBudgetIndex: number
	) => {
		const expenseSelected = expenseEntries?.find(
			(expenseEntry) => expenseEntry.value === event.target.value
		);

		setFieldValue(
			`activities[${currentActivityIndex}].budgets[${currentBudgetIndex}].expense`,
			{
				key: expenseSelected?.value,
				name: expenseSelected?.label,
			}
		);
	};

	useEffect(() => {
		const amountValue = budgets?.reduce<number>(
			(previousValue, currentValue) => previousValue + +currentValue.cost,
			0
		);

		if (amountValue) {
			setBudgetsAmount(amountValue);

			setFieldValue(
				`activities[${currentActivityIndex}].totalCostOfExpense`,
				amountValue
			);

			setFieldValue(
				`activities[${currentActivityIndex}].mdfRequestAmount`,
				amountValue * 0.5
			);
		}
	}, [budgets, currentActivityIndex, setFieldValue]);

	return (
		<PRMForm.Section
			subtitle="Add all the expenses that best match with your Activity to add your Total  MDF Requested Amount"
			title="Budget Breakdown"
		>
			<div>
				{budgets.map((_, index) => (
					<div className="align-items-center d-flex" key={index}>
						<PRMForm.Group className="mr-4">
							<PRMFormik.Field
								component={PRMForm.Select}
								label="Expense"
								name={`activities[${currentActivityIndex}].budgets[${index}].expense`}
								onChange={(
									event: React.ChangeEvent<HTMLInputElement>
								) => onExpenseSelected(event, index)}
								options={expenseEntries}
								required
							/>

							<PRMFormik.Field
								component={PRMForm.InputText}
								label="Budget"
								name={`activities[${currentActivityIndex}].budgets[${index}].cost`}
								required
							/>
						</PRMForm.Group>

						<ClayButtonWithIcon
							className="mt-2"
							displayType="secondary"
							onClick={() => arrayHelpers.remove(index)}
							small
							symbol="hr"
						/>
					</div>
				))}

				<Button
					className="d-flex"
					onClick={() => arrayHelpers.push(getNewBudget())}
					outline
					small
				>
					<span className="inline-item inline-item-before">
						<ClayIcon symbol="plus" />
					</span>
					Add Expense
				</Button>
			</div>

			<div className="my-3">
				<BudgetResumeCard
					leftContent="Total MDF Requested Amount"
					rightContent={String(budgetsAmount)}
				/>

				<BudgetResumeCard
					className="mt-3"
					leftContent="Claim Percent"
					rightContent={String(0.5)}
				/>
			</div>

			<PRMFormik.Field
				component={PRMForm.InputText}
				disabled
				label="Total MDF Requested Amount"
				name={`activities[${currentActivityIndex}].mdfRequestAmount`}
				required
			/>
		</PRMForm.Section>
	);
};

export default BudgetBreakdownSection;
