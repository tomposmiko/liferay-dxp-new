import ChartTooltip from 'shared/components/chart-tooltip';
import Circle from 'shared/components/Circle';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React, {useEffect, useRef, useState} from 'react';
import ReactDOM from 'react-dom';
import {getAxisMeasuresFromData} from 'shared/util/charts';
import {getPercentage} from 'shared/util/util';
import {Column as TooltipColumn} from './chart-tooltip/types';
import {toRounded, toThousands} from 'shared/util/numbers';

const CLASSNAME = 'analytics-bar-chart-html';

type Column = {
	color?: string;
	icon?: string;
	label: string | (() => React.ReactNode);
};

export type Grid = {
	formatter: (value: number | string) => React.ReactNode;
	maxValue: number;
	minValue: number;
	precision: number;
	show: boolean;
	type: 'number' | 'percentage';
};

type Interval = {
	end: number;
	start: number;
};

export type Item = {
	columns: Column[];
	expanded: boolean;
	intervals: Interval[];
	items: Item[];
	progress: Progress[];
	showControls: boolean;
	tooltip: {
		header: {
			columns: TooltipColumn[];
		}[];
		rows: {
			columns: TooltipColumn[];
		}[];
	};
};

type Progress = {
	color: string;
	value: string | number;
};

interface IHTMLBarChartProps {
	disableScroll: boolean;
	formatSpacement: boolean;
	grid: Grid;
	header?: Column[];
	items: Item[];
}

