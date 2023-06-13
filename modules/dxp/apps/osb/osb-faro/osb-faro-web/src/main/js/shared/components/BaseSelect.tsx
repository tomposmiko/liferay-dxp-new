import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import getCN from 'classnames';
import Input from './Input';
import Overlay from './Overlay';
import Promise from 'metal-promise';
import React, {useEffect, useImperativeHandle, useRef, useState} from 'react';
import Spinner from './Spinner';
import {ARROW_DOWN, ARROW_UP, ENTER} from '../util/key-constants';
import {DocumentNode} from 'graphql';
import {identity, noop} from 'lodash';
import {useDebounce} from 'shared/hooks';
import {useQuery} from '@apollo/react-hooks';
import {useRequest} from 'shared/hooks';

const DEBOUNCE_DELAY = 250;
const SELECT_KEYS = [ARROW_DOWN, ARROW_UP, ENTER];

type GraphqlQuery = {
	mapResultsToProps: (data: any) => TMappedData;
	variables: object;
	query: DocumentNode;
};

type TMappedData = {
	data: string[];
	total: number;
};

interface IItemProps extends React.HTMLAttributes<HTMLLIElement> {
	active?: boolean;
	disabled?: boolean;
	item: any;
	itemRenderer: (item: any) => React.ReactNode;
	onSelect: (item: any) => void;
}

export const Item: React.FC<IItemProps> = ({
	active,
	className,
	disabled,
	item,
	itemRenderer,
	onSelect
}) => (
	<li className={className}>
		<ClayButton
			className={getCN('button-root dropdown-item text-truncate', {
				active
			})}
			disabled={disabled}
			displayType='unstyled'
			onClick={() => onSelect(item)}
		>
			{itemRenderer(item)}
		</ClayButton>
	</li>
);

interface IBaseSelectProps extends React.HTMLAttributes<HTMLInputElement> {
	alwaysFetchOnFocus?: boolean;
	className?: string;
	containerClass?: string;
	dataSourceFn?: (value: string | number) => Promise<any>;
	disabled?: boolean;
	emptyInputOnInactive?: boolean;
	inputName?: string;
	focusOnInit?: boolean;
	forwardedRef?: React.Ref<any>;
	graphqlQuery?: GraphqlQuery;
	id?: string;
	inputSize?: string;
	inputValue?: string | React.ReactText;
	inset?: boolean;
	itemRenderer?: (item: any) => React.ReactNode;
	menuTitle?: string;
	onFocus?: () => void;
	onInputValueChange?: (value: string | number) => void;
	onSelect?: (item: any) => void;
	selectedItem?: any;
}

