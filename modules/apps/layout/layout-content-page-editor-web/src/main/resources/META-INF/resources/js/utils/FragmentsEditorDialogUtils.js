/**
 * Possible types that can be returned by the image selector
 */
const IMAGE_SELECTOR_RETURN_TYPES = {
	downloadUrl: 'com.liferay.item.selector.criteria.DownloadURLItemSelectorReturnType',
	fileEntryItemSelector: 'com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType',
	url: 'URL'
};

/**
 * @param {object} options
 * @param {function} options.callback
 * @param {string} options.assetBrowserURL
 * @param {string} options.modalTitle
 * @param {string} options.portletNamespace
 * @param {function} [options.destroyedCallback=null]
 */
function openAssetBrowser(
	{
		assetBrowserURL,
		callback,
		modalTitle,
		portletNamespace,
		destroyedCallback = null
	}
) {
	Liferay.Util.selectEntity(
		{
			dialog: {
				constrain: true,
				destroyOnHide: true,
				modal: true
			},
			eventName: `${portletNamespace}selectAsset`,
			title: modalTitle,
			uri: assetBrowserURL
		},
		event => {
			if (event.assetclassnameid) {
				callback(
					{
						classNameId: event.assetclassnameid,
						classPK: event.assetclasspk,
						title: event.assettitle
					}
				);
			}
			else if (destroyedCallback) {
				destroyedCallback();
			}
		}
	);
}

/**
 * @param {object} options
 * @param {function} options.callback
 * @param {string} options.imageSelectorURL
 * @param {string} options.portletNamespace
 * @param {function} [options.destroyedCallback=null]
 */
function openImageSelector(
	{
		callback,
		imageSelectorURL,
		portletNamespace,
		destroyedCallback = null
	}
) {
	AUI().use(
		'liferay-item-selector-dialog',
		A => {
			const itemSelector = new A.LiferayItemSelectorDialog(
				{
					eventName: `${portletNamespace}selectImage`,
					on: {
						selectedItemChange: event => {
							const selectedItem = event.newVal || {};

							const {returnType, value} = selectedItem;
							let selectedImageURL = '';

							if (returnType === IMAGE_SELECTOR_RETURN_TYPES.downloadUrl ||
								returnType === IMAGE_SELECTOR_RETURN_TYPES.url) {

								selectedImageURL = value;
							}

							if (returnType === IMAGE_SELECTOR_RETURN_TYPES.fileEntryItemSelector) {
								selectedImageURL = JSON.parse(value).url;
							}

							if (selectedImageURL) {
								callback(selectedImageURL);
							}
						},

						visibleChange: event => {
							if ((event.newVal === false) &&
								destroyedCallback) {

								destroyedCallback();
							}
						}
					},
					title: Liferay.Language.get('select'),
					url: imageSelectorURL
				}
			);

			itemSelector.open();
		}
	);
}

export {openAssetBrowser, openImageSelector};