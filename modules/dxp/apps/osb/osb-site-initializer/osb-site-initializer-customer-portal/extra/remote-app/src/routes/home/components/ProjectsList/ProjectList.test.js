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

import {render, screen} from '@testing-library/react';
import ProjectList from '.';

describe('Project List', () => {
	window.IntersectionObserver = jest.fn(() => ({
		observer: jest.fn(),
		unobserver: jest.fn(),
	}));

	it('displays "results not found" message if there is no project to show', () => {
		render(<ProjectList />);

		const showNotFoundMessage = screen.getByText(/no results found/i);
		expect(showNotFoundMessage).toBeInTheDocument();
	});

	it('displays projects as cards if has less than 05 projects', () => {
		const {container} = render(<ProjectList hasManyProjects={false} />);

		expect(
			container.getElementsByClassName('cp-home-projects').length
		).toBe(1);
	});

	it('displays projects as a list if has more than 05 projects', () => {
		const {container} = render(<ProjectList hasManyProjects />);

		expect(
			container.getElementsByClassName('cp-home-projects-sm').length
		).toBe(1);
	});
});
