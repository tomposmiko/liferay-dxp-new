import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React, {createRef} from 'react';
import {ENTER} from 'shared/util/key-constants';
import {withField} from 'shared/components/form';

interface ITitleEditorProps {
	editable?: boolean;
	inputName: string;
	onBlur?: (event) => void;
	onChange: (event) => void;
	placeholder?: string;
	value?: string;
}

export class TitleEditor extends React.Component<
	ITitleEditorProps,
	{editing: boolean}
> {
	static defaultProps = {
		editable: true
	};

	state = {
		editing: false
	};

	private _titleInput = createRef<HTMLInputElement>();

	@autobind
	handleBlur(event) {
		const {onBlur} = this.props;

		this.setState({editing: false});

		if (onBlur) {
			onBlur(event);
		}
	}

	@autobind
	handleEdit(event) {
		event.preventDefault();

		const {editable} = this.props;
		if (editable) {
			this.editing();
		}
	}

	@autobind
	handleKeyDown(event) {
		if (event.keyCode === ENTER) {
			this._titleInput.current.blur();
		}
	}

	@autobind
	handleKeyDownEdit(event) {
		event.preventDefault();

		if (event.keyCode === ENTER) {
			this.editing();
		}
	}

	@autobind
	editing() {
		this.setState(
			{
				editing: !this.state.editing
			},
			() => this._titleInput.current.select()
		);
	}

	render() {
		const {
			props: {editable, inputName, onChange, placeholder, value = ''},
			state: {editing}
		} = this;

		const rootClasses = getCN('title-editor-root', {editing});

		const inputClasses = getCN('title-input', {
			hide: !editing
		});

		const displayClasses = getCN('title-display', {
			hide: editing,
			'placeholder-display': !value
		});

		return (
			<div className={rootClasses}>
				<input
					className={inputClasses}
					name={inputName}
					onBlur={this.handleBlur}
					onChange={onChange}
					onKeyDown={this.handleKeyDown}
					placeholder={placeholder}
					ref={this._titleInput}
					required
					type='text'
					value={value}
				/>

				<div className={displayClasses}>
					<span
						className='title-value'
						onClick={this.handleEdit}
						onKeyDown={this.handleKeyDownEdit}
						role='button'
						tabIndex={0}
					>
						{value || placeholder}
					</span>

					{editable && (
						<Button
							aria-label={Liferay.Language.get('edit')}
							borderless
							display='unstyled'
							onClick={this.handleEdit}
							outline
							size='sm'
						>
							<Icon symbol='pencil' />
						</Button>
					)}
				</div>
			</div>
		);
	}
}

export default withField(({field: {name, ...otherFields}, ...otherProps}) => {
	const handleChange = event => {
		const {
			form: {setFieldValue}
		} = otherProps;

		setFieldValue(name, event.target.value);
	};

	return (
		<TitleEditor
			{...otherFields}
			{...otherProps}
			inputName={name}
			onChange={handleChange}
		/>
	);
});
