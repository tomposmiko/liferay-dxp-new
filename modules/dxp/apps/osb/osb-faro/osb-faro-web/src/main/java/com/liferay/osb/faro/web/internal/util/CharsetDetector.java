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

package com.liferay.osb.faro.web.internal.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.Charset;

import org.mozilla.universalchardet.UniversalDetector;

/**
 * @author Alejo Ceballos
 */
public class CharsetDetector {

	public Charset detect(File file) throws IOException {
		UniversalDetector universalDetector = new UniversalDetector();

		try (InputStream fileInputStream = new FileInputStream(file)) {
			byte[] bytes = new byte[4096];
			int length = 0;

			while (((length = fileInputStream.read(bytes)) > 0) &&
				   !universalDetector.isDone()) {

				universalDetector.handleData(bytes, 0, length);
			}

			universalDetector.dataEnd();

			try {
				return Charset.forName(universalDetector.getDetectedCharset());
			}
			catch (IllegalArgumentException illegalArgumentException) {
				_log.error(illegalArgumentException);

				return null;
			}
		}
		finally {
			universalDetector.reset();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CharsetDetector.class);

}