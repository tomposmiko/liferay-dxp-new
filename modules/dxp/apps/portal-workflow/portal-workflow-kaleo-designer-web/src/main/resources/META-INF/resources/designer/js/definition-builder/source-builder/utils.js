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

import {DEFAULT_LANGUAGE} from './constants';

export function parseActions(node) {
	const actions = {};

	node.actions.forEach((item) => {
		actions.name = parseProperty(actions, item, 'name');
		actions.description = parseProperty(actions, item, 'description');
		actions.executionType = parseProperty(actions, item, 'execution-type');
		actions.priority = parseProperty(actions, item, 'priority');
		actions.script = parseProperty(actions, item, 'script');
		actions.scriptLanguage = parseProperty(
			actions,
			item,
			'script-language'
		);
		actions.status = parseProperty(actions, item, 'status');
	});

	return actions;
}

export function parseAssignments(node) {
	const assignments = {};
	const autoCreateValues = [];
	const roleKeys = [];
	const roleTypes = [];
	const users = [];
	const typeUser = Object.keys(node.assignments[0])[0];

	node.assignments.forEach((item) => {
		const itemKeys = Object.keys(item);

		if (itemKeys.includes('resource-action')) {
			assignments.assignmentType = ['resourceActions'];
			assignments.resourceAction = item['resource-action'];
		}
		else if (itemKeys.includes('role-id')) {
			assignments.assignmentType = ['roleId'];
			assignments.roleId = parseInt(item['role-id'], 10);
		}
		else if (itemKeys.includes('role-type')) {
			assignments.assignmentType = ['roleType'];
			autoCreateValues.push(item['auto-create']);
			roleKeys.push(item.name);
			roleTypes.push(item['role-type']);
		}
		else if (itemKeys.includes('script')) {
			assignments.assignmentType = ['scriptedAssignment'];
			assignments.script = [item.script];
			assignments.scriptLanguage = item['script-language'];
		}
		else if (itemKeys.includes('user')) {
			assignments.assignmentType = ['user'];
		}
		else if (itemKeys.includes('email-address')) {
			assignments.assignmentType = ['user'];
			users.push(item['email-address']);
		}
		else if (itemKeys.includes('user-id')) {
			assignments.assignmentType = ['user'];
			users.push(item['user-id']);
		}
		else if (itemKeys.includes('screen-name')) {
			assignments.assignmentType = ['user'];
			users.push(item['screen-name']);
		}
	});

	if (users.length) {
		if (typeUser === 'email-address') {
			assignments.emailAddress = users;
		}
		if (typeUser === 'user-id') {
			assignments.userId = users;
		}
		if (typeUser === 'screen-name') {
			assignments.screenName = users;
		}
	}

	if (assignments.assignmentType[0] === 'roleType') {
		assignments.autoCreate = autoCreateValues[0];
		assignments.roleKey = roleKeys[0];
		assignments.roleType = roleTypes[0];
	}

	return assignments;
}

export function parseReassignments(node) {
	const assignments = {};
	const autoCreateValues = [];
	const roleKeys = [];
	const roleTypes = [];
	const users = [];
	const typeUser = Object.keys(node.assignments[0])[0];

	node.assignments.forEach((item) => {
		const itemKeys = Object.keys(item);
		if (itemKeys.includes('resource-action')) {
			assignments.assignmentType = ['resourceActions'];
			assignments.resourceAction = item['resource-action'];
		}
		else if (itemKeys.includes('role')) {
			assignments.assignmentType = ['roleId'];
			assignments.roleId = parseInt(item['role'], 10);
		}
		else if (itemKeys.includes('role-type')) {
			assignments.assignmentType = ['roleType'];
			autoCreateValues.push(item['auto-create']);
			roleKeys.push(item.name);
			roleTypes.push(item['role-type']);
		}
		else if (itemKeys.includes('script')) {
			assignments.assignmentType = ['scriptedAssignment'];
			assignments.script = [item.script];
			assignments.scriptLanguage = item['script-language'];
		}
		else if (itemKeys.includes('user')) {
			assignments.assignmentType = ['user'];
		}
		else if (itemKeys.includes('email-address')) {
			assignments.assignmentType = ['user'];
			users.push(item['email-address']);
		}
		else if (itemKeys.includes('user-id')) {
			assignments.assignmentType = ['user'];
			users.push(item['user-id']);
		}
		else if (itemKeys.includes('screen-name')) {
			assignments.assignmentType = ['user'];
			users.push(item['screen-name']);
		}
	});

	if (users.length) {
		if (typeUser === 'email-address') {
			assignments.emailAddress = users;
		}
		if (typeUser === 'user-id') {
			assignments.userId = users;
		}
		if (typeUser === 'screen-name') {
			assignments.screenName = users;
		}
	}

	if (assignments.assignmentType[0] === 'roleType') {
		assignments.autoCreate = autoCreateValues[0];
		assignments.roleKey = roleKeys[0];
		assignments.roleType = roleTypes[0];
	}

	return assignments;
}

export function parseNotifications(node) {
	const notifications = {notificationTypes: [], recipients: []};

	node.notifications.forEach((item, index) => {
		notifications.description = parseProperty(
			notifications,
			item,
			'description'
		);
		notifications.executionType = parseProperty(
			notifications,
			item,
			'execution-type'
		);
		notifications.name = parseProperty(notifications, item, 'name');

		let notificationTypes = parseProperty(
			notifications,
			item,
			'notification-type'
		);

		if (Array.isArray(notificationTypes[0])) {
			const typeArray = [];
			notificationTypes[0].forEach((type) => {
				typeArray.push({notificationType: type});
			});

			notificationTypes = typeArray;
		}
		else {
			notificationTypes = [{notificationType: notificationTypes[0]}];
		}

		notifications.notificationTypes[index] = notificationTypes;

		notifications.template = parseProperty(notifications, item, 'template');
		notifications.templateLanguage = parseProperty(
			notifications,
			item,
			'template-language'
		);

		if (!notifications.recipients[index]) {
			notifications.recipients[index] = [];
		}

		if (item.assignees) {
			if (item.receptionType) {
				notifications.recipients[index].push({
					assignmentType: ['taskAssignees'],
					receptionType: [
						item.receptionType[
							notifications.recipients[index].length
						],
					],
				});
			}
			else {
				notifications.recipients[index].push({
					assignmentType: ['taskAssignees'],
				});
			}
		}

		if (item['user']) {
			if (item['user'].some((item) => item['email-address'])) {
				const emailAddress = [];

				item['user'].forEach((item) => {
					emailAddress.push(item['email-address']);
				});

				if (item.receptionType) {
					notifications.recipients[index].push({
						assignmentType: ['user'],
						emailAddress,
						receptionType: [
							item.receptionType[
								notifications.recipients[index].length
							],
						],
					});
				}
				else {
					notifications.recipients[index].push({
						assignmentType: ['user'],
						emailAddress,
					});
				}
			}

			if (item['user'].some((item) => item['user-id'])) {
				const userId = [];

				item['user'].forEach((item) => {
					userId.push(item['user-id']);
				});

				if (item.receptionType) {
					notifications.recipients[index].push({
						assignmentType: ['user'],
						receptionType: [
							item.receptionType[
								notifications.recipients[index].length
							],
						],
						userId,
					});
				}
				else {
					notifications.recipients[index].push({
						assignmentType: ['user'],
						userId,
					});
				}
			}

			if (item['user'].some((item) => item['screen-name'])) {
				const screenName = [];

				item['user'].forEach((item) => {
					screenName.push(item['screen-name']);
				});

				if (item.receptionType) {
					notifications.recipients[index].push({
						assignmentType: ['user'],
						receptionType: [
							item.receptionType[
								notifications.recipients[index].length
							],
						],
						screenName,
					});
				}
				else {
					notifications.recipients[index].push({
						assignmentType: ['user'],
						screenName,
					});
				}
			}
		}

		if (item['role-type']) {
			if (item.receptionType) {
				notifications.recipients[index].push({
					assignmentType: ['roleType'],
					autoCreate: item['auto-create'],
					receptionType: [
						item.receptionType[
							notifications.recipients[index].length
						],
					],
					roleKey: item['role-name'],
					roleType: item['role-type'],
				});
			}
			else {
				notifications.recipients[index].push({
					assignmentType: ['roleType'],
					autoCreate: item['auto-create'],
					roleKey: item['role-name'],
					roleType: item['role-type'],
				});
			}
		}

		if (item['role-id']) {
			if (item.receptionType) {
				notifications.recipients[index].push({
					assignmentType: ['roleId'],
					receptionType: [
						item.receptionType[
							notifications.recipients[index].length
						],
					],
					roleId: item['role-id'][0],
				});
			}
			else {
				notifications.recipients[index].push({
					assignmentType: ['roleId'],
					roleId: item['role-id'][0],
				});
			}
		}

		if (item['scripted-recipient']) {
			const scriptedRecipient = item['scripted-recipient'][0];

			const script = scriptedRecipient.script;
			const scriptLanguage = scriptedRecipient['script-language'];

			if (item.receptionType) {
				notifications.recipients[index].push({
					assignmentType: ['scriptedRecipient'],
					receptionType: [
						item.receptionType[
							notifications.recipients[index].length
						],
					],
					script: [script],
					scriptLanguage: scriptLanguage || [DEFAULT_LANGUAGE],
				});
			}
			else {
				notifications.recipients[index].push({
					assignmentType: ['scriptedRecipient'],
					script: [script],
					scriptLanguage: scriptLanguage || [DEFAULT_LANGUAGE],
				});
			}
		}

		if (!notifications.recipients[index].length) {
			if (item.receptionType) {
				notifications.recipients[index].push({
					assignmentType: ['user'],
					receptionType: [
						item.receptionType[
							notifications.recipients[index].length
						],
					],
				});
			}
			else {
				notifications.recipients[index].push({
					assignmentType: ['user'],
				});
			}
		}
	});

	return notifications;
}

function parseProperty(data, item, property) {
	let newProperty = property;

	if (property === 'execution-type') {
		newProperty = 'executionType';
	}
	else if (property === 'script-language') {
		newProperty = 'scriptLanguage';
	}
	else if (property === 'template-language') {
		newProperty = 'templateLanguage';
	}
	if (Array.isArray(data[newProperty])) {
		data[newProperty].push(item[property]);

		return data[newProperty];
	}

	return new Array(item[property]);
}

export function parseTimers(node) {
	const taskTimers = {};
	taskTimers.delay = [];
	taskTimers.reassignments = [];
	taskTimers.timerActions = [];
	taskTimers.timerNotifications = [];

	node.taskTimers.forEach((item, index) => {
		taskTimers.delay.push({
			duration: node.taskTimers[index].duration,
			scale: node.taskTimers[index].scale,
		});
		taskTimers.reassignments.push(
			node.taskTimers[index]['reassignments']
				? parseReassignments({
						assignments: node.taskTimers[index]['reassignments'],
				  })
				: {}
		);
		taskTimers.timerActions.push(
			node.taskTimers[index]['timer-action']
				? parseActions({
						actions: node.taskTimers[index]['timer-action'],
				  })
				: {}
		);
		taskTimers.timerNotifications.push({});
		taskTimers.name = parseProperty(taskTimers, item, 'name');
		taskTimers.description = parseProperty(taskTimers, item, 'description');
		taskTimers.blocking = parseProperty(taskTimers, item, 'blocking');
	});

	return taskTimers;
}
