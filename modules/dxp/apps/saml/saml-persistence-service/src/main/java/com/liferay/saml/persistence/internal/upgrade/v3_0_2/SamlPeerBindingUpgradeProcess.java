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

package com.liferay.saml.persistence.internal.upgrade.v3_0_2;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.saml.persistence.model.impl.SamlPeerBindingImpl;

/**
 * @author Stian Sigvartsen
 */
public class SamlPeerBindingUpgradeProcess extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		_dropIndex(SamlPeerBindingImpl.TABLE_NAME, "IX_E642E1AE");

		_dropIndex(SamlPeerBindingImpl.TABLE_NAME, "IX_81ACF542");

		_dropIndex(SamlPeerBindingImpl.TABLE_NAME, "IX_BC82BDFC");
	}

	private void _dropIndex(String tableName, String indexName)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info(
				String.format(
					"Dropping index %s from table %s", indexName, tableName));
		}

		if (hasIndex(tableName, indexName)) {
			runSQL(
				StringBundler.concat(
					"drop index ", indexName, " on ", tableName));
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SamlPeerBindingUpgradeProcess.class);

}