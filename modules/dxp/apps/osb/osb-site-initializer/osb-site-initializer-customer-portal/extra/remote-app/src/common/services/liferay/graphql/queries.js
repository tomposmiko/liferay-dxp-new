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

import {gql} from '@apollo/client';

export const getDXPCloudPageInfo = gql`
	query getDXPCloudPageInfo($accountSubscriptionsFilter: String) {
		c {
			accountSubscriptions(filter: $accountSubscriptionsFilter) {
				items {
					accountKey
					hasDisasterDataCenterRegion
					externalReferenceCode
					name
				}
			}
			dXPCDataCenterRegions {
				items {
					dxpcDataCenterRegionId
					name
					value
				}
			}
		}
	}
`;

export const getCommerceOrderItems = gql`
	query getCommerceOrderItems(
		$filter: String
		$page: Int = 1
		$pageSize: Int = 20
	) {
		orderItems(filter: $filter, page: $page, pageSize: $pageSize) {
			items {
				externalReferenceCode
				quantity
				customFields {
					name
					customValue {
						data
					}
				}
				options
			}
			totalCount
		}
	}
`;

export const patchOrderItemByExternalReferenceCode = gql`
	mutation patchOrderItemByExternalReferenceCode(
		$externalReferenceCode: String
		$orderItem: InputOrderItem
	) {
		patchOrderItemByExternalReferenceCode(
			externalReferenceCode: $externalReferenceCode
			orderItem: $orderItem
		)
	}
`;

export const getStructuredContentFolders = gql`
	query getStructuredContentFolders($siteKey: String!, $filter: String) {
		structuredContentFolders(siteKey: $siteKey, filter: $filter) {
			items {
				id
				name
				structuredContents {
					items {
						friendlyUrlPath
						id
						key
					}
				}
			}
		}
	}
`;

export const getAccountSubscriptions = gql`
	query getAccountSubscriptions($filter: String) {
		c {
			accountSubscriptions(filter: $filter) {
				items {
					accountKey
					accountSubscriptionGroupERC
					accountSubscriptionId
					c_accountSubscriptionId
					endDate
					externalReferenceCode
					instanceSize
					name
					quantity
					startDate
					subscriptionStatus
				}
			}
		}
	}
`;

export const addAccountFlag = gql`
	mutation addAccountFlag($accountFlag: InputC_AccountFlag!) {
		createAccountFlag(input: $accountFlag)
			@rest(
				method: "POST"
				type: "C_AccountFlag"
				path: "/c/accountflags"
			) {
			accountKey
			name
			finished
		}
	}
`;

export const getBannedEmailDomains = gql`
	query getBannedEmailDomains(
		$aggregation: [String]
		$filter: String
		$page: Int = 1
		$pageSize: Int = 20
		$search: String
		$sort: String
	) {
		c {
			bannedEmailDomains(
				aggregation: $aggregation
				filter: $filter
				page: $page
				pageSize: $pageSize
				search: $search
				sort: $sort
			) {
				items {
					bannedEmailDomainId
					domain
				}
			}
		}
	}
`;

export const addDXPCloudEnvironment = gql`
	mutation addDXPCloudEnvironment(
		$DXPCloudEnvironment: InputC_DXPCloudEnvironment!
	) {
		createDXPCloudEnvironment(input: $DXPCloudEnvironment)
			@rest(
				method: "POST"
				type: "C_DXPCloudEnvironment"
				path: "/c/dxpcloudenvironments/"
			) {
			accountKey
			dataCenterRegion
			disasterDataCenterRegion
			id
			projectId
		}
	}
`;

export const addAdminDXPCloud = gql`
	mutation addAdminDXPCloud($AdminDXPCloud: InputC_AdminDXPCloud!) {
		createAdminDXPCloud(input: $AdminDXPCloud)
			@rest(
				method: "POST"
				type: "C_AdminDXPCloud"
				path: "/c/admindxpclouds/"
			) {
			emailAddress
			firstName
			githubUsername
			lastName
			dxpCloudEnvironmentId
		}
	}
`;

export const updateDXPCloudEnvironment = gql`
	mutation updateDXPCloudProjectId(
		$dxpCloudEnvironmentId: Long!
		$DXPCloudEnvironment: InputC_DXPCloudEnvironment!
	) {
		updateDXPCloudEnvironment(
			dxpCloudEnvironmentId: $dxpCloudEnvironmentId
			input: $DXPCloudEnvironment
		)
			@rest(
				method: "PUT"
				type: "C_DXPCloudEnvironment"
				path: "/c/dxpcloudenvironments/{args.dxpCloudEnvironmentId}"
			) {
			dxpCloudEnvironmentId
		}
	}
`;