const HTMLBarChart: React.FC<IHTMLBarChartProps> = ({
	disableScroll = false,
	formatSpacement = true,
	grid = {
		formatter: value => value,
		precision: 1,
		show: false,
		type: 'number'
	},
	header,
	items: initialItems
}) => {
	const [items, setItems] = useState<Item[]>(initialItems);
	const [showArrowDownIcon, setShowArrowDownIcon] = useState(false);
	const [tooltip, setTooltip] = useState({
		header: [],
		position: {
			left: '0px',
			top: '0px'
		},
		rows: [],
		show: false
	});

	const _groupItemsRef = useRef<HTMLDivElement>();
	const _tooltipRef = useRef<HTMLDivElement>();

	useEffect(() => {
		setShowArrowDownIcon(handleShowArrowDownIcon(_groupItemsRef.current));
	}, [items]);

	useEffect(() => {
		setItems(initialItems);
	}, [initialItems]);

	const {intervals: gridIntervals} = getAxisMeasuresFromData([
		'data1',
		grid.minValue,
		grid.maxValue
	]);

	const alignTooltip = ({pageX, pageY}, width, height) => {
		const arrowPopoverSize = 6;
		const tooltipDistance = 15;

		return {
			left: pageX - width / 2,
			top: pageY - height - arrowPopoverSize - tooltipDistance
		};
	};

	const getIntervalWidth = ({end, start}) => {
		const {show} = grid;

		let width: number | string = end - start;

		if (typeof start === 'number' && show) {
			const startPosition = Number(
				toRounded(
					getPercentage(
						start,
						gridIntervals[gridIntervals.length - 1]
					)
				)
			);

			const endPosition = Number(
				toRounded(
					getPercentage(end, gridIntervals[gridIntervals.length - 1])
				)
			) as number;

			width = `${endPosition - startPosition}%`;
		}

		return width;
	};

	const getProgressWidth = value => {
		const {show} = grid;

		let width = value;

		if (typeof value === 'number' && show) {
			width = `${toRounded(
				getPercentage(value, gridIntervals[gridIntervals.length - 1])
			)}%`;
		}

		return width;
	};

	const handleClickToggleList = ({
		currentTarget: {
			dataset: {index}
		}
	}: React.MouseEvent<HTMLButtonElement>): void => {
		const newItems = items.map((item, currIndex) => {
			if (currIndex === Number(index)) {
				return {
					...item,
					expanded: !item.expanded
				};
			}
			return item;
		});

		setItems(newItems);
	};

	const handleMouseEnterItem = ({header, rows}) => {
		setTooltip({
			...tooltip,
			header,
			rows,
			show: true
		});
	};

	const handleMouseLeaveItem = () => {
		setTooltip({
			...tooltip,
			show: false
		});
	};

	const handleMouseMoveItem = (event: React.MouseEvent<HTMLLIElement>) => {
		if (!tooltip.show) return;

		const {clientHeight, clientWidth} = _tooltipRef.current;
		const {left, top} = alignTooltip(event, clientWidth, clientHeight);

		setTooltip({
			...tooltip,
			position: {
				left: `${left}px`,
				top: `${top}px`
			}
		});
	};

	const handleScrollItems = ({target}) => {
		setShowArrowDownIcon(handleShowArrowDownIcon(target));
	};

	const getIntervalStartPosition = start => {
		const {show} = grid;

		let startPosition: number | string = start;

		if (typeof start === 'number' && show) {
			startPosition = `${toRounded(
				getPercentage(start, gridIntervals[gridIntervals.length - 1])
			)}%`;
		}

		return startPosition;
	};

	const handleShowArrowDownIcon = ({
		clientHeight,
		offsetHeight,
		scrollHeight,
		scrollTop
	}) =>
		scrollHeight > clientHeight &&
		offsetHeight + scrollTop !== scrollHeight;

	const hasItems = items => !!items?.length;

	const renderGridContent = value => {
		const {formatter, precision, type} = grid;

		if (type === 'percentage') {
			return <span>{`${toRounded(value, precision)}%`}</span>;
		}

		return <span>{formatter(toThousands(value))}</span>;
	};

	const renderHeader = ({
		columns,
		expanded,
		index,
		intervals,
		items,
		progress,
		showControls
	}: {
		columns: Column[];
		expanded?: boolean;
		index?: number;
		intervals?: Interval[];
		items?: Item[];
		progress?: Progress[];
		showControls?: boolean;
	}) => (
		<div className={`${CLASSNAME}-header`}>
			{progress && (
				<div className={`${CLASSNAME}-progress`}>
					{hasItems(progress) &&
						progress.map(({color, value}, index) => (
							<div
								key={index}
								style={{
									backgroundColor: color,
									width: getProgressWidth(value)
								}}
							/>
						))}
				</div>
			)}

			{intervals && (
				<div className={`${CLASSNAME}-interval`}>
					{hasItems(intervals) &&
						intervals.map(({end, start}, index) => {
							const startPosition = getIntervalStartPosition(
								start
							);
							const width = getIntervalWidth({end, start});

							return (
								<div
									key={index}
									style={{marginLeft: startPosition, width}}
								/>
							);
						})}
				</div>
			)}

			{hasItems(columns) && (
				<div>
					{columns.map(({color, icon, label}, index) => (
						<div className={`${CLASSNAME}-column`} key={index}>
							{icon && renderIcon({color, icon})}

							<div className='text-truncate w-100'>
								{typeof label === 'function' ? (
									label()
								) : (
									<span>{label}</span>
								)}
							</div>
						</div>
					))}
				</div>
			)}

			{hasItems(items) && showControls && (
				<button
					className={`${CLASSNAME}-button`}
					data-index={index}
					onClick={handleClickToggleList}
				>
					<Icon className='icon' symbol={expanded ? 'hr' : 'plus'} />
				</button>
			)}
		</div>
	);

	const renderIcon = ({color, icon}) => {
		if (color) {
			return (
				<Circle color={color} size={32}>
					<Icon symbol={icon} />
				</Circle>
			);
		}

		return <Icon className={`${CLASSNAME}-icon`} symbol={icon} />;
	};

	const renderItems = items => (
		<ul className={`${CLASSNAME}-items`}>
			{hasItems(items) &&
				items.map(
					(
						{
							columns,
							expanded,
							intervals,
							items,
							progress,
							showControls,
							tooltip
						},
						index
					) => (
						<li
							className={`${CLASSNAME}-item`}
							key={index}
							{...(tooltip
								? {
										onBlur: () => false,
										onFocus: () => false,
										onMouseEnter: () =>
											handleMouseEnterItem(tooltip),
										onMouseLeave: handleMouseLeaveItem,
										onMouseMove: handleMouseMoveItem
								  }
								: {})}
						>
							{renderHeader({
								columns,
								expanded,
								index,
								intervals,
								items,
								progress,
								showControls
							})}

							{hasItems(items) && expanded && renderItems(items)}
						</li>
					)
				)}
		</ul>
	);

	const renderTooltip = ({header, position, rows}) => {
		const {left, top} = position;

		return ReactDOM.createPortal(
			<div
				className={`${CLASSNAME}-tooltip bb-tooltip-container`}
				ref={_tooltipRef}
				style={{left, top}}
			>
				<ChartTooltip header={[{columns: header}]} rows={rows} />
			</div>,
			document.querySelector('body.dxp')
		);
	};

	return (
		<div
			className={getCN(CLASSNAME, {
				'disable-scroll': disableScroll,
				'format-spacement': formatSpacement
			})}
		>
			{grid.show && (
				<div className={`${CLASSNAME}-grid`}>
					{gridIntervals.map((interval, index) => (
						<div className={`${CLASSNAME}-grid-item`} key={index}>
							{renderGridContent(interval)}
						</div>
					))}
				</div>
			)}

			{header && renderHeader({columns: header})}

			<div
				className={`${CLASSNAME}-group-items`}
				onScroll={handleScrollItems}
				ref={_groupItemsRef}
			>
				{renderItems(items)}
			</div>

			{showArrowDownIcon && (
				<Icon className='icon text-l-secondary ' symbol='angle-down' />
			)}

			{tooltip.show && renderTooltip(tooltip)}
		</div>
	);
};

export default HTMLBarChart;
