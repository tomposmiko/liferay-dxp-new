import DefaultEventHandler from 'frontend-js-web/liferay/DefaultEventHandler.es';
import {Config} from 'metal-state';

class ManagementToolbarDefaultEventHandler extends DefaultEventHandler {
	created() {
		Liferay.on(
			this.ns('selectAddMenuItem'),
			function(event) {
				location.href = Liferay.Util.addParams(this.ns('ddmStructureKey') + '=' + event.ddmStructureKey, this.addArticleURL);
			}
		);
	}

	deleteEntries() {
		let message = Liferay.Language.get('are-you-sure-you-want-to-delete-the-selected-entries');

		if (this.trashEnabled) {
			message = Liferay.Language.get('are-you-sure-you-want-to-move-the-selected-entries-to-the-recycle-bin');
		}

		if (confirm(message)) {
			Liferay.fire(
				this.ns('editEntry'),
				{
					action: this.trashEnabled ? 'moveEntriesToTrash' : 'deleteEntries'
				}
			);
		}
	}

	expireEntries() {
		Liferay.fire(
			this.ns('editEntry'),
			{
				action: 'expireEntries'
			}
		);
	}

	handleCreationMenuMoreButtonClicked() {
		Liferay.Util.openWindow(
			{
				dialog: {
					destroyOnHide: true,
					modal: true
				},
				id: this.ns('selectAddMenuItem'),
				title: Liferay.Language.get('more'),
				uri: this.openViewMoreStructuresURL
			}
		);
	}

	moveEntries() {
		Liferay.fire(
			this.ns('editEntry'),
			{
				action: 'moveEntries'
			}
		);
	}

	openDDMStructuresSelector() {
		let namespace = this.namespace;
		let uri = this.viewDDMStructureArticlesURL;

		Liferay.Util.selectEntity(
			{
				dialog: {
					constrain: true,
					modal: true
				},
				eventName: this.ns('selectDDMStructure'),
				title: Liferay.Language.get('structures'),
				uri: this.selectEntityURL
			},
			function(event) {
				location.href = Liferay.Util.addParams(namespace + 'ddmStructureKey=' + event.ddmstructurekey, uri);
			}
		);
	}
}

ManagementToolbarDefaultEventHandler.STATE = {
	addArticleURL: Config.string(),
	folderId: Config.string(),
	namespace: Config.string(),
	openViewMoreStructuresURL: Config.string(),
	selectEntityURL: Config.string(),
	trashEnabled: Config.bool(),
	viewDDMStructureArticlesURL: Config.string()
};

export default ManagementToolbarDefaultEventHandler;