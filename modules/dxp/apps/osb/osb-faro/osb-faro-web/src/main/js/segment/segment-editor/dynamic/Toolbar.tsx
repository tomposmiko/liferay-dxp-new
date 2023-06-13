import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import debounce from 'shared/util/debounce-decorator';
import Form, {validateRequired} from 'shared/components/form';
import Icon from 'shared/components/Icon';
import InfoPopover from 'shared/components/InfoPopover';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import TitleEditor from 'shared/components/TitleEditor';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {createOrderIOMap, NAME} from 'shared/util/pagination';
import {Criteria} from './utils/types';
import {hasChanges} from 'shared/util/react';
import {INDIVIDUALS} from 'shared/util/router';
import {individualsListColumns} from 'shared/util/table-columns';
import {Modal} from 'shared/types';
import {Routes, SEGMENTS, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {validateSegmentInputs} from './utils/utils';

interface IToolbarProps {
	channelId: string;
	close: Modal.close;
	criteria: Criteria;
	criteriaString: string;
	groupId: string;
	id: string;
	includeAnonymousUsers: boolean;
	open: Modal.open;
	valid: boolean;
}

interface IToolbarState {
	countLoading: boolean;
	criteriaValid: boolean;
	membersCount: number;
}

@hasRequest
export class Toolbar extends React.Component<IToolbarProps, IToolbarState> {
	state = {
		countLoading: true,
		criteriaValid: false,
		membersCount: 0
	};

	componentDidMount() {
		this.setState(
			{criteriaValid: validateSegmentInputs(this.props.criteria)},
			this.getMembersCount
		);
	}

	componentDidUpdate(prevProps) {
		if (
			hasChanges(
				prevProps,
				this.props,
				'criteria',
				'includeAnonymousUsers'
			)
		) {
			this.setState(
				{criteriaValid: validateSegmentInputs(this.props.criteria)},
				this.getMembersCount
			);
		}
	}

	componentWillUnmount() {
		// @ts-ignore: Property 'cancel' does not exist on type '() => any'.
		this.getMembersCount.cancel();
	}

	@autoCancel
	@autobind
	fetchMembers(params) {
		const {channelId, criteriaString, groupId} = this.props;

		return API.individuals.search({
			channelId,
			filter: criteriaString,
			groupId,
			...params
		});
	}

	@debounce(400)
	getMembersCount() {
		const {
			props: {criteria, includeAnonymousUsers},
			state: {criteriaValid}
		} = this;

		if (criteria) {
			this.setState({countLoading: true});
		} else {
			this.setState({countLoading: false, membersCount: 0});
		}

		if (criteriaValid) {
			return this.fetchMembers({delta: 0, includeAnonymousUsers})
				.then(({total}) =>
					this.setState({countLoading: false, membersCount: total})
				)
				.catch(err => {
					if (!err.IS_CANCELLATION_ERROR) {
						this.setState({countLoading: false});
					}
				});
		}
	}

	@autobind
	handlePreviewClick() {
		const {close, open} = this.props;

		open(modalTypes.SEARCHABLE_ENTITIES_TABLE_MODAL, {
			columns: [individualsListColumns.name],
			dataSourceFn: this.fetchMembers,
			entityLabel: Liferay.Language.get('individuals'),
			entityType: INDIVIDUALS,
			initialOrderIOMap: createOrderIOMap(NAME),
			onClose: close,
			rowIdentifier: 'id',
			size: 'lg',
			title: Liferay.Language.get('known-segment-members')
		});
	}

	getPreviewCriteriaTooltipProps() {
		const {criteriaValid} = this.state;

		return criteriaValid
			? {}
			: {
					'data-tooltip': true,
					'data-tooltip-align': 'bottom',
					title: Liferay.Language.get(
						'some-of-your-criteria-are-incomplete-or-invalid'
					)
			  };
	}

	render() {
		const {
			props: {channelId, groupId, id, valid},
			state: {countLoading, criteriaValid, membersCount}
		} = this;

		const totalMembersCount = countLoading ? (
			<Spinner key='MEMBERS_COUNT_SPINNER' size='sm' />
		) : (
			membersCount.toLocaleString()
		);

		return (
			<div className='form-header'>
				<div className='page-container'>
					<div className='container-fluid form-header-container'>
						<div className='form-header-section-left'>
							<TitleEditor
								name='name'
								placeholder={Liferay.Language.get(
									'unnamed-segment'
								)}
								validate={validateRequired}
							/>
						</div>

						<div className='form-header-section-right'>
							<Button.Group>
								<Button.GroupItem>
									<Form.ToggleSwitch
										className='include-anonymous'
										label={Liferay.Language.get(
											'include-anonymous'
										)}
										name='includeAnonymousUsers'
									/>
								</Button.GroupItem>

								<Button.GroupItem>
									<InfoPopover
										className='include-anon-help-icon'
										content={Liferay.Language.get(
											'criteria-containing-individual-or-account-attributes-excludes-anonymous-individuals'
										)}
									/>
								</Button.GroupItem>
							</Button.Group>

							<Button.Group>
								<Button.GroupItem>
									<div className='total-members'>
										{sub(
											Liferay.Language.get(
												'total-members-x'
											),
											[
												<div
													className='total-members-count'
													key='TOTAL_MEMBERS_COUNT'
												>
													{totalMembersCount}
												</div>
											],
											false
										)}
									</div>

									<Button
										borderless
										className='preview-criteria'
										data-testid='preview-criteria-button'
										data-tooltip
										disabled={
											!criteriaValid ||
											(criteriaValid && !membersCount)
										}
										onClick={this.handlePreviewClick}
										size='sm'
										title={Liferay.Language.get(
											'view-members'
										)}
									>
										<span
											{...this.getPreviewCriteriaTooltipProps()}
										>
											<Icon symbol='view' />
										</span>
									</Button>
								</Button.GroupItem>
							</Button.Group>

							<Button.Group>
								<Button.GroupItem className='save'>
									<Button
										disabled={!valid}
										display='primary'
										size='sm'
										type='submit'
									>
										{Liferay.Language.get('save-segment')}
									</Button>
								</Button.GroupItem>

								<Button.GroupItem className='cancel'>
									<Button
										display='secondary'
										href={
											id
												? toRoute(
														Routes.CONTACTS_SEGMENT,
														{channelId, groupId, id}
												  )
												: toRoute(
														Routes.CONTACTS_LIST_SEGMENT,
														{
															channelId,
															groupId,
															type: SEGMENTS
														}
												  )
										}
										size='sm'
									>
										{Liferay.Language.get('cancel')}
									</Button>
								</Button.GroupItem>
							</Button.Group>
						</div>
					</div>
				</div>
			</div>
		);
	}
}

export default connect(null, {
	close,
	open
})(Toolbar);
