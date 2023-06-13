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
	'liferay-search-bar',
	A => {
		var SearchBar = function(form) {
			var instance = this;

			instance.form = form;

			instance.form.on('submit', A.bind(instance._onSubmit, instance));

			var emptySearchInput = instance.form.one(
				'.search-bar-empty-search-input'
			);

			if (emptySearchInput.val() === 'true') {
				instance.emptySearchEnabled = true;
			} else {
				instance.emptySearchEnabled = false;
			}

			instance.keywordsInput = instance.form.one(
				'.search-bar-keywords-input'
			);

			instance.resetStartPage = instance.form.one(
				'.search-bar-reset-start-page'
			);

			instance.scopeSelect = instance.form.one(
				'.search-bar-scope-select'
			);

			var searchButton = instance.form.one('.search-bar-search-button');

			searchButton.on('click', A.bind(instance._onClick, instance));
		};

		A.mix(SearchBar.prototype, {
			_onClick() {
				var instance = this;

				instance.search();
			},

			_onSubmit(event) {
				var instance = this;

				event.stopPropagation();

				instance.search();
			},

			getKeywords() {
				var instance = this;

				var keywords = instance.keywordsInput.val();

				return keywords.replace(/^\s+|\s+$/, '');
			},

			isSubmitEnabled() {
				var instance = this;

				return (
					instance.getKeywords() !== '' || instance.emptySearchEnabled
				);
			},

			search() {
				var instance = this;

				if (instance.isSubmitEnabled()) {
					var searchURL = instance.form.get('action');

					var queryString = instance.updateQueryString(
						document.location.search
					);

					document.location.href = searchURL + queryString;
				}
			},

			updateQueryString(queryString) {
				var instance = this;

				var searchParams = new URLSearchParams(queryString);

				searchParams.set(
					instance.keywordsInput.get('name'),
					instance.getKeywords()
				);
				searchParams.delete('p_p_id');
				searchParams.delete('p_p_state');

				if (instance.scopeSelect) {
					searchParams.set(
						instance.scopeSelect.get('name'),
						instance.scopeSelect.val()
					);
				}

				searchParams.delete('start');

				if (instance.resetStartPage) {
					var resetStartPageName = instance.resetStartPage.get(
						'name'
					);

					searchParams.delete(resetStartPageName);
				}

				return '?' + searchParams.toString();
			}
		});

		Liferay.namespace('Search').SearchBar = SearchBar;
	},
	''
);
