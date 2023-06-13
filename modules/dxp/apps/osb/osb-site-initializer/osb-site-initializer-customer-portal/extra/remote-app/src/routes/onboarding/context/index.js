import {createContext, useContext, useEffect, useReducer} from 'react';
import client from '../../../apolloClient';
import FormProvider from '../../../common/providers/FormProvider';
import {LiferayTheme} from '../../../common/services/liferay';
import {
	addAccountFlag,
	getAccountSubscriptionGroups,
	getKoroneikiAccounts,
	getUserAccount,
} from '../../../common/services/liferay/graphql/queries';
import {
	PARAMS_KEYS,
	SearchParams,
} from '../../../common/services/liferay/search-params';
import {ROUTES} from '../../../common/utils/constants';
import {isValidPage} from '../../../common/utils/page.validation';
import {PRODUCTS} from '../../customer-portal/utils/constants';
import {
	getInitialDxpAdmin,
	getInitialInvite,
	roles,
	steps,
} from '../utils/constants';
import reducer, {actionTypes} from './reducer';

const initialForm = {
	dxp: {
		admins: [getInitialDxpAdmin()],
		dataCenterRegion: {},
		disasterDataCenterRegion: {},
		projectId: '',
	},
	invites: [
		getInitialInvite(),
		getInitialInvite(roles.MEMBER.key),
		getInitialInvite(roles.MEMBER.key),
	],
};

const AppContext = createContext();

const AppContextProvider = ({assetsPath, children}) => {
	const [state, dispatch] = useReducer(reducer, {
		assetsPath,
		koroneikiAccount: {},
		project: undefined,
		step: steps.welcome,
		subscriptionGroups: undefined,
		userAccount: undefined,
	});

	useEffect(() => {
		const getUser = async () => {
			const {data} = await client.query({
				query: getUserAccount,
				variables: {
					id: LiferayTheme.getUserId(),
				},
			});

			if (data) {
				dispatch({
					payload: data.userAccount,
					type: actionTypes.UPDATE_USER_ACCOUNT,
				});

				return data.userAccount;
			}
		};

		const getProject = async (externalReferenceCode, accountBrief) => {
			const {data: projects} = await client.query({
				query: getKoroneikiAccounts,
				variables: {
					filter: `accountKey eq '${externalReferenceCode}'`,
				},
			});

			if (projects) {
				dispatch({
					payload: {
						...projects.c.koroneikiAccounts.items[0],
						id: accountBrief.id,
						name: accountBrief.name,
					},
					type: actionTypes.UPDATE_PROJECT,
				});
			}
		};

		const getSubscriptionGroups = async (accountKey) => {
			const {data} = await client.query({
				query: getAccountSubscriptionGroups,
				variables: {
					filter: `(accountKey eq '${accountKey}') and (name eq '${PRODUCTS.dxp_cloud}')`,
				},
			});

			if (data) {
				const items = data.c?.accountSubscriptionGroups?.items;
				dispatch({
					payload: items,
					type: actionTypes.UPDATE_SUBSCRIPTION_GROUPS,
				});
			}
		};

		const fetchData = async () => {
			const user = await getUser();

			const projectExternalReferenceCode = SearchParams.get(
				PARAMS_KEYS.PROJECT_APPLICATION_EXTERNAL_REFERENCE_CODE
			);

			if (!user) {
				return;
			}

			const isValid = await isValidPage(
				user,
				projectExternalReferenceCode,
				ROUTES.ONBOARDING
			);

			if (user && isValid) {
				const accountBrief = user.accountBriefs?.find(
					(accountBrief) =>
						accountBrief.externalReferenceCode ===
						projectExternalReferenceCode
				);

				if (accountBrief) {
					getProject(projectExternalReferenceCode, accountBrief);
					getSubscriptionGroups(projectExternalReferenceCode);

					client.mutate({
						mutation: addAccountFlag,
						variables: {
							accountFlag: {
								accountKey: projectExternalReferenceCode,
								name: ROUTES.ONBOARDING,
								userUuid: user.externalReferenceCode,
								value: 1,
							},
						},
					});
				}
			}
		};

		fetchData();
	}, []);

	return (
		<AppContext.Provider value={[state, dispatch]}>
			<FormProvider initialValues={initialForm}>{children}</FormProvider>
		</AppContext.Provider>
	);
};

const useOnboarding = () => useContext(AppContext);

export {AppContext, AppContextProvider, useOnboarding};
