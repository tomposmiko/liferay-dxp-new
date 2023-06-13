import Card from 'shared/components/Card';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import React from 'react';
import {compose} from 'redux';
import {withRangeKey} from 'shared/hoc';
import {WithRangeKeyProps} from 'shared/hoc/WithRangeKey';

interface ICardWithRangeKeyProps
	extends WithRangeKeyProps,
		React.HTMLAttributes<HTMLElement> {
	children: (val) => React.ReactNode;
	label: string;
	legacyDropdownRangeKey: boolean;
}

const CardWithRangeKey = compose(withRangeKey)(
	({
		children,
		className,
		label,
		legacyDropdownRangeKey = true,
		onRangeSelectorsChange,
		rangeSelectors,
		...otherProps
	}: ICardWithRangeKeyProps) => (
		<Card className={className}>
			<Card.Header className='align-items-center d-flex justify-content-between'>
				<Card.Title>{label}</Card.Title>

				<DropdownRangeKey
					legacy={legacyDropdownRangeKey}
					onChange={onRangeSelectorsChange}
					rangeSelectors={rangeSelectors}
				/>
			</Card.Header>

			{children({...otherProps, rangeSelectors})}
		</Card>
	)
);

export default CardWithRangeKey;
