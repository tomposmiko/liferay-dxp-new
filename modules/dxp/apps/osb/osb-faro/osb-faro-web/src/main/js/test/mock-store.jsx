import * as data from 'test/data';
import middleware from 'shared/store/configure-middleware';
import React from 'react';
import reducers from 'shared/reducers';
import {
	Account,
	DataSource,
	DistributionTab,
	Individual,
	Project,
	RemoteData,
	Segment,
	User
} from 'shared/util/records';
import {createStore} from 'redux';
import {fromJS, List} from 'immutable';
import {ProjectStates, UserRoleNames} from 'shared/util/constants';
import {Provider} from 'react-redux';
import {shallow} from 'enzyme';

export function toRD(data) {
	return new RemoteData({data, loading: false});
}

export const mockStoreData = fromJS({
	accounts: {
		test: toRD(new Account(fromJS(data.mockAccount('test'))))
	},
	cards: {
		test: {
			card1: {},
			card2: {}
		}
	},
	cardTemplates: {
		card1: data.mockCardTemplate('card1'),
		card2: data.mockCardTemplate('card2')
	},
	currentUser: new RemoteData({data: '23', loading: false}),
	dataSources: {
		23: toRD(new DataSource(fromJS(data.mockLiferayDataSource(23)))),
		24: toRD(new DataSource(fromJS(data.mockCSVDataSource(24)))),
		'25-25': toRD(
			new DataSource(fromJS(data.mockLiferayDataSource(25, {id: null})))
		),
		26: toRD(new DataSource(fromJS(data.mockCSVDataSource(26)))),
		'26-26': toRD('26'),

		27: toRD(new DataSource(fromJS(data.mockSalesforceDataSource(27))))
	},
	distributions: {
		individualsDashboard: toRD({items: [], total: 0})
	},
	individuals: {
		test: toRD(new Individual(fromJS(data.mockIndividual('test'))))
	},
	layouts: {
		1: {
			contactsCardData: {1: {id: 'cardData1'}, 2: {id: 'cardData2'}},
			contactsLayoutTemplate: {
				contactsCardTemplatesList: [['card1']],
				headerContactsCardTemplates: ['card2'],
				id: 'layout1',
				name: 'layoutTemplate'
			},
			faroEntity: 'test'
		}
	},
	preferences: {
		group: {
			distributionCardTabs: {
				123: toRD(new List([])),
				individualsDashboard: toRD(
					new List([
						new DistributionTab({
							context: 'demographics',
							id: 'streets',
							numberOfBins: 0,
							propertyId: '379649776998500415',
							propertyType: 'Text',
							title: 'Streets'
						})
					])
				)
			}
		},
		user: {
			defaultChannelId: toRD('321320')
		}
	},
	projects: {
		23: toRD(
			new Project(
				data.mockProject('23', {
					recommendationsEnabled: true,
					stateStartDate: 1531263666366
				})
			)
		),
		24: toRD(
			new Project(data.mockProject('24', {state: ProjectStates.NotReady}))
		),
		25: toRD(
			new Project(
				data.mockProject('25', {state: ProjectStates.Maintenance})
			)
		),
		26: toRD(
			new Project(
				data.mockProject('26', {state: ProjectStates.Unavailable})
			)
		),
		27: toRD(
			new Project(
				data.mockProject('27', {state: ProjectStates.Scheduled})
			)
		),
		28: toRD(
			new Project(
				data.mockProject('28', {state: ProjectStates.Activating})
			)
		),
		29: toRD(
			new Project(
				data.mockProject('29', {state: ProjectStates.Deactivated})
			)
		),
		corpProjectUuid23: toRD(new Project(fromJS(data.mockProject('23')))),
		corpProjectUuid26: toRD(new Project(fromJS(data.mockProject('26'))))
	},
	segments: {
		test: toRD(new Segment(data.mockSegment('test')))
	},
	users: {
		23: toRD(new User(data.mockUser('23', {groupId: '23'}))),
		24: toRD(
			new User(
				data.mockUser('24', {
					groupId: '23',
					roleName: UserRoleNames.Member
				})
			)
		),
		26: toRD(new User(data.mockUser('26', {groupId: '26'})))
	}
});

export default function mockStore(
	initialState = mockStoreData,
	reducer = reducers
) {
	return createStore(reducer, initialState, middleware);
}

export function renderWithStore(Component, props, mapStore = s => s) {
	return shallow(
		<Provider store={mockStore(mapStore(mockStoreData))}>
			<Component key='child' {...props} />
		</Provider>
	);
}
