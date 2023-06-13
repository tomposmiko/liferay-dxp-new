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

package com.liferay.object.rest.internal.odata.filter.expression;

import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.field.business.type.ObjectFieldBusinessType;
import com.liferay.object.field.business.type.ObjectFieldBusinessTypeRegistry;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.related.models.ObjectRelatedModelsPredicateProvider;
import com.liferay.object.related.models.ObjectRelatedModelsPredicateProviderRegistry;
import com.liferay.object.rest.internal.odata.entity.v1_0.ObjectEntryEntityModel;
import com.liferay.object.service.ObjectDefinitionLocalServiceUtil;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectRelationshipLocalServiceUtil;
import com.liferay.petra.function.UnsafeBiFunction;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.petra.sql.dsl.spi.expression.DefaultPredicate;
import com.liferay.petra.sql.dsl.spi.expression.Operand;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.filter.expression.BinaryExpression;
import com.liferay.portal.odata.filter.expression.CollectionPropertyExpression;
import com.liferay.portal.odata.filter.expression.ComplexPropertyExpression;
import com.liferay.portal.odata.filter.expression.Expression;
import com.liferay.portal.odata.filter.expression.ExpressionVisitException;
import com.liferay.portal.odata.filter.expression.ExpressionVisitor;
import com.liferay.portal.odata.filter.expression.LambdaFunctionExpression;
import com.liferay.portal.odata.filter.expression.LambdaVariableExpression;
import com.liferay.portal.odata.filter.expression.ListExpression;
import com.liferay.portal.odata.filter.expression.LiteralExpression;
import com.liferay.portal.odata.filter.expression.MemberExpression;
import com.liferay.portal.odata.filter.expression.MethodExpression;
import com.liferay.portal.odata.filter.expression.PrimitivePropertyExpression;
import com.liferay.portal.odata.filter.expression.UnaryExpression;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marco Leo
 */
