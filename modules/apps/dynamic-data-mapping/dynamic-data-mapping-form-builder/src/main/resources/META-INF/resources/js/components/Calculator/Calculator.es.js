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

import 'clay-dropdown';
import Component from 'metal-component';
import Soy from 'metal-soy';
import {Config} from 'metal-state';

import Token from '../../expressions/Token.es';
import Tokenizer from '../../expressions/Tokenizer.es';
import templates from './Calculator.soy';

/**
 * Calculator.
 * @extends Component
 */

class Calculator extends Component {
	addTokenToExpression(tokenType, tokenValue) {
		const {expression, index} = this;
		const newToken = new Token(tokenType, tokenValue);
		const tokens = Tokenizer.tokenize(expression);

		if (this.shouldAddImplicitMultiplication(tokens, newToken)) {
			tokens.push(new Token(Token.OPERATOR, '*'));
		}

		tokens.push(newToken);

		this.setState({
			expression: Tokenizer.stringifyTokens(tokens),
		});

		this.emit('editExpression', {
			expression: this.expression,
			index,
		});
	}

	_isDotDisabled(tokens) {
		if (
			tokens.length === 0 ||
			(tokens.length > 0 &&
				tokens[tokens.length - 1].type !== Token.LITERAL)
		) {
			return true;
		}

		return false;
	}

	_isOperatorDisabled(tokens) {
		if (
			tokens.length > 0 &&
			tokens[tokens.length - 1].type === Token.OPERATOR
		) {
			return true;
		}

		return false;
	}

	getStateBasedOnExpression(expression) {
		const tokens = Tokenizer.tokenize(expression);

		const disableDot = this._isDotDisabled(tokens);
		const disableOperators = this._isOperatorDisabled(tokens);

		return {
			disableDot,
			disableFunctions: false,
			disableNumbers: false,
			disableOperators,
			showOnlyRepeatableFields: false,
		};
	}

	prepareStateForRender(state) {
		const {expression} = state;

		return {
			...state,
			...this.getStateBasedOnExpression(expression),
			expression: expression.replace(/[[\]]/g, ''),
			placeholder: Liferay.Browser.isIe()
				? ''
				: Liferay.Language.get('the-expression-will-be-displayed-here'),
		};
	}

	removeTokenFromExpression() {
		const {expression, index} = this;
		const tokens = Tokenizer.tokenize(expression);

		const removedToken = tokens.pop();

		if (
			removedToken &&
			removedToken.type === Token.LEFT_PARENTHESIS &&
			tokens.length &&
			tokens[tokens.length - 1].type === Token.FUNCTION
		) {
			tokens.pop();
		}

		this.setState({
			expression: Tokenizer.stringifyTokens(tokens),
		});

		this.emit('editExpression', {
			expression: this.expression,
			index,
		});
	}

	shouldAddImplicitMultiplication(tokens, newToken) {
		const lastToken = tokens[tokens.length - 1];

		return (
			lastToken !== undefined &&
			((newToken.type === Token.LEFT_PARENTHESIS &&
				lastToken.type !== Token.OPERATOR &&
				lastToken.type !== Token.FUNCTION &&
				lastToken.type !== Token.LEFT_PARENTHESIS) ||
				(newToken.type === Token.FUNCTION &&
					lastToken.type !== Token.OPERATOR &&
					lastToken.type !== Token.LEFT_PARENTHESIS) ||
				(newToken.type === Token.VARIABLE &&
					(lastToken.type === Token.VARIABLE ||
						lastToken.type === Token.LITERAL)) ||
				(newToken.type === Token.LITERAL &&
					(lastToken.type === Token.VARIABLE ||
						lastToken.type === Token.FUNCTION))) &&
			this._isSumAction(tokens) !== true
		);
	}

	_handleButtonClicked({delegateTarget}) {
		const {tokenType, tokenValue} = delegateTarget.dataset;

		if (tokenValue === 'backspace') {
			this.removeTokenFromExpression();
		}
		else {
			this.addTokenToExpression(tokenType, tokenValue);
		}
	}

	_handleFieldsDropdownItemClicked({data}) {
		const {item} = data;
		const {fieldName} = item;

		this.addTokenToExpression(Token.VARIABLE, fieldName);
	}

	_handleFunctionsDropdownItemClicked({data}) {
		const {item} = data;

		this.addTokenToExpression(Token.FUNCTION, item.value);
		this.addTokenToExpression(Token.LEFT_PARENTHESIS, '(');
	}

	_isSumAction(token) {
		return (
			token.length > 1 &&
			token[token.length - 1].type === Token.LEFT_PARENTHESIS &&
			token[token.length - 2].type === Token.FUNCTION &&
			token[token.length - 2].value === 'sum'
		);
	}

	_repeatableFieldsValueFn() {
		const {fields} = this;

		return fields.filter(({repeatable}) => repeatable === true);
	}
}

Calculator.STATE = {
	expression: Config.string().value(''),

	fields: Config.arrayOf(
		Config.shapeOf({
			fieldName: Config.string(),
			label: Config.string(),
			repeatable: Config.bool(),
			value: Config.string(),
		})
	).value([]),

	functions: Config.array().value([]),

	index: Config.number().value(0),

	repeatableFields: Config.array().valueFn('_repeatableFieldsValueFn'),

	spritemap: Config.string().required(),
};

Soy.register(Calculator, templates);

export default Calculator;
