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

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import ClayLayout from '@clayui/layout';
import ClayList from '@clayui/list';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import ClayPanel from '@clayui/panel';
import getCN from 'classnames';
import {openSelectionModal} from 'frontend-js-web';
import PropTypes from 'prop-types';
import React from 'react';
import {DndProvider} from 'react-dnd';
import {HTML5Backend} from 'react-dnd-html5-backend';

import {contributorShape, propertyGroupShape} from '../../utils/types.es';
import {getPluralMessage} from '../../utils/utils.es';
import CriteriaSidebar from '../criteria_sidebar/CriteriaSidebar.es';
import Conjunction from './Conjunction.es';
import CriteriaBuilder from './CriteriaBuilder.es';
import EmptyPlaceholder from './EmptyPlaceholder.es';

class ContributorBuilder extends React.Component {
	static propTypes = {
		contributors: PropTypes.arrayOf(contributorShape),
		editing: PropTypes.bool.isRequired,
		emptyContributors: PropTypes.bool.isRequired,
		isSegmentationDisabledAlertDismissed: PropTypes.bool,
		isSegmentationEnabled: PropTypes.bool,
		membersCount: PropTypes.number,
		membersCountLoading: PropTypes.bool,
		onAlertClose: PropTypes.func,
		onConjunctionChange: PropTypes.func,
		onPreviewMembers: PropTypes.func,
		onQueryChange: PropTypes.func,
		portletNamespace: PropTypes.string.isRequired,
		previewMembersURL: PropTypes.string,
		propertyGroups: PropTypes.arrayOf(propertyGroupShape),
		renderEmptyValuesErrors: PropTypes.bool,
		scopeName: PropTypes.string,
		siteItemSelectorURL: PropTypes.string,
	};

	static defaultProps = {
		contributors: [],
		membersCount: 0,
		membersCountLoading: false,
		onAlertClose: () => {},
		onConjunctionChange: () => {},
		onPreviewMembers: () => {},
		onQueryChange: () => {},
		renderEmptyValuesErrors: false,
	};

	constructor(props) {
		super(props);

		const {contributors, propertyGroups, scopeName} = props;

		const firstContributorNotEmpty = contributors.find(
			(contributor) => contributor.query !== ''
		);

		const propertyKey = firstContributorNotEmpty
			? firstContributorNotEmpty.propertyKey
			: propertyGroups[0].propertyKey;

		this.state = {
			editingId: propertyKey,
			scopeName,
		};
	}

	_handleCriteriaChange = (criteriaChange, index) => {
		const {onQueryChange} = this.props;

		onQueryChange(criteriaChange, index);
	};

	_handleCriteriaEdit = (id, editing) => {
		this.setState({
			editingId: editing ? undefined : id,
		});
	};

	_handleScopeChange = () => {
		openSelectionModal({
			onSelect: (selectedItem) => {
				this.setState({
					scopeName: selectedItem.groupdescriptivename
						? selectedItem.groupdescriptivename
						: selectedItem.groupscopelabel,
				});
				const input = document.querySelector(
					`[name="${this.props.portletNamespace}groupId"]`
				);

				if (input) {
					input.value = selectedItem.groupid;
				}
			},
			selectEventName: 'sitesSelectItem',
			title: Liferay.Language.get('select-site'),
			url: this.props.siteItemSelectorURL,
		});
	};

