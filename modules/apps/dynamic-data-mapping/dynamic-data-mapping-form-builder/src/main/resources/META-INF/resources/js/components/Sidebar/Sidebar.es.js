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

import classnames from 'classnames';
import ClayButton from 'clay-button';
import {ClayActionsDropdown, ClayDropdownBase} from 'clay-dropdown';
import {ClayIcon} from 'clay-icon';
import ClayModal from 'clay-modal';
import {
	Form,
	FormSupport,
	PagesVisitor,
	generateInstanceId,
	generateName,
} from 'dynamic-data-mapping-form-renderer';
import {makeFetch} from 'dynamic-data-mapping-form-renderer/js/util/fetch.es';
import {openModal} from 'frontend-js-web';
import dom from 'metal-dom';
import {Drag, DragDrop} from 'metal-drag-drop';
import {EventHandler} from 'metal-events';
import Component, {Fragment} from 'metal-jsx';
import {Config} from 'metal-state';

import RulesSupport from '../../components/RuleBuilder/RulesSupport.es';
import {focusedFieldStructure} from '../../util/config.es';
import {selectText} from '../../util/dom.es';
import {
	getFieldProperties,
	normalizeSettingsContextPages,
} from '../../util/fieldSupport.es';
import FieldTypeBox from '../FieldTypeBox/FieldTypeBox.es';

/**
 * Sidebar is a tooling to mount forms.
 */

class Sidebar extends Component {
	attached() {
		this._bindDragAndDrop();

		this._eventHandler.add(
			dom.on(document, 'mousedown', this._handleDocumentMouseDown, false)
		);
	}

	changeFieldType(type) {
		const {
			defaultLanguageId,
			editingLanguageId,
			fieldTypes,
			focusedField,
		} = this.props;
		const {dispatch} = this.context;
		const newFieldType = fieldTypes.find(({name}) => name === type);
		const newSettingsContext = {
			...newFieldType.settingsContext,
			pages: normalizeSettingsContextPages(
				newFieldType.settingsContext.pages,
				defaultLanguageId,
				editingLanguageId,
				newFieldType,
				focusedField.fieldName
			),
		};
		let {settingsContext} = focusedField;

		if (type !== focusedField.type) {
			settingsContext = this._mergeFieldTypeSettings(
				settingsContext,
				newSettingsContext
			);
		}

		dispatch('focusedFieldEvaluationEnded', {
			...focusedField,
			...newFieldType,
			...getFieldProperties(
				settingsContext,
				defaultLanguageId,
				editingLanguageId
			),
			changedFieldType: true,
			instanceId: generateInstanceId(8),
			settingsContext,
			type: newFieldType.name,
		});
	}

	close() {
		this.setState({
			open: false,
		});
	}

	created() {
		this._eventHandler = new EventHandler();
		const transitionEnd = this._getTransitionEndEvent();

		this.supportsTransitionEnd = transitionEnd !== false;
		this.transitionEnd = transitionEnd || 'transitionend';

		this._handleChangeFieldTypeItemClicked = this._handleChangeFieldTypeItemClicked.bind(
			this
		);
		this._handleCloseButtonClicked = this._handleCloseButtonClicked.bind(
			this
		);
		this._handleDocumentMouseDown = this._handleDocumentMouseDown.bind(
			this
		);
		this._handleDragEnded = this._handleDragEnded.bind(this);
		this._handleDragStarted = this._handleDragStarted.bind(this);
		this._handleDragTargetEnter = this._handleDragTargetEnter.bind(this);
		this._handleDragTargetLeave = this._handleDragTargetLeave.bind(this);
		this._handleEvaluatorChanged = this._handleEvaluatorChanged.bind(this);
		this._handleElementSettingsClicked = this._handleElementSettingsClicked.bind(
			this
		);
		this._handlePreviousButtonClicked = this._handlePreviousButtonClicked.bind(
			this
		);
		this._handleSettingsFieldBlurred = this._handleSettingsFieldBlurred.bind(
			this
		);
		this._handleSettingsFieldEdited = this._handleSettingsFieldEdited.bind(
			this
		);
		this._handleSettingsFormAttached = this._handleSettingsFormAttached.bind(
			this
		);
		this._handleTabItemClicked = this._handleTabItemClicked.bind(this);
		this._renderFieldTypeDropdownLabel = this._renderFieldTypeDropdownLabel.bind(
			this
		);
	}

