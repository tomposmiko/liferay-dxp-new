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
import {ClayToggle} from '@clayui/form';
import {useModal} from '@clayui/modal';
import ClayTable from '@clayui/table';
import React, {useState} from 'react';

import AssignModal from '../components/AssignModal';

export type TDataSource = {
	dataSourceId: string;
	siteIds: Array<number>;
};

export type TProperty = {
	channelId: string;
	dataSources: Array<TDataSource>;
	name: string;
};

interface IPropertiesTable {
	properties: Array<TProperty>;
}

const PropertiesTable: React.FC<IPropertiesTable> = ({properties}) => {
	const {observer, onOpenChange, open} = useModal();
	const [property, setProperty] = useState<TProperty>(properties[0]);

	return (
		<ClayTable className="mt-4">
			<ClayTable.Head>
				<ClayTable.Row>
					<ClayTable.Cell expanded headingCell>
						{Liferay.Language.get('available-properties')}
					</ClayTable.Cell>

					<ClayTable.Cell headingCell>
						{Liferay.Language.get('channel')}
					</ClayTable.Cell>

					<ClayTable.Cell headingCell>
						{Liferay.Language.get('sites')}
					</ClayTable.Cell>

					<ClayTable.Cell headingCell>
						{Liferay.Language.get('commerce')}
					</ClayTable.Cell>
				</ClayTable.Row>
			</ClayTable.Head>

			<ClayTable.Body>
				{properties.map((property, index) => {
					return (
						<>
							<ClayTable.Row key={index}>
								<ClayTable.Cell className="table-cell-expand">
									{property.name}
								</ClayTable.Cell>

								<ClayTable.Cell
									className="mr-2"
									columnTextAlignment="end"
								>
									{/* TODO: When backend for commerce is completed, display the channels linked*/}
									-
								</ClayTable.Cell>

								<ClayTable.Cell
									className="mr-2"
									columnTextAlignment="end"
								>
									{property.dataSources.length >= 1
										? property.dataSources[0]?.siteIds
												.length
										: property.dataSources.length}
								</ClayTable.Cell>

								<ClayTable.Cell
									className="mr-2"
									columnTextAlignment="end"
								>
									{/* TODO: When backend for commerce is completed,
								create function to connect to commerce */}

									<ClayToggle
										toggled

										// onToggle={() => setActiveCommerce(!active)}

									/>
								</ClayTable.Cell>

								<ClayTable.Cell columnTextAlignment="end">
									<ClayButton
										displayType="secondary"
										onClick={() => {
											onOpenChange(true);
											setProperty(property);
										}}
										type="button"
									>
										{Liferay.Language.get('assign')}
									</ClayButton>
								</ClayTable.Cell>
							</ClayTable.Row>
						</>
					);
				})}
			</ClayTable.Body>

			{open && (
				<AssignModal
					observer={observer}
					onCloseModal={() => onOpenChange(false)}
					property={property}
				/>
			)}
		</ClayTable>
	);
};

export default PropertiesTable;
