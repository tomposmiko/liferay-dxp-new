import Button from '../Button';
import getCN from 'classnames';
import Icon from '../Icon';
import React from 'react';

interface IHeaderProps {
	border?: boolean;
	className?: string;
	iconSymbol?: string;
	onClose?: () => void;
	title?: React.ReactNode;
}

const Header: React.FC<IHeaderProps> = ({
	border = false,
	className,
	iconSymbol,
	onClose,
	title
}) => (
	<div className={getCN('modal-header', className, {border})}>
		{title && (
			<h4 className='modal-title'>
				{iconSymbol && (
					<Icon
						className='modal-title-indicator'
						symbol={iconSymbol}
					/>
				)}

				{title}
			</h4>
		)}

		{!!onClose && (
			<Button className='close' onClick={onClose}>
				<Icon symbol='times' />
			</Button>
		)}
	</div>
);

export default Header;
