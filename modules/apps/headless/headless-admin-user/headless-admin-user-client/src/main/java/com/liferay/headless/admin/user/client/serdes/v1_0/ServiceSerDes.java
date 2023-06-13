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

package com.liferay.headless.admin.user.client.serdes.v1_0;

import com.liferay.headless.admin.user.client.dto.v1_0.HoursAvailable;
import com.liferay.headless.admin.user.client.dto.v1_0.Service;
import com.liferay.headless.admin.user.client.json.BaseJSONParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class ServiceSerDes {

	public static Service toDTO(String json) {
		ServiceJSONParser serviceJSONParser = new ServiceJSONParser();

		return serviceJSONParser.parseToDTO(json);
	}

	public static Service[] toDTOs(String json) {
		ServiceJSONParser serviceJSONParser = new ServiceJSONParser();

		return serviceJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Service service) {
		if (service == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (service.getHoursAvailable() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"hoursAvailable\":");

			sb.append("[");

			for (int i = 0; i < service.getHoursAvailable().length; i++) {
				sb.append(
					HoursAvailableSerDes.toJSON(
						service.getHoursAvailable()[i]));

				if ((i + 1) < service.getHoursAvailable().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (service.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\":");

			sb.append(service.getId());
		}

		if (service.getServiceType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"serviceType\":");

			sb.append("\"");

			sb.append(service.getServiceType());

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, String> toMap(Service service) {
		if (service == null) {
			return null;
		}

		Map<String, String> map = new HashMap<>();

		if (service.getHoursAvailable() == null) {
			map.put("hoursAvailable", null);
		}
		else {
			map.put(
				"hoursAvailable", String.valueOf(service.getHoursAvailable()));
		}

		if (service.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(service.getId()));
		}

		if (service.getServiceType() == null) {
			map.put("serviceType", null);
		}
		else {
			map.put("serviceType", String.valueOf(service.getServiceType()));
		}

		return map;
	}

	private static class ServiceJSONParser extends BaseJSONParser<Service> {

		@Override
		protected Service createDTO() {
			return new Service();
		}

		@Override
		protected Service[] createDTOArray(int size) {
			return new Service[size];
		}

		@Override
		protected void setField(
			Service service, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "hoursAvailable")) {
				if (jsonParserFieldValue != null) {
					service.setHoursAvailable(
						Stream.of(
							toStrings((Object[])jsonParserFieldValue)
						).map(
							object -> HoursAvailableSerDes.toDTO((String)object)
						).toArray(
							size -> new HoursAvailable[size]
						));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					service.setId(Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "serviceType")) {
				if (jsonParserFieldValue != null) {
					service.setServiceType((String)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}