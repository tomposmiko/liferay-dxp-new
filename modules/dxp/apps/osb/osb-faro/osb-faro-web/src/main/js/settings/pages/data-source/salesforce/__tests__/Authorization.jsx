import * as data from 'test/data';
import mockStore from 'test/mock-store';
import React from 'react';
import {DataSource, User} from 'shared/util/records';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {SalesforceAuthorization} from '../Authorization';
import {StaticRouter} from 'react-router-dom';
import {UserRoleNames} from 'shared/util/constants';

jest.unmock('react-dom');

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<SalesforceAuthorization
				currentUser={data.getImmutableMock(User, data.mockUser)}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource
				)}
				groupId='23'
				id='test'
				{...props}
			/>
		</StaticRouter>
	</Provider>
);

describe('SalesforceAuthorization', () => {
	it('should render', () => {
		const {container, queryByText} = render(<DefaultComponent />);

		expect(queryByText('Delete Data Source')).toBeTruthy();
		expect(queryByText('Edit')).toBeTruthy();
		expect(container).toMatchSnapshot();
	});

	it('should render as read-only if the user is not authorized to make changes', () => {
		const {queryByText} = render(
			<DefaultComponent
				currentUser={data.getImmutableMock(User, data.mockUser, '24', {
					roleName: UserRoleNames.Member
				})}
			/>
		);

		expect(queryByText('Delete Data Source')).toBeNull();
		expect(queryByText('Edit')).toBeNull();
	});
});
