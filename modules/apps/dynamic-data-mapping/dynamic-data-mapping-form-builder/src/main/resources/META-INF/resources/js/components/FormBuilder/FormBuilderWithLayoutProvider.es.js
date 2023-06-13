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

import Component from 'metal-jsx';

import LayoutProvider from '../LayoutProvider/LayoutProvider.es';
import FormBuilder from './FormBuilder.es';

/**
 * LayoutProvider listens to your children's events to
 * control the `pages` and make manipulations.
 * @extends Component
 */

class LayoutProviderWithFormBuilder extends Component {
	render() {
		const {formBuilderProps, layoutProviderProps} = this.props;

		const LProvider = LayoutProvider;

		const FBuilder = FormBuilder;

		return (
			<LProvider {...layoutProviderProps}>
				<FBuilder {...formBuilderProps} />
			</LProvider>
		);
	}
}

export default LayoutProviderWithFormBuilder;
