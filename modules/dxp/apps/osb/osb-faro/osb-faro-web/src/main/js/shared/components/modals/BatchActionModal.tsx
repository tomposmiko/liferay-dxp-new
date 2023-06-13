import Button from 'shared/components/Button';
import Dropdown from 'shared/components/Dropdown';
import getCN from 'classnames';
import Modal from 'shared/components/modal';
import Promise from 'metal-promise';
import React, {useEffect, useState} from 'react';
import Table, {Column} from 'shared/components/table';
import {
	ACTION_TYPES,
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {fromJS, List} from 'immutable';
import {sub} from 'shared/util/lang';

interface IBatchActionModalProps extends React.HTMLAttributes<HTMLDivElement> {
	actionOptions: {
		actionCountString: string;
		options: {label: string; value: string}[];
		optionsLabel: string;
	};
	columns: Column[];
	editableAttr: string;
	fitContent: boolean;
	items: any[];
	onClose: () => void;
	onSave: (params: {
		edits: {[key: string]: string};
		ids: string[];
	}) => Promise<any>;
	title: string;
}

const BatchActionModal: React.FC<IBatchActionModalProps> = ({
	actionOptions: {actionCountString = '', options = [], optionsLabel = ''},
	className,
	columns = [],
	editableAttr = '',
	fitContent = false,
	items = [],
	onClose,
	onSave,
	title = ''
}) => {
	const [itemsIList, setItemsIList] = useState<List<any>>(fromJS(items));
	const [selectedKey, setSelectedKey] = useState(optionsLabel);

	const {
		selectedItems: selectedItemsIOMap,
		selectionDispatch
	} = useSelectionContext();

	useEffect(() => {
		items.length &&
			selectionDispatch({payload: {items}, type: ACTION_TYPES.add});
	}, []);

	const handleEdits = event => {
		const newVal = event.target.value;

		setItemsIList(
			itemsIList.map(itemIMap =>
				selectedItemsIOMap.has(itemIMap.get('id'))
					? itemIMap.set(editableAttr, newVal)
					: itemIMap
			) as List<any>
		);
		setSelectedKey(newVal);
	};

	const handleItemsChange = item => {
		selectionDispatch({payload: {item}, type: ACTION_TYPES.toggle});
	};

	const handleSave = () => {
		onSave({
			edits: {[editableAttr]: selectedKey},
			ids: selectedItemsIOMap.keySeq().toArray()
		}).then(onClose);
	};

	return (
		<Modal
			className={getCN(
				className,
				'scroll-container',
				'batch-action-modal-root',
				{
					'fit-content': fitContent
				}
			)}
			size='lg'
		>
			<Modal.Header onClose={onClose} title={title} />

			<Modal.Body>
				<div>
					<Dropdown label={selectedKey}>
						{options.map(option => (
							<Dropdown.Item
								hideOnClick
								key={option.value}
								onClick={handleEdits}
								value={option.value}
							>
								{option.label}
							</Dropdown.Item>
						))}
					</Dropdown>

					<p className='text-secondary'>
						{sub(
							actionCountString,
							[
								<b key='selectedCount'>
									{selectedItemsIOMap.size}
								</b>
							],
							false
						)}
					</p>
				</div>

				<Table
					columns={columns}
					items={itemsIList.toJS()}
					onSelectItemsChange={handleItemsChange}
					rowIdentifier='id'
					selectedItemsIOMap={selectedItemsIOMap}
					showCheckbox
				/>
			</Modal.Body>

			<Modal.Footer>
				<Button onClick={onClose}>
					{Liferay.Language.get('cancel')}
				</Button>

				<Button
					disabled={
						selectedKey === optionsLabel ||
						selectedItemsIOMap.isEmpty()
					}
					display='primary'
					onClick={handleSave}
				>
					{Liferay.Language.get('save')}
				</Button>
			</Modal.Footer>
		</Modal>
	);
};

export default withSelectionProvider(BatchActionModal);
