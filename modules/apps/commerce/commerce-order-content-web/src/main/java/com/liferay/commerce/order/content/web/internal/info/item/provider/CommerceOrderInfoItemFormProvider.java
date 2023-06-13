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

package com.liferay.commerce.order.content.web.internal.info.item.provider;

import com.liferay.asset.info.item.provider.AssetEntryInfoItemFieldSetProvider;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.content.web.internal.info.CommerceOrderInfoItemFields;
import com.liferay.expando.info.item.provider.ExpandoInfoItemFieldSetProvider;
import com.liferay.info.field.InfoFieldSet;
import com.liferay.info.form.InfoForm;
import com.liferay.info.item.field.reader.InfoItemFieldReaderFieldSetProvider;
import com.liferay.info.item.provider.InfoItemFormProvider;
import com.liferay.info.localized.InfoLocalizedValue;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.template.info.item.provider.TemplateInfoItemFieldSetProvider;

import java.util.Locale;
import java.util.Set;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Danny Situ
 */
@Component(
	immediate = true, property = Constants.SERVICE_RANKING + ":Integer=10",
	service = InfoItemFormProvider.class
)
public class CommerceOrderInfoItemFormProvider
	implements InfoItemFormProvider<CommerceOrder> {

	@Override
	public InfoForm getInfoForm() {
		return _getInfoForm();
	}

	@Override
	public InfoForm getInfoForm(CommerceOrder commerceOrder) {
		return _getInfoForm();
	}

	@Override
	public InfoForm getInfoForm(String formVariationKey, long groupId) {
		return _getInfoForm();
	}

	private InfoFieldSet _getBasicInformationInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.accountIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.accountNameInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.billingAddressIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.couponCodeInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				deliveryCommerceTermEntryDescriptionInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.deliveryCommerceTermEntryIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.deliveryCommerceTermEntryNameInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.formattedDiscountAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.formattedDiscountWithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.formattedShippingAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.formattedShippingWithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.formattedSubtotalAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.formattedSubtotalWithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.formattedTotalAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.formattedTotalWithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.orderDateInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.orderIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.orderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.orderTypeIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				paymentCommerceTermEntryDescriptionInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.paymentCommerceTermEntryIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.paymentCommerceTermEntryNameInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.purchaseOrderNumberInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.requestedDeliveryDateInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.shippingAddressIdInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				"com.liferay.commerce.lang", "basic-information")
		).name(
			"basic-information"
		).build();
	}

	private InfoFieldSet _getDetailedInformationInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.advanceStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.b2bInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.currencyIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.companyIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.createDateInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.defaultLanguageIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.emptyInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.lastPriceUpdateDateInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.manuallyAdjustedInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.modifiedDateInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.paymenMethodKeyInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.paymentStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.scopeGroupIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.shippingAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.shippingDiscountAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				shippingDiscountPercentageLevel1InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				shippingDiscountPercentageLevel1WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				shippingDiscountPercentageLevel2InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				shippingDiscountPercentageLevel2WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				shippingDiscountPercentageLevel3InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				shippingDiscountPercentageLevel3WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				shippingDiscountPercentageLevel4InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				shippingDiscountPercentageLevel4WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.shippingMethodIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.shippingOptionNameInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.shippingWithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.stagedModelTypeInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.subscriptionOrderInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.subtotalInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.subtotalDiscountAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				subtotalDiscountPercentageLevel1InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				subtotalDiscountPercentageLevel1WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				subtotalDiscountPercentageLevel2InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				subtotalDiscountPercentageLevel2WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				subtotalDiscountPercentageLevel3InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				subtotalDiscountPercentageLevel3WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				subtotalDiscountPercentageLevel4InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				subtotalDiscountPercentageLevel4WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.subtotalDiscountWithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.subtotalWithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.taxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.totalInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.totalDiscountAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.totalDiscountPercentageLevel1InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				totalDiscountPercentageLevel1WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.totalDiscountPercentageLevel2InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				totalDiscountPercentageLevel2WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.totalDiscountPercentageLevel3InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				totalDiscountPercentageLevel3WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.totalDiscountPercentageLevel4InfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.
				totalDiscountPercentageLevel4WithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.totalDiscountWithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.totalWithTaxAmountInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.transactionIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.userIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.userNameInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.userUuidInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.uuidInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				"com.liferay.commerce.lang", "detailed-information")
		).name(
			"detailed-information"
		).build();
	}

	private InfoForm _getInfoForm() {
		Set<Locale> availableLocales = _language.getAvailableLocales();

		InfoLocalizedValue.Builder<String> infoLocalizedValueBuilder =
			InfoLocalizedValue.builder();

		for (Locale locale : availableLocales) {
			infoLocalizedValueBuilder.value(
				locale,
				ResourceActionsUtil.getModelResource(
					locale, CommerceOrder.class.getName()));
		}

		return InfoForm.builder(
		).infoFieldSetEntry(
			_getBasicInformationInfoFieldSet()
		).infoFieldSetEntry(
			_getDetailedInformationInfoFieldSet()
		).infoFieldSetEntry(
			_getOrderStatusInformationInfoFieldSet()
		).infoFieldSetEntry(
			_getWorkflowStatusInformationInfoFieldSet()
		).infoFieldSetEntry(
			_expandoInfoItemFieldSetProvider.getInfoFieldSet(
				CommerceOrder.class.getName())
		).infoFieldSetEntry(
			_templateInfoItemFieldSetProvider.getInfoFieldSet(
				CommerceOrder.class.getName())
		).infoFieldSetEntry(
			_infoItemFieldReaderFieldSetProvider.getInfoFieldSet(
				CommerceOrder.class.getName())
		).labelInfoLocalizedValue(
			infoLocalizedValueBuilder.build()
		).name(
			CommerceOrder.class.getName()
		).build();
	}

	private InfoFieldSet _getOrderStatusInformationInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.awaitingPickupOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.cancelledOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.completedOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.declinedOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.disputedOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.inProgressOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.onHoldOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.openOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.partiallyRefundedOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.partiallyShippedOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.pendingOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.processingOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.refundedOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.shippedOrderStatusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.subscriptionOrderStatusInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				"com.liferay.commerce.lang", "order-status-information")
		).name(
			"order-status-information"
		).build();
	}

	private InfoFieldSet _getWorkflowStatusInformationInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.approvedInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.deniedInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.draftInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.expiredInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.inactiveInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.incompleteInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.pendingInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.scheduledInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.statusInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.statusByUserIdInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.statusByUserNameInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.statusByUserUuidInfoField
		).infoFieldSetEntry(
			CommerceOrderInfoItemFields.statusDateInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				"com.liferay.commerce.lang", "workflow-status-information")
		).name(
			"workflow-status-information"
		).build();
	}

	@Reference
	private AssetEntryInfoItemFieldSetProvider
		_assetEntryInfoItemFieldSetProvider;

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private ExpandoInfoItemFieldSetProvider _expandoInfoItemFieldSetProvider;

	@Reference
	private InfoItemFieldReaderFieldSetProvider
		_infoItemFieldReaderFieldSetProvider;

	@Reference
	private Language _language;

	@Reference
	private TemplateInfoItemFieldSetProvider _templateInfoItemFieldSetProvider;

}