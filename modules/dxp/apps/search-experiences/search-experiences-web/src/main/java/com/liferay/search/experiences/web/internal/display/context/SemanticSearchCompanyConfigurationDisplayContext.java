/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.web.internal.display.context;

import java.util.List;
import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class SemanticSearchCompanyConfigurationDisplayContext {

	public List<String> getAssetEntryClassNames() {
		return _assetEntryClassNames;
	}

	public Map<String, String> getAvailableAssetEntryClassNames() {
		return _availableAssetEntryClassNames;
	}

	public List<String> getAvailableEmbeddingVectorDimensions() {
		return _availableEmbeddingVectorDimensions;
	}

	public Map<String, String> getAvailableLanguageDisplayNames() {
		return _availableLanguageDisplayNames;
	}

	public Map<String, String> getAvailableSentenceTransformers() {
		return _availableSentenceTransformers;
	}

	public Map<String, String> getAvailableTextTruncationStrategies() {
		return _availableTextTruncationStrategies;
	}

	public int getCacheTimeout() {
		return _cacheTimeout;
	}

	public int getEmbeddingVectorDimensions() {
		return _embeddingVectorDimensions;
	}

	public String getHuggingFaceAccessToken() {
		return _huggingFaceAccessToken;
	}

	public List<String> getLanguageIds() {
		return _languageIds;
	}

	public int getMaxCharacterCount() {
		return _maxCharacterCount;
	}

	public String getModel() {
		return _model;
	}

	public int getModelTimeout() {
		return _modelTimeout;
	}

	public String getSentenceTransformer() {
		return _sentenceTransformer;
	}

	public String getTextTruncationStrategy() {
		return _textTruncationStrategy;
	}

	public String getTxtaiHostAddress() {
		return _txtaiHostAddress;
	}

	public String getTxtaiPassword() {
		return _txtaiPassword;
	}

	public String getTxtaiUserName() {
		return _txtaiUsername;
	}

	public boolean isSentenceTransformerEnabled() {
		return _sentenceTransformerEnabled;
	}

	public void setAssetEntryClassNames(List<String> assetEntryClassNames) {
		_assetEntryClassNames = assetEntryClassNames;
	}

	public void setAvailableAssetEntryClassNames(
		Map<String, String> availableAssetEntryClassNames) {

		_availableAssetEntryClassNames = availableAssetEntryClassNames;
	}

	public void setAvailableEmbeddingVectorDimensions(
		List<String> availableEmbeddingVectorDimensions) {

		_availableEmbeddingVectorDimensions =
			availableEmbeddingVectorDimensions;
	}

	public void setAvailableLanguageDisplayNames(
		Map<String, String> availableLanguageDisplayNames) {

		_availableLanguageDisplayNames = availableLanguageDisplayNames;
	}

	public void setAvailableSentenceTransformers(
		Map<String, String> availableSentenceTransformers) {

		_availableSentenceTransformers = availableSentenceTransformers;
	}

	public void setAvailableTextTruncationStrategies(
		Map<String, String> availableTextTruncationStrategies) {

		_availableTextTruncationStrategies = availableTextTruncationStrategies;
	}

	public void setCacheTimeout(int cacheTimeout) {
		_cacheTimeout = cacheTimeout;
	}

	public void setEmbeddingVectorDimensions(int embeddingVectorDimensions) {
		_embeddingVectorDimensions = embeddingVectorDimensions;
	}

	public void setHuggingFaceAccessToken(String huggingFaceAccessToken) {
		_huggingFaceAccessToken = huggingFaceAccessToken;
	}

	public void setLanguageIds(List<String> languageIds) {
		_languageIds = languageIds;
	}

	public void setMaxCharacterCount(int maxCharacterCount) {
		_maxCharacterCount = maxCharacterCount;
	}

	public void setModel(String model) {
		_model = model;
	}

	public void setModelTimeout(int modelTimeout) {
		_modelTimeout = modelTimeout;
	}

	public void setSentenceTransformer(String sentenceTransformer) {
		_sentenceTransformer = sentenceTransformer;
	}

	public void setSentenceTransformerEnabled(
		boolean sentenceTransformerEnabled) {

		_sentenceTransformerEnabled = sentenceTransformerEnabled;
	}

	public void setTextTruncationStrategy(String textTruncationStrategy) {
		_textTruncationStrategy = textTruncationStrategy;
	}

	public void setTxtaiHostAddress(String txtaiHostAddress) {
		_txtaiHostAddress = txtaiHostAddress;
	}

	public void setTxtaiPassword(String txtaiPassword) {
		_txtaiPassword = txtaiPassword;
	}

	public void setTxtaiUserName(String txtaiUsername) {
		_txtaiUsername = txtaiUsername;
	}

	private List<String> _assetEntryClassNames;
	private Map<String, String> _availableAssetEntryClassNames;
	private List<String> _availableEmbeddingVectorDimensions;
	private Map<String, String> _availableLanguageDisplayNames;
	private Map<String, String> _availableSentenceTransformers;
	private Map<String, String> _availableTextTruncationStrategies;
	private int _cacheTimeout;
	private int _embeddingVectorDimensions;
	private String _huggingFaceAccessToken;
	private List<String> _languageIds;
	private int _maxCharacterCount;
	private String _model;
	private int _modelTimeout;
	private String _sentenceTransformer;
	private boolean _sentenceTransformerEnabled;
	private String _textTruncationStrategy;
	private String _txtaiHostAddress;
	private String _txtaiPassword;
	private String _txtaiUsername;

}