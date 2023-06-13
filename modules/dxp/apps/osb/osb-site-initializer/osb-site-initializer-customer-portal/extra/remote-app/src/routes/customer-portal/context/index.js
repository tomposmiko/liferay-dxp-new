import {useQuery} from '@apollo/client';
import {createContext, useEffect, useReducer} from 'react';
import {useCustomEvent} from '../../../common/hooks/useCustomEvent';
import {LiferayTheme} from '../../../common/services/liferay';
import {getUserAccount} from '../../../common/services/liferay/graphql/queries';
import {
	PARAMS_KEYS,
	SearchParams,
} from '../../../common/services/liferay/search-params';
import {CUSTOM_EVENTS} from '../utils/constants';
import reducer, {actionTypes} from './reducer';

const AppContext = createContext();

const AppContextProvider = ({assetsPath, children, page}) => {
	const dispatchEvent = useCustomEvent(CUSTOM_EVENTS.USER_ACCOUNT);
	const [state, dispatch] = useReducer(reducer, {
		assetsPath,
		page,
		project: {},
		subscriptionGroups: [],
		userAccount: undefined,
	});

	const {data} = useQuery(getUserAccount, {
		variables: {id: LiferayTheme.getUserId()},
	});

	const userAccount = data?.userAccount;

	useEffect(() => {
		const projectExternalReferenceCode = SearchParams.get(
			PARAMS_KEYS.PROJECT_APPLICATION_EXTERNAL_REFERENCE_CODE
		);

		dispatch({
			payload: {
				accountKey: projectExternalReferenceCode,
			},
			type: actionTypes.UPDATE_PROJECT,
		});
	}, []);

	useEffect(() => {
		if (userAccount) {
			dispatch({
				payload: userAccount,
				type: actionTypes.UPDATE_USER_ACCOUNT,
			});

			dispatchEvent(userAccount);
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [userAccount]);

	return (
		<AppContext.Provider value={[state, dispatch]}>
			{children}
		</AppContext.Provider>
	);
};

export {AppContext, AppContextProvider};
