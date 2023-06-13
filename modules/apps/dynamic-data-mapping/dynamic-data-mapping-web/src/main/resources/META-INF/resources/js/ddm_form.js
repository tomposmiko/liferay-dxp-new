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

AUI.add(
	'liferay-ddm-form',
	(A) => {
		const AArray = A.Array;

		const AQueue = A.Queue;

		const DateMath = A.DataType.DateMath;

		const Lang = A.Lang;

		const INSTANCE_ID_PREFIX = '_INSTANCE_';

		const INTEGER_MIN_VALUE = 0;

		const INTEGER_MAX_VALUE = 2147483647;

		const SELECTOR_REPEAT_BUTTONS =
			'.lfr-ddm-repeatable-add-button, .lfr-ddm-repeatable-delete-button';

		const TPL_ICON_CARET =
			'<span class="collapse-icon-closed"><span class="icon-caret-right"></span></span>';

		const TPL_PUBLIC_PRIVATE_LAYOUTS_NAVBAR =
			'<nav class="navbar navbar-collapse-absolute navbar-expand-md navbar-underline navigation-bar navigation-bar-light">' +
			'<div class="collapse navbar-collapse">' +
			'<ul class="nav navbar-nav">' +
			'<li class="public {publicLayoutClass}"><a href="javascript:void(0);">' +
			Liferay.Language.get('public-pages') +
			'</a></li>' +
			'<li class="private {privateLayoutClass}"><a href="javascript:void(0);">' +
			Liferay.Language.get('private-pages') +
			'</a></li>' +
			'</ul>' +
			'</div>' +
			'</nav>';

		const TPL_LAYOUTS_NAVBAR =
			'<nav class="navbar navbar-collapse-absolute navbar-expand-md navbar-underline navigation-bar navigation-bar-light">' +
			'<div class="collapse navbar-collapse">' +
			'<ul class="nav navbar-nav">' +
			'<li class="public active"><a href="javascript:void(0);">' +
			Liferay.Language.get('pages') +
			'</a></li>' +
			'</ul>' +
			'</div>' +
			'</nav>';

		const TPL_LOADER = '<span class="linear loading-icon"></span>';

		const TPL_PAGE =
			'<li class="lfr-ddm-link" data-groupId="{groupId}" data-layoutId="{layoutId}" data-nodeType="{nodeType}" data-privateLayout="{privateLayout}">' +
			'<input class="lfr-ddm-page-radio" {checked} name="lfr-ddm-page" type="radio" />' +
			'<a class="collapsed collapse-icon lfr-ddm-page-label" href="javascript:void(0);">{pageTitle}{icon}</a>' +
			'</li>';

		const TPL_PAGES_BREADCRUMB =
			'<ul class="breadcrumb lfr-ddm-breadcrumb"></ul>';

		const TPL_PAGES_BREADCRUMB_ELEMENT =
			'<li class="lfr-ddm-breadcrumb-element" data-groupId={groupId} data-layoutId={layoutId} data-privateLayout={privateLayout}>' +
			'<a title="{label}">{label}</a>' +
			'</li>';

		const TPL_PAGES_CONTAINER =
			'<ul class="lfr-ddm-pages-container nav vertical-scrolling"></ul>';

		const TPL_REPEATABLE_ADD =
			'<a class="lfr-ddm-repeatable-add-button" href="javascript:void(0);">' +
			Liferay.Util.getLexiconIconTpl('plus') +
			'</a>';

		const TPL_REPEATABLE_ICON =
			'<div class="lfr-ddm-repeatable-drag-icon">' +
			Liferay.Util.getLexiconIconTpl('drag') +
			'</div>';

		const TPL_REPEATABLE_DELETE =
			'<a class="hide lfr-ddm-repeatable-delete-button" href="javascript:void(0);">' +
			Liferay.Util.getLexiconIconTpl('hr') +
			'</a>';

		const TPL_REPEATABLE_HELPER =
			'<div class="lfr-ddm-repeatable-helper"></div>';

		const TPL_REQUIRED_MARK =
			'<span class="reference-mark">' +
			Liferay.Util.getLexiconIconTpl('asterisk') +
			'<span class="hide-accessible sr-only">' +
			Liferay.Language.get('required') +
			'</span></span>';

		const FieldTypes = Liferay.namespace('DDM.FieldTypes');

		const getFieldClass = function (type) {
			return FieldTypes[type] || FieldTypes.field;
		};

		const isNode = function (node) {
			return node && (node._node || node.nodeType);
		};

		const DDMPortletSupport = function () {};

		DDMPortletSupport.ATTRS = {
			doAsGroupId: {},

			fieldsNamespace: {},

			isPrivateLayoutsEnabled: {
				validator: Lang.isBoolean,
				value: true,
			},

			p_l_id: {},

			portletNamespace: {},
		};

		const FieldsSupport = function () {};

		FieldsSupport.ATTRS = {
			container: {
				setter: A.one,
			},

			definition: {},

			displayLocale: {
				valueFn: '_valueDisplayLocale',
			},

			fields: {
				valueFn: '_valueFields',
			},

			mode: {},

			values: {
				value: {},
			},
		};

		FieldsSupport.prototype = {
			_getField(fieldNode) {
				const instance = this;

				const displayLocale = instance.get('displayLocale');

				const fieldInstanceId = instance.extractInstanceId(fieldNode);

				const fieldName = fieldNode.getData('fieldName');

				const definition = instance.get('definition');

				const fieldDefinition = instance.getFieldInfo(
					definition,
					'name',
					fieldName
				);

				const FieldClass = getFieldClass(fieldDefinition.type);

				const field = new FieldClass({
					...instance.getAttrs(Object.keys(DDMPortletSupport.ATTRS)),
					container: fieldNode,
					dataType: fieldDefinition.dataType,
					definition,
					displayLocale,
					instanceId: fieldInstanceId,
					name: fieldName,
					parent: instance,
					values: instance.get('values'),
				});

				const form = instance.getForm();

				field.addTarget(form);

				return field;
			},

			_getTemplate(callback) {
				const instance = this;

				const key =
					Liferay.Util.getPortletNamespace(
						Liferay.PortletKeys.DYNAMIC_DATA_MAPPING
					) + 'definition';

				const data = new URLSearchParams();
				data.append(key, JSON.stringify(instance.get('definition')));

				Liferay.Util.fetch(instance._getTemplateResourceURL(), {
					body: data,
					method: 'POST',
				})
					.then((response) => {
						return response.text();
					})
					.then((response) => {
						if (callback) {
							callback.call(instance, response);
						}
					});
			},

			_getTemplateResourceURL() {
				const instance = this;

				const container = instance.get('container');

				const dataType = instance.get('dataType');

				const templateResourceParameters = {
					doAsGroupId: instance.get('doAsGroupId'),
					fieldName: instance.get('name'),
					mode: instance.get('mode'),
					namespace: instance.get('fieldsNamespace'),
					p_l_id: instance.get('p_l_id'),
					p_p_auth: container.getData('ddmAuthToken'),
					p_p_id: Liferay.PortletKeys.DYNAMIC_DATA_MAPPING,
					p_p_isolated: true,
					p_p_resource_id:
						'/dynamic_data_mapping/render_structure_field',
					p_p_state: 'pop_up',
					portletNamespace: instance.get('portletNamespace'),
					readOnly: instance.get('readOnly'),
				};

				if (dataType && dataType === 'html') {
					delete templateResourceParameters.doAsGroupId;
				}

				const fields = instance._valueFields();

				if (fields && fields.length) {
					instance._removeDoAsGroupIdParam(
						fields,
						templateResourceParameters
					);
				}

				const templateResourceURL = Liferay.Util.PortletURL.createResourceURL(
					themeDisplay.getLayoutURL(),
					templateResourceParameters
				);

				return templateResourceURL.toString();
			},

			_removeDoAsGroupIdParam(fields, templateResourceParameters) {
				const instance = this;

				fields.forEach((field) => {
					if (!templateResourceParameters.doAsGroupId) {
						return;
					}

					const dataType = field.get('dataType');

					if (dataType && dataType === 'html') {
						delete templateResourceParameters.doAsGroupId;

						return;
					}

					const nestedFields = field.get('fields');

					if (nestedFields && nestedFields.length) {
						instance._removeDoAsGroupIdParam(
							nestedFields,
							templateResourceParameters
						);
					}
				});
			},

			_valueDisplayLocale() {
				const instance = this;

				let displayLocale = instance.get('displayLocale');

				if (!displayLocale) {
					displayLocale = instance.getDefaultLocale();
				}

				const defaultEditLocale = instance.get('defaultEditLocale');

				if (defaultEditLocale) {
					displayLocale = defaultEditLocale;
				}

				return displayLocale;
			},

			_valueFields() {
				const instance = this;

				const fields = [];

				instance.getFieldNodes().each((item) => {
					fields.push(instance._getField(item));
				});

				return fields;
			},

			eachParent(fn) {
				const instance = this;

				let parent = instance.get('parent');

				while (parent !== undefined) {
					fn.call(instance, parent);

					parent = parent.get('parent');
				}
			},

			extractInstanceId(fieldNode) {
				const fieldInstanceId = fieldNode.getData('fieldNamespace');

				return fieldInstanceId.replace(INSTANCE_ID_PREFIX, '');
			},

			getDefaultLocale() {
				const instance = this;

				let defaultLocale = themeDisplay.getDefaultLanguageId();

				const definition = instance.get('definition');

				if (definition) {
					defaultLocale = definition.defaultLanguageId;
				}

				return defaultLocale;
			},

			getFieldInfo(tree, key, value) {
				const queue = new AQueue(tree);

				const addToQueue = function (item) {
					if (queue._q.indexOf(item) === -1) {
						queue.add(item);
					}
				};

				let fieldInfo = {};

				while (queue.size() > 0) {
					const next = queue.next();

					if (next[key] === value) {
						fieldInfo = next;
					}
					else {
						const children =
							next.fields ||
							next.nestedFields ||
							next.fieldValues ||
							next.nestedFieldValues;

						if (children) {
							children.forEach(addToQueue);
						}
					}
				}

				return fieldInfo;
			},

			getFieldNodes() {
				const instance = this;

				return instance.get('container').all('> .field-wrapper');
			},

			getForm() {
				const instance = this;

				let root;

				instance.eachParent((parent) => {
					root = parent;
				});

				return root || instance;
			},

			getReadOnly() {
				const instance = this;

				let retVal = false;

				if (instance.get('readOnly')) {
					retVal = true;
				}
				else {
					const form = instance.getForm();

					if (
						!instance.get('localizable') &&
						form.getDefaultLocale() !==
							instance.get('displayLocale')
					) {
						retVal = true;
					}
				}

				return retVal;
			},
		};

		const Field = A.Component.create({
			ATTRS: {
				container: {
					setter: A.one,
				},

				dataType: {},

				definition: {
					validator: Lang.isObject,
				},

				formNode: {
					valueFn: '_valueFormNode',
				},

				instanceId: {},

				liferayForm: {
					valueFn: '_valueLiferayForm',
				},

				localizable: {
					getter: '_getLocalizable',
					readOnly: true,
				},

				localizationMap: {
					valueFn: '_valueLocalizationMap',
				},

				name: {
					validator: Lang.isString,
				},

				node: {},

				parent: {},

				readOnly: {},

				repeatable: {
					getter: '_getRepeatable',
					readOnly: true,
				},
			},

			AUGMENTS: [DDMPortletSupport, FieldsSupport],

			EXTENDS: A.Base,

			NAME: 'liferay-ddm-field',

			prototype: {
				_addFieldValidation(newField, originalField) {
					const instance = this;

					instance.fire('liferay-ddm-field:repeat', {
						field: newField,
						originalField,
					});

					newField.get('fields').forEach((item) => {
						const name = item.get('name');

						const originalChildField = originalField.getFirstFieldByName(
							name
						);

						if (originalChildField) {
							instance._addFieldValidation(
								item,
								originalChildField
							);
						}
					});
				},

				_addTip(labelNode, tipNode) {
					if (tipNode) {
						const instance = this;

						const defaultLocale = instance.getDefaultLocale();
						const fieldDefinition = instance.getFieldDefinition();

						const tipsMap = fieldDefinition.tip;

						if (Lang.isObject(tipsMap)) {
							const tip =
								tipsMap[instance.get('displayLocale')] ||
								tipsMap[defaultLocale];

							tipNode.attr('title', tip);
						}

						labelNode.append(tipNode);
					}
				},

				_afterFormRegistered(event) {
					const instance = this;

					const formNode = instance.get('formNode');

					if (event.formName === formNode.attr('name')) {
						instance.set('liferayForm', event.form);
					}

					instance.addIntegerRangeRule();
				},

				_getLocalizable() {
					const instance = this;

					return instance.getFieldDefinition().localizable === true;
				},

				_getRepeatable() {
					const instance = this;

					return instance.getFieldDefinition().repeatable === true;
				},

				_handleToolbarClick(event) {
					const instance = this;

					const currentTarget = event.currentTarget;

					instance.ddmRepeatableButton = currentTarget;

					if (
						currentTarget.hasClass('lfr-ddm-repeatable-add-button')
					) {
						instance.repeat();
					}
					else if (
						currentTarget.hasClass(
							'lfr-ddm-repeatable-delete-button'
						)
					) {
						instance.remove();

						instance.syncRepeatablelUI();
					}

					event.stopPropagation();
				},

				_onLocaleChanged(event) {
					const instance = this;

					const currentLocale = instance.get('displayLocale');
					const displayLocale = event.item.getAttribute('data-value');

					instance.updateLocalizationMap(currentLocale);

					instance.set('displayLocale', displayLocale);

					instance.syncLabel(displayLocale);
					instance.syncValueUI();
					instance.syncReadOnlyUI();
				},

				_removeFieldValidation(field) {
					const instance = this;

					field.get('fields').forEach((item) => {
						instance._removeFieldValidation(item);
					});

					instance.fire('liferay-ddm-field:remove', {
						field,
					});
				},

				_valueFormNode() {
					const instance = this;

					const container = instance.get('container');

					return container.ancestor('form', true);
				},

				_valueLiferayForm() {
					const instance = this;

					const formNode = instance.get('formNode');

					let formName = null;

					if (formNode) {
						formName = formNode.attr('name');
					}

					return Liferay.Form.get(formName);
				},

				_valueLocalizationMap() {
					const instance = this;

					const instanceId = instance.get('instanceId');

					const values = instance.get('values');

					const fieldValue = instance.getFieldInfo(
						values,
						'instanceId',
						instanceId
					);

					let localizationMap = {};

					if (fieldValue && fieldValue.value) {
						localizationMap = fieldValue.value;
					}

					return localizationMap;
				},

				addIntegerRangeRule() {
					const instance = this;

					const dataType = instance.get('dataType');

					if (dataType && dataType === 'integer') {
						const liferayForm = instance.get('liferayForm');

						if (liferayForm) {
							const node = instance.getInputNode();

							const fieldName = node.get('name');

							const errorMessage = Liferay.Util.sub(
								Liferay.Language.get(
									'please-enter-a-valid-integer-value-between-x-and-x'
								),
								INTEGER_MIN_VALUE,
								INTEGER_MAX_VALUE
							);

							liferayForm.addRule(
								fieldName,
								'integerRange_custom',
								errorMessage,
								(val) => {
									return (
										val >= INTEGER_MIN_VALUE &&
										val <= INTEGER_MAX_VALUE
									);
								},
								true
							);
						}
					}
				},

				bindUI() {
					const instance = this;

					instance.eventHandlers.push(
						Liferay.on(
							'inputLocalized:localeChanged',
							instance._onLocaleChanged,
							instance
						)
					);

					const formNode = instance.get('formNode');

					if (formNode) {
						instance.eventHandlers.push(
							Liferay.after(
								'form:registered',
								instance._afterFormRegistered,
								instance
							)
						);
					}
				},

				convertNumberLocale(number, sourceLocale, targetLocale) {
					if (sourceLocale !== targetLocale) {
						const test = 1.1;
						const sourceDecimalSeparator = test
							.toLocaleString(sourceLocale.replace('_', '-'))
							.charAt(1);
						const targetDecimalSeparator = test
							.toLocaleString(targetLocale.replace('_', '-'))
							.charAt(1);

						if (sourceDecimalSeparator !== targetDecimalSeparator) {
							if (
								['.', ','].includes(sourceDecimalSeparator) &&
								['.', ','].includes(targetDecimalSeparator)
							) {
								number = number.replace(
									/[,.]/g,
									(separator) => {
										if (targetDecimalSeparator === '.') {
											return separator === '.' ? '' : '.';
										}
										else {
											return separator === '.' ? ',' : '';
										}
									}
								);
							}
						}
					}

					return number;
				},

				createField(fieldTemplate) {
					const instance = this;

					const fieldNode = A.Node.create(fieldTemplate);

					instance.get('container').placeAfter(fieldNode);

					instance.parseContent(fieldTemplate);

					const parent = instance.get('parent');

					const siblings = instance.getSiblings();

					const field = parent._getField(fieldNode);

					let index = siblings.indexOf(instance);

					siblings.splice(++index, 0, field);

					field.set('parent', parent);

					return field;
				},

				destructor() {
					const instance = this;

					AArray.invoke(instance.eventHandlers, 'detach');

					AArray.invoke(instance.get('fields'), 'destroy');

					instance.eventHandlers = null;

					instance.get('container').remove();
				},

				getDefaultLocalization(locale) {
					const instance = this;

					const localizationMap = instance.get('localizationMap');

					if (Lang.isUndefined(localizationMap[locale])) {
						const predefinedValue = instance.getPredefinedValueByLocale(
							locale
						);

						if (predefinedValue) {
							return predefinedValue;
						}

						const defaultLocale = instance.getDefaultLocale();

						if (defaultLocale && localizationMap[defaultLocale]) {
							const name = instance.get('name');

							const field = instance.getFieldByNameInFieldDefinition(
								name
							);

							if (field) {
								const type = field.type;

								if (
									type === 'ddm-number' ||
									type === 'ddm-decimal'
								) {
									return instance.convertNumberLocale(
										localizationMap[defaultLocale],
										defaultLocale,
										locale
									);
								}
							}

							return localizationMap[defaultLocale];
						}

						return '';
					}

					return localizationMap[locale];
				},

				getFieldByNameInFieldDefinition(name) {
					const instance = this;

					const definition = instance.get('definition');

					const fields = [];

					if (definition && definition.fields) {
						definition.fields.forEach((field) => {
							fields.push(field);
							if (field.nestedFields) {
								field.nestedFields.forEach((nestedField) => {
									fields.push(nestedField);
								});
							}
						});
					}

					return AArray.find(fields, (item) => {
						return item.name === name;
					});
				},

				getFieldDefinition() {
					const instance = this;

					const definition = instance.get('definition');

					const name = instance.get('name');

					return instance.getFieldInfo(definition, 'name', name);
				},

				getFirstFieldByName(name) {
					const instance = this;

					return AArray.find(instance.get('fields'), (item) => {
						return item.get('name') === name;
					});
				},

				getInputName() {
					const instance = this;

					const fieldsNamespace = instance.get('fieldsNamespace');
					const portletNamespace = instance.get('portletNamespace');

					const prefix = [portletNamespace];

					if (fieldsNamespace) {
						prefix.push(fieldsNamespace);
					}

					return prefix
						.concat([
							instance.get('name'),
							INSTANCE_ID_PREFIX,
							instance.get('instanceId'),
						])
						.join('');
				},

				getInputNode() {
					const instance = this;

					return instance
						.get('container')
						.one('[name=' + instance.getInputName() + ']');
				},

				getLabelNode() {
					const instance = this;

					return instance.get('container').one('.control-label');
				},

				getPredefinedValueByLocale(locale) {
					const instance = this;

					const name = instance.get('name');

					const field = instance.getFieldByNameInFieldDefinition(
						name
					);

					let predefinedValue;

					if (field) {
						const type = field.type;

						if (
							field.predefinedValue &&
							field.predefinedValue[locale]
						) {
							predefinedValue = field.predefinedValue[locale];
						}

						const localizationMap = instance.get('localizationMap');

						if (
							type === 'select' &&
							(predefinedValue === '[""]' ||
								// eslint-disable-next-line @liferay/aui/no-object
								!A.Object.isEmpty(localizationMap))
						) {
							predefinedValue = '';
						}
					}

					return predefinedValue;
				},

				getRepeatedSiblings() {
					const instance = this;

					return instance.getSiblings().filter((item) => {
						return item.get('name') === instance.get('name');
					});
				},

				getRuleInputName() {
					const instance = this;

					const inputName = instance.getInputName();

					return inputName;
				},

				getSiblings() {
					const instance = this;

					return instance.get('parent').get('fields');
				},

				getValue() {
					const instance = this;

					const inputNode = instance.getInputNode();

					let value = '';

					if (inputNode) {
						value = Liferay.Util.unescapeHTML(inputNode.val());
					}

					return value;
				},

				initializer() {
					const instance = this;

					instance.eventHandlers = [];

					instance.bindUI();
				},

				parseContent(content) {
					const instance = this;

					const container = instance.get('container');

					container.plug(A.Plugin.ParseContent);

					const parser = container.ParseContent;

					parser.parseContent(content);
				},

				remove() {
					const instance = this;

					const siblings = instance.getSiblings();

					const index = siblings.indexOf(instance);

					siblings.splice(index, 1);

					instance._removeFieldValidation(instance);

					instance.destroy();

					instance.get('container').remove(true);
				},

				renderRepeatableUI() {
					const instance = this;

					const container = instance.get('container');

					const fieldDefinition = instance.getFieldDefinition();

					if (
						fieldDefinition &&
						(fieldDefinition.dataType === 'html' ||
							fieldDefinition.type === 'ddm-geolocation' ||
							fieldDefinition.type === 'ddm-separator')
					) {
						container._node.insertAdjacentHTML(
							'afterbegin',
							TPL_REPEATABLE_ICON
						);
					}
					else {
						const containerLabel = container._node.children[0];

						containerLabel.insertAdjacentHTML(
							'afterbegin',
							TPL_REPEATABLE_ICON
						);
					}

					container.append(TPL_REPEATABLE_ADD);
					container.append(TPL_REPEATABLE_DELETE);

					container.delegate(
						'click',
						instance._handleToolbarClick,
						SELECTOR_REPEAT_BUTTONS,
						instance
					);
				},

				renderUI() {
					const instance = this;

					if (instance.get('repeatable')) {
						instance.renderRepeatableUI();
						instance.syncRepeatablelUI();
					}

					instance.syncLabel(instance.get('displayLocale'));

					instance.syncValueUI();

					AArray.invoke(instance.get('fields'), 'renderUI');

					instance.fire('liferay-ddm-field:render', {
						field: instance,
					});
				},

				repeat() {
					const instance = this;

					instance._getTemplate((fieldTemplate) => {
						const field = instance.createField(fieldTemplate);

						const displayLocale = instance.get('displayLocale');

						field.set('displayLocale', displayLocale);

						if (instance.originalField) {
							field.originalField = instance.originalField;
						}
						else {
							field.originalField = instance;
						}

						const form = field.getForm();

						form.newRepeatableInstances.push(field);

						field.renderUI();

						instance._addFieldValidation(field, instance);
					});
				},

				setLabel(label) {
					const instance = this;

					const labelNode = instance.getLabelNode();

					if (labelNode) {
						const tipNode = labelNode.one('.taglib-icon-help');

						if (
							!A.UA.ie &&
							Lang.isValue(label) &&
							Lang.isNode(labelNode)
						) {
							labelNode.html(A.Escape.html(label));
						}

						const fieldDefinition = instance.getFieldDefinition();

						if (!A.UA.ie && fieldDefinition.required) {
							labelNode.append(TPL_REQUIRED_MARK);
						}

						instance._addTip(labelNode, tipNode);
					}
				},

				setValue(value) {
					const instance = this;

					const inputNode = instance.getInputNode();

					if (Lang.isValue(value)) {
						inputNode.val(value);
					}
				},

				syncLabel(locale) {
					const instance = this;

					const fieldDefinition = instance.getFieldDefinition();

					if (Lang.isUndefined(fieldDefinition.label[locale])) {
						instance.setLabel(
							fieldDefinition.label[instance.getDefaultLocale()]
						);
					}
					else {
						instance.setLabel(fieldDefinition.label[locale]);
					}
				},

				syncReadOnlyUI() {
					const instance = this;

					const readOnly = instance.getReadOnly();

					const inputNode = instance.getInputNode();

					if (inputNode) {
						inputNode.attr('disabled', readOnly);
					}

					const container = instance.get('container');

					if (container) {
						const selectorInput = container.one('.selector-input');

						if (selectorInput) {
							selectorInput.attr('disabled', readOnly);
						}

						if (instance.getFieldDefinition().type === 'checkbox') {
							const checkboxInput = container.one(
								'input[type="checkbox"][name*="' +
									instance.getFieldDefinition().name +
									'"]'
							);

							if (checkboxInput) {
								checkboxInput.attr('disabled', readOnly);
							}

							const disableCheckboxInput = container.one(
								'input[type="checkbox"][name$="disable"]'
							);

							if (
								inputNode &&
								disableCheckboxInput &&
								disableCheckboxInput.get('checked')
							) {
								inputNode.attr('disabled', true);
							}
						}
					}
				},

				syncRepeatablelUI() {
					const instance = this;

					const container = instance.get('container');

					const siblings = instance.getRepeatedSiblings();

					const parentField = siblings[0];

					container
						.one('.lfr-ddm-repeatable-delete-button')
						.toggle(
							siblings.length > 1 &&
								siblings.includes(instance) &&
								!parentField
									.get('container')
									.compareTo(container)
						);
				},

				syncValueUI() {
					const instance = this;

					const dataType = instance.get('dataType');

					if (dataType) {
						const localizationMap = instance.get('localizationMap');

						let value;

						if (instance.get('localizable')) {
							// eslint-disable-next-line @liferay/aui/no-object
							if (!A.Object.isEmpty(localizationMap)) {
								value =
									localizationMap[
										instance.get('displayLocale')
									];
							}

							if (Lang.isUndefined(value)) {
								value = instance.getDefaultLocalization(
									instance.get('displayLocale')
								);
							}

							instance.setValue(value);
						}
						else {
							if (
								(dataType === 'double' ||
									dataType === 'number') &&
								// eslint-disable-next-line @liferay/aui/no-object
								!A.Object.isEmpty(localizationMap)
							) {
								instance.setValue(localizationMap);
							}
							else {
								instance.setValue(instance.getValue());
							}
						}
					}
				},

				toJSON() {
					const instance = this;

					const fieldJSON = {
						instanceId: instance.get('instanceId'),
						name: instance.get('name'),
					};

					const dataType = instance.get('dataType');

					const fields = instance.get('fields');

					if (dataType || fields.length) {
						fieldJSON.value = instance.get('localizationMap');

						if (instance.get('localizable')) {
							const form = instance.getForm();

							form.addAvailableLanguageIds(
								Object.keys(fieldJSON.value)
							);
						}
					}

					if (fields.length) {
						fieldJSON.nestedFieldValues = AArray.invoke(
							fields,
							'toJSON'
						);
					}

					return fieldJSON;
				},

				updateLocalizationMap(locale) {
					const instance = this;

					let localizationMap = instance.get('localizationMap');

					const value = instance.getValue();

					if (instance.get('localizable')) {
						const defaultLocale = instance.getDefaultLocale();

						if (
							locale === defaultLocale ||
							(localizationMap[defaultLocale] !== undefined &&
								value !== localizationMap[defaultLocale]) ||
							localizationMap[locale] !== undefined
						) {
							localizationMap[locale] = value;
						}

						for (const key in localizationMap) {
							if (!localizationMap[key]) {
								localizationMap[key] = '';
							}
						}
					}
					else {
						localizationMap = value;
					}

					instance.set('localizationMap', localizationMap);
				},
			},
		});

		const CheckboxField = A.Component.create({
			EXTENDS: Field,

			prototype: {
				getLabelNode() {
					const instance = this;

					return instance.get('container').one('label');
				},

				getValue() {
					const instance = this;

					return instance.getInputNode().test(':checked') + '';
				},

				setLabel(label) {
					const instance = this;

					const labelNode = instance.getLabelNode();

					const tipNode = labelNode.one('.taglib-icon-help');

					const inputNode = instance.getInputNode();

					if (Lang.isValue(label) && Lang.isNode(labelNode)) {
						labelNode.html('&nbsp;' + A.Escape.html(label));

						const fieldDefinition = instance.getFieldDefinition();

						if (fieldDefinition.required) {
							labelNode.append(TPL_REQUIRED_MARK);
						}

						labelNode.prepend(inputNode);
					}

					instance._addTip(labelNode, tipNode);
				},

				setValue(value) {
					const instance = this;

					instance.getInputNode().attr('checked', value === 'true');
				},
			},
		});

		FieldTypes.checkbox = CheckboxField;

		const ColorField = A.Component.create({
			EXTENDS: Field,

			prototype: {
				getValue() {
					const instance = this;

					const container = instance.get('container');
					const valueField = container.one('.color-value');

					return valueField.val();
				},

				initializer() {
					const instance = this;

					const container = instance.get('container');

					const selectorInput = container.one('.selector-input');
					const valueField = container.one('.color-value');

					const colorPicker = new A.ColorPickerPopover({
						position: 'bottom',
						trigger: selectorInput,
						zIndex: 65535,
					}).render();

					colorPicker.on('select', (event) => {
						selectorInput.setStyle('backgroundColor', event.color);

						valueField.val(event.color);

						instance.validateField(valueField);
					});

					colorPicker.after('visibleChange', (event) => {
						if (!event.newVal) {
							instance.validateField(valueField);
						}
					});

					colorPicker.set('color', valueField.val(), {
						trigger: selectorInput,
					});

					instance.set('colorPicker', colorPicker);
				},

				setValue(value) {
					const instance = this;

					const container = instance.get('container');

					const colorPicker = instance.get('colorPicker');
					const selectorInput = container.one('.selector-input');
					const valueField = container.one('.color-value');

					if (!colorPicker) {
						return;
					}

					valueField.val(value);
					selectorInput.setStyle('backgroundColor', value);

					colorPicker.set('color', value);
				},

				validateField(valueField) {
					const instance = this;

					const liferayForm = instance.get('liferayForm');

					if (liferayForm) {
						const formValidator = liferayForm.formValidator;

						if (formValidator) {
							formValidator.validateField(valueField);
						}
					}
				},
			},
		});

		FieldTypes['ddm-color'] = ColorField;

		const DateField = A.Component.create({
			EXTENDS: Field,

			prototype: {
				getDatePicker() {
					const instance = this;

					const inputNode = instance.getInputNode();

					return Liferay.component(
						inputNode.attr('id') + 'DatePicker'
					);
				},

				getValue() {
					const instance = this;

					const datePicker = instance.getDatePicker();

					let value = '';

					if (datePicker) {
						const selectedDate = datePicker.getDate();

						const formattedDate = A.DataType.Date.format(
							selectedDate
						);

						const inputNode = instance.getInputNode();

						value = inputNode.val() ? formattedDate : '';
					}

					return value;
				},

				repeat() {
					const instance = this;

					instance._getTemplate((fieldTemplate) => {
						const field = instance.createField(fieldTemplate);

						const inputNode = field.getInputNode();

						Liferay.after(
							inputNode.attr('id') + 'DatePicker:registered',
							() => {
								field.renderUI();
							}
						);

						instance._addFieldValidation(field, instance);
					});
				},

				setValue(value) {
					const instance = this;

					const datePicker = instance.getDatePicker();

					if (!datePicker) {
						return;
					}

					datePicker.set('activeInput', instance.getInputNode());

					datePicker.deselectDates();

					if (value) {
						let date = A.DataType.Date.parse(value);

						if (!date) {
							datePicker.get('activeInput').val('');

							datePicker.clearSelection();

							return;
						}

						date = DateMath.add(
							date,
							DateMath.MINUTES,
							date.getTimezoneOffset()
						);

						datePicker.selectDates(date);
					}
					else {
						datePicker.get('activeInput').val('');

						datePicker.clearSelection();
					}
				},
			},
		});

		FieldTypes['ddm-date'] = DateField;

		const DocumentLibraryField = A.Component.create({
			ATTRS: {
				acceptedFileFormats: {
					value: ['*'],
				},
			},

			EXTENDS: Field,

			prototype: {
				_handleButtonsClick(event) {
					const instance = this;

					if (!instance.get('readOnly')) {
						const currentTarget = event.currentTarget;

						if (currentTarget.test('.select-button')) {
							instance._handleSelectButtonClick(event);
						}
						else if (currentTarget.test('.clear-button')) {
							instance._handleClearButtonClick(event);
						}
					}
				},

				_handleClearButtonClick() {
					const instance = this;

					instance.setValue('');
				},

				_handleSelectButtonClick() {
					const instance = this;

					const portletNamespace = instance.get('portletNamespace');

					Liferay.Util.openSelectionModal({
						onSelect: (selectedItem) => {
							if (selectedItem) {
								const itemValue = JSON.parse(
									selectedItem.value
								);

								instance.setValue({
									classPK: itemValue.fileEntryId,
									groupId: itemValue.groupId,
									title: itemValue.title,
									type: itemValue.type,
									uuid: itemValue.uuid,
								});
							}
						},
						selectEventName:
							portletNamespace + 'selectDocumentLibrary',
						title: Liferay.Language.get('select-file'),
						url: instance.getDocumentLibrarySelectorURL(),
					});
				},

				_validateField(fieldNode) {
					const instance = this;

					const liferayForm = instance.get('liferayForm');

					if (liferayForm) {
						const formValidator = liferayForm.formValidator;

						if (formValidator) {
							formValidator.validateField(fieldNode);
						}
					}
				},

				getDocumentLibrarySelectorURL() {
					const instance = this;

					const form = instance.getForm();

					const documentLibrarySelectorURL = form.get(
						'documentLibrarySelectorURL'
					);

					let retVal = instance.getDocumentLibraryURL(
						'com.liferay.item.selector.criteria.file.criterion.FileItemSelectorCriterion'
					);

					if (documentLibrarySelectorURL) {
						retVal = documentLibrarySelectorURL;
					}

					return retVal;
				},

				getDocumentLibraryURL(criteria) {
					const instance = this;

					const container = instance.get('container');

					const portletNamespace = instance.get('portletNamespace');

					const criterionJSON = {
						desiredItemSelectorReturnTypes:
							'com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType,com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType',
					};

					const uploadCriterionJSON = {
						URL: instance.getUploadURL(),
						desiredItemSelectorReturnTypes:
							'com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType,com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType',
					};

					const documentLibraryParameters = {
						'0_json': JSON.stringify(criterionJSON),
						'1_json': JSON.stringify(criterionJSON),
						'2_json': JSON.stringify(uploadCriterionJSON),
						criteria,
						'itemSelectedEventName':
							portletNamespace + 'selectDocumentLibrary',
						'p_p_auth': container.getData('itemSelectorAuthToken'),
						'p_p_id': Liferay.PortletKeys.ITEM_SELECTOR,
						'p_p_mode': 'view',
						'p_p_state': 'pop_up',
					};

					const documentLibraryURL = Liferay.Util.PortletURL.createPortletURL(
						themeDisplay.getLayoutRelativeControlPanelURL(),
						documentLibraryParameters
					);

					return documentLibraryURL.toString();
				},

				getParsedValue(value) {
					if (Lang.isString(value)) {
						if (value !== '') {
							value = JSON.parse(value);
						}
						else {
							value = {};
						}
					}

					return value;
				},

				getRuleInputName() {
					const instance = this;

					const inputName = instance.getInputName();

					return inputName + 'Title';
				},

				getUploadURL() {
					const uploadParameters = {
						'cmd': 'add_temp',
						'javax.portlet.action':
							'/document_library/upload_file_entry',
						'p_auth': Liferay.authToken,
						'p_p_id': Liferay.PortletKeys.DOCUMENT_LIBRARY,
					};

					const uploadURL = Liferay.Util.PortletURL.createActionURL(
						themeDisplay.getLayoutRelativeControlPanelURL(),
						uploadParameters
					);

					return uploadURL.toString();
				},

				initializer() {
					const instance = this;

					const container = instance.get('container');

					container.delegate(
						'click',
						instance._handleButtonsClick,
						'> .form-group .btn',
						instance
					);
				},

				setValue(value) {
					const instance = this;

					const parsedValue = instance.getParsedValue(value);

					if (!parsedValue.title && !parsedValue.uuid) {
						value = '';
					}
					else {
						value = JSON.stringify(parsedValue);
					}

					DocumentLibraryField.superclass.setValue.call(
						instance,
						value
					);

					instance.syncUI();
				},

				syncReadOnlyUI() {
					const instance = this;

					const readOnly = instance.getReadOnly();

					const container = instance.get('container');

					const selectButtonNode = container.one(
						'#' + instance.getInputName() + 'SelectButton'
					);

					selectButtonNode.attr('disabled', readOnly);

					const clearButtonNode = container.one(
						'#' + instance.getInputName() + 'ClearButton'
					);

					clearButtonNode.attr('disabled', readOnly);

					const altNode = container.one(
						'input[name=' + instance.getInputName() + 'Alt]'
					);

					if (altNode) {
						altNode.set('readOnly', readOnly);
					}
				},

				syncUI() {
					const instance = this;

					const parsedValue = instance.getParsedValue(
						instance.getValue()
					);

					const titleNode = A.one(
						'input[name=' + instance.getInputName() + 'Title]'
					);

					titleNode.val(parsedValue.title || '');

					instance._validateField(titleNode);

					const clearButtonNode = A.one(
						'#' + instance.getInputName() + 'ClearButton'
					);

					clearButtonNode.toggle(!!parsedValue.uuid);
				},
			},
		});

		FieldTypes['ddm-documentlibrary'] = DocumentLibraryField;

		const LinkToPageField = A.Component.create({
			ATTRS: {
				delta: {
					value: 10,
				},

				selectedLayout: {
					valueFn() {
						const instance = this;

						const layoutValue = instance.getParsedValue(
							instance.getValue()
						);

						let retVal = null;

						if (layoutValue.layoutId) {
							retVal = layoutValue;
						}

						return retVal;
					},
				},

				selectedLayoutPath: {
					valueFn() {
						const instance = this;

						const layoutValue = instance.getParsedValue(
							instance.getValue()
						);

						const privateLayout = !!(
							layoutValue && layoutValue.privateLayout
						);

						const groupId = instance._getGroupId();

						const layoutsRoot = {
							groupId,
							label: Liferay.Language.get('all'),
							layoutId: 0,
							privateLayout,
						};

						return [layoutsRoot];
					},
				},
			},

			EXTENDS: Field,

			prototype: {
				_addBreadcrumbElement(label, layoutId, groupId, privateLayout) {
					const instance = this;

					const breadcrumbNode = instance._modal.bodyNode.one(
						'.lfr-ddm-breadcrumb'
					);

					const breadcrumbElementNode = A.Node.create(
						Lang.sub(TPL_PAGES_BREADCRUMB_ELEMENT, {
							groupId,
							label,
							layoutId,
							privateLayout,
						})
					);

					breadcrumbNode.append(breadcrumbElementNode);
				},

				_addListElement(layout, container, selected, prepend) {
					const entryNode = A.Node.create(
						Lang.sub(TPL_PAGE, {
							checked: selected ? 'checked="checked"' : '',
							groupId: layout.groupId,
							icon: layout.hasChildren ? TPL_ICON_CARET : '',
							layoutId: layout.layoutId,
							nodeType: layout.hasChildren ? 'root' : 'leaf',
							pageTitle: layout.name,
							privateLayout: layout.privateLayout,
						})
					);

					if (prepend) {
						container.prepend(entryNode);
					}
					else {
						container.append(entryNode);
					}

					if (selected) {
						entryNode.scrollIntoView();
					}
				},

				_afterSelectedLayoutChange(event) {
					const instance = this;

					const modal = instance._modal;

					if (modal) {
						const notSelected = !event.newVal;

						const selectButton = modal.get('toolbars.footer')[0];

						const boundingBox = selectButton.boundingBox;

						boundingBox.attr('disabled', notSelected);
						boundingBox.toggleClass('disabled', notSelected);
					}
				},

				_afterSelectedLayoutPathChange(event) {
					const instance = this;

					instance._renderBreadcrumb(event.newVal);
				},

				_canLoadMore(key, start, end) {
					const instance = this;

					const cache = instance._getCache(key);

					return !cache || start < cache.start || end > cache.end;
				},

				_cleanSelectedLayout() {
					const instance = this;

					const checkedElement = instance._modal.bodyNode.one(
						'.lfr-ddm-page-radio:checked'
					);

					if (checkedElement) {
						checkedElement.attr('checked', false);

						instance.set('selectedLayout', null);
					}
				},

				_getCache(key) {
					const instance = this;

					let cache;

					if (instance._cache && instance._cache[key]) {
						cache = instance._cache[key];
					}

					return cache;
				},

				_getGroupId() {
					let groupId = themeDisplay.getScopeGroupId();

					if (!themeDisplay.isStagedPortlet()) {
						groupId = themeDisplay.getScopeGroupIdOrLiveGroupId();
					}

					return groupId;
				},

				_getModalConfig() {
					const instance = this;

					return {
						dialog: {
							cssClass: 'lfr-ddm-link-to-page-modal',
							height: 600,
							modal: true,
							on: {
								destroy() {
									instance.set('selectedLayout', null);
								},
							},
							resizable: false,
							toolbars: {
								footer: [
									{
										cssClass: 'btn-primary',
										disabled: !instance.get(
											'selectedLayout'
										),
										label: Liferay.Language.get('select'),
										on: {
											click: A.bind(
												instance._handleChooseButtonClick,
												instance
											),
										},
									},
									{
										cssClass: 'btn-link',
										label: Liferay.Language.get('cancel'),
										on: {
											click: A.bind(
												instance._handleCancelButtonClick,
												instance
											),
										},
									},
								],
								header: [
									{
										cssClass: 'close',
										discardDefaultButtonCssClasses: true,
										labelHTML: Liferay.Util.getLexiconIconTpl(
											'times'
										),
										on: {
											click: A.bind(
												instance._handleCancelButtonClick,
												instance
											),
										},
									},
								],
							},
							width: 400,
						},
						title: Liferay.Language.get('select-layout'),
					};
				},

				_handleBreadcrumbElementClick(event) {
					const instance = this;

					const currentTargetLayoutId = Number(
						event.currentTarget.getData('layoutId')
					);

					const selectedLayoutPath = instance.get(
						'selectedLayoutPath'
					);

					let lastLayoutIndex = selectedLayoutPath.length - 1;

					let lastLayout = selectedLayoutPath[lastLayoutIndex];

					let clickedLastElement =
						Number(lastLayout.layoutId) === currentTargetLayoutId;

					if (!clickedLastElement) {
						instance._cleanSelectedLayout();

						while (!clickedLastElement) {
							if (
								Number(lastLayout.layoutId) !==
								currentTargetLayoutId
							) {
								selectedLayoutPath.pop();

								lastLayoutIndex = selectedLayoutPath.length - 1;

								lastLayout =
									selectedLayoutPath[lastLayoutIndex];
							}
							else {
								clickedLastElement = true;

								const groupId = lastLayout.groupId;

								const privateLayout = lastLayout.privateLayout;

								instance._currentParentLayoutId = Number(
									currentTargetLayoutId
								);

								const bodyNode = instance._modal.bodyNode;

								const listNode = bodyNode.one(
									'.lfr-ddm-pages-container'
								);

								listNode.empty();

								instance._showLoader(listNode);

								listNode.addClass('top-ended');

								instance._requestInitialLayouts(
									currentTargetLayoutId,
									groupId,
									privateLayout,
									instance._renderLayouts
								);
							}
						}

						instance.set('selectedLayoutPath', selectedLayoutPath);
					}
				},

				_handleCancelButtonClick() {
					const instance = this;

					instance._modal.hide();
				},

				_handleChooseButtonClick() {
					const instance = this;

					const selectedLayout = instance.get('selectedLayout');

					instance.setValue(selectedLayout);

					instance._modal.hide();
				},

				_handleClearButtonClick() {
					const instance = this;

					instance._clearedModal = true;

					instance.setValue('');

					instance.set(
						'selectedLayout',
						instance.get('selectedLayoutPath')[0]
					);
				},

				_handleControlButtonsClick(event) {
					const instance = this;

					if (!instance.get('readOnly')) {
						const currentTarget = event.currentTarget;

						if (currentTarget.test('.select-button')) {
							instance._handleSelectButtonClick(event);
						}
						else {
							instance._handleClearButtonClick(event);
						}
					}
				},

				_handleListEntryClick(event) {
					const instance = this;

					const currentTarget = event.currentTarget;

					const label = event.currentTarget.text();

					const layoutId = event.currentTarget.getData('layoutId');

					const groupId = Number(
						event.currentTarget.getData('groupId')
					);

					const privateLayout = A.DataType.Boolean.parse(
						event.currentTarget.getData('privateLayout')
					);

					if (event.target.hasClass('lfr-ddm-page-label')) {
						if (currentTarget.getData('nodeType') === 'root') {
							instance._cleanSelectedLayout();

							instance._currentParentLayoutId = layoutId;

							instance._showLoader(currentTarget);

							const selectedLayoutPath = instance.get(
								'selectedLayoutPath'
							);

							selectedLayoutPath.push({
								groupId,
								label,
								layoutId,
								privateLayout,
							});

							instance.set(
								'selectedLayoutPath',
								selectedLayoutPath
							);

							const listNode = instance._modal.bodyNode.one(
								'.lfr-ddm-pages-container'
							);

							listNode.addClass('top-ended');

							instance._requestInitialLayouts(
								layoutId,
								groupId,
								privateLayout,
								instance._renderLayouts
							);
						}
						else if (
							currentTarget.getData('nodeType') === 'leaf'
						) {
							const inputRadioNode = currentTarget
								.getElementsByTagName('input')
								.first();

							inputRadioNode.attr('checked', 'true');

							instance.set('selectedLayout', {
								groupId,
								label,
								layoutId,
								path: instance.get('selectedLayoutPath'),
								privateLayout,
							});
						}
					}
					else if (event.target.hasClass('lfr-ddm-page-radio')) {
						instance.set('selectedLayout', {
							groupId,
							label,
							layoutId,
							path: instance.get('selectedLayoutPath'),
							privateLayout,
						});
					}
				},

				_handleModalScroll(event) {
					const instance = this;

					const listNode = event.currentTarget;

					const innerHeight = listNode.innerHeight();

					const scrollHeight = listNode.get('scrollHeight');
					const scrollTop = listNode.get('scrollTop');

					const delta = instance.get('delta');

					const groupId = instance._getGroupId();

					const parentLayoutId = instance._currentParentLayoutId;

					const privateLayout = !!instance._navbar
						.one('.private')
						.hasClass('active');

					const key = [parentLayoutId, groupId, privateLayout].join(
						'-'
					);

					if (!instance._isListNodeEmpty(key)) {
						const cache = instance._getCache(key);

						let end = cache.end;
						let start = cache.start;

						if (scrollTop === 0) {
							start -= delta;

							if (start < 0) {
								start = 0;
								end = cache.start;
							}

							if (end > start) {
								listNode.prepend(
									instance._loadingAnimationNode
								);

								instance._requestLayouts(
									parentLayoutId,
									groupId,
									privateLayout,
									start,
									end,
									A.rbind(
										'_renderLayoutsFragment',
										instance,
										key,
										'up'
									)
								);
							}
						}
						else if (
							scrollHeight - (scrollTop + innerHeight) <=
							1
						) {
							start = end;
							end = start + delta;

							if (
								start <= cache.total &&
								start !== cache.oldStart
							) {
								cache.oldStart = start;

								listNode.append(instance._loadingAnimationNode);

								instance._requestLayouts(
									parentLayoutId,
									groupId,
									privateLayout,
									start,
									end,
									A.rbind(
										'_renderLayoutsFragment',
										instance,
										key
									)
								);
							}
						}
					}
				},

				_handleNavbarClick(event) {
					const instance = this;

					const currentTarget = event.currentTarget;

					event.container.one('.active').removeClass('active');

					currentTarget.addClass('active');

					instance._currentParentLayoutId = 0;

					instance._cleanSelectedLayout();

					const privateLayout = currentTarget.test('.private');

					instance._resetBreadcrumb(privateLayout);
					instance._renderLayoutsList(privateLayout);
				},

				_handleSelectButtonClick() {
					const instance = this;

					instance._openLinkToPageModal();
				},

				_hideLoader() {
					const instance = this;

					instance._loadingAnimationNode.remove();
				},

				_initBreadcrumb() {
					const instance = this;

					const breadcrumbNode = A.Node.create(TPL_PAGES_BREADCRUMB);

					instance._modal.bodyNode.append(breadcrumbNode);

					breadcrumbNode.delegate(
						'click',
						instance._handleBreadcrumbElementClick,
						'.lfr-ddm-breadcrumb-element',
						instance
					);
				},

				_initLayoutsList() {
					const instance = this;

					const bodyNode = instance._modal.bodyNode;

					if (!bodyNode.one('.lfr-ddm-pages-container')) {
						const navNode = A.Node.create(TPL_PAGES_CONTAINER);

						bodyNode.append(navNode);

						navNode.delegate(
							'click',
							instance._handleListEntryClick,
							'.lfr-ddm-link',
							instance
						);
					}
				},

				_isListNodeEmpty(key) {
					const instance = this;

					const cache = instance._getCache(key);

					return !(cache && cache.layouts);
				},

				_openLinkToPageModal() {
					const instance = this;

					const value = instance.getParsedValue(instance.getValue());

					const privateLayout = !!value.privateLayout;

					let modal = instance._modal;

					if (!modal) {
						const config = instance._getModalConfig();

						modal = Liferay.Util.Window.getWindow(config);

						modal.render();

						instance._modal = modal;

						instance._initBreadcrumb();
						instance._initLayoutsList();

						instance._renderNavbar(privateLayout);
						instance._renderBreadcrumb(
							instance.get('selectedLayoutPath')
						);
						instance._renderLayoutsList(privateLayout);

						const listNode = modal.bodyNode.one(
							'.lfr-ddm-pages-container'
						);

						listNode.on(
							'scroll',
							instance._handleModalScroll,
							instance
						);
					}
					else if (instance._clearedModal) {
						instance._navbar.one('.active').removeClass('active');

						const activeClass = privateLayout
							? '.private'
							: '.public';

						instance._navbar.one(activeClass).addClass('active');
						instance._resetBreadcrumb(privateLayout);
						instance._renderLayoutsList(privateLayout);
						instance._clearedModal = false;
					}

					modal.show();

					instance._syncModalHeight();
				},

				_renderBreadcrumb(layoutsPath) {
					const instance = this;

					const bodyNode = instance._modal.bodyNode;

					const breadcrumbContainer = bodyNode.one(
						'.lfr-ddm-breadcrumb'
					);

					breadcrumbContainer.empty();

					const layoutsPathLength = layoutsPath.length;

					for (let index = 0; index < layoutsPathLength; index++) {
						const layoutPath = layoutsPath[index];

						instance._addBreadcrumbElement(
							layoutPath.label,
							layoutPath.layoutId,
							layoutPath.groupId,
							layoutPath.privateLayout
						);

						if (index < layoutsPathLength - 1) {
							if (instance._modal && instance._modal.bodyNode) {
								const breadcrumbNode = instance._modal.bodyNode.one(
									'.lfr-ddm-breadcrumb'
								);

								if (breadcrumbNode) {
									breadcrumbNode.append('&nbsp;&gt;&nbsp;');
								}
							}
						}
					}
				},

				_renderLayouts(layouts) {
					const instance = this;

					const bodyNode = instance._modal.bodyNode;

					const listNode = bodyNode.one('.lfr-ddm-pages-container');

					const selectedLayout = instance.get('selectedLayout');

					listNode.empty();

					layouts.forEach((layout) => {
						const selected =
							selectedLayout &&
							layout.layoutId === selectedLayout.layoutId;

						instance._addListElement(layout, listNode, selected);
					});

					instance._syncModalHeight();
				},

				_renderLayoutsFragment(layouts, key, direction) {
					const instance = this;

					const bodyNode = instance._modal.bodyNode;

					let index;

					const listNode = bodyNode.one('.lfr-ddm-pages-container');

					instance._hideLoader();

					const total = layouts.length;

					if (direction === 'up') {
						const cache = instance._getCache(key);

						listNode.toggleClass('top-ended', cache.start === 0);

						for (index = total - 1; index >= 0; index--) {
							instance._addListElement(
								layouts[index],
								listNode,
								false,
								true
							);
						}

						if (
							cache.start > 0 &&
							listNode.get('scrollTop') === 0
						) {
							listNode.set('scrollTop', 60);
						}
					}
					else {
						for (index = 0; index < total; index++) {
							instance._addListElement(
								layouts[index],
								listNode,
								false
							);
						}
					}

					instance._syncModalHeight();
				},

				_renderLayoutsList(privateLayout) {
					const instance = this;

					const bodyNode = instance._modal.bodyNode;

					const listNode = bodyNode.one('.lfr-ddm-pages-container');

					instance._showLoader(listNode);

					instance._syncModalHeight();

					const selectedLayout = instance.get('selectedLayout');

					const groupId = instance._getGroupId();

					if (selectedLayout && selectedLayout.layoutId) {
						instance._requestSiblingLayouts(
							groupId,
							privateLayout,
							(layouts) => {
								const key = [
									instance._currentParentLayoutId,
									groupId,
									privateLayout,
								].join('-');

								const cache = instance._getCache(key);

								listNode.toggleClass(
									'top-ended',
									cache.start === 0
								);

								instance._renderLayouts(layouts);

								if (
									cache.start > 0 &&
									listNode.get('scrollTop') === 0
								) {
									listNode.set('scrollTop', 50);
								}

								instance._hideLoader();
							}
						);
					}
					else {
						listNode.addClass('top-ended');

						instance._requestInitialLayouts(
							0,
							groupId,
							privateLayout,
							instance._renderLayouts
						);
					}
				},

				_renderNavbar(privateLayout) {
					const instance = this;

					let navbar = instance._navbar;

					if (!navbar) {
						const isPrivateLayoutsEnabled = instance.get(
							'isPrivateLayoutsEnabled'
						);

						if (isPrivateLayoutsEnabled) {
							navbar = A.Node.create(
								Lang.sub(TPL_PUBLIC_PRIVATE_LAYOUTS_NAVBAR, {
									privateLayoutClass: privateLayout
										? 'active'
										: '',
									publicLayoutClass: privateLayout
										? ''
										: 'active',
								})
							);
						}
						else {
							navbar = A.Node.create(TPL_LAYOUTS_NAVBAR);
						}

						navbar.delegate(
							'click',
							instance._handleNavbarClick,
							'li',
							instance
						);

						instance._navbar = navbar;

						navbar.insertBefore(navbar, instance._modal.bodyNode);
					}
				},

				_requestInitialLayouts(
					parentLayoutId,
					groupId,
					privateLayout,
					callback
				) {
					const instance = this;

					const end = instance.get('delta');

					const start = 0;

					instance._requestLayouts(
						parentLayoutId,
						groupId,
						privateLayout,
						start,
						end,
						callback
					);
				},

				_requestLayouts(
					parentLayoutId,
					groupId,
					privateLayout,
					start,
					end,
					callback
				) {
					const instance = this;

					const key = [parentLayoutId, groupId, privateLayout].join(
						'-'
					);

					const cache = instance._getCache(key);

					if (!cache || start <= cache.total) {
						if (instance._canLoadMore(key, start, end)) {
							const data = new URLSearchParams({
								cmd: 'get',
								end,
								expandParentLayouts: false,
								groupId,
								p_auth: Liferay.authToken,
								paginate: true,
								parentLayoutId,
								privateLayout,
								start,
							});

							Liferay.Util.fetch(
								themeDisplay.getPathMain() +
									'/portal/get_layouts',
								{
									body: data,
									method: 'POST',
								}
							)
								.then((response) => {
									return response.json();
								})
								.then((response) => {
									const layouts =
										response && response.layouts;

									if (layouts) {
										instance._updateCache(
											key,
											layouts,
											start,
											end,
											response.total
										);

										callback.call(instance, layouts);
									}
								});
						}
						else if (cache) {
							callback.call(instance, cache.layouts);
						}
					}
				},

				_requestSiblingLayouts(groupId, privateLayout, callback) {
					const instance = this;

					let cache;

					const path = instance.get('selectedLayoutPath');

					const lastIndex = path.length - 1;

					if (lastIndex >= 0) {
						const parentLayout = path[lastIndex];

						const key = [
							parentLayout.layoutId,
							parentLayout.groupId,
							parentLayout.privateLayout,
						].join('-');

						cache = instance._getCache(key);
					}

					if (cache) {
						callback.call(instance, cache.layouts);
					}
					else {
						const selectedLayout = instance.get('selectedLayout');

						const data = new URLSearchParams({
							cmd: 'getSiblingLayoutsJSON',
							expandParentLayouts: false,
							groupId,
							layoutId: selectedLayout.layoutId,
							max: instance.get('delta'),
							p_auth: Liferay.authToken,
							paginate: true,
							privateLayout,
						});

						Liferay.Util.fetch(
							themeDisplay.getPathMain() + '/portal/get_layouts',
							{
								body: data,
								method: 'POST',
							}
						)
							.then((response) => {
								return response.json();
							})
							.then((response) => {
								const layouts = response && response.layouts;

								if (layouts) {
									const parentLayoutId =
										response.ancestorLayoutIds[0];

									const key = [
										parentLayoutId,
										groupId,
										privateLayout,
									].join('-');

									const start = response.start;

									const end = start + layouts.length;

									instance._currentParentLayoutId = parentLayoutId;

									instance._setSelectedLayoutPath(
										groupId,
										privateLayout,
										response
									);

									instance._updateCache(
										key,
										layouts,
										start,
										end,
										response.total
									);

									callback.call(instance, layouts);
								}
							})
							.catch(() => {
								const bodyNode = instance._modal.bodyNode;

								const listNode = bodyNode.one(
									'.lfr-ddm-pages-container'
								);

								listNode.addClass('top-ended');

								instance._requestInitialLayouts(
									0,
									groupId,
									privateLayout,
									instance._renderLayouts
								);
							});
					}
				},

				_resetBreadcrumb(privateLayout) {
					const instance = this;

					const selectedLayoutRoot = instance.get(
						'selectedLayoutPath'
					)[0];

					selectedLayoutRoot.privateLayout = privateLayout;

					instance.set('selectedLayoutPath', [selectedLayoutRoot]);
				},

				_setSelectedLayoutPath(groupId, privateLayout, response) {
					const instance = this;

					const ancestorLayoutIds = response.ancestorLayoutIds;

					if (ancestorLayoutIds) {
						const selectedLayoutPath = [
							instance.get('selectedLayoutPath')[0],
						];

						const ancestorLayoutNames =
							response.ancestorLayoutNames;

						for (
							let index = ancestorLayoutIds.length - 1;
							index >= 0;
							index--
						) {
							selectedLayoutPath.push({
								groupId,
								label: ancestorLayoutNames[index],
								layoutId: ancestorLayoutIds[index],
								privateLayout,
							});
						}

						instance.set('selectedLayoutPath', selectedLayoutPath);
					}
				},

				_showLoader(node) {
					const instance = this;

					instance._loadingAnimationNode.appendTo(node);
				},

				_syncModalHeight() {
					const instance = this;

					const modal = instance._modal;

					const bodyNode = modal.bodyNode;

					modal.fillHeight(bodyNode);

					bodyNode.set(
						'offsetHeight',
						Lang.toInt(bodyNode.get('offsetHeight')) -
							Lang.toInt(instance._navbar.get('offsetHeight'))
					);
				},

				_updateCache(key, layouts, start, end, total) {
					const instance = this;

					let cache = instance._cache[key];

					if (!cache) {
						const path = instance.get('selectedLayoutPath');

						cache = {
							end,
							layouts,
							oldStart: 0,
							path: path.slice(),
							start,
							total,
						};

						instance._cache[key] = cache;
					}
					else {
						let cachedLayouts = cache.layouts || [];

						if (cache.start > start) {
							cachedLayouts = layouts.concat(cachedLayouts);

							cache.start = start;
						}

						if (cache.end < end) {
							cachedLayouts = cachedLayouts.concat(layouts);

							cache.end = end;
						}

						cache.layouts = cachedLayouts;
					}
				},

				_validateField(fieldNode) {
					const instance = this;

					const liferayForm = instance.get('liferayForm');

					if (liferayForm) {
						const formValidator = liferayForm.formValidator;

						if (formValidator) {
							formValidator.validateField(fieldNode);
						}
					}
				},

				getParsedValue(value) {
					if (Lang.isString(value)) {
						if (value) {
							value = JSON.parse(value);
						}
						else {
							value = {};
						}
					}

					return value;
				},

				getRuleInputName() {
					const instance = this;

					const inputName = instance.getInputName();

					return inputName + 'LayoutName';
				},

				initializer() {
					const instance = this;

					const container = instance.get('container');

					instance._currentParentLayoutId = 0;
					instance._loadingAnimationNode = A.Node.create(TPL_LOADER);

					instance._cache = {};

					instance._clearedModal = false;

					instance.after(
						'selectedLayoutChange',
						instance._afterSelectedLayoutChange
					);
					instance.after(
						'selectedLayoutPathChange',
						instance._afterSelectedLayoutPathChange
					);

					container.delegate(
						'click',
						instance._handleControlButtonsClick,
						'> .form-group .btn',
						instance
					);
				},

				setValue(value) {
					const instance = this;

					const container = instance.get('container');

					const inputName = instance.getInputName();

					const layoutNameNode = container.one(
						'input[name=' + inputName + 'LayoutName]'
					);

					const parsedValue = instance.getParsedValue(value);

					if (parsedValue && parsedValue.layoutId) {
						if (parsedValue.label) {
							layoutNameNode.val(parsedValue.label);
						}

						value = JSON.stringify(parsedValue);
					}
					else {
						layoutNameNode.val('');

						value = '';
					}

					instance._validateField(layoutNameNode);

					const clearButtonNode = container.one(
						'#' + inputName + 'ClearButton'
					);

					clearButtonNode.toggle(!!value);

					LinkToPageField.superclass.setValue.call(instance, value);
				},

				syncReadOnlyUI() {
					const instance = this;

					const readOnly = instance.getReadOnly();

					const container = instance.get('container');

					const selectButtonNode = container.one(
						'#' + instance.getInputName() + 'SelectButton'
					);

					selectButtonNode.attr('disabled', readOnly);

					const clearButtonNode = container.one(
						'#' + instance.getInputName() + 'ClearButton'
					);

					clearButtonNode.attr('disabled', readOnly);
				},
			},
		});

		FieldTypes['ddm-link-to-page'] = LinkToPageField;

		FieldTypes.field = Field;

		const FieldsetField = A.Component.create({
			EXTENDS: Field,

			prototype: {
				getFieldNodes() {
					const instance = this;

					return instance
						.get('container')
						.all('> fieldset > div > .field-wrapper');
				},
			},
		});

		FieldTypes.fieldset = FieldsetField;

		const ImageField = A.Component.create({
			ATTRS: {
				acceptedFileFormats: {
					value: [
						'image/gif',
						'image/jpeg',
						'image/jpg',
						'image/png',
					],
				},
			},

			EXTENDS: DocumentLibraryField,

			prototype: {
				_getImagePreviewURL() {
					const instance = this;

					let imagePreviewURL;

					const value = instance.getParsedValue(instance.getValue());

					if (value.data) {
						imagePreviewURL =
							themeDisplay.getPathContext() + value.data;
					}
					else if (value.uuid) {
						imagePreviewURL = [
							themeDisplay.getPathContext(),
							'documents',
							value.groupId,
							value.uuid,
						].join('/');
					}

					return imagePreviewURL;
				},

				_handleButtonsClick(event) {
					const instance = this;

					const currentTarget = event.currentTarget;

					if (currentTarget.test('.preview-button')) {
						instance._handlePreviewButtonClick(event);
					}

					ImageField.superclass._handleButtonsClick.apply(
						instance,
						arguments
					);
				},

				_handlePreviewButtonClick() {
					const instance = this;

					if (!instance.viewer) {
						instance.viewer = new A.ImageViewer({
							caption: 'alt',
							links:
								'#' +
								instance.getInputName() +
								'PreviewContainer a',
							preloadAllImages: false,
							zIndex: Liferay.zIndex.OVERLAY,
						});

						instance.viewer.TPL_CLOSE = instance.viewer.TPL_CLOSE.replace(
							/<\s*span[^>]*>(.*?)<\s*\/\s*span>/,
							Liferay.Util.getLexiconIconTpl(
								'times',
								'icon-monospaced'
							)
						);

						const TPL_PLAYER_PAUSE =
							'<span>' +
							Liferay.Util.getLexiconIconTpl(
								'pause',
								'glyphicon'
							) +
							'</span>';

						const TPL_PLAYER_PLAY =
							'<span>' +
							Liferay.Util.getLexiconIconTpl(
								'play',
								'glyphicon'
							) +
							'</span>';

						instance.viewer.TPL_PLAYER = TPL_PLAYER_PLAY;

						instance.viewer._syncPlaying = function () {
							if (this.get('playing')) {
								this._player.setHTML(TPL_PLAYER_PAUSE);
							}
							else {
								this._player.setHTML(TPL_PLAYER_PLAY);
							}
						};

						instance.viewer.render();
					}

					const imagePreviewURL = instance._getImagePreviewURL();

					const previewImageNode = A.one(
						'#' + instance.getInputName() + 'PreviewContainer img'
					);
					const previewLinkNode = A.one(
						'#' + instance.getInputName() + 'PreviewContainer a'
					);

					previewLinkNode.attr('href', imagePreviewURL);
					previewImageNode.attr('src', imagePreviewURL);

					instance.viewer.set('currentIndex', 0);
					instance.viewer.set('links', previewLinkNode);

					instance.viewer.show();
				},

				_validateField(fieldNode) {
					const instance = this;

					const liferayForm = instance.get('liferayForm');

					if (liferayForm) {
						const formValidator = liferayForm.formValidator;

						if (formValidator) {
							formValidator.validateField(fieldNode);
						}
					}
				},

				getAltRuleInputName() {
					const instance = this;

					const inputName = instance.getInputName();

					return inputName + 'Alt';
				},

				getDocumentLibrarySelectorURL() {
					const instance = this;

					const form = instance.getForm();

					const imageSelectorURL = form.get('imageSelectorURL');

					let retVal = instance.getDocumentLibraryURL(
						'com.liferay.journal.item.selector.criterion.JournalItemSelectorCriterion,com.liferay.item.selector.criteria.image.criterion.ImageItemSelectorCriterion'
					);

					if (imageSelectorURL) {
						retVal = imageSelectorURL;
					}

					return retVal;
				},

				getDocumentLibraryURL(criteria) {
					const instance = this;

					const container = instance.get('container');

					const parsedValue = instance.getParsedValue(
						ImageField.superclass.getValue.apply(
							instance,
							arguments
						)
					);

					const portletNamespace = instance.get('portletNamespace');

					const journalCriterionJSON = {
						desiredItemSelectorReturnTypes:
							'com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType,com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType',
						resourcePrimKey: parsedValue.resourcePrimKey,
					};

					const imageCriterionJSON = {
						desiredItemSelectorReturnTypes:
							'com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType,com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType',
					};

					const documentLibraryParameters = {
						'0_json': JSON.stringify(journalCriterionJSON),
						'1_json': JSON.stringify(imageCriterionJSON),
						criteria,
						'itemSelectedEventName':
							portletNamespace + 'selectDocumentLibrary',
						'p_p_auth': container.getData('itemSelectorAuthToken'),
						'p_p_id': Liferay.PortletKeys.ITEM_SELECTOR,
						'p_p_mode': 'view',
						'p_p_state': 'pop_up',
					};

					const documentLibraryURL = Liferay.Util.PortletURL.createPortletURL(
						themeDisplay.getLayoutRelativeControlPanelURL(),
						documentLibraryParameters
					);

					return documentLibraryURL.toString();
				},

				getValue() {
					const instance = this;

					let value;

					const parsedValue = instance.getParsedValue(
						ImageField.superclass.getValue.apply(
							instance,
							arguments
						)
					);

					if (instance.isNotEmpty(parsedValue)) {
						const altNode = A.one(
							'input[name=' + instance.getInputName() + 'Alt]'
						);

						parsedValue.alt = altNode.val();

						value = JSON.stringify(parsedValue);
					}
					else {
						value = '';
					}

					return value;
				},

				isNotEmpty(value) {
					const instance = this;

					const parsedValue = instance.getParsedValue(value);

					return (
						(Object.prototype.hasOwnProperty.call(
							parsedValue,
							'data'
						) &&
							parsedValue.data !== '') ||
						Object.prototype.hasOwnProperty.call(
							parsedValue,
							'uuid'
						)
					);
				},

				setValue(value) {
					const instance = this;

					const parsedValue = instance.getParsedValue(value);

					if (instance.isNotEmpty(parsedValue)) {
						if (!parsedValue.name && parsedValue.title) {
							parsedValue.name = parsedValue.title;
						}

						const altNode = A.one(
							'input[name=' + instance.getInputName() + 'Alt]'
						);

						altNode.val(parsedValue.alt);

						value = JSON.stringify(parsedValue);
					}
					else {
						value = '';
					}

					DocumentLibraryField.superclass.setValue.call(
						instance,
						value
					);

					instance.syncUI();
				},

				syncUI() {
					const instance = this;

					const parsedValue = instance.getParsedValue(
						instance.getValue()
					);

					const notEmpty = instance.isNotEmpty(parsedValue);

					const altNode = A.one(
						'input[name=' + instance.getInputName() + 'Alt]'
					);

					altNode.attr('disabled', !notEmpty);

					const titleNode = A.one(
						'input[name=' + instance.getInputName() + 'Title]'
					);

					if (notEmpty) {
						altNode.val(parsedValue.alt || '');
						titleNode.val(parsedValue.title || '');
					}
					else {
						altNode.val('');
						titleNode.val('');
					}

					instance._validateField(altNode);
					instance._validateField(titleNode);

					const clearButtonNode = A.one(
						'#' + instance.getInputName() + 'ClearButton'
					);

					clearButtonNode.toggle(notEmpty);

					const previewButtonNode = A.one(
						'#' + instance.getInputName() + 'PreviewButton'
					);

					previewButtonNode.toggle(notEmpty);
				},
			},
		});

		FieldTypes['ddm-image'] = ImageField;

		const GeolocationField = A.Component.create({
			EXTENDS: Field,

			prototype: {
				initializer() {
					const instance = this;

					Liferay.componentReady(instance.getInputName()).then(
						(map) => {
							map.on(
								'positionChange',
								instance.onPositionChange,
								instance
							);
						}
					);
				},

				onPositionChange(event) {
					const instance = this;

					const inputName = instance.getInputName();

					const location = event.newVal.location;

					instance.setValue(
						JSON.stringify({
							latitude: location.lat,
							longitude: location.lng,
						})
					);

					const locationNode = A.one(`#${inputName}Location`);

					locationNode.html(event.newVal.address);
				},
			},
		});

		FieldTypes['ddm-geolocation'] = GeolocationField;

		const TextHTMLField = A.Component.create({
			EXTENDS: Field,

			prototype: {
				_afterRenderTextHTMLField() {
					const instance = this;

					instance.editorContainer.placeAfter(instance.readOnlyText);
				},

				getEditor() {
					const instance = this;

					return window[instance.getInputName() + 'Editor'];
				},

				getValue() {
					const instance = this;

					const editor = instance.getEditor();

					return isNode(editor)
						? A.one(editor).val()
						: editor.getHTML();
				},

				initializer() {
					const instance = this;

					const editorComponentName =
						instance.getInputName() + 'Editor';

					instance.editorContainer = A.one(
						'#' + editorComponentName + 'Container'
					);

					instance.readOnlyText = A.Node.create(
						'<div class="cke_editable hide"></div>'
					);

					instance.after({
						'liferay-ddm-field:render':
							instance._afterRenderTextHTMLField,
					});
				},

				setValue(value) {
					const instance = this;

					const editorComponentName =
						instance.getInputName() + 'Editor';

					Liferay.componentReady(editorComponentName).then(function (
						editor
					) {
						if (isNode(editor)) {
							TextHTMLField.superclass.setValue.apply(
								instance,
								arguments
							);
						}
						else {
							const localizationMap = instance.get(
								'localizationMap'
							);

							if (
								value ===
									instance.getFieldDefinition()
										.predefinedValue[
										instance.get('displayLocale')
									] ||
								value ===
									localizationMap[
										instance.get('displayLocale')
									] ||
								(!localizationMap[
									instance.get('displayLocale')
								] &&
									value ===
										localizationMap[
											instance.getDefaultLocale()
										])
							) {
								editor.setHTML(value);
							}
						}
					});
				},

				syncReadOnlyUI() {
					const instance = this;

					instance.readOnlyText.html(instance.getValue());

					const readOnly = instance.getReadOnly();

					instance.readOnlyText.toggle(readOnly);

					instance.editorContainer.toggle(!readOnly);
				},
			},
		});

		FieldTypes['ddm-text-html'] = TextHTMLField;

		const RadioField = A.Component.create({
			EXTENDS: Field,

			prototype: {
				getInputNode() {
					const instance = this;

					const container = instance.get('container');

					return container.one(
						'[name=' + instance.getInputName() + ']:checked'
					);
				},

				getRadioNodes() {
					const instance = this;

					const container = instance.get('container');

					return container.all(
						'[name=' + instance.getInputName() + ']'
					);
				},

				getValue() {
					const instance = this;

					let value = '';

					if (instance.getInputNode()) {
						value = RadioField.superclass.getValue.apply(
							instance,
							arguments
						);
					}

					return value;
				},

				setLabel() {
					const instance = this;

					const container = instance.get('container');

					const fieldDefinition = instance.getFieldDefinition();

					container.all('label').each((item, index) => {
						const optionDefinition = fieldDefinition.options[index];

						const inputNode = item.one('input');

						const optionLabel =
							optionDefinition.label[
								instance.get('displayLocale')
							];

						if (Lang.isValue(optionLabel)) {
							item.html(A.Escape.html(optionLabel));

							item.prepend(inputNode);
						}
					});

					RadioField.superclass.setLabel.apply(instance, arguments);
				},

				setValue(value) {
					const instance = this;

					const radioNodes = instance.getRadioNodes();

					radioNodes.set('checked', false);

					radioNodes.each((radioNode) => {
						if (radioNode.get('value') === value) {
							radioNode.set('checked', true);
						}
					});
				},

				syncReadOnlyUI() {
					const instance = this;

					const readOnly = instance.getReadOnly();

					const radioNodes = instance.getRadioNodes();

					radioNodes.attr('disabled', readOnly);
				},
			},
		});

		FieldTypes.radio = RadioField;

		const SelectField = A.Component.create({
			EXTENDS: RadioField,

			prototype: {
				_getOptions() {
					const instance = this;

					const fieldDefinition = instance.getFieldDefinition();

					let fieldOptions = fieldDefinition.options;

					if (fieldOptions && fieldOptions[0]) {
						fieldOptions = fieldOptions.filter(
							(fieldOption) => fieldOption.value !== ''
						);
						fieldOptions.unshift(instance._getPlaceholderOption());
					}

					return fieldOptions;
				},

				_getPlaceholderOption() {
					const instance = this;
					const label = {};

					label[instance.get('displayLocale')] = '';

					return {
						label,
						value: '',
					};
				},

				getInputNode() {
					const instance = this;

					return Field.prototype.getInputNode.apply(
						instance,
						arguments
					);
				},

				getValue() {
					const instance = this;

					const selectedItems = instance
						.getInputNode()
						.all('option:selected');

					let value;

					if (selectedItems._nodes && !!selectedItems._nodes.length) {
						value = selectedItems.val();
					}
					else {
						value = [];
					}

					return value;
				},

				setLabel() {
					const instance = this;

					const options = instance._getOptions();

					instance
						.getInputNode()
						.all('option')
						.each((item, index) => {
							const optionDefinition = options[index];

							const optionLabel =
								optionDefinition.label[
									instance.get('displayLocale')
								];

							if (Lang.isValue(optionLabel)) {
								item.html(A.Escape.html(optionLabel));
							}
						});

					Field.prototype.setLabel.apply(instance, arguments);
				},

				setValue(value) {
					const instance = this;

					if (Lang.isString(value)) {
						if (value !== '') {
							value = JSON.parse(value);
						}
						else {
							value = [''];
						}
					}

					instance
						.getInputNode()
						.all('option')
						.each((item) => {
							item.set(
								'selected',
								value.indexOf(item.val()) > -1
							);
						});
				},
			},
		});

		FieldTypes.select = SelectField;

		const SeparatorField = A.Component.create({
			EXTENDS: Field,

			prototype: {
				getValue() {
					return '';
				},
			},
		});

		FieldTypes['ddm-separator'] = SeparatorField;

		const Form = A.Component.create({
			ATTRS: {
				availableLanguageIds: {
					value: [],
				},

				ddmFormValuesInput: {
					setter: A.one,
				},

				defaultEditLocale: {},

				documentLibrarySelectorURL: {},

				formNode: {
					valueFn: '_valueFormNode',
				},

				imageSelectorURL: {},

				liferayForm: {
					valueFn: '_valueLiferayForm',
				},

				repeatable: {
					validator: Lang.isBoolean,
					value: false,
				},

				requestedLocale: {
					validator: Lang.isString,
				},

				synchronousFormSubmission: {
					validator: Lang.isBoolean,
					value: true,
				},
			},

			AUGMENTS: [DDMPortletSupport, FieldsSupport],

			EXTENDS: A.Base,

			NAME: 'liferay-ddm-form',

			prototype: {
				_afterFormRegistered(event) {
					const instance = this;

					const formNode = instance.get('formNode');

					if (event.formName === formNode.attr('name')) {
						instance.set('liferayForm', event.form);
					}
				},

				_afterRenderField(event) {
					const instance = this;

					const field = event.field;

					if (field.get('repeatable')) {
						instance.registerRepeatable(field);
					}
				},

				_afterRepeatableDragAlign() {
					const DDM = A.DD.DDM;

					DDM.syncActiveShims();
					DDM._dropMove();
				},

				_afterRepeatableDragEnd(event, parentField) {
					const instance = this;

					const node = event.target.get('node');

					let oldIndex = -1;

					parentField.get('fields').some((item, index) => {
						oldIndex = index;

						return (
							item.get('instanceId') ===
							instance.extractInstanceId(node)
						);
					});

					const newIndex = node
						.ancestor()
						.all('> .field-wrapper')
						.indexOf(node);

					instance.moveField(parentField, oldIndex, newIndex);
				},

				_afterUpdateRepeatableFields(event) {
					const instance = this;

					const field = event.field;

					const liferayForm = instance.get('liferayForm');

					if (liferayForm) {
						const validatorRules = liferayForm.formValidator.get(
							'rules'
						);

						if (event.type === 'liferay-ddm-field:repeat') {
							const originalField = event.originalField;

							const originalFieldRuleInputName = originalField.getRuleInputName();

							let originalFieldRules =
								validatorRules[originalFieldRuleInputName];

							if (originalFieldRules) {
								validatorRules[
									field.getRuleInputName()
								] = originalFieldRules;
							}

							if (field.get('dataType') === 'image') {
								const originalAltRuleInputName = originalField.getAltRuleInputName();

								originalFieldRules =
									validatorRules[originalAltRuleInputName];

								if (originalFieldRules) {
									validatorRules[
										field.getAltRuleInputName()
									] = originalFieldRules;
								}
							}
						}
						else if (event.type === 'liferay-ddm-field:remove') {
							delete validatorRules[field.getRuleInputName()];

							delete liferayForm.formValidator.errors[
								field.getRuleInputName()
							];

							const inputNode = field.getInputNode();

							if (inputNode) {
								liferayForm.formValidator.resetField(inputNode);
							}

							if (field.get('repeatable')) {
								instance.unregisterRepeatable(field);
							}
						}

						liferayForm.formValidator.set('rules', validatorRules);
					}
				},

				_onDefaultLocaleChanged(event) {
					const instance = this;

					const definition = instance.get('definition');

					definition.defaultLanguageId = event.item.getAttribute(
						'data-value'
					);

					instance.set('definition', definition);
				},

				_onLiferaySubmitForm(event) {
					const instance = this;

					const formNode = instance.get('formNode');

					if (event.form.attr('name') === formNode.attr('name')) {
						instance.updateDDMFormInputValue();
					}
				},

				_onSubmitForm() {
					const instance = this;

					instance.updateDDMFormInputValue();
				},

				_updateNestedLocalizationMaps(fields) {
					const instance = this;

					fields.forEach((field) => {
						const nestedFields = field.get('fields');

						field.updateLocalizationMap(field.get('displayLocale'));

						if (nestedFields.length) {
							instance._updateNestedLocalizationMaps(
								nestedFields
							);
						}
					});
				},

				_valueFormNode() {
					const instance = this;

					const container = instance.get('container');

					return container.ancestor('form', true);
				},

				_valueLiferayForm() {
					const instance = this;

					const formNode = instance.get('formNode');

					let formName = null;

					if (formNode) {
						formName = formNode.attr('name');
					}

					return Liferay.Form.get(formName);
				},

				addAvailableLanguageIds(availableLanguageIds) {
					const instance = this;

					const currentAvailableLanguageIds = instance.get(
						'availableLanguageIds'
					);

					availableLanguageIds.forEach((item) => {
						if (currentAvailableLanguageIds.indexOf(item) === -1) {
							currentAvailableLanguageIds.push(item);
						}
					});
				},

				bindUI() {
					const instance = this;

					const formNode = instance.get('formNode');

					if (formNode) {
						instance.eventHandlers.push(
							instance.after(
								'liferay-ddm-field:render',
								instance._afterRenderField,
								instance
							),
							instance.after(
								[
									'liferay-ddm-field:repeat',
									'liferay-ddm-field:remove',
								],
								instance._afterUpdateRepeatableFields,
								instance
							),
							Liferay.after(
								'form:registered',
								instance._afterFormRegistered,
								instance
							),
							Liferay.on(
								'inputLocalized:defaultLocaleChanged',
								A.bind('_onDefaultLocaleChanged', instance)
							)
						);

						if (instance.get('synchronousFormSubmission')) {
							instance.eventHandlers.push(
								formNode.on(
									'submit',
									instance._onSubmitForm,
									instance
								),
								Liferay.on(
									'submitForm',
									instance._onLiferaySubmitForm,
									instance
								)
							);
						}
					}
				},

				destructor() {
					const instance = this;

					AArray.invoke(instance.eventHandlers, 'detach');
					AArray.invoke(instance.get('fields'), 'destroy');

					instance.get('container').remove();

					instance.eventHandlers = null;

					A.each(instance.repeatableInstances, (item) => {
						item.destroy();
					});

					instance.repeatableInstances = null;
				},

				fillEmptyLocales(instance, fields, availableLanguageIds) {
					fields.forEach((field) => {
						if (field.get('localizable')) {
							const localizationMap = field.get(
								'localizationMap'
							);

							const defaultLocale = field.getDefaultLocale();

							availableLanguageIds.forEach((locale) => {
								if (localizationMap[locale] === undefined) {
									localizationMap[locale] =
										localizationMap[defaultLocale];
								}
							});

							field.set('localizationMap', localizationMap);
						}

						instance.fillEmptyLocales(
							instance,
							field.get('fields'),
							availableLanguageIds
						);
					});
				},

				finalizeRepeatableFieldLocalizations() {
					const instance = this;

					const defaultLocale = instance.getDefaultLocale();

					Object.keys(instance.newRepeatableInstances).forEach(
						(x) => {
							const field = instance.newRepeatableInstances[x];

							if (!field.get('localizable')) {
								return;
							}

							instance.populateBlankLocalizationMap(
								defaultLocale,
								field.originalField,
								field
							);
							instance.populateBlankLocalizationMap(
								defaultLocale,
								field,
								field.originalField
							);
						}
					);
				},

				initializer() {
					const instance = this;

					instance.eventHandlers = [];
					instance.newRepeatableInstances = [];
					instance.repeatableInstances = {};

					instance.bindUI();
					instance.renderUI();
				},

				moveField(parentField, oldIndex, newIndex) {
					const instance = this;

					const fields = parentField.get('fields');

					fields.splice(newIndex, 0, fields.splice(oldIndex, 1)[0]);

					instance.recreateEditors(fields[newIndex]);
				},

				populateBlankLocalizationMap(
					defaultLocale,
					originalField,
					repeatedField
				) {
					const instance = this;

					const newFieldLocalizations = repeatedField.get(
						'localizationMap'
					);

					let totalLocalizations = {};

					if (originalField) {
						totalLocalizations = originalField.get(
							'localizationMap'
						);
					}

					const currentLocale = repeatedField.get('displayLocale');

					const localizations = Object.keys(totalLocalizations);

					localizations.push(currentLocale);

					localizations.forEach((localization) => {
						if (!newFieldLocalizations[localization]) {
							let localizationValue = '';

							if (newFieldLocalizations[defaultLocale]) {
								localizationValue =
									newFieldLocalizations[defaultLocale];
							}
							else if (
								defaultLocale ===
									repeatedField.get('displayLocale') &&
								repeatedField.getValue()
							) {
								localizationValue = repeatedField.getValue();
							}

							newFieldLocalizations[
								localization
							] = localizationValue;
						}
					});

					repeatedField.set('localizationMap', newFieldLocalizations);

					const newNestedFields = repeatedField.get('fields');

					let originalNestedFields = [];

					if (originalField) {
						originalNestedFields = originalField.get('fields');
					}

					for (let i = 0; i < newNestedFields.length; i++) {
						instance.populateBlankLocalizationMap(
							defaultLocale,
							originalNestedFields[i],
							newNestedFields[i]
						);
					}
				},

				recreateEditor(field) {
					const usingCKEditor =
						CKEDITOR &&
						CKEDITOR.instances &&
						CKEDITOR.instances[field.getInputName() + 'Editor'];

					const editor = field.getEditor();

					const nativeEditor = editor.getNativeEditor();

					const usingAlloyEditor =
						nativeEditor &&
						nativeEditor._editor &&
						nativeEditor._editor.window.$.AlloyEditor;

					if (usingCKEditor && !usingAlloyEditor) {
						const html = editor.getHTML();

						editor.dispose();

						editor.create();

						CKEDITOR.on('instanceReady', () => {
							editor.setHTML(html);
						});
					}
				},

				recreateEditors(field) {
					const instance = this;

					const fieldDefinition = field.getFieldDefinition();

					if (fieldDefinition) {
						const type = fieldDefinition.type;

						if (type === 'ddm-text-html') {
							instance.recreateEditor(field);
						}

						const nestedFields = field.get('fields');

						if (!nestedFields || !nestedFields.length) {
							return;
						}

						nestedFields.forEach((nestedField) => {
							instance.recreateEditors(nestedField);
						});
					}
				},

				registerRepeatable(field) {
					const instance = this;

					const fieldName = field.get('name');

					const fieldContainer = field.get('container');

					const parentField = field.get('parent');

					const parentNode = fieldContainer.get('parentNode');

					const treeName =
						fieldName + '_' + parentField.get('instanceId');

					let repeatableInstance =
						instance.repeatableInstances[treeName];

					if (!repeatableInstance) {
						const ddPlugins = [];

						if (Liferay.Util.getTop() === A.config.win) {
							ddPlugins.push({
								fn: A.Plugin.DDWinScroll,
							});
						}
						else {
							ddPlugins.push(
								{
									cfg: {
										constrain: '.lfr-ddm-container',
									},
									fn: A.Plugin.DDConstrained,
								},
								{
									cfg: {
										horizontal: false,
										node: '.lfr-ddm-container',
									},
									fn: A.Plugin.DDNodeScroll,
								}
							);
						}

						repeatableInstance = new Liferay.DDM.RepeatableSortableList(
							{
								dd: {
									plugins: ddPlugins,
								},
								dropOn: '#' + parentNode.attr('id'),
								helper: A.Node.create(TPL_REPEATABLE_HELPER),
								nodes:
									'#' +
									parentNode.attr('id') +
									' [data-fieldName=' +
									fieldName +
									']',
								placeholder: A.Node.create(
									'<div class="form-builder-placeholder"></div>'
								),
								sortCondition(event) {
									const dropNode = event.drop.get('node');

									const dropNodeAncestor = dropNode.ancestor();

									const dragNode = event.drag.get('node');

									const dragNodeAncestor = dragNode.ancestor();

									let retVal =
										dropNode.getData('fieldName') ===
										fieldName;

									if (
										dropNodeAncestor.get('id') !==
										dragNodeAncestor.get('id')
									) {
										retVal = false;
									}

									return retVal;
								},
							}
						);

						repeatableInstance.after(
							'drag:align',
							A.bind(instance._afterRepeatableDragAlign, instance)
						);

						repeatableInstance.after(
							'drag:end',
							A.rbind(
								instance._afterRepeatableDragEnd,
								instance,
								parentField
							)
						);

						instance.repeatableInstances[
							treeName
						] = repeatableInstance;
					}
					else {
						repeatableInstance.add(fieldContainer);
					}

					if (fieldContainer.hasAttribute('draggable')) {
						fieldContainer.removeAttribute('draggable');
					}

					const drag = A.DD.DDM.getDrag(fieldContainer);

					drag.addInvalid('.alloy-editor');
					drag.addInvalid('.cke');
					drag.addInvalid('.lfr-map');
					drag.addInvalid('.lfr-source-editor');
				},

				renderUI() {
					const instance = this;

					AArray.invoke(instance.get('fields'), 'renderUI');
				},

				toJSON() {
					const instance = this;

					const definition = instance.get('definition');

					const fieldValues = AArray.invoke(
						instance.get('fields'),
						'toJSON'
					);

					return {
						availableLanguageIds: instance.get(
							'availableLanguageIds'
						),
						defaultLanguageId:
							definition.defaultLanguageId ||
							themeDisplay.getDefaultLanguageId(),
						fieldValues,
					};
				},

				unregisterRepeatable(field) {
					field.get('container').dd.destroy();
				},

				updateDDMFormInputValue() {
					const instance = this;

					instance.toJSON();

					const fields = instance.get('fields');

					instance._updateNestedLocalizationMaps(fields);

					instance.fillEmptyLocales(
						instance,
						instance.get('fields'),
						instance.get('availableLanguageIds')
					);

					instance.finalizeRepeatableFieldLocalizations();

					const ddmFormValuesInput = instance.get(
						'ddmFormValuesInput'
					);

					ddmFormValuesInput.val(JSON.stringify(instance.toJSON()));
				},
			},
		});

		Liferay.DDM.RepeatableSortableList = A.Component.create({
			EXTENDS: A.SortableList,

			prototype: {
				_createDrag(node) {
					const instance = this;

					const helper = instance.get('helper');

					if (!A.DD.DDM.getDrag(node)) {
						const dragOptions = {
							bubbleTargets: instance,
							node,
							target: true,
						};

						const proxyOptions = instance.get('proxy');

						if (helper) {
							proxyOptions.borderStyle = null;
						}

						new A.DD.Drag(
							A.mix(dragOptions, instance.get('dd'))
						).plug(A.Plugin.DDProxy, proxyOptions);
					}
				},
			},
		});

		Liferay.DDM.Form = Form;
	},
	'',
	{
		requires: [
			'aui-base',
			'aui-color-picker-popover',
			'aui-datatable',
			'aui-datatype',
			'aui-image-viewer',
			'aui-parse-content',
			'aui-set',
			'aui-sortable-list',
			'json',
			'liferay-form',
			'liferay-layouts-tree',
			'liferay-layouts-tree-radio',
			'liferay-layouts-tree-selectable',
			'liferay-map-base',
			'liferay-translation-manager',
		],
	}
);
