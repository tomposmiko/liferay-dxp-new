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

import PropTypes from 'prop-types';

function CountryRegionDynamicSelect({
	countrySelect,
	countrySelectId,
	countrySelectVal,
	regionSelect,
	regionSelectId,
	regionSelectVal,
}) {
	let japanCountryId;

	new Liferay.DynamicSelect([
		{
			select: countrySelect,
			selectData(callback) {
				Liferay.Service(
					'/country/get-countries',
					{
						active: true,
						companyId: Liferay.ThemeDisplay.getCompanyId,
					},
					(countries) => {
						const countryJP = countries.find(
							(country) => country.a2 === 'JP'
						);

						japanCountryId = countryJP.countryId;

						callback(countries);
					}
				);
			},
			selectDesc: 'nameCurrentValue',
			selectId: countrySelectId,
			selectSort: true,
			selectVal: countrySelectVal,
		},
		{
			select: regionSelect,
			selectData(callback, selectKey) {
				Liferay.Service(
					'/region/get-regions',
					{
						active: true,
						countryId: Number(selectKey),
					},
					(regions) => {
						if (
							selectKey === japanCountryId &&
							Liferay.ThemeDisplay.getLanguageId() === 'ja_JP'
						) {
							regions.sort((region1, region2) => {
								if (
									Number(region1.regionCode) >
									Number(region2.regionCode)
								) {
									return 1;
								}

								if (
									Number(region1.regionCode) <
									Number(region2.regionCode)
								) {
									return -1;
								}

								return 0;
							});
						}

						callback(regions);
					}
				);
			},
			selectDesc: 'title',
			selectDisableOnEmpty: true,
			selectId: regionSelectId,
			selectVal: regionSelectVal,
		},
	]);
}

CountryRegionDynamicSelect.prototypes = {
	countrySelect: PropTypes.string.isRequired,
	countrySelectId: PropTypes.string.isRequired,
	countrySelectVal: PropTypes.string,
	regionSelect: PropTypes.string.isRequired,
	regionSelectId: PropTypes.string.isRequired,
	regionSelectVal: PropTypes.string,
};

export default CountryRegionDynamicSelect;
