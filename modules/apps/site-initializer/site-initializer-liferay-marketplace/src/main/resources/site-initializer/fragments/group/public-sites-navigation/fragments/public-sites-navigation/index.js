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

/* eslint-disable no-undef */

adtSpatialNavigationProvider = new navigation.default.SpatialNavigationProvider(
	'.adt-submenu-item-link'
);

spatialNavigationProvider = new navigation.default.SpatialNavigationProvider(
	'.adt-nav-text'
);

const primaryNav = fragmentElement.querySelector('.primary-nav');

spatialNavigationProvider.addFocusableClasses(primaryNav);

window.addEventListener('load', () => {
	new navigation.default.DropdownProvider(
		'.account-info',
		'.account-info',
		'menu-open'
	);

	new navigation.default.DropdownProvider(
		'.account-info',
		'.account-dropdown',
		'show',
		true
	);

	new navigation.default.DropdownProvider(
		'.sites',
		'.liferay-sites-dropdown',
		'show',
		true
	);

	new navigation.default.DropdownProvider('.sites', '.sites', 'show', true);

	new navigation.default.DropdownProvider(
		'.menu-button-group',
		'.menu-button-group',
		'menu-open'
	);

	new navigation.default.DropdownProvider(
		'.menu-button-group',
		'.tablet-mobile-nav-section',
		'menu-open',
		true
	);

	new navigation.default.DropdownProvider(
		'.adt-nav-text',
		'.adt-submenu',
		'dropdown-open',
		false,
		(menu) => {
			adtSpatialNavigationProvider.addFocusableClasses(menu);
		},
		(menu) => {
			adtSpatialNavigationProvider.removeFocusableClasses(menu);
		}
	);

	new navigation.default.DropdownProvider(
		'.language',
		'.language-selector',
		'list-open',
		true
	);

	new navigation.default.DropdownProvider(
		'.language',
		'.language-dropdown-list-container',
		'list-open',
		true
	);

	new navigation.default.DropdownProvider(
		'.search-icon, .close-search',
		'.search-wrapper',
		'search-open',
		true
	);
});

const searchSuggestionsInput = fragmentElement.querySelector(
	'.search-suggestions-input'
);
const suggestions = fragmentElement.querySelector('.suggestions');
const searchSuggestions = fragmentElement.querySelector('.search-suggestions');

const searchSuggestionItemTemplate = suggestions.querySelector('template');

const seeAllResultsLink = fragmentElement.querySelector(
	'.search-suggestions-see-all-results-text'
);

const searchSuggestionItem = searchSuggestionItemTemplate.content.querySelector(
	'a'
);

function updateSearch() {
	searchSuggestions.innerHTML = '';

	const searchSuggestionsInputValue = searchSuggestionsInput.value;

	if (searchSuggestionsInputValue) {
		seeAllResultsLink.href = '/search?q=' + searchSuggestionsInputValue;
		suggestions.classList.add('performing-search');
		performSearch(searchSuggestionsInputValue);
	}
	else {
		suggestions.classList.remove(
			'loading-search',
			'performing-search',
			'search-error',
			'search-results-found'
		);
	}
}

let debounceTimer;

const debounce = (callback, time) => {
	window.clearTimeout(debounceTimer);
	debounceTimer = window.setTimeout(callback, time);
};

searchSuggestionsInput.addEventListener(
	'input',
	() => {
		suggestions.classList.add('loading-search');
		debounce(updateSearch, 250);
	},
	false
);

function performSearch(query) {
	const postDataURL = `/o/portal-search-rest/v1.0/suggestions?currentURL=${
		window.location.href
	}&destinationFriendlyURL=/search&groupId=${Liferay.ThemeDisplay.getScopeGroupId()}&plid=${Liferay.ThemeDisplay.getPlid()}&scope=this-site&search=${query}`;

	postData(postDataURL, [
		{
			attributes: {
				includeAssetSearchSummary: true,
				includeassetURL: true,
				sxpBlueprintId: configuration.searchBlueprintId,
			},
			contributorName: 'sxpBlueprint',
			displayGroupName: 'Public Nav Search Recommendations',
			size: '3',
		},
	])
		.then((data) => {
			if (data && data.items && data.items[0]) {
				const items = JSON.parse(JSON.stringify(data.items[0]));
				if (items) {
					searchSuggestions.innerHTML = '';

					const searchTermRegExp = new RegExp(
						'(' + query + ')',
						'gi'
					);

					for (const suggestion of items.suggestions) {
						const suggestionLink = document.importNode(
							searchSuggestionItem,
							true
						);

						const assetURL = suggestion.attributes.assetURL.replace(
							/\?.*$/,
							''
						);

						suggestionLink.href = assetURL;

						const suggestionTitle = suggestionLink.querySelector(
							'.search-suggestion-item-title'
						);

						suggestionTitle.appendChild(
							document.createTextNode(suggestion.text)
						);

						const suggestionContent = suggestionLink.querySelector(
							'.search-suggestion-item-content'
						);

						let suggestionContentTextValue =
							suggestion.attributes.assetSearchSummary;

						if (suggestionContentTextValue) {
							suggestionContentTextValue = suggestionContentTextValue.substring(
								0,
								500
							);

							suggestionContent.innerHTML = suggestionContentTextValue.replace(
								searchTermRegExp,
								`<b>$1</b>`
							);
						}

						const suggestionURL = suggestionLink.querySelector(
							'.search-suggestion-item-link'
						);

						suggestionURL.appendChild(
							document.createTextNode(
								getBreadcrumbFromURL(assetURL)
							)
						);

						searchSuggestions.appendChild(suggestionLink);

						suggestions.classList.add('search-results-found');
						suggestions.classList.remove('loading-search');
					}
				}
			}
			else {
				suggestions.classList.remove('search-results-found');
				suggestions.classList.remove('loading-search');
			}
			suggestions.classList.remove('search-error');
		})
		.catch(() => {
			suggestions.classList.remove('loading-search');
			suggestions.classList.add('search-error');
		});
}

async function postData(url = '', data = {}) {
	const response = await Liferay.Util.fetch(url, {
		body: JSON.stringify(data),
		credentials: 'include',
		headers: {
			'Accept': 'application/json',
			'Accept-Language': Liferay.ThemeDisplay.getBCP47LanguageId(),
			'Content-Type': 'application/json',
			'x-csrf-token': Liferay.authToken,
		},
		method: 'POST',
	});

	return response.json();
}

function getBreadcrumbFromURL(url) {
	if (!url) {
		return '';
	}

	url = url
		.replaceAll('/web/guest/w/', 'home/')
		.replaceAll('/web/guest/', 'home/')
		.replaceAll('/', ' / ')
		.replaceAll('-', ' ');

	const ancronymList = ['api', 'ccr', 'dxp', 'mvc', ' ui ', 'url'];

	ancronymList.forEach((word) => {
		if (url.includes(word)) {
			const regEx = new RegExp(word, 'ig');
			url = url.replace(regEx, word.toUpperCase());
		}
	});

	let breadcrumbs = [];
	const excludeWords = ['a', 'and', 'of', 'the', 'to', 'via'];
	breadcrumbs = url.split(' ');

	return breadcrumbs
		.map((word, i) => {
			return excludeWords.includes(word) && i !== 0
				? [word]
				: word.charAt(0).toUpperCase() + word.slice(1);
		})
		.join(' ');
}
