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

package com.liferay.segments.internal.odata.filter.expression;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.filter.expression.BinaryExpression;
import com.liferay.portal.odata.filter.expression.Expression;
import com.liferay.portal.odata.filter.expression.ExpressionVisitException;
import com.liferay.portal.odata.filter.expression.ExpressionVisitor;
import com.liferay.portal.odata.filter.expression.LiteralExpression;
import com.liferay.portal.odata.filter.expression.MemberExpression;
import com.liferay.portal.odata.filter.expression.MethodExpression;
import com.liferay.portal.odata.filter.expression.PrimitivePropertyExpression;
import com.liferay.portal.odata.filter.expression.UnaryExpression;
import com.liferay.segments.context.Context;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Eduardo García
 */
public class ExpressionVisitorImpl implements ExpressionVisitor<Object> {

	public ExpressionVisitorImpl(EntityModel entityModel) {
		_entityModel = entityModel;
	}

	@Override
	public Predicate<Context> visitBinaryExpressionOperation(
		BinaryExpression.Operation operation, Object left, Object right) {

		Optional<Predicate<Context>> predicateOptional = _getPredicateOptional(
			operation, left, right);

		return predicateOptional.orElseThrow(
			() -> new UnsupportedOperationException(
				"Unsupported method visitBinaryExpressionOperation with " +
					"operation " + operation));
	}

