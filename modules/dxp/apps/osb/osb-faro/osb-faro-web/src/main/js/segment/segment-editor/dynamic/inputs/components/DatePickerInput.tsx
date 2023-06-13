import DateInput from 'shared/components/DateInput';
import DateRangeInput, {DateRange} from 'shared/components/DateRangeInput';
import getCN from 'classnames';
import React from 'react';

interface IDatePickerInputProps {
	isRange?: boolean;
	onBlur: () => void;
	onChange: (param: string | DateRange) => void;
	touched: boolean;
	valid: boolean;
	value: string | DateRange;
}

const DatePickerInput: React.FC<IDatePickerInputProps> = ({
	isRange,
	onBlur,
	onChange,
	touched,
	valid,
	value
}) => {
	const classNames = getCN({
		'has-error': !valid && touched
	});

	return (
		<>
			{isRange ? (
				<DateRangeInput
					className={classNames}
					onBlur={onBlur}
					onChange={onChange as (param: DateRange) => void}
					value={value as DateRange}
				/>
			) : (
				<DateInput
					className={classNames}
					onDateInputBlur={onBlur}
					onDateInputChange={onChange}
					readOnly
					value={value}
				/>
			)}
		</>
	);
};

export default DatePickerInput;
