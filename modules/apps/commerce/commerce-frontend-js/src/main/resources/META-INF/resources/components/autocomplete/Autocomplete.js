/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import ClayAutocomplete from '@clayui/autocomplete';
import ClayDropDown from '@clayui/drop-down';
import {FocusScope} from '@clayui/shared';
import {ReactPortal, useIsMounted} from '@liferay/frontend-js-react-web';
import PropTypes from 'prop-types';
import React, {useEffect, useMemo, useRef, useState} from 'react';

import {debouncePromise} from '../../utilities/debounce';
import {AUTOCOMPLETE_VALUE_UPDATED} from '../../utilities/eventsDefinitions';
import {useLiferayModule} from '../../utilities/hooks';
import {
	formatAutocompleteItem,
	getData,
	getValueFromItem,
} from '../../utilities/index';
import {showErrorNotification} from '../../utilities/notifications';
import InfiniteScroller from '../infinite_scroller/InfiniteScroller';

function Autocomplete({onChange, onItemsUpdated, onValueUpdated, ...props}) {
	const [query, setQuery] = useState(props.initialLabel || '');
	const [initialised, setInitialised] = useState(
		Boolean(props.customViewModuleUrl || props.customView)
	);
	const [active, setActive] = useState(false);
	const [selectedItem, updateSelectedItem] = useState(
		formatAutocompleteItem(
			props.initialValue,
			props.itemsKey,
			props.initialLabel,
			props.itemsLabel
		)
	);
	const [items, updateItems] = useState(null);
	const [loading, setLoading] = useState(false);
	const [totalCount, updateTotalCount] = useState(null);
	const [lastPage, updateLastPage] = useState(null);
	const [page, updatePage] = useState(1);
	const [pageSize, updatePageSize] = useState(props.pageSize);
	const node = useRef();
	const dropdownNode = useRef();
	const inputNode = useRef();
	const FetchedCustomView = useLiferayModule(props.customViewModuleUrl);
	const isMounted = useIsMounted();

	const debouncedGetItems = useMemo(
		() => debouncePromise(getData, props.fetchDataDebounce),
		[props.fetchDataDebounce]
	);

	useEffect(() => {
		if (items && items.length === 1 && props.autofill) {
			const firstItem = items[0];
			updateSelectedItem(firstItem);
		}
	}, [items, props.autofill, props.itemsKey, props.itemsLabel]);

	useEffect(() => {
		updateSelectedItem(
			formatAutocompleteItem(
				props.initialValue,
				props.itemsKey,
				props.initialLabel,
				props.itemsLabel
			)
		);

		setInitialised(Boolean(props.customViewModuleUrl || props.customView));
	}, [
		props.customView,
		props.customViewModuleUrl,
		props.initialLabel,
		props.initialValue,
		props.itemsKey,
		props.itemsLabel,
	]);

	useEffect(() => {
		if (!initialised) {
			return;
		}

		const currentValue = selectedItem
			? getValueFromItem(selectedItem, props.itemsKey)
			: null;

		if (props.id) {
			Liferay.fire(AUTOCOMPLETE_VALUE_UPDATED, {
				currentValue,
				id: props.id,
				itemData: selectedItem,
			});
		}

		if (onValueUpdated) {
			onValueUpdated(currentValue, selectedItem);
		}

		if (onChange) {
			onChange({target: {value: currentValue}});
		}
	}, [
		initialised,
		selectedItem,
		props.id,
		onValueUpdated,
		onChange,
		props.itemsKey,
	]);

	useEffect(() => {
		if (query) {
			setInitialised(true);
		}

		if (props.infiniteScrollMode) {
			updateItems(null);
		}

		updatePage(1);
		updateTotalCount(null);
		updateLastPage(null);
	}, [props.infiniteScrollMode, query]);

	useEffect(() => {
		if (initialised && debouncedGetItems && !props.disabled) {
			setLoading(true);

			debouncedGetItems(props.apiUrl, query, page, pageSize)
				.then((jsonResponse) => {
					if (!isMounted()) {
						return;
					}

					updateItems((prevItems) => {
						if (
							props.infiniteScrollMode &&
							prevItems?.length &&
							page > 1
						) {
							return [...prevItems, ...jsonResponse.items];
						}

						return jsonResponse.items;
					});

					updateTotalCount(jsonResponse.totalCount);
					updateLastPage(jsonResponse.lastPage);
					setLoading(false);

					if (!query) {
						return;
					}
					const found = jsonResponse.items.find(
						(item) =>
							getValueFromItem(item, props.itemsLabel) === query
					);
					if (found) {
						updateSelectedItem(found);
					}
				})
				.catch(() => {
					showErrorNotification();
					setLoading(false);
				});
		}
	}, [
		debouncedGetItems,
		initialised,
		isMounted,
		query,
		page,
		pageSize,
		props.disabled,
		props.infiniteScrollMode,
		props.apiUrl,
		props.itemsLabel,
		props.showErrorNotification,
	]);

	useEffect(() => {
		if (onItemsUpdated) {
			onItemsUpdated(items);
		}
	}, [items, onItemsUpdated]);

	useEffect(() => {
		function handleClick(event) {
			if (
				node.current.contains(event.target) ||
				event.target === dropdownNode.current.parentElement ||
				(dropdownNode.current &&
					dropdownNode.current.contains(event.target))
			) {
				return;
			}

			setActive(false);
		}
		if (active) {
			document.addEventListener('mousedown', handleClick);
		}

		return () => {
			document.removeEventListener('mousedown', handleClick);
		};
	}, [active]);

	const CustomView = props.customView || FetchedCustomView;

	const results = CustomView ? (
		<CustomView
			items={items}
			lastPage={lastPage}
			loading={loading}
			page={page}
			pageSize={pageSize}
			totalCount={totalCount}
			updatePage={updatePage}
			updatePageSize={updatePageSize}
			updateSelectedItem={updateSelectedItem}
		/>
	) : (
		<ClayDropDown.ItemList className="mb-0">
			{items && items.length === 0 && (
				<ClayDropDown.Item className="disabled">
					{Liferay.Language.get('no-items-were-found')}
				</ClayDropDown.Item>
			)}
			{items &&
				items.length > 0 &&
				items.map((item) => (
					<ClayAutocomplete.Item
						key={item.id || String(item[props.itemsKey])}
						onClick={() => {
							updateSelectedItem(item);
							setActive(false);
						}}
						value={String(getValueFromItem(item, props.itemsLabel))}
					/>
				))}
		</ClayDropDown.ItemList>
	);

	const wrappedResults =
		props.infiniteScrollMode && CustomView ? (
			<InfiniteScroller
				onBottomTouched={() => {
					if (!loading) {
						updatePage((currentPage) =>
							currentPage < lastPage
								? currentPage + 1
								: currentPage
						);
					}
				}}
				scrollCompleted={!items || items.length === totalCount}
			>
				{results}
			</InfiniteScroller>
		) : (
			results
		);

	return (
		<>
			<FocusScope>
				<ClayAutocomplete className={props.inputClass} ref={node}>
					<input
						id={props.inputId || props.inputName}
						name={props.inputName}
						type="hidden"
						value={
							selectedItem
								? getValueFromItem(selectedItem, props.itemsKey)
								: ''
						}
					/>
					<ClayAutocomplete.Input
						disabled={props.readOnly}
						id={props.id}
						name={props.name}
						onChange={(event) => {
							updateSelectedItem(null);
							updatePage(1);
							setQuery(event.target.value);
						}}
						onFocus={(_e) => {
							setActive(true);
							setInitialised(true);
						}}
						onKeyUp={(event) => {
							setActive(event.keyCode !== 27);
						}}
						placeholder={props.inputPlaceholder}
						ref={inputNode}
						required={props.required || false}
						value={
							selectedItem
								? getValueFromItem(
										selectedItem,
										props.itemsLabel
								  )
								: query
						}
					/>
					{!CustomView && !props.disabled && (
						<ClayAutocomplete.DropDown
							active={
								active && ((items && page === 1) || page > 1)
							}
						>
							<div
								className="autocomplete-items"
								ref={dropdownNode}
							>
								{wrappedResults}
							</div>
						</ClayAutocomplete.DropDown>
					)}
					{loading && <ClayAutocomplete.LoadingIndicator />}
				</ClayAutocomplete>
			</FocusScope>
			{CustomView &&
				!props.disabled &&
				(props.contentWrapperRef
					? props.contentWrapperRef.current && (
							<ReactPortal
								container={props.contentWrapperRef.current}
							>
								{wrappedResults}
							</ReactPortal>
					  )
					: wrappedResults)}
		</>
	);
}

