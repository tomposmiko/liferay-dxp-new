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

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import {ClayTooltipProvider} from '@clayui/tooltip';
import {fetch, sub} from 'frontend-js-web';
import React, {useEffect, useState} from 'react';

import {SENTENCE_TRANSFORM_PROVIDER_TYPES} from './constants';

/**
 * A button to test the connection for the semantic search settings page.
 * This can be found on: System Settings > Search Experiences > Semantic Search
 */
function TestConfigurationButton({
	assetEntryClassNames,
	availableSentenceTransformProviders,
	cacheTimeout,
	embeddingVectorDimensions,
	enableGPU,
	errors,
	huggingFaceAccessToken,
	languageIds,
	maxCharacterCount,
	model,
	modelTimeout,
	sentenceTransformProvider,
	textTruncationStrategy,
	txtaiHostAddress,
}) {
	const [loading, setLoading] = useState(false);
	const [testResultsMessage, setTestResultsMessage] = useState({}); // {message, type}

	/**
	 * Clear the message if any input values change.
	 */
	useEffect(() => {
		setTestResultsMessage({});
	}, [
		assetEntryClassNames,
		cacheTimeout,
		embeddingVectorDimensions,
		enableGPU,
		huggingFaceAccessToken,
		languageIds,
		maxCharacterCount,
		model,
		modelTimeout,
		sentenceTransformProvider,
		textTruncationStrategy,
		txtaiHostAddress,
	]);

	/**
	 * Used for the `/sentence-transformer/validate-configuration` endpoint
	 * to conditionally send the appropriate data according to the user-selected
	 * sentence transform provider type.
	 * @returns {object}
	 */
	const _getSentenceTransformProviderSettings = () => {
		if (
			sentenceTransformProvider ===
			SENTENCE_TRANSFORM_PROVIDER_TYPES.HUGGING_FACE_INFERENCE_API
		) {
			return {
				enableGPU,
				huggingFaceAccessToken,
				model,
				modelTimeout,
			};
		}

		if (
			sentenceTransformProvider ===
			SENTENCE_TRANSFORM_PROVIDER_TYPES.TXTAI
		) {
			return {
				txtaiHostAddress,
			};
		}

		return {};
	};

	const _handleTestConfigurationButtonClick = () => {
		setLoading(true);

		// Organizing fetch body property groups by how they appear in the UI.

		const generalSettings = {
			cacheTimeout,
			sentenceTransformerEnabled: true, // Always set as `true`. LPS-167506
		};

		const generalTransformProviderSettings = {
			embeddingVectorDimensions,
			sentenceTransformProvider,
		};

		const indexingSettings = {
			assetEntryClassNames,
			languageIds,
			maxCharacterCount,
			textTruncationStrategy,
		};

		fetch(
			'/o/search-experiences-rest/v1.0/sentence-transformer/validate-configuration',
			{
				body: JSON.stringify({
					...generalSettings,
					...generalTransformProviderSettings,
					..._getSentenceTransformProviderSettings(),
					...indexingSettings,
				}),
				headers: new Headers({
					'Accept': 'application/json',
					'Accept-Language': Liferay.ThemeDisplay.getBCP47LanguageId(),
					'Content-Type': 'application/json',
				}),
				method: 'POST',
			}
		)
			.then((response) => response.json())
			.then((responseData) => {

				// If there is an error with the connection.
				//
				// Example `errorMessage` string (Can vary based on sentence
				//  transformer provider):
				// '{"error": "Authorization header is correct, but the token seems invalid"}'
				// '[{\"generated_text":\"com.liferay.portal.kernel.util.Http$Body@7e13...\"}]'

				if (responseData.errorMessage) {
					try {
						const errorMessage =
							typeof responseData.errorMessage === 'string'
								? JSON.parse(responseData.errorMessage)
								: responseData.errorMessage;

						// If `errorMessage` has `error` property that is a string.

						if (
							errorMessage?.error &&
							typeof errorMessage?.error === 'string'
						) {
							return setTestResultsMessage({
								message: errorMessage?.error,
								type: 'warning',
							});
						}

						// If `errorMessage` value is an object or array.

						return setTestResultsMessage({
							message: sub(
								Liferay.Language.get(
									'unable-to-connect-to-x.-connection-failed-with-x'
								),
								[
									availableSentenceTransformProviders[
										sentenceTransformProvider
									],
									JSON.stringify(errorMessage),
								]
							),
							type: 'warning',
						});
					}
					catch {

						// If `errorMessage` is a string.

						return setTestResultsMessage({
							message: sub(
								Liferay.Language.get(
									'unable-to-connect-to-x.-connection-failed-with-x'
								),
								[
									availableSentenceTransformProviders[
										sentenceTransformProvider
									],
									responseData.errorMessage,
								]
							),
							type: 'warning',
						});
					}
				}

				// If the user has no permissions for the REST endpoint.
				// Example: {"message": "Access denied to com.liferay.search.experiences.rest.internal.resource.v1_0.SentenceTransformerValidationResultResourceImpl#postSentenceTransformerValidateConfiguration"}

				if (responseData.message) {
					throw new Error(responseData.message);
				}

				// If the response expected dimensions is 0. This means no
				// results were returned from the sentence transformer.

				if (Number(responseData.expectedDimensions === 0)) {
					return setTestResultsMessage({
						message: Liferay.Language.get(
							'the-sentence-transform-provider-returned-no-results'
						),
						type: 'danger',
					});
				}

				// If the expected dimensions don't match the configure
				// dimensions.

				if (
					Number(responseData.expectedDimensions) !==
					Number(embeddingVectorDimensions)
				) {
					return setTestResultsMessage({
						message: sub(
							Liferay.Language.get(
								'the-dimensions-from-the-connection-do-not-match-the-configured-embedding-vector-dimensions'
							),
							[
								embeddingVectorDimensions,
								responseData.expectedDimensions,
							]
						),
						type: 'warning',
					});
				}

				// If the none of the previous checks are caught, assume a
				// successful connection.

				setTestResultsMessage({
					message: Liferay.Language.get('connection-is-successful'),
					type: 'success',
				});
			})
			.catch((error) => {
				setTestResultsMessage({
					message: Liferay.Language.get(
						'unable-to-test-configuration-due-to-an-unexpected-error'
					),
					type: 'danger',
				});

				if (process.env.NODE_ENV === 'development') {
					console.error(error);
				}
			})
			.finally(() => {
				setLoading(false);
			});
	};

	const isMissingRequiredFields = () => {
		if (
			sentenceTransformProvider ===
			SENTENCE_TRANSFORM_PROVIDER_TYPES.HUGGING_FACE
		) {
			return (
				errors.huggingFaceAccessToken ||
				errors.model ||
				errors.modelTimeout
			);
		}

		return false;
	};

	return (
		<div className="test-configuration-button-root">
			<ClayTooltipProvider>
				<ClayButton
					aria-disabled={loading || isMissingRequiredFields()}
					aria-label={Liferay.Language.get('test-configuration')}
					className={
						loading || isMissingRequiredFields() ? 'disabled' : ''
					}
					displayType="secondary"
					onClick={_handleTestConfigurationButtonClick}
					{...(isMissingRequiredFields()
						? {
								title: Liferay.Language.get(
									'required-fields-missing'
								),
						  }
						: {})}
				>
					{loading && (
						<span className="inline-item inline-item-before">
							<ClayLoadingIndicator small />
						</span>
					)}

					{Liferay.Language.get('test-configuration')}
				</ClayButton>
			</ClayTooltipProvider>

			{!!testResultsMessage.message && (
				<ClayAlert
					className="mt-2"
					displayType={testResultsMessage.type}
					title={testResultsMessage.message}
					variant="feedback"
				/>
			)}
		</div>
	);
}

export default TestConfigurationButton;
