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
import ClayForm, {ClayInput} from '@clayui/form';
import {openSelectionModal} from 'frontend-js-web';
import React, {useState} from 'react';

export default function ThemeCSSReplacementSelector({
	placeholder,
	portletNamespace,
	selectThemeCSSClientExtensionEventName,
	selectThemeCSSClientExtensionURL,
	themeCSSCETExternalReferenceCode,
	themeCSSExtensionName,
}) {
	const [extensionName, setExtensionName] = useState(themeCSSExtensionName);
	const [cetExternalReferenceCode, setCETExternalReferenceCode] = useState(
		themeCSSCETExternalReferenceCode
	);

	const onClick = () => {
		openSelectionModal({
			onSelect: (selectedItem) => {
				const item = JSON.parse(selectedItem.value);

				setCETExternalReferenceCode(item.cetExternalReferenceCode);
				setExtensionName(item.name);
			},
			selectEventName: selectThemeCSSClientExtensionEventName,
			title: Liferay.Language.get('select-theme-css-extension'),
			url: selectThemeCSSClientExtensionURL,
		});
	};

	return (
		<>
			<ClayInput
				name={`${portletNamespace}themeCSSCETExternalReferenceCode`}
				type="hidden"
				value={cetExternalReferenceCode}
			/>
			<ClayForm.Group>
				<label
					htmlFor={`${portletNamespace}themeCSSReplacementExtension`}
				>
					{Liferay.Language.get('extension')}
				</label>

				<ClayInput.Group className="w-50" small>
					<ClayInput.GroupItem>
						<ClayInput
							id={`${portletNamespace}themeCSSReplacementExtension`}
							onClick={onClick}
							placeholder={placeholder}
							readOnly
							type="text"
							value={extensionName}
						/>
					</ClayInput.GroupItem>

					<ClayInput.GroupItem shrink>
						{extensionName ? (
							<>
								<ClayButtonWithIcon
									aria-label={Liferay.Language.get('replace')}
									className="mr-2"
									displayType="secondary"
									onClick={onClick}
									small
									symbol="change"
								/>

								<ClayButtonWithIcon
									aria-label={Liferay.Language.get('delete')}
									displayType="secondary"
									onClick={() => {
										setExtensionName('');
										setCETExternalReferenceCode('');
									}}
									small
									symbol="trash"
								/>
							</>
						) : (
							<ClayButtonWithIcon
								aria-label={Liferay.Language.get('select')}
								displayType="secondary"
								onClick={onClick}
								small
								symbol="plus"
							/>
						)}
					</ClayInput.GroupItem>
				</ClayInput.Group>
			</ClayForm.Group>
		</>
	);
}
