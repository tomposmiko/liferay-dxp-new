import {
	fireEvent,
	getByLabelText,
	getByPlaceholderText,
	getByTestId,
	getByText,
	waitForElement
} from '@testing-library/react';

export const inputSearchText = (container, searchText) => {
	const searchBarInput = getByPlaceholderText(container, 'Search');
	fireEvent.change(searchBarInput, {
		target: {value: searchText}
	});
	fireEvent.keyDown(searchBarInput, {code: 13, key: 'Enter'});
	jest.runAllTimers();
};

export const selectAllAndToggle = container => {
	fireEvent.click(getByTestId(container, 'select-all-checkbox'));
	jest.runAllTimers();
	fireEvent.click(getByTestId(container, 'view-selected'));
	jest.runAllTimers();
};

export const selectFilterDropdownItem = (container, labelText) => {
	fireEvent.click(getByTestId(container, 'filter-and-order-button'));
	const overlay = getByTestId(document.body, 'overlay');
	fireEvent.click(getByLabelText(overlay, labelText));
	fireEvent.click(container);
	jest.runAllTimers();
};

export const waitForTable = async container => {
	await waitForElement(() => container.querySelector('.table-root'), {
		container
	});
};

export const waitForLoading = async container => {
	await waitForElement(() => container.querySelector('.spinner-root'));
};

export const selectDropdownItem = labelText => {
	const overlay = getByTestId(document.body, 'overlay');
	fireEvent.click(getByText(overlay, labelText));
	fireEvent.click(document.body);
	jest.runAllTimers();
};