	render() {
		const {
			contributors,
			editing,
			emptyContributors,
			isSegmentationDisabledAlertDismissed,
			isSegmentationEnabled,
			membersCount,
			membersCountLoading,
			onAlertClose,
			onConjunctionChange,
			onPreviewMembers,
			propertyGroups,
			renderEmptyValuesErrors,
		} = this.props;

		const {editingId, scopeName} = this.state;

		const rootClasses = getCN('contributor-builder-root', {
			editing,
		});

		const showDisabledSegmentationAlert =
			!isSegmentationEnabled && !isSegmentationDisabledAlertDismissed;

		const sidebarClasses = getCN('criteria-builder-section-sidebar', {
			'criteria-builder-section-sidebar--with-warning': showDisabledSegmentationAlert,
		});

		function handleViewMembersClick(event) {
			event.stopPropagation();

			onPreviewMembers();
		}

		return (
			<DndProvider backend={HTML5Backend}>
				<div className={rootClasses}>
					<div className={sidebarClasses}>
						<CriteriaSidebar
							onTitleClicked={this._handleCriteriaEdit}
							propertyGroups={propertyGroups}
							propertyKey={editingId}
						/>
					</div>

					<div className="criteria-builder-section-main">
						<div className="contributor-container">
							{renderEmptyValuesErrors && (
								<section className="alert-danger criteria-builder-empty-errors-alert">
									<div className="criteria-builder-empty-errors-alert__inner">
										<ClayAlert
											className="border-bottom-0"
											displayType="danger"
											onClose={onAlertClose}
											variant="stripe"
										>
											{Liferay.Language.get(
												'values-need-to-be-added-to-properties-for-the-segment-to-be-saved'
											)}
										</ClayAlert>
									</div>
								</section>
							)}

							<ClayLayout.ContainerFluid>
								<div className="content-wrapper p-4">
									{Liferay.FeatureFlags['LPS-166954'] ? (
										<>
											<ClayPanel
												className="mb-4"
												collapsable
												defaultExpanded={true}
												displayTitle={
													<ClayPanel.Title>
														<h2 className="m-0 text-dark">
															{Liferay.Language.get(
																'scope'
															)}
														</h2>
													</ClayPanel.Title>
												}
												displayType="secondary"
												showCollapseIcon
											>
												<ClayPanel.Body className="p-4">
													{this.props
														.siteItemSelectorURL && (
														<div className="align-items-center d-flex justify-content-between mb-3">
															<p className="mb-0 mr-6 text-dark">
																{Liferay.Language.get(
																	'select-the-scope-of-your-segment-to-specify-where-it-can-be-used'
																)}
															</p>

															<ClayButton
																displayType="secondary"
																onClick={
																	this
																		._handleScopeChange
																}
																size="sm"
															>
																{Liferay.Language.get(
																	'select'
																)}
															</ClayButton>
														</div>
													)}

													<ClayList>
														<ClayList.Item flex>
															<ClayList.ItemField
																expand
															>
																<ClayList.ItemTitle>
																	{scopeName}
																</ClayList.ItemTitle>
															</ClayList.ItemField>
														</ClayList.Item>
													</ClayList>
												</ClayPanel.Body>
											</ClayPanel>

											<ClayPanel
												collapsable
												defaultExpanded={true}
												displayTitle={
													<ClayPanel.Header className="p-0">
														<div className="align-items-center d-flex flex-wrap justify-content-between">
															<h2 className="mb-0 sheet-title">
																{Liferay.Language.get(
																	'conditions'
																)}
															</h2>

															<div className="criterion-string">
																<div className="btn-group">
																	<div className="btn-group-item inline-item mt-0">
																		{membersCountLoading && (
																			<ClayLoadingIndicator
																				className="mr-4"
																				small
																			/>
																		)}

																		{!membersCountLoading && (
																			<span className="mr-4">
																				{Liferay.Language.get(
																					'conditions-match'
																				)}

																				<b className="ml-2 text-dark">
																					{getPluralMessage(
																						Liferay.Language.get(
																							'x-member'
																						),
																						Liferay.Language.get(
																							'x-members'
																						),
																						membersCount
																					)}
																				</b>
																			</span>
																		)}

																		<ClayButton
																			displayType="secondary"
																			onClick={(
																				event
																			) => {
																				handleViewMembersClick(
																					event
																				);
																			}}
																			small
																			type="button"
																		>
																			{Liferay.Language.get(
																				'view-members'
																			)}
																		</ClayButton>
																	</div>
																</div>
															</div>
														</div>
													</ClayPanel.Header>
												}
											>
												<ClayPanel.Body>
													{emptyContributors &&
														(editingId ===
															undefined ||
															!editing) && (
															<EmptyPlaceholder />
														)}

													{contributors
														.filter((criteria) => {
															const editingCriteria =
																editingId ===
																	criteria.propertyKey &&
																editing;
															const emptyCriteriaQuery =
																criteria.query ===
																'';

															return (
																editingCriteria ||
																!emptyCriteriaQuery
															);
														})
														.map((criteria, i) => {
															return (
																<React.Fragment
																	key={i}
																>
																	{i !==
																		0 && (
																		<>
																			<Conjunction
																				className="mb-4 ml-0 mt-4"
																				conjunctionName={
																					criteria.conjunctionId
																				}
																				editing={
																					editing
																				}
																				onSelect={
																					onConjunctionChange
																				}
																			/>
																		</>
																	)}

																	<CriteriaBuilder
																		criteria={
																			criteria.criteriaMap
																		}
																		editing={
																			editing
																		}
																		emptyContributors={
																			emptyContributors
																		}
																		entityName={
																			criteria.entityName
																		}
																		modelLabel={
																			criteria.modelLabel
																		}
																		onChange={
																			this
																				._handleCriteriaChange
																		}
																		propertyKey={
																			criteria.propertyKey
																		}
																		renderEmptyValuesErrors={
																			renderEmptyValuesErrors
																		}
																		supportedProperties={
																			criteria.properties
																		}
																	/>
																</React.Fragment>
															);
														})}
												</ClayPanel.Body>
											</ClayPanel>
										</>
									) : (
										<ClayLayout.Sheet>
											<div className="d-flex flex-wrap justify-content-between mb-4">
												<h2 className="mb-2 sheet-title">
													{Liferay.Language.get(
														'conditions'
													)}
												</h2>

												<div className="criterion-string">
													<div className="btn-group">
														<div className="btn-group-item inline-item">
															{membersCountLoading && (
																<ClayLoadingIndicator
																	className="mr-4"
																	small
																/>
															)}

															{!membersCountLoading && (
																<span className="mr-4">
																	{Liferay.Language.get(
																		'conditions-match'
																	)}

																	<b className="ml-2 text-dark">
																		{getPluralMessage(
																			Liferay.Language.get(
																				'x-member'
																			),
																			Liferay.Language.get(
																				'x-members'
																			),
																			membersCount
																		)}
																	</b>
																</span>
															)}

															<ClayButton
																displayType="secondary"
																onClick={
																	onPreviewMembers
																}
																small
																type="button"
															>
																{Liferay.Language.get(
																	'view-members'
																)}
															</ClayButton>
														</div>
													</div>
												</div>
											</div>

											{emptyContributors &&
												(editingId === undefined ||
													!editing) && (
													<EmptyPlaceholder />
												)}

											{contributors
												.filter((criteria) => {
													const editingCriteria =
														editingId ===
															criteria.propertyKey &&
														editing;
													const emptyCriteriaQuery =
														criteria.query === '';

													return (
														editingCriteria ||
														!emptyCriteriaQuery
													);
												})
												.map((criteria, i) => {
													return (
														<React.Fragment key={i}>
															{i !== 0 && (
																<>
																	<Conjunction
																		className="mb-4 ml-0 mt-4"
																		conjunctionName={
																			criteria.conjunctionId
																		}
																		editing={
																			editing
																		}
																		onSelect={
																			onConjunctionChange
																		}
																	/>
																</>
															)}

															<CriteriaBuilder
																criteria={
																	criteria.criteriaMap
																}
																editing={
																	editing
																}
																emptyContributors={
																	emptyContributors
																}
																entityName={
																	criteria.entityName
																}
																modelLabel={
																	criteria.modelLabel
																}
																onChange={
																	this
																		._handleCriteriaChange
																}
																propertyKey={
																	criteria.propertyKey
																}
																renderEmptyValuesErrors={
																	renderEmptyValuesErrors
																}
																supportedProperties={
																	criteria.properties
																}
															/>
														</React.Fragment>
													);
												})}
										</ClayLayout.Sheet>
									)}
								</div>
							</ClayLayout.ContainerFluid>
						</div>
					</div>
				</div>
			</DndProvider>
		);
	}
}

export default ContributorBuilder;