	disposeDragAndDrop() {
		if (this._dragAndDrop) {
			this._dragAndDrop.dispose();
		}
	}

	disposeInternal() {
		super.disposeInternal();

		this._eventHandler.removeAllListeners();
		this.disposeDragAndDrop();
	}

	getDropTargetsSelector() {
		return '.ddm-target:not([data-drop-disabled="true"])';
	}

	getSettingsFormContext() {
		const {
			defaultLanguageId,
			editingLanguageId,
			focusedField,
			readOnlyFieldName,
		} = this.props;
		const {settingsContext} = focusedField;
		const visitor = new PagesVisitor(settingsContext.pages);

		return {
			...settingsContext,
			pages: visitor.mapFields((field) => {
				const updatedField = {
					...field,
					defaultLanguageId,
					editingLanguageId,
					keywordReadOnly:
						field.fieldName == 'columns' ||
						field.fieldName == 'options' ||
						field.fieldName == 'rows'
							? readOnlyFieldName
							: false,
					readOnly:
						field.fieldName == 'name' ? readOnlyFieldName : false,
					visible: field.fieldName !== 'name' ? field.visible : false,
				};

				return {
					...updatedField,
					name: generateName(field.name, updatedField),
				};
			}),
		};
	}

	open() {
		const {container} = this.refs;
		const {open} = this.state;
		const {transitionEnd} = this;

		if (open) {
			return;
		}

		dom.once(container, transitionEnd, () => {
			if (this._isEditMode()) {
				const firstInput = this.element.querySelector('input');

				if (firstInput && !container.contains(document.activeElement)) {
					firstInput.focus();
					selectText(firstInput);
				}
			}
		});

		this.setState({
			activeTab: 0,
			open: true,
		});

		this.refreshDragAndDrop();
	}

	refreshDragAndDrop() {
		this._dragAndDrop.setState({
			targets: this.getDropTargetsSelector(),
		});
	}

	render() {
		const {activeTab, open} = this.state;
		const {spritemap} = this.props;
		const editMode = this._isEditMode();
		const styles = classnames('sidebar-container', {open});

		return (
			<div>
				<div class={styles} ref="container">
					<div class="sidebar sidebar-light">
						<nav class="component-tbar tbar">
							<div class="container-fluid">
								{this._renderTopBar()}
							</div>
						</nav>
						<nav class="component-navigation-bar navbar navigation-bar navbar-collapse-absolute navbar-expand-md navbar-underline">
							<a
								aria-controls="sidebarLightCollapse00"
								aria-expanded="false"
								aria-label="Toggle Navigation"
								class="collapsed navbar-toggler navbar-toggler-link"
								data-toggle="liferay-collapse"
								href="#sidebarLightCollapse00"
								role="button"
							>
								<span class="navbar-text-truncate">
									{'Details'}
								</span>
								<svg
									aria-hidden="true"
									class="lexicon-icon lexicon-icon-caret-bottom"
								>
									<use
										xlink:href={`${spritemap}#caret-bottom`}
									/>
								</svg>
							</a>
							<div
								class="collapse navbar-collapse"
								id="sidebarLightCollapse00"
							>
								<ul class="nav navbar-nav" role="tablist">
									{this._renderNavItems()}
								</ul>
							</div>
						</nav>
						<div class="ddm-sidebar-body">
							{!editMode &&
								activeTab == 0 &&
								this._renderFieldTypeGroups()}

							{!editMode &&
								activeTab == 1 &&
								this._renderElementSets()}

							{editMode && (
								<div class="sidebar-body ddm-field-settings">
									<div class="tab-content">
										<form>
											{this._renderSettingsForm()}
										</form>
									</div>
								</div>
							)}
						</div>
					</div>
				</div>

				<ClayModal
					body={Liferay.Language.get(
						'are-you-sure-you-want-to-cancel'
					)}
					events={{
						clickButton: this._handleCancelChangesModalButtonClicked.bind(
							this
						),
					}}
					footerButtons={[
						{
							alignment: 'right',
							label: Liferay.Language.get('dismiss'),
							style: 'primary',
							type: 'close',
						},
						{
							alignment: 'right',
							label: Liferay.Language.get('yes-cancel'),
							style: 'primary',
							type: 'button',
						},
					]}
					ref="cancelChangesModal"
					size="sm"
					spritemap={spritemap}
					title={Liferay.Language.get(
						'cancel-field-changes-question'
					)}
				/>
			</div>
		);
	}

