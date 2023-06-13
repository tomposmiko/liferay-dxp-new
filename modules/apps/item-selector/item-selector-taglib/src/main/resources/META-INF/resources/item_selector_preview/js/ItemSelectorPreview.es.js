/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import {useIsMounted} from 'frontend-js-react-web';
import PropTypes from 'prop-types';
import React, {useCallback, useEffect, useState} from 'react';
import ReactDOM from 'react-dom';

import Carousel from './Carousel.es';
import Footer from './Footer.es';
import Header from './Header.es';

const KEY_CODE = {
	ESC: 27,
	LEFT: 37,
	RIGTH: 39,
};

const itemIsImage = ({mimeType, type}) =>
	type === 'image' || Boolean(mimeType?.match(/image.*/));

const ItemSelectorPreview = ({
	container,
	currentIndex = 0,
	editItemURL,
	handleSelectedItem,
	headerTitle,
	items,
	uploadItemReturnType,
	uploadItemURL,
}) => {
	const [currentItemIndex, setCurrentItemIndex] = useState(currentIndex);
	const [isImage, setIsImage] = useState(itemIsImage(items[currentIndex]));
	const [itemList, setItemList] = useState(items);
	const [reloadOnHide, setReloadOnHide] = useState(false);

	const infoButtonRef = React.createRef();

	const isMounted = useIsMounted();

	const close = useCallback(() => {
		ReactDOM.unmountComponentAtNode(container);
	}, [container]);

	const handleClickNext = useCallback(() => {
		if (itemList.length > 1) {
			setCurrentItemIndex((index) => {
				const lastIndex = itemList.length - 1;
				const shouldResetIndex = index === lastIndex;

				return shouldResetIndex ? 0 : index + 1;
			});
		}
	}, [itemList.length]);

	const handleClickPrevious = useCallback(() => {
		if (itemList.length > 1) {
			setCurrentItemIndex((index) => {
				const lastIndex = itemList.length - 1;
				const shouldResetIndex = index === 0;

				return shouldResetIndex ? lastIndex : index - 1;
			});
		}
	}, [itemList.length]);

	const handleOnKeyDown = useCallback(
		(e) => {
			if (!isMounted()) {
				return;
			}

			switch (e.which || e.keyCode) {
				case KEY_CODE.LEFT:
					handleClickPrevious();
					break;
				case KEY_CODE.RIGTH:
					handleClickNext();
					break;
				case KEY_CODE.ESC:
					e.preventDefault();
					e.stopPropagation();
					close();
					break;
				default:
					break;
			}
		},
		[close, handleClickNext, handleClickPrevious, isMounted]
	);

	const currentItem = itemList[currentItemIndex];

	const updateItemList = (newItemList) => {
		setItemList(newItemList);
		setReloadOnHide(true);
	};

	const updateCurrentItem = useCallback(
		({url, value}) => {
			if (isMounted()) {
				const newItemList = [...itemList];

				newItemList[currentItemIndex] = {...currentItem, url, value};

				updateItemList(newItemList);
			}
		},
		[currentItem, currentItemIndex, isMounted, itemList]
	);

	useEffect(() => {
		document.documentElement.addEventListener('keydown', handleOnKeyDown);

		const updateCurrentItemHandler = Liferay.on(
			'updateCurrentItem',
			updateCurrentItem
		);

		Liferay.component('ItemSelectorPreview', ItemSelectorPreview);

		return () => {
			document.documentElement.removeEventListener(
				'keydown',
				handleOnKeyDown
			);

			Liferay.detach(updateCurrentItemHandler);
			Liferay.component('ItemSelectorPreview', null);
		};
	}, [handleOnKeyDown, updateCurrentItem]);

	useEffect(() => {
		const sidenavToggle = infoButtonRef.current;

		if (sidenavToggle) {
			Liferay.SideNavigation.initialize(sidenavToggle, {
				container: '.sidenav-container',
				position: 'right',
				typeMobile: 'fixed',
				width: '320px',
			});
		}

		return () => {
			Liferay.SideNavigation.destroy(sidenavToggle);
		};
	}, [infoButtonRef]);

	const handleClickBack = () => {
		close();

		if (reloadOnHide) {
			const frame = window.frameElement;

			if (frame) {
				frame.contentWindow.location.reload();
			}
		}
	};

	const handleClickDone = () => {

		// LPS-120692

		close();

		handleSelectedItem(currentItem);
	};

	const handleSaveEdit = (e) => {
		const itemData = e.data.file;

		const editedItemMetadata = {
			groups: [
				{
					data: [
						{
							key: Liferay.Language.get('format'),
							value: itemData.type,
						},
						{
							key: Liferay.Language.get('name'),
							value: itemData.title,
						},
					],
					title: Liferay.Language.get('file-info'),
				},
			],
		};

		const editedItem = {
			fileentryid: currentItem.fileentryid,
			metadata: JSON.stringify(editedItemMetadata),
			returntype: uploadItemReturnType,
			title: itemData.title,
			url: itemData.url,
			value: itemData.resolvedValue,
		};

		const updatedItemList = [...itemList, editedItem];
		updateItemList(updatedItemList);
		setCurrentItemIndex(updatedItemList.length - 1);
	};

	const handleClickEdit = () => {
		const itemTitle = currentItem.title;
		const editDialogTitle = `${Liferay.Language.get(
			'edit'
		)} ${itemTitle} (${Liferay.Language.get('copy')})`;

		const editEntityBaseZIndex = Liferay.zIndex.WINDOW;

		Liferay.Util.editEntity(
			{
				dialog: {
					destroyOnHide: true,
					zIndex: editEntityBaseZIndex + 100,
				},
				id: 'Edit_' + itemTitle,
				stack: false,
				title: editDialogTitle,
				uri: editItemURL,
				urlParams: {
					entityURL: currentItem.url,
					saveFileEntryId: currentItem.fileentryid,
					saveFileName: itemTitle,
					saveParamName: 'imageSelectorFileName',
					saveURL: uploadItemURL,
				},
			},
			handleSaveEdit
		);
	};

	useEffect(() => {
		setIsImage(itemIsImage(currentItem));
	}, [currentItem]);

	return (
		<div className="fullscreen item-selector-preview">
			<Header
				disabledAddButton={!currentItem.url}
				handleClickAdd={handleClickDone}
				handleClickBack={handleClickBack}
				handleClickEdit={handleClickEdit}
				headerTitle={headerTitle}
				infoButtonRef={infoButtonRef}
				showEditIcon={isImage}
				showInfoIcon={!!currentItem.metadata}
			/>

			<Carousel
				currentItem={currentItem}
				handleClickNext={handleClickNext}
				handleClickPrevious={handleClickPrevious}
				isImage={isImage}
				showArrows={itemList.length > 1}
			/>

			<Footer
				currentIndex={currentItemIndex}
				title={currentItem.title}
				totalItems={itemList.length}
			/>
		</div>
	);
};

ItemSelectorPreview.propTypes = {
	container: PropTypes.instanceOf(Element).isRequired,
	currentIndex: PropTypes.number,
	editItemURL: PropTypes.string,
	handleSelectedItem: PropTypes.func.isRequired,
	headerTitle: PropTypes.string.isRequired,
	items: PropTypes.arrayOf(
		PropTypes.shape({
			base64: PropTypes.string,
			metadata: PropTypes.string,
			returntype: PropTypes.string.isRequired,
			title: PropTypes.string.isRequired,
			url: PropTypes.string,
			value: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
		})
	).isRequired,
	uploadItemReturnType: PropTypes.string,
	uploadItemURL: PropTypes.string,
};

export default ItemSelectorPreview;
