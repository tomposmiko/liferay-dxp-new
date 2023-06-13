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

package com.liferay.data.engine.exception;

import com.liferay.portal.kernel.exception.PortalException;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Leonardo Barros
 */
public class DEDataRecordCollectionException extends PortalException {

	public DEDataRecordCollectionException() {
	}

	public DEDataRecordCollectionException(String msg) {
		super(msg);
	}

	public DEDataRecordCollectionException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DEDataRecordCollectionException(Throwable cause) {
		super(cause);
	}

	public static class AddDataRecord extends DEDataRecordCollectionException {

		public AddDataRecord(Throwable cause) {
			super(cause);
		}

	}

	public static class DeleteDataRecord
		extends DEDataRecordCollectionException {

		public DeleteDataRecord(Throwable cause) {
			super(cause);
		}

	}

	public static class GetDataRecord extends DEDataRecordCollectionException {

		public GetDataRecord(Throwable cause) {
			super(cause);
		}

	}

	public static class InvalidDataRecord
		extends DEDataRecordCollectionException {

		public InvalidDataRecord(Map<String, Set<String>> validationErrors) {
			_validationErrors = validationErrors;
		}

		public Map<String, Set<String>> getValidationErrors() {
			return Collections.unmodifiableMap(_validationErrors);
		}

		private final Map<String, Set<String>> _validationErrors;

	}

	public static class MustHavePermission
		extends DEDataRecordCollectionException {

		public MustHavePermission(String[] actionId, Throwable cause) {
			super(cause);

			_actionId = actionId;
		}

		public String[] getActionId() {
			return _actionId;
		}

		private final String[] _actionId;

	}

	public static class NoSuchDataRecord
		extends DEDataRecordCollectionException {

		public NoSuchDataRecord(long deDataRecordId, Throwable cause) {
			super(cause);

			_deDataRecordId = deDataRecordId;
		}

		public long getDEDataRecordId() {
			return _deDataRecordId;
		}

		private final long _deDataRecordId;

	}

	public static class NoSuchDataRecordCollection
		extends DEDataRecordCollectionException {

		public NoSuchDataRecordCollection(
			long deDataRecordCollectionId, Throwable cause) {

			super(cause);

			_deDataRecordCollectionId = deDataRecordCollectionId;
		}

		public long getDEDataRecordCollectionId() {
			return _deDataRecordCollectionId;
		}

		private final long _deDataRecordCollectionId;

	}

	public static class NoSuchDataStorage
		extends DEDataRecordCollectionException {

		public NoSuchDataStorage(String storageType) {
			_storageType = storageType;
		}

		public String getStorageType() {
			return _storageType;
		}

		private final String _storageType;

	}

	public static class NoSuchFields extends DEDataRecordCollectionException {

		public NoSuchFields(String[] fieldNames) {
			_fieldNames = fieldNames;
		}

		public String[] getFieldNames() {
			return _fieldNames;
		}

		private final String[] _fieldNames;

	}

	public static class NoSuchRoles extends DEDataRecordCollectionException {

		public NoSuchRoles(String[] roleNames) {
			_roleNames = roleNames;
		}

		public String[] getRoleNames() {
			return _roleNames;
		}

		private final String[] _roleNames;

	}

	public static class PrincipalException
		extends DEDataRecordCollectionException {

		public PrincipalException(Throwable cause) {
			super(cause);
		}

	}

}