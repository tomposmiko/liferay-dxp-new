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

/**
 * Since json-loader isn't supported in 7.3.x, this is a copy of
 * sxp-query-element.schema.json as a JavaScript object so it can be imported
 * by another JavaScript file.
 */
export default {
	$id: 'sxp-query-element.schema.json',
	$schema: 'http://json-schema.org/draft-07/schema#',
	definitions: {
		Clause: {
			properties: {
				additive: {
					type: 'boolean',
				},
				boost: {
					type: 'number',
				},
				context: {
					type: 'string',
				},
				disabled: {
					type: 'boolean',
				},
				field: {
					type: 'string',
				},
				name: {
					type: 'string',
				},
				occur: {
					type: 'string',
				},
				parent: {
					type: 'string',
				},
				query: {
					type: 'object',
				},
				type: {
					type: 'string',
				},
				value: {
					type: 'string',
				},
			},
			type: 'object',
		},
		Condition: {
			properties: {
				allConditions: {
					items: {
						$ref: '#/definitions/Condition',
					},
					type: 'array',
				},
				anyConditions: {
					items: {
						$ref: '#/definitions/Condition',
					},
					type: 'array',
				},
				contains: {
					$ref: '#/definitions/Contains',
				},
				equals: {
					$ref: '#/definitions/Equals',
				},
				exists: {
					$ref: '#/definitions/Exists',
				},
				in: {
					$ref: '#/definitions/In',
				},
				not: {
					$ref: '#/definitions/Condition',
				},
				range: {
					$ref: '#/definitions/Range',
				},
			},
			type: 'object',
		},
		Configuration: {
			properties: {
				queryConfiguration: {
					$ref: '#/definitions/QueryConfiguration',
				},
			},
			type: 'object',
		},
		Contains: {
			properties: {
				parameterName: {
					type: 'string',
				},
				value: {
					type: 'object',
				},
			},
			type: 'object',
		},
		ElementDefinition: {
			properties: {
				category: {
					type: 'string',
				},
				configuration: {
					$ref: '#/definitions/Configuration',
				},
				icon: {
					type: 'string',
				},
				uiConfiguration: {
					$ref: '#/definitions/UiConfiguration',
				},
			},
			type: 'object',
		},
		Equals: {
			properties: {
				format: {
					type: 'string',
				},
				parameterName: {
					type: 'string',
				},
				value: {
					type: 'object',
				},
			},
			type: 'object',
		},
		Exists: {
			properties: {
				parameterName: {
					type: 'string',
				},
			},
			type: 'object',
		},
		Field: {
			properties: {
				fieldMappings: {
					items: {
						$ref: '#/definitions/FieldMapping',
					},
					type: 'array',
				},
				helpText: {
					type: 'string',
				},
				label: {
					type: 'string',
				},
				name: {
					type: 'string',
				},
				step: {
					type: 'object',
				},
				type: {
					type: 'string',
				},
				typeOptions: {
					$ref: '#/definitions/TypeOptions',
				},
			},
			type: 'object',
		},
		FieldMapping: {
			properties: {
				boost: {
					type: 'number',
				},
				field: {
					type: 'string',
				},
				locale: {
					type: 'string',
				},
			},
			type: 'object',
		},
		FieldSet: {
			properties: {
				fields: {
					items: {
						$ref: '#/definitions/Field',
					},
					type: 'array',
				},
			},
			type: 'object',
		},
		In: {
			properties: {
				parameterName: {
					type: 'string',
				},
				value: {
					type: 'object',
				},
			},
			type: 'object',
		},
		Option: {
			properties: {
				label: {
					type: 'string',
				},
				value: {
					type: 'string',
				},
			},
			type: 'object',
		},
		QueryConfiguration: {
			properties: {
				queryEntries: {
					items: {
						$ref: '#/definitions/QueryEntry',
					},
					type: 'array',
				},
			},
			type: 'object',
		},
		QueryEntry: {
			properties: {
				clauses: {
					items: {
						$ref: '#/definitions/Clause',
					},
					type: 'array',
				},
				condition: {
					$ref: '#/definitions/Condition',
				},
				enabled: {
					type: 'boolean',
				},
				postFilterClauses: {
					items: {
						$ref: '#/definitions/Clause',
					},
					type: 'array',
				},
				rescores: {
					items: {
						$ref: '#/definitions/Rescore',
					},
					type: 'array',
				},
			},
			type: 'object',
		},
		Range: {
			properties: {
				format: {
					type: 'string',
				},
				lte: {
					type: 'object',
				},
				parameterName: {
					type: 'string',
				},
			},
			type: 'object',
		},
		Rescore: {
			properties: {
				query: {
					type: 'object',
				},
				queryWeight: {
					type: 'number',
				},
				rescoreQueryWeight: {
					type: 'number',
				},
				scoreMode: {
					type: 'string',
				},
				windowSize: {
					type: 'integer',
				},
			},
			type: 'object',
		},
		TypeOptions: {
			properties: {
				boost: {
					type: 'boolean',
				},
				format: {
					type: 'string',
				},
				nullable: {
					type: 'boolean',
				},
				options: {
					items: {
						$ref: '#/definitions/Option',
					},
					type: 'array',
				},
				required: {
					type: 'boolean',
				},
				step: {
					type: 'object',
				},
				unit: {
					type: 'string',
				},
				unitSuffix: {
					type: 'string',
				},
			},
			type: 'object',
		},
		UiConfiguration: {
			properties: {
				fieldSets: {
					items: {
						$ref: '#/definitions/FieldSet',
					},
					type: 'array',
				},
			},
			type: 'object',
		},
	},
	properties: {
		description_i18n: {
			additionalProperties: {
				type: 'string',
			},
			type: 'object',
		},
		elementDefinition: {
			$ref: '#/definitions/ElementDefinition',
		},
		title_i18n: {
			additionalProperties: {
				type: 'string',
			},
			type: 'object',
		},
	},
	type: 'object',
};
