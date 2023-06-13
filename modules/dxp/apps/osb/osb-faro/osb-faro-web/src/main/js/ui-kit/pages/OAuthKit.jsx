import Alert from 'shared/components/Alert';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Form, {validateRequired} from 'shared/components/form';
import React from 'react';
import {DataSourceTypes} from 'shared/util/constants';
import {getTempCredentials} from 'shared/util/oauth';
import {PropTypes} from 'prop-types';

export default class OAuthKit extends React.Component {
	static propTypes = {
		groupId: PropTypes.string.isRequired
	};

	state = {
		error: null,
		tempCredentials: null
	};

	constructor(props) {
		super(props);

		this._formRef = React.createRef();
	}

	@autobind
	handleSubmit({baseUrl, consumerKey, consumerSecret}) {
		const {setSubmitting} = this._formRef.current.getFormikBag();

		const authWindow = open(
			null,
			'authorizeWindow',
			'width=400,height=500'
		);

		getTempCredentials({
			authWindow,
			baseUrl,
			consumerKey,
			consumerSecret,
			groupId: this.props.groupId,
			type: DataSourceTypes.Liferay
		})
			.then(tempCredentials => {
				setSubmitting(false);

				this.setState({
					error: null,
					tempCredentials
				});
			})
			.catch(error => {
				setSubmitting(false);

				this.setState({
					error,
					tempCredentials: null
				});
			});
	}

	render() {
		const {error, tempCredentials} = this.state;

		return (
			<div
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				<Form
					initialValues={{
						baseUrl: '',
						consumerKey: '',
						consumerSecret: ''
					}}
					onSubmit={this.handleSubmit}
					ref={this._formRef}
				>
					{({handleSubmit, isSubmitting, isValid}) => (
						<Form.Form onSubmit={handleSubmit}>
							<Form.Group>
								<Form.Input
									label='Url'
									name='baseUrl'
									validate={validateRequired}
								/>
							</Form.Group>

							<Form.Group>
								<Form.Input
									label='Client Secret'
									name='consumerSecret'
									validate={validateRequired}
								/>
							</Form.Group>

							<Form.Group>
								<Form.Input
									label='Client Key'
									name='consumerKey'
									validate={validateRequired}
								/>
							</Form.Group>

							<Form.Group>
								<Button
									disabled={!isValid}
									display='primary'
									loading={isSubmitting}
									type='submit'
								>
									{'Get Token'}
								</Button>
							</Form.Group>
						</Form.Form>
					)}
				</Form>

				{error ? (
					<Alert
						title={`Authorization failed: ${error.type}`}
						type='danger'
					/>
				) : (
					<Alert
						title={`Received credentials: ${JSON.stringify(
							tempCredentials
						)}`}
						type='success'
					/>
				)}
			</div>
		);
	}
}
