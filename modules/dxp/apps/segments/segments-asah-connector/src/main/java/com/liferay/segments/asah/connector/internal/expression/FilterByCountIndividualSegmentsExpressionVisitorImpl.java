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

package com.liferay.segments.asah.connector.internal.expression;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.segments.asah.connector.internal.expression.parser.IndividualSegmentsExpressionBaseVisitor;
import com.liferay.segments.asah.connector.internal.expression.parser.IndividualSegmentsExpressionParser;

import java.util.Objects;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * @author Cristina González
 */
public class FilterByCountIndividualSegmentsExpressionVisitorImpl
	extends IndividualSegmentsExpressionBaseVisitor<Object> {

	@Override
	public FilterByCount visitAndExpression(
		@NotNull IndividualSegmentsExpressionParser.AndExpressionContext
			andExpressionContext) {

		ParseTree leftParseTree = andExpressionContext.getChild(0);

		Object left = leftParseTree.accept(this);

		ParseTree rightParseTree = andExpressionContext.getChild(2);

		Object right = rightParseTree.accept(this);

		if ((left instanceof FilterByCount.Event) &&
			(right instanceof FilterByCount.Day)) {

			return new FilterByCount(
				(FilterByCount.Day)right, (FilterByCount.Event)left);
		}

		throw new UnsupportedOperationException(
			StringBundler.concat(
				"Trying to create a filter by count expression with  ",
				left.getClass(), " and ", right.getClass()));
	}

	@Override
	public Object visitChildren(@NotNull RuleNode ruleNode) {
		Object result = defaultResult();

		for (int i = 0; i < ruleNode.getChildCount(); i++) {
			if (!shouldVisitNextChild(ruleNode, result)) {
				break;
			}

			ParseTree parseTree = ruleNode.getChild(i);

			Object object = parseTree.accept(this);

			result = aggregateResult(result, object);
		}

		return result;
	}

	@Override
	public Object visitEqualsExpression(
		@NotNull IndividualSegmentsExpressionParser.EqualsExpressionContext
			equalsExpressionContext) {

		ParseTree leftParseTree = equalsExpressionContext.getChild(0);

		Object value = leftParseTree.accept(this);

		if (Objects.equals("activityKey", value)) {
			ParseTree rightParseTree = equalsExpressionContext.getChild(2);

			String eventString = (String)rightParseTree.accept(this);

			String[] parts = eventString.split(StringPool.POUND);

			if (parts.length != 3) {
				throw new UnsupportedOperationException(
					"invalid event " + eventString);
			}

			return new FilterByCount.Event(parts[1], parts[2]);
		}
		else if (Objects.equals("day", value)) {
			return _getFilterByCountDay(
				leftParseTree, equalsExpressionContext.getChild(1),
				equalsExpressionContext.getChild(2));
		}

		throw new UnsupportedOperationException(
			"Unsupported operation with value " + value);
	}

	@Override
	public FilterByCount.Day visitFunctionCallExpression(
		@NotNull
			IndividualSegmentsExpressionParser.FunctionCallExpressionContext
				functionCallExpressionContext) {

		ParseTree parseTree1 = functionCallExpressionContext.getChild(0);

		Object functionName = parseTree1.accept(this);

		if (Objects.equals(Function.BETWEEN.getValue(), functionName)) {
			ParseTree parseTree2 = functionCallExpressionContext.getChild(2);

			String[] parameters = (String[])parseTree2.accept(this);

			if (parameters.length != 3) {
				throw new UnsupportedOperationException(
					StringBundler.concat(
						"Unsupported function between ", parameters.length,
						" params"));
			}

			if (!Objects.equals("day", parameters[0])) {
				throw new UnsupportedOperationException(
					"Unsupported function between with first param " +
						parameters[0]);
			}

			return new FilterByCount.Day(
				Function.BETWEEN.getValue(), parameters[1], parameters[2]);
		}

		throw new UnsupportedOperationException(
			"Unsupported function " + functionName);
	}

	@Override
	public String[] visitFunctionParameters(
		@NotNull IndividualSegmentsExpressionParser.FunctionParametersContext
			functionParametersContext) {

		String[] parameters =
			new String[(functionParametersContext.getChildCount() / 2) + 1];

		int j = 0;

		for (int i = 0; i < functionParametersContext.getChildCount();
			 i = i + 2) {

			ParseTree parseTree = functionParametersContext.getChild(i);

			parameters[j++] = String.valueOf(parseTree.accept(this));
		}

		return parameters;
	}

	@Override
	public FilterByCount.Day visitGreaterThanExpression(
		@NotNull IndividualSegmentsExpressionParser.GreaterThanExpressionContext
			greaterThanExpressionContext) {

		return _getFilterByCountDay(
			greaterThanExpressionContext.getChild(0),
			greaterThanExpressionContext.getChild(1),
			greaterThanExpressionContext.getChild(2));
	}

	@Override
	public FilterByCount.Day visitGreaterThanOrEqualsExpression(
		@NotNull
			IndividualSegmentsExpressionParser.
				GreaterThanOrEqualsExpressionContext
					greaterThanOrEqualsExpressionContext) {

		return _getFilterByCountDay(
			greaterThanOrEqualsExpressionContext.getChild(0),
			greaterThanOrEqualsExpressionContext.getChild(1),
			greaterThanOrEqualsExpressionContext.getChild(2));
	}

	@Override
	public FilterByCount.Day visitLessThanExpression(
		@NotNull IndividualSegmentsExpressionParser.LessThanExpressionContext
			lessThanExpressionContext) {

		return _getFilterByCountDay(
			lessThanExpressionContext.getChild(0),
			lessThanExpressionContext.getChild(1),
			lessThanExpressionContext.getChild(2));
	}

	@Override
	public FilterByCount.Day visitLessThanOrEqualsExpression(
		@NotNull
			IndividualSegmentsExpressionParser.LessThanOrEqualsExpressionContext
				lessThanOrEqualsExpressionContext) {

		return _getFilterByCountDay(
			lessThanOrEqualsExpressionContext.getChild(0),
			lessThanOrEqualsExpressionContext.getChild(1),
			lessThanOrEqualsExpressionContext.getChild(2));
	}

	@Override
	public String visitTerminal(TerminalNode terminalNode) {
		if (Objects.equals(terminalNode.getText(), "<EOF>")) {
			return null;
		}

		return _normalizeStringLiteral(terminalNode.getText());
	}

	@Override
	public FilterByCount visitToLogicalAndExpression(
		@NotNull
			IndividualSegmentsExpressionParser.ToLogicalAndExpressionContext
				toLogicalAndExpressionContext) {

		Object object = visitChildren(toLogicalAndExpressionContext);

		if (object instanceof FilterByCount.Event) {
			return new FilterByCount(null, (FilterByCount.Event)object);
		}

		return (FilterByCount)object;
	}

	public static class FilterByCount {

		public FilterByCount(Day day, Event event) {
			_day = day;
			_event = event;
		}

		public Day getDay() {
			return _day;
		}

		public Event getEvent() {
			return _event;
		}

		public JSONObject toJSONObject() {
			if (_event != null) {
				JSONObject jsonObject = _event.toJSONObject();

				if (_day != null) {
					jsonObject.put("day", _day.toJSONObject());
				}

				return jsonObject;
			}

			return null;
		}

		public static class Day {

			public Day(String operator, String... values) {
				_operator = operator;
				_values = values;
			}

			public String getOperator() {
				return _operator;
			}

			public String[] getValues() {
				return _values;
			}

			public JSONObject toJSONObject() {
				if (_values.length == 1) {
					return JSONUtil.put(
						"operatorName", _operator
					).put(
						"value", _values[0]
					);
				}

				return JSONUtil.put(
					"operatorName", _operator
				).put(
					"value",
					JSONUtil.put(
						"end", _values[1]
					).put(
						"start", _values[0]
					)
				);
			}

			private final String _operator;
			private final String[] _values;

		}

		public static class Event {

			public Event(String event, String assetId) {
				_event = event;
				_assetId = assetId;
			}

			public String getAssetId() {
				return _assetId;
			}

			public String getEvent() {
				return _event;
			}

			public JSONObject toJSONObject() {
				return JSONUtil.put(
					"assetId", _assetId
				).put(
					"propertyName", _event
				);
			}

			private final String _assetId;
			private String _event;

		}

		private final Day _day;
		private Event _event;

	}

	public enum Function {

		BETWEEN("between");

		public String getValue() {
			return _value;
		}

		@Override
		public String toString() {
			return _value;
		}

		private Function(String value) {
			_value = value;
		}

		private final String _value;

	}

	@Override
	protected Object aggregateResult(Object query, Object object) {
		if (query == null) {
			return object;
		}
		else if (object == null) {
			return query;
		}

		return object;
	}

	private FilterByCount.Day _getFilterByCountDay(
		ParseTree leftParseTree, ParseTree operatorParseTree,
		ParseTree rightParseTree) {

		Object value = leftParseTree.accept(this);

		if (!Objects.equals("day", value)) {
			throw new UnsupportedOperationException(
				"Unsupported operation with value " + value);
		}

		return new FilterByCount.Day(
			(String)operatorParseTree.accept(this),
			(String)rightParseTree.accept(this));
	}

	private String _normalizeStringLiteral(String literal) {
		literal = StringUtil.unquote(literal);

		return StringUtil.replace(
			literal, StringPool.DOUBLE_APOSTROPHE, StringPool.APOSTROPHE);
	}

}