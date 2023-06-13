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

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import ClayLabel from '@clayui/label';
import {useModal} from '@clayui/modal';
import ClayPopover from '@clayui/popover';
import {openConfirmModal, sub} from 'frontend-js-web';
import PropTypes from 'prop-types';
import React, {useCallback, useEffect, useMemo, useState} from 'react';

import {COLLECTION_APPLIED_FILTERS_FRAGMENT_ENTRY_KEY} from '../../../../../../../app/config/constants/collectionAppliedFiltersFragmentKey';
import {COLLECTION_FILTER_FRAGMENT_ENTRY_KEY} from '../../../../../../../app/config/constants/collectionFilterFragmentEntryKey';
import {COMMON_STYLES_ROLES} from '../../../../../../../app/config/constants/commonStylesRoles';
import {CONTENT_DISPLAY_OPTIONS} from '../../../../../../../app/config/constants/contentDisplayOptions';
import {FREEMARKER_FRAGMENT_ENTRY_PROCESSOR} from '../../../../../../../app/config/constants/freemarkerFragmentEntryProcessor';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../../../../../app/config/constants/layoutDataItemTypes';
import {VIEWPORT_SIZES} from '../../../../../../../app/config/constants/viewportSizes';
import {config} from '../../../../../../../app/config/index';
import {useDisplayPagePreviewItem} from '../../../../../../../app/contexts/DisplayPagePreviewItemContext';
import {
	useDispatch,
	useGetState,
	useSelector,
} from '../../../../../../../app/contexts/StoreContext';
import selectSegmentsExperienceId from '../../../../../../../app/selectors/selectSegmentsExperienceId';
import CollectionService from '../../../../../../../app/services/CollectionService';
import InfoItemService from '../../../../../../../app/services/InfoItemService';
import updateCollectionDisplayCollection from '../../../../../../../app/thunks/updateCollectionDisplayCollection';
import updateItemConfig from '../../../../../../../app/thunks/updateItemConfig';
import {CACHE_KEYS} from '../../../../../../../app/utils/cache';
import {getResponsiveConfig} from '../../../../../../../app/utils/getResponsiveConfig';
import useCache from '../../../../../../../app/utils/useCache';
import Collapse from '../../../../../../../common/components/Collapse';
import CollectionSelector from '../../../../../../../common/components/CollectionSelector';
import {useId} from '../../../../../../../common/hooks/useId';
import CollectionFilterConfigurationModal from '../../CollectionFilterConfigurationModal';
import {CommonStyles} from '../CommonStyles';
import {FlexOptions} from '../FlexOptions';
import {EmptyCollectionOptions} from './EmptyCollectionOptions';
import {LayoutSelector} from './LayoutSelector';
import {ListItemStyleSelector} from './ListItemStyleSelector';
import {NoPaginationOptions} from './NoPaginationOptions';
import {PaginationOptions} from './PaginationOptions';
import {PaginationSelector} from './PaginationSelector';
import {ShowGutterSelector} from './ShowGutterSelector';
import {StyleDisplaySelector} from './StyleDisplaySelector';
import {VerticalAlignmentSelector} from './VerticalAlignmentSelector';

const LIST_STYLES = {
	flexColumn: CONTENT_DISPLAY_OPTIONS.flexColumn,
	flexRow: CONTENT_DISPLAY_OPTIONS.flexRow,
	grid: '',
};

