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
import {useOutletContext} from 'react-router-dom';

import Button from '../../../components/Button';
import Container from '../../../components/Layout/Container';
import ListView from '../../../components/ListView';
import SearchBuilder from '../../../core/SearchBuilder';
import i18n from '../../../i18n';
import {
	TestrayCase,
	TestrayProject,
	TestrayRequirementCase,
	testrayCaseRequirementsImpl,
} from '../../../services/rest';
import CaseRequirementLinkModal from './CaseRequirementLinkModal';
import useCaseRequirementActions from './useCaseRequirementActions';

const CaseRequirement = () => {
	const {
		testrayCase,
		testrayProject,
	}: {
		testrayCase: TestrayCase;
		testrayProject: TestrayProject;
	} = useOutletContext();

	const {formModal} = useCaseRequirementActions({caseId: testrayCase.id});

	return (
		<Container>
			<ListView
				forceRefetch={formModal.forceRefetch}
				managementToolbarProps={{
					buttons: (
						<Button
							displayType="secondary"
							onClick={() => formModal.open()}
							symbol="list-ul"
							toolbar
						>
							{i18n.translate('link-requirements')}
						</Button>
					),
					filterSchema: 'caseRequirements',
					title: i18n.translate('requirements'),
				}}
				resource={testrayCaseRequirementsImpl.resource}
				tableProps={{
					columns: [
						{
							clickable: true,
							key: 'key',
							render: (
								_,
								requirementCase: TestrayRequirementCase
							) => requirementCase.requirement?.key,
							value: i18n.translate('key'),
						},
						{
							key: 'linkTitle',
							render: (
								_,
								requirementCase: TestrayRequirementCase
							) => (
								<a
									href={requirementCase.requirement?.linkURL}
									rel="noopener noreferrer"
									target="_blank"
								>
									{requirementCase.requirement?.linkTitle}

									<ClayIcon
										className="ml-2"
										symbol="shortcut"
									/>
								</a>
							),
							value: i18n.translate('link'),
						},
						{
							key: 'team',
							render: (
								_,
								requirementCase: TestrayRequirementCase
							) =>
								requirementCase.requirement?.component?.team
									?.name,
							value: i18n.translate('team'),
						},
						{
							key: 'component',
							render: (
								_,
								requirementCase: TestrayRequirementCase
							) => requirementCase.requirement?.component?.name,
							value: i18n.translate('component'),
						},
						{
							key: 'components',
							render: (
								_,
								requirementCase: TestrayRequirementCase
							) => requirementCase.requirement?.components,
							value: i18n.translate('jira-components'),
						},
						{
							key: 'summary',
							render: (
								_,
								requirementCase: TestrayRequirementCase
							) => requirementCase.requirement?.summary,
							value: i18n.translate('summary'),
						},
						{
							key: 'description',
							render: (
								_,
								requirementCase: TestrayRequirementCase
							) => requirementCase.requirement?.description,
							value: i18n.translate('description'),
						},
					],
					navigateTo: ({requirement}: TestrayRequirementCase) =>
						`/project/${testrayProject.id}/requirements/${requirement?.id}`,
				}}
				transformData={(response) =>
					testrayCaseRequirementsImpl.transformDataFromList(response)
				}
				variables={{
					filter: SearchBuilder.eq('caseId', testrayCase.id),
				}}
			>
				{(response) => (
					<CaseRequirementLinkModal
						items={response?.items}
						modal={formModal}
					/>
				)}
			</ListView>
		</Container>
	);
};

export default CaseRequirement;