	@Override
	public Object visitLiteralExpression(LiteralExpression literalExpression) {
		if (Objects.equals(
				LiteralExpression.Type.BOOLEAN, literalExpression.getType())) {

			return Boolean.valueOf(literalExpression.getText());
		}
		else if (Objects.equals(
					LiteralExpression.Type.DATE, literalExpression.getType())) {

			return LocalDate.parse(literalExpression.getText());
		}
		else if (Objects.equals(
					LiteralExpression.Type.DATE_TIME,
					literalExpression.getType())) {

			return ZonedDateTime.parse(literalExpression.getText());
		}
		else if (Objects.equals(
					LiteralExpression.Type.DOUBLE,
					literalExpression.getType())) {

			return Double.valueOf(literalExpression.getText());
		}
		else if (Objects.equals(
					LiteralExpression.Type.INTEGER,
					literalExpression.getType())) {

			return Integer.valueOf(literalExpression.getText());
		}
		else if (Objects.equals(
					LiteralExpression.Type.STRING,
					literalExpression.getType())) {

			return _normalizeStringLiteral(literalExpression.getText());
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

		if (type == MethodExpression.Type.CONTAINS) {
			if (expressions.size() != 2) {
				throw new UnsupportedOperationException(
					StringBundler.concat(
						"Unsupported method visitMethodExpression with method",
						"type ", type, " and ", expressions.size(),
						" parameters"));
			}

			return _contains(
				(EntityField)expressions.get(0), expressions.get(1));
		}

		throw new UnsupportedOperationException(
			"Unsupported method visitMethodExpression with method type " +
				type);
	}

	@Override
	public Object visitPrimitivePropertyExpression(
		PrimitivePropertyExpression primitivePropertyExpression) {

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		return entityFieldsMap.get(primitivePropertyExpression.getName());
	}

	@Override
	public Predicate<Context> visitUnaryExpressionOperation(
		UnaryExpression.Operation operation, Object operand) {

		if (Objects.equals(UnaryExpression.Operation.NOT, operation)) {
			return _getNotPredicate((Predicate<Context>)operand);
		}

		throw new UnsupportedOperationException(
			"Unsupported method visitUnaryExpressionOperation with operation " +
				operation);
	}

	private Predicate<Context> _contains(
		EntityField entityField, Object fieldValue) {

		if (Objects.equals(entityField.getType(), EntityField.Type.STRING)) {
			Predicate<Context> predicate = p -> StringUtils.containsIgnoreCase(
				String.valueOf(p.get(entityField.getName())),
				String.valueOf(fieldValue));

			return predicate;
		}

		throw new UnsupportedOperationException(
			"Unsupported method _contains with entity field type " +
				entityField.getType());
	}

	private Predicate<Context> _getANDPredicate(
		Predicate<Context> leftPredicate, Predicate<Context> rightPredicate) {

		return leftPredicate.and(rightPredicate);
	}

	private Predicate<Context> _getEQPredicate(
		EntityField entityField, Object fieldValue) {

		Predicate<Context> predicate = null;

		if (Objects.equals(entityField.getType(), EntityField.Type.STRING)) {
			predicate = p -> fieldValue.equals(
				_normalizeStringLiteral(
					String.valueOf(p.get(entityField.getName()))));
		}
		else {
			predicate = p -> fieldValue.equals(p.get(entityField.getName()));
		}

		return predicate;
	}

	private Predicate<Context> _getGEPredicate(
		EntityField entityField, Object fieldValue) {

		if (fieldValue instanceof Comparable &&
			(Objects.equals(entityField.getType(), EntityField.Type.DATE) ||
			 Objects.equals(
				 entityField.getType(), EntityField.Type.DATE_TIME) ||
			 Objects.equals(entityField.getType(), EntityField.Type.DOUBLE) ||
			 Objects.equals(entityField.getType(), EntityField.Type.INTEGER) ||
			 Objects.equals(entityField.getType(), EntityField.Type.STRING))) {

			Predicate<Context> predicate = p ->
				((Comparable)fieldValue).compareTo(
					p.get(entityField.getName())) <= 0;

			return predicate;
		}

		throw new UnsupportedOperationException(
			"Unsupported method _getGEPredicate with entity field type " +
				entityField.getType());
	}

	private Predicate<Context> _getGTPredicate(
		EntityField entityField, Object fieldValue) {

		if (fieldValue instanceof Comparable &&
			(Objects.equals(entityField.getType(), EntityField.Type.DATE) ||
			 Objects.equals(
				 entityField.getType(), EntityField.Type.DATE_TIME) ||
			 Objects.equals(entityField.getType(), EntityField.Type.DOUBLE) ||
			 Objects.equals(entityField.getType(), EntityField.Type.INTEGER) ||
			 Objects.equals(entityField.getType(), EntityField.Type.STRING))) {

			Predicate<Context> predicate = p ->
				((Comparable)fieldValue).compareTo(
					p.get(entityField.getName())) < 0;

			return predicate;
		}

		throw new UnsupportedOperationException(
			"Unsupported method _getGTPredicate with entity field type " +
				entityField.getType());
	}

	private Predicate<Context> _getLEPredicate(
		EntityField entityField, Object fieldValue) {

		if (fieldValue instanceof Comparable &&
			(Objects.equals(entityField.getType(), EntityField.Type.DATE) ||
			 Objects.equals(
				 entityField.getType(), EntityField.Type.DATE_TIME) ||
			 Objects.equals(entityField.getType(), EntityField.Type.DOUBLE) ||
			 Objects.equals(entityField.getType(), EntityField.Type.INTEGER) ||
			 Objects.equals(entityField.getType(), EntityField.Type.STRING))) {

			Predicate<Context> predicate = p ->
				((Comparable)fieldValue).compareTo(
					p.get(entityField.getName())) >= 0;

			return predicate;
		}

		throw new UnsupportedOperationException(
			"Unsupported method _getLEPredicate with entity field type " +
				entityField.getType());
	}

	private Predicate<Context> _getLTPredicate(
		EntityField entityField, Object fieldValue) {

		if (fieldValue instanceof Comparable &&
			(Objects.equals(entityField.getType(), EntityField.Type.DATE) ||
			 Objects.equals(
				 entityField.getType(), EntityField.Type.DATE_TIME) ||
			 Objects.equals(entityField.getType(), EntityField.Type.DOUBLE) ||
			 Objects.equals(entityField.getType(), EntityField.Type.INTEGER) ||
			 Objects.equals(entityField.getType(), EntityField.Type.STRING))) {

			Predicate<Context> predicate = p ->
				((Comparable)fieldValue).compareTo(
					p.get(entityField.getName())) > 0;

			return predicate;
		}

		throw new UnsupportedOperationException(
			"Unsupported method _getLTPredicate with entity field type " +
				entityField.getType());
	}

	private Predicate<Context> _getNotPredicate(Predicate<Context> predicate) {
		return predicate.negate();
	}

	private Predicate<Context> _getORPredicate(
		Predicate<Context> leftPredicate, Predicate<Context> rightPredicate) {

		return leftPredicate.or(rightPredicate);
	}

	private Optional<Predicate<Context>> _getPredicateOptional(
		BinaryExpression.Operation operation, Object left, Object right) {

		Predicate<Context> predicate = null;

		if (Objects.equals(BinaryExpression.Operation.AND, operation)) {
			predicate = _getANDPredicate(
				(Predicate<Context>)left, (Predicate<Context>)right);
		}
		else if (Objects.equals(BinaryExpression.Operation.EQ, operation)) {
			predicate = _getEQPredicate((EntityField)left, right);
		}
		else if (Objects.equals(BinaryExpression.Operation.GE, operation)) {
			predicate = _getGEPredicate((EntityField)left, right);
		}
		else if (Objects.equals(BinaryExpression.Operation.GT, operation)) {
			predicate = _getGTPredicate((EntityField)left, right);
		}
		else if (Objects.equals(BinaryExpression.Operation.LE, operation)) {
			predicate = _getLEPredicate((EntityField)left, right);
		}
		else if (Objects.equals(BinaryExpression.Operation.LT, operation)) {
			predicate = _getLTPredicate((EntityField)left, right);
		}
		else if (Objects.equals(BinaryExpression.Operation.OR, operation)) {
			predicate = _getORPredicate(
				(Predicate<Context>)left, (Predicate<Context>)right);
		}
		else {
			return Optional.empty();
		}

		return Optional.of(predicate);
	}

	private Object _normalizeStringLiteral(String literal) {
		literal = StringUtil.toLowerCase(literal);

		literal = StringUtil.unquote(literal);

		return StringUtil.replace(
			literal, StringPool.DOUBLE_APOSTROPHE, StringPool.APOSTROPHE);
	}

	private final EntityModel _entityModel;

}