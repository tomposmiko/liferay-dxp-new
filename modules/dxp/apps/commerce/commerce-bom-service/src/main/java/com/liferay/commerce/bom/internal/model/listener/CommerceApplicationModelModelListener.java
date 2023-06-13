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

package com.liferay.commerce.bom.internal.model.listener;

import com.liferay.commerce.application.model.CommerceApplicationModel;
import com.liferay.commerce.application.service.CommerceApplicationModelLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(enabled = false, immediate = true, service = ModelListener.class)
public class CommerceApplicationModelModelListener
	extends BaseModelListener<CommerceApplicationModel> {

	@Override
	public void onBeforeRemove(
		CommerceApplicationModel commerceApplicationModel) {

		try {
			_commerceApplicationModelLocalService.
				deleteCommerceApplicationModels(
					commerceApplicationModel.getCommerceApplicationModelId());
		}
		catch (PortalException portalException) {
			if (_log.isWarnEnabled()) {
				_log.warn(portalException, portalException);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceApplicationModelModelListener.class);

	@Reference
	private CommerceApplicationModelLocalService
		_commerceApplicationModelLocalService;

}