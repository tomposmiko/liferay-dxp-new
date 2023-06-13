import ClayButton from '@clayui/button';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import client from 'shared/apollo/client';
import Form, {
	toPromise,
	validateMaxLength,
	validateRequired
} from 'shared/components/form';
import Modal from 'shared/components/modal';
import React from 'react';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {Attribute, DataTypes, Event} from 'event-analysis/utils/types';
import {connect, ConnectedProps} from 'react-redux';
import {DATA_TYPE_LABELS_MAP} from 'event-analysis/utils/utils';
import {debounce, get} from 'lodash/fp';
import {DocumentNode} from 'graphql';
import {
	EventAttributeDefinitionData,
	EventAttributeDefinitionVariables,
	UpdateEventAttributeDefinitionVariables
} from 'event-analysis/queries/EventAttributeDefinitionQuery';
import {
	EventDefinitionData,
	EventDefinitionVariables,
	UpdateEventDefinitionVariables
} from 'event-analysis/queries/EventDefinitionQuery';
import {Modal as ModalType} from 'shared/types/Modal';
import {SafeResults} from 'shared/hoc/util';
import {sequence} from 'shared/util/promise';
import {sub} from 'shared/util/lang';
import {useMutation, useQuery} from '@apollo/react-hooks';

const DATA_TYPE_OPTIONS = [
	DataTypes.Boolean,
	DataTypes.Date,
	DataTypes.Duration,
	DataTypes.Number,
	DataTypes.String
];

const connector = connect(null, {addAlert});

type PropsFromRedux = ConnectedProps<typeof connector>;

interface IEditAttributeEventModalProps extends PropsFromRedux {
	id: string;
	mutation: DocumentNode;
	onClose: (save: boolean) => ModalType.actionTypes;
	query: DocumentNode;
	showTypecast?: boolean;
}

const EditAttributeEventModal: React.FC<IEditAttributeEventModalProps> = ({
	addAlert,
	id,
	mutation,
	onClose,
	query,
	showTypecast
}) => {
	const [update] = useMutation<
		EventDefinitionData | EventAttributeDefinitionData,
		UpdateEventDefinitionVariables | UpdateEventAttributeDefinitionVariables
	>(mutation);
	const result = useQuery<
		EventDefinitionData | EventAttributeDefinitionData,
		EventDefinitionVariables | EventAttributeDefinitionVariables
	>(query, {
		fetchPolicy: 'no-cache',
		variables: {id}
	});

	const dataMapper = get(
		showTypecast ? 'eventAttributeDefinition' : 'eventDefinition'
	);

	const validateDisplayName = debounce(250)(
		(value: string): Promise<string> => {
			let error = '';

			if (value && value !== dataMapper(result.data).displayName) {
				return client
					.query({
						fetchPolicy: 'no-cache',
						query,
						variables: {displayName: value.trim()}
					})
					.then(({data}) => {
						if (dataMapper(data)) {
							error = sub(
								Liferay.Language.get(
									'an-x-already-exists-with-that-display-name.-please-enter-a-different-display-name'
								),
								[
									showTypecast
										? Liferay.Language.get(
												'attribute'
										  ).toLowerCase()
										: Liferay.Language.get(
												'event'
										  ).toLowerCase()
								]
							) as string;
						}

						return error;
					})
					.catch(() => {
						error = Liferay.Language.get(
							'there-was-an-error-processing-your-request.-please-try-again'
						);

						return error;
					});
			} else {
				return toPromise(error);
			}
		}
	);

	return (
		<Modal>
			<SafeResults {...result} page={false} pageDisplay={false}>
				{(item: {
					eventAttributeDefinition: Attribute;
					eventDefinition: Event;
				}) => {
					const {
						dataType,
						description,
						displayName,
						name
					} = dataMapper(item);

					return (
						<>
							<Modal.Header
								onClose={() => onClose(false)}
								title={
									displayName
										? `${name} - ${displayName}`
										: name
								}
							/>

							<Form
								initialValues={
									showTypecast
										? {
												dataType,
												description: description || '',
												displayName:
													displayName || name || ''
										  }
										: {
												description: description || '',
												displayName:
													displayName || name || ''
										  }
								}
								onSubmit={(variables, {setSubmitting}) => {
									update({
										variables: {
											id,
											...variables
										}
									})
										.then(({data}) => {
											const {displayName, name} = get(
												showTypecast
													? 'updateEventAttributeDefinition'
													: 'updateEventDefinition'
											)(data);
											addAlert({
												alertType: Alert.Types.Success,
												message: sub(
													Liferay.Language.get(
														'x-has-been-updated'
													),
													[displayName || name]
												) as string
											});

											setSubmitting(false);

											onClose(true);
										})
										.catch(() => {
											addAlert({
												alertType: Alert.Types.Error,
												message: Liferay.Language.get(
													'there-was-an-error-processing-your-request.-please-try-again'
												)
											});

											setSubmitting(false);
										});
								}}
							>
								{({handleSubmit, isSubmitting, isValid}) => (
									<Form.Form onSubmit={handleSubmit}>
										<Modal.Body>
											<Form.Group autoFit>
												<Form.GroupItem>
													<Form.Input
														label={Liferay.Language.get(
															'display-name'
														)}
														name='displayName'
														required
														type='text'
														validate={sequence([
															validateRequired,
															validateMaxLength(
																255
															),
															validateDisplayName
														])}
													/>
												</Form.GroupItem>
											</Form.Group>

											<Form.Group autoFit>
												<Form.GroupItem>
													<Form.Input
														label={Liferay.Language.get(
															'description'
														)}
														name='description'
														type='textarea'
														validate={sequence([
															validateMaxLength(
																255
															)
														])}
													/>
												</Form.GroupItem>
											</Form.Group>

											{showTypecast && (
												<Form.Group>
													<Form.GroupItem>
														<Form.Select
															label={Liferay.Language.get(
																'default-data-typecast'
															)}
															name='dataType'
														>
															{DATA_TYPE_OPTIONS.map(
																value => (
																	<Form.Select.Item
																		key={
																			value
																		}
																		value={
																			value
																		}
																	>
																		{
																			DATA_TYPE_LABELS_MAP[
																				value
																			]
																		}
																	</Form.Select.Item>
																)
															)}
														</Form.Select>
													</Form.GroupItem>

													<Form.GroupItem className='text-secondary'>
														{Liferay.Language.get(
															'data-typecast-determines-how-attributes-can-be-analyzed'
														)}
													</Form.GroupItem>

													<Form.GroupItem className='text-secondary'>
														{Liferay.Language.get(
															'e.g.-typecasting-to-number-will-support-greater-than-or-less-than-conditions'
														)}
													</Form.GroupItem>
												</Form.Group>
											)}
										</Modal.Body>

										<Modal.Footer>
											<ClayButton
												className='button-root'
												displayType='secondary'
												onClick={() => onClose(false)}
											>
												{Liferay.Language.get('cancel')}
											</ClayButton>

											<ClayButton
												className='button-root'
												disabled={!isValid}
												displayType='primary'
												type='submit'
											>
												{isSubmitting && (
													<ClayLoadingIndicator
														className='d-inline-block mr-2'
														displayType='secondary'
														size='sm'
													/>
												)}

												{Liferay.Language.get('save')}
											</ClayButton>
										</Modal.Footer>
									</Form.Form>
								)}
							</Form>
						</>
					);
				}}
			</SafeResults>
		</Modal>
	);
};

export default connector(EditAttributeEventModal);
