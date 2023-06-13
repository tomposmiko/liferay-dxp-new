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

/**
 * GeoJSONBase
 * Allows adding controls (called features) to the map, that produce
 * diverse actions. For example, a button for centering the map
 * view will act as a feature.
 * @abstract
 * @review
 */

import {EventEmitter} from 'frontend-js-web';

class GeoJSONBase extends EventEmitter {

	/**
	 * Receives an object with native features data and tries
	 * to parse it with the implemented method _getNativeFeatures.
	 * If the generated Array of native features is not empty, it fires
	 * a 'featuresAdded' event.
	 * @param {Object} nativeFeaturesData Data to be processed.
	 * @review
	 */
	addData(nativeFeaturesData) {
		const nativeFeatures = this._getNativeFeatures(nativeFeaturesData);

		if (nativeFeatures.length) {
			this.emit('featuresAdded', {
				features: nativeFeatures.map(this._wrapNativeFeature),
			});
		}
	}

	/**
	 * Callback executed when a native feature has been clicked.
	 * It receives the feature as parameter, and emits a 'featureClick'
	 * event with the wrapped feature as event data.
	 * @param {Object} nativeFeature Feature to be wrapped and sent
	 * @protected
	 * @review
	 */
	_handleFeatureClicked(nativeFeature) {
		this.emit('featureClick', {
			feature: this._wrapNativeFeature(nativeFeature),
		});
	}

	/**
	 * Parses an object and return an array of the
	 * parsed features. If no feature has been parsed it may return an
	 * empty array.
	 * @abstract
	 * @param {Object} nativeFeaturesData
	 * @protected
	 * @return {Object[]} List of native features to be added
	 * @review
	 */
	_getNativeFeatures(_nativeFeaturesData) {
		throw new Error('Must be implemented');
	}

	/**
	 * Wraps a native feature.
	 * @abstract
	 * @param {Object} nativeFeature
	 * @protected
	 * @return {Object} Wrapped native feature
	 * @review
	 */
	_wrapNativeFeature(_nativeFeature) {
		throw new Error('Must be implemented');
	}
}

window.Liferay = window.Liferay || {};

window.Liferay.MapGeojsonBase = GeoJSONBase;

export default GeoJSONBase;
export {GeoJSONBase};
