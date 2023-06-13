const isRTL = document.documentElement.classList.contains('rtl');

const buttonElement = fragmentElement.querySelector('.btn');
const dropdownElement = fragmentElement.querySelector('.dropdown-menu');
const optionListElement = fragmentElement.querySelector('.list-unstyled');

const chooseOptionElement = document.getElementById(
	// eslint-disable-next-line no-undef
	`${fragmentEntryLinkNamespace}-choose-option-message`
);
const labelInputElement = document.getElementById(
	// eslint-disable-next-line no-undef
	`${fragmentEntryLinkNamespace}-label-input`
);
const loadingResultsElement = document.getElementById(
	// eslint-disable-next-line no-undef
	`${fragmentEntryLinkNamespace}-loading-results-message`
);
const noResultsElement = document.getElementById(
	// eslint-disable-next-line no-undef
	`${fragmentEntryLinkNamespace}-no-results-message`
);
const uiInputElement = document.getElementById(
	// eslint-disable-next-line no-undef
	`${fragmentEntryLinkNamespace}-select-from-list-input`
);
const valueInputElement = document.getElementById(
	// eslint-disable-next-line no-undef
	`${fragmentEntryLinkNamespace}-value-input`
);

if (layoutMode === 'edit') {
	buttonElement.setAttribute('disabled', true);
	uiInputElement.setAttribute('disabled', true);
}

buttonElement.addEventListener('click', toggleDropdown);
buttonElement.addEventListener('blur', handleResultListBlur);
uiInputElement.addEventListener('click', toggleDropdown);
uiInputElement.addEventListener('input', debounce(handleInputChange, 1000));
uiInputElement.addEventListener('blur', handleInputBlur);
uiInputElement.addEventListener('keydown', handleInputKeyDown);
optionListElement.addEventListener('click', handleResultListClick);

window.addEventListener('resize', handleWindowResizeOrScroll, {
	passive: true,
});
window.addEventListener('scroll', handleWindowResizeOrScroll, {
	passive: true,
});

const MAX_ITEMS = 10;

let lastSearchAbortController = new AbortController();
let lastSearchQuery = null;

const KEYS = {
	ArrowDown: 'ArrowDown',
	ArrowUp: 'ArrowUp',
	End: 'End',
	Enter: 'Enter',
	Home: 'Home',
};

const optionList = (input.attributes.options || []).map((option) => ({
	textContent: option.label,
	textValue: option.label.toLowerCase(),
	value: option.value,
}));

function handleResultListClick(event) {
	let selectedOptionElement = null;

	if (event.target.matches('.dropdown-item')) {
		selectedOptionElement = event.target;
	}
	else if (event.target.closest('.dropdown-item')) {
		selectedOptionElement = event.target.closest('.dropdown-item');
	}

	if (selectedOptionElement) {
		setFocusedOption(selectedOptionElement);
		setSelectedOption(selectedOptionElement);
	}
}

function handleInputBlur() {
	uiInputElement.value = labelInputElement.value;

	if (checkIsOpenDropdown()) {
		setTimeout(() => closeDropdown(), 500);
	}
}

function handleResultListBlur() {
	if (checkIsOpenDropdown()) {
		setTimeout(() => closeDropdown(), 500);
	}
}

