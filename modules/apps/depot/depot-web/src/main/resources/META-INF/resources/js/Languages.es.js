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

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import {ClayRadio, ClayRadioGroup} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayLabel from '@clayui/label';
import ClayTable from '@clayui/table';
import PropTypes from 'prop-types';
import React, {useState} from 'react';

/**
 * @class Languages
 */
const Languages = ({
	availableLocales,
	defaultLocaleId,
	inheritLocales = false,
	portletNamespace,
	siteAvailableLocales,
	siteDefaultLocaleId,
	translatedLanguages
}) => {
	const [selectedRadioGroupValue, setSelectedRadioGroupValue] = useState(
		inheritLocales
	);

	const [customDefaultLocaleId, setCustomDefaultLocaleId] = useState(
		siteDefaultLocaleId
	);

	const [languageWarning, setLanguageWarning] = useState(false);
	const [
		languageTranslationWarning,
		setLanguageTranslationWarning
	] = useState(false);

	const form = document[`_${portletNamespace}_fm`];

	const Language = ({displayName, isDefault, localeId, showActions}) => {
		const [active, setActive] = useState(false);

		const makeDefault = event => {
			setActive(false);
			setCustomDefaultLocaleId(localeId);
			setLanguageWarning(true);
			setLanguageTranslationWarning(
				translatedLanguages && !translatedLanguages[localeId]
			);

			Liferay.fire('inputLocalized:defaultLocaleChanged', {
				item: event.currentTarget
			});

			Liferay.Util.setFormValues(form, {
				languageId: localeId
			});
		};

		return (
			<ClayTable.Row>
				<ClayTable.Cell expanded>
					{displayName}
					<span className="hide"> {localeId} </span>
					{isDefault && (
						<ClayLabel className="ml-3" displayType="info">
							{Liferay.Language.get('default')}
						</ClayLabel>
					)}
				</ClayTable.Cell>
				{showActions && (
					<ClayTable.Cell align="center">
						<ClayDropDown
							active={active}
							onActiveChange={setActive}
							trigger={
								<ClayButton
									displayType="unstyled"
									monospaced
									small
								>
									<ClayIcon symbol="ellipsis-v" />
								</ClayButton>
							}
						>
							<ClayDropDown.ItemList>
								<ClayDropDown.Item
									data-value={localeId}
									key={localeId}
									onClick={event => makeDefault(event)}
								>
									{Liferay.Language.get('make-default')}
								</ClayDropDown.Item>
							</ClayDropDown.ItemList>
						</ClayDropDown>
					</ClayTable.Cell>
				)}
			</ClayTable.Row>
		);
	};

	const LanguagesList = ({defaultLocaleId, locales, showActions = false}) => {
		return (
			<ClayTable borderless headVerticalAlignment="middle">
				<ClayTable.Head>
					<ClayTable.Row>
						<ClayTable.Cell expanded headingCell headingTitle>
							{Liferay.Language.get('language')}
						</ClayTable.Cell>

						{showActions && (
							<ClayTable.Cell align="center">
								<ClayButton displayType="secondary" small>
									{Liferay.Language.get('add')}
								</ClayButton>
							</ClayTable.Cell>
						)}
					</ClayTable.Row>
				</ClayTable.Head>

				<ClayTable.Body>
					{locales.map(locale => {
						return (
							<Language
								{...locale}
								isDefault={defaultLocaleId === locale.localeId}
								key={locale.localeId}
								showActions={showActions}
							/>
						);
					})}
				</ClayTable.Body>
			</ClayTable>
		);
	};

	return (
		<div className="mt-5">
			<ClayRadioGroup
				name={`_${portletNamespace}_TypeSettingsProperties--inheritLocales--`}
				onSelectedValueChange={setSelectedRadioGroupValue}
				selectedValue={selectedRadioGroupValue}
			>
				<ClayRadio
					label={Liferay.Language.get(
						'use-the-default-language-options'
					)}
					value={true}
				/>

				<ClayRadio
					label={Liferay.Language.get(
						'define-a-custom-default-language-and-additional-available-languages-for-this-repository'
					)}
					value={false}
				/>
			</ClayRadioGroup>

			{selectedRadioGroupValue && (
				<LanguagesList
					defaultLocaleId={defaultLocaleId}
					locales={availableLocales}
				/>
			)}

			{!selectedRadioGroupValue && (
				<LanguagesList
					defaultLocaleId={customDefaultLocaleId}
					locales={siteAvailableLocales}
					showActions
				/>
			)}

			{languageWarning && (
				<ClayAlert
					displayType="warning"
					onClose={() => setLanguageWarning(false)}
					title={Liferay.Language.get('warning')}
				>
					{Liferay.Language.get(
						'this-change-will-only-affect-the-newly-created-localized-content'
					)}
				</ClayAlert>
			)}

			{languageTranslationWarning && (
				<ClayAlert
					displayType="warning"
					onClose={() => setLanguageTranslationWarning(false)}
					title={Liferay.Language.get('warning')}
				>
					{Liferay.Language.get(
						'repository-name-will-display-a-generic-text-until-a-translation-is-added'
					)}
				</ClayAlert>
			)}
		</div>
	);
};

Languages.propTypes = {
	availableLocales: PropTypes.arrayOf(
		PropTypes.shape({
			displayName: PropTypes.string,
			localeId: PropTypes.string
		})
	).isRequired,
	defaultLocaleId: PropTypes.string.isRequired,
	inheritLocales: PropTypes.bool,
	portletNamespace: PropTypes.string.isRequired,
	siteAvailableLocales: PropTypes.arrayOf(
		PropTypes.shape({
			displayName: PropTypes.string,
			localeId: PropTypes.string
		})
	).isRequired,
	siteDefaultLocaleId: PropTypes.string.isRequired,
	translatedLanguages: PropTypes.object
};

export default function(props) {
	return <Languages {...props} />;
}
