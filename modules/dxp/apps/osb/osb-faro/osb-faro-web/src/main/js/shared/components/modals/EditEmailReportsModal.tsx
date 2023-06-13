import Button from '../Button';
import Form from 'shared/components/form';
import Modal from '../modal';
import React from 'react';
import {Frequency, Report} from 'settings/channels/components/EmailReports';
import {Modal as ModalTypes} from 'shared/types';

interface IEditEmailReportsModalProps
	extends React.HTMLAttributes<HTMLElement> {
	onCancel: ModalTypes.close;
	onSave: (report: Report) => void;
	report: Report | null;
}

const FREQUENCIES = {
	[Frequency.Daily]: Liferay.Language.get('daily'),
	[Frequency.Weekly]: Liferay.Language.get('weekly'),
	[Frequency.Monthly]: Liferay.Language.get('monthly')
};

const EditEmailReportsModal: React.FC<IEditEmailReportsModalProps> = ({
	onCancel,
	onSave,
	report
}) => (
	<Modal>
		<Modal.Header title={Liferay.Language.get('configure-email-reports')} />

		<Form
			initialValues={{
				...report
			}}
			onSubmit={onSave}
		>
			{({handleSubmit, isSubmitting, isValid, values}) => (
				<Form.Form onSubmit={handleSubmit}>
					<Modal.Body>
						<Form.Group
							autoFit
							className='align-items-center d-flex'
						>
							<div>
								<div className='font-weight-semibold mb-2'>
									{Liferay.Language.get(
										'enable-email-reports'
									)}
								</div>

								<div className='text-secondary'>
									{Liferay.Language.get(
										'enable-email-reports-to-configure-how-frequently-you-would-like-to-receive-notifications-about-this-property-activities.-other-users-will-not-be-affected-by-this-action'
									)}
								</div>
							</div>
							<div>
								<Form.GroupItem className='ml-3'>
									<Form.ToggleSwitch name='enabled' />
								</Form.GroupItem>
							</div>
						</Form.Group>
						<Form.Group autoFit>
							<Form.GroupItem>
								<Form.Select
									disabled={!values.enabled}
									label={Liferay.Language.get('frequency')}
									name='frequency'
								>
									{Object.keys(FREQUENCIES).map(frequency => (
										<Form.Select.Item
											key={frequency}
											value={frequency}
										>
											{FREQUENCIES[frequency]}
										</Form.Select.Item>
									))}
								</Form.Select>
							</Form.GroupItem>
						</Form.Group>
					</Modal.Body>
					<Modal.Footer>
						<Button display='secondary' onClick={onCancel}>
							{Liferay.Language.get('cancel')}
						</Button>

						<Button
							disabled={isSubmitting || !isValid}
							display='primary'
							loading={isSubmitting}
							type='submit'
						>
							{isSubmitting
								? Liferay.Language.get('saving')
								: Liferay.Language.get('save')}
						</Button>
					</Modal.Footer>
				</Form.Form>
			)}
		</Form>
	</Modal>
);

export default EditEmailReportsModal;
