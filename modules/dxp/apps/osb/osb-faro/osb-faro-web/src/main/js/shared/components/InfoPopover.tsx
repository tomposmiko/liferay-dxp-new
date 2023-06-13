import ClayIcon from '@clayui/icon';
import getCN from 'classnames';
import Popover from 'shared/components/Popover';
import React, {useRef, useState} from 'react';
import ReactDOM from 'react-dom';

interface IInfoPopoverProps extends React.HTMLAttributes<HTMLElement> {
	content?: React.ReactText;
	popOverAttr?: {className: string};
	title?: string;
}

const InfoPopover: React.FC<IInfoPopoverProps> = ({
	className,
	content,
	popOverAttr,
	title
}) => {
	const _iconSpanRef = useRef();

	const [showPopover, setShowPopover] = useState(false);

	return (
		<>
			<span
				className={getCN('info-popover-root', className)}
				onBlur={() => setShowPopover(false)}
				onFocus={() => setShowPopover(true)}
				onMouseOut={() => setShowPopover(false)}
				onMouseOver={() => setShowPopover(true)}
				ref={_iconSpanRef}
			>
				<ClayIcon className='icon-root' symbol='question-circle-full' />
			</span>

			{ReactDOM.createPortal(
				<Popover
					alignElement={_iconSpanRef.current}
					content={content}
					title={title}
					visible={showPopover}
					{...popOverAttr}
				/>,
				document.querySelector('body.dxp')
			)}
		</>
	);
};

export default InfoPopover;
