function ${namespace}sharing(sharingURL, title) {
	Liferay.Util.openWindow(
		{
			dialog: {
				centered: true,
				constrain: true,
				cssClass: 'sharing-dialog',
				destroyOnHide: true,
				modal: true,
				width: 800
			},
			id: '${sharingDialogId}',
			title: title,
			uri: sharingURL
		}
	);
}