	syncEditingLanguageId() {
		const {dispatch} = this.context;
		const {evaluableForm} = this.refs;
		const {editingLanguageId, focusedField} = this.props;

		if (evaluableForm && evaluableForm.reactComponentRef.current) {
			evaluableForm.reactComponentRef.current
				.evaluate(editingLanguageId)
				.then((pages) => {
					dispatch('focusedFieldEvaluationEnded', {
						...focusedField,
						changedEditingLanguage: true,
						settingsContext: {
							...focusedField.settingsContext,
							pages,
						},
					});
				})
				.catch((error) => dispatch('evaluationError', error));
		}
	}

	syncVisible(visible) {
		if (!visible) {
			this.dispatchFieldBlurred();
		}
	}

	_bindDragAndDrop() {
		this._dragAndDrop = new DragDrop({
			container: document.body,
			dragPlaceholder: Drag.Placeholder.CLONE,
			sources: '.ddm-drag-item',
			targets: this.getDropTargetsSelector(),
			useShim: false,
		});

		this._eventHandler.add(
			this._dragAndDrop.on(Drag.Events.START, this._handleDragStarted),
			this._dragAndDrop.on(DragDrop.Events.END, this._handleDragEnded),
			this._dragAndDrop.on(
				DragDrop.Events.TARGET_ENTER,
				this._handleDragTargetEnter
			),
			this._dragAndDrop.on(
				DragDrop.Events.TARGET_LEAVE,
				this._handleDragTargetLeave
			)
		);
	}

	_cancelFieldChanges() {
		const {cancelChangesModal} = this.refs;

		cancelChangesModal.show();
	}

	_deleteField(fieldName) {
		const {dispatch} = this.context;

		dispatch('fieldDeleted', {fieldName});
	}

	dispatchFieldBlurred() {
		const {dispatch} = this.context;

		if (!this.isDisposed()) {
			dispatch('sidebarFieldBlurred');
		}
	}

	_dropdownFieldTypesValueFn() {
		const {fieldTypes} = this.props;

		return fieldTypes
			.filter(({system}) => {
				return !system;
			})
			.map((fieldType) => {
				return {
					...fieldType,
					type: 'item',
				};
			});
	}

	_duplicateField(fieldName) {
		const {dispatch} = this.context;

		dispatch('fieldDuplicated', {fieldName});
	}

	_fetchElementSet(fieldSetId) {
		const {
			editingLanguageId,
			fieldSetDefinitionURL,
			groupId,
			portletNamespace,
		} = this.props;

		return makeFetch({
			method: 'GET',
			url: `${fieldSetDefinitionURL}?ddmStructureId=${fieldSetId}&languageId=${editingLanguageId}&portletNamespace=${portletNamespace}&scopeGroupId=${groupId}`,
		})
			.then(({pages}) => pages)
			.catch((error) => {
				throw new Error(error);
			});
	}

	_fieldTypesGroupValueFn() {
		const {fieldTypes} = this.props;
		const group = {
			basic: {
				fields: [],
				label: Liferay.Language.get('field-types-basic-elements'),
			},
			customized: {
				fields: [],
				label: Liferay.Language.get('field-types-customized-elements'),
			},
		};

		return fieldTypes.reduce((prev, next) => {
			if (next.group && !next.system) {
				if (next.group === 'interface') {
					prev.basic.fields.push(next);
				}
				else {
					prev[next.group].fields.push(next);
				}
			}

			return prev;
		}, group);
	}

	_getTabItems() {
		if (!this._isEditMode()) {
			return this.state.tabs;
		}

		const {focusedField} = this.props;
		const {settingsContext} = focusedField;

		return settingsContext.pages.map(({title}) => title);
	}

	_getTransitionEndEvent() {
		const el = document.createElement('metalClayTransitionEnd');

		const transitionEndEvents = {
			MozTransition: 'transitionend',
			OTransition: 'oTransitionEnd otransitionend',
			WebkitTransition: 'webkitTransitionEnd',
			transition: 'transitionend',
		};

		let eventName = false;

		Object.keys(transitionEndEvents).some((name) => {
			if (el.style[name] !== undefined) {
				eventName = transitionEndEvents[name];

				return true;
			}
		});

		return eventName;
	}

