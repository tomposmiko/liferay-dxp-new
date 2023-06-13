/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.web.internal.controller.api;

import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.util.FaroThreadLocal;
import com.liferay.osb.faro.web.internal.context.GroupInfo;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;

import java.net.URI;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marcellus Tavares
 */
@Component(service = ReportController.class)
@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
public class ReportController extends BaseFaroController {

	@GET
	@Path("{any:(?!/export.*).*}")
	public Map<Object, Object> get(
			@Context GroupInfo groupInfo, @Context UriInfo uriInfo)
		throws Exception {

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByGroupId(
				groupInfo.getGroupId());

		return contactsEngineClient.get(
			faroProject, _createHeaders(uriInfo.getBaseUri()),
			"/api/" + uriInfo.getPath(), uriInfo.getQueryParameters(),
			Map.class);
	}

	@GET
	@Path("/export/{type}")
	public Object get(
			@QueryParam("fromDate") String fromDateString,
			@Context GroupInfo groupInfo,
			@QueryParam("toDate") String toDateString,
			@PathParam("type") String type)
		throws Exception {

		if (Validator.isBlank(fromDateString) ||
			Validator.isBlank(toDateString)) {

			return _reportControllerResponseFactory.create(
				"\"fromDate\" and \"toDate\" query parameters are mandatory " +
					"and must be ISO 8601 compliant " + _ISO_8601_FORMAT,
				Response.Status.BAD_REQUEST);
		}

		Date fromDate;
		Date toDate;

		try {
			fromDate = _toUTCDate(fromDateString);
			toDate = _toUTCDate(toDateString);
		}
		catch (Exception exception) {
			_log.error(exception);

			return _reportControllerResponseFactory.create(
				"Both dates in range must be ISO 8601 compliant " +
					_ISO_8601_FORMAT,
				Response.Status.BAD_REQUEST);
		}

		if (fromDate.after(toDate)) {
			return _reportControllerResponseFactory.create(
				"Wrong range date. \"fromDate\" cannot be after \"toDate\"",
				Response.Status.BAD_REQUEST);
		}

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByGroupId(
				groupInfo.getGroupId());

		String path = "/api/reports/export/" + type;

		Map<String, Object> responseMap;

		Map<String, List<String>> queryParameters =
			HashMapBuilder.<String, List<String>>put(
				"fromDate", Collections.singletonList(fromDateString)
			).put(
				"toDate", Collections.singletonList(toDateString)
			).build();

		try {
			responseMap = contactsEngineClient.get(
				faroProject, Collections.emptyMap(), path, queryParameters,
				Map.class);
		}
		catch (Exception exception) {
			_log.error(exception);

			return _reportControllerResponseFactory.create(
				"An internal problem happened when trying to reach our " +
					"services",
				Response.Status.INTERNAL_SERVER_ERROR);
		}

		String status = MapUtil.getString(responseMap, "status");

		if (!Objects.equals(status, "COMPLETED")) {
			return _reportControllerResponseFactory.create(
				responseMap, Response.Status.OK);
		}

		StreamingOutput streamingOutput = outputStream -> {
			try {
				FaroThreadLocal.setCacheEnabled(false);

				contactsEngineClient.getToOutputStream(
					faroProject,
					HashMapBuilder.put(
						"Accept", "application/octet-stream, */*"
					).build(),
					String.format("%s/file", path), queryParameters,
					outputStream);
			}
			catch (Exception exception) {
				_log.error(exception);
			}

			outputStream.flush();
		};

		return Response.ok(
			streamingOutput
		).build();
	}

	private Map<String, String> _createHeaders(URI baseURI) {
		return HashMapBuilder.put(
			"X-Forwarded-Host", baseURI.getHost()
		).put(
			"X-Forwarded-Port", String.valueOf(baseURI.getPort())
		).put(
			"X-Forwarded-Proto", baseURI.getScheme()
		).build();
	}

	private Date _toUTCDate(String dateString) {
		LocalDateTime localDateTime = LocalDateTime.parse(
			dateString, _dateTimeFormatter);

		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);

		return Date.from(zonedDateTime.toInstant());
	}

	private static final String _ISO_8601_FORMAT =
		"yyyy-MM-dd'T'HH:mm[:ss.SSS'Z']";

	private static final Log _log = LogFactoryUtil.getLog(
		ReportController.class);

	private static final DateTimeFormatter _dateTimeFormatter =
		DateTimeFormatter.ofPattern(_ISO_8601_FORMAT);
	private static final ReportControllerResponseFactory
		_reportControllerResponseFactory =
			new ReportControllerResponseFactory();

}