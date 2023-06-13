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

import Leaflet from 'leaflet';
import MapGoogleMaps from 'map-google-maps/js/MapGoogleMaps.es';
import MapOpenStreetMap from 'map-openstreetmap/js/MapOpenStreetMap.es';
import {useEffect, useRef} from 'react';

export const MAP_PROVIDER = {
	googleMaps: 'GoogleMaps',
	openStreetMap: 'OpenStreetMap',
};

const {CONTROLS} = Liferay.MapBase;

const MAP_CONFIG = {
	controls: [
		CONTROLS.HOME,
		CONTROLS.PAN,
		CONTROLS.SEARCH,
		CONTROLS.TYPE,
		CONTROLS.ZOOM,
	],
	geolocation: true,
	position: {location: {lat: 0, lng: 0}},
};

const setupMapOpenStreetMaps = (callback) => {
	Leaflet.Icon.Default.imagePath =
		'https://npmcdn.com/leaflet@1.7.1/dist/images/';

	if (!window['L']) {
		window['L'] = Leaflet;
	}

	callback();
};

const setupGoogleMaps = (googleMapsAPIKey, callback) => {
	if (
		window.google &&
		window.google.maps &&
		Liferay.Maps &&
		Liferay.Maps.gmapsReady
	) {
		callback();
	}
	else {
		Liferay.namespace('Maps').onGMapsReady = function () {
			Liferay.Maps.gmapsReady = true;
			Liferay.fire('gmapsReady');
		};

		Liferay.once('gmapsReady', () => callback());

		let apiURL = `${location.protocol}//maps.googleapis.com/maps/api/js?v=3.exp&libraries=places&callback=Liferay.Maps.onGMapsReady`;

		if (googleMapsAPIKey) {
			apiURL += '&key=' + googleMapsAPIKey;
		}

		let script = document.createElement('script');

		script.setAttribute('src', apiURL);

		document.head.appendChild(script);

		script = null;
	}
};

export const useGeolocation = ({
	disabled,
	googleMapsAPIKey,
	instanceId,
	mapProviderKey,
	name,
	onChange,
	value,
	viewMode,
}) => {
	const componentInstance = useRef(null);

	const eventHandlerPositionChanged = (event) => {
		const {
			newVal: {location},
		} = event;

		const newValue = JSON.stringify(location);

		document
			.getElementById(`input_value_${instanceId}`)
			.setAttribute('value', newValue);

		onChange(newValue);
	};

	const registerMapBase = (MapProvide, mapConfig) => {
		componentInstance.current = new MapProvide(mapConfig);

		componentInstance.current.on(
			'positionChange',
			eventHandlerPositionChanged
		);

		Liferay.MapBase.register(
			name,
			componentInstance.current,
			`#map_${instanceId}`
		);
	};

	useEffect(() => {
		if (!disabled || viewMode) {
			const mapConfig = {...MAP_CONFIG};
			mapConfig.boundingBox = `#map_${instanceId}`;

			if (value && disabled) {
				mapConfig.position.location = JSON.parse(value);
			}

			switch (mapProviderKey) {
				case MAP_PROVIDER.openStreetMap:
					setupMapOpenStreetMaps(() =>
						registerMapBase(MapOpenStreetMap, mapConfig)
					);
					break;

				case MAP_PROVIDER.googleMaps:
					setupGoogleMaps(googleMapsAPIKey, () =>
						registerMapBase(MapGoogleMaps, mapConfig)
					);
					break;

				default:
					throw new Error('mapProvider is not supported!');
			}
		}

		return () => {
			if (componentInstance.current) {
				componentInstance.current.dispose();
			}
		};
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);
};
