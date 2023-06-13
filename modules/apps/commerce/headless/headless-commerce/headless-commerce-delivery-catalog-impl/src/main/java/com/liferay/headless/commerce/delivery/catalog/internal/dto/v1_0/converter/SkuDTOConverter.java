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

package com.liferay.headless.commerce.delivery.catalog.internal.dto.v1_0.converter;

import com.liferay.commerce.configuration.CommercePriceConfiguration;
import com.liferay.commerce.constants.CommerceConstants;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.util.CommercePriceFormatter;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.inventory.CPDefinitionInventoryEngine;
import com.liferay.commerce.inventory.constants.CommerceInventoryAvailabilityConstants;
import com.liferay.commerce.inventory.engine.CommerceInventoryEngine;
import com.liferay.commerce.price.CommerceProductPrice;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.price.CommerceProductPriceRequest;
import com.liferay.commerce.product.content.util.CPContentHelper;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.option.CommerceOptionValue;
import com.liferay.commerce.product.option.CommerceOptionValueHelper;
import com.liferay.commerce.product.service.CPDefinitionOptionRelLocalService;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.commerce.product.util.JsonHelper;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Availability;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.DDMOption;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Price;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Product;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.Sku;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.SkuOption;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.settings.SystemSettingsLocator;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Sbarra
 * @author Alessio Antonio Rendina
 */
@Component(
	property = "dto.class.name=CPSku",
	service = {DTOConverter.class, SkuDTOConverter.class}
)
public class SkuDTOConverter implements DTOConverter<CPInstance, Sku> {

	@Override
	public String getContentType() {
		return Product.class.getSimpleName();
	}

	@Override
	public Sku toDTO(DTOConverterContext dtoConverterContext) throws Exception {
		SkuDTOConverterContext cpSkuDTOConverterConvertContext =
			(SkuDTOConverterContext)dtoConverterContext;

		CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
			(Long)cpSkuDTOConverterConvertContext.getId());

		CommerceContext commerceContext =
			cpSkuDTOConverterConvertContext.getCommerceContext();

		JSONArray keyValuesJSONArray = _jsonHelper.toJSONArray(
			_cpDefinitionOptionRelLocalService.
				getCPDefinitionOptionRelKeysCPDefinitionOptionValueRelKeys(
					cpInstance.getCPInstanceId()));

		Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
			cpDefinitionOptionRelsMap =
				_cpInstanceHelper.getCPDefinitionOptionValueRelsMap(
					cpInstance.getCPDefinitionId(),
					keyValuesJSONArray.toString());

		DDMOption[] ddmOptions = _getDDMOptions(cpDefinitionOptionRelsMap);

