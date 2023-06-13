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

package com.liferay.headless.commerce.admin.catalog.client.dto.v1_0;

import com.liferay.headless.commerce.admin.catalog.client.function.UnsafeSupplier;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.SkuVirtualSettingsSerDes;

import java.io.Serializable;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public class SkuVirtualSettings implements Cloneable, Serializable {

	public static SkuVirtualSettings toDTO(String json) {
		return SkuVirtualSettingsSerDes.toDTO(json);
	}

	public Integer getActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(Integer activationStatus) {
		this.activationStatus = activationStatus;
	}

	public void setActivationStatus(
		UnsafeSupplier<Integer, Exception> activationStatusUnsafeSupplier) {

		try {
			activationStatus = activationStatusUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Integer activationStatus;

	public Status getActivationStatusInfo() {
		return activationStatusInfo;
	}

	public void setActivationStatusInfo(Status activationStatusInfo) {
		this.activationStatusInfo = activationStatusInfo;
	}

	public void setActivationStatusInfo(
		UnsafeSupplier<Status, Exception> activationStatusInfoUnsafeSupplier) {

		try {
			activationStatusInfo = activationStatusInfoUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Status activationStatusInfo;

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public void setAttachment(
		UnsafeSupplier<String, Exception> attachmentUnsafeSupplier) {

		try {
			attachment = attachmentUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String attachment;

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public void setDuration(
		UnsafeSupplier<Long, Exception> durationUnsafeSupplier) {

		try {
			duration = durationUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long duration;

	public Integer getMaxUsages() {
		return maxUsages;
	}

	public void setMaxUsages(Integer maxUsages) {
		this.maxUsages = maxUsages;
	}

	public void setMaxUsages(
		UnsafeSupplier<Integer, Exception> maxUsagesUnsafeSupplier) {

		try {
			maxUsages = maxUsagesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Integer maxUsages;

	public Boolean getOverride() {
		return override;
	}

	public void setOverride(Boolean override) {
		this.override = override;
	}

	public void setOverride(
		UnsafeSupplier<Boolean, Exception> overrideUnsafeSupplier) {

		try {
			override = overrideUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean override;

	public String getSampleAttachment() {
		return sampleAttachment;
	}

	public void setSampleAttachment(String sampleAttachment) {
		this.sampleAttachment = sampleAttachment;
	}

	public void setSampleAttachment(
		UnsafeSupplier<String, Exception> sampleAttachmentUnsafeSupplier) {

		try {
			sampleAttachment = sampleAttachmentUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String sampleAttachment;

	public String getSampleSrc() {
		return sampleSrc;
	}

	public void setSampleSrc(String sampleSrc) {
		this.sampleSrc = sampleSrc;
	}

	public void setSampleSrc(
		UnsafeSupplier<String, Exception> sampleSrcUnsafeSupplier) {

		try {
			sampleSrc = sampleSrcUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String sampleSrc;

	public String getSampleUrl() {
		return sampleUrl;
	}

	public void setSampleUrl(String sampleUrl) {
		this.sampleUrl = sampleUrl;
	}

	public void setSampleUrl(
		UnsafeSupplier<String, Exception> sampleUrlUnsafeSupplier) {

		try {
			sampleUrl = sampleUrlUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String sampleUrl;

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public void setSrc(UnsafeSupplier<String, Exception> srcUnsafeSupplier) {
		try {
			src = srcUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String src;

	public Map<String, String> getTermsOfUseContent() {
		return termsOfUseContent;
	}

	public void setTermsOfUseContent(Map<String, String> termsOfUseContent) {
		this.termsOfUseContent = termsOfUseContent;
	}

	public void setTermsOfUseContent(
		UnsafeSupplier<Map<String, String>, Exception>
			termsOfUseContentUnsafeSupplier) {

		try {
			termsOfUseContent = termsOfUseContentUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Map<String, String> termsOfUseContent;

	public Long getTermsOfUseJournalArticleId() {
		return termsOfUseJournalArticleId;
	}

	public void setTermsOfUseJournalArticleId(Long termsOfUseJournalArticleId) {
		this.termsOfUseJournalArticleId = termsOfUseJournalArticleId;
	}

	public void setTermsOfUseJournalArticleId(
		UnsafeSupplier<Long, Exception>
			termsOfUseJournalArticleIdUnsafeSupplier) {

		try {
			termsOfUseJournalArticleId =
				termsOfUseJournalArticleIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long termsOfUseJournalArticleId;

	public Boolean getTermsOfUseRequired() {
		return termsOfUseRequired;
	}

	public void setTermsOfUseRequired(Boolean termsOfUseRequired) {
		this.termsOfUseRequired = termsOfUseRequired;
	}

	public void setTermsOfUseRequired(
		UnsafeSupplier<Boolean, Exception> termsOfUseRequiredUnsafeSupplier) {

		try {
			termsOfUseRequired = termsOfUseRequiredUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean termsOfUseRequired;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUrl(UnsafeSupplier<String, Exception> urlUnsafeSupplier) {
		try {
			url = urlUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String url;

	public Boolean getUseSample() {
		return useSample;
	}

	public void setUseSample(Boolean useSample) {
		this.useSample = useSample;
	}

	public void setUseSample(
		UnsafeSupplier<Boolean, Exception> useSampleUnsafeSupplier) {

		try {
			useSample = useSampleUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean useSample;

	@Override
	public SkuVirtualSettings clone() throws CloneNotSupportedException {
		return (SkuVirtualSettings)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof SkuVirtualSettings)) {
			return false;
		}

		SkuVirtualSettings skuVirtualSettings = (SkuVirtualSettings)object;

		return Objects.equals(toString(), skuVirtualSettings.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return SkuVirtualSettingsSerDes.toJSON(this);
	}

}