Autocomplete.propTypes = {
	apiUrl: PropTypes.string.isRequired,
	autofill: PropTypes.bool,
	contentWrapperRef: PropTypes.object,
	customView: PropTypes.func,
	customViewModuleUrl: PropTypes.string,
	disabled: PropTypes.bool,
	fetchDataDebounce: PropTypes.number,
	id: PropTypes.string,
	infiniteScrollMode: PropTypes.bool,
	initialLabel: PropTypes.oneOfType([PropTypes.number, PropTypes.string])
		.isRequired,
	initialValue: PropTypes.oneOfType([PropTypes.number, PropTypes.string])
		.isRequired,
	inputClass: PropTypes.string,
	inputId: PropTypes.string,
	inputName: PropTypes.string.isRequired,
	inputPlaceholder: PropTypes.string,
	itemsKey: PropTypes.string.isRequired,
	itemsLabel: PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.arrayOf(PropTypes.string),
	]).isRequired,
	loadingView: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
	onChange: PropTypes.func,
	onItemsUpdated: PropTypes.func,
	onValueUpdated: PropTypes.func,
	required: PropTypes.bool,
	value: PropTypes.string,
};

Autocomplete.defaultProps = {
	autofill: false,
	disabled: false,
	fetchDataDebounce: 200,
	infiniteScrollMode: false,
	initialLabel: '',
	initialValue: '',
	inputPlaceholder: Liferay.Language.get('type-here'),
	pageSize: 10,
};

export default Autocomplete;