		return new Sku() {
			{
				availability = _getAvailability(
					cpInstance.getGroupId(),
					commerceContext.getCommerceChannelGroupId(),
					cpSkuDTOConverterConvertContext.getCompanyId(),
					cpInstance.getSku(), cpInstance,
					cpSkuDTOConverterConvertContext.getLocale());
				DDMOptions = ddmOptions;
				depth = cpInstance.getDepth();
				displayDate = cpInstance.getDisplayDate();
				expirationDate = cpInstance.getExpirationDate();
				gtin = cpInstance.getGtin();
				height = cpInstance.getHeight();
				id = cpInstance.getCPInstanceId();
				incomingQuantityLabel =
					_cpContentHelper.getIncomingQuantityLabel(
						cpSkuDTOConverterConvertContext.getCompanyId(),
						cpSkuDTOConverterConvertContext.getLocale(),
						cpInstance.getSku(),
						cpSkuDTOConverterConvertContext.getUser());
				manufacturerPartNumber = cpInstance.getManufacturerPartNumber();
				price = _getPrice(
					cpSkuDTOConverterConvertContext.getCommerceContext(),
					cpInstance,
					ArrayUtil.toString(ddmOptions, StringPool.BLANK),
					cpSkuDTOConverterConvertContext.getLocale(),
					cpSkuDTOConverterConvertContext.getQuantity());
				published = cpInstance.isPublished();
				purchasable = cpInstance.isPurchasable();
				sku = cpInstance.getSku();
				skuOptions = _getSkuOptions(cpDefinitionOptionRelsMap);
				weight = cpInstance.getWeight();
				width = cpInstance.getWidth();

				setDisplayDiscountLevels(
					() -> {
						CommercePriceConfiguration commercePriceConfiguration =
							_configurationProvider.getConfiguration(
								CommercePriceConfiguration.class,
								new SystemSettingsLocator(
									CommerceConstants.
										SERVICE_NAME_COMMERCE_PRICE));

						return commercePriceConfiguration.
							displayDiscountLevels();
					});
			}
		};
	}

	private Availability _getAvailability(
			long commerceCatalogGroupId, long commerceChannelGroupId,
			long companyId, String sku, CPInstance cpInstance, Locale locale)
		throws Exception {

		Availability availability = new Availability();

		if (_cpDefinitionInventoryEngine.isDisplayAvailability(cpInstance)) {
			if (Objects.equals(
					_commerceInventoryEngine.getAvailabilityStatus(
						cpInstance.getCompanyId(), commerceCatalogGroupId,
						commerceChannelGroupId,
						_cpDefinitionInventoryEngine.getMinStockQuantity(
							cpInstance),
						cpInstance.getSku()),
					CommerceInventoryAvailabilityConstants.AVAILABLE)) {

				availability.setLabel_i18n(_language.get(locale, "available"));
				availability.setLabel("available");
			}
			else {
				availability.setLabel_i18n(
					_language.get(locale, "unavailable"));
				availability.setLabel("unavailable");
			}
		}

		if (_cpDefinitionInventoryEngine.isDisplayStockQuantity(cpInstance)) {
			availability.setStockQuantity(
				_commerceInventoryEngine.getStockQuantity(
					companyId, commerceCatalogGroupId, commerceChannelGroupId,
					sku));
		}

		return availability;
	}

	private CommerceProductPriceRequest _getCommerceProductPriceRequest(
		CommerceContext commerceContext,
		List<CommerceOptionValue> commerceOptionValues, long cpInstanceId,
		int quantity) {

		CommerceProductPriceRequest commerceProductPriceRequest =
			new CommerceProductPriceRequest();

		commerceProductPriceRequest.setCommerceContext(commerceContext);
		commerceProductPriceRequest.setCommerceOptionValues(
			commerceOptionValues);
		commerceProductPriceRequest.setCpInstanceId(cpInstanceId);
		commerceProductPriceRequest.setQuantity(quantity);
		commerceProductPriceRequest.setSecure(true);

		return commerceProductPriceRequest;
	}

	private DDMOption[] _getDDMOptions(
		Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
			cpDefinitionOptionRelsMap) {

		List<DDMOption> ddmOptions = new ArrayList<>();

		for (Map.Entry<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				entry : cpDefinitionOptionRelsMap.entrySet()) {

			CPDefinitionOptionRel cpDefinitionOptionRel = entry.getKey();

			ddmOptions.add(
				new DDMOption() {
					{
						key = cpDefinitionOptionRel.getKey();
						required =
							cpDefinitionOptionRel.isRequired() ||
							cpDefinitionOptionRel.isSkuContributor();
						value = TransformUtil.transformToArray(
							entry.getValue(),
							CPDefinitionOptionValueRel::getKey, String.class);
					}
				});
		}

		return ddmOptions.toArray(new DDMOption[0]);
	}

	private String[] _getFormattedDiscountPercentages(
			BigDecimal[] discountPercentages, Locale locale)
		throws Exception {

		List<String> formattedDiscountPercentages = new ArrayList<>();

		for (BigDecimal percentage : discountPercentages) {
			formattedDiscountPercentages.add(
				_commercePriceFormatter.format(percentage, locale));
		}

		return formattedDiscountPercentages.toArray(new String[0]);
	}

	private Price _getPrice(
			CommerceContext commerceContext, CPInstance cpInstance,
			String ddmFormValues, Locale locale, int quantity)
		throws Exception {

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				_getCommerceProductPriceRequest(
					commerceContext,
					_commerceOptionValueHelper.
						getCPDefinitionCommerceOptionValues(
							cpInstance.getCPDefinitionId(), ddmFormValues),
					cpInstance.getCPInstanceId(), quantity));

		if (commerceProductPrice == null) {
			return new Price();
		}

		CommerceCurrency commerceCurrency =
			commerceContext.getCommerceCurrency();

		CommerceMoney unitPriceCommerceMoney =
			commerceProductPrice.getUnitPrice();

		CommerceMoney unitPromoPriceCommerceMoney =
			commerceProductPrice.getUnitPromoPrice();

		BigDecimal unitPromoPrice = unitPromoPriceCommerceMoney.getPrice();

		BigDecimal unitPrice = unitPriceCommerceMoney.getPrice();

		Price price = new Price() {
			{
				currency = commerceCurrency.getName(locale);
				price = unitPrice.doubleValue();
				priceFormatted = unitPriceCommerceMoney.format(locale);
			}
		};

		if ((unitPromoPrice != null) &&
			(unitPromoPrice.compareTo(BigDecimal.ZERO) > 0) &&
			(unitPromoPrice.compareTo(unitPriceCommerceMoney.getPrice()) < 0)) {

			price.setPromoPrice(unitPromoPrice.doubleValue());
			price.setPromoPriceFormatted(
				unitPromoPriceCommerceMoney.format(locale));
		}

		CommerceDiscountValue discountValue =
			commerceProductPrice.getDiscountValue();

		if (discountValue != null) {
			CommerceMoney discountAmountCommerceMoney =
				discountValue.getDiscountAmount();

			CommerceMoney finalPriceCommerceMoney =
				commerceProductPrice.getFinalPrice();

			price.setDiscount(discountAmountCommerceMoney.format(locale));
			price.setDiscountPercentage(
				_commercePriceFormatter.format(
					discountValue.getDiscountPercentage(), locale));
			price.setDiscountPercentages(
				_getFormattedDiscountPercentages(
					discountValue.getPercentages(), locale));
			price.setFinalPrice(finalPriceCommerceMoney.format(locale));
		}

		return price;
	}

	private SkuOption[] _getSkuOptions(
			Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				cpDefinitionOptionRelsMap)
		throws Exception {

		List<SkuOption> skuOptions = new ArrayList<>();

		for (Map.Entry<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				entry : cpDefinitionOptionRelsMap.entrySet()) {

			CPDefinitionOptionRel cpDefinitionOptionRel = entry.getKey();

			List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels =
				entry.getValue();

			for (CPDefinitionOptionValueRel cpDefinitionOptionValueRel :
					cpDefinitionOptionValueRels) {

				SkuOption skuOption = new SkuOption() {
					{
						key =
							cpDefinitionOptionRel.getCPDefinitionOptionRelId();
						value =
							cpDefinitionOptionValueRel.
								getCPDefinitionOptionValueRelId();
					}
				};

				skuOptions.add(skuOption);
			}
		}

		return skuOptions.toArray(new SkuOption[0]);
	}

	@Reference
	private CommerceInventoryEngine _commerceInventoryEngine;

	@Reference
	private CommerceOptionValueHelper _commerceOptionValueHelper;

	@Reference
	private CommercePriceFormatter _commercePriceFormatter;

	@Reference
	private CommerceProductPriceCalculation _commerceProductPriceCalculation;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private CPContentHelper _cpContentHelper;

	@Reference
	private CPDefinitionInventoryEngine _cpDefinitionInventoryEngine;

	@Reference
	private CPDefinitionOptionRelLocalService
		_cpDefinitionOptionRelLocalService;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private JsonHelper _jsonHelper;

	@Reference
	private Language _language;

}