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

import {array, boolean, mixed, number, object, string} from 'yup';

const KB_TO_MB = 1024;
const MAX_MB = KB_TO_MB * 3;

const validateDocument = {
	fileSize: {
		maxSize: MAX_MB,
		message: 'File Size is too large',
	},
	imageDocument: {
		message:
			'Unsupported File Format, upload a valid format *jpg *jpeg *tiff *png *pdf *doc *docx',
		types: [
			'image/jpg',
			'image/jpeg',
			'image/tiff',
			'image/png',
			'application/pdf',
			'application/msword',
			'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
		],
	},
	listOfLeadsDocuments: {
		message:
			'Unsupported File Format, upload a valid format *csv *xlsx *xls',
		types: [
			'application/vnd.ms-excel',
			'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
			'text/csv',
		],
	},
};

const claimSchema = object({
	activities: array()
		.of(
			object({
				budgets: array().when('selected', {
					is: (selected: boolean) => selected,
					then: (schema) =>
						schema.of(
							object({
								invoice: mixed().when('selected', {
									is: (selected: boolean) => selected,
									then: (schema) =>
										schema
											.test(
												'fileSize',
												validateDocument.fileSize
													.message,
												(invoice) => {
													return invoice
														? Math.ceil(
																invoice.size /
																	1000
														  ) <=
																validateDocument
																	.fileSize
																	.maxSize
														: false;
												}
											)
											.test(
												'fileType',
												validateDocument.imageDocument
													.message,
												(invoice) =>
													invoice
														? validateDocument.imageDocument.types.includes(
																invoice.type
														  )
														: false
											),
								}),
								invoiceAmount: number().when('selected', {
									is: (selected: boolean) => selected,
									then: (schema) =>
										schema
											.moreThan(
												0,
												'Need be bigger than 0'
											)
											.test(
												'biggerAmount',
												'Invoice amount is larger than the MDF requested amount',
												(invoiceAmount, testContext) =>
													Number(invoiceAmount) <=
													Number(
														testContext.parent
															.requestAmount
													)
											),
								}),

								requestAmount: number(),
							})
						),
				}),

				listQualifiedLeads: mixed().when('selected', {
					is: (selected: boolean) => selected,
					then: (schema) =>
						schema
							.test(
								'fileSize',
								validateDocument.fileSize.message,
								(listQualifiedLeads) =>
									listQualifiedLeads
										? Math.ceil(
												listQualifiedLeads.size / 1000
										  ) <= validateDocument.fileSize.maxSize
										: false
							)
							.test(
								'fileType',
								validateDocument.listOfLeadsDocuments.message,
								(listQualifiedLeads) =>
									listQualifiedLeads
										? validateDocument.listOfLeadsDocuments.types.includes(
												listQualifiedLeads.type
										  )
										: false
							),
				}),
				metrics: string().max(
					350,
					'You have exceeded the character limit'
				),
				selected: boolean(),
			})
		)
		.test(
			'needAtLeatOneSelectedActivity',
			'Need at least one activity selected',
			(activities) =>
				Boolean(activities?.some((activity) => activity.selected))
		)
		.test(
			'needMoreThanOneBudgetSelected',
			'Need at least one budget selected',
			(activities) =>
				Boolean(
					activities?.some((activity) =>
						Boolean(
							activity.budgets?.some((budget) => budget.selected)
						)
					)
				)
		)
		.test(
			'needMoreThanOneBudgetInvoice',
			'Need at least one budget invoice added',
			(activities) =>
				Boolean(
					activities?.some((activity) =>
						Boolean(
							activity.budgets?.some((budget) => budget.invoice)
						)
					)
				)
		),

	reimbursementInvoice: mixed()
		.required('Required')
		.test(
			'fileSize',
			validateDocument.fileSize.message,
			(reimbursementInvoice) =>
				reimbursementInvoice
					? Math.ceil(reimbursementInvoice.size / 1000) <=
					  validateDocument.fileSize.maxSize
					: false
		)
		.test(
			'fileType',
			validateDocument.imageDocument.message,
			(reimbursementInvoice) =>
				reimbursementInvoice
					? validateDocument.imageDocument.types.includes(
							reimbursementInvoice.type
					  )
					: false
		),
	totalClaimAmount: number()
		.moreThan(0, 'Need be bigger than 0')
		.required('Required')
		.test(
			'is-greater-than-the-requested-amount',
			'Total Claim Amount cannot be greater than Total MDF Requested Amount',
			(totalClaimAmount, testContext) =>
				Number(totalClaimAmount) <=
				Number(testContext.parent.totalrequestedAmount)
		),
});

export default claimSchema;
