AUI.add(
	'liferay-ddm-soy-template-util',
	function(A) {
		var AObject = A.Object;

		var SoyTemplateUtil = {
			getTemplateRenderer: function(templateNamespace) {
				var renderer = AObject.getValue(window, templateNamespace.split('.'));

				if (!renderer) {
					throw new Error('Form template renderer is not defined: "' + templateNamespace);
				}

				return renderer;
			},

			loadModules: function(modules, callback) {
				Liferay.Loader.require.apply(
					Liferay.Loader,
					modules.concat([callback])
				);
			}
		};

		Liferay.namespace('DDM').SoyTemplateUtil = SoyTemplateUtil;
	},
	'',
	{
		requires: []
	}
);