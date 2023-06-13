import Form from 'shared/components/form';
import getCN from 'classnames';
import Input from 'shared/components/Input';
import React from 'react';
import {ClaySelectWithOption} from '@clayui/select';
import {Criterion} from '../../utils/types';
import {isValid} from '../../utils/utils';
import {OCCURENCE_OPTIONS} from '../../utils/constants';

const MAX_OCCURENCE_COUNT_LIMIT = 2147483647;

const isValidOccurenceCount = occurenceCount =>
	isValid(occurenceCount) &&
	occurenceCount >= 0 &&
	occurenceCount <= MAX_OCCURENCE_COUNT_LIMIT;

interface IOccurenceConjunctionInputProps {
	onChange: (params: {
		criterion?: Criterion;
		touched?: boolean;
		valid?: boolean;
	}) => void;
	operatorName: Criterion['operatorName'];
	touched: boolean;
	valid: boolean;
	value: number | string;
}

const OccurenceConjunctionInput: React.FC<IOccurenceConjunctionInputProps> = ({
	onChange,
	operatorName,
	touched,
	valid,
	value
}) => (
	<>
		<Form.GroupItem shrink>
			<ClaySelectWithOption
				className='operator-input'
				onChange={({target: {value}}) => {
					onChange({
						criterion: {
							operatorName: value as Criterion['operatorName']
						}
					});
				}}
				options={OCCURENCE_OPTIONS.map(({key, label}) => ({
					label,
					value: key
				}))}
				value={operatorName}
			/>
		</Form.GroupItem>

		<Form.GroupItem
			className={getCN({
				'has-error': !valid && touched
			})}
			shrink
		>
			<Input
				className='number-input'
				data-testid='occurence-count-input'
				min='0'
				onBlur={({target: {value}}) => {
					onChange({
						touched: true,
						valid: isValidOccurenceCount(value)
					});
				}}
				onChange={({target: {value}}) => {
					let numberVal: string | number = '';

					if (isValid(value)) {
						numberVal = parseInt(value);
					}

					onChange({
						criterion: {value: numberVal},
						touched: true,
						valid: isValidOccurenceCount(numberVal)
					});
				}}
				type='number'
				value={value}
			/>
		</Form.GroupItem>

		<Form.GroupItem className='unit' label shrink>
			{Liferay.Language.get('times')}
		</Form.GroupItem>
	</>
);

export default OccurenceConjunctionInput;
