window.Liferay = {
	Language: {
		get: key => key
	},
	ThemeDisplay: {
		getLanguageId: () => 'en_US'
	}
};

window.AlloyEditor = {
	Selections: [
		{
			buttons: ['linkEdit'],
			name: 'link'
		},
		{
			buttons: ['styles', 'bold', 'italic', 'underline', 'link', 'twitter'],
			name: 'text'
		}
	]
};

window.AUI = () => (
	{
		use: (_, callback) => callback(
			{
				LiferayAlloyEditor: () => (
					{
						render: () => (
							{
								getHTML: () => 'test',
								getNativeEditor: () => (
									{
										on: () => true,
										setData: () => false
									}
								)
							}
						)
					}
				),
				one: () => (
					{
						innerHTML: () => {}
					}
				)
			}
		)
	}
);

window.themeDisplay = window.Liferay.ThemeDisplay;