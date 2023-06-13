/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

// For details about this file see: LPS-2155

(function (A, Liferay) {
	var Util = Liferay.Util;

	var Lang = A.Lang;

	var AObject = A.Object;

	var htmlEscapedValues = [];
	var htmlUnescapedValues = [];

	var MAP_HTML_CHARS_ESCAPED = {
		'"': '&#034;',
		'&': '&amp;',
		"'": '&#039;',
		'/': '&#047;',
		'<': '&lt;',
		'>': '&gt;',
		'`': '&#096;',
	};

	var MAP_HTML_CHARS_UNESCAPED = {};

	AObject.each(MAP_HTML_CHARS_ESCAPED, (item, index) => {
		MAP_HTML_CHARS_UNESCAPED[item] = index;

		htmlEscapedValues.push(item);
		htmlUnescapedValues.push(index);
	});

	var REGEX_DASH = /-([a-z])/gi;

	var STR_RIGHT_SQUARE_BRACKET = ']';

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.actsAsAspect = function (object) {
		object.yield = null;
		object.rv = {};

		object.before = function (method, f) {
			var original = eval('this.' + method);

			this[method] = function () {
				f.apply(this, arguments);

				return original.apply(this, arguments);
			};
		};

		object.after = function (method, f) {
			var original = eval('this.' + method);

			this[method] = function () {
				this.rv[method] = original.apply(this, arguments);

				return f.apply(this, arguments);
			};
		};

		object.around = function (method, f) {
			var original = eval('this.' + method);

			this[method] = function () {
				this.yield = original;

				return f.apply(this, arguments);
			};
		};
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.addInputFocus = function () {
		A.use('aui-base', (A) => {
			var handleFocus = function (event) {
				var target = event.target;

				var tagName = target.get('tagName');

				if (tagName) {
					tagName = tagName.toLowerCase();
				}

				var nodeType = target.get('type');

				if (
					(tagName == 'input' && /text|password/.test(nodeType)) ||
					tagName == 'textarea'
				) {
					var action = 'addClass';

					if (/blur|focusout/.test(event.type)) {
						action = 'removeClass';
					}

					target[action]('focus');
				}
			};

			A.on('focus', handleFocus, document);
			A.on('blur', handleFocus, document);
		});

		Util.addInputFocus = function () {};
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.addInputType = function (el) {
		Util.addInputType = Lang.emptyFn;

		if (Liferay.Browser.isIe() && Liferay.Browser.getMajorVersion() < 7) {
			Util.addInputType = function (el) {
				if (el) {
					el = A.one(el);
				}
				else {
					el = A.one(document.body);
				}

				var defaultType = 'text';

				el.all('input').each((item) => {
					var type = item.get('type') || defaultType;

					item.addClass(type);
				});
			};
		}

		return Util.addInputType(el);
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.camelize = function (value, separator) {
		var regex = REGEX_DASH;

		if (separator) {
			regex = new RegExp(separator + '([a-z])', 'gi');
		}

		value = value.replace(regex, (match0, match1) => {
			return match1.toUpperCase();
		});

		return value;
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.clamp = function (value, min, max) {
		return Math.min(Math.max(value, min), max);
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.isEditorPresent = function (editorName) {
		return Liferay.EDITORS && Liferay.EDITORS[editorName];
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.randomMinMax = function (min, max) {
		return Math.round(Math.random() * (max - min)) + min;
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.selectAndCopy = function (el) {
		el.focus();
		el.select();

		if (document.all) {
			var textRange = el.createTextRange();

			textRange.execCommand('copy');
		}
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.setBox = function (oldBox, newBox) {
		for (var i = oldBox.length - 1; i > -1; i--) {
			oldBox.options[i] = null;
		}

		for (i = 0; i < newBox.length; i++) {
			oldBox.options[i] = new Option(newBox[i].value, i);
		}

		oldBox.options[0].selected = true;
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.startsWith = function (str, x) {
		return str.indexOf(x) === 0;
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.textareaTabs = function (event) {
		var el = event.currentTarget.getDOM();

		if (event.isKey('TAB')) {
			event.halt();

			var oldscroll = el.scrollTop;

			if (el.setSelectionRange) {
				var caretPos = el.selectionStart + 1;
				var elValue = el.value;

				el.value =
					elValue.substring(0, el.selectionStart) +
					'\t' +
					elValue.substring(el.selectionEnd, elValue.length);

				setTimeout(() => {
					el.focus();
					el.setSelectionRange(caretPos, caretPos);
				}, 0);
			}
			else {
				document.selection.createRange().text = '\t';
			}

			el.scrollTop = oldscroll;

			return false;
		}
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Util.uncamelize = function (value, separator) {
		separator = separator || ' ';

		value = value.replace(
			/([a-zA-Z][a-zA-Z])([A-Z])([a-z])/g,
			'$1' + separator + '$2$3'
		);
		value = value.replace(/([a-z])([A-Z])/g, '$1' + separator + '$2');

		return value;
	};

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Liferay.provide(
		Util,
		'check',
		(form, name, checked) => {
			var checkbox = A.one(form[name]);

			if (checkbox) {
				checkbox.attr('checked', checked);
			}
		},
		['aui-base']
	);

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Liferay.provide(
		Util,
		'disableSelectBoxes',
		(toggleBoxId, value, selectBoxId) => {
			var selectBox = A.one('#' + selectBoxId);
			var toggleBox = A.one('#' + toggleBoxId);

			if (selectBox && toggleBox) {
				var dynamicValue = Lang.isFunction(value);

				var disabled = function () {
					var currentValue = selectBox.val();

					var visible = value == currentValue;

					if (dynamicValue) {
						visible = value(currentValue, value);
					}

					toggleBox.attr('disabled', !visible);
				};

				disabled();

				selectBox.on('change', disabled);
			}
		},
		['aui-base']
	);

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Liferay.provide(
		Util,
		'disableTextareaTabs',
		(textarea) => {
			textarea = A.one(textarea);

			if (textarea && textarea.attr('textareatabs') != 'enabled') {
				textarea.attr('textareatabs', 'disabled');

				textarea.detach('keydown', Util.textareaTabs);
			}
		},
		['aui-base']
	);

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Liferay.provide(
		Util,
		'enableTextareaTabs',
		(textarea) => {
			textarea = A.one(textarea);

			if (textarea && textarea.attr('textareatabs') != 'enabled') {
				textarea.attr('textareatabs', 'disabled');

				textarea.on('keydown', Util.textareaTabs);
			}
		},
		['aui-base']
	);

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Liferay.provide(
		Util,
		'removeItem',
		(box, value) => {
			box = A.one(box);

			var selectedIndex = box.get('selectedIndex');

			if (!value) {
				box.all('option').item(selectedIndex).remove(true);
			}
			else {
				box.all('option[value=' + value + STR_RIGHT_SQUARE_BRACKET)
					.item(selectedIndex)
					.remove(true);
			}
		},
		['aui-base']
	);

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Liferay.provide(
		Util,
		'resizeTextarea',
		(elString, usingRichEditor) => {
			var el = A.one('#' + elString);

			if (!el) {
				el = A.one(
					'textarea[name=' + elString + STR_RIGHT_SQUARE_BRACKET
				);
			}

			if (el) {
				var pageBody = A.getBody();

				var diff;

				var resize = function (event) {
					var pageBodyHeight = pageBody.get('winHeight');

					if (usingRichEditor) {
						try {
							if (el.get('nodeName').toLowerCase() != 'iframe') {
								el = window[elString];
							}
						}
						catch (e) {}
					}

					if (!diff) {
						var buttonRow = pageBody.one('.button-holder');
						var templateEditor = pageBody.one(
							'.lfr-template-editor'
						);

						if (buttonRow && templateEditor) {
							var region = templateEditor.getXY();

							diff = buttonRow.outerHeight(true) + region[1] + 25;
						}
						else {
							diff = 170;
						}
					}

					el = A.one(el);

					var styles = {
						width: '98%',
					};

					if (event) {
						styles.height = pageBodyHeight - diff;
					}

					if (usingRichEditor) {
						if (!el || !A.DOM.inDoc(el)) {
							A.on(
								'available',
								() => {
									el = A.one(window[elString]);

									if (el) {
										el.setStyles(styles);
									}
								},
								'#' + elString + '_cp'
							);

							return;
						}
					}

					if (el) {
						el.setStyles(styles);
					}
				};

				resize();

				var dialog = Liferay.Util.getWindow();

				if (dialog) {
					var resizeEventHandle = dialog.iframe.after(
						'resizeiframe:heightChange',
						resize
					);

					A.getWin().on(
						'unload',
						resizeEventHandle.detach,
						resizeEventHandle
					);
				}
			}
		},
		['aui-base']
	);

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Liferay.provide(
		Util,
		'setSelectedValue',
		(col, value) => {
			var option = A.one(col).one(
				'option[value=' + value + STR_RIGHT_SQUARE_BRACKET
			);

			if (option) {
				option.attr('selected', true);
			}
		},
		['aui-base']
	);

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	Liferay.provide(
		Util,
		'switchEditor',
		(options) => {
			var uri = options.uri;

			var windowName = Liferay.Util.getWindowName();

			var dialog = Liferay.Util.getWindow(windowName);

			if (dialog) {
				dialog.iframe.set('uri', uri);
			}
		},
		['aui-io']
	);
})(AUI(), Liferay);