	_handleCancelChangesModalButtonClicked(event) {
		const {dispatch} = this.context;
		const {target} = event;
		const {cancelChangesModal} = this.refs;

		event.stopPropagation();

		if (this._isOutsideModal(target)) {
			this.close();
		}

		cancelChangesModal.emit('hide');

		if (!event.target.classList.contains('close-modal')) {
			dispatch('fieldChangesCanceled', {});
		}
	}

	_handleChangeFieldTypeItemClicked({data}) {
		const newFieldType = data.item.name;
		const {fieldName} = this.props.focusedField;
		const {rules} = this.props;

		if (rules && RulesSupport.findRuleByFieldName(fieldName, null, rules)) {
			const dropdown = document.querySelector('.dropdown-menu.show');

			dropdown.classList.remove('show');

			openModal({
				bodyHTML: Liferay.Language.get(
					'a-rule-is-applied-to-this-field-by-changing-its-type'
				),
				buttons: [
					{
						displayType: 'secondary',
						label: Liferay.Language.get('cancel'),
						type: 'cancel',
					},
					{
						displayType: 'danger',
						label: Liferay.Language.get('change-field-type'),
						onClick: () => {
							this._handleChangeFieldTypeModalButtonClicked(
								newFieldType
							);
						},
						type: 'cancel',
					},
				],
				id: 'ddm-change-field-type-with-rule-modal',
				size: 'md',
				title: Liferay.Language.get(
					'change-field-type-with-rule-applied'
				),
			});
		}
		else {
			this.changeFieldType(newFieldType);
		}
	}

	_handleChangeFieldTypeModalButtonClicked(newFieldType) {
		this.changeFieldType(newFieldType);
	}

	_handleCloseButtonClicked() {
		this.close();
	}

	_handleDocumentMouseDown({target}) {
		const {transitionEnd} = this;
		const {open} = this.state;

		const ckeContext = target
			? target.closest('.cke_dialog_container')
			: undefined;

		if (
			this._isCloseButton(target) ||
			(open &&
				!ckeContext &&
				!this._isControlProductMenuItem(target) &&
				!this._isProductMenuSidebarItem(target) &&
				!this._isSidebarElement(target) &&
				!this._isTranslationItem(target) &&
				!this._isModalElement(target))
		) {
			this.close();

			dom.once(this.refs.container, transitionEnd, () =>
				this.dispatchFieldBlurred()
			);

			if (!this._isModalElement(target)) {
				setTimeout(() => this.dispatchFieldBlurred(), 500);
			}
		}
	}

	_handleDragEnded(data, event) {
		const {dispatch} = this.context;

		event.preventDefault();

		if (!data.target) {
			return;
		}

		this._handleDragTargetLeave(data);

		const {fieldTypes} = this.props;
		const {fieldSetId} = data.source.dataset;
		const columnNode = dom.closest(data.target, '.col-ddm');
		const indexes = FormSupport.getIndexes(columnNode);

		if (fieldSetId) {
			this._fetchElementSet(fieldSetId).then((pages) => {
				dispatch('elementSetAdded', {
					data,
					fieldSetId,
					fieldSetPages: pages,
					indexes,
				});
			});
		}
		else {
			const fieldType = fieldTypes.find(({name}) => {
				return name === data.source.dataset.fieldTypeName;
			});
			let parentFieldName;
			const parentFieldNode = dom.closest(
				data.target.parentElement,
				'.ddm-field'
			);

			if (parentFieldNode) {
				parentFieldName = parentFieldNode.dataset.fieldName;
			}

			const payload = {
				data: {
					...data,
					fieldName: data.target.dataset.fieldName,
					parentFieldName,
				},
				fieldType: {
					...fieldType,
					editable: true,
				},
				indexes,
			};

			if (dom.closest(data.target, '.col-empty')) {
				const addedToPlaceholder = dom.closest(
					data.target,
					'.placeholder'
				);

				dispatch('fieldAdded', {
					...payload,
					addedToPlaceholder,
				});
			}
			else {
				dispatch('sectionAdded', payload);
			}
		}
	}

	_handleDragStarted() {
		this.refreshDragAndDrop();

		this.close();
	}

