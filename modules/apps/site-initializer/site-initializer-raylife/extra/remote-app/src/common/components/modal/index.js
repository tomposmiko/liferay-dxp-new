import ClayIcon from '@clayui/icon';
import classNames from 'classnames';
import {useEffect} from 'react';
import useImperativeDisableScroll from '../../hooks/useImperativeDisableScroll';

const MODAL_BACKDROP_ID = 'modal-backdrop';

const Modal = ({
	children,
	footer,
	onClose = () => {},
	size = 'medium',
	show,
}) => {
	useImperativeDisableScroll({disabled: show, element: document.body});

	useEffect(() => {
		const clickOutsideEventListener = (event) => {
			const [firstPath] = event.path ||
				(event.composedPath && event.composedPath()) || [{}];

			if (firstPath.id === MODAL_BACKDROP_ID) {
				onClose();
			}
		};

		if (show) {
			document.addEventListener('mousedown', clickOutsideEventListener);
			document.addEventListener('touchstart', clickOutsideEventListener);
		}

		return () => {
			document.removeEventListener(
				'mousedown',
				clickOutsideEventListener
			);
			document.removeEventListener(
				'touchstart',
				clickOutsideEventListener
			);
		};
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [show]);

	return (
		<div
			className={classNames(
				'h-100 w-100 overflow-auto position-fixed backdrop',
				{
					'd-block': show,
					'd-none': !show,
				}
			)}
			id={MODAL_BACKDROP_ID}
		>
			<div
				className={classNames(
					'align-items-stretch bg-neutral-0 d-flex flex-column m-auto modal-content-body position-absolute pb-4 pt-3 px-3 rounded',
					{
						'modal-large': size === 'large',
						'modal-medium': size === 'medium',
						'modal-small': size === 'small',
					}
				)}
			>
				<div className="align-items-center border-bottom-0 d-flex justify-content-end p-0">
					<div className="close-modal" onClick={onClose}>
						<ClayIcon symbol="times" />
					</div>
				</div>

				{children}

				{footer}
			</div>
		</div>
	);
};

export default Modal;
