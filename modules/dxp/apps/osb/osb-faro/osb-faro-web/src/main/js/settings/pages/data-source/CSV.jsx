import * as API from 'shared/api';
import * as breadcrumbs from 'shared/util/breadcrumbs';
import autobind from 'autobind-decorator';
import BasePage from 'settings/components/BasePage';
import ClayButton from '@clayui/button';
import ClayLink from '@clayui/link';
import DataTransformationList from 'settings/components/data-transformation-list';
import DefinitionItem from 'shared/components/DefinitionItem';
import getCN from 'classnames';
import React from 'react';
import Sheet from 'shared/components/Sheet';
import Spinner from 'shared/components/Spinner';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {DataSource, User} from 'shared/util/records';
import {FieldContexts} from 'shared/util/constants';
import {hasChanges} from 'shared/util/react';
import {List, Map} from 'immutable';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';
import {sequence} from 'shared/util/promise';
import {
	toPromise,
	validateMaxLength,
	validateRequired
} from 'shared/components/form';
import {validateUniqueName} from 'shared/util/data-sources';
import {withCurrentUser} from 'shared/hoc';

@hasRequest
export class CSV extends React.Component {
	static propTypes = {
		close: PropTypes.func.isRequired,
		currentUser: PropTypes.instanceOf(User).isRequired,
		dataSource: PropTypes.instanceOf(DataSource).isRequired,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string,
		open: PropTypes.func.isRequired
	};

	state = {
		fieldsIList: new List(),
		fileName: '',
		loading: true,
		mappingSuggestions: {},
		name: '',
		sourceFields: {}
	};

	constructor(props) {
		super(props);

		const {dataSource} = this.props;

		this._cachedNameValues = new Map();

		this.state = {
			...this.state,
			fileName: dataSource.fileName,
			name: dataSource.name
		};
	}

	componentDidMount() {
		this.fetchMappings();
	}

	componentDidUpdate(prevProps) {
		if (hasChanges(prevProps, this.props, 'dataSource')) {
			const {fileName, name} = this.props.dataSource;

			this.setState({
				fileName,
				name
			});
		}
	}

	@autoCancel
	fetchMappings() {
		const {groupId, id} = this.props;

		this.setState({
			loading: true
		});

		return API.dataSource
			.fetchMappingsLite({
				context: FieldContexts.Demographics,
				groupId,
				id
			})
			.then(mappings => {
				const mappingSuggestions = {};
				const sourceFields = {};

				for (const {name, suggestions, values} of mappings) {
					mappingSuggestions[name] = suggestions;
					sourceFields[name] = values[0];
				}

				this.setState({
					fieldsIList: this.processMappings(mappings),
					loading: false,
					mappingSuggestions,
					sourceFields
				});
			})
			.catch(noop);
	}

	@autobind
	handleValidate(value) {
		const {groupId} = this.props;

		let error = '';

		if (this._cachedNameValues.has(value)) {
			error = this._cachedNameValues.get(value);
		} else {
			error = validateUniqueName({groupId, value});

			this._cachedNameValues.set(value, error);
		}

		return toPromise(error);
	}

	processMappings(mappings) {
		const {id} = this.props;

		if (id && mappings.find(({mapping}) => mapping)) {
			mappings = mappings.filter(({mapping}) => mapping);
		}

		return new List(
			mappings.map(({mapping, name, suggestions, values}) => {
				const suggestion = id ? mapping : suggestions[0];

				return new Map({
					source: new Map({
						name,
						value: values[0]
					}),
					suggestion: new Map({
						name: suggestion && suggestion.name,
						value: suggestion && suggestion.values[0]
					})
				});
			})
		);
	}

	@autobind
	handleCSVPreviewModal() {
		const {
			props: {close, groupId, id, open},
			state: {name}
		} = this;

		open(modalTypes.CSV_PREVIEW_MODAL, {
			groupId,
			id,
			name,
			onClose: close
		});
	}

	@autobind
	handleUpdateName(name) {
		const {groupId, id} = this.props;

		return API.dataSource
			.updateCSV({
				groupId,
				id,
				name
			})
			.then(() => {
				this.setState({
					name
				});
			});
	}

	render() {
		const {
			props: {className, currentUser, groupId, id},
			state: {
				fieldsIList,
				fileName,
				loading,
				mappingSuggestions,
				name,
				sourceFields
			}
		} = this;

		const authorized = currentUser.isAdmin();

		return (
			<BasePage
				breadcrumbItems={[
					breadcrumbs.getDataSources({groupId}),
					breadcrumbs.getEntityName({
						label: name
					})
				]}
				className={getCN('csv-data-source-root', className)}
				documentTitle={name || Liferay.Language.get('csv-file')}
				groupId={groupId}
			>
				<Sheet>
					<Sheet.Header className='header-content'>
						<div className='page-title-group'>
							<h3 className='w-50'>
								<DefinitionItem
									editable={authorized}
									onSubmit={this.handleUpdateName}
									validate={sequence([
										validateRequired,
										validateMaxLength(255),
										this.handleValidate
									])}
									value={name}
								/>
							</h3>

							{authorized && (
								<div className='button-row'>
									<ClayLink
										button
										className='button-root'
										displayType='primary'
										href={toRoute(
											Routes.SETTINGS_DATA_SOURCE_EDIT,
											{
												groupId,
												id
											}
										)}
									>
										{Liferay.Language.get('edit-csv')}
									</ClayLink>

									<ClayLink
										button
										className='button-root'
										displayType='primary'
										href={toRoute(
											Routes.SETTINGS_DATA_SOURCE_DELETE,
											{
												groupId,
												id
											}
										)}
									>
										{Liferay.Language.get(
											'delete-data-source'
										)}
									</ClayLink>
								</div>
							)}
						</div>

						<div className='file-info'>
							<DefinitionItem value={fileName} />

							<ClayButton
								className='button-root toggle-preview'
								display='secondary'
								onClick={this.handleCSVPreviewModal}
								size='sm'
							>
								{Liferay.Language.get('view-file-preview')}
							</ClayButton>
						</div>
					</Sheet.Header>

					<Sheet.Body>
						<h3 className='mappings-header'>
							{Liferay.Language.get('current-mappings')}
						</h3>
					</Sheet.Body>

					{loading ? (
						<Spinner spacer />
					) : (
						<DataTransformationList
							fieldsIList={fieldsIList}
							groupId={groupId}
							id={id}
							mappingSuggestions={mappingSuggestions}
							name={name}
							readOnly
							sourceFields={sourceFields}
							sourceTitle={Liferay.Language.get('csv-field')}
							suggestionsTitle={Liferay.Language.get(
								'analytics-cloud-field'
							)}
						/>
					)}
				</Sheet>
			</BasePage>
		);
	}
}

export default compose(connect(null, {close, open}), withCurrentUser)(CSV);
