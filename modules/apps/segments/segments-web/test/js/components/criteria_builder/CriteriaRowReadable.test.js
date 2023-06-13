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

import {render} from '@testing-library/react';
import React from 'react';

import CriteriaRowReadable from '../../../../src/main/resources/META-INF/resources/js/components/criteria_builder/CriteriaRowReadable';
import {dateToInternationalHuman} from '../../../../src/main/resources/META-INF/resources/js/utils/utils';

import '@testing-library/jest-dom/extend-expect';

import {
	booleanCriterion,
	booleanProperty,
	collectionCriterion,
	collectionProperty,
	dateCriterion,
	dateProperty,
	doubleCriterion,
	doubleProperty,
	entityCriterion,
	entityProperty,
	stringCriterion,
	stringProperty,
} from '../../mockData';

const equalsOperator = {label: 'Equals', name: 'eq'};

describe('CriteriaRowReadable', () => {
	it('renders string criterion', () => {
		const {getByText} = render(
			<CriteriaRowReadable
				criterion={stringCriterion}
				selectedOperator={equalsOperator}
				selectedProperty={stringProperty}
			/>
		);

		expect(getByText(stringProperty.label)).toBeInTheDocument();
		expect(getByText(equalsOperator.label)).toBeInTheDocument();
		expect(getByText(stringCriterion.value)).toBeInTheDocument();
	});

	it('renders boolean criterion', () => {
		const {getByText} = render(
			<CriteriaRowReadable
				criterion={booleanCriterion}
				selectedOperator={equalsOperator}
				selectedProperty={booleanProperty}
			/>
		);

		expect(getByText(booleanProperty.label)).toBeInTheDocument();
		expect(getByText(equalsOperator.label)).toBeInTheDocument();
		expect(getByText(booleanCriterion.value)).toBeInTheDocument();
	});

	it('renders date criterion', () => {
		const {getByText} = render(
			<CriteriaRowReadable
				criterion={dateCriterion}
				selectedOperator={equalsOperator}
				selectedProperty={dateProperty}
			/>
		);

		expect(getByText(dateProperty.label)).toBeInTheDocument();
		expect(getByText(equalsOperator.label)).toBeInTheDocument();
		expect(
			getByText(dateToInternationalHuman(dateCriterion.value))
		).toBeInTheDocument();
	});

	it('renders entity criterion', () => {
		const {getByText} = render(
			<CriteriaRowReadable
				criterion={entityCriterion}
				selectedOperator={equalsOperator}
				selectedProperty={entityProperty}
			/>
		);

		expect(getByText(entityProperty.label)).toBeInTheDocument();
		expect(getByText(equalsOperator.label)).toBeInTheDocument();
		expect(getByText(entityCriterion.displayValue)).toBeInTheDocument();
	});

	it('renders collection criterion', () => {
		const {getByText} = render(
			<CriteriaRowReadable
				criterion={collectionCriterion}
				selectedOperator={equalsOperator}
				selectedProperty={collectionProperty}
			/>
		);

		expect(getByText(collectionProperty.label)).toBeInTheDocument();
		expect(getByText(equalsOperator.label)).toBeInTheDocument();
		expect(getByText(collectionCriterion.value)).toBeInTheDocument();
	});

	it('renders double criterion', () => {
		const {getByText} = render(
			<CriteriaRowReadable
				criterion={doubleCriterion}
				selectedOperator={equalsOperator}
				selectedProperty={doubleProperty}
			/>
		);

		expect(getByText(doubleProperty.label)).toBeInTheDocument();
		expect(getByText(equalsOperator.label)).toBeInTheDocument();
		expect(getByText(doubleCriterion.value)).toBeInTheDocument();
	});
});
