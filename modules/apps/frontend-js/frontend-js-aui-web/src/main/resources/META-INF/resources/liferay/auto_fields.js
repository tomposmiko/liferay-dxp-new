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
	'liferay-auto-fields',
	(A) => {
		// eslint-disable-next-line @liferay/aui/no-object
		const AObject = A.Object;
		const Lang = A.Lang;

		const CSS_ICON_LOADING = 'loading-animation';

		const CSS_VALIDATION_HELPER_CLASSES = [
			'error',
			'error-field',
			'has-error',
			'success',
			'success-field',
		];

		const TPL_ADD_BUTTON =
			'<button class="add-row btn btn-icon-only btn-monospaced btn-secondary toolbar-first toolbar-item" title="' +
			Liferay.Language.get('add') +
			'" type="button">' +
			Liferay.Util.getLexiconIconTpl('plus') +
			'</button>';

		const TPL_DELETE_BUTTON =
			'<button class="btn btn-icon-only btn-monospaced btn-secondary delete-row toolbar-item toolbar-last" title="' +
			Liferay.Language.get('remove') +
			'" type="button">' +
			Liferay.Util.getLexiconIconTpl('hr') +
			'</button>';

		const TPL_AUTOROW_CONTROLS =
			'<span class="lfr-autorow-controls toolbar toolbar-horizontal">' +
			'<span class="toolbar-content">' +
			TPL_ADD_BUTTON +
			TPL_DELETE_BUTTON +
			'</span>' +
			'</span>';

		const TPL_LOADING = '<div class="' + CSS_ICON_LOADING + '"></div>';

		/**
		 * OPTIONS
		 *
		 * Required
		 * container {string|object}: A selector that contains the rows you wish to duplicate.
		 * baseRows {string|object}: A selector that defines which fields are duplicated.
		 *
		 * Optional
		 * fieldIndexes {string}: The name of the POST parameter that will contain a list of the order for the fields.
		 * sortable{boolean}: Whether or not the rows should be sortable
		 * sortableHandle{string}: A selector that defines a handle for the sortables
		 *
		 */

		const AutoFields = A.Component.create({
			AUGMENTS: [Liferay.PortletBase],

			EXTENDS: A.Base,

			NAME: 'autofields',

			prototype: {
				_addHandleClass(node) {
					const instance = this;

					const sortableHandle = instance.config.sortableHandle;

					if (sortableHandle) {
						node.all(sortableHandle).addClass(
							'handle-sort-vertical'
						);
					}
				},

				_attachSubmitListener() {
					Liferay.on(
						'submitForm',
						A.bind('fire', Liferay, 'saveAutoFields')
					);

					AutoFields.prototype._attachSubmitListener = Lang.emptyFn;
				},

				_clearForm(node) {
					node.all('input, select, textarea').each((item) => {
						const tag = item.get('nodeName').toLowerCase();

						const type = item.getAttribute('type');

						if (
							type === 'text' ||
							type === 'password' ||
							tag === 'textarea'
						) {
							item.val('');
						}
						else if (type === 'checkbox' || type === 'radio') {
							item.attr('checked', false);
						}
						else if (tag === 'select') {
							let selectedIndex = 0;

							if (item.getAttribute('showEmptyOption')) {
								selectedIndex = -1;
							}

							item.attr('selectedIndex', selectedIndex);
						}
					});

					CSS_VALIDATION_HELPER_CLASSES.forEach((item) => {
						node.all('.' + item).removeClass(item);
					});
				},

				_clearHiddenRows(item) {
					const instance = this;

					if (instance._isHiddenRow(item)) {
						item.remove(true);
					}
				},

				_clearInputsLocalized(node) {
					node.all('.language-value').attr('placeholder', '');
					node.all('.form-text').setHTML('');
				},

				_createClone(node) {
					const instance = this;

					const currentRow = node;

					const clone = currentRow.clone();

					const guid = instance._guid++;

					const formValidator = instance._getFormValidator(node);

					const paletteIsCloned =
						clone.one("[id$='PaletteBoundingBox']") !== null;

					const inputsLocalized = node.all('.language-value');

					let clonedRow;

					if (inputsLocalized && !paletteIsCloned) {
						const palette = document.querySelector(
							"[id$='PaletteBoundingBox']"
						);

						const trigger = clone.one('button');

						const list = A.Node.create(
							'<ul class="dropdown-menu dropdown-menu-left-side"></ul>'
						);

						trigger.placeAfter(list);

						list.append(palette);
					}

					if (instance.url) {
						clonedRow = instance._createCloneFromURL(clone, guid);
					}
					else {
						clonedRow = instance._createCloneFromMarkup(
							clone,
							guid,
							formValidator,
							inputsLocalized
						);
					}

					return clonedRow;
				},

				_createCloneFromMarkup(
					node,
					guid,
					formValidator,
					inputsLocalized
				) {
					const instance = this;

					let fieldStrings;

					let rules;

					if (formValidator) {
						fieldStrings = formValidator.get('fieldStrings');

						rules = formValidator.get('rules');
					}

					node.all('button, input, select, textarea, span, div').each(
						(item) => {
							const inputNodeName = item.attr('nodeName');
							const inputType = item.attr('type');

							let oldName = item.attr('name') || item.attr('id');

							const newName = oldName.replace(
								/([0-9]+)([_A-Za-z]*)$/,
								guid + '$2'
							);

							if (inputType === 'radio') {
								oldName = item.attr('id');

								item.attr('checked', '');
								item.attr('value', guid);
								item.attr('id', newName);
							}
							else if (
								inputNodeName === 'button' ||
								inputNodeName === 'div' ||
								inputNodeName === 'span'
							) {
								if (oldName) {
									item.attr('id', newName);
								}
							}
							else {
								item.attr('name', newName);
								item.attr('id', newName);
							}

							if (fieldStrings && fieldStrings[oldName]) {
								fieldStrings[newName] = fieldStrings[oldName];
							}

							if (rules && rules[oldName]) {
								rules[newName] = rules[oldName];
							}

							if (item.attr('aria-describedby')) {
								item.attr(
									'aria-describedby',
									newName + '_desc'
								);
							}

							node.all('label[for=' + oldName + ']').attr(
								'for',
								newName
							);
						}
					);

					instance._clearInputsLocalized(node);

					instance.once('clone', () => {
						inputsLocalized.each((item) => {
							const inputId = item.attr('id');

							instance._registerInputLocalized(
								Liferay.InputLocalized._instances[inputId],
								guid
							);
						});
					});

					node.all('.form-validator-stack').remove();
					node.all('.help-inline').remove();

					instance._clearForm(node);

					node.all('input[type=hidden]').val('');

					return node;
				},

				_createCloneFromURL(node, guid) {
					const instance = this;

					const contentBox = node.one('> div');

					contentBox.html(TPL_LOADING);

					contentBox.plug(A.Plugin.ParseContent);

					const data = {
						index: guid,
					};

					const namespace = instance.urlNamespace
						? instance.urlNamespace
						: instance.namespace;

					const namespacedData = Liferay.Util.ns(namespace, data);

					Liferay.Util.fetch(instance.url, {
						body: Liferay.Util.objectToFormData(namespacedData),
						method: 'POST',
					})
						.then((response) => response.text())
						.then((response) => contentBox.setContent(response));

					return node;
				},

				_getFormValidator(node) {
					let formValidator;

					const form = node.ancestor('form');

					if (form) {
						const formId = form.attr('id');

						formValidator = Liferay.Form.get(formId).formValidator;
					}

					return formValidator;
				},

				_guid: 0,

				_isHiddenRow(row) {
					return row.hasClass(row._hideClass || 'hide');
				},

				_makeSortable(sortableHandle) {
					const instance = this;

					const rows = instance._contentBox.all('.lfr-form-row');

					instance._addHandleClass(rows);

					instance._sortable = new A.Sortable({
						container: instance._contentBox,
						handles: [sortableHandle],
						nodes: '.lfr-form-row',
						opacity: 0,
					});

					instance._undoManager.on('clearList', () => {
						rows.all('.lfr-form-row').each((item) => {
							if (instance._isHiddenRow(item)) {
								A.DD.DDM.getDrag(item).destroy();
							}
						});
					});
				},

				_registerInputLocalized(inputLocalized, guid) {
					const inputLocalizedId = inputLocalized
						.get('id')
						.replace(/([0-9]+)$/, guid);

					const inputLocalizedNamespace = inputLocalized.get(
						'namespace'
					);

					const inputLocalizedNamespaceId = `${inputLocalizedNamespace}${inputLocalizedId}`;

					Liferay.InputLocalized.register(inputLocalizedNamespaceId, {
						boundingBox: `#${inputLocalizedNamespaceId}PaletteBoundingBox`,
						columns: inputLocalized.get('columns'),
						contentBox: `#${inputLocalizedNamespaceId}PaletteContentBox`,
						defaultLanguageId: inputLocalized.get(
							'defaultLanguageId'
						),
						fieldPrefix: inputLocalized.get('fieldPrefix'),
						fieldPrefixSeparator: inputLocalized.get(
							'fieldPrefixSeparator'
						),
						helpMessage: inputLocalized.get('helpMessage'),
						id: inputLocalizedId,
						inputBox: `#${inputLocalizedNamespaceId}BoundingBox`,
						inputPlaceholder: '#' + inputLocalizedNamespaceId,
						items: inputLocalized.get('items'),
						itemsError: inputLocalized.get('itemsError'),
						name: inputLocalizedId,
						namespace: inputLocalized.get('namespace'),
						selected: inputLocalized
							.get('items')
							.indexOf(inputLocalized.getSelectedLanguageId()),
						toggleSelection: inputLocalized.get('toggleSelection'),
						translatedLanguages: inputLocalized.get(
							'translatedLanguages'
						),
					});

					const inputLocalizedMenuId = `${inputLocalizedNamespace}${inputLocalizedNamespaceId}Menu`;

					Liferay.Menu.register(inputLocalizedMenuId);
				},

				_updateContentButtons() {
					const instance = this;

					const minimumRows = instance.minimumRows;

					if (minimumRows) {
						const deleteRowButtons = instance._contentBox.all(
							'.lfr-form-row:not(.hide) .delete-row'
						);

						Liferay.Util.toggleDisabled(
							deleteRowButtons,
							deleteRowButtons.size() <= minimumRows
						);
					}
				},

				addRow(node) {
					const instance = this;

					const clone = instance._createClone(node);

					clone.resetId();

					node.placeAfter(clone);

					const input = clone.one(
						'input[type=text], input[type=password], textarea'
					);

					if (input) {
						Liferay.Util.focusFormField(input);
					}

					instance._updateContentButtons();

					instance.fire('clone', {
						guid: instance._guid,
						originalRow: node,
						row: clone,
					});

					if (instance._sortable) {
						instance._addHandleClass(clone);
					}
				},

				deleteRow(node) {
					const instance = this;

					const contentBox = instance._contentBox;

					const visibleRows = contentBox.all('.lfr-form-row:visible');

					const visibleRowsSize = visibleRows.size();

					let deleteRow = visibleRowsSize > 1;

					if (visibleRowsSize === 1) {
						instance.addRow(node);

						deleteRow = true;
					}

					if (deleteRow) {
						const form = node.ancestor('form');

						node.hide();

						CSS_VALIDATION_HELPER_CLASSES.forEach((item) => {
							const disabledClass = item + '-disabled';

							node.all('.' + item).replaceClass(
								item,
								disabledClass
							);
						});

						let rules;

						const deletedRules = {};

						const formValidator = instance._getFormValidator(node);

						if (formValidator) {
							const errors = formValidator.errors;

							rules = formValidator.get('rules');

							node.all('input, select, textarea').each((item) => {
								const name =
									item.attr('name') || item.attr('id');

								if (rules && rules[name]) {
									deletedRules[name] = rules[name];

									delete rules[name];
								}

								if (errors && errors[name]) {
									delete errors[name];
								}
							});
						}

						instance._undoManager.add(() => {
							if (rules) {
								AObject.each(deletedRules, (item, index) => {
									rules[index] = item;
								});
							}

							CSS_VALIDATION_HELPER_CLASSES.forEach((item) => {
								const disabledClass = item + '-disabled';

								node.all('.' + disabledClass).replaceClass(
									disabledClass,
									item
								);
							});

							node.show();

							instance._updateContentButtons();

							if (form) {
								form.fire('autofields:update');
							}
						});

						instance.fire('delete', {
							deletedRow: node,
							guid: instance._guid,
						});

						if (form) {
							form.fire('autofields:update');
						}
					}

					instance._updateContentButtons();
				},

				initializer(config) {
					const instance = this;

					instance.config = config;
				},

				render() {
					const instance = this;

					const baseContainer = A.Node.create(
						'<div class="lfr-form-row"><div class="row-fields"></div></div>'
					);

					const config = instance.config;
					const contentBox = A.one(config.contentBox);

					const baseRows = contentBox.all(
						config.baseRows || '.lfr-form-row'
					);

					instance._contentBox = contentBox;
					instance._guid = baseRows.size();

					instance.minimumRows = config.minimumRows;
					instance.namespace = config.namespace;
					instance.url = config.url;
					instance.urlNamespace = config.urlNamespace;

					instance._undoManager = new Liferay.UndoManager().render(
						contentBox
					);

					if (config.fieldIndexes) {
						instance._fieldIndexes = A.all(
							'[name=' + config.fieldIndexes + ']'
						);

						if (!instance._fieldIndexes.size()) {
							instance._fieldIndexes = A.Node.create(
								'<input name="' +
									config.fieldIndexes +
									'" type="hidden" />'
							);

							contentBox.append(instance._fieldIndexes);
						}
					}
					else {
						instance._fieldIndexes = A.all([]);
					}

					contentBox.delegate(
						'click',
						(event) => {
							const link = event.currentTarget;

							const currentRow = link.ancestor('.lfr-form-row');

							if (link.hasClass('add-row')) {
								instance.addRow(currentRow);
							}
							else if (link.hasClass('delete-row')) {
								link.fire('change');

								instance.deleteRow(currentRow);
							}
						},
						'.lfr-autorow-controls .btn:not(:disabled)'
					);

					baseRows.each((item, index) => {
						let firstChild;
						let formRow;

						if (item.hasClass('lfr-form-row')) {
							formRow = item;
						}
						else {
							formRow = baseContainer.clone();
							firstChild = formRow.one('> div');
							firstChild.append(item);
						}

						formRow.append(TPL_AUTOROW_CONTROLS);

						if (!contentBox.contains(formRow)) {
							contentBox.append(formRow);
						}

						if (index === 0) {
							instance._rowTemplate = formRow.clone();
							instance._clearForm(instance._rowTemplate);
						}
					});

					instance._updateContentButtons();

					if (config.sortable) {
						instance._makeSortable(config.sortableHandle);
					}

					Liferay.on('saveAutoFields', (event) => {
						instance.save(event.form);
					});

					instance._undoManager.on('clearList', () => {
						contentBox
							.all('.lfr-form-row')
							.each(instance._clearHiddenRows, instance);
					});

					instance._attachSubmitListener();

					return instance;
				},

				reset() {
					const instance = this;

					const contentBox = instance._contentBox;

					contentBox.all('.lfr-form-row').each((item) => {
						instance.deleteRow(item);
					});

					instance._undoManager.clear();
				},

				save(form) {
					const instance = this;

					const contentBox = form || instance._contentBox;

					contentBox
						.all('.lfr-form-row')
						.each(instance._clearHiddenRows, instance);

					const fieldOrder = instance.serialize();

					instance._fieldIndexes.val(fieldOrder);
				},

				serialize(filter) {
					const instance = this;

					const visibleRows = instance._contentBox
						.all('.lfr-form-row')
						.each(instance._clearHiddenRows, instance);

					let serializedData = [];

					if (filter) {
						serializedData =
							filter.call(instance, visibleRows) || [];
					}
					else {
						visibleRows.each((item) => {
							const formField = item.one(
								'input, textarea, select'
							);

							let fieldId = formField.attr('id');

							if (!fieldId) {
								fieldId = formField.attr('name');
							}

							fieldId = (fieldId || '').match(/([0-9]+)$/);

							if (fieldId && fieldId[0]) {
								serializedData.push(fieldId[0]);
							}
						});
					}

					return serializedData.join();
				},
			},
		});

		Liferay.AutoFields = AutoFields;
	},
	'',
	{
		requires: [
			'aui-base',
			'aui-data-set-deprecated',
			'aui-parse-content',
			'base',
			'liferay-form',
			'liferay-menu',
			'liferay-portlet-base',
			'liferay-undo-manager',
			'sortable',
		],
	}
);