	_handleDragTargetEnter({target}) {
		const parentFieldNode = dom.closest(
			target.parentElement,
			`.ddm-field-container`
		);

		if (parentFieldNode) {
			parentFieldNode.classList.add('active-drop-child');
		}
	}

	_handleDragTargetLeave({target}) {
		const parentFieldNode = dom.closest(
			target.parentElement,
			`.ddm-field-container`
		);

		if (parentFieldNode) {
			parentFieldNode.classList.remove('active-drop-child');
		}
	}

	_handleEvaluatorChanged(pages) {
		const {dispatch} = this.context;
		const {focusedField} = this.props;

		dispatch('focusedFieldEvaluationEnded', {
			...focusedField,
			settingsContext: {
				...focusedField.settingsContext,
				pages,
			},
		});
	}

	_handleElementSettingsClicked({data: {item}}) {
		const {
			columnIndex,
			fieldName,
			pageIndex,
			rowIndex,
		} = this.props.focusedField;
		const {settingsItem} = item;
		const indexes = {
			columnIndex,
			pageIndex,
			rowIndex,
		};

		if (!item.disabled) {
			if (settingsItem === 'duplicate-field') {
				this._duplicateField(fieldName);
			}
			else if (settingsItem === 'delete-field') {
				const {rules} = this.props;

				if (
					rules &&
					RulesSupport.findRuleByFieldName(fieldName, null, rules)
				) {
					const dropdown = document.querySelector(
						'.dropdown-menu.show'
					);

					dropdown.classList.remove('show');
				}

				this._deleteField(fieldName);
			}
			else if (settingsItem === 'cancel-field-changes') {
				this._cancelFieldChanges(indexes);
			}
		}
	}

	_handlePreviousButtonClicked() {
		const {transitionEnd} = this;

		this.close();

		dom.once(this.refs.container, transitionEnd, () => {
			this.dispatchFieldBlurred();
			this.open();
		});
	}

	_handleSettingsFieldBlurred({fieldInstance, value}) {
		const {dispatch} = this.context;
		const {editingLanguageId} = this.props;
		const {fieldName} = fieldInstance;

		dispatch('fieldBlurred', {
			editingLanguageId,
			propertyName: fieldName,
			propertyValue: value,
		});
	}

	_handleSettingsFieldEdited({fieldInstance, value}) {
		if (fieldInstance && !fieldInstance.isDisposed() && this.state.open) {
			const {editingLanguageId} = this.props;
			const {fieldName} = fieldInstance;
			const {dispatch} = this.context;

			dispatch('fieldEdited', {
				editingLanguageId,
				propertyName: fieldName,
				propertyValue: value,
			});
		}
	}

	_handleSettingsFormAttached() {
		const reactForm = this.refs.evaluableForm.reactComponentRef.current;
		const {editingLanguageId} = this.props;

		if (reactForm) {
			reactForm.evaluate(editingLanguageId);
		}
	}

	_handleTabItemClicked(event) {
		const {target} = event;
		const {
			dataset: {index},
		} = dom.closest(target, '.nav-item');

		event.preventDefault();

		this.setState({
			activeTab: parseInt(index, 10),
		});
	}

	_isCloseButton(node) {
		const {closeButton} = this.refs;

		return closeButton.contains(node);
	}

	_isControlProductMenuItem(node) {
		return !!dom.closest(node, '.sidenav-toggler');
	}

	_isEditMode() {
		const {focusedField} = this.props;

		return Object.keys(focusedField).length > 0;
	}

	_isModalElement(node) {
		return dom.closest(node, '.modal');
	}

	_isProductMenuSidebarItem(node) {
		return !!dom.closest(node, '.sidenav-menu');
	}

	_isOutsideModal(node) {
		return !dom.closest(node, '.close-modal');
	}

	_isSettingsElement(target) {
		const {fieldSettingsActions} = this.refs;
		let dropdownPortal;

		if (fieldSettingsActions) {
			const {dropdown} = fieldSettingsActions.refs;
			const {portal} = dropdown.refs;

			dropdownPortal = portal.element.contains(target);
		}

		return dropdownPortal;
	}

	_isSidebarElement(node) {
		const {element} = this;
		const alloyEditorToolbarNode = dom.closest(node, '.ae-ui');
		const fieldColumnNode = dom.closest(node, '.col-ddm');
		const fieldTypesDropdownNode = dom.closest(node, '.dropdown-menu');

		return (
			alloyEditorToolbarNode ||
			fieldTypesDropdownNode ||
			fieldColumnNode ||
			element.contains(node) ||
			this._isSettingsElement(node)
		);
	}

