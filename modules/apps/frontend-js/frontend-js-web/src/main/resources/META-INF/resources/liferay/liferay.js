Liferay = window.Liferay || {};

(function($, Liferay) {
	var isFunction = function(val) {
		return typeof val === 'function';
	};

	var isNode = function(node) {
		return node && (node._node || node.jquery || node.nodeType);
	};

	var REGEX_METHOD_GET = /^get$/i;

	var STR_MULTIPART = 'multipart/form-data';

	Liferay.namespace = function namespace(obj, path) {
		if (path === undefined) {
			path = obj;

			obj = this;
		}

		var parts = path.split('.');

		for (var part; parts.length && (part = parts.shift());) {
			if (obj[part] && obj[part] !== Object.prototype[part]) {
				obj = obj[part];
			}
			else {
				obj = obj[part] = {};
			}
		}

		return obj;
	};

	$.ajaxSetup(
		{
			data: {},
			type: 'POST'
		}
	);

	$.ajaxPrefilter(
		function(options) {
			if (options.crossDomain) {
				options.contents.script = false;
			}

			if (options.url) {
				options.url = Liferay.Util.getURLWithSessionId(options.url);
			}
		}
	);

	var jqueryInit = $.prototype.init;

	$.prototype.init = function(selector, context, root) {
		if (selector === '#') {
			selector = '';
		}

		return new jqueryInit(selector, context, root);
	};

	$(document).on(
		'show.bs.collapse',
		function(event) {
			var target = $(event.target);

			var ancestor = target.parents('.panel-group');

			if (target.hasClass('panel-collapse') && ancestor.length) {
				var openChildren = ancestor.find('.panel-collapse.in').not(target);

				if (openChildren.length && ancestor.find('[data-parent="#' + ancestor.attr('id') + '"]').length) {
					openChildren.removeClass('in');
				}
			}

			if (target.hasClass('in')) {
				target.addClass('show');
				target.removeClass('in');

				target.collapse('hide');

				return false;
			}
		}
	);

	$(document).on(
		'show.bs.dropdown',
		function() {
			Liferay.fire(
				'dropdownShow',
				{
					src: 'BootstrapDropdown'
				}
			);
		}
	);

	Liferay.on(
		'dropdownShow',
		function(event) {
			if (event.src !== 'BootstrapDropdown') {
				$('.dropdown.show .dropdown-toggle').dropdown('toggle');
			}
		}
	);

	/**
	 * OPTIONS
	 *
	 * Required
	 * service {string|object}: Either the service name, or an object with the keys as the service to call, and the value as the service configuration object.
	 *
	 * Optional
	 * data {object|node|string}: The data to send to the service. If the object passed is the ID of a form or a form element, the form fields will be serialized and used as the data.
	 * successCallback {function}: A function to execute when the server returns a response. It receives a JSON object as it's first parameter.
	 * exceptionCallback {function}: A function to execute when the response from the server contains a service exception. It receives a the exception message as it's first parameter.
	 */

	var Service = function() {
		var instance = this;

		var args = Service.parseInvokeArgs(Array.prototype.slice.call(arguments, 0));

		return Service.invoke.apply(Service, args);
	};

	Service.URL_INVOKE = themeDisplay.getPathContext() + '/api/jsonws/invoke';

	Service.bind = function() {
		var args = Array.prototype.slice.call(arguments, 0);

		return function() {
			var newArgs = Array.prototype.slice.call(arguments, 0);

			return Service.apply(Service, args.concat(newArgs));
		};
	};

	Service.parseInvokeArgs = function(args) {
		var instance = this;

		var payload = args[0];

		var ioConfig = instance.parseIOConfig(args);

		if (typeof payload === 'string') {
			payload = instance.parseStringPayload(args);

			instance.parseIOFormConfig(ioConfig, args);

			var lastArg = args[args.length - 1];

			if (typeof lastArg === 'object' && lastArg.method) {
				ioConfig.method = lastArg.method;
			}
		}

		return [payload, ioConfig];
	};

	Service.parseIOConfig = function(args) {
		var instance = this;

		var payload = args[0];

		var ioConfig = payload.io || {};

		delete payload.io;

		if (!ioConfig.success) {
			var callbacks = args.filter(isFunction);

			var callbackException = callbacks[1];
			var callbackSuccess = callbacks[0];

			if (!callbackException) {
				callbackException = callbackSuccess;
			}

			ioConfig.complete = function(xhr) {
				var response = xhr.responseJSON;

				if ((response !== null) && !response.hasOwnProperty('exception')) {
					if (callbackSuccess) {
						callbackSuccess.call(this, response);
					}
				}
				else if (callbackException) {
					var exception = response ? response.exception : 'The server returned an empty response';

					callbackException.call(this, exception, response);
				}
			};
		}

		if (!ioConfig.hasOwnProperty('cache') && REGEX_METHOD_GET.test(ioConfig.type)) {
			ioConfig.cache = false;
		}

		if (Liferay.PropsValues.NTLM_AUTH_ENABLED && Liferay.Browser.isIe()) {
			ioConfig.type = 'GET';
		}

		return ioConfig;
	};

	Service.parseIOFormConfig = function(ioConfig, args) {
		var instance = this;

		var form = args[1];

		if (isNode(form)) {
			ioConfig.form = form;

			if (ioConfig.form.enctype == STR_MULTIPART) {
				ioConfig.contentType = false;
				ioConfig.processData = false;
			}
		}
	};

	Service.parseStringPayload = function(args) {
		var params = {};
		var payload = {};

		var config = args[1];

		if (!isFunction(config) && !isNode(config)) {
			params = config;
		}

		payload[args[0]] = params;

		return payload;
	};

	Service.invoke = function(payload, ioConfig) {
		var instance = this;

		var cmd = JSON.stringify(payload);
		var p_auth = Liferay.authToken;

		ioConfig = Object.assign(
			{
				data: {
					cmd: cmd,
					p_auth: p_auth
				},
				dataType: 'JSON'
			},
			ioConfig
		);

		if (ioConfig.form) {
			if (ioConfig.form.enctype == STR_MULTIPART && isFunction(window.FormData)) {
				ioConfig.data = new FormData(ioConfig.form);

				ioConfig.data.append('cmd', cmd);
				ioConfig.data.append('p_auth', p_auth);
			}
			else {
				$(ioConfig.form).serializeArray().forEach(
					function(item) {
						ioConfig.data[item.name] = item.value;
					}
				);
			}

			delete ioConfig.form;
		}

		return $.ajax(
			instance.URL_INVOKE,
			ioConfig
		);
	};

	['get', 'delete', 'post', 'put', 'update'].forEach(
		function(item) {
			var methodName = item;

			if (item === 'delete') {
				methodName = 'del';
			}

			Service[methodName] = function() {
				var args = Array.prototype.slice.call(arguments, 0);

				var method = {method: item};

				args.push(method);

				return Service.apply(Service, args);
			};
		}
	);

	Liferay.Service = Service;

	Liferay.Template = {
		PORTLET: '<div class="portlet"><div class="portlet-topper"><div class="portlet-title"></div></div><div class="portlet-content"></div><div class="forbidden-action"></div></div>'
	};
})(AUI.$, Liferay);

(function(A, Liferay) {
	A.mix(
		A.namespace('config.io'),
		{
			method: 'POST',
			uriFormatter: function(value) {
				return Liferay.Util.getURLWithSessionId(value);
			}
		},
		true
	);
})(AUI(), Liferay);