const BaseSelect: React.FC<IBaseSelectProps> = ({
	alwaysFetchOnFocus = false,
	className,
	containerClass,
	dataSourceFn,
	disabled = false,
	emptyInputOnInactive = false,
	focusOnInit = false,
	forwardedRef,
	graphqlQuery,
	id,
	inputName,
	inputSize,
	inputValue = '',
	inset = false,
	itemRenderer,
	menuTitle,
	onBlur,
	onFocus,
	onInputValueChange = noop,
	onSelect = noop,
	placeholder = '',
	selectedItem,
	...otherProps
}) => {
	useImperativeHandle(forwardedRef, () => ({
		focus: () => {
			handleFocus();

			_inputRef.current.focus();
		}
	}));

	const [active, setActive] = useState<boolean>(false);
	const [focusIndex, setFocusIndex] = useState<number>(0);

	const _inputRef = useRef<any>();

	let response;

	if (graphqlQuery) {
		const {
			mapResultsToProps = value => value,
			query,
			variables
		} = graphqlQuery;
		const debouncedInputValue = useDebounce(inputValue, DEBOUNCE_DELAY);

		response = useQuery(query, {
			fetchPolicy: 'network-only',
			skip: !active,
			variables: {
				...variables,
				keywords: debouncedInputValue
			}
		});

		response = {
			...response,
			...mapResultsToProps(response.data)
		};
	} else {
		response = useRequest({
			dataSourceFn: ({value}) => dataSourceFn(value),
			debounceDelay: DEBOUNCE_DELAY,
			initialState: {
				data: [],
				error: false,
				loading: false
			},
			resetStateIfSkipingRequest: true,
			skipRequest: !active,
			variables: {value: inputValue}
		});
	}

	const {data: items = [], loading, refetch} = response;

	useEffect(() => {
		if (focusOnInit) {
			_inputRef.current.focus();
		}
	}, []);

	const handleBlur = (event: React.FocusEvent<HTMLInputElement>) => {
		onBlur && onBlur(event);
	};

	const handleFocus = () => {
		if (!active) {
			if (!items?.length || alwaysFetchOnFocus) {
				refetch();
			}

			onFocus && onFocus();

			setActive(true);

			handleSetFocusIndex(0);
		}
	};

	const handleKeyDown = (event: React.KeyboardEvent) => {
		const {keyCode} = event;

		if (!SELECT_KEYS.includes(keyCode)) {
			return;
		}

		event.preventDefault();

		switch (keyCode) {
			case ARROW_DOWN:
				handleSetFocusIndex(focusIndex + 1);
				break;
			case ARROW_UP:
				handleSetFocusIndex(focusIndex - 1);
				break;
			case ENTER:
				handleSelect(items[focusIndex]);
				break;
			default:
				break;
		}
	};

	const handleOutsideClick = () => {
		_inputRef.current.blur();

		setActive(false);
	};

	const handleSelect = item => {
		handleOutsideClick();

		onSelect(item);
	};

	const handleSetFocusIndex = (val: number): void => {
		const count = items?.length;

		setFocusIndex((val + count) % count || 0);
	};

	return (
		<Overlay
			{...otherProps}
			active={active}
			alignment='bottomLeft'
			containerClass={getCN('base-select-container', containerClass)}
			onOutsideClick={handleOutsideClick}
			usePortal={false}
		>
			<Input.Group
				className={getCN(
					'base-select-input-root select-input-root',
					className,
					{inset}
				)}
				onClick={disabled ? null : handleFocus}
			>
				<Input.GroupItem>
					<Input
						autoComplete='off'
						disabled={disabled}
						id={id}
						inset='after'
						name={inputName}
						onBlur={handleBlur}
						onChange={(
							event: React.ChangeEvent<HTMLInputElement>
						) => {
							onInputValueChange(event.target.value);
						}}
						onFocus={handleFocus}
						onKeyDown={handleKeyDown}
						placeholder={placeholder}
						ref={_inputRef}
						size={inputSize}
						value={
							active || !emptyInputOnInactive ? inputValue : ''
						}
					/>

					<Input.Inset position='after'>
						{loading ? (
							<Spinner size='sm' />
						) : (
							<ClayIcon
								className='icon-root'
								symbol='caret-bottom'
							/>
						)}
					</Input.Inset>
				</Input.GroupItem>

				{!active && selectedItem && itemRenderer && (
					<div className='selected-item-container'>
						{itemRenderer(selectedItem)}
					</div>
				)}
			</Input.Group>

			{!!items?.length && (
				<div className='dropdown-root'>
					<ul className='base-select-menu dropdown-menu show'>
						{!!menuTitle && (
							<li className='dropdown-header'>{menuTitle}</li>
						)}

						{items.map((item, i) => (
							<Item
								active={i === focusIndex}
								disabled={loading}
								item={item}
								itemRenderer={itemRenderer || identity}
								key={i}
								onSelect={handleSelect}
							/>
						))}
					</ul>
				</div>
			)}
		</Overlay>
	);
};

export default React.forwardRef<HTMLInputElement, IBaseSelectProps>(
	(props, ref) => <BaseSelect {...props} forwardedRef={ref} />
);
