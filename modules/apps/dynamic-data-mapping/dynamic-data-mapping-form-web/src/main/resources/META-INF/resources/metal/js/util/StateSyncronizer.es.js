import * as FormSupport from 'dynamic-data-mapping-form-builder/metal/js/components/Form/FormSupport.es';
import Component from 'metal-jsx';
import {Config} from 'metal-state';
import {EventHandler} from 'metal-events';
import {PagesVisitor} from 'dynamic-data-mapping-form-builder/metal/js/util/visitors.es';

class StateSyncronizer extends Component {
	static PROPS = {
		descriptionEditor: Config.any(),
		layoutProvider: Config.any(),
		localizedDescription: Config.object().value({}),
		localizedName: Config.object().value({}),
		nameEditor: Config.any(),
		namespace: Config.string().required(),
		published: Config.bool(),
		settingsDDMForm: Config.any(),
		translationManager: Config.any()
	};

	created() {
		const {descriptionEditor, nameEditor} = this.props;

		this._eventHandler = new EventHandler();

		this._eventHandler.add(
			descriptionEditor.on('change', this._handleDescriptionEditorChanged.bind(this)),
			nameEditor.on('change', this._handleNameEditorChanged.bind(this))
		);
	}

	disposeInternal() {
		super.disposeInternal();

		this._eventHandler.removeAllListeners();
	}

	getState() {
		const {layoutProvider, localizedDescription, localizedName, translationManager} = this.props;

		const state = {
			availableLanguageIds: translationManager.get('availableLocales'),
			defaultLanguageId: translationManager.get('defaultLocale'),
			description: localizedDescription,
			name: localizedName,
			pages: layoutProvider.state.pages,
			paginationMode: layoutProvider.state.paginationMode,
			rules: layoutProvider.state.rules,
			successPageSettings: layoutProvider.state.successPageSettings
		};

		return state;
	}

	isEmpty() {
		const {layoutProvider} = this.props;

		return FormSupport.emptyPages(layoutProvider.state.pages);
	}

	syncInputs() {
		const {namespace, settingsDDMForm} = this.props;
		const state = this.getState();
		const {
			description,
			name
		} = state;

		const publishedField = settingsDDMForm.getField('published');

		publishedField.set('value', this.props.published);

		const settings = settingsDDMForm.get('context');

		document.querySelector(`#${namespace}name`).value = JSON.stringify(name);
		document.querySelector(`#${namespace}description`).value = JSON.stringify(description);
		document.querySelector(`#${namespace}serializedFormBuilderContext`).value = this._getSerializedFormBuilderContext();
		document.querySelector(`#${namespace}serializedSettingsContext`).value = JSON.stringify(settings);
	}

	_getSerializedFormBuilderContext() {
		const state = this.getState();

		const visitor = new PagesVisitor(state.pages);

		const pages = visitor.mapPages(
			page => {
				return {
					...page,
					description: page.localizedDescription,
					title: page.localizedTitle
				};
			}
		);

		visitor.setPages(pages);

		return JSON.stringify(
			{
				...state,
				pages: visitor.mapFields(
					field => {
						if (field.options) {
							field = {
								...field,
								settingsContext: {
									...field.settingsContext,
									pages: this._filterEmptyOptions(field.settingsContext.pages)
								}
							};
						}

						return field;
					}
				)
			}
		);
	}

	_filterEmptyOptions(pages) {
		const visitor = new PagesVisitor(pages);

		return visitor.mapFields(
			field => {
				if (field.type === 'options') {
					const {value} = field;
					const newValue = {};

					for (const locale in value) {
						newValue[locale] = value[locale].filter(({value}) => value !== '');
					}

					field = {
						...field,
						value: newValue
					};
				}

				return field;
			}
		);
	}

	_handleDescriptionEditorChanged(event) {
		const {descriptionEditor, localizedDescription, translationManager} = this.props;
		const editor = window[descriptionEditor.name];

		localizedDescription[translationManager.get('editingLocale')] = editor.getHTML();
	}

	_handleNameEditorChanged(event) {
		const {localizedName, nameEditor, translationManager} = this.props;
		const editor = window[nameEditor.name];

		localizedName[translationManager.get('editingLocale')] = editor.getHTML();
	}
}

export default StateSyncronizer;