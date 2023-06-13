Liferay.Loader.require.apply(
	Liferay.Loader,
	$MODULES.concat(
		[
			function(Component) {
				var context = $CONTEXT;

				var destroyConfig = {
					destroyOnNavigate: true,
					portletId: context.portletId
				};

				if (context.defaultEventHandler) {
					Liferay.componentReady(
						context.defaultEventHandler
					).then(
						function(defaultEventHandler) {
							context.defaultEventHandler = defaultEventHandler;

							if ($WRAPPER) {
								Liferay.component(
									'$ID',
									new Component.default(context, '#$ID'),
									destroyConfig
								);
							}
							else {
								Liferay.component(
									'$ID',
									new Component.default(context),
									destroyConfig
								);
							}
						}
					);
				}
				else {
					if ($WRAPPER) {
						Liferay.component(
							'$ID',
							new Component.default(context, '#$ID'),
							destroyConfig
						);
					}
					else {
						Liferay.component(
							'$ID',
							new Component.default(context),
							destroyConfig
						);
					}
				}
			},
			function(error) {
				console.error('Unable to load ' + $MODULES);

				Liferay.fire(
					'soyComponentLoadingError',
					{
						error: error,
						modules: $MODULES
					}
				);
			}
		]
	)
);