import BooleanFilter from '../BooleanFilter';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('BooleanFilter', () => {
	it('should render', () => {
		const {container} = render(<BooleanFilter onSubmit={jest.fn()} />);

		expect(container).toMatchSnapshot();
	});
});
