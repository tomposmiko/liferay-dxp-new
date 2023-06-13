AUI.add(
	'liferay-ddm-form-builder-rule-validator',
	function(A) {
		var AObject = A.Object;

		var CONDITIONS_OPERATOR = {
			'belongs-to': 'binary',
			'contains': 'binary',
			'equals-to': 'binary',
			'greater-than': 'binary',
			'greater-than-equals': 'binary',
			'is-empty': 'unary',
			'less-than': 'binary',
			'less-than-equals': 'binary',
			'not-contains': 'binary',
			'not-equals-to': 'binary',
			'not-is-empty': 'unary'
		};

		var FormBuilderRuleValidator = A.Component.create(
			{
				ATTRS: {
				},

				AUGMENTS: [],

				NAME: 'liferay-ddm-form-builder-rule-validator',

				prototype: {
					isValidRule: function(rule) {
						var instance = this;

						var validConditions = instance._isValidConditions(rule.conditions);

						var validActions = instance._isValidActions(rule.actions);

						return validConditions && validActions;
					},

					_checkRequiredInputIsFilled: function(inputs, requiredInputs) {
						for (var input in inputs) {
							if (requiredInputs[input]) {
								return true;
							}
						}

						return false;
					},

					_isEmpty: function(content) {
						var instance = this;

						var empty = false;

						if (content === '') {
							empty = true;
						}

						return empty;
					},

					_isValidAction: function(action) {
						var instance = this;

						if (action.action === '') {
							return false;
						}

						return instance._validateAction(action);
					},

					_isValidActions: function(actions) {
						var instance = this;

						if (actions.length === 0) {
							return false;
						}

						for (var i = 0; i < actions.length; i++) {
							if (!instance._isValidAction(actions[i])) {
								return false;
							}
						}

						return true;
					},

					_isValidConditions: function(conditions) {
						var instance = this;

						if (conditions.length === 0) {
							return false;
						}

						for (var i = 0; i < conditions.length; i++) {
							if (!instance._isValidConditon(conditions[i])) {
								return false;
							}
						}

						return true;
					},

					_isValidConditon: function(condition) {
						var instance = this;

						var valid = false;

						if (condition.operands.length === 0) {
							valid = false;
						}

						if (!condition.operator) {
							valid = false;
						}

						var operatorType = CONDITIONS_OPERATOR[condition.operator];

						if (operatorType === 'unary') {
							valid = true;
						}

						if (operatorType === 'binary' && condition.operands.length == 2) {
							if (condition.operands[1].type && !instance._isEmpty(condition.operands[1].value)) {
								valid = true;
							}
						}

						return valid;
					},

					_validateAction: function(action) {
						var instance = this;

						var valid = false;

						if (action.action === 'show' | action.action === 'enable' | action.action === 'require' | action.action === 'jump-to-page') {
							valid = action.target;
						}
						else if (action.action === 'auto-fill') {
							var inputs = action.inputs;

							var outputs = action.outputs;

							var requiredInputs = action.requiredInputs;

							if (!AObject.isEmpty(requiredInputs)) {
								return !AObject.isEmpty(outputs) && !AObject.isEmpty(inputs) && instance._checkRequiredInputIsFilled(inputs, requiredInputs);
							}

							valid = !AObject.isEmpty(outputs);
						}
						else if (action.action === 'calculate') {
							valid = action.expression && action.target;
						}

						return valid;
					}
				}
			}
		);

		Liferay.namespace('DDM').FormBuilderRuleValidator = FormBuilderRuleValidator;
	},
	'',
	{
		requires: []
	}
);