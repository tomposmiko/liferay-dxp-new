import PortletBase from 'frontend-js-web/liferay/PortletBase.es';
import {Config} from 'metal-state';

class ElementsDefaultEventHandler extends PortletBase {
	handleItemClicked(event) {
		var itemData = event.data.item.data;

		if (itemData && itemData.action && this[itemData.action]) {
			this[itemData.action](itemData);
		}
	}

	compareVersions(itemData) {
		let namespace = this.namespace;

		Liferay.Util.selectEntity(
			{
				dialog: {
					constrain: true,
					destroyOnHide: true,
					modal: true
				},
				eventName: this.ns('selectVersionFm'),
				id: this.ns('compareVersions'),
				title: Liferay.Language.get('compare-versions'),
				uri: itemData.compareVersionsURL
			},
			function(event) {
				let uri = itemData.redirectURL;

				uri = Liferay.Util.addParams(namespace + 'sourceVersion=' + event.sourceversion, uri);
				uri = Liferay.Util.addParams(namespace + 'targetVersion=' + event.targetversion, uri);

				location.href = uri;
			}
		);
	}

	copyArticle(itemData) {
		this._send(itemData.copyArticleURL);
	}

	delete(itemData) {
		let message = 'are-you-sure-you-want-to-delete-this';

		if (this.trashEnabled) {
			message = 'are-you-sure-you-want-to-move-this-to-the-recycle-bin';
		}

		if (confirm(Liferay.Language.get(message))) {
			this._send(itemData.deleteURL);
		}
	}

	expireArticles(itemData) {
		this._send(itemData.expireURL);
	}

	permissions(itemData) {
		Liferay.Util.openWindow(
			{
				dialog: {
					destroyOnHide: true,
					modal: true
				},
				dialogIframe: {
					bodyCssClass: 'dialog-with-footer'
				},
				title: Liferay.Language.get('permissions'),
				uri: itemData.permissionsURL
			}
		);
	}

	preview(itemData) {
		Liferay.fire(
			'previewArticle',
			{
				title: itemData.title,
				uri: itemData.previewURL
			}
		);
	}

	publishToLive(itemData) {
		if (confirm(Liferay.Language.get('are-you-sure-you-want-to-publish-the-selected-web-content'))) {
			this._send(itemData.publishArticleURL);
		}
	}

	subscribeArticle(itemData) {
		this._send(itemData.subscribeArticleURL);
	}

	unsubscribeArticle(itemData) {
		this._send(itemData.unsubscribeArticleURL);
	}

	_send(url) {
		submitForm(document.hrefFm, url);
	}
}

ElementsDefaultEventHandler.STATE = {
	namespace: Config.string(),
	trashEnabled: Config.bool()
};

export default ElementsDefaultEventHandler;