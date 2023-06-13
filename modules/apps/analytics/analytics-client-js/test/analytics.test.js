import AnalyticsClient from '../src/analytics';
import {assert, expect} from 'chai';

let Analytics;
let EVENT_ID = 0;

const ANALYTICS_IDENTITY = {email: 'foo@bar.com'};
const ANALYTICS_KEY = 'ANALYTICS_KEY';
const FLUSH_INTERVAL = 100;
const LOCAL_USER_ID = 'LOCAL_USER_ID';
const MOCKED_REQUEST_DURATION = 5000;
const SERVICE_USER_ID = 'SERVICE_USER_ID';

// Local Storage keys
const STORAGE_KEY_EVENTS = 'lcs_client_batch';
const STORAGE_KEY_USER_ID = 'lcs_client_user_id';
const STORAGE_KEY_IDENTITY = 'lcs_client_identity';

const fetchMock = window.fetchMock;

/**
 * Sends dummy events to test the Analytics API
 * @param {number} eventsNumber Number of events to send
 */
function sendDummyEvents(client, eventsNumber = 5) {
	for (let i = 0; i < eventsNumber; i++) {
		const applicationId = 'test';
		const eventId = EVENT_ID++;
		const properties = {
			a: 1,
			b: 2,
			c: 3,
		};

		client.send(eventId, applicationId, properties);
	}
}

describe('Analytics Client', () => {
	afterEach(() => {
		Analytics.reset();
		Analytics.dispose();
		fetchMock.restore();
	});

	beforeEach(
		() => {
			fetchMock.mock(/asahlfr/ig, () => Promise.resolve(200));

			Analytics = AnalyticsClient.create();

			localStorage.removeItem(STORAGE_KEY_EVENTS);
			localStorage.removeItem(STORAGE_KEY_USER_ID);
		}
	);

	it('should be exposed in the global scope', () => {
		expect(Analytics).to.be.a('object');
	});

	it('expose a "create" instantiation method', () => {
		Analytics.create.should.be.a('function');
	});

	it('should accept a configuration object', () => {
		const config = {a: 1, b: 2, c: 3};

		Analytics.reset();
		Analytics.dispose();

		Analytics = Analytics.create(config);
		Analytics.config.should.deep.equal(config);
	});

	describe('.flush', () => {
		it('should be exposed as an Analytics static method', () => {
			Analytics.flush.should.be.a('function');
		});

		it('should prevent overlapping requests', (done) => {
			fetchMock.mock(/identity/ig, () => Promise.resolve(200));

			let fetchCalled = 0;

			fetchMock.mock(
				/send-analytics-events$/,
				function() {
					fetchCalled += 1;

					return new Promise(
						resolve => {
							setTimeout(() => resolve({}), MOCKED_REQUEST_DURATION);
						}
					);
				}
			);

			Analytics.reset();
			Analytics.dispose();

			Analytics = AnalyticsClient.create(
				{
					flushInterval: FLUSH_INTERVAL,
				}
			);

			const spy = sinon.spy(Analytics, 'flush');

			sendDummyEvents(Analytics, 10);

			setTimeout(
				() => {
					// Flush must be called at least 3 times

					expect(spy.callCount).to.be.at.least(2);

					// Without sending another Fetch Request

					expect(fetchCalled).to.equal(1);

					Analytics.flush.restore();

					done();
				},
				FLUSH_INTERVAL * 3
			);
		});

		it('should regenerate the stored identity if the identity changed' , () => {
			fetchMock.mock(/identity/ig, () => Promise.resolve(200));

			Analytics.reset();
			Analytics.dispose();

			Analytics = AnalyticsClient.create(
				{
					analyticsKey: ANALYTICS_KEY,
				}
			);

			Analytics.setIdentity(ANALYTICS_IDENTITY);

			const previousIdentityHash = localStorage.getItem(STORAGE_KEY_IDENTITY);

			return Analytics.setIdentity({
				email: 'john@liferay.com',
				name: 'John'
			}).then(() => {
				const currentIdentityHash = localStorage.getItem(STORAGE_KEY_IDENTITY);

				expect(currentIdentityHash).not.to.equal(previousIdentityHash);
			});
		});

		it('should report identity changes to the Identity Service', () => {
			fetchMock.mock('*', () => Promise.resolve(200));

			Analytics.reset();
			Analytics.dispose();

			Analytics = AnalyticsClient.create(
				{
					analyticsKey: ANALYTICS_KEY,
				}
			);

			let identityCalled = 0;

			return Analytics.setIdentity(ANALYTICS_IDENTITY)
			.then(() => {
				fetchMock.restore();
				fetchMock.mock(/asahlfr/ig, () => Promise.resolve(200));
				fetchMock.mock(
					/identity/ig,
					function(url) {
						identityCalled += 1;
						return '';
					}
				);
			})
			.then(() => Analytics.setIdentity({email: 'john@liferay.com'}))
			.then(() => expect(identityCalled).to.equal(1));
		});

		it('should not request the Identity Service when identity hasn\'t changed', () => {
			fetchMock.mock(/identity/ig, () => Promise.resolve(200));

			Analytics.reset();
			Analytics.dispose();

			Analytics = AnalyticsClient.create(
				{
					analyticsKey: ANALYTICS_KEY,
				}
			);

			let identityCalled = 0;

			return Analytics.setIdentity(ANALYTICS_IDENTITY)
			.then(() => {
				fetchMock.restore();
				fetchMock.mock(/asahlfr/ig, () => Promise.resolve(200));
				fetchMock.mock(
					/send-identity-context/,
					function(url) {
						identityCalled += 1;
						return '';
					}
				)
			})
			.then(() => Analytics.setIdentity(ANALYTICS_IDENTITY))
			.then(() => expect(identityCalled).to.equal(0));
		});

		it('should only clear the persisted events when done', () => {
			Analytics.reset();
			Analytics.dispose();

			Analytics = AnalyticsClient.create(
				{
					flushInterval: FLUSH_INTERVAL * 10
				}
			);

			fetchMock.mock(/send-identity-context$/, () => Promise.resolve({}));

			fetchMock.mock(
				/send-analytics-events$/,
				function() {
					// Send events while flush is in progress
					sendDummyEvents(Analytics, 7);

					return new Promise(
						resolve => {
							setTimeout(() => resolve({}), 300);
						}
					);
				}
			);

			sendDummyEvents(Analytics, 5);

			return Analytics.flush().then(() => {
				const events = Analytics.events;

				events.should.have.lengthOf(7);
			});
		});
	});

	describe('.send', () => {
		it('should be exposed as an Analytics static method', () => {
			Analytics.send.should.be.a('function');
		});

		it('should add the given event to the event queue', () => {
			const eventId = 'eventId';
			const applicationId = 'applicationId';
			const properties = {a: 1, b: 2, c: 3};

			Analytics.send(eventId, applicationId, properties);

			const events = Analytics.events;

			events.should.have.lengthOf(1);

			events[0].should.deep.include({
				eventId,
				applicationId,
				properties,
			});
		});

		it('should persist the given events to the LocalStorage', () => {
			const eventsNumber = 5;

			sendDummyEvents(Analytics, eventsNumber);

			const events = JSON.parse(localStorage.getItem(STORAGE_KEY_EVENTS));

			events.should.have.lengthOf.at.least(eventsNumber);
		});
	});
});