	_isTranslationItem(node) {
		return !!dom.closest(node, '.lfr-translationmanager');
	}

	_mergeFieldTypeSettings(oldSettingsContext, newSettingsContext) {
		const newVisitor = new PagesVisitor(newSettingsContext.pages);
		const oldVisitor = new PagesVisitor(oldSettingsContext.pages);

		const excludedFields = ['indexType', 'readOnly', 'type', 'validation'];

		const getPreviousField = ({fieldName, type}) => {
			let field;

			oldVisitor.findField((oldField) => {
				if (
					excludedFields.indexOf(fieldName) === -1 &&
					oldField.fieldName === fieldName &&
					oldField.type === type
				) {
					field = oldField;
				}

				return field;
			});

			return field;
		};

		return {
			...newSettingsContext,
			pages: newVisitor.mapFields((newField) => {
				if (newField.visible) {
					const previousField = getPreviousField(newField);

					if (previousField) {
						newField.value = previousField.value;

						if (newField.localizable && previousField.localizable) {
							newField.localizedValue = {
								...previousField.localizedValue,
							};
						}
					}

					if (newField.fieldName == 'predefinedValue') {
						delete newField.value;

						newField.localizedValue = {};

						if (newField.options) {
							newField.options = this._getPredefinedOptions(
								newVisitor
							);
						}
					}
				}

				return newField;
			}),
		};
	}

	_getPredefinedOptions(visitor) {
		const options = visitor.findField((field) => {
			return field.fieldName == 'options';
		});

		if (options) {
			const locale = options.locale;

			return options.value[locale];
		}

		return options;
	}

	_renderElementSets() {
		const {fieldSets} = this.props;
		const groups = Object.keys(fieldSets);

		let elementSetsArea = '';

		if (groups.length > 0) {
			elementSetsArea = this._renderElementSetsGroups(groups);
		}
		else {
			elementSetsArea = this._renderEmptyElementSets();
		}

		return elementSetsArea;
	}

	_renderElementSetsGroups(groups) {
		const {fieldSets, spritemap} = this.props;

		return (
			<div
				aria-orientation="vertical"
				class="ddm-field-types-panel panel-group"
				id="accordion03"
				role="tablist"
			>
				{groups.map((key) => (
					<div
						aria-labelledby={`#ddm-field-types-${key}-header`}
						class="panel-collapse show"
						id={`ddm-field-types-${key}-body`}
						key={key}
						role="tabpanel"
					>
						<div class="panel-body p-0 m-0 list-group">
							<div
								class="ddm-drag-item list-group-item list-group-item-flex"
								data-field-set-id={fieldSets[key].id}
								data-field-set-name={fieldSets[key].name}
								key={`fieldType_${fieldSets[key].name}`}
								ref={`fieldType_${fieldSets[key].name}`}
							>
								<div class="autofit-col">
									<span class="sticker sticker-secondary">
										<span class="inline-item">
											<svg
												aria-hidden="true"
												class={`lexicon-icon lexicon-icon-${fieldSets[key].icon}`}
											>
												<use
													xlink:href={`${spritemap}#${fieldSets[key].icon}`}
												/>
											</svg>
										</span>
									</span>
								</div>
								<div class="autofit-col autofit-col-expand">
									<h4 class="list-group-title text-truncate">
										<span>{fieldSets[key].name}</span>
									</h4>
								</div>
							</div>
						</div>
					</div>
				))}
			</div>
		);
	}

	_renderEmptyElementSets() {
		return (
			<div class="list-group-body  list-group">
				<div class="main-content-body">
					<div class="text-center text-muted">
						<p class="text-default">
							{Liferay.Language.get(
								'there-are-no-element-sets-yet'
							)}
						</p>
					</div>
				</div>
			</div>
		);
	}

	_renderFieldTypeDropdownLabel() {
		const {fieldTypes, focusedField, spritemap} = this.props;
		const {icon, label} = fieldTypes.find(
			({name}) => name === focusedField.type
		);

		return (
			<Fragment>
				<ClayIcon
					elementClasses={'inline-item inline-item-before'}
					spritemap={spritemap}
					symbol={icon}
				/>
				{label}
				<ClayIcon
					elementClasses={'inline-item inline-item-after'}
					spritemap={spritemap}
					symbol={'caret-bottom'}
				/>
			</Fragment>
		);
	}

	_renderFieldTypeGroups() {
		const {spritemap} = this.props;
		const {fieldTypesGroup} = this.state;
		const group = Object.keys(fieldTypesGroup);

		return (
			<div
				aria-orientation="vertical"
				class="ddm-field-types-panel panel-group"
				id="accordion03"
				role="tablist"
			>
				{group.map((key, index) => (
					<div
						class="panel panel-secondary"
						key={`fields-group-${key}-${index}`}
					>
						<a
							aria-controls="collapseTwo"
							aria-expanded="true"
							class="collapse-icon panel-header panel-header-link"
							data-parent="#accordion03"
							data-toggle="liferay-collapse"
							href={`#ddm-field-types-${key}-body`}
							id={`ddm-field-types-${key}-header`}
							role="tab"
						>
							<span class="panel-title">
								{fieldTypesGroup[key].label}
							</span>
							<span class="collapse-icon-closed">
								<svg
									aria-hidden="true"
									class="lexicon-icon lexicon-icon-angle-right"
								>
									<use
										xlink:href={`${spritemap}#angle-right`}
									/>
								</svg>
							</span>
							<span class="collapse-icon-open">
								<svg
									aria-hidden="true"
									class="lexicon-icon lexicon-icon-angle-down"
								>
									<use
										xlink:href={`${spritemap}#angle-down`}
									/>
								</svg>
							</span>
						</a>
						<div
							aria-labelledby={`#ddm-field-types-${key}-header`}
							class="panel-collapse show"
							id={`ddm-field-types-${key}-body`}
							role="tabpanel"
						>
							<div class="panel-body p-0 m-0 list-group">
								{fieldTypesGroup[key].fields.map(
									(fieldType) => (
										<FieldTypeBox
											fieldType={fieldType}
											key={fieldType.name}
											spritemap={spritemap}
										/>
									)
								)}
							</div>
						</div>
					</div>
				))}
			</div>
		);
	}

	_renderNavItems() {
		const {activeTab} = this.state;

		return this._getTabItems().map((name, index) => {
			const style = classnames('nav-link', {
				active: index === activeTab,
			});

			return (
				<li
					class="nav-item"
					data-index={index}
					data-onclick={this._handleTabItemClicked}
					key={`tab${index}`}
					ref={`tab${index}`}
				>
					<a
						aria-controls="sidebarLightDetails"
						class={style}
						href="javascript:;"
						role="tab"
					>
						<span class="navbar-text-truncate">{name}</span>
					</a>
				</li>
			);
		});
	}

	_renderSettingsForm() {
		const {activeTab} = this.state;
		const {
			defaultLanguageId,
			editingLanguageId,
			portletNamespace,
			rules: builderRules,
			spritemap,
		} = this.props;
		const {pages, rules} = this.getSettingsFormContext();
		const sidebarTabIndex = pages.length - 1;

		if (sidebarTabIndex < activeTab) {
			this.setState({
				activeTab: sidebarTabIndex,
			});
		}

		return (
			<Form
				activePage={activeTab}
				builderRules={builderRules}
				defaultLanguageId={defaultLanguageId}
				editable={true}
				editingLanguageId={editingLanguageId}
				events={{
					attached: this._handleSettingsFormAttached,
					evaluated: this._handleEvaluatorChanged,
					fieldBlurred: this._handleSettingsFieldBlurred,
					fieldEdited: this._handleSettingsFieldEdited,
				}}
				pages={pages}
				paginationMode="tabbed"
				portletNamespace={portletNamespace}
				ref={`evaluableForm`}
				rules={rules}
				spritemap={spritemap}
			/>
		);
	}