function handleInputKeyDown(event) {
	if (!optionListElement.firstElementChild) {
		return;
	}

	const currentFocusedOption = document.getElementById(
		optionListElement.getAttribute('aria-activedescendant')
	);

	if (KEYS[event.key]) {
		openDropdown();
		event.preventDefault();
	}

	if (event.key === KEYS.ArrowDown && !event.altKey) {
		if (currentFocusedOption) {
			setFocusedOption(
				currentFocusedOption.nextElementSibling ||
					optionListElement.firstElementChild
			);
		}
		else {
			setFocusedOption(optionListElement.firstElementChild);
		}
	}
	else if (event.key === KEYS.ArrowUp) {
		if (currentFocusedOption) {
			setFocusedOption(
				currentFocusedOption.previousElementSibling ||
					optionListElement.lastElementChild
			);
		}
		else {
			setFocusedOption(optionListElement.lastElementChild);
		}
	}
	else if (event.key === KEYS.Home) {
		setFocusedOption(optionListElement.firstElementChild);
	}
	else if (event.key === KEYS.End) {
		setFocusedOption(optionListElement.lastElementChild);
	}
	else if (event.key === KEYS.Enter && currentFocusedOption) {
		setFocusedOption(currentFocusedOption);
		setSelectedOption(currentFocusedOption);
	}
}

function handleInputChange() {
	const filterValue = uiInputElement.value.toLowerCase();

	if (filterValue !== lastSearchQuery) {
		openDropdown();

		lastSearchQuery = filterValue;

		chooseOptionElement.classList.add('d-none');
		loadingResultsElement.classList.remove('d-none');

		filterOptions(filterValue).then((filteredOptions) => {
			loadingResultsElement.classList.add('d-none');
			renderOptionList(filteredOptions);

			if (optionListElement.firstElementChild) {
				chooseOptionElement.classList.remove('d-none');
				noResultsElement.classList.add('d-none');
				setFocusedOption(optionListElement.firstElementChild);
			}
			else {
				chooseOptionElement.classList.add('d-none');
				noResultsElement.classList.remove('d-none');
			}
		});
	}
}

function filterOptions(query) {
	return new Promise((resolve) => {
		if (input.attributes.relationshipURL) {
			lastSearchAbortController.abort();
			lastSearchAbortController = new AbortController();
			filterRemoteOptions(query, lastSearchAbortController).then(resolve);
		}
		else if (query) {
			resolve(filterLocalOptions(query));
		}
		else {
			resolve(optionList);
		}
	});
}

function filterLocalOptions(query) {
	const options = [];

	optionList.forEach((option) => {
		if (!option.value) {
			return;
		}

		if (option.textValue.startsWith(query)) {
			options.push(option);
		}
	});

	optionList.forEach((option) => {
		if (!option.value) {
			return;
		}

		if (option.textValue.includes(query) && !options.includes(option)) {
			options.push(option);
		}
	});

	return options;
}

function filterRemoteOptions(query, abortController) {
	if (
		!input.attributes.relationshipLabelFieldName ||
		!input.attributes.relationshipURL ||
		!input.attributes.relationshipValueFieldName
	) {
		return Promise.resolve({items: []});
	}

	const url = new URL(input.attributes.relationshipURL);
	url.searchParams.set('search', query);

	return Liferay.Util.fetch(url, {
		headers: new Headers({
			'Accept': 'application/json',
			'Content-Type': 'application/json',
		}),
		method: 'GET',
		signal: abortController.signal,
	})
		.then((response) => response.json())
		.then((result) => {
			return result.items.map((entry) => {
				let label = entry[input.attributes.relationshipLabelFieldName];

				if (Array.isArray(label)) {
					label = label.map((label) => label.name).join(', ');
				}
				else if (typeof label === 'object') {
					label = label.name;
				}

				return {
					textContent: label,
					textValue: label,
					value: `${
						entry[input.attributes.relationshipValueFieldName]
					}`,
				};
			});
		});
}

function handleWindowResizeOrScroll() {
	if (!document.body.contains(fragmentElement)) {
		window.removeEventListener('resize', handleWindowResizeOrScroll);
		window.removeEventListener('scroll', handleWindowResizeOrScroll);

		if (document.body.contains(dropdownElement)) {
			dropdownElement.parentElement.removeChild(dropdownElement);
		}

		return;
	}

	if (checkIsOpenDropdown()) {
		repositionDropdownElement();
	}
}

