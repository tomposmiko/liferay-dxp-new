import React from 'react';
import SearchableList from '../SearchableList';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('BaseDropdownSearchableList', () => {
	const WrappedComponent = props => (
		<SearchableList
			items={[
				{
					displayName: 'Test 0',
					id: '0',
					name: 'testName',
					type: 'custom'
				},
				{
					displayName: 'Test 1',
					id: '1',
					name: 'testName1',
					type: 'default'
				}
			]}
			onEditClick={jest.fn()}
			onItemClick={jest.fn()}
			onItemOptionsClick={jest.fn()}
			onQueryChange={jest.fn()}
			query=''
			{...props}
		/>
	);

	it('should render', () => {
		const {container} = render(<WrappedComponent />);

		expect(container.querySelector('.active')).toBeNull();
		expect(container.querySelector('.disabled')).toBeNull();
		expect(container).toMatchSnapshot();
	});

	it('should render with query "1"', () => {
		const {queryByText} = render(<WrappedComponent query='Test 1' />);

		expect(queryByText('Test 1')).toBeTruthy();
		expect(queryByText('Test 0')).toBeNull();
	});

	it('should render with an active item', () => {
		const {container} = render(<WrappedComponent activeId='0' />);

		expect(container.querySelector('.disabled')).toBeNull();
		expect(container.querySelector('.active')).toBeTruthy();
	});

	it('should render with 2 disabled items', () => {
		const {container} = render(
			<WrappedComponent disabledIds={['0', '1']} />
		);

		expect(container.querySelectorAll('.disabled').length).toBe(2);
		expect(container.querySelector('.active')).toBeNull();
	});
});
