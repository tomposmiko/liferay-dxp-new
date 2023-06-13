import AnalyticsClient from '../../src/analytics';
import {assert, expect} from 'chai';

const applicationId = 'Form';

let Analytics;

describe('Forms Plugin', () => {
	afterEach(() => {
		Analytics.reset();
		Analytics.dispose();
	});

	beforeEach(() => {
		// Force attaching DOM Content Loaded event
		Object.defineProperty(document, 'readyState', {
			value: 'loading',
			writable: false
		});

		fetchMock.mock('*', () => 200);
		Analytics = AnalyticsClient.create();
	});

	describe('formViewed event', () => {
		it('should be fired for every form on the page', () => {
			const formWithAssetId = document.createElement('form');
			formWithAssetId.dataset.analyticsAssetId = 'assetId';
			formWithAssetId.dataset.analyticsAssetTitle = 'Form Title 1';
			document.body.appendChild(formWithAssetId);

			const formWithFormId = document.createElement('form');
			formWithFormId.dataset.analyticsFormId = 'formId';
			formWithFormId.dataset.analyticsAssetTitle = 'Form Title 2';
			document.body.appendChild(formWithFormId);

			const domContentLoaded = new Event('DOMContentLoaded');
			document.dispatchEvent(domContentLoaded);

			const events = Analytics.events.filter(
				({eventId, properties}) => eventId === 'formViewed'
			);

			expect(events.length).to.equal(2);

			events[1].should.deep.include({
				applicationId,
				eventId: 'formViewed'
			});
			expect(events[1].properties.formId).to.equal('formId');

			events[0].should.deep.include({
				applicationId,
				eventId: 'formViewed'
			});
			expect(events[0].properties.formId).to.equal('assetId');

			document.body.removeChild(formWithAssetId);
			document.body.removeChild(formWithFormId);
		});
	});

	describe('formSubmitted event', () => {
		it('should be fired when a form is submitted', () => {
			const form = document.createElement('form');
			form.dataset.analyticsAssetId = 'formId';
			form.dataset.analyticsAssetTitle = 'Form Title';
			document.body.appendChild(form);
			form.addEventListener('submit', event => event.preventDefault());

			const event = new Event('submit', {
				cancelable: true
			});
			form.dispatchEvent(event);

			const events = Analytics.events.filter(
				({eventId}) => eventId === 'formSubmitted'
			);

			expect(events.length).to.equal(1);

			events[0].should.deep.include({
				applicationId,
				eventId: 'formSubmitted',
				properties: {
					formId: 'formId'
				}
			});
		});
	});

	describe('fieldFocused event', () => {
		it('should be fired whenever a field is focused', () => {
			const form = document.createElement('form');
			form.dataset.analyticsAssetId = 'formId';
			form.dataset.analyticsAssetTitle = 'Form Title';
			document.body.appendChild(form);
			const field = document.createElement('input');
			field.name = 'myField';
			field.type = 'text';
			form.appendChild(field);

			field.dispatchEvent(new Event('focus'));

			const events = Analytics.events.filter(
				({eventId}) => eventId === 'fieldFocused'
			);

			expect(events.length).to.equal(1);

			events[0].should.deep.include({
				applicationId,
				eventId: 'fieldFocused',
				properties: {
					formId: 'formId',
					fieldName: 'myField'
				}
			});
		});
	});

	describe('fieldBlurred event', () => {
		it('should be fired whenever a field is blurred', (done) => {
			const form = document.createElement('form');
			form.dataset.analyticsAssetId = 'formId';
			form.dataset.analyticsAssetTitle = 'Form Title';
			document.body.appendChild(form);
			const field = document.createElement('input');
			field.name = 'myField';
			field.type = 'text';
			form.appendChild(field);

			field.dispatchEvent(new Event('focus'));

			setTimeout(() => {
				field.dispatchEvent(new Event('blur'));

				const events = Analytics.events.filter(
					({eventId}) => eventId === 'fieldBlurred'
				);

				expect(events.length).to.equal(1);

				events[0].applicationId.should.equal(applicationId);
				events[0].eventId.should.equal('fieldBlurred');
				events[0].properties.formId.should.equal('formId');
				events[0].properties.fieldName.should.equal('myField');
				events[0].properties.focusDuration.should.be.at.least(1500);

				done();
			}, 1500);
		});
	});
});