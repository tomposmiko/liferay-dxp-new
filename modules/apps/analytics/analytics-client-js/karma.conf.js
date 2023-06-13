const path = require('path');
const webpack = require('./webpack.test.config');

module.exports = function(config) {
	config.set({
		browsers: ['ChromeHeadless'],

		coverageIstanbulReporter: {
			dir: path.join(__dirname, 'test-coverage'),
			reports: ['html', 'lcovonly', 'text-summary']
		},

		coverageReporter: {
			reporters: [
				{
					subdir: 'lcov',
					type: 'lcov',
				}, {
					type: 'text-summary',
				},
			],
		},

		customLaunchers: {
			ChromeHeadless: {
				base: 'Chrome',
				flags: [
					'--no-sandbox',
					'--headless',
					'--disable-gpu',
					'--disable-translate',
					'--disable-extensions',
					'--remote-debugging-port=9222'
				]
			}
		},

		files: [
			'test.webpack.js',
		],

		frameworks: ['chai', 'mocha', 'sinon'],

		plugins: [
			'karma-chai',
			'karma-chrome-launcher',
			'karma-coverage-istanbul-reporter',
			'karma-mocha',
			'karma-sinon',
			'karma-sourcemap-loader',
			'karma-webpack',
		],

		preprocessors: {
			'test.webpack.js': ['webpack', 'sourcemap'],
		},

		reporters: ['progress', 'coverage-istanbul'],

		singleRun: true,

		webpack,
	});
};