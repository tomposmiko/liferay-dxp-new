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

package com.liferay.info.exception;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.Locale;

/**
 * @author Lourdes Fernández Besada
 */
public class InfoFormValidationException extends InfoFormException {

	public InfoFormValidationException() {
		_infoFieldUniqueId = StringPool.BLANK;
	}

	public InfoFormValidationException(String infoFieldUniqueId) {
		_infoFieldUniqueId = infoFieldUniqueId;
	}

	public String getInfoFieldUniqueId() {
		return _infoFieldUniqueId;
	}

	public String getLocalizedMessage(String fieldLabel, Locale locale) {
		return LanguageUtil.format(
			locale, "x-an-error-occurred", fieldLabel, false);
	}

	public static class ExceedsMaxLength extends InvalidInfoFieldValue {

		public ExceedsMaxLength(String infoFieldUniqueId, int maxLength) {
			super(infoFieldUniqueId);

			_maxLength = maxLength;
		}

		@Override
		public String getLocalizedMessage(Locale locale) {
			return LanguageUtil.get(
				locale, "value-exceeds-maximum-length-of-x");
		}

		@Override
		public String getLocalizedMessage(String fieldLabel, Locale locale) {
			return LanguageUtil.format(
				locale, "value-exceeds-maximum-length-of-x-for-field-x",
				new String[] {String.valueOf(_maxLength), fieldLabel}, false);
		}

		private final int _maxLength;

	}

	public static class ExceedsMaxValue extends InvalidInfoFieldValue {

		public ExceedsMaxValue(String infoFieldUniqueId, long maxValue) {
			super(infoFieldUniqueId);

			_maxValue = maxValue;
		}

		@Override
		public String getLocalizedMessage(Locale locale) {
			return LanguageUtil.get(locale, "value-exceeds-maximum-value-of-x");
		}

		@Override
		public String getLocalizedMessage(String fieldLabel, Locale locale) {
			return LanguageUtil.format(
				locale, "value-exceeds-maximum-value-of-x-for-field-x",
				new String[] {String.valueOf(_maxValue), fieldLabel}, false);
		}

		private final long _maxValue;

	}

	public static class ExceedsMinValue extends InvalidInfoFieldValue {

		public ExceedsMinValue(String infoFieldUniqueId, long minValue) {
			super(infoFieldUniqueId);

			_minValue = minValue;
		}

		@Override
		public String getLocalizedMessage(Locale locale) {
			return LanguageUtil.get(locale, "value-exceeds-minimum-value-of-x");
		}

		@Override
		public String getLocalizedMessage(String fieldLabel, Locale locale) {
			return LanguageUtil.format(
				locale, "value-exceeds-minimum-value-of-x-for-field-x",
				new String[] {String.valueOf(_minValue), fieldLabel}, false);
		}

		private final long _minValue;

	}

	public static class FileSize extends InfoFormValidationException {

		public FileSize(String infoFieldUniqueId, String maximumSizeAllowed) {
			super(infoFieldUniqueId);

			_maximumSizeAllowed = maximumSizeAllowed;
		}

		@Override
		public String getLocalizedMessage(Locale locale) {
			return LanguageUtil.format(
				locale,
				"file-size-is-larger-than-the-allowed-overall-maximum-upload-" +
					"request-size-x",
				_maximumSizeAllowed);
		}

		@Override
		public String getLocalizedMessage(String fieldLabel, Locale locale) {
			return LanguageUtil.format(
				locale,
				"x-file-size-is-larger-than-the-allowed-overall-maximum-" +
					"upload-request-size-x",
				new String[] {fieldLabel, _maximumSizeAllowed}, false);
		}

		public String getMaximumSizeAllowed() {
			return _maximumSizeAllowed;
		}

		private final String _maximumSizeAllowed;

	}

	public static class InvalidCaptcha extends InfoFormValidationException {

		@Override
		public String getLocalizedMessage(Locale locale) {
			return LanguageUtil.get(locale, "captcha-verification-failed");
		}

		@Override
		public String getLocalizedMessage(String fieldLabel, Locale locale) {
			return getLocalizedMessage(locale);
		}

	}

	public static class InvalidFileExtension
		extends InfoFormValidationException {

		public InvalidFileExtension(
			String infoFieldUniqueId, String validFileExtensions) {

			super(infoFieldUniqueId);

			_validFileExtensions = validFileExtensions;
		}

		@Override
		public String getLocalizedMessage(Locale locale) {
			return LanguageUtil.format(
				locale, "please-enter-a-file-with-a-valid-extension-x",
				_validFileExtensions);
		}

		@Override
		public String getLocalizedMessage(String fieldLabel, Locale locale) {
			return LanguageUtil.format(
				locale, "x-please-enter-a-file-with-a-valid-extension-x",
				new String[] {fieldLabel, _validFileExtensions}, false);
		}

		public String getValidFileExtensions() {
			return _validFileExtensions;
		}

		private final String _validFileExtensions;

	}

	public static class InvalidInfoFieldValue
		extends InfoFormValidationException {

		public InvalidInfoFieldValue(String infoFieldUniqueId) {
			super(infoFieldUniqueId);
		}

		@Override
		public String getLocalizedMessage(Locale locale) {
			return LanguageUtil.get(locale, "this-field-is-invalid");
		}

		@Override
		public String getLocalizedMessage(String fieldLabel, Locale locale) {
			return LanguageUtil.format(
				locale, "the-x-is-invalid", fieldLabel, false);
		}

	}

	public static class RequiredInfoField extends InfoFormValidationException {

		public RequiredInfoField(String infoFieldUniqueId) {
			super(infoFieldUniqueId);
		}

		@Override
		public String getLocalizedMessage(Locale locale) {
			return LanguageUtil.get(locale, "this-field-is-required");
		}

		@Override
		public String getLocalizedMessage(String fieldLabel, Locale locale) {
			return LanguageUtil.format(
				locale, "the-x-is-required", fieldLabel, false);
		}

	}

	private final String _infoFieldUniqueId;

}