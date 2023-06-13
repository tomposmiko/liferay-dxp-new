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

package com.liferay.portal.workflow.kaleo.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.LocalizedModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.WorkflowedModel;
import com.liferay.portal.kernel.model.change.tracking.CTModel;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the KaleoDefinitionVersion service. Represents a row in the &quot;KaleoDefinitionVersion&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.workflow.kaleo.model.impl.KaleoDefinitionVersionModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.workflow.kaleo.model.impl.KaleoDefinitionVersionImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoDefinitionVersion
 * @generated
 */
@ProviderType
public interface KaleoDefinitionVersionModel
	extends BaseModel<KaleoDefinitionVersion>, CTModel<KaleoDefinitionVersion>,
			GroupedModel, LocalizedModel, MVCCModel, ShardedModel,
			WorkflowedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a kaleo definition version model instance should use the {@link KaleoDefinitionVersion} interface instead.
	 */

	/**
	 * Returns the primary key of this kaleo definition version.
	 *
	 * @return the primary key of this kaleo definition version
	 */
	@Override
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this kaleo definition version.
	 *
	 * @param primaryKey the primary key of this kaleo definition version
	 */
	@Override
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this kaleo definition version.
	 *
	 * @return the mvcc version of this kaleo definition version
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this kaleo definition version.
	 *
	 * @param mvccVersion the mvcc version of this kaleo definition version
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the ct collection ID of this kaleo definition version.
	 *
	 * @return the ct collection ID of this kaleo definition version
	 */
	@Override
	public long getCtCollectionId();

	/**
	 * Sets the ct collection ID of this kaleo definition version.
	 *
	 * @param ctCollectionId the ct collection ID of this kaleo definition version
	 */
	@Override
	public void setCtCollectionId(long ctCollectionId);

	/**
	 * Returns the kaleo definition version ID of this kaleo definition version.
	 *
	 * @return the kaleo definition version ID of this kaleo definition version
	 */
	public long getKaleoDefinitionVersionId();

	/**
	 * Sets the kaleo definition version ID of this kaleo definition version.
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID of this kaleo definition version
	 */
	public void setKaleoDefinitionVersionId(long kaleoDefinitionVersionId);

	/**
	 * Returns the group ID of this kaleo definition version.
	 *
	 * @return the group ID of this kaleo definition version
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this kaleo definition version.
	 *
	 * @param groupId the group ID of this kaleo definition version
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this kaleo definition version.
	 *
	 * @return the company ID of this kaleo definition version
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this kaleo definition version.
	 *
	 * @param companyId the company ID of this kaleo definition version
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this kaleo definition version.
	 *
	 * @return the user ID of this kaleo definition version
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this kaleo definition version.
	 *
	 * @param userId the user ID of this kaleo definition version
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this kaleo definition version.
	 *
	 * @return the user uuid of this kaleo definition version
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this kaleo definition version.
	 *
	 * @param userUuid the user uuid of this kaleo definition version
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this kaleo definition version.
	 *
	 * @return the user name of this kaleo definition version
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this kaleo definition version.
	 *
	 * @param userName the user name of this kaleo definition version
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this kaleo definition version.
	 *
	 * @return the create date of this kaleo definition version
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this kaleo definition version.
	 *
	 * @param createDate the create date of this kaleo definition version
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this kaleo definition version.
	 *
	 * @return the modified date of this kaleo definition version
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this kaleo definition version.
	 *
	 * @param modifiedDate the modified date of this kaleo definition version
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the kaleo definition ID of this kaleo definition version.
	 *
	 * @return the kaleo definition ID of this kaleo definition version
	 */
	public long getKaleoDefinitionId();

	/**
	 * Sets the kaleo definition ID of this kaleo definition version.
	 *
	 * @param kaleoDefinitionId the kaleo definition ID of this kaleo definition version
	 */
	public void setKaleoDefinitionId(long kaleoDefinitionId);

	/**
	 * Returns the name of this kaleo definition version.
	 *
	 * @return the name of this kaleo definition version
	 */
	@AutoEscape
	public String getName();

	/**
	 * Sets the name of this kaleo definition version.
	 *
	 * @param name the name of this kaleo definition version
	 */
	public void setName(String name);

	/**
	 * Returns the title of this kaleo definition version.
	 *
	 * @return the title of this kaleo definition version
	 */
	public String getTitle();

	/**
	 * Returns the localized title of this kaleo definition version in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized title of this kaleo definition version
	 */
	@AutoEscape
	public String getTitle(Locale locale);

	/**
	 * Returns the localized title of this kaleo definition version in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized title of this kaleo definition version. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	public String getTitle(Locale locale, boolean useDefault);

	/**
	 * Returns the localized title of this kaleo definition version in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized title of this kaleo definition version
	 */
	@AutoEscape
	public String getTitle(String languageId);

	/**
	 * Returns the localized title of this kaleo definition version in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized title of this kaleo definition version
	 */
	@AutoEscape
	public String getTitle(String languageId, boolean useDefault);

	@AutoEscape
	public String getTitleCurrentLanguageId();

	@AutoEscape
	public String getTitleCurrentValue();

	/**
	 * Returns a map of the locales and localized titles of this kaleo definition version.
	 *
	 * @return the locales and localized titles of this kaleo definition version
	 */
	public Map<Locale, String> getTitleMap();

	/**
	 * Sets the title of this kaleo definition version.
	 *
	 * @param title the title of this kaleo definition version
	 */
	public void setTitle(String title);

	/**
	 * Sets the localized title of this kaleo definition version in the language.
	 *
	 * @param title the localized title of this kaleo definition version
	 * @param locale the locale of the language
	 */
	public void setTitle(String title, Locale locale);

	/**
	 * Sets the localized title of this kaleo definition version in the language, and sets the default locale.
	 *
	 * @param title the localized title of this kaleo definition version
	 * @param locale the locale of the language
	 * @param defaultLocale the default locale
	 */
	public void setTitle(String title, Locale locale, Locale defaultLocale);

	public void setTitleCurrentLanguageId(String languageId);

	/**
	 * Sets the localized titles of this kaleo definition version from the map of locales and localized titles.
	 *
	 * @param titleMap the locales and localized titles of this kaleo definition version
	 */
	public void setTitleMap(Map<Locale, String> titleMap);

	/**
	 * Sets the localized titles of this kaleo definition version from the map of locales and localized titles, and sets the default locale.
	 *
	 * @param titleMap the locales and localized titles of this kaleo definition version
	 * @param defaultLocale the default locale
	 */
	public void setTitleMap(Map<Locale, String> titleMap, Locale defaultLocale);

	/**
	 * Returns the description of this kaleo definition version.
	 *
	 * @return the description of this kaleo definition version
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this kaleo definition version.
	 *
	 * @param description the description of this kaleo definition version
	 */
	public void setDescription(String description);

	/**
	 * Returns the content of this kaleo definition version.
	 *
	 * @return the content of this kaleo definition version
	 */
	@AutoEscape
	public String getContent();

	/**
	 * Sets the content of this kaleo definition version.
	 *
	 * @param content the content of this kaleo definition version
	 */
	public void setContent(String content);

	/**
	 * Returns the version of this kaleo definition version.
	 *
	 * @return the version of this kaleo definition version
	 */
	@AutoEscape
	public String getVersion();

	/**
	 * Sets the version of this kaleo definition version.
	 *
	 * @param version the version of this kaleo definition version
	 */
	public void setVersion(String version);

	/**
	 * Returns the start kaleo node ID of this kaleo definition version.
	 *
	 * @return the start kaleo node ID of this kaleo definition version
	 */
	public long getStartKaleoNodeId();

	/**
	 * Sets the start kaleo node ID of this kaleo definition version.
	 *
	 * @param startKaleoNodeId the start kaleo node ID of this kaleo definition version
	 */
	public void setStartKaleoNodeId(long startKaleoNodeId);

	/**
	 * Returns the status of this kaleo definition version.
	 *
	 * @return the status of this kaleo definition version
	 */
	@Override
	public int getStatus();

	/**
	 * Sets the status of this kaleo definition version.
	 *
	 * @param status the status of this kaleo definition version
	 */
	@Override
	public void setStatus(int status);

	/**
	 * Returns the status by user ID of this kaleo definition version.
	 *
	 * @return the status by user ID of this kaleo definition version
	 */
	@Override
	public long getStatusByUserId();

	/**
	 * Sets the status by user ID of this kaleo definition version.
	 *
	 * @param statusByUserId the status by user ID of this kaleo definition version
	 */
	@Override
	public void setStatusByUserId(long statusByUserId);

	/**
	 * Returns the status by user uuid of this kaleo definition version.
	 *
	 * @return the status by user uuid of this kaleo definition version
	 */
	@Override
	public String getStatusByUserUuid();

	/**
	 * Sets the status by user uuid of this kaleo definition version.
	 *
	 * @param statusByUserUuid the status by user uuid of this kaleo definition version
	 */
	@Override
	public void setStatusByUserUuid(String statusByUserUuid);

	/**
	 * Returns the status by user name of this kaleo definition version.
	 *
	 * @return the status by user name of this kaleo definition version
	 */
	@AutoEscape
	@Override
	public String getStatusByUserName();

	/**
	 * Sets the status by user name of this kaleo definition version.
	 *
	 * @param statusByUserName the status by user name of this kaleo definition version
	 */
	@Override
	public void setStatusByUserName(String statusByUserName);

	/**
	 * Returns the status date of this kaleo definition version.
	 *
	 * @return the status date of this kaleo definition version
	 */
	@Override
	public Date getStatusDate();

	/**
	 * Sets the status date of this kaleo definition version.
	 *
	 * @param statusDate the status date of this kaleo definition version
	 */
	@Override
	public void setStatusDate(Date statusDate);

	/**
	 * Returns <code>true</code> if this kaleo definition version is approved.
	 *
	 * @return <code>true</code> if this kaleo definition version is approved; <code>false</code> otherwise
	 */
	@Override
	public boolean isApproved();

	/**
	 * Returns <code>true</code> if this kaleo definition version is denied.
	 *
	 * @return <code>true</code> if this kaleo definition version is denied; <code>false</code> otherwise
	 */
	@Override
	public boolean isDenied();

	/**
	 * Returns <code>true</code> if this kaleo definition version is a draft.
	 *
	 * @return <code>true</code> if this kaleo definition version is a draft; <code>false</code> otherwise
	 */
	@Override
	public boolean isDraft();

	/**
	 * Returns <code>true</code> if this kaleo definition version is expired.
	 *
	 * @return <code>true</code> if this kaleo definition version is expired; <code>false</code> otherwise
	 */
	@Override
	public boolean isExpired();

	/**
	 * Returns <code>true</code> if this kaleo definition version is inactive.
	 *
	 * @return <code>true</code> if this kaleo definition version is inactive; <code>false</code> otherwise
	 */
	@Override
	public boolean isInactive();

	/**
	 * Returns <code>true</code> if this kaleo definition version is incomplete.
	 *
	 * @return <code>true</code> if this kaleo definition version is incomplete; <code>false</code> otherwise
	 */
	@Override
	public boolean isIncomplete();

	/**
	 * Returns <code>true</code> if this kaleo definition version is pending.
	 *
	 * @return <code>true</code> if this kaleo definition version is pending; <code>false</code> otherwise
	 */
	@Override
	public boolean isPending();

	/**
	 * Returns <code>true</code> if this kaleo definition version is scheduled.
	 *
	 * @return <code>true</code> if this kaleo definition version is scheduled; <code>false</code> otherwise
	 */
	@Override
	public boolean isScheduled();

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
	public KaleoDefinitionVersion cloneWithOriginalValues();

	public default String toXmlString() {
		return null;
	}

}