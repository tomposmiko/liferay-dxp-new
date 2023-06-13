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

import {fetch, navigate, openToast, sub} from 'frontend-js-web';
import PropTypes from 'prop-types';
import React, {useContext, useEffect, useRef, useState} from 'react';

import {AppContext} from './AppContext';
import {CodeMirrorEditor} from './CodeMirrorEditor';

export function Editor({autocompleteData, initialScript, mode}) {
	const {inputChannel, portletNamespace} = useContext(AppContext);

	const [script, setScript] = useState(initialScript);

	const scriptRef = useRef(script);
	scriptRef.current = script;

	useEffect(() => {
		const refreshHandler = Liferay.on(
			`${portletNamespace}refreshEditor`,
			() => {
				const formElement = document.getElementById(
					`${portletNamespace}fm`
				);

				if (!formElement) {
					return;
				}

				if (scriptRef.current === initialScript) {
					setScript('');
				}

				Liferay.fire(`${portletNamespace}saveTemplate`);

				requestAnimationFrame(() => {
					formElement.action = window.location.href;
					formElement.submit();
				});
			}
		);

		return () => {
			refreshHandler.detach();
		};
	}, [initialScript, portletNamespace]);

	useEffect(() => {
		const scriptImportedHandler = Liferay.on(
			`${portletNamespace}scriptImported`,
			(event) => {
				setScript(event.script);

				openToast({
					message: sub(
						Liferay.Language.get('x-imported'),
						event.fileName
					),
					title: Liferay.Language.get('success'),
					type: 'success',
				});
			}
		);

		return () => {
			scriptImportedHandler.detach();
		};
	}, [initialScript, portletNamespace]);

	useEffect(() => {
		const saveTemplate = (redirect) => {
			const form = document.getElementById(`${portletNamespace}fm`);

			if (!redirect) {
				const saveAndContinueInput = document.getElementById(
					`${portletNamespace}saveAndContinue`
				);

				saveAndContinueInput.value = true;
			}

			const saveButtons = document.querySelectorAll('save-button');

			const changeDisabled = (disabled) => {
				saveButtons.forEach((button) => {
					button.disabled = disabled;
				});
			};

			const formData = new FormData(form);

			formData.append(
				`${portletNamespace}scriptContent`,
				new File([new Blob([script])], 'scriptContent')
			);

			changeDisabled(true);

			const liferayForm = Liferay.Form.get(form.id);

			if (liferayForm) {
				const validator = liferayForm.formValidator;

				validator.validate();

				if (validator.hasErrors()) {
					validator.focusInvalidField();
				}
			}

			fetch(form.action, {body: formData, method: 'POST'})
				.then((response) => {
					if (response.redirected) {
						navigate(response.url);
					}

					changeDisabled(false);
				})
				.catch(() => {
					changeDisabled(true);
				});
		};

		const saveAndContinueButton = document.querySelector(
			'.save-and-continue-button'
		);

		const saveButton = document.querySelector('.save-button');

		const onSaveAndContinueButtonClick = (event) => {
			event.preventDefault();

			saveTemplate(false);
		};

		const onSaveButtonClick = (event) => {
			event.preventDefault();

			saveTemplate(true);
		};

		saveAndContinueButton.addEventListener(
			'click',
			onSaveAndContinueButtonClick
		);
		saveButton.addEventListener('click', onSaveButtonClick);

		return () => {
			saveAndContinueButton.removeEventListener(
				'click',
				onSaveAndContinueButtonClick
			);
			saveButton.removeEventListener('click', onSaveButtonClick);
		};
	}, [portletNamespace, script]);

	useEffect(() => {
		const exportScriptHandler = Liferay.on(
			`${portletNamespace}exportScript`,
			() => {
				exportScript(scriptRef.current, 'ftl');
			}
		);

		return () => {
			exportScriptHandler.detach();
		};
	}, [initialScript, portletNamespace]);

	return (
		<>
			<CodeMirrorEditor
				autocompleteData={autocompleteData}
				content={script}
				inputChannel={inputChannel}
				mode={mode}
				onChange={setScript}
			/>
		</>
	);
}

Editor.propTypes = {
	autocompleteData: PropTypes.object.isRequired,
	initialScript: PropTypes.string.isRequired,
	mode: PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.shape({
			globalVars: PropTypes.bool.isRequired,
			name: PropTypes.string.isRequired,
		}),
	]),
};

const exportScript = (script) => {
	const link = document.createElement('a');
	const blob = new Blob([script]);

	const fileURL = URL.createObjectURL(blob);

	link.href = fileURL;
	link.download = 'script.ftl';

	link.click();

	URL.revokeObjectURL(fileURL);
};
