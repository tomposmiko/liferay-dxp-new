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

import {useEffect, useState} from 'react';

import LiferayAccountBrief from '../interfaces/liferayAccountBrief';
import LiferayPicklist from '../interfaces/liferayPicklist';
import useGetAccountById from '../services/liferay/accounts/useGetAccountById';
import isObjectEmpty from '../utils/isObjectEmpty';

export default function useCompanyOptions(
	handleSelected: (
		partnerCountry: LiferayPicklist,
		company: LiferayAccountBrief,
		currency: LiferayPicklist,
		accountExternalReferenceCode?: string
	) => void,
	companyOptions?: React.OptionHTMLAttributes<HTMLOptionElement>[],
	currencyOptions?: React.OptionHTMLAttributes<HTMLOptionElement>[],
	currentCurrency?: LiferayPicklist,
	countryOptions?: React.OptionHTMLAttributes<HTMLOptionElement>[],
	currentCountry?: LiferayPicklist,
	currentCompany?: LiferayAccountBrief
) {
	const [selectedAccountBrief, setSelectedAccountBrief] = useState<
		LiferayAccountBrief | undefined
	>(currentCompany);

	const {data: account} = useGetAccountById(selectedAccountBrief?.id);

	const currencyPicklist =
		account &&
		currencyOptions &&
		currencyOptions.find((options) => options.value === account.currency);

	const countryPicklist =
		account &&
		countryOptions &&
		countryOptions.find(
			(options) => options.value === account.partnerCountry
		);

	if (!companyOptions && account) {
		companyOptions = [
			{
				label: account.name,
				value: account.id,
			},
		];
	}

	useEffect(() => {
		if (!isObjectEmpty(selectedAccountBrief) && selectedAccountBrief) {
			handleSelected(
				currentCountry
					? currentCountry
					: (countryPicklist && {
							key: countryPicklist.value as string,
							name: countryPicklist.label as string,
					  }) ||
							{},
				selectedAccountBrief,
				currentCurrency && !isObjectEmpty(currentCurrency)
					? currentCurrency
					: (currencyPicklist && {
							key: currencyPicklist.value as string,
							name: currencyPicklist.label as string,
					  }) ||
							{},
				account?.externalReferenceCode
			);
		}
	}, [
		account?.externalReferenceCode,
		countryPicklist,
		currencyPicklist,
		currentCountry,
		currentCurrency,
		handleSelected,
		selectedAccountBrief,
	]);

	const onCompanySelected = (event: React.ChangeEvent<HTMLInputElement>) => {
		const optionSelected = companyOptions?.find(
			(options) => options.value === +event.target.value
		);

		setSelectedAccountBrief({
			id: optionSelected?.value as number,
			name: optionSelected?.label as string,
		});
	};

	return {
		companyOptions,
		onCompanySelected,
	};
}
