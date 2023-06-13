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

import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import {ReactNode, useState} from 'react';
import {Link, useOutletContext} from 'react-router-dom';

import Avatar from '../../../../../../components/Avatar';
import AssignToMe from '../../../../../../components/Avatar/AssigneToMe';
import Code from '../../../../../../components/Code';
import Container from '../../../../../../components/Layout/Container';
import StatusBadge from '../../../../../../components/StatusBadge';
import QATable, {Orientation} from '../../../../../../components/Table/QATable';
import {TestrayCaseResult} from '../../../../../../graphql/queries';
import useAssignCaseResult from '../../../../../../hooks/useAssignCaseResult';
import i18n from '../../../../../../i18n';
import {getStatusLabel} from '../../../../../../util/constants';
import {getTimeFromNow} from '../../../../../../util/date';

type CollapsableItemProps = {
	children: ReactNode;
	count: number;
	title: string;
};

type TestrayAttachment = {
	name: string;
	url: string;
	value: string;
};

const CollapsableItem: React.FC<CollapsableItemProps> = ({
	children,
	count,
	title,
}) => {
	const [visible, setVisible] = useState(false);

	return (
		<>
			<span className="custom-link" onClick={() => setVisible(!visible)}>
				{`${i18n.translate(
					visible ? 'hide' : 'show'
				)} ${count} ${title}`}
			</span>

			{visible && <div>{children}</div>}
		</>
	);
};

const CaseResult = () => {
	const {
		caseResult,
		projectId,
		refetch,
	}: {
		caseResult: TestrayCaseResult;
		projectId: string;
		refetch: () => void;
	} = useOutletContext();

	const {onAssignToMe} = useAssignCaseResult();

	const getAttachments = (): TestrayAttachment[] => {
		try {
			return JSON.parse(caseResult.attachments);
		}
		catch (error) {
			return [];
		}
	};

	const attachments = getAttachments();

	return (
		<ClayLayout.Row>
			<ClayLayout.Col xs={9}>
				<Container className="mt-4" collapsable title="Test Details">
					<QATable
						items={[
							{
								title: i18n.translate('status'),
								value: (
									<StatusBadge
										type={getStatusLabel(
											caseResult.dueStatus
										)}
									>
										{getStatusLabel(caseResult.dueStatus)}
									</StatusBadge>
								),
							},
							{
								title: i18n.translate('errors'),
								value: caseResult.errors && (
									<Code>{caseResult.errors}</Code>
								),
							},
							{
								flexHeading: true,
								title: i18n.sub(
									'warnings-x',
									caseResult.warnings.toString()
								),
								value: attachments.find(({name}) =>
									name.toLowerCase().includes('warning')
								)?.name,
							},
							{
								flexHeading: true,
								title: i18n.sub(
									'attachments-x',
									attachments.length.toString()
								),
								value: (
									<CollapsableItem
										count={attachments.length}
										title={i18n.translate('attachment')}
									>
										<div className="d-flex flex-column mb-1">
											{attachments.map(
												(attachment, index) => (
													<a
														className="mt-2"
														href={attachment.url}
														key={index}
														rel="noopener noreferrer"
														target="_blank"
													>
														{attachment.name}

														<ClayIcon
															className="ml-2"
															fontSize={12}
															symbol="shortcut"
														/>
													</a>
												)
											)}
										</div>
									</CollapsableItem>
								),
							},
							{
								title: i18n.translate('git-hash'),
								value: '',
							},
							{
								title: i18n.translate('github-compare-urls'),
								value: '',
							},
						]}
					/>
				</Container>

				<Container className="mt-4" collapsable title="Case Details">
					<QATable
						items={[
							{
								title: i18n.translate('priority'),
								value: caseResult.case?.priority,
							},
							{
								title: i18n.translate('main-component'),
								value: caseResult.case?.component?.name,
							},
							{
								title: i18n.translate('subcomponents'),
								value: '',
							},
							{
								title: i18n.translate('Type'),
								value: caseResult.case?.caseType?.name,
							},
							{
								title: i18n.translate('estimated-duration'),
								value: caseResult.case?.estimatedDuration || 0,
							},
							{
								title: i18n.translate('description'),
								value: caseResult.case?.description,
							},
							{
								title: i18n.translate('steps'),
								value: caseResult.case?.steps,
							},
						]}
					/>

					<Link
						to={`/project/${projectId}/cases/${caseResult.case.id}`}
					>
						{i18n.translate('view-case')}
					</Link>
				</Container>
			</ClayLayout.Col>

			<ClayLayout.Col xs={3}>
				<Container collapsable title={i18n.translate('dates')}>
					<QATable
						items={[
							{
								title: i18n.translate('updated'),
								value: getTimeFromNow(caseResult.dateModified),
							},
							{
								title: '',
								value: '',
							},
							{
								divider: true,
								title: i18n.translate('execution-date'),
								value: getTimeFromNow(caseResult.dateModified),
							},
							{
								divider: true,
								title: i18n.translate('assignee'),
								value: caseResult?.user ? (
									<Avatar
										displayName
										name={`${caseResult.user.givenName} ${caseResult.user.additionalName}`}
									/>
								) : (
									<AssignToMe
										onClick={() =>
											onAssignToMe(caseResult).then(
												refetch
											)
										}
									/>
								),
							},
							{
								divider: true,
								title: i18n.translate('issues'),
								value: '-',
							},
							{
								title: i18n.translate('comment'),
								value: 'None',
							},
						]}
						orientation={Orientation.VERTICAL}
					/>
				</Container>
			</ClayLayout.Col>
		</ClayLayout.Row>
	);
};

export default CaseResult;
