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

package com.liferay.partner.service;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

import com.sforce.async.BatchInfo;
import com.sforce.async.BatchStateEnum;
import com.sforce.async.BulkConnection;
import com.sforce.async.ConcurrencyMode;
import com.sforce.async.ContentType;
import com.sforce.async.JobInfo;
import com.sforce.async.JobStateEnum;
import com.sforce.async.OperationEnum;
import com.sforce.async.QueryResultList;

import java.io.ByteArrayInputStream;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONTokener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jair Medeiros
 */
@Service
public class SalesforceService {

	public JSONArray getObjectJSONArray(
			String[] objectFields, String objectType)
		throws Exception {

		JobInfo jobInfo = _bulkConnection.createJob(
			new JobInfo() {
				{
					setConcurrencyMode(ConcurrencyMode.Parallel);
					setContentType(ContentType.CSV);
					setObject(objectType);
					setOperation(OperationEnum.query);
				}
			});

		_bulkConnection.updateJob(
			new JobInfo() {
				{
					setId(jobInfo.getId());
					setState(JobStateEnum.Closed);
				}
			});

		BatchInfo batchInfo = _getBatchInfo(jobInfo, objectFields, objectType);

		String batchInfoId = batchInfo.getId();

		while (true) {
			batchInfo = _bulkConnection.getBatchInfo(
				jobInfo.getId(), batchInfoId);

			if (batchInfo.getState() == BatchStateEnum.Completed) {
				break;
			}

			if (batchInfo.getState() == BatchStateEnum.Failed) {
				throw new Exception("Batch ID " + batchInfoId + " failed");
			}

			Thread.sleep(1000);
		}

		QueryResultList queryResultList = _bulkConnection.getQueryResultList(
			jobInfo.getId(), batchInfoId);

		String[] queryResults = queryResultList.getResult();

		if ((queryResults != null) && (queryResults.length == 1)) {
			JSONTokener jsonTokener = new JSONTokener(
				_bulkConnection.getQueryResultStream(
					jobInfo.getId(), batchInfoId, queryResults[0]));

			return CDL.toJSONArray(jsonTokener);
		}

		return new JSONArray();
	}

	private BatchInfo _getBatchInfo(
			JobInfo jobInfo, String[] objectFields, String objectType)
		throws Exception {

		String query = StringBundler.concat(
			"SELECT ", StringUtil.merge(objectFields, ", "), " FROM ",
			objectType);

		try (ByteArrayInputStream byteArrayInputStream =
				new ByteArrayInputStream(query.getBytes())) {

			return _bulkConnection.createBatchFromStream(
				jobInfo, byteArrayInputStream);
		}
	}

	@Autowired
	private BulkConnection _bulkConnection;

}