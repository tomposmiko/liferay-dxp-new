import DateInput from 'shared/components/DateInput';
import DateRangeInput, {DateRange} from 'shared/components/DateRangeInput';
import DurationInput from 'shared/components/DurationInput';
import Form from 'shared/components/form';
import getCN from 'classnames';
import Input from 'shared/components/Input';
import NumberInput from '../NumberInput';
import React from 'react';
import {BetweenNumber} from '../BetweenNumberInput';
import {BOOLEAN_OPTIONS} from 'event-analysis/utils/utils';
import {ClaySelectWithOption} from '@clayui/select';
import {createOption, validateAttributeValue} from './utils';
import {Criterion} from '../../../utils/types';
import {DataTypes} from 'event-analysis/utils/types';
import {FunctionalOperators} from '../../../utils/constants';
import {isValid} from '../../../utils/utils';

interface IValueInputProps {
	dataType: DataTypes;
	onChange: (params: {
		criterion?: Criterion;
		touched?: {attributeValue: boolean};
		valid?: {attributeValue: boolean};
	}) => void;
	operatorName: Criterion['operatorName'];
	touched: boolean;
	valid: boolean;
	value: string | number | BetweenNumber | DateRange;
}

const ValueInput: React.FC<IValueInputProps> = ({
	dataType,
	onChange,
	operatorName,
	touched,
	valid,
	value
}) => {
	const handleAttributeValueBlur = () => {
		onChange({
			touched: {attributeValue: true}
		});
	};

	switch (dataType) {
		case DataTypes.Boolean:
			return (
				<ClaySelectWithOption
					className='boolean-input'
					data-testid='attribute-value-boolean-input'
					onBlur={handleAttributeValueBlur}
					onChange={event => {
						const {value} = event.target;

						onChange({
							criterion: {value},
							touched: {attributeValue: true},
							valid: {
								attributeValue: validateAttributeValue(
									value,
									dataType
								)
							}
						});
					}}
					options={BOOLEAN_OPTIONS.map(option =>
						createOption(option, dataType)
					)}
				/>
			);
		case DataTypes.Date:
			if (operatorName === FunctionalOperators.Between) {
				return (
					<Form.GroupItem
						className={getCN({
							'has-error': !valid && touched
						})}
						shrink
					>
						<DateRangeInput
							onBlur={handleAttributeValueBlur}
							onChange={value => {
								onChange({
									criterion: {
										value
									},
									touched: {attributeValue: true},
									valid: {
										attributeValue:
											!!value.end && !!value.start
									}
								});
							}}
							overlayAlignment='rightCenter'
							value={value as DateRange}
						/>
					</Form.GroupItem>
				);
			}

			return (
				<Form.GroupItem
					className={getCN({
						'has-error': !valid && touched
					})}
					shrink
				>
					<DateInput
						onDateInputBlur={handleAttributeValueBlur}
						onDateInputChange={value => {
							onChange({
								criterion: {
									value
								},
								touched: {attributeValue: true},
								valid: {
									attributeValue: validateAttributeValue(
										value,
										dataType
									)
								}
							});
						}}
						overlayAlignment='rightCenter'
						value={value}
					/>
				</Form.GroupItem>
			);
		case DataTypes.Duration:
			return (
				<Form.GroupItem
					className={getCN({
						'has-error': !valid && touched
					})}
					shrink
				>
					<DurationInput
						onChange={value => {
							onChange({
								criterion: {
									value
								},
								touched: {attributeValue: true},
								valid: {attributeValue: isValid(value)}
							});
						}}
						value={value as string}
					/>
				</Form.GroupItem>
			);
		case DataTypes.Number:
			return (
				<NumberInput
					onChange={({touched, valid, value}) =>
						onChange({
							criterion: {value},
							touched: {attributeValue: touched},
							valid: {attributeValue: valid}
						})
					}
					operatorName={operatorName}
					touched={touched}
					valid={valid}
					value={value}
				/>
			);
		case DataTypes.String:
		default:
			return (
				<Form.GroupItem
					className={getCN({
						'has-error': !valid && touched
					})}
					shrink
				>
					<Input
						data-testid='attribute-value-string-input'
						onBlur={handleAttributeValueBlur}
						onChange={event => {
							const {value} = event.target;

							onChange({
								criterion: {value},
								touched: {attributeValue: true},
								valid: {
									attributeValue: validateAttributeValue(
										value,
										dataType
									)
								}
							});
						}}
						value={value}
					/>
				</Form.GroupItem>
			);
	}
};

export default ValueInput;
