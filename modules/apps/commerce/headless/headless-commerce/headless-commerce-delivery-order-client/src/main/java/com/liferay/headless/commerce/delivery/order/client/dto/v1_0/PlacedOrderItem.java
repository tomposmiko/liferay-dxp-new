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

package com.liferay.headless.commerce.delivery.order.client.dto.v1_0;

import com.liferay.headless.commerce.delivery.order.client.function.UnsafeSupplier;
import com.liferay.headless.commerce.delivery.order.client.serdes.v1_0.PlacedOrderItemSerDes;

import java.io.Serializable;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public class PlacedOrderItem implements Cloneable, Serializable {

	public static PlacedOrderItem toDTO(String json) {
		return PlacedOrderItemSerDes.toDTO(json);
	}

	public String getAdaptiveMediaImageHTMLTag() {
		return adaptiveMediaImageHTMLTag;
	}

	public void setAdaptiveMediaImageHTMLTag(String adaptiveMediaImageHTMLTag) {
		this.adaptiveMediaImageHTMLTag = adaptiveMediaImageHTMLTag;
	}

	public void setAdaptiveMediaImageHTMLTag(
		UnsafeSupplier<String, Exception>
			adaptiveMediaImageHTMLTagUnsafeSupplier) {

		try {
			adaptiveMediaImageHTMLTag =
				adaptiveMediaImageHTMLTagUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String adaptiveMediaImageHTMLTag;

	public Map<String, ?> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(Map<String, ?> customFields) {
		this.customFields = customFields;
	}

	public void setCustomFields(
		UnsafeSupplier<Map<String, ?>, Exception> customFieldsUnsafeSupplier) {

		try {
			customFields = customFieldsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Map<String, ?> customFields;

	public String[] getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(String[] errorMessages) {
		this.errorMessages = errorMessages;
	}

	public void setErrorMessages(
		UnsafeSupplier<String[], Exception> errorMessagesUnsafeSupplier) {

		try {
			errorMessages = errorMessagesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] errorMessages;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(UnsafeSupplier<Long, Exception> idUnsafeSupplier) {
		try {
			id = idUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setName(UnsafeSupplier<String, Exception> nameUnsafeSupplier) {
		try {
			name = nameUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String name;

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public void setOptions(
		UnsafeSupplier<String, Exception> optionsUnsafeSupplier) {

		try {
			options = optionsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String options;

	public Long getParentOrderItemId() {
		return parentOrderItemId;
	}

	public void setParentOrderItemId(Long parentOrderItemId) {
		this.parentOrderItemId = parentOrderItemId;
	}

	public void setParentOrderItemId(
		UnsafeSupplier<Long, Exception> parentOrderItemIdUnsafeSupplier) {

		try {
			parentOrderItemId = parentOrderItemIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long parentOrderItemId;

	public PlacedOrderItemShipment[] getPlacedOrderItemShipments() {
		return placedOrderItemShipments;
	}

	public void setPlacedOrderItemShipments(
		PlacedOrderItemShipment[] placedOrderItemShipments) {

		this.placedOrderItemShipments = placedOrderItemShipments;
	}

	public void setPlacedOrderItemShipments(
		UnsafeSupplier<PlacedOrderItemShipment[], Exception>
			placedOrderItemShipmentsUnsafeSupplier) {

		try {
			placedOrderItemShipments =
				placedOrderItemShipmentsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected PlacedOrderItemShipment[] placedOrderItemShipments;

	public PlacedOrderItem[] getPlacedOrderItems() {
		return placedOrderItems;
	}

	public void setPlacedOrderItems(PlacedOrderItem[] placedOrderItems) {
		this.placedOrderItems = placedOrderItems;
	}

	public void setPlacedOrderItems(
		UnsafeSupplier<PlacedOrderItem[], Exception>
			placedOrderItemsUnsafeSupplier) {

		try {
			placedOrderItems = placedOrderItemsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected PlacedOrderItem[] placedOrderItems;

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public void setPrice(UnsafeSupplier<Price, Exception> priceUnsafeSupplier) {
		try {
			price = priceUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Price price;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setProductId(
		UnsafeSupplier<Long, Exception> productIdUnsafeSupplier) {

		try {
			productId = productIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long productId;

	public Map<String, String> getProductURLs() {
		return productURLs;
	}

	public void setProductURLs(Map<String, String> productURLs) {
		this.productURLs = productURLs;
	}

	public void setProductURLs(
		UnsafeSupplier<Map<String, String>, Exception>
			productURLsUnsafeSupplier) {

		try {
			productURLs = productURLsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Map<String, String> productURLs;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setQuantity(
		UnsafeSupplier<Integer, Exception> quantityUnsafeSupplier) {

		try {
			quantity = quantityUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Integer quantity;

	public String getReplacedSku() {
		return replacedSku;
	}

	public void setReplacedSku(String replacedSku) {
		this.replacedSku = replacedSku;
	}

	public void setReplacedSku(
		UnsafeSupplier<String, Exception> replacedSkuUnsafeSupplier) {

		try {
			replacedSku = replacedSkuUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String replacedSku;

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public void setSettings(
		UnsafeSupplier<Settings, Exception> settingsUnsafeSupplier) {

		try {
			settings = settingsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Settings settings;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public void setSku(UnsafeSupplier<String, Exception> skuUnsafeSupplier) {
		try {
			sku = skuUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String sku;

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public void setSkuId(UnsafeSupplier<Long, Exception> skuIdUnsafeSupplier) {
		try {
			skuId = skuIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long skuId;

	public Boolean getSubscription() {
		return subscription;
	}

	public void setSubscription(Boolean subscription) {
		this.subscription = subscription;
	}

	public void setSubscription(
		UnsafeSupplier<Boolean, Exception> subscriptionUnsafeSupplier) {

		try {
			subscription = subscriptionUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean subscription;

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void setThumbnail(
		UnsafeSupplier<String, Exception> thumbnailUnsafeSupplier) {

		try {
			thumbnail = thumbnailUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String thumbnail;

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public void setValid(
		UnsafeSupplier<Boolean, Exception> validUnsafeSupplier) {

		try {
			valid = validUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean valid;

	public String[] getVirtualItemURLs() {
		return virtualItemURLs;
	}

	public void setVirtualItemURLs(String[] virtualItemURLs) {
		this.virtualItemURLs = virtualItemURLs;
	}

	public void setVirtualItemURLs(
		UnsafeSupplier<String[], Exception> virtualItemURLsUnsafeSupplier) {

		try {
			virtualItemURLs = virtualItemURLsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] virtualItemURLs;

	@Override
	public PlacedOrderItem clone() throws CloneNotSupportedException {
		return (PlacedOrderItem)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof PlacedOrderItem)) {
			return false;
		}

		PlacedOrderItem placedOrderItem = (PlacedOrderItem)object;

		return Objects.equals(toString(), placedOrderItem.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return PlacedOrderItemSerDes.toJSON(this);
	}

}