export const getDXPCloudEnvironment = gql`
	query getDXPCloudEnvironment($filter: String) {
		c {
			dXPCloudEnvironments(filter: $filter) {
				items {
					dxpCloudEnvironmentId
					projectId
				}
			}
		}
	}
`;

export const addAnalyticsCloudWorkspace = gql`
	mutation addAnalyticsCloudWorkspace(
		$analyticsCloudWorkspace: InputC_AnalyticsCloudWorkspace!
	) {
		createAnalyticsCloudWorkspace(input: $analyticsCloudWorkspace)
			@rest(
				method: "POST"
				type: "C_AnalyticsCloudWorkspace"
				path: "/c/analyticscloudworkspaces/"
			) {
			accountKey
			dataCenterLocation
			id
			ownerEmailAddress
			workspaceName
		}
	}
`;

export const addIncidentReportAnalyticsCloud = gql`
	mutation addIncidentReportAnalyticsCloud(
		$IncidentReportContactAnalyticsCloud: InputC_IncidentReportContactAnalyticsCloud!
	) {
		createIncidentReportContactAnalyticsCloud(
			input: $IncidentReportContactAnalyticsCloud
		)
			@rest(
				method: "POST"
				type: "C_IncidentReportContactAnalyticsCloud"
				path: "/c/incidentreportcontactanalyticsclouds/"
			) {
			emailAddress
			analyticsCloudWorkspaceId
		}
	}
`;

export const getAnalyticsCloudWorkspace = gql`
	query getAnalyticsCloudWorkspace($filter: String) {
		c {
			analyticsCloudWorkspaces(filter: $filter) {
				items {
					analyticsCloudWorkspaceId
					workspaceGroupId
				}
			}
		}
	}
`;

export const getLiferayExperienceCloudEnvironments = gql`
	query getLiferayExperienceCloudEnvironments($filter: String) {
		c {
			liferayExperienceCloudEnvironments(filter: $filter) {
				items {
					liferayExperienceCloudEnvironmentId
					projectId
				}
			}
		}
	}
`;

export const getAnalyticsCloudPageInfo = gql`
	query getAnalyticsCloudPageInfo($accountSubscriptionsFilter: String) {
		c {
			accountSubscriptions(filter: $accountSubscriptionsFilter) {
				items {
					accountKey
					externalReferenceCode
					hasDisasterDataCenterRegion
					name
				}
			}
			analyticsCloudDataCenterLocations {
				items {
					analyticsCloudDataCenterLocationId
					name
					value
				}
			}
		}
	}
`;

export const addTeamMembersInvitation = gql`
	mutation addTeamMembersInvitation(
		$TeamMembersInvitation: [InputC_TeamMembersInvitation]!
	) {
		createTeamMembersInvitation(input: $TeamMembersInvitation)
			@rest(
				method: "POST"
				type: "C_TeamMembersInvitation"
				path: "/c/teammembersinvitations/batch"
			) {
			email
			role
		}
	}
`;

export const associateUserAccountWithAccountAndAccountRole = gql`
	mutation associateUserAccountWithAccountAndAccountRole(
		$emailAddress: String!
		$accountKey: String!
		$accountRoleId: Long!
	) {
		createAccountUserAccountByExternalReferenceCodeByEmailAddress(
			emailAddress: $emailAddress
			externalReferenceCode: $accountKey
		)
		createAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddress(
			accountRoleId: $accountRoleId
			emailAddress: $emailAddress
			externalReferenceCode: $accountKey
		)
	}
`;

export const getAccountFlags = gql`
	query getAccountFlags($filter: String) {
		c {
			accountFlags(filter: $filter) {
				items {
					accountKey
					name
					finished
				}
			}
		}
	}
`;

export const getAccountRoles = gql`
	query getAccountRoles($accountId: Long!) {
		accountAccountRoles(accountId: $accountId) {
			items {
				id
				name
			}
		}
	}
`;

