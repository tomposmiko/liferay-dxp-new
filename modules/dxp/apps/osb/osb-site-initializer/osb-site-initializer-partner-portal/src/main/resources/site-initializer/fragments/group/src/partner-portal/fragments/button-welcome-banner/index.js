/* eslint-disable no-undef */
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

const videoButtons = fragmentElement.querySelectorAll('.video-tour-button');

if (layoutMode !== 'edit') {
	for (const videoButton of videoButtons) {
		videoButton.onclick = () =>
			Liferay.Util.openModal({
				bodyHTML: `<iframe width="100%" height="500" src=${configuration.videoButtonLink} frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>`,
				size: 'lg',
			});
	}
}
