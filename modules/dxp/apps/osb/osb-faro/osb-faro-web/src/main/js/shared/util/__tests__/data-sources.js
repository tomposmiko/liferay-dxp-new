import * as API from 'shared/api';
import * as data from 'test/data';
import Promise from 'metal-promise';
import {
	CredentialTypes,
	DataSourceStates,
	DataSourceStatuses
} from 'shared/util/constants';
import {DataSource} from 'shared/util/records';
import {
	dataSourceRedirectFn,
	getDataSourceDisplayObject,
	getIdsFromConfiguration,
	getServiceAlertConfig,
	hasLegacyDXPConnection,
	isDataSourceValid,
	STATUS_DISPLAY,
	validAnalyticsConfig,
	validateUniqueName,
	validContactsConfig
} from '../data-sources';
import {fromJS} from 'immutable';
import {noop, range} from 'lodash';
import {Routes, toRoute} from 'shared/util/router';

function getMockLiferayDataSource(id, config) {
	return data.getImmutableMock(
		DataSource,
		data.mockLiferayDataSource,
		id,
		config
	);
}

describe('data-sources', () => {
	describe('dataSourceRedirectFn', () => {
		it('should return the SETTINGS_DATA_SOURCE route if the data source state is NOT VALID', () => {
			const groupId = '23';
			const id = '123';

			const result = dataSourceRedirectFn({
				dataSource: getMockLiferayDataSource(id, {
					state: DataSourceStates.UrlInvalid
				}),
				groupId
			});

			expect(result).toBe(
				toRoute(Routes.SETTINGS_DATA_SOURCE, {groupId, id})
			);
		});

		it('should return null if the data source state is VALID', () => {
			const groupId = '23';
			const id = '123';

			const result = dataSourceRedirectFn({
				dataSource: getMockLiferayDataSource(id, {
					state: DataSourceStates.Ready
				}),
				groupId
			});

			expect(result).toBe(null);
		});
	});

	describe('isDataSourceValid', () => {
		it.each`
			state                                     | isValid
			${DataSourceStates.CredentialsInvalid}    | ${false}
			${DataSourceStates.CredentialsValid}      | ${true}
			${DataSourceStates.LiferayVersionInvalid} | ${false}
			${DataSourceStates.Ready}                 | ${true}
			${DataSourceStates.UndefinedError}        | ${false}
			${DataSourceStates.UrlInvalid}            | ${false}
			${null}                                   | ${false}
		`(
			'should return whether the datasource state is considered valid',
			({isValid, state}) => {
				const result = isDataSourceValid(state);

				expect(result).toEqual(isValid);
			}
		);
	});

	describe('getDataSourceDisplayObject', () => {
		it('should return the active state display object if credentials are valid, at least one configuration is valid, and active is true', () => {
			const result = getDataSourceDisplayObject(
				getMockLiferayDataSource(1, {
					provider: {
						contactsConfiguration: {enableAllContacts: true}
					},
					state: DataSourceStates.CredentialsValid
				})
			);

			expect(result).toEqual(STATUS_DISPLAY.active);
		});

		it('should return the active display object if credentials are valid, and only analytics are configured', () => {
			const result = getDataSourceDisplayObject(
				getMockLiferayDataSource(1, {
					provider: {
						analyticsConfiguration: {sites: [{id: '1'}]}
					},
					state: DataSourceStates.CredentialsValid,
					status: DataSourceStatuses.Inactive
				})
			);

			expect(result).toEqual(STATUS_DISPLAY.active);
		});

		it('should return the authenticated state display object if credentials are valid but active is false', () => {
			const result = getDataSourceDisplayObject(
				getMockLiferayDataSource(1, {
					state: DataSourceStates.CredentialsValid,
					status: DataSourceStatuses.Inactive
				})
			);

			expect(result).toEqual(
				STATUS_DISPLAY[DataSourceStates.CredentialsValid]
			);
		});

		it('should return the invalid credentials state display object if credentials are invalid', () => {
			const result = getDataSourceDisplayObject(
				getMockLiferayDataSource(1, {
					state: DataSourceStates.CredentialsInvalid
				})
			);

			expect(result).toEqual(
				STATUS_DISPLAY[DataSourceStates.CredentialsInvalid]
			);
		});

		it('should return the invalid credentials state display object if the state is ANALYTICS_CLIENT_CONFIGURATION_FAILURE', () => {
			const result = getDataSourceDisplayObject(
				getMockLiferayDataSource(1, {
					state: DataSourceStates.AnalyticsClientConfigurationFailure
				})
			);

			expect(result).toEqual(
				STATUS_DISPLAY[DataSourceStates.CredentialsInvalid]
			);
		});

		it('should return the "in-progress deletion" state display object if the data source state is in deletion', () => {
			const result = getDataSourceDisplayObject(
				getMockLiferayDataSource(1, {
					state: DataSourceStates.InProgressDeleting
				})
			);

			expect(result).toEqual(
				STATUS_DISPLAY[DataSourceStates.InProgressDeleting]
			);
		});

		it('should return the not configured state display object if the state does match any of the state types', () => {
			const result = getDataSourceDisplayObject(
				getMockLiferayDataSource(1, {
					state: undefined,
					status: DataSourceStatuses.Inactive
				})
			);

			expect(result).toEqual(STATUS_DISPLAY.default);
		});

		it('should return the undefined error state display object if the state is UNDEFINED_ERROR', () => {
			const result = getDataSourceDisplayObject(
				getMockLiferayDataSource(1, {
					state: DataSourceStates.UndefinedError,
					status: DataSourceStatuses.Inactive
				})
			);

			expect(result).toMatchSnapshot();
		});

		it('should return the disconnected state display object if the state is DISCONNECTED', () => {
			const result = getDataSourceDisplayObject(
				getMockLiferayDataSource(1, {
					state: DataSourceStates.Disconnected,
					status: DataSourceStatuses.Inactive
				})
			);

			expect(result).toMatchSnapshot();
		});

		xit('should return the "action needed" state display object if the data source state is in oAuth1 Authentication', () => {
			const result = getDataSourceDisplayObject(
				getMockLiferayDataSource(1, {
					credentials: {type: CredentialTypes.OAuth1}
				}),
				true
			);

			expect(result).toEqual(
				STATUS_DISPLAY[DataSourceStates.ActionNeeded]
			);
		});

		xit('should return the "action needed" state display object if the data source state is in oAuth2 Authentication', () => {
			const result = getDataSourceDisplayObject(
				getMockLiferayDataSource(1, {
					credentials: {type: CredentialTypes.OAuth2}
				}),
				true
			);

			expect(result).toEqual(
				STATUS_DISPLAY[DataSourceStates.ActionNeeded]
			);
		});
	});

	describe('getIdsFromConfiguration', () => {
		it('should return an array of numerical ids from a Map.<List.<Map>>', () => {
			const expectedArray = range(5);

			const testKey = 'testKey';

			const mockMap = fromJS({
				[testKey]: expectedArray.map(i => ({id: i}))
			});

			expect(getIdsFromConfiguration(mockMap, testKey)).toEqual(
				expectedArray
			);
		});
	});

	describe('getServiceAlertConfig', () => {
		it('should return a service permission related alert props', () => {
			expect(getServiceAlertConfig(403)).toMatchSnapshot();
		});

		it('should return a service unresponsive related alert props', () => {
			expect(getServiceAlertConfig(404)).toMatchSnapshot();
		});
	});

	describe('hasLegacyDXPConnection', () => {
		it('should return true if the DataSource has a credential type other than "token"', () => {
			expect(
				hasLegacyDXPConnection(
					getMockLiferayDataSource(0, {
						credentials: {type: CredentialTypes.OAuth1}
					})
				)
			).toBeTrue();
			expect(
				hasLegacyDXPConnection(
					getMockLiferayDataSource(0, {
						credentials: {type: CredentialTypes.OAuth2}
					})
				)
			).toBeTrue();
		});

		it('should return false if the DataSource type is not Liferay', () => {
			expect(
				hasLegacyDXPConnection(
					data.getImmutableMock(DataSource, data.mockCSVDataSource)
				)
			).toBeFalse();

			expect(
				hasLegacyDXPConnection(
					data.getImmutableMock(
						DataSource,
						data.mockSalesforceDataSource
					)
				)
			).toBeFalse();
		});

		it('should return false if the Liferay DataSource has a credential type of "token"', () => {
			expect(
				hasLegacyDXPConnection(
					getMockLiferayDataSource(0, {
						credentials: {type: CredentialTypes.Token}
					})
				)
			).toBeFalse();
		});
	});
	describe('validateUniqueName', () => {
		it('should return a success assertion if the data source name does NOT already exist', () => {
			expect.assertions(1);

			const uniqueDataSourceName = 'barbaz';

			API.dataSource.search.mockReturnValueOnce(
				Promise.resolve(data.mockSearch(noop, 0))
			);

			return validateUniqueName({
				groupId: '23',
				value: uniqueDataSourceName
			}).then(result => {
				expect(result).toBe('');
			});
		});

		it('should return a failure assertion if the data source name already exists', () => {
			expect.assertions(1);

			const existingDataSourceName = 'foo';

			API.dataSource.search.mockReturnValueOnce(
				Promise.resolve(
					data.mockSearch(data.mockLiferayDataSource, 1, [
						{
							name: existingDataSourceName
						}
					])
				)
			);

			return expect(
				validateUniqueName({
					groupId: '23',
					value: `${existingDataSourceName}1`
				})
			).rejects.toMatchSnapshot();
		});
	});

	describe('validAnalyticsConfig', () => {
		it('should return false for csv data source types since they only have contacts', () => {
			const mockCSVDataSource = data.getImmutableMock(
				DataSource,
				data.mockCSVDataSource,
				1
			);

			expect(validAnalyticsConfig(mockCSVDataSource)).toBe(false);
		});

		it('should return be able to determine if a liferay data source has a valid analyticsConfiguration', () => {
			const withValidAnalytics = getMockLiferayDataSource(1, {
				provider: {
					analyticsConfiguration: {sites: [{id: '1'}]}
				}
			});

			const withInvalidAnalytics = getMockLiferayDataSource(1);

			expect(validAnalyticsConfig(withValidAnalytics)).toBe(true);

			expect(validAnalyticsConfig(withInvalidAnalytics)).toBe(false);
		});

		it('should return be able to determine if a salesforce data source has a valid analyticsConfiguration', () => {
			const withInvalidAnalytics = data.getImmutableMock(
				DataSource,
				data.mockSalesforceDataSource,
				1
			);

			expect(validAnalyticsConfig(withInvalidAnalytics)).toBe(false);
		});
	});

	describe('validContactsConfig', () => {
		it('should return true for csv types if the status is ACTIVE', () => {
			const mockCSVDataSource = data.getImmutableMock(
				DataSource,
				data.mockCSVDataSource,
				1
			);

			expect(validContactsConfig(mockCSVDataSource)).toBe(true);
		});

		it('should return be able to determine if a liferay data source has a valid contactsConfiguration', () => {
			const withValidContacts = getMockLiferayDataSource(1, {
				provider: {
					contactsConfiguration: {enableAllContacts: true}
				}
			});

			const withInvalidContacts = getMockLiferayDataSource(1);

			expect(validContactsConfig(withValidContacts)).toBe(true);

			expect(validContactsConfig(withInvalidContacts)).toBe(false);
		});

		it('should return be able to determine if a salesforce data source has a valid contactsConfiguration', () => {
			const withInvalidContacts = data.getImmutableMock(
				DataSource,
				data.mockSalesforceDataSource,
				1
			);

			expect(validContactsConfig(withInvalidContacts)).toBe(false);
		});
	});
});
