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

import {isNode} from 'react-flow-renderer';

import {
	retrieveAccountRoles,
	retrieveRoleById,
	retrieveRoles,
	retrieveUsersBy,
} from '../../util/fetchUtil';

const populateNotificationsData = (
	accountEntryId,
	initialElements,
	setElements
) => {
	for (let i = 0; i < initialElements.length; i++) {
		const element = initialElements[i];

		if (isNode(element) && element.data.notifications) {
			const recipients = element.data.notifications.recipients;

			recipients.map((recipient, index) => {
				if (recipient[0] !== undefined) {
					recipient = recipient[0];
				}

				if (recipient?.assignmentType?.[0] === 'roleId') {
					retrieveRoleById(recipient.roleId)
						.then((response) => response.json())
						.then((response) => {
							initialElements[i].data.notifications.recipients[
								index
							].sectionsData = {
								id: response.id,
								name: response.name,
								roleType: response.roleType,
							};

							setElements([...initialElements]);
						});
				}
				else if (recipient?.assignmentType?.[0] === 'roleType') {
					Promise.all([
						retrieveRoles(),
						retrieveAccountRoles(accountEntryId),
					]).then(([response1, response2]) =>
						Promise.all([response1.json(), response2.json()]).then(
							([roles, accountRoles]) => {
								const items = roles.items.concat(
									accountRoles.items
								);

								initialElements[
									i
								].data.notifications.recipients[
									index
								].roleKey.forEach((key) => {
									const role = items.find(
										(item) =>
											item.externalReferenceCode ===
												key || item.displayName === key
									);

									if (
										!initialElements[i].data.notifications
											.recipients[index].roleName
									) {
										initialElements[
											i
										].data.notifications.recipients[
											index
										].roleName = [];
									}

									initialElements[
										i
									].data.notifications.recipients[
										index
									].roleName.push(role?.name);
								});

								setElements([...initialElements]);
							}
						)
					);
				}
				else if (
					recipient?.assignmentType?.[0] === 'user' &&
					(recipient.emailAddress ||
						recipient.screenName ||
						recipient.userId)
				) {
					const sectionsData = [];

					let filterTypeRetrieveUsersBy = Object.keys(recipient)[1];
					const keywordRetrieveUsersBy = Object.values(recipient)[1];

					if (filterTypeRetrieveUsersBy === 'screenName') {
						filterTypeRetrieveUsersBy = 'alternateName';
					}
					else if (filterTypeRetrieveUsersBy === 'userId') {
						filterTypeRetrieveUsersBy = filterTypeRetrieveUsersBy
							.toLocaleLowerCase()
							.replace('user', '');
					}

					retrieveUsersBy(
						filterTypeRetrieveUsersBy,
						keywordRetrieveUsersBy
					)
						.then((response) => response.json())
						.then(({items}) => {
							items.forEach((item, index) => {
								sectionsData.push({
									emailAddress: item.emailAddress,
									identifier: `${Date.now()}-${index}`,
									name: item.name,
									screenName: item.alternateName,
									userId: item.id,
								});
							});
						})
						.then(() => {
							initialElements[i].data.notifications.recipients[
								index
							].sectionsData = sectionsData;

							setElements([...initialElements]);
						});
				}
			});
		}
	}
};

export default populateNotificationsData;
