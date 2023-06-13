import {closest, getClosestAssetElement} from '../utils/assets.js';
import {onReady} from '../utils/events.js';

const applicationId = 'Document';

/**
 * Returns analytics payload with Document information.
 * @param {object} documentElement The document DOM element
 * @return {object} The payload with document information
 */
function getDocumentPayload(documentElement) {
	const {dataset} = documentElement;

	let payload = {
		fileEntryId: dataset.analyticsAssetId,
		fileEntryVersion: dataset.analyticsAssetVersion,
	};

	if (dataset.analyticsAssetTitle) {
		payload = {...payload, title: dataset.analyticsAssetTitle};
	}

	return payload;
}

/**
 * Wether a Document is trackable or not.
 * @param {object} element The Document DOM element
 * @return {boolean} True if the element is trackable.
 */
function isTrackableDocument(documentElement) {
	return documentElement && 'analyticsAssetId' in documentElement.dataset;
}

/**
 * Sends information when user clicks on a Document.
 * @param {object} The Analytics client instance
 */
function trackDocumentDownloaded(analytics) {
	const onClick = ({target}) => {
		const actionElement = closest(
			target,
			'[data-analytics-asset-action="download"]'
		);

		const documentElement = getClosestAssetElement(target, 'document');

		if (actionElement && isTrackableDocument(documentElement)) {
			analytics.send(
				'documentDownloaded',
				applicationId,
				getDocumentPayload(documentElement)
			);
		}
	};

	document.addEventListener('click', onClick);

	return () => document.removeEventListener('click', onClick);
}

/**
 * Sends information when user scrolls on a Document.
 * @param {object} The Analytics client instance
 */
function trackDocumentPreviewed(analytics) {
	const stopTrackingOnReady = onReady(() => {
		Array.prototype.slice
			.call(
				document.querySelectorAll(
					'[data-analytics-asset-action="preview"]'
				)
			)
			.filter(element => isTrackableDocument(element))
			.forEach(element => {
				const payload = getDocumentPayload(element);

				analytics.send('documentPreviewed', applicationId, payload);
			});
	});
	return () => stopTrackingOnReady();
}

/**
 * Plugin function that registers listeners for Document events
 * @param {object} analytics The Analytics client
 */
function documents(analytics) {
	const stopTrackingDocumentDownloaded = trackDocumentDownloaded(analytics);
	const stopTrackingDocumentPreviewed = trackDocumentPreviewed(analytics);

	return () => {
		stopTrackingDocumentDownloaded();
		stopTrackingDocumentPreviewed();
	};
}

export {documents};
export default documents;