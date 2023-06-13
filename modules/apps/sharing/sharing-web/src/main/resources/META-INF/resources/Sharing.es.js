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

import 'clay-multi-select';

import 'clay-sticker';
import {PortletBase} from 'frontend-js-web';
import dom from 'metal-dom';
import Soy from 'metal-soy';
import {Config} from 'metal-state';

import templates from './Sharing.soy';

class Sharing extends PortletBase {
	constructor(config, ...args) {
		super(config, ...args);

		this._classNameId = config.classNameId;
		this._classPK = config.classPK;
		this._dialogId = config.dialogId;
		this._refererPortletNamespace = config.refererPortletNamespace;
		this._userEmailAddresses = [];
	}

	attached() {
		const portalElement = dom.toElement('#clay_dropdown_portal');

		if (portalElement) {
			dom.addClasses(portalElement, 'show');
		}
	}

	/**
	 * Fetches autocomplete results
	 * @private
	 * @review
	 */
	_dataSource(query) {
		return this.fetch(this.sharingUserAutocompleteURL, {
			query
		})
			.then(res => res.json())
			.then(users =>
				users.map(({emailAddress, fullName, portraitURL, userId}) => ({
					emailAddress,
					fullName,
					label: fullName,
					portraitURL,
					spritemap: this.spritemap,
					userId,
					value: emailAddress
				}))
			);
	}

	/**
	 * Disables filtering of results
	 * @private
	 * @review
	 */
	_handleDataChange(e) {
		e.preventDefault();

		if (e.data && e.target.refs.autocomplete._query) {
			e.target.filteredItems = e.data.map((element, index) => ({
				data: element,
				index,
				matches: [],
				score: 0,
				value: element
			}));
		} else {
			e.target.filteredItems = [];
		}

		e.target.refs.autocomplete._isFetching = false;
		e.target.refs.autocomplete.refs.dataProvider.isLoading = false;
	}

	/**
	 * Close the SharingDialog
	 * @private
	 * @review
	 */
	_closeDialog() {
		const sharingDialog = Liferay.Util.getWindow(this._dialogId);

		if (sharingDialog && sharingDialog.hide) {
			sharingDialog.hide();
		}
	}

	/**
	 * Sync the inputs with the state
	 * @param {!Event} event
	 * @private
	 * @review
	 */
	_handleInputChange(event) {
		const target = event.target;

		const name = target.name;
		const value =
			target.type === 'checkbox' ? target.checked : target.value;

		this[name] = value;
	}

	/**
	 * Validates if the email addresses introduced is valid
	 * and exists as a user.
	 *
	 * @param {!Event} event
	 * @private
	 * @review
	 */
	_handleEmailAddressAdded(event) {
		const {item, selectedItems} = event.data;

		this._userEmailAddresses = selectedItems;

		this.emailAddressErrorMessage = '';
		this._inputValue = '';

		const itemAdded = item.value;

		if (!this._isEmailAddressValid(itemAdded)) {
			this.emailAddressErrorMessage = Liferay.Language.get(
				'please-enter-a-valid-email-address'
			);
			this._inputValue = itemAdded;
			this._userEmailAddresses.pop();
		} else {
			this.fetch(this.sharingVerifyEmailAddressURL, {
				emailAddress: itemAdded
			})
				.then(response => response.json())
				.then(result => {
					const {userExists} = result;

					if (!userExists) {
						this.emailAddressErrorMessage = Liferay.Util.sub(
							Liferay.Language.get('user-x-does-not-exist'),
							itemAdded
						);

						this._userEmailAddresses = this._userEmailAddresses.filter(
							item => item.value != itemAdded
						);

						setTimeout(() => {
							this._inputValue = itemAdded;
						}, 0);
					}
				});
		}
	}

	/**
	 * When input has been cleared removes the errors.
	 *
	 * @param  {!Event} event
	 * @private
	 * @review
	 */
	_handleEmailInputChange(event) {
		if (!event.data.value) {
			this._inputValue = '';
			this.emailAddressErrorMessage = '';
		}
	}

	/**
	 * Checks wether the input has emails or not.
	 *
	 * @param {!Event} event
	 * @private
	 * @review
	 */
	_handleEmailRemoved(event) {
		this._userEmailAddresses = event.data.selectedItems;
		this._validateRequiredEmailAddress();
	}

	/**
	 * Save the share permisions
	 * @param {!Event} event
	 * @private
	 * @review
	 */
	_handleSubmit(event) {
		event.preventDefault();

		if (!this.submitting && this._validateRequiredEmailAddress()) {
			this.submitting = true;

			this.fetch(this.shareActionURL, {
				classNameId: this._classNameId,
				classPK: this._classPK,
				shareable: this.shareable,
				sharingEntryPermissionDisplayActionId: this
					.sharingEntryPermissionDisplayActionId,
				userEmailAddress: this._userEmailAddresses
					.map(({value}) => value)
					.join(',')
			})
				.then(response => {
					this.submitting = false;

					const jsonResponse = response.json();

					return response.ok
						? jsonResponse
						: jsonResponse.then(json => {
								const error = new Error(
									json.errorMessage || response.statusText
								);
								throw Object.assign(error, {response});
						  });
				})
				.then(json => {
					parent.Liferay.Portlet.refresh(
						`#p_p_id${this._refererPortletNamespace}`
					);

					this._showNotification(json.successMessage);
				})
				.catch(error => {
					this.submitting = false;

					this._showNotification(error.message, true);
				});
		}
	}

	/**
	 * Show notification in the opener and closes dialog
	 * after is rendered
	 * @param {string} message message for notification
	 * @param {boolean} error Flag indicating if is an error or not
	 * @private
	 * @review
	 */
	_showNotification(message, error) {
		const parentOpenToast = Liferay.Util.getOpener().Liferay.Util.openToast;

		const openToastParams = {
			events: {
				attached: this._closeDialog.bind(this)
			},
			message
		};

		if (error) {
			openToastParams.title = Liferay.Language.get('error');
			openToastParams.type = 'danger';
		}

		parentOpenToast(openToastParams);
	}

	/**
	 * Check if a passed email has a valid format
	 * @private
	 * @review
	 * @return {Boolean} is valid or not
	 */
	_isEmailAddressValid(email) {
		const emailRegex = /.+@.+\..+/i;

		return emailRegex.test(email);
	}

	/**
	 * Check if the userEmailAddresses is filled and show error message
	 * @private
	 * @review
	 * @return {Boolean} is valid or not
	 */
	_validateRequiredEmailAddress() {
		const valid = !!this._userEmailAddresses.length;

		this.emailAddressErrorMessage = valid
			? ''
			: Liferay.Language.get('this-field-is-required');

		return valid;
	}
}

/**
 * State definition.
 * @review
 * @static
 * @type {!Object}
 */
Sharing.STATE = {
	_inputValue: Config.string().internal(),
	emailAddressErrorMessage: Config.string().value(''),
	shareActionURL: Config.string().required(),
	shareable: Config.bool().value(true),
	sharingEntryPermissionDisplayActionId: Config.string().required(),
	sharingUserAutocompleteURL: Config.string().required(),
	sharingVerifyEmailAddressURL: Config.string().required(),
	spritemap: Config.string().required(),
	submitting: Config.bool().value(false)
};

Soy.register(Sharing, templates);

export default Sharing;
