import ClayIcon from './ClayIcon.es';
import getCN from 'classnames';
import React, {Component} from 'react';
import {PropTypes} from 'prop-types';

class ClayButton extends Component {
	static propTypes = {
		borderless: PropTypes.bool,
		className: PropTypes.string,
		href: PropTypes.string,
		iconName: PropTypes.string,
		label: PropTypes.string,
		monospaced: PropTypes.bool,
		size: PropTypes.oneOf(
			[
				'sm'
			]
		),
		style: PropTypes.oneOf(
			[
				'primary',
				'secondary',
				'info',
				'success',
				'warning',
				'danger',
				'dark',
				'light',
				'unstyled'
			]
		),
		type: PropTypes.string
	};

	static defaultProps = {
		borderless: false,
		monospaced: false,
		style: 'secondary',
		type: 'button'
	};

	render() {
		const {
			borderless,
			className,
			href,
			iconName,
			label,
			monospaced,
			size,
			style,
			type,
			...otherProps
		} = this.props;

		const stylePrefix = borderless ? 'btn-outline-' : 'btn-';

		const classes = getCN(
			'btn',
			`${stylePrefix}${style}`,
			{
				[`btn-${size}`]: size,
				'btn-monospaced': monospaced,
				'btn-outline-borderless': borderless
			},
			className
		);

		return href ? (
			<a className={classes} href={href} {...otherProps}>{label}</a>
		) : (
			<button
				className={classes}
				type={type}
				{...otherProps}
			>
				{iconName && (
					<ClayIcon
						className={label ? 'inline-item inline-item-before' : 'icon'}
						iconName={iconName}
					/>
				)}

				{label}
			</button>
		);
	}
}

export default ClayButton;