function setFocusedOption(optionElement) {
	const currentFocusedOption = document.getElementById(
		optionListElement.getAttribute('aria-activedescendant')
	);

	if (currentFocusedOption) {
		currentFocusedOption.removeAttribute('aria-selected');
	}

	if (optionElement) {
		optionListElement.setAttribute(
			'aria-activedescendant',
			optionElement.id
		);

		optionElement.setAttribute('aria-selected', 'true');
		optionElement.scrollIntoView({block: 'center'});
	}
	else {
		optionListElement.removeAttribute('aria-activedescendant');
	}
}

function createOptionElement(option) {
	const optionElement = document.createElement('li');

	optionElement.dataset.optionValue = option.value;
	// eslint-disable-next-line no-undef
	optionElement.id = `${fragmentEntryLinkNamespace}-option-${option.value}`;
	optionElement.textContent = option.textContent;

	optionElement.classList.add('dropdown-item');
	optionElement.setAttribute('role', 'option');

	if (
		optionListElement.getAttribute('aria-activedescendant') ===
		optionElement.id
	) {
		optionElement.setAttribute('aria-selected', 'true');
		optionElement.scrollIntoView({block: 'center'});
	}

	if (valueInputElement.value === option.value) {
		optionElement.classList.add('active');
	}

	return optionElement;
}

function setSelectedOption(optionElement) {
	closeDropdown();

	const selectedOption = document.getElementById(
		// eslint-disable-next-line no-undef
		`${fragmentEntryLinkNamespace}-option-${valueInputElement.value}`
	);

	if (selectedOption) {
		selectedOption.classList.remove('active');
	}

	lastSearchQuery = optionElement.textContent.toLowerCase();

	optionElement.classList.add('active');

	labelInputElement.value = optionElement.textContent;
	uiInputElement.value = optionElement.textContent;
	valueInputElement.value = optionElement.dataset.optionValue;
}

function checkIsOpenDropdown() {
	return (
		uiInputElement.getAttribute('aria-expanded') === 'true' &&
		buttonElement.getAttribute('aria-expanded') === 'true'
	);
}

function openDropdown() {
	const canFetchOptions = input.attributes.relationshipURL;

	if (!canFetchOptions && !optionList.length) {
		return;
	}

	dropdownElement.classList.replace('d-none', 'show');
	uiInputElement.setAttribute('aria-expanded', 'true');
	buttonElement.setAttribute('aria-expanded', 'true');

	requestAnimationFrame(() => {
		handleInputChange();
		repositionDropdownElement();
	});
}

function closeDropdown() {
	dropdownElement.classList.replace('show', 'd-none');
	uiInputElement.setAttribute('aria-expanded', 'false');
	buttonElement.setAttribute('aria-expanded', 'false');
}

function toggleDropdown() {
	if (checkIsOpenDropdown()) {
		closeDropdown();
	}
	else {
		openDropdown();
	}
}

function repositionDropdownElement() {
	const uiInputRect = uiInputElement.getBoundingClientRect();

	if (document.body.contains(fragmentElement)) {
		if (fragmentElement.contains(dropdownElement)) {
			document.body.appendChild(dropdownElement);
		}
	}
	else if (document.body.contains(dropdownElement)) {
		dropdownElement.parentNode.removeChild(dropdownElement);
	}

	dropdownElement.style.transform = `
		translateX(${
			(isRTL ? uiInputRect.right - window.innerWidth : uiInputRect.left) +
			window.scrollX
		}px)
		translateY(${uiInputRect.bottom + window.scrollY}px)
	`;
}

function renderOptionList(options) {
	optionListElement.innerHTML = '';

	options
		.slice(0, MAX_ITEMS)
		.forEach((option) =>
			optionListElement.appendChild(createOptionElement(option))
		);
}

function debounce(fn, delay) {
	let debounceId = null;

	return function (...args) {
		clearTimeout(debounceId);
		debounceId = setTimeout(() => fn(...args), delay);
	};
}