public class PredicateExpressionVisitorImpl
	implements ExpressionVisitor<Object> {

	public PredicateExpressionVisitorImpl(
		EntityModel entityModel, long objectDefinitionId,
		ObjectFieldBusinessTypeRegistry objectFieldBusinessTypeRegistry,
		ObjectFieldLocalService objectFieldLocalService,
		ObjectRelatedModelsPredicateProviderRegistry
			objectRelatedModelsPredicateProviderRegistry) {

		this(
			entityModel, new HashMap<>(), objectDefinitionId,
			objectFieldBusinessTypeRegistry, objectFieldLocalService,
			objectRelatedModelsPredicateProviderRegistry);
	}

	@Override
	public Predicate visitBinaryExpressionOperation(
		BinaryExpression.Operation operation, Object left, Object right) {

		Predicate predicate = null;

		if (_isComplexProperExpression(left)) {
			predicate = _getPredicateForRelationships(
				left,
				(objectFieldName, relatedObjectDefinitionId) -> _getPredicate(
					objectFieldName, relatedObjectDefinitionId, operation,
					right));
		}
		else {
			predicate = _getPredicate(
				left, _objectDefinitionId, operation, right);
		}

		if (predicate != null) {
			return predicate;
		}

		throw new UnsupportedOperationException(
			"Unsupported method visitBinaryExpressionOperation with " +
				"operation " + operation);
	}

	@Override
	public Predicate visitCollectionPropertyExpression(
			CollectionPropertyExpression collectionPropertyExpression)
		throws ExpressionVisitException {

		LambdaFunctionExpression lambdaFunctionExpression =
			collectionPropertyExpression.getLambdaFunctionExpression();

		return (Predicate)lambdaFunctionExpression.accept(
			new PredicateExpressionVisitorImpl(
				_getObjectDefinitionEntityModel(_objectDefinitionId),
				Collections.singletonMap(
					lambdaFunctionExpression.getVariableName(),
					collectionPropertyExpression.getName()),
				_objectDefinitionId, _objectFieldBusinessTypeRegistry,
				_objectFieldLocalService,
				_objectRelatedModelsPredicateProviderRegistry));
	}

	@Override
	public Object visitComplexPropertyExpression(
			ComplexPropertyExpression complexPropertyExpression)
		throws ExpressionVisitException {

		return complexPropertyExpression.toString();
	}

	@Override
	public Object visitLambdaFunctionExpression(
			LambdaFunctionExpression.Type type, String variableName,
			Expression expression)
		throws ExpressionVisitException {

		return expression.accept(this);
	}

	@Override
	public Object visitLambdaVariableExpression(
		LambdaVariableExpression lambdaVariableExpression) {

		return _lambdaVariableExpressionFieldNames.get(
			lambdaVariableExpression.getVariableName());
	}

	@Override
	public Predicate visitListExpressionOperation(
			ListExpression.Operation operation, Object left,
			List<Object> rights)
		throws ExpressionVisitException {

		if (Objects.equals(ListExpression.Operation.IN, operation)) {
			Predicate predicate = null;

			if (_isComplexProperExpression(left)) {
				predicate = _getPredicateForRelationships(
					left,
					(objectFieldName, relatedObjectDefinitionId) ->
						_getInPredicate(
							objectFieldName, relatedObjectDefinitionId,
							rights));
			}
			else {
				predicate = _getInPredicate(left, _objectDefinitionId, rights);
			}

			return predicate;
		}

		throw new UnsupportedOperationException(
			"Unsupported method visitListExpressionOperation with operation " +
				operation);
	}

	@Override
	public Object visitLiteralExpression(LiteralExpression literalExpression) {
		if (Objects.equals(
				LiteralExpression.Type.BOOLEAN, literalExpression.getType())) {

			return GetterUtil.getBoolean(literalExpression.getText());
		}
		else if (Objects.equals(
					LiteralExpression.Type.DATE, literalExpression.getType())) {

			return GetterUtil.getDate(
				literalExpression.getText(),
				DateFormatFactoryUtil.getSimpleDateFormat("yyyy-MM-dd"));
		}
		else if (Objects.equals(
					LiteralExpression.Type.DOUBLE,
					literalExpression.getType())) {

			return GetterUtil.getDouble(literalExpression.getText());
		}
		else if (Objects.equals(
					LiteralExpression.Type.INTEGER,
					literalExpression.getType())) {

			return GetterUtil.getLong(literalExpression.getText());
		}
		else if (Objects.equals(
					LiteralExpression.Type.NULL, literalExpression.getType())) {

			return null;
		}
		else if (Objects.equals(
					LiteralExpression.Type.STRING,
					literalExpression.getType())) {

			return StringUtil.replace(
				StringUtil.unquote(literalExpression.getText()),
				StringPool.DOUBLE_APOSTROPHE, StringPool.APOSTROPHE);
		}

		return literalExpression.getText();
	}

	@Override
	public Object visitMemberExpression(MemberExpression memberExpression)
		throws ExpressionVisitException {

		Expression expression = memberExpression.getExpression();

		return expression.accept(this);
	}

	@Override
	public Object visitMethodExpression(
		List<Object> expressions, MethodExpression.Type type) {

		if (expressions.size() == 2) {
			String left = (String)expressions.get(0);
			Object fieldValue = expressions.get(1);

			Predicate predicate = null;

			if (type == MethodExpression.Type.CONTAINS) {
				if (_isComplexProperExpression(left)) {
					predicate = _getPredicateForRelationships(
						left,
						(objectFieldName, relatedObjectDefinitionId) ->
							_contains(
								objectFieldName, fieldValue,
								relatedObjectDefinitionId));
				}
				else {
					predicate = _contains(
						left, fieldValue, _objectDefinitionId);
				}

				if (predicate != null) {
					return predicate;
				}
			}
			else if (type == MethodExpression.Type.STARTS_WITH) {
				if (_isComplexProperExpression(left)) {
					predicate = _getPredicateForRelationships(
						left,
						(objectFieldName, relatedObjectDefinitionId) ->
							_startsWith(
								objectFieldName, fieldValue,
								relatedObjectDefinitionId));
				}
				else {
					predicate = _startsWith(
						left, fieldValue, _objectDefinitionId);
				}

				if (predicate != null) {
					return predicate;
				}
			}
		}

		throw new UnsupportedOperationException(
			StringBundler.concat(
				"Unsupported method visitMethodExpression with method type ",
				type, " and ", expressions.size(), " parameters"));
	}

	@Override
	public Object visitPrimitivePropertyExpression(
		PrimitivePropertyExpression primitivePropertyExpression) {

		return primitivePropertyExpression.getName();
	}

	@Override
	public Predicate visitUnaryExpressionOperation(
		UnaryExpression.Operation operation, Object operand) {

		if (!Objects.equals(UnaryExpression.Operation.NOT, operation)) {
			throw new UnsupportedOperationException(
				"Unsupported method visitUnaryExpressionOperation with " +
					"operation " + operation);
		}

		DefaultPredicate defaultPredicate = (DefaultPredicate)operand;

		if (Objects.equals(Operand.IN, defaultPredicate.getOperand())) {
			return new DefaultPredicate(
				defaultPredicate.getLeftExpression(), Operand.NOT_IN,
				defaultPredicate.getRightExpression());
		}

		return Predicate.not(defaultPredicate);
	}

	private PredicateExpressionVisitorImpl(
		EntityModel entityModel,
		Map<String, String> lambdaVariableExpressionFieldNames,
		long objectDefinitionId,
		ObjectFieldBusinessTypeRegistry objectFieldBusinessTypeRegistry,
		ObjectFieldLocalService objectFieldLocalService,
		ObjectRelatedModelsPredicateProviderRegistry
			objectRelatedModelsPredicateProviderRegistry) {

		_entityModels.put(objectDefinitionId, entityModel);
		_lambdaVariableExpressionFieldNames =
			lambdaVariableExpressionFieldNames;
		_objectDefinitionId = objectDefinitionId;
		_objectFieldBusinessTypeRegistry = objectFieldBusinessTypeRegistry;
		_objectFieldLocalService = objectFieldLocalService;
		_objectRelatedModelsPredicateProviderRegistry =
			objectRelatedModelsPredicateProviderRegistry;
	}

	private Predicate _contains(
		Object fieldName, Object fieldValue, long objectDefinitionId) {

		Column<?, Object> column = _getColumn(fieldName, objectDefinitionId);

		return column.like(
			StringPool.PERCENT +
				_getValue(fieldName, objectDefinitionId, fieldValue) +
					StringPool.PERCENT);
	}

	private EntityModel _createEntityModel(long objectDefinitionId) {
		try {
			return new ObjectEntryEntityModel(
				ObjectDefinitionLocalServiceUtil.getObjectDefinition(
					objectDefinitionId),
				_objectFieldLocalService.getObjectFields(objectDefinitionId));
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return new ObjectEntryEntityModel(Collections.emptyList());
		}
	}

	private ObjectRelationship _fetchObjectRelationship(
		String relationshipName) {

		try {
			return ObjectRelationshipLocalServiceUtil.
				getObjectRelationshipByObjectDefinitionId(
					_objectDefinitionId,
					GetterUtil.getString(relationshipName));
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return null;
		}
	}

	private Column<?, Object> _getColumn(
		Object fieldName, long objectDefinitionId) {

		EntityField entityField = _getEntityField(
			fieldName, objectDefinitionId);

		return (Column<?, Object>)_objectFieldLocalService.getColumn(
			objectDefinitionId, entityField.getFilterableName(null));
	}

	private EntityField _getEntityField(
		Object fieldName, long objectDefinitionId) {

		Map<String, EntityField> entityFieldsMap = _getEntityFieldsMap(
			objectDefinitionId);

		return entityFieldsMap.get(GetterUtil.getString(fieldName));
	}

	private Map<String, EntityField> _getEntityFieldsMap(
		long objectDefinitionId) {

		EntityModel entityModel = _getObjectDefinitionEntityModel(
			objectDefinitionId);

		return entityModel.getEntityFieldsMap();
	}

	private Predicate _getExpressionPredicate(
		Column<?, Object> column, BinaryExpression.Operation operation,
		Object value) {

		if (Objects.equals(BinaryExpression.Operation.EQ, operation)) {
			return column.eq(value);
		}
		else if (Objects.equals(BinaryExpression.Operation.GE, operation)) {
			return column.gte(value);
		}
		else if (Objects.equals(BinaryExpression.Operation.GT, operation)) {
			return column.gt(value);
		}
		else if (Objects.equals(BinaryExpression.Operation.LE, operation)) {
			return column.lte(value);
		}
		else if (Objects.equals(BinaryExpression.Operation.LT, operation)) {
			return column.lt(value);
		}
		else if (Objects.equals(BinaryExpression.Operation.NE, operation)) {
			return column.neq(value);
		}

		return null;
	}

	private Predicate _getInPredicate(
		Object left, long objectDefinitionId, List<Object> rights) {

		return _getColumn(
			left, objectDefinitionId
		).in(
			TransformUtil.transformToArray(
				rights, right -> _getValue(left, objectDefinitionId, right),
				Object.class)
		);
	}

	private EntityModel _getObjectDefinitionEntityModel(
		long objectDefinitionId) {

		EntityModel entityModel = _entityModels.get(objectDefinitionId);

		if (entityModel == null) {
			entityModel = _createEntityModel(objectDefinitionId);

			_entityModels.put(objectDefinitionId, entityModel);
		}

		return entityModel;
	}

	private Predicate _getPredicate(
		Object left, long objectDefinitionId,
		BinaryExpression.Operation operation, Object right) {

		Predicate predicate = null;

		if (Objects.equals(BinaryExpression.Operation.AND, operation)) {
			predicate = Predicate.and(
				Predicate.withParentheses((Predicate)left),
				Predicate.withParentheses((Predicate)right));
		}
		else if (Objects.equals(BinaryExpression.Operation.OR, operation)) {
			predicate = Predicate.or(
				Predicate.withParentheses((Predicate)left),
				Predicate.withParentheses((Predicate)right));
		}
		else {
			ObjectField objectField = _objectFieldLocalService.fetchObjectField(
				objectDefinitionId, String.valueOf(left));

			if ((objectField != null) &&
				StringUtil.equals(
					objectField.getBusinessType(),
					ObjectFieldConstants.BUSINESS_TYPE_MULTISELECT_PICKLIST)) {

				predicate = _contains(left, right, objectDefinitionId);
			}
		}

		if (predicate != null) {
			return predicate;
		}

		return _getExpressionPredicate(
			_getColumn(left, objectDefinitionId), operation,
			_getValue(left, objectDefinitionId, right));
	}

	private Predicate _getPredicateForRelationships(
		Object left,
		UnsafeBiFunction<String, Long, Predicate, Exception> unsafeBiFunction) {

		String leftString = (String)left;

		String[] leftStringParts = leftString.split(StringPool.SLASH);

		String relationshipName = leftStringParts[0];

		ObjectRelationship objectRelationship = _fetchObjectRelationship(
			relationshipName);

		if (objectRelationship != null) {
			String objectFieldName = leftStringParts[1];

			try {
				return _getPredicateForRelationships(
					objectRelationship,
					unsafeBiFunction.apply(
						objectFieldName,
						_getRelatedObjectDefinitionId(
							_objectDefinitionId, objectRelationship)));
			}
			catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		}

		return null;
	}

	private Predicate _getPredicateForRelationships(
			ObjectRelationship objectRelationship, Predicate predicate)
		throws Exception {

		ObjectDefinition objectDefinition =
			ObjectDefinitionLocalServiceUtil.getObjectDefinition(
				_objectDefinitionId);

		ObjectRelatedModelsPredicateProvider
			objectRelatedModelsPredicateProvider =
				_objectRelatedModelsPredicateProviderRegistry.
					getObjectRelatedModelsPredicateProvider(
						objectDefinition.getClassName(),
						objectRelationship.getType());

		return objectRelatedModelsPredicateProvider.getPredicate(
			objectRelationship, predicate);
	}

	private long _getRelatedObjectDefinitionId(
		long objectDefinitionId, ObjectRelationship objectRelationship) {

		if (objectRelationship.getObjectDefinitionId1() != objectDefinitionId) {
			return objectRelationship.getObjectDefinitionId1();
		}

		return objectRelationship.getObjectDefinitionId2();
	}

	private Object _getValue(
		Object left, long objectDefinitionId, Object right) {

		EntityField entityField = _getEntityField(left, objectDefinitionId);

		EntityField.Type entityType = entityField.getType();

		DB db = DBManagerUtil.getDB();

		if (entityType.equals(EntityField.Type.DATE_TIME) &&
			(db.getDBType() == DBType.HYPERSONIC)) {

			try {
				Format format = FastDateFormatFactoryUtil.getSimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss.SSS");

				DateFormat dateFormat =
					DateFormatFactoryUtil.getSimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");

				Date date = dateFormat.parse(right.toString());

				right = format.format(date);
			}
			catch (ParseException parseException) {
				throw new RuntimeException(parseException);
			}
		}

		String entityFieldFilterableName = entityField.getFilterableName(null);
		String entityFieldName = entityField.getName();

		try {
			ObjectField objectField = _objectFieldLocalService.getObjectField(
				_objectDefinitionId, entityFieldFilterableName);

			ObjectFieldBusinessType objectFieldBusinessType =
				_objectFieldBusinessTypeRegistry.getObjectFieldBusinessType(
					objectField.getBusinessType());

			Object value = objectFieldBusinessType.getValue(
				objectField, Collections.singletonMap(entityFieldName, right));

			if (value == null) {
				return right;
			}

			if (Objects.equals(
					objectFieldBusinessType.getDBType(),
					ObjectFieldConstants.DB_TYPE_LONG)) {

				return GetterUtil.getLong(value);
			}

			return value;
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return right;
		}
	}

	private boolean _isComplexProperExpression(Object object) {
		if (object instanceof String) {
			String string = (String)object;

			return string.contains(StringPool.SLASH);
		}

		return false;
	}

	private Predicate _startsWith(
		Object fieldName, Object fieldValue, long objectDefinitionId) {

		Column<?, Object> column = _getColumn(fieldName, objectDefinitionId);

		return column.like(
			_getValue(fieldName, objectDefinitionId, fieldValue) +
				StringPool.PERCENT);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PredicateExpressionVisitorImpl.class);

	private final Map<Long, EntityModel> _entityModels = new HashMap<>();
	private final Map<String, String> _lambdaVariableExpressionFieldNames;
	private final long _objectDefinitionId;
	private final ObjectFieldBusinessTypeRegistry
		_objectFieldBusinessTypeRegistry;
	private final ObjectFieldLocalService _objectFieldLocalService;
	private final ObjectRelatedModelsPredicateProviderRegistry
		_objectRelatedModelsPredicateProviderRegistry;

}