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

import {cleanup, fireEvent, render} from '@testing-library/react';

import '@testing-library/jest-dom/extend-expect';
import {openSelectionModal} from 'frontend-js-web';
import React from 'react';

import ContributorsBuilder from '../../../../src/main/resources/META-INF/resources/js/components/criteria_builder/ContributorsBuilder.es';
import {CONJUNCTIONS} from '../../../../src/main/resources/META-INF/resources/js/utils/constants';

const initialContributors = [
	{
		conjunctionId: CONJUNCTIONS.AND,
		conjunctionInputId: 'conjunction_input_test_id',
		initialQuery: '',
		inputId: 'input_test_id',
		propertyKey: 'user',
	},
];

const propertyGroups = [
	{
		entityName: 'User',
		name: 'User',
		properties: [
			{
				label: 'Ancestor Organization IDs',
				name: 'ancestorOrganizationIds',
				type: 'string',
			},
			{
				label: 'Class PK',
				name: 'classPK',
				type: 'string',
			},
			{
				label: 'Company ID',
				name: 'companyId',
				type: 'string',
			},
			{
				label: 'Date Modified',
				name: 'dateModified',
				type: 'date',
			},
			{
				label: 'Email Address',
				name: 'emailAddress',
				type: 'string',
			},
			{
				label: 'First Name',
				name: 'firstName',
				type: 'string',
			},
			{
				label: 'Group ID',
				name: 'groupId',
				type: 'string',
			},
			{
				label: 'Group IDs',
				name: 'groupIds',
				type: 'string',
			},
			{
				label: 'Job Title',
				name: 'jobTitle',
				type: 'string',
			},
			{
				label: 'Last Name',
				name: 'lastName',
				type: 'string',
			},
			{
				label: 'Organization IDs',
				name: 'organizationIds',
				type: 'string',
			},
			{
				label: 'Role IDs',
				name: 'roleIds',
				type: 'string',
			},
			{
				label: 'Scope Group ID',
				name: 'scopeGroupId',
				type: 'string',
			},
			{
				label: 'Screen Name',
				name: 'screenName',
				type: 'string',
			},
			{
				label: 'Team IDs',
				name: 'teamIds',
				type: 'string',
			},
			{
				label: 'User Group IDs',
				name: 'userGroupIds',
				type: 'string',
			},
			{
				label: 'User ID',
				name: 'userId',
				type: 'string',
			},
			{
				label: 'User Name',
				name: 'userName',
				type: 'string',
			},
		],
		propertyKey: 'user',
	},
	{
		entityName: 'User Organization',
		name: 'User Organization',
		properties: [
			{
				label: 'Class PK',
				name: 'classPK',
				type: 'string',
			},
			{
				label: 'Company ID',
				name: 'companyId',
				type: 'string',
			},
			{
				label: 'Date Modified',
				name: 'dateModified',
				type: 'date',
			},
			{
				label: 'Name',
				name: 'name',
				type: 'string',
			},
			{
				label: 'Name Tree Path',
				name: 'nameTreePath',
				type: 'string',
			},
			{
				label: 'Organization ID',
				name: 'organizationId',
				type: 'string',
			},
			{
				label: 'Parent Organization ID',
				name: 'parentOrganizationId',
				type: 'string',
			},
			{
				label: 'Type',
				name: 'type',
				type: 'string',
			},
		],
		propertyKey: 'user-organization',
	},
];

jest.mock('frontend-js-web', () => ({
	...jest.requireActual('frontend-js-web'),
	openSelectionModal: jest.fn(),
}));

describe('ContributorsBuilder', () => {
	afterEach(cleanup);

	it('renders builder with sidebar', () => {
		const editing = true;

		const {asFragment} = render(
			<ContributorsBuilder
				editing={editing}
				emptyContributors={false}
				initialContributors={initialContributors}
				portletNamespace="segments_portlet"
				propertyGroups={propertyGroups}
			/>
		);

		expect(asFragment()).toMatchSnapshot('initialRenderEditing');
	});

	it('renders builder without sidebar', () => {
		const editing = false;

		const {asFragment} = render(
			<ContributorsBuilder
				editing={editing}
				emptyContributors={false}
				initialContributors={initialContributors}
				portletNamespace="segments_portlet"
				propertyGroups={propertyGroups}
			/>
		);

		expect(asFragment()).toMatchSnapshot('initialRenderNotEditing');
	});

	it('renders the Scope Card', () => {
		window.Liferay.FeatureFlags['LPS-166954'] = true;

		const scopeName = 'Liferay DXP';

		const editing = true;

		const {getByText} = render(
			<ContributorsBuilder
				editing={editing}
				emptyContributors={false}
				initialContributors={initialContributors}
				portletNamespace="segments_portlet"
				propertyGroups={propertyGroups}
				scopeName={scopeName}
				siteItemSelectorURL="http://example.org"
			/>
		);

		expect(getByText('scope')).toBeInTheDocument();
		expect(getByText(scopeName)).toBeInTheDocument();
	});

	it('renders the Scope Card with select site button enabled', () => {
		window.Liferay.FeatureFlags['LPS-166954'] = true;

		const editing = true;

		const {getByText} = render(
			<ContributorsBuilder
				editing={editing}
				emptyContributors={false}
				initialContributors={initialContributors}
				portletNamespace="segments_portlet"
				propertyGroups={propertyGroups}
				scopeName="Liferray DXP"
				siteItemSelectorURL="http://example.org"
			/>
		);

		expect(getByText('select')).toBeInTheDocument();
	});

	it('renders the Scope Card without the select site button if no site selector url is provided', () => {
		window.Liferay.FeatureFlags['LPS-166954'] = true;

		const editing = true;

		const {queryByText} = render(
			<ContributorsBuilder
				editing={editing}
				emptyContributors={false}
				initialContributors={initialContributors}
				portletNamespace="segments_portlet"
				propertyGroups={propertyGroups}
				scopeName="Liferray DXP"
			/>
		);

		expect(queryByText('select')).not.toBeInTheDocument();
	});

	it('renders a select site button that opens a selection modal', () => {
		window.Liferay.FeatureFlags['LPS-166954'] = true;

		const editing = true;

		const {getByText} = render(
			<ContributorsBuilder
				editing={editing}
				emptyContributors={false}
				initialContributors={initialContributors}
				portletNamespace="segments_portlet"
				propertyGroups={propertyGroups}
				scopeName="Liferray DXP"
				siteItemSelectorURL="http://example.org"
			/>
		);

		const selectButton = getByText('select');
		fireEvent.click(selectButton);

		expect(openSelectionModal).toHaveBeenCalledTimes(1);
	});

	it('renders the site readible name after selecting a site', () => {
		window.Liferay.FeatureFlags['LPS-166954'] = true;
		openSelectionModal.mockImplementation(({onSelect}) =>
			onSelect({
				groupdescriptivename: 'Site descriptive name',
				groupid: 1,
			})
		);

		const {getByText} = render(
			<ContributorsBuilder
				editing={true}
				emptyContributors={false}
				initialContributors={initialContributors}
				portletNamespace="segments_portlet"
				propertyGroups={propertyGroups}
				scopeName="Liferray DXP"
				siteItemSelectorURL="http://example.org"
			/>
		);

		const selectButton = getByText('select');
		fireEvent.click(selectButton);

		expect(getByText('Site descriptive name')).toBeInTheDocument();
	});

	it('renders the site group scope label if no readible name is present', () => {
		window.Liferay.FeatureFlags['LPS-166954'] = true;
		openSelectionModal.mockImplementation(({onSelect}) =>
			onSelect({
				groupid: 1,
				groupscopelabel: 'Group scope label',
			})
		);

		const {getByText} = render(
			<ContributorsBuilder
				editing={true}
				emptyContributors={false}
				initialContributors={initialContributors}
				portletNamespace="segments_portlet"
				propertyGroups={propertyGroups}
				scopeName="Liferray DXP"
				siteItemSelectorURL="http://example.org"
			/>
		);

		const selectButton = getByText('select');
		fireEvent.click(selectButton);

		expect(getByText('Group scope label')).toBeInTheDocument();
	});
});
