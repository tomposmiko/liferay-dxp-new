import infoCircleIcon from '../../assets/icons/info-circle-icon.svg';
import {Input} from '../../components/Input/Input';
import {BillingAddress} from './BillingAddress';
import {PaymentMethodMode} from './PaymentMethodMode';
import {PaymentMethodSelector} from './PaymentMethodSelector';
import {TrialTimeline} from './TrialTimeline';

export function SelectPaymentMethod({
	addresses,
	billingAddress,
	email,
	enableTrialMethod,
	phoneNumber,
	purchaseOrderNumber,
	selectedAddress,
	selectedPaymentMethod,
	setBillingAddress,
	setEmail,
	setPurchaseOrderNumber,
	setSelectedAddress,
	setSelectedPaymentMethod,
	setShowNewAddressButton,
	showNewAddressButton,
}: {
	addresses: PostalAddressResponse[];
	billingAddress: BillingAddress;
	email: string;
	enableTrialMethod: string;
	phoneNumber: string;
	purchaseOrderNumber: string;
	selectedAddress: string;
	selectedPaymentMethod: PaymentMethodSelector;
	setBillingAddress: (value: BillingAddress) => void;
	setEmail: (value: string) => void;
	setSelectedAddress: (value: string) => void;
	setSelectedPaymentMethod: (value: PaymentMethodSelector) => void;
	setShowNewAddressButton: (value: boolean) => void;
	setPurchaseOrderNumber: (value: string) => void;
	showNewAddressButton: boolean;
}) {
	return (
		<>
			<div className="get-app-modal-payment-methods">
				<div className="get-app-modal-payment-methods-container">
					<PaymentMethodSelector
						enableTrial={enableTrialMethod}
						selectedPaymentMethod={selectedPaymentMethod as string}
						setSelectedPaymentMethod={setSelectedPaymentMethod}
					/>
				</div>
			</div>

			{selectedPaymentMethod === 'trial' && <TrialTimeline />}

			{selectedPaymentMethod === 'pay' && (
				<PaymentMethodMode
					selectedPaymentMethod={selectedPaymentMethod}
				/>
			)}

			{selectedPaymentMethod === 'order' && (
				<>
					<Input
						label="Purchase order number"
						onChange={({target}) =>
							setPurchaseOrderNumber(target.value)
						}
						required
						value={purchaseOrderNumber}
					/>

					<Input
						label="Email Address"
						onChange={({target}) => setEmail(target.value)}
						required
						value={email}
					/>
				</>
			)}

			<BillingAddress
				addresses={addresses}
				billingAddress={billingAddress}
				phoneNumber={phoneNumber}
				selectedAddress={selectedAddress}
				setBillingAddress={setBillingAddress}
				setSelectedAddress={setSelectedAddress}
				setShowNewAddressButton={setShowNewAddressButton}
				showNewAddressButton={showNewAddressButton}
			/>

			<img
				alt="Account icon"
				className="get-app-modal-info-icon"
				src={infoCircleIcon}
			/>

			<span className="get-app-modal-use-terms">
				Terms, privacy, returns, or contact support. All costs are in US
				Dollars
			</span>
		</>
	);
}
