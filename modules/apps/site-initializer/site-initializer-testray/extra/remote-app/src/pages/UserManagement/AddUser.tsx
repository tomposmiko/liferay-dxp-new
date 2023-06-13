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

import ClayButton from '@clayui/button';
import ClayForm, {ClayCheckbox} from '@clayui/form';
import ClayLayout from '@clayui/layout';
import {useState} from 'react';

import Input from '../../components/Input';
import Container from '../../components/Layout/Container';
import i18n from '../../i18n';

const AddUser: React.FC = () => {
	const [form, setForm] = useState({
		confirmPassword: '',
		emailAddress: '',
		firstName: '',
		lastName: '',
		password: '',
		screenName: '',
		testrayAdministrator: false,
		testrayAnalyst: false,
		testrayLead: false,
		testrayUser: false,
	});

	const onChange = (event: any) => {
		const {
			target: {checked, name, type, ...target},
		} = event;

		let {value} = target;

		if (type === 'checkbox') {
			value = checked;
		}

		setForm({
			...form,
			[name]: value,
		});
	};

	return (
		<ClayLayout.Container>
			<Container>
				<ClayForm>
					<ClayLayout.Row justify="start">
						<ClayLayout.Col size={3} sm={12} xl={3}>
							<h5 className="font-weight-normal">
								User Information
							</h5>
						</ClayLayout.Col>

						<ClayLayout.Col size={3} sm={12} xl={7}>
							<ClayForm.Group className="form-group-sm">
								<Input
									label={i18n.translate('first-name')}
									name="firstName"
									onChange={onChange}
									required
								/>

								<Input
									label={i18n.translate('last-name')}
									name="lastName"
									onChange={onChange}
									required
								/>

								<Input
									label={i18n.translate('email-address')}
									name="emailAddress"
									onChange={onChange}
									required
									type="email"
								/>

								<Input
									label={i18n.translate('screen-name')}
									name={form.screenName}
									onChange={onChange}
									required
								/>
							</ClayForm.Group>
						</ClayLayout.Col>
					</ClayLayout.Row>

					<hr />

					<ClayLayout.Row justify="start">
						<ClayLayout.Col size={3} sm={12} xl={3}>
							<h5 className="font-weight-normal">Password</h5>
						</ClayLayout.Col>

						<ClayLayout.Col size={3} sm={12} xl={3}>
							<ClayForm.Group className="form-group-sm">
								<Input
									label={i18n.translate('password')}
									name={form.password}
									onChange={onChange}
									required
									type="password"
								/>

								<Input
									label="Confirm Password"
									name={form.confirmPassword}
									onChange={onChange}
									required
									type="password"
								/>
							</ClayForm.Group>
						</ClayLayout.Col>
					</ClayLayout.Row>

					<hr />

					<ClayLayout.Row justify="start">
						<ClayLayout.Col size={3} sm={12} xl={3}>
							<h5 className="font-weight-normal">Roles</h5>
						</ClayLayout.Col>

						<ClayLayout.Col size={3} sm={12} xl={9}>
							<div>
								<ClayCheckbox
									checked={form.testrayAdministrator}
									label="Testray Administrator"
									name="testrayAdministrator"
									onChange={onChange}
								/>
							</div>

							<div>
								<ClayCheckbox
									checked={form.testrayAnalyst}
									label="Testray Analyst"
									name="testrayAnalyst"
									onChange={onChange}
								/>
							</div>

							<div>
								<ClayCheckbox
									checked={form.testrayLead}
									label="Testray Lead"
									name="testrayLead"
									onChange={onChange}
								/>
							</div>

							<div>
								<ClayCheckbox
									checked={form.testrayUser}
									label="Testray User"
									name="testrayUser"
									onChange={onChange}
								/>
							</div>
						</ClayLayout.Col>
					</ClayLayout.Row>

					<ClayLayout.Row>
						<ClayLayout.Col>
							<ClayButton.Group
								className="form-group-sm mt-5"
								key={3}
								spaced
							>
								<ClayButton className="bg-primary-2 borderless mr-2 primary text-primary-7">
									Save
								</ClayButton>

								<ClayButton displayType="secondary">
									Cancel
								</ClayButton>
							</ClayButton.Group>
						</ClayLayout.Col>
					</ClayLayout.Row>
				</ClayForm>
			</Container>
		</ClayLayout.Container>
	);
};

export default AddUser;
