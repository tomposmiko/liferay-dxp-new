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

package com.liferay.commerce.product.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.LocalizedModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.StagedAuditedModel;
import com.liferay.portal.kernel.model.change.tracking.CTModel;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the CPOptionValue service. Represents a row in the &quot;CPOptionValue&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.commerce.product.model.impl.CPOptionValueModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.commerce.product.model.impl.CPOptionValueImpl</code>.
 * </p>
 *
 * @author Marco Leo
 * @see CPOptionValue
 * @generated
 */
@ProviderType
public interface CPOptionValueModel
	extends BaseModel<CPOptionValue>, CTModel<CPOptionValue>, LocalizedModel,
			MVCCModel, ShardedModel, StagedAuditedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a cp option value model instance should use the {@link CPOptionValue} interface instead.
	 */

	/**
	 * Returns the primary key of this cp option value.
	 *
	 * @return the primary key of this cp option value
	 */
	@Override
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this cp option value.
	 *
	 * @param primaryKey the primary key of this cp option value
	 */
	@Override
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this cp option value.
	 *
	 * @return the mvcc version of this cp option value
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this cp option value.
	 *
	 * @param mvccVersion the mvcc version of this cp option value
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the ct collection ID of this cp option value.
	 *
	 * @return the ct collection ID of this cp option value
	 */
	@Override
	public long getCtCollectionId();

	/**
	 * Sets the ct collection ID of this cp option value.
	 *
	 * @param ctCollectionId the ct collection ID of this cp option value
	 */
	@Override
	public void setCtCollectionId(long ctCollectionId);

	/**
	 * Returns the uuid of this cp option value.
	 *
	 * @return the uuid of this cp option value
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this cp option value.
	 *
	 * @param uuid the uuid of this cp option value
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the external reference code of this cp option value.
	 *
	 * @return the external reference code of this cp option value
	 */
	@AutoEscape
	public String getExternalReferenceCode();

	/**
	 * Sets the external reference code of this cp option value.
	 *
	 * @param externalReferenceCode the external reference code of this cp option value
	 */
	public void setExternalReferenceCode(String externalReferenceCode);

	/**
	 * Returns the cp option value ID of this cp option value.
	 *
	 * @return the cp option value ID of this cp option value
	 */
	public long getCPOptionValueId();

	/**
	 * Sets the cp option value ID of this cp option value.
	 *
	 * @param CPOptionValueId the cp option value ID of this cp option value
	 */
	public void setCPOptionValueId(long CPOptionValueId);

	/**
	 * Returns the company ID of this cp option value.
	 *
	 * @return the company ID of this cp option value
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this cp option value.
	 *
	 * @param companyId the company ID of this cp option value
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this cp option value.
	 *
	 * @return the user ID of this cp option value
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this cp option value.
	 *
	 * @param userId the user ID of this cp option value
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this cp option value.
	 *
	 * @return the user uuid of this cp option value
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this cp option value.
	 *
	 * @param userUuid the user uuid of this cp option value
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this cp option value.
	 *
	 * @return the user name of this cp option value
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this cp option value.
	 *
	 * @param userName the user name of this cp option value
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this cp option value.
	 *
	 * @return the create date of this cp option value
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this cp option value.
	 *
	 * @param createDate the create date of this cp option value
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this cp option value.
	 *
	 * @return the modified date of this cp option value
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this cp option value.
	 *
	 * @param modifiedDate the modified date of this cp option value
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the cp option ID of this cp option value.
	 *
	 * @return the cp option ID of this cp option value
	 */
	public long getCPOptionId();

	/**
	 * Sets the cp option ID of this cp option value.
	 *
	 * @param CPOptionId the cp option ID of this cp option value
	 */
	public void setCPOptionId(long CPOptionId);

	/**
	 * Returns the name of this cp option value.
	 *
	 * @return the name of this cp option value
	 */
	public String getName();

	/**
	 * Returns the localized name of this cp option value in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized name of this cp option value
	 */
	@AutoEscape
	public String getName(Locale locale);

	/**
	 * Returns the localized name of this cp option value in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized name of this cp option value. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	public String getName(Locale locale, boolean useDefault);

	/**
	 * Returns the localized name of this cp option value in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized name of this cp option value
	 */
	@AutoEscape
	public String getName(String languageId);

	/**
	 * Returns the localized name of this cp option value in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized name of this cp option value
	 */
	@AutoEscape
	public String getName(String languageId, boolean useDefault);

	@AutoEscape
	public String getNameCurrentLanguageId();

	@AutoEscape
	public String getNameCurrentValue();

	/**
	 * Returns a map of the locales and localized names of this cp option value.
	 *
	 * @return the locales and localized names of this cp option value
	 */
	public Map<Locale, String> getNameMap();

	/**
	 * Sets the name of this cp option value.
	 *
	 * @param name the name of this cp option value
	 */
	public void setName(String name);

	/**
	 * Sets the localized name of this cp option value in the language.
	 *
	 * @param name the localized name of this cp option value
	 * @param locale the locale of the language
	 */
	public void setName(String name, Locale locale);

	/**
	 * Sets the localized name of this cp option value in the language, and sets the default locale.
	 *
	 * @param name the localized name of this cp option value
	 * @param locale the locale of the language
	 * @param defaultLocale the default locale
	 */
	public void setName(String name, Locale locale, Locale defaultLocale);

	public void setNameCurrentLanguageId(String languageId);

	/**
	 * Sets the localized names of this cp option value from the map of locales and localized names.
	 *
	 * @param nameMap the locales and localized names of this cp option value
	 */
	public void setNameMap(Map<Locale, String> nameMap);

	/**
	 * Sets the localized names of this cp option value from the map of locales and localized names, and sets the default locale.
	 *
	 * @param nameMap the locales and localized names of this cp option value
	 * @param defaultLocale the default locale
	 */
	public void setNameMap(Map<Locale, String> nameMap, Locale defaultLocale);

	/**
	 * Returns the priority of this cp option value.
	 *
	 * @return the priority of this cp option value
	 */
	public double getPriority();

	/**
	 * Sets the priority of this cp option value.
	 *
	 * @param priority the priority of this cp option value
	 */
	public void setPriority(double priority);

	/**
	 * Returns the key of this cp option value.
	 *
	 * @return the key of this cp option value
	 */
	@AutoEscape
	public String getKey();

	/**
	 * Sets the key of this cp option value.
	 *
	 * @param key the key of this cp option value
	 */
	public void setKey(String key);

	/**
	 * Returns the last publish date of this cp option value.
	 *
	 * @return the last publish date of this cp option value
	 */
	public Date getLastPublishDate();

	/**
	 * Sets the last publish date of this cp option value.
	 *
	 * @param lastPublishDate the last publish date of this cp option value
	 */
	public void setLastPublishDate(Date lastPublishDate);

	@Override
	public String[] getAvailableLanguageIds();

	@Override
	public String getDefaultLanguageId();

	@Override
	public void prepareLocalizedFieldsForImport() throws LocaleException;

	@Override
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale)
		throws LocaleException;

	@Override
	public CPOptionValue cloneWithOriginalValues();

	public default String toXmlString() {
		return null;
	}

}