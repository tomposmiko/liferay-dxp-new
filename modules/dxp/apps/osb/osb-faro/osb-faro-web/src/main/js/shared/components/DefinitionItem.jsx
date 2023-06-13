import autobind from 'autobind-decorator';
import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import Form from 'shared/components/form';
import React from 'react';
import TextTruncate from 'shared/components/TextTruncate';
import {PropTypes} from 'prop-types';

export default class DefinitionItem extends React.Component {
	static defaultProps = {
		name: 'name'
	};

	static propTypes = {
		editable: PropTypes.bool,
		inputWidth: PropTypes.number,
		label: PropTypes.string,
		name: PropTypes.string,
		onSubmit: PropTypes.func,
		validate: PropTypes.func,
		value: PropTypes.string
	};

	state = {
		editing: false
	};

	constructor(props) {
		super(props);

		this._formRef = React.createRef();
	}

	@autobind
	handleSubmit(values) {
		const {name, onSubmit} = this.props;

		const {setSubmitting} = this._formRef.current.getFormikActions();

		if (onSubmit) {
			onSubmit(values[name], name)
				.then(() => {
					setSubmitting(false);

					this.setState({editing: false});
				})
				.catch(err => {
					if (!err.IS_CANCELLATION_ERROR) {
						setSubmitting(false);
					}
				});
		}
	}

	@autobind
	handleEditToggle() {
		this.setState({
			editing: !this.state.editing
		});
	}

	render() {
		const {
			props: {editable, inputWidth, label, name, validate, value},
			state: {editing}
		} = this;

		return (
			<div
				className={`definition-item-root${
					this.props.className ? ` ${this.props.className}` : ''
				}`}
			>
				{label && <h6>{label}</h6>}

				{editing ? (
					<Form
						initialValues={{[name]: value}}
						key='EDITING'
						onSubmit={this.handleSubmit}
						ref={this._formRef}
					>
						{({handleSubmit, isSubmitting, isValid}) => (
							<Form.Form
								className='definition-item-editor'
								onSubmit={handleSubmit}
							>
								<Form.Group autoFit>
									<Form.Input
										name={name}
										validate={validate}
										width={inputWidth}
									/>

									<ClayButton
										aria-label={Liferay.Language.get(
											'cancel'
										)}
										className='button-root'
										displayType='secondary'
										onClick={this.handleEditToggle}
										size='sm'
									>
										<ClayIcon
											className='icon-root'
											symbol='times'
										/>
									</ClayButton>

									<ClayButton
										aria-label={Liferay.Language.get(
											'submit'
										)}
										className='button-root'
										disabled={!isValid}
										displayType='primary'
										size='sm'
										type='submit'
									>
										{isSubmitting && (
											<ClayLoadingIndicator
												className='d-inline-block mr-2'
												displayType='secondary'
												size='sm'
											/>
										)}

										<ClayIcon
											className='icon-root'
											symbol='check'
										/>
									</ClayButton>
								</Form.Group>
							</Form.Form>
						)}
					</Form>
				) : (
					<div
						className='d-flex align-items-center'
						key='NOT_EDITING'
					>
						{value ? <TextTruncate title={value} /> : '-'}

						{editable && (
							<ClayButton
								aria-label={Liferay.Language.get('edit')}
								className='button-root'
								displayType='secondary'
								onClick={this.handleEditToggle}
								size='sm'
							>
								<ClayIcon
									className='icon-root'
									symbol='pencil'
								/>
							</ClayButton>
						)}
					</div>
				)}
			</div>
		);
	}
}
