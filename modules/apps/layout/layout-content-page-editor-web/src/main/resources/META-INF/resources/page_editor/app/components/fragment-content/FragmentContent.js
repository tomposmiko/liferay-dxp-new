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

import {useIsMounted} from '@liferay/frontend-js-react-web';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import React, {useCallback, useEffect, useState} from 'react';

import {
	useGetContent,
	useGetFieldValue,
	useToControlsId,
} from '../../contexts/CollectionItemContext';
import {useIsProcessorEnabled} from '../../contexts/EditableProcessorContext';
import {useGlobalContext} from '../../contexts/GlobalContext';
import {
	useDispatch,
	useSelector,
	useSelectorCallback,
} from '../../contexts/StoreContext';
import selectCanConfigureWidgets from '../../selectors/selectCanConfigureWidgets';
import selectLanguageId from '../../selectors/selectLanguageId';
import selectSegmentsExperienceId from '../../selectors/selectSegmentsExperienceId';
import resolveEditableConfig from '../../utils/editable-value/resolveEditableConfig';
import resolveEditableValue from '../../utils/editable-value/resolveEditableValue';
import getLayoutDataItemCssClasses from '../../utils/getLayoutDataItemCssClasses';
import getLayoutDataItemUniqueClassName from '../../utils/getLayoutDataItemUniqueClassName';
import {getResponsiveConfig} from '../../utils/getResponsiveConfig';
import hasInnerCommonStyles from '../../utils/hasInnerCustomStyles';
import useBackgroundImageValue from '../../utils/useBackgroundImageValue';
import {useId} from '../../utils/useId';
import UnsafeHTML from '../UnsafeHTML';
import FragmentContentInteractionsFilter from './FragmentContentInteractionsFilter';
import FragmentContentProcessor from './FragmentContentProcessor';
import getAllEditables from './getAllEditables';

