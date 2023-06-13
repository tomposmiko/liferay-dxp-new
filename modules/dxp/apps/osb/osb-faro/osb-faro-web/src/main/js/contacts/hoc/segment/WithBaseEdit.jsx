import * as API from 'shared/api';
import * as breadcrumbs from 'shared/util/breadcrumbs';
import autobind from 'autobind-decorator';
import BasePage from 'shared/components/base-page';
import getCN from 'classnames';
import Label from 'shared/components/Label';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {ChannelContext} from 'shared/context/channel';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {PropTypes} from 'prop-types';
import {Routes, SEGMENTS, toRoute} from 'shared/util/router';
import {Segment} from 'shared/util/records';
import {SegmentTypes} from 'shared/util/constants';

export default WrappedComponent => {
	class BaseEdit extends React.Component {
		static contextType = ChannelContext;

		static propTypes = {
			addAlert: PropTypes.func.isRequired,
			channelId: PropTypes.string,
			close: PropTypes.func.isRequired,
			groupId: PropTypes.string.isRequired,
			history: PropTypes.object.isRequired,
			id: PropTypes.string,
			open: PropTypes.func.isRequired,
			segment: PropTypes.instanceOf(Segment),
			type: PropTypes.oneOf([SegmentTypes.Dynamic, SegmentTypes.Static])
		};

		state = {
			onDelete: false
		};

		componentDidMount() {
			this._startDate = Date.now();
		}

		@autobind
		deleteSegment() {
			const {
				addAlert,
				channelId,
				close,
				groupId,
				history,
				id,
				open
			} = this.props;

			open(modalTypes.CONFIRMATION_MODAL, {
				message: (
					<div>
						<h4 className='text-secondary'>
							{Liferay.Language.get(
								'are-you-sure-you-want-to-delete-this-segment'
							)}
						</h4>

						<p>
							{Liferay.Language.get(
								'you-will-lose-all-data-related-to-this-segment.-you-will-not-be-able-to-undo-this-operation'
							)}
						</p>
					</div>
				),
				modalVariant: 'modal-warning',
				onClose: close,
				onSubmit: () => {
					this.setState({onDelete: true});

					return API.individualSegment
						.delete({
							groupId,
							id
						})
						.then(() => {
							addAlert({
								alertType: Alert.Types.Success,
								message: Liferay.Language.get(
									'the-segment-has-been-deleted'
								)
							});

							history.push(
								toRoute(Routes.CONTACTS_LIST_ENTITY, {
									channelId,
									groupId,
									type: SEGMENTS
								})
							);
						})
						.catch(() => {
							addAlert({
								alertType: Alert.Types.Error,
								message: Liferay.Language.get('error'),
								timeout: false
							});

							this.setState({onDelete: false});
						});
				},
				submitButtonDisplay: 'warning',
				submitMessage: Liferay.Language.get('delete'),
				title: Liferay.Language.get('warning'),
				titleIcon: 'warning-full'
			});
		}

		getPageTitle() {
			const {segment} = this.props;

			return this.props.id && segment
				? Liferay.Language.get('edit-individuals-segment')
				: Liferay.Language.get('create-individuals-segment');
		}

		getPageTitleLabel() {
			const {type} = this.props;

			return type === SegmentTypes.Static
				? Liferay.Language.get('static-segment')
				: Liferay.Language.get('dynamic-segment');
		}

		@autobind
		handleSubmit(form, formRef, submitFn) {
			const {
				addAlert,
				channelId,
				close,
				groupId,
				history,
				id,
				open
			} = this.props;

			const {setSubmitting} = formRef.current.getFormikActions();

			open(
				modalTypes.LOADING_MODAL,
				{
					message: Liferay.Language.get(
						'this-will-only-take-a-moment'
					),
					title: id
						? Liferay.Language.get('updating')
						: Liferay.Language.get('creating')
				},
				{closeOnBlur: false}
			);

			submitFn(form)
				.then(segment => {
					if (!id) {
						const {channelId, id, segmentType} = Array.isArray(
							segment
						)
							? segment[0]
							: segment;

						analytics.track(
							`${segmentType[0]}${segmentType
								.slice(1)
								.toLowerCase()} Segment Creation - Saved`,
							{
								channelId,
								createDelta: Date.now() - this._startDate,
								segmentId: id
							},
							{ip: '0'}
						);
					}

					if (
						(Array.isArray(segment) && segment.length) ||
						(segment && !Array.isArray(segment))
					) {
						history.push(
							toRoute(Routes.CONTACTS_ENTITY, {
								channelId,
								groupId,
								id: segment.id || segment[0].id,
								type: SEGMENTS
							})
						);

						addAlert({
							alertType: Alert.Types.Success,
							message: Liferay.Language.get(
								'changes-to-segment-saved'
							)
						});
					}

					setSubmitting(false);

					close();

					return segment;
				})
				.catch(() => {
					addAlert({
						alertType: Alert.Types.Error,
						message: Liferay.Language.get('error')
					});

					setSubmitting(false);

					close();
				});
		}

		render() {
			const {
				channelId,
				className,
				groupId,
				id,
				segment,
				type,
				...otherProps
			} = this.props;

			const {onDelete} = this.state;

			const {selectedChannel} = this.context;

			const editing = !!id;

			const breadcrumbItems = id
				? [
						breadcrumbs.getEntityName({
							active: false,
							href: toRoute(Routes.CONTACTS_SEGMENT, {
								channelId,
								groupId,
								id
							}),
							label: segment.name
						}),
						{
							active: true,
							label: Liferay.Language.get('edit')
						}
				  ]
				: [
						{
							active: true,
							label: Liferay.Language.get('create-segment')
						}
				  ];

			return (
				<BasePage
					className={getCN('segment-edit-root', className, {
						editing
					})}
					documentTitle={`${this.getPageTitle()} - ${Liferay.Language.get(
						'segment'
					)}`}
				>
					<BasePage.Header
						breadcrumbs={[
							breadcrumbs.getHome({
								channelId,
								groupId,
								label: selectedChannel && selectedChannel.name
							}),
							breadcrumbs.getSegments({channelId, groupId}),
							...breadcrumbItems
						]}
						groupId={groupId}
					>
						<BasePage.Row>
							<BasePage.Header.TitleSection
								title={this.getPageTitle()}
							>
								<Label display='secondary' size='lg' uppercase>
									{this.getPageTitleLabel()}
								</Label>
							</BasePage.Header.TitleSection>

							<BasePage.Header.Section>
								<BasePage.Header.PageActions
									actions={
										editing
											? [
													{
														label: Liferay.Language.get(
															'delete-segment'
														),
														onClick: this
															.deleteSegment
													}
											  ]
											: []
									}
								/>
							</BasePage.Header.Section>
						</BasePage.Row>
					</BasePage.Header>

					<BasePage.Body pageContainer={type === SegmentTypes.Static}>
						<WrappedComponent
							{...omitDefinedProps(
								otherProps,
								BaseEdit.propTypes
							)}
							channelId={channelId}
							editing={editing}
							groupId={groupId}
							id={id}
							onDelete={onDelete}
							onSubmit={this.handleSubmit}
							segment={segment}
						/>
					</BasePage.Body>
				</BasePage>
			);
		}
	}

	return connect(null, {
		addAlert,
		close,
		open
	})(BaseEdit);
};
