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

package com.liferay.headless.site.internal.resource.v1_0;

import com.liferay.headless.site.dto.v1_0.Site;
import com.liferay.headless.site.resource.v1_0.SiteResource;
import com.liferay.portal.kernel.change.tracking.CTTransactionException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.LayoutSetPrototype;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.GroupService;
import com.liferay.portal.kernel.service.LayoutSetPrototypeLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.liveusers.LiveUsers;
import com.liferay.portal.security.permission.PermissionCacheUtil;
import com.liferay.site.initializer.SiteInitializer;
import com.liferay.site.initializer.SiteInitializerRegistry;
import com.liferay.sites.kernel.util.Sites;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Rubén Pulido
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/site.properties",
	scope = ServiceScope.PROTOTYPE, service = SiteResource.class
)
public class SiteResourceImpl extends BaseSiteResourceImpl {

	@Override
	public Site postSite(Site site) throws Exception {
		try {
			Group group = TransactionInvokerUtil.invoke(
				_transactionConfig, new GroupCallable(site));

			return new Site() {
				{
					friendlyUrlPath = group.getFriendlyURL();
					id = group.getGroupId();
					key = group.getGroupKey();
					name = group.getName(LocaleUtil.getDefault());
				}
			};
		}
		catch (CTTransactionException ctTransactionException) {
			throw ctTransactionException;
		}
		catch (Throwable throwable) {
			throw new Exception(throwable);
		}
	}

	private Group _addGroup(Site site) throws Exception {
		if (Validator.isNull(site.getTemplateKey()) &&
			Validator.isNotNull(site.getTemplateType())) {

			throw new IllegalArgumentException(
				"Template key cannot be empty if template type is specified");
		}

		if (Validator.isNotNull(site.getTemplateKey()) &&
			Validator.isNull(site.getTemplateType())) {

			throw new IllegalArgumentException(
				"Template type cannot be empty if template key is specified");
		}

		if (Objects.equals(
				Site.TemplateType.SITE_INITIALIZER, site.getTemplateType())) {

			SiteInitializer siteInitializer =
				_siteInitializerRegistry.getSiteInitializer(
					site.getTemplateKey());

			if (siteInitializer == null) {
				throw new IllegalArgumentException(
					"No site initializer was found for site template key " +
						site.getTemplateKey());
			}

			if (!siteInitializer.isActive(contextCompany.getCompanyId())) {
				throw new IllegalArgumentException(
					"Site initializer with site template key " +
						site.getTemplateKey() + " is inactive");
			}
		}
		else if (Objects.equals(
					Site.TemplateType.SITE_TEMPLATE, site.getTemplateType())) {

			LayoutSetPrototype layoutSetPrototype =
				_layoutSetPrototypeLocalService.fetchLayoutSetPrototype(
					GetterUtil.getLongStrict(site.getTemplateKey()));

			if (layoutSetPrototype == null) {
				throw new IllegalArgumentException(
					"No site template found for site template key " +
						site.getTemplateKey());
			}

			if (!layoutSetPrototype.isActive()) {
				throw new IllegalArgumentException(
					"Site template with site template key " +
						site.getTemplateKey() + " is inactive");
			}
		}

		ServiceContext serviceContext = new ServiceContext() {
			{
				setCompanyId(contextCompany.getCompanyId());
				setLanguageId(LocaleUtil.toLanguageId(LocaleUtil.getDefault()));
				setUserId(contextUser.getUserId());
			}
		};

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			return _addGroup(site, serviceContext);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	private Group _addGroup(Site site, ServiceContext serviceContext)
		throws Exception {

		long parentGroupId = GroupConstants.DEFAULT_PARENT_GROUP_ID;

		if (Validator.isNotNull(site.getParentSiteKey())) {
			Group parentGroup = _groupLocalService.getGroup(
				contextCompany.getCompanyId(), site.getParentSiteKey());

			parentGroupId = parentGroup.getGroupId();
		}

		Map<Locale, String> nameMap = new HashMap<>();

		if (Validator.isNotNull(site.getName())) {
			nameMap.put(LocaleUtil.getDefault(), site.getName());
		}

		int type = GroupConstants.TYPE_SITE_OPEN;

		Site.MembershipType membershipType = site.getMembershipType();

		if (membershipType != null) {
			if (membershipType.equals(Site.MembershipType.PRIVATE)) {
				type = GroupConstants.TYPE_SITE_PRIVATE;
			}
			else if (membershipType.equals(Site.MembershipType.RESTRICTED)) {
				type = GroupConstants.TYPE_SITE_RESTRICTED;
			}
		}

		Group group = _groupService.addGroup(
			parentGroupId, GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap, null,
			type, true, GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, null,
			true, false, true, serviceContext);

		LiveUsers.joinGroup(
			contextCompany.getCompanyId(), group.getGroupId(),
			contextUser.getUserId());

		if (Objects.equals(
				Site.TemplateType.SITE_TEMPLATE, site.getTemplateType())) {

			_sites.updateLayoutSetPrototypesLinks(
				group, GetterUtil.getLongStrict(site.getTemplateKey()), 0L,
				true, false);
		}
		else {
			String siteInitializerKey = "blank-site-initializer";

			if (Validator.isNotNull(site.getTemplateKey())) {
				siteInitializerKey = site.getTemplateKey();
			}

			SiteInitializer siteInitializer =
				_siteInitializerRegistry.getSiteInitializer(siteInitializerKey);

			siteInitializer.initialize(group.getGroupId());
		}

		return group;
	}

	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRED, new Class<?>[] {Exception.class});

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private GroupService _groupService;

	@Reference
	private LayoutSetPrototypeLocalService _layoutSetPrototypeLocalService;

	@Reference
	private SiteInitializerRegistry _siteInitializerRegistry;

	@Reference
	private Sites _sites;

	private class GroupCallable implements Callable<Group> {

		@Override
		public Group call() throws Exception {
			try {
				return _addGroup(_site);
			}
			catch (Exception exception) {

				// LPS-169057

				PermissionCacheUtil.clearCache(contextUser.getUserId());

				throw exception;
			}
		}

		private GroupCallable(Site site) {
			_site = site;
		}

		private final Site _site;

	}

}