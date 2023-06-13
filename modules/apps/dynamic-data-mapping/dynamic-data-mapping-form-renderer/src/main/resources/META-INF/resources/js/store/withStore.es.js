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

import dom from 'metal-dom';

import {evaluate} from '../util/evaluation.es';
import {PagesVisitor} from '../util/visitors.es';
import handleActivePageUpdated from './actions/handleActivePageUpdated.es';
import handleFieldBlurred from './actions/handleFieldBlurred.es';
import handleFieldEdited from './actions/handleFieldEdited.es';
import handleFieldFocused from './actions/handleFieldFocused.es';
import handleFieldRemoved from './actions/handleFieldRemoved.es';
import handleFieldRepeated from './actions/handleFieldRepeated.es';
import handleFormSubmitted from './actions/handleFormSubmitted.es';
import handlePaginationItemClicked from './actions/handlePaginationItemClicked.es';
import handlePaginationNextClicked from './actions/handlePaginationNextClicked.es';
import handlePaginationPreviousClicked from './actions/handlePaginationPreviousClicked.es';

const _handleFieldEdited = function(properties) {
	const {fieldInstance} = properties;
	const {evaluable} = fieldInstance;
	const evaluatorContext = this.getEvaluatorContext();

	handleFieldEdited(evaluatorContext, properties)
		.then(evaluatedPages => {
			if (fieldInstance.isDisposed()) {
				return;
			}

			this.setState(
				{
					pages: evaluatedPages
				},
				() => {
					if (evaluable) {
						this.emit('evaluated', evaluatedPages);
					}
				}
			);
		})
		.catch(error => this.emit('evaluationError', error));
};

const _handleFieldBlurred = function(properties) {
	const {fieldInstance} = properties;
	const {pages} = this;

	handleFieldBlurred(pages, properties).then(blurredFieldPages => {
		if (fieldInstance.isDisposed()) {
			return;
		}

		this.setState({
			pages: blurredFieldPages
		});
	});
};

const _handleFieldFocused = function(properties) {
	const {pages} = this;

	handleFieldFocused(pages, properties).then(focusedFieldPages => {
		this.setState({
			pages: focusedFieldPages
		});
	});
};

export default Component => {
	return class withStore extends Component {
		attached() {
			super.attached();

			this.on(
				'activePageUpdated',
				this._handleActivePageUpdated.bind(this)
			);
			this.on('fieldBlurred', _handleFieldBlurred.bind(this));
			this.on('fieldEdited', _handleFieldEdited.bind(this));
			this.on('fieldFocused', _handleFieldFocused.bind(this));
			this.on('fieldRemoved', this._handleFieldRemoved.bind(this));
			this.on('fieldRepeated', this._handleFieldRepeated.bind(this));
			this.on(
				'paginationItemClicked',
				this._handlePaginationItemClicked.bind(this)
			);
			this.on(
				'paginationNextClicked',
				this._handlePaginationNextClicked.bind(this)
			);
			this.on(
				'paginationPreviousClicked',
				this._handlePaginationPreviousClicked.bind(this)
			);
			this.on(
				'pageValidationFailed',
				this._handlePageValidationFailed.bind(this)
			);

			const form = this.getFormNode();

			if (form) {
				dom.on(form, 'submit', this._handleFormSubmitted.bind(this));
			}

			Liferay.on('submitForm', this._handleLiferayFormSubmitted, this);
		}

		dispatch(event, payload) {
			this.emit(event, payload);
		}

		evaluate() {
			return evaluate(null, this.getEvaluatorContext());
		}

		getChildContext() {
			return {
				dispatch: this.dispatch.bind(this),
				store: this
			};
		}

		getEvaluatorContext() {
			const {
				defaultLanguageId,
				editingLanguageId,
				pages,
				portletNamespace,
				rules
			} = this;

			return {
				defaultLanguageId,
				editingLanguageId,
				pages,
				portletNamespace,
				rules
			};
		}

		getFormNode() {
			return dom.closest(this.element, 'form');
		}

		toJSON() {
			const {
				description,
				name,
				paginationMode,
				successPageSettings
			} = this;

			return {
				...this.getEvaluatorContext(),
				description,
				name,
				paginationMode,
				successPageSettings
			};
		}

		_handleActivePageUpdated(event) {
			this.setState(handleActivePageUpdated(event));
		}

		_handleFieldRemoved(name) {
			this.setState({
				pages: handleFieldRemoved(this.pages, name)
			});
		}

		_handleFieldRepeated(name) {
			this.setState({
				pages: handleFieldRepeated(this.pages, name)
			});
		}

		_handleFormSubmitted(event) {
			event.preventDefault();

			handleFormSubmitted(this.getEvaluatorContext()).then(validForm => {
				if (validForm) {
					Liferay.Util.submitForm(event.target);
				} else {
					this.dispatch('pageValidationFailed', this.activePage);
				}
			});
		}

		_handleLiferayFormSubmitted(event) {
			if (event.form && event.form.getDOM() === this.getFormNode()) {
				event.preventDefault();
			}
		}

		_handlePageValidationFailed(pageIndex) {
			const {pages} = this;
			const visitor = new PagesVisitor(pages);

			this.setState({
				pages: visitor.mapFields(
					(
						field,
						fieldIndex,
						columnIndex,
						rowIndex,
						currentPageIndex
					) => {
						return {
							...field,
							displayErrors: currentPageIndex === pageIndex
						};
					}
				)
			});
		}

		_handlePaginationItemClicked({pageIndex}) {
			handlePaginationItemClicked({pageIndex}, this.dispatch.bind(this));
		}

		_handlePaginationNextClicked() {
			const {activePage} = this;

			handlePaginationNextClicked(
				{
					activePage,
					...this.getEvaluatorContext()
				},
				this.dispatch.bind(this)
			);
		}

		_handlePaginationPreviousClicked() {
			const {activePage} = this;

			handlePaginationPreviousClicked(
				{
					activePage,
					...this.getEvaluatorContext()
				},
				this.dispatch.bind(this)
			);
		}
	};
};
