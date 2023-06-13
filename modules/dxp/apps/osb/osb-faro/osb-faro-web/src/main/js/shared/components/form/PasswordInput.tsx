import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Form from 'shared/components/form';
import Icon from 'shared/components/Icon';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import PropTypes from 'prop-types';
import React from 'react';

interface IPasswordInputProps extends React.ComponentProps<typeof Form.Input> {
	disabled?: boolean;
}

interface IPasswordInputState {
	showPassword: boolean;
}

export default class PasswordInput extends React.Component<
	IPasswordInputProps,
	IPasswordInputState
> {
	static defaultProps = {
		disabled: false
	};

	static propTypes = {
		disabled: PropTypes.bool
	};

	state = {
		showPassword: false
	};

	@autobind
	handleShowPasswordToggle() {
		this.setState({
			showPassword: !this.state.showPassword
		});
	}

	render() {
		const {
			props: {disabled, placeholder, ...otherProps},
			state: {showPassword}
		} = this;

		return (
			<Form.Input
				{...omitDefinedProps(otherProps, PasswordInput.propTypes)}
				disabled={disabled}
				inset={{
					content: (
						<Button
							disabled={disabled}
							display='unstyled'
							onClick={this.handleShowPasswordToggle}
						>
							<Icon symbol={showPassword ? 'hidden' : 'view'} />
						</Button>
					),
					position: 'after'
				}}
				placeholder={placeholder}
				type={showPassword ? 'text' : 'password'}
			/>
		);
	}
}
