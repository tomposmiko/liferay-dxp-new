/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.web.internal.activator;

import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoTableConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.oauth2.provider.model.OAuth2Authorization;
import com.liferay.oauth2.provider.scope.spi.prefix.handler.PrefixHandler;
import com.liferay.oauth2.provider.scope.spi.prefix.handler.PrefixHandlerFactory;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import com.liferay.oauth2.provider.scope.spi.scope.mapper.ScopeMapper;
import com.liferay.osb.faro.web.internal.application.ApiApplication;
import com.liferay.osb.faro.web.internal.controller.api.RecommendationController;
import com.liferay.osb.faro.web.internal.controller.api.ReportController;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.security.service.access.policy.model.SAPEntry;
import com.liferay.portal.security.service.access.policy.service.SAPEntryLocalService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = {
		"osgi.jaxrs.name=Liferay.Analytics.Cloud.REST", "sap.scope.finder=true"
	},
	service = {
		PortalInstanceLifecycleListener.class, PrefixHandlerFactory.class,
		ScopeFinder.class, ScopeMapper.class
	}
)
public class OAuth2AuthorizationExpandoPortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener
	implements PrefixHandlerFactory, ScopeFinder, ScopeMapper {

	@Override
	public PrefixHandler create(
		Function<String, Object> propertyAccessorFunction) {

		return PrefixHandler.PASS_THROUGH_PREFIX_HANDLER;
	}

	@Override
	public Collection<String> findScopes() {
		return _scopeAliasesList;
	}

	@Override
	public Set<String> map(String scope) {
		return Collections.singleton(scope);
	}

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {
		_addSAPEntries(company.getCompanyId());

		Long companyId = CompanyThreadLocal.getCompanyId();

		try {
			CompanyThreadLocal.setCompanyId(company.getCompanyId());

			long classNameId = _classNameLocalService.getClassNameId(
				OAuth2Authorization.class.getName());

			ExpandoTable expandoTable = _expandoTableLocalService.fetchTable(
				company.getCompanyId(), classNameId,
				ExpandoTableConstants.DEFAULT_TABLE_NAME);

			if (expandoTable == null) {
				expandoTable = _expandoTableLocalService.addTable(
					company.getCompanyId(), classNameId,
					ExpandoTableConstants.DEFAULT_TABLE_NAME);
			}

			addExpandoColumn(
				expandoTable, "groupId", ExpandoColumnConstants.LONG);
		}
		finally {
			CompanyThreadLocal.setCompanyId(companyId);
		}
	}

	@Override
	public void portalInstanceUnregistered(Company company) {
		_deleteSAPEntries(company.getCompanyId());

		ExpandoTable expandoTable = _expandoTableLocalService.fetchTable(
			company.getCompanyId(),
			_classNameLocalService.getClassNameId(
				OAuth2Authorization.class.getName()),
			ExpandoTableConstants.DEFAULT_TABLE_NAME);

		_expandoColumnLocalService.deleteColumn(
			expandoTable.getTableId(), "groupId");

		List<ExpandoColumn> expandoColumns =
			_expandoColumnLocalService.getColumns(expandoTable.getTableId());

		if (expandoColumns.isEmpty()) {
			_expandoTableLocalService.deleteExpandoTable(expandoTable);
		}
	}

	@Activate
	protected void activate() {
		Stream<String[]> stream = Arrays.stream(_SAP_ENTRY_OBJECT_ARRAYS);

		_scopeAliasesList = stream.map(
			sapEntryObjectArray -> StringUtil.replaceFirst(
				sapEntryObjectArray[0], "OAUTH2_", StringPool.BLANK)
		).collect(
			Collectors.toList()
		);
	}

	protected void addExpandoColumn(
			ExpandoTable expandoTable, String name, int type)
		throws Exception {

		ExpandoColumn expandoColumn = _expandoColumnLocalService.getColumn(
			expandoTable.getTableId(), name);

		if (expandoColumn != null) {
			return;
		}

		_expandoColumnLocalService.addColumn(
			expandoTable.getTableId(), name, type);
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	private void _addSAPEntries(long companyId) throws Exception {
		for (String[] sapEntryObjectArray : _SAP_ENTRY_OBJECT_ARRAYS) {
			String sapEntryName = sapEntryObjectArray[0];

			SAPEntry sapEntry = _sapEntryLocalService.fetchSAPEntry(
				companyId, sapEntryName);

			if (sapEntry != null) {
				continue;
			}

			_sapEntryLocalService.addSAPEntry(
				_userLocalService.getDefaultUserId(companyId),
				sapEntryObjectArray[1], true, true, sapEntryName,
				Collections.singletonMap(LocaleUtil.getDefault(), sapEntryName),
				new ServiceContext());
		}
	}

	private void _deleteSAPEntries(long companyId) {
		for (String[] sapEntryObjectArray : _SAP_ENTRY_OBJECT_ARRAYS) {
			try {
				SAPEntry sapEntry = _sapEntryLocalService.fetchSAPEntry(
					companyId, sapEntryObjectArray[0]);

				if (sapEntry != null) {
					_sapEntryLocalService.deleteSAPEntry(sapEntry);
				}
			}
			catch (Exception exception) {
				_log.error(exception, exception);
			}
		}
	}

	private static final String[][] _SAP_ENTRY_OBJECT_ARRAYS = {
		{
			"OAUTH2_" +
				ApiApplication.OAuth2ScopeAliases.RECOMMENDATIONS_EVERYTHING,
			RecommendationController.class.getName() + "*"
		},
		{
			"OAUTH2_" + ApiApplication.OAuth2ScopeAliases.REPORTS_EVERYTHING,
			ReportController.class.getName() + "*"
		}
	};

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2AuthorizationExpandoPortalInstanceLifecycleListener.class);

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private ExpandoColumnLocalService _expandoColumnLocalService;

	@Reference
	private ExpandoTableLocalService _expandoTableLocalService;

	@Reference
	private SAPEntryLocalService _sapEntryLocalService;

	private List<String> _scopeAliasesList;

	@Reference
	private UserLocalService _userLocalService;

}