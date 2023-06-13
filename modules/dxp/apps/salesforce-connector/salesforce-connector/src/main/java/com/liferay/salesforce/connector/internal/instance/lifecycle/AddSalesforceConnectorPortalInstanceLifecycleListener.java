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

package com.liferay.salesforce.connector.internal.instance.lifecycle;

import com.liferay.dispatch.executor.DispatchTaskExecutor;
import com.liferay.dispatch.model.DispatchTrigger;
import com.liferay.dispatch.repository.DispatchFileRepository;
import com.liferay.dispatch.service.DispatchTriggerLocalService;
import com.liferay.dispatch.talend.archive.TalendArchiveParserUtil;
import com.liferay.document.library.kernel.store.Store;
import com.liferay.document.library.service.DLFileVersionPreviewLocalService;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.File;
import java.io.FileInputStream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Igor Beslic
 */
@Component(
	property = "service.ranking:Integer=100",
	service = PortalInstanceLifecycleListener.class
)
public class AddSalesforceConnectorPortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener {

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {
		_addDispatchTrigger(
			company, "etl-salesforce-account-connector-0.4.zip");
		_addDispatchTrigger(company, "etl-salesforce-order-connector-0.7.zip");
		_addDispatchTrigger(
			company, "etl-salesforce-price-list-connector-0.7.zip");
		_addDispatchTrigger(
			company, "etl-salesforce-product-connector-0.4.zip");
	}

	private void _addDispatchTrigger(Company company, String name)
		throws Exception {

		DispatchTrigger dispatchTrigger =
			_dispatchTriggerLocalService.fetchDispatchTrigger(
				company.getCompanyId(), name);

		if (dispatchTrigger != null) {
			return;
		}

		Class<?> clazz = getClass();

		File connectorArchiveFile = FileUtil.createTempFile(
			clazz.getResourceAsStream("/" + name));

		try (FileInputStream fileInputStream = new FileInputStream(
				connectorArchiveFile)) {

			UnicodeProperties unicodeProperties = new UnicodeProperties();

			TalendArchiveParserUtil.updateUnicodeProperties(
				fileInputStream, unicodeProperties);

			long userId = _userLocalService.getDefaultUserId(
				company.getCompanyId());

			dispatchTrigger = _dispatchTriggerLocalService.addDispatchTrigger(
				null, userId, _dispatchTaskExecutor, "talend",
				unicodeProperties, name, true);

			_dispatchFileRepository.addFileEntry(
				userId, dispatchTrigger.getDispatchTriggerId(), name, 0,
				"application/zip", new FileInputStream(connectorArchiveFile));
		}
		finally {
			FileUtil.delete(connectorArchiveFile);
		}
	}

	@Reference
	private DispatchFileRepository _dispatchFileRepository;

	@Reference(target = "(dispatch.task.executor.type=talend)")
	private DispatchTaskExecutor _dispatchTaskExecutor;

	@Reference
	private DispatchTriggerLocalService _dispatchTriggerLocalService;

	@Reference
	private DLFileVersionPreviewLocalService _dlFileVersionPreviewLocalService;

	@Reference(target = "(default=true)")
	private Store _store;

	@Reference
	private UserLocalService _userLocalService;

}