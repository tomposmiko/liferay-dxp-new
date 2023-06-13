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

import ProjectSupport from '../../components/ProjectSupport';
import QuickLinksPanel from '../../containers/QuickLinksPanel';

const Layout = ({children, hasProjectContact, hasQuickLinks, project}) => {
	return (
		<div className="d-flex position-relative w-100">
			<div className="w-100">
				{hasProjectContact && <ProjectSupport project={project} />}

				{children}
			</div>

			{hasQuickLinks && (
				<QuickLinksPanel accountKey={project.accountKey} />
			)}
		</div>
	);
};

export default Layout;
