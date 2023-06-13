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

import {ClayButtonWithIcon} from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import {navigate} from 'frontend-js-web';
import React from 'react';

const availableActionMethods: IAvailableActionMethods = {
	BLANK: (url: string) => {
		window.open(url, '_blank', 'noopener');
	},
	NAVIGATE: (url: string) => {
		navigate(url);
	},
	SUBMIT_FORM: (url: string): void => {
		window.submitForm((document as IDocument).hrefFm, url);
	},
};

const VersionActions = ({actions}: IProps) => {
	const handleActionClick = ({
		type,
		url,
	}: {
		type: string;
		url: string;
	}): void => {
		if (type in availableActionMethods) {
			availableActionMethods[type as keyof IAvailableActionMethods](url);
		}
		else {
			if (
				process.env.NODE_ENV === 'development' ||
				process.env.NODE_ENV === 'test'
			) {
				console.error(
					`No action type method called ${type} is available in availableActionMethods object definition`
				);
			}
		}
	};

	return (
		<ClayDropDown
			className="align-self-start pt-2"
			closeOnClick
			data-tooltip-align="left"
			title={Liferay.Language.get('actions')}
			trigger={
				<ClayButtonWithIcon
					borderless
					displayType="secondary"
					symbol="ellipsis-v"
				/>
			}
		>
			<ClayDropDown.ItemList>
				{actions.map(({icon, label, name, type, url}) => (
					<ClayDropDown.Item
						key={name}
						onClick={() => handleActionClick({type, url})}
					>
						<ClayIcon symbol={icon || ''} />

						<span className="pl-3">{label}</span>
					</ClayDropDown.Item>
				))}
			</ClayDropDown.ItemList>
		</ClayDropDown>
	);
};

interface IProps {
	actions: IAction[];
}

export interface IAction {
	icon?: string;
	label: string;
	name: string;
	type: string;
	url: string;
}

declare global {
	interface Window {
		submitForm: (form: HTMLElement, url: string) => void;
	}

	interface IDocument extends Document {
		hrefFm: HTMLElement;
	}
}

interface IAvailableActionMethods {
	BLANK: (url: string) => void;
	NAVIGATE: (url: string) => void;
	SUBMIT_FORM: (url: string) => void;
}

export default VersionActions;
