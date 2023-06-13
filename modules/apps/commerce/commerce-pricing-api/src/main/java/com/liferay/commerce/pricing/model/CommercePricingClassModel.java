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

package com.liferay.commerce.pricing.model;

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
 * The base model interface for the CommercePricingClass service. Represents a row in the &quot;CommercePricingClass&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.commerce.pricing.model.impl.CommercePricingClassModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.commerce.pricing.model.impl.CommercePricingClassImpl</code>.
 * </p>
 *
 * @author Riccardo Alberti
 * @see CommercePricingClass
 * @generated
 */
@ProviderType
public interface CommercePricingClassModel
	extends BaseModel<CommercePricingClass>, CTModel<CommercePricingClass>,
			LocalizedModel, MVCCModel, ShardedModel, StagedAuditedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a commerce pricing class model instance should use the {@link CommercePricingClass} interface instead.
	 */

	/**
	 * Returns the primary key of this commerce pricing class.
	 *
	 * @return the primary key of this commerce pricing class
	 */
	@Override
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this commerce pricing class.
	 *
	 * @param primaryKey the primary key of this commerce pricing class
	 */
	@Override
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this commerce pricing class.
	 *
	 * @return the mvcc version of this commerce pricing class
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this commerce pricing class.
	 *
	 * @param mvccVersion the mvcc version of this commerce pricing class
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the ct collection ID of this commerce pricing class.
	 *
	 * @return the ct collection ID of this commerce pricing class
	 */
	@Override
	public long getCtCollectionId();

	/**
	 * Sets the ct collection ID of this commerce pricing class.
	 *
	 * @param ctCollectionId the ct collection ID of this commerce pricing class
	 */
	@Override
	public void setCtCollectionId(long ctCollectionId);

	/**
	 * Returns the uuid of this commerce pricing class.
	 *
	 * @return the uuid of this commerce pricing class
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this commerce pricing class.
	 *
	 * @param uuid the uuid of this commerce pricing class
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the external reference code of this commerce pricing class.
	 *
	 * @return the external reference code of this commerce pricing class
	 */
	@AutoEscape
	public String getExternalReferenceCode();

	/**
	 * Sets the external reference code of this commerce pricing class.
	 *
	 * @param externalReferenceCode the external reference code of this commerce pricing class
	 */
	public void setExternalReferenceCode(String externalReferenceCode);

	/**
	 * Returns the commerce pricing class ID of this commerce pricing class.
	 *
	 * @return the commerce pricing class ID of this commerce pricing class
	 */
	public long getCommercePricingClassId();

	/**
	 * Sets the commerce pricing class ID of this commerce pricing class.
	 *
	 * @param commercePricingClassId the commerce pricing class ID of this commerce pricing class
	 */
	public void setCommercePricingClassId(long commercePricingClassId);

	/**
	 * Returns the company ID of this commerce pricing class.
	 *
	 * @return the company ID of this commerce pricing class
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this commerce pricing class.
	 *
	 * @param companyId the company ID of this commerce pricing class
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this commerce pricing class.
	 *
	 * @return the user ID of this commerce pricing class
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this commerce pricing class.
	 *
	 * @param userId the user ID of this commerce pricing class
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this commerce pricing class.
	 *
	 * @return the user uuid of this commerce pricing class
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this commerce pricing class.
	 *
	 * @param userUuid the user uuid of this commerce pricing class
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this commerce pricing class.
	 *
	 * @return the user name of this commerce pricing class
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this commerce pricing class.
	 *
	 * @param userName the user name of this commerce pricing class
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this commerce pricing class.
	 *
	 * @return the create date of this commerce pricing class
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this commerce pricing class.
	 *
	 * @param createDate the create date of this commerce pricing class
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this commerce pricing class.
	 *
	 * @return the modified date of this commerce pricing class
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this commerce pricing class.
	 *
	 * @param modifiedDate the modified date of this commerce pricing class
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the title of this commerce pricing class.
	 *
	 * @return the title of this commerce pricing class
	 */
	public String getTitle();

	/**
	 * Returns the localized title of this commerce pricing class in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized title of this commerce pricing class
	 */
	@AutoEscape
	public String getTitle(Locale locale);

	/**
	 * Returns the localized title of this commerce pricing class in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized title of this commerce pricing class. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	public String getTitle(Locale locale, boolean useDefault);

	/**
	 * Returns the localized title of this commerce pricing class in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized title of this commerce pricing class
	 */
	@AutoEscape
	public String getTitle(String languageId);

	/**
	 * Returns the localized title of this commerce pricing class in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized title of this commerce pricing class
	 */
	@AutoEscape
	public String getTitle(String languageId, boolean useDefault);

	@AutoEscape
	public String getTitleCurrentLanguageId();

	@AutoEscape
	public String getTitleCurrentValue();

	/**
	 * Returns a map of the locales and localized titles of this commerce pricing class.
	 *
	 * @return the locales and localized titles of this commerce pricing class
	 */
	public Map<Locale, String> getTitleMap();

	/**
	 * Sets the title of this commerce pricing class.
	 *
	 * @param title the title of this commerce pricing class
	 */
	public void setTitle(String title);

	/**
	 * Sets the localized title of this commerce pricing class in the language.
	 *
	 * @param title the localized title of this commerce pricing class
	 * @param locale the locale of the language
	 */
	public void setTitle(String title, Locale locale);

	/**
	 * Sets the localized title of this commerce pricing class in the language, and sets the default locale.
	 *
	 * @param title the localized title of this commerce pricing class
	 * @param locale the locale of the language
	 * @param defaultLocale the default locale
	 */
	public void setTitle(String title, Locale locale, Locale defaultLocale);

	public void setTitleCurrentLanguageId(String languageId);

	/**
	 * Sets the localized titles of this commerce pricing class from the map of locales and localized titles.
	 *
	 * @param titleMap the locales and localized titles of this commerce pricing class
	 */
	public void setTitleMap(Map<Locale, String> titleMap);

	/**
	 * Sets the localized titles of this commerce pricing class from the map of locales and localized titles, and sets the default locale.
	 *
	 * @param titleMap the locales and localized titles of this commerce pricing class
	 * @param defaultLocale the default locale
	 */
	public void setTitleMap(Map<Locale, String> titleMap, Locale defaultLocale);

	/**
	 * Returns the description of this commerce pricing class.
	 *
	 * @return the description of this commerce pricing class
	 */
	public String getDescription();

	/**
	 * Returns the localized description of this commerce pricing class in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized description of this commerce pricing class
	 */
	@AutoEscape
	public String getDescription(Locale locale);

	/**
	 * Returns the localized description of this commerce pricing class in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized description of this commerce pricing class. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	public String getDescription(Locale locale, boolean useDefault);

	/**
	 * Returns the localized description of this commerce pricing class in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized description of this commerce pricing class
	 */
	@AutoEscape
	public String getDescription(String languageId);

	/**
	 * Returns the localized description of this commerce pricing class in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized description of this commerce pricing class
	 */
	@AutoEscape
	public String getDescription(String languageId, boolean useDefault);

	@AutoEscape
	public String getDescriptionCurrentLanguageId();

	@AutoEscape
	public String getDescriptionCurrentValue();

	/**
	 * Returns a map of the locales and localized descriptions of this commerce pricing class.
	 *
	 * @return the locales and localized descriptions of this commerce pricing class
	 */
	public Map<Locale, String> getDescriptionMap();

	/**
	 * Sets the description of this commerce pricing class.
	 *
	 * @param description the description of this commerce pricing class
	 */
	public void setDescription(String description);

	/**
	 * Sets the localized description of this commerce pricing class in the language.
	 *
	 * @param description the localized description of this commerce pricing class
	 * @param locale the locale of the language
	 */
	public void setDescription(String description, Locale locale);

	/**
	 * Sets the localized description of this commerce pricing class in the language, and sets the default locale.
	 *
	 * @param description the localized description of this commerce pricing class
	 * @param locale the locale of the language
	 * @param defaultLocale the default locale
	 */
	public void setDescription(
		String description, Locale locale, Locale defaultLocale);

	public void setDescriptionCurrentLanguageId(String languageId);

	/**
	 * Sets the localized descriptions of this commerce pricing class from the map of locales and localized descriptions.
	 *
	 * @param descriptionMap the locales and localized descriptions of this commerce pricing class
	 */
	public void setDescriptionMap(Map<Locale, String> descriptionMap);

	/**
	 * Sets the localized descriptions of this commerce pricing class from the map of locales and localized descriptions, and sets the default locale.
	 *
	 * @param descriptionMap the locales and localized descriptions of this commerce pricing class
	 * @param defaultLocale the default locale
	 */
	public void setDescriptionMap(
		Map<Locale, String> descriptionMap, Locale defaultLocale);

	/**
	 * Returns the last publish date of this commerce pricing class.
	 *
	 * @return the last publish date of this commerce pricing class
	 */
	public Date getLastPublishDate();

	/**
	 * Sets the last publish date of this commerce pricing class.
	 *
	 * @param lastPublishDate the last publish date of this commerce pricing class
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
	public CommercePricingClass cloneWithOriginalValues();

	public default String toXmlString() {
		return null;
	}

}