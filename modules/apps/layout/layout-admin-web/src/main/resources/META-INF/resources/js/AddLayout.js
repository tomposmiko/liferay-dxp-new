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

import {fetch, getOpener, openToast} from 'frontend-js-web';

export default function ({namespace}) {
	const addButton = document.getElementById(`${namespace}addButton`);

	const form = document.getElementById(`${namespace}fm`);

	form.addEventListener('submit', (event) => {
		event.preventDefault();
		event.stopPropagation();

		if (addButton.disabled) {
			return;
		}

		addButton.disabled = true;

		const formData = new FormData(form);

		fetch(form.action, {
			body: formData,
			method: 'POST',
		})
			.then((response) => {
				return response.json();
			})
			.then((response) => {
				if (response.redirectURL) {
					const redirectURL = new URL(
						response.redirectURL,
						window.location.origin
					);

					redirectURL.searchParams.set('p_p_state', 'normal');

					const opener = getOpener();

					opener.Liferay.fire('closeModal', {
						id: 'addLayoutDialog',
						redirect: redirectURL.toString(),
					});
				}
				else {
					addButton.disabled = false;

					if (form.querySelector('.alert')) {
						return;
					}

					const alertWrapper = document.createElement('div');

					form.prepend(alertWrapper);

					openToast({
						autoClose: false,
						container: alertWrapper,
						message: response.errorMessage,
						type: 'danger',
					});
				}
			});
	});
}
