import * as data from 'test/data';
import AttributeConjunctionDisplay from '../AttributeConjunctionDisplay';
import React from 'react';
import {DataTypes} from 'event-analysis/utils/types';
import {
	FunctionalOperators,
	NotOperators,
	RelationalOperators
} from 'segment/segment-editor/dynamic/utils/constants';
import {ReferencedObjectsProvider} from 'segment/segment-editor/dynamic/context/referencedObjects';
import {render} from '@testing-library/react';
import {Segment} from 'shared/util/records';

jest.unmock('react-dom');

const mockSegment = data.getImmutableMock(Segment, data.mockSegment, 0, {
	referencedObjects: {
		attributes: {
			1: {
				dataType: DataTypes.Boolean,
				displayName: 'Foo Attribute Boolean',
				id: '1'
			},
			2: {
				dataType: DataTypes.String,
				displayName: 'Foo Attribute String',
				id: '2'
			},
			3: {
				dataType: DataTypes.Number,
				displayName: 'Foo Attribute Number',
				id: '3'
			},
			4: {
				dataType: DataTypes.Date,
				displayName: 'Foo Attribute Date',
				id: '4'
			},
			5: {
				dataType: DataTypes.Duration,
				displayName: 'Foo Attribute Duration',
				id: '5'
			}
		}
	}
});

const DefaultComponent = props => (
	<ReferencedObjectsProvider segment={mockSegment}>
		<AttributeConjunctionDisplay {...props} />
	</ReferencedObjectsProvider>
);

describe('AttributeConjunctionDisplay', () => {
	it('should render', () => {
		const {container} = render(
			<DefaultComponent
				conjunctionCriterion={{
					operatorName: RelationalOperators.EQ,
					propertyName: 'attribute/2',
					value: 'Test'
				}}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it.each`
		operatorName                    | propertyName     | value                                       | displayName                 | operatorLabel        | displayValue
		${RelationalOperators.EQ}       | ${'attribute/1'} | ${'false'}                                  | ${'Foo Attribute Boolean'}  | ${'is'}              | ${'False'}
		${RelationalOperators.EQ}       | ${'attribute/1'} | ${'true'}                                   | ${'Foo Attribute Boolean'}  | ${'is'}              | ${'True'}
		${RelationalOperators.EQ}       | ${'attribute/2'} | ${'Test'}                                   | ${'Foo Attribute String'}   | ${'is'}              | ${'"Test"'}
		${RelationalOperators.NE}       | ${'attribute/2'} | ${'Test'}                                   | ${'Foo Attribute String'}   | ${'is not'}          | ${'"Test"'}
		${FunctionalOperators.Contains} | ${'attribute/2'} | ${'Test'}                                   | ${'Foo Attribute String'}   | ${'contains'}        | ${'"Test"'}
		${NotOperators.NotContains}     | ${'attribute/2'} | ${'Test'}                                   | ${'Foo Attribute String'}   | ${'not contains'}    | ${'"Test"'}
		${RelationalOperators.GT}       | ${'attribute/3'} | ${123}                                      | ${'Foo Attribute Number'}   | ${'is greater than'} | ${'123'}
		${RelationalOperators.LT}       | ${'attribute/3'} | ${321}                                      | ${'Foo Attribute Number'}   | ${'is less than'}    | ${'321'}
		${FunctionalOperators.Between}  | ${'attribute/3'} | ${{end: 321, start: 1}}                     | ${'Foo Attribute Number'}   | ${'between'}         | ${'1 - 321'}
		${RelationalOperators.GT}       | ${'attribute/4'} | ${'2020-12-05'}                             | ${'Foo Attribute Date'}     | ${'after'}           | ${'Dec 5, 2020'}
		${RelationalOperators.LT}       | ${'attribute/4'} | ${'2020-12-05'}                             | ${'Foo Attribute Date'}     | ${'before'}          | ${'Dec 5, 2020'}
		${FunctionalOperators.Between}  | ${'attribute/4'} | ${{end: '2020-12-11', start: '2020-12-01'}} | ${'Foo Attribute Date'}     | ${'is between'}      | ${'Dec 1, 2020 - Dec 11, 2020'}
		${RelationalOperators.GT}       | ${'attribute/5'} | ${3661000}                                  | ${'Foo Attribute Duration'} | ${'is greater than'} | ${'01:01:01'}
		${RelationalOperators.LT}       | ${'attribute/5'} | ${3661000}                                  | ${'Foo Attribute Duration'} | ${'is less than'}    | ${'01:01:01'}
	`(
		'should return $displayName $operatorLabel $displayValue when operatorName $operatorName, propertyName $propertyName, & value $value',
		({
			displayName,
			displayValue,
			operatorLabel,
			operatorName,
			propertyName,
			value
		}) => {
			const {queryByText} = render(
				<DefaultComponent
					conjunctionCriterion={{
						operatorName,
						propertyName,
						value
					}}
				/>
			);

			expect(queryByText(displayName)).toBeTruthy();
			expect(queryByText(operatorLabel)).toBeTruthy();
			expect(queryByText(displayValue)).toBeTruthy();
		}
	);
});
