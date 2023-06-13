import React from 'react';
import SegmentSticker from '../SegmentSticker';
import {render} from '@testing-library/react';
import {SegmentStates, SegmentTypes} from 'shared/util/constants';

jest.unmock('react-dom');

describe('SegmentSticker', () => {
	it('should render', () => {
		const {container} = render(
			<SegmentSticker segmentType={SegmentTypes.Static} />
		);
		expect(container).toMatchSnapshot();
	});

	it('should render with a dynamic segment icon', () => {
		const {container} = render(
			<SegmentSticker segmentType={SegmentTypes.Dynamic} />
		);

		expect(container.querySelector('use')).toHaveAttribute(
			'xlink:href',
			'#individual-dynamic-segment'
		);
	});

	it('should render with a disabled segment icon', () => {
		const {container} = render(
			<SegmentSticker state={SegmentStates.Disabled} />
		);

		expect(container.querySelector('use')).toHaveAttribute(
			'xlink:href',
			'#warning'
		);
	});
});