export function CollectionGeneralPanel({item}) {
	const {
		collection,
		displayAllItems,
		displayAllPages,
		emptyCollectionOptions,
		listStyle,
		numberOfColumns,
		numberOfItems: initialNumberOfItems,
		numberOfItemsPerPage: initialNumberOfItemsPerPage,
		numberOfPages: initialNumberOfPages,
		paginationType,
	} = item.config;

	const collectionItemType = collection?.itemType || null;
	const flexEnabled =
		listStyle === LIST_STYLES.flexColumn ||
		listStyle === LIST_STYLES.flexRow;

	const restrictedItemIds = new Set(config.restrictedItemIds);
	const segmentsExperienceId = useSelector(selectSegmentsExperienceId);
	const selectedViewportSize = useSelector(
		(state) => state.selectedViewportSize
	);

	const [availableListItemStyles, setAvailableListItemStyles] = useState([]);
	const [collectionConfiguration, setCollectionConfiguration] = useState(
		null
	);
	const [
		filterConfigurationVisible,
		setFilterConfigurationVisible,
	] = useState(false);

	const collectionConfig = getResponsiveConfig(
		item.config,
		selectedViewportSize
	);

	const collectionEmptyCollectionMessageId = useId();
	const collectionLayoutId = useId();
	const collectionListItemStyleId = useId();
	const collectionPaginationTypeId = useId();
	const collectionVerticalAlignmentId = useId();

	const dispatch = useDispatch();
	const getState = useGetState();

	const {
		observer: filterConfigurationObserver,
		onClose: onFilterConfigurationClose,
	} = useModal({onClose: () => setFilterConfigurationVisible(false)});

	const editConfigurationURL = useCache({
		fetcher: () =>
			CollectionService.getCollectionEditConfigurationUrl({
				collectionKey: collection?.key,
				itemId: item.itemId,
				segmentsExperienceId,
			}).then(({url}) => url),
		key: [CACHE_KEYS.collectionConfigurationUrl, collection?.key],
	});

	const previewItem = useDisplayPagePreviewItem();

	const optionsMenuItems = useMemo(() => {
		if (Liferay.FeatureFlags['LPS-166036']) {
			if (!editConfigurationURL) {
				return [];
			}

			const url = new URL(editConfigurationURL);

			url.searchParams.set(
				`${config.portletNamespace}type`,
				collection.type
			);

			if (previewItem) {
				url.searchParams.set(
					`${config.portletNamespace}classNameId`,
					previewItem.data.classNameId
				);

				url.searchParams.set(
					`${config.portletNamespace}classPK`,
					previewItem.data.classPK
				);
			}

			return [
				{
					href: url.href,
					label: Liferay.Language.get('filter-collection'),
					symbolLeft: 'filter',
				},
			];
		}
		else {
			return collectionConfiguration
				? [
						{
							label: Liferay.Language.get('prefilter-collection'),
							onClick: () => setFilterConfigurationVisible(true),
							symbolLeft: 'filter',
						},
				  ]
				: [];
		}
	}, [
		collection,
		collectionConfiguration,
		editConfigurationURL,
		previewItem,
		setFilterConfigurationVisible,
	]);

	const handleCollectionSelect = (collection = {}) => {
		dispatch(
			updateCollectionDisplayCollection({
				collection: Object.keys(collection).length ? collection : null,
				itemId: item.itemId,
				listStyle: LIST_STYLES.grid,
			})
		);
	};

	const handleConfigurationChanged = useCallback(
		(itemConfig) => {
			if (selectedViewportSize !== VIEWPORT_SIZES.desktop) {
				itemConfig = {[selectedViewportSize]: itemConfig};
			}

			dispatch(
				updateItemConfig({
					itemConfig,
					itemId: item.itemId,
				})
			);
		},
		[item.itemId, dispatch, selectedViewportSize]
	);

	const onBeforeCollectionSelect = useCallback(
		({preventDefault}) => {
			const state = getState();

			const isLinkedToFilter = Object.values(state.layoutData.items).some(
				(layoutDataItem) => {
					if (
						layoutDataItem.type !== LAYOUT_DATA_ITEM_TYPES.fragment
					) {
						return false;
					}

					const fragmentEntryLink =
						state.fragmentEntryLinks[
							layoutDataItem.config.fragmentEntryLinkId
						];

					if (
						fragmentEntryLink.fragmentEntryKey !==
							COLLECTION_FILTER_FRAGMENT_ENTRY_KEY &&
						fragmentEntryLink.fragmentEntryKey !==
							COLLECTION_APPLIED_FILTERS_FRAGMENT_ENTRY_KEY
					) {
						return false;
					}

					return fragmentEntryLink.editableValues[
						FREEMARKER_FRAGMENT_ENTRY_PROCESSOR
					]?.targetCollections?.includes(item.itemId);
				}
			);

			if (isLinkedToFilter) {
				openConfirmModal({
					message: `${Liferay.Language.get(
						'if-you-change-the-collection-you-unlink-the-collection-filter'
					)}\n\n${Liferay.Language.get('do-you-want-to-continue')}`,
					onConfirm: (isConfirmed) => {
						if (!isConfirmed) {
							preventDefault();
						}
					},
				});
			}
		},
		[getState, item.itemId]
	);

	useEffect(() => {
		if (
			collection &&
			listStyle &&
			!Object.values(LIST_STYLES).includes(listStyle)
		) {
			InfoItemService.getAvailableListItemRenderers({
				itemSubtype: collection.itemSubtype,
				itemType: collection.itemType,
				listStyle,
			})
				.then((response) => {
					setAvailableListItemStyles(response);
				})
				.catch(() => {
					setAvailableListItemStyles([]);
				});
		}
	}, [collection, listStyle]);

	useEffect(() => {
		if (collection?.key) {
			CollectionService.getCollectionConfiguration(collection)
				.then((nextCollectionConfiguration) => {
					if (
						nextCollectionConfiguration?.fieldSets.some(
							(fieldSet) => fieldSet?.fields?.length
						)
					) {
						setCollectionConfiguration(nextCollectionConfiguration);
					}
					else {
						setCollectionConfiguration(null);
					}
				})
				.catch(() => setCollectionConfiguration(null));
		}
		else {
			setCollectionConfiguration(null);
		}
	}, [collection]);

	if (
		Liferay.FeatureFlags['LPS-169923'] &&
		restrictedItemIds.has(item.itemId)
	) {
		return (
			<ClayAlert displayType="secondary" role={null}>
				{Liferay.Language.get(
					'this-content-cannot-be-displayed-due-to-permission-restrictions'
				)}
			</ClayAlert>
		);
	}

	return (
		<>
			<div className="mb-3">
				<Collapse
					label={Liferay.Language.get('collection-display-options')}
					open
				>
					{selectedViewportSize === VIEWPORT_SIZES.desktop && (
						<>
							<CollectionSelector
								collectionItem={collection}
								itemSelectorURL={config.collectionSelectorURL}
								label={Liferay.Language.get('collection')}
								onBeforeCollectionSelect={
									onBeforeCollectionSelect
								}
								onCollectionSelect={handleCollectionSelect}
								optionsMenuItems={optionsMenuItems}
							/>

							{collection?.classPK && (
								<VariationsPopover
									classPK={collection.classPK}
								/>
							)}
						</>
					)}

					{collection && (
						<>
							{selectedViewportSize ===
								VIEWPORT_SIZES.desktop && (
								<StyleDisplaySelector
									collectionItemType={collectionItemType}
									handleConfigurationChanged={
										handleConfigurationChanged
									}
									listStyle={listStyle}
								/>
							)}

							{flexEnabled && (
								<FlexOptions
									itemConfig={collectionConfig}
									onConfigChange={(name, value) => {
										handleConfigurationChanged({
											[name]: value,
										});
									}}
								/>
							)}

							{listStyle === LIST_STYLES.grid && (
								<>
									<LayoutSelector
										collectionConfig={collectionConfig}
										collectionLayoutId={collectionLayoutId}
										handleConfigurationChanged={
											handleConfigurationChanged
										}
									/>

									{selectedViewportSize ===
										VIEWPORT_SIZES.desktop && (
										<>
											{numberOfColumns > 1 && (
												<ShowGutterSelector
													checked={
														item.config.gutters
													}
													handleConfigurationChanged={
														handleConfigurationChanged
													}
												/>
											)}

											<VerticalAlignmentSelector
												collectionVerticalAlignmentId={
													collectionVerticalAlignmentId
												}
												handleConfigurationChanged={
													handleConfigurationChanged
												}
												value={
													item.config
														.verticalAlignment
												}
											/>
										</>
									)}
								</>
							)}

							{selectedViewportSize ===
								VIEWPORT_SIZES.desktop && (
								<>
									{listStyle !== LIST_STYLES.grid &&
										!!availableListItemStyles.length && (
											<ListItemStyleSelector
												availableListItemStyles={
													availableListItemStyles
												}
												collectionListItemStyleId={
													collectionListItemStyleId
												}
												handleConfigurationChanged={
													handleConfigurationChanged
												}
												item={item}
											/>
										)}

									<EmptyCollectionOptions
										collectionEmptyCollectionMessageId={
											collectionEmptyCollectionMessageId
										}
										emptyCollectionOptions={
											emptyCollectionOptions
										}
										handleConfigurationChanged={
											handleConfigurationChanged
										}
									/>

									<PaginationSelector
										collectionPaginationTypeId={
											collectionPaginationTypeId
										}
										handleConfigurationChanged={
											handleConfigurationChanged
										}
										value={paginationType || 'none'}
									/>

									{paginationType !== 'none' ? (
										<PaginationOptions
											displayAllPages={displayAllPages}
											handleConfigurationChanged={
												handleConfigurationChanged
											}
											initialNumberOfItemsPerPage={
												initialNumberOfItemsPerPage
											}
											initialNumberOfPages={
												initialNumberOfPages
											}
										/>
									) : (
										<NoPaginationOptions
											collection={collection}
											displayAllItems={displayAllItems}
											handleConfigurationChanged={
												handleConfigurationChanged
											}
											initialNumberOfItems={
												initialNumberOfItems
											}
										/>
									)}
								</>
							)}
						</>
					)}
				</Collapse>

				{filterConfigurationVisible ? (
					<CollectionFilterConfigurationModal
						collectionConfiguration={collectionConfiguration}
						handleConfigurationChanged={handleConfigurationChanged}
						itemConfig={item.config}
						observer={filterConfigurationObserver}
						onClose={onFilterConfigurationClose}
					/>
				) : null}
			</div>

			<CommonStyles
				commonStylesValues={collectionConfig.styles}
				item={item}
				role={COMMON_STYLES_ROLES.general}
			/>
		</>
	);
}

CollectionGeneralPanel.propTypes = {
	item: PropTypes.object.isRequired,
};

function VariationsPopover({classPK}) {
	const popoverId = useId();

	const variations = useCache({
		fetcher: () => CollectionService.getCollectionVariations(classPK),
		key: [CACHE_KEYS.collectionVariations, classPK],
	});

	if (!variations?.length) {
		return null;
	}

	return (
		<ClayPopover
			alignPosition="bottom"
			closeOnClickOutside
			header={
				<span className="font-weight-bold">
					{Liferay.Language.get('collection-variations')}
				</span>
			}
			id={popoverId}
			trigger={
				<ClayButton
					borderless
					className="mb-0 mt-2"
					displayType="secondary"
					size="xs"
				>
					{sub(
						Liferay.Language.get('x-variations'),
						variations.length
					)}
				</ClayButton>
			}
		>
			{variations.map((variation, index) => (
				<ClayLabel
					className="mb-2 mr-2"
					displayType="secondary"
					key={index}
				>
					{variation}
				</ClayLabel>
			))}
		</ClayPopover>
	);
}

VariationsPopover.propTypes = {
	classPK: PropTypes.string.isRequired,
};
