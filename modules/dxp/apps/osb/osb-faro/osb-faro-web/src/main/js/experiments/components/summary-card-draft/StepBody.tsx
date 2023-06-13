import Card from 'shared/components/Card';
import ClayButton from '@clayui/button';
import ClayLink, {ClayLinkContext} from '@clayui/link';
import Icon from 'shared/components/Icon';
import React, {useState} from 'react';
import {CLASSNAME} from '../summary-base-card/constants';
import {DisplayType} from 'shared/types';
import {Step} from '../summary-base-card/types';
import {useModal} from '@clayui/modal';

interface ISummaryCardDraftStepBodyProps
	extends React.HTMLAttributes<HTMLElement> {
	status: string;
	step: Step & {
		buttonProps: {
			displayType: DisplayType;
			disabled: boolean;
			small: boolean;
		};
	};
}

const SummaryCardDraftStepBody: React.FC<ISummaryCardDraftStepBodyProps> = ({
	status,
	step: {buttonProps, Description, link, modal, title}
}) => {
	const [visibleModal, setVisibleModal] = useState(false);
	const {observer, onClose} = useModal({
		onClose: () => setVisibleModal(false)
	});

	buttonProps = {
		disabled: status === 'wait',
		displayType: status === 'active' ? 'primary' : 'secondary',
		small: true,
		...buttonProps
	};

	const renderModal = () => {
		const {Component, props} = modal;

		return (
			<>
				<ClayButton
					{...buttonProps}
					onClick={() => setVisibleModal(true)}
				>
					{buttonProps.symbol && (
						<Icon className='mr-2' symbol={buttonProps.symbol} />
					)}
					{buttonProps.label}
				</ClayButton>

				{visibleModal && (
					<Component
						{...props}
						observer={observer}
						onClose={onClose}
					/>
				)}
			</>
		);
	};

	const renderLink = () => (
		<ClayLinkContext.Provider
			value={({children, ...otherProps}) => (
				<a {...otherProps}>{children}</a>
			)}
		>
			<ClayLink
				className={`btn btn-sm ${
					status === 'active' ? 'btn-primary' : 'btn-secondary'
				}`}
				href={link}
				target='_blank'
			>
				{buttonProps.symbol && (
					<Icon className='mr-2' symbol={buttonProps.symbol} />
				)}
				{buttonProps.label}
			</ClayLink>
		</ClayLinkContext.Provider>
	);

	return (
		<Card
			className={`${CLASSNAME}-step-content ${CLASSNAME}-step-content-${status}`}
		>
			<Card.Body>
				<h4>{title}</h4>
				<Description
					className={`${CLASSNAME}-step-content-description`}
				/>
				{modal && renderModal()}

				{link && renderLink()}
			</Card.Body>
		</Card>
	);
};

export default SummaryCardDraftStepBody;
