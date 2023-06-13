/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayButton from '@clayui/button';
import ClayToolbar from '@clayui/toolbar';
import getCN from 'classnames';
import {setNestedObjectValues, useFormik} from 'formik';
import {fetch, navigate} from 'frontend-js-web';
import {PropTypes} from 'prop-types';
import React, {
	useCallback,
	useContext,
	useEffect,
	useRef,
	useState,
} from 'react';

import useFetchData from '../hooks/useFetchData';
import useShouldConfirmBeforeNavigate from '../hooks/useShouldConfirmBeforeNavigate';
import LearnMessage from '../shared/LearnMessage';
import PageToolbar from '../shared/PageToolbar';
import Sidebar from '../shared/Sidebar';
import SubmitWarningModal from '../shared/SubmitWarningModal';
import ThemeContext from '../shared/ThemeContext';
import {DEFAULT_INDEX_CONFIGURATION} from '../utils/constants';
import {DEFAULT_ERROR} from '../utils/errorMessages';
import addParams from '../utils/fetch/add_params';
import fetchData, {DEFAULT_HEADERS} from '../utils/fetch/fetch_data';
import fetchPreviewSearch from '../utils/fetch/fetch_preview_search';
import filterAndSortClassNames from '../utils/functions/filter_and_sort_class_names';
import getResultsError from '../utils/functions/get_results_error';
import isDefined from '../utils/functions/is_defined';
import formatLocaleWithUnderscores from '../utils/language/format_locale_with_underscores';
import renameKeys from '../utils/language/rename_keys';
import {setStorageAddSXPElementSidebar} from '../utils/sessionStorage';
import cleanUIConfiguration from '../utils/sxp_element/clean_ui_configuration';
import getUIConfigurationValues from '../utils/sxp_element/get_ui_configuration_values';
import isCustomJSONSXPElement from '../utils/sxp_element/is_custom_json_sxp_element';
import parseCustomSXPElement from '../utils/sxp_element/parse_custom_sxp_element';
import replaceTemplateVariable from '../utils/sxp_element/replace_template_variable';
import transformToSearchContextAttributes from '../utils/sxp_element/transform_to_search_context_attributes';
import transformToSearchPreviewHits from '../utils/sxp_element/transform_to_search_preview_hits';
import {TEST_IDS} from '../utils/testIds';
import {
	openErrorToast,
	openSuccessToast,
	setInitialSuccessToast,
} from '../utils/toasts';
import {INPUT_TYPES} from '../utils/types/inputTypes';
import {SIDEBAR_TYPES} from '../utils/types/sidebarTypes';
import validateBoost from '../utils/validation/validate_boost';
import validateJSON from '../utils/validation/validate_json';
import validateNumberRange from '../utils/validation/validate_number_range';
import validateRequired from '../utils/validation/validate_required';
import AddSXPElementSidebar from './add_sxp_element_sidebar/index';
import ClauseContributorsSidebar from './clause_contributors_sidebar/index';
import ConfigurationTab from './configuration_tab/index';
import PreviewSidebar from './preview_sidebar/index';
import QueryBuilderTab from './query_builder_tab/index';

// Tabs in display order
/* eslint-disable sort-keys */
const TABS = {
	'query-builder': Liferay.Language.get('query-builder'),
	'configuration': Liferay.Language.get('configuration'),
};
/* eslint-enable sort-keys */

function EditSXPBlueprintForm({
	entityJSON,
	initialConfiguration = {},
	initialDescription = '',
	initialDescriptionI18n = {},
	initialSXPElementInstances = [],
	initialTitle = '',
	initialTitleI18n = {},
	sxpBlueprintId,
}) {
	const {
		featureFlagLps153813,
		isCompanyAdmin,
		locale,
		redirectURL,
	} = useContext(ThemeContext);

	const formRef = useRef();
	const sxpElementIdCounterRef = useRef(
		initialSXPElementInstances.length || 0
	);

	const controllerRef = useRef();

	const [errors, setErrors] = useState([]);
	const [previewInfo, setPreviewInfo] = useState(() => ({
		loading: false,
		results: {},
	}));
	const [openSidebar, setOpenSidebar] = useState(
		SIDEBAR_TYPES.ADD_SXP_ELEMENT
	);
	const [showSubmitWarningModal, setShowSubmitWarningModal] = useState(false);
	const [tab, setTab] = useState('query-builder');

	const [indexFields, setIndexFields] = useState(null);
	const [searchIndexes, setSearchIndexes] = useState(null);

	const {
		data: searchableTypes,
		refetch: refetchSearchableTypes,
	} = useFetchData({
		resource: `/o/search-experiences-rest/v1.0/searchable-asset-names/${locale}`,
	});

	const {
		data: keywordQueryContributors,
		refetch: refetchKeywordQueryContributors,
	} = useFetchData({
		getData: (response) => filterAndSortClassNames(response?.items),
		resource: '/o/search-experiences-rest/v1.0/keyword-query-contributors',
	});

	const {
		data: modelPrefilterContributors,
		refetch: refetchModelPrefilterContributors,
	} = useFetchData({
		getData: (response) => filterAndSortClassNames(response?.items),
		resource:
			'/o/search-experiences-rest/v1.0/model-prefilter-contributors',
	});

	const {
		data: queryPrefilterContributors,
		refetch: refetchQueryPrefilterContributors,
	} = useFetchData({
		getData: (response) => filterAndSortClassNames(response?.items),
		resource:
			'/o/search-experiences-rest/v1.0/query-prefilter-contributors',
	});

	/**
	 * This method must go before the useFormik hook.
	 */
	const _handleFormikSubmit = async (values) => {
		let configuration;
		let elementInstances;

		try {
			configuration = _getConfiguration(values);
			elementInstances = _getElementInstances(values);
		}
		catch (error) {
			openErrorToast({
				message: Liferay.Language.get(
					'the-configuration-has-missing-or-invalid-values'
				),
			});

			if (process.env.NODE_ENV === 'development') {
				console.error(error);
			}

			return;
		}

		try {

			// If the warning modal is already open, assume the form was submitted
			// using the "Continue To Save" action and should skip the schema
			// validation step.

			// TODO: Update this once a validation REST endpoint is decided

			if (!showSubmitWarningModal) {
				const validateErrors = {errors: []};

				/*
				const validateErrors = await fetch(
					'/o/search-experiences-rest/v1.0/sxp-blueprints/validate',
					{
						body: JSON.stringify({
							configuration,
							description_i18n: renameKeys(
								formik.values.description_i18n,
								formatLocaleWithUnderscores
							),
							elementInstances,
							title_i18n: renameKeys(formik.values.title_i18n,
								formatLocaleWithUnderscores),
						}),
						headers: DEFAULT_HEADERS,
						method: 'POST',
					}
				).then((response) => response.json());
			*/

				if (validateErrors.errors?.length) {
					setErrors(validateErrors.errors);
					setShowSubmitWarningModal(true);

					return;
				}
			}

			const responseContent = await fetch(
				`/o/search-experiences-rest/v1.0/sxp-blueprints/${sxpBlueprintId}`,
				{
					body: JSON.stringify({
						configuration,
						description_i18n: renameKeys(
							formik.values.description_i18n,
							formatLocaleWithUnderscores
						),
						elementInstances,
						title_i18n: renameKeys(
							formik.values.title_i18n,
							formatLocaleWithUnderscores
						),
					}),
					headers: DEFAULT_HEADERS,
					method: 'PATCH',
				}
			).then((response) => {
				if (!response.ok) {
					setShowSubmitWarningModal(false);

					throw DEFAULT_ERROR;
				}

				return response.json();
			});

			if (
				Object.prototype.hasOwnProperty.call(responseContent, 'errors')
			) {
				responseContent.errors.forEach((message) =>
					openErrorToast({message})
				);
			}
			else {
				setInitialSuccessToast(
					Liferay.Language.get('the-blueprint-was-saved-successfully')
				);

				navigate(redirectURL);
			}
		}
		catch (error) {
			openErrorToast();

			if (process.env.NODE_ENV === 'development') {
				console.error(error);
			}
		}
	};

	/**
	 * This method must go before the useFormik hook.
	 */
	const _handleFormikValidate = (values) => {
		const errors = {};

		// Validate the elements added to the query builder

		const elementInstancesArray = [];

		values.elementInstances.map(
			({sxpElement, uiConfigurationValues}, index) => {
				const enabled =
					sxpElement.elementDefinition?.configuration
						?.queryConfiguration?.queryEntries?.[0]?.enabled;
				const uiConfiguration =
					sxpElement.elementDefinition?.uiConfiguration;

				if (isDefined(enabled) && !enabled) {
					return;
				}

				const configErrors = {};
				const fieldSets = cleanUIConfiguration(uiConfiguration)
					.fieldSets;

				if (
					!!fieldSets.length &&
					!isCustomJSONSXPElement(uiConfigurationValues)
				) {
					fieldSets.map(({fields}) => {
						fields.map(({name, type, typeOptions = {}}) => {
							const configValue = uiConfigurationValues[name];

							const configError =
								validateRequired(
									configValue,
									name,
									typeOptions.nullable,
									typeOptions.required,
									type
								) ||
								validateBoost(configValue, type) ||
								validateNumberRange(
									configValue,
									type,
									typeOptions
								) ||
								validateJSON(configValue, type);

							if (configError) {
								configErrors[name] = configError;
							}
						});
					});
				}
				else if (isCustomJSONSXPElement(uiConfigurationValues)) {
					const configValue = uiConfigurationValues?.sxpElement;

					const configError =
						validateRequired(
							configValue,
							'',
							false,
							true,
							INPUT_TYPES.JSON
						) || validateJSON(configValue, INPUT_TYPES.JSON);

					if (configError) {
						configErrors.sxpElement = configError;
					}
				}

				if (Object.keys(configErrors).length) {
					elementInstancesArray[index] = {
						uiConfigurationValues: configErrors,
					};
				}
			}
		);

		if (elementInstancesArray.length) {
			errors.elementInstances = elementInstancesArray;
		}

		// Validate all JSON inputs on the configuration tab

		[
			'advancedConfig',
			'aggregationConfig',
			'highlightConfig',
			'parameterConfig',
			'sortConfig',
		].map((configName) => {
			const configError = validateJSON(
				values[configName],
				INPUT_TYPES.JSON
			);

			if (configError) {
				errors[configName] = configError;
			}
		});

		return errors;
	};

	const formik = useFormik({
		initialValues: {
			advancedConfig: JSON.stringify(
				initialConfiguration.advancedConfiguration,
				null,
				'\t'
			),
			aggregationConfig: JSON.stringify(
				initialConfiguration.aggregationConfiguration,
				null,
				'\t'
			),
			applyIndexerClauses:
				initialConfiguration.queryConfiguration?.applyIndexerClauses,
			description_i18n: initialDescriptionI18n,
			elementInstances: initialSXPElementInstances.map(
				(elementInstance, index) => ({
					...elementInstance,
					id: index,
				})
			),
			frameworkConfig: initialConfiguration.generalConfiguration || {
				clauseContributorsExcludes: [],
				clauseContributorsIncludes: [],
			},
			highlightConfig: JSON.stringify(
				initialConfiguration.highlightConfiguration,
				null,
				'\t'
			),
			indexConfig:
				initialConfiguration.indexConfiguration ||
				DEFAULT_INDEX_CONFIGURATION,
			parameterConfig: JSON.stringify(
				initialConfiguration.parameterConfiguration,
				null,
				'\t'
			),
			sortConfig: JSON.stringify(
				initialConfiguration.sortConfiguration,
				null,
				'\t'
			),
			title_i18n: initialTitleI18n,
		},
		onSubmit: _handleFormikSubmit,
		validate: _handleFormikValidate,
	});

	useShouldConfirmBeforeNavigate(formik.dirty && !formik.isSubmitting);

	useEffect(() => {
		if (featureFlagLps153813 && isCompanyAdmin) {

			// Example response:
			// {
			// 	actions: {},
			// 	facets: [],
			// 	items: [
			// 		{
			// 			external: false,
			// 			name: 'search-tuning-rankings',
			// 		}
			// 	],
			// 	lastPage: 1,
			// 	page: 1,
			// 	pageSize: 14,
			// 	totalCount: 14,
			// }

			fetchData(`/o/search-experiences-rest/v1.0/search-indexes`)
				.then((responseContent) =>
					setSearchIndexes(responseContent?.items || [])
				)
				.catch(() => setSearchIndexes([]));
		}
		else {
			setSearchIndexes([]);
		}

		setStorageAddSXPElementSidebar('open');
	}, []); //eslint-disable-line

	/**
	 * Refetch field mapping infos when indexConfiguration changes.
	 */
	useEffect(() => {

		// Example response:
		// {
		// 	actions: {},
		// 	facets: [],
		// 	items: [
		// 		{
		// 			languageIdPosition: -1,
		// 			name: 'ddmTemplateKey',
		// 			type: 'keyword',
		// 		}
		// 	],
		// 	lastPage: 1,
		// 	page: 1,
		// 	pageSize: 218,
		// 	totalCount: 218,
		// }

		fetchData(
			addParams('/o/search-experiences-rest/v1.0/field-mapping-infos', {
				external: formik.values.indexConfig.external,
				indexName: formik.values.indexConfig.indexName,
			})
		)
			.then((responseContent) => setIndexFields(responseContent.items))
			.catch(() => setIndexFields([]));
	}, [
		formik.values.indexConfig.external,
		formik.values.indexConfig.indexName,
	]);

	/**
	 * Formats the form values for the "configuration" parameter to send to
	 * the server. Sets defaults so the JSON.parse calls don't break.
	 * @param {Object} values Form values
	 * @return {Object}
	 */
	const _getConfiguration = ({
		advancedConfig,
		aggregationConfig,
		applyIndexerClauses,
		frameworkConfig,
		highlightConfig,
		indexConfig,
		parameterConfig,
		sortConfig,
	}) => {
		const configuration = {
			advancedConfiguration: advancedConfig
				? JSON.parse(advancedConfig)
				: {},
			aggregationConfiguration: aggregationConfig
				? JSON.parse(aggregationConfig)
				: {},
			generalConfiguration: frameworkConfig,
			highlightConfiguration: highlightConfig
				? JSON.parse(highlightConfig)
				: {},
			parameterConfiguration: parameterConfig
				? JSON.parse(parameterConfig)
				: {},
			queryConfiguration: {
				applyIndexerClauses,
			},
			sortConfiguration: sortConfig ? JSON.parse(sortConfig) : {},
		};

		if (featureFlagLps153813) {
			configuration.indexConfiguration =
				indexConfig || DEFAULT_INDEX_CONFIGURATION;
		}

		return configuration;
	};

	const _getElementInstances = (values) =>
		values.elementInstances.map(
			({
				id, // eslint-disable-line
				sxpElement,
				sxpElementId,
				type,
				uiConfigurationValues,
			}) => ({
				configurationEntry: replaceTemplateVariable({
					sxpElement,
					uiConfigurationValues,
				}),
				sxpElement: parseCustomSXPElement(
					sxpElement,
					uiConfigurationValues
				),
				sxpElementId,
				type,
				uiConfigurationValues,
			})
		);

	const _handleAddSXPElement = (sxpElement) => {
		if (formik.touched?.elementInstances) {
			formik.setTouched({
				...formik.touched,
				elementInstances: [
					undefined,
					...formik.touched.elementInstances,
				],
			});
		}

		formik.setFieldValue('elementInstances', [
			{
				id: sxpElementIdCounterRef.current++,
				sxpElement,
				uiConfigurationValues: getUIConfigurationValues(sxpElement),
			},
			...formik.values.elementInstances,
		]);

		openSuccessToast({
			message: Liferay.Language.get('element-added'),
		});
	};

	const _handleApplyIndexerClausesChange = (value) => {
		formik.setFieldValue('applyIndexerClauses', value);
	};

	const _handleChangeTab = (tab) => {
		if (
			tab !== 'query-builder' &&
			(openSidebar === SIDEBAR_TYPES.CLAUSE_CONTRIBUTORS ||
				openSidebar === SIDEBAR_TYPES.INDEXER_CLAUSES)
		) {
			setOpenSidebar('');
		}

		setTab(tab);
	};

	const _handleChangeTitleAndDescription = ({description, title}) => {
		formik.setFieldValue('description_i18n', description);
		formik.setFieldValue('title_i18n', title);
	};

	const _handleCloseSidebar = () => {
		setOpenSidebar('');
	};

	const _handleDeleteSXPElement = (id) => {
		const index = formik.values.elementInstances.findIndex(
			(item) => item.id === id
		);

		if (formik.touched?.elementInstances) {
			formik.setTouched({
				...formik.touched,
				elementInstances: formik.touched.elementInstances.filter(
					(_, i) => i !== index
				),
			});
		}

		formik.setFieldValue(
			'elementInstances',
			formik.values.elementInstances.filter((item) => item.id !== id)
		);

		openSuccessToast({
			message: Liferay.Language.get('element-removed'),
		});
	};

	/**
	 * Used by the preview sidebar to cancel any unexpectedly slow search.
	 */
	const _handleFetchPreviewCancel = () => {
		controllerRef.current.abort();
	};

	/**
	 * Used by the preview sidebar to perform searches.
	 * @param {string} query The keyword search query
	 * @param {number} delta The number of results to return
	 * @param {number} page The page to return
	 * @param {Array} attributes The search context attributes
	 */
	const _handleFetchPreviewSearch = async (
		query,
		delta,
		page,
		attributes
	) => {
		controllerRef.current = new AbortController();

		setPreviewInfo((previewInfo) => ({
			...previewInfo,
			loading: true,
		}));

		let configuration;
		let elementInstances;

		try {
			configuration = _getConfiguration(formik.values);
			elementInstances = _getElementInstances(formik.values);

			// Touch inputs with errors to show validation errors.

			const errors = await formik.validateForm();

			formik.setTouched(setNestedObjectValues(errors, true));

			// Don't perform a search if there are missing required fields.

			if (!formik.isValid) {
				throw Liferay.Language.get(
					'the-configuration-has-missing-or-invalid-values'
				);
			}
		}
		catch (error) {

			// Add a delay so the loading indicator is visible before showing
			// the error message. This provides feedback that a new search has
			// been made.

			setTimeout(() => {
				setPreviewInfo({
					loading: false,
					results: {
						errors: [
							{
								msg: Liferay.Language.get(
									'the-configuration-has-missing-or-invalid-values'
								),
							},
						],
					},
				});
			}, 100);

			if (process.env.NODE_ENV === 'development') {
				console.error(error);
			}

			return;
		}

		const parseResponseContent = (responseContent) => {
			const exceptionKey = 'java.lang.RuntimeException';

			if (
				responseContent.searchHits?.totalHits > 0 ||
				!responseContent.responseString?.startsWith(exceptionKey)
			) {
				return responseContent;
			}

			let exceptionClass;

			const exceptionKeyIndex = responseContent.responseString.indexOf(
				':',
				exceptionKey.length + 1
			);

			if (exceptionKeyIndex !== -1) {
				exceptionClass = responseContent.responseString.substring(
					exceptionKey.length + 1,
					exceptionKeyIndex
				);
			}

			let msg;

			const errorObjectIndex = responseContent.responseString.indexOf(
				'{"error":{'
			);

			if (errorObjectIndex > 0) {
				const errorJSONObject = JSON.parse(
					responseContent.responseString.substring(errorObjectIndex)
				);

				msg = errorJSONObject.error.root_cause[0]?.reason;
			}

			return getResultsError({
				exceptionClass,
				exceptionTrace: responseContent.responseString,
				msg,
			});
		};

		return fetchPreviewSearch(
			{
				page,
				pageSize: delta,
				query,
			},
			{
				body: JSON.stringify({
					configuration: {
						...configuration,
						generalConfiguration: {
							...configuration?.generalConfiguration,
							emptySearchEnabled: true,
							explain: true,
							includeResponseString: true,
							languageId: Liferay.ThemeDisplay.getLanguageId(),
						},
						searchContextAttributes: transformToSearchContextAttributes(
							attributes
						),
					},
					elementInstances,
				}),
				signal: controllerRef.current.signal,
			}
		)
			.then((response) => {
				return response.json().then((data) => ({
					ok: response.ok,
					responseContent: data,
				}));
			})
			.then(({ok, responseContent}) => {
				setPreviewInfo({
					loading: false,
					results: parseResponseContent(
						ok
							? responseContent
							: getResultsError({
									msg: responseContent?.title,
							  })
					),
				});
			})
			.catch((error) => {
				setPreviewInfo({
					loading: false,
					results:
						error.name === 'AbortError'
							? previewInfo.results
							: getResultsError({}),
				});
			});
	};

	const _handleFocusSXPElement = (prefixedId) => {
		const sxpElement = document.getElementById(prefixedId);

		if (sxpElement) {
			window.scrollTo({
				behavior: 'smooth',
				top:
					sxpElement.getBoundingClientRect().top +
					window.pageYOffset -
					55 - // Control menu height
					104 - // Page toolbar height
					20, // Additional padding
			});

			sxpElement.classList.remove('focus');

			void sxpElement.offsetWidth; // Triggers reflow to restart animation

			sxpElement.classList.add('focus');
		}
	};

	const _handleFrameworkConfigChange = useCallback(
		(value) =>
			formik.setFieldValue('frameworkConfig', {
				...formik.values.frameworkConfig,
				...value,
			}),
		[formik]
	);

	const _handleSubmit = (event) => {
		event.preventDefault();

		formik.handleSubmit();

		if (!formik.isValid) {
			openErrorToast({
				message: Liferay.Language.get(
					'unable-to-save-due-to-invalid-or-missing-configuration-values'
				),
			});
		}
	};

	const _handleToggleSidebar = (type) => () => {
		if (type === SIDEBAR_TYPES.PREVIEW) {
			setStorageAddSXPElementSidebar('closed');
		}

		setOpenSidebar(openSidebar === type ? '' : type);
	};

	const _isIndexCompany = () => {
		return (
			formik.values.indexConfig.indexName ===
			DEFAULT_INDEX_CONFIGURATION.indexName
		);
	};

	const _renderTabContent = () => {
		switch (tab) {
			case 'configuration':
				return (
					<ConfigurationTab
						advancedConfig={formik.values.advancedConfig}
						aggregationConfig={formik.values.aggregationConfig}
						errors={formik.errors}
						highlightConfig={formik.values.highlightConfig}
						indexConfig={formik.values.indexConfig}
						parameterConfig={formik.values.parameterConfig}
						searchIndexes={searchIndexes}
						setFieldTouched={formik.setFieldTouched}
						setFieldValue={formik.setFieldValue}
						sortConfig={formik.values.sortConfig}
						touched={formik.touched}
					/>
				);
			default:
				return (
					<>
						<AddSXPElementSidebar
							isIndexCompany={_isIndexCompany()}
							onAddSXPElement={_handleAddSXPElement}
							onClose={_handleCloseSidebar}
							visible={
								openSidebar === SIDEBAR_TYPES.ADD_SXP_ELEMENT
							}
						/>

						<ClauseContributorsSidebar
							frameworkConfig={formik.values.frameworkConfig}
							initialClauseContributorsList={[
								{
									label: 'KeywordQueryContributor',
									value: keywordQueryContributors,
								},
								{
									label: 'ModelPreFilterContributor',
									value: modelPrefilterContributors,
								},
								{
									label: 'QueryPreFilterContributor',
									value: queryPrefilterContributors,
								},
							]}
							onClose={_handleCloseSidebar}
							onFetchContributors={() => {
								refetchKeywordQueryContributors();
								refetchModelPrefilterContributors();
								refetchQueryPrefilterContributors();
							}}
							onFrameworkConfigChange={
								_handleFrameworkConfigChange
							}
							visible={
								openSidebar ===
								SIDEBAR_TYPES.CLAUSE_CONTRIBUTORS
							}
						/>

						<Sidebar
							className="info-sidebar"
							onClose={_handleCloseSidebar}
							title={Liferay.Language.get(
								'search-framework-indexer-clauses'
							)}
							visible={
								openSidebar === SIDEBAR_TYPES.INDEXER_CLAUSES
							}
						>
							<div className="container-fluid text-secondary">
								<span className="help-text">
									{Liferay.Language.get(
										'search-framework-indexer-clauses-description'
									)}
								</span>

								<LearnMessage resourceKey="query-clause-contributors-configuration" />
							</div>
						</Sidebar>

						<div
							className={getCN({
								'open-add-sxp-element':
									openSidebar ===
									SIDEBAR_TYPES.ADD_SXP_ELEMENT,
								'open-clause-contributors':
									openSidebar ===
									SIDEBAR_TYPES.CLAUSE_CONTRIBUTORS,
								'open-info':
									openSidebar ===
									SIDEBAR_TYPES.INDEXER_CLAUSES,
							})}
						>
							<QueryBuilderTab
								applyIndexerClauses={
									formik.values.applyIndexerClauses
								}
								clauseContributorsList={[
									...keywordQueryContributors,
									...modelPrefilterContributors,
									...queryPrefilterContributors,
								]}
								elementInstances={
									formik.values.elementInstances
								}
								entityJSON={entityJSON}
								errors={formik.errors.elementInstances}
								frameworkConfig={formik.values.frameworkConfig}
								indexFields={indexFields}
								isIndexCompany={_isIndexCompany()}
								isSubmitting={
									formik.isSubmitting || previewInfo.loading
								}
								onApplyIndexerClausesChange={
									_handleApplyIndexerClausesChange
								}
								onBlur={formik.handleBlur}
								onChange={formik.handleChange}
								onDeleteSXPElement={_handleDeleteSXPElement}
								onFetchSearchableTypes={refetchSearchableTypes}
								onFrameworkConfigChange={
									_handleFrameworkConfigChange
								}
								openSidebar={openSidebar}
								searchableTypes={searchableTypes?.items}
								setFieldTouched={formik.setFieldTouched}
								setFieldValue={formik.setFieldValue}
								setOpenSidebar={setOpenSidebar}
								touched={formik.touched.elementInstances}
							/>
						</div>
					</>
				);
		}
	};

	if (!indexFields || !searchIndexes) {
		return null;
	}

	return (
		<form ref={formRef}>
			<SubmitWarningModal
				errors={errors}
				isSubmitting={formik.isSubmitting}
				message={Liferay.Language.get(
					'the-blueprint-configuration-has-errors-that-may-cause-unexpected-results.-use-the-preview-panel-to-review-these-errors'
				)}
				onClose={() => setShowSubmitWarningModal(false)}
				onSubmit={_handleSubmit}
				visible={showSubmitWarningModal}
			/>

			<PageToolbar
				description={initialDescription}
				descriptionI18n={formik.values.description_i18n}
				isSubmitting={formik.isSubmitting}
				onCancel={redirectURL}
				onChangeTab={_handleChangeTab}
				onSubmit={_handleSubmit}
				onTitleAndDescriptionChange={_handleChangeTitleAndDescription}
				tab={tab}
				tabs={TABS}
				title={initialTitle}
				titleI18n={formik.values.title_i18n}
			>
				<ClayToolbar.Item>
					<ClayButton
						borderless
						className={getCN({
							active: openSidebar === SIDEBAR_TYPES.PREVIEW,
						})}
						data-testid={TEST_IDS.PREVIEW_SIDEBAR_BUTTON}
						displayType="secondary"
						onClick={_handleToggleSidebar(SIDEBAR_TYPES.PREVIEW)}
						small
					>
						{Liferay.Language.get('preview')}
					</ClayButton>
				</ClayToolbar.Item>
			</PageToolbar>

			<PreviewSidebar
				errors={previewInfo.results.errors}
				hits={transformToSearchPreviewHits(previewInfo.results)}
				loading={previewInfo.loading}
				onClose={_handleCloseSidebar}
				onFetchCancel={_handleFetchPreviewCancel}
				onFetchResults={_handleFetchPreviewSearch}
				onFocusSXPElement={_handleFocusSXPElement}
				requestString={previewInfo.results.requestString}
				responseString={previewInfo.results.responseString}
				totalHits={previewInfo.results.searchHits?.totalHits}
				visible={openSidebar === SIDEBAR_TYPES.PREVIEW}
			/>

			<div
				className={getCN({
					'open-preview': openSidebar === SIDEBAR_TYPES.PREVIEW,
				})}
			>
				{_renderTabContent()}
			</div>
		</form>
	);
}

EditSXPBlueprintForm.propTypes = {
	entityJSON: PropTypes.object,
	initialConfiguration: PropTypes.object,
	initialDescription: PropTypes.string,
	initialDescriptionI18n: PropTypes.object,
	initialSXPElementInstances: PropTypes.arrayOf(PropTypes.object),
	initialTitle: PropTypes.string,
	initialTitleI18n: PropTypes.object,
	sxpBlueprintId: PropTypes.string,
};

export default React.memo(EditSXPBlueprintForm);
