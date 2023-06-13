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

package com.liferay.asset.auto.tagger.internal.model.listener;

import com.liferay.asset.auto.tagger.internal.AssetAutoTaggerImpl;
import com.liferay.asset.auto.tagger.internal.constants.AssetAutoTaggerDestinationNames;
import com.liferay.asset.auto.tagger.model.AssetAutoTaggerEntry;
import com.liferay.asset.auto.tagger.service.AssetAutoTaggerEntryLocalService;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationConfiguration;
import com.liferay.portal.kernel.messaging.DestinationFactory;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.concurrent.Callable;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(immediate = true, service = ModelListener.class)
public class AssetEntryModelListener extends BaseModelListener<AssetEntry> {

	@Override
	public void onAfterCreate(AssetEntry assetEntry)
		throws ModelListenerException {

		TransactionCommitCallbackUtil.registerCallback(
			(Callable<Void>)() -> {
				if (!ListUtil.isEmpty(assetEntry.getTags())) {
					return null;
				}

				if (!_assetAutoTaggerImpl.isAutoTaggable(assetEntry)) {
					return null;
				}

				Message message = new Message();

				message.setPayload(assetEntry);

				_messageBus.sendMessage(
					AssetAutoTaggerDestinationNames.ASSET_AUTO_TAGGER, message);

				return null;
			});
	}

	@Override
	public void onAfterRemoveAssociation(
			Object classPK, String associationClassName,
			Object associationClassPK)
		throws ModelListenerException {

		if (associationClassName.equals(AssetTag.class.getName())) {
			AssetAutoTaggerEntry assetAutoTaggerEntry =
				_assetAutoTaggerEntryLocalService.fetchAssetAutoTaggerEntry(
					(Long)classPK, (Long)associationClassPK);

			if (assetAutoTaggerEntry != null) {
				_assetAutoTaggerEntryLocalService.deleteAssetAutoTaggerEntry(
					assetAutoTaggerEntry);
			}
		}
	}

	@Activate
	protected void activate() {
		DestinationConfiguration destinationConfiguration =
			new DestinationConfiguration(
				DestinationConfiguration.DESTINATION_TYPE_PARALLEL,
				AssetAutoTaggerDestinationNames.ASSET_AUTO_TAGGER);

		Destination destination = _destinationFactory.createDestination(
			destinationConfiguration);

		_messageBus.addDestination(destination);
	}

	@Deactivate
	protected void deactivate() {
		_messageBus.removeDestination(
			AssetAutoTaggerDestinationNames.ASSET_AUTO_TAGGER);
	}

	@Reference
	private AssetAutoTaggerEntryLocalService _assetAutoTaggerEntryLocalService;

	@Reference
	private AssetAutoTaggerImpl _assetAutoTaggerImpl;

	@Reference
	private DestinationFactory _destinationFactory;

	@Reference
	private MessageBus _messageBus;

}