const FragmentContent = ({
	className,
	computeEditables,
	elementRef,
	fragmentEntryLinkId,
	getPortals,
	item,
}) => {
	const dispatch = useDispatch();
	const isMounted = useIsMounted();
	const isProcessorEnabled = useIsProcessorEnabled();
	const globalContext = useGlobalContext();
	const toControlsId = useToControlsId();
	const getFieldValue = useGetFieldValue();

	const canConfigureWidgets = useSelector(selectCanConfigureWidgets);

	const [editables, setEditables] = useState([]);

	/**
	 * Updates editables array for the rendered fragment.
	 * @param {HTMLElement} [nextFragmentElement] Fragment element
	 *  If not specified, fragmentElement state is used instead.
	 * @return {Array} Updated editables array
	 */
	const onRender = useCallback(
		(fragmentElement) => {
			if (!computeEditables) {
				return;
			}

			let nextEditables = [];

			if (isMounted()) {
				nextEditables = getAllEditables(fragmentElement).map(
					(editable) => ({
						...editable,
						fragmentEntryLinkId,
						itemId: `${fragmentEntryLinkId}-${editable.editableId}`,
						parentId: item.itemId,
					})
				);
			}

			setEditables(nextEditables);

			return nextEditables;
		},
		[isMounted, fragmentEntryLinkId, item, computeEditables]
	);

	const fragmentEntryLink = useSelectorCallback(
		(state) => state.fragmentEntryLinks[fragmentEntryLinkId],
		[fragmentEntryLinkId]
	);

	const languageId = useSelector(selectLanguageId);
	const segmentsExperienceId = useSelector(selectSegmentsExperienceId);
	const selectedViewportSize = useSelector(
		(state) => state.selectedViewportSize
	);

	const defaultContent = useGetContent(
		fragmentEntryLink,
		languageId,
		segmentsExperienceId
	);
	const [content, setContent] = useState(defaultContent);

	/* eslint-disable-next-line react-hooks/exhaustive-deps */
	const editableValues = fragmentEntryLink
		? fragmentEntryLink.editableValues
		: {};

	const fragmentEntryLinkError = fragmentEntryLink?.error;

	const cssClasses = getLayoutDataItemCssClasses(item);

	useEffect(() => {
		if (fragmentEntryLinkError) {
			throw new Error(fragmentEntryLinkError);
		}
	}, [fragmentEntryLinkError]);

	const isBeingEdited = editables.some((editable) =>
		isProcessorEnabled(toControlsId(editable.itemId))
	);

	/**
	 * fragmentElement keeps a copy of the fragment real HTML,
	 * we perform editableValues replacements over this copy
	 * to avoid multiple re-renders, when every replacement has
	 * finished, this function must be called.
	 *
	 * Synchronizes fragmentElement's content to the real fragment
	 * content. When this happens, the real re-render is performed.
	 */
	useEffect(() => {
		let fragmentElement = document.createElement('div');

		if (!isBeingEdited) {
			fragmentElement.innerHTML = defaultContent;

			if (hasInnerCommonStyles(fragmentEntryLink)) {
				const stylesElement = fragmentElement.querySelector(
					'[data-lfr-styles]'
				);

				if (stylesElement) {
					stylesElement.className = `${stylesElement.className} ${cssClasses}`;
				}
			}

			Promise.all(
				getAllEditables(fragmentElement).map((editable) => {
					const editableValue =
						editableValues[editable.editableValueNamespace][
							editable.editableId
						];

					return Promise.all([
						resolveEditableValue(
							editableValue,
							languageId,
							getFieldValue
						),
						resolveEditableConfig(
							editableValue?.config || {},
							languageId,
							getFieldValue
						),
					]).then(([value, editableConfig]) => {
						editable.processor.render(
							editable.element,
							value,
							editableConfig,
							languageId
						);

						editable.element.classList.add('page-editor__editable');
					});
				})
			).then(() => {
				if (isMounted() && fragmentElement) {
					setContent(fragmentElement.innerHTML);
				}
			});
		}

		return () => {
			fragmentElement = null;
		};
	}, [
		defaultContent,
		dispatch,
		editableValues,
		fragmentEntryLink,
		fragmentEntryLinkId,
		getFieldValue,
		isBeingEdited,
		isMounted,
		isProcessorEnabled,
		languageId,
		segmentsExperienceId,
		toControlsId,
		cssClasses,
	]);

	const responsiveConfig = getResponsiveConfig(
		item.config,
		selectedViewportSize
	);

	const {backgroundImage} = responsiveConfig.styles;

	const elementId = useId();
	const backgroundImageValue = useBackgroundImageValue(
		elementId,
		backgroundImage,
		getFieldValue
	);

	const style = {};

	if (backgroundImageValue.url) {
		style[
			`--lfr-background-image-${item.itemId}`
		] = `url(${backgroundImageValue.url})`;

		if (backgroundImage?.fileEntryId) {
			style['--background-image-file-entry-id'] =
				backgroundImage.fileEntryId;
		}
	}

	return (
		<>
			<FragmentContentInteractionsFilter
				editables={editables}
				fragmentEntryLinkId={fragmentEntryLinkId}
				itemId={item.itemId}
			>
				<UnsafeHTML
					className={classNames(
						className,
						`page-editor__fragment-content ${fragmentEntryLink?.cssClass}`,
						{
							[getLayoutDataItemCssClasses(item)]:
								Liferay.FeatureFlags['LPS-147511'] &&
								!hasInnerCommonStyles(fragmentEntryLink),
							[getLayoutDataItemUniqueClassName(
								item.itemId
							)]: !hasInnerCommonStyles(fragmentEntryLink),
							'page-editor__fragment-content--portlet-topper-hidden': !canConfigureWidgets,
						}
					)}
					contentRef={elementRef}
					data={{fragmentEntryLinkId}}
					getPortals={getPortals}
					globalContext={globalContext}
					id={elementId}
					markup={content}
					onRender={onRender}
					style={style}
				/>

				{backgroundImageValue.mediaQueries ? (
					<style>{backgroundImageValue.mediaQueries}</style>
				) : null}
			</FragmentContentInteractionsFilter>

			<FragmentContentProcessor
				editables={editables}
				fragmentEntryLinkId={fragmentEntryLinkId}
			/>
		</>
	);
};

FragmentContent.propTypes = {
	className: PropTypes.string,
	fragmentEntryLinkId: PropTypes.string.isRequired,
	getPortals: PropTypes.func.isRequired,
	item: PropTypes.object.isRequired,
};

export default React.memo(FragmentContent);
