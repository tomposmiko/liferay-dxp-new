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

import Icon from '@clayui/icon';
import classNames from 'classnames';
import React, {useContext, useEffect, useState} from 'react';

import DatasetDisplayContext from '../../DatasetDisplayContext';

function MainSearch() {
	const {searchParam, setPageNumber, updateSearchParam} = useContext(
		DatasetDisplayContext
	);

	const [inputValue, updateInputValue] = useState(searchParam);

	useEffect(() => {
		updateInputValue(searchParam || '');
	}, [searchParam]);

	function handleKeyDown(e) {
		if (e.keyCode === 13) {
			e.preventDefault();

			setPageNumber(1);
			updateSearchParam(inputValue);
		}
	}

	return (
		<div className="d-inline">
			<div className="input-group">
				<div className="input-group-item">
					<div className="main-input-wrapper">
						<input
							className="form-control input-group-inset input-group-inset-after main-input-search"
							onChange={(e) => updateInputValue(e.target.value)}
							onKeyDown={handleKeyDown}
							placeholder={Liferay.Language.get('search')}
							type="text"
							value={inputValue}
						/>

						<button
							className={classNames(
								'main-input-reset-button btn btn-unstyled',
								!inputValue.length && 'd-none'
							)}
							disabled={!inputValue.length}
							onClick={(e) => {
								e.preventDefault();
								setPageNumber(1);
								updateInputValue('');
								updateSearchParam('');
							}}
							type="button"
						>
							<Icon symbol="times-circle" />
						</button>
					</div>

					<span className="input-group-inset-item input-group-inset-item-after">
						<button
							className="btn btn-unstyled"
							onClick={(e) => {
								e.preventDefault();
								setPageNumber(1);
								updateSearchParam(inputValue);
							}}
							type="button"
						>
							<Icon symbol="search" />
						</button>
					</span>
				</div>
			</div>
		</div>
	);
}

export default MainSearch;
