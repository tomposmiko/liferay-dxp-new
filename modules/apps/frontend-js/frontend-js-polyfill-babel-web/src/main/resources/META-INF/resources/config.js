if (Liferay.Loader.version().indexOf('3.') === 0) {
	Liferay.Loader.addModule(
		{
			dependencies: [],
			exports: '_babelPolyfill',
			name: 'polyfill-babel',
			path: MODULE_PATH + '/browser-polyfill.min.js'
		}
	);
}