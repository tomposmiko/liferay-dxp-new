import React from 'react';
import Row from '../Row';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('BasePage.Row', () => {
	afterEach(cleanup);

	it('renders Row', () => {
		const {container} = render(<Row>{'Test Test'}</Row>);

		expect(container).toMatchSnapshot();
	});
});
