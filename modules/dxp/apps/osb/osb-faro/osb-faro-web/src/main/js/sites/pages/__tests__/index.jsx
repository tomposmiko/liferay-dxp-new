import * as data from 'test/data';
import * as useDataSource from 'shared/hooks/useDataSource';
import BasePage from 'shared/components/base-page';
import client from 'shared/apollo/client';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {ChannelContext} from 'shared/context/channel';
import {cleanup, render} from '@testing-library/react';
import {Dashboard} from '../index';
import {MemoryRouter, Route} from 'react-router-dom';
import {mockChannelContext} from 'test/mock-channel-context';
import {mockEmptyState, mockSuccessState} from 'test/__mocks__/mock-objects';
import {OAuthUpgradeWarningContext} from 'shared/context/oAuthUpgradeWarning';
import {Provider} from 'react-redux';
import {Routes} from 'shared/util/router';
import {User} from 'shared/util/records';
import {UserRoleNames} from 'shared/util/constants';

jest.unmock('react-dom');

const ADMIN_USER = new User(
	data.mockUser(24, {roleName: UserRoleNames.Administrator})
);

const MEMBER_USER = new User(
	data.mockUser(23, {roleName: UserRoleNames.Member})
);

const MOCK_CONTEXT = {
	rangeKey: {defaultValue: '30'},
	router: {
		params: {
			channelId: '123',
			groupId: '2000'
		},
		query: {
			rangeKey: '30'
		}
	}
};

const mockUseDataSource = useDataSource;

const WARNING_STRIPE_CONTEXT_MOCK = {
	setShowOAuthUpgradeWarning: () => {},
	showOAuthUpgradeWarning: false
};

const WrappedComponent = props => (
	<ApolloProvider client={client}>
		<Provider store={mockStore()}>
			<OAuthUpgradeWarningContext.Provider
				value={WARNING_STRIPE_CONTEXT_MOCK}
			>
				<MemoryRouter initialEntries={['/workspace/2000/123/sites']}>
					<Route path={Routes.SITES}>
						<ChannelContext.Provider value={mockChannelContext()}>
							<BasePage.Context.Provider value={MOCK_CONTEXT}>
								<Dashboard
									currentUser={MEMBER_USER}
									router={MOCK_CONTEXT.router}
									{...props}
								/>
							</BasePage.Context.Provider>
						</ChannelContext.Provider>
					</Route>
				</MemoryRouter>
			</OAuthUpgradeWarningContext.Provider>
		</Provider>
	</ApolloProvider>
);

describe('Sites Dashboard Index', () => {
	afterEach(cleanup);
	mockUseDataSource.useDataSource = jest.fn(() => mockSuccessState);

	beforeAll(() => {
		delete window.location;
	});

	it('renders w/ "No Sites Connected" as title', () => {
		window.location = {
			pathname: '/workspace/2000/123/sites'
		};

		const CHANNEL_CONTEXT_MOCK = {
			channelDispatch: () => {},
			channels: [],
			selectedChannel: null
		};

		const WrappedComponentWithContext = props => (
			<ApolloProvider client={client}>
				<Provider store={mockStore()}>
					<OAuthUpgradeWarningContext.Provider
						value={WARNING_STRIPE_CONTEXT_MOCK}
					>
						<MemoryRouter
							initialEntries={['/workspace/2000/123/sites']}
						>
							<Route path={Routes.SITES}>
								<ChannelContext.Provider
									value={CHANNEL_CONTEXT_MOCK}
								>
									<BasePage.Context.Provider
										value={MOCK_CONTEXT}
									>
										<Dashboard
											currentUser={MEMBER_USER}
											router={MOCK_CONTEXT.router}
											{...props}
										/>
									</BasePage.Context.Provider>
								</ChannelContext.Provider>
							</Route>
						</MemoryRouter>
					</OAuthUpgradeWarningContext.Provider>
				</Provider>
			</ApolloProvider>
		);

		const {container} = render(<WrappedComponentWithContext />);

		expect(container.querySelector('.title-section')).toHaveTextContent(
			'No Sites Connected'
		);
	});

	xit('Should render a warning stripe if the user is admin and the showWarningStripe is true', () => {
		const SHOW_WARNING_STRIPE_CONTEXT_MOCK = {
			setShowOAuthUpgradeWarning: () => {},
			showOAuthUpgradeWarning: true
		};

		const WrappedComponentWithContext = props => (
			<ApolloProvider client={client}>
				<Provider store={mockStore()}>
					<OAuthUpgradeWarningContext.Provider
						value={SHOW_WARNING_STRIPE_CONTEXT_MOCK}
					>
						<MemoryRouter
							initialEntries={['/workspace/2000/123/sites']}
						>
							<Route path={Routes.SITES}>
								<ChannelContext.Provider
									value={mockChannelContext()}
								>
									<BasePage.Context.Provider
										value={MOCK_CONTEXT}
									>
										<Dashboard
											currentUser={ADMIN_USER}
											router={MOCK_CONTEXT.router}
											{...props}
										/>
									</BasePage.Context.Provider>
								</ChannelContext.Provider>
							</Route>
						</MemoryRouter>
					</OAuthUpgradeWarningContext.Provider>
				</Provider>
			</ApolloProvider>
		);

		const {container} = render(<WrappedComponentWithContext />);

		expect(container.querySelector('.btn-warning')).toHaveTextContent(
			'Go to Data Sources'
		);
	});
});

describe('sites with no Data Source', () => {
	it('should render EmptyState', () => {
		window.location = {
			pathname: '/workspace/2000/123/sites'
		};
		mockUseDataSource.useDataSource = jest.fn(() => mockEmptyState);

		const {getByText} = render(<WrappedComponent />);

		expect(
			getByText('No Sites Synced from Data Sources')
		).toBeInTheDocument();
		expect(
			getByText('Connect a data source with sites data.')
		).toBeInTheDocument();
		expect(
			getByText('Access our documentation to learn more.')
		).toBeInTheDocument();
	});
});
