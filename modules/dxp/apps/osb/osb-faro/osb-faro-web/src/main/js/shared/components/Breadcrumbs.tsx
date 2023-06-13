import ClayButton from '@clayui/button';
import ClayDropDown, {Align} from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import getCN from 'classnames';
import React from 'react';
import {IBreadcrumbArgs} from 'shared/util/breadcrumbs';
import {Link} from 'react-router-dom';
import {useHistory} from 'react-router-dom';

interface IBreadcrumbProps {
	bufferSize?: number;
	className?: string;
	items: IBreadcrumbArgs[];
	onClick?: (id: string) => void;
}

interface IBreadcrumbItemProps {
	item: IBreadcrumbArgs;
	onClick?: (id: string) => void;
}

const BreadcrumbItem: React.FC<IBreadcrumbItemProps> = ({
	children,
	item: {active, href, id},
	onClick
}) => {
	const classes = getCN('breadcrumb-item', {active});

	function renderItem() {
		const contentClasses = getCN('breadcrumb-text-truncate', {
			'breadcrumb-link': href
		});

		if (active) {
			return <span className={contentClasses}>{children}</span>;
		} else if (href) {
			return (
				<Link className={contentClasses} to={href}>
					{children}
				</Link>
			);
		} else {
			return (
				<ClayButton
					className={contentClasses}
					displayType='unstyled'
					onClick={onClick ? () => onClick(id) : () => {}}
				>
					{children}
				</ClayButton>
			);
		}
	}

	return <li className={classes}>{renderItem()}</li>;
};

const Breadcrumb: React.FC<IBreadcrumbProps> = ({
	bufferSize = 3,
	className,
	items,
	onClick
}) => {
	const history = useHistory();
	const totalItems = items.length;

	let shownItems = items;
	let dropdownItems;

	if (bufferSize > 0 && totalItems > bufferSize) {
		const breakpoint = totalItems - bufferSize;

		shownItems = items.slice(breakpoint);
		dropdownItems = items.slice(0, breakpoint);
	}

	return (
		<ol className={getCN('breadcrumb', 'breadcrumbs-root', className)}>
			{dropdownItems && (
				<ClayDropDown
					alignmentPosition={Align.BottomCenter}
					className='dropdown-root breadcrumb-item'
					containerElement='li'
					trigger={
						<ClayButton
							className='breadcrumb-link'
							displayType='unstyled'
						>
							<ClayIcon
								className='icon-root'
								symbol='ellipsis-h'
							/>
						</ClayButton>
					}
				>
					{items.map(({href, label}, i) => (
						<ClayDropDown.Item
							key={i}
							onClick={() => history.push(href)}
						>
							{label}
						</ClayDropDown.Item>
					))}
				</ClayDropDown>
			)}

			{shownItems.map((item, i) => (
				<BreadcrumbItem item={item} key={i} onClick={onClick}>
					{item.label}
				</BreadcrumbItem>
			))}
		</ol>
	);
};

export default Breadcrumb;
