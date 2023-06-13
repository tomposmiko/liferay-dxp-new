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

export declare const CONJUNCTIONS: {
	readonly AND: 'and';
	readonly OR: 'or';
};
export declare const FUNCTIONAL_OPERATORS: {
	readonly CONTAINS: 'contains';
};
export declare const NOT_OPERATORS: {
	readonly NOT_CONTAINS: 'not-contains';
	readonly NOT_EQ: 'not-eq';
};
export declare const HAS_OPERATORS: {
	readonly HAS: 'has';
	readonly NOT_HAS: 'has-not';
};
export declare const RELATIONAL_OPERATORS: {
	readonly EQ: 'eq';
	readonly GE: 'ge';
	readonly GT: 'gt';
	readonly LE: 'le';
	readonly LT: 'lt';
};
export declare const DATE_OPERATORS: {
	readonly BETWEEN: 'between';
	readonly EVER: 'ever';
};
export declare const SINCE_VALUES: {
	readonly last7Days: string;
	readonly last24Hours: string;
	readonly last28Days: string;
	readonly last30Days: string;
	readonly last90Days: string;
	readonly yesterday: string;
};

/**
 * Constants to match property types in the passed in supportedProperties array.
 */
export declare const PROPERTY_TYPES: {
	readonly BOOLEAN: 'boolean';
	readonly COLLECTION: 'collection';
	readonly DATE: 'date';
	readonly DATE_TIME: 'date-time';
	readonly DOUBLE: 'double';
	readonly EVENT: 'event';
	readonly ID: 'id';
	readonly INTEGER: 'integer';
	readonly STRING: 'string';
};
export declare const PROPERTY_GROUPS: {
	readonly EVENT: 'event';
};
export declare const SUPPORTED_CONJUNCTIONS: readonly [
	{
		readonly label: string;
		readonly name: 'and';
	},
	{
		readonly label: string;
		readonly name: 'or';
	}
];
export declare const SUPPORTED_OPERATORS: readonly [
	{
		readonly label: string;
		readonly name: 'eq';
	},
	{
		readonly label: string;
		readonly name: 'not-eq';
	},
	{
		readonly label: string;
		readonly name: 'gt';
	},
	{
		readonly label: string;
		readonly name: 'ge';
	},
	{
		readonly label: string;
		readonly name: 'lt';
	},
	{
		readonly label: string;
		readonly name: 'le';
	},
	{
		readonly label: string;
		readonly name: 'contains';
	},
	{
		readonly label: string;
		readonly name: 'not-contains';
	}
];
export declare const SUPPORTED_EVENT_OPERATORS: readonly [
	{
		readonly label: string;
		readonly name: 'ge';
	},
	{
		readonly label: string;
		readonly name: 'le';
	},
	{
		readonly label: string;
		readonly name: 'has';
	},
	{
		readonly label: string;
		readonly name: 'has-not';
	}
];
export declare const SUPPORTED_EVENT_DATE_OPERATORS: readonly [
	{
		readonly label: string;
		readonly name: 'between';
	},
	{
		readonly label: string;
		readonly name: 'eq';
	},
	{
		readonly label: string;
		readonly name: 'ever';
	},
	{
		readonly label: string;
		readonly name: 'gt';
	},
	{
		readonly label: string;
		readonly name: 'lt';
	}
];
export declare const SUPPORTED_PROPERTY_TYPES: {
	readonly 'boolean': readonly ['eq', 'not-eq'];
	readonly 'collection': readonly [
		'eq',
		'not-eq',
		'contains',
		'not-contains'
	];
	readonly 'date': readonly ['eq', 'ge', 'gt', 'le', 'lt', 'not-eq'];
	readonly 'date-time': readonly ['eq', 'ge', 'gt', 'le', 'lt', 'not-eq'];
	readonly 'double': readonly ['eq', 'ge', 'gt', 'le', 'lt', 'not-eq'];
	readonly 'event': {
		readonly DATE: readonly ['between', 'eq', 'ever', 'gt', 'lt'];
		readonly INTEGER: readonly ['ge', 'le'];
		readonly NOT: readonly ['has', 'has-not'];
		readonly SINCE: readonly [
			string,
			string,
			string,
			string,
			string,
			string
		];
	};
	readonly 'id': readonly ['eq', 'not-eq'];
	readonly 'integer': readonly ['eq', 'ge', 'gt', 'le', 'lt', 'not-eq'];
	readonly 'string': readonly ['eq', 'not-eq', 'contains', 'not-contains'];
};

/**
 * Values for criteria row inputs.
 */
export declare const BOOLEAN_OPTIONS: readonly [
	{
		readonly label: string;
		readonly value: 'true';
	},
	{
		readonly label: string;
		readonly value: 'false';
	}
];
