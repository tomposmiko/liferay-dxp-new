import ModalInfoBar from '../ModalInfoBar';
import React from 'react';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('ModalInfoBar', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<ModalInfoBar children={['Test Info']} />);
		expect(container).toMatchSnapshot();
	});
});