export const getAccountSubscriptionGroups = gql`
	query getAccountSubscriptionGroups(
		$aggregation: [String]
		$filter: String
		$page: Int = 1
		$pageSize: Int = 20
		$search: String
		$sort: String
	) {
		c {
			accountSubscriptionGroups(
				aggregation: $aggregation
				filter: $filter
				page: $page
				pageSize: $pageSize
				search: $search
				sort: $sort
			) {
				items {
					accountSubscriptionGroupId
					accountKey
					activationStatus
					externalReferenceCode
					hasActivation
					name
					tabOrder
					menuOrder
				}
			}
		}
	}
`;

export const getKoroneikiAccounts = gql`
	query getKoroneikiAccounts(
		$filter: String
		$pageSize: Int = 20
		$page: Int = 1
	) {
		c {
			koroneikiAccounts(
				filter: $filter
				pageSize: $pageSize
				page: $page
			) {
				items {
					accountKey
					acWorkspaceGroupId
					code
					dxpVersion
					externalReferenceCode
					liferayContactEmailAddress
					liferayContactName
					liferayContactRole
					maxRequestors
					partner
					region
					name
					slaCurrent
					slaCurrentEndDate
					slaCurrentStartDate
					slaExpired
					slaExpiredEndDate
					slaExpiredStartDate
					slaFuture
					slaFutureEndDate
					slaFutureStartDate
				}
				totalCount
			}
		}
	}
`;

export const getListTypeDefinitions = gql`
	query getListTypeDefinitions($filter: String) {
		listTypeDefinitions(filter: $filter) {
			items {
				listTypeEntries {
					key
					name
				}
			}
		}
	}
`;

export const getAccounts = gql`
	query getAccounts($pageSize: Int = 20) {
		accounts(pageSize: $pageSize) {
			items {
				externalReferenceCode
				name
			}
		}
	}
`;

export const getAccountByExternalReferenceCode = gql`
	query getAccountByExternalReferenceCode($externalReferenceCode: String!) {
		accountByExternalReferenceCode(
			externalReferenceCode: $externalReferenceCode
		) {
			id
			name
		}
	}
`;

export const getAccountUserAccountsByExternalReferenceCode = gql`
	query getAccountUserAccountsByExternalReferenceCode(
		$externalReferenceCode: String!
		$pageSize: Int = 20
		$filter: String
	) {
		accountUserAccountsByExternalReferenceCode(
			externalReferenceCode: $externalReferenceCode
			pageSize: $pageSize
			filter: $filter
		) {
			items {
				dateCreated
				id
				emailAddress
				lastLoginDate
				name
				accountBriefs {
					name
					externalReferenceCode
					roleBriefs {
						id
						name
					}
				}
			}
		}
	}
`;

export const getUserAccount = gql`
	query getUserAccount($id: Long!) {
		userAccount(userAccountId: $id) {
			accountBriefs {
				externalReferenceCode
				id
				name
				roleBriefs {
					id
					name
				}
			}
			externalReferenceCode
			id
			image
			name
			roleBriefs {
				id
				name
			}
			organizationBriefs {
				id
				name
			}
		}
	}
`;

export const updateAccountSubscriptionGroups = gql`
	mutation putAccountSubscriptionGroups(
		$id: Long!
		$accountSubscriptionGroup: InputC_AccountSubscriptionGroup!
	) {
		updateAccountSubscriptionGroup(
			accountSubscriptionGroupId: $id
			input: $accountSubscriptionGroup
		)
			@rest(
				method: "PUT"
				type: "C_AccountSubscriptionGroup"
				path: "/c/accountsubscriptiongroups/{args.accountSubscriptionGroupId}"
			) {
			accountSubscriptionGroupId
			accountKey
			activationStatus
			externalReferenceCode
			name
		}
	}
`;

export const deleteAccountUserRoles = gql`
	mutation deleteAccountUserRoles(
		$accountRoleId: Long!
		$emailAddress: String!
		$accountKey: String!
	) {
		deleteAccountByExternalReferenceCodeAccountRoleUserAccountByEmailAddress(
			accountRoleId: $accountRoleId
			emailAddress: $emailAddress
			externalReferenceCode: $accountKey
		)
	}
`;

export const deleteAccountUserAccount = gql`
	mutation deleteAccountUserAccountByExternalReferenceCodeByEmailAddress(
		$emailAddress: String!
		$accountKey: String!
	) {
		deleteAccountUserAccountByExternalReferenceCodeByEmailAddress(
			emailAddress: $emailAddress
			externalReferenceCode: $accountKey
		)
	}
`;
