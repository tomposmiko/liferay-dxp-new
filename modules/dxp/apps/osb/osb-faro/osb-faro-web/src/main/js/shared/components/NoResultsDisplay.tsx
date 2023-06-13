import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React from 'react';
import {Sizes} from 'shared/util/constants';
import {sub} from 'shared/util/lang';

interface IIconProps {
	border?: boolean;
	size?: Sizes;
	symbol: string;
}

export interface INoResultsDisplayProps
	extends React.HTMLAttributes<HTMLElement> {
	children?: React.ReactElement;
	description?: string | React.ReactNode;
	displayCard?: boolean;
	icon?: IIconProps;
	primary?: boolean;
	spacer?: boolean;
	title?: string;
}

interface INoResultsDisplayIcon
	extends React.HTMLAttributes<HTMLDivElement>,
		IIconProps {}

const NoResultsDisplayIcon: React.FC<INoResultsDisplayIcon> = ({
	border = true,
	size = Sizes.XXLarge,
	symbol
}) => {
	const classes = getCN('no-results-icon', {
		'no-results-icon-border': border
	});

	return (
		<div className={classes}>
			<Icon size={size} symbol={symbol} />
		</div>
	);
};

const NoResultsDisplay: React.FC<INoResultsDisplayProps> = ({
	children,
	className,
	description,
	displayCard = false,
	icon,
	primary = false,
	spacer = false,
	title = getFormattedTitle(),
	...otherProps
}) => {
	const classes = getCN(className, 'no-results-root flex-grow-1', {
		'display-card': displayCard,
		'no-results-primary': primary
	});

	return (
		<div {...otherProps} className={classes}>
			<div className={getCN('no-results-content', {spacer})}>
				{icon && <NoResultsDisplayIcon {...icon} />}

				{title && <h4 className='no-results-title'>{title}</h4>}

				{description && (
					<div className='no-results-description'>{description}</div>
				)}

				{children}
			</div>
		</div>
	);
};

type GetFormattedTitle = (name?: string, title?: string) => string;

export const getFormattedTitle: GetFormattedTitle = (
	name = Liferay.Language.get('items').toLowerCase(),
	title = Liferay.Language.get('there-are-no-x-found')
) => sub(title, [name]) as string;

export default NoResultsDisplay;
