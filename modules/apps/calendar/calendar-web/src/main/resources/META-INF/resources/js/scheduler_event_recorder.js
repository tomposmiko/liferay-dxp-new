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
	'liferay-scheduler-event-recorder',
	(A) => {
		const AArray = A.Array;
		const Lang = A.Lang;

		const CalendarWorkflow = Liferay.CalendarWorkflow;

		const isObject = Lang.isObject;
		const isString = Lang.isString;
		const isValue = Lang.isValue;

		const toInt = function (value) {
			return Lang.toInt(value, 10, 0);
		};

		const STR_BLANK = '';

		const STR_COMMA_SPACE = ', ';

		const CalendarUtil = Liferay.CalendarUtil;

		const SchedulerEventRecorder = A.Component.create({
			ATTRS: {
				calendarContainer: {
					validator: isObject,
					value: null,
				},

				calendarId: {
					setter: toInt,
					value: 0,
				},

				dateFormat: {
					validator: isString,
					value: Liferay.Language.get('a-b-d'),
				},

				editCalendarBookingURL: {
					setter: String,
					validator: isValue,
					value: STR_BLANK,
				},

				permissionsCalendarBookingURL: {
					setter: String,
					validator: isValue,
					value: STR_BLANK,
				},

				portletNamespace: {
					setter: String,
					validator: isValue,
					value: STR_BLANK,
				},

				remoteServices: {
					validator: isObject,
					value: null,
				},

				status: {
					setter: toInt,
					value: CalendarWorkflow.STATUS_DRAFT,
				},

				toolbar: {
					value: {
						children: [],
					},
				},

				viewCalendarBookingURL: {
					setter: String,
					validator: isValue,
					value: STR_BLANK,
				},
			},

			EXTENDS: A.SchedulerEventRecorder,

			NAME: 'scheduler-event-recorder',

			prototype: {
				_getFooterToolbar() {
					const instance = this;

					let schedulerEvent = instance.get('event');

					let schedulerEventCreated = false;

					if (schedulerEvent) {
						schedulerEventCreated = true;
					}
					else {
						schedulerEvent = instance;
					}

					const children = [];
					const editGroup = [];
					const respondGroup = [];

					const calendarContainer = instance.get('calendarContainer');

					const calendar = calendarContainer.getCalendar(
						schedulerEvent.get('calendarId')
					);
					const status = schedulerEvent.get('status');

					if (calendar) {
						const permissions = calendar.get('permissions');

						if (
							instance._hasSaveButton(
								permissions,
								calendar,
								status
							)
						) {
							editGroup.push({
								id: 'saveBtn',
								label: Liferay.Language.get('save'),
								on: {
									click: A.bind(
										instance._handleSaveEvent,
										instance
									),
								},
								primary: true,
							});
						}

						if (
							instance._hasEditButton(
								permissions,
								calendar,
								status
							)
						) {
							editGroup.push({
								id: 'editBtn',
								label: Liferay.Language.get('edit'),
								on: {
									click: A.bind(
										instance._handleEditEvent,
										instance
									),
								},
							});
						}

						if (
							schedulerEventCreated === true &&
							permissions.VIEW_BOOKING_DETAILS
						) {
							editGroup.push({
								id: 'viewBtn',
								label: Liferay.Language.get('view-details'),
								on: {
									click: A.bind(
										instance._handleViewEvent,
										instance
									),
								},
							});
						}

						if (
							schedulerEvent.isMasterBooking() &&
							instance._hasDeleteButton(
								permissions,
								calendar,
								status
							)
						) {
							editGroup.push({
								id: 'deleteBtn',
								label: Liferay.Language.get('delete'),
								on: {
									click: A.bind(
										instance._handleDeleteEvent,
										instance
									),
								},
							});
						}

						if (editGroup.length) {
							children.push(editGroup);
						}

						if (respondGroup.length) {
							children.push(respondGroup);
						}
					}

					return children;
				},

				_handleEditEvent() {
					const instance = this;

					const scheduler = instance.get('scheduler');

					const activeViewName = scheduler
						.get('activeView')
						.get('name');

					const date = scheduler.get('date');

					const schedulerEvent = instance.get('event');

					const editCalendarBookingURL = decodeURIComponent(
						instance.get('editCalendarBookingURL')
					);

					const data = instance.serializeForm();

					data.activeView = activeViewName;

					data.date = date.getTime();

					const endTime = new Date(data.endTime);

					data.endTimeDay = endTime.getDate();
					data.endTimeHour = endTime.getHours();
					data.endTimeMinute = endTime.getMinutes();
					data.endTimeMonth = endTime.getMonth();
					data.endTimeYear = endTime.getFullYear();

					const startTime = new Date(data.startTime);

					data.startTimeDay = startTime.getDate();
					data.startTimeHour = startTime.getHours();
					data.startTimeMinute = startTime.getMinutes();
					data.startTimeMonth = startTime.getMonth();
					data.startTimeYear = startTime.getFullYear();

					data.titleCurrentValue = encodeURIComponent(data.content);

					if (schedulerEvent) {
						data.allDay = schedulerEvent.get('allDay');
						data.calendarBookingId = schedulerEvent.get(
							'calendarBookingId'
						);
					}

					Liferay.Util.openWindow({
						dialog: {
							after: {
								destroy() {
									scheduler.load();
								},
							},
							destroyOnHide: true,
							modal: true,
						},
						dialogIframe: {
							bodyCssClass: 'dialog-with-footer',
						},
						refreshWindow: window,
						title: Liferay.Language.get('edit-calendar-booking'),
						uri: CalendarUtil.fillURLParameters(
							editCalendarBookingURL,
							data
						),
					});

					instance.hidePopover();
				},

				_handleEventAnswer(event) {
					const instance = this;

					const currentTarget = event.currentTarget;

					const schedulerEvent = instance.get('event');

					const linkEnabled = A.DataType.Boolean.parse(
						currentTarget.hasClass('calendar-event-answer-true')
					);

					const statusData = toInt(currentTarget.getData('status'));

					if (schedulerEvent && linkEnabled) {
						const remoteServices = instance.get('remoteServices');

						if (schedulerEvent.isRecurring()) {
							Liferay.RecurrenceUtil.openConfirmationPanel(
								'invokeTransition',
								() => {
									remoteServices.invokeTransition(
										schedulerEvent,
										schedulerEvent.get('instanceIndex'),
										statusData,
										true,
										false
									);
								},
								() => {
									remoteServices.invokeTransition(
										schedulerEvent,
										schedulerEvent.get('instanceIndex'),
										statusData,
										true,
										true
									);
								},
								() => {
									remoteServices.invokeTransition(
										schedulerEvent,
										schedulerEvent.get('instanceIndex'),
										statusData,
										false,
										false
									);
								}
							);
						}
						else {
							remoteServices.invokeTransition(
								schedulerEvent,
								0,
								statusData,
								false,
								false
							);
						}
					}
				},

				_handleViewEvent(event) {
					const instance = this;

					const viewCalendarBookingURL = decodeURIComponent(
						instance.get('viewCalendarBookingURL')
					);

					const data = instance.serializeForm();

					const schedulerEvent = instance.get('event');

					data.calendarBookingId = schedulerEvent.get(
						'calendarBookingId'
					);

					Liferay.Util.openWindow({
						dialog: {
							after: {
								destroy() {
									schedulerEvent.syncWithServer();
								},
							},
							destroyOnHide: true,
							modal: true,
						},
						refreshWindow: window,
						title: Liferay.Language.get(
							'view-calendar-booking-details'
						),
						uri: CalendarUtil.fillURLParameters(
							viewCalendarBookingURL,
							data
						),
					});

					event.domEvent.preventDefault();
				},

				_hasDeleteButton(permissions, calendar, _status) {
					return permissions.MANAGE_BOOKINGS && calendar;
				},

				_hasEditButton(permissions, _calendar, _status) {
					return permissions.MANAGE_BOOKINGS;
				},

				_hasSaveButton(permissions, _calendar, _status) {
					return permissions.MANAGE_BOOKINGS;
				},

				_hasWorkflowStatusPermission(schedulerEvent, newStatus) {
					const instance = this;

					let hasPermission = false;

					if (schedulerEvent) {
						const calendarId = schedulerEvent.get('calendarId');

						const calendarContainer = instance.get(
							'calendarContainer'
						);

						const calendar = calendarContainer.getCalendar(
							calendarId
						);

						const permissions = calendar.get('permissions');

						const status = schedulerEvent.get('status');

						hasPermission =
							permissions.MANAGE_BOOKINGS &&
							status !== newStatus &&
							status !== CalendarWorkflow.STATUS_DRAFT;
					}

					return hasPermission;
				},

				_renderPopOver() {
					const instance = this;

					const popoverBB = instance.popover.get('boundingBox');

					SchedulerEventRecorder.superclass._renderPopOver.apply(
						this,
						arguments
					);

					popoverBB.delegate(
						['change', 'keypress'],
						(event) => {
							const schedulerEvent =
								instance.get('event') || instance;

							const calendarId = toInt(event.currentTarget.val());

							const calendarContainer = instance.get(
								'calendarContainer'
							);

							const selectedCalendar = calendarContainer.getCalendar(
								calendarId
							);

							if (selectedCalendar) {
								schedulerEvent.set(
									'color',
									selectedCalendar.get('color'),
									{
										silent: true,
									}
								);
							}
						},
						'#' +
							instance.get('portletNamespace') +
							'eventRecorderCalendar'
					);
				},

				_showResources() {
					const instance = this;

					const schedulerEvent = instance.get('event');

					const popoverBB = instance.popover.get('boundingBox');

					popoverBB.toggleClass(
						'calendar-portlet-event-recorder-editing',
						!!schedulerEvent
					);

					const calendarContainer = instance.get('calendarContainer');

					const defaultCalendar = calendarContainer.get(
						'defaultCalendar'
					);

					let calendarId = defaultCalendar.get('calendarId');
					let color = defaultCalendar.get('color');

					let eventInstance = instance;

					if (schedulerEvent) {
						calendarId = schedulerEvent.get('calendarId');

						const calendar = calendarContainer.getCalendar(
							calendarId
						);

						if (calendar) {
							color = calendar.get('color');

							eventInstance = schedulerEvent;
						}
					}

					eventInstance.set('color', color, {
						silent: true,
					});

					const portletNamespace = instance.get('portletNamespace');

					const eventRecorderCalendar = document.querySelector(
						`#${portletNamespace}eventRecorderCalendar`
					);

					if (eventRecorderCalendar) {
						eventRecorderCalendar.value = calendarId.toString();
					}

					instance._syncInvitees();
				},

				_syncInvitees() {
					const instance = this;

					const schedulerEvent = instance.get('event');

					if (schedulerEvent) {
						const calendarContainer = instance.get(
							'calendarContainer'
						);

						const calendar = calendarContainer.getCalendar(
							schedulerEvent.get('calendarId')
						);

						if (calendar) {
							const permissions = calendar.get('permissions');

							if (permissions.VIEW_BOOKING_DETAILS) {
								const parentCalendarBookingId = schedulerEvent.get(
									'parentCalendarBookingId'
								);

								const portletNamespace = instance.get(
									'portletNamespace'
								);

								const remoteServices = instance.get(
									'remoteServices'
								);

								remoteServices.getCalendarBookingInvitees(
									parentCalendarBookingId,
									(data) => {
										const results = AArray.partition(
											data,
											(item) => {
												return (
													toInt(item.classNameId) ===
													CalendarUtil.USER_CLASS_NAME_ID
												);
											}
										);

										instance._syncInviteesContent(
											'#' +
												portletNamespace +
												'eventRecorderUsers',
											results.matches
										);
										instance._syncInviteesContent(
											'#' +
												portletNamespace +
												'eventRecorderResources',
											results.rejects
										);
									}
								);
							}
						}
					}
				},

				_syncInviteesContent(contentNode, calendarResources) {
					const values = calendarResources.map((item) => {
						return Liferay.Util.escapeHTML(item.name);
					});

					contentNode = document.querySelector(contentNode);

					const messageNode = contentNode.querySelector(
						'.calendar-portlet-invitees'
					);

					let messageHTML = '&mdash;';

					if (values.length) {
						contentNode.style.display = '';

						messageHTML = values.join(STR_COMMA_SPACE);
					}

					messageNode.innerHTML = messageHTML;
				},

				getTemplateData() {
					const instance = this;

					let editing = true;

					let schedulerEvent = instance.get('event');

					if (!schedulerEvent) {
						editing = false;

						schedulerEvent = instance;
					}

					const calendarContainer = instance.get('calendarContainer');

					const calendar = calendarContainer.getCalendar(
						schedulerEvent.get('calendarId')
					);

					const permissions = calendar.get('permissions');

					const templateData = SchedulerEventRecorder.superclass.getTemplateData.apply(
						this,
						arguments
					);

					return {
						...templateData,
						acceptLinkEnabled: instance._hasWorkflowStatusPermission(
							schedulerEvent,
							CalendarWorkflow.STATUS_APPROVED
						),
						allDay: schedulerEvent.get('allDay'),
						availableCalendars: calendarContainer.get(
							'availableCalendars'
						),
						calendar,
						calendarIds: Object.keys(
							calendarContainer.get('availableCalendars')
						),
						declineLinkEnabled: instance._hasWorkflowStatusPermission(
							schedulerEvent,
							CalendarWorkflow.STATUS_DENIED
						),
						editing,
						endTime: templateData.endDate,
						hasWorkflowInstanceLink: schedulerEvent.get(
							'hasWorkflowInstanceLink'
						),
						instanceIndex: schedulerEvent.get('instanceIndex'),
						maybeLinkEnabled: instance._hasWorkflowStatusPermission(
							schedulerEvent,
							CalendarWorkflow.STATUS_MAYBE
						),
						permissions,
						startTime: templateData.startDate,
						status: schedulerEvent.get('status'),
						workflowStatus: CalendarWorkflow,
					};
				},

				getUpdatedSchedulerEvent(optAttrMap) {
					const instance = this;

					const attrMap = {
						color: instance.get('color'),
					};

					const event = instance.get('event');

					if (event) {
						const calendarContainer = instance.get(
							'calendarContainer'
						);

						const calendar = calendarContainer.getCalendar(
							event.get('calendarId')
						);

						if (calendar) {
							attrMap.color = calendar.get('color');
						}
					}

					return SchedulerEventRecorder.superclass.getUpdatedSchedulerEvent.call(
						instance,
						{...attrMap, ...optAttrMap}
					);
				},

				initializer() {
					const instance = this;

					const popoverBB = instance.popover.get('boundingBox');

					popoverBB.delegate(
						'click',
						instance._handleEventAnswer,
						'.calendar-event-answer',
						instance
					);
				},

				isMasterBooking: Lang.emptyFnFalse,

				populateForm() {
					const instance = this;

					const bodyTemplate = instance.get('bodyTemplate');

					const headerTemplate = instance.get('headerTemplate');

					const templateData = instance.getTemplateData();

					if (
						A.instanceOf(bodyTemplate, A.Template) &&
						A.instanceOf(headerTemplate, A.Template)
					) {
						instance.popover.setStdModContent(
							'body',
							bodyTemplate.parse(templateData)
						);
						instance.popover.setStdModContent(
							'header',
							headerTemplate.parse(templateData)
						);

						instance.popover.addToolbar(
							instance._getFooterToolbar(),
							'footer'
						);
					}
					else {
						SchedulerEventRecorder.superclass.populateForm.apply(
							instance,
							arguments
						);
					}

					instance.popover.addToolbar(
						[
							{
								cssClass: 'close',
								label: '\u00D7',
								on: {
									click: A.bind(
										instance._handleCancelEvent,
										instance
									),
								},
								render: true,
							},
						],
						'body'
					);

					if (instance.popover.headerNode) {
						instance.popover.headerNode.toggleClass(
							'hide',
							!templateData.permissions.VIEW_BOOKING_DETAILS
						);
					}

					instance._showResources();
				},
			},
		});

		Liferay.SchedulerEventRecorder = SchedulerEventRecorder;
	},
	'',
	{
		requires: ['dd-plugin', 'liferay-calendar-util', 'resize-plugin'],
	}
);