	_renderTopBar() {
		const {fieldTypes, focusedField, spritemap} = this.props;
		const editMode = this._isEditMode();
		const fieldActions = [
			{
				label: Liferay.Language.get('duplicate-field'),
				settingsItem: 'duplicate-field',
			},
			{
				label: Liferay.Language.get('remove-field'),
				settingsItem: 'delete-field',
			},
			{
				label: Liferay.Language.get('cancel-field-changes'),
				settingsItem: 'cancel-field-changes',
			},
		];
		const focusedFieldType = fieldTypes.find(
			({name}) => name === focusedField.type
		);
		const previousButtonEvents = {
			click: this._handlePreviousButtonClicked,
		};

		return (
			<ul class="tbar-nav">
				{!editMode && (
					<li class="tbar-item tbar-item-expand text-left">
						<div class="tbar-section">
							<span class="text-truncate-inline">
								<span class="text-truncate">
									{Liferay.Language.get('add-elements')}
								</span>
							</span>
						</div>
					</li>
				)}
				{editMode && (
					<Fragment>
						<li class="tbar-item">
							<ClayButton
								elementClasses="nav-link"
								events={previousButtonEvents}
								icon="angle-left"
								ref="previousButton"
								size="sm"
								spritemap={spritemap}
								style="secondary"
							/>
						</li>
						<li class="tbar-item ddm-fieldtypes-dropdown tbar-item-expand text-left">
							<div>
								<ClayDropdownBase
									events={{
										itemClicked: this
											._handleChangeFieldTypeItemClicked,
									}}
									icon={focusedFieldType.icon}
									items={this.state.dropdownFieldTypes}
									itemsIconAlignment={'left'}
									label={this._renderFieldTypeDropdownLabel}
									spritemap={spritemap}
									style={'secondary'}
									triggerClasses={'nav-link btn-sm'}
								/>
							</div>
						</li>
						<li class="tbar-item">
							<ClayActionsDropdown
								events={{
									itemClicked: this
										._handleElementSettingsClicked,
								}}
								items={fieldActions}
								ref="fieldSettingsActions"
								spritemap={spritemap}
								triggerClasses={'component-action'}
							/>
						</li>
					</Fragment>
				)}
				<li class="tbar-item">
					<a
						class="component-action sidebar-close"
						data-onclick={this._handleCloseButtonClicked}
						href="javascript:;"
						ref="closeButton"
						role="button"
					>
						<svg
							aria-hidden="true"
							class="lexicon-icon lexicon-icon-times"
						>
							<use xlink:href={`${spritemap}#times`} />
						</svg>
					</a>
				</li>
			</ul>
		);
	}
}

Sidebar.STATE = {

	/**
	 * @default 0
	 * @instance
	 * @memberof Sidebar
	 * @type {?number}
	 */

	activeTab: Config.number().value(0).internal(),

	/**
	 * @default _dropdownFieldTypesValueFn
	 * @instance
	 * @memberof Sidebar
	 * @type {?array}
	 */

	dropdownFieldTypes: Config.array().valueFn('_dropdownFieldTypesValueFn'),

	/**
	 * @instance
	 * @memberof Sidebar
	 * @type {array}
	 */

	fieldTypesGroup: Config.object().valueFn('_fieldTypesGroupValueFn'),

	/**
	 * @default false
	 * @instance
	 * @memberof Sidebar
	 * @type {?bool}
	 */

	open: Config.bool().internal().value(false),

	/**
	 * @default object
	 * @instance
	 * @memberof Sidebar
	 * @type {?object}
	 */

	tabs: Config.object()
		.value([
			Liferay.Language.get('elements'),
			Liferay.Language.get('element-sets'),
		])
		.internal(),
};

Sidebar.PROPS = {

	/**
	 * @default undefined
	 * @instance
	 * @memberof Sidebar
	 * @type {?string}
	 */

	defaultLanguageId: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Sidebar
	 * @type {?string}
	 */

	editingLanguageId: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Sidebar
	 * @type {?string}
	 */

	fieldSetDefinitionURL: Config.string(),

	/**
	 * @default []
	 * @instance
	 * @memberof Sidebar
	 * @type {?(array|undefined)}
	 */

	fieldSets: Config.array().value([]),

	/**
	 * @default []
	 * @instance
	 * @memberof Sidebar
	 * @type {?(array|undefined)}
	 */

	fieldTypes: Config.array().value([]),

	/**
	 * @default {}
	 * @instance
	 * @memberof Sidebar
	 * @type {?object}
	 */

	focusedField: focusedFieldStructure.value({}),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Sidebar
	 * @type {?string}
	 */

	portletNamespace: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Sidebar
	 * @type {?bool}
	 */

	readOnlyFieldName: Config.bool().value(true),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Sidebar
	 * @type {?(string|undefined)}
	 */

	spritemap: Config.string().required(),
};

export default